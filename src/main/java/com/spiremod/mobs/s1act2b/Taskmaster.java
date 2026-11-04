package com.spiremod.mobs.s1act2b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 2 elite (with the Slavers): Taskmaster. 54-60 HP. Scouring Whip every turn: 7 damage + Wound. */
public final class Taskmaster {
    private Taskmaster() {}
    static final int SKIN = 0xa87a5c, SKIN_D = 0x7a5840, COAT = 0x3a2a30, COAT_L = 0x5a3a44, RED = 0x9a2a2a, LEATHER = 0x4a3220, BELT = 0xc8962e;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("taskmaster");
        m.scale(1.15f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 9, 0);
            leg.box(-2, -9, -2, 4, 9, 4).color(COAT).grad(COAT_L, COAT).bevel(0.1f);
            leg.box(-2.3f, -9, -2.8f, 4, 3, 6).color(LEATHER).bevel(0.2f);
        }
        Part body = m.add("body").at(0, 9, 0);
        body.box(-4.5f, 0, -3, 9, 3, 6).color(COAT).grad(COAT_L, COAT);
        body.box(-4.7f, 1, -3.2f, 9, 1, 6).inflate(0.2f).color(BELT).flat();
        Part torso = body.add("torso").at(0, 3, 0).rot(6, 0, 0);
        torso.box(-5.5f, 0, -3.5f, 11, 9, 7).color(COAT).grad(COAT_L, COAT).bevel(0.12f);
        torso.box(-6, 8, -4, 12, 3, 8).color(COAT_L).bevel(0.2f);                        // heavy shoulders
        torso.box(-1, 0, -3.7f, 2, 9, 1).color(RED).flat();                              // red sash
        torso.box(-5.8f, 0, -3.7f, 1, 8, 1).color(BELT).flat();
        torso.box(4.8f, 0, -3.7f, 1, 8, 1).color(BELT).flat();
        Part head = torso.add("head").at(0, 11, 0);
        head.box(-3, 0, -3, 6, 7, 6).color(SKIN).grad(SKIN, SKIN_D).noise(0.08f)
            .paint(Face.FRONT, 1, 3, 2, 1, 0x201010).glow(Face.FRONT, 4, 3, 1, 1, 0xff5030)
            .paint(Face.FRONT, 1, 5, 4, 1, 0x4a2a20).paint(Face.FRONT, 0, 0, 1, 2, SKIN_D);
        head.box(-3.4f, 5, -3.4f, 7, 2, 7).color(0x201418).inflate(0.1f);                // black cap
        head.box(-0.5f, 3, 3.0f, 1, 1, 1).color(0x6a2020).flat();                        // scar bridge
        head.box(-1, 2, 3.0f, 4, 1, 1).color(0x1a1010).flat();                           // eyepatch strap
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 7, 9, 0);
            arm.box(-2, -6, -2, 4, 6, 4).color(COAT).grad(COAT_L, COAT).bevel(0.1f);
            arm.box(-2.3f, -2, -2.3f, 5, 2, 5).color(BELT).flat();
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -6, 0);
            fore.box(-1.7f, -5, -1.7f, 3, 5, 3).color(SKIN).noise(0.08f);
            fore.box(-1.9f, -2, -1.9f, 4, 2, 4).color(LEATHER).flat();                      // bracer
            Part hand = fore.add(s < 0 ? "handR" : "handL").at(0, -5, 0);
            hand.box(-1.3f, -2, -1.3f, 3, 2, 3).color(SKIN_D);
            if (s < 0) { // whip in the right hand
                Part whip = hand.add("whip").at(0, -1, 0).rot(160, 0, 0);
                whip.box(-0.5f, -1, -0.5f, 1, 4, 1).color(LEATHER).flat();
                Part w1 = whip.add("whip1").at(0, 3, 0);
                w1.box(-0.3f, 0, -0.3f, 1, 6, 1).color(0x2a1a10).flat();
                Part w2 = w1.add("whip2").at(0, 6, 0);
                w2.box(-0.3f, 0, -0.3f, 1, 6, 1).color(0x2a1a10).flat();
                Part w3 = w2.add("whip3").at(0, 6, 0);
                w3.box(-0.3f, 0, -0.3f, 1, 5, 1).color(0x2a1a10).flat();
                w3.box(-0.8f, 5, -0.8f, 2, 2, 2).color(0x909098).flat();                    // barbed tip
            }
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 34f, 20f);
        m.clip("idle", 50).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 25, -1.5f, 0, 0).rot("torso", 50, 1.5f, 0, 0)
            .rot("armR", 0, 20, 0, 6).rot("armR", 25, 24, 0, 8).rot("armR", 50, 20, 0, 6)
            .rot("whip1", 0, 0, 0, 0).rot("whip1", 25, 10, 0, 6).rot("whip1", 50, 0, 0, 0)
            .rot("whip2", 0, 0, 0, 0).rot("whip2", 25, 16, 0, -8).rot("whip2", 50, 0, 0, 0)
            .rot("whip3", 0, 0, 0, 0).rot("whip3", 25, 22, 0, 10).rot("whip3", 50, 0, 0, 0)
            .rot("armL", 0, 4, 0, -6).rot("armL", 25, 8, 0, -8).rot("armL", 50, 4, 0, -6);
        // SCOURING WHIP: whip arm cocked back over shoulder, lean, crack forward with whiplash chain, snap back
        m.clip("scouring_whip", 32)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -14, -30, 0, Ease.OUT_CUBIC).rot("torso", 14, 22, 32, 0, Ease.IN_CUBIC).rot("torso", 22, 18, 25, 0).rot("torso", 32, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -2).pos("body", 14, 0, -1, 4, Ease.IN_CUBIC).pos("body", 32, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -170, 0, 20, Ease.OUT_CUBIC).rot("armR", 14, -80, 0, -10, Ease.IN_CUBIC).rot("armR", 22, -75, 0, -10).rot("armR", 32, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 10, -80, 0, 0).rot("foreR", 14, 10, 0, 0, Ease.IN_CUBIC).rot("foreR", 32, 0, 0, 0)
            .rot("whip", 0, 0, 0, 0).rot("whip", 10, 50, 0, 0).rot("whip", 14, -20, 0, 0, Ease.IN_CUBIC).rot("whip", 32, 0, 0, 0)
            .rot("whip1", 0, 0, 0, 0).rot("whip1", 10, 70, 0, 0).rot("whip1", 15, -20, 0, 0, Ease.OUT_BACK).rot("whip1", 32, 0, 0, 0)
            .rot("whip2", 0, 0, 0, 0).rot("whip2", 10, 80, 0, 0).rot("whip2", 16, -25, 0, 0, Ease.OUT_BACK).rot("whip2", 32, 0, 0, 0)
            .rot("whip3", 0, 0, 0, 0).rot("whip3", 10, 70, 0, 0).rot("whip3", 17, -30, 0, 0, Ease.OUT_BACK).rot("whip3", 32, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -40, 0, -30).rot("armL", 14, 30, 0, 15).rot("armL", 32, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 10, 20, 0).rot("head", 14, 5, -25, 0).rot("head", 32, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 14, -35, 0, 0).rot("legR", 32, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 14, 25, 0, 0).rot("legL", 32, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 18, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 10).rot("head", 10, 0, 0, 0);
        m.clip("death", 34)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -4, 0, Ease.IN_QUAD).pos("body", 34, 0, -9, 4, Ease.OUT_BOUNCE)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -10, 0, 0).rot("torso", 34, 72, 0, 0, Ease.IN_QUAD)
            .rot("legR", 0, 0, 0, 0).rot("legR", 14, -70, 0, 0).rot("legR", 34, -80, 0, 5)
            .rot("legL", 0, 0, 0, 0).rot("legL", 14, -60, 0, 0).rot("legL", 34, -80, 0, -5)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -100, 0, 30).rot("armR", 34, -140, 0, 40)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -80, 0, -30).rot("armL", 34, -140, 0, -40)
            .rot("head", 0, 0, 0, 0).rot("head", 34, 20, 20, 10);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("taskmaster", "Taskmaster", "タスクマスター")
            .game(1, 2).size(0.7f, 2.0f).hp(57).speed(0.25f).style(Style.ZOMBIE).egg(0x3a2a30, 0x9a2a2a).elite()
            .model(Taskmaster::model)
            .spawn(1, 1, 1, MobDef.Habitat.ANY)
            .pattern(Pattern.cycle("scouring_whip"))
            .move(Move.of("scouring_whip", 32).intent(Intent.ATTACK_DEBUFF, 7, 1).reach(3.6f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.7f)
                .at(15, (s, t) -> { Fx.sound(s, SoundEvents.LEASH_KNOT_BREAK, 1.4f, 0.6f); Fx.sound(s, SoundEvents.PLAYER_ATTACK_CRIT, 0.8f, 1.2f);
                                    Fx.particlesAt(s, ParticleTypes.CRIT, Fx.rel(s, 3, 0, 1), 10, 0.5, 0.1);
                                    Fx.meleeNow(s, 7, 4.2, 70, v -> Fx.curse(v, Fx.Curse.WOUND)); }));
    }
}
