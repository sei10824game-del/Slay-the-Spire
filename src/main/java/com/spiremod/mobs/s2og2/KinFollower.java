package com.spiremod.mobs.s2og2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth: Kin Follower (58 HP minion of the Kin Priest). Cycle Quick Slash 5 -> Boomerang 2x2 -> Power Dance (+2 Strength).
 * var=1 followers start the cycle at Power Dance (the two followers are offset from each other).
 */
public final class KinFollower {
    private KinFollower() {}

    static final int ROBE = 0x9a3b2a, ROBE_D = 0x5c2018, ROBE_L = 0xc2563a, WRAP = 0xe3d3a0, SKIN = 0x3f3346, BONE = 0xe6dcc0, BLADE = 0xc9ced6, WOOD = 0x7a5232, EYE = 0xffd24a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_kin_follower").scale(1.0f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 8, 0);
            leg.box(-1.5f, -8, -1.5f, 3, 8, 3).color(WRAP).band(2, 1, 0xb3a374).band(5, 1, 0xb3a374).noise(0.1f);
            leg.box(-2, -8, -2.5f, 4, 2, 5).color(0x3a2a22).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 8, 0);
        body.box(-4, 0, -2.5f, 8, 3, 5).color(ROBE_D).grad(ROBE, ROBE_D);
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-4, 0, -2.5f, 8, 8, 5).color(ROBE).grad(ROBE_L, ROBE).bevel(0.1f).paint(Face.FRONT, 0, 5, 8, 1, WRAP);
        torso.box(-4.2f, 0, -2.7f, 8, 2, 5).color(WRAP).flat().noise(0.05f).inflate(0.25f);
        Part skirt = body.add("skirt").at(0, 2, 0);
        skirt.box(-5, -7, -3.2f, 10, 5, 6).color(ROBE_D).grad(ROBE, ROBE_D);
        Part head = torso.add("head").at(0, 8, 0);
        head.box(-3, 0, -3, 6, 6, 6).color(SKIN);
        head.box(-3.6f, 3, -3.6f, 7, 4, 7).color(ROBE_D).grad(ROBE, ROBE_D);                                // hood
        head.box(-2.7f, 0.2f, 3, 5, 5, 1).color(BONE).bevel(0.15f).glow(Face.FRONT, 0, 1, 1, 1, EYE).glow(Face.FRONT, 4, 1, 1, 1, EYE).paint(Face.FRONT, 1, 3, 3, 1, 0x3a2030);
        for (int s = -1; s <= 1; s += 2) {
            Part ear = head.add(s < 0 ? "earR" : "earL").at(s * 3.2f, 6, 0).rot(0, 0, s * -25);
            ear.box(-0.5f, 0, -0.5f, 1, 5, 1).color(ROBE_D).bevel(0.1f);
            ear.box(-0.5f, 5, -0.5f, 1, 1, 1).color(EYE).flat();
        }
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part arm = torso.add("arm" + n).at(s * 5.5f, 7, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(ROBE).bevel(0.1f);
            Part hand = arm.add("hand" + n).at(0, -6, 0);
            hand.box(-1, -3, -1, 2, 3, 2).color(SKIN);
            if (s < 0) { // right hand: hooked blade
                Part blade = hand.add("blade").at(0, -2, 0).rot(-90, 0, 0);
                blade.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
                blade.box(-1.5f, 2, -0.5f, 3, 1, 1).color(0xd4a63a).flat();
                blade.box(-0.5f, 3, -0.4f, 1, 7, 1).color(BLADE).flat();
                blade.box(-0.5f, 9, 0.4f, 1, 2, 1).color(BLADE).flat();
            } else {     // left hand carries the boomerang (flies out in Boomerang)
                Part boom = hand.add("boomerang").at(0, -2, 0).rot(0, 0, 0);
                boom.box(-1, -1, -5, 2, 2, 7).color(WOOD).bevel(0.1f);
                boom.box(-1, -1, 1, 2, 2, 5).color(WOOD).bevel(0.1f).inflate(0.1f);
                boom.box(-1, -1, 4, 2, 2, 2).color(ROBE_L).flat();
            }
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 38f, 25f);
        m.clip("idle", 40).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 20, -2, 0, 0).rot("torso", 40, 2, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 0, 10, 4).rot("head", 28, 0, -10, -4).rot("head", 40, 0, 0, 0)
            .rot("armR", 0, 6, 0, 6).rot("armR", 20, 10, 0, 8).rot("armR", 40, 6, 0, 6)
            .rot("armL", 0, 6, 0, -6).rot("armL", 20, 10, 0, -8).rot("armL", 40, 6, 0, -6);
        m.clip("quick_slash", 22)
            .rot("torso", 0, 0, 0, 0).rot("torso", 6, -8, -28, 0).rot("torso", 9, 20, 30, 0, Ease.IN_CUBIC).rot("torso", 22, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, 0, -2).pos("body", 9, 0, -1, 5, Ease.IN_CUBIC).pos("body", 22, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, 40, 20, 40).rot("armR", 9, -110, -20, -10, Ease.IN_CUBIC).rot("armR", 14, -105, -20, -10).rot("armR", 22, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 6, -30, 0, -20).rot("armL", 9, 20, 0, -10).rot("armL", 22, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 9, -45, 0, 0).rot("legR", 22, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 6, 20, 0, 0).rot("legL", 9, 25, 0, 0).rot("legL", 22, 0, 0, 0);
        // BOOMERANG: wind up, hurl twice (left arm sweeps, boomerang flies out and returns)
        m.clip("boomerang", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -10, 25, 0).rot("torso", 11, 10, -25, 0, Ease.IN_CUBIC).rot("torso", 20, -6, 25, 0).rot("torso", 23, 10, -25, 0, Ease.IN_CUBIC).rot("torso", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -40, 0, -70).rot("armL", 11, -120, -20, 20, Ease.IN_CUBIC).rot("armL", 16, 10, 0, -10).rot("armL", 20, -40, 0, -70).rot("armL", 23, -120, -20, 20, Ease.IN_CUBIC).rot("armL", 36, 0, 0, 0)
            .pos("boomerang", 0, 0, 0, 0).pos("boomerang", 11, 0, 0, 14, Ease.OUT_QUAD).pos("boomerang", 16, 0, 0, 0).pos("boomerang", 23, 0, 0, 14, Ease.OUT_QUAD).pos("boomerang", 30, 0, 0, 0)
            .rot("boomerang", 0, 0, 0, 0).rot("boomerang", 11, 0, 360, 0).rot("boomerang", 16, 0, 720, 0).rot("boomerang", 23, 0, 1080, 0).rot("boomerang", 30, 0, 1440, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, 20, 0, 20).rot("armR", 36, 0, 0, 0);
        m.clip("power_dance", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 0, 90, 0).rot("body", 16, 0, 180, 0).rot("body", 24, 0, 270, 0).rot("body", 32, 0, 360, 0).rot("body", 40, 0, 360, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 3, 0, Ease.OUT_QUAD).pos("body", 16, 0, 0, 0).pos("body", 24, 0, 3, 0, Ease.OUT_QUAD).pos("body", 32, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -150, 0, 40, Ease.OUT_BACK).rot("armR", 32, -150, 0, 40).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -150, 0, -40, Ease.OUT_BACK).rot("armL", 32, -150, 0, -40).rot("armL", 40, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 8, -35, 0, 0).rot("legR", 16, 10, 0, 0).rot("legR", 24, -35, 0, 0).rot("legR", 40, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 8, 10, 0, 0).rot("legL", 16, -35, 0, 0).rot("legL", 24, 10, 0, 0).rot("legL", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -25, 0, 0).rot("head", 32, -25, 0, 0).rot("head", 40, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 20, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 10).rot("head", 10, 0, 0, 0);
        m.clip("death", 28)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 25, 0, 0).rot("torso", 28, 8, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -4, 0, Ease.IN_QUAD).pos("body", 28, 0, -8, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -120, 0, 40).rot("armR", 28, 20, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -40).rot("armL", 28, 10, 0, -70)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 28, 30, 15, 10)
            .rot("legR", 0, 0, 0, 0).rot("legR", 16, -70, 0, 10).rot("legR", 28, -80, 0, 10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 16, -60, 0, -10).rot("legL", 28, -80, 0, -10);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_kin_follower", "Kin Follower", "キンの信徒")
            .game(2, 1).minion().size(0.6f, 1.75f).hp(58).speed(0.27f).style(Style.PILLAGER).egg(0x9a3b2a, 0xffd24a)
            .model(KinFollower::model)
            .pattern(s -> new String[]{"quick_slash", "boomerang", "power_dance"}[(s.getTurn() + (s.getVar() == 1 ? 2 : 0)) % 3])
            .move(Move.of("quick_slash", 22).intent(Intent.ATTACK, 5, 1).reach(2.4f)
                .sound(7, SoundEvents.PHANTOM_FLAP, 0.8f, 1.5f)
                .at(9, Fx.melee(5, 2.8, 70)))
            .move(Move.of("boomerang", 36).intent(Intent.ATTACK, 2, 2).ranged(9)
                .sound(8, SoundEvents.TRIDENT_THROW, 0.9f, 1.4f)
                .at(13, (s, t) -> { Fx.hitTarget(2).run(s, t); Fx.sound(s, SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.5f); })
                .at(25, (s, t) -> { Fx.hitTarget(2).run(s, t); Fx.sound(s, SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.5f); }))
            .move(Move.of("power_dance", 40).intent(Intent.BUFF).noTarget()
                .sound(8, SoundEvents.PARROT_IMITATE_WITCH, 1f, 1.6f)
                .at(30, (s, t) -> { s.addStrength(2); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 8, 0.5); }));
    }
}
