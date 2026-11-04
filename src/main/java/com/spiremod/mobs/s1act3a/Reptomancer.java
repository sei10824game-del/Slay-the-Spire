package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 Reptomancer (180 HP, elite). Turn 1 summons 2 Daggers; then 33% Summon (2 Daggers, only if <=2 alive), 33% Snake Strike (13x2 + Weak), 33% Big Bite (30).
 * Never the same move twice. Snake-headed mage in jade robes, staff in hand.
 */
public final class Reptomancer {
    private Reptomancer() {}
    static final int ROBE = 0x2f5a46, ROBE_D = 0x16301f, GOLD = 0xd0a840, SCALE = 0x6aa04a, SCALE_D = 0x3a6a2c, BELLY = 0xe0d08a, EYE = 0xffd030, STAFF = 0x4a3020;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("reptomancer");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3, 9, 0);
            leg.box(-2, -9, -2, 4, 9, 4).color(SCALE_D).noise(0.1f);
            leg.box(-2.5f, -9, -3, 5, 2, 6).color(0x20301a).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 9, 0);
        body.box(-5, 0, -3, 10, 4, 6).color(ROBE_D).grad(ROBE, ROBE_D);
        Part skirt = body.add("skirt").at(0, 3, 0);
        skirt.box(-6, -10, -4, 12, 8, 8).color(ROBE_D).grad(ROBE, ROBE_D);
        skirt.box(-6.5f, -10.5f, -4.5f, 13, 2, 9).color(GOLD).flat().noise(0.04f);
        Part torso = body.add("torso").at(0, 4, 0);
        torso.box(-5, 0, -3, 10, 10, 6).color(ROBE).grad(0x3d7058, ROBE).bevel(0.1f);
        torso.box(-5.2f, 0, -3.2f, 10, 2, 6).color(GOLD).flat().inflate(0.2f);
        torso.box(-1, 2, -3.4f, 2, 8, 1).color(GOLD).flat();
        torso.box(-7, 8, -3.5f, 14, 3, 7).color(ROBE_D).bevel(0.2f);                       // mantle
        Part neck = torso.add("neck").at(0, 10, 0);
        neck.box(-2, 0, -2, 4, 3, 4).color(SCALE);
        Part head = neck.add("head").at(0, 3, 0);
        head.box(-3, 0, -3, 6, 5, 7).color(SCALE).grad(0x86c05a, SCALE).bevel(0.15f);        // skull
        head.box(-2, -0.5f, 4, 4, 2, 4).color(SCALE).bevel(0.15f);                           // snout upper
        head.box(-3.5f, 4, -3.5f, 7, 2, 7).color(GOLD).flat().noise(0.04f);                  // crown band
        head.box(-0.5f, 6, -1, 1, 3, 2).color(GOLD).flat();
        for (int s = -1; s <= 1; s += 2) head.box(s * 3 - 0.5f, 3, 1, 1, 2, 2).color(0x101008).flat().glow(Face.FRONT, 0, 0, 1, 2, EYE);
        head.box(-3.01f, 1, 1.5f, 1, 2, 2).color(0x101008).flat();
        Part jaw = head.add("jaw").at(0, 0, 1);
        jaw.box(-2, -1.5f, 0, 4, 1, 7).color(BELLY).bevel(0.1f);
        jaw.box(-1.5f, -0.5f, 6.5f, 1, 1, 1).color(0xffffff).flat();
        jaw.box(0.5f, -0.5f, 6.5f, 1, 1, 1).color(0xffffff).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 7, 9, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(ROBE).bevel(0.1f);
            arm.box(-2, -7, -2, 4, 2, 4).color(GOLD).flat();
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -7, 0);
            hand.box(-1.5f, -3, -1.5f, 3, 3, 3).color(SCALE);
            if (s < 0) {
                Part staff = hand.add("staff").at(0, -2, 0).rot(-90, 0, 0);
                staff.box(-0.5f, -8, -0.5f, 1, 24, 1).color(STAFF);
                staff.box(-1.5f, 15, -1.5f, 3, 3, 3).color(GOLD).flat();
                staff.box(-1, 18, -1, 2, 3, 2).color(0x8aff6a).flat().glow(Face.FRONT, 0, 0, 2, 3, 0x8aff6a);
            }
        }
        Part tail = body.add("tail").at(0, 1, -3);
        tail.box(-1.5f, -2, -8, 3, 3, 8).color(SCALE).noise(0.1f);
        Part tail2 = tail.add("tail2").at(0, 0, -8);
        tail2.box(-1, -1.5f, -7, 2, 2, 7).color(SCALE_D);
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 34f, 22f);
        m.clip("idle", 50).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 25, -2, 0, 0).rot("torso", 50, 2, 0, 0)
            .rot("tail", 0, 0, 12, 0).rot("tail", 25, 0, -12, 0).rot("tail", 50, 0, 12, 0)
            .rot("tail2", 0, 0, -18, 0).rot("tail2", 25, 0, 18, 0).rot("tail2", 50, 0, -18, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 36, 0, 0, 0).rot("jaw", 40, 14, 0, 0).rot("jaw", 44, 0, 0, 0)
            .rot("armR", 0, 4, 0, 6).rot("armR", 25, 7, 0, 8).rot("armR", 50, 4, 0, 6);
        m.clip("spawn_dagger", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 12, 0, 0).rot("torso", 18, -14, 0, 0, Ease.OUT_BACK).rot("torso", 34, -8, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 20, 0, 10).rot("armR", 18, -170, 0, 25, Ease.OUT_BACK).rot("armR", 34, -165, 0, 25).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 20, 0, -10).rot("armL", 18, -150, 0, -25, Ease.OUT_BACK).rot("armL", 34, -150, 0, -25).rot("armL", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 18, -35, 0, 0).rot("head", 34, -30, 0, 0).rot("head", 44, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 18, 30, 0, 0).rot("jaw", 34, 30, 0, 0).rot("jaw", 44, 0, 0, 0);
        // SNAKE STRIKE: two quick staff / head lunges
        m.clip("snake_strike", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -14, -20, 0).rot("torso", 11, 20, 25, 0, Ease.IN_CUBIC).rot("torso", 19, -12, 20, 0).rot("torso", 22, 22, -25, 0, Ease.IN_CUBIC).rot("torso", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -130, 0, 20).rot("armR", 11, -60, 0, -10, Ease.IN_CUBIC).rot("armR", 19, -130, 0, -10).rot("armR", 22, -60, 0, 10, Ease.IN_CUBIC).rot("armR", 40, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 8, -18, 0, 0).rot("neck", 11, 40, 0, 0, Ease.IN_CUBIC).rot("neck", 19, -18, 0, 0).rot("neck", 22, 40, 0, 0, Ease.IN_CUBIC).rot("neck", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 25, 0, 0).rot("jaw", 12, 0, 0, 0).rot("jaw", 19, 25, 0, 0).rot("jaw", 23, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 11, 0, 0, 4).pos("body", 22, 0, 0, 6).pos("body", 40, 0, 0, 0);
        // BIG BITE: rear up, head back, jaws wide, slam down
        m.clip("big_bite", 46)
            .rot("torso", 0, 0, 0, 0).rot("torso", 16, -26, 0, 0).rot("torso", 22, 38, 0, 0, Ease.IN_CUBIC).rot("torso", 46, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 16, -35, 0, 0).rot("neck", 22, 50, 0, 0, Ease.IN_CUBIC).rot("neck", 46, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 16, 45, 0, 0).rot("jaw", 22, -8, 0, 0, Ease.IN_CUBIC).rot("jaw", 28, -8, 0, 0).rot("jaw", 46, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, 1, -4).pos("body", 22, 0, -3, 9, Ease.IN_CUBIC).pos("body", 46, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 16, -50, 0, 30).rot("armR", 22, 20, 0, 0).rot("armR", 46, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 16, -50, 0, -30).rot("armL", 22, 20, 0, 0).rot("armL", 46, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 16, 0, 6).rot("torso", 10, 0, 0, 0).rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 30, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 34).rot("torso", 0, 0, 0, 0).rot("torso", 10, 20, 0, 0).rot("torso", 34, 10, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, -5, 0, Ease.IN_QUAD).pos("body", 34, 0, -9, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 34, 10, 0, 60).rot("armL", 0, 0, 0, 0).rot("armL", 34, 10, 0, -60)
            .rot("head", 0, 0, 0, 0).rot("head", 34, 30, 15, 10).rot("jaw", 0, 0, 0, 0).rot("jaw", 20, 40, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 20, -70, 0, 10).rot("legL", 0, 0, 0, 0).rot("legL", 20, -60, 0, -10);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("reptomancer", "Reptomancer", "レプトマンサー")
            .game(1, 3).size(0.8f, 1.9f).hp(180).speed(0.25f).style(Style.WITCH).egg(0x2f5a46, 0xd0a840).elite()
            .model(Reptomancer::model)
            .spawn(1, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(s -> {
                if (s.getTurn() == 0) return "spawn_dagger";
                int alive = Fx.countAlive(s, "dagger", 24);
                String last = s.lastMove();
                for (int i = 0; i < 10; i++) {
                    int r = s.rand().nextInt(3);
                    String id = r == 0 ? "spawn_dagger" : r == 1 ? "snake_strike" : "big_bite";
                    if (id.equals(last) || (id.equals("spawn_dagger") && alive > 2)) continue;
                    return id;
                }
                return "snake_strike";
            })
            .move(Move.of("spawn_dagger", 44).intent(Intent.SUMMON).noTarget()
                .sound(16, SoundEvents.EVOKER_PREPARE_SUMMON, 1.2f, 1f)
                .at(20, (s, t) -> { Fx.summon(s, "dagger", -1.4, 1.0); Fx.summon(s, "dagger", 1.4, 1.0); }))
            .move(Move.of("snake_strike", 40).intent(Intent.ATTACK_DEBUFF, 13, 2).reach(2.6f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.2f).sound(19, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.3f)
                .at(11, Fx.melee(13, 3.0, 100, v -> Fx.weak(v, 5))).at(22, Fx.melee(13, 3.0, 100, v -> Fx.weak(v, 5))))
            .move(Move.of("big_bite", 46).intent(Intent.ATTACK, 30, 1).reach(2.8f)
                .sound(16, SoundEvents.RAVAGER_ROAR, 1f, 1.2f).sound(22, SoundEvents.RAVAGER_ATTACK, 1.2f, 0.8f)
                .at(22, Fx.melee(30, 3.2, 100)));
    }
}
