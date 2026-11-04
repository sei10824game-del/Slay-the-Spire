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
    static final int METAL = 0x7a8088, METAL_D = 0x3a3e46, METAL_L = 0xaeb6bc, EYE = 0xffa020;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("sentry");
        Part core = m.add("core").at(0, 13, 0);
        core.box(-4, -9, -4, 8, 18, 8).color(METAL).grad(METAL_L, METAL_D).bevel(0.15f).noise(0.07f)
            .paint(Face.FRONT, 0, 5, 8, 1, METAL_D).paint(Face.FRONT, 0, 12, 8, 1, METAL_D);
        core.box(-4.5f, 8, -4.5f, 9, 2, 9).color(METAL_D).bevel(0.2f);                       // cap
        core.box(-1, 10, -1, 2, 3, 2).color(METAL_L).flat();                                 // antenna
        core.box(-4.5f, -10, -4.5f, 9, 2, 9).color(METAL_D).bevel(0.2f);
        for (int i = 0; i < 3; i++) core.box(-3 + i * 3, -14, -0.5f, 2, 4, 1).color(METAL_L).flat();   // underside prongs
        Part eye = core.add("eye").at(0, 2, 4);
        eye.box(-3, -3, -0.5f, 6, 6, 1).color(0x281810).bevel(0.2f)
            .glow(Face.FRONT, 1, 1, 4, 4, EYE).glow(Face.FRONT, 2, 2, 2, 2, 0xfff0b0);
        Part lid = eye.add("lid").at(0, 3, 0.6f);
        lid.box(-3, -1.5f, 0, 6, 2, 1).color(METAL_D).bevel(0.2f);
        for (int s = -1; s <= 1; s += 2) {
            Part w = core.add(s < 0 ? "wingR" : "wingL").at(s * 4.5f, 4, 0);
            w.box(s < 0 ? -2 : 0, -10, -3, 2, 16, 6).color(METAL).grad(METAL_L, METAL_D).bevel(0.15f);
            w.box(s < 0 ? -4 : 2, -6, -2, 2, 10, 4).color(METAL_D).bevel(0.15f).grad(METAL, METAL_D);
            w.box(s < 0 ? -5 : 4, -3, -0.5f, 1, 4, 1).color(METAL_L).flat();
            w.box(s < 0 ? -2.5f : 2, -9, 2.8f, 1, 14, 1).color(EYE).flat().glow(Face.FRONT, 0, 0, 1, 14, EYE);   // light strip
        }
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
            .game(1, 1).size(1.0f, 1.9f).hp(40).speed(0.15f).style(Style.METAL).elite().flying().egg(0x7a8088, 0xffa020)
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
