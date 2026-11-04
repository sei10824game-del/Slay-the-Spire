package com.spiremod.spec;

import java.util.List;

/** Packs box UVs and paints the procedural texture (+ emissive mask) for a ModelSpec. Pure Java. */
public final class TexBuilder {
    public int w, h;
    public int[] argb;   // base texture
    public int[] glow;   // emissive texture (0 = none)

    public static TexBuilder build(ModelSpec spec) {
        List<Box> boxes = spec.allBoxes();
        int size = Math.max(32, spec.texSize);
        TexBuilder t = new TexBuilder();
        for (; size <= 2048; size *= 2) { if (pack(boxes, size)) break; }
        t.w = t.h = size;
        t.argb = new int[size * size]; t.glow = new int[size * size];
        int bi = 0;
        for (Box b : boxes) { t.paintBox(b, bi++); }
        return t;
    }

    private static boolean pack(List<Box> boxes, int size) {
        // shelf packing, tallest first is not needed: keep order stable; simple rows
        int x = 0, y = 0, rowH = 0;
        for (Box b : boxes) {
            int bw = b.texW(), bh = b.texH();
            if (bw > size) return false;
            if (x + bw > size) { x = 0; y += rowH; rowH = 0; }
            if (y + bh > size) return false;
            b.u = x; b.v = y; x += bw; rowH = Math.max(rowH, bh);
        }
        return true;
    }

    private static int hash(int a, int b, int c, int d) {
        int h = (int) (a * 73856093L ^ b * 19349663L ^ c * 83492791L ^ d * 2654435761L);
        h ^= h >>> 15; h *= 0x2c1b3c6d; h ^= h >>> 12; h *= 0x297a2d39; h ^= h >>> 15;
        return h;
    }

    private static int mul(int rgb, float f) {
        int r = Math.min(255, Math.max(0, Math.round(((rgb >> 16) & 255) * f)));
        int g = Math.min(255, Math.max(0, Math.round(((rgb >> 8) & 255) * f)));
        int b = Math.min(255, Math.max(0, Math.round((rgb & 255) * f)));
        return (r << 16) | (g << 8) | b;
    }
    private static int lerp(int a, int b, float t) {
        int r = Math.round(((a >> 16) & 255) * (1 - t) + ((b >> 16) & 255) * t);
        int g = Math.round(((a >> 8) & 255) * (1 - t) + ((b >> 8) & 255) * t);
        int bl = Math.round((a & 255) * (1 - t) + (b & 255) * t);
        return (r << 16) | (g << 8) | bl;
    }

    private static float faceLight(Face f) {
        switch (f) { case TOP: return 1.12f; case BOTTOM: return 0.72f; case FRONT: return 1.0f; case BACK: return 0.88f; default: return 0.93f; }
    }

    private void paintBox(Box b, int bi) {
        for (Face f : Face.values()) {
            int fw = f.w(b.dx, b.dy, b.dz), fh = f.h(b.dx, b.dy, b.dz);
            int ou = b.u + f.u(b.dx, b.dy, b.dz), ov = b.v + f.v(b.dx, b.dy, b.dz);
            for (int j = 0; j < fh; j++) for (int i = 0; i < fw; i++) {
                int c = b.color;
                if (b.gradTop >= 0 && f != Face.TOP && f != Face.BOTTOM) c = lerp(b.gradTop, b.gradBot, fh <= 1 ? 0 : (float) j / (fh - 1));
                float lit = b.shade ? faceLight(f) : 1f;
                float n = 1f + ((hash(bi, f.ordinal(), i, j) & 1023) / 1023f - 0.5f) * 2f * b.noise;
                if (b.bevel > 0 && (i == 0 || j == 0 || i == fw - 1 || j == fh - 1)) lit *= 1f - b.bevel;
                put(ou + i, ov + j, 0xFF000000 | mul(c, lit * n), 0);
            }
        }
        for (Box.Op op : b.ops) {
            int fw = op.face.w(b.dx, b.dy, b.dz), fh = op.face.h(b.dx, b.dy, b.dz);
            int ou = b.u + op.face.u(b.dx, b.dy, b.dz), ov = b.v + op.face.v(b.dx, b.dy, b.dz);
            for (int j = 0; j < op.h; j++) for (int i = 0; i < op.w; i++) {
                int px = op.x + i, py = op.y + j;
                if (px < 0 || py < 0 || px >= fw || py >= fh) continue;
                int col = 0xFF000000 | op.color;
                put(ou + px, ov + py, col, op.glow ? col : 0);
                if (!op.glow) glow[(ov + py) * w + ou + px] = 0;
            }
        }
    }

    private void put(int x, int y, int c, int g) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        argb[y * w + x] = c; if (g != 0) glow[y * w + x] = g;
    }
}
