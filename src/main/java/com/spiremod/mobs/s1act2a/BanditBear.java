package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Bandit Bear (Masked Bandits, with Pointy and Romeo). 38-42 HP. Cycle: Bear Hug (-2 Dexterity), Lunge 9 + 9 Block, Maul 18.
 */
public final class BanditBear {
    private BanditBear() {}
    static final int FUR = 0x6a4a30, FUR_D = 0x3e2a1a, BELLY = 0x9a7650, CLAW = 0xe8e0c8;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("bandit_bear");
        Bip b = Bip.make(m, FUR, FUR, FUR_D, FUR_D, 0x2a1c12, 12, 8, 14, 11, 7);
        b.torso.box(-4, 1, 3, 8, 8, 1).color(BELLY).grad(0xb8946a, BELLY).noise(0.1f);
        b.torso.box(-4, 0, -3.6f, 8, 1, 7).color(0x8a2a2a).flat().inflate(0.2f);                       // sash
        b.head.box(-2.5f, 0.5f, 3.5f, 5, 3, 3).color(BELLY).bevel(0.1f);                               // snout
        b.head.box(-1, 2.5f, 6.5f, 2, 1, 1).color(0x1a1010);                                            // nose
        b.head.box(-4.2f, 4.5f, -1, 2, 2, 2).color(FUR_D); b.head.box(2.2f, 4.5f, -1, 2, 2, 2).color(FUR_D);  // ears
        b.head.box(-3.6f, 2.8f, 3.5f, 7, 2, 1).color(0x111111).flat().glow(Face.FRONT, 1, 0, 1, 1, 0xff8040).glow(Face.FRONT, 5, 0, 1, 1, 0xff8040); // bandit mask w/ eyes
        Part jaw = b.head.add("jaw").at(0, 0.5f, 3.5f);
        jaw.box(-2, -1.5f, 0, 4, 1, 3).color(FUR_D);
        for (Part h : new Part[]{b.handR, b.handL}) for (int i = 0; i < 3; i++) h.box(-1.5f + i * 1.2f, -4, 1.5f, 1, 2, 1).color(CLAW).flat();
        m.clip("bear_hug", 40)   // arms wide, lunge and squeeze shut
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -90, 0, 70, Ease.OUT_BACK).rot("armR", 22, -95, 0, -10, Ease.IN_CUBIC).rot("armR", 32, -95, 0, -10).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -70, Ease.OUT_BACK).rot("armL", 22, -95, 0, 10, Ease.IN_CUBIC).rot("armL", 32, -95, 0, 10).rot("armL", 40, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -12, 0, 0).rot("torso", 22, 22, 0, 0, Ease.IN_CUBIC).rot("torso", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -2).pos("body", 22, 0, 0, 5, Ease.IN_CUBIC).pos("body", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -25, 0, 0).rot("head", 22, 15, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 40, 0, 0).rot("jaw", 22, 0, 0, 0);
        m.clip("lunge", 30)     // shoulder-charge with fist, guard up
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -10, -30, 0).rot("torso", 12, 25, 25, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -1, -3).pos("body", 12, 0, -1, 6, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, 30, 0, 25).rot("armR", 12, -100, -10, -5, Ease.IN_CUBIC).rot("armR", 20, -95, -10, -5).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -80, 0, -30).rot("armL", 24, -80, 0, -30).rot("armL", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 15, 0, 0).rot("head", 30, 0, 0, 0);
        m.clip("maul", 38)      // rear up on hind legs, roar, double-claw slam down
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -25, 0, 0, Ease.OUT_BACK).rot("torso", 20, 35, 0, 0, Ease.IN_CUBIC).rot("torso", 38, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 3, -3).pos("body", 20, 0, -2, 6, Ease.IN_CUBIC).pos("body", 38, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -170, 0, 35, Ease.OUT_BACK).rot("armR", 20, -30, 0, -5, Ease.IN_CUBIC).rot("armR", 30, -30, 0, -5).rot("armR", 38, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -170, 0, -35, Ease.OUT_BACK).rot("armL", 20, -30, 0, 5, Ease.IN_CUBIC).rot("armL", 30, -30, 0, 5).rot("armL", 38, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -35, 0, 0).rot("head", 20, 25, 0, 0, Ease.IN_CUBIC).rot("head", 38, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 0, 0, 0).rot("jaw", 14, 55, 0, 0).rot("jaw", 20, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("bandit_bear", "Bandit Bear", "バンディットベア")
            .game(1, 2).size(0.9f, 1.8f).hp(40).speed(0.25f).style(Style.BEAST).egg(0x6a4a30, 0x8a2a2a)
            .model(BanditBear::model).spawn(4, 1, 1, MobDef.Habitat.TAIGA)
            .pattern(Pattern.cycle("bear_hug", "lunge", "maul"))
            .move(Move.of("bear_hug", 40).intent(Intent.DEBUFF).reach(2.6f)
                .sound(8, SoundEvents.POLAR_BEAR_WARNING, 1f, 0.9f)
                .at(22, (s, t) -> { for (var v : Fx.foes(s, s.getBoundingBox().inflate(2.6, 1, 2.6))) { Fx.slow(v, 6, 1); Fx.weak(v, 4); } }))
            .move(Move.of("lunge", 30).intent(Intent.ATTACK_DEFEND, 9, 1).reach(2.4f)
                .at(8, Fx.block(9)).at(12, Fx.melee(9, 2.8, 90)))
            .move(Move.of("maul", 38).intent(Intent.ATTACK, 18, 1).reach(2.6f)
                .sound(14, SoundEvents.POLAR_BEAR_WARNING, 1.3f, 0.7f).at(20, Fx.melee(18, 3.0, 100)));
    }
}
