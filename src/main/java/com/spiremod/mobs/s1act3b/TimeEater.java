package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 boss Time Eater: 456 HP. Time Warp: every 12 cards the player plays ends their turn and gives it +2 Strength
 * (here: every 12 hits it takes, it gains +2 Str and slows its attacker).
 * Reverberate 7x3, Ripple (20 Block, Vulnerable 1, Weak 1), Head Slam 26 (+Slimed); once under half HP: Haste (cleanse, heal to half max HP).
 * AI: 45% Reverberate / 35% Head Slam / 20% Ripple, no repeats of Head Slam/Ripple, max 2 Reverberates.
 * Look: hulking brass-and-teal brute whose head is a giant clock face with turning hands, gear-studded shoulders and gauntlets.
 */
public final class TimeEater {
    private TimeEater() {}

    static final int GREEN = 0xa8dcc0, GREEN_D = 0x5ea088, TEAL = 0xa03aa0, TEAL_L = 0xd060c8, TEAL_D = 0x6a1a6c, BRASS = 0xc29a3a, BRASS_D = 0x8a6a22, CREAM = 0xe8dfc0, TICK = 0x3a2a14, RUST = 0x8a5a2a, GLOW = 0xffc040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("time_eater");
        m.scale(1.5f);
        // StS1 Time Eater: a hulking hunched purple robe, a pale green skull-like head with a trailing tentacle, and a big pocket watch dangling from one hand
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 5, 5, 0);
            leg.box(-2.5f, -5, -2.5f, 5, 5, 8).color(GREEN).grad(GREEN, GREEN_D).bevel(0.2f);          // pale feet peeking from the hem
        }
        Part body = m.add("body").at(0, 5, 0);
        body.box(-9, 0, -7, 18, 8, 14).color(TEAL).grad(TEAL_L, TEAL_D).bevel(0.4f).noise(0.1f);
        for (int i = 0; i < 6; i++) body.box(-9 + i * 3.3f, -2, -7.5f + (i % 2) * 14, 3, 3, 1).color(TEAL_D).flat();     // ragged hem
        Part torso = body.add("torso").at(0, 8, -1).rot(22, 0, 0);                                            // hunched forward
        torso.box(-8, 0, -6, 16, 10, 12).color(TEAL).grad(TEAL_L, TEAL).bevel(0.4f).noise(0.1f);
        torso.box(-7, 10, -5, 14, 7, 10).color(TEAL).grad(TEAL_L, TEAL).bevel(0.4f).noise(0.1f);
        torso.box(-5, 17, -4, 10, 5, 8).color(TEAL_D).grad(TEAL, TEAL_D).bevel(0.4f);                         // high hooded shoulders
        for (int i = 0; i < 4; i++) torso.box(-6 + i * 4, 3 + i * 2, 6.2f, 2, 5, 1).color(TEAL_D).flat();      // leaf patterns
        Part head = torso.add("head").at(0, 17, 5).rot(30, 0, 0);
        head.box(-3.5f, -3, 0, 7, 9, 8).color(GREEN).grad(0xd0f4e0, GREEN_D).bevel(0.3f).noise(0.06f);
        head.box(-3, -1, 7.6f, 6, 3, 1).color(0x142018).flat().glow(Face.FRONT, 0, 0, 1, 1, 0x80ffd0).glow(Face.FRONT, 5, 0, 1, 1, 0x80ffd0);   // dark eye sockets
        head.box(-1.5f, -5, 4, 3, 3, 4).color(GREEN_D).bevel(0.2f);                                           // jaw
        Part tent = head.add("tent").at(-3, -2, 3);
        tent.box(-1, -14, -1, 2, 14, 2).color(GREEN).grad(GREEN, GREEN_D).bevel(0.15f);                      // long tentacle
        tent.box(-0.5f, -20, -0.5f, 1, 6, 1).color(GREEN_D).flat();
        // pocket watch: hangs from the right hand; the proc spins its hands
        for (int s = -1; s <= 1; s += 2) {
            Part gear = torso.add(s < 0 ? "gearR" : "gearL").at(s * 8.5f, 14, 2);
            gear.box(-1.5f, -1.5f, -0.5f, 3, 3, 1).color(BRASS).bevel(0.2f);
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 8, 14, 3);
            arm.box(-2, -8, -2, 4, 8, 4).color(s < 0 ? GREEN : TEAL).grad(s < 0 ? GREEN : TEAL_L, s < 0 ? GREEN_D : TEAL_D).bevel(0.2f);
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -8, 0);
            fore.box(-1.5f, -9, -1.5f, 3, 9, 3).color(s < 0 ? GREEN : TEAL_D).bevel(0.2f);
            Part fist = fore.add(s < 0 ? "fistR" : "fistL").at(0, -9, 0);
            fist.box(-1.5f, -3, -1.5f, 3, 3, 3).color(GREEN_D).bevel(0.2f);
            if (s < 0) {                                                                                         // the watch on its chain
                fist.box(-0.5f, -9, -0.5f, 1, 6, 1).color(0xc8c8d0).flat();
                Part watch = fist.add("watch").at(0, -9, 0);
                watch.box(-5, -10, -1.5f, 10, 10, 3).color(0xe0e8e0).bevel(0.5f).noise(0.04f);
                watch.box(-5.5f, -10.5f, -2, 11, 11, 1).color(0xa8c8c0).bevel(0.4f);
                watch.box(-4, -9, 1.4f, 8, 8, 1).color(CREAM).flat().paint(Face.FRONT, 3, 0, 2, 1, TICK).paint(Face.FRONT, 3, 7, 2, 1, TICK).paint(Face.FRONT, 0, 3, 1, 2, TICK).paint(Face.FRONT, 7, 3, 1, 2, TICK);
                watch.box(-0.7f, 0, -0.5f, 2, 2, 1).color(0xc8c8d0).flat();
                Part hMin = watch.add("hMin").at(0, -5, 2.6f); hMin.box(-0.5f, -0.5f, 0, 1, 4, 1).color(0x201408).flat();
                Part hHr = watch.add("hHr").at(0, -5, 2.4f); hHr.box(-0.8f, -0.5f, 0, 2, 3, 1).color(0x201408).flat();
            }
        }
        m.look("head", 40, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 30f, 20f);
        m.proc((rig, c) -> {
            float fast = (c.move != null && c.move.equals("haste")) ? 40f : 1f;
            rig.rot("hMin", 0, 0, -c.age * 6f * fast); rig.rot("hHr", 0, 0, -c.age * 0.5f * fast);
            rig.rot("gearR", 0, 0, c.age * 2f * fast); rig.rot("gearL", 0, 0, -c.age * 2f * fast);
        });
        m.clip("idle", 60).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 30, -1, 0, 0).rot("torso", 60, 2, 0, 0)
            .rot("armR", 0, 3, 0, 6).rot("armR", 30, 6, 0, 8).rot("armR", 60, 3, 0, 6)
            .rot("armL", 0, 3, 0, -6).rot("armL", 30, 6, 0, -8).rot("armL", 60, 3, 0, -6)
            .rot("head", 0, 0, 0, 0).rot("head", 30, 0, 5, 2).rot("head", 60, 0, 0, 0);
        // REVERBERATE: hammer both fists down three times, clock hands shuddering
        m.clip("reverberate", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -15, 0, 0).rot("torso", 13, 25, 0, 0, Ease.IN_CUBIC).rot("torso", 19, -10, 0, 0).rot("torso", 23, 25, 0, 0, Ease.IN_CUBIC).rot("torso", 29, -10, 0, 0).rot("torso", 33, 25, 0, 0, Ease.IN_CUBIC).rot("torso", 50, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -150, 0, 20, Ease.OUT_CUBIC).rot("armR", 13, -30, 0, 5, Ease.IN_CUBIC).rot("armR", 19, -140, 0, 15).rot("armR", 23, -30, 0, 5, Ease.IN_CUBIC).rot("armR", 29, -145, 0, 15).rot("armR", 33, -25, 0, 5, Ease.IN_CUBIC).rot("armR", 50, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, -150, 0, -20, Ease.OUT_CUBIC).rot("armL", 13, -30, 0, -5, Ease.IN_CUBIC).rot("armL", 19, -140, 0, -15).rot("armL", 23, -30, 0, -5, Ease.IN_CUBIC).rot("armL", 29, -145, 0, -15).rot("armL", 33, -25, 0, -5, Ease.IN_CUBIC).rot("armL", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 9, -15, 0, 0).rot("head", 13, 15, 0, 0).rot("head", 29, -10, 0, 0).rot("head", 33, 15, 0, 0).rot("head", 50, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 13, 0, -1, 3).pos("body", 23, 0, -1, 3).pos("body", 33, 0, -1, 4).pos("body", 50, 0, 0, 0);
        // RIPPLE: arms sweep out in a slow circle, body glows and squares up
        m.clip("ripple", 46)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -80, 0, 80, Ease.OUT_CUBIC).rot("armR", 24, -90, 0, 20).rot("armR", 46, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -80, 0, -80, Ease.OUT_CUBIC).rot("armL", 24, -90, 0, -20).rot("armL", 46, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -8, 0, 0).rot("torso", 24, 10, 0, 0).rot("torso", 46, 0, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 12, 0.95f, 1.05f, 0.95f).scl("torso", 24, 1.1f, 0.95f, 1.1f, Ease.OUT_BACK).scl("torso", 46, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -10, 0, 0).rot("head", 24, 0, 0, 15).rot("head", 46, 0, 0, 0);
        // HEAD SLAM: rear the clock-head high, then crash it forward onto the target
        m.clip("head_slam", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -30, 0, 0, Ease.OUT_CUBIC).rot("torso", 20, 45, 0, 0, Ease.IN_CUBIC).rot("torso", 30, 40, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -30, 0, 0).rot("head", 20, 35, 0, 0, Ease.IN_CUBIC).rot("head", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, 40, 0, 40).rot("armR", 20, -40, 0, 20).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, 40, 0, -40).rot("armL", 20, -40, 0, -20).rot("armL", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -2, -4).pos("body", 20, 0, -3, 6, Ease.IN_CUBIC).pos("body", 44, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 20, -25, 0, 0).rot("legR", 44, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 20, 15, 0, 0).rot("legL", 44, 0, 0, 0);
        // HASTE: crouch and shudder as the clock spins madly, then burst upright
        m.clip("haste", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 20, 0, 0).rot("torso", 12, 20, 0, 4).rot("torso", 16, 20, 0, -4).rot("torso", 20, 20, 0, 4).rot("torso", 24, 20, 0, -4).rot("torso", 34, -18, 0, 0, Ease.OUT_BACK).rot("torso", 50, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 30, 0, 30).rot("armR", 34, -165, 0, 40, Ease.OUT_BACK).rot("armR", 50, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 30, 0, -30).rot("armL", 34, -165, 0, -40, Ease.OUT_BACK).rot("armL", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 25, 0, 0).rot("head", 34, -35, 0, 0, Ease.OUT_BACK).rot("head", 50, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -3, 0).pos("body", 34, 0, 2, 0, Ease.OUT_BACK).pos("body", 50, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -10, 0, 3).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -14, 6, 6).rot("head", 10, 0, 0, 0);
        m.clip("death", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 16, -15, 0, 0).rot("torso", 50, 35, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 34, 0, -8, 0, Ease.OUT_BOUNCE).pos("body", 50, 0, -9, 2)
            .rot("armR", 0, 0, 0, 0).rot("armR", 16, -110, 0, 40).rot("armR", 50, 20, 0, 60)
            .rot("armL", 0, 0, 0, 0).rot("armL", 16, -110, 0, -40).rot("armL", 50, 20, 0, -60)
            .rot("head", 0, 0, 0, 0).rot("head", 16, -30, 0, 0).rot("head", 50, 30, 12, 8)
            .rot("legR", 0, 0, 0, 0).rot("legR", 30, -70, 0, 8).rot("legR", 50, -80, 0, 8)
            .rot("legL", 0, 0, 0, 0).rot("legL", 30, -60, 0, -8).rot("legL", 50, -80, 0, -8);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("time_eater", "Time Eater", "タイムイーター")
            .game(1, 3).size(2.0f, 3.8f).hp(456).speed(0.22f).kbResist(0.9f).style(Style.METAL).egg(0x2f4a55, 0xc29a3a)
            .model(TimeEater::model).boss()
            .onHurt((s, src, d) -> {
                // Time Warp: 12 hits taken -> it gains 2 Strength and freezes the culprit briefly
                if (s.inc("warp") >= 12) {
                    s.setCount("warp", 0);
                    s.addStrength(2);
                    Fx.sound(s, SoundEvents.BELL_BLOCK, 1.5f, 0.5f);
                    Fx.ring(s, ParticleTypes.END_ROD, s.position().add(0, 0.3, 0), 3.0, 40);
                    if (src.getEntity() instanceof net.minecraft.world.entity.LivingEntity v) Fx.slow(v, 3, 3);
                }
                return d;
            })
            .pattern(s -> {
                if (s.getHealth() < s.getMaxHealth() / 2 && s.count("haste") == 0) return "haste";
                int n = s.rand().nextInt(100);
                String last = s.lastMove();
                String pick = n < 45 ? "reverberate" : n < 80 ? "head_slam" : "ripple";
                if (pick.equals(last) && !pick.equals("reverberate")) pick = "reverberate";
                if (pick.equals("reverberate") && s.repeatCount("reverberate") >= 2) pick = s.rand().nextBoolean() ? "head_slam" : "ripple";
                return pick;
            })
            .move(Move.of("reverberate", 50).intent(Intent.ATTACK, 7, 3).reach(3.6f)
                .sound(8, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.5f)
                .at(13, S3.multi(Fx.aoe(7, 3.4))).at(23, S3.multi(Fx.aoe(7, 3.4))).at(33, S3.multi(Fx.aoe(7, 3.4)))
                .at(13, Fx.sfx(SoundEvents.ANVIL_LAND, 0.9f, 0.6f)).at(23, Fx.sfx(SoundEvents.ANVIL_LAND, 0.9f, 0.55f)).at(33, Fx.sfx(SoundEvents.ANVIL_LAND, 1f, 0.5f)))
            .move(Move.of("ripple", 46).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(8, SoundEvents.BELL_RESONATE, 1.4f, 0.6f)
                .at(24, (s, t) -> {
                    s.addBlock(20);
                    Fx.ring(s, ParticleTypes.END_ROD, s.position().add(0, 0.3, 0), 3.5, 36);
                    for (net.minecraft.world.entity.LivingEntity v : Fx.foes(s, s.getBoundingBox().inflate(5.0))) { Fx.vulnerable(v, 4); Fx.weak(v, 4); }
                }))
            .move(Move.of("head_slam", 44).intent(Intent.ATTACK_DEBUFF, 26, 1).reach(3.4f)
                .sound(10, SoundEvents.IRON_GOLEM_ATTACK, 1.3f, 0.5f)
                .at(21, Fx.melee(26, 3.8, 100, v -> Fx.curse(v, Fx.Curse.SLIMED)))
                .at(21, (s, t) -> { Fx.sound(s, SoundEvents.ANVIL_LAND, 1.2f, 0.5f); Fx.particlesAt(s, ParticleTypes.EXPLOSION, Fx.rel(s, 2.2, 0, 0.4), 2, 0.3, 0); }))
            .move(Move.of("haste", 50).intent(Intent.BUFF).noTarget()
                .sound(6, SoundEvents.BELL_BLOCK, 1.5f, 0.4f)
                .at(34, (s, t) -> {
                    s.setCount("haste", 1);
                    S3.cleanse(s);
                    float half = s.getMaxHealth() / 2f;
                    if (s.getHealth() < half) s.setHealth(half);
                    Fx.sound(s, SoundEvents.ENDER_DRAGON_GROWL, 1.4f, 0.7f);
                    Fx.ring(s, ParticleTypes.END_ROD, s.position().add(0, 0.3, 0), 3.0, 40);
                    Fx.particles(s, ParticleTypes.HEART, 8, 0.8);
                }));
    }
}
