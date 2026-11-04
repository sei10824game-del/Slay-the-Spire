package com.spiremod.mobs.s2ud2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks ELITE: Terror Eel (140 HP). Shriek 70: the first time its HP drops to 70 or below it is Stunned,
 * then uses Terror (99 Vulnerable). Otherwise alternates Crash (16) / Thrash (3x3, gains 6 Vigor), starting with Crash.
 */
public final class TerrorEel {
    private TerrorEel() {}

    static final int SKIN = 0x46607c, SKIN_D = 0x2b3d55, BELLY = 0xbfae8e, SPOT = 0x22324a,
        YEL = 0xe8aa1c, ORA = 0xe2680e, RED = 0xc81e1e, SCL = 0xe9cfc0, IRIS = 0xe03434, PUP = 0x3a0c10, TOOTH = 0x14141c;

    /** bubble eye glued to a face of `p` */
    static void eye(Part p, Face f, float x, float y, float z, int n) {
        int dx = (f == Face.LEFT || f == Face.RIGHT) ? 1 : n, dy = (f == Face.TOP) ? 1 : n, dz = (f == Face.FRONT || f == Face.TOP) ? (f == Face.TOP ? n : 1) : n;
        Box b = p.box(x, y, z, dx, dy, dz).color(SCL).flat().noise(0.03f).bevel(0.25f);
        if (n >= 3) {
            b.glow(f, 1, 1, n - 2, n - 2, IRIS);
            int c = (n - 1) / 2, w = n % 2 == 1 ? 1 : 2;
            b.paint(f, c, c, w, w, PUP);
        } else b.glow(f, 0, 0, n, n, IRIS);
    }

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_terror_eel");
        m.scale(1.45f);
        // ---- mid body hump (rests on the ground)
        Part body = m.add("body").at(0, 9, 0);
        body.box(-6, -9, -9, 12, 15, 18).color(SKIN).grad(SKIN, BELLY).noise(0.14f).bevel(0.12f)
            .paint(Face.TOP, 2, 3, 2, 2, SPOT).paint(Face.TOP, 7, 9, 2, 2, SPOT).paint(Face.TOP, 4, 13, 1, 1, SPOT)
            .band(13, 2, 0xa89878);
        // orange dorsal sail
        Part fin = body.add("fin").at(0, 6, 0);
        fin.box(-0.5f, 0, -7, 1, 4, 13).color(ORA).flat().grad(0xf0a030, ORA).noise(0.05f)
            .glow(Face.LEFT, 0, 0, 13, 1, 0xffb040).glow(Face.RIGHT, 0, 0, 13, 1, 0xffb040);
        fin.box(-0.5f, 4, -4, 1, 2, 8).color(0xf0a030).flat().glow(Face.LEFT, 0, 0, 8, 1, 0xffc060).glow(Face.RIGHT, 0, 0, 8, 1, 0xffc060);
        // body eyes
        eye(body, Face.LEFT, 6, -1, -6, 5); eye(body, Face.RIGHT, -7, -1, -6, 5);
        eye(body, Face.LEFT, 6, 1, 2, 4);   eye(body, Face.RIGHT, -7, 1, 2, 4);
        eye(body, Face.LEFT, 6, -5, 4, 3);  eye(body, Face.RIGHT, -7, -5, 4, 3);
        eye(body, Face.TOP, -4, 6, 1, 3);
        // orange / yellow stripes on the flanks
        for (int s = -1; s <= 1; s += 2) {
            body.box(s < 0 ? -6.3f : 5.3f, 0, -2, 1, 6, 1).color(ORA).flat();
            body.box(s < 0 ? -6.3f : 5.3f, -2, -9, 1, 6, 1).color(YEL).flat();
        }
        // ---- neck + head (front, low, long crocodile-like snout)
        Part neck = body.add("neck").at(0, -1, 9);
        neck.box(-5, -7, -1, 10, 12, 9).color(SKIN).grad(SKIN, BELLY).noise(0.14f).bevel(0.1f);
        for (int s = -1; s <= 1; s += 2) neck.box(s < 0 ? -5.3f : 4.3f, 0, 2, 1, 4, 1).color(RED).flat();
        eye(neck, Face.LEFT, 5, -1, 1, 4); eye(neck, Face.RIGHT, -6, -1, 1, 4);
        eye(neck, Face.TOP, -1.5f, 5, 3, 3);
        Part head = neck.add("head").at(0, -2, 8);
        head.box(-4.5f, -1, -1, 9, 6, 11).color(SKIN).grad(SKIN, SKIN_D).noise(0.12f).bevel(0.12f)
            .paint(Face.LEFT, 0, 5, 11, 1, TOOTH).paint(Face.RIGHT, 0, 5, 11, 1, TOOTH);
        head.box(-3.5f, -1, 10, 7, 5, 6).color(SKIN).grad(SKIN, SKIN_D).noise(0.12f).bevel(0.15f)
            .paint(Face.LEFT, 0, 4, 6, 1, TOOTH).paint(Face.RIGHT, 0, 4, 6, 1, TOOTH);
        head.box(-2.5f, -1, 16, 5, 3, 3).color(SKIN_D).bevel(0.2f);              // snout tip, hooked
        head.box(-1.5f, -2, 18, 3, 2, 1).color(SKIN_D);
        for (int i = 0; i < 4; i++) head.box(i % 2 == 0 ? 4.3f : -5.3f, 1 + (i / 2) * 1.5f, 2 + i * 2, 1, 1, 2).color(RED).flat(); // red scratches
        eye(head, Face.LEFT, 4.5f, 1, 1, 3); eye(head, Face.RIGHT, -5.5f, 1, 1, 3);
        // upper teeth row (black fangs)
        for (int i = 0; i < 6; i++) { head.box(3.2f, -2, 1 + i * 2.5f, 1, 1, 1).color(0xe8e0d0).flat(); head.box(-4.2f, -2, 1 + i * 2.5f, 1, 1, 1).color(0xe8e0d0).flat(); }
        Part jaw = head.add("jaw").at(0, -1, 0);
        jaw.box(-4, -3, -1, 8, 3, 16).color(BELLY).grad(SKIN_D, BELLY).noise(0.1f).bevel(0.12f)
            .paint(Face.TOP, 1, 0, 6, 16, 0x7a2a30);                              // red mouth lining
        jaw.box(-3, -3, 15, 6, 2, 3).color(BELLY).bevel(0.2f);
        for (int i = 0; i < 6; i++) { jaw.box(2.5f, 0, 1 + i * 2.5f, 1, 1, 1).color(0xe8e0d0).flat(); jaw.box(-3.5f, 0, 1 + i * 2.5f, 1, 1, 1).color(0xe8e0d0).flat(); }
        // ---- tail: runs back, rises vertically, curls forward like a swan neck, arrow fin on top
        Part t1 = body.add("tail1").at(0, -2, -9);
        t1.box(-5, -7, -10, 10, 12, 10).color(SKIN).grad(SKIN, BELLY).noise(0.14f).bevel(0.1f);
        eye(t1, Face.LEFT, 5, -2, -8, 4); eye(t1, Face.RIGHT, -6, -2, -8, 4); eye(t1, Face.LEFT, 5, 2, -3, 3); eye(t1, Face.RIGHT, -6, 2, -3, 3);
        for (int s = -1; s <= 1; s += 2) { t1.box(s < 0 ? -5.3f : 4.3f, -2, -5, 1, 7, 1).color(ORA).flat(); t1.box(s < 0 ? -5.3f : 4.3f, -4, -9, 1, 6, 1).color(YEL).flat(); }
        Part t2 = t1.add("tail2").at(0, 3, -7).rot(-8, 0, 0);
        t2.box(-4, 0, -3.5f, 8, 13, 7).color(SKIN).grad(SKIN, SKIN_D).noise(0.12f).bevel(0.12f)
            .paint(Face.BACK, 0, 3, 8, 2, YEL).paint(Face.BACK, 0, 8, 8, 2, YEL)
            .paint(Face.LEFT, 0, 4, 3, 2, YEL).paint(Face.RIGHT, 4, 4, 3, 2, YEL).paint(Face.LEFT, 2, 9, 4, 1, ORA).paint(Face.RIGHT, 1, 9, 4, 1, ORA);
        eye(t2, Face.FRONT, -1.5f, 6, 3.5f, 3); eye(t2, Face.LEFT, 4, 1, -1, 3); eye(t2, Face.RIGHT, -5, 1, -1, 3);
        Part t3 = t2.add("tail3").at(0, 13, 0).rot(40, 0, 0);
        t3.box(-3, 0, -3, 6, 9, 6).color(SKIN).grad(SKIN, SKIN).noise(0.12f).bevel(0.12f)
            .paint(Face.BACK, 0, 2, 6, 2, YEL).paint(Face.LEFT, 0, 5, 6, 1, YEL).paint(Face.RIGHT, 0, 5, 6, 1, YEL);
        Part t4 = t3.add("tail4").at(0, 9, 0).rot(45, 0, 0);
        t4.box(-2, 0, -2, 4, 6, 4).color(SKIN_D).bevel(0.15f);
        Part tf = t4.add("tailfin").at(0, 6, 0);
        tf.box(-0.5f, 0, -3, 1, 6, 6).color(0x8090a8).flat().bevel(0.2f);        // grey arrow blade
        tf.box(-0.5f, 6, -1, 1, 2, 2).color(0x8090a8).flat();
        tf.box(-2.5f, 1, -2.5f, 2, 4, 3).color(ORA).flat().glow(Face.RIGHT, 0, 0, 3, 1, 0xffa040);   // orange frill lobe
        tf.box(0.5f, 1, -2.5f, 2, 4, 3).color(ORA).flat().glow(Face.LEFT, 0, 0, 3, 1, 0xffa040);

