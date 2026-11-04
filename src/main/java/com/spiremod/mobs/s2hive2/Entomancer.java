package com.spiremod.mobs.s2hive2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Hive elite ENTOMANCER (145 HP): bug-priest with a living hive on its back.
 * Personal Hive N: when hit it stuffs Dazed into the attacker (N copies). Cycle: Beeeees! 3x7 -> Spear! 18 -> Pheromone Spit (Hive+1, Str+1; Hive at 3 -> Str+2).
 */
public final class Entomancer {
    private Entomancer() {}

    static final int ROBE = 0x3a2a4a, ROBE_D = 0x20162e, TRIM = 0xd8a830, CHITIN = 0x7a5a2a, CHITIN_D = 0x4a3418, COMB = 0xe8b040, COMB_D = 0x9a6a1a, EYE = 0xffe040, WING = 0xcfe8e0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_entomancer");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3.5f, 11, 0);
            leg.box(-1.5f, -11, -1.5f, 3, 11, 3).color(CHITIN).grad(CHITIN, CHITIN_D).noise(0.08f);
            leg.box(-2.5f, -11, -3, 5, 2, 6).color(CHITIN_D).bevel(0.2f);
        }
        Part body = m.add("body").at(0, 11, 0);
        body.box(-5, 0, -3, 10, 4, 6).color(ROBE_D).grad(ROBE, ROBE_D);
        Part skirt = body.add("skirt").at(0, 3, 0);
        skirt.box(-6.5f, -11, -4, 13, 8, 8).color(ROBE_D).grad(ROBE, ROBE_D).noise(0.1f);
        skirt.box(-7, -12, -4.5f, 14, 2, 9).color(TRIM).flat().noise(0.05f);
        Part torso = body.add("torso").at(0, 4, 0);
        torso.box(-5.5f, 0, -3.5f, 11, 11, 7).color(ROBE).grad(0x54406a, ROBE).bevel(0.1f);
        torso.box(-1, 1, -3.8f, 2, 9, 1).color(TRIM).flat();
        torso.box(-6, 9, -4, 12, 3, 8).color(CHITIN).grad(CHITIN, CHITIN_D).bevel(0.25f);                // shoulder carapace
        // the Personal Hive: honeycomb mass on the back
        Part hive = torso.add("hive").at(0, 6, -4);
        hive.box(-6, -5, -9, 12, 14, 9).color(COMB).grad(COMB, COMB_D).noise(0.15f).bevel(0.3f);
        for (int i = 0; i < 4; i++) for (int j = 0; j < 3; j++)
            hive.box(-5 + i * 3, -4 + j * 4, -9.4f, 2, 2, 1).color(0x3a2208).flat().glow(Face.BACK, 0, 0, 2, 2, (i + j) % 2 == 0 ? 0xffb020 : 0x3a2208);
        hive.box(-3, 9, -7, 6, 3, 5).color(COMB_D).bevel(0.3f);
        // head
        Part head = torso.add("head").at(0, 11, 0);
        head.box(-3.5f, 0, -3.5f, 7, 7, 7).color(CHITIN).grad(0x9a7438, CHITIN_D).bevel(0.15f).noise(0.1f);
        head.box(-3.5f, 0, 3.4f, 7, 4, 1).color(CHITIN_D).flat();
        head.box(-5.5f, 3, 1, 2, 4, 3).color(EYE).flat().glow(Face.LEFT, 0, 0, 3, 4, EYE).glow(Face.FRONT, 0, 0, 2, 4, EYE);
        head.box(3.5f, 3, 1, 2, 4, 3).color(EYE).flat().glow(Face.RIGHT, 0, 0, 3, 4, EYE).glow(Face.FRONT, 0, 0, 2, 4, EYE);
        Part mdL = head.add("mdL").at(1, 1, 3.5f); mdL.box(-0.5f, -4, 0, 1, 4, 2).color(0xe8d8a8).flat();
        Part mdR = head.add("mdR").at(-1, 1, 3.5f); mdR.box(-0.5f, -4, 0, 1, 4, 2).color(0xe8d8a8).flat();
        head.box(-4, 6.5f, -4, 8, 2, 8).color(TRIM).flat().bevel(0.15f).inflate(0.2f);                  // circlet
        for (int sd = -1; sd <= 1; sd += 2) {
            Part an = head.add(sd < 0 ? "antR" : "antL").at(sd * 2, 7, 1).rot(-30, 0, sd * -25);
            an.box(-0.5f, 0, -0.5f, 1, 8, 1).color(CHITIN_D);
            an.box(-1, 8, -1, 2, 2, 2).color(COMB).flat().glow(Face.FRONT, 0, 0, 2, 2, 0xffd060);
            // wings
            Part wg = torso.add(sd < 0 ? "wingR" : "wingL").at(sd * 2, 9, -4).rot(0, sd * 20, sd * -30);
            wg.box(sd < 0 ? -9 : 0, 0, -0.5f, 9, 11, 1).color(WING).flat().noise(0.04f).grad(0xffffff, WING);
        }
        // arms
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 7, 10, 0);
            arm.box(-1.5f, -7, -1.5f, 3, 7, 3).color(ROBE).bevel(0.1f);
            arm.box(-1.8f, -7.3f, -1.8f, 4, 2, 4).color(TRIM).flat();
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -7, 0);
            hand.box(-1.5f, -4, -1.5f, 3, 4, 3).color(CHITIN).grad(CHITIN, CHITIN_D).bevel(0.2f);
            if (s < 0) {       // stinger spear
                Part sp = hand.add("spear").at(0, -3, 0).rot(-90, 0, 0);
                sp.box(-0.7f, -6, -0.7f, 1, 12, 1).color(0x4a3418);
                sp.box(-1.5f, 6, -0.5f, 3, 6, 1).color(0xd8d0a0).flat().bevel(0.2f);
                sp.box(-0.5f, 12, -0.5f, 1, 4, 1).color(0xf0eadc).flat();
            }
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 35f, 20f);
        m.proc((r, c) -> {
            float f = 22 + (c.var >= 2 ? 10 : 0) + c.var * 4;
            float buzz = "beeeees".equals(c.move) ? 40 : 14;
            r.rot("wingR", 0, (float) Math.sin(c.age * 1.6f) * buzz * 0.4f, 0); r.rot("wingL", 0, -(float) Math.sin(c.age * 1.6f) * buzz * 0.4f, 0);
            float sc = 1 + c.var * 0.08f; r.scl("hive", sc, sc, sc);
        });
        m.clip("idle", 50).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 25, -1, 0, 0).rot("torso", 50, 2, 0, 0)
            .rot("armR", 0, 6, 0, 8).rot("armR", 25, 10, 0, 10).rot("armR", 50, 6, 0, 8)
            .rot("armL", 0, 6, 0, -8).rot("armL", 25, 10, 0, -10).rot("armL", 50, 6, 0, -8)
            .rot("mdL", 0, 0, 0, 0).rot("mdL", 20, 0, 0, 12).rot("mdL", 30, 0, 0, 0).rot("mdR", 0, 0, 0, 0).rot("mdR", 20, 0, 0, -12).rot("mdR", 30, 0, 0, 0)
            .scl("hive", 0, 1, 1, 1).scl("hive", 25, 1.04f, 1.04f, 1.04f).scl("hive", 50, 1, 1, 1);
        // BEEEEES!: arms up, hive bursts open, swarm lashed forward
        m.clip("beeeees", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -18, 0, 0).rot("torso", 16, 22, 0, 0, Ease.IN_CUBIC).rot("torso", 34, 10, 0, 0).rot("torso", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -150, 0, 25).rot("armR", 16, -80, 0, 5, Ease.IN_CUBIC).rot("armR", 34, -80, 0, 5).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -150, 0, -25).rot("armL", 16, -80, 0, -5, Ease.IN_CUBIC).rot("armL", 34, -80, 0, -5).rot("armL", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -25, 0, 0).rot("head", 16, 10, 0, 0).rot("head", 40, 0, 0, 0)
            .scl("hive", 0, 1, 1, 1).scl("hive", 10, 0.9f, 0.9f, 0.9f).scl("hive", 14, 1.35f, 1.35f, 1.35f, Ease.OUT_BACK).scl("hive", 40, 1, 1, 1);
        // SPEAR!: coil, big stab with the stinger spear
        m.clip("spear", 30)
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -8, -35, 0).rot("torso", 13, 20, 40, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, -3).pos("body", 13, 0, -1, 6, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, 40, 20, 20).rot("armR", 13, -100, -10, -4, Ease.IN_CUBIC).rot("armR", 20, -100, -10, -4).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, -40, 0, -20).rot("armL", 13, 20, 0, -10).rot("armL", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 9, 10, 30, 0).rot("head", 13, 5, -25, 0).rot("head", 30, 0, 0, 0)
            .rot("mdL", 0, 0, 0, 0).rot("mdL", 13, 0, 0, 30).rot("mdL", 24, 0, 0, 0).rot("mdR", 0, 0, 0, 0).rot("mdR", 13, 0, 0, -30).rot("mdR", 24, 0, 0, 0);
        // PHEROMONE SPIT: hunch, hive pulses, spit forward with a heave
        m.clip("pheromone_spit", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -16, 0, 0).rot("torso", 16, 28, 0, 0, Ease.IN_CUBIC).rot("torso", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -30, 0, 0).rot("head", 16, 25, 0, 0, Ease.IN_CUBIC).rot("head", 36, 0, 0, 0)
            .rot("mdL", 0, 0, 0, 0).rot("mdL", 16, 0, 0, 45).rot("mdL", 30, 0, 0, 0).rot("mdR", 0, 0, 0, 0).rot("mdR", 16, 0, 0, -45).rot("mdR", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 16, -40, 0, 30).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 16, -40, 0, -30).rot("armL", 36, 0, 0, 0)
            .scl("hive", 0, 1, 1, 1).scl("hive", 8, 1.3f, 1.3f, 1.3f).scl("hive", 12, 0.9f, 0.9f, 0.9f).scl("hive", 20, 1.25f, 1.25f, 1.25f, Ease.OUT_BACK).scl("hive", 36, 1, 1, 1);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 14, 0, 6).rot("torso", 10, 0, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 3, -20, 0, 8).rot("head", 10, 0, 0, 0)
            .scl("hive", 0, 1, 1, 1).scl("hive", 3, 1.3f, 1.3f, 1.3f).scl("hive", 10, 1, 1, 1);
        m.clip("death", 34)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -20, 0, 0).rot("torso", 34, 25, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, -5, 0, Ease.IN_QUAD).pos("body", 34, 0, -9, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -110, 0, 40).rot("armR", 34, 20, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -90, 0, -40).rot("armL", 34, 10, 0, -70)
            .rot("legR", 0, 0, 0, 0).rot("legR", 20, -80, 0, 10).rot("legL", 0, 0, 0, 0).rot("legL", 20, -70, 0, -10)
            .scl("hive", 0, 1, 1, 1).scl("hive", 20, 1.4f, 1.4f, 1.4f).scl("hive", 34, 0.5f, 0.5f, 0.5f);
        return m;
    }

    static final Move.Action B = (s, t) -> {
        Fx.particlesAt(s, ParticleTypes.CRIT, Fx.rel(s, 2, 0, 1.4), 6, 0.4, 0.2);
        if (t != null) { H.line(s, ParticleTypes.CRIT, Fx.rel(s, 1, 0, 1.4), t.position().add(0, t.getBbHeight() * 0.6, 0), 6); H.hit(s, t, 3); }
    };

    public static MobDef def() {
        return MobDef.of("s2_entomancer", "Entomancer", "蟲使い")
            .game(2, 2).size(0.9f, 2.0f).hp(145).speed(0.24f).style(Style.INSECT).egg(0xe8b040, 0x3a2a4a)
            .elite().model(Entomancer::model)
            .spawn(1, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("beeeees", "spear", "pheromone_spit"))
            .move(Move.of("beeeees", 40).intent(Intent.ATTACK, 3, 7).ranged(10)
                .sound(6, SoundEvents.BEE_LOOP_AGGRESSIVE, 1.4f, 0.8f)
                .at(15, (s, t) -> Fx.sound(s, SoundEvents.BEE_LOOP_AGGRESSIVE, 1.6f, 1.2f))
                .at(16, B).at(18, B).at(20, B).at(22, B).at(24, B).at(26, B).at(28, B))
            .move(Move.of("spear", 30).intent(Intent.ATTACK, 18, 1).reach(3.2f)
                .sound(9, SoundEvents.BEE_STING, 1f, 0.6f)
                .at(13, Fx.melee(18, 3.6, 45)))
            .move(Move.of("pheromone_spit", 36).intent(Intent.BUFF).noTarget()
                .sound(14, SoundEvents.LLAMA_SPIT, 1.2f, 0.5f)
                .at(16, (s, t) -> {
                    int hive = s.getVar();
                    if (hive >= 3) s.addStrength(2); else { s.setVar(hive + 1); s.addStrength(1); }
                    Fx.particles(s, ParticleTypes.FALLING_HONEY, 14, 0.6); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 4, 0.6);
                }))
            .onSpawn(s -> s.setVar(1))
            .onHurt((s, src, amt) -> {
                if (src.getEntity() instanceof net.minecraft.world.entity.LivingEntity v && src.getDirectEntity() == v && !(v instanceof SpireMob))
                    for (int i = 0; i < Math.max(1, s.getVar()); i++) Fx.curse(v, Fx.Curse.DAZED);
                return amt;
            });
    }
}
