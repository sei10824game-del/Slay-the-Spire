package com.spiremod.mobs.s2ud2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks BOSS: Soul Fysh (211 HP). Cycle: Beckon (2 Beckon cards) / De-Gas 16 / Gaze 7 + Beckon / Fade (2 Intangible) / Scream 13 + 3 Vulnerable.
 * Intangible: all damage reduced to 1 until its next move ends.
 */
public final class SoulFysh {
    private SoulFysh() {}
    static final int SK = 0x7a6cc8, SK_D = 0x3c3480, BEL = 0xc8c0f0, FIN = 0xa090ff, EYE = 0xfff0a0, MAW = 0x1a1030, TOOTH = 0xf0f0ff, SOUL = 0x90f0ff;
    static final String[] CYC = {"beckon", "de_gas", "gaze", "fade", "scream"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_soul_fysh");
        m.scale(1.9f);
        Part body = m.add("body").at(0, 14, 0);
        body.box(-6, -7, -8, 12, 15, 16).color(SK).grad(SK, SK_D).noise(0.12f).bevel(0.15f).band(10, 2, BEL)
            .paint(Face.LEFT, 3, 5, 10, 1, BEL).paint(Face.RIGHT, 3, 5, 10, 1, BEL);
        body.box(-6, -7.5f, -7, 12, 4, 14).color(BEL).grad(BEL, SK).noise(0.1f).bevel(0.2f);   // pale belly
        Part dors = body.add("dorsal").at(0, 8, 0);
        dors.box(-0.5f, 0, -7, 1, 7, 14).color(FIN).flat().grad(0xd8d0ff, FIN).glow(Face.LEFT, 0, 0, 14, 1, 0xe8e0ff).glow(Face.RIGHT, 0, 0, 14, 1, 0xe8e0ff);
        for (int s = -1; s <= 1; s += 2) {
            Part f = body.add(s < 0 ? "finR" : "finL").at(s * 6, -2, 2).rot(0, 0, s * -20);
            f.box(s < 0 ? -8 : 0, -1, -3, 8, 1, 7).color(FIN).flat().bevel(0.2f).glow(Face.TOP, 0, 0, 8, 1, 0xd0c8ff);
        }
        // big head up front, huge maw
        Part head = body.add("head").at(0, 0, 8);
        head.box(-6.5f, -5, 0, 13, 11, 9).color(SK).grad(SK, SK_D).noise(0.12f).bevel(0.15f);
        for (int s = -1; s <= 1; s += 2) {
            Part eye = head.add(s < 0 ? "eyeR" : "eyeL").at(s * 6.5f, 3, 4);
            eye.box(s < 0 ? -2 : -1, -2, -2, 3, 4, 4).color(0xfff0d8).flat().bevel(0.2f)
                .glow(s < 0 ? Face.RIGHT : Face.LEFT, 0, 1, 2, 2, EYE);
        }
        head.box(-6.5f, -5, 8, 13, 5, 2).color(SK_D).bevel(0.15f);
        for (int i = 0; i < 5; i++) head.box(-5.5f + i * 2.5f, -7, 8.5f, 1, 2, 1).color(TOOTH).flat();       // upper fangs
        Part jaw = head.add("jaw").at(0, -4, 1);
        jaw.box(-6, -3, 0, 12, 3, 10).color(BEL).grad(SK_D, BEL).bevel(0.15f).paint(Face.TOP, 1, 1, 10, 8, MAW);
        for (int i = 0; i < 5; i++) jaw.box(-5 + i * 2.5f, 0, 9, 1, 2, 1).color(TOOTH).flat();
        // lure with a soul flame
        Part lure = head.add("lure").at(0, 6, 3).rot(-30, 0, 0);
        lure.box(-0.5f, 0, -0.5f, 1, 9, 1).color(SK_D);
        Part bulb = lure.add("bulb").at(0, 9, 0);
        bulb.box(-2, 0, -2, 4, 4, 4).color(SOUL).flat().glow(Face.FRONT, 0, 0, 4, 4, SOUL).glow(Face.TOP, 0, 0, 4, 4, SOUL).glow(Face.LEFT, 0, 0, 4, 4, SOUL).glow(Face.RIGHT, 0, 0, 4, 4, SOUL);
        // tail
        Part t1 = body.add("t1").at(0, 0, -8);
        t1.box(-4, -5, -8, 8, 11, 8).color(SK).grad(SK, SK_D).noise(0.12f).bevel(0.15f);
        Part t2 = t1.add("t2").at(0, 0, -8);
        t2.box(-2.5f, -3, -7, 5, 7, 7).color(SK_D).grad(SK, SK_D).bevel(0.15f);
        Part tf = t2.add("tailfin").at(0, 0, -7);
        tf.box(-0.5f, -7, -6, 1, 15, 6).color(FIN).flat().grad(0xd8d0ff, FIN).glow(Face.LEFT, 0, 0, 6, 1, 0xe8e0ff).glow(Face.RIGHT, 0, 0, 6, 1, 0xe8e0ff);
        m.look("head", 40, 25);
        m.bob(2f, 0.1f);
        m.proc((r, c) -> {
            float w = c.age * 0.15f;
            r.rot("t1", 0, (float) Math.sin(w) * 8, 0); r.rot("t2", 0, (float) Math.sin(w - 1) * 14, 0); r.rot("tailfin", 0, (float) Math.sin(w - 2) * 18, 0);
            r.rot("finR", 0, 0, (float) Math.sin(w * 1.6f) * 14); r.rot("finL", 0, 0, -(float) Math.sin(w * 1.6f) * 14);
            r.rot("bulb", (float) Math.sin(w * 1.3f) * 10, 0, 0);
        });
        m.clip("idle", 60).loop()
            .rot("head", 0, 3, 0, 0).rot("head", 30, -3, 0, 0).rot("head", 60, 3, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 20, 12, 0, 0).rot("jaw", 40, 0, 0, 0).rot("jaw", 60, 0, 0, 0)
            .rot("lure", 0, -30, 0, 5).rot("lure", 30, -20, 0, -5).rot("lure", 60, -30, 0, 5);
        m.clip("walk", 20).loop();
        // BECKON: lure waves hypnotically, eyes wide, finned arms beckon
        m.clip("beckon", 44)
            .rot("lure", 0, -30, 0, 0).rot("lure", 10, -70, 0, 0, Ease.OUT_BACK).rot("lure", 18, -50, 0, 25).rot("lure", 26, -50, 0, -25).rot("lure", 34, -50, 0, 25).rot("lure", 44, -30, 0, 0)
            .rot("finR", 0, 0, 0, 0).rot("finR", 14, 0, 0, 45).rot("finR", 22, 0, 0, 0).rot("finR", 30, 0, 0, 45).rot("finR", 44, 0, 0, 0)
            .rot("finL", 0, 0, 0, 0).rot("finL", 14, 0, 0, -45).rot("finL", 22, 0, 0, 0).rot("finL", 30, 0, 0, -45).rot("finL", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -12, 0, 0).rot("head", 44, 0, 0, 0)
            .scl("eyeR", 0, 1, 1, 1).scl("eyeR", 10, 1.4f, 1.4f, 1.4f).scl("eyeR", 44, 1, 1, 1).scl("eyeL", 0, 1, 1, 1).scl("eyeL", 10, 1.4f, 1.4f, 1.4f).scl("eyeL", 44, 1, 1, 1);
        // DE-GAS: inhale (body swells), then exhale a gust forward (jaw wide, lunge)
        m.clip("de_gas", 40)
            .scl("body", 0, 1, 1, 1).scl("body", 14, 1.25f, 1.25f, 1.15f, Ease.OUT_QUAD).scl("body", 20, 0.9f, 0.9f, 0.9f, Ease.IN_CUBIC).scl("body", 40, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -22, 0, 0).rot("head", 20, 16, 0, 0, Ease.IN_CUBIC).rot("head", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 55, 0, 0).rot("jaw", 32, 50, 0, 0).rot("jaw", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 0, -4).pos("body", 20, 0, 0, 5, Ease.IN_CUBIC).pos("body", 40, 0, 0, 0)
            .rot("tailfin", 0, 0, 0, 0).rot("tailfin", 20, 0, 30, 0).rot("tailfin", 40, 0, 0, 0);
        // GAZE: swivels, eyes bulge and lock on, small snap
        m.clip("gaze", 36)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 0, 30, 0).rot("head", 18, 0, -30, 0).rot("head", 24, 10, 0, 0, Ease.IN_CUBIC).rot("head", 36, 0, 0, 0)
            .scl("eyeR", 0, 1, 1, 1).scl("eyeR", 18, 1.7f, 1.7f, 1.7f, Ease.OUT_BACK).scl("eyeR", 36, 1, 1, 1).scl("eyeL", 0, 1, 1, 1).scl("eyeL", 18, 1.7f, 1.7f, 1.7f, Ease.OUT_BACK).scl("eyeL", 36, 1, 1, 1)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 22, 30, 0, 0).rot("jaw", 26, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 24, 0, 0, 4, Ease.IN_CUBIC).pos("body", 36, 0, 0, 0);
        // FADE: curls up and shimmers, shrinking slightly then pulsing (translucent feel)
        m.clip("fade", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 14, 0, 0, 25).rot("body", 28, 0, 0, -25).rot("body", 40, 0, 0, 0)
            .rot("t1", 0, 0, 0, 0).rot("t1", 16, 0, 40, 0).rot("t1", 40, 0, 0, 0).rot("t2", 0, 0, 0, 0).rot("t2", 16, 0, 50, 0).rot("t2", 40, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 14, 0.85f, 0.85f, 0.85f).scl("body", 28, 1.1f, 1.1f, 1.1f, Ease.OUT_BACK).scl("body", 40, 1, 1, 1)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, 5, 0).pos("body", 40, 0, 0, 0);
        // SCREAM: rears back, giant maw, tremor
        Clip sc = m.clip("scream", 46);
        sc.rot("head", 0, 0, 0, 0).rot("head", 12, -38, 0, 0, Ease.OUT_BACK).rot("head", 36, -34, 0, 0).rot("head", 46, 0, 0, 0)
          .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 75, 0, 0, Ease.OUT_BACK).rot("jaw", 38, 70, 0, 0).rot("jaw", 46, 0, 0, 0)
          .rot("body", 0, 0, 0, 0).rot("body", 12, -14, 0, 0).rot("body", 38, -12, 0, 0).rot("body", 46, 0, 0, 0)
          .scl("body", 0, 1, 1, 1).scl("body", 14, 1.12f, 1.12f, 1.05f, Ease.OUT_BACK).scl("body", 38, 1.1f, 1.1f, 1.05f).scl("body", 46, 1, 1, 1);
        for (int t = 14; t < 38; t += 2) sc.rot("head", t, -36 + (t % 4 == 0 ? 3 : -3), (t % 4 == 0 ? 3 : -3), 0, Ease.LINEAR);
        m.clip("hurt", 10).rot("head", 0, 0, 0, 0).rot("head", 3, -15, 8, 0).rot("head", 10, 0, 0, 0).rot("body", 0, 0, 0, 0).rot("body", 3, 0, 0, 10).rot("body", 10, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 30, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 50)
            .rot("body", 0, 0, 0, 0).rot("body", 30, 0, 0, 180, Ease.OUT_QUAD).rot("body", 50, 0, 0, 180)
            .pos("body", 0, 0, 0, 0).pos("body", 50, 0, -13, 0, Ease.OUT_BOUNCE)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 60, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 20, -20, 0, 0)
            .rot("t2", 0, 0, 0, 0).rot("t2", 40, 0, 60, 0).rot("tailfin", 0, 0, 0, 0).rot("tailfin", 40, 0, -60, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 50, 1, 0.8f, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_soul_fysh", "Soul Fysh", "ソウルフィッシュ")
            .game(2, 1).boss().flying().size(2.0f, 2.6f).hp(211).speed(0.2f).style(Style.GHOST).egg(0x7a6cc8, 0x90f0ff)
            .model(SoulFysh::model)
            .pattern(s -> CYC[s.count("ct") % 5])
            .move(Move.of("beckon", 44).intent(Intent.DEBUFF).ranged(14)
                .sound(10, SoundEvents.SOUL_ESCAPE, 1.4f, 0.6f)
                .at(16, (s, t) -> { for (var v : Fx.foes(s, s.getBoundingBox().inflate(14))) { Fx.curse(v, Fx.Curse.VOID); Fx.curse(v, Fx.Curse.VOID); Fx.ring(s, ParticleTypes.SOUL, v.position().add(0, 1, 0), 1.0, 12); } }))
            .move(Move.of("de_gas", 40).intent(Intent.ATTACK, 16, 1).reach(4.0f).sound(12, SoundEvents.GENERIC_EXPLODE, 1.2f, 0.6f)
                .at(20, (s, t) -> { Fx.meleeNow(s, 16, 5.0, 70, v -> Fx.knock(s, v, 0.8)); Fx.particlesAt(s, ParticleTypes.SOUL, Fx.rel(s, 3, 0, 1.2), 30, 0.6, 0.25); }))
            .move(Move.of("gaze", 36).intent(Intent.ATTACK_DEBUFF, 7, 1).reach(3.6f).sound(18, SoundEvents.ELDER_GUARDIAN_CURSE, 0.8f, 1.2f)
                .at(25, (s, t) -> { Fx.meleeNow(s, 7, 4.0, 90, v -> Fx.curse(v, Fx.Curse.VOID)); }))
            .move(Move.of("fade", 40).intent(Intent.BUFF).noTarget().sound(12, SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, 1.5f, 0.4f)
                .at(16, (s, t) -> { s.flags.add("intangible"); Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 25, 0.8); }))
            .move(Move.of("scream", 46).intent(Intent.ATTACK_DEBUFF, 13, 1).ranged(14)
                .sound(12, SoundEvents.ELDER_GUARDIAN_CURSE, 1.6f, 0.5f)
                .at(16, (s, t) -> {
                    Fx.ring(s, ParticleTypes.SCULK_SOUL, s.position().add(0, 1.5, 0), 3.0, 40);
                    Fx.hitTarget(13, v -> Fx.vulnerable(v, 9)).run(s, t);
                }))
            .onMoveEnd(s -> {
                String l = s.lastMove();
                if (!l.equals("fade")) s.flags.remove("intangible");        // intangibility lasts through the player's turn, until its next move ends
                s.inc("ct");
            })
            .onHurt((s, src, dmg) -> s.flags.contains("intangible") ? Math.min(dmg, 1f) : dmg);
    }
}
