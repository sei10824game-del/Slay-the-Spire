package com.spiremod.mobs.s2glory2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * StS2 Act 3 (Glory) boss: the Queen (400 HP), fought with her Torch Head Amalgam.
 * Opening: Puppet Strings (3 Chains of Binding) -> You're Mine (99 Frail/Weak/Vulnerable).
 * While the Amalgam lives: Burn Bright for Me (+1 Str to Amalgam, 20 Block) forever.
 * Amalgam dead: Enrage (only if she was on Burn Bright) then loop Off with Your Head (3x5) -> Execution (15) -> Enrage (+2 Str).
 * MC translation: Chains of Binding = Mining Fatigue + Slowness (can't act freely); 99 turns of debuffs = 5 minutes.
 */
public final class Queen {
    private Queen() {}

    static final String ID = "s2_queen";
    static final int GOWN = 0x6e1532, GOWN_D = 0x3a0a1c, GOWN_L = 0x93264a, VIOLET = 0x3c1d4c, VIOLET_D = 0x24112f,
        GOLD = 0xd8aa45, GOLD_D = 0x8a6420, SKIN = 0xddd2dc, SKIN_D = 0xa898aa, EYE = 0xff2a3c, HAIR = 0x1c1024,
        STEEL = 0xc8ccd6, STRING = 0xf2dc9a, GEM = 0xc01848;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec(ID).scale(1.55f);
        Part all = m.add("all").at(0, 0, 0);
        // ---------- gown: four flaring tiers with a gold front panel
        Part gown = all.add("gown").at(0, 0, 0);
        gown.box(-7, 0, -6, 14, 3, 12).grad(GOWN, GOWN_D).bevel(0.15f).paint(Face.FRONT, 5, 0, 4, 3, GOLD_D).paint(Face.FRONT, 6, 0, 2, 3, GOLD);
        gown.box(-7.3f, 0, -6.3f, 14, 1, 12).inflate(0.3f).color(GOLD).flat().noise(0.05f);
        gown.box(-6, 3, -5, 12, 4, 10).grad(GOWN_L, GOWN).bevel(0.12f).paint(Face.FRONT, 4, 0, 4, 4, GOLD_D).paint(Face.FRONT, 5, 0, 2, 4, GOLD)
            .px(Face.FRONT, 5, 1, GEM).px(Face.FRONT, 6, 3, GEM);
        gown.box(-5, 7, -4, 10, 4, 8).grad(GOWN_L, GOWN).bevel(0.12f).paint(Face.FRONT, 3, 0, 4, 4, GOLD_D).paint(Face.FRONT, 4, 0, 2, 4, GOLD);
        gown.box(-4, 11, -3, 8, 3, 6).grad(GOWN, GOWN_D).paint(Face.FRONT, 3, 0, 2, 3, GOLD);
        gown.box(-6.2f, 6.6f, -5.2f, 12, 1, 10).inflate(0.2f).color(GOLD_D).flat(); // embroidered seams
        gown.box(-5.2f, 10.6f, -4.2f, 10, 1, 8).inflate(0.2f).color(GOLD).flat();
        // ---------- torso
        Part body = all.add("body").at(0, 14, 0);
        body.box(-3, 0, -2, 6, 3, 4).color(VIOLET).grad(VIOLET, VIOLET_D);
        body.box(-3.2f, 0, -2.2f, 6, 1, 4).inflate(0.2f).color(GOLD).flat();
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-4, 0, -2.5f, 8, 7, 5).grad(GOWN_L, GOWN).bevel(0.1f)
            .paint(Face.FRONT, 3, 0, 2, 7, GOLD_D).px(Face.FRONT, 2, 1, GOLD).px(Face.FRONT, 5, 1, GOLD).px(Face.FRONT, 2, 3, GOLD).px(Face.FRONT, 5, 3, GOLD)
            .px(Face.FRONT, 2, 5, GOLD).px(Face.FRONT, 5, 5, GOLD).glow(Face.FRONT, 3, 2, 2, 2, GEM);
        for (int s = -1; s <= 1; s += 2) { // pauldrons
            torso.box(s < 0 ? -6.5f : 4, 5, -2.5f, 3, 2, 5).color(GOLD).bevel(0.2f).noise(0.04f);
            torso.box(s < 0 ? -6.5f : 4, 4, -2.5f, 3, 1, 5).color(GOLD_D).flat();
        }
        // towering fan collar
        Part collar = torso.add("collar").at(0, 6, -2.4f).rot(-12, 0, 0);
        collar.box(-7, -1, -1, 14, 10, 1).grad(GOWN, VIOLET_D).paint(Face.FRONT, 0, 0, 14, 1, GOLD).paint(Face.FRONT, 0, 0, 1, 10, GOLD).paint(Face.FRONT, 13, 0, 1, 10, GOLD)
            .paint(Face.FRONT, 3, 2, 1, 8, GOLD_D).paint(Face.FRONT, 10, 2, 1, 8, GOLD_D).paint(Face.FRONT, 6, 1, 2, 9, GOLD_D).flat();
        collar.box(-7, -1, -1.6f, 14, 10, 1).color(GOLD_D).bevel(0.2f);
        for (int i = -3; i <= 3; i++) collar.box(i * 2 - 0.5f, 9, -1.3f, 1, i % 2 == 0 ? 3 : 2, 1).color(GOLD).flat();
        collar.box(-0.5f, 12, -1.3f, 1, 1, 1).color(GEM).glow(Face.FRONT, 0, 0, 1, 1, GEM);
        // cape / train
        Part cape = torso.add("cape").at(0, 6, -2.8f).rot(10, 0, 0);
        cape.box(-5, -21, -1, 10, 21, 1).grad(VIOLET, VIOLET_D).paint(Face.BACK, 0, 20, 10, 1, GOLD).paint(Face.BACK, 4, 0, 2, 21, GOWN);
        // ---------- head + crown
        Part head = torso.add("head").at(0, 7, 0.3f);
        head.box(-1, 0, -1, 2, 2, 2).color(SKIN_D);
        head.box(-2.5f, 2, -2.5f, 5, 6, 5).color(SKIN).bevel(0.12f)
            .glow(Face.FRONT, 1, 2, 1, 1, EYE).glow(Face.FRONT, 3, 2, 1, 1, EYE)
            .paint(Face.FRONT, 0, 1, 2, 1, HAIR).paint(Face.FRONT, 3, 1, 2, 1, HAIR)   // severe brows
            .paint(Face.FRONT, 2, 4, 1, 1, SKIN_D).paint(Face.FRONT, 1, 5, 3, 1, 0x5a0a1a);
        head.box(-3, 1.5f, -3, 6, 7, 2).color(HAIR).noise(0.1f);                 // hair behind
        head.box(-3, 4, -2.7f, 1, 5, 5).color(HAIR); head.box(2, 4, -2.7f, 1, 5, 5).color(HAIR);
        head.box(-1.5f, -2, -3.5f, 3, 4, 2).color(HAIR).bevel(0.2f);           // bun / braid
        Part crown = head.add("crown").at(0, 8, 0);
        crown.box(-3, 0, -3, 6, 2, 6).color(GOLD).bevel(0.15f).noise(0.04f)
            .glow(Face.FRONT, 2, 0, 2, 1, GEM).glow(Face.LEFT, 2, 0, 2, 1, GEM).glow(Face.RIGHT, 2, 0, 2, 1, GEM);
        for (int i = -1; i <= 1; i++) for (int k = -1; k <= 1; k += 2) {
            crown.box(i * 2 - 0.5f, 2, k * 2.5f - 0.5f, 1, i == 0 ? 4 : 2, 1).color(GOLD).flat();
            crown.box(k * 2.5f - 0.5f, 2, i * 2 - 0.5f, 1, i == 0 ? 3 : 2, 1).color(GOLD_D).flat();
        }
        crown.box(-0.5f, 6, 2, 1, 1, 1).color(GEM).glow(Face.FRONT, 0, 0, 1, 1, GEM).glow(Face.TOP, 0, 0, 1, 1, GEM);
        // ---------- arms (shoulder -> forearm -> hand), puppet strings from the fingers, executioner's blade in the right hand
        for (int s = -1; s <= 1; s += 2) {
            String sd = s < 0 ? "R" : "L";
            Part arm = torso.add("arm" + sd).at(s * 5.2f, 6, 0).rot(0, 0, s * -8);
            arm.box(-1.5f, -3, -1.5f, 3, 3, 3).color(GOWN_L).bevel(0.15f);             // puffed sleeve
            arm.box(-1, -6, -1, 2, 4, 2).color(VIOLET);
            Part fore = arm.add("fore" + sd).at(0, -6, 0).rot(-35, 0, 0);
            fore.box(-1, -6, -1, 2, 6, 2).color(0x231628).noise(0.05f);                 // long black glove
            fore.box(-1.3f, -1.2f, -1.3f, 2, 1, 2).inflate(0.3f).color(GOLD).flat();
            Part hand = fore.add("hand" + sd).at(0, -6, 0);
            hand.box(-1, -2, -1, 2, 2, 2).color(0x2c1c32);
            for (int f = 0; f < 3; f++) hand.box(-1 + f * 0.75f - 0.1f, -4, 0, 1, 2, 1).color(0x2c1c32);
            Part str = hand.add("strings" + sd).at(0, -4, 0.5f);
            for (int f = 0; f < 3; f++) str.box(-1.4f + f * 1.2f, -14, -0.5f, 1, 14, 1).color(STRING).flat().noise(0f)
                .glow(Face.FRONT, 0, 0, 1, 14, STRING).glow(Face.LEFT, 0, 0, 1, 14, STRING).glow(Face.RIGHT, 0, 0, 1, 14, STRING);
            if (s < 0) {
                Part blade = hand.add("blade").at(0, -1, 0.2f).rot(-55, 0, 0);
                blade.box(-0.5f, -1, -0.5f, 1, 4, 1).color(0x3a2a1e);                      // grip (behind fist)
                blade.box(-0.6f, 3, -0.6f, 1, 1, 1).inflate(0.3f).color(GOLD).flat();
                blade.box(-2, -2, -0.5f, 4, 1, 1).color(GOLD).flat();                       // crossguard
                blade.box(-1, -16, -0.5f, 2, 14, 1).grad(0xe8ecf2, STEEL).noise(0.03f).glow(Face.FRONT, 0, 0, 1, 14, 0x9aa2b4);
                blade.box(-3, -17, -0.5f, 4, 4, 1).color(STEEL).noise(0.03f);              // broad headsman's tip
                blade.box(-0.5f, -10, -0.7f, 1, 7, 1).color(GEM).flat();                    // blood groove
            }
        }
        m.look("head", 40, 25);
        // ---------- idle: regal sway, fingers working the strings, gown hem ripples
        m.clip("idle", 60).loop()
            .pos("all", 0, 0, 0, 0).pos("all", 30, 0, 0.6f, 0).pos("all", 60, 0, 0, 0)
            .rot("torso", 0, 0, 0, 2).rot("torso", 30, 2, 0, -2).rot("torso", 60, 0, 0, 2)
            .rot("armR", 0, -10, 0, 8).rot("armR", 30, -5, 0, 10).rot("armR", 60, -10, 0, 8)
            .rot("armL", 0, -30, 0, -14).rot("armL", 15, -42, 0, -16).rot("armL", 30, -30, 0, -14).rot("armL", 45, -42, 0, -12).rot("armL", 60, -30, 0, -14)
            .rot("foreL", 0, -20, 0, 0).rot("foreL", 15, -5, 0, 0).rot("foreL", 30, -20, 0, 0).rot("foreL", 45, -5, 0, 0).rot("foreL", 60, -20, 0, 0)
            .rot("stringsL", 0, 30, 0, 0).rot("stringsL", 15, 45, 0, 5).rot("stringsL", 30, 30, 0, 0).rot("stringsL", 45, 45, 0, -5).rot("stringsL", 60, 30, 0, 0)
            .rot("cape", 0, 0, 0, 0).rot("cape", 30, 4, 0, 1).rot("cape", 60, 0, 0, 0)
            .rot("collar", 0, 0, 0, 0).rot("collar", 30, -2, 0, 0).rot("collar", 60, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, -4, 6, 0).rot("head", 40, -4, -6, 0).rot("head", 60, 0, 0, 0);
        // walk: she glides, the gown sways
        m.clip("walk", 20).loop()
            .pos("all", 0, 0, 0.8f, 0).pos("all", 10, 0, 0, 0).pos("all", 20, 0, 0.8f, 0)
            .rot("gown", 0, -3, 0, 2).rot("gown", 10, -3, 0, -2).rot("gown", 20, -3, 0, 2)
            .rot("cape", 0, 14, 0, 0).rot("cape", 10, 20, 0, 0).rot("cape", 20, 14, 0, 0)
            .rot("torso", 0, 6, 0, 0).rot("torso", 20, 6, 0, 0);
        // PUPPET STRINGS: arms rise, fingers splay, strings flung out, then a cruel yank down
        m.clip("puppet_strings", 36)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -150, 0, 30, Ease.OUT_BACK).rot("armR", 18, -160, 0, 34).rot("armR", 23, -60, 0, 12, Ease.IN_CUBIC).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -150, 0, -30, Ease.OUT_BACK).rot("armL", 18, -160, 0, -34).rot("armL", 23, -60, 0, -12, Ease.IN_CUBIC).rot("armL", 36, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 10, 30, 0, 0).rot("foreR", 23, -50, 0, 0).rot("foreR", 36, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 10, 30, 0, 0).rot("foreL", 23, -50, 0, 0).rot("foreL", 36, 0, 0, 0)
            .rot("stringsR", 0, 0, 0, 0).rot("stringsR", 12, -150, 0, 0, Ease.OUT_BACK).rot("stringsR", 23, -90, 0, 0).rot("stringsR", 36, 0, 0, 0)
            .rot("stringsL", 0, 0, 0, 0).rot("stringsL", 12, -150, 0, 0, Ease.OUT_BACK).rot("stringsL", 23, -90, 0, 0).rot("stringsL", 36, 0, 0, 0)
            .scl("stringsR", 0, 1, 1, 1).scl("stringsR", 12, 1, 1.8f, 1).scl("stringsR", 28, 1, 1.8f, 1).scl("stringsR", 36, 1, 1, 1)
            .scl("stringsL", 0, 1, 1, 1).scl("stringsL", 12, 1, 1.8f, 1).scl("stringsL", 28, 1, 1.8f, 1).scl("stringsL", 36, 1, 1, 1)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -12, 0, 0).rot("torso", 23, 14, 0, 0, Ease.IN_CUBIC).rot("torso", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -20, 0, 0).rot("head", 23, 15, 0, 0).rot("head", 36, 0, 0, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 10, 0, 2, 0).pos("all", 23, 0, -0.5f, 0).pos("all", 36, 0, 0, 0);
        // YOU'RE MINE: leans in, beckoning hand reaches for the target and clenches; blade levelled at the victim
        m.clip("youre_mine", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -8, 15, 0).rot("torso", 20, 20, -10, 0, Ease.OUT_BACK).rot("torso", 32, 18, -10, 0).rot("torso", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -60, 0, -40).rot("armL", 20, -100, -20, -5, Ease.OUT_BACK).rot("armL", 32, -95, -20, -5).rot("armL", 40, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 20, 35, 0, 0).rot("foreL", 25, -10, 0, 0, Ease.IN_CUBIC).rot("foreL", 40, 0, 0, 0)
            .rot("handL", 0, 0, 0, 0).rot("handL", 22, 0, 0, 0).rot("handL", 25, -60, 0, 0, Ease.IN_CUBIC).rot("handL", 33, -60, 0, 0).rot("handL", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 20, -70, 15, 15).rot("armR", 32, -70, 15, 15).rot("armR", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 10, 10, 12).rot("head", 20, -10, -10, -14, Ease.OUT_BACK).rot("head", 32, -8, -10, -14).rot("head", 40, 0, 0, 0)
            .rot("collar", 0, 0, 0, 0).rot("collar", 20, -10, 0, 0).rot("collar", 40, 0, 0, 0);
        // BURN BRIGHT FOR ME: arms spread wide, then she conducts her pet with a sweeping upward gesture (gains block)
        m.clip("burn_bright", 36)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -20, 0, 80, Ease.OUT_BACK).rot("armR", 20, -130, 40, 50).rot("armR", 28, -120, 40, 50).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -20, 0, -80, Ease.OUT_BACK).rot("armL", 20, -130, -40, -50).rot("armL", 28, -120, -40, -50).rot("armL", 36, 0, 0, 0)
            .rot("stringsL", 0, 0, 0, 0).rot("stringsL", 20, -120, 0, 0).rot("stringsL", 36, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -10, 0, 0).rot("torso", 20, -16, 0, 0).rot("torso", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, -25, 0, 0).rot("head", 36, 0, 0, 0)
            .rot("cape", 0, 0, 0, 0).rot("cape", 12, 30, 0, 0, Ease.OUT_BACK).rot("cape", 36, 0, 0, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 20, 0, 2.5f, 0).pos("all", 36, 0, 0, 0);
        // OFF WITH YOUR HEAD: five whirling decapitating slashes, alternating fore/backhand (hits at 12,19,26,33,40)
        Clip off = m.clip("off_with_your_head", 52)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -120, 0, 70)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -6, 45, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 8, 0, 0, -2);
        for (int i = 0; i < 5; i++) {
            int t = 12 + i * 7; boolean fore = i % 2 == 0;
            off.rot("armR", t, -95, 0, fore ? -60 : 70, Ease.IN_CUBIC).rot("armR", t + 3, -100, 0, fore ? -70 : 80)
               .rot("torso", t, 4, fore ? -40 : 40, 0, Ease.IN_CUBIC).rot("torso", t + 3, 4, fore ? -42 : 42, 0)
               .pos("all", t, 0, 0, 1 + i * 0.8f, Ease.IN_CUBIC);
        }
        off.rot("armR", 52, 0, 0, 0).rot("torso", 52, 0, 0, 0).pos("all", 52, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -40, 0, -50).rot("armL", 44, -40, 0, -50).rot("armL", 52, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 8, 20, 0, 0).rot("foreR", 44, 20, 0, 0).rot("foreR", 52, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 8, -20, 0).rot("head", 44, 8, 20, 0).rot("head", 52, 0, 0, 0)
            .rot("cape", 0, 0, 0, 0).rot("cape", 20, 25, 0, 10).rot("cape", 34, 25, 0, -10).rot("cape", 52, 0, 0, 0);
        // EXECUTION: blade hoisted high with both hands, a terrible pause, then the headsman's chop (hit at 24)
        m.clip("execution", 44)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -185, 0, 10, Ease.OUT_BACK).rot("armR", 20, -190, 0, 10).rot("armR", 24, -40, 0, 8, Ease.IN_CUBIC).rot("armR", 32, -35, 0, 8).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -180, 0, -30, Ease.OUT_BACK).rot("armL", 20, -185, 0, -30).rot("armL", 24, -45, 0, -25, Ease.IN_CUBIC).rot("armL", 32, -40, 0, -25).rot("armL", 44, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 14, 20, 0, 0).rot("foreR", 24, 30, 0, 0).rot("foreR", 44, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -18, 0, 0).rot("torso", 20, -20, 0, 0).rot("torso", 24, 28, 0, 0, Ease.IN_CUBIC).rot("torso", 32, 26, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -20, 0, 0).rot("head", 24, 15, 0, 0).rot("head", 44, 0, 0, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 14, 0, 3, -1).pos("all", 20, 0, 3.5f, -1).pos("all", 24, 0, -1.5f, 3, Ease.IN_CUBIC).pos("all", 32, 0, -1, 3).pos("all", 44, 0, 0, 0)
            .rot("cape", 0, 0, 0, 0).rot("cape", 24, 40, 0, 0, Ease.IN_CUBIC).rot("cape", 44, 0, 0, 0);
        // ENRAGE: silent fury after her pet dies - she hunches, trembles, then throws her head back, crown blazing
        Clip enr = m.clip("enrage", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 22, 0, 0).rot("torso", 24, -22, 0, 0, Ease.OUT_BACK).rot("torso", 34, -18, 0, 0).rot("torso", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 25, 0, 0).rot("head", 24, -35, 0, 0, Ease.OUT_BACK).rot("head", 34, -30, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 20, 0, 30).rot("armR", 24, -40, 0, 70, Ease.OUT_BACK).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 20, 0, -30).rot("armL", 24, -40, 0, -70, Ease.OUT_BACK).rot("armL", 40, 0, 0, 0)
            .scl("crown", 0, 1, 1, 1).scl("crown", 24, 1.3f, 1.5f, 1.3f, Ease.OUT_BACK).scl("crown", 40, 1, 1, 1)
            .scl("collar", 0, 1, 1, 1).scl("collar", 24, 1.2f, 1.25f, 1, Ease.OUT_BACK).scl("collar", 40, 1, 1, 1);
        for (int t = 11; t < 22; t += 2) enr.rot("all", t, 0, 0, (t % 4 == 1) ? 2.5f : -2.5f);
        enr.rot("all", 0, 0, 0, 0).rot("all", 24, 0, 0, 0);
        m.clip("hurt", 12)
            .rot("torso", 0, 0, 0, 0).rot("torso", 3, -14, 0, 5).rot("torso", 12, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -18, 0, -10).rot("head", 12, 0, 0, 0)
            .rot("collar", 0, 0, 0, 0).rot("collar", 3, -10, 0, 0).rot("collar", 12, 0, 0, 0);
        // death: she clutches at the air, crown topples, the gown crumples to the floor
        m.clip("death", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -25, 0, 0).rot("torso", 30, 40, 0, 10).rot("torso", 50, 70, 0, 15, Ease.OUT_BOUNCE)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -40, 0, 0).rot("head", 50, 30, 20, 10)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -160, 0, 30).rot("armR", 50, -20, 0, 80)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -140, 0, -30).rot("armL", 50, -10, 0, -80)
            .pos("crown", 0, 0, 0, 0).pos("crown", 20, 0, 3, 2).pos("crown", 40, 2, -14, 8, Ease.OUT_BOUNCE).pos("crown", 50, 2, -14, 8)
            .rot("crown", 0, 0, 0, 0).rot("crown", 40, 70, 30, 40).rot("crown", 50, 70, 30, 40)
            .scl("gown", 0, 1, 1, 1).scl("gown", 50, 1.25f, 0.35f, 1.25f, Ease.IN_QUAD)
            .pos("body", 0, 0, 0, 0).pos("body", 50, 0, -9, 0, Ease.IN_QUAD);
        return m;
    }

    static boolean amalgamAlive(SpireMob s) {
        for (SpireMob m : s.minions()) if (m.def.id.equals(TorchHeadAmalgam.ID)) return true;
        return false;
    }

    static final String[] LOOP = {"off_with_your_head", "execution", "enrage"};

    public static MobDef def() {
        return MobDef.of(ID, "Queen", "クイーン")
            .game(2, 3).size(1.6f, 4.4f).hp(400).speed(0.22f).style(Style.WITCH).egg(0x6e1532, 0xd8aa45).boss().kbResist(1f)
            .model(Queen::model)
            .onSpawn(s -> Fx.summon(s, TorchHeadAmalgam.ID, 3.2, 1.5))
            .pattern(s -> {
                int t = s.getTurn();
                if (t == 0) return "puppet_strings";
                if (t == 1) return "youre_mine";
                if (!s.flags.contains("amalgam_dead")) return "burn_bright";
                return LOOP[s.inc("loop") % 3 == 0 ? 2 : (s.count("loop") % 3) - 1]; // off -> exec -> enrage -> ...
            })
            .onTick(s -> {
                if (s.tickCount % 10 != 0 || s.flags.contains("amalgam_dead") || s.tickCount < 40) return;
                if (amalgamAlive(s)) return;
                s.flags.add("amalgam_dead"); s.setCount("loop", 0);
                Fx.sound(s, SoundEvents.WITCH_CELEBRATE, 2f, 0.5f);
                // she was busy with Burn Bright for Me: switch straight to Enrage (not if the Amalgam fell during the opening)
                if (s.getTurn() >= 2) { s.setCount("loop", 2); s.forceNext("enrage"); }
            })
            .move(Move.of("puppet_strings", 36).intent(Intent.STRONG_DEBUFF).ranged(14)
                .sound(8, SoundEvents.CROSSBOW_LOADING_MIDDLE, 1.5f, 0.6f)
                .at(12, (s, t) -> stringsTo(s, t))
                .at(23, (s, t) -> { if (t == null) return; // 3 Chains of Binding: can barely act
                    t.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * 60, 2));
                    Fx.slow(t, 30, 1); Fx.sound(s, SoundEvents.CHAIN_PLACE, 2f, 0.6f); Fx.knock(s, t, -0.6); }))
            .move(Move.of("youre_mine", 40).intent(Intent.STRONG_DEBUFF).ranged(14)
                .sound(18, SoundEvents.WITCH_AMBIENT, 2f, 0.6f)
                .at(25, (s, t) -> { if (t == null) return; // 99 Frail, 99 Weak, 99 Vulnerable
                    Fx.frail(t, 300); Fx.weak(t, 300); Fx.vulnerable(t, 300);
                    Fx.particlesAt(s, ParticleTypes.WITCH, t.position().add(0, 1, 0), 30, 0.5, 0.05);
                    Fx.sound(s, SoundEvents.WITHER_AMBIENT, 1f, 1.6f); }))
            .move(Move.of("burn_bright", 36).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(10, SoundEvents.BLAZE_SHOOT, 1.2f, 0.6f)
                .at(20, (s, t) -> {
                    s.addBlock(20);
                    for (SpireMob a : s.minions()) if (a.def.id.equals(TorchHeadAmalgam.ID)) {
                        a.addStrength(1); Fx.particlesAt(s, ParticleTypes.FLAME, a.position().add(0, 3, 0), 30, 0.8, 0.05);
                    }
                    Fx.particles(s, ParticleTypes.ENCHANT, 30, 1.0); Fx.sound(s, SoundEvents.ARMOR_EQUIP_GOLD, 1.5f, 0.7f); }))
            .move(Move.of("off_with_your_head", 52).intent(Intent.ATTACK, 3, 5).reach(3.6f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 1.2f, 0.7f)
                .at(12, Fx.melee(3, 4.2, 150)).at(19, Fx.melee(3, 4.2, 150)).at(26, Fx.melee(3, 4.2, 150))
                .at(33, Fx.melee(3, 4.2, 150)).at(40, Fx.melee(3, 4.2, 150))
                .atAll(new int[]{12, 19, 26, 33, 40}, Fx.sfx(SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.9f)))
            .move(Move.of("execution", 44).intent(Intent.ATTACK, 15, 1).reach(3.6f)
                .sound(12, SoundEvents.ANVIL_LAND, 0.6f, 0.5f)
                .at(24, (s, t) -> { Fx.meleeNow(s, 15, 4.4, 90, v -> Fx.knock(s, v, 0.6));
                    Fx.particlesAt(s, ParticleTypes.SWEEP_ATTACK, Fx.rel(s, 2.5, 0, 0.6), 3, 0.3, 0);
                    Fx.sound(s, SoundEvents.PLAYER_ATTACK_CRIT, 1.6f, 0.5f); }))
            .move(Move.of("enrage", 40).intent(Intent.BUFF).noTarget()
                .at(24, (s, t) -> { s.addStrength(2); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 12, 1.0);
                    Fx.sound(s, SoundEvents.RAVAGER_ROAR, 1.2f, 1.5f); }))
            .onClientTick(s -> {
                if (s.tickCount % 6 == 0) s.level().addParticle(ParticleTypes.ENCHANT,
                    s.getX() + (s.getRandom().nextDouble() - .5) * 1.2, s.getY() + 4.2, s.getZ() + (s.getRandom().nextDouble() - .5) * 1.2, 0, 0.05, 0);
            });
    }

    /** golden thread particles from her hands to the target */
    static void stringsTo(SpireMob s, LivingEntity t) {
        if (t == null) return;
        var from = Fx.rel(s, 0.8, 0, 2.6); var to = t.position().add(0, t.getBbHeight() * 0.6, 0);
        for (int i = 0; i <= 24; i++) {
            var p = from.lerp(to, i / 24.0);
            Fx.particlesAt(s, ParticleTypes.WAX_ON, p, 2, 0.08, 0);
        }
    }
}
