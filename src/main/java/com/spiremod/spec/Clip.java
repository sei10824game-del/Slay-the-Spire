package com.spiremod.spec;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Keyframe animation. Times in ticks (20/s). Values are OFFSETS from the part's rest pose
 * (rot in degrees, pos in pixels, scl is a multiplier, 1 = unchanged). A channel that is never keyed is not touched by this clip.
 * When used as an attack/move clip, keyed channels OVERRIDE idle/walk motion (blended in over 3 ticks).
 */
public final class Clip {
    public final String name;
    public final int length;
    public boolean loop;
    public final Map<String, Track[]> tracks = new LinkedHashMap<>(); // part -> [rot,pos,scl]

    public static final class Key { public final float t; public final float a, b, c; public final Ease ease;
        Key(float t, float a, float b, float c, Ease e) { this.t = t; this.a = a; this.b = b; this.c = c; ease = e; } }

    public static final class Track {
        public final List<Key> keys = new ArrayList<>();
        public final float[] sample(float t, boolean loop, int len, float[] out) {
            List<Key> k = keys;
            if (k.size() == 1 || t <= k.get(0).t && !loop) { Key f = k.get(0); out[0] = f.a; out[1] = f.b; out[2] = f.c; return out; }
            if (loop && len > 0) {
                t = t % len; if (t < 0) t += len;
                Key last = k.get(k.size() - 1), first = k.get(0);
                if (t >= last.t) { // wrap segment last -> first (+len)
                    float span = first.t + len - last.t;
                    float p = span <= 0 ? 1 : (t - last.t) / span;
                    float e = first.ease.apply(p);
                    out[0] = last.a + (first.a - last.a) * e; out[1] = last.b + (first.b - last.b) * e; out[2] = last.c + (first.c - last.c) * e;
                    return out;
                }
                if (t <= first.t) { out[0] = first.a; out[1] = first.b; out[2] = first.c; return out; }
            }
            Key last = k.get(k.size() - 1);
            if (t >= last.t) { out[0] = last.a; out[1] = last.b; out[2] = last.c; return out; }
            for (int i = 1; i < k.size(); i++) {
                Key n = k.get(i);
                if (t <= n.t) {
                    Key p = k.get(i - 1);
                    float span = n.t - p.t; float f = span <= 0 ? 1 : (t - p.t) / span; float e = n.ease.apply(f);
                    out[0] = p.a + (n.a - p.a) * e; out[1] = p.b + (n.b - p.b) * e; out[2] = p.c + (n.c - p.c) * e;
                    return out;
                }
            }
            out[0] = last.a; out[1] = last.b; out[2] = last.c; return out;
        }
    }

    Clip(String name, int length) { this.name = name; this.length = length; }

    public Clip loop() { loop = true; return this; }

    private Clip key(String part, int type, float t, float a, float b, float c, Ease e) {
        Track[] tr = tracks.computeIfAbsent(part, k -> new Track[3]);
        if (tr[type] == null) tr[type] = new Track();
        List<Key> ks = tr[type].keys; Key nk = new Key(t, a, b, c, e);
        int i = ks.size(); while (i > 0 && ks.get(i - 1).t > t) i--;
        ks.add(i, nk);
        return this;
    }
    /** rotation offset keyframe (degrees) */
    public Clip rot(String part, float t, float rx, float ry, float rz) { return key(part, 0, t, rx, ry, rz, Ease.SMOOTH); }
    public Clip rot(String part, float t, float rx, float ry, float rz, Ease e) { return key(part, 0, t, rx, ry, rz, e); }
    /** position offset keyframe (pixels, DSL axes: x left, y up, z forward) */
    public Clip pos(String part, float t, float x, float y, float z) { return key(part, 1, t, x, y, z, Ease.SMOOTH); }
    public Clip pos(String part, float t, float x, float y, float z, Ease e) { return key(part, 1, t, x, y, z, e); }
    /** scale multiplier keyframe */
    public Clip scl(String part, float t, float x, float y, float z) { return key(part, 2, t, x, y, z, Ease.SMOOTH); }
    public Clip scl(String part, float t, float x, float y, float z, Ease e) { return key(part, 2, t, x, y, z, e); }
    public Clip scl(String part, float t, float s) { return key(part, 2, t, s, s, s, Ease.SMOOTH); }
}
