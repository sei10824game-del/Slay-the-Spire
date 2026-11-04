package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * StS1 Act 3 Spire Growth: 170 HP. A thorny teal tentacle arching out of the ground.
 * Quick Tackle 16, Smash 22, Constrict (10 Constricted: damage at the end of every turn).
 * AI: 50% Quick Tackle / else Constrict; if Constrict used last turn or the player is Constricted -> Smash. No attack 3x in a row.
 */
public final class SpireGrowth {
    private SpireGrowth() {}

    static final int TEAL = 0x78c2b0, TEAL_L = 0xa3e0cf, TEAL_D = 0x4f9586, THORN = 0x2f5f57, DIRT = 0x3b3530;
    static final int N = 10;
    static final int[] LEN = {6, 6, 6, 5, 5, 5, 5, 4, 4, 4};
    static final int[] W = {11, 10, 9, 8, 7, 6, 6, 5, 4, 3};
    static final float[] REST = {-8, 10, 14, 16, 18, 20, 22, 22, 24, 26};

    static void tentacle(Part parent, String pre, int n, int[] len, int[] w, float[] rest, boolean thorns) {
        Part p = parent;
        for (int i = 0; i < n; i++) {
            Part s = p.add(pre + i).at(0, i == 0 ? 0 : len[i - 1], 0).rot(rest[i], 0, 0);
            int ww = w[i];
            s.cbox(0, ww, len[i] + (i < n - 1 ? 1 : 0), ww).color(TEAL).grad(TEAL_L, TEAL_D).bevel(0.25f).noise(0.08f)
                .band(0, 1, TEAL_D).paint(Face.FRONT, 1, 1, Math.max(1, ww - 2), 1, TEAL_L);
            if (thorns && ww >= 4) {
                // thorns on the outer (back) side and flanks
                s.box(-0.5f, len[i] * 0.4f, -ww / 2f - 2, 1, 2, 2).color(THORN).flat();
                if (i % 2 == 0) {
                    s.box(ww / 2f, len[i] * 0.3f, -0.5f, 2, 1, 1).color(THORN).flat();
                    s.box(-ww / 2f - 2, len[i] * 0.6f, -0.5f, 2, 1, 1).color(THORN).flat();
                }
            }
            if (i == n - 1) s.cbox(len[i], Math.max(1, ww - 2), 2, Math.max(1, ww - 2)).color(TEAL_L).bevel(0.2f); // rounded tip
            p = s;
        }
    }

    /** key every segment of the big tentacle: s0 = base, others = per */
    static void curl(Clip c, int t, float base, float per, float twist, Ease e) {
        c.rot("s0", t, base, twist, 0, e);
        for (int i = 1; i < N; i++) c.rot("s" + i, t, per, 0, i < 3 ? twist * 0.3f : 0, e);
    }

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("spire_growth");
        // ---- ground mound with roots
        Part base = m.add("base").at(0, 0, -2);
        base.cbox(0, 16, 3, 14).color(TEAL_D).grad(TEAL, DIRT).bevel(0.3f).noise(0.12f);
        base.cbox(2, 13, 2, 11).color(TEAL).grad(TEAL_L, TEAL_D).bevel(0.2f);
        base.box(-11, 0, -3, 4, 2, 5).color(TEAL_D).bevel(0.3f);
        base.box(7, 0, 2, 5, 2, 4).color(TEAL_D).bevel(0.3f);
        base.box(-3, 0, 6, 6, 1, 4).color(TEAL_D).bevel(0.3f);
        // ---- main arching tentacle
        Part root = base.add("trunk").at(0, 3, 0);
        tentacle(root, "s", N, LEN, W, REST, true);
        // ---- two small curling side tentacles
        Part l = base.add("sideL").at(9, 1, 4).rot(0, 40, -18);
        tentacle(l, "a", 5, new int[]{4, 4, 3, 3, 3}, new int[]{4, 3, 3, 2, 2}, new float[]{0, -20, -30, -40, -45}, true);
        Part r = base.add("sideR").at(-10, 1, -5).rot(0, -30, 14);
        tentacle(r, "b", 5, new int[]{4, 4, 3, 3, 3}, new int[]{4, 3, 3, 2, 2}, new float[]{0, -18, -28, -38, -45}, true);

