package com.spiremod.mobs.s1act1a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 1 Jaw Worm. 40-44 HP. Opens with Chomp (11). Then Chomp 25% / Thrash 30% (7 dmg + 5 block) / Bellow 45% (+3 Str, 6 block).
 * No Chomp or Bellow twice in a row, no Thrash three times in a row.
 */
public final class JawWorm {
    private JawWorm() {}

    static final int SHELL = 0x3aa8b4, SHELL_D = 0x1f6f80, SHELL_L = 0x6cd0d6, BELLY = 0xa6dcd8, BELLY_D = 0x5c9aa0, SPOT = 0x1c5a6a,
        TOOTH = 0xf4f0e0, GUM = 0xb02a3a, MOUTH = 0x5a0f1c, EYE = 0xffd23c;

    static Box plate(Box b) { return b.grad(SHELL_L, SHELL_D).bevel(0.25f).noise(0.08f); }

    public static ModelSpec model() {
        // StS1 Jaw Worm: a teal fish-lizard with a huge toothy mouth, back spines and fins, dragging itself on stubby limbs
        ModelSpec m = new ModelSpec("jaw_worm").scale(0.8f);
        Part body = m.add("body").at(0, 6, 0);
        plate(body.box(-6, -5, -6, 12, 12, 12)).paint(Face.TOP, 2, 3, 2, 2, SPOT).paint(Face.TOP, 6, 9, 2, 2, SPOT).paint(Face.RIGHT, 4, 3, 2, 2, SPOT);
        body.box(-5, -5.5f, -5, 10, 1, 10).color(BELLY).grad(BELLY, BELLY_D);                             // pale belly
        for (int i = 0; i < 5; i++) body.box(-0.5f, 7, -5 + i * 2.5f, 1, 3 + (i == 2 ? 3 : i % 2), 2).color(SHELL_L).bevel(0.15f);   // back spines
        for (int s = -1; s <= 1; s += 2) {                                                                  // side fins
            Part fin = body.add(s < 0 ? "hornR" : "hornL").at(s * 5, 1, 1).rot(0, 0, s * -20);
            fin.box(s < 0 ? -5 : 0, -1, -2, 5, 1, 5).color(SHELL_L).grad(SHELL_L, SHELL).bevel(0.15f);
        }
        // ---- neck/head chain pointing forward so the whole front can rear and lunge
        Part seg2 = body.add("seg2").at(0, 0, 6);
        plate(seg2.box(-5.5f, -4.5f, 0, 11, 10, 4));
        seg2.box(-3.5f, -4, 0.5f, 7, 1, 4).color(BELLY).grad(BELLY, BELLY_D);
        Part seg3 = seg2.add("seg3").at(0, 0, 4);
        plate(seg3.box(-5, -4, 0, 10, 9, 3));
        Part head = seg3.add("head").at(0, 0, 3);
        plate(head.box(-6, -4, 0, 12, 10, 8)).paint(Face.TOP, 1, 1, 2, 2, SPOT).paint(Face.TOP, 5, 3, 2, 2, SPOT);
        head.box(-5.6f, 3, 5.4f, 2, 2, 2).color(EYE).flat().glow(Face.FRONT, 0, 0, 2, 1, EYE);
        head.box(3.6f, 3, 5.4f, 2, 2, 2).color(EYE).flat().glow(Face.FRONT, 0, 0, 2, 1, EYE);
        for (int i = 0; i < 3; i++) head.box(-0.5f, 3.5f, 0.5f + i * 2, 1, 2 + (i == 0 ? 1 : 0), 1).color(SHELL_L);   // crest spines
        // upper jaw (snout) with teeth hanging down
        Part jawU = head.add("jawU").at(0, 0.5f, 7.5f);
        jawU.box(-4.5f, -0.5f, 0, 9, 2.5f > 2 ? 3 : 2, 7).color(SHELL).grad(SHELL_L, SHELL_D).bevel(0.2f);
        jawU.box(-4, -1.2f, 0.5f, 8, 1, 6).color(GUM);
        for (int i = 0; i < 8; i++) jawU.box(-4 + i, -2, 6 - (i % 2), 1, 1, 1).color(TOOTH).flat();
        // huge lower mandible: red mouth, white teeth pointing up
        Part jawL = head.add("jawL").at(0, -2f, 5);
        jawL.box(-4.5f, -2.5f, 0, 9, 3, 10).color(BELLY).grad(SHELL, BELLY_D).bevel(0.2f);
        jawL.box(-4, -0.6f, 0.5f, 8, 1, 9).color(GUM);
        for (int i = 0; i < 8; i++) jawL.box(-4 + (i % 2) * 7.5f, -0.2f, 1 + i, 1, 2, 1).color(TOOTH).flat();
        jawL.box(-1, -0.6f, 2, 2, 1, 4).color(MOUTH);                                                      // tongue/throat shadow
        // tail: three tapering segments ending in a tail fin
        Part tail = body.add("tail").at(0, 0, -6);
        plate(tail.box(-4, -4, -5, 8, 9, 5));
        Part tail2 = tail.add("tail2").at(0, 0, -5);
        plate(tail2.box(-3, -3, -5, 6, 6, 5));
        Part tail3 = tail2.add("tail3").at(0, 0, -5);
        plate(tail3.box(-2, -2.5f, -5, 4, 5, 5));
        tail3.box(-0.5f, -5, -9, 1, 11, 5).color(SHELL_L).bevel(0.15f);                                       // vertical tail fin
        // stubby clawed front/back limbs
        for (int s = -1; s <= 1; s += 2) {
            Part leg = body.add(s < 0 ? "legR" : "legL").at(s * 5, -3, s < 0 ? 4 : 4).rot(0, 0, s * -12);
            leg.box(-1.2f, -5, -1.2f, 3, 5, 3).color(SHELL_D).bevel(0.2f);
            for (int c = 0; c < 3; c++) leg.box(-1.5f + c, -6, 0.5f, 1, 1, 2).color(TOOTH).flat();
        }
        m.look("head", 30, 20);
        m.clip("walk", 20).loop()
            .rot("body", 0, 6, 0, 0).rot("body", 10, -6, 0, 0).rot("body", 20, 6, 0, 0)
            .rot("seg2", 0, -8, 0, 0).rot("seg2", 10, 8, 0, 0).rot("seg2", 20, -8, 0, 0)
            .rot("tail", 0, 0, 10, 0).rot("tail", 10, 0, -10, 0).rot("tail", 20, 0, 10, 0)
            .rot("tail2", 0, 0, -15, 0).rot("tail2", 10, 0, 15, 0).rot("tail2", 20, 0, -15, 0)
            .rot("legR", 0, 35, 0, 0).rot("legR", 10, -35, 0, 0).rot("legR", 20, 35, 0, 0)
            .rot("legL", 0, -35, 0, 0).rot("legL", 10, 35, 0, 0).rot("legL", 20, -35, 0, 0);
        m.clip("idle", 40).loop()
            .rot("seg2", 0, 2, 0, 2).rot("seg2", 20, -2, 0, -2).rot("seg2", 40, 2, 0, 2)
            .rot("seg3", 0, -2, 0, -2).rot("seg3", 20, 3, 0, 2).rot("seg3", 40, -2, 0, -2)
            .rot("head", 0, 0, 4, 0).rot("head", 20, 0, -4, 0).rot("head", 40, 0, 4, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 24, 0, 0, 0).rot("jawL", 27, 18, 0, 0).rot("jawL", 30, 0, 0, 0).rot("jawL", 33, 14, 0, 0).rot("jawL", 36, 0, 0, 0)
            .rot("tail3", 0, 0, 10, 0).rot("tail3", 20, 0, -10, 0).rot("tail3", 40, 0, 10, 0);
        // CHOMP: rear way back with the mandible gaping, then lunge forward and snap shut
        m.clip("chomp", 28)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -8, 0, 0).rot("body", 14, 8, 0, 0, Ease.IN_CUBIC).rot("body", 20, 6, 0, 0).rot("body", 28, 0, 0, 0)
            .rot("seg2", 0, 0, 0, 0).rot("seg2", 10, -12, 0, 0).rot("seg2", 14, 14, 0, 0, Ease.IN_CUBIC).rot("seg2", 20, 12, 0, 0).rot("seg2", 28, 0, 0, 0)
            .rot("seg3", 0, 0, 0, 0).rot("seg3", 10, -12, 0, 0).rot("seg3", 14, 14, 0, 0, Ease.IN_CUBIC).rot("seg3", 28, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -18, 0, 0).rot("head", 14, -6, 0, 0, Ease.IN_CUBIC).rot("head", 28, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 10, 55, 0, 0).rot("jawL", 13, 50, 0, 0).rot("jawL", 15, -5, 0, 0, Ease.IN_CUBIC).rot("jawL", 18, 10, 0, 0).rot("jawL", 20, 0, 0, 0)
            .rot("jawU", 0, 0, 0, 0).rot("jawU", 10, -30, 0, 0).rot("jawU", 15, 3, 0, 0, Ease.IN_CUBIC).rot("jawU", 20, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -2).pos("body", 14, 0, 0, 6, Ease.IN_CUBIC).pos("body", 20, 0, 0, 5).pos("body", 28, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 10, 8, 0, 0).rot("tail", 14, -8, 0, 0).rot("tail", 28, 0, 0, 0);
        // THRASH: whip the whole body left-right-left, slamming the head into the target, then hunker (block)
        m.clip("thrash", 34)
            .rot("body", 0, 0, 0, 0).rot("body", 6, 0, 30, -15).rot("body", 11, 0, -35, 20, Ease.IN_CUBIC).rot("body", 16, 0, 30, -18, Ease.IN_CUBIC)
            .rot("body", 21, 0, -20, 12, Ease.IN_CUBIC).rot("body", 27, 8, 0, 0).rot("body", 34, 0, 0, 0)
            .rot("seg2", 0, 0, 0, 0).rot("seg2", 6, 0, 0, -15).rot("seg2", 11, 0, 0, 22).rot("seg2", 16, 0, 0, -22).rot("seg2", 21, 0, 0, 12).rot("seg2", 27, 10, 0, 0).rot("seg2", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 6, 0, -20, -15).rot("head", 11, 0, 25, 20).rot("head", 16, 0, -25, -20).rot("head", 21, 0, 15, 10).rot("head", 27, 15, 0, 0).rot("head", 34, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 6, 30, 0, 0).rot("jawL", 11, 10, 0, 0).rot("jawL", 16, 35, 0, 0).rot("jawL", 21, 5, 0, 0).rot("jawL", 34, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 6, 0, -30, 0).rot("tail", 11, 0, 35, 0).rot("tail", 16, 0, -30, 0).rot("tail", 21, 0, 20, 0).rot("tail", 34, 0, 0, 0)
            .rot("tail2", 0, 0, 0, 0).rot("tail2", 8, 0, -30, 0).rot("tail2", 13, 0, 35, 0).rot("tail2", 18, 0, -30, 0).rot("tail2", 23, 0, 15, 0).rot("tail2", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 11, 0, 0, 3).pos("body", 16, 0, 0, 3).pos("body", 27, 0, -1.5f, 0).pos("body", 34, 0, 0, 0);
        // BELLOW: coil down, then rear up tall, throw head back and roar with jaw wide open, shuddering
        m.clip("bellow", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 12, 0, 0).rot("body", 15, -14, 0, 0, Ease.OUT_BACK).rot("body", 32, -12, 0, 0).rot("body", 40, 0, 0, 0)
            .rot("seg2", 0, 0, 0, 0).rot("seg2", 8, 10, 0, 0).rot("seg2", 15, -10, 0, 0, Ease.OUT_BACK).rot("seg2", 32, -8, 0, 0).rot("seg2", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 20, 0, 0).rot("head", 15, -45, 0, 0, Ease.OUT_BACK)
            .rot("head", 18, -42, 0, 4).rot("head", 21, -45, 0, -4).rot("head", 24, -42, 0, 4).rot("head", 27, -45, 0, -4).rot("head", 32, -40, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 8, 0, 0, 0).rot("jawL", 15, 65, 0, 0, Ease.OUT_BACK).rot("jawL", 32, 60, 0, 0).rot("jawL", 40, 0, 0, 0)
            .rot("jawU", 0, 0, 0, 0).rot("jawU", 15, -25, 0, 0, Ease.OUT_BACK).rot("jawU", 32, -22, 0, 0).rot("jawU", 40, 0, 0, 0)
            .scl("seg2", 0, 1, 1, 1).scl("seg2", 15, 1.1f, 1.15f, 1.1f).scl("seg2", 19, 1.15f, 1.05f, 1.15f).scl("seg2", 23, 1.1f, 1.15f, 1.1f).scl("seg2", 27, 1.15f, 1.05f, 1.15f).scl("seg2", 40, 1, 1, 1)
            .rot("hornR", 0, 0, 0, 0).rot("hornR", 15, -15, 0, -10).rot("hornR", 40, 0, 0, 0)
            .rot("hornL", 0, 0, 0, 0).rot("hornL", 15, -15, 0, 10).rot("hornL", 40, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 8, -12, 0, 0).rot("tail", 15, 14, 0, 0, Ease.OUT_BACK).rot("tail", 32, 12, 0, 0).rot("tail", 40, 0, 0, 0);
        m.clip("hurt", 10)
            .rot("seg2", 0, 0, 0, 0).rot("seg2", 3, -14, 0, 6).rot("seg2", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -18, 0, -8).rot("head", 10, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 3, 30, 0, 0).rot("jawL", 10, 0, 0, 0);
        m.clip("death", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -10, 0, 0).rot("body", 22, 0, 0, 85, Ease.IN_QUAD).rot("body", 30, 0, 0, 80, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 22, -3, 4.5f, 0).pos("body", 30, -3, 4.5f, 0)
            .rot("seg2", 0, 0, 0, 0).rot("seg2", 22, 15, 0, 10).rot("seg2", 30, 20, 0, 10)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -30, 0, 0).rot("head", 30, 25, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 8, 50, 0, 0).rot("jawL", 30, 40, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 30, 0, 20, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("jaw_worm", "Jaw Worm", "ジョーワーム")
            .game(1, 1).size(1.3f, 1.1f).hp(42).speed(0.22f).style(Style.INSECT).egg(SHELL, BELLY)
            .model(JawWorm::model)
            .spawn(8, 1, 1, MobDef.Habitat.PLAINS)
            .pattern(Pattern.first("chomp", Pattern.random().add("chomp", 25).add("thrash", 30).add("bellow", 45)
                .maxRepeat("chomp", 1).maxRepeat("bellow", 1).maxRepeat("thrash", 2)))
            .move(Move.of("chomp", 28).intent(Intent.ATTACK, 11, 1).reach(2.4f)
                .sound(9, SoundEvents.RAVAGER_ROAR, 0.4f, 1.8f)
                .at(14, (s, t) -> { Fx.meleeNow(s, 11, 3.2, 90, null); Fx.sound(s, SoundEvents.EVOKER_FANGS_ATTACK, 1f, 1.2f); }))
            .move(Move.of("thrash", 34).intent(Intent.ATTACK_DEFEND, 7, 1).reach(2.4f)
                .sound(6, SoundEvents.SILVERFISH_AMBIENT, 1f, 0.6f)
                .at(11, Fx.melee(7, 3.2, 140))
                .at(26, Fx.block(5)))
            .move(Move.of("bellow", 40).intent(Intent.DEFEND_BUFF).noTarget()
                .at(15, (s, t) -> { Fx.sound(s, SoundEvents.RAVAGER_ROAR, 1.2f, 1.3f); s.addStrength(3); s.addBlock(6);
                    Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.5); Fx.ring(s, ParticleTypes.CLOUD, s.position().add(0, 0.2, 0), 1.2, 16); }));
    }
}
