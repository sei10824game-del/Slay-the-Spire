package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 boss pair, part 2: Deca. 250 HP, Artifact 2, floats.
 * Turn 1 Beam (10 x2, +2 Dazed), then alternates Square of Protection (ALL enemies gain 16 Block) and Beam.
 * Look: a tumbling amber cube balanced on a corner with a slit eye, four orbiting square plates and a spiked crest.
 */
public final class Deca {
    private Deca() {}

    static final int ORANGE = 0xf0902c, ORANGE_D = 0xa8501c, ORANGE_L = 0xffc060, GLOW = 0xfff0c0, DARK = 0x4a2008;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("deca");
        // StS1 Deca: a gleaming orange crystal (octahedron) with sharp spikes underneath
        Part root = m.add("root").at(0, 10, 0);
        Part core = root.add("core").at(0, 14, 0);
        Part cube = core.add("cube").at(0, 0, 0);                                           // the crystal body (spun slowly by proc)
        int[] w = {4, 10, 16, 21, 24, 24, 21, 16, 10, 4};
        for (int i = 0; i < w.length; i++)
            cube.box(-w[i] / 2f, -15 + i * 3, -w[i] / 2f, w[i], 3, w[i]).color(ORANGE).grad(i < 5 ? ORANGE_L : ORANGE, i < 5 ? ORANGE : ORANGE_D).bevel(0.3f).noise(0.08f)
                .glow(Face.FRONT, 0, 0, w[i], 3, i < 5 ? ORANGE_L : ORANGE).glow(Face.LEFT, 0, 0, w[i], 3, ORANGE).glow(Face.RIGHT, 0, 0, w[i], 3, ORANGE).glow(Face.BACK, 0, 0, w[i], 3, ORANGE);
        Part face = core.add("face").at(0, 0, 0);
        face.box(-3, -1, 12.6f, 6, 3, 1).color(DARK).glow(Face.FRONT, 1, 1, 4, 1, GLOW);       // small glowing eye on the front
        for (int i = 0; i < 4; i++) {                                                         // four spike legs
            double ang = Math.toRadians(i * 90 + 45);
            Part leg = root.add("leg" + i).at((float) (Math.cos(ang) * 6), 0, (float) (Math.sin(ang) * 6));
            leg.box(-1.2f, -7, -1.2f, 3, 7, 3).color(ORANGE).grad(ORANGE_L, ORANGE_D).bevel(0.2f).glow(Face.FRONT, 0, 0, 3, 7, ORANGE);
            leg.box(-0.6f, -11, -0.6f, 1, 4, 1).color(ORANGE_L).flat();
        }
        for (int i = 0; i < 4; i++) {                                                         // four square plates orbit the crystal
            Part pl = root.add("plate" + i).at(0, 14, 0);
            Part sq = pl.add("sq" + i).at(0, 0, 18);
            sq.box(-4, -4, -1, 8, 8, 2).color(i % 2 == 0 ? ORANGE_L : 0xb0b4bc).bevel(0.25f)
                .paint(Face.FRONT, 2, 2, 4, 4, ORANGE_D).glow(Face.FRONT, 3, 3, 2, 2, GLOW);
        }
        Part tail = root.add("tail").at(0, 3, 0); tail.box(-0.5f, -0.5f, -0.5f, 1, 1, 1).color(ORANGE_D);
        Part ring = root.add("ring").at(0, 10, 0); ring.box(-0.5f, -0.5f, -0.5f, 1, 1, 1).color(ORANGE_D);
        m.bob(1.0f, 0.06f);
        m.proc((rig, c) -> {
            boolean sq = c.move != null && c.move.equals("square_of_protection");
            float sp = c.age * (sq ? 8f : 2.5f);
            for (int i = 0; i < 4; i++) rig.rot("plate" + i, 0, sp + i * 90, 0);
            rig.rot("cube", (float) Math.sin(c.age * 0.04f) * 10, 45 + c.age * 0.6f, (float) Math.cos(c.age * 0.05f) * 6);
        });
        m.clip("idle", 40).loop().rot("core", 0, 0, 0, 0).rot("core", 20, 0, -4, 0).rot("core", 40, 0, 0, 0);
        m.clip("walk", 20).loop().rot("root", 0, 8, 0, 0).rot("root", 20, 8, 0, 0);
        // SQUARE OF PROTECTION: plates swing out into a wide square, body flares
        m.clip("square_of_protection", 46)
            .pos("root", 0, 0, 0, 0).pos("root", 10, 0, -3, 0).pos("root", 22, 0, 6, 0, Ease.OUT_BACK).pos("root", 46, 0, 0, 0)
            .scl("core", 0, 1, 1, 1).scl("core", 10, 1.15f, 0.85f, 1.15f).scl("core", 22, 0.9f, 1.2f, 0.9f, Ease.OUT_BACK).scl("core", 46, 1, 1, 1)
            .pos("sq0", 0, 0, 0, 0).pos("sq0", 22, 0, 0, 14, Ease.OUT_BACK).pos("sq0", 46, 0, 0, 0)
            .pos("sq1", 0, 0, 0, 0).pos("sq1", 22, 0, 0, 14, Ease.OUT_BACK).pos("sq1", 46, 0, 0, 0)
            .pos("sq2", 0, 0, 0, 0).pos("sq2", 22, 0, 0, 14, Ease.OUT_BACK).pos("sq2", 46, 0, 0, 0)
            .pos("sq3", 0, 0, 0, 0).pos("sq3", 22, 0, 0, 14, Ease.OUT_BACK).pos("sq3", 46, 0, 0, 0)
            .scl("sq0", 0, 1, 1, 1).scl("sq0", 22, 1.6f, 1.6f, 1).scl("sq0", 46, 1, 1, 1)
            .scl("sq1", 0, 1, 1, 1).scl("sq1", 22, 1.6f, 1.6f, 1).scl("sq1", 46, 1, 1, 1)
            .scl("sq2", 0, 1, 1, 1).scl("sq2", 22, 1.6f, 1.6f, 1).scl("sq2", 46, 1, 1, 1)
            .scl("sq3", 0, 1, 1, 1).scl("sq3", 22, 1.6f, 1.6f, 1).scl("sq3", 46, 1, 1, 1);
        // BEAM: rear back, stab forward twice with the slit eye blazing
        m.clip("beam", 40)
            .rot("core", 0, 0, 0, 0).rot("core", 10, -22, 0, 0).rot("core", 14, 24, 0, 0, Ease.IN_CUBIC).rot("core", 20, 6, 0, 0).rot("core", 24, 28, 0, 0, Ease.IN_CUBIC).rot("core", 40, 0, 0, 0)
            .pos("core", 0, 0, 0, 0).pos("core", 10, 0, 0, -3).pos("core", 14, 0, 0, 3, Ease.IN_CUBIC).pos("core", 20, 0, 0, -1).pos("core", 24, 0, 0, 4, Ease.IN_CUBIC).pos("core", 40, 0, 0, 0)
            .scl("face", 0, 1, 1, 1).scl("face", 14, 1.1f, 1.8f, 1).scl("face", 24, 1.1f, 1.8f, 1).scl("face", 40, 1, 1, 1);
        m.clip("hurt", 10).rot("core", 0, 0, 0, 0).rot("core", 3, -12, 0, -8).rot("core", 10, 0, 0, 0);
        m.clip("death", 36)
            .rot("core", 0, 0, 0, 0).rot("core", 36, -30, 0, -40)
            .pos("root", 0, 0, 0, 0).pos("root", 36, 0, -16, 0, Ease.OUT_BOUNCE)
            .scl("core", 0, 1, 1, 1).scl("core", 36, 1.2f, 0.45f, 1.2f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("deca", "Deca", "デカ")
            .game(1, 3).size(1.5f, 2.6f).hp(250).speed(0.18f).flying().kbResist(0.9f).style(Style.METAL).egg(0xd4802a, 0x2a1608)
            .model(Deca::model).boss()
            .onSpawn(s -> {
                s.setCount("artifact", 2);
            })
            .onTick(S3::artifact)
            .pattern(Pattern.cycle("beam", "square_of_protection"))
            .move(Move.of("beam", 40).intent(Intent.ATTACK_DEBUFF, 10, 2).ranged(14)
                .sound(8, SoundEvents.BEACON_AMBIENT, 1.2f, 1.4f)
                .at(14, Fx.hitTarget(10)).at(14, Donu.beamFx()).at(14, Fx.sfx(SoundEvents.GUARDIAN_ATTACK, 1f, 1.2f))
                .at(24, S3.multi(Fx.hitTarget(10, v -> Fx.curse(v, Fx.Curse.DAZED)))).at(24, Donu.beamFx()).at(24, Fx.sfx(SoundEvents.GUARDIAN_ATTACK, 1f, 1.2f)))
            .move(Move.of("square_of_protection", 46).intent(Intent.DEFEND).noTarget()
                .sound(8, SoundEvents.BEACON_POWER_SELECT, 1.2f, 0.9f)
                .at(22, (s, t) -> {
                    for (SpireMob a : S3.allies(s, 20)) { a.addBlock(16); Fx.particlesAt(s, ParticleTypes.WAX_ON, a.position().add(0, a.getBbHeight() * 0.6, 0), 18, 0.6, 0.05); }
                    Fx.ring(s, ParticleTypes.END_ROD, s.position().add(0, 0.5, 0), 3.0, 40);
                    Fx.sound(s, SoundEvents.ANVIL_PLACE, 1f, 0.7f);
                }));
    }
}
