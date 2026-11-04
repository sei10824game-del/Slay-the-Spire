package com.spiremod.spec;

import java.util.HashMap;
import java.util.Map;

/** Evaluates clips + procedural hook for one frame into offset arrays. Shared by game renderer and Preview. */
public final class Posing {
    public final ModelSpec spec;
    public final int n;
    public final float[] ox, oy, oz, orx, ory, orz, osx, osy, osz;
    public final boolean[] vis;
    private final Map<String, Integer> idx = new HashMap<>();
    private final float[] tmp = new float[3];

    public Posing(ModelSpec spec) {
        this.spec = spec; n = spec.parts.size();
        ox = new float[n]; oy = new float[n]; oz = new float[n]; orx = new float[n]; ory = new float[n]; orz = new float[n];
        osx = new float[n]; osy = new float[n]; osz = new float[n]; vis = new boolean[n];
        for (Part p : spec.parts) idx.put(p.name, p.index);
    }

    private void reset() {
        for (int i = 0; i < n; i++) { ox[i] = oy[i] = oz[i] = orx[i] = ory[i] = orz[i] = 0; osx[i] = osy[i] = osz[i] = 1; vis[i] = true; }
    }

    /** apply clip at time t. override: blend toward clip value (weight w); additive: add w*value */
    private void apply(Clip c, float t, float w, boolean additive) {
        if (c == null || w <= 0) return;
        for (Map.Entry<String, Clip.Track[]> e : c.tracks.entrySet()) {
            Integer i = idx.get(e.getKey()); if (i == null) continue;
            Clip.Track[] tr = e.getValue();
            if (tr[0] != null) { float[] v = tr[0].sample(t, c.loop, c.length, tmp);
                if (additive) { orx[i] += v[0] * w; ory[i] += v[1] * w; orz[i] += v[2] * w; }
                else { orx[i] += (v[0] - orx[i]) * w; ory[i] += (v[1] - ory[i]) * w; orz[i] += (v[2] - orz[i]) * w; } }
            if (tr[1] != null) { float[] v = tr[1].sample(t, c.loop, c.length, tmp);
                if (additive) { ox[i] += v[0] * w; oy[i] += v[1] * w; oz[i] += v[2] * w; }
                else { ox[i] += (v[0] - ox[i]) * w; oy[i] += (v[1] - oy[i]) * w; oz[i] += (v[2] - oz[i]) * w; } }
            if (tr[2] != null) { float[] v = tr[2].sample(t, c.loop, c.length, tmp);
                if (additive) { osx[i] *= 1 + (v[0] - 1) * w; osy[i] *= 1 + (v[1] - 1) * w; osz[i] *= 1 + (v[2] - 1) * w; }
                else { osx[i] += (v[0] - osx[i]) * w; osy[i] += (v[1] - osy[i]) * w; osz[i] += (v[2] - osz[i]) * w; } }
        }
    }

    private static float sm(float t) { t = Math.max(0, Math.min(1, t)); return t * t * (3 - 2 * t); }

    public void eval(Ctx c) {
        reset();
        Clip base = spec.clips.get(c.base != null ? c.base : "idle");
        if (base == null) base = spec.clips.get("idle");
        if (base != null) apply(base, c.age, 1f, false);
        Clip walk = spec.clips.get("walk");
        if (walk != null && c.swingAmt > 0.01f) {
            float ph = (c.swing / 9.43f) % 1f; if (ph < 0) ph += 1f;
            apply(walk, ph * walk.length, Math.min(1f, c.swingAmt * 2f), false);
        }
        if (spec.lookPart != null && idx.containsKey(spec.lookPart)) {
            int i = idx.get(spec.lookPart);
            ory[i] += -Math.max(-spec.lookMaxYaw, Math.min(spec.lookMaxYaw, c.headYaw)); // MC yaw positive = turn right; DSL ry+ = left
            orx[i] += Math.max(-spec.lookMaxPitch, Math.min(spec.lookMaxPitch, c.headPitch));
        }
        if (c.spawnT >= 0) { Clip s = spec.clips.get("spawn"); if (s != null && c.spawnT <= s.length) apply(s, c.spawnT, 1f, false); }
        if (c.fade != null && c.fadeT >= 0 && c.fadeT < 6) {
            Clip f = spec.clips.get(c.fade);
            if (f != null) apply(f, f.length, 1f - sm(c.fadeT / 6f), false);
        }
        if (c.move != null) {
            Clip m = spec.clips.get(c.move);
            if (m != null) apply(m, Math.min(c.moveT, m.length), sm(c.moveT / 3f), false);
        }
        if (c.hurtT >= 0) {
            Clip h = spec.clips.get("hurt");
            if (h != null && c.hurtT <= h.length) apply(h, c.hurtT, 1f, true);
        }
        if (spec.proc != null) spec.proc.accept(rig, c);
        if (c.deathT >= 0) {
            Clip d = spec.clips.get("death");
            if (d != null) apply(d, Math.min(c.deathT, d.length), 1f, false);
        }
    }

    private final Rig rig = new Rig() {
        public void rot(String p, float x, float y, float z) { Integer i = idx.get(p); if (i != null) { orx[i] += x; ory[i] += y; orz[i] += z; } }
        public void pos(String p, float x, float y, float z) { Integer i = idx.get(p); if (i != null) { ox[i] += x; oy[i] += y; oz[i] += z; } }
        public void scl(String p, float x, float y, float z) { Integer i = idx.get(p); if (i != null) { osx[i] *= x; osy[i] *= y; osz[i] *= z; } }
        public void show(String p, boolean v) { Integer i = idx.get(p); if (i != null) vis[i] = v; }
        public boolean has(String p) { return idx.containsKey(p); }
    };
}
