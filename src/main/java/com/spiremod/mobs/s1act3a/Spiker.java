package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

/**
 * StS1 Act 3 Spiker (one of the floating "Shapes", 42-56 HP). Thorns 3.
 * 50% Cut (7 dmg, never twice in a row), 50% Spike (+2 Thorns, at most 6 times).
 * Look: hovering bruise-violet stone sphere bristling with bone spikes, one glowing slit eye.
 */
public final class Spiker {
    private Spiker() {}

    static final int STONE = 0x4a3a5e, STONE_D = 0x2a2038, STONE_L = 0x7a6496, SPIKE = 0xd9cfb4, SPIKE_T = 0xfff0d0, EYE = 0xff4a6a;

    /** rounded ball made of a core box + inset caps; r = half width in px */
    static void ball(Part p, int r, int col, int light, int dark) {
        int d = r * 2;
        p.box(-r, -r + 1, -r, d, d - 2, d).color(col).grad(light, dark).bevel(0.25f).noise(0.1f);
        p.box(-r + 1, r - 1, -r + 1, d - 2, 1, d - 2).color(light).bevel(0.2f);
        p.box(-r + 1, -r, -r + 1, d - 2, 1, d - 2).color(dark).bevel(0.2f);
        p.box(-r - 1, -r + 2, -r + 1, 1, d - 4, d - 2).color(col).bevel(0.2f);
        p.box(r, -r + 2, -r + 1, 1, d - 4, d - 2).color(col).bevel(0.2f);
        p.box(-r + 1, -r + 2, r, d - 2, d - 4, 1).color(col).bevel(0.2f);
        p.box(-r + 1, -r + 2, -r - 1, d - 2, d - 4, 1).color(dark).bevel(0.2f);
    }

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("spiker");
        Part body = m.add("body").at(0, 12, 0);
        ball(body, 6, STONE, STONE_L, STONE_D);
        Part eye = body.add("eye").at(0, 0, 7);
        eye.box(-3, -1.5f, 0, 6, 3, 1).color(0x1a0f22).flat().glow(Face.FRONT, 1, 1, 4, 1, EYE).glow(Face.FRONT, 2, 0, 2, 3, EYE);
        Part lid = eye.add("lid").at(0, 1.5f, 0.2f);
        lid.box(-3.5f, -1, 0, 7, 1, 1).color(STONE_D);
        // spikes on a shell that can spin and flare
        Part spikes = body.add("spikes").at(0, 0, 0);
        float[][] dirs = {{0, 90}, {0, -90}, {90, 0}, {-90, 0}, {0, 0}, {180, 0}, {45, 45}, {-45, 45}, {135, 45}, {-135, 45},
            {45, -45}, {-45, -45}, {135, -45}, {-135, -45}, {90, 45}, {-90, -45}, {180, 45}, {0, -45}};
        for (int i = 0; i < dirs.length; i++) {
            if (dirs[i][0] == 0 && dirs[i][1] == 0) continue;                     // keep the eye clear
            // yaw around Y, pitch: aim +Y spike along (yaw, pitch)
            Part sp = spikes.add("sp" + i).rot(90 - dirs[i][1], dirs[i][0], 0);
            sp.box(-1, 5, -1, 2, 3, 2).color(SPIKE).bevel(0.2f);
            sp.box(-0.5f, 8, -0.5f, 1, 3, 1).color(SPIKE_T).flat();
        }
        m.bob(1.2f, 0.12f);
        m.proc((r, c) -> { float g = 1 + 0.12f * Math.min(c.var, 6); r.scl("spikes", g, g, g); });   // grows with Thorns
        m.clip("idle", 60).loop()
            .rot("body", 0, 0, 0, 0).rot("body", 30, 0, 20, 4).rot("body", 60, 0, 0, 0)
            .rot("spikes", 0, 0, 0, 0).rot("spikes", 60, 0, 360, 0, Ease.LINEAR)
            .pos("lid", 0, 0, 0, 0).pos("lid", 44, 0, 0, 0).pos("lid", 46, 0, -2, 0).pos("lid", 49, 0, 0, 0);
        m.clip("walk", 20).loop().rot("body", 0, 10, 0, 0).rot("body", 10, 14, 0, 0).rot("body", 20, 10, 0, 0);
        // CUT: wind back, then roll-spin forward raking the target with its spikes
        m.clip("cut", 28)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 2, -4).pos("body", 13, 0, -1, 9, Ease.IN_CUBIC).pos("body", 19, 0, 0, 6).pos("body", 28, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -35, 0, 0).rot("body", 13, 160, 0, 0, Ease.IN_CUBIC).rot("body", 19, 330, 0, 0).rot("body", 28, 360, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 8, 1.1f, 0.9f, 1.1f).scl("body", 13, 0.9f, 1.1f, 1.1f).scl("body", 28, 1, 1, 1)
            .pos("lid", 0, 0, 0, 0).pos("lid", 8, 0, -1, 0).pos("lid", 28, 0, 0, 0);
        // SPIKE: clench (spikes retract), then bristle outward with a shiver
        m.clip("spike", 30)
            .scl("spikes", 0, 1, 1, 1).scl("spikes", 8, 0.7f, 0.7f, 0.7f).scl("spikes", 12, 1.35f, 1.35f, 1.35f, Ease.OUT_BACK).scl("spikes", 22, 1.2f, 1.2f, 1.2f).scl("spikes", 30, 1, 1, 1)
            .scl("body", 0, 1, 1, 1).scl("body", 8, 0.88f, 0.88f, 0.88f).scl("body", 12, 1.08f, 1.08f, 1.08f, Ease.OUT_BACK).scl("body", 30, 1, 1, 1)
            .rot("body", 0, 0, 0, 0).rot("body", 12, 0, 0, 0).rot("body", 14, 0, 0, 6).rot("body", 16, 0, 0, -6).rot("body", 18, 0, 0, 5).rot("body", 20, 0, 0, -4).rot("body", 22, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 6, 0, -3, 0).pos("lid", 12, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -20, 0, 10).rot("body", 10, 0, 0, 0).pos("lid", 0, 0, 0, 0).pos("lid", 3, 0, -3, 0).pos("lid", 10, 0, 0, 0);
        m.clip("death", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 2, 0).pos("body", 22, 0, -6, 0, Ease.IN_QUAD).pos("body", 30, 0, -6, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 22, 40, 90, 30)
            .scl("spikes", 0, 1, 1, 1).scl("spikes", 14, 0.6f, 0.6f, 0.6f).pos("lid", 0, 0, 0, 0).pos("lid", 10, 0, -3, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("spiker", "Spiker", "スパイカー")
            .game(1, 3).size(1.0f, 1.3f).hp(48).speed(0.22f).style(Style.SHULKER).egg(0x4a3a5e, 0xd9cfb4)
            .model(Spiker::model).flying()
            .spawn(5, 1, 2, MobDef.Habitat.MOUNTAIN)
            .pattern(s -> {
                boolean canSpike = s.count("spikes") < 6, canCut = !s.lastMove().equals("cut");
                if (canSpike && canCut) return s.rand().nextBoolean() ? "cut" : "spike";
                return canCut ? "cut" : "spike";
            })
            .move(Move.of("cut", 28).intent(Intent.ATTACK, 7, 1).reach(2.2f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.3f)
                .at(13, Fx.melee(7, 2.6, 100)))
            .move(Move.of("spike", 30).intent(Intent.BUFF).noTarget()
                .sound(11, SoundEvents.PUFFER_FISH_BLOW_UP, 1.2f, 0.8f)
                .at(12, (s, t) -> { s.inc("spikes"); s.setCount("thorns", s.count("thorns") + 2); s.setVar(s.count("spikes"));
                    Fx.particles(s, ParticleTypes.CRIT, 15, 0.5); }))
            .onSpawn(s -> s.setCount("thorns", 3))
            .onHurt((s, src, amt) -> {
                if (src.getDirectEntity() instanceof LivingEntity a && a != s && a.distanceTo(s) < 4) {
                    Fx.raw(s, a, s.count("thorns") * Cfg.DMG);
                    Fx.particlesAt(s, ParticleTypes.CRIT, a.position().add(0, 1, 0), 6, 0.3, 0.1);
                }
                return amt;
            });
    }
}
