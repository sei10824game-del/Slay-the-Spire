package com.spiremod.spec;

import java.util.ArrayList;
import java.util.List;

/**
 * One textured cuboid of a Part. Coordinates are DSL pixels relative to the part pivot:
 * x = entity LEFT (+), y = UP (+), z = FORWARD (+). (x0,y0,z0) is the min corner, sizes must be integers.
 * All painting methods return this for chaining.
 */
public final class Box {
    public final float x0, y0, z0;
    public final int dx, dy, dz;
    public float inflate;
    public int color = 0x808080;
    public int gradTop = -1, gradBot = -1;
    public float noise = 0.07f;
    public float bevel = 0f;       // 0..1 darkening of the outermost pixel ring
    public boolean shade = true;   // directional shading (top light, bottom dark)
    public int u, v;               // assigned by packer
    public final List<Op> ops = new ArrayList<>();

    public static final class Op {
        public final Face face; public final int x, y, w, h, color; public final boolean glow;
        Op(Face f, int x, int y, int w, int h, int c, boolean g) { face = f; this.x = x; this.y = y; this.w = w; this.h = h; color = c; glow = g; }
    }

    Box(float x0, float y0, float z0, int dx, int dy, int dz) {
        this.x0 = x0; this.y0 = y0; this.z0 = z0; this.dx = Math.max(1, dx); this.dy = Math.max(1, dy); this.dz = Math.max(1, dz);
    }

    /** base colour 0xRRGGBB */
    public Box color(int rgb) { color = rgb; return this; }
    /** vertical gradient on side faces (top -> bottom colours) */
    public Box grad(int top, int bottom) { gradTop = top; gradBot = bottom; return this; }
    public Box noise(float n) { noise = n; return this; }
    public Box bevel(float b) { bevel = b; return this; }
    public Box flat() { shade = false; return this; }
    public Box inflate(float g) { inflate = g; return this; }
    /** paint a rect on one face; (x,y) = face-local pixel from the top-left as seen from outside */
    public Box paint(Face f, int x, int y, int w, int h, int rgb) { ops.add(new Op(f, x, y, w, h, rgb, false)); return this; }
    public Box px(Face f, int x, int y, int rgb) { return paint(f, x, y, 1, 1, rgb); }
    /** like paint but the pixels are also emissive (fullbright glow, e.g. eyes, flames) */
    public Box glow(Face f, int x, int y, int w, int h, int rgb) { ops.add(new Op(f, x, y, w, h, rgb, true)); return this; }
    /** paint a rect on all 4 side faces at the same local coords (belts, stripes) */
    public Box band(int y, int h, int rgb) {
        for (Face f : new Face[]{Face.FRONT, Face.BACK, Face.LEFT, Face.RIGHT}) paint(f, 0, y, f.w(dx, dy, dz), h, rgb);
        return this;
    }
    /** paint a rect on every face */
    public Box all(int rgb) { for (Face f : Face.values()) paint(f, 0, 0, f.w(dx, dy, dz), f.h(dx, dy, dz), rgb); return this; }

    /** DSL position on face at fractional (c,r): c across (viewer's right), r down (TOP/BOTTOM: r toward front). */
    public float[] corner(Face f, float c, float r) {
        float x1 = x0 + dx, y1 = y0 + dy, z1 = z0 + dz;
        switch (f) {
            case FRONT: return new float[]{x0 + c * dx, y1 - r * dy, z1};
            case BACK: return new float[]{x1 - c * dx, y1 - r * dy, z0};
            case RIGHT: return new float[]{x0, y1 - r * dy, z0 + c * dz};
            case LEFT: return new float[]{x1, y1 - r * dy, z1 - c * dz};
            case TOP: return new float[]{x0 + c * dx, y1, z0 + r * dz};
            default: return new float[]{x0 + c * dx, y0, z0 + r * dz};
        }
    }
    public static float[] normal(Face f) {
        switch (f) {
            case FRONT: return new float[]{0, 0, 1};
            case BACK: return new float[]{0, 0, -1};
            case RIGHT: return new float[]{-1, 0, 0};
            case LEFT: return new float[]{1, 0, 0};
            case TOP: return new float[]{0, 1, 0};
            default: return new float[]{0, -1, 0};
        }
    }
    public int texW() { return 2 * (dx + dz); }
    public int texH() { return dy + dz; }
}
