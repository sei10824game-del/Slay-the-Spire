package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Sewer Clam (56 HP). Plating 8 (Block regained each turn, shrinking by 1).
 * Jet (10) first, then Pressurize (+4 Strength, coral grows) / Jet alternating.
 */
public final class SewerClam {
    private SewerClam() {}
    static final int SHELL = 0x6e7a6c, SHELL_D = 0x3f4a42, SHELL_L = 0x93a08c, FLESH = 0xc98a8a, CORAL = 0xe0707a, CORAL_L = 0xf5a0a0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_sewer_clam");
        Part bottom = m.add("bottom").at(0, 0, 0);
        bottom.box(-8, 0, -7, 16, 4, 14).color(SHELL).grad(SHELL_L, SHELL_D).noise(0.2f).bevel(0.3f);
        bottom.box(-7, 4, -6, 14, 1, 12).color(FLESH).noise(0.1f);                                     // soft inside
        for (int i = 0; i < 4; i++) bottom.box(-8.4f, 0.5f + (i % 2), -5 + i * 3, 1, 3, 2).color(SHELL_D).noise(0.2f); // ribs
        bottom.box(-3, 0, 6, 6, 3, 2).color(0x59606a).noise(0.2f);                                     // muddy foot
        Part tongue = bottom.add("tongue").at(0, 5, 0);
        tongue.box(-3, 0, -2, 6, 2, 6).color(0xd9a0a0).grad(0xefbcbc, 0xc98a8a).bevel(0.3f);
        for (int s = -1; s <= 1; s += 2) {
            Part eye = bottom.add(s < 0 ? "eyeR" : "eyeL").at(s * 3.5f, 5, 4);
            eye.box(-0.5f, 0, -0.5f, 1, 4, 1).color(FLESH);
            eye.box(-1.5f, 4, -1.5f, 3, 3, 3).color(0xf2ecd0).bevel(0.2f).glow(Face.FRONT, 1, 1, 1, 1, 0xfff4a0).paint(Face.FRONT, 1, 1, 1, 1, 0x101010);
        }
        Part lid = m.add("lid").at(0, 5, -7);
        lid.box(-8, 0, 0, 16, 5, 14).color(SHELL).grad(SHELL_D, SHELL_L).noise(0.2f).bevel(0.3f);
        lid.box(-8.4f, 0.5f, 1, 1, 3, 12).color(SHELL_D).noise(0.2f);
        lid.box(7.4f, 0.5f, 1, 1, 3, 12).color(SHELL_D).noise(0.2f);
        for (int i = 0; i < 5; i++) lid.box(-7 + i * 3, 5, 1, 1, 1, 12).color(SHELL_L).noise(0.15f); // growth rings
        Part coral = lid.add("coral").at(0, 5, 7);
        for (int i = 0; i < 5; i++) {
            Part c = coral.add("coral" + i).at(-4 + i * 2, 0, (i % 2) * 2 - 1).rot(0, 0, (i - 2) * 12);
            c.box(-1, 0, -1, 2, 3 + (i % 3) * 2, 2).color(i % 2 == 0 ? CORAL : CORAL_L).noise(0.1f).bevel(0.2f);
            c.box(-2, 4 + (i % 3) * 2, -1, 4, 1, 2).color(CORAL_L).bevel(0.15f);
        }
        m.proc((r, c) -> { float g = 1 + 0.18f * Math.min(c.var, 6); r.scl("coral", g, g, g); });
        m.clip("idle", 50).loop()
            .rot("lid", 0, 0, 0, 0).rot("lid", 25, -4, 0, 0).rot("lid", 50, 0, 0, 0)
            .pos("tongue", 0, 0, 0, 0).pos("tongue", 25, 0, 0.6f, 0.6f).pos("tongue", 50, 0, 0, 0)
            .rot("eyeR", 0, 0, 0, 0).rot("eyeR", 15, 8, 0, 6).rot("eyeR", 35, -5, 0, -4).rot("eyeR", 50, 0, 0, 0)
            .rot("eyeL", 0, 0, 0, 0).rot("eyeL", 20, 6, 0, -6).rot("eyeL", 40, -6, 0, 4).rot("eyeL", 50, 0, 0, 0);
        m.clip("walk", 16).loop()
            .pos("bottom", 0, 0, 0, 0).pos("bottom", 4, 0, 1.5f, 1).pos("bottom", 8, 0, 0, 2).pos("bottom", 12, 0, 1.5f, 1).pos("bottom", 16, 0, 0, 0)
            .rot("lid", 0, 0, 0, 0).rot("lid", 4, -10, 0, 0).rot("lid", 8, 0, 0, 0).rot("lid", 12, -10, 0, 0).rot("lid", 16, 0, 0, 0);
        // JET: clamp shut and shudder, snap the shell wide and blast a water jet
        m.clip("jet", 34)
            .rot("lid", 0, 0, 0, 0).rot("lid", 6, 4, 0, 0).rot("lid", 12, -12, 0, 0).rot("lid", 15, -55, 0, 0, Ease.OUT_BACK).rot("lid", 26, -40, 0, 0).rot("lid", 34, 0, 0, 0)
            .rot("bottom", 0, 0, 0, 0).rot("bottom", 12, 0, 0, 2).rot("bottom", 14, 0, 0, -2).rot("bottom", 16, -8, 0, 0, Ease.OUT_BACK).rot("bottom", 34, 0, 0, 0)
            .pos("tongue", 0, 0, 0, 0).pos("tongue", 15, 0, 1, 4, Ease.OUT_BACK).pos("tongue", 26, 0, 0, 2).pos("tongue", 34, 0, 0, 0)
            .scl("tongue", 15, 1.2f, 1.3f, 1.4f).scl("tongue", 34, 1, 1, 1)
            .pos("bottom", 0, 0, 0, 0).pos("bottom", 12, 0, 0, -1).pos("bottom", 16, 0, 0, -3).pos("bottom", 34, 0, 0, 0);
        // PRESSURIZE: shell clamps and vibrates harder and harder, bulges, coral swells
        m.clip("pressurize", 38)
            .rot("lid", 0, 0, 0, 0).rot("lid", 6, 5, 0, 0).rot("lid", 10, 5, 0, 3).rot("lid", 12, 5, 0, -3).rot("lid", 14, 5, 0, 3).rot("lid", 16, 5, 0, -3).rot("lid", 18, 5, 0, 3).rot("lid", 20, 5, 0, -3)
            .rot("lid", 26, -22, 0, 0, Ease.OUT_BACK).rot("lid", 38, 0, 0, 0)
            .scl("bottom", 0, 1, 1, 1).scl("bottom", 20, 1.12f, 1.08f, 1.12f).scl("bottom", 26, 1.2f, 1.1f, 1.2f, Ease.OUT_BACK).scl("bottom", 38, 1, 1, 1)
            .scl("coral", 0, 1, 1, 1).scl("coral", 26, 1.4f, 1.4f, 1.4f, Ease.OUT_BACK).scl("coral", 38, 1, 1, 1)
            .pos("bottom", 0, 0, 0, 0).pos("bottom", 10, 0.5f, 0, 0).pos("bottom", 12, -0.5f, 0, 0).pos("bottom", 14, 0.5f, 0, 0).pos("bottom", 16, -0.5f, 0, 0).pos("bottom", 18, 0.5f, 0, 0).pos("bottom", 20, 0, 0, 0);
        m.clip("hurt", 10).rot("lid", 0, 0, 0, 0).rot("lid", 3, -18, 0, 0).rot("lid", 10, 0, 0, 0)
            .rot("bottom", 0, 0, 0, 0).rot("bottom", 3, 6, 0, 4).rot("bottom", 10, 0, 0, 0);
        m.clip("death", 36)
            .rot("lid", 0, 0, 0, 0).rot("lid", 8, -10, 0, 0).rot("lid", 24, -70, 0, 0, Ease.OUT_BOUNCE).rot("lid", 36, -75, 0, 0)
            .rot("bottom", 0, 0, 0, 0).rot("bottom", 20, 0, 0, 10).pos("bottom", 0, 0, 0, 0).pos("bottom", 24, 0, -1, 0)
            .pos("tongue", 0, 0, 0, 0).pos("tongue", 24, 0, -2, 0).scl("tongue", 36, 1, 0.6f, 1)
            .rot("eyeR", 0, 0, 0, 0).rot("eyeR", 24, 70, 0, 20).rot("eyeL", 0, 0, 0, 0).rot("eyeL", 24, 70, 0, -20);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_sewer_clam", "Sewer Clam", "下水貝")
            .game(2, 1).size(1.3f, 1.0f).hp(56).speed(0.14f).style(Style.SLIME).egg(0x6e7a6c, 0xe0707a)
            .model(SewerClam::model)
            .spawn(6, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(Pattern.cycle("jet", "pressurize"))
            .onSpawn(s -> { s.setCount("plating", 8); s.addBlock(8); })
            .onMoveEnd(s -> { int p = s.count("plating"); if (p > 0) { s.addBlock(p); s.setCount("plating", p - 1); } })
            .move(Move.of("jet", 34).intent(Intent.ATTACK, 10, 1).reach(6).ranged(6)
                .sound(15, SoundEvents.DOLPHIN_SPLASH, 1.4f, 0.7f)
                .at(16, (s, t) -> { Fx.particles(s, ParticleTypes.SPLASH, 30, 0.6); Fx.particles(s, ParticleTypes.BUBBLE, 20, 0.5); Fx.hitTarget(10).run(s, t); }))
            .move(Move.of("pressurize", 38).intent(Intent.BUFF).noTarget()
                .sound(8, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, 1.2f, 0.6f)
                .at(26, (s, t) -> { s.addStrength(4); s.inc("coral"); s.setVar(s.count("coral")); Fx.particles(s, ParticleTypes.BUBBLE_POP, 20, 0.7); Fx.sound(s, SoundEvents.TURTLE_EGG_CRACK, 1.2f, 0.6f); }));
    }
}
