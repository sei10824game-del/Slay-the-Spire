package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Gremlin Merc minion: Sneaky Gremlin (10-14 HP). Wakes up (does nothing), then Tackle (9) every turn. */
public final class SneakyGremlin {
    private SneakyGremlin() {}
    static final int SKIN = 0x8aa84a, SKIN_D = 0x5a7a30, CLOTH = 0x3a3a52;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_sneaky_gremlin");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 1.8f, 4, 0);
            leg.box(-1, -4, -1, 2, 4, 2).color(SKIN_D);
            leg.box(-1.5f, -4, -1.5f, 3, 1, 4).color(0x2a2218).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 4, 0);
        body.box(-3, 0, -2, 6, 5, 4).color(CLOTH).grad(0x555570, CLOTH).bevel(0.25f);
        body.box(-3.2f, 1, -2.2f, 6, 1, 4).color(0xc8a040).flat().inflate(0.2f);
        Part head = body.add("head").at(0, 5, 0);
        head.box(-3.5f, 0, -3, 7, 6, 6).color(SKIN).grad(0xa8c860, SKIN_D).bevel(0.3f);
        head.box(-4, 4, -3.5f, 8, 3, 7).color(CLOTH).bevel(0.3f);                                 // hood
        head.box(-2, 1, 3, 4, 1, 1).color(0xf0e8c0).flat();                                      // sly grin
        for (int s = -1; s <= 1; s += 2) {
            head.box(s * 1.8f - 0.5f, 3, 3, 1, 1, 1).color(0xfff080).glow(Face.FRONT, 0, 0, 1, 1, 0xffe040);
            Part ear = head.add(s < 0 ? "earR" : "earL").at(s * 3.5f, 3, 0).rot(0, s * 20, s * -35);
            ear.box(-0.5f, -1, -1, 5, 3, 1).color(SKIN).bevel(0.15f);
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 4, 5, 0);
            arm.box(-1, -5, -1, 2, 5, 2).color(SKIN_D);
            arm.box(-1.5f, -6, -1.5f, 3, 1.5f > 0 ? 1 : 1, 3).color(SKIN);
        }
        m.look("head", 45, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 45f, 30f);
        m.clip("idle", 30).loop()
            .rot("body", 0, 6, 0, 0).rot("body", 15, 10, 0, 0).rot("body", 30, 6, 0, 0)
            .rot("armR", 0, 20, 0, 10).rot("armR", 15, 30, 0, 12).rot("armR", 30, 20, 0, 10)
            .rot("armL", 0, 20, 0, -10).rot("armL", 15, 30, 0, -12).rot("armL", 30, 20, 0, -10)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 0, 10, 0).rot("head", 20, 0, -10, 0).rot("head", 30, 0, 0, 0);
        m.clip("spawned", 24)
            .scl("body", 0, 0.4f, 0.4f, 0.4f).scl("body", 12, 1.2f, 1.2f, 1.2f, Ease.OUT_BACK).scl("body", 24, 1, 1, 1)
            .rot("head", 0, 30, 0, 0).rot("head", 12, -15, 0, 0).rot("head", 24, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -150, 0, 20).rot("armR", 24, 0, 0, 0).rot("armL", 0, 0, 0, 0).rot("armL", 14, -150, 0, -20).rot("armL", 24, 0, 0, 0);
        m.clip("tackle", 24)
            .rot("body", 0, 0, 0, 0).rot("body", 7, -14, 0, 0).rot("body", 11, 35, 0, 0, Ease.IN_CUBIC).rot("body", 24, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 7, 0, -1, -2).pos("body", 11, 0, 0, 6, Ease.IN_CUBIC).pos("body", 24, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 7, 40, 0, 20).rot("armR", 11, -100, 0, -10).rot("armR", 24, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 7, 40, 0, -20).rot("armL", 11, -100, 0, 10).rot("armL", 24, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 11, -40, 0, 0).rot("legR", 24, 0, 0, 0).rot("legL", 0, 0, 0, 0).rot("legL", 11, 30, 0, 0).rot("legL", 24, 0, 0, 0);
        U.hurt(m, "body");
        U.death(m, "body", 90, 2);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_sneaky_gremlin", "Sneaky Gremlin", "こそこそグレムリン")
            .game(2, 1).size(0.5f, 1.0f).hp(12).speed(0.32f).style(Style.ZOMBIE).egg(0x8aa84a, 0x3a3a52)
            .model(SneakyGremlin::model)
            .pattern(Pattern.first("spawned", s -> "tackle"))
            .move(Move.of("spawned", 24).intent(Intent.STUN).noTarget())
            .move(Move.of("tackle", 24).intent(Intent.ATTACK, 9, 1).reach(2.0f)
                .sound(8, SoundEvents.VILLAGER_CELEBRATE, 0.9f, 1.9f)
                .at(11, Fx.melee(9, 2.4, 80)));
    }
}
