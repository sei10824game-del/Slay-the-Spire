package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Toadpole (21-25 HP). Cycle: Whirl (7) -> Spiken (+2 Thorns) -> Spike Spit (3x3, removes 2 Thorns) -> ...
 * Thorns are tracked in counter "thorns" and shown by the size of the back spikes.
 */
public final class Toadpole {
    private Toadpole() {}
    static final int SKIN = 0x6b8f4a, SKIN_D = 0x3f5c2e, BELLY = 0xc9d49a, SPIKE = 0xe6dca0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_toadpole");
        Part tail = m.add("tail").at(0, 4, -4);
        tail.box(-1, -3, -6, 2, 5, 6).color(SKIN_D).grad(SKIN, SKIN_D);
        Part tail2 = tail.add("tail2").at(0, 0, -6);
        tail2.box(-0.5f, -2, -5, 1, 4, 5).color(SKIN_D).noise(0.1f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3.2f, 2.5f, 2.5f);
            leg.box(-1, -3, -1, 2, 3, 2).color(SKIN_D);
            leg.box(-1.5f, -2.5f, -1, 3, 1, 3).color(SKIN_D).bevel(0.2f);
            Part leg2 = m.add(s < 0 ? "hindR" : "hindL").at(s * 3.2f, 2.5f, -2.5f);
            leg2.box(-1, -3, -1, 2, 3, 2).color(SKIN_D);
            leg2.box(-1.5f, -2.5f, -1, 3, 1, 3).color(SKIN_D).bevel(0.2f);
        }
        Part body = m.add("body").at(0, 2.5f, 0);
        body.box(-4, 0, -5, 8, 6, 10).color(SKIN).grad(0x82a85a, SKIN_D).noise(0.14f).bevel(0.3f);
        body.box(-3.5f, 0, -4.5f, 7, 1, 9).color(BELLY).noise(0.08f);
        for (int i = 0; i < 6; i++) body.box(-3 + (i % 2) * 3.5f, 6, -4 + i * 1.5f, 2, 1, 2).color(SKIN_D).noise(0.2f).bevel(0.1f); // warts
        // spikes along the back (scaled by Thorns)
        Part spikes = body.add("spikes").at(0, 6, 0);
        for (int i = 0; i < 5; i++) {
            Part sp = spikes.add("spike" + i).at(0, 0, -4 + i * 2).rot(0, 0, 0);
            sp.box(-1, 0, -1, 2, 3 + (i % 2), 2).color(SPIKE).grad(0xfff4c8, 0xb8ac78);
            Part sl = spikes.add("spikeL" + i).at(i % 2 == 0 ? -2.5f : 2.5f, -1, -3 + i * 1.8f).rot(0, 0, i % 2 == 0 ? 35 : -35);
            sl.box(-1, 0, -1, 2, 3, 2).color(SPIKE);
        }
        Part head = body.add("head").at(0, 2, 5);
        head.box(-3.5f, -1, 0, 7, 5, 4).color(SKIN).grad(0x82a85a, SKIN).bevel(0.25f);
        head.box(-3, -2, 0.5f, 6, 1, 4).color(BELLY);
        Part jaw = head.add("jaw").at(0, -0.5f, 4);
        jaw.box(-3, -1, -4, 6, 1, 4).color(SKIN_D).bevel(0.2f);
        jaw.box(-2, 0, -2, 4, 1, 1).color(0xd24a5a).flat();       // tongue
        for (int s = -1; s <= 1; s += 2) {
            Part eye = head.add(s < 0 ? "eyeR" : "eyeL").at(s * 2.4f, 4, 2);
            eye.box(-1.5f, 0, -1.5f, 3, 2, 3).color(SKIN).bevel(0.2f);
            eye.box(-1, 1, 0.9f, 2, 1, 1).color(0xf2e65a).glow(Face.FRONT, 0, 0, 2, 1, 0xfff080).paint(Face.FRONT, 0, 0, 1, 1, 0x101010);
        }
        m.proc((r, c) -> { float g = c.var <= 0 ? 0.001f : 0.55f + 0.25f * Math.min(c.var, 6); r.scl("spikes", g, g, g); });
        m.walkQuad("legR", "legL", "hindR", "hindL", 40);
        m.clip("idle", 36).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 18, 1.06f, 0.94f, 1.04f).scl("body", 36, 1, 1, 1)
            .rot("tail", 0, 0, 12, 0).rot("tail", 18, 0, -12, 0).rot("tail", 36, 0, 12, 0)
            .rot("tail2", 0, 0, -18, 0).rot("tail2", 18, 0, 18, 0).rot("tail2", 36, 0, -18, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 18, 8, 0, 0).rot("jaw", 36, 0, 0, 0)
            .rot("eyeR", 0, 0, 0, 0).rot("eyeR", 24, 0, 0, 6).rot("eyeR", 36, 0, 0, 0);
        // WHIRL: brace, then spin the whole body with the tail lashing around (7 dmg)
        m.clip("whirl", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 6, 12, 0, 0).rot("body", 10, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 8, 0, 60, 0).rot("tail", 12, 0, -150, 0, Ease.IN_CUBIC).rot("tail", 16, 0, -90, 0).rot("tail", 30, 0, 0, 0)
            .rot("tail2", 0, 0, 0, 0).rot("tail2", 8, 0, 40, 0).rot("tail2", 12, 0, -60, 0).rot("tail2", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, -1, 0).pos("body", 12, 0, 1.5f, 0).pos("body", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 30, 0, 0).rot("jaw", 24, 0, 0, 0);
        m.clip("spiken", 30)
            .scl("body", 0, 1, 1, 1).scl("body", 10, 0.88f, 0.9f, 0.88f).scl("body", 16, 1.22f, 1.2f, 1.22f, Ease.OUT_BACK).scl("body", 30, 1, 1, 1)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 8, 0, 0).rot("body", 16, -10, 0, 0, Ease.OUT_BACK).rot("body", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 16, 25, 0, 0).rot("jaw", 28, 0, 0, 0);
        // SPIKE SPIT: puff up, rock back, three quick spits
        m.clip("spike_spit", 36)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -22, 0, 0).rot("body", 12, 10, 0, 0, Ease.IN_CUBIC).rot("body", 16, -6, 0, 0).rot("body", 20, 10, 0, 0, Ease.IN_CUBIC)
            .rot("body", 24, -6, 0, 0).rot("body", 28, 10, 0, 0, Ease.IN_CUBIC).rot("body", 36, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 40, 0, 0).rot("jaw", 12, 55, 0, 0).rot("jaw", 14, 10, 0, 0).rot("jaw", 16, 40, 0, 0).rot("jaw", 20, 55, 0, 0).rot("jaw", 22, 10, 0, 0).rot("jaw", 25, 40, 0, 0).rot("jaw", 28, 55, 0, 0).rot("jaw", 36, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 8, 1.12f, 1.12f, 1.12f).scl("body", 30, 0.95f, 0.95f, 0.95f).scl("body", 36, 1, 1, 1);
        U.hurt(m, "body");
        m.clip("death", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -15, 0, 15).rot("body", 24, 0, 0, 180, Ease.OUT_BOUNCE).rot("body", 30, 0, 0, 180)
            .pos("body", 0, 0, 0, 0).pos("body", 24, 0, 3, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 24, 50, 0, 20).rot("legL", 0, 0, 0, 0).rot("legL", 24, -50, 0, -20)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 20, 40, 0, 0);
        return m;
    }

    public static MobDef def() {
        if (ProjKind.of("s2_toad_spike") == null) ProjKind.bolt("s2_toad_spike", 0xe6dca0, 6, 1).trail(ParticleTypes.CRIT).life(40);
        return MobDef.of("s2_toadpole", "Toadpole", "ヒキジャクシ")
            .game(2, 1).size(0.8f, 0.7f).hp(23).speed(0.24f).style(Style.SLIME).egg(0x6b8f4a, 0xe6dca0)
            .model(Toadpole::model)
            .spawn(8, 2, 3, MobDef.Habitat.SWAMP)
            .pattern(Pattern.cycle("whirl", "spiken", "spike_spit"))
            .onSpawn(s -> s.setCount("thorns", 0))
            .onHurt((s, src, amt) -> {
                if (s.count("thorns") > 0 && src.getDirectEntity() instanceof net.minecraft.world.entity.LivingEntity a && a != s && a.distanceTo(s) < 4) {
                    Fx.raw(s, a, s.count("thorns") * Cfg.DMG);
                    Fx.particlesAt(s, ParticleTypes.CRIT, a.position().add(0, 1, 0), 5, 0.3, 0.1);
                }
                return amt;
            })
            .move(Move.of("whirl", 30).intent(Intent.ATTACK, 7, 1).reach(2.2f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.1f)
                .at(12, Fx.melee(7, 2.8, 200)))
            .move(Move.of("spiken", 30).intent(Intent.BUFF).noTarget()
                .sound(14, SoundEvents.PUFFER_FISH_BLOW_UP, 1.2f, 0.9f)
                .at(16, (s, t) -> { s.setCount("thorns", s.count("thorns") + 2); s.setVar(s.count("thorns")); Fx.particles(s, ParticleTypes.CRIT, 14, 0.5); }))
            .move(Move.of("spike_spit", 36).intent(Intent.ATTACK, 3, 3).reach(8).ranged(8)
                .sound(12, SoundEvents.LLAMA_SPIT, 1f, 1.4f)
                .atAll(new int[]{12, 20, 28}, Fx.shoot("s2_toad_spike", 3, 1.4f))
                .at(29, (s, t) -> { s.setCount("thorns", Math.max(0, s.count("thorns") - 2)); s.setVar(s.count("thorns")); }));
    }
}
