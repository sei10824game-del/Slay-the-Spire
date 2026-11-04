package com.spiremod.mobs.s2og2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth boss Vantom (173 HP). Ink phantom with Slippery 9 (next 9 HP-loss events cost only 1 HP).
 * Cycle: Ink Blot 7 -> Inky Lance 6x2 -> Dismember 27 + 3 Wounds -> Prepare (+2 Strength).
 */
public final class Vantom {
    private Vantom() {}

    static final int INK = 0x17122b, INK_L = 0x2f2658, INK_D = 0x0b0817, MASK = 0xe9e6f2, MASK_D = 0xb9b4cc, EYE = 0xa8f0ff, VIOLET = 0x6a4fd0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_vantom").scale(1.1f);
        Part body = m.add("body").at(0, 22, 0);
        body.box(-8, 0, -5, 16, 5, 10).color(INK_D).grad(INK, INK_D).bevel(0.15f).noise(0.12f);     // lower torso melts away
        Part torso = body.add("torso").at(0, 5, 0);
        torso.box(-9, 0, -6, 18, 11, 12).color(INK).grad(INK_L, INK).bevel(0.12f).noise(0.14f)
            .glow(Face.FRONT, 8, 4, 2, 2, VIOLET);                                                   // ink core
        torso.box(-10, 8, -7, 20, 4, 14).color(INK_L).grad(0x3c3070, INK_L).inflate(0.2f).noise(0.2f); // heavy shoulders
        // ink dribbles hanging below
        for (int i = 0; i < 6; i++) body.box(-7 + i * 2.7f, -4 - (i % 3) * 2, -3 + (i % 2) * 4, 2, 5 + (i % 3) * 2, 2).color(INK_D).noise(0.2f);
        // ---- head with pale mask face
        Part head = torso.add("head").at(0, 12, 0);
        head.box(-5.5f, 0, -5, 11, 11, 10).color(INK).grad(INK_L, INK).bevel(0.12f).noise(0.12f);
        head.box(-4.5f, 1, 5, 9, 9, 1).color(MASK).grad(MASK, MASK_D).bevel(0.2f)
            .glow(Face.FRONT, 1, 3, 2, 2, EYE).glow(Face.FRONT, 6, 3, 2, 2, EYE)
            .paint(Face.FRONT, 3, 3, 3, 1, MASK_D).paint(Face.FRONT, 2, 6, 5, 1, 0x2a2440).paint(Face.FRONT, 3, 7, 3, 1, 0x2a2440);
        for (int s = -1; s <= 1; s += 2) {
            Part horn = head.add(s < 0 ? "hornR" : "hornL").at(s * 3.5f, 10, 0).rot(-10, 0, s * -22);
            horn.box(-1, 0, -1, 2, 7, 2).color(INK_L).glow(Face.TOP, 0, 0, 2, 2, VIOLET);
            Part hornTip = horn.add(s < 0 ? "hornTR" : "hornTL").at(0, 7, 0).rot(0, 0, s * 25);
            hornTip.box(-0.5f, 0, -0.5f, 1, 5, 1).color(VIOLET).flat();
        }
        // ---- arms: segmented ink limbs ending in talons
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part last = Bits.chain(torso, "arm" + n, s * 11, 9, 0, 0, 0, s * 12, 3, 8, 5, INK, -1, -14, 0, s * -4, -1);
            last.box(-2.5f, -10, -2, 5, 3, 4).color(INK_L).bevel(0.15f);                              // palm
            for (int f = -1; f <= 1; f++) last.box(f * 1.8f - 0.4f, -17, -0.5f, 1, 7, 1).color(MASK).flat();   // pale talons
        }
        // ---- trailing tail (ghostly, tapering) coming off the lower back
        Bits.chain(body, "tail", 0, 2, -5, -125, 0, 0, 5, 7, 7, INK_D, -1, 14, 0, 0, 1);
        m.look("head", 45, 25);
        m.proc((rig, ctx) -> {
            float t = ctx.age * 0.12f;
            Bits.wiggle(rig, "tail", 5, t, 6f, 0);
            rig.pos("body", 0, (float) Math.sin(t * 0.7f) * 1.6f, 0);
        });
        m.clip("idle", 50).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 25, -3, 3, 0).rot("torso", 50, 2, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 25, 5, -6, 0).rot("head", 50, 0, 0, 0)
            .rot("armR0", 0, 6, 0, 0).rot("armR0", 25, 14, 0, 6).rot("armR0", 50, 6, 0, 0)
            .rot("armL0", 0, 6, 0, 0).rot("armL0", 25, 14, 0, -6).rot("armL0", 50, 6, 0, 0);
        // INK BLOT: lean back, scoop with right arm, fling the blob
        m.clip("ink_blot", 30)
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -14, -22, 0).rot("torso", 14, 18, 28, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .rot("armR0", 0, 0, 0, 0).rot("armR0", 9, 30, 0, 45).rot("armR0", 14, -110, 20, -10, Ease.IN_CUBIC).rot("armR0", 20, -100, 20, -10).rot("armR0", 30, 0, 0, 0)
            .rot("armR1", 0, 0, 0, 0).rot("armR1", 14, -35, 0, 0, Ease.IN_CUBIC).rot("armR1", 30, 0, 0, 0)
            .rot("armL0", 0, 0, 0, 0).rot("armL0", 9, -20, 0, -30).rot("armL0", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 9, -10, -15, 0).rot("head", 14, 8, 20, 0).rot("head", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, -1, -3).pos("body", 14, 0, 0, 4, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0);
        // INKY LANCE: two alternating lance thrusts
        m.clip("inky_lance", 34)
            .rot("torso", 0, 0, 0, 0).rot("torso", 6, -8, -20, 0).rot("torso", 10, 12, 22, 0, Ease.IN_CUBIC).rot("torso", 18, -6, 20, 0).rot("torso", 22, 12, -22, 0, Ease.IN_CUBIC).rot("torso", 34, 0, 0, 0)
            .rot("armR0", 0, 0, 0, 0).rot("armR0", 6, 40, 0, 30).rot("armR0", 10, -95, 10, 0, Ease.IN_CUBIC).rot("armR0", 16, -90, 10, 0).rot("armR0", 22, 10, 0, 20).rot("armR0", 34, 0, 0, 0)
            .rot("armL0", 0, 0, 0, 0).rot("armL0", 14, 40, 0, -30).rot("armL0", 22, -95, -10, 0, Ease.IN_CUBIC).rot("armL0", 28, -90, -10, 0).rot("armL0", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, 5, Ease.IN_CUBIC).pos("body", 16, 0, 0, 1).pos("body", 22, 0, 0, 5, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 10, 0, 0).rot("head", 22, 10, 0, 0).rot("head", 34, 0, 0, 0);
        // DISMEMBER: rise, both arms high with the whole body glowing, SLAM down
        m.clip("dismember", 48)
            .pos("body", 0, 0, 0, 0).pos("body", 22, 0, 12, -3).pos("body", 30, 0, 12, -3).pos("body", 33, 0, -3, 7, Ease.IN_CUBIC).pos("body", 48, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 22, -22, 0, 0).rot("torso", 30, -24, 0, 0).rot("torso", 33, 34, 0, 0, Ease.IN_CUBIC).rot("torso", 48, 0, 0, 0)
            .rot("armR0", 0, 0, 0, 0).rot("armR0", 22, -170, 0, 35, Ease.OUT_BACK).rot("armR0", 30, -172, 0, 38).rot("armR0", 33, -40, 0, 8, Ease.IN_CUBIC).rot("armR0", 48, 0, 0, 0)
            .rot("armL0", 0, 0, 0, 0).rot("armL0", 22, -170, 0, -35, Ease.OUT_BACK).rot("armL0", 30, -172, 0, -38).rot("armL0", 33, -40, 0, -8, Ease.IN_CUBIC).rot("armL0", 48, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 22, -30, 0, 0).rot("head", 33, 25, 0, 0, Ease.IN_CUBIC).rot("head", 48, 0, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 22, 1.12f, 1.1f, 1.12f).scl("torso", 33, 1.0f, 0.9f, 1.0f).scl("torso", 48, 1, 1, 1);
        // PREPARE: coil in on itself, pulse, burst
        m.clip("prepare", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, 25, 0, 0).rot("torso", 24, -12, 0, 0, Ease.OUT_BACK).rot("torso", 36, 0, 0, 0)
            .rot("armR0", 0, 0, 0, 0).rot("armR0", 12, -50, 0, -60).rot("armR0", 24, 10, 0, 70, Ease.OUT_BACK).rot("armR0", 36, 0, 0, 0)
            .rot("armL0", 0, 0, 0, 0).rot("armL0", 12, -50, 0, 60).rot("armL0", 24, 10, 0, -70, Ease.OUT_BACK).rot("armL0", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 20, 0, 0).rot("head", 24, -30, 0, 0, Ease.OUT_BACK).rot("head", 36, 0, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 12, 0.8f, 0.85f, 0.8f).scl("torso", 24, 1.2f, 1.18f, 1.2f, Ease.OUT_BACK).scl("torso", 36, 1, 1, 1)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -3, 0).pos("body", 24, 0, 4, 0, Ease.OUT_BACK).pos("body", 36, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -16, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -22, 10, 0).rot("head", 10, 0, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 3, 1.1f, 0.9f, 1.1f).scl("torso", 10, 1, 1, 1);
        // DIE: bursts into a puddle of ink
        m.clip("death", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -20, 0, 0).rot("torso", 36, 25, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 10, 1.25f, 1.1f, 1.25f, Ease.OUT_BACK).scl("torso", 36, 1.5f, 0.12f, 1.5f, Ease.IN_QUAD)
            .scl("head", 0, 1, 1, 1).scl("head", 36, 1.3f, 0.1f, 1.3f, Ease.IN_QUAD)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 4, 0).pos("body", 36, 0, -21, 0, Ease.IN_QUAD)
            .rot("armR0", 0, 0, 0, 0).rot("armR0", 12, -150, 0, 50).rot("armR0", 36, 20, 0, 80)
            .rot("armL0", 0, 0, 0, 0).rot("armL0", 12, -150, 0, -50).rot("armL0", 36, 20, 0, -80)
            .scl("body", 0, 1, 1, 1).scl("body", 36, 1.4f, 0.2f, 1.4f, Ease.IN_QUAD);
        return m;
    }

    public static MobDef def() {
        ProjKind.orb("s2_vantom_ink", 0x2a1d52, 4).trail(ParticleTypes.SQUID_INK);
        return MobDef.of("s2_vantom", "Vantom", "ヴァントム")
            .game(2, 1).boss().size(1.9f, 3.4f).hp(173).speed(0.2f).style(Style.GHOST).egg(0x17122b, 0xa8f0ff).kbResist(0.9f)
            .model(Vantom::model)
            .pattern(Pattern.cycle("ink_blot", "inky_lance", "dismember", "prepare"))
            .move(Move.of("ink_blot", 30).intent(Intent.ATTACK, 7, 1).ranged(12)
                .sound(10, SoundEvents.SQUID_SQUIRT, 1.4f, 0.7f)
                .at(14, Fx.shoot("s2_vantom_ink", 7, 1.1f)))
            .move(Move.of("inky_lance", 34).intent(Intent.ATTACK, 6, 2).reach(4.2f)
                .sound(8, SoundEvents.SQUID_SQUIRT, 1f, 1.5f)
                .at(10, (s, t) -> { Fx.meleeNow(s, 6, 4.4, 50, null); Fx.particles(s, ParticleTypes.SQUID_INK, 12, 0.6); })
                .at(22, (s, t) -> { Fx.meleeNow(s, 6, 4.4, 50, null); Fx.particles(s, ParticleTypes.SQUID_INK, 12, 0.6); }))
            .move(Move.of("dismember", 48).intent(Intent.ATTACK_DEBUFF, 27, 1).reach(4f)
                .sound(18, SoundEvents.WARDEN_SONIC_CHARGE, 1.4f, 0.8f)
                .at(33, (s, t) -> {
                    Fx.aoeAt(s, Fx.rel(s, 2.2, 0, 0), 27, 3.2, v -> { Fx.curse(v, Fx.Curse.WOUND); Fx.curse(v, Fx.Curse.WOUND); Fx.curse(v, Fx.Curse.WOUND); });
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 1.2f, 0.7f); Fx.particlesAt(s, ParticleTypes.SQUID_INK, Fx.rel(s, 2.2, 0, 0.3), 70, 1.5, 0.15);
                }))
            .move(Move.of("prepare", 36).intent(Intent.BUFF).noTarget()
                .at(24, (s, t) -> { s.addStrength(2); Fx.sound(s, SoundEvents.WARDEN_AGITATED, 1.2f, 0.7f); Fx.particles(s, ParticleTypes.SQUID_INK, 40, 1.0); }))
            .onHurt((s, src, dmg) -> {                                       // Slippery 9: the next 9 hits only cost 1 HP
                if (s.count("slip") < 9 && dmg > 0) { s.inc("slip"); return Math.min(dmg, 1f); }
                return dmg;
            })
            .onClientTick(s -> { if (s.tickCount % 5 == 0) s.level().addParticle(ParticleTypes.SQUID_INK, s.getRandomX(0.7), s.getY() + 1.2 + s.getRandom().nextDouble() * 1.5, s.getRandomZ(0.7), 0, -0.02, 0); });
    }
}
