package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Gremlin Merc (47-49 HP): a Fat Gremlin carrying a Sneaky Gremlin, disguised as a Gremlin Leader.
 * Thievery 20 (steals gold with each hit). Cycle: Gimme (7x2) -> Double Smash (6x2 + Weak 2) -> Hehe (8, +2 Str).
 * On death (Surprise) it spawns a Fat Gremlin (inherits the stolen gold) and a Sneaky Gremlin.
 */
public final class GremlinMerc {
    private GremlinMerc() {}
    static final int SKIN = 0x8aa84a, SKIN_D = 0x5a7a30, CLOAK = 0x6a2a3a, CLOAK_D = 0x3e1822, GOLD = 0xe0b83a, IRON = 0x6a6e78, EYE = 0xffe040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_gremlin_merc");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3, 6, 0);
            leg.box(-1.5f, -6, -1.5f, 3, 6, 3).color(SKIN_D).noise(0.1f);
            leg.box(-2, -6, -2.5f, 4, 2, 5).color(0x3a2a22).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 6, 0);
        body.box(-6, 0, -4, 12, 10, 8).color(SKIN).grad(0xa8c860, SKIN_D).noise(0.12f).bevel(0.4f);          // fat gremlin belly
        body.box(-6.5f, 0, -4.5f, 13, 6, 9).color(CLOAK).grad(0x8a3a4a, CLOAK_D).inflate(0.3f).bevel(0.3f);     // long coat
        body.box(-6.8f, 5, -4.8f, 13, 1, 9).color(GOLD).flat().inflate(0.2f);                                   // gold sash
        body.box(-2, -1, 4.6f, 4, 5, 1).color(GOLD).noise(0.05f).flat();                                          // coin pouch
        body.box(-1, 3, 5.2f, 2, 2, 1).color(0xfff0a0).flat();
        Part torso = body.add("torso").at(0, 10, 0);
        torso.box(-5, 0, -3.5f, 10, 7, 7).color(CLOAK).grad(0x8a3a4a, CLOAK_D).bevel(0.3f);
        torso.box(-5.4f, 5, -3.9f, 11, 2, 8).color(GOLD).flat().noise(0.05f);                                   // epaulettes
        // the Sneaky Gremlin riding on the shoulders, wearing the leader's mask
        Part sneaky = torso.add("sneaky").at(0, 7, -1);
        sneaky.box(-3, 0, -2.5f, 6, 5, 5).color(CLOAK_D).bevel(0.3f);
        Part head = sneaky.add("head").at(0, 5, 0);
        head.box(-3.5f, 0, -3, 7, 6, 6).color(SKIN).grad(0xa8c860, SKIN_D).bevel(0.3f);
        head.box(-4, 4, -3.5f, 8, 3, 7).color(IRON).grad(0x9aa0aa, IRON).bevel(0.3f);                           // leader helm
        for (int s = -1; s <= 1; s += 2) {
            Part horn = head.add(s < 0 ? "hornR" : "hornL").at(s * 3.5f, 6, 0).rot(0, 0, s * -30);
            horn.box(-1, 0, -1, 2, 5, 2).color(0xe8e0c0).bevel(0.2f);
            head.box(s * 2 - 0.5f, 2, 3, 1, 2, 1).color(0xf0e8c0).glow(Face.FRONT, 0, 0, 1, 1, EYE);                 // beady eyes
            Part ear = head.add(s < 0 ? "earR" : "earL").at(s * 3.5f, 3, 0).rot(0, s * 20, s * -40);
            ear.box(-0.5f, -1, -1, 5, 3, 1).color(SKIN).bevel(0.15f);
        }
        head.box(-3, 0, 2.8f, 6, 2, 1).color(0xe8e0c0).bevel(0.15f);                                            // big grin mask
        head.box(-2.5f, 0.3f, 3.2f, 5, 1, 1).color(0xf0e8c0).flat();
        for (int i = 0; i < 4; i++) head.box(-2.2f + i * 1.3f, 0, 3.4f, 1, 1, 1).color(0x1a1a1a).flat();
        Part nose = head.add("nose").at(0, 3, 3);
        nose.box(-1, -1, 0, 2, 3, 3).color(SKIN_D).bevel(0.3f);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 6.5f, 6, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(CLOAK).bevel(0.15f);
            arm.box(-1.7f, -6.3f, -1.7f, 3, 1, 3).inflate(0.2f).color(GOLD).flat();
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1.5f, -3, -1.5f, 3, 3, 3).color(SKIN);
            for (int f = 0; f < 3; f++) hand.box(-1.5f + f, -5, 0.5f, 1, 2, 1).color(SKIN_D);                     // grabby fingers
            if (s > 0) { // little blade / kunai
                Part k = hand.add("kunai").at(0, -2, 1).rot(-90, 0, 0);
                k.box(-0.5f, 0, -0.5f, 1, 5, 1).color(0xc9cfd6).flat(); k.box(-1.2f, -1, -0.5f, 3, 1, 1).color(GOLD).flat();
            }
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 38f, 25f);
        m.clip("idle", 40).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 20, -1.5f, 0, 2).rot("torso", 40, 1.5f, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 0, 10, 4).rot("head", 30, 0, -10, -4).rot("head", 40, 0, 0, 0)
            .rot("armR", 0, 6, 0, 5).rot("armR", 20, 14, 0, 9).rot("armR", 40, 6, 0, 5)
            .rot("armL", 0, 6, 0, -5).rot("armL", 20, 14, 0, -9).rot("armL", 40, 6, 0, -5)
            .scl("body", 0, 1, 1, 1).scl("body", 20, 1.03f, 1.03f, 1.03f).scl("body", 40, 1, 1, 1);
        // GIMME: both grabbing hands reach forward and snatch twice
        m.clip("gimme", 32)
            .rot("torso", 0, 0, 0, 0).rot("torso", 6, -8, 0, 0).rot("torso", 10, 18, 0, 0, Ease.IN_CUBIC).rot("torso", 16, -8, 0, 0).rot("torso", 20, 18, 0, 0, Ease.IN_CUBIC).rot("torso", 32, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, 30, 0, 25).rot("armR", 10, -105, -10, -5, Ease.IN_CUBIC).rot("armR", 15, -30, 0, 10).rot("armR", 32, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, 30, 0, -25).rot("armL", 20, -105, 10, 5, Ease.IN_CUBIC).rot("armL", 26, -30, 0, -10).rot("armL", 32, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, 3).pos("body", 20, 0, 0, 3).pos("body", 32, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 12, 0, 0).rot("head", 20, 12, 0, 0).rot("head", 32, 0, 0, 0);
        // DOUBLE SMASH: raised fists slam down twice
        m.clip("double_smash", 36)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -160, 0, 15).rot("armR", 12, -20, 0, 0, Ease.IN_CUBIC).rot("armR", 22, -160, 0, 15).rot("armR", 26, -20, 0, 0, Ease.IN_CUBIC).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -160, 0, -15).rot("armL", 12, -20, 0, 0, Ease.IN_CUBIC).rot("armL", 22, -160, 0, -15).rot("armL", 26, -20, 0, 0, Ease.IN_CUBIC).rot("armL", 36, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -22, 0, 0).rot("torso", 12, 24, 0, 0, Ease.IN_CUBIC).rot("torso", 22, -22, 0, 0).rot("torso", 26, 24, 0, 0, Ease.IN_CUBIC).rot("torso", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1.5f, -1).pos("body", 12, 0, -1, 2, Ease.IN_CUBIC).pos("body", 22, 0, 1.5f, -1).pos("body", 26, 0, -1, 2, Ease.IN_CUBIC).pos("body", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -18, 0, 0).rot("head", 12, 20, 0, 0).rot("head", 22, -18, 0, 0).rot("head", 26, 20, 0, 0).rot("head", 36, 0, 0, 0);
        // HEHE: stab-poke then shake with giggles
        m.clip("hehe", 34)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -10, -20, 0).rot("torso", 12, 20, 25, 0, Ease.IN_CUBIC).rot("torso", 18, 0, 0, 4).rot("torso", 20, 0, 0, -4).rot("torso", 22, 0, 0, 4).rot("torso", 24, 0, 0, -4).rot("torso", 34, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, 35, 0, -20).rot("armL", 12, -110, 10, 0, Ease.IN_CUBIC).rot("armL", 20, -80, 0, 0).rot("armL", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, 0, 0, 25).rot("armR", 20, -60, 0, 25).rot("armR", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -12, 0, 0).rot("head", 18, -12, 8, 0).rot("head", 22, -12, -8, 0).rot("head", 26, -12, 8, 0).rot("head", 34, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 18, 1.08f, 1.08f, 1.08f).scl("body", 34, 1, 1, 1);
        U.hurt(m, "torso");
        m.clip("death", 32)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -15, 0, 8).rot("body", 24, -50, 0, 0, Ease.OUT_BOUNCE).rot("body", 32, -50, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 24, 0, -2, 0).rot("torso", 0, 0, 0, 0).rot("torso", 20, 20, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -150, 0, 40).rot("armL", 0, 0, 0, 0).rot("armL", 14, -150, 0, -40)
            .rot("head", 0, 0, 0, 0).rot("head", 18, 40, 0, 15).rot("sneaky", 0, 0, 0, 0).rot("sneaky", 20, -30, 0, 40);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_gremlin_merc", "Gremlin Merc", "グレムリン傭兵")
            .game(2, 1).size(0.9f, 2.0f).hp(48).speed(0.26f).style(Style.ZOMBIE).egg(0x6a2a3a, 0xe0b83a)
            .model(GremlinMerc::model)
            .spawn(5, 1, 1, MobDef.Habitat.CAVE)
            .pattern(Pattern.cycle("gimme", "double_smash", "hehe"))
            .onDeath((s, src) -> {
                SpireMob fat = Fx.summon(s, "s2_fat_gremlin", 1.0, 0.5);
                if (fat != null) { fat.stolenGold = s.stolenGold; s.stolenGold = 0; }
                Fx.summon(s, "s2_sneaky_gremlin", -1.0, 0.5);
            })
            .move(Move.of("gimme", 32).intent(Intent.ATTACK, 7, 2).reach(2.4f)
                .sound(6, SoundEvents.VILLAGER_CELEBRATE, 1f, 1.6f)
                .at(10, U.hit(7, 2.8, 80, v -> {})).at(20, U.hit(7, 2.8, 80, v -> {}))
                .at(11, (s, t) -> { if (t != null) Fx.steal(s, t, 3); }).at(21, (s, t) -> { if (t != null) Fx.steal(s, t, 3); }))
            .move(Move.of("double_smash", 36).intent(Intent.ATTACK_DEBUFF, 6, 2).reach(2.4f)
                .sound(8, SoundEvents.VILLAGER_NO, 1f, 1.5f)
                .at(12, U.hit(6, 2.8, 90, v -> Fx.weak(v, 5))).at(26, U.hit(6, 2.8, 90, v -> Fx.weak(v, 5)))
                .at(13, (s, t) -> { if (t != null) Fx.steal(s, t, 3); }).at(27, (s, t) -> { if (t != null) Fx.steal(s, t, 3); }))
            .move(Move.of("hehe", 34).intent(Intent.ATTACK_BUFF, 8, 1).reach(2.4f)
                .sound(17, SoundEvents.VILLAGER_CELEBRATE, 1f, 1.9f)
                .at(12, (s, t) -> { Fx.meleeNow(s, 8, 2.8, 80, null); if (t != null) Fx.steal(s, t, 3); s.addStrength(2); Fx.particles(s, ParticleTypes.HAPPY_VILLAGER, 8, 0.5); }));
    }
}
