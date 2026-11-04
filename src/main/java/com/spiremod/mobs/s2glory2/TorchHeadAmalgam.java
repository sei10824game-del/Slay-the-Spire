package com.spiremod.mobs.s2glory2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Glory: Torch Head Amalgam, the Queen's pet (199 HP, Minion). Fixed sequence:
 * Strong Tackle 26 -> Tackle 18 -> [Beam 8x3 -> Weak Tackle 14 -> Weak Tackle 14] repeating.
 */
public final class TorchHeadAmalgam {
    private TorchHeadAmalgam() {}

    static final String ID = "s2_torch_head_amalgam";
    static final int WOOD = 0x5a3a24, WOOD_D = 0x33201a, IRON = 0x4a4a52, IRON_L = 0x7a7a86, RUST = 0x7a4a2a, FLAME = 0xff9a28, FLAME_H = 0xffe070,
        CLOTH = 0x3b2230, BONE = 0xcbbf9a;

    /** iron-cage head with a flame inside; returns the head part */
    static Part cageHead(Part parent, String name, float x, float y, float z, int r) {
        Part h = parent.add(name).at(x, y, z);
        h.box(-r, 0, -r, 2 * r, 2, 2 * r).color(IRON).bevel(0.15f);                      // base ring
        G.fire(h.box(-r + 0.5f, 2, -r + 0.5f, 2 * r - 1, 2 * r - 1, 2 * r - 1), FLAME).grad(FLAME_H, FLAME);
        for (int sx = -1; sx <= 1; sx += 2) for (int sz = -1; sz <= 1; sz += 2)
            h.box(sx * (r - 0.5f) - 0.5f, 2, sz * (r - 0.5f) - 0.5f, 1, 2 * r + 1, 1).color(IRON_L).flat();   // cage bars
        h.box(-r, 2 * r + 1, -r, 2 * r, 1, 2 * r).color(IRON).bevel(0.15f);
        Part fl = h.add(name + "Fl").at(0, 2 * r + 1, 0);
        G.fire(fl.box(-1.5f, 0, -1.5f, 3, 5, 3), FLAME_H).grad(FLAME_H, FLAME);
        G.fire(fl.box(-0.5f, 5, -0.5f, 1, 3, 1), FLAME_H);
        return h;
    }

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec(ID).scale(1.15f);
        // legs: two stout stakes lashed together
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 4, 10, 0);
            leg.box(-2.5f, -10, -2.5f, 5, 10, 5).color(WOOD).grad(WOOD, WOOD_D).noise(0.12f);
            leg.box(-3, -10, -3.5f, 6, 2, 7).color(IRON).bevel(0.2f);
            leg.box(-2.8f, -6, -2.8f, 6, 1, 6).inflate(0.1f).color(CLOTH).flat();
        }
        Part body = m.add("body").at(0, 10, 0);
        body.box(-6, 0, -4, 12, 4, 8).color(WOOD_D).grad(WOOD, WOOD_D).noise(0.1f);
        Part torso = body.add("torso").at(0, 4, 0);
        torso.box(-7, 0, -4.5f, 14, 10, 9).color(WOOD).grad(0x70482c, WOOD_D).noise(0.12f).bevel(0.12f);
        torso.box(-7.3f, 3, -4.8f, 14, 1, 9).inflate(0.3f).color(IRON).flat();            // iron straps
        torso.box(-7.3f, 7, -4.8f, 14, 1, 9).inflate(0.3f).color(IRON).flat();
        torso.box(-1, 0, 4.4f, 2, 10, 1).color(RUST).flat();
        G.fire(torso.box(-2, 3, 4.5f, 4, 4, 1), FLAME);                                   // furnace window in the chest
        torso.box(-3, 2, 4.8f, 6, 1, 1).color(IRON).flat(); torso.box(-3, 7, 4.8f, 6, 1, 1).color(IRON).flat();
        // fused extra heads: two small ones on the shoulders, one on the back
        cageHead(torso, "headS1", -6, 10, 0, 2).rot(0, 0, 14);
        cageHead(torso, "headS2", 6, 10, 0, 2).rot(0, 0, -14);
        cageHead(torso, "headB", 0, 9, -4, 2).rot(-25, 0, 0);
        Part neck = torso.add("neck").at(0, 10, 0);
        neck.box(-2.5f, 0, -2.5f, 5, 3, 5).color(WOOD_D);
        Part head = cageHead(neck, "head", 0, 3, 0, 4);
        head.box(-3, 5, 4.1f, 6, 1, 1).color(0x120a08).flat();                             // snarling slit in the cage
        head.box(-2, 4, 3.9f, 1, 1, 1).color(BONE).flat(); head.box(1, 4, 3.9f, 1, 1, 1).color(BONE).flat();
        // arms: uneven, one huge club arm, one cage-fist
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 8.5f, 9, 0);
            int w = s < 0 ? 5 : 4;
            arm.box(-w / 2f, -9, -w / 2f, w, 9, w).color(WOOD).grad(WOOD, WOOD_D).noise(0.12f);
            arm.box(-w / 2f - 0.3f, -3, -w / 2f - 0.3f, w, 1, w).inflate(0.3f).color(IRON).flat();
            Part fist = arm.add(s < 0 ? "fistR" : "fistL").at(0, -9, 0);
            fist.box(-3, -5, -3, 6, 5, 6).color(WOOD_D).bevel(0.2f);
            fist.box(-3.3f, -2, -3.3f, 6, 1, 6).inflate(0.3f).color(IRON).flat();
            for (int i = -1; i <= 1; i++) fist.box(i * 2 - 0.5f, -7, 2.5f, 1, 2, 1).color(IRON_L).flat();   // nails
        }
        m.look("head", 40, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 30f, 20f);
        m.proc((r, c) -> { // flames lick
            float f = 1 + 0.12f * (float) Math.sin(c.age * 0.7);
            for (String n : new String[]{"headFl", "headS1Fl", "headS2Fl", "headBFl"}) r.scl(n, 1, f, 1);
        });
        m.clip("idle", 40).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 20, -2, 0, 0).rot("torso", 40, 2, 0, 0)
            .rot("head", 0, 0, 6, 0).rot("head", 20, 0, -6, 0).rot("head", 40, 0, 6, 0)
            .rot("headS1", 0, 0, 0, 0).rot("headS1", 10, 6, 0, 8).rot("headS1", 30, -6, 0, -4).rot("headS1", 40, 0, 0, 0)
            .rot("headS2", 0, 0, 0, 0).rot("headS2", 10, -6, 0, -8).rot("headS2", 30, 6, 0, 4).rot("headS2", 40, 0, 0, 0)
            .rot("armR", 0, 4, 0, 4).rot("armR", 20, 8, 0, 6).rot("armR", 40, 4, 0, 4)
            .rot("armL", 0, 4, 0, -4).rot("armL", 20, 8, 0, -6).rot("armL", 40, 4, 0, -4);
        // strong tackle: huge wind-up, lowers shoulder and charges forward
        m.clip("strong_tackle", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -20, -20, 0).rot("torso", 18, 38, 15, 0, Ease.IN_CUBIC).rot("torso", 28, 30, 10, 0).rot("torso", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -1, -5).pos("body", 18, 0, -2, 8, Ease.IN_CUBIC).pos("body", 28, 0, -2, 8).pos("body", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, 60, 0, 30).rot("armR", 18, -80, 0, -10, Ease.IN_CUBIC).rot("armR", 28, -70, 0, -10).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, 60, 0, -30).rot("armL", 18, -80, 0, 10, Ease.IN_CUBIC).rot("armL", 28, -70, 0, 10).rot("armL", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -25, 0, 0).rot("head", 18, 35, 0, 0, Ease.IN_CUBIC).rot("head", 36, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 12, 25, 0, 0).rot("legR", 18, -40, 0, 0).rot("legR", 36, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 12, -20, 0, 0).rot("legL", 18, 30, 0, 0).rot("legL", 36, 0, 0, 0);
        // tackle: shorter shoulder charge
        m.clip("tackle", 28)
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -14, 14, 0).rot("torso", 14, 28, -10, 0, Ease.IN_CUBIC).rot("torso", 28, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, -3).pos("body", 14, 0, -1, 6, Ease.IN_CUBIC).pos("body", 28, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, 40, 0, 20).rot("armR", 14, -70, 0, 0, Ease.IN_CUBIC).rot("armR", 28, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, 30, 0, -20).rot("armL", 14, -50, 0, 0, Ease.IN_CUBIC).rot("armL", 28, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 25, 0, 0).rot("head", 28, 0, 0, 0);
        m.clip("weak_tackle", 24)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -8, 0, 0).rot("torso", 12, 18, 0, 0, Ease.IN_CUBIC).rot("torso", 24, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 0, 4, Ease.IN_CUBIC).pos("body", 24, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, 25, 0, 10).rot("armR", 12, -45, 0, 0, Ease.IN_CUBIC).rot("armR", 24, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -30, 0, 0).rot("armL", 24, 0, 0, 0);
        // beam: head rears back, flames swell, three searing pulses
        Clip beam = m.clip("beam", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -14, 0, 0).rot("torso", 36, -8, 0, 0).rot("torso", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -40, 0, 0, Ease.OUT_BACK).rot("head", 36, -20, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 0, 0, 55).rot("armR", 36, 0, 0, 40).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 0, 0, -55).rot("armL", 36, 0, 0, -40).rot("armL", 40, 0, 0, 0)
            .scl("head", 0, 1, 1, 1).scl("head", 10, 1.2f, 1.2f, 1.2f);
        for (int t : new int[]{16, 23, 30}) beam.rot("head", t, -12, 0, 0, Ease.IN_CUBIC).rot("head", t + 3, -34, 0, 0).scl("head", t, 1.4f, 1.4f, 1.4f).scl("head", t + 3, 1.15f, 1.15f, 1.15f);
        beam.scl("head", 40, 1, 1, 1);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -14, 0, 5).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 8, 0).rot("head", 10, 0, 0, 0)
            .rot("headS1", 0, 0, 0, 0).rot("headS1", 3, 20, 0, 20).rot("headS1", 10, 0, 0, 0)
            .rot("headS2", 0, 0, 0, 0).rot("headS2", 3, 20, 0, -20).rot("headS2", 10, 0, 0, 0);
        m.clip("death", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -18, 0, 0).rot("torso", 36, 55, 0, 6, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 36, 0, -8, -3, Ease.IN_QUAD)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -35, 0, 0).rot("head", 36, 40, 0, 15)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -90, 0, 40).rot("armR", 36, 10, 0, 60)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -70, 0, -40).rot("armL", 36, 10, 0, -60)
            .rot("legR", 0, 0, 0, 0).rot("legR", 36, -60, 0, 8).rot("legL", 0, 0, 0, 0).rot("legL", 36, -50, 0, -8)
            .scl("headFl", 0, 1, 1, 1).scl("headFl", 30, 1, 0.1f, 1).scl("headS1Fl", 0, 1, 1, 1).scl("headS1Fl", 24, 1, 0.1f, 1)
            .scl("headS2Fl", 0, 1, 1, 1).scl("headS2Fl", 24, 1, 0.1f, 1).scl("headBFl", 0, 1, 1, 1).scl("headBFl", 24, 1, 0.1f, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of(ID, "Torch Head Amalgam", "トーチヘッド・アマルガム")
            .game(2, 3).size(1.5f, 2.4f).hp(199).speed(0.24f).style(Style.FIRE).egg(0x5a3a24, 0xff9a28).minion().fireImmune()
            .model(TorchHeadAmalgam::model)
            .pattern(Pattern.seq(2, "strong_tackle", "tackle", "beam", "weak_tackle", "weak_tackle"))
            .move(Move.of("strong_tackle", 36).intent(Intent.ATTACK, 26, 1).reach(2.6f)
                .sound(10, SoundEvents.RAVAGER_STEP, 1.2f, 0.6f)
                .at(18, (s, t) -> { Fx.meleeNow(s, 26, 3.2, 100, v -> Fx.knock(s, v, 1.0)); Fx.particlesAt(s, ParticleTypes.FLAME, Fx.rel(s, 2, 0, 1), 12, 0.4, 0.05); }))
            .move(Move.of("tackle", 28).intent(Intent.ATTACK, 18, 1).reach(2.6f)
                .at(14, (s, t) -> { Fx.meleeNow(s, 18, 3.0, 100, v -> Fx.knock(s, v, 0.6)); Fx.particlesAt(s, ParticleTypes.FLAME, Fx.rel(s, 2, 0, 1), 8, 0.3, 0.04); }))
            .move(Move.of("weak_tackle", 24).intent(Intent.ATTACK, 14, 1).reach(2.6f)
                .at(12, Fx.melee(14, 3.0, 100)))
            .move(Move.of("beam", 40).intent(Intent.ATTACK, 8, 3).ranged(14)
                .sound(8, SoundEvents.BLAZE_SHOOT, 1.2f, 0.6f)
                .at(16, G.beam(8, ParticleTypes.FLAME, 0.8, 2.3)).at(23, G.beam(8, ParticleTypes.FLAME, 0.8, 2.3)).at(30, G.beam(8, ParticleTypes.FLAME, 0.8, 2.3))
                .atAll(new int[]{16, 23, 30}, Fx.sfx(SoundEvents.FIRECHARGE_USE, 1f, 0.8f)))
            .onClientTick(s -> {
                if (s.tickCount % 3 == 0) s.level().addParticle(ParticleTypes.FLAME, s.getX() + (s.getRandom().nextDouble() - .5) * 0.5, s.getY() + 2.6, s.getZ() + (s.getRandom().nextDouble() - .5) * 0.5, 0, 0.03, 0);
            });
    }
}
