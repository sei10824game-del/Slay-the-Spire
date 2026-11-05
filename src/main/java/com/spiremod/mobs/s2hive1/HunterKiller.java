package com.spiremod.mobs.s2hive1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Hunter Killer (121 HP): opens Tenderizing Goop (Tender 1), then Puncture (7x3, weight 2) or Bite (17, weight 1, never twice in a row). */
public final class HunterKiller {
    private HunterKiller() {}
    static final int CH = 0x3a2a4a, CH_D = 0x1c1428, CH_L = 0x6a4a80, STRIPE = 0xe08828, EYE = 0xffe040, GOOP = 0xa8d848;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_hunter_killer");
        Part body = m.add("body").at(0, 11, 0);
        body.box(-4, -3, -4, 8, 8, 9).color(CH).grad(CH_L, CH_D).bevel(0.2f).noise(0.1f);                               // thorax
        body.box(-4.4f, 4, -4, 9, 1, 8).color(STRIPE).flat().inflate(0.05f);
        Part abd = body.add("abdomen").at(0, 0, -4).rot(18, 0, 0);
        abd.box(-3.5f, -3.5f, -14, 7, 7, 14).color(CH).grad(CH_L, CH_D).bevel(0.2f).noise(0.1f)
            .band(1, 1, STRIPE).paint(Face.TOP, 0, 2, 7, 1, STRIPE).paint(Face.TOP, 0, 6, 7, 1, STRIPE).paint(Face.TOP, 0, 10, 7, 1, STRIPE);
        Part sting = abd.add("stinger").at(0, 0, -14).rot(-30, 0, 0);
        sting.box(-1.5f, -1.5f, -4, 3, 3, 4).color(CH_D).bevel(0.15f);
        sting.box(-0.5f, -0.5f, -9, 1, 1, 5).color(0xd8d0e0).flat();
        Part head = body.add("head").at(0, 2, 5);
        head.box(-3, -3, 0, 6, 6, 5).color(CH).grad(CH_L, CH_D).bevel(0.2f);
        for (int s = -1; s <= 1; s += 2) {
            head.box(s < 0 ? -4.4f : 2.2f, -1, 0.5f, 2, 4, 3).color(0x201818).flat().glow(Face.FRONT, 0, 0, 2, 4, EYE).glow(s < 0 ? Face.RIGHT : Face.LEFT, 0, 0, 3, 4, EYE);
        }
        Part prob = head.add("proboscis").at(0, -1.5f, 5);
        prob.box(-1, -1, 0, 2, 2, 8).color(CH_D).grad(CH_L, CH_D).bevel(0.15f);
        prob.box(-0.5f, -0.5f, 8, 1, 1, 3).color(0xd8d0e0).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part mnd = head.add(s < 0 ? "mandR" : "mandL").at(s * 2, -3, 4.5f);
            mnd.box(-0.7f, -0.7f, 0, 2, 2, 5).color(STRIPE).bevel(0.2f);
            // raptor forelimbs: upper arm + hooked blade forearm
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 4.5f, 3, 3);
            arm.box(-1, -1, -1, 2, 2, 7).color(CH).bevel(0.2f);
            Part fa = arm.add(s < 0 ? "foreR" : "foreL").at(0, 0, 6);
            fa.box(-1, -1, -1, 2, 2, 8).color(CH_D).bevel(0.2f);
            fa.box(-0.5f, -3, 2, 1, 4, 5).color(0xe0d8e8).flat();
            fa.box(-0.5f, -4, 5, 1, 2, 3).color(0xf8f4ff).flat();
        }
        Kit.legs(body, "leg", 4, -1.5f, new float[]{1.5f, -3}, 5, 10, 30, 14, CH, CH_D);
        Kit.scuttle(m, "leg", 2, 24);
        m.look("head", 40, 25);
        Clip i = m.clip("idle", 40).loop();
        Kit.R(i, "body", 0, 0, 0, 0, 0, 20, -3, 0, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(i, "abdomen", 0, 0, 0, 0, 0, 20, 6, 0, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(i, "foreR", 0, 0, 0, 0, 0, 20, -14, 0, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(i, "foreL", 0, 0, 0, 0, 0, 20, -14, 0, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(i, "mandR", 0, 0, 0, 0, 0, 24, 0, -18, 0, 0, 28, 0, 0, 0, 0);
        Kit.R(i, "mandL", 0, 0, 0, 0, 0, 24, 0, 18, 0, 0, 28, 0, 0, 0, 0);
        // TENDERIZING GOOP: arch up, abdomen pumps, spit a glob
        Clip g = m.clip("tenderizing_goop", 38);
        Kit.R(g, "body", 8, -22, 0, 0, 0, 20, -26, 0, 0, 0, 24, 14, 0, 0, 1, 38, 0, 0, 0, 0);
        Kit.R(g, "head", 8, -20, 0, 0, 0, 20, -30, 0, 0, 0, 24, 25, 0, 0, 1, 38, 0, 0, 0, 0);
        Kit.R(g, "abdomen", 8, 30, 0, 0, 0, 20, 45, 0, 0, 0, 24, 5, 0, 0, 1, 38, 0, 0, 0, 0);
        Kit.S(g, "abdomen", 12, 1, 1, 1, 0, 20, 1.3f, 1.3f, 1.3f, 0, 24, 0.9f, 0.9f, 0.9f, 1, 38, 1, 1, 1, 0);
        Kit.S(g, "proboscis", 22, 1, 1, 1, 0, 25, 1, 1, 1.5f, 1, 34, 1, 1, 1, 0);
        // BITE: rear back, mandibles spread, lunge and clamp
        Clip b = m.clip("bite", 28);
        Kit.R(b, "body", 8, -18, 0, 0, 0, 12, 16, 0, 0, 1, 28, 0, 0, 0, 0);
        Kit.R(b, "head", 8, -18, 0, 0, 0, 12, 22, 0, 0, 1, 28, 0, 0, 0, 0);
        Kit.P(b, "body", 8, 0, 1, -3, 0, 12, 0, -1, 7, 1, 20, 0, 0, 3, 0, 28, 0, 0, 0, 0);
        Kit.R(b, "mandR", 8, 0, -50, 0, 0, 12, 0, 30, 0, 1, 28, 0, 0, 0, 0);
        Kit.R(b, "mandL", 8, 0, 50, 0, 0, 12, 0, -30, 0, 1, 28, 0, 0, 0, 0);
        Kit.R(b, "foreR", 8, -50, 0, 0, 0, 12, 20, 0, 0, 1, 28, 0, 0, 0, 0);
        Kit.R(b, "foreL", 8, -50, 0, 0, 0, 12, 20, 0, 0, 1, 28, 0, 0, 0, 0);
        // PUNCTURE: raise up on hind legs, three needle stabs (arms + proboscis alternate)
        Clip p = m.clip("puncture", 36);
        Kit.R(p, "body", 6, -26, 0, 0, 0, 28, -26, 0, 0, 0, 36, 0, 0, 0, 0);
        Kit.P(p, "body", 6, 0, 2, -3, 0, 28, 0, 2, -3, 0, 36, 0, 0, 0, 0);
        Kit.R(p, "head", 6, 10, 0, 0, 0, 10, -8, 0, 0, 1, 16, 10, 0, 0, 1, 20, -8, 0, 0, 1, 26, 10, 0, 0, 1, 30, -8, 0, 0, 1, 36, 0, 0, 0, 0);
        Kit.R(p, "armR", 6, -50, 0, 20, 0, 10, 40, 0, 0, 1, 16, -50, 0, 20, 0, 28, -50, 0, 20, 0, 30, 40, 0, 0, 1, 36, 0, 0, 0, 0);
        Kit.R(p, "armL", 6, -50, 0, -20, 0, 13, -50, 0, -20, 0, 16, 40, 0, 0, 1, 20, -50, 0, -20, 0, 36, 0, 0, 0, 0);
        Kit.S(p, "proboscis", 10, 1, 1, 1, 0, 11, 1, 1, 1.7f, 1, 14, 1, 1, 1, 0, 26, 1, 1, 1, 0, 27, 1, 1, 1.7f, 1, 32, 1, 1, 1, 0);
        Kit.hurt(m, "body", "head");
        Clip d = m.clip("death", 30);
        Kit.R(d, "body", 8, 0, 0, 30, 0, 22, 0, 0, 180, 3, 30, 0, 0, 180, 0);
        Kit.P(d, "body", 10, 0, 2, 0, 0, 22, 0, 4, 0, 0, 30, 0, 4, 0, 0);
        Kit.R(d, "foreR", 14, 0, 0, 0, 0, 30, -80, 0, 0, 0);
        Kit.R(d, "foreL", 14, 0, 0, 0, 0, 30, -80, 0, 0, 0);
        Kit.curlLegs(d, "leg", 2, 0, 20);
        return m;
    }

    public static MobDef def() {
        ProjKind.orb("s2_hunter_goop", GOOP, 3).trail(ParticleTypes.ITEM_SLIME);
        return MobDef.of("s2_hunter_killer", "Hunter Killer", "ハンターキラー")
            .game(2, 2).size(1.0f, 1.8f).hp(121).speed(0.28f).style(Style.INSECT).egg(CH, STRIPE)
            .model(HunterKiller::model).spawn(4, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.first("tenderizing_goop", Pattern.random().add("bite", 1).add("puncture", 2).maxRepeat("bite", 1)))
            .move(Move.of("tenderizing_goop", 38).intent(Intent.DEBUFF).ranged(10)
                .sound(24, SoundEvents.SLIME_SQUISH, 1.2f, 0.6f)
                .at(25, Fx.shoot("s2_hunter_goop", 0, 1.0f, 0, 1.3, 0.6, v -> { Fx.weak(v, 10); Fx.slow(v, 6, 0); })))
            .move(Move.of("bite", 28).intent(Intent.ATTACK, 17, 1).reach(2.6f)
                .sound(12, SoundEvents.SPIDER_HURT, 1f, 0.6f).at(13, Fx.melee(17, 2.8, 90)))
            .move(Move.of("puncture", 36).intent(Intent.ATTACK, 7, 3).reach(2.8f)
                .sound(10, SoundEvents.TRIDENT_THROW, 0.8f, 1.6f).sound(16, SoundEvents.TRIDENT_THROW, 0.8f, 1.6f).sound(27, SoundEvents.TRIDENT_THROW, 0.8f, 1.6f)
                .at(11, Fx.melee(7, 3.2, 70)).at(17, Fx.melee(7, 3.2, 70)).at(27, Fx.melee(7, 3.2, 70)));
    }
}
