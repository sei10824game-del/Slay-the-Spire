package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 boss pair, part 1: Donu. 250 HP, Artifact 2, floats.
 * Turn 1 Circle of Power (ALL enemies gain 3 Strength), then alternates Beam (10 x2) and Circle of Power.
 * Look: a teal faceted orb with one cyan eye, ringed by a spinning octagonal halo, hovering over a hanging crystal tail.
 */
public final class Donu {
    private Donu() {}

    static final int GREEN = 0x78cc78, GREEN_D = 0x3c8a4a, GREEN_L = 0xa8ecb0, GOLD = 0xd8b04a, GLOW = 0xe8ffe0, DARK = 0x143a1c;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("donu");
        // StS1 Donu: a fat green ring (donut) with a bulging side wall, a small mouth-eye and four stubby legs
        Part root = m.add("root").at(0, 8, 0);
        Part core = root.add("core").at(0, 16, 0);
        // the torus stands like a wheel facing the front; a real hole goes through it
        core.box(-14, -14, -7, 28, 7, 14).color(GREEN).grad(GREEN_L, GREEN_D).bevel(1.2f).noise(0.1f).glow(Face.FRONT, 0, 0, 28, 7, GREEN).glow(Face.TOP, 0, 0, 28, 14, GREEN_L);   // bottom
        core.box(-14, 7, -7, 28, 7, 14).color(GREEN).grad(GREEN_L, GREEN_D).bevel(1.2f).noise(0.1f).glow(Face.FRONT, 0, 0, 28, 7, GREEN_L).glow(Face.TOP, 0, 0, 28, 14, GREEN_L);    // top
        core.box(-14, -7, -7, 7, 14, 14).color(GREEN).grad(GREEN_L, GREEN_D).bevel(1.2f).noise(0.1f).glow(Face.FRONT, 0, 0, 7, 14, GREEN).glow(Face.LEFT, 0, 0, 14, 14, GREEN);     // left
        core.box(7, -7, -7, 7, 14, 14).color(GREEN).grad(GREEN_L, GREEN_D).bevel(1.2f).noise(0.1f).glow(Face.FRONT, 0, 0, 7, 14, GREEN).glow(Face.RIGHT, 0, 0, 14, 14, GREEN);     // right
        core.box(-3, -12, 7.2f, 6, 2, 1).color(DARK).glow(Face.FRONT, 1, 0, 4, 1, GLOW);     // tiny eye on the lower rim
        for (int i = 0; i < 4; i++) {                                           // stubby legs
            double ang = Math.toRadians(i * 90 + 45);
            Part leg = root.add("leg" + i).at((float) (Math.cos(ang) * 9), 0, (float) (Math.sin(ang) * 9));
            leg.box(-1.5f, -6, -1.5f, 3, 6, 3).color(GREEN_D).bevel(0.3f);
            leg.box(-2.5f, -8, -2.5f, 5, 2, 5).color(GREEN_D).bevel(0.3f);
        }
        Part tail = root.add("tail").at(0, 3, 0); tail.box(-0.5f, -0.5f, -0.5f, 1, 1, 1).color(GREEN_D);
        Part ring = root.add("ring").at(0, 10, 0); ring.box(-0.5f, -0.5f, -0.5f, 1, 1, 1).color(GREEN_D);
        m.bob(1.0f, 0.06f);
        m.proc((rig, c) -> {
            float spin = c.age * (c.move != null && c.move.equals("circle_of_power") ? 14f : 3f);
            rig.rot("ring", 25 + (float) Math.sin(c.age * 0.05f) * 6, spin, 0);
            rig.rot("tail", (float) Math.sin(c.age * 0.08f) * 6, 0, (float) Math.cos(c.age * 0.07f) * 5);
        });
        m.clip("idle", 40).loop().rot("core", 0, 0, 0, 0).rot("core", 20, 0, 4, 0).rot("core", 40, 0, 0, 0);
        m.clip("walk", 20).loop().rot("root", 0, 8, 0, 0).rot("root", 20, 8, 0, 0);
        // CIRCLE OF POWER: sink, bloom upward, ring expands and flares
        m.clip("circle_of_power", 44)
            .pos("root", 0, 0, 0, 0).pos("root", 10, 0, -3, 0).pos("root", 20, 0, 5, 0, Ease.OUT_BACK).pos("root", 44, 0, 0, 0)
            .scl("core", 0, 1, 1, 1).scl("core", 10, 1.12f, 0.88f, 1.12f).scl("core", 20, 0.92f, 1.18f, 0.92f, Ease.OUT_BACK).scl("core", 44, 1, 1, 1)
            .scl("ring", 0, 1, 1, 1).scl("ring", 10, 0.85f, 1, 0.85f).scl("ring", 22, 1.5f, 1, 1.5f, Ease.OUT_BACK).scl("ring", 44, 1, 1, 1)
            .rot("core", 0, 0, 0, 0).rot("core", 20, -15, 0, 0, Ease.OUT_BACK).rot("core", 44, 0, 0, 0);
        // BEAM: tip forward and charge, fire twice with recoil
        m.clip("beam", 40)
            .rot("core", 0, 0, 0, 0).rot("core", 10, -20, 0, 0).rot("core", 14, 22, 0, 0, Ease.IN_CUBIC).rot("core", 20, 6, 0, 0).rot("core", 24, 26, 0, 0, Ease.IN_CUBIC).rot("core", 40, 0, 0, 0)
            .pos("core", 0, 0, 0, 0).pos("core", 10, 0, 0, -3).pos("core", 14, 0, 0, 3, Ease.IN_CUBIC).pos("core", 20, 0, 0, -1).pos("core", 24, 0, 0, 4, Ease.IN_CUBIC).pos("core", 40, 0, 0, 0)
            .scl("core", 0, 1, 1, 1).scl("core", 10, 1.1f, 1.1f, 0.9f).scl("core", 14, 0.95f, 0.95f, 1.2f).scl("core", 40, 1, 1, 1)
            .rot("tail", 0, 0, 0, 0).rot("tail", 14, -25, 0, 0).rot("tail", 24, -30, 0, 0).rot("tail", 40, 0, 0, 0);
        m.clip("hurt", 10).rot("core", 0, 0, 0, 0).rot("core", 3, -12, 0, 8).rot("core", 10, 0, 0, 0);
        m.clip("death", 36)
            .rot("core", 0, 0, 0, 0).rot("core", 36, 40, 0, 30)
            .pos("root", 0, 0, 0, 0).pos("root", 36, 0, -16, 0, Ease.OUT_BOUNCE)
            .scl("ring", 0, 1, 1, 1).scl("ring", 18, 1.6f, 1, 1.6f).scl("ring", 36, 0.1f, 1, 0.1f)
            .scl("core", 0, 1, 1, 1).scl("core", 36, 1.2f, 0.4f, 1.2f);
        return m;
    }

    static Move.Action beamFx() {
        return (s, t) -> { for (int i = 1; i <= 9; i++) Fx.particlesAt(s, ParticleTypes.END_ROD, Fx.rel(s, i * 0.9, 0, 1.4), 2, 0.05, 0); };
    }

    public static MobDef def() {
        return MobDef.of("donu", "Donu", "ドヌ")
            .game(1, 3).size(1.5f, 2.6f).hp(250).speed(0.18f).flying().kbResist(0.9f).style(Style.METAL).egg(0x78cc78, 0xa8ecb0)
            .model(Donu::model).boss()
            .onSpawn(s -> {
                s.setCount("artifact", 2);
                if (!s.flags.contains("paired") && Fx.countAlive(s, "deca", 12) == 0) { s.flags.add("paired"); SpireMob d = Fx.summon(s, "deca", 5, 0); if (d != null) d.flags.add("paired"); }
            })
            .onTick(S3::artifact)
            .pattern(Pattern.cycle("circle_of_power", "beam"))
            .move(Move.of("circle_of_power", 44).intent(Intent.BUFF).noTarget()
                .sound(8, SoundEvents.BEACON_POWER_SELECT, 1.2f, 0.6f)
                .at(20, (s, t) -> {
                    for (SpireMob a : S3.allies(s, 20)) { a.addStrength(3); Fx.particlesAt(s, ParticleTypes.ANGRY_VILLAGER, a.position().add(0, a.getBbHeight() * 0.7, 0), 8, 0.4, 0.05); }
                    Fx.ring(s, ParticleTypes.END_ROD, s.position().add(0, 0.5, 0), 3.0, 40);
                    Fx.sound(s, SoundEvents.BEACON_ACTIVATE, 1.2f, 0.8f);
                }))
            .move(Move.of("beam", 40).intent(Intent.ATTACK, 10, 2).ranged(14)
                .sound(8, SoundEvents.BEACON_AMBIENT, 1.2f, 1.6f)
                .at(14, Fx.hitTarget(10)).at(14, beamFx()).at(14, Fx.sfx(SoundEvents.GUARDIAN_ATTACK, 1f, 1.4f))
                .at(24, S3.multi(Fx.hitTarget(10))).at(24, beamFx()).at(24, Fx.sfx(SoundEvents.GUARDIAN_ATTACK, 1f, 1.4f)));
    }
}
