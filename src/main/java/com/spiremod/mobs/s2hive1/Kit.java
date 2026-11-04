package com.spiremod.mobs.s2hive1;

import com.spiremod.spec.*;

/** Shared bits for this group's arthropods: jointed legs + tripod scuttle gait. */
final class Kit {
    private Kit() {}

    /** side s: -1 = right (x-), +1 = left (x+). Leg names: prefix+"R0"/"L0".. (upper) and +"k" (lower). */
    static void legs(Part body, String prefix, float halfW, float y, float[] zs, float len1, float len2, float tilt, float splay, int col, int tip) {
        for (int i = 0; i < zs.length; i++) for (int s = -1; s <= 1; s += 2) {
            String n = prefix + (s < 0 ? "R" : "L") + i;
            float fan = (zs.length == 1 ? 0 : (i - (zs.length - 1) / 2f) * -18f) * s; // front legs point forward, back legs backward
            Part up = body.add(n).at(s * halfW, y, zs[i]).rot(0, fan, s * tilt);
            up.box(s > 0 ? 0 : -len1, -0.5f, -0.5f, (int) len1, 1, 1).color(col).bevel(0.2f);
            Part lo = up.add(n + "k").at(s * len1, 0, 0).rot(0, 0, s * (splay - tilt));
            lo.box(-0.5f, -len2, -0.5f, 1, (int) len2, 1).color(col).grad(col, tip);
        }
    }

    /** alternating tripod gait on legs made by {@link #legs}; also a faint idle twitch */
    static void scuttle(ModelSpec m, String prefix, int pairs, float amp) {
        m.proc((r, c) -> {
            for (int i = 0; i < pairs; i++) for (int s = -1; s <= 1; s += 2) {
                String n = prefix + (s < 0 ? "R" : "L") + i;
                if (!r.has(n)) continue;
                double ph = ((i + (s < 0 ? 1 : 0)) % 2 == 0) ? 0 : Math.PI;
                double a = c.swing * 1.1f + ph;
                float yaw = (float) Math.cos(a) * amp * c.swingAmt;
                float lift = (float) Math.max(0, Math.sin(a)) * amp * 0.7f * c.swingAmt;
                float tw = (float) Math.sin(c.age * 0.15f + i * 1.7f + s) * 2f;
                r.rot(n, 0, yaw, s * (lift + tw));
            }
        });
    }

    /** death helper: all legs curl in (key both upper and lower) */
    static Clip curlLegs(Clip c, String prefix, int pairs, int t0, int t1) {
        for (int i = 0; i < pairs; i++) for (int s = -1; s <= 1; s += 2) {
            String n = prefix + (s < 0 ? "R" : "L") + i;
            c.rot(n, t0, 0, 0, 0).rot(n, t1, 0, 0, s * 40f).rot(n + "k", t0, 0, 0, 0).rot(n + "k", t1, 0, 0, -s * 70f);
        }
        return c;
    }
}
