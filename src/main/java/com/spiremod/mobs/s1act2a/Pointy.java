package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 2 Pointy (Masked Bandit). 30 HP. Every turn: Stab 5 x2. */
public final class Pointy {
    private Pointy() {}
    static final int SKIN = 0xe0c0a0, SHIRT = 0xd8d0b8, VEST = 0x4a5a3a, BAND = 0xb33a3a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("pointy");
        Bip b = Bip.make(m, SKIN, SHIRT, 0x8a826c, 0x3a3a46, 0x2a1c12, 7, 7, 11, 8, 6);
        b.torso.box(-3.8f, 1, -2.4f, 7, 8, 5).color(VEST).grad(0x5f7248, 0x34402a).bevel(0.1f).inflate(0.2f);
        b.head.box(-3.4f, 0.3f, -3.4f, 7, 3, 7).color(BAND).flat().inflate(0.05f).paint(Face.FRONT, 0, 1, 7, 2, 0x111111).glow(Face.FRONT, 1, 1, 1, 1, 0xffffff).glow(Face.FRONT, 5, 1, 1, 1, 0xffffff);
        b.head.box(-3.5f, 5, -3.5f, 7, 2, 7).color(0x2a2a2a).bevel(0.1f);          // messy dark hair
        Part d = b.handR.add("dagger").at(0, -2, 0).rot(-90, 0, 0);
        d.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
        d.box(-1.5f, 2, -0.5f, 3, 1, 1).color(0x8a8a90).flat();
        d.box(-0.5f, 3, -0.4f, 1, 7, 1).color(0xd9dee6).flat();
        Part d2 = b.handL.add("dagger2").at(0, -2, 0).rot(-90, 0, 0);
        d2.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
        d2.box(-1.5f, 2, -0.5f, 3, 1, 1).color(0x8a8a90).flat();
        d2.box(-0.5f, 3, -0.4f, 1, 7, 1).color(0xd9dee6).flat();
        m.clip("stab", 30)   // right jab then left jab
            .rot("torso", 0, 0, 0, 0).rot("torso", 6, -6, -25, 0).rot("torso", 9, 10, 25, 0, Ease.IN_CUBIC).rot("torso", 16, -6, 25, 0).rot("torso", 19, 10, -25, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, 4, Ease.IN_CUBIC).pos("body", 14, 0, 0, 0).pos("body", 19, 0, 0, 4, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, 30, 0, 20).rot("armR", 9, -95, -10, 0, Ease.IN_CUBIC).rot("armR", 14, -40, 0, 10).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, 30, 0, -20).rot("armL", 19, -95, 10, 0, Ease.IN_CUBIC).rot("armL", 24, -40, 0, -10).rot("armL", 30, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 9, -30, 0, 0).rot("legR", 14, 0, 0, 0).rot("legL", 0, 0, 0, 0).rot("legL", 19, -30, 0, 0).rot("legL", 24, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("pointy", "Pointy", "ポインティ")
            .game(1, 2).size(0.55f, 1.5f).hp(30).speed(0.3f).style(Style.PILLAGER).egg(0xd8d0b8, 0x4a5a3a)
            .model(Pointy::model).spawn(4, 1, 1, MobDef.Habitat.TAIGA)
            .pattern(s -> "stab")
            .move(Move.of("stab", 30).intent(Intent.ATTACK, 5, 2).reach(2.2f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.6f)
                .at(9, Fx.melee(5, 2.6, 80)).at(19, Fx.melee(5, 2.6, 80)));
    }
}
