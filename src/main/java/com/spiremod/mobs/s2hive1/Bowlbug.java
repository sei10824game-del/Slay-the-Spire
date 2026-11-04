package com.spiremod.mobs.s2hive1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Hive Bowlbugs: a round two-lobed beetle carrying its cargo in a bowl on its back.
 * ROCK 45 HP Headbutt 15 / Dizzy (Imbalanced: stunned if Headbutt is fully blocked).
 * EGG 21 HP Bite 7 + 7 Block every turn.  SILK 40 HP Spin Web (Weak 1) <-> Thrash 4x2.
 * NECTAR 35 HP Thrash 3, then Buff (+15 Strength) once, then Thrash forever.
 * Variant classes BowlbugRock/Egg/Silk/Nectar just call model(kind)/def(kind).
 */
final class Bowlbug {
    private Bowlbug() {}
    static final int ROCK = 0, EGG = 1, SILK = 2, NECTAR = 3;
    // shell colours per kind: main, dark, light
    static final int[][] SHELL = { {0xd8504a, 0x9a3030, 0xee7a6a}, {0x9ab048, 0x6a8030, 0xc0d070}, {0xc89a98, 0x9a7070, 0xe0bcb8}, {0xc458b0, 0x8a3a80, 0xe080d0} };
    static final int EYE = 0x7ff0e0, PUPIL = 0x10303a, SOCKET = 0xd09090, MAND = 0xf0b030, MAND_D = 0xb87a18, LEG = 0x8a3a3a;

