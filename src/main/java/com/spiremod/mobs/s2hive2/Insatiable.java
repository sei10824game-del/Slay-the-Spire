package com.spiremod.mobs.s2hive2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Hive boss THE INSATIABLE (321 HP): colossal sand-worm erupting from the dunes with a four-petal toothed maw.
 * Opens with Liquify Ground (Sandpit 4, 6 Frantic Escape cards: here a heavy Slowness + Weakness area), then loops Thrash 8x2 -> Lunging Bite 28 -> Salivate +2 Str -> Thrash.
 */
public final class Insatiable {
    private Insatiable() {}

    static final int SAND = 0xc8a468, SAND_L = 0xe8cc8c, SAND_D = 0x8a6a3a, RING = 0x6a4a2a, MAW = 0xa02838, MAW_D = 0x5a1020, TOOTH = 0xf0e8d0;
    static final int[] W = {26, 23, 21, 19, 17, 15};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_insatiable");
        Part base = m.add("base").at(0, 0, 0);
        base.box(-20, 0, -20, 40, 3, 40).color(SAND_D).grad(SAND, SAND_D).noise(0.2f).bevel(0.2f);          // churned dune mound
        base.box(-14, 3, -14, 28, 3, 28).color(SAND).noise(0.2f).bevel(0.2f);
        Part prev = base;
        for (int i = 0; i < W.length; i++) {
            Part sg = prev.add("s" + i).at(0, i == 0 ? 4 : 10, 0).rot(i == 0 ? -4 : (i % 2 == 0 ? 6 : -4), 0, 0);
            int w = W[i];
            sg.box(-w / 2f, 0, -w / 2f, w, 11, w).color(SAND).grad(SAND_L, SAND_D).bevel(0.25f).noise(0.14f)
                .band(0, 1, RING).band(10, 1, RING).paint(Face.FRONT, 2, 3, w - 4, 1, SAND_D).paint(Face.FRONT, 3, 7, w - 6, 1, SAND_D);
            sg.box(-w / 2f - 0.6f, 4, -w / 2f - 0.6f, w + 1, 3, w + 1).color(RING).grad(SAND_D, RING).flat().noise(0.1f).inflate(0.2f);   // plated ring
            for (int k = -1; k <= 1; k += 2) sg.box(k * (w / 2f) - 1, 7, -1, 2, 3, 2).color(TOOTH).flat().bevel(0.2f);                       // spines
            prev = sg;
        }
        // maw head: faces +z (rotated up-forward at rest)
        Part head = prev.add("head").at(0, 10, 0).rot(65, 0, 0);
        head.box(-8, 0, -8, 16, 5, 16).color(RING).grad(SAND_D, RING).bevel(0.25f).noise(0.1f);
        head.box(-6, 4, -6, 12, 1, 12).color(MAW).flat().glow(Face.TOP, 2, 2, 8, 8, MAW);                  // throat
        head.box(-3, 5, -3, 6, 1, 6).color(MAW_D).flat();
        String[] pn = {"pF", "pB", "pL", "pR"};
        float[][] pp = {{0, 5, 8}, {0, 5, -8}, {8, 5, 0}, {-8, 5, 0}};
        for (int i = 0; i < 4; i++) {
            Part p = head.add(pn[i]).at(pp[i][0], pp[i][1], pp[i][2]);
            boolean fb = i < 2;
            int dx = fb ? 14 : 3, dz = fb ? 3 : 14;
            float x0 = fb ? -7 : -1.5f, z0 = fb ? -1.5f : -7;
            p.box(x0, 0, z0, dx, 12, dz).color(SAND).grad(SAND_L, SAND_D).bevel(0.3f).noise(0.14f);
            p.box(x0 + (fb ? 1 : -0.4f), 12, z0 + (fb ? -0.4f : 1), fb ? 12 : 2, 4, fb ? 2 : 12).color(SAND_L).grad(SAND_L, SAND).bevel(0.3f);   // petal tip
            // teeth along the inner edge
            for (int t = 0; t < 4; t++) {
                float o = -5 + t * 3.4f;
                p.box(fb ? o : (i == 2 ? -2.2f : 1.2f), 5 + (t % 2) * 3, fb ? (i == 0 ? -2.3f : 1.3f) : o, 1, 5, 1).color(TOOTH).flat().bevel(0.2f);
            }
            float c = 28; p.rot(i == 0 ? -c : i == 1 ? c : 0, 0, i == 2 ? c : i == 3 ? -c : 0);
        }
        // eyes / sensory nubs
        for (int sd = -1; sd <= 1; sd += 2) head.box(sd * 8.2f - 1, 1, -2, 2, 2, 2).color(0xffd040).flat().glow(Face.FRONT, 0, 0, 2, 2, 0xffd040);
        m.proc((r, c) -> {
            for (int i = 0; i < 6; i++) r.rot("s" + i, (float) Math.sin(c.age * 0.07f + i * 0.9f) * 3, (float) Math.sin(c.age * 0.05f + i * 0.5f) * 4, 0);
            float drool = "salivate".equals(c.move) ? 1 : 0; r.scl("head", 1 + drool * 0.03f, 1, 1 + drool * 0.03f);
        });
        m.clip("walk", 16).loop().pos("base", 0, 0, 0, 0).pos("base", 8, 0, -0.8f, 0).pos("base", 16, 0, 0, 0);
        Clip id = m.clip("idle", 60).loop();
        id.rot("head", 0, 0, 0, 0).rot("head", 30, -8, 8, 0).rot("head", 60, 0, 0, 0);
        petals(id, 0, 0); petals(id, 30, -10); petals(id, 60, 0);
        // LIQUIFY GROUND: rear up, crash down, ground turns to sand-soup (ring burst)
        Clip lq = m.clip("liquify_ground", 56);
        lq.rot("s0", 0, 0, 0, 0).rot("s0", 16, -14, 0, 0).rot("s0", 24, 18, 0, 0, Ease.IN_CUBIC).rot("s0", 56, 0, 0, 0)
          .rot("s3", 0, 0, 0, 0).rot("s3", 16, -22, 0, 0).rot("s3", 24, 28, 0, 0, Ease.IN_CUBIC).rot("s3", 56, 0, 0, 0)
          .rot("head", 0, 0, 0, 0).rot("head", 16, -50, 0, 0).rot("head", 24, 45, 0, 0, Ease.IN_CUBIC).rot("head", 56, 0, 0, 0)
          .pos("base", 0, 0, 0, 0).pos("base", 16, 0, 4, 0).pos("base", 24, 0, -8, 0, Ease.IN_CUBIC).pos("base", 34, 0, -8, 0).pos("base", 48, 0, 0, 0, Ease.OUT_BACK).pos("base", 56, 0, 0, 0)
          .scl("base", 0, 1, 1, 1).scl("base", 24, 1.4f, 1, 1.4f, Ease.OUT_BACK).scl("base", 56, 1, 1, 1);
        petals(lq, 0, 0); petals(lq, 16, -70); petals(lq, 24, 10); petals(lq, 40, -25); petals(lq, 56, 0);
        // THRASH: two sweeping bites left then right
        Clip th = m.clip("thrash", 36);
        th.rot("s2", 0, 0, 0, 0).rot("s2", 6, 0, -22, 0).rot("s2", 10, 0, 24, 0, Ease.IN_CUBIC).rot("s2", 17, 0, 24, 0).rot("s2", 20, 0, -26, 0, Ease.IN_CUBIC).rot("s2", 28, 0, -10, 0).rot("s2", 36, 0, 0, 0)
          .rot("s4", 0, 0, 0, 0).rot("s4", 6, 15, -20, 0).rot("s4", 10, -10, 28, 0, Ease.IN_CUBIC).rot("s4", 20, -10, -30, 0, Ease.IN_CUBIC).rot("s4", 36, 0, 0, 0)
          .rot("head", 0, 0, 0, 0).rot("head", 6, -40, -20, 0).rot("head", 10, 25, 30, 0, Ease.IN_CUBIC).rot("head", 14, 10, 30, 0).rot("head", 20, 25, -34, 0, Ease.IN_CUBIC).rot("head", 36, 0, 0, 0)
          .pos("base", 0, 0, 0, 0).pos("base", 10, 0, 0, 3, Ease.IN_CUBIC).pos("base", 20, 0, 0, 3, Ease.IN_CUBIC).pos("base", 36, 0, 0, 0);
        petals(th, 0, 0); petals(th, 6, -75); petals(th, 10, 15); petals(th, 14, -60); petals(th, 20, 15); petals(th, 36, 0);
        // LUNGING BITE: coil back, stretch up, launch forward, huge chomp
        Clip lb = m.clip("lunging_bite", 44);
        lb.rot("s0", 0, 0, 0, 0).rot("s0", 14, -20, 0, 0).rot("s0", 20, 30, 0, 0, Ease.IN_CUBIC).rot("s0", 44, 0, 0, 0)
          .rot("s2", 0, 0, 0, 0).rot("s2", 14, -25, 0, 0).rot("s2", 20, 24, 0, 0, Ease.IN_CUBIC).rot("s2", 44, 0, 0, 0)
          .rot("s4", 0, 0, 0, 0).rot("s4", 14, -25, 0, 0).rot("s4", 20, 20, 0, 0, Ease.IN_CUBIC).rot("s4", 44, 0, 0, 0)
          .rot("head", 0, 0, 0, 0).rot("head", 14, -55, 0, 0).rot("head", 20, 30, 0, 0, Ease.IN_CUBIC).rot("head", 24, 30, 0, 0).rot("head", 44, 0, 0, 0)
          .pos("base", 0, 0, 0, 0).pos("base", 14, 0, 0, -5).pos("base", 20, 0, -2, 14, Ease.IN_CUBIC).pos("base", 44, 0, 0, 0, Ease.OUT_CUBIC);
        petals(lb, 0, 0); petals(lb, 14, -80); petals(lb, 20, -85); petals(lb, 23, 25, Ease.IN_CUBIC); petals(lb, 30, 15); petals(lb, 44, 0);
        // SALIVATE: head thrown back, maw gaping, trembling
        Clip sa = m.clip("salivate", 40);
        sa.rot("head", 0, 0, 0, 0).rot("head", 12, -45, 0, 0, Ease.OUT_BACK).rot("head", 28, -45, 5, 0).rot("head", 40, 0, 0, 0)
          .rot("s3", 0, 0, 0, 0).rot("s3", 12, -12, 0, 0).rot("s3", 14, -12, 4, 0).rot("s3", 16, -12, -4, 0).rot("s3", 18, -12, 4, 0).rot("s3", 28, -12, 0, 0).rot("s3", 40, 0, 0, 0)
          .scl("s5", 0, 1, 1, 1).scl("s5", 12, 1.15f, 1, 1.15f, Ease.OUT_BACK).scl("s5", 40, 1, 1, 1);
        petals(sa, 0, 0); petals(sa, 12, -85); petals(sa, 20, -70); petals(sa, 26, -85); petals(sa, 40, 0);
        m.clip("hurt", 10).rot("head", 0, 0, 0, 0).rot("head", 3, 20, 6, 0).rot("head", 10, 0, 0, 0).rot("s3", 0, 0, 0, 0).rot("s3", 3, 10, 0, 0).rot("s3", 10, 0, 0, 0);
        Clip d = m.clip("death", 44);
        d.rot("head", 0, 0, 0, 0).rot("head", 14, -50, 0, 0).rot("head", 44, 60, 0, 0)
         .rot("s2", 0, 0, 0, 0).rot("s2", 14, -15, 0, 0).rot("s2", 44, 30, 0, 0)
         .rot("s4", 0, 0, 0, 0).rot("s4", 14, -18, 0, 0).rot("s4", 44, 55, 0, 0)
         .pos("base", 0, 0, 0, 0).pos("base", 16, 0, 2, 0).pos("base", 44, 0, -30, 0, Ease.IN_QUAD);
        petals(d, 0, 0); petals(d, 14, -80); petals(d, 44, -40);
        return m;
    }


    static void petals(Clip c, int t, float off) { petals(c, t, off, Ease.SMOOTH); }
    /** off<0 opens the petals (offset from their closed rest pose), off>0 clamps them shut */
    static void petals(Clip c, int t, float off, Ease e) {
        c.rot("pF", t, off, 0, 0, e).rot("pB", t, -off, 0, 0, e).rot("pL", t, 0, 0, -off, e).rot("pR", t, 0, 0, off, e);
    }

    public static MobDef def() {
        return MobDef.of("s2_the_insatiable", "The Insatiable", "飽くなき者")
            .game(2, 2).size(3.4f, 4.4f).hp(321).speed(0.2f).style(Style.BEAST).egg(0xc8a468, 0xa02838)
            .boss().model(Insatiable::model)
            .pattern(Pattern.seq(1, "liquify_ground", "thrash", "lunging_bite", "salivate", "thrash"))
            .move(Move.of("liquify_ground", 56).intent(Intent.STRONG_DEBUFF).noTarget()
                .sound(16, SoundEvents.RAVAGER_ROAR, 1.6f, 0.5f)
                .at(24, (s, t) -> {
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 1.4f, 0.6f); Fx.sound(s, SoundEvents.SAND_BREAK, 1.6f, 0.5f);
                    Fx.ring(s, ParticleTypes.POOF, s.position().add(0, 0.2, 0), 3, 30); Fx.ring(s, ParticleTypes.POOF, s.position().add(0, 0.2, 0), 6, 40); Fx.ring(s, ParticleTypes.CLOUD, s.position().add(0, 0.2, 0), 9, 40);
                    for (net.minecraft.world.entity.LivingEntity e : Fx.foes(s, H.around(s, 14))) { Fx.slow(e, 12, 3); Fx.weak(e, 6); Fx.curse(e, Fx.Curse.DAZED); }
                }))
            .move(Move.of("thrash", 36).intent(Intent.ATTACK, 8, 2).reach(4.6f)
                .sound(6, SoundEvents.RAVAGER_ATTACK, 1.2f, 0.6f).sound(16, SoundEvents.RAVAGER_ATTACK, 1.2f, 0.7f)
                .at(10, H.cone(8, 4.6, 150)).at(20, H.cone(8, 4.6, 150)))
            .move(Move.of("lunging_bite", 44).intent(Intent.ATTACK, 28, 1).reach(7f)
                .sound(14, SoundEvents.RAVAGER_ROAR, 1.2f, 0.8f)
                .at(17, Fx.dash(0.85, 0.15))
                .at(23, (s, t) -> { H.cone(s, 28, 5.2, 90, null, null); Fx.sound(s, SoundEvents.RAVAGER_ATTACK, 1.6f, 0.5f); Fx.particlesAt(s, ParticleTypes.CLOUD, Fx.rel(s, 3.5, 0, 0.3), 14, 0.8, 0.1); }))
            .move(Move.of("salivate", 40).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.RAVAGER_STUNNED, 1.4f, 0.5f)
                .at(14, (s, t) -> { s.addStrength(2); Fx.particlesAt(s, ParticleTypes.FALLING_HONEY, Fx.rel(s, 3, 0, 3.2), 14, 0.6, 0.02); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 1); }));
    }
}
