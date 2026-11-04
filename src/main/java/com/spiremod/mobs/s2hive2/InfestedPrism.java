package com.spiremod.mobs.s2hive2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Hive elite INFESTED PRISM (161 HP): floating orange "Dorito" crystal riddled with bugs. Vital Spark 2 (our take: Pulsate also exposes foes).
 * Cycle: Jab 15 -> Radiate 11 + 16 Block -> Whirlwind 5x3 -> Pulsate 8 + 20 Block + Vital Spark.
 */
public final class InfestedPrism {
    private InfestedPrism() {}

    static final int AMB = 0xff9a30, AMB_L = 0xffd878, AMB_D = 0xc86a20, CRACK = 0x3a1a10, CORE = 0xfff0a0, BUG = 0x5a6a2a, BUG_D = 0x2e3a14, EYE = 0xff3020;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_infested_prism");
        Part body = m.add("body").at(0, 26, 0);
        Part cr = body.add("cr").at(0, 0, 0);
        int[] w = {26, 22, 18, 14, 10, 6, 2};
        for (int i = 0; i < w.length; i++) {
            Box b = cr.box(-w[i] / 2f, -14 + i * 4, -4, w[i], 4, 8).color(AMB).grad(AMB_L, AMB_D).bevel(0.3f).noise(0.1f);
            if (i % 2 == 0) b.paint(Face.FRONT, 2, 1, Math.max(1, w[i] - 4), 1, CRACK);
        }
        cr.box(-11, -13, 3.8f, 22, 1, 1).color(AMB_D).flat();                       // bottom rim
        cr.box(-3, -8, 4.1f, 6, 9, 1).color(AMB_L).flat().glow(Face.FRONT, 1, 1, 4, 7, CORE); // glowing core window
        cr.box(-2, -6, -5f, 4, 8, 1).color(CORE).flat().glow(Face.BACK, 0, 0, 4, 8, CORE);
        // infesting bugs: grubs chewing out of the cracks, mandibles on the lower tip
        cr.box(-9, -10, 3.5f, 3, 2, 3).color(BUG).grad(BUG, BUG_D).bevel(0.2f);
        cr.box(6, -4, 3.5f, 3, 3, 3).color(BUG).grad(BUG, BUG_D).bevel(0.2f);
        cr.box(-3, 5, 3.6f, 2, 2, 2).color(BUG_D).bevel(0.2f);
        cr.box(-7, -6, 3.7f, 1, 1, 1).color(EYE).flat().glow(Face.FRONT, 0, 0, 1, 1, EYE);
        cr.box(8, -9, 3.7f, 1, 1, 1).color(EYE).flat().glow(Face.FRONT, 0, 0, 1, 1, EYE);
        for (int sd = -1; sd <= 1; sd += 2) {
            Part md = cr.add(sd < 0 ? "mandR" : "mandL").at(sd * 3, -14, 4);
            md.box(-0.5f, -5, -0.5f, 1, 5, 1).color(BUG_D);
            md.box(-0.5f, -6, -0.5f, 1, 1, 3).color(0xd8d0a0).flat();
            // bug legs on the flanks
            for (int k = 0; k < 3; k++) {
                Part lg = cr.add("lg" + k + (sd < 0 ? "R" : "L")).at(sd * (12 - k * 3.5f), -9 + k * 5, 0).rot(0, 0, sd * -55);
                lg.box(-0.5f, -6, -0.5f, 1, 6, 1).color(BUG_D);
            }
        }
        // jab spike (extends forward only during Jab)
        Part sp = body.add("spike").at(0, -2, 4);
        sp.box(-1.5f, -1.5f, 0, 3, 3, 10).color(AMB_L).flat().glow(Face.FRONT, 0, 0, 3, 3, CORE);
        // orbiting shards
        for (int i = 0; i < 3; i++) {
            Part sh = m.add("sh" + i).at(0, 26, 0);
            Part s2 = sh.add("shp" + i).at(16, i * 3, 0);
            s2.box(-1.5f, -2, -1.5f, 3, 5, 3).color(AMB_L).grad(CORE, AMB).bevel(0.2f).glow(Face.FRONT, 1, 1, 1, 2, CORE);
        }
        m.proc((r, c) -> {
            for (int i = 0; i < 3; i++) {
                float sp2 = "whirlwind".equals(c.move) ? 28 : 4;
                r.rot("sh" + i, 0, c.age * sp2 + i * 120, 0);
            }
            r.show("spike", "jab".equals(c.move) && c.moveT > 9 && c.moveT < 20);
            for (int k = 0; k < 3; k++) for (int sd = -1; sd <= 1; sd += 2)
                r.rot("lg" + k + (sd < 0 ? "R" : "L"), (float) Math.sin(c.age * 0.4f + k * 2 + sd) * 18, 0, 0);
            r.rot("mandR", 0, 0, (float) Math.sin(c.age * 0.3f) * 6); r.rot("mandL", 0, 0, -(float) Math.sin(c.age * 0.3f) * 6);
        });
        m.bob(1.2f, 0.12f);
        m.clip("walk", 20).loop().rot("body", 0, 0, 0, 4).rot("body", 10, 0, 0, -4).rot("body", 20, 0, 0, 4);
        m.clip("idle", 60).loop()
            .rot("cr", 0, 0, 0, 3).rot("cr", 30, 0, 12, -3).rot("cr", 60, 0, 0, 3)
            .scl("cr", 0, 1, 1, 1).scl("cr", 30, 1.03f, 1.03f, 1.03f).scl("cr", 60, 1, 1, 1);
        // JAB: tilt back, spear the tip forward
        m.clip("jab", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -22, 0, 0).rot("body", 14, 38, 0, 0, Ease.IN_CUBIC).rot("body", 22, 10, 0, 0).rot("body", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 2, -4).pos("body", 14, 0, -3, 9, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 14, 0, 0, 40).rot("mandR", 30, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 14, 0, 0, -40).rot("mandL", 30, 0, 0, 0);
        // RADIATE: swell, flash out waves, shell hardens
        m.clip("radiate", 34)
            .scl("cr", 0, 1, 1, 1).scl("cr", 12, 0.85f, 0.85f, 0.85f).scl("cr", 16, 1.3f, 1.3f, 1.3f, Ease.OUT_BACK).scl("cr", 26, 1.1f, 1.1f, 1.1f).scl("cr", 34, 1, 1, 1)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -3, 0).pos("body", 16, 0, 4, 0, Ease.OUT_BACK).pos("body", 34, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 20, 0, 40, 0).rot("body", 34, 0, 0, 0);
        // WHIRLWIND: three spinning slashes
        m.clip("whirlwind", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 0, -30, 14).rot("body", 14, 0, 360 * 0.4f, 14, Ease.IN_CUBIC).rot("body", 22, 0, 360 * 0.8f, 14).rot("body", 30, 0, 360 * 1.2f, 8).rot("body", 40, 0, 360 * 1.2f, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -2, 0).pos("body", 14, 0, 2, 0).pos("body", 40, 0, 0, 0)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 12, 0, 0, 50).rot("mandR", 40, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 12, 0, 0, -50).rot("mandL", 40, 0, 0, 0);
        // PULSATE: contract then thump twice, glow
        m.clip("pulsate", 40)
            .scl("cr", 0, 1, 1, 1).scl("cr", 8, 0.8f, 0.8f, 0.8f).scl("cr", 12, 1.25f, 1.25f, 1.25f, Ease.OUT_BACK).scl("cr", 18, 0.85f, 0.85f, 0.85f).scl("cr", 22, 1.3f, 1.3f, 1.3f, Ease.OUT_BACK).scl("cr", 40, 1, 1, 1)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 15, 0, 0).rot("body", 12, -15, 0, 0, Ease.OUT_BACK).rot("body", 22, -10, 0, 0).rot("body", 40, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -12, 0, 10).rot("body", 10, 0, 0, 0);
        m.clip("death", 36)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 20, 0, 30).rot("body", 30, 70, 0, 120, Ease.IN_QUAD)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 3, 0).pos("body", 36, 0, -24, 0, Ease.OUT_BOUNCE)
            .scl("cr", 0, 1, 1, 1).scl("cr", 36, 1.15f, 0.6f, 1.15f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_infested_prism", "Infested Prism", "寄生プリズム")
            .game(2, 2).size(1.5f, 2.2f).hp(161).speed(0.2f).style(Style.INSECT).egg(0xe8903a, 0x5a6a2a)
            .elite().flying().model(InfestedPrism::model)
            .spawn(1, 1, 1, MobDef.Habitat.CAVE)
            .pattern(Pattern.cycle("jab", "radiate", "whirlwind", "pulsate"))
            .move(Move.of("jab", 30).intent(Intent.ATTACK, 15, 1).reach(3f)
                .sound(8, SoundEvents.SILVERFISH_AMBIENT, 1f, 0.5f)
                .at(15, Fx.melee(15, 3.4, 50)))
            .move(Move.of("radiate", 34).intent(Intent.ATTACK_DEFEND, 11, 1).reach(3.2f)
                .sound(14, SoundEvents.AMETHYST_BLOCK_RESONATE, 1.4f, 0.6f)
                .at(16, (s, t) -> { s.addBlock(16); Fx.ring(s, ParticleTypes.END_ROD, s.position().add(0, 1, 0), 2.5, 24); H.cone(s, 11, 3.2, 360, null, null); }))
            .move(Move.of("whirlwind", 40).intent(Intent.ATTACK, 5, 3).reach(3f)
                .sound(8, SoundEvents.PHANTOM_FLAP, 1f, 1.5f)
                .at(14, H.cone(5, 3, 360)).at(22, H.cone(5, 3, 360)).at(30, H.cone(5, 3, 360)))
            .move(Move.of("pulsate", 40).intent(Intent.ATTACK_DEFEND, 8, 1).reach(3.2f)
                .sound(12, SoundEvents.AMETHYST_BLOCK_CHIME, 1.4f, 0.5f)
                .at(12, (s, t) -> { s.addBlock(20); Fx.particles(s, ParticleTypes.GLOW, 20, 0.8); H.cone(s, 8, 3.2, 360, null, e -> Fx.vulnerable(e, 4)); })); // ponytail: Vital Spark approximated as Vulnerable
    }
}