    static ModelSpec model(int kind) {
        int[] c = SHELL[kind];
        ModelSpec m = new ModelSpec("s2_bowlbug_" + new String[]{"rock", "egg", "silk", "nectar"}[kind]);
        Part body = m.add("body").at(0, 7, 0);
        // rear bowl carrying the cargo
        body.box(-5, -3, -7, 10, 6, 9).color(c[0]).grad(c[2], c[1]).bevel(0.15f).paint(Face.TOP, 1, 1, 8, 7, c[1]);
        body.box(-5.5f, 2, -7.5f, 11, 1, 10).color(c[2]).flat().inflate(0.1f).paint(Face.TOP, 1, 1, 9, 8, 0x3a2020); // rim
        body.box(-4, -4, -6, 8, 1, 7).color(c[1]); // belly
        // two big frontal shell lobes (the "head")
        Part head = body.add("head").at(0, -1, 2);
        for (int s = -1; s <= 1; s += 2) {
            head.box(s < 0 ? -5.5f : 0.2f, -2.5f, 0, 5, 10, 7).color(c[0]).grad(c[2], c[1]).bevel(0.25f).noise(0.06f)
                .paint(Face.FRONT, s < 0 ? 4 : 0, 0, 1, 10, c[1]).paint(Face.TOP, 1, 1, 3, 2, c[2]);
        }
        head.box(-3, -3, 1, 6, 3, 6).color(c[1]); // underside
        for (int s = -1; s <= 1; s += 2) {
            Part eye = head.add(s < 0 ? "eyeR" : "eyeL").at(s * 4.2f, 0.5f, 5.5f);
            eye.box(-2, -2, -1, 4, 4, 2).color(SOCKET).bevel(0.2f);
            eye.box(-1.5f, -1.5f, 0.6f, 3, 3, 1).color(EYE).flat().glow(Face.FRONT, 1, 1, 1, 1, PUPIL).paint(Face.FRONT, 0, 0, 1, 1, 0xd0fff8);
            Part mand = head.add(s < 0 ? "mandR" : "mandL").at(s * 2.2f, -2.5f, 6.5f);
            mand.box(-1, -1, 0, 2, 2, 3).color(MAND).grad(MAND, MAND_D).bevel(0.2f);
            Part tip = mand.add(s < 0 ? "mandRt" : "mandLt").at(0, -0.5f, 3).rot(0, s * 60, 0);
            tip.box(-0.8f, -1, 0, 2, 2, 3).color(MAND).grad(MAND, MAND_D);
            tip.box(s < 0 ? 0.5f : -1.5f, -1, 2, 1, 1, 1).color(MAND_D);
        }
        // cargo
        Part cargo = body.add("cargo").at(0, 2.5f, -2.5f);
        switch (kind) {
            case ROCK -> {
                cargo.box(-5, 0, -4.5f, 10, 7, 9).color(0x7a7458).grad(0x8a8466, 0x5a5440).noise(0.12f).bevel(0.2f)
                    .paint(Face.FRONT, 0, 0, 3, 7, 0x6a644a).paint(Face.LEFT, 2, 1, 4, 3, 0x8e886a);
                Part top = cargo.add("rockTop").at(0, 7, 0).rot(0, 25, 8);
                top.box(-3.5f, 0, -3.5f, 7, 3, 7).color(0x847e60).noise(0.12f).bevel(0.2f);
                top.box(-2, 3, -2, 4, 1, 3).color(0x948e70).noise(0.1f);
            }
            case EGG -> {
                cargo.box(-4, 0, -4, 8, 6, 8).color(0x58c0b0).grad(0x7ad8c8, 0x3a9a8a).bevel(0.3f)
                    .paint(Face.LEFT, 1, 1, 2, 2, 0x2a7a70).paint(Face.RIGHT, 4, 2, 2, 2, 0x2a7a70).paint(Face.FRONT, 2, 3, 2, 1, 0x2a7a70).paint(Face.TOP, 5, 2, 2, 2, 0x2a7a70);
                Part top = cargo.add("eggTop").at(0, 6, 0);
                top.box(-3, 0, -3, 6, 2, 6).color(0x6ad0c0).bevel(0.3f).paint(Face.TOP, 1, 1, 2, 2, 0x2a7a70).paint(Face.TOP, 3, 3, 1, 1, 0xb0fff0);
            }
            case SILK -> {
                cargo.box(-5, 0, -5, 10, 9, 10).color(0xf4f4f6).grad(0xffffff, 0xd8d8e0).noise(0.05f).bevel(0.3f)
                    .band(2, 1, 0xd0d0dc).band(5, 1, 0xdadae4).paint(Face.FRONT, 3, 0, 1, 9, 0xd0d0dc).paint(Face.LEFT, 6, 0, 1, 9, 0xd0d0dc).paint(Face.TOP, 0, 4, 10, 1, 0xd8d8e2);
                Part strand = cargo.add("strand").at(0, 9, 0);
                strand.box(-3, 0, -3, 6, 1, 6).color(0xffffff).noise(0.04f);
            }
            default -> {
                cargo.box(-5, 0, -5, 10, 6, 10).color(0xf0a020).grad(0xffc040, 0xe08010).flat().glow(Face.TOP, 2, 2, 6, 6, 0xffb830)
                    .paint(Face.FRONT, 1, 1, 2, 1, 0xfff0a0).paint(Face.LEFT, 2, 1, 2, 1, 0xfff0a0);
                Part top = cargo.add("blob").at(0, 6, 0);
                top.box(-4, 0, -4, 8, 2, 8).color(0xf8b030).flat().glow(Face.TOP, 2, 2, 3, 2, 0xffe080).paint(Face.FRONT, 1, 0, 3, 1, 0xfff0b0);
                top.box(-2, 2, -1, 3, 1, 3).color(0xffc040).flat();
            }
        }
        Kit.legs(body, "leg", 4.5f, -2.5f, new float[]{1, -2.5f, -6}, 3, 7, 25, 15, c[1], 0x3a1a1a);
        Kit.scuttle(m, "leg", 3, 22);
        m.look("head", 20, 15);

        m.clip("idle", 40).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, -0.4f, 0).pos("body", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, -3, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 26, 0, 0, 0).rot("mandR", 29, 0, -25, 0).rot("mandR", 32, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 26, 0, 0, 0).rot("mandL", 29, 0, 25, 0).rot("mandL", 32, 0, 0, 0)
            .scl("cargo", 0, 1, 1, 1).scl("cargo", 20, 1.02f, 0.98f, 1.02f).scl("cargo", 40, 1, 1, 1);
        // HEADBUTT: rear up on hind legs, dig in, then ram forward headfirst
        m.clip("headbutt", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -22, 0, 0, Ease.OUT_QUAD).rot("body", 14, 14, 0, 0, Ease.IN_CUBIC).rot("body", 20, 8, 0, 0).rot("body", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 1, -3, Ease.OUT_QUAD).pos("body", 14, 0, -1, 7, Ease.IN_CUBIC).pos("body", 18, 0, 0, 5, Ease.OUT_BACK).pos("body", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -10, 0, 0).rot("head", 14, 18, 0, 0, Ease.IN_CUBIC).rot("head", 16, 10, 0, 0).rot("head", 30, 0, 0, 0)
            .rot("cargo", 0, 0, 0, 0).rot("cargo", 10, -6, 0, 0).rot("cargo", 15, 12, 0, 0, Ease.OUT_BACK).rot("cargo", 22, -4, 0, 0).rot("cargo", 30, 0, 0, 0)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 10, 0, -35, 0).rot("mandR", 14, 0, 10, 0).rot("mandR", 30, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 10, 0, 35, 0).rot("mandL", 14, 0, -10, 0).rot("mandL", 30, 0, 0, 0);
        // DIZZY: tipsy wobble, eyes spin
        m.clip("dizzy", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 0, 0, 14).rot("body", 16, 0, 0, -14).rot("body", 24, 0, 0, 12).rot("body", 32, 0, 0, -10).rot("body", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 15, 20, 0).rot("head", 20, 15, -20, 0).rot("head", 30, 12, 15, 0).rot("head", 40, 0, 0, 0)
            .rot("eyeR", 0, 0, 0, 0).rot("eyeR", 20, 0, 0, 360, Ease.LINEAR).rot("eyeR", 40, 0, 0, 720, Ease.LINEAR)
            .rot("eyeL", 0, 0, 0, 0).rot("eyeL", 20, 0, 0, -360, Ease.LINEAR).rot("eyeL", 40, 0, 0, -720, Ease.LINEAR)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -1, 0).pos("body", 16, 0, -0.5f, 0).pos("body", 40, 0, 0, 0);
        // BITE (egg): lunge, mandibles snap shut, then hunker down behind the shell (block)
        m.clip("bite", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0.5f, -2).pos("body", 12, 0, -0.5f, 5, Ease.IN_CUBIC).pos("body", 18, 0, -1.5f, 0).pos("body", 26, 0, -1.5f, 0).pos("body", 30, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -10, 0, 0).rot("body", 12, 10, 0, 0, Ease.IN_CUBIC).rot("body", 18, 0, 0, 0)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 8, 0, -50, 0).rot("mandR", 12, 0, 25, 0, Ease.IN_CUBIC).rot("mandR", 18, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 8, 0, 50, 0).rot("mandL", 12, 0, -25, 0, Ease.IN_CUBIC).rot("mandL", 18, 0, 0, 0)
            .scl("cargo", 0, 1, 1, 1).scl("cargo", 18, 1.12f, 0.9f, 1.12f, Ease.OUT_BACK).scl("cargo", 30, 1, 1, 1);
        // THRASH (silk x2 / nectar x1): head swipes left-right
        m.clip("thrash2", 34)
            .rot("body", 0, 0, 0, 0).rot("body", 6, 0, 30, 0).rot("body", 10, 6, -35, 0, Ease.IN_CUBIC).rot("body", 16, 0, -32, 0).rot("body", 20, 6, 35, 0, Ease.IN_CUBIC).rot("body", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, 3, Ease.IN_CUBIC).pos("body", 20, 0, 0, 3, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 6, -10, 15, 0).rot("head", 10, 12, -20, 0).rot("head", 16, -8, -15, 0).rot("head", 20, 12, 20, 0).rot("head", 34, 0, 0, 0)
            .rot("cargo", 0, 0, 0, 0).rot("cargo", 12, 0, 0, 8).rot("cargo", 22, 0, 0, -8).rot("cargo", 34, 0, 0, 0);
        m.clip("thrash1", 26)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -6, 35, 0).rot("body", 12, 8, -35, 0, Ease.IN_CUBIC).rot("body", 26, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -1).pos("body", 12, 0, 0, 3, Ease.IN_CUBIC).pos("body", 26, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -12, 15, 0).rot("head", 12, 14, -20, 0, Ease.IN_CUBIC).rot("head", 26, 0, 0, 0)
            .rot("cargo", 0, 0, 0, 0).rot("cargo", 13, 0, 0, 10, Ease.OUT_BACK).rot("cargo", 26, 0, 0, 0);
        // SPIN WEB (silk): rear up and spin the silk ball, then flick a strand forward
        m.clip("spin_web", 36)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -18, 0, 0).rot("body", 24, -18, 0, 0).rot("body", 28, 10, 0, 0, Ease.IN_CUBIC).rot("body", 36, 0, 0, 0)
            .rot("cargo", 0, 0, 0, 0).rot("cargo", 24, 0, 540, 0, Ease.IN_OUT_QUAD).rot("cargo", 36, 0, 540, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -15, 0, 0).rot("head", 28, 15, 0, 0, Ease.IN_CUBIC).rot("head", 36, 0, 0, 0)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 26, 0, -40, 0).rot("mandR", 29, 0, 10, 0).rot("mandR", 36, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 26, 0, 40, 0).rot("mandL", 29, 0, -10, 0).rot("mandL", 36, 0, 0, 0);
        // BUFF (nectar): dip the head back into the nectar bowl, gulp, swell up
        m.clip("buff", 44)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -55, 0, 0, Ease.OUT_QUAD).rot("head", 16, -50, 0, 0).rot("head", 20, -55, 0, 0).rot("head", 24, -50, 0, 0).rot("head", 30, 5, 0, 0, Ease.OUT_BACK).rot("head", 44, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -12, 0, 0).rot("body", 30, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 28, 1, 1, 1).scl("body", 34, 1.18f, 1.14f, 1.18f, Ease.OUT_BACK).scl("body", 44, 1, 1, 1)
            .scl("cargo", 0, 1, 1, 1).scl("cargo", 24, 0.85f, 0.7f, 0.85f).scl("cargo", 44, 1, 1, 1)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 14, 0, -30, 0).rot("mandR", 17, 0, 0, 0).rot("mandR", 20, 0, -30, 0).rot("mandR", 23, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 14, 0, 30, 0).rot("mandL", 17, 0, 0, 0).rot("mandL", 20, 0, 30, 0).rot("mandL", 23, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -12, 0, 6).rot("body", 10, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 3, 0, 0, -1.5f).pos("body", 10, 0, 0, 0);
        Clip d = m.clip("death", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 0, 0, 40).rot("body", 20, 0, 0, 180, Ease.OUT_BOUNCE).rot("body", 30, 0, 0, 180)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 3, 0).pos("body", 20, 0, 3, 0).pos("body", 30, 0, 2, 0)
            .pos("cargo", 0, 0, 0, 0).pos("cargo", 14, 0, 0, 0).pos("cargo", 24, 0, 6, -6, Ease.OUT_QUAD).pos("cargo", 30, 0, 9, -8);
        Kit.curlLegs(d, "leg", 3, 0, 20);
        return m;
    }

    static MobDef def(int kind) {
        String[] ids = {"rock", "egg", "silk", "nectar"};
        String[] en = {"Rock", "Egg", "Silk", "Nectar"};
        String[] ja = {"岩", "卵", "糸", "みつ"};
        int[] c = SHELL[kind];
        MobDef d = MobDef.of("s2_bowlbug_" + ids[kind], "Bowlbug (" + en[kind] + ")", "ボウルバグ（" + ja[kind] + "）")
            .game(2, 2).style(Style.INSECT).egg(c[0], kind == ROCK ? 0x7a7458 : kind == EGG ? 0x58c0b0 : kind == SILK ? 0xf4f4f6 : 0xf0a020)
            .model(() -> model(kind)).speed(0.24f);
        switch (kind) {
            case ROCK -> d.hp(46).size(0.9f, 1.15f).spawn(6, 1, 1, MobDef.Habitat.CAVE)
                .pattern(s -> "headbutt")
                .move(Move.of("headbutt", 30).intent(Intent.ATTACK, 15, 1).reach(2.2f)
                    .sound(8, SoundEvents.RAVAGER_STEP, 0.8f, 1.4f)
                    .at(13, Fx.dash(0.6, 0.05))
                    .at(14, (s, t) -> {
                        int n = Fx.meleeNow(s, 15, 2.6, 90, v -> Fx.knock(s, v, 0.6));
                        // Imbalanced: a fully blocked Headbutt (shield up) stuns it -> Dizzy next turn
                        if (n == 0 && t != null && t.isBlocking() && s.distanceTo(t) < 3.5) { s.forceNext("dizzy"); Fx.sound(s, SoundEvents.ANVIL_LAND, 0.7f, 1.6f); }
                    }))
                .move(Move.of("dizzy", 40).intent(Intent.STUN).noTarget()
                    .at(2, (s, t) -> Fx.particles(s, ParticleTypes.CRIT, 12, 0.5)).at(20, (s, t) -> Fx.particles(s, ParticleTypes.CRIT, 12, 0.5)));
            case EGG -> d.hp(21).size(0.75f, 0.95f).spawn(6, 1, 2, MobDef.Habitat.CAVE)
                .pattern(s -> "bite")
                .move(Move.of("bite", 30).intent(Intent.ATTACK_DEFEND, 7, 1).reach(2f)
                    .sound(11, SoundEvents.SPIDER_AMBIENT, 0.8f, 1.6f)
                    .at(12, Fx.melee(7, 2.4, 90)).at(18, Fx.block(7)));
            case SILK -> d.hp(41).size(0.85f, 1.1f).spawn(5, 1, 1, MobDef.Habitat.CAVE)
                .pattern(Pattern.cycle("spin_web", "thrash"))
                .move(Move.of("spin_web", 36).intent(Intent.DEBUFF).ranged(10)
                    .sound(10, SoundEvents.WOOL_PLACE, 1f, 0.7f)
                    .at(28, Fx.shoot("s2_bowlbug_web", 0, 0.9f, 0, 0.9, 0.5, v -> Fx.weak(v, 8))))
                .move(Move.of("thrash", 34).anim("thrash2").intent(Intent.ATTACK, 4, 2).reach(2f)
                    .at(10, Fx.melee(4, 2.4, 100)).at(20, Fx.melee(4, 2.4, 100)));
            default -> d.hp(36).size(0.85f, 1.0f).spawn(5, 1, 1, MobDef.Habitat.CAVE)
                .pattern(Pattern.seq(2, "thrash", "buff", "thrash"))
                .move(Move.of("thrash", 26).anim("thrash1").intent(Intent.ATTACK, 3, 1).reach(2f).at(12, Fx.melee(3, 2.4, 100)))
                .move(Move.of("buff", 44).intent(Intent.BUFF).noTarget()
                    .sound(14, SoundEvents.HONEY_DRINK, 1f, 0.8f).sound(20, SoundEvents.HONEY_DRINK, 1f, 0.9f)
                    .at(32, (s, t) -> { s.addStrength(15); Fx.particles(s, ParticleTypes.DRIPPING_HONEY, 20, 0.5); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.4); }));
        }
        if (kind == SILK) ProjKind.orb("s2_bowlbug_web", 0xf0f0f4, 2).trail(ParticleTypes.WHITE_ASH);
        return d;
    }
}
