package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth: Fuzzy Wurm Crawler 55-57 HP. Acid Goop (4) -> Inhale (+7 Str) -> Acid Goop (4) cycle. */
public final class FuzzyWurm {
    private FuzzyWurm() {}

    // ================================================================= FUZZY WURM CRAWLER
    static final int FUR = 0xc4893c, FUR_L = 0xe8b868, FUR_D = 0x8a5a24, SKINW = 0xd8b58a, GOOP = 0x9fd13c, TEETH = 0xf4efd8;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_fuzzy_wurm_crawler").scale(0.75f);
        // 4 body segments chained back-to-front so the whole thing undulates; origin = middle of the worm
        Part seg1 = m.add("seg1").at(0, 6, 4);
        fuzzSeg(seg1, 12, 0);
        Part seg2 = seg1.add("seg2").at(0, 0, -9);
        fuzzSeg(seg2, 11, 1);
        Part seg3 = seg2.add("seg3").at(0, 0, -8);
        fuzzSeg(seg3, 10, 2);
        Part seg4 = seg3.add("seg4").at(0, 0, -7);
        fuzzSeg(seg4, 8, 3);
        Part tip = seg4.add("tip").at(0, 0, -6);
        tip.box(-2, -2, -5, 4, 4, 5).color(FUR_D).grad(FUR, FUR_D).noise(0.2f);
        // head at the front of seg1
        Part head = seg1.add("head").at(0, 0, 6);
        head.box(-5, -4, 0, 10, 9, 8).color(FUR).grad(FUR_L, FUR_D).noise(0.2f).bevel(0.1f)
            .glow(Face.FRONT, 1, 1, 2, 2, 0xfff070).glow(Face.FRONT, 7, 1, 2, 2, 0xfff070).paint(Face.FRONT, 2, 2, 1, 1, 0x201000).paint(Face.FRONT, 7, 2, 1, 1, 0x201000);
        head.box(-6, 4, 1, 12, 3, 7).color(FUR_L).noise(0.25f).inflate(0.3f);                // shaggy fringe
        head.box(-4, -3, 8, 8, 5, 1).color(0x3a1a18).flat();                                 // mouth cavity
        Part jaw = head.add("jaw").at(0, -3, 8);
        jaw.box(-4, -2, -1, 8, 2, 4).color(SKINW).grad(SKINW, FUR_D)
            .paint(Face.FRONT, 0, 0, 1, 1, TEETH).paint(Face.FRONT, 3, 0, 2, 1, TEETH).paint(Face.FRONT, 7, 0, 1, 1, TEETH);
        for (int s = -1; s <= 1; s += 2) {
            Part ant = head.add(s < 0 ? "antR" : "antL").at(s * 3, 5, 4).rot(-30, 0, s * -20);
            ant.box(-0.5f, 0, -0.5f, 1, 5, 1).color(FUR_D);
            ant.box(-1, 5, -1, 2, 2, 2).color(GOOP).glow(Face.FRONT, 0, 0, 2, 2, GOOP);
        }
        // little leg nubs per segment
        for (int i = 1; i <= 3; i++) for (int s = -1; s <= 1; s += 2)
            m.byName.get("seg" + i).add("nub" + i + (s < 0 ? "R" : "L")).at(s * 5, -5, 0).box(-1, -3, -1.5f, 2, 3, 3).color(SKINW).noise(0.1f);
        m.look("head", 40, 25);
        m.proc((r, c) -> {
            float w = c.age * 0.12f, a = 1f + c.swingAmt * 2f;
            r.rot("seg1", 0, (float) Math.sin(w) * 6 * a, 0); r.rot("seg2", 0, (float) Math.sin(w - 0.9f) * 12 * a, 0);
            r.rot("seg3", 0, (float) Math.sin(w - 1.8f) * 14 * a, 0); r.rot("seg4", 0, (float) Math.sin(w - 2.7f) * 16 * a, 0);
            r.rot("tip", 0, (float) Math.sin(w - 3.6f) * 20 * a, 0);
        });
        Clip c = m.clip("idle", 48).loop();
        Kit.R(c, "head", 0, 0, 0, 0, 16, -6, 8, 0, 32, 4, -8, 0, 48, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 36, 0, 0, 0, 40, 14, 0, 0, 44, 0, 0, 0);
        Kit.S(c, "seg2", 0, 1, 1, 1, 24, 1.05f, 1.06f, 1.05f, 48, 1, 1, 1);
        m.clip("walk", 16).loop();
        // ACID GOOP: rears head back, hacks up a glob with a big heave
        c = m.clip("acid_goop", 32);
        Kit.R(c, "head", 0, 0, 0, 0, 10, -30, 0, 0, -14, 28, 0, 0, 22, 12, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 10, 40, 0, 0, -14, 55, 0, 0, 24, 15, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "seg1", 0, 0, 0, 0, 10, -22, 0, 0, -14, 18, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "seg2", 0, 0, 0, 0, 10, 14, 0, 0, -14, -8, 0, 0, 32, 0, 0, 0);
        Kit.S(c, "head", 0, 1, 1, 1, 10, 1.15f, 1.15f, 1.15f, 14, 0.95f, 0.95f, 0.95f, 32, 1, 1, 1);
        Kit.P(c, "seg1", 0, 0, 0, 0, 10, 0, 0, -2, -14, 0, 0, 3, 32, 0, 0, 0);
        // INHALE: whole body balloons, fur bristles, head lifts and gasps in air
        c = m.clip("inhale", 44);
        Kit.R(c, "head", 0, 0, 0, 0, 12, -35, 0, 0, 34, -35, 0, 0, 44, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 12, 50, 0, 0, 34, 50, 0, 0, 38, 0, 0, 0, 44, 0, 0, 0);
        Kit.S(c, "seg1", 0, 1, 1, 1, 14, 1.2f, 1.25f, 1.2f, 34, 1.35f, 1.4f, 1.35f, 1038, 1, 1, 1, 44, 1, 1, 1);
        Kit.S(c, "seg2", 0, 1, 1, 1, 20, 1.2f, 1.25f, 1.2f, 34, 1.35f, 1.4f, 1.35f, 1038, 1, 1, 1, 44, 1, 1, 1);
        Kit.S(c, "seg3", 0, 1, 1, 1, 26, 1.2f, 1.25f, 1.2f, 34, 1.3f, 1.35f, 1.3f, 1038, 1, 1, 1, 44, 1, 1, 1);
        Kit.S(c, "head", 0, 1, 1, 1, 14, 1.15f, 1.15f, 1.15f, 34, 1.3f, 1.3f, 1.3f, 1038, 1, 1, 1, 44, 1, 1, 1);
        Kit.R(c, "seg1", 0, 0, 0, 0, 12, -14, 0, 0, 34, -14, 0, 0, 44, 0, 0, 0);
        Kit.hurt(m, "head", -22, 10);
        c = m.clip("death", 32);
        Kit.R(c, "head", 0, 0, 0, 0, 10, -25, 0, 0, 32, 25, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 10, 40, 0, 0, 32, 20, 0, 0);
        Kit.S(c, "seg1", 0, 1, 1, 1, 14, 1.1f, 0.8f, 1.1f, 32, 1.3f, 0.35f, 1.3f);
        Kit.S(c, "seg2", 0, 1, 1, 1, 14, 1.1f, 0.8f, 1.1f, 32, 1.3f, 0.35f, 1.3f);
        Kit.S(c, "seg3", 0, 1, 1, 1, 14, 1.1f, 0.8f, 1.1f, 32, 1.3f, 0.35f, 1.3f);
        Kit.S(c, "seg4", 0, 1, 1, 1, 14, 1.1f, 0.8f, 1.1f, 32, 1.3f, 0.35f, 1.3f);
        Kit.P(c, "seg1", 0, 0, 0, 0, 32, 0, -3, 0);
        return m;
    }

    private static void fuzzSeg(Part seg, int w, int i) {
        seg.box(-w / 2f, -5, -5, w, 10, 10).color(FUR).grad(FUR_L, FUR_D).noise(0.22f).bevel(0.1f);
        seg.box(-w / 2f - 0.6f, -1, -5.6f, w + 1, 5, 4).color(FUR_L).noise(0.3f).inflate(0.2f);      // fuzzy tuft ring
        seg.box(-w / 2f - 0.5f, -4.5f, 0.5f, w + 1, 4, 4).color(FUR_D).noise(0.3f).inflate(0.2f);
        seg.box(-1, 5, -4, 2, 2, 8).color(FUR_L).noise(0.3f);                                         // dorsal bristle ridge
        seg.box(-w / 2f, -4, -3, w, 3, 6).color(i % 2 == 0 ? 0xa8702e : 0xd89c4c).noise(0.2f).inflate(0.1f);
    }

    public static MobDef def() {
        if (ProjKind.of("s2_wurm_goop") == null) ProjKind.orb("s2_wurm_goop", 0x9fd13c, 3).gravity(0.03).trail(ParticleTypes.ITEM_SLIME);
        return MobDef.of("s2_fuzzy_wurm_crawler", "Fuzzy Wurm Crawler", "剛毛のワーム")
            .game(2, 1).size(1.0f, 0.8f).hp(56).speed(0.2f).style(Style.INSECT).egg(0xc4893c, 0x9fd13c)
            .model(FuzzyWurm::model)
            .spawn(7, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("acid_goop", "inhale", "acid_goop"))
            .move(Move.of("acid_goop", 32).intent(Intent.ATTACK, 4, 1).ranged(7)
                .sound(12, SoundEvents.LLAMA_SPIT, 1.0f, 0.7f).at(15, Fx.shoot("s2_wurm_goop", 4, 0.9f, 0, 0.7, 1.0, null)))
            .move(Move.of("inhale", 44).intent(Intent.BUFF).noTarget()
                .sound(10, SoundEvents.WARDEN_SNIFF, 1.4f, 0.8f).at(34, (s, t) -> Kit.str(s, 7)));
    }
}
