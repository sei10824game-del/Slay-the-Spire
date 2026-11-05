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

    static final int AMB = 0xd4802a, AMB_L = 0xf0a850, AMB_D = 0x8a4a14, GLOW = 0xffe070, DARK = 0x2a1608, STEEL = 0x8a8f98;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("deca");
        Part root = m.add("root").at(0, 16, 0);
        Part core = root.add("core").at(0, 10, 0);
        Part cube = core.add("cube").at(0, 0, 0).rot(0, 45, 0);
        cube.cbox(-7, 14, 14, 14).color(AMB).grad(AMB_L, AMB_D).bevel(0.5f).noise(0.06f);
        cube.cbox(-1, 14, 2, 14).inflate(0.2f).color(AMB_D).flat();                              // seam band
        Part face = core.add("face").at(0, 0, 0);
        face.box(-5, -1.5f, 9.2f, 10, 3, 1).color(DARK).glow(Face.FRONT, 1, 1, 8, 1, GLOW);   // slit eye
        face.box(-6.5f, 3, 8.4f, 3, 3, 2).color(AMB_D).bevel(0.2f);
        face.box(3.5f, 3, 8.4f, 3, 3, 2).color(AMB_D).bevel(0.2f);
        Part crest = core.add("crest").at(0, 9, 0);
        for (int i = -1; i <= 1; i++) crest.box(i * 3.5f - 1, 0, -1, 2, 5 - Math.abs(i) * 1, 2).color(STEEL).flat().bevel(0.2f);
        Part under = core.add("under").at(0, -8, 0);
        under.cbox(-6, 6, 6, 6).color(AMB_D).bevel(0.3f);
        under.cbox(-10, 3, 4, 3).color(AMB).bevel(0.2f);
        // four square plates orbit the core
        for (int i = 0; i < 4; i++) {
            Part pl = root.add("plate" + i).at(0, 10, 0);
            Part sq = pl.add("sq" + i).at(0, 0, 15);
            sq.box(-4, -4, -1, 8, 8, 2).color(i % 2 == 0 ? AMB_L : STEEL).bevel(0.25f)
                .paint(Face.FRONT, 2, 2, 4, 4, i % 2 == 0 ? AMB_D : 0x5a5f68).glow(Face.FRONT, 3, 3, 2, 2, GLOW);
        }
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
