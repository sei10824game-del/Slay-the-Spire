package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 2 Romeo (Masked Bandit). 35-39 HP. Cycle: Mock (nothing), Agonizing Slash 10 + Weak 2, Cross Slash 15. */
public final class Romeo {
    private Romeo() {}
    static final int SKIN = 0xe8cfb0, COAT = 0x8a2a4a, COAT_D = 0x4a1228, GOLD = 0xe0c050, HAT = 0x2a1a30, FEATHER = 0xf0f0f0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("romeo");
        Bip b = Bip.make(m, SKIN, COAT, COAT_D, 0x2a2a3a, 0x1a1210, 8, 8, 12, 9, 6);
        b.torso.box(-1, 1, 2.1f, 2, 9, 1).color(GOLD).flat();
        Part cape = b.torso.add("cape").at(0, 11, -2.5f);
        cape.box(-5, -14, -1, 10, 14, 1).color(COAT_D).grad(COAT, COAT_D).bevel(0.1f);
        cape.box(-5, -14, -1.1f, 10, 1, 1).color(GOLD).flat();
        b.torso.box(-5, 9, -3, 10, 2, 6).color(COAT_D).flat().inflate(0.1f);                          // collar
        b.head.box(-3.2f, 1.2f, 3.2f, 6, 2, 1).color(0x111111).flat().glow(Face.FRONT, 1, 0, 1, 1, 0xffe0a0).glow(Face.FRONT, 4, 0, 1, 1, 0xffe0a0); // mask
        b.head.box(-5, 5.5f, -5, 10, 1, 10).color(HAT).bevel(0.1f);                                  // hat brim
        b.head.box(-3.5f, 6.5f, -3.5f, 7, 3, 7).color(HAT).grad(0x4a3050, HAT);
        Part plume = b.head.add("plume").at(3.5f, 8, 0).rot(0, 0, -30);
        plume.box(-0.5f, 0, -3, 1, 2, 6).color(FEATHER).flat();
        plume.box(-0.5f, 1, -4, 1, 1, 3).color(0xffc0c0).flat();
        Part rap = b.handR.add("rapier").at(0, -2, 0).rot(-90, 0, 0);
        rap.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
        rap.box(-2.5f, 2, -2.5f, 5, 1, 5).color(GOLD).flat();
        rap.box(-0.4f, 3, -0.4f, 1, 14, 1).color(0xe6ebf2).flat();
        m.clip("mock", 36)   // flourish, bow with the hat, cackle
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 30, 0, 0, Ease.OUT_BACK).rot("torso", 26, 28, 0, 0).rot("torso", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -120, 0, -30).rot("armL", 26, -110, 0, -30).rot("armL", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, -60, 0, 20).rot("armR", 14, -50, 0, 50).rot("armR", 20, -60, 0, 20).rot("armR", 26, -50, 0, 50).rot("armR", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -20, 10, 0).rot("head", 18, -15, -10, 0).rot("head", 26, -20, 10, 0).rot("head", 36, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 10, -25, 0, 0).rot("legR", 26, -25, 0, 0).rot("legR", 36, 0, 0, 0);
        m.clip("agonizing_slash", 30)   // drawn-out diagonal slash
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -8, -35, 0).rot("torso", 13, 14, 35, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -150, 0, 30).rot("armR", 13, -50, 0, -25, Ease.IN_CUBIC).rot("armR", 22, -45, 0, -25).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, -30, 0, -40).rot("armL", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, -3).pos("body", 13, 0, -1, 5, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 13, -40, 0, 0).rot("legR", 30, 0, 0, 0);
        m.clip("cross_slash", 34)       // X-shaped double slash
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -10, -30, 0).rot("torso", 11, 12, 30, 0, Ease.IN_CUBIC).rot("torso", 18, -8, 30, 0).rot("torso", 22, 14, -30, 0, Ease.IN_CUBIC).rot("torso", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -160, 0, 40).rot("armR", 11, -60, 0, -35, Ease.IN_CUBIC).rot("armR", 18, -160, 0, -40).rot("armR", 22, -60, 0, 40, Ease.IN_CUBIC).rot("armR", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 11, 0, -1, 4, Ease.IN_CUBIC).pos("body", 22, 0, -1, 6, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -40, 0, -30).rot("armL", 34, 0, 0, 0)
            .rot("cape", 0, 0, 0, 0).rot("cape", 11, -20, 0, 0).rot("cape", 22, -30, 0, 0).rot("cape", 34, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("romeo", "Romeo", "ロミオ")
            .game(1, 2).size(0.6f, 1.75f).hp(37).speed(0.28f).style(Style.PILLAGER).egg(0x8a2a4a, 0xe0c050)
            .model(Romeo::model).spawn(4, 1, 1, MobDef.Habitat.TAIGA)
            .pattern(Pattern.cycle("mock", "agonizing_slash", "cross_slash"))
            .move(Move.of("mock", 36).intent(Intent.UNKNOWN).noTarget()
                .sound(12, SoundEvents.WITCH_CELEBRATE, 1f, 1.4f).at(12, (s, t) -> Fx.particles(s, ParticleTypes.NOTE, 5, 0.5)))
            .move(Move.of("agonizing_slash", 30).intent(Intent.ATTACK_DEBUFF, 10, 1).reach(2.6f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 0.9f, 1.3f).at(13, Fx.melee(10, 3.0, 90, v -> Fx.weak(v, 5))))
            .move(Move.of("cross_slash", 34).intent(Intent.ATTACK, 15, 1).reach(2.6f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.0f).sound(18, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.2f)
                .at(22, Fx.melee(15, 3.0, 100)));
    }
}