        // ---- procedural slow writhing wave (small, always on)
        m.proc((rig, c) -> {
            float a = c.age;
            for (int i = 0; i < N; i++) rig.rot("s" + i, (float) Math.sin(a * 0.09f - i * 0.6f) * 2.2f, 0, (float) Math.sin(a * 0.06f - i * 0.5f) * 2.5f);
            for (int i = 0; i < 5; i++) {
                rig.rot("a" + i, (float) Math.sin(a * 0.13f - i * 0.8f) * 7, 0, 0);
                rig.rot("b" + i, (float) Math.sin(a * 0.11f - i * 0.8f + 2) * 7, 0, 0);
            }
            if (c.swingAmt > 0.05f) rig.rot("base", 0, (float) Math.sin(c.swing * 0.5f) * 6 * c.swingAmt, (float) Math.cos(c.swing * 0.5f) * 3 * c.swingAmt);
        });

        Clip idle = m.clip("idle", 60).loop();
        curl(idle, 0, 0, 0, 0, Ease.SMOOTH); curl(idle, 30, -3, 2, 4, Ease.SMOOTH); curl(idle, 60, 0, 0, 0, Ease.SMOOTH);
        idle.scl("base", 0, 1, 1, 1).scl("base", 30, 1.03f, 0.97f, 1.03f).scl("base", 60, 1, 1, 1);

        Clip walk = m.clip("walk", 20).loop();
        walk.pos("base", 0, 0, 0, 0).pos("base", 10, 0, 0.6f, 0).pos("base", 20, 0, 0, 0);
        curl(walk, 0, 4, -1, 0, Ease.SMOOTH); curl(walk, 10, -2, 1, 0, Ease.SMOOTH); curl(walk, 20, 4, -1, 0, Ease.SMOOTH);

        // QUICK TACKLE: snap the tentacle back, whip forward like a lash, settle
        Clip qt = m.clip("quick_tackle", 26);
        curl(qt, 0, 0, 0, 0, Ease.SMOOTH);
        curl(qt, 8, -32, -9, -10, Ease.OUT_CUBIC);
        curl(qt, 12, 36, 3, 8, Ease.IN_CUBIC);
        curl(qt, 15, 38, 6, 6, Ease.OUT_QUAD);
        curl(qt, 26, 0, 0, 0, Ease.SMOOTH);
        qt.scl("base", 0, 1, 1, 1).scl("base", 8, 0.92f, 1.08f, 0.92f).scl("base", 12, 1.12f, 0.88f, 1.12f, Ease.IN_CUBIC).scl("base", 26, 1, 1, 1)
          .pos("base", 0, 0, 0, 0).pos("base", 8, 0, 0, -2).pos("base", 12, 0, 0, 4, Ease.IN_CUBIC).pos("base", 26, 0, 0, 0);

        // SMASH: rear up straight and tall, tremble, slam the whole arch into the ground
        Clip sm = m.clip("smash", 36);
        curl(sm, 0, 0, 0, 0, Ease.SMOOTH);
        curl(sm, 12, -18, -15, 0, Ease.OUT_BACK);
        curl(sm, 15, -20, -16, 3, Ease.SMOOTH);
        curl(sm, 17, -18, -15, -3, Ease.SMOOTH);
        curl(sm, 20, 52, 0, 0, Ease.IN_CUBIC);
        curl(sm, 24, 48, -1, 0, Ease.OUT_BOUNCE);
        curl(sm, 36, 0, 0, 0, Ease.SMOOTH);
        sm.scl("s0", 0, 1, 1, 1).scl("s0", 12, 0.9f, 1.25f, 0.9f).scl("s0", 20, 1.15f, 0.9f, 1.15f, Ease.IN_CUBIC).scl("s0", 36, 1, 1, 1)
          .scl("base", 0, 1, 1, 1).scl("base", 20, 1, 1, 1).scl("base", 22, 1.2f, 0.75f, 1.2f).scl("base", 36, 1, 1, 1)
          .rot("sideL", 0, 0, 0, 0).rot("sideL", 20, 0, 0, 0).rot("sideL", 22, -30, 0, -20).rot("sideL", 36, 0, 0, 0)
          .rot("sideR", 0, 0, 0, 0).rot("sideR", 20, 0, 0, 0).rot("sideR", 22, -30, 0, 20).rot("sideR", 36, 0, 0, 0);

        // CONSTRICT: reach out low and straight, then coil the tip tightly around the victim and squeeze
        Clip co = m.clip("constrict", 40);
        curl(co, 0, 0, 0, 0, Ease.SMOOTH);
        curl(co, 10, 36, -7, 0, Ease.OUT_CUBIC);
        co.rot("s0", 16, 30, 0, 0, Ease.SMOOTH);
        for (int i = 1; i < N; i++) co.rot("s" + i, 16, i < 4 ? -4 : 26, 0, 0, Ease.OUT_BACK);
        for (int k = 0; k < 3; k++) {
            int t = 20 + k * 5;
            for (int i = 4; i < N; i++) co.rot("s" + i, t, 32, 0, 0, Ease.IN_QUAD).rot("s" + i, t + 3, 24, 0, 0);
            co.scl("s5", t, 0.85f, 1, 0.85f).scl("s5", t + 3, 1, 1, 1);
        }
        curl(co, 40, 0, 0, 0, Ease.SMOOTH);
        co.scl("s5", 0, 1, 1, 1).scl("s5", 40, 1, 1, 1);

