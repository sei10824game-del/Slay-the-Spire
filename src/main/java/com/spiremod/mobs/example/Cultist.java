package com.spiremod.mobs.example;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * REFERENCE CREATURE (StS1 Act 1 Cultist). Copy its structure:
 *   - public static ModelSpec model()  : pure look + animation (no Minecraft calls)
 *   - public static MobDef def()       : stats, moves (timed actions), pattern, passives
 * Original: 48-54 HP. Turn 1 Incantation (gains Ritual: +3 Strength at the end of each of its turns), then Dark Strike (6) forever.
 */
public final class Cultist {
    private Cultist() {}

    static final int ROBE = 0x5a2d4a, ROBE_D = 0x3d1d33, TRIM = 0xc8962e, SKIN = 0xb98a62, MASK = 0xe9e2c8, BEAK = 0xd9a232, EYE = 0xffb020;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("cultist");
        // ---- legs (pivot at hip, boxes hang down: y0 negative)
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 8, 0);
            leg.box(-1.5f, -8, -1.5f, 3, 8, 3).color(SKIN).noise(0.1f);
            leg.box(-2, -8, -2.5f, 4, 2, 5).color(0x3a2a22).bevel(0.15f); // wrapped foot
        }
        // ---- body
        Part body = m.add("body").at(0, 8, 0);
        body.box(-4, 0, -2.5f, 8, 3, 5).color(ROBE_D).grad(ROBE, ROBE_D); // hips
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-4, 0, -2.5f, 8, 8, 5).color(ROBE).grad(0x6a3558, ROBE).bevel(0.1f);
        torso.box(-4.2f, 0, -2.7f, 8, 2, 5).color(TRIM).flat().noise(0.05f).inflate(0.25f); // belt-ish gold sash
        torso.box(-1, 2, -2.9f, 2, 6, 1).color(TRIM).flat();                                 // chest stripe
        // robe skirt flares out in two tiers (hangs from hip)
        Part skirt = body.add("skirt").at(0, 2, 0);
        skirt.box(-5, -9, -3.2f, 10, 6, 6).color(ROBE_D).grad(ROBE, ROBE_D);
        skirt.box(-5.5f, -10, -3.5f, 11, 2, 7).color(TRIM).flat().noise(0.04f);
        // ---- head
        Part head = torso.add("head").at(0, 8, 0);
        head.box(-3, 0, -3, 6, 6, 6).color(SKIN);
        head.box(-3.6f, 3, -3.6f, 7, 4, 7).color(ROBE_D).grad(ROBE, ROBE_D);                  // hood
        head.box(-3.6f, -0.5f, 2.5f, 7, 7, 2).color(ROBE_D).bevel(0.1f);                       // hood back
        Part mask = head.add("mask").at(0, 3, 3);
        mask.box(-2.5f, -3, 0, 5, 6, 1).color(MASK).bevel(0.12f)
            .glow(Face.FRONT, 0, 1, 1, 1, EYE).glow(Face.FRONT, 4, 1, 1, 1, EYE)              // glowing eyes
            .paint(Face.FRONT, 0, 0, 5, 1, 0x7a6a50);
        Part beakU = mask.add("beakU").at(0, 0, 1);
        beakU.box(-1, -1, 0, 2, 2, 3).color(BEAK).bevel(0.1f);
        beakU.box(-0.5f, -2, 2, 1, 1, 1).color(0x8a6a1c);
        Part beakL = mask.add("beakL").at(0, -1, 1);
        beakL.box(-1, -1.5f, 0, 2, 1, 2).color(0xb8862a);
        Part feather = head.add("feather").at(2, 6, -1).rot(-25, 0, 20);
        feather.box(-0.5f, 0, -0.5f, 1, 5, 1).color(0x1d1a2b);
        feather.box(-0.5f, 5, -0.5f, 1, 2, 1).color(0xc2362f).flat();
        // ---- arms (pivot at shoulder)
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 5.5f, 7, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(ROBE).bevel(0.1f);                           // sleeve
            arm.box(-1.7f, -6.3f, -1.7f, 3, 1, 3).inflate(0.2f).color(TRIM).flat();             // cuff
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1, -3, -1, 2, 3, 2).color(SKIN);
            if (s < 0) { // right hand holds the ritual dagger
                Part dag = hand.add("dagger").at(0, -2, -1).rot(-90, 0, 0);
                dag.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
                dag.box(-1.5f, 2, -0.5f, 3, 1, 1).color(TRIM).flat();
                dag.box(-0.5f, 3, -0.4f, 1, 6, 1).color(0xc9cfd6).flat().noise(0.03f);
            }
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 38f, 25f);
        // ---- idle: breathing + slow sway, beak chatter
        m.clip("idle", 48).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 24, -1.5f, 0, 0).rot("torso", 48, 1.5f, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 0, 8, 3).rot("head", 36, 0, -8, -3).rot("head", 48, 0, 0, 0)
            .rot("armR", 0, 4, 0, 6).rot("armR", 24, 8, 0, 8).rot("armR", 48, 4, 0, 6)
            .rot("armL", 0, 4, 0, -6).rot("armL", 24, 8, 0, -8).rot("armL", 48, 4, 0, -6)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 30, 0, 0, 0).rot("beakL", 33, -22, 0, 0).rot("beakL", 36, 0, 0, 0).rot("beakL", 48, 0, 0, 0);
        // ---- INCANTATION: crouch, throw arms to the sky, head back, caw with beak wide, shudder, snap back
        m.clip("incantation", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 12, 0, 0).rot("torso", 16, -14, 0, 0, Ease.OUT_BACK).rot("torso", 30, -10, 0, 0).rot("torso", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -2.5f, 0).pos("body", 16, 0, 1.5f, 0, Ease.OUT_BACK).pos("body", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, 30, 0, 10).rot("armR", 16, -165, 0, 25, Ease.OUT_BACK).rot("armR", 30, -160, 0, 28).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, 30, 0, -10).rot("armL", 16, -165, 0, -25, Ease.OUT_BACK).rot("armL", 30, -160, 0, -28).rot("armL", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 18, 0, 0).rot("head", 16, -40, 0, 0, Ease.OUT_BACK).rot("head", 30, -35, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 15, 0, 0, 0).rot("beakL", 17, -35, 0, 0).rot("beakL", 21, -5, 0, 0).rot("beakL", 23, -35, 0, 0).rot("beakL", 27, -5, 0, 0).rot("beakL", 29, -35, 0, 0).rot("beakL", 34, 0, 0, 0)
            .rot("feather", 0, 0, 0, 0).rot("feather", 18, -20, 0, 25).rot("feather", 40, 0, 0, 0)
            .rot("skirt", 0, 0, 0, 0).rot("skirt", 16, 0, 0, 0).rot("skirt", 20, 0, 8, 0).rot("skirt", 24, 0, -8, 0).rot("skirt", 28, 0, 8, 0).rot("skirt", 40, 0, 0, 0);
        // ---- DARK STRIKE: coil back, snap-lunge with dagger thrust, recover
        m.clip("dark_strike", 26)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -10, -25, 0).rot("torso", 12, 22, 30, 0, Ease.IN_CUBIC).rot("torso", 26, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -3).pos("body", 12, 0, -1, 5, Ease.IN_CUBIC).pos("body", 26, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, 40, 20, 20).rot("armR", 12, -105, -15, -5, Ease.IN_CUBIC).rot("armR", 17, -100, -15, -5).rot("armR", 26, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -30, 0, -20).rot("armL", 12, 25, 0, -10).rot("armL", 26, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 12, 25, 0).rot("head", 12, 5, -25, 0).rot("head", 26, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 8, 25, 0, 0).rot("legR", 12, -50, 0, 0).rot("legR", 26, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 8, -15, 0, 0).rot("legL", 12, 30, 0, 0).rot("legL", 26, 0, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 12, -28, 0, 0).rot("beakL", 20, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 18, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 10).rot("head", 10, 0, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 3, -30, 0, 0).rot("beakL", 10, 0, 0, 0);
        m.clip("death", 30)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 25, 0, 0).rot("torso", 30, 8, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -4, 0, Ease.IN_QUAD).pos("body", 30, 0, -8, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -120, 0, 40).rot("armR", 30, 20, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -40).rot("armL", 30, 10, 0, -70)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 30, 30, 15, 10)
            .rot("legR", 0, 0, 0, 0).rot("legR", 18, -70, 0, 10).rot("legR", 30, -80, 0, 10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 18, -60, 0, -10).rot("legL", 30, -80, 0, -10);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("cultist", "Cultist", "カルティスト")
            .game(1, 1).size(0.6f, 1.75f).hp(50).speed(0.26f).style(Style.BIRD).egg(0x5a2d4a, 0xc8962e)
            .model(Cultist::model)
            .spawn(8, 1, 2, MobDef.Habitat.FOREST)
            .pattern(Pattern.seq(1, "incantation", "dark_strike"))
            .move(Move.of("incantation", 40).intent(Intent.BUFF).noTarget()
                .sound(15, SoundEvents.PARROT_IMITATE_WITCH, 1.4f, 0.6f)
                .at(17, (s, t) -> { Fx.sound(s, SoundEvents.PARROT_IMITATE_PHANTOM, 1.6f, 0.5f); Fx.particles(s, ParticleTypes.WITCH, 25, 0.5); s.flags.add("ritual"); }))
            .move(Move.of("dark_strike", 26).intent(Intent.ATTACK, 6, 1).reach(2.2f)
                .sound(8, SoundEvents.PHANTOM_FLAP, 0.8f, 1.4f)
                .at(12, Fx.melee(6, 2.8, 70)))
            .onMoveEnd(s -> { if (s.flags.contains("ritual") && !s.lastMove().equals("incantation")) s.addStrength(3); });
    }
}
