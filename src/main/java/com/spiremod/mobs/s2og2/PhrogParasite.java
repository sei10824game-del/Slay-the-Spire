package com.spiremod.mobs.s2og2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth elite Phrog Parasite (61-64 HP). A bloated olive phrog carcass bristling with parasitic worms.
 * Alternates Infect (3 Infection into discard -> poison) and Lash (4x4). Infested: on death 4 Wrigglers burst out, stunned for their first turn.
 */
public final class PhrogParasite {
    private PhrogParasite() {}

    static final int OLIVE = 0x6f7f1a, OLIVE_D = 0x4b5810, OLIVE_L = 0x95a52c, BELLY = 0xcdb44a, PUS = 0xc9e66a, PUS_L = 0xe6f6a0,
        FROG = 0x4d5e1c, FROG_D = 0x2f3a10, WORM = 0xc0904a, WORM_B = 0x8a5f26, WORM2 = 0xd4b06a, WORM2_B = 0xa07a3a;

    // top tentacle worms: name, x, y, z, rx, ry, rz, segments
    static final float[][] TOP = {
        {0, 4, 24, -2, -10, 0, 8, 4}, {1, -9, 20, 4, -20, 0, 35, 4}, {2, 9, 19, 5, -25, 0, -38, 4}, {3, -5, 17, 9, -60, 0, 15, 3},
        {4, 6, 15, 10, -70, 0, -12, 3}, {5, -12, 14, -4, 15, 0, 60, 3}, {6, 12, 13, -5, 18, 0, -62, 3}, {7, 0, 18, -9, 50, 0, 0, 3},
        {8, -2, 9, 12, -85, 0, 5, 3}};
    // worm legs under the body
    static final float[][] LEG = {{0, -9, 1, 7, 28, 0, 0}, {1, 9, 1, 7, 28, 0, 0}, {2, -11, 1, -2, 0, 0, -28}, {3, 11, 1, -2, 0, 0, 28},
        {4, -6, 1, -9, -28, 0, -10}, {5, 6, 1, -9, -28, 0, 10}};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_phrog_parasite").scale(1.35f);
        Part body = m.add("body").at(0, 11, 0);
        body.box(-11, 0, -10, 22, 5, 20).color(OLIVE_D).grad(OLIVE, OLIVE_D).paint(Face.FRONT, 4, 0, 14, 5, BELLY);
        body.box(-14, 4, -12, 28, 10, 24).color(OLIVE).grad(OLIVE_L, OLIVE).bevel(0.12f)
            .paint(Face.FRONT, 5, 2, 18, 8, BELLY).paint(Face.FRONT, 7, 1, 14, 1, 0xb89c3a).paint(Face.FRONT, 3, 6, 2, 4, 0xb89c3a)
            .paint(Face.LEFT, 8, 4, 10, 6, 0xb09a40).paint(Face.RIGHT, 6, 4, 10, 6, 0xb09a40);
        body.box(-12, 14, -11, 24, 6, 22).color(OLIVE).grad(OLIVE_L, OLIVE).bevel(0.1f).paint(Face.FRONT, 8, 3, 8, 3, 0xb8a440);
        body.box(-9, 20, -8, 18, 3, 16).color(OLIVE_L).grad(0xa2b236, OLIVE_L);
        body.box(-5, 23, -5, 10, 1, 10).color(0xa8b83a);
        // pustules (pale lime blisters)
        float[][] pus = {{-8, 23, -2, 5}, {5, 23, 2, 4}, {-1, 24, -4, 3}, {9, 20, -6, 4}, {-11, 18, 5, 4}, {11, 16, 6, 3}, {-13, 10, -6, 4}, {-6, 21, 6, 3}, {3, 20, -9, 4}, {13, 9, -2, 3}};
        for (float[] p : pus) {
            int s = (int) p[3];
            body.box(p[0] - s / 2f, p[1], p[2] - s / 2f, s, 2, s).color(PUS).bevel(0.2f).glow(Face.TOP, s / 2 - 1, s / 2 - 1, 1, 1, PUS_L);
        }
        // phrog limbs clinging to the outside of the bloated carcass
        for (int sd = -1; sd <= 1; sd += 2) {
            String n = sd < 0 ? "R" : "L";
            Part arm = body.add("arm" + n).at(sd * 9, 16, 9).rot(-20, 0, sd * -15);
            arm.box(-1.5f, -8, -1.5f, 3, 8, 3).color(FROG).bevel(0.15f);
            Part fore = arm.add("fore" + n).at(0, -8, 0).rot(-30, 0, sd * 25);
            fore.box(-1, -7, -1, 2, 7, 2).color(FROG).noise(0.1f);
            Part hand = fore.add("hand" + n).at(0, -7, 0);
            hand.box(-2, -1, -1, 4, 1, 2).color(FROG_D);
            for (int f = -1; f <= 1; f++) hand.box(f * 1.5f - 0.5f, -4, -0.5f, 1, 3, 1).color(FROG_D).flat();
            for (int f = -1; f <= 1; f++) hand.box(f * 1.5f - 1f, -5, -1f, 2, 1, 2).color(0x22290c).flat();
            // hind legs folded at the rear sides
            Part hip = body.add("hip" + n).at(sd * 12, 6, -6).rot(30, 0, sd * -20);
            hip.box(-1.5f, -7, -1.5f, 3, 7, 3).color(FROG_D).bevel(0.15f);
            Part shin = hip.add("shin" + n).at(0, -7, 0).rot(-70, 0, 0);
            shin.box(-1, -7, -1, 2, 7, 2).color(FROG_D);
            Part foot = shin.add("foot" + n).at(0, -7, 0).rot(40, 0, 0);
            foot.box(-2, -1, -1, 4, 1, 4).color(0x22290c);
        }
        // parasitic worms bursting out of the top/sides
        for (float[] w : TOP) {
            boolean light = ((int) w[0]) % 3 == 1;
            Bits.chain(body, "w" + (int) w[0] + "_", w[1], w[2], w[3], w[4], w[5], w[6], (int) w[7], 5, 3,
                light ? WORM2 : WORM, light ? WORM2_B : WORM_B, -18, 0, ((int) w[0]) % 2 == 0 ? 15 : -15, 1);
        }
        // worm legs carrying the carcass (hang down from the belly)
        for (float[] w : LEG) {
            Bits.chain(body, "l" + (int) w[0] + "_", w[1], w[2], w[3], w[4], w[5], w[6], 3, 4, 3, WORM, WORM_B, -12, 0, 0, -1);
        }
        m.proc((r, c) -> {
            float t = c.age * 0.12f;
            for (float[] w : TOP) Bits.wiggle(r, "w" + (int) w[0] + "_", (int) w[7], t, 9f, w[0] * 1.7f);
            // worm legs: scuttle while walking, slight squirm at rest
            float s = (float) Math.cos(c.swing * 0.9f) * c.swingAmt;
            for (float[] w : LEG) {
                int i = (int) w[0];
                float ph = (i % 2 == 0 ? 1 : -1) * (i < 2 || i > 3 ? 1 : -1);
                r.rot("l" + i + "_0", s * 35 * ph + (float) Math.sin(t + i) * 3, 0, 0);
                r.rot("l" + i + "_1", Math.abs(s) * -20, 0, 0);
            }
            r.pos("body", 0, Math.abs(s) * 1.2f, 0);
        });
        m.clip("idle", 40).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 20, 1.04f, 0.97f, 1.04f).scl("body", 40, 1, 1, 1)
            .rot("armR", 0, 0, 0, 0).rot("armR", 20, -6, 0, 4).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 25, -6, 0, -4).rot("armL", 40, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 0, 0, 2).rot("body", 30, 0, 0, -2).rot("body", 40, 0, 0, 0);
        // LASH: rear back, then four worms whip down one after another (hits at 12, 18, 24, 30)
        Clip lash = m.clip("lash", 42)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -14, 0, 0).rot("body", 12, 12, 0, 0, Ease.IN_CUBIC).rot("body", 15, 2, 0, 0)
            .rot("body", 18, 12, 0, 0, Ease.IN_CUBIC).rot("body", 21, 2, 0, 0).rot("body", 24, 12, 0, 0, Ease.IN_CUBIC).rot("body", 27, 2, 0, 0)
            .rot("body", 30, 16, 0, 0, Ease.IN_CUBIC).rot("body", 42, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1, -2).pos("body", 12, 0, 0, 3, Ease.IN_CUBIC).pos("body", 30, 0, -1, 4).pos("body", 42, 0, 0, 0);
        int[] whip = {3, 4, 8, 0};
        int[] hit = {12, 18, 24, 30};
        for (int k = 0; k < 4; k++) {
            String w = "w" + whip[k] + "_";
            int h = hit[k];
            lash.rot(w + "0", 0, 0, 0, 0).rot(w + "0", h - 6, 70, 0, 0).rot(w + "0", h, -60, 0, 0, Ease.IN_CUBIC).rot(w + "0", h + 6, -40, 0, 0).rot(w + "0", 42, 0, 0, 0)
                .rot(w + "1", 0, 0, 0, 0).rot(w + "1", h - 6, 40, 0, 0).rot(w + "1", h, -45, 0, 0, Ease.IN_CUBIC).rot(w + "1", h + 6, -20, 0, 0).rot(w + "1", 42, 0, 0, 0)
                .rot(w + "2", 0, 0, 0, 0).rot(w + "2", h - 6, 30, 0, 0).rot(w + "2", h + 1, -50, 0, 0, Ease.IN_CUBIC).rot(w + "2", h + 6, -10, 0, 0).rot(w + "2", 42, 0, 0, 0);
        }
        lash.rot("armR", 0, 0, 0, 0).rot("armR", 10, -50, 0, -20).rot("armR", 30, -40, 0, -25).rot("armR", 42, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -50, 0, 20).rot("armL", 30, -40, 0, 25).rot("armL", 42, 0, 0, 0);
        // INFECT: swell up, every worm flares out, then the carcass heaves forward and sprays infection
        Clip inf = m.clip("infect", 40)
            .scl("body", 0, 1, 1, 1).scl("body", 12, 1.14f, 0.86f, 1.14f).scl("body", 18, 0.9f, 1.18f, 0.9f, Ease.OUT_BACK)
            .scl("body", 22, 1.12f, 0.9f, 1.12f).scl("body", 26, 0.96f, 1.06f, 0.96f).scl("body", 40, 1, 1, 1)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -10, 0, 0).rot("body", 19, 20, 0, 0, Ease.IN_CUBIC).rot("body", 28, 14, 0, 0).rot("body", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -1.5f, -1).pos("body", 19, 0, 1, 3, Ease.IN_CUBIC).pos("body", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, 20, 0, -40).rot("armR", 19, -60, 0, -20).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, 20, 0, 40).rot("armL", 19, -60, 0, 20).rot("armL", 40, 0, 0, 0);
        for (float[] w : TOP) {
            String n = "w" + (int) w[0] + "_0";
            float out = w[1] < 0 ? 1 : -1;
            inf.rot(n, 0, 0, 0, 0).rot(n, 12, 25, 0, out * 30).rot(n, 19, -45, 0, out * -10, Ease.IN_CUBIC).rot(n, 30, -20, 0, 0).rot(n, 40, 0, 0, 0);
        }
        m.clip("hurt", 10).scl("body", 0, 1, 1, 1).scl("body", 3, 1.1f, 0.88f, 1.1f).scl("body", 10, 1, 1, 1)
            .rot("body", 0, 0, 0, 0).rot("body", 3, -10, 0, 5).rot("body", 10, 0, 0, 0)
            .rot("w0_0", 0, 0, 0, 0).rot("w0_0", 3, 30, 0, 20).rot("w0_0", 10, 0, 0, 0)
            .rot("w1_0", 0, 0, 0, 0).rot("w1_0", 3, 20, 0, 30).rot("w1_0", 10, 0, 0, 0)
            .rot("w2_0", 0, 0, 0, 0).rot("w2_0", 3, 20, 0, -30).rot("w2_0", 10, 0, 0, 0);
        // DEATH: bloats, convulses, bursts (wrigglers emerge) and the husk collapses
        Clip d = m.clip("death", 36)
            .scl("body", 0, 1, 1, 1).scl("body", 10, 1.2f, 1.15f, 1.2f).scl("body", 13, 1.1f, 1.2f, 1.1f).scl("body", 16, 1.25f, 1.1f, 1.25f)
            .scl("body", 20, 1.2f, 0.5f, 1.2f, Ease.IN_CUBIC).scl("body", 36, 1.15f, 0.35f, 1.15f, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, 1, 0).pos("body", 26, 0, -8, 0, Ease.IN_QUAD).pos("body", 36, 0, -9, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 11, 0, 0, 5).rot("body", 14, 0, 0, -5).rot("body", 17, 0, 0, 4).rot("body", 36, 6, 0, 0);
        for (float[] w : TOP) {
            String n = "w" + (int) w[0] + "_";
            d.rot(n + "0", 0, 0, 0, 0).rot(n + "0", 16, -30, 0, w[1] < 0 ? 40 : -40).rot(n + "0", 36, 60, 0, w[1] < 0 ? 70 : -70, Ease.OUT_BOUNCE)
                .rot(n + "1", 0, 0, 0, 0).rot(n + "1", 36, 50, 0, 0);
        }
        for (float[] w : LEG) d.rot("l" + (int) w[0] + "_0", 0, 0, 0, 0).rot("l" + (int) w[0] + "_0", 30, w[3] > 0 ? 70 : -70, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_phrog_parasite", "Phrog Parasite", "フロッグパラサイト")
            .game(2, 1).elite().size(2.4f, 2.7f).hp(62).speed(0.2f).style(Style.FROG).egg(0x6f7f1a, 0xc0904a).kbResist(0.5f)
            .model(PhrogParasite::model)
            .spawn(1, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(Pattern.cycle("infect", "lash"))
            .move(Move.of("infect", 40).intent(Intent.STRONG_DEBUFF).ranged(10)
                .sound(10, SoundEvents.SLIME_SQUISH, 1.4f, 0.5f)
                .at(19, (s, t) -> {
                    Fx.sound(s, SoundEvents.FROG_EAT, 1.6f, 0.5f);
                    Fx.particlesAt(s, ParticleTypes.ITEM_SLIME, Fx.rel(s, 1.6, 0, 1.6), 30, 0.5, 0.25);
                    Fx.particlesAt(s, ParticleTypes.SPORE_BLOSSOM_AIR, Fx.rel(s, 1.6, 0, 1.6), 20, 0.8, 0.05);
                    if (t != null && s.distanceTo(t) < 11) {
                        Fx.particlesAt(s, ParticleTypes.ITEM_SLIME, t.position().add(0, 1, 0), 20, 0.4, 0.05);
                        Fx.poison(t, 6);              // 3 Infections shuffled in -> lingering poison
                        Fx.curse(t, Fx.Curse.SLIMED);
                    }
                }))
            .move(Move.of("lash", 42).intent(Intent.ATTACK, 4, 4).reach(3.2f)
                .sound(6, SoundEvents.SLIME_JUMP, 1f, 0.6f)
                .atAll(new int[]{12, 18, 24, 30}, (s, t) -> { Fx.meleeNow(s, 4, 3.6, 120, null); Fx.sound(s, SoundEvents.SLIME_ATTACK, 1f, 0.7f); }))
            .onDeath((s, src) -> {
                Fx.particles(s, ParticleTypes.ITEM_SLIME, 60, 1.2);
                Fx.sound(s, SoundEvents.SLIME_DEATH, 2f, 0.5f);
                for (int i = 0; i < 4; i++) {
                    double a = i * Math.PI / 2 + 0.4;
                    SpireMob w = Fx.summon(s, "s2_wriggler", Math.cos(a) * 1.6, Math.sin(a) * 1.6);
                    if (w != null) { w.setLeader(null); w.setVar(i % 2); w.stun(60); }   // Infested: stunned on their first turn
                }
            })
            .onClientTick(s -> { if (s.tickCount % 6 == 0) s.level().addParticle(ParticleTypes.ITEM_SLIME, s.getRandomX(0.8), s.getY() + 1.5 + s.getRandom().nextDouble(), s.getRandomZ(0.8), 0, 0, 0); });
    }
}
