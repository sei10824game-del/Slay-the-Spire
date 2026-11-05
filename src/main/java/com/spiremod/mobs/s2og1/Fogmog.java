package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth: Fogmog 74 HP, a hulking fungal beast that exhales illusions.
 * Illusory Spores (summons an Eye With Teeth) -> Thwack (8, +1 Strength) -> then Headbutt (14) / Thwack alternating (a Thwack is always followed by Headbutt and vice versa).
 */
public final class Fogmog {
    private Fogmog() {}

    static final int HIDE = 0x6a5a7a, HIDE_L = 0x9a88aa, HIDE_D = 0x3a3048, FUNGUS = 0xa8b86a, FUNGUS_D = 0x6a7a3a, BELLY = 0xc8bcd0, MAW = 0x3a1a28, TOOTH = 0xf0ecd8, EYE = 0xffd04a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_fogmog");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 6, 8, 0);
            leg.box(-3, -8, -3.5f, 6, 8, 7).color(HIDE_D).grad(HIDE, HIDE_D).noise(0.12f).bevel(0.2f);
            leg.box(-3.5f, -9, -4.5f, 7, 2, 9).color(HIDE_D).noise(0.1f);
            for (int k = 0; k < 3; k++) leg.box(-2.5f + k * 2.5f, -9, 4.4f, 1, 1, 2).color(TOOTH).flat();
        }
        Part body = m.add("body").at(0, 8, 0);
        body.box(-8, 0, -6, 16, 6, 12).color(HIDE_D).grad(HIDE, HIDE_D).noise(0.12f);
        Part torso = body.add("torso").at(0, 6, 0);
        torso.box(-9, 0, -7, 18, 11, 14).color(HIDE).grad(HIDE_L, HIDE_D).noise(0.14f).bevel(0.3f);
        torso.box(-6, 1, 6.5f, 12, 7, 1).color(BELLY).grad(BELLY, HIDE_L).noise(0.1f);            // pale belly
        // fungal growths on the back (cap shelves + glowing nodes)
        for (int i = 0; i < 4; i++) {
            Part sh = torso.add("shelf" + i).at(-6 + i * 4, 11 - (i % 2), -5 + (i % 3) * 3).rot(10 - i * 5, 0, (i - 2) * 8);
            sh.box(-3, 0, -3, 6, 2, 6).color(FUNGUS).grad(FUNGUS, FUNGUS_D).noise(0.15f).bevel(0.2f);
            sh.box(-2, 2, -2, 4, 1, 4).color(FUNGUS).flat();
            sh.box(-0.5f, 3, -0.5f, 1, 1, 1).color(EYE).glow(Face.TOP, 0, 0, 1, 1, EYE);
        }
        Part head = torso.add("head").at(0, 8, 7);
        head.box(-6, -4, 0, 12, 9, 8).color(HIDE).grad(HIDE_L, HIDE_D).noise(0.12f).bevel(0.3f)
            .glow(Face.FRONT, 1, 1, 2, 2, EYE).glow(Face.FRONT, 9, 1, 2, 2, EYE).paint(Face.FRONT, 2, 2, 1, 1, 0x201000).paint(Face.FRONT, 9, 2, 1, 1, 0x201000);
        head.box(-6.5f, 4, -1, 13, 2, 9).color(FUNGUS).grad(FUNGUS, FUNGUS_D).noise(0.2f).inflate(0.2f);   // mushroom cap brow
        head.box(-3, -2, 8, 6, 3, 1).color(HIDE_D).flat();                                                      // nostrils
        Part jaw = head.add("jaw").at(0, -3, 1);
        jaw.box(-5.5f, -3, 0, 11, 3, 8).color(HIDE_D).grad(HIDE, HIDE_D).noise(0.1f)
            .paint(Face.TOP, 0, 0, 11, 8, MAW).paint(Face.TOP, 0, 6, 11, 2, TOOTH).paint(Face.TOP, 0, 0, 1, 8, TOOTH).paint(Face.TOP, 10, 0, 1, 8, TOOTH);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 10.5f, 10, 0);
            arm.box(-2.5f, -8, -2.5f, 5, 8, 5).color(HIDE).grad(HIDE_L, HIDE_D).noise(0.12f).bevel(0.2f);
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -8, 0);
            fore.box(-3, -8, -3, 6, 8, 6).color(HIDE).grad(HIDE, HIDE_D).noise(0.12f).bevel(0.2f);
            for (int k = 0; k < 3; k++) fore.box(-2 + k * 2, -11, -2, 1, 3, 1).color(TOOTH).flat();
        }
        m.look("head", 45, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 26f, 20f);
        Clip c = m.clip("idle", 56).loop();
        Kit.R(c, "torso", 0, 3, 0, 0, 28, -1, 0, 0, 56, 3, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 14, -3, 10, 0, 42, 2, -10, 0, 56, 0, 0, 0);
        Kit.R(c, "armR", 0, 5, 0, 8, 28, 10, 0, 12, 56, 5, 0, 8);
        Kit.R(c, "armL", 0, 5, 0, -8, 28, 10, 0, -12, 56, 5, 0, -8);
        Kit.S(c, "torso", 0, 1, 1, 1, 28, 1.03f, 1.05f, 1.03f, 56, 1, 1, 1);
        Kit.R(c, "jaw", 0, 0, 0, 0, 40, 0, 0, 0, 46, 8, 0, 0, 52, 0, 0, 0);
        // ILLUSORY SPORES: rears up and exhales a great cloud of spores from its maw
        c = m.clip("illusory_spores", 44);
        Kit.R(c, "torso", 0, 0, 0, 0, 12, -22, 0, 0, 34, -20, 0, 0, 44, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 12, -30, 0, 0, 20, -26, 4, 0, 26, -26, -4, 0, 34, -18, 0, 0, 44, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 12, 55, 0, 0, 34, 50, 0, 0, 44, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 12, -40, 0, 50, 34, -40, 0, 50, 44, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 12, -40, 0, -50, 34, -40, 0, -50, 44, 0, 0, 0);
        Kit.S(c, "torso", 0, 1, 1, 1, 12, 1.2f, 1.2f, 1.2f, 34, 1.2f, 1.2f, 1.2f, 1040, 1, 1, 1, 44, 1, 1, 1);
        for (int i = 0; i < 4; i++) Kit.S(c, "shelf" + i, 0, 1, 1, 1, 14, 1.4f, 1.4f, 1.4f, 34, 1.4f, 1.4f, 1.4f, 44, 1, 1, 1);
        // THWACK: sideways backhand with the heavy forearm
        c = m.clip("thwack", 28);
        Kit.R(c, "armR", 0, 0, 0, 0, 8, -50, 40, 60, -13, -75, -50, -10, 20, -60, -40, 0, 28, 0, 0, 0);
        Kit.R(c, "foreR", 0, 0, 0, 0, 8, -50, 0, 0, -13, -5, 0, 0, 28, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 8, 0, 28, 0, -13, 8, -32, 0, 28, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 8, -20, 0, -20, 28, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 8, 0, 0, -1.5f, -13, 0, 0, 3, 28, 0, 0, 0);
        // HEADBUTT: rears head back, then a heavy charging butt
        c = m.clip("headbutt", 32);
        Kit.R(c, "torso", 0, 0, 0, 0, 12, -18, 0, 0, -18, 30, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 12, -30, 0, 0, -18, 35, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 12, 20, 0, 0, -18, 0, 0, 0, 32, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 12, 0, 1, -3, -18, 0, -2, 7, 32, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 12, 30, 0, 20, -18, -30, 0, 10, 32, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 12, 30, 0, -20, -18, -30, 0, -10, 32, 0, 0, 0);
        Kit.R(c, "legR", 0, 0, 0, 0, 12, 20, 0, 0, -18, -30, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "legL", 0, 0, 0, 0, 12, -20, 0, 0, -18, 25, 0, 0, 32, 0, 0, 0);
        Kit.hurt(m, "torso", -12, 6);
        c = m.clip("death", 36);
        Kit.R(c, "torso", 0, 0, 0, 0, 12, -20, 0, 0, 36, 70, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 20, 0, -4, 0, 1036, 0, -7, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 12, -30, 0, 0, 36, 30, 10, 10);
        Kit.R(c, "jaw", 0, 0, 0, 0, 12, 50, 0, 0, 36, 20, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 14, -60, 0, 50, 36, 20, 0, 60);
        Kit.R(c, "armL", 0, 0, 0, 0, 14, -60, 0, -50, 36, 20, 0, -60);
        Kit.R(c, "legR", 0, 0, 0, 0, 24, -30, 0, 5, 36, -50, 0, 5);
        Kit.R(c, "legL", 0, 0, 0, 0, 24, -40, 0, -5, 36, -50, 0, -5);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_fogmog", "Fogmog", "フォグモグ")
            .game(2, 1).size(1.5f, 2.0f).hp(74).speed(0.2f).style(Style.BEAST).egg(0x6a5a7a, 0xa8b86a)
            .model(Fogmog::model)
            .spawn(4, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(s -> s.getTurn() == 0 ? "illusory_spores" : s.getTurn() == 1 ? "thwack" : s.lastMove().equals("thwack") ? "headbutt" : "thwack")
            .move(Move.of("illusory_spores", 44).intent(Intent.SUMMON).noTarget()
                .sound(12, SoundEvents.PUFFER_FISH_BLOW_OUT, 1.4f, 0.5f)
                .at(22, (s, t) -> {
                    Fx.particles(s, ParticleTypes.SPORE_BLOSSOM_AIR, 50, 1.2);
                    if (Fx.countAlive(s, "s2_eye_with_teeth", 24) < 1) Fx.summon(s, "s2_eye_with_teeth", 0, 2.2);
                }))
            .move(Move.of("thwack", 28).intent(Intent.ATTACK_BUFF, 8, 1).reach(3.2f)
                .sound(13, SoundEvents.PLAYER_ATTACK_STRONG, 1.0f, 0.6f).at(13, Fx.melee(8, 3.6, 130)).at(14, (s, t) -> Kit.str(s, 1)))
            .move(Move.of("headbutt", 32).intent(Intent.ATTACK, 14, 1).reach(3.0f)
                .sound(16, SoundEvents.RAVAGER_ATTACK, 1.2f, 0.6f).at(17, Fx.dash(0.35, 0.0)).at(18, Fx.melee(14, 3.4, 100)));
    }
}
