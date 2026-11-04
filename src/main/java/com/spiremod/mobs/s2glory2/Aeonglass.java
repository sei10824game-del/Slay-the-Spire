package com.spiremod.mobs.s2glory2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Glory boss: Aeonglass (512 HP, Artifact 3, Withering Presence: every 6 cards played a Wither is added to your hand).
 * Cycle: Ebb (22 dmg + 33 Block) -> Eye Lasers (11 x2) -> Increasing Intensity (+2+X Str, upgrades the Withers; X = uses so far).
 * MC translation: Withering Presence = every 6 s all foes nearby are Withered (Fx.curse DRAIN, longer as X grows).
 */
public final class Aeonglass {
    private Aeonglass() {}

    static final String ID = "s2_aeonglass";
    static final int GOLD = 0xc8a050, GOLD_D = 0x6e5424, GLASS = 0xa8d8e8, SAND = 0xe8b860, SAND_D = 0xb07a30, EYE_W = 0xf4f0e4, IRIS = 0xa060ff, STONE = 0x3a3446;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec(ID).scale(2.0f);
        Part all = m.add("all").at(0, 0, 0);
        // base + feet
        all.box(-11, 0, -11, 22, 3, 22).color(GOLD).grad(GOLD, GOLD_D).bevel(0.2f).noise(0.06f);
        all.box(-9, 3, -9, 18, 1, 18).color(STONE).bevel(0.2f);
        for (int sx = -1; sx <= 1; sx += 2) for (int sz = -1; sz <= 1; sz += 2) {   // clawed feet
            all.box(sx * 10 - 2, -2, sz * 10 - 2, 4, 2, 4).color(GOLD_D).bevel(0.2f);
            Part p = all.add("pillar" + (sx + 1) + (sz + 1)).at(sx * 8f, 3, sz * 8f);
            p.box(-1, 0, -1, 2, 42, 2).grad(GOLD, GOLD_D).bevel(0.15f).noise(0.05f);
            p.box(-1.5f, 20, -1.5f, 3, 3, 3).color(GOLD).flat();
            p.box(-1.5f, 41, -1.5f, 3, 2, 3).color(GOLD).flat();
        }
        // lower bulb (sand-filled; glass rims between layers)
        int[] w = {16, 15, 13, 10, 7, 4};
        Part low = all.add("low").at(0, 4, 0);
        for (int i = 0; i < w.length; i++) {
            G.fire(low.box(-w[i] / 2f, i * 3, -w[i] / 2f, w[i], 3, w[i]), SAND).grad(SAND, SAND_D);
            low.box(-w[i] / 2f - 0.4f, i * 3 + 2.6f, -w[i] / 2f - 0.4f, w[i], 1, w[i]).inflate(0.4f).color(GLASS).flat();
        }
        // upper bulb
        Part up = all.add("up").at(0, 26, 0);
        Part sand = up.add("sandUp").at(0, 0, 0);
        for (int i = 0; i < w.length; i++) {
            int k = w.length - 1 - i;
            G.fire(sand.box(-w[k] / 2f, i * 3, -w[k] / 2f, w[k], 3, w[k]), SAND).grad(SAND, SAND_D);
            up.box(-w[k] / 2f - 0.4f, i * 3 + 2.6f, -w[k] / 2f - 0.4f, w[k], 1, w[k]).inflate(0.4f).color(GLASS).flat();
        }
        up.box(-9, 18, -9, 18, 2, 18).color(GOLD).grad(GOLD, GOLD_D).bevel(0.2f);          // top cap
        up.box(-6, 20, -6, 12, 2, 12).color(STONE).bevel(0.2f);
        G.fire(up.box(-1, 22, -1, 2, 3, 2), IRIS);                                           // crown gem
        // falling stream through the neck
        Part stream = all.add("stream").at(0, 4, 0);
        G.fire(stream.box(-0.5f, 4, -0.5f, 1, 22, 1), SAND);
        // the eye in the neck, with lids
        Part eye = all.add("eye").at(0, 25, 5);
        eye.box(-7, -7, 0, 14, 14, 3).color(GOLD_D).bevel(0.2f);                             // socket
        Part ball = eye.add("ball").at(0, 0, 3);
        G.fire(ball.box(-5, -5, 0, 10, 10, 2), EYE_W);
        G.fire(ball.box(-3, -3, 2, 6, 6, 1), IRIS).glow(Face.FRONT, 2, 2, 2, 2, 0xffffff);
        Part lidU = eye.add("lidU").at(0, 6, 4); lidU.box(-7, -3, 0, 14, 3, 2).color(GOLD).bevel(0.2f);
        Part lidL = eye.add("lidL").at(0, -6, 4); lidL.box(-7, 0, 0, 14, 3, 2).color(GOLD).bevel(0.2f);
        // orbiting hour rings: two floating gold arcs
        for (int i = 0; i < 2; i++) {
            Part ring = all.add("ring" + i).at(0, 25, 0);
            ring.box(-13, -0.5f, -1, 4, 1, 2).color(GOLD).flat(); ring.box(9, -0.5f, -1, 4, 1, 2).color(GOLD).flat();
            ring.box(-1, -0.5f, -13, 2, 1, 4).color(GOLD).flat(); ring.box(-1, -0.5f, 9, 2, 1, 4).color(GOLD).flat();
        }
        m.bob(0.8f, 0.08f);
        m.proc((r, c) -> {
            r.rot("ring0", 0, c.age * 2.5f, 0); r.rot("ring1", 0, -c.age * 1.6f, 0);
            r.scl("sandUp", 1, 0.3f + 0.7f * c.hpFrac, 1);
            r.scl("stream", 1 + 0.5f * (float) Math.sin(c.age * 0.5), 1, 1 + 0.5f * (float) Math.sin(c.age * 0.5));
            r.rot("all", 0, 0, 0);
        });
        m.look("eye", 40, 20);
        m.clip("idle", 80).loop()
            .rot("all", 0, 0, 0, 1.5f).rot("all", 40, 0, 0, -1.5f).rot("all", 80, 0, 0, 1.5f)
            .rot("lidU", 0, 0, 0, 0).rot("lidU", 60, 0, 0, 0).rot("lidU", 64, 0, 0, 0).rot("lidU", 66, -40, 0, 0).rot("lidU", 70, 0, 0, 0)
            .pos("lidL", 0, 0, 0, 0).pos("lidL", 62, 0, 0, 0).pos("lidL", 66, 0, 1.5f, 0).pos("lidL", 70, 0, 0, 0);
        m.clip("walk", 40).loop()
            .rot("all", 0, 3, 0, 2).rot("all", 20, 3, 0, -2).rot("all", 40, 3, 0, 2);
        // EBB: tide pulls back (sand drains, tilts away), then the whole glass crashes forward like a wave
        m.clip("ebb", 40)
            .rot("all", 0, 0, 0, 0).rot("all", 14, -16, 0, 0, Ease.OUT_CUBIC).rot("all", 20, 30, 0, 0, Ease.IN_CUBIC).rot("all", 32, 18, 0, 0).rot("all", 40, 0, 0, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 14, 0, 1, -4).pos("all", 20, 0, -1, 7, Ease.IN_CUBIC).pos("all", 32, 0, 0, 5).pos("all", 40, 0, 0, 0)
            .scl("stream", 0, 1, 1, 1).scl("stream", 14, 3, 1, 3).scl("stream", 40, 1, 1, 1)
            .rot("eye", 0, 0, 0, 0).rot("eye", 20, 20, 0, 0).rot("eye", 40, 0, 0, 0);
        // EYE LASERS: lids snap wide, eye swells and shudders, two searing beams
        m.clip("eye_lasers", 40)
            .rot("lidU", 0, 0, 0, 0).rot("lidU", 8, -70, 0, 0, Ease.OUT_BACK).rot("lidU", 34, -70, 0, 0).rot("lidU", 40, 0, 0, 0)
            .pos("lidL", 0, 0, 0, 0).pos("lidL", 8, 0, -3, 0).pos("lidL", 34, 0, -3, 0).pos("lidL", 40, 0, 0, 0)
            .scl("ball", 0, 1, 1, 1).scl("ball", 12, 1.3f, 1.3f, 1.3f, Ease.OUT_BACK).scl("ball", 17, 1.6f, 1.6f, 1.6f).scl("ball", 20, 1.2f, 1.2f, 1.2f)
            .scl("ball", 25, 1.6f, 1.6f, 1.6f).scl("ball", 28, 1.2f, 1.2f, 1.2f).scl("ball", 40, 1, 1, 1)
            .rot("all", 0, 0, 0, 0).rot("all", 12, -8, 0, 0).rot("all", 18, 5, 0, 0, Ease.IN_CUBIC).rot("all", 26, 5, 0, 0, Ease.IN_CUBIC).rot("all", 40, 0, 0, 0)
            .pos("ball", 0, 0, 0, 0).pos("ball", 17, 0, 0, -2, Ease.IN_CUBIC).pos("ball", 20, 0, 0, 0).pos("ball", 25, 0, 0, -2, Ease.IN_CUBIC).pos("ball", 28, 0, 0, 0);
        // INCREASING INTENSITY: spins faster and faster, flips over (sand pours the other way), flares
        m.clip("increasing_intensity", 50)
            .rot("all", 0, 0, 0, 0).rot("all", 10, 0, 90, 0, Ease.IN_QUAD).rot("all", 22, 0, 360, 0, Ease.IN_QUAD).rot("all", 34, 0, 720, 0, Ease.IN_QUAD).rot("all", 44, 0, 720, 0).rot("all", 50, 0, 720, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 24, 0, 5, 0, Ease.OUT_CUBIC).pos("all", 44, 0, 5, 0).pos("all", 50, 0, 0, 0, Ease.OUT_BOUNCE)
            .scl("stream", 0, 1, 1, 1).scl("stream", 30, 4, 1, 4).scl("stream", 50, 1, 1, 1)
            .scl("ball", 0, 1, 1, 1).scl("ball", 36, 1.5f, 1.5f, 1.5f, Ease.OUT_BACK).scl("ball", 50, 1, 1, 1);
        m.clip("hurt", 10).rot("all", 0, 0, 0, 0).rot("all", 3, -6, 0, 5).rot("all", 10, 0, 0, 0)
            .rot("lidU", 0, 0, 0, 0).rot("lidU", 3, 20, 0, 0).rot("lidU", 10, 0, 0, 0);
        // death: cracks, tips over, shatters (everything collapses to a sand heap)
        m.clip("death", 50)
            .rot("all", 0, 0, 0, 0).rot("all", 20, 0, 0, 12).rot("all", 40, 0, 0, 80, Ease.IN_QUAD).rot("all", 50, 0, 0, 90, Ease.OUT_BOUNCE)
            .pos("all", 0, 0, 0, 0).pos("all", 50, 6, -3, 0)
            .scl("sandUp", 0, 1, 1, 1).scl("sandUp", 40, 1, 0.1f, 1)
            .scl("ball", 0, 1, 1, 1).scl("ball", 30, 1, 0.1f, 1)
            .scl("stream", 0, 1, 1, 1).scl("stream", 30, 3, 1, 3).scl("stream", 50, 1, 0.1f, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of(ID, "Aeonglass", "永劫の砂時計")
            .game(2, 3).size(2.6f, 5.8f).hp(512).speed(0.18f).style(Style.GHOST).egg(0xc8a050, 0xa060ff).boss().kbResist(1f)
            .model(Aeonglass::model)
            .pattern(Pattern.cycle("ebb", "eye_lasers", "increasing_intensity"))
            .onTick(s -> { // Withering Presence
                if (s.tickCount % 120 != 60 || s.getTarget() == null) return;
                int x = s.count("ii");
                for (var v : Fx.foes(s, s.getBoundingBox().inflate(24, 8, 24))) {
                    Fx.curse(v, Fx.Curse.DRAIN);
                    if (x > 0) v.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.WITHER, 40 * x));
                    Fx.particlesAt(s, ParticleTypes.SQUID_INK, v.position().add(0, 1.2, 0), 8, 0.3, 0.02);
                }
                Fx.sound(s, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.value(), 1.2f, 0.5f);
            })
            .move(Move.of("ebb", 40).intent(Intent.ATTACK_DEFEND, 22, 1).reach(5.5f)
                .sound(8, SoundEvents.WATER_AMBIENT, 1.4f, 0.5f)
                .at(20, (s, t) -> { Fx.aoeAt(s, s.position(), 22, 6.0, v -> Fx.knock(s, v, 1.2)); s.addBlock(33);
                    Fx.ring(s, ParticleTypes.CLOUD, s.position().add(0, 0.2, 0), 5, 40); Fx.sound(s, SoundEvents.GENERIC_SPLASH, 1.5f, 0.5f);
                    Fx.sound(s, SoundEvents.ANVIL_LAND, 0.7f, 0.5f); }))
            .move(Move.of("eye_lasers", 40).intent(Intent.ATTACK, 11, 2).ranged(20)
                .sound(8, SoundEvents.BEACON_ACTIVATE, 1.4f, 1.5f)
                .at(18, G.beam(11, ParticleTypes.SOUL_FIRE_FLAME, 1.8, 5.0)).at(26, G.beam(11, ParticleTypes.SOUL_FIRE_FLAME, 1.8, 5.0))
                .atAll(new int[]{18, 26}, Fx.sfx(SoundEvents.GUARDIAN_ATTACK, 1.2f, 1.4f)))
            .move(Move.of("increasing_intensity", 50).intent(Intent.DEBUFF).noTarget()
                .sound(10, SoundEvents.BELL_BLOCK, 2f, 0.5f)
                .at(36, (s, t) -> { int x = s.inc("ii"); s.addStrength(2 + x); s.setVar(x);
                    for (var v : Fx.foes(s, s.getBoundingBox().inflate(24, 8, 24))) Fx.curse(v, Fx.Curse.DRAIN);
                    Fx.particles(s, ParticleTypes.ENCHANT, 50, 2.0); Fx.sound(s, SoundEvents.BELL_RESONATE, 2f, 0.5f); }))
            .onClientTick(s -> {
                if (s.tickCount % 2 == 0) s.level().addParticle(ParticleTypes.ASH,
                    s.getX() + (s.getRandom().nextDouble() - .5) * 0.6, s.getY() + 3.0, s.getZ() + (s.getRandom().nextDouble() - .5) * 0.6, 0, -0.06, 0);
            });
    }
}
