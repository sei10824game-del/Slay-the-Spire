package com.spiremod.mobs.s1act1b;

import com.spiremod.spec.*;

/** Compact keyframe helpers: groups of (t,a,b,c). t>=100 = snap (IN_CUBIC, t-100); t>=200 = overshoot (OUT_BACK, t-200). */
final class K {
    private K() {}
    private static Ease e(float t) { return t >= 200 ? Ease.OUT_BACK : t >= 100 ? Ease.IN_CUBIC : Ease.SMOOTH; }
    private static float tt(float t) { return t >= 200 ? t - 200 : t >= 100 ? t - 100 : t; }
    static Clip r(Clip c, String p, float... v) { for (int i = 0; i < v.length; i += 4) c.rot(p, (int) tt(v[i]), v[i + 1], v[i + 2], v[i + 3], e(v[i])); return c; }
    static Clip p(Clip c, String p, float... v) { for (int i = 0; i < v.length; i += 4) c.pos(p, (int) tt(v[i]), v[i + 1], v[i + 2], v[i + 3], e(v[i])); return c; }
    static Clip s(Clip c, String p, float... v) { for (int i = 0; i < v.length; i += 4) c.scl(p, (int) tt(v[i]), v[i + 1], v[i + 2], v[i + 3]); return c; }
}
