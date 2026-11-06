package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

/**
 * StS1 Act 4 final boss Corrupt Heart: 750 HP. Invincible 300 (damage taken is capped per cycle), Beat of Death 1 (each hit on it hurts the attacker).
 * AI: Debilitate (Vulnerable/Weak/Frail 2 + status cards) first; then each 3-move cycle = Blood Shots (2x12) and Echo (40) in random order, then Buff
 * (+2 Str and a rotating extra: Artifact 2, Beat of Death +1, Painful Stabs, +10 Str, +50 Str).
 * Look: a vast black-red heart pulsing on a nest of ribs, ringed with writhing aortas, a rune-lit core glowing through the muscle.
 */
public final class CorruptHeart {
    private CorruptHeart() {}

    static final int FLESH = 0x6aa8b4, FLESH_L = 0xa6dce2, FLESH_D = 0x3a6a7c, VEIN = 0xe8607a, GLOW = 0xff7090, CORE = 0xffc0d0, RIB = 0x3a2a58, RIB_L = 0x5a4a88;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("corrupt_heart");
        m.scale(1.5f);
        Part cage = m.add("cage").at(0, 0, 0);                                                          // pivot only (clips spin it)
        cage.cbox(0, 1, 1, 1).color(FLESH_D).flat();
        Part heart = m.add("heart").at(0, 22, 0);
        // two ventricle lobes + atria
        heart.box(-12, -8, -10, 14, 18, 20).color(FLESH).grad(FLESH_L, FLESH_D).bevel(0.5f).noise(0.1f);
        heart.box(-2, -8, -9, 14, 18, 18).color(FLESH).grad(FLESH_L, FLESH_D).bevel(0.5f).noise(0.1f);
        heart.box(-9, -14, -8, 18, 6, 16).color(FLESH_D).grad(FLESH, FLESH_D).bevel(0.4f).noise(0.1f);   // lower cone
        heart.box(-5, -20, -5, 10, 6, 10).color(FLESH_D).bevel(0.4f);
        heart.box(-2, -25, -2, 4, 5, 4).color(FLESH_D).bevel(0.4f);
        heart.box(-13, 8, -7, 10, 8, 14).color(FLESH_L).grad(FLESH_L, FLESH).bevel(0.4f).noise(0.1f);   // atria
        heart.box(3, 8, -7, 10, 8, 14).color(FLESH_L).grad(FLESH_L, FLESH).bevel(0.4f).noise(0.1f);
        // veins painted over the front, glowing wound-core under a chest crack
        heart.box(-6, -4, 9.6f, 12, 14, 1).color(0x8ac0c8).bevel(0.2f)
            .glow(Face.FRONT, 5, 1, 2, 12, GLOW).glow(Face.FRONT, 3, 4, 2, 1, GLOW).glow(Face.FRONT, 7, 7, 2, 1, GLOW).glow(Face.FRONT, 5, 11, 2, 2, CORE);
        heart.box(-11, 4, 10.1f, 1, 8, 1).color(VEIN).flat();
        heart.box(10, 2, 10.1f, 1, 10, 1).color(VEIN).flat();
        heart.box(-1, 11, 10.1f, 1, 6, 1).color(VEIN).flat();
        // aortas: two thick vessels arcing up and out
        for (int s = -1; s <= 1; s += 2) {
            Part ao = heart.add(s < 0 ? "aoR" : "aoL").at(s * 5, 14, 0).rot(0, 0, -s * 14);
            ao.cbox(0, 6, 10, 6).color(0x5a82b8).grad(0x7aa2d0, 0x3a5a90).bevel(0.3f).noise(0.08f);
            Part ao2 = ao.add(s < 0 ? "aoR2" : "aoL2").at(0, 10, 0).rot(0, 0, -s * 30);
            ao2.cbox(0, 5, 10, 5).color(0x4a6aa0).grad(0x7aa2d0, 0x3a4a80).bevel(0.3f);
            ao2.cbox(10, 7, 2, 7).color(RIB).flat();
        }
        // spiked horns of corruption on the shoulders of the muscle
        for (int s = -1; s <= 1; s += 2) for (int k = 0; k < 2; k++) {
            Part sp = heart.add((s < 0 ? "spR" : "spL") + k).at(s * (11 + k), -2 + k * 9, -3 + k * 4).rot(0, 0, -s * (55 + k * 10));
            sp.box(-1.5f, 0, -1.5f, 3, 6, 3).color(RIB).bevel(0.3f);
            sp.box(-1, 6, -1, 2, 2, 2).color(RIB_L).flat();
        }
        m.proc((rig, c) -> {
            float bpm = 0.28f + c.hpFrac * 0f + (c.phase > 0 ? 0.1f : 0f);
            float t = (c.age * bpm) % 6.28318f;
            float beat = (float) (Math.max(0, Math.sin(t)) * Math.max(0, Math.sin(t * 2 + 0.6))) * 0.06f;   // lub-dub
            rig.scl("heart", 1 + beat, 1 + beat * 1.2f, 1 + beat);
            rig.rot("aoR", 0, 0, (float) Math.sin(c.age * 0.1f) * 4); rig.rot("aoL", 0, 0, (float) Math.sin(c.age * 0.1f + 2) * -4);
            rig.pos("heart", 0, (float) Math.sin(c.age * 0.07f) * 1.2f, 0);
        });
        m.clip("idle", 60).loop().rot("heart", 0, 0, 0, 0).rot("heart", 30, 0, 3, 0).rot("heart", 60, 0, 0, 0);
        m.clip("walk", 20).loop().rot("heart", 0, 0, 0, 0).rot("heart", 10, 0, 2, 0).rot("heart", 20, 0, 0, 0);
        // DEBILITATE: swell and rear back, then clench and send the curse out in a pulse
        m.clip("debilitate", 46)
            .scl("heart", 0, 1, 1, 1).scl("heart", 14, 1.2f, 1.15f, 1.2f, Ease.OUT_CUBIC).scl("heart", 20, 0.88f, 0.9f, 0.88f, Ease.IN_CUBIC).scl("heart", 28, 1.12f, 1.1f, 1.12f).scl("heart", 46, 1, 1, 1)
            .rot("heart", 0, 0, 0, 0).rot("heart", 14, -14, 0, 0).rot("heart", 20, 10, 0, 0, Ease.IN_CUBIC).rot("heart", 46, 0, 0, 0)
            .rot("aoR", 0, 0, 0, 0).rot("aoR", 14, 0, 0, 25).rot("aoR", 46, 0, 0, 0)
            .rot("aoL", 0, 0, 0, 0).rot("aoL", 14, 0, 0, -25).rot("aoL", 46, 0, 0, 0);
        // BLOOD SHOTS: twelve rapid contractions, each firing
        {
            Clip c = m.clip("blood_shots", 58);
            c.scl("heart", 0, 1, 1, 1);
            for (int i = 0; i < 12; i++) {
                int t = 8 + i * 3;
                c.scl("heart", t, 1.1f, 1.08f, 1.1f).scl("heart", t + 2, 0.94f, 0.96f, 0.94f);
                c.rot("heart", t, (i % 2 == 0 ? -4 : 4), 0, 0);
            }
            c.scl("heart", 58, 1, 1, 1).rot("heart", 58, 0, 0, 0);
            c.rot("aoR", 0, 0, 0, 0).rot("aoR", 10, 0, 0, 20).rot("aoR", 46, 0, 0, 20).rot("aoR", 58, 0, 0, 0)
                .rot("aoL", 0, 0, 0, 0).rot("aoL", 10, 0, 0, -20).rot("aoL", 46, 0, 0, -20).rot("aoL", 58, 0, 0, 0);
        }
        // ECHO: collapse inward, hold, then explode out in a huge beat
        m.clip("echo", 56)
            .scl("heart", 0, 1, 1, 1).scl("heart", 16, 0.75f, 0.78f, 0.75f, Ease.OUT_CUBIC).scl("heart", 28, 0.72f, 0.75f, 0.72f).scl("heart", 32, 1.45f, 1.4f, 1.45f, Ease.IN_CUBIC).scl("heart", 40, 1.25f, 1.2f, 1.25f).scl("heart", 56, 1, 1, 1)
            .rot("heart", 0, 0, 0, 0).rot("heart", 16, 12, 0, 0).rot("heart", 32, -8, 0, 0, Ease.IN_CUBIC).rot("heart", 56, 0, 0, 0)
            .rot("aoR", 0, 0, 0, 0).rot("aoR", 16, 0, 0, -20).rot("aoR", 32, 0, 0, 40, Ease.IN_CUBIC).rot("aoR", 56, 0, 0, 0)
            .rot("aoL", 0, 0, 0, 0).rot("aoL", 16, 0, 0, 20).rot("aoL", 32, 0, 0, -40, Ease.IN_CUBIC).rot("aoL", 56, 0, 0, 0);
        // BUFF: slow deep inflation, spikes flare
        m.clip("buff", 50)
            .scl("heart", 0, 1, 1, 1).scl("heart", 24, 1.3f, 1.25f, 1.3f, Ease.OUT_BACK).scl("heart", 34, 1.25f, 1.2f, 1.25f).scl("heart", 50, 1, 1, 1)
            .rot("heart", 0, 0, 0, 0).rot("heart", 24, -10, 0, 0).rot("heart", 50, 0, 0, 0)
            .rot("aoR", 0, 0, 0, 0).rot("aoR", 24, 0, 0, 35, Ease.OUT_BACK).rot("aoR", 50, 0, 0, 0)
            .rot("aoL", 0, 0, 0, 0).rot("aoL", 24, 0, 0, -35, Ease.OUT_BACK).rot("aoL", 50, 0, 0, 0)
            .rot("cage", 0, 0, 0, 0).rot("cage", 24, 0, 25, 0).rot("cage", 50, 0, 0, 0);
        m.clip("hurt", 8).scl("heart", 0, 1, 1, 1).scl("heart", 2, 0.92f, 0.94f, 0.92f).scl("heart", 8, 1, 1, 1);
        m.clip("death", 60)
            .scl("heart", 0, 1, 1, 1).scl("heart", 10, 1.3f, 1.3f, 1.3f).scl("heart", 18, 0.8f, 0.8f, 0.8f).scl("heart", 26, 1.2f, 1.2f, 1.2f).scl("heart", 34, 0.6f, 0.6f, 0.6f).scl("heart", 60, 0.05f, 0.05f, 0.05f)
            .pos("heart", 0, 0, 0, 0).pos("heart", 60, 0, -14, 0)
            .rot("aoR", 0, 0, 0, 0).rot("aoR", 40, 0, 0, 80).rot("aoL", 0, 0, 0, 0).rot("aoL", 40, 0, 0, -80);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("corrupt_heart", "Corrupt Heart", "腐敗した心臓")
            .game(1, 4).size(2.6f, 3.4f).hp(750).speed(0.02f).kbResist(1.0f).fireImmune().style(Style.BEAST).egg(0x8c1a2a, 0xff5a3a)
            .model(CorruptHeart::model).boss().gold(40, 60)
            .onSpawn(s -> { s.setCount("beat", 1); })
            .onHurt((s, src, d) -> {
                // Invincible 300: at most 300 HP of damage per cycle
                int used = s.count("inv");
                float allowed = Math.max(0, 300 - used);
                if (d > allowed) { d = allowed; Fx.particles(s, ParticleTypes.CRIT, 6, 0.8); }
                s.setCount("inv", used + Math.round(d));
                // Beat of Death: every hit on the heart hurts the attacker
                if (src.getEntity() instanceof LivingEntity v && !(v instanceof SpireMob) && d > 0) {
                    Fx.sound(s, SoundEvents.NOTE_BLOCK_BASEDRUM.value(), 1.1f, 0.5f);
                    v.invulnerableTime = 0;
                    Fx.raw(s, v, s.count("beat") * 1.0f);
                }
                return d;
            })
            .onMoveEnd(s -> { if (s.lastMove().equals("buff")) s.setCount("inv", 0); })
            .pattern(s -> {
                if (s.getTurn() == 0) return "debilitate";
                if ((s.getTurn() - 1) % 3 == 2) return "buff";
                String l = s.lastMove();
                if (l.equals("blood_shots")) return "echo";
                if (l.equals("echo")) return "blood_shots";
                return s.rand().nextBoolean() ? "blood_shots" : "echo";
            })
            .move(Move.of("debilitate", 46).intent(Intent.STRONG_DEBUFF).ranged(20)
                .sound(14, SoundEvents.WARDEN_HEARTBEAT, 2f, 0.6f)
                .at(22, (s, t) -> {
                    Fx.sound(s, SoundEvents.WARDEN_ROAR, 1.2f, 0.7f);
                    Fx.ring(s, ParticleTypes.DAMAGE_INDICATOR, s.position().add(0, 1.2, 0), 3.0, 30);
                    for (LivingEntity v : Fx.foes(s, s.getBoundingBox().inflate(20))) {
                        Fx.vulnerable(v, 8); Fx.weak(v, 8); Fx.frail(v, 8);
                        Fx.curse(v, Fx.Curse.VOID); Fx.curse(v, Fx.Curse.DAZED); Fx.curse(v, Fx.Curse.BURN);
                    }
                }))
            .move(blood())
            .move(Move.of("echo", 56).intent(Intent.ATTACK, 40, 1).ranged(20)
                .sound(8, SoundEvents.WARDEN_HEARTBEAT, 2f, 0.5f)
                .at(32, (s, t) -> Fx.hitTarget(40, v -> { if (s.flags.contains("stabs")) Fx.curse(v, Fx.Curse.WOUND); }).run(s, t))
                .at(32, (s, t) -> { Fx.sound(s, SoundEvents.WARDEN_SONIC_BOOM, 1.4f, 0.6f); Fx.ring(s, ParticleTypes.EXPLOSION, s.position().add(0, 1.4, 0), 3.0, 14); }))
            .move(Move.of("buff", 50).intent(Intent.BUFF).noTarget()
                .sound(10, SoundEvents.WARDEN_HEARTBEAT, 2f, 0.4f)
                .at(24, (s, t) -> {
                    s.addStrength(2);
                    int n = s.inc("buffs") - 1;
                    switch (n) {
                        case 0 -> s.setCount("artifact", s.count("artifact") + 2);
                        case 1 -> s.setCount("beat", s.count("beat") + 1);
                        case 2 -> s.flags.add("stabs");
                        case 3 -> s.addStrength(10);
                        default -> s.addStrength(50);
                    }
                    Fx.sound(s, SoundEvents.WITHER_SPAWN, 0.9f, 0.5f);
                    Fx.ring(s, ParticleTypes.SOUL_FIRE_FLAME, s.position().add(0, 0.3, 0), 3.5, 40);
                    Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 12, 1.2);
                }))
            .onTick(S3::artifact);
    }

    static Move blood() {
        Move mv = Move.of("blood_shots", 58).intent(Intent.ATTACK, 2, 12).ranged(20).sound(6, SoundEvents.WARDEN_HEARTBEAT, 1.6f, 0.8f);
        for (int i = 0; i < 12; i++) {
            mv.at(8 + i * 3, (s, t) -> {
                if (t != null) t.invulnerableTime = 0;
                Fx.hitTarget(2, v -> { if (s.flags.contains("stabs") && s.rand().nextInt(4) == 0) Fx.curse(v, Fx.Curse.WOUND); }).run(s, t);
                Fx.sound(s, SoundEvents.PLAYER_ATTACK_SWEEP, 0.7f, 0.5f);
                Fx.particlesAt(s, ParticleTypes.DAMAGE_INDICATOR, Fx.rel(s, 1.6, (s.rand().nextDouble() - 0.5) * 1.6, 1.6), 3, 0.2, 0.05);
            });
        }
        return mv;
    }
}
