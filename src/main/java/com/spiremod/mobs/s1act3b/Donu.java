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

    static final int TEAL = 0x2f8fa8, TEAL_D = 0x1b5468, TEAL_L = 0x58bfd8, GOLD = 0xd8b04a, GLOW = 0x9ffcff, DARK = 0x10282f;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("donu");
        Part root = m.add("root").at(0, 16, 0);
        Part core = root.add("core").at(0, 10, 0);
        core.cbox(-7, 14, 14, 14).color(TEAL).grad(TEAL_L, TEAL_D).bevel(0.5f).noise(0.06f);
        core.cbox(-6, 16, 12, 12).color(TEAL).bevel(0.4f).noise(0.06f);                         // chamfered "sphere"
        core.cbox(-4, 12, 8, 16).color(TEAL).bevel(0.4f);
        core.box(-4, -2, 7.2f, 8, 4, 1).color(DARK).glow(Face.FRONT, 2, 1, 4, 2, GLOW).paint(Face.FRONT, 0, 0, 8, 1, 0x061418);   // the eye
        core.box(-2, 7, -2, 4, 3, 4).color(GOLD).flat();                                       // top cap
        core.box(-1, 10, -1, 2, 4, 2).color(GOLD).flat();
        // hanging crystal tail
        Part tail = root.add("tail").at(0, 3, 0);
        tail.cbox(-6, 8, 6, 8).color(TEAL_D).grad(TEAL, TEAL_D).bevel(0.4f);
        tail.cbox(-12, 5, 6, 5).color(TEAL_D).bevel(0.4f);
        tail.cbox(-18, 3, 6, 3).color(TEAL_L).glow(Face.FRONT, 1, 1, 1, 3, GLOW).bevel(0.3f);
        // the halo: eight bars on an octagon, tilted, spun by proc
        Part ring = root.add("ring").at(0, 10, 0).rot(25, 0, 0);
        for (int i = 0; i < 8; i++) {
            double a = Math.toRadians(i * 45);
            Part bar = ring.add("bar" + i).at((float) (Math.cos(a) * 13), 0, (float) (Math.sin(a) * 13)).rot(0, (float) -(i * 45 + 90), 0);
            bar.box(-5, -1.25f, -1.25f, 10, 3, 3).color(i % 2 == 0 ? GOLD : TEAL_L).bevel(0.2f);
            if (i % 2 == 0) bar.box(-1, -1, -2.2f, 2, 2, 1).color(GLOW).flat();
        }
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
            .game(1, 3).size(1.5f, 2.6f).hp(250).speed(0.18f).flying().kbResist(0.9f).style(Style.METAL).egg(0x2f8fa8, 0xd8b04a)
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
