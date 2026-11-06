package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 1 Elite Sentry (appears in 3s): 38-42 HP, Artifact 1 (not modelled). Alternates Bolt (2 Dazed) and Beam (9).
 * Random starting move stands in for the staggered start of the three sentries.
 */
public final class Sentry {
    private Sentry() {}
    static final int SAND = 0xd8c68a, SAND_D = 0x8a7a48, SAND_L = 0xf2e6b4, EYE = 0x6ad0ff;

    public static ModelSpec model() {
        // StS1 Sentry: two sand-coloured pyramids (upper points up, lower is an open inverted cup ending in a spike) hovering around a glowing blue orb
        ModelSpec m = new ModelSpec("sentry").scale(0.65f);
        Part core = m.add("core").at(0, 24, 0);
        Part eye = core.add("eye").at(0, 0, 0);
        eye.box(-3, -3, -3, 6, 6, 6).color(0x3a9ae8).grad(0x9ae0ff, 0x2a70c0).bevel(0.5f)
            .glow(Face.FRONT, 0, 0, 6, 6, 0x6ad0ff).glow(Face.LEFT, 0, 0, 6, 6, 0x4ab0f0).glow(Face.RIGHT, 0, 0, 6, 6, 0x4ab0f0).glow(Face.TOP, 0, 0, 6, 6, 0xb0f0ff)
            .glow(Face.BACK, 0, 0, 6, 6, 0x4ab0f0).glow(Face.BOTTOM, 0, 0, 6, 6, 0x2a80d0);
        eye.box(-1.5f, 0, 3, 3, 2, 1).color(0xe8ffff).flat().glow(Face.FRONT, 0, 0, 3, 2, 0xe8ffff);          // bright pupil highlight
        Part lid = eye.add("lid").at(0, 3, 0);
        lid.box(-1, 0, 0, 1, 1, 1).color(0x3a9ae8).flat();
        int[] pw = {16, 14, 12, 10, 8, 6, 4, 2};
        Part up = core.add("wingR").at(0, 4, 0);                                                                // upper pyramid, point up
        for (int i = 0; i < pw.length; i++)
            up.box(-pw[i] / 2f, i * 3, -pw[i] / 2f, pw[i], 3, pw[i]).color(SAND).grad(SAND_L, SAND_D).bevel(0.15f).noise(0.12f)
                .paint(Face.FRONT, 0, 0, pw[i], 1, SAND_D).paint(Face.LEFT, 0, 0, pw[i], 1, SAND_D);
        Part low = core.add("wingL").at(0, -4, 0);                                                              // lower pyramid: open cup, narrowing to a spike
        for (int i = 0; i < pw.length; i++)
            low.box(-pw[i] / 2f, -3 - i * 3, -pw[i] / 2f, pw[i], 3, pw[i]).color(SAND_D).grad(SAND, SAND_D).bevel(0.15f).noise(0.12f);
        low.box(-8, -1, -8, 16, 1, 2).color(SAND_L).bevel(0.1f); low.box(-8, -1, 6, 16, 1, 2).color(SAND_L).bevel(0.1f);        // cup rim
        low.box(-8, -1, -6, 2, 1, 12).color(SAND_L).bevel(0.1f); low.box(6, -1, -6, 2, 1, 12).color(SAND_L).bevel(0.1f);
        m.look("core", 60, 25).bob(1.2f, 0.12f);
        m.clip("idle", 40).loop()
            .rot("wingR", 0, 0, 0, 8).rot("wingR", 20, 0, 0, 14).rot("wingR", 40, 0, 0, 8)
            .rot("wingL", 0, 0, 0, -8).rot("wingL", 20, 0, 0, -14).rot("wingL", 40, 0, 0, -8)
            .rot("core", 0, 0, 0, 0).rot("core", 20, 0, 4, 0).rot("core", 40, 0, 0, 0)
            .scl("eye", 0, 1, 1, 1).scl("eye", 28, 1, 1, 1).scl("eye", 32, 1.1f, 1.1f, 1.1f).scl("eye", 36, 1, 1, 1);
        // BOLT: wings clamp in, eye flares, then wings snap wide as a flash fires toward the target
        Clip b = m.clip("bolt", 32);
        K.r(b, "wingR", 0, 0, 0, 8, 10, 0, 0, -12, 114, 0, 40, 40, 24, 0, 30, 25, 32, 0, 0, 8);
        K.r(b, "wingL", 0, 0, 0, -8, 10, 0, 0, 12, 114, 0, -40, -40, 24, 0, -30, -25, 32, 0, 0, -8);
        K.r(b, "core", 0, 0, 0, 0, 10, -12, 0, 0, 114, 14, 0, 0, 32, 0, 0, 0);
        K.s(b, "eye", 0, 1, 1, 1, 10, 1.3f, 1.3f, 1.3f, 14, 0.8f, 0.8f, 0.8f, 32, 1, 1, 1);
        K.p(b, "core", 0, 0, 0, 0, 10, 0, 0, -2, 114, 0, 0, 3, 32, 0, 0, 0);
        K.r(b, "lid", 0, 0, 0, 0, 8, 40, 0, 0, 14, 0, 0, 0);
        // BEAM: wings sweep back like a charging dish, core tips up, eye swells, sustained blast, then sag
        Clip bm = m.clip("beam", 38);
        K.r(bm, "wingR", 0, 0, 0, 8, 14, 30, 50, 35, 26, 30, 50, 35, 38, 0, 0, 8);
        K.r(bm, "wingL", 0, 0, 0, -8, 14, 30, -50, -35, 26, 30, -50, -35, 38, 0, 0, -8);
        K.r(bm, "core", 0, 0, 0, 0, 14, -18, 0, 0, 114, 6, 0, 0, 30, 6, 0, 0, 38, 0, 0, 0);
        K.s(bm, "eye", 0, 1, 1, 1, 14, 1.5f, 1.5f, 1.5f, 18, 1.2f, 1.2f, 1.2f, 30, 1.6f, 1.6f, 1.6f, 38, 1, 1, 1);
        K.p(bm, "core", 0, 0, 0, 0, 14, 0, 1.5f, -2, 114, 0, 0, 2, 38, 0, 0, 0);
        m.clip("hurt", 10).rot("core", 0, 0, 0, 0).rot("core", 3, 15, 0, 12).rot("core", 10, 0, 0, 0)
            .rot("wingR", 0, 0, 0, 8).rot("wingR", 3, 0, 0, -20).rot("wingR", 10, 0, 0, 8)
            .rot("wingL", 0, 0, 0, -8).rot("wingL", 3, 0, 0, 20).rot("wingL", 10, 0, 0, -8);
        Clip d = m.clip("death", 28);
        K.r(d, "core", 0, 0, 0, 0, 10, 20, 0, 20, 28, 80, 0, 50);
        K.p(d, "core", 0, 0, 0, 0, 14, 0, -3, 0, 28, 0, -11, 0);
        K.r(d, "wingR", 0, 0, 0, 8, 10, 0, 0, 50, 28, 0, 0, 80);
        K.r(d, "wingL", 0, 0, 0, -8, 10, 0, 0, -50, 28, 0, 0, -80);
        K.s(d, "eye", 0, 1, 1, 1, 6, 1.4f, 1.4f, 1.4f, 14, 0.2f, 0.2f, 0.2f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("sentry", "Sentry", "セントリー")
            .game(1, 1).size(1.0f, 1.8f).hp(40).speed(0.15f).style(Style.METAL).elite().flying().egg(0xd8c68a, 0x3a9ae8)
            .model(Sentry::model).spawn(2, 1, 3, MobDef.Habitat.MOUNTAIN)
            .onSpawn(s -> s.setVar(s.rand().nextInt(2)))
            .pattern(s -> s.getTurn() == 0 ? (s.getVar() == 0 ? "bolt" : "beam") : s.lastMove().equals("bolt") ? "beam" : "bolt")
            .move(Move.of("bolt", 32).intent(Intent.DEBUFF).ranged(12)
                .sound(8, SoundEvents.BEACON_POWER_SELECT, 1f, 1.8f)
                .at(15, (s, t) -> {
                    if (t == null) return;
                    Fx.curse(t, Fx.Curse.DAZED);
                    for (int i = 1; i <= 8; i++) Fx.particlesAt(s, ParticleTypes.ELECTRIC_SPARK, s.getEyePosition().lerp(t.getEyePosition(), i / 8.0), 3, 0.1, 0.02);
                    Fx.sound(s, SoundEvents.AMETHYST_BLOCK_RESONATE, 1.2f, 0.7f);
                }))
            .move(Move.of("beam", 38).intent(Intent.ATTACK, 9, 1).ranged(14)
                .sound(8, SoundEvents.BEACON_ACTIVATE, 1f, 1.6f)
                .at(18, Fx.hitTarget(9, v -> { }))
                .at(18, (s, t) -> {
                    if (t == null) return;
                    for (int i = 1; i <= 14; i++) Fx.particlesAt(s, ParticleTypes.FLAME, s.getEyePosition().lerp(t.getEyePosition(), i / 14.0), 2, 0.05, 0.0);
                    Fx.sound(s, SoundEvents.GUARDIAN_ATTACK, 1.2f, 1.2f);
                }));
    }
}
