package com.spiremod.mobs.s1act1a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Looter, 44-48 HP. Mug 10 x2 turns, then 50% Lunge 12 / 50% Smoke Bomb (6 Block); Lunge on turn 3 is followed by Smoke Bomb;
 * Smoke Bomb is followed by Escape. Steals 15 gold per attack (refunded on death by the engine's stolenGold).
 */
public final class Looter {
    private Looter() {}

    static final int CLOAK = 0x4a4438, CLOAK_D = 0x2a261e, CLOAK_L = 0x6a6250, SKIN = 0xc9a07a, SACK = 0x8a6a3a, STEEL = 0xcfd6dc;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("looter");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.2f, 8, 0);
            leg.box(-1.5f, -8, -1.5f, 3, 8, 3).color(0x3a3228).noise(0.08f);
            leg.box(-1.8f, -8, -2.4f, 3, 2, 5).color(0x1d1812).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 8, 0);
        body.box(-3.5f, 0, -2.5f, 7, 3, 5).color(CLOAK_D);
        Part torso = body.add("torso").at(0, 3, 0).rot(12, 0, 0);          // hunched
        torso.box(-4, 0, -2.5f, 8, 8, 5).color(CLOAK).grad(CLOAK_L, CLOAK_D).noise(0.08f).bevel(0.1f);
        torso.box(-4.2f, 1, -2.7f, 8, 1, 5).color(0x2a1c10).flat().inflate(0.2f);
        Part skirt = body.add("skirt").at(0, 2, 0);
        skirt.box(-4.5f, -8, -3, 9, 6, 6).color(CLOAK_D).grad(CLOAK, CLOAK_D);
        // sack of loot on back
        Part sack = torso.add("sack").at(0, 3, -2.5f);
        sack.box(-3.5f, 0, -6, 7, 7, 6).color(SACK).grad(0xa88848, 0x5a4220).noise(0.15f).bevel(0.3f);
        sack.box(-1, 7, -4.5f, 2, 2, 3).color(0x6a4a24);
        sack.box(-2, 3, -6.2f, 1, 1, 1).color(0xffd84a).flat().glow(Face.BACK, 0, 0, 1, 1, 0xffd84a);
        Part head = torso.add("head").at(0, 8, 0).rot(-8, 0, 0);
        head.box(-3, 0, -3, 6, 6, 6).color(SKIN).noise(0.05f);
        head.box(-3.5f, 2.5f, -3.5f, 7, 5, 7).color(CLOAK).grad(CLOAK_L, CLOAK_D).inflate(0.1f); // hood
        head.box(-3.4f, -0.5f, -3.4f, 7, 3, 1).color(CLOAK_D);                                    // cloth over mouth
        head.box(-2, 3, -3.1f, 1, 1, 1).color(0xffe070).flat().glow(Face.FRONT, 0, 0, 1, 1, 0xffe070);
        head.box(1, 3, -3.1f, 1, 1, 1).color(0xffe070).flat().glow(Face.FRONT, 0, 0, 1, 1, 0xffe070);
        head.box(-0.5f, 1, 3, 1, 1, 1).color(CLOAK_D);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 5, 7, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(CLOAK).grad(CLOAK_L, CLOAK_D).bevel(0.1f);
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1, -3, -1, 2, 3, 2).color(SKIN);
            if (s < 0) { // curved dagger
                Part d = hand.add("dagger").at(0, -2, -1).rot(-90, 0, 0);
                d.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
                d.box(-1.5f, 2, -0.5f, 3, 1, 1).color(0xc8962e).flat();
                d.box(-0.5f, 3, -0.4f, 1, 6, 1).color(STEEL).flat();
                d.box(-0.5f, 8, 0.2f, 1, 2, 1).color(STEEL).flat();
            }
        }
        m.look("head", 50, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 40f, 25f);
        m.clip("idle", 40).loop()
            .rot("torso", 0, 0, 0, 0).rot("torso", 20, 3, 0, 0).rot("torso", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 0, 18, 0).rot("head", 20, 0, 18, 0).rot("head", 30, 0, -18, 0).rot("head", 40, 0, 0, 0)
            .rot("armR", 0, 5, 0, 6).rot("armR", 20, 10, 0, 8).rot("armR", 40, 5, 0, 6)
            .rot("sack", 0, 0, 0, 0).rot("sack", 20, 0, 0, 3).rot("sack", 40, 0, 0, 0);
        // MUG: slash across then a second backhand, snatching gold
        m.clip("mug", 26)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -5, -30, 0).rot("torso", 12, 20, 30, 0, Ease.IN_CUBIC).rot("torso", 26, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -2).pos("body", 12, 0, -1, 4, Ease.IN_CUBIC).pos("body", 26, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -140, 30, 30).rot("armR", 12, -80, -30, -10, Ease.IN_CUBIC).rot("armR", 18, -75, -30, -10).rot("armR", 26, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -40, 0, -30).rot("armL", 12, -100, 0, 10).rot("armL", 17, -100, 0, 10).rot("armL", 26, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 10, 25, 0).rot("head", 12, 5, -25, 0).rot("head", 26, 0, 0, 0)
            .rot("sack", 0, 0, 0, 0).rot("sack", 12, 12, 0, 0).rot("sack", 20, -6, 0, 0).rot("sack", 26, 0, 0, 0);
        // LUNGE: crouch low, spring forward with dagger thrust
        m.clip("lunge", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -4, -3).pos("body", 14, 0, -2, 8, Ease.IN_CUBIC).pos("body", 22, 0, -1, 7).pos("body", 30, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -10, 0, 0).rot("torso", 14, 40, 0, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -150, 0, 15).rot("armR", 14, -100, 0, 0, Ease.IN_CUBIC).rot("armR", 22, -100, 0, 0).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 30, 0, -30).rot("armL", 14, -60, 0, -35).rot("armL", 30, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 10, 45, 0, 0).rot("legR", 14, -60, 0, 0).rot("legR", 30, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 10, -30, 0, 0).rot("legL", 14, 40, 0, 0).rot("legL", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 10, 0, 0).rot("head", 14, -15, 0, 0).rot("head", 30, 0, 0, 0);
        // SMOKE BOMB: throw a bomb to the ground, cloak billows, wrap in cloak
        m.clip("smoke_bomb", 30)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -160, 0, 20).rot("armR", 14, -30, 0, 0, Ease.IN_CUBIC).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, 0, 0, 0).rot("armL", 20, -80, 0, -60).rot("armL", 30, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -12, 0, 0).rot("torso", 14, 25, 0, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .rot("skirt", 0, 0, 0, 0).rot("skirt", 18, 0, 0, 0).rot("skirt", 22, 0, 12, 0).rot("skirt", 26, 0, -12, 0).rot("skirt", 30, 0, 0, 0);
        // ESCAPE: hunch and bolt (it vanishes at the end)
        m.clip("escape", 24)
            .rot("torso", 0, 0, 0, 0).rot("torso", 6, 30, 0, 0).rot("torso", 24, 35, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, 40, 0, 15).rot("armR", 24, -40, 0, 15)
            .rot("armL", 0, 0, 0, 0).rot("armL", 6, 40, 0, -15).rot("armL", 24, -40, 0, -15)
            .rot("legR", 0, 0, 0, 0).rot("legR", 4, 50, 0, 0).rot("legR", 8, -50, 0, 0).rot("legR", 12, 50, 0, 0).rot("legR", 16, -50, 0, 0).rot("legR", 20, 50, 0, 0).rot("legR", 24, -50, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 4, -50, 0, 0).rot("legL", 8, 50, 0, 0).rot("legL", 12, -50, 0, 0).rot("legL", 16, 50, 0, 0).rot("legL", 20, -50, 0, 0).rot("legL", 24, 50, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 6, -25, 0, 0).rot("head", 24, -25, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 18, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 10).rot("head", 10, 0, 0, 0)
            .rot("sack", 0, 0, 0, 0).rot("sack", 3, -15, 0, 0).rot("sack", 10, 0, 0, 0);
        m.clip("death", 30)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 25, 0, 0).rot("torso", 30, 8, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -4, 0, Ease.IN_QUAD).pos("body", 30, 0, -8, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -120, 0, 40).rot("armR", 30, 20, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -40).rot("armL", 30, 10, 0, -70)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 30, 30, 15, 10)
            .rot("legR", 0, 0, 0, 0).rot("legR", 18, -70, 0, 10).rot("legR", 30, -80, 0, 10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 18, -60, 0, -10).rot("legL", 30, -80, 0, -10);
        return m;
    }

    static void steal(SpireMob s, net.minecraft.world.entity.LivingEntity v) {
        if (Fx.steal(s, v, 15) > 0) Fx.sound(s, SoundEvents.EXPERIENCE_ORB_PICKUP, 1f, 1.6f);
    }

    public static MobDef def() {
        return MobDef.of("looter", "Looter", "ルーター")
            .game(1, 1).size(0.6f, 1.55f).hp(46).speed(0.3f).style(Style.PILLAGER).egg(0x4a4438, 0xffd84a)
            .model(Looter::model)
            .spawn(5, 1, 1, MobDef.Habitat.ANY)
            .pattern(s -> {
                int n = s.count("n"); s.setCount("n", n + 1);
                String last = s.lastMove();
                if (n < 2) return "mug";
                if (last.equals("smoke_bomb")) return "escape";
                if (n == 3 && last.equals("lunge")) return "smoke_bomb";
                return s.rand().nextBoolean() ? "lunge" : "smoke_bomb";
            })
            .move(Move.of("mug", 26).intent(Intent.ATTACK, 10, 1).reach(2.2f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.5f)
                .at(12, (s, t) -> Fx.meleeNow(s, 10, 2.8, 80, v -> steal(s, v))))
            .move(Move.of("lunge", 30).intent(Intent.ATTACK, 12, 1).reach(3.0f)
                .sound(10, SoundEvents.PLAYER_ATTACK_STRONG, 0.9f, 1.2f)
                .at(14, Fx.dash(0.5, 0.1)).at(15, (s, t) -> Fx.meleeNow(s, 12, 3.2, 80, v -> steal(s, v))))
            .move(Move.of("smoke_bomb", 30).intent(Intent.DEFEND).noTarget()
                .at(14, (s, t) -> { s.addBlock(6); Fx.sound(s, SoundEvents.FIRECHARGE_USE, 1f, 1.2f); Fx.particles(s, ParticleTypes.LARGE_SMOKE, 40, 0.8); }))
            .move(Move.of("escape", 24).intent(Intent.ESCAPE).noTarget().moving()
                .at(22, (s, t) -> Fx.vanish(s)));
    }
}
