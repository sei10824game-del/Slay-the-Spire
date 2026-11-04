package com.spiremod.tools;

import com.spiremod.core.*;
import com.spiremod.spec.*;

import java.util.*;

/**
 * Static sanity check of creature classes (no game needed):
 *   java com.spiremod.tools.Validate com.spiremod.mobs.x.Foo [more classes...]
 * Each class needs `public static ModelSpec model()` and `public static MobDef def()` (or a Group.collect for --group).
 */
public final class Validate {
    public static void main(String[] a) throws Exception {
        net.minecraft.SharedConstants.tryDetectVersion();
        net.minecraft.server.Bootstrap.bootStrap();
        int bad = 0;
        List<MobDef> defs = new ArrayList<>();
        if (a.length > 0 && a[0].equals("--group")) {
            Class.forName(a[1]).getMethod("collect", java.util.function.Consumer.class).invoke(null, (java.util.function.Consumer<MobDef>) defs::add);
        } else if (a.length > 0 && a[0].equals("--all")) {
            Catalog.init(); defs.addAll(Catalog.ALL);
        } else for (String c : a) defs.add((MobDef) Class.forName(c).getMethod("def").invoke(null));
        Set<String> ids = new HashSet<>();
        for (MobDef d : defs) {
            List<String> err = new ArrayList<>(); List<String> warn = new ArrayList<>();
            if (!d.id.matches("[a-z0-9_]+")) err.add("bad id");
            if (!ids.add(d.id)) err.add("duplicate id");
            ModelSpec m = null;
            try { m = d.model.get(); } catch (Throwable t) { err.add("model() threw " + t); }
            if (m != null) {
                for (Clip c : m.clips.values()) for (String p : c.tracks.keySet()) if (!m.byName.containsKey(p)) err.add("clip '" + c.name + "' keys unknown part '" + p + "'");
                if (!m.clips.containsKey("idle")) warn.add("no idle clip");
                if (!m.clips.containsKey("death")) warn.add("no death clip");
                if (!m.clips.containsKey("hurt")) warn.add("no hurt clip");
                if (m.lookPart != null && !m.byName.containsKey(m.lookPart)) err.add("look part missing");
                if (m.parts.isEmpty()) err.add("model has no parts");
                try { TexBuilder t = TexBuilder.build(m); if (t.w > 512) warn.add("texture " + t.w + "px is large"); } catch (Throwable t) { err.add("texture build failed: " + t); }
                try { Posing p = new Posing(m); Ctx c = new Ctx(); p.eval(c); } catch (Throwable t) { err.add("posing failed " + t); }
                for (Move mv : d.moves.values()) {
                    if (!m.clips.containsKey(mv.anim)) err.add("move '" + mv.id + "' anim clip '" + mv.anim + "' missing in model");
                    else if (m.clips.get(mv.anim).length < mv.length - 2) warn.add("move '" + mv.id + "' is " + mv.length + " ticks but clip is " + m.clips.get(mv.anim).length);
                    for (Move.Timed t : mv.actions) if (t.tick >= mv.length) err.add("move '" + mv.id + "' action at tick " + t.tick + " >= length " + mv.length);
                    if (mv.intent == Intent.ATTACK && mv.dmg == 0) warn.add("move '" + mv.id + "' ATTACK intent without dmg");
                }
                if (!d.initialBase.equals("idle") && !m.clips.containsKey(d.initialBase)) err.add("base clip '" + d.initialBase + "' missing");
            }
            if (d.moves.isEmpty()) err.add("no moves");
            if (d.pattern == null) err.add("no pattern");
            else try { // simulate pattern ids: only checks it returns known ids using a fake-less approach is impossible; checked in game
            } catch (Throwable ignored) {}
            if (d.hp <= 0) err.add("hp");
            System.out.println((err.isEmpty() ? "OK   " : "FAIL ") + d.id + (m != null ? " parts=" + m.parts.size() + " clips=" + m.clips.keySet() : ""));
            for (String s : err) { System.out.println("   ERROR " + s); bad++; }
            for (String s : warn) System.out.println("   warn  " + s);
        }
        System.out.println(bad == 0 ? "PASS: all " + defs.size() + " ok" : "FAILED: " + bad + " errors");
        if (bad > 0) System.exit(1);
    }
}
