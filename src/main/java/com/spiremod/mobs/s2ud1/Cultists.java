package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** Shared builder for the Underdocks Calcified Cultist and Damp Cultist (Incantation -> Dark Strike forever). */
final class Cultists {
    private Cultists() {}

    static ModelSpec build(boolean damp) {
        int robe = damp ? 0x3d6a6e : 0x6d6a78, robeD = damp ? 0x23403f : 0x44424f, trim = damp ? 0x7fae8a : 0xd8d2c0, skin = damp ? 0x7ea88c : 0xc8c0b0;
        int shell = 0xe8e2d0, shellD = 0xa9a28c;
        ModelSpec m = new ModelSpec(damp ? "s2_damp_cultist" : "s2_calcified_cultist");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 8, 0);
            leg.box(-1.5f, -8, -1.5f, 3, 8, 3).color(skin).noise(0.1f);
            leg.box(-2, -8, -2.5f, 4, 2, 5).color(robeD).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 8, 0);
        body.box(-4, 0, -2.5f, 8, 3, 5).color(robeD).grad(robe, robeD);
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-4, 0, -2.5f, 8, 8, 5).color(robe).grad(damp ? 0x4c7f82 : 0x7d7a8a, robe).bevel(0.1f);
        torso.box(-4.2f, 0, -2.7f, 8, 2, 5).color(trim).flat().noise(0.05f).inflate(0.25f);
        torso.box(-1, 2, -2.9f, 2, 6, 1).color(trim).flat();
        Part skirt = body.add("skirt").at(0, 2, 0);
        skirt.box(-5, -9, -3.2f, 10, 6, 6).color(robeD).grad(robe, robeD);
        skirt.box(-5.5f, -10, -3.5f, 11, 2, 7).color(trim).flat().noise(0.05f);
        Part head = torso.add("head").at(0, 8, 0);
        head.box(-3, 0, -3, 6, 6, 6).color(skin);
        head.box(-3.6f, 3, -3.6f, 7, 4, 7).color(robeD).grad(robe, robeD);
        head.box(-3.6f, -0.5f, 2.5f, 7, 7, 2).color(robeD).bevel(0.1f);
        Part mask = head.add("mask").at(0, 3, 3);
        mask.box(-2.5f, -3, 0, 5, 6, 1).color(0xe9e2c8).bevel(0.12f)
            .glow(Face.FRONT, 0, 1, 1, 1, 0xffb020).glow(Face.FRONT, 4, 1, 1, 1, 0xffb020).paint(Face.FRONT, 0, 0, 5, 1, 0x7a6a50);
        Part beakU = mask.add("beakU").at(0, 0, 1);
        beakU.box(-1, -1, 0, 2, 2, 3).color(0xd9a232).bevel(0.1f);
        Part beakL = mask.add("beakL").at(0, -1, 1);
        beakL.box(-1, -1.5f, 0, 2, 1, 2).color(0xb8862a);
        if (damp) { // pale wet fish-belly hood fin and drips
            Part fin = head.add("fin").at(0, 7, -1);
            fin.box(-0.5f, 0, -2, 1, 4, 4).color(0x5fa59a).grad(0x8fd0bf, 0x4a8a80).bevel(0.1f);
            for (int i = 0; i < 3; i++) body.add("drip" + i).at(-3 + i * 3, 0, 3.2f).box(-0.5f, -2, 0, 1, 3, 1).color(0x8fd0e0).flat();
        } else { // barnacle / coral growths
            Part feather = head.add("feather").at(2, 6, -1).rot(-25, 0, 20);
            feather.box(-0.5f, 0, -0.5f, 1, 5, 1).color(shellD);
            feather.box(-1, 4, -1, 2, 3, 2).color(shell).bevel(0.15f).noise(0.1f);
            torso.box(-5.5f, 6, -2.5f, 3, 3, 5).color(shell).noise(0.12f).bevel(0.2f);
            torso.box(2.5f, 6, -2.5f, 3, 3, 5).color(shell).noise(0.12f).bevel(0.2f);
            torso.box(-2, 3, 2.5f, 4, 4, 2).color(shellD).noise(0.15f).bevel(0.2f);
        }
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 5.5f, 7, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(robe).bevel(0.1f);
            arm.box(-1.7f, -6.3f, -1.7f, 3, 1, 3).inflate(0.2f).color(trim).flat();
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1, -3, -1, 2, 3, 2).color(skin);
            if (s < 0) {
                Part dag = hand.add("dagger").at(0, -2, -1).rot(-90, 0, 0);
                dag.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
                dag.box(-1.5f, 2, -0.5f, 3, 1, 1).color(trim).flat();
                dag.box(-0.5f, 3, -0.4f, 1, damp ? 3 : 6, 1).color(damp ? 0x9ab0b0 : 0xe0ddd0).flat().noise(0.03f);
            }
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 38f, 25f);
        m.clip("idle", 48).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 24, -1.5f, 0, 0).rot("torso", 48, 1.5f, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 0, 8, 3).rot("head", 36, 0, -8, -3).rot("head", 48, 0, 0, 0)
            .rot("armR", 0, 4, 0, 6).rot("armR", 24, 8, 0, 8).rot("armR", 48, 4, 0, 6)
            .rot("armL", 0, 4, 0, -6).rot("armL", 24, 8, 0, -8).rot("armL", 48, 4, 0, -6)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 30, 0, 0, 0).rot("beakL", 33, -22, 0, 0).rot("beakL", 36, 0, 0, 0).rot("beakL", 48, 0, 0, 0);
        m.clip("incantation", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 12, 0, 0).rot("torso", 16, -14, 0, 0, Ease.OUT_BACK).rot("torso", 30, -10, 0, 0).rot("torso", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -2.5f, 0).pos("body", 16, 0, 1.5f, 0, Ease.OUT_BACK).pos("body", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, 30, 0, 10).rot("armR", 16, -165, 0, 25, Ease.OUT_BACK).rot("armR", 30, -160, 0, 28).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, 30, 0, -10).rot("armL", 16, -165, 0, -25, Ease.OUT_BACK).rot("armL", 30, -160, 0, -28).rot("armL", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 18, 0, 0).rot("head", 16, -40, 0, 0, Ease.OUT_BACK).rot("head", 30, -35, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 15, 0, 0, 0).rot("beakL", 17, -35, 0, 0).rot("beakL", 21, -5, 0, 0).rot("beakL", 23, -35, 0, 0).rot("beakL", 27, -5, 0, 0).rot("beakL", 29, -35, 0, 0).rot("beakL", 34, 0, 0, 0)
            .rot("skirt", 0, 0, 0, 0).rot("skirt", 20, 0, 8, 0).rot("skirt", 24, 0, -8, 0).rot("skirt", 28, 0, 8, 0).rot("skirt", 40, 0, 0, 0);
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
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -120, 0, 40).rot("armR", 30, 20, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -40).rot("armL", 30, 10, 0, -70)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 30, 30, 15, 10)
            .rot("legR", 0, 0, 0, 0).rot("legR", 18, -70, 0, 10).rot("legR", 30, -80, 0, 10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 18, -60, 0, -10).rot("legL", 30, -80, 0, -10);
        return m;
    }

    /** ritual = Strength gained per turn after Incantation; strike = Dark Strike damage */
    static MobDef def(String id, String en, String ja, boolean damp, int hp, int ritual, int strike, int egg1, int egg2, MobDef.Habitat hab) {
        return MobDef.of(id, en, ja)
            .game(2, 1).size(0.6f, 1.75f).hp(hp).speed(0.26f).style(Style.BIRD).egg(egg1, egg2)
            .model(() -> build(damp))
            .spawn(damp ? 5 : 6, 1, 1, hab)
            .pattern(Pattern.seq(1, "incantation", "dark_strike"))
            .move(Move.of("incantation", 40).intent(Intent.BUFF).noTarget()
                .sound(15, SoundEvents.PARROT_IMITATE_WITCH, 1.4f, damp ? 0.5f : 0.7f)
                .at(17, (s, t) -> { Fx.sound(s, SoundEvents.PARROT_IMITATE_PHANTOM, 1.6f, 0.5f); Fx.particles(s, damp ? ParticleTypes.DRIPPING_WATER : ParticleTypes.WITCH, 25, 0.5); s.flags.add("ritual"); }))
            .move(Move.of("dark_strike", 26).intent(Intent.ATTACK, strike, 1).reach(2.2f)
                .sound(8, SoundEvents.PHANTOM_FLAP, 0.8f, 1.4f)
                .at(12, Fx.melee(strike, 2.8, 70)))
            .onMoveEnd(s -> { if (s.flags.contains("ritual") && !s.lastMove().equals("incantation")) s.addStrength(ritual); });
    }
}
