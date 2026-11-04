package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Gremlin Merc minion: Fat Gremlin (13-17 HP). Heist: killing it returns the stolen gold; otherwise it flees with it. */
public final class FatGremlin {
    private FatGremlin() {}
    static final int SKIN = 0x8aa84a, SKIN_D = 0x5a7a30, CLOTH = 0x7a4a2a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_fat_gremlin");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 3, 0);
            leg.box(-1.5f, -3, -1.5f, 3, 3, 3).color(SKIN_D);
            leg.box(-2, -3, -2, 4, 1, 5).color(0x2a2218).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 3, 0);
        body.box(-5, 0, -4, 10, 8, 8).color(SKIN).grad(0xa8c860, SKIN_D).noise(0.12f).bevel(0.5f);       // big round belly
        body.box(-4, 0, -4.4f, 8, 5, 1).color(0xb8d070).noise(0.1f);
        body.box(-5.3f, 3, -4.3f, 10, 1, 8).color(CLOTH).flat().inflate(0.2f);                              // belt
        Part sack = body.add("sack").at(0, 6, -4.5f);                                                       // gold sack on its back
        sack.box(-3, -3, -3, 6, 6, 3).color(0xa8884a).grad(0xc8a860, 0x7a5c2c).noise(0.2f).bevel(0.5f);
        sack.box(-1.5f, 3, -2.5f, 3, 1, 2).color(0xe0b83a).flat();
        Part head = body.add("head").at(0, 8, 0.5f);
        head.box(-3, 0, -2.5f, 6, 5, 5).color(SKIN).grad(0xa8c860, SKIN_D).bevel(0.3f);
        head.box(-1, 1, 2.4f, 2, 2, 2).color(SKIN_D).bevel(0.3f);                                            // blobby nose
        head.box(-2, 0, 2.4f, 4, 1, 1).color(0xf0e8c0).flat();
        for (int s = -1; s <= 1; s += 2) {
            head.box(s * 1.6f - 0.5f, 3, 2.4f, 1, 1, 1).color(0xfff080).glow(Face.FRONT, 0, 0, 1, 1, 0xffe040);
            Part ear = head.add(s < 0 ? "earR" : "earL").at(s * 3, 3, 0).rot(0, s * 15, s * -30);
            ear.box(-0.5f, -1, -1, 4, 3, 1).color(SKIN).bevel(0.15f);
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 5, 6, 0);
            arm.box(-1.5f, -5, -1.5f, 3, 5, 3).color(SKIN_D).bevel(0.2f);
        }
        m.look("head", 40, 20);
        m.walkBiped("legR", "legL", "armR", "armL", 35f, 20f);
        m.clip("idle", 36).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 18, 1.05f, 1.03f, 1.05f).scl("body", 36, 1, 1, 1)
            .rot("armR", 0, 5, 0, 10).rot("armR", 18, 10, 0, 14).rot("armR", 36, 5, 0, 10)
            .rot("armL", 0, 5, 0, -10).rot("armL", 18, 10, 0, -14).rot("armL", 36, 5, 0, -10)
            .rot("sack", 0, 0, 0, 0).rot("sack", 18, 6, 0, 5).rot("sack", 36, 0, 0, 0);
        m.clip("spawned", 24)
            .scl("body", 0, 0.4f, 0.4f, 0.4f).scl("body", 12, 1.2f, 1.2f, 1.2f, Ease.OUT_BACK).scl("body", 24, 1, 1, 1)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -150, 0, 20).rot("armR", 24, 0, 0, 0).rot("armL", 0, 0, 0, 0).rot("armL", 14, -150, 0, -20).rot("armL", 24, 0, 0, 0);
        // FLEE: panic, clutch the sack, scamper off
        m.clip("flee", 28)
            .rot("body", 0, 0, 0, 0).rot("body", 6, -12, 0, 0).rot("body", 12, 25, 0, 0).rot("body", 28, 25, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, 1, -1).pos("body", 14, 0, 0, -6, Ease.IN_QUAD).pos("body", 28, 0, 0, -14)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, -140, 0, 20).rot("armR", 12, 20, 0, 20).rot("armR", 18, -40, 0, 20).rot("armR", 28, 20, 0, 20)
            .rot("armL", 0, 0, 0, 0).rot("armL", 6, -140, 0, -20).rot("armL", 12, 20, 0, -20).rot("armL", 18, -40, 0, -20).rot("armL", 28, 20, 0, -20)
            .rot("legR", 0, 0, 0, 0).rot("legR", 8, 50, 0, 0).rot("legR", 14, -50, 0, 0).rot("legR", 20, 50, 0, 0).rot("legR", 28, -50, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 8, -50, 0, 0).rot("legL", 14, 50, 0, 0).rot("legL", 20, -50, 0, 0).rot("legL", 28, 50, 0, 0);
        U.hurt(m, "body");
        U.death(m, "body", 90, 2);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_fat_gremlin", "Fat Gremlin", "太っちょグレムリン")
            .game(2, 1).size(0.7f, 0.9f).hp(15).speed(0.22f).style(Style.ZOMBIE).egg(0x8aa84a, 0xa8884a)
            .model(FatGremlin::model)
            .pattern(Pattern.first("spawned", s -> "flee"))
            .move(Move.of("spawned", 24).intent(Intent.STUN).noTarget())
            .move(Move.of("flee", 28).intent(Intent.ESCAPE).noTarget()
                .sound(6, SoundEvents.VILLAGER_NO, 1f, 1.8f)
                .at(24, (s, t) -> Fx.vanish(s)));
    }
}
