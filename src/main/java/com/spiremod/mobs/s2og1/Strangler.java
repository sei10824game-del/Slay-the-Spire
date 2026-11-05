package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;

/**
 * StS2 Overgrowth: Slithering Strangler 53-55 HP, a thick vine-serpent.
 * Constrict (applies 3 Constrict) -> Thwack (7 + 5 Block) or Lash (12) 50/50 -> Constrict -> ...
 * Constrict is approximated with Slowness + Poison (damage over time) on the victim.
 */
public final class Strangler {
    private Strangler() {}

    static final int SKIN = 0x4a7a32, SKIN_L = 0x7aac4a, SKIN_D = 0x2a4a20, BELLY = 0xc8c27a, THORN = 0x6a4a2a, EYE = 0xffd23a, MOUTH = 0x6a1a28;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_slithering_strangler");
        // coiled base on the ground: 3 stacked rings, then a neck rising from it (chain of segments)
        Part coil = m.add("coil").at(0, 0, 0);
        coil.box(-9, 0, -7, 18, 4, 14).color(SKIN).grad(SKIN_L, SKIN_D).noise(0.08f).bevel(0.3f).paint(Face.TOP, 2, 3, 14, 1, SKIN_D).paint(Face.TOP, 2, 9, 14, 1, SKIN_D);
        coil.box(-7, 4, -5.5f, 14, 3, 11).color(SKIN).grad(SKIN_L, SKIN_D).noise(0.08f).bevel(0.3f);
        coil.box(-9.5f, 0, 2, 3, 3, 8).color(BELLY).flat().inflate(0.1f);                         // belly scales peeking out
        Part tail = coil.add("tail").at(0, 1.5f, -7);
        tail.box(-2.5f, -1.5f, -7, 5, 3, 7).color(SKIN).grad(SKIN_L, SKIN_D).bevel(0.2f);
        Part tail2 = tail.add("tail2").at(0, 0, -7);
        tail2.box(-1.5f, -1, -6, 3, 2, 6).color(SKIN_D);
        Part n1 = coil.add("neck1").at(0, 6, 1);
        n1.box(-3, 0, -3, 6, 7, 6).color(SKIN).grad(SKIN_L, SKIN_D).bevel(0.2f).noise(0.08f).paint(Face.FRONT, 1, 0, 4, 7, BELLY);
        Part n2 = n1.add("neck2").at(0, 7, 0);
        n2.box(-2.8f, 0, -2.8f, 6, 7, 6).color(SKIN).grad(SKIN_L, SKIN).bevel(0.2f).noise(0.08f).paint(Face.FRONT, 1, 0, 4, 7, BELLY);
        n2.box(-1, 7, 1.5f, 2, 2, 2).color(THORN).flat();
        Part n3 = n2.add("neck3").at(0, 7, 0);
        n3.box(-2.5f, 0, -2.5f, 5, 6, 5).color(SKIN).grad(SKIN_L, SKIN).bevel(0.2f).paint(Face.FRONT, 1, 0, 3, 6, BELLY);
        Part head = n3.add("head").at(0, 6, 0);
        head.box(-3.5f, 0, -3, 7, 4, 7).color(SKIN_L).grad(SKIN_L, SKIN).bevel(0.2f).glow(Face.FRONT, 0, 0, 1, 1, EYE).glow(Face.FRONT, 6, 0, 1, 1, EYE)
            .glow(Face.LEFT, 1, 1, 2, 1, EYE).glow(Face.RIGHT, 4, 1, 2, 1, EYE);
        head.box(-2.5f, 0, 4, 5, 3, 3).color(SKIN_L).bevel(0.15f).paint(Face.FRONT, 0, 0, 1, 1, SKIN_D).paint(Face.FRONT, 4, 0, 1, 1, SKIN_D);
        head.box(-3.5f, 4, -3, 7, 1, 4).color(THORN).flat();                                       // brow thorn ridge
        for (int s = -1; s <= 1; s += 2) head.box(s * 3 - 0.5f, 3, -2.5f, 1, 4, 1).color(THORN).flat().inflate(0.1f);
        Part jaw = head.add("jaw").at(0, 0, -2);
        jaw.box(-3, -2, 0, 6, 2, 8).color(BELLY).grad(BELLY, SKIN_D).paint(Face.TOP, 0, 0, 6, 8, MOUTH).paint(Face.TOP, 0, 6, 1, 2, 0xf0f0e0).paint(Face.TOP, 5, 6, 1, 2, 0xf0f0e0);
        // a few leafy vines draped on the coil
        for (int i = 0; i < 3; i++) {
            Part v = coil.add("vine" + i).at(-6 + i * 6, 6, -2 + i * 2).rot(20, i * 60, 25 - i * 20);
            v.box(-0.5f, 0, -0.5f, 1, 6, 1).color(0x3a8a3a).flat();
            v.box(-2, 5, -0.5f, 4, 2, 1).color(0x5aa84a).flat();
        }
        m.look("head", 50, 25);
        m.proc((r, c) -> {
            float w = (float) Math.sin(c.age * 0.1f) * 5;
            r.rot("neck1", 0, w, 0); r.rot("neck2", 0, w, 0); r.rot("neck3", 0, w, 0);
            r.rot("tail", 0, (float) Math.sin(c.age * 0.08f) * 22, 0); r.rot("tail2", 0, (float) Math.sin(c.age * 0.08f + 1) * 28, 0);
        });
        Clip c = m.clip("idle", 40).loop();
        Kit.R(c, "neck1", 0, 4, 0, 0, 20, -3, 0, 0, 40, 4, 0, 0);
        Kit.R(c, "neck2", 0, 6, 0, 0, 20, -5, 0, 0, 40, 6, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 14, 0, 8, 0, 28, 0, -8, 0, 40, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 30, 0, 0, 0, 33, 14, 0, 0, 36, 0, 0, 0);
        c = m.clip("walk", 20).loop();
        Kit.S(c, "coil", 0, 1, 1, 1, 10, 1.04f, 0.95f, 1.04f, 20, 1, 1, 1);
        // CONSTRICT: neck lashes out and loops around the target, squeezing (whole body scrunches)
        c = m.clip("constrict", 40);
        Kit.R(c, "neck1", 0, 0, 0, 0, 10, -25, 0, 0, -16, 55, 0, 0, 26, 40, 0, 0, 40, 0, 0, 0);
        Kit.R(c, "neck2", 0, 0, 0, 0, 10, -25, 0, 0, -16, 40, 20, 0, 22, 55, 90, 0, 30, 55, 140, 0, 40, 0, 0, 0);
        Kit.R(c, "neck3", 0, 0, 0, 0, 10, -20, 0, 0, -16, 25, 0, 0, 22, 55, 90, 0, 30, 55, 140, 0, 40, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 10, -15, 0, 0, -16, 20, 0, 0, 30, 20, 0, 0, 40, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 10, 30, 0, 0, -16, 5, 0, 0, 40, 0, 0, 0);
        Kit.P(c, "coil", 0, 0, 0, 0, 10, 0, 0, -2, -16, 0, 0, 5, 26, 0, 0, 3, 40, 0, 0, 0);
        Kit.S(c, "coil", 0, 1, 1, 1, 22, 1.1f, 0.9f, 1.1f, 30, 0.92f, 1.1f, 0.92f, 40, 1, 1, 1);
        // THWACK: tail whips the target and curls back to shield the body
        c = m.clip("thwack", 32);
        Kit.R(c, "tail", 0, 0, 0, 0, 10, 0, -70, 0, -15, 0, 80, 0, 26, 0, 20, 0, 32, 0, 0, 0);
        Kit.R(c, "tail2", 0, 0, 0, 0, 10, 0, -50, 0, -15, 0, 70, 0, 32, 0, 0, 0);
        Kit.R(c, "coil", 0, 0, 0, 0, 10, 0, 18, 0, -15, 0, -30, 0, 32, 0, 0, 0);
        Kit.R(c, "neck1", 0, 0, 0, 0, 10, 12, 0, 0, 32, 0, 0, 0);
        Kit.S(c, "coil", 0, 1, 1, 1, 24, 1, 1, 1, 28, 1.1f, 1.15f, 1.1f, 32, 1, 1, 1);
        // LASH: head rears back like a cobra, then whips down in a heavy bite-slam
        c = m.clip("lash", 34);
        Kit.R(c, "neck1", 0, 0, 0, 0, 12, -32, 0, 0, -17, 50, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "neck2", 0, 0, 0, 0, 12, -30, 0, 0, -17, 45, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "neck3", 0, 0, 0, 0, 12, -20, 0, 0, -17, 40, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 12, -25, 0, 0, -17, 35, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 12, 50, 0, 0, -17, 30, 0, 0, 22, 0, 0, 0);
        Kit.P(c, "coil", 0, 0, 0, 0, 12, 0, 0, -3, -17, 0, 0, 6, 34, 0, 0, 0);
        Kit.hurt(m, "neck2", -20, 6);
        Kit.R(c = m.clip("death", 30), "neck1", 0, 0, 0, 0, 10, -20, 0, 0, 30, 85, 0, 0);
        Kit.R(c, "neck2", 0, 0, 0, 0, 10, -20, 0, 0, 30, 20, 0, 0);
        Kit.R(c, "neck3", 0, 0, 0, 0, 30, 25, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 10, -25, 0, 0, 30, 30, 20, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 10, 50, 0, 0, 30, 25, 0, 0);
        Kit.S(c, "coil", 0, 1, 1, 1, 30, 1.2f, 0.4f, 1.2f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_slithering_strangler", "Slithering Strangler", "絞蛇")
            .game(2, 1).size(1.3f, 1.4f).hp(54).speed(0.2f).style(Style.SPIDER).egg(0x4a7a32, 0xc8c27a)
            .model(Strangler::model)
            .spawn(6, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(s -> s.getTurn() % 2 == 0 ? "constrict" : (s.rand().nextBoolean() ? "thwack" : "lash"))
            .move(Move.of("constrict", 40).intent(Intent.DEBUFF).reach(3.6f)
                .sound(14, SoundEvents.SNIFFER_HAPPY, 0.8f, 0.5f).sound(22, SoundEvents.LEASH_KNOT_PLACE, 1.2f, 0.6f)
                .at(24, (s, t) -> { if (t == null) return; Fx.slow(t, 3 * Kit.TURN, 1); Fx.poison(t, 3 * Kit.TURN); Fx.particlesAt(s, ParticleTypes.COMPOSTER, t.position().add(0, 1, 0), 20, 0.4, 0.05); }))
            .move(Move.of("thwack", 32).intent(Intent.ATTACK_DEFEND, 7, 1).reach(3.2f)
                .sound(15, SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 0.7f).at(15, Kit.hit(7, 3.6, 160)).at(18, (s, t) -> Kit.block(s, 5)))
            .move(Move.of("lash", 34).intent(Intent.ATTACK, 12, 1).reach(3.2f)
                .sound(17, SoundEvents.PLAYER_ATTACK_STRONG, 1.0f, 0.6f).at(17, Kit.hit(12, 3.6, 100)));
    }
}
