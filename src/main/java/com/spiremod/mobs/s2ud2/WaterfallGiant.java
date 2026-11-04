package com.spiremod.mobs.s2ud2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks BOSS: Waterfall Giant (240 HP). T1 Pressurize (+15 Steam Eruption), then Stomp 15 + Weak / Ram 10 / Siphon heal 10 / Pressure Gun 20 (+5 per use) / Pressure Up 13;
 * every move except Pressurize adds 3 Steam Eruption. When killed with steam > 0 it becomes invulnerable, About To Blow, then Explode for the stored steam and dies.
 */
public final class WaterfallGiant {
    private WaterfallGiant() {}
    static final int ROCK = 0x6a7480, ROCK_D = 0x3c4450, MOSS = 0x4f7a58, WATER = 0x5cb0e0, WATER_L = 0xb8e8ff, PIPE = 0x8a7a5a, STEAM = 0xfff0d0, EYE = 0xa0f0ff;
    static final String[] CYC = {"stomp", "ram", "siphon", "pressure_gun", "pressure_up"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_waterfall_giant");
        m.scale(2.0f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 4.5f, 10, 0);
            leg.box(-3, -10, -3, 6, 10, 6).color(ROCK).grad(ROCK, ROCK_D).noise(0.2f).bevel(0.12f).band(0, 2, MOSS);
            leg.box(-3.5f, -10, -4.5f, 7, 3, 9).color(ROCK_D).noise(0.2f).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 10, 0);
        body.box(-6, 0, -3.5f, 12, 5, 7).color(ROCK_D).grad(ROCK, ROCK_D).noise(0.2f).bevel(0.1f);
        Part torso = body.add("torso").at(0, 5, 0);
        torso.box(-8, 0, -5, 16, 12, 10).color(ROCK).grad(0x8a94a0, ROCK).noise(0.22f).bevel(0.1f)
            .paint(Face.FRONT, 2, 1, 12, 3, WATER).glow(Face.FRONT, 3, 1, 10, 1, WATER_L).paint(Face.FRONT, 0, 7, 16, 2, MOSS);
        // waterfall cascading down the chest
        torso.box(-3, 0.5f, 5, 6, 11, 1).color(WATER).flat().grad(WATER_L, WATER).glow(Face.FRONT, 1, 0, 1, 10, WATER_L).glow(Face.FRONT, 4, 0, 1, 10, WATER_L);
        // back boiler + pressure pipes with vents
        Part tank = torso.add("tank").at(0, 5, -5);
        tank.box(-5, -5, -5, 10, 10, 5).color(PIPE).grad(0xa89a76, ROCK_D).noise(0.12f).bevel(0.15f).band(3, 1, 0x3a3224).band(7, 1, 0x3a3224);
        for (int s = -1; s <= 1; s += 2) {
            Part v = tank.add(s < 0 ? "ventR" : "ventL").at(s * 3.5f, 5, -3);
            v.box(-1.5f, 0, -1.5f, 3, 6, 3).color(PIPE).bevel(0.1f);
            v.box(-1, 6, -1, 2, 1, 2).color(STEAM).flat().glow(Face.TOP, 0, 0, 2, 2, STEAM);
        }
        Part head = torso.add("head").at(0, 12, 0);
        head.box(-4, 0, -4, 8, 7, 8).color(ROCK).grad(0x8a94a0, ROCK).noise(0.2f).bevel(0.15f)
            .glow(Face.FRONT, 1, 2, 2, 1, EYE).glow(Face.FRONT, 5, 2, 2, 1, EYE).paint(Face.FRONT, 2, 5, 4, 1, ROCK_D);
        head.box(-4.5f, 6, -4.5f, 9, 2, 9).color(MOSS).noise(0.15f).bevel(0.2f);
        head.box(-1.5f, -1.5f, 3.5f, 3, 4, 1).color(WATER).flat().grad(WATER_L, WATER);   // water beard
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 10, 10, 0);
            arm.box(-3, -8, -3, 6, 8, 6).color(ROCK).grad(ROCK, ROCK_D).noise(0.2f).bevel(0.12f).band(6, 2, MOSS);
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -8, 0);
            fore.box(-3.2f, -8, -3.2f, 6, 8, 6).color(ROCK_D).noise(0.2f).bevel(0.12f);
            fore.box(-4, -13, -4, 8, 6, 8).color(ROCK).grad(ROCK, ROCK_D).noise(0.25f).bevel(0.15f);     // fist
        }
        m.look("head", 40, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 28f, 18f);
        m.proc((r, c) -> {
            float p = 1f - c.hpFrac; float k = (float) Math.sin(c.age * 0.5f) * 2;
            r.scl("ventR", 1, 1 + p * 0.3f, 1); r.scl("ventL", 1, 1 + p * 0.3f, 1);
            r.rot("tank", k * p, 0, 0);
        });
        m.clip("idle", 60).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 30, -1, 0, 0).rot("torso", 60, 2, 0, 0)
            .rot("armR", 0, 3, 0, 5).rot("armR", 30, 6, 0, 8).rot("armR", 60, 3, 0, 5)
            .rot("armL", 0, 3, 0, -5).rot("armL", 30, 6, 0, -8).rot("armL", 60, 3, 0, -5)
            .rot("head", 0, 0, 0, 0).rot("head", 30, 3, 0, 0).rot("head", 60, 0, 0, 0)
            .pos("tank", 0, 0, 0, 0).pos("tank", 30, 0, 0.5f, 0).pos("tank", 60, 0, 0, 0);
        m.clip("walk", 20).loop();
        // PRESSURIZE: crouches, arms pump, tank swells, vents scream
        m.clip("pressurize", 52)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, 15, 0, 0).rot("torso", 24, -10, 0, 0, Ease.OUT_BACK).rot("torso", 40, -8, 0, 0).rot("torso", 52, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -3, 0).pos("body", 24, 0, 1, 0, Ease.OUT_BACK).pos("body", 52, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, 40, 0, 30).rot("armR", 24, -40, 0, 70, Ease.OUT_BACK).rot("armR", 40, -40, 0, 70).rot("armR", 52, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, 40, 0, -30).rot("armL", 24, -40, 0, -70, Ease.OUT_BACK).rot("armL", 40, -40, 0, -70).rot("armL", 52, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 24, -25, 0, 0, Ease.OUT_BACK).rot("head", 52, 0, 0, 0)
            .scl("tank", 0, 1, 1, 1).scl("tank", 24, 1.35f, 1.35f, 1.2f, Ease.OUT_BACK).scl("tank", 40, 1.3f, 1.3f, 1.15f).scl("tank", 52, 1, 1, 1)
            .scl("ventR", 0, 1, 1, 1).scl("ventR", 24, 1.6f, 2f, 1.6f).scl("ventR", 52, 1, 1, 1).scl("ventL", 0, 1, 1, 1).scl("ventL", 24, 1.6f, 2f, 1.6f).scl("ventL", 52, 1, 1, 1);
        // STOMP: lift knee, slam foot, ground shudder
        m.clip("stomp", 40)
            .rot("legR", 0, 0, 0, 0).rot("legR", 12, -60, 0, 0, Ease.OUT_QUAD).rot("legR", 17, 8, 0, 0, Ease.IN_CUBIC).rot("legR", 40, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -14, 0, 0).rot("torso", 17, 24, 0, 0, Ease.IN_CUBIC).rot("torso", 22, 20, 0, 0).rot("torso", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 3, 0).pos("body", 17, 0, -4, 0, Ease.IN_CUBIC).pos("body", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -50, 0, 30).rot("armR", 17, 30, 0, 10, Ease.IN_CUBIC).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -50, 0, -30).rot("armL", 17, 30, 0, -10, Ease.IN_CUBIC).rot("armL", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 17, 18, 0, 0).rot("head", 40, 0, 0, 0);
        // RAM: lowers shoulder and barrels forward
        m.clip("ram", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 20, -25, 0).rot("torso", 16, 35, 20, 0, Ease.IN_CUBIC).rot("torso", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -4, -4).pos("body", 16, 0, -3, 10, Ease.IN_CUBIC).pos("body", 24, 0, -2, 10).pos("body", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 30, 0, 30).rot("armR", 16, -70, 0, 20, Ease.IN_CUBIC).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 30, 0, -30).rot("armL", 16, -70, 0, -20, Ease.IN_CUBIC).rot("armL", 36, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 10, 25, 0, 0).rot("legR", 16, -40, 0, 0).rot("legR", 36, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 10, -20, 0, 0).rot("legL", 16, 35, 0, 0).rot("legL", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 16, 25, 0, 0).rot("head", 36, 0, 0, 0);
        // SIPHON: both arms reach out, then drag inward while the chest cascade glows
        m.clip("siphon", 44)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -100, 0, 40, Ease.OUT_BACK).rot("armR", 26, -100, 0, 40).rot("armR", 36, -50, 0, -10, Ease.OUT_QUAD).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -100, 0, -40, Ease.OUT_BACK).rot("armL", 26, -100, 0, -40).rot("armL", 36, -50, 0, 10, Ease.OUT_QUAD).rot("armL", 44, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -8, 0, 0).rot("torso", 36, 10, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -20, 0, 0).rot("head", 36, 12, 0, 0).rot("head", 44, 0, 0, 0);
        // PRESSURE GUN: both fists together at chest, tank jets, fires a jet from the fists
        m.clip("pressure_gun", 48)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -85, -20, -15).rot("armR", 32, -88, -20, -15).rot("armR", 48, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -85, 20, 15).rot("armL", 32, -88, 20, 15).rot("armL", 48, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, 12, 0, 0).rot("torso", 20, -12, 0, 0, Ease.IN_CUBIC).rot("torso", 34, -6, 0, 0).rot("torso", 48, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -1, -3).pos("body", 20, 0, 0, 4, Ease.IN_CUBIC).pos("body", 48, 0, 0, 0)
            .scl("tank", 0, 1, 1, 1).scl("tank", 14, 1.3f, 1.3f, 1.3f).scl("tank", 24, 0.9f, 0.9f, 0.9f, Ease.IN_CUBIC).scl("tank", 48, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 10, 0, 0).rot("head", 20, -15, 0, 0).rot("head", 48, 0, 0, 0);
        // PRESSURE UP: overhead double smash
        m.clip("pressure_up", 40)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -165, 0, 12, Ease.OUT_QUAD).rot("armR", 15, -165, 0, 12).rot("armR", 20, -20, 0, 5, Ease.IN_CUBIC).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -165, 0, -12, Ease.OUT_QUAD).rot("armL", 15, -165, 0, -12).rot("armL", 20, -20, 0, -5, Ease.IN_CUBIC).rot("armL", 40, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -18, 0, 0).rot("torso", 20, 28, 0, 0, Ease.IN_CUBIC).rot("torso", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -25, 0, 0).rot("head", 20, 20, 0, 0).rot("head", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 2, 0).pos("body", 20, 0, -4, 3, Ease.IN_CUBIC).pos("body", 40, 0, 0, 0);
        // ABOUT TO BLOW: slumps, shakes violently, glows
        Clip ab = m.clip("about_to_blow", 40);
        ab.rot("torso", 0, 0, 0, 0).rot("torso", 10, 25, 0, 0).pos("body", 0, 0, 0, 0).pos("body", 10, 0, -4, 0).rot("head", 0, 0, 0, 0).rot("head", 10, 30, 0, 0)
          .rot("armR", 0, 0, 0, 0).rot("armR", 10, 15, 0, 40).rot("armL", 0, 0, 0, 0).rot("armL", 10, 15, 0, -40)
          .scl("tank", 0, 1, 1, 1).scl("tank", 38, 1.5f, 1.5f, 1.5f);
        for (int t = 12; t < 40; t += 2) ab.pos("body", t, (t % 4 == 0 ? 0.8f : -0.8f), -4, (t % 4 == 0 ? 0.5f : -0.5f), Ease.LINEAR);
        m.clip("explode", 36)
            .scl("tank", 0, 1.5f, 1.5f, 1.5f).scl("tank", 14, 2.2f, 2.2f, 2.2f, Ease.IN_CUBIC).scl("tank", 18, 0.4f, 0.4f, 0.4f).scl("tank", 36, 0.4f, 0.4f, 0.4f)
            .rot("torso", 0, 25, 0, 0).rot("torso", 14, -30, 0, 0, Ease.IN_CUBIC).rot("torso", 36, -30, 0, 0)
            .rot("armR", 0, 15, 0, 40).rot("armR", 16, -120, 0, 80, Ease.OUT_BACK).rot("armR", 36, -120, 0, 80)
            .rot("armL", 0, 15, 0, -40).rot("armL", 16, -120, 0, -80, Ease.OUT_BACK).rot("armL", 36, -120, 0, -80)
            .rot("head", 0, 30, 0, 0).rot("head", 14, -40, 0, 0).rot("head", 36, -40, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 8, 0, 4).rot("torso", 10, 0, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 3, -12, 0, 0).rot("head", 10, 0, 0, 0);
        m.clip("death", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 20, 30, 0, 0).rot("torso", 50, 60, 0, 0, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 30, 0, -5, 0, Ease.IN_QUAD).pos("body", 50, 0, -9, 4, Ease.OUT_BOUNCE)
            .rot("head", 0, 0, 0, 0).rot("head", 30, 30, 15, 0).rot("armR", 0, 0, 0, 0).rot("armR", 40, 20, 0, 70).rot("armL", 0, 0, 0, 0).rot("armL", 40, 20, 0, -70)
            .rot("legR", 0, 0, 0, 0).rot("legR", 40, -70, 0, 0).rot("legL", 0, 0, 0, 0).rot("legL", 40, -60, 0, 0);
        return m;
    }

    static void steam(SpireMob s, int n) { s.setCount("steam", s.count("steam") + n); Fx.particles(s, ParticleTypes.CLOUD, 8, 0.6); }

    public static MobDef def() {
        return MobDef.of("s2_waterfall_giant", "Waterfall Giant", "滝の巨人")
            .game(2, 1).boss().size(2.2f, 4.0f).hp(240).speed(0.22f).style(Style.METAL).egg(0x6a7480, 0x5cb0e0)
            .model(WaterfallGiant::model)
            .pattern(s -> {
                if (s.flags.contains("blow")) return s.lastMove().equals("about_to_blow") ? "explode" : "about_to_blow";
                if (s.getTurn() == 0) return "pressurize";
                return CYC[s.count("ct") % 5];
            })
            .move(Move.of("pressurize", 52).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.PISTON_EXTEND, 1.2f, 0.5f)
                .at(24, (s, t) -> { s.setCount("steam", s.count("steam") + 15); Fx.sound(s, SoundEvents.FIRE_EXTINGUISH, 1.5f, 0.6f); Fx.particles(s, ParticleTypes.CLOUD, 30, 1.0); }))
            .move(Move.of("stomp", 40).intent(Intent.ATTACK_DEBUFF, 15, 1).reach(3.6f)
                .at(17, (s, t) -> { Fx.aoeAt(s, Fx.rel(s, 1.5, 0, 0), 15, 4.0, v -> Fx.weak(v, 6)); steam(s, 3); Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.8f, 0.6f); Fx.particlesAt(s, ParticleTypes.CAMPFIRE_COSY_SMOKE, Fx.rel(s, 1.5, 0, 0), 10, 1.5, 0.02); }))
            .move(Move.of("ram", 36).intent(Intent.ATTACK_BUFF, 10, 1).reach(3.8f).at(16, (s, t) -> { Fx.dash(0.8, 0.1).run(s, t); Fx.meleeNow(s, 10, 4.2, 100, v -> Fx.knock(s, v, 0.8)); steam(s, 3); }))
            .move(Move.of("siphon", 44).intent(Intent.BUFF).noTarget()
                .at(20, (s, t) -> { s.heal(10); steam(s, 3); Fx.sound(s, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, 1.5f, 0.6f); Fx.particles(s, ParticleTypes.HEART, 6, 0.5); }))
            .move(Move.of("pressure_gun", 48).intent(Intent.ATTACK_BUFF, 20, 1).ranged(14)
                .sound(18, SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1.2f, 0.5f)
                .at(20, (s, t) -> { Fx.hitTarget(20 + 5 * s.count("gun")).run(s, t); s.inc("gun"); steam(s, 3); Fx.particlesAt(s, ParticleTypes.BUBBLE_POP, Fx.rel(s, 2, 0, 1.8), 30, 0.3, 0.3); }))
            .move(Move.of("pressure_up", 40).intent(Intent.ATTACK_BUFF, 13, 1).reach(3.6f)
                .at(20, (s, t) -> { Fx.aoeAt(s, Fx.rel(s, 2, 0, 0), 13, 3.6, null); steam(s, 3); Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.9f, 0.7f); }))
            .move(Move.of("about_to_blow", 40).intent(Intent.STUN).noTarget()
                .at(2, (s, t) -> { Fx.sound(s, SoundEvents.WARDEN_SONIC_CHARGE, 1.5f, 0.8f); s.addStrength(-s.getStrength()); }))
            .move(Move.of("explode", 36).intent(Intent.ATTACK, 99, 1).noTarget()
                .at(16, (s, t) -> {
                    float d = s.count("steam");
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 2f, 0.7f); Fx.particlesAt(s, ParticleTypes.EXPLOSION_EMITTER, s.position().add(0, 2, 0), 3, 1.5, 0);
                    Fx.aoeAt(s, s.position(), d, 12, null);
                    s.setCount("steam", 0); s.flags.remove("blow"); s.kill();
                }))
            .onMoveEnd(s -> { if (!s.flags.contains("blow") && !s.lastMove().equals("pressurize")) s.inc("ct"); })
            .onHurt((s, src, dmg) -> {
                if (s.flags.contains("blow")) return 0;                               // invulnerable while blowing up
                if (s.getHealth() - dmg <= 0 && s.count("steam") > 0) {
                    s.flags.add("blow"); s.cancelMove(); s.forceNext("about_to_blow");
                    return s.getHealth() - 1;                                          // survive at 1 HP
                }
                return dmg;
            });
    }
}
