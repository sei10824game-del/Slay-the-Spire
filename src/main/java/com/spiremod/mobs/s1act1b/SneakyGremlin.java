package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 1 Sneaky Gremlin: 10-14 HP. Puncture (9) every turn. Magenta imp with a jagged yellow lightning-dagger. */
public final class SneakyGremlin {
    private SneakyGremlin() {}
    static final int SKIN = 0xb2357a, DARK = 0x6e1a4c, HORN = 0x8ccbea, EYE = 0xeaf6ff, BLADE = 0xf2e05a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("sneaky_gremlin");
        GremlinKit.build(m, SKIN, DARK, HORN, EYE, false);
        // zig-zag lightning dagger held forward in the right hand
        Part d = m.byName.get("handR").add("dagger").at(0, -1.5f, 0.5f).rot(-80, 0, 0);
        d.box(-0.5f, -1, -0.5f, 1, 2, 1).color(0x3a2a40);
        d.box(-1.5f, 1, -0.5f, 3, 1, 1).color(0x6a5a70);
        d.box(-0.5f, 2, -0.5f, 1, 2, 1).color(BLADE).flat().glow(Face.FRONT, 0, 0, 1, 2, BLADE);
        d.box(0.5f, 3.5f, -0.5f, 1, 2, 1).color(BLADE).flat().glow(Face.FRONT, 0, 0, 1, 2, BLADE);
        d.box(-0.5f, 5, -0.5f, 1, 2, 1).color(BLADE).flat().glow(Face.FRONT, 0, 0, 1, 2, BLADE);
        d.box(0.5f, 6.5f, -0.5f, 1, 2, 1).color(0xfff6b0).flat().glow(Face.FRONT, 0, 0, 1, 2, 0xfff6b0);
        m.byName.get("armR").rot(-45, 0, 10);
        m.byName.get("body").rot(15, 0, 0);       // sneaky hunch
        m.byName.get("head").rot(-12, 0, 0);
        GremlinKit.stdClips(m);
        // PUNCTURE: slink back, dagger drawn to the hip, then a darting lunge-stab with full extension
        m.clip("puncture", 24)
            .pos("body", 0, 0, 0, 0).pos("body", 7, 0, -1.2f, -2).pos("body", 11, 0, -0.5f, 4, Ease.IN_CUBIC).pos("body", 15, 0, -0.5f, 4).pos("body", 24, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 7, -10, 35, 0).rot("torso", 11, 25, -25, 0, Ease.IN_CUBIC).rot("torso", 15, 22, -20, 0).rot("torso", 24, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 7, 70, 20, -10).rot("armR", 11, -50, 0, 5, Ease.IN_CUBIC).rot("armR", 15, -48, 0, 5).rot("armR", 24, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 7, -50, 0, 20).rot("armL", 11, 45, 0, -10).rot("armL", 24, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 7, 10, -25, 0).rot("head", 11, 0, 15, 0).rot("head", 24, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 0, 0, 0).rot("jaw", 12, 28, 0, 0).rot("jaw", 20, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 7, 20, 0, 0).rot("legR", 11, 35, 0, 0).rot("legR", 24, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 7, -30, 0, 0).rot("legL", 11, -55, 0, 0).rot("legL", 24, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("sneaky_gremlin", "Sneaky Gremlin", "スニーキーグレムリン")
            .game(1, 1).size(0.6f, 1.1f).hp(12).speed(0.32f).style(Style.PILLAGER).egg(0xb2357a, 0xf2e05a)
            .model(SneakyGremlin::model).spawn(4, 1, 3, MobDef.Habitat.FOREST)
            .pattern(Pattern.cycle("puncture"))
            .move(Move.of("puncture", 24).intent(Intent.ATTACK, 9, 1).reach(2.4f)
                .sound(7, SoundEvents.PLAYER_ATTACK_SWEEP, 0.5f, 1.8f)
                .at(10, Fx.dash(0.5, 0.05))
                .at(11, Fx.melee(9, 2.8, 50))
                .at(11, (s, t) -> Fx.particlesAt(s, ParticleTypes.ELECTRIC_SPARK, Fx.rel(s, 1.2, 0, 0.8), 8, 0.2, 0.1)));
    }
}
