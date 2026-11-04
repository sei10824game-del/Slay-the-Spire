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

    static final int SHELL = 0x55704a, SHELL_D = 0x34462f, SHELL_L = 0x7d9663, BELLY = 0xcdb683, BELLY_D = 0x9c8458,
        TOOTH = 0xeee6c8, GUM = 0x8a3a3a, MOUTH = 0x3a1414, EYE = 0xffd23c;

    static void plate(Box b) { b.grad(SHELL_L, SHELL_D).bevel(0.25f).noise(0.08f); }

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("jaw_worm");
        // ---- tail curling behind on the ground
        Part body = m.add("body").at(0, 0, 0);
        Part tail = body.add("tail").at(0, 0, -3);
        plate(tail.box(-3.5f, 0, -6, 7, 5, 6));
        tail.box(-3, 0, -6, 6, 1, 6).color(BELLY_D);
        Part tail2 = tail.add("tail2").at(0, 0, -6);
        plate(tail2.box(-2.5f, 0, -5, 5, 4, 5));
        Part tail3 = tail2.add("tail3").at(0, 0, -5);
        plate(tail3.box(-1.5f, 0, -4, 3, 3, 4));
        tail3.box(-0.5f, 1, -6, 1, 1, 2).color(SHELL_D);
        // ---- upright segmented body (chain of pivots so it can rear and lunge)
        plate(body.box(-4.5f, 0, -3.5f, 9, 6, 7));
        body.box(-3.5f, 0.5f, 3, 7, 5, 1).color(BELLY).grad(BELLY, BELLY_D).band(2, 1, BELLY_D);   // belly plate
        Part seg2 = body.add("seg2").at(0, 6, 0);
        plate(seg2.box(-4, 0, -3, 8, 5, 6));
        seg2.box(-3, 0.5f, 2.5f, 6, 4, 1).color(BELLY).grad(BELLY, BELLY_D);
        seg2.box(-1, 3, -4, 2, 2, 2).color(SHELL_D).bevel(0.3f);                                 // dorsal knob
        Part seg3 = seg2.add("seg3").at(0, 5, 0.5f);
        plate(seg3.box(-3.5f, 0, -2.5f, 7, 4, 5));
        seg3.box(-2.5f, 0.5f, 2, 5, 3, 1).color(BELLY).grad(BELLY, BELLY_D);
        seg3.box(-1, 2, -3.5f, 2, 2, 2).color(SHELL_D).bevel(0.3f);
        // ---- big armoured head
        Part head = seg3.add("head").at(0, 4, 0.5f);
        plate(head.box(-4.5f, 0, -3.5f, 9, 5, 7));
        head.box(-5, 3, -3, 10, 2, 5).color(SHELL_D).bevel(0.2f).inflate(0.2f);                  // brow ridge
        head.box(-4, 0, 3, 8, 5, 1).color(SHELL).grad(SHELL_L, SHELL_D)
            .glow(Face.FRONT, 1, 1, 1, 1, EYE).glow(Face.FRONT, 6, 1, 1, 1, EYE)                  // beady eyes
            .paint(Face.FRONT, 0, 0, 8, 1, SHELL_D);
        // horns sweeping back
        for (int s = -1; s <= 1; s += 2) {
            Part horn = head.add(s < 0 ? "hornR" : "hornL").at(s * 4, 4, -1).rot(-35, 0, s * -25);
            horn.box(-1, 0, -1, 2, 3, 2).color(SHELL_D).bevel(0.2f);
            horn.box(-0.5f, 3, -0.5f, 1, 2, 1).color(BELLY_D);
        }
        // upper jaw (snout) and big lower mandible with teeth
        Part jawU = head.add("jawU").at(0, 1, 4);
        jawU.box(-4, 0, 0, 8, 3, 4).color(SHELL).grad(SHELL_L, SHELL_D).bevel(0.2f);
        jawU.box(-3.5f, -0.5f, 0, 7, 1, 3).color(GUM);
        for (int i = 0; i < 4; i++) jawU.box(-3.5f + i * 2, -1.5f, 3, 1, 1, 1).color(TOOTH).flat();
        jawU.box(-4, -2, 0.5f, 1, 2, 1).color(TOOTH).flat();
        jawU.box(3, -2, 0.5f, 1, 2, 1).color(TOOTH).flat();
        Part jawL = head.add("jawL").at(0, 0.5f, 2.5f);
        jawL.box(-5, -3, 0, 10, 3, 6).color(BELLY).grad(SHELL, BELLY_D).bevel(0.25f);             // huge underbite
        jawL.box(-4, 0, 0.5f, 8, 1, 4).color(MOUTH);
        for (int i = 0; i < 5; i++) jawL.box(-4.5f + i * 2, 0, 5, 1, 2, 1).color(TOOTH).flat();
        jawL.box(-5, 0, 3, 1, 3, 1).color(TOOTH).flat();                                          // tusks
        jawL.box(4, 0, 3, 1, 3, 1).color(TOOTH).flat();
        // tiny stub legs on the base
        for (int s = -1; s <= 1; s += 2) {
            Part leg = body.add(s < 0 ? "legR" : "legL").at(s * 4.5f, 2, 1);
            leg.box(-1, -2, -1, 2, 3, 2).color(SHELL_D).bevel(0.2f);
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
            .game(1, 1).size(0.9f, 1.4f).hp(42).speed(0.22f).style(Style.INSECT).egg(SHELL, BELLY)
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