        m.look("neck", 30, 20);
        // slither: lateral S-wave travelling down the body
        m.proc((r, c) -> {
            float a = c.swingAmt, w = c.swing * 0.7f;
            r.rot("body", 0, (float) Math.sin(w) * 10 * a, 0);
            r.rot("neck", 0, (float) Math.sin(w - 1.2f) * -14 * a, 0);
            r.rot("tail1", 0, (float) Math.sin(w + 1.2f) * -16 * a, 0);
            r.rot("tail2", 0, 0, (float) Math.sin(w + 2.0f) * 10 * a);
            r.pos("body", 0, (float) Math.abs(Math.sin(w)) * 1.2f * a, 0);
        });
        m.clip("idle", 60).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 30, 0, 0.8f, 0).pos("body", 60, 0, 0, 0)
            .rot("neck", 0, 4, -5, 0).rot("neck", 30, -3, 5, 0).rot("neck", 60, 4, -5, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 30, -4, 0, 0).rot("head", 60, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 20, 8, 0, 0).rot("jaw", 40, 0, 0, 0).rot("jaw", 60, 0, 0, 0)
            .rot("tail2", 0, 0, 0, -4).rot("tail2", 30, 0, 0, 4).rot("tail2", 60, 0, 0, -4)
            .rot("tail3", 0, 0, 0, 0).rot("tail3", 30, 8, 0, 0).rot("tail3", 60, 0, 0, 0)
            .rot("tailfin", 0, 0, -10, 0).rot("tailfin", 30, 0, 10, 0).rot("tailfin", 60, 0, -10, 0)
            .rot("fin", 0, 0, 0, -5).rot("fin", 30, 0, 0, 5).rot("fin", 60, 0, 0, -5);
        m.clip("walk", 20).loop();
        // CRASH: rear the head up high, jaw gaping, then slam the whole front down onto the target
        m.clip("crash", 34)
            .rot("neck", 0, 0, 0, 0).rot("neck", 12, -45, 0, 0, Ease.OUT_QUAD).rot("neck", 16, 30, 0, 0, Ease.IN_CUBIC).rot("neck", 24, 25, 0, 0).rot("neck", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -15, 0, 0).rot("head", 16, 15, 0, 0, Ease.IN_CUBIC).rot("head", 34, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 50, 0, 0).rot("jaw", 15, 55, 0, 0).rot("jaw", 17, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 34, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -14, 0, 0).rot("body", 16, 12, 0, 0, Ease.IN_CUBIC).rot("body", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 3, -4).pos("body", 16, 0, -1, 8, Ease.IN_CUBIC).pos("body", 22, 0, 0, 7).pos("body", 34, 0, 0, 0)
            .rot("tail1", 0, 0, 0, 0).rot("tail1", 12, 15, 0, 0).rot("tail1", 16, -12, 0, 0, Ease.IN_CUBIC).rot("tail1", 34, 0, 0, 0)
            .rot("tail3", 0, 0, 0, 0).rot("tail3", 12, -20, 0, 0).rot("tail3", 18, 30, 0, 0, Ease.OUT_BACK).rot("tail3", 34, 0, 0, 0)
            .rot("fin", 0, 0, 0, 0).rot("fin", 12, 0, 0, 0).rot("fin", 17, 0, 0, 12).rot("fin", 21, 0, 0, -10).rot("fin", 26, 0, 0, 0);
        // THRASH: three whipping side-bites, tail lashing opposite
        Clip th = m.clip("thrash", 40);
        th.rot("neck", 0, 0, 0, 0).rot("jaw", 0, 0, 0, 0).rot("body", 0, 0, 0, 0).rot("tail1", 0, 0, 0, 0).rot("tail2", 0, 0, 0, 0).pos("body", 0, 0, 0, 0);
        for (int i = 0; i < 3; i++) {
            int t0 = 4 + i * 8; float s = i % 2 == 0 ? 1 : -1;
            th.rot("neck", t0 + 5, -10, 40 * s, 10 * s, Ease.OUT_QUAD).rot("neck", t0 + 8, 15, -30 * s, -12 * s, Ease.IN_CUBIC)
              .rot("jaw", t0 + 5, 50, 0, 0).rot("jaw", t0 + 8, 0, 0, 0, Ease.IN_CUBIC)
              .rot("body", t0 + 8, 0, -12 * s, 6 * s, Ease.IN_CUBIC)
              .rot("tail1", t0 + 8, 0, 30 * s, 0, Ease.IN_CUBIC).rot("tail2", t0 + 8, 0, 0, -20 * s)
              .pos("body", t0 + 8, 0, 0, 4, Ease.IN_CUBIC);
        }
        th.rot("neck", 40, 0, 0, 0).rot("jaw", 40, 0, 0, 0).rot("body", 40, 0, 0, 0).rot("tail1", 40, 0, 0, 0).rot("tail2", 40, 0, 0, 0).pos("body", 40, 0, 0, 0);
        // STUNNED: slumps flat, head lolls, tail droops, eyes squint
        String[] eyesHurt = {"neck", "head", "tail2", "tail3"};
        Clip st = m.clip("stunned", 50);
        st.pos("body", 0, 0, 0, 0).pos("body", 10, 0, -2.5f, 0, Ease.OUT_BOUNCE).pos("body", 40, 0, -2.5f, 0).pos("body", 50, 0, 0, 0)
          .rot("neck", 0, 0, 0, 0).rot("neck", 10, 22, 0, 18, Ease.OUT_BOUNCE).rot("neck", 25, 22, 10, 22).rot("neck", 40, 22, -5, 18).rot("neck", 50, 0, 0, 0)
          .rot("head", 0, 0, 0, 0).rot("head", 10, 10, 0, 15).rot("head", 50, 0, 0, 0)
          .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 25, 0, 0).rot("jaw", 44, 25, 0, 0).rot("jaw", 50, 0, 0, 0)
          .rot("tail2", 0, 0, 0, 0).rot("tail2", 12, 50, 0, 25, Ease.OUT_BOUNCE).rot("tail2", 40, 50, 0, 25).rot("tail2", 50, 0, 0, 0)
          .rot("tail3", 0, 0, 0, 0).rot("tail3", 12, 30, 0, 0).rot("tail3", 50, 0, 0, 0)
          .rot("fin", 0, 0, 0, 0).rot("fin", 10, 0, 0, 35).rot("fin", 45, 0, 0, 35).rot("fin", 50, 0, 0, 0);
        // TERROR (cast): rears tall, the whole body bloats, every eye stares, gaping shriek with shudders
        Clip te = m.clip("terror", 44);
        te.rot("neck", 0, 0, 0, 0).rot("neck", 12, -55, 0, 0, Ease.OUT_BACK).rot("neck", 34, -50, 0, 0).rot("neck", 44, 0, 0, 0)
          .rot("head", 0, 0, 0, 0).rot("head", 12, -25, 0, 0).rot("head", 34, -20, 0, 0).rot("head", 44, 0, 0, 0)
          .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 0, 0, 0).rot("jaw", 14, 70, 0, 0, Ease.OUT_BACK).rot("jaw", 34, 65, 0, 0).rot("jaw", 44, 0, 0, 0)
          .rot("body", 0, 0, 0, 0).rot("body", 12, -10, 0, 0).rot("body", 34, -10, 0, 0).rot("body", 44, 0, 0, 0)
          .scl("body", 0, 1, 1, 1).scl("body", 14, 1.12f, 1.12f, 1.05f, Ease.OUT_BACK).scl("body", 34, 1.1f, 1.1f, 1.05f).scl("body", 44, 1, 1, 1)
          .rot("tail2", 0, 0, 0, 0).rot("tail2", 12, -15, 0, 0).rot("tail2", 44, 0, 0, 0)
          .rot("tail3", 0, 0, 0, 0).rot("tail3", 12, -35, 0, 0).rot("tail3", 44, 0, 0, 0)
          .rot("fin", 0, 0, 0, 0).scl("fin", 0, 1, 1, 1).scl("fin", 14, 1, 1.6f, 1.1f, Ease.OUT_BACK).scl("fin", 36, 1, 1.5f, 1.1f).scl("fin", 44, 1, 1, 1);
        for (int t = 16; t < 34; t += 2) te.rot("neck", t, -50 + (t % 4 == 0 ? 3 : -3), (t % 4 == 0 ? 4 : -4), 0, Ease.LINEAR);
        m.clip("hurt", 10).rot("neck", 0, 0, 0, 0).rot("neck", 3, -15, 12, 0).rot("neck", 10, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 30, 0, 0).rot("jaw", 10, 0, 0, 0)
            .rot("tail2", 0, 0, 0, 0).rot("tail2", 3, 12, 0, 8).rot("tail2", 10, 0, 0, 0);
        // DEATH: writhes, rolls over onto its side, sinks
        m.clip("death", 44)
            .rot("neck", 0, 0, 0, 0).rot("neck", 8, -40, 20, 0).rot("neck", 22, 25, -15, 10, Ease.OUT_BOUNCE).rot("neck", 44, 25, -15, 10)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 60, 0, 0).rot("jaw", 44, 30, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 0, 10, -10).rot("body", 26, 0, 0, 85, Ease.OUT_BOUNCE).rot("body", 44, 0, 0, 85)
            .pos("body", 0, 0, 0, 0).pos("body", 26, 0, -2, 0).pos("body", 44, 0, -5, 0)
            .rot("tail2", 0, 0, 0, 0).rot("tail2", 10, -25, 0, 0).rot("tail2", 26, 60, 0, 0, Ease.OUT_BOUNCE).rot("tail2", 44, 60, 0, 0)
            .rot("tail3", 0, 0, 0, 0).rot("tail3", 26, 40, 0, 0).rot("tail3", 44, 40, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_terror_eel", "Terror Eel", "テラーウツボ")
            .game(2, 1).elite().size(1.7f, 2.0f).hp(140).speed(0.24f).kbResist(0.6f).style(Style.AQUATIC).egg(0x46607c, 0xe03434)
            .model(TerrorEel::model)
            .spawn(1, 1, 1, MobDef.Habitat.BEACH)
            .pattern(s -> {
                if (s.lastMove().equals("stunned")) return "terror";
                return s.count("ct") % 2 == 0 ? "crash" : "thrash";
            })
            .move(Move.of("crash", 34).intent(Intent.ATTACK, 16, 1).reach(3.2f)
                .sound(4, SoundEvents.GUARDIAN_ATTACK, 1.2f, 0.6f)
                .at(16, (s, t) -> {
                    Fx.meleeNow(s, 16 + s.count("vigor"), 3.8, 100, v -> Fx.knock(s, v, 0.6));
                    s.setCount("vigor", 0);
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.7f, 1.4f);
                    Fx.particlesAt(s, ParticleTypes.SPLASH, Fx.rel(s, 2.2, 0, 0.2), 40, 0.8, 0.3);
                }))
            .move(Move.of("thrash", 40).intent(Intent.ATTACK_BUFF, 3, 3).reach(3.0f)
                .at(12, Fx.melee(3, 3.4, 140)).at(20, Fx.melee(3, 3.4, 140)).at(28, Fx.melee(3, 3.4, 140))
                .sound(11, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.7f).sound(19, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.8f).sound(27, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.9f)
                .at(34, (s, t) -> { s.setCount("vigor", s.count("vigor") + 6); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.6); }))
            .move(Move.of("stunned", 50).intent(Intent.STUN).noTarget()
                .at(2, (s, t) -> { Fx.sound(s, SoundEvents.GUARDIAN_HURT_LAND, 1.2f, 0.5f); Fx.particles(s, ParticleTypes.CRIT, 20, 0.6); }))
            .move(Move.of("terror", 44).intent(Intent.STRONG_DEBUFF).ranged(16)
                .at(14, (s, t) -> {
                    Fx.sound(s, SoundEvents.ELDER_GUARDIAN_CURSE, 1.5f, 0.8f);
                    Fx.ring(s, ParticleTypes.SCULK_SOUL, s.position().add(0, 1.5, 0), 3.0, 40);
                    for (var v : Fx.foes(s, s.getBoundingBox().inflate(16))) Fx.vulnerable(v, 99 * 3);   // 99 turns = rest of the fight
                }))
            .onMoveEnd(s -> { String l = s.lastMove(); if (l.equals("crash") || l.equals("thrash")) s.inc("ct"); })
            .onHurt((s, src, dmg) -> {
                float after = s.getHealth() - dmg;
                if (!s.flags.contains("shriek") && after <= 70 && after > 0) {      // Shriek 70
                    s.flags.add("shriek");
                    s.cancelMove(); s.forceNext("stunned");
                }
                return dmg;
            });
    }
}
