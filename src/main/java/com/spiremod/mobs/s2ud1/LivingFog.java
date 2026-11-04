package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * StS2 Underdocks Living Fog (80 HP). Advanced Gas (8 + Smoggy) first, then alternates Bloat (5 + summons a Gas Bomb) / Super Gas Blast (8).
 * Smoggy (only 1 Skill per turn) is approximated with Nausea + Mining Fatigue.
 */
public final class LivingFog {
    private LivingFog() {}
    static final int FOG = 0x8a9a8a, FOG_D = 0x4c5a50, FOG_L = 0xc2d0c0, EYE = 0xd6ff70;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_living_fog");
        Part core = m.add("core").at(0, 10, 0);
        core.box(-9, -8, -7, 18, 16, 14).color(FOG).grad(FOG_L, FOG_D).noise(0.3f).bevel(0.8f);
        core.box(-7, 8, -5, 14, 5, 10).color(FOG_L).noise(0.3f).bevel(0.6f);
        core.box(-10.5f, -4, -4, 3, 8, 8).color(FOG).noise(0.3f).bevel(0.6f);
        core.box(7.5f, -4, -4, 3, 8, 8).color(FOG).noise(0.3f).bevel(0.6f);
        // sickly face in the cloud
        core.box(-6, 1, 6.6f, 5, 3, 1).color(0x1a2a20).flat().glow(Face.FRONT, 1, 0, 3, 2, EYE);
        core.box(1, 1, 6.6f, 5, 3, 1).color(0x1a2a20).flat().glow(Face.FRONT, 1, 0, 3, 2, EYE);
        Part mouth = core.add("mouth").at(0, -3, 6.6f);
        mouth.box(-4, -2, 0, 8, 4, 1).color(0x1a2a20).flat();
        mouth.box(-3, 0, 0.6f, 6, 1, 1).color(0x5a7a40).flat();
        // wispy tendrils (arms) and trailing mist
        for (int s = -1; s <= 1; s += 2) {
            Part arm = core.add(s < 0 ? "armR" : "armL").at(s * 10, 2, 0);
            arm.box(-2, -6, -2, 4, 6, 4).color(FOG).grad(FOG_L, FOG_D).noise(0.3f).bevel(0.6f);
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-3, -5, -3, 6, 5, 6).color(FOG_L).noise(0.3f).bevel(0.8f);
        }
        for (int i = 0; i < 6; i++) {
            Part w = m.add("wisp" + i).at(-6 + i * 2.4f, 0, (i % 2) * 4 - 2);
            w.box(-2, 0, -2, 4, 4 + (i % 3) * 2, 4).color(i % 2 == 0 ? FOG_D : FOG).noise(0.35f).bevel(0.6f);
        }
        for (int i = 0; i < 3; i++) core.add("puff" + i).at(-8 + i * 8, 9, -3 + i * 3).box(-3, 0, -3, 6, 4, 6).color(FOG_L).noise(0.3f).bevel(0.8f);
        Part bomb = core.add("bomb").at(0, 0, 10);                    // gas pocket that becomes the Gas Bomb during Bloat
        bomb.cbox(-3, 6, 6, 6).color(0xa6c85a).noise(0.2f).bevel(0.8f).glow(Face.FRONT, 2, 2, 2, 2, 0xd6ff70);
        m.proc((r, c) -> {
            for (int i = 0; i < 6; i++) r.pos("wisp" + i, 0, 1 + (float) Math.sin(c.age * 0.12f + i) * 1.5f, 0);
            for (int i = 0; i < 3; i++) { float g = 1 + 0.15f * (float) Math.sin(c.age * 0.1f + i * 2); r.scl("puff" + i, g, g, g); }
            r.show("bomb", "bloat".equals(c.move) && c.moveT > 12 && c.moveT < 26);
        });
        m.bob(1.5f, 0.07f);
        m.look("core", 30, 15);
        m.clip("idle", 50).loop()
            .scl("core", 0, 1, 1, 1).scl("core", 25, 1.05f, 0.96f, 1.05f).scl("core", 50, 1, 1, 1)
            .rot("armR", 0, 0, 0, 8).rot("armR", 25, 5, 0, 16).rot("armR", 50, 0, 0, 8)
            .rot("armL", 0, 0, 0, -8).rot("armL", 25, 5, 0, -16).rot("armL", 50, 0, 0, -8)
            .rot("mouth", 0, 0, 0, 0).rot("mouth", 25, 8, 0, 0).rot("mouth", 50, 0, 0, 0);
        m.clip("walk", 30).loop()
            .pos("core", 0, 0, 0, 0).pos("core", 15, 0, 1.5f, 0).pos("core", 30, 0, 0, 0)
            .rot("core", 0, 4, 0, 3).rot("core", 15, 4, 0, -3).rot("core", 30, 4, 0, 3);
        // ADVANCED GAS: inhale, then belch a cloud
        m.clip("advanced_gas", 40)
            .scl("core", 0, 1, 1, 1).scl("core", 14, 0.85f, 0.85f, 0.85f).scl("core", 18, 1.2f, 1.15f, 1.3f, Ease.OUT_BACK).scl("core", 40, 1, 1, 1)
            .rot("core", 0, 0, 0, 0).rot("core", 14, -14, 0, 0).rot("core", 18, 12, 0, 0, Ease.IN_CUBIC).rot("core", 40, 0, 0, 0)
            .rot("mouth", 0, 0, 0, 0).rot("mouth", 14, 0, 0, 0).rot("mouth", 18, 30, 0, 0, Ease.OUT_BACK).rot("mouth", 32, 20, 0, 0).rot("mouth", 40, 0, 0, 0)
            .scl("mouth", 0, 1, 1, 1).scl("mouth", 18, 1.5f, 2.2f, 1).scl("mouth", 32, 1.3f, 1.8f, 1).scl("mouth", 40, 1, 1, 1)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -50, 0, 50).rot("armR", 20, -30, 0, 30).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -50, 0, -50).rot("armL", 20, -30, 0, -30).rot("armL", 40, 0, 0, 0);
        // BLOAT: swell up hugely and push out a bomb
        m.clip("bloat", 40)
            .scl("core", 0, 1, 1, 1).scl("core", 12, 1.35f, 1.3f, 1.35f, Ease.OUT_QUAD).scl("core", 16, 0.9f, 0.9f, 0.9f, Ease.IN_CUBIC).scl("core", 24, 1.05f, 1, 1.05f).scl("core", 40, 1, 1, 1)
            .rot("core", 0, 0, 0, 0).rot("core", 12, -10, 0, 0).rot("core", 16, 10, 0, 0).rot("core", 40, 0, 0, 0)
            .rot("mouth", 0, 0, 0, 0).rot("mouth", 12, 25, 0, 0).rot("mouth", 16, 35, 0, 0, Ease.OUT_BACK).rot("mouth", 28, 15, 0, 0).rot("mouth", 40, 0, 0, 0)
            .pos("bomb", 12, 0, 0, -6).pos("bomb", 18, 0, 0, 0, Ease.OUT_BACK).pos("bomb", 26, 0, 1, 6)
            .scl("bomb", 12, 0.3f, 0.3f, 0.3f).scl("bomb", 18, 1, 1, 1, Ease.OUT_BACK).scl("bomb", 26, 1.2f, 1.2f, 1.2f)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -20, 0, 40).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -20, 0, -40).rot("armL", 40, 0, 0, 0);
        // SUPER GAS BLAST: both arms rear back, then slam a gust forward
        m.clip("super_gas_blast", 38)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 40, 0, 60).rot("armR", 15, -110, 0, -10, Ease.IN_CUBIC).rot("armR", 24, -100, 0, -10).rot("armR", 38, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 40, 0, -60).rot("armL", 15, -110, 0, 10, Ease.IN_CUBIC).rot("armL", 24, -100, 0, 10).rot("armL", 38, 0, 0, 0)
            .rot("core", 0, 0, 0, 0).rot("core", 10, -14, 0, 0).rot("core", 15, 18, 0, 0, Ease.IN_CUBIC).rot("core", 38, 0, 0, 0)
            .scl("core", 0, 1, 1, 1).scl("core", 10, 0.9f, 1, 0.9f).scl("core", 15, 1.15f, 1, 1.35f, Ease.OUT_BACK).scl("core", 38, 1, 1, 1)
            .rot("mouth", 0, 0, 0, 0).rot("mouth", 15, 40, 0, 0, Ease.OUT_BACK).rot("mouth", 30, 20, 0, 0).rot("mouth", 38, 0, 0, 0);
        U.hurt(m, "core");
        m.clip("death", 36)
            .scl("core", 0, 1, 1, 1).scl("core", 10, 1.3f, 1.2f, 1.3f).scl("core", 36, 0.2f, 0.1f, 0.2f, Ease.IN_QUAD)
            .pos("core", 0, 0, 0, 0).pos("core", 36, 0, -8, 0).rot("core", 0, 0, 0, 0).rot("core", 20, 0, 90, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 16, -60, 0, 60).rot("armL", 0, 0, 0, 0).rot("armL", 16, -60, 0, -60);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_living_fog", "Living Fog", "生ける霧")
            .game(2, 1).size(1.6f, 1.9f).hp(80).speed(0.16f).style(Style.GHOST).egg(0x8a9a8a, 0xd6ff70)
            .model(LivingFog::model)
            .spawn(4, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(Pattern.seq(1, "advanced_gas", "bloat", "super_gas_blast"))
            .onClientTick(s -> { if (s.tickCount % 3 == 0) s.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, s.getX() + (s.getRandom().nextDouble() - 0.5) * 1.6, s.getY() + 0.4 + s.getRandom().nextDouble() * 1.2, s.getZ() + (s.getRandom().nextDouble() - 0.5) * 1.6, 0, 0.01, 0); })
            .move(Move.of("advanced_gas", 40).intent(Intent.ATTACK_DEBUFF, 8, 1).reach(5).ranged(5)
                .sound(16, SoundEvents.GENERIC_EXTINGUISH_FIRE, 1.2f, 0.6f)
                .at(18, (s, t) -> { Fx.particles(s, ParticleTypes.LARGE_SMOKE, 30, 1.0);
                    Fx.hitTarget(8, v -> { v.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160)); v.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 160)); }).run(s, t); }))
            .move(Move.of("bloat", 40).intent(Intent.ATTACK_DEBUFF, 5, 1).reach(5).ranged(5)
                .sound(14, SoundEvents.PUFFER_FISH_BLOW_UP, 1.2f, 0.6f)
                .at(16, (s, t) -> { Fx.hitTarget(5).run(s, t); Fx.summon(s, "s2_gas_bomb", 1.4, 0.8); }))
            .move(Move.of("super_gas_blast", 38).intent(Intent.ATTACK, 8, 1).reach(5).ranged(5)
                .sound(14, SoundEvents.GENERIC_EXPLODE, 0.7f, 1.4f)
                .at(15, (s, t) -> { Fx.particles(s, ParticleTypes.CLOUD, 30, 1.2); Fx.hitTarget(8).run(s, t); }));
    }
}
