package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth: Eye With Teeth 6 HP minion summoned by Fogmog (Illusion). Distract every turn: shuffles 3 Dazed. Cannot damage. */
public final class EyeWithTeeth {
    private EyeWithTeeth() {}

    static final int FLESH = 0xc8a0a8, FLESH_D = 0x8a5a6a, WHITE = 0xf0eae0, IRIS = 0x4aa8ff, VEIN = 0xb03a4a, TOOTH = 0xf8f4e0, MOUTH = 0x4a1020;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_eye_with_teeth");
        Part ball = m.add("ball").at(0, 14, 0);
        ball.box(-5, -5, -5, 10, 10, 10).color(WHITE).grad(WHITE, 0xc8c0b8).bevel(0.4f)
            .paint(Face.FRONT, 2, 6, 1, 3, VEIN).paint(Face.FRONT, 7, 5, 1, 4, VEIN).paint(Face.LEFT, 3, 2, 4, 1, VEIN).paint(Face.RIGHT, 3, 2, 4, 1, VEIN);
        Part iris = ball.add("iris").at(0, 1.5f, 5);
        iris.box(-3, -3, -0.4f, 6, 6, 1).color(IRIS).glow(Face.FRONT, 0, 0, 6, 6, IRIS).paint(Face.FRONT, 2, 2, 2, 2, 0x04081a).glow(Face.FRONT, 0, 0, 6, 1, 0x8ad0ff);
        Part lidU = ball.add("lidU").at(0, 5, 0);
        lidU.box(-5.5f, 0, -5.5f, 11, 2, 11).color(FLESH).grad(FLESH, FLESH_D).bevel(0.3f).inflate(0.1f);
        Part jaw = ball.add("jaw").at(0, -2, 1);          // a toothy lower mouth
        jaw.box(-5, -5, -1, 10, 3, 7).color(FLESH_D).grad(FLESH, FLESH_D).bevel(0.2f)
            .paint(Face.TOP, 1, 1, 8, 5, MOUTH).paint(Face.TOP, 0, 0, 10, 1, TOOTH).paint(Face.TOP, 0, 6, 10, 1, TOOTH).paint(Face.FRONT, 0, 0, 1, 1, TOOTH).paint(Face.FRONT, 3, 0, 1, 1, TOOTH).paint(Face.FRONT, 6, 0, 1, 1, TOOTH).paint(Face.FRONT, 9, 0, 1, 1, TOOTH);
        for (int s = -1; s <= 1; s += 2) {
            Part w = ball.add(s < 0 ? "finR" : "finL").at(s * 5, 0, -2);
            w.box(s < 0 ? -7 : 0, -2, -2, 7, 1, 5).color(FLESH).flat().noise(0.1f);
        }
        m.look("ball", 40, 25);
        m.bob(2f, 0.1f);
        m.proc((r, c) -> { float f = (float) Math.sin(c.age * 0.5f) * 25; r.rot("finR", 0, 0, -f); r.rot("finL", 0, 0, f); });
        Clip c = m.clip("idle", 40).loop();
        Kit.R(c, "ball", 0, 0, 0, 0, 20, 0, 0, 6, 40, 0, 0, 0);
        Kit.P(c, "iris", 0, -0.8f, 0, 0, 14, 0.8f, 0.4f, 0, 28, 0, -0.5f, 0, 40, -0.8f, 0, 0);
        Kit.P(c, "lidU", 0, 0, 0, 0, 32, 0, 0, 0, 34, 0, -3, 0, 37, 0, 0, 0, 40, 0, 0, 0);
        m.clip("walk", 20).loop();
        // DISTRACT: eye swells and flashes a hypnotic stare while the mouth chatters
        c = m.clip("distract", 30);
        Kit.S(c, "ball", 0, 1, 1, 1, 8, 1.3f, 1.3f, 1.3f, -14, 0.9f, 0.9f, 1.4f, 30, 1, 1, 1);
        Kit.S(c, "iris", 0, 1, 1, 1, 8, 1.6f, 1.6f, 1.6f, 14, 0.5f, 0.5f, 1, 22, 1.4f, 1.4f, 1, 30, 1, 1, 1);
        Kit.R(c, "jaw", 0, 0, 0, 0, 8, 25, 0, 0, 12, 0, 0, 0, 16, 25, 0, 0, 20, 0, 0, 0, 24, 25, 0, 0, 28, 0, 0, 0);
        Kit.R(c, "ball", 0, 0, 0, 0, 8, -20, 0, 0, -14, 15, 0, 0, 30, 0, 0, 0);
        Kit.P(c, "ball", 0, 0, 0, 0, 8, 0, 1, -1, -14, 0, 0, 4, 30, 0, 0, 0);
        Kit.hurt(m, "ball", -20, 12);
        c = m.clip("death", 20);
        Kit.S(c, "ball", 0, 1, 1, 1, 6, 1.4f, 1.4f, 1.4f, 20, 0.01f, 0.01f, 0.01f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_eye_with_teeth", "Eye With Teeth", "牙つきの眼")
            .game(2, 1).size(0.8f, 1.0f).hp(6).speed(0.15f).flying().minion().style(Style.GHOST).egg(0xf0eae0, 0x4aa8ff)
            .model(EyeWithTeeth::model)
            .spawn(0, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("distract"))
            .move(Move.of("distract", 30).intent(Intent.DEBUFF).ranged(10)
                .sound(10, SoundEvents.ENDERMAN_STARE, 0.8f, 1.6f)
                .at(15, (s, t) -> { if (t != null) for (int i = 0; i < 3; i++) Fx.curse(t, Fx.Curse.DAZED); }));
    }
}
