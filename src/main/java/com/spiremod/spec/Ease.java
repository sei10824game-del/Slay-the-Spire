package com.spiremod.spec;

/** Easing applied to the segment that ARRIVES at a keyframe. */
public enum Ease {
    LINEAR, SMOOTH, IN_QUAD, OUT_QUAD, IN_OUT_QUAD, IN_CUBIC, OUT_CUBIC, IN_BACK, OUT_BACK, OUT_ELASTIC, OUT_BOUNCE, STEP;

    public float apply(float t) {
        switch (this) {
            case LINEAR: return t;
            case SMOOTH: return t * t * (3 - 2 * t);
            case IN_QUAD: return t * t;
            case OUT_QUAD: return 1 - (1 - t) * (1 - t);
            case IN_OUT_QUAD: return t < .5f ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2;
            case IN_CUBIC: return t * t * t;
            case OUT_CUBIC: return 1 - (float) Math.pow(1 - t, 3);
            case IN_BACK: { float c1 = 1.70158f, c3 = c1 + 1; return c3 * t * t * t - c1 * t * t; }
            case OUT_BACK: { float c1 = 1.70158f, c3 = c1 + 1; return 1 + c3 * (float) Math.pow(t - 1, 3) + c1 * (float) Math.pow(t - 1, 2); }
            case OUT_ELASTIC: {
                if (t <= 0) return 0; if (t >= 1) return 1;
                return (float) (Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * (2 * Math.PI / 3)) + 1);
            }
            case OUT_BOUNCE: {
                float n1 = 7.5625f, d1 = 2.75f;
                if (t < 1 / d1) return n1 * t * t;
                if (t < 2 / d1) { t -= 1.5f / d1; return n1 * t * t + .75f; }
                if (t < 2.5 / d1) { t -= 2.25f / d1; return n1 * t * t + .9375f; }
                t -= 2.625f / d1; return n1 * t * t + .984375f;
            }
            case STEP: return t >= 1 ? 1 : 0;
            default: return t;
        }
    }
}
