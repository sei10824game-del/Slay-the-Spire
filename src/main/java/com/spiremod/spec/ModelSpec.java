package com.spiremod.spec;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Pure-Java description of a creature's look and motion (no Minecraft classes).
 * Build: parts (tree of boxes) + clips (keyframes) + optional procedural hook.
 * Standard clip names: "idle"(loop, base), "walk"(loop, driven by walk speed), "hurt", "death", "spawn"; move clips use the Move's anim name.
 * Extra base clips (selected with SpireMob.setBase): any name, looped, e.g. "sleep".
 */
public final class ModelSpec {
    public final String id;
    public float scale = 1f;                 // overall render scale
    public final Part root;                  // virtual root at ground centre
    public final List<Part> parts = new ArrayList<>();   // DFS order, excludes root
    public final Map<String, Part> byName = new LinkedHashMap<>();
    public final Map<String, Clip> clips = new LinkedHashMap<>();
    public BiConsumer<Rig, Ctx> proc;
    public String lookPart; public float lookMaxYaw = 60, lookMaxPitch = 35;
    public String shadowless;
    public int deathTicks = 20;              // length of death anim before the mob is removed
    /** texture side hint: 0 = auto */
    public int texSize;

    public ModelSpec(String id) { this.id = id; root = new Part(this, null, "root"); }

    Part newPart(Part parent, String name) {
        if (byName.containsKey(name)) throw new IllegalArgumentException("duplicate part '" + name + "' in " + id);
        Part p = new Part(this, parent, name);
        p.index = parts.size(); parts.add(p); byName.put(name, p); parent.kids.add(p);
        return p;
    }

    /** add a top-level part (pivot relative to ground centre; y up) */
    public Part add(String name) { return root.add(name); }

    public Clip clip(String name, int lengthTicks) { if (name.equals("death")) deathTicks = lengthTicks; Clip c = new Clip(name, lengthTicks); clips.put(name, c); return c; }

    /** procedural motion hook, runs last */
    public ModelSpec proc(BiConsumer<Rig, Ctx> p) {
        if (proc == null) proc = p; else { BiConsumer<Rig, Ctx> o = proc; proc = (r, c) -> { o.accept(r, c); p.accept(r, c); }; }
        return this;
    }
    /** head tracking: part rotates toward target */
    public ModelSpec look(String part, float maxYaw, float maxPitch) { lookPart = part; lookMaxYaw = maxYaw; lookMaxPitch = maxPitch; return this; }
    public ModelSpec scale(float s) { scale = s; return this; }
    public ModelSpec deathTicks(int t) { deathTicks = t; return this; }

    // ---------- helpers for common locomotion ----------
    /** two-legged swing (+ optional arms) while walking */
    public ModelSpec walkBiped(String legR, String legL, String armR, String armL, float legAmp, float armAmp) {
        return proc((r, c) -> {
            float s = (float) Math.cos(c.swing * 0.6662f) * c.swingAmt;
            if (r.has(legR)) r.rot(legR, s * legAmp, 0, 0);
            if (r.has(legL)) r.rot(legL, -s * legAmp, 0, 0);
            if (armR != null && r.has(armR)) r.rot(armR, -s * armAmp, 0, 0);
            if (armL != null && r.has(armL)) r.rot(armL, s * armAmp, 0, 0);
        });
    }
    /** four-legged diagonal gait */
    public ModelSpec walkQuad(String fr, String fl, String br, String bl, float amp) {
        return proc((r, c) -> {
            float s = (float) Math.cos(c.swing * 0.6662f) * c.swingAmt * amp;
            if (r.has(fr)) r.rot(fr, s, 0, 0); if (r.has(bl)) r.rot(bl, s, 0, 0);
            if (r.has(fl)) r.rot(fl, -s, 0, 0); if (r.has(br)) r.rot(br, -s, 0, 0);
        });
    }
    /** lift ALL top-level parts up/down (hovering, bobbing). amp in pixels, speed in rad/tick */
    public ModelSpec bob(float amp, float speed) {
        return proc((r, c) -> { float y = (float) Math.sin(c.age * speed) * amp; for (Part p : root.kids) r.pos(p.name, 0, y, 0); });
    }

    public List<Box> allBoxes() { List<Box> l = new ArrayList<>(); for (Part p : parts) l.addAll(p.boxes); return l; }
}
