package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth: Inklet 11-17 HP, appears in threes, starts with Slippery 1 (a hit deals at most 1 damage, then loses a stack).
 * Outer inklets open with Jab (3); the middle one opens with Whirlwind (2 x3). Then alternates Jab and random Piercing Gaze (10) / Whirlwind.
 * var 1 = the middle inklet (1/3 chance when spawned).
 */
public final class Inklet {
    private Inklet() {}

    static final int INK = 0x1c1a2e, INK_L = 0x3a3660, INK_D = 0x0c0a18, EYE = 0xe8f4ff, PUPIL = 0x6a4aff, DRIP = 0x2a2850;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_inklet");
        Part body = m.add("body").at(0, 2, 0);
        body.box(-5, 0, -4, 10, 6, 8).color(INK).grad(INK_L, INK_D).bevel(0.3f).noise(0.06f);
        body.box(-4, 6, -3.5f, 8, 3, 7).color(INK).grad(INK_L, INK).bevel(0.3f);
        body.box(-6, 0, -3, 12, 2, 6).color(INK_D).flat().noise(0.05f);                            // ink puddle skirt
        for (int i = 0; i < 4; i++) body.box(-5 + i * 3, -2, -3.5f + (i % 2) * 5, 2, 2, 2).color(DRIP).flat();  // drips
        Part head = body.add("head").at(0, 9, 0);
        head.box(-4, 0, -3.5f, 8, 6, 7).color(INK).grad(INK_L, INK).bevel(0.3f).noise(0.05f);
        Part eyeR = head.add("eyeR").at(-2, 3, 3.5f), eyeL = head.add("eyeL").at(2, 3, 3.5f);
        eyeR.box(-1.7f, -1.7f, -0.3f, 3, 3, 1).color(EYE).glow(Face.FRONT, 0, 0, 3, 3, EYE).glow(Face.FRONT, 1, 1, 1, 1, PUPIL);
        eyeL.box(-1.3f, -1.7f, -0.3f, 3, 3, 1).color(EYE).glow(Face.FRONT, 0, 0, 3, 3, EYE).glow(Face.FRONT, 1, 1, 1, 1, PUPIL);
        head.box(-2, 0.5f, 3.3f, 4, 1, 1).color(INK_D).flat();                                  // thin mouth
        head.box(-0.5f, 6, -0.5f, 1, 3, 1).color(INK_L).flat();                                 // ink drip tuft on top
        head.box(-1.5f, 8, -1.5f, 3, 2, 3).color(INK).bevel(0.3f);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 5, 7, 0);
            arm.box(-1, -5, -1, 2, 5, 2).color(INK).grad(INK_L, INK_D);
            arm.box(-1.5f, -7, -1.5f, 3, 2, 3).color(INK_L).bevel(0.3f);
        }
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2, 3, 0);
            leg.box(-1.5f, -3, -1.5f, 3, 3, 3).color(INK_D).bevel(0.2f);
        }
        m.look("head", 40, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 30f, 20f);
        Clip c = m.clip("idle", 36).loop();
        Kit.S(c, "body", 0, 1, 1, 1, 18, 1.05f, 0.94f, 1.05f, 36, 1, 1, 1);
        Kit.R(c, "head", 0, 0, 0, 0, 10, 4, 8, 0, 26, 0, -8, 0, 36, 0, 0, 0);
        Kit.P(c, "eyeR", 0, 0, 0, 0, 20, 0.3f, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "armR", 0, 5, 0, 6, 18, 12, 0, 8, 36, 5, 0, 6);
        Kit.R(c, "armL", 0, 5, 0, -6, 18, 12, 0, -8, 36, 5, 0, -6);
        // JAB: quick jabbing thrust of one arm, squash-and-stretch
        c = m.clip("jab", 20);
        Kit.R(c, "armR", 0, 0, 0, 0, 5, 30, 0, 20, -8, -100, -10, -5, 14, -80, 0, 0, 20, 0, 0, 0);
        Kit.R(c, "body", 0, 0, 0, 0, 5, -8, -18, 0, -8, 12, 20, 0, 20, 0, 0, 0);
        Kit.S(c, "body", 0, 1, 1, 1, 5, 1.1f, 0.85f, 1.1f, -8, 0.9f, 1.15f, 0.95f, 20, 1, 1, 1);
        Kit.P(c, "body", 0, 0, 0, 0, 5, 0, 0, -1.5f, -8, 0, 0, 3, 20, 0, 0, 0);
        // WHIRLWIND: arms spread, whole body spins like a top
        c = m.clip("whirlwind", 36);
        Kit.R(c, "body", 0, 0, 0, 0, 6, 0, 0, 0, 12, 0, 360, 0, 18, 0, 720, 0, 24, 0, 1080, 0, 30, 0, 1080, 0, 36, 0, 1080, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 6, 0, 0, 80, 30, 0, 0, 80, 36, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 6, 0, 0, -80, 30, 0, 0, -80, 36, 0, 0, 0);
        Kit.S(c, "body", 0, 1, 1, 1, 6, 0.9f, 1.15f, 0.9f, 30, 0.9f, 1.15f, 0.9f, 36, 1, 1, 1);
        // PIERCING GAZE: eyes swell, head locks on, a bright beam-stare
        c = m.clip("piercing_gaze", 32);
        Kit.S(c, "eyeR", 0, 1, 1, 1, 10, 1.6f, 1.6f, 1.6f, -16, 2.2f, 2.2f, 1.5f, 26, 1.8f, 1.8f, 1.5f, 32, 1, 1, 1);
        Kit.S(c, "eyeL", 0, 1, 1, 1, 10, 1.6f, 1.6f, 1.6f, -16, 2.2f, 2.2f, 1.5f, 26, 1.8f, 1.8f, 1.5f, 32, 1, 1, 1);
        Kit.R(c, "head", 0, 0, 0, 0, 10, -12, 0, 0, -16, 8, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "body", 0, 0, 0, 0, 10, -10, 0, 0, 32, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 10, 0, 1, -1, 16, 0, 0, 1, 32, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 10, 0, 0, 40, 28, 0, 0, 40, 32, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 10, 0, 0, -40, 28, 0, 0, -40, 32, 0, 0, 0);
        Kit.hurt(m, "body", -12, 8);
        c = m.clip("death", 22);   // goes pop: swells then bursts into a splat
        Kit.S(c, "body", 0, 1, 1, 1, 8, 1.4f, 1.4f, 1.4f, 12, 1.7f, 1.7f, 1.7f, 22, 2.2f, 0.1f, 2.2f);
        Kit.S(c, "head", 0, 1, 1, 1, 12, 1.4f, 1.4f, 1.4f, 22, 0.01f, 0.01f, 0.01f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_inklet", "Inklet", "インクレット")
            .game(2, 1).size(0.6f, 0.95f).hp(14).speed(0.26f).style(Style.SLIME).egg(0x1c1a2e, 0xe8f4ff)
            .model(Inklet::model)
            .spawn(6, 3, 3, MobDef.Habitat.JUNGLE)
            .onSpawn(s -> { s.setCount("slippery", 1); if (s.rand().nextInt(3) == 0) s.setVar(1); })
            .onHurt((s, src, dmg) -> { if (s.count("slippery") > 0) { s.setCount("slippery", s.count("slippery") - (dmg > 1 ? 1 : 0)); return Math.min(dmg, 1f); } return dmg; })
            .pattern(s -> {
                int t = s.getTurn(); boolean mid = s.getVar() == 1;
                if (t == 0) return mid ? "whirlwind" : "jab";
                boolean jab = mid ? t % 2 == 1 : t % 2 == 0;
                return jab ? "jab" : (s.rand().nextBoolean() ? "piercing_gaze" : "whirlwind");
            })
            .move(Move.of("jab", 20).intent(Intent.ATTACK, 3, 1).reach(2.0f)
                .sound(8, SoundEvents.PLAYER_ATTACK_KNOCKBACK, 0.8f, 1.5f).at(8, Kit.hit(3, 2.4, 80)))
            .move(Move.of("whirlwind", 36).intent(Intent.ATTACK, 2, 3).reach(2.2f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.3f)
                .at(12, Kit.hit(2, 2.6, 360)).at(18, Kit.hit(2, 2.6, 360)).at(24, Kit.hit(2, 2.6, 360)))
            .move(Move.of("piercing_gaze", 32).intent(Intent.ATTACK, 10, 1).ranged(8)
                .sound(12, SoundEvents.GUARDIAN_ATTACK, 0.8f, 1.5f)
                .at(16, (s, t) -> { if (t != null) { Fx.particlesAt(s, ParticleTypes.END_ROD, t.position().add(0, 1.2, 0), 14, 0.2, 0.02); Kit.zap(10).run(s, t); } }));
    }
}
