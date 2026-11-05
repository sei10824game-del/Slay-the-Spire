package com.spiremod.mobs.s2hive1;

import com.spiremod.spec.*;
import java.util.function.Consumer;

/** Compact insectoid model + generic attack/buff clips for the remaining Hive normals (see HiveB). */
final class Bug {
    private Bug() {}

    /** body w x h x l px, head hs px, `pairs` leg pairs; extras(body, head) adds creature-specific parts. */
    static ModelSpec model(String name, int w, int h, int l, int hs, int pairs, int col, int dark, int light, int accent, Consumer<Part[]> extras) {
        ModelSpec m = new ModelSpec(name);
        Part body = m.add("body").at(0, 4 + h / 2f, 0);
        body.box(-w / 2f, -h / 2f, -l / 2f, w, h, l).color(col).grad(light, dark).bevel(0.2f).noise(0.1f)
            .paint(Face.TOP, 0, l / 3, w, 1, dark).paint(Face.TOP, 0, 2 * l / 3, w, 1, dark);
        body.box(-w / 2f + 1, -h / 2f - 1, -l / 2f + 1, w - 2, 1, l - 2).color(accent);
        Part head = body.add("head").at(0, h / 4f, l / 2f);
        head.box(-hs / 2f, -hs / 2f, 0, hs, hs, hs - 1).color(dark).grad(col, dark).bevel(0.2f);
        head.box(-hs / 2f - 0.3f, 0, hs - 1.4f, 2, 2, 1).color(0xffe060).glow(Face.FRONT, 0, 0, 2, 2, 0xffe060);
        head.box(hs / 2f - 1.7f, 0, hs - 1.4f, 2, 2, 1).color(0xffe060).glow(Face.FRONT, 0, 0, 2, 2, 0xffe060);
        for (int s = -1; s <= 1; s += 2) {
            Part md = head.add(s < 0 ? "mandR" : "mandL").at(s * hs / 4f, -hs / 4f, hs - 1);
            md.box(-1, -1, 0, 2, 2, 4).color(accent).bevel(0.2f);
        }
        float[] zs = new float[pairs]; for (int i = 0; i < pairs; i++) zs[i] = l / 2f - 3 - i * (l - 6f) / Math.max(1, pairs - 1);
        Kit.legs(body, "leg", w / 2f, -h / 2f + 1, zs, 4, 5, 20, 18, dark, accent);
        Kit.scuttle(m, "leg", pairs, 18);
        if (extras != null) extras.accept(new Part[]{body, head});
        m.look("head", 25, 12);
        Clip i = m.clip("idle", 40).loop();
        Kit.R(i, "head", 0, 0, 0, 0, 0, 20, -5, 4, 0, 0, 40, 0, 0, 0, 0);
        Kit.P(i, "body", 0, 0, 0, 0, 0, 20, 0, 0.6f, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(i, "mandR", 0, 0, 0, 0, 0, 20, 0, -14, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(i, "mandL", 0, 0, 0, 0, 0, 20, 0, 14, 0, 0, 40, 0, 0, 0, 0);
        Kit.hurt(m, "body", "head");
        Clip d = m.clip("death", 30);
        Kit.R(d, "body", 6, 0, 0, 14, 0, 22, 0, 0, 160, 3, 30, 0, 0, 180, 0);
        Kit.P(d, "body", 10, 0, 2, 0, 0, 30, 0, -1.5f, 0, 0);
        Kit.curlLegs(d, "leg", pairs, 2, 22);
        return m;
    }

    /** wind-up (lean back, mandibles open) -> snap forward at `strike` -> recover. hits>1 repeats the snap every `gap` ticks. */
    static Clip attack(ModelSpec m, String name, int len, int wind, int strike, int hits, int gap) {
        Clip c = m.clip(name, len);
        Kit.R(c, "body", 0, 0, 0, 0, 0, wind, -16, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 0, wind, 0, 1, -3, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 0, wind, -30, 0, 0, 0);
        Kit.R(c, "mandR", 0, 0, 0, 0, 0, wind, 0, -40, 0, 0);
        Kit.R(c, "mandL", 0, 0, 0, 0, 0, wind, 0, 40, 0, 0);
        for (int h = 0; h < hits; h++) {
            int t = strike + h * gap;
            Kit.R(c, "body", t, 18, 0, 0, 1, t + 4, 6, 0, 0, 4);
            Kit.P(c, "body", t, 0, -1, 5, 1, t + 4, 0, 0, 2, 4);
            Kit.R(c, "head", t, 32, 0, 0, 1, t + 4, 8, 0, 0, 4);
            Kit.R(c, "mandR", t, 0, 12, 0, 1, t + 4, 0, -14, 0, 0);
            Kit.R(c, "mandL", t, 0, -12, 0, 1, t + 4, 0, 14, 0, 0);
            if (h + 1 < hits) { Kit.R(c, "body", t + gap - 2, -8, 0, 0, 0); Kit.P(c, "body", t + gap - 2, 0, 0.5f, -1.5f, 0); }
        }
        Kit.R(c, "body", len, 0, 0, 0, 0); Kit.P(c, "body", len, 0, 0, 0, 0);
        Kit.R(c, "head", len, 0, 0, 0, 0); Kit.R(c, "mandR", len, 0, 0, 0, 0); Kit.R(c, "mandL", len, 0, 0, 0, 0);
        return c;
    }

    /** rear up, shudder, settle: buffs / debuffs / summons / stuns */
    static Clip rear(ModelSpec m, String name, int len, int peak) {
        Clip c = m.clip(name, len);
        Kit.R(c, "body", 0, 0, 0, 0, 0, peak, -28, 0, 0, 2, len, 0, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 0, peak, 0, 3, -2, 2, len, 0, 0, 0, 0);
        Kit.S(c, "body", peak, 1, 1, 1, 0, peak + 3, 1.12f, 1.12f, 1.12f, 3, len, 1, 1, 1, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 0, peak, -35, 0, 0, 2, len, 0, 0, 0, 0);
        Kit.R(c, "mandR", 0, 0, 0, 0, 0, peak, 0, -45, 0, 0, len, 0, 0, 0, 0);
        Kit.R(c, "mandL", 0, 0, 0, 0, 0, peak, 0, 45, 0, 0, len, 0, 0, 0, 0);
        return c;
    }
}
