package com.spiremod.mobs.s2glory1;

import com.spiremod.spec.*;

/** Shared floating-drone body for the four Fabricator bots (parts: body, eye, podR, podL, ant). */
final class Bots {
    private Bots() {}

    static ModelSpec drone(String id, int main, int dark, int glow) {
        ModelSpec m = new ModelSpec(id);
        Part body = m.add("body").at(0, 12, 0);
        body.box(-4.5f, -4, -4, 9, 8, 8).color(main).grad(Kit_lighten(main), dark).bevel(0.25f).noise(0.1f);
        body.box(-5, -0.5f, -4.5f, 10, 1, 9).color(dark).flat();                                    // seam
        body.box(-3, -2, 3.9f, 6, 4, 1).color(0x101418).glow(Face.FRONT, 1, 1, 4, 2, glow);         // visor / eye
        Part ant = body.add("ant").at(0, 4, -1);
        ant.box(-0.5f, 0, -0.5f, 1, 4, 1).color(dark); ant.box(-1, 4, -1, 2, 2, 2).color(glow).flat().glow(Face.FRONT, 0, 0, 2, 2, glow);
        for (int s = -1; s <= 1; s += 2) {
            Part pod = body.add(s < 0 ? "podR" : "podL").at(s * 5.5f, -2, 0);
            pod.box(-1.5f, -3, -2.5f, 3, 6, 5).color(dark).bevel(0.25f);
            pod.box(-1, -4, -1.5f, 2, 1, 3).color(glow).flat().glow(Face.BOTTOM, 0, 0, 2, 3, glow);
        }
        m.look("body", 40, 20);
        m.bob(1.5f, 0.2f);
        m.clip("idle", 40).loop().rot("body", 0, 2, 0, -3).rot("body", 20, -2, 0, 3).rot("body", 40, 2, 0, -3)
            .rot("podR", 0, 0, 0, 0).rot("podR", 20, 8, 0, 0).rot("podR", 40, 0, 0, 0).rot("podL", 0, 8, 0, 0).rot("podL", 20, 0, 0, 0).rot("podL", 40, 8, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -20, 0, 15).rot("body", 10, 0, 0, 0);
        m.clip("death", 26).rot("body", 0, 0, 0, 0).rot("body", 12, 25, 0, 40).rot("body", 26, 80, 0, 90)
            .pos("body", 0, 0, 0, 0).pos("body", 26, 0, -10, 0, Ease.OUT_BOUNCE)
            .rot("podR", 0, 0, 0, 0).rot("podR", 20, 0, 0, 40).rot("podL", 0, 0, 0, 0).rot("podL", 20, 0, 0, -40);
        return m;
    }
    static int Kit_lighten(int c) { int r = Math.min(255, ((c >> 16) & 255) + 40), g = Math.min(255, ((c >> 8) & 255) + 40), b = Math.min(255, (c & 255) + 40); return r << 16 | g << 8 | b; }

    /** body lunges at `hit`, ant whips */
    static Clip strike(ModelSpec m, String name, int len, int hit) {
        Clip c = m.clip(name, len);
        Kit.sp(c, "body", len, hit, hit - 8, 0, 0.5f, -3, hit, 0, -0.5f, 6, hit + 6, 0, 0, 2);
        Kit.seq(c, "body", len, hit, hit - 8, -15, 0, 0, hit, 20, 0, 0);
        Kit.seq(c, "ant", len, -1, hit - 8, -20, 0, 0, hit, 25, 0, 0);
        return c;
    }
}