        Clip hurt = m.clip("hurt", 10);
        hurt.rot("s0", 0, 0, 0, 0).rot("s0", 3, -12, 0, 6).rot("s0", 10, 0, 0, 0)
            .rot("s4", 0, 0, 0, 0).rot("s4", 3, -10, 0, 0).rot("s4", 10, 0, 0, 0);

        Clip death = m.clip("death", 40);
        curl(death, 0, 0, 0, 0, Ease.SMOOTH);
        curl(death, 10, -15, -6, 10, Ease.OUT_QUAD);
        curl(death, 30, 70, -6, 0, Ease.OUT_BOUNCE);
        curl(death, 40, 74, -8, 0, Ease.SMOOTH);
        death.scl("base", 0, 1, 1, 1).scl("base", 30, 1.1f, 0.6f, 1.1f).scl("base", 40, 1.1f, 0.4f, 1.1f)
             .rot("sideL", 0, 0, 0, 0).rot("sideL", 30, 60, 0, -50).rot("sideR", 0, 0, 0, 0).rot("sideR", 30, 60, 0, 50);
        return m;
    }

    static boolean constricted(SpireMob s) { return s.count("constrict_id") != 0; }

    public static MobDef def() {
        return MobDef.of("spire_growth", "Spire Growth", "スパイアグロウス")
            .game(1, 3).size(1.6f, 2.6f).hp(170).speed(0.16f).kbResist(0.7f).style(Style.SLIME).egg(0x67b0a2, 0x2f5f57)
            .model(SpireGrowth::model)
            .spawn(3, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(s -> {
                boolean twoTackles = s.lastMove().equals("quick_tackle") && s.moveAgo(1).equals("quick_tackle");
                boolean twoSmash = s.lastMove().equals("smash") && s.moveAgo(1).equals("smash");
                if (s.rand().nextInt(100) < 50 && !twoTackles) return "quick_tackle";
                if (!constricted(s) && !s.lastMove().equals("constrict")) return "constrict";
                return twoSmash ? "quick_tackle" : "smash";
            })
            .move(Move.of("quick_tackle", 26).intent(Intent.ATTACK, 16, 1).reach(3.0f)
                .sound(8, SoundEvents.SLIME_JUMP, 1f, 0.6f)
                .at(12, Fx.melee(16, 3.6, 80)))
            .move(Move.of("smash", 36).intent(Intent.ATTACK, 22, 1).reach(3.2f)
                .sound(10, SoundEvents.SLIME_SQUISH, 1.2f, 0.5f)
                .at(20, (s, t) -> {
                    Fx.meleeNow(s, 22, 4.0, 90, null);
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.7f, 1.4f);
                    Fx.particlesAt(s, ParticleTypes.CLOUD, Fx.rel(s, 2.2, 0, 0.2), 16, 0.6, 0.05);
                }))
            .move(Move.of("constrict", 40).intent(Intent.STRONG_DEBUFF).reach(3.4f)
                .sound(14, SoundEvents.SLIME_ATTACK, 1f, 0.5f)
                .at(16, (s, t) -> {
                    if (t == null || t.distanceTo(s) > 5.5) return;
                    s.setCount("constrict_id", t.getId());
                    Fx.slow(t, 2, 2);
                    Fx.particlesAt(s, ParticleTypes.ITEM_SLIME, t.position().add(0, 1, 0), 20, 0.4, 0.05);
                }))
            // Constricted 10: deals 10 damage at the end of each "turn" (every 3 s) until the Spire Growth dies
            .onTick(s -> {
                int id = s.count("constrict_id");
                if (id == 0 || s.tickCount % 60 != 0) return;
                Entity e = s.level().getEntity(id);
                if (!(e instanceof LivingEntity v) || !v.isAlive() || v.distanceTo(s) > 48) { s.setCount("constrict_id", 0); return; }
                v.invulnerableTime = 0;
                Fx.raw(s, v, 10 * Cfg.DMG);
                Fx.particlesAt(s, ParticleTypes.ITEM_SLIME, v.position().add(0, 1, 0), 10, 0.3, 0.02);
            });
    }
}
