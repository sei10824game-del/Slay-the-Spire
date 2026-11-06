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

    static final int TEAL = 0xa03aa0, TEAL_L = 0xd060c8, TEAL_D = 0x6a1a6c, BRASS = 0xc29a3a, BRASS_D = 0x8a6a22, CREAM = 0xe8dfc0, TICK = 0x3a2a14, RUST = 0x8a5a2a, GLOW = 0xffc040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("time_eater");
        m.scale(1.7f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 4.5f, 11, 0);
            leg.box(-2.5f, -11, -2.5f, 5, 11, 5).color(TEAL).grad(TEAL_L, TEAL_D).bevel(0.2f);
            leg.box(-3, -11, -3.5f, 6, 3, 7).color(BRASS).bevel(0.25f).noise(0.05f);
        }
        Part body = m.add("body").at(0, 11, 0);
        body.cbox(0, 14, 5, 9).color(TEAL_D).bevel(0.2f);
        body.cbox(-1, 16, 3, 10).color(BRASS_D).flat();                                        // belt
        Part torso = body.add("torso").at(0, 5, 0);
        torso.cbox(0, 16, 14, 10).color(TEAL).grad(TEAL_L, TEAL_D).bevel(0.25f).noise(0.06f);
        torso.cbox(9, 18, 4, 11).color(TEAL_D).bevel(0.3f);                                    // yoke
        torso.box(-2, 3, 5.1f, 4, 8, 1).color(BRASS).flat();                                    // chest plate
        torso.box(-3, 6, 5.1f, 6, 2, 1).color(BRASS_D).flat();
        torso.box(-1, 11, 5.1f, 2, 2, 1).color(0x120a04).glow(Face.FRONT, 0, 0, 2, 2, GLOW);
        Part head = torso.add("head").at(0, 15, 0);
        // the clock face (octagon from three overlapping slabs), centred on the pivot
        head.box(-9, -6, -3, 18, 12, 6).color(BRASS).bevel(0.3f).noise(0.05f);
        head.box(-6, -9, -3, 12, 18, 6).color(BRASS).bevel(0.3f).noise(0.05f);
        head.box(-8, -8, -3, 16, 16, 5).color(BRASS_D).bevel(0.3f);
        head.box(-7, -7, 2.2f, 14, 14, 1).color(CREAM).flat().noise(0.04f)
            .paint(Face.FRONT, 6, 0, 2, 2, TICK).paint(Face.FRONT, 6, 12, 2, 2, TICK).paint(Face.FRONT, 0, 6, 2, 2, TICK).paint(Face.FRONT, 12, 6, 2, 2, TICK)
            .paint(Face.FRONT, 2, 2, 1, 1, TICK).paint(Face.FRONT, 11, 2, 1, 1, TICK).paint(Face.FRONT, 2, 11, 1, 1, TICK).paint(Face.FRONT, 11, 11, 1, 1, TICK);
        head.box(-1, -1, 3.2f, 2, 2, 1).color(GLOW).flat();
        Part hMin = head.add("hMin").at(0, 0, 3.4f);
        hMin.box(-0.5f, -1, 0, 1, 7, 1).color(0x201408).flat();
        Part hHr = head.add("hHr").at(0, 0, 3.1f);
        hHr.box(-1, -1, 0, 2, 4, 1).color(0x201408).flat();
        head.box(-3, 9, -1, 6, 2, 2).color(BRASS_D).bevel(0.2f);                               // crown bell
        head.box(-1, 11, -1, 2, 2, 2).color(BRASS).flat();
        // shoulder gears + arms
        for (int s = -1; s <= 1; s += 2) {
            Part gear = torso.add(s < 0 ? "gearR" : "gearL").at(s * 11, 12, 0);
            gear.box(-5, -5, -1.5f, 10, 10, 3).color(BRASS).bevel(0.3f).noise(0.05f);
            gear.box(-2, -7, -1.5f, 4, 14, 3).color(BRASS_D).bevel(0.2f);
            gear.box(-7, -2, -1.5f, 14, 4, 3).color(BRASS_D).bevel(0.2f);
            gear.box(-1.5f, -1.5f, 1.5f, 3, 3, 1).color(GLOW).flat();
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 10, 12, 0);
            arm.box(-3, -8, -3, 6, 8, 6).color(TEAL).grad(TEAL_L, TEAL_D).bevel(0.2f);
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -8, 0);
            fore.box(-3.5f, -8, -3.5f, 7, 8, 7).color(TEAL_D).bevel(0.2f);
            fore.box(-4, -9, -4, 8, 3, 8).color(BRASS).bevel(0.25f).noise(0.05f);
            Part fist = fore.add(s < 0 ? "fistR" : "fistL").at(0, -8, 0);
            fist.box(-3.5f, -6, -3.5f, 7, 6, 7).color(BRASS).bevel(0.3f).noise(0.06f);
            fist.box(-3.5f, -3, 3.5f, 7, 1, 1).color(BRASS_D).flat();
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
