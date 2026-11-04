package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Two-Tailed Rat (17-21 HP). First move staggered (Scratch 8 / Disease Bite 6 / Screech Frail 1), then random;
 * from turn 3 it may Call for Backup (once per rat, group capped by a live-rat count) to summon another rat.
 */
public final class TwoTailedRat {
    private TwoTailedRat() {}
    static final int FUR = 0x6a5a4e, FUR_D = 0x453a32, BELLY = 0x9c8a74, PINK = 0xd89a9a, TAIL = 0xc9a0a0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_two_tailed_rat");
        float[][] lp = {{-2.5f, 3.5f}, {2.5f, 3.5f}, {-2.5f, -3.5f}, {2.5f, -3.5f}};
        String[] ln = {"legFR", "legFL", "legBR", "legBL"};
        for (int i = 0; i < 4; i++) {
            Part leg = m.add(ln[i]).at(lp[i][0], 3.5f, lp[i][1]);
            leg.box(-1, -3.5f, -1, 2, 4, 2).color(FUR_D).noise(0.1f);
            leg.box(-1.5f, -3.5f, -1, 3, 1, 3).color(PINK).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 3.5f, 0);
        body.box(-3.5f, 0, -5.5f, 7, 5, 11).color(FUR).grad(0x8a7a6a, FUR_D).noise(0.2f).bevel(0.3f);
        body.box(-3, 0, -5, 6, 1, 10).color(BELLY).noise(0.1f);
        for (int i = 0; i < 4; i++) body.box(-1, 5, -4 + i * 2.5f, 2, 1, 1).color(FUR_D).noise(0.2f);          // matted ridge
        Part head = body.add("head").at(0, 1, 5);
        head.box(-2.5f, 0, 0, 5, 4, 4).color(FUR).grad(0x8a7a6a, FUR).noise(0.15f).bevel(0.25f);
        head.box(-1.5f, 0.5f, 4, 3, 2, 3).color(0x8a7a6a).bevel(0.25f);                                  // snout
        head.box(-0.5f, 1.5f, 6.8f, 1, 1, 1).color(0xe07a8a).flat();                                      // nose
        Part jaw = head.add("jaw").at(0, 1, 6);
        jaw.box(-1, -1, -3, 2, 1, 3).color(PINK).bevel(0.15f);
        jaw.box(-0.8f, 0, -0.2f, 1, 1, 1).color(0xf0e8c8).flat();
        for (int s = -1; s <= 1; s += 2) {
            head.box(s * 1.8f - 0.5f, 3, 2.5f, 1, 1, 1).color(0xff3a3a).glow(Face.FRONT, 0, 0, 1, 1, 0xff3030);
            Part ear = head.add(s < 0 ? "earR" : "earL").at(s * 2, 4, 1);
            ear.box(-1.5f, 0, -0.5f, 3, 3, 1).color(PINK).bevel(0.15f);
            Part whisk = head.add("wh" + s).at(s * 1.5f, 1.5f, 6).rot(0, s * 35, 0);
            whisk.box(0, 0, 0, 1, 1, 3).color(0xd8d0c0).flat();
        }
        // one tail forking into two near the tip
        Part tail = body.add("tail").at(0, 2, -5);
        tail.box(-0.5f, -0.5f, -5, 1, 1, 5).color(TAIL).noise(0.1f);
        Part tail2 = tail.add("tail2").at(0, 0, -5);
        tail2.box(-0.5f, -0.5f, -4, 1, 1, 4).color(TAIL);
        for (int s = -1; s <= 1; s += 2) {
            Part f = tail2.add(s < 0 ? "forkR" : "forkL").at(0, 0, -4).rot(0, s * 40, 0);
            f.box(-0.5f, -0.5f, -4, 1, 1, 4).color(TAIL);
            f.box(-0.5f, -0.5f, -6, 1, 1, 2).color(0xe8c0c0);
        }
        m.look("head", 40, 25);
        m.walkQuad("legFR", "legBL", "legFL", "legBR", 40);
        m.clip("idle", 36).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 18, 1.03f, 1.05f, 1, Ease.SMOOTH).scl("body", 36, 1, 1, 1)
            .rot("tail", 0, 0, 14, 0).rot("tail", 18, 0, -14, 0).rot("tail", 36, 0, 14, 0)
            .rot("tail2", 0, 0, -20, 0).rot("tail2", 18, 0, 20, 0).rot("tail2", 36, 0, -20, 0)
            .rot("earR", 0, 0, 0, 0).rot("earR", 14, 0, 0, 12).rot("earR", 28, 0, 0, 0).rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 10, 0, 0).rot("jaw", 14, 0, 0, 0).rot("jaw", 36, 0, 0, 0);
        m.clip("scratch", 24)
            .rot("body", 0, 0, 0, 0).rot("body", 7, -22, 0, 0).rot("body", 11, 12, 0, 0, Ease.IN_CUBIC).rot("body", 24, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 7, 0, 1.5f, -1).pos("body", 11, 0, 0, 3, Ease.IN_CUBIC).pos("body", 24, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 7, -120, 0, 0).rot("legFR", 11, -10, 0, 0, Ease.IN_CUBIC).rot("legFR", 14, -80, 0, 0).rot("legFR", 17, 0, 0, 0).rot("legFR", 24, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 9, -120, 0, 0).rot("legFL", 13, -10, 0, 0, Ease.IN_CUBIC).rot("legFL", 24, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 11, 30, 0, 0).rot("jaw", 20, 0, 0, 0);
        m.clip("disease_bite", 26)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -12, 0, 0).rot("body", 12, 15, 0, 0, Ease.IN_CUBIC).rot("body", 26, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -2).pos("body", 12, 0, 0, 4, Ease.IN_CUBIC).pos("body", 26, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -15, 0, 0).rot("head", 12, 25, 0, 0, Ease.IN_CUBIC).rot("head", 18, 10, 0, 0).rot("head", 26, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 60, 0, 0, Ease.OUT_BACK).rot("jaw", 12, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 15, 45, 0, 0).rot("jaw", 18, 0, 0, 0).rot("jaw", 26, 0, 0, 0);
        m.clip("screech", 28)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -30, 0, 0, Ease.OUT_BACK).rot("body", 22, -26, 0, 0).rot("body", 28, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 2, -1).pos("body", 28, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -25, 0, 0).rot("head", 22, -20, 0, 0).rot("head", 28, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 70, 0, 0, Ease.OUT_BACK).rot("jaw", 22, 60, 0, 0).rot("jaw", 28, 0, 0, 0)
            .rot("earR", 0, 0, 0, 0).rot("earR", 8, 0, 0, 40).rot("earL", 0, 0, 0, 0).rot("earL", 8, 0, 0, -40).rot("earR", 28, 0, 0, 0).rot("earL", 28, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 10, 0, 40, 0).rot("tail", 16, 0, -40, 0).rot("tail", 22, 0, 40, 0).rot("tail", 28, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 8, 1.12f, 1.12f, 1.12f).scl("body", 28, 1, 1, 1);
        m.clip("call_for_backup", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -35, 0, 0, Ease.OUT_BACK).rot("body", 24, -30, 0, 0).rot("body", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -40, 0, 0).rot("head", 24, -30, 0, 0).rot("head", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 50, 0, 0).rot("jaw", 14, 10, 0, 0).rot("jaw", 18, 50, 0, 0).rot("jaw", 22, 10, 0, 0).rot("jaw", 30, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 10, -60, 0, -20).rot("legFR", 30, 0, 0, 0).rot("legFL", 0, 0, 0, 0).rot("legFL", 10, -60, 0, 20).rot("legFL", 30, 0, 0, 0);
        U.hurt(m, "body");
        m.clip("death", 28)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -15, 0, 10).rot("body", 20, 0, 0, 180, Ease.OUT_BOUNCE).rot("body", 28, 0, 0, 180)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, 4, 0, Ease.OUT_BOUNCE)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 20, 0, 0, 60).rot("legFL", 0, 0, 0, 0).rot("legFL", 20, 0, 0, -60)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 20, 0, 0, 60).rot("legBL", 0, 0, 0, 0).rot("legBL", 20, 0, 0, -60);
        return m;
    }

    static final String[] OPEN = {"scratch", "disease_bite", "screech"};

    public static MobDef def() {
        return MobDef.of("s2_two_tailed_rat", "Two-Tailed Rat", "双尾のネズミ")
            .game(2, 1).size(0.7f, 0.6f).hp(19).speed(0.32f).style(Style.SPIDER).egg(0x6a5a4e, 0xc9a0a0)
            .model(TwoTailedRat::model)
            .spawn(8, 3, 3, MobDef.Habitat.CAVE)
            .pattern(s -> {
                if (s.getTurn() == 0) return OPEN[s.rand().nextInt(3)];
                if (s.getTurn() >= 2 && !s.flags.contains("called") && Fx.countAlive(s, "s2_two_tailed_rat", 32) < 6 && s.rand().nextInt(4) == 0) return "call_for_backup";
                return OPEN[s.rand().nextInt(3)];
            })
            .move(Move.of("scratch", 24).intent(Intent.ATTACK, 8, 1).reach(2.0f)
                .sound(7, SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.6f)
                .at(11, Fx.melee(8, 2.4, 80)))
            .move(Move.of("disease_bite", 26).intent(Intent.ATTACK, 6, 1).reach(2.0f)
                .sound(8, SoundEvents.FOX_BITE, 1f, 1.3f)
                .at(12, Fx.melee(6, 2.4, 70, v -> Fx.curse(v, Fx.Curse.WOUND))))
            .move(Move.of("screech", 28).intent(Intent.DEBUFF).reach(6)
                .sound(8, SoundEvents.BAT_AMBIENT, 1.4f, 0.6f)
                .at(10, (s, t) -> { Fx.particles(s, ParticleTypes.NOTE, 8, 0.5); if (t != null && t.distanceTo(s) < 7) Fx.frail(t, 6); }))
            .move(Move.of("call_for_backup", 30).intent(Intent.SUMMON).noTarget()
                .sound(12, SoundEvents.BAT_TAKEOFF, 1.4f, 0.6f)
                .at(16, (s, t) -> { s.flags.add("called"); Fx.summon(s, "s2_two_tailed_rat", 1.2, 0.6); }));
    }
}
