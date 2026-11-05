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

    // ------------------------------------------------------------------ compact clip keys
    static final Ease[] E = {Ease.SMOOTH, Ease.IN_CUBIC, Ease.OUT_BACK, Ease.OUT_BOUNCE, Ease.OUT_QUAD, Ease.LINEAR};

    /** rot keys, groups of 5 floats: tick, rx, ry, rz, ease# (0 smooth,1 in_cubic,2 out_back,3 bounce,4 out_quad,5 linear). Rest keys at 0 / end are added. */
    static Clip R(Clip c, String p, float... k) { return keys(c, p, 0, 0f, k); }
    /** pos keys, same layout */
    static Clip P(Clip c, String p, float... k) { return keys(c, p, 1, 0f, k); }
    /** scale keys, same layout (rest = 1) */
    static Clip S(Clip c, String p, float... k) { return keys(c, p, 2, 1f, k); }

    private static Clip keys(Clip c, String p, int ch, float rest, float[] k) {
        if (k[0] > 0) put(c, ch, p, 0, rest, rest, rest, 0);
        for (int i = 0; i + 4 < k.length; i += 5) put(c, ch, p, k[i], k[i + 1], k[i + 2], k[i + 3], (int) k[i + 4]);
        if (k[k.length - 5] < c.length) put(c, ch, p, c.length, rest, rest, rest, 0);
        return c;
    }

    private static void put(Clip c, int ch, String p, float t, float a, float b, float d, int e) {
        if (ch == 0) c.rot(p, t, a, b, d, E[e]); else if (ch == 1) c.pos(p, t, a, b, d, E[e]); else c.scl(p, t, a, b, d, E[e]);
    }

    // ------------------------------------------------------------------ generic humanoid
    /**
     * Builds legR/legL, body(hips), torso, head, armR/armL(+handR/handL) with idle/walk/hurt/death. Dimensions in px.
     * Returns nothing: decorate via m.byName.get("torso") etc.
     */
    static void hum(ModelSpec m, float legLen, float legW, float hipX, float tw, float th, float td, float hw, float hh, float armLen, float armW,
                    int body, int bodyD, int limb, int headc) {
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * hipX, legLen, 0);
            leg.box(-legW / 2, -legLen, -legW / 2, (int) legW, (int) legLen, (int) legW).color(limb).grad(limb, bodyD).bevel(0.12f);
            leg.box(-legW / 2 - 0.5f, -legLen, -legW / 2 - 1, (int) legW + 1, 2, (int) legW + 2).color(bodyD).bevel(0.15f);
        }
        Part b = m.add("body").at(0, legLen, 0);
        b.box(-tw / 2, 0, -td / 2, (int) tw, 3, (int) td).color(bodyD).bevel(0.1f);
        Part t = b.add("torso").at(0, 3, 0);
        t.box(-tw / 2, 0, -td / 2, (int) tw, (int) th, (int) td).color(body).grad(body, bodyD).bevel(0.1f).noise(0.08f);
        Part h = t.add("head").at(0, th, 0);
        h.box(-hw / 2, 0, -hw / 2, (int) hw, (int) hh, (int) hw).color(headc).bevel(0.12f);
        for (int s = -1; s <= 1; s += 2) {
            Part a = t.add(s < 0 ? "armR" : "armL").at(s * (tw / 2 + armW / 2), th - 1.5f, 0);
            a.box(-armW / 2, -armLen, -armW / 2, (int) armW, (int) armLen, (int) armW).color(limb).bevel(0.12f).noise(0.08f);
            Part hd = a.add(s < 0 ? "handR" : "handL").at(0, -armLen, 0);
            hd.box(-armW / 2 - 0.3f, -armW, -armW / 2 - 0.3f, (int) armW + 1, (int) armW, (int) armW + 1).color(bodyD).bevel(0.15f);
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 32f, 22f);
        Clip idl = m.clip("idle", 48).loop();
        R(idl, "torso", 0, 1.5f, 0, 0, 0, 24, -1.5f, 0, 0, 0, 48, 1.5f, 0, 0, 0);
        R(idl, "armR", 0, 3, 0, 5, 0, 24, 6, 0, 7, 0, 48, 3, 0, 5, 0);
        R(idl, "armL", 0, 3, 0, -5, 0, 24, 6, 0, -7, 0, 48, 3, 0, -5, 0);
        R(idl, "head", 0, 0, 0, 0, 0, 14, 0, 6, 2, 0, 34, 0, -6, -2, 0, 48, 0, 0, 0, 0);
        hurt(m, "torso", "head");
        Clip d = m.clip("death", 32);
        R(d, "torso", 8, 20, 0, 0, 0, 32, 8, 0, 0, 0);
        R(d, "body", 8, -8, 0, 0, 0, 22, -80, 0, 0, 1, 32, -88, 0, 0, 3);
        P(d, "body", 12, 0, 0, -1, 0, 32, 0, -legLen + 2.5f, -3, 3);
        R(d, "armR", 10, -110, 0, 30, 0, 32, 10, 0, 60, 0);
        R(d, "armL", 10, -80, 0, -30, 0, 32, 15, 0, -60, 0);
        R(d, "head", 12, -25, 0, 0, 0, 32, 25, 12, 8, 0);
        R(d, "legR", 18, -30, 0, 8, 0, 32, -20, 0, 10, 0);
        R(d, "legL", 18, -45, 0, -8, 0, 32, -35, 0, -10, 0);
    }

    /** flinch: two parts rock back */
    static void hurt(ModelSpec m, String a, String b) {
        Clip c = m.clip("hurt", 10);
        R(c, a, 3, 16, 0, 6, 0, 10, 0, 0, 0, 0);
        R(c, b, 3, -22, 0, 8, 0, 10, 0, 0, 0, 0);
    }
}
