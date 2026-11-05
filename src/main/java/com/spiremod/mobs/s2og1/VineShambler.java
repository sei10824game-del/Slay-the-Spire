package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth: Vine Shambler 61 HP. Fixed cycle: Swipe (6 x2) -> Grasping Vines (8 + 1 Tangled) -> Chomp (16). */
public final class VineShambler {
    private VineShambler() {}

    static final int VINE = 0x3a6a2a, VINE_L = 0x6aa040, VINE_D = 0x1e3a18, WOOD = 0x5a4228, BLOOM = 0xc2386a, BLOOM_D = 0x7a1a3a, TOOTH = 0xf0ecd0, MAW = 0x3a1020, EYE = 0xffe040;

    static void braid(Part p, float x, float y, float z, int w, int h, int d) {
        p.box(x, y, z, w, h, d).color(VINE).grad(VINE_L, VINE_D).noise(0.18f).bevel(0.1f);
        for (int i = 1; i < h; i += 3) p.box(x - 0.3f, y + i, z - 0.3f, w, 1, d).color(VINE_D).flat().inflate(0.2f);   // binding rings
    }

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_vine_shambler");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 4, 11, 0);
            braid(leg, -2.5f, -11, -2.5f, 5, 11, 5);
            leg.box(-3.5f, -12, -3.5f, 7, 2, 8).color(WOOD).noise(0.2f).bevel(0.2f);                 // root feet
            leg.box(-1, -11, 4.5f, 2, 1, 2).color(VINE_L).flat();
        }
        Part body = m.add("body").at(0, 11, 0);
        body.box(-5, 0, -3.5f, 10, 4, 7).color(VINE_D).grad(VINE, VINE_D).noise(0.2f);
        Part torso = body.add("torso").at(0, 4, 0);
        braid(torso, -6, 0, -4, 12, 11, 8);
        torso.box(-3, 2, -4.6f, 6, 6, 1).color(WOOD).noise(0.3f).paint(Face.FRONT, 1, 1, 4, 1, VINE_D);   // gnarled chest
        torso.box(-1, 5, -5, 2, 2, 1).color(BLOOM).glow(Face.FRONT, 0, 0, 2, 2, BLOOM);
        for (int i = 0; i < 4; i++) torso.box(-6.2f + i * 3.8f, 11, -2, 2, 2, 4).color(VINE_L).noise(0.2f);  // shoulder ridges
        Part head = torso.add("head").at(0, 11, 1);
        head.box(-4, 0, -4, 8, 6, 8).color(VINE).grad(VINE_L, VINE_D).noise(0.2f).bevel(0.15f);
        head.box(-3, 1.5f, -4.4f, 2, 2, 1).color(EYE).glow(Face.FRONT, 0, 0, 2, 2, EYE);
        head.box(1, 1.5f, -4.4f, 2, 2, 1).color(EYE).glow(Face.FRONT, 0, 0, 2, 2, EYE);
        Part jaw = head.add("jaw").at(0, 1, -3);    // venus-flytrap maw (front = -z in head frame is back; maw built on +z)
        jaw.box(-3.5f, -3, 3, 7, 3, 5).color(BLOOM_D).grad(BLOOM, BLOOM_D).bevel(0.2f)
            .paint(Face.TOP, 0, 0, 7, 5, MAW).paint(Face.TOP, 0, 0, 1, 5, TOOTH).paint(Face.TOP, 6, 0, 1, 5, TOOTH).paint(Face.TOP, 0, 4, 7, 1, TOOTH);
        Part up = head.add("jawU").at(0, 4, 3);
        up.box(-3.5f, -3, 0, 7, 3, 6).color(BLOOM).grad(BLOOM, BLOOM_D).bevel(0.2f).paint(Face.BOTTOM, 0, 0, 7, 6, MAW).paint(Face.BOTTOM, 0, 5, 7, 1, TOOTH).paint(Face.BOTTOM, 0, 0, 1, 6, TOOTH).paint(Face.BOTTOM, 6, 0, 1, 6, TOOTH);
        for (int i = 0; i < 3; i++) {                 // flowers / leaves on top
            Part f = head.add("bloom" + i).at(-2.5f + i * 2.5f, 6, 0).rot(10, 0, (i - 1) * 25);
            f.box(-1, 0, -1, 2, 3 + i % 2 * 2, 2).color(VINE_L).flat();
            f.box(-1.5f, 3 + i % 2 * 2, -1.5f, 3, 2, 3).color(i == 1 ? BLOOM : 0xe8c24a).glow(Face.TOP, 0, 0, 3, 3, i == 1 ? BLOOM : 0xe8c24a);
        }
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 8, 10, 0);
            braid(arm, -2, -9, -2, 4, 9, 4);
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -9, 0);
            braid(fore, -2.5f, -8, -2.5f, 5, 8, 5);
            for (int k = -1; k <= 1; k++) {       // claw vines
                Part cl = fore.add((s < 0 ? "clR" : "clL") + k).at(k * 1.6f, -8, 0).rot(0, 0, k * 12);
                cl.box(-0.5f, -5, -0.5f, 1, 5, 1).color(VINE_L).flat();
                cl.box(-0.5f, -7, -0.5f, 1, 2, 1).color(THORNC).flat();
            }
            for (int k = 0; k < 2; k++) fore.box(s * 2.5f - 0.5f, -3 - k * 3, -0.5f, 1, 1, 3).color(THORNC).flat();
        }
        m.look("head", 40, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 30f, 20f);
        Clip c = m.clip("idle", 52).loop();
        Kit.R(c, "torso", 0, 3, 0, 0, 26, -2, 0, 0, 52, 3, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 14, 4, 10, 0, 38, 0, -10, 0, 52, 0, 0, 0);
        Kit.R(c, "armR", 0, 6, 0, 8, 26, 12, 0, 12, 52, 6, 0, 8);
        Kit.R(c, "armL", 0, 6, 0, -8, 26, 12, 0, -12, 52, 6, 0, -8);
        Kit.R(c, "foreR", 0, -8, 0, 0, 26, -16, 0, 0, 52, -8, 0, 0);
        Kit.R(c, "foreL", 0, -8, 0, 0, 26, -16, 0, 0, 52, -8, 0, 0);
        Kit.R(c, "jawU", 0, 0, 0, 0, 40, 0, 0, 0, 44, -8, 0, 0, 48, 0, 0, 0);
        // SWIPE (x2): alternating claw raking swipes
        c = m.clip("swipe", 34);
        Kit.R(c, "armR", 0, 0, 0, 0, 7, -120, 0, 40, -10, -70, -40, -30, 14, -30, 0, 10, 17, -120, 0, 40, 22, -90, 0, 40, 34, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 14, -10, 0, -10, 17, -120, 0, -40, -20, -70, 40, 30, 28, -20, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "foreR", 0, 0, 0, 0, 7, -50, 0, 0, -10, -10, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "foreL", 0, 0, 0, 0, 17, -50, 0, 0, -20, -10, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 7, 0, 25, 0, -10, 8, -28, 0, 14, 0, 0, 0, 17, 0, -25, 0, -20, 8, 28, 0, 34, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 7, 0, 0, -1.5f, -10, 0, 0, 2, 17, 0, 0, -1.5f, -20, 0, 0, 2, 34, 0, 0, 0);
        // GRASPING VINES: both arms plunge down and vines burst up from the ground to ensnare
        c = m.clip("grasping_vines", 38);
        Kit.R(c, "armR", 0, 0, 0, 0, 12, -150, 0, 25, -18, -70, 0, 10, 38, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 12, -150, 0, -25, -18, -70, 0, -10, 38, 0, 0, 0);
        Kit.R(c, "foreR", 0, 0, 0, 0, 12, -30, 0, 0, -18, 10, 0, 0, 38, 0, 0, 0);
        Kit.R(c, "foreL", 0, 0, 0, 0, 12, -30, 0, 0, -18, 10, 0, 0, 38, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 12, -14, 0, 0, -18, 28, 0, 0, 38, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 12, -10, 0, 0, -18, 15, 0, 0, 38, 0, 0, 0);
        Kit.S(c, "armR", 0, 1, 1, 1, 18, 1, 1.5f, 1, 28, 1, 1.5f, 1, 38, 1, 1, 1);
        Kit.S(c, "armL", 0, 1, 1, 1, 18, 1, 1.5f, 1, 28, 1, 1.5f, 1, 38, 1, 1, 1);
        // CHOMP: head rises, maw gapes enormous, then slams down and snaps shut
        c = m.clip("chomp", 36);
        Kit.R(c, "torso", 0, 0, 0, 0, 12, -22, 0, 0, -18, 38, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 12, -30, 0, 0, -18, 35, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 12, 50, 0, 0, -18, 0, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "jawU", 0, 0, 0, 0, 12, -55, 0, 0, -18, 0, 0, 0, 36, 0, 0, 0);
        Kit.S(c, "head", 0, 1, 1, 1, 12, 1.2f, 1.2f, 1.2f, -18, 1.3f, 1.3f, 1.3f, 24, 1, 1, 1);
        Kit.P(c, "body", 0, 0, 0, 0, 12, 0, 2, -3, -18, 0, -3, 6, 36, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 12, -60, 0, 30, -18, 30, 0, 10, 36, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 12, -60, 0, -30, -18, 30, 0, -10, 36, 0, 0, 0);
        Kit.hurt(m, "torso", -14, 8);
        c = m.clip("death", 34);
        Kit.R(c, "torso", 0, 0, 0, 0, 12, 20, 0, 0, 34, 30, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 20, 0, -6, 0, 1034, 0, -10, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 14, -20, 0, 0, 34, 40, 0, 20);
        Kit.R(c, "armR", 0, 0, 0, 0, 14, -70, 0, 60, 34, 10, 0, 85);
        Kit.R(c, "armL", 0, 0, 0, 0, 14, -70, 0, -60, 34, 10, 0, -85);
        Kit.R(c, "legR", 0, 0, 0, 0, 20, -35, 0, 5, 34, -60, 0, 10);
        Kit.R(c, "legL", 0, 0, 0, 0, 20, -20, 0, -5, 34, -60, 0, -10);
        Kit.S(c, "torso", 0, 1, 1, 1, 34, 1.2f, 0.75f, 1.2f);
        return m;
    }
    static final int THORNC = 0x8a6a30;

    public static MobDef def() {
        return MobDef.of("s2_vine_shambler", "Vine Shambler", "ヴァインシャンブラー")
            .game(2, 1).size(1.0f, 2.5f).hp(61).speed(0.2f).style(Style.ZOMBIE).egg(0x3a6a2a, 0xc2386a)
            .model(VineShambler::model)
            .spawn(5, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("swipe", "grasping_vines", "chomp"))
            .move(Move.of("swipe", 34).intent(Intent.ATTACK, 6, 2).reach(3.0f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 0.8f).sound(18, SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 0.8f)
                .at(10, Kit.hit(6, 3.4, 120)).at(19, Kit.hit(6, 3.4, 120)))
            .move(Move.of("grasping_vines", 38).intent(Intent.ATTACK_DEBUFF, 8, 1).reach(3.6f)
                .sound(18, SoundEvents.AZALEA_LEAVES_BREAK, 1.2f, 0.6f)
                .at(19, (s, t) -> { Fx.particles(s, ParticleTypes.COMPOSTER, 20, 0.7); Kit.hit(8, 3.8, 120).run(s, t); if (t != null) Kit.tangled(t, 1); }))
            .move(Move.of("chomp", 36).intent(Intent.ATTACK, 16, 1).reach(3.2f)
                .sound(18, SoundEvents.RAVAGER_ATTACK, 1.2f, 0.7f).at(19, Kit.hit(16, 3.4, 100)));
    }
}
