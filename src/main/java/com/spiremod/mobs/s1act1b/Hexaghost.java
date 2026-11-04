package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 1 Boss Hexaghost: 250 HP. Turn 1 Activate, turn 2 Divider (6 hits, D = player HP/12+1; fixed 7 here),
 * then the 7-turn loop Sear (6 + Burn), Tackle (5 x2), Sear, Inflame (+2 Str, 12 Block), Tackle, Sear, Inferno (2 x6 + Burns).
 * Six flame orbs orbit the ghost; ctx.var = number of lit orbs.
 */
public final class Hexaghost {
    private Hexaghost() {}
    static final int ROBE = 0x2c2c46, ROBE_D = 0x14142a, ROBE_L = 0x4a4a70, BONE = 0xdcdcec, FIRE = 0x4be8b8, FIRE_C = 0xe8fff4, EYE = 0x7af0ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("hexaghost").scale(1.7f);
        Part body = m.add("body").at(0, 6, 0);
        body.box(-4, 0, -3, 8, 8, 6).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.12f).noise(0.08f);
        body.box(-5, -6, -4, 10, 6, 8).color(ROBE).grad(ROBE, ROBE_D).bevel(0.12f);               // tapering ghost robe
        body.box(-3, -11, -2.5f, 6, 5, 5).color(ROBE_D).grad(ROBE_D, 0x080818).bevel(0.12f);
        body.box(-1.5f, -15, -1.5f, 3, 4, 3).color(0x080818).grad(0x080818, 0x000000);
        Part torso = body.add("torso").at(0, 8, 0);
        torso.box(-5, 0, -3.5f, 10, 6, 7).color(ROBE).grad(ROBE_L, ROBE).bevel(0.12f).noise(0.08f);
        torso.box(-5.5f, 4, -4, 11, 2, 8).color(ROBE_L).grad(0x6a6a96, ROBE_L).bevel(0.15f);         // hunched shoulder mantle
        torso.box(-1, 1, -3.8f, 2, 3, 1).color(FIRE).flat().glow(Face.FRONT, 0, 0, 2, 3, FIRE);      // chest sigil
        Part head = torso.add("head").at(0, 6, 0);
        head.box(-3.5f, 0, -3.5f, 7, 7, 7).color(BONE).grad(0xf4f4ff, 0xa8a8c0).bevel(0.15f).noise(0.06f)
            .paint(Face.FRONT, 1, 2, 2, 2, 0x10101c).paint(Face.FRONT, 4, 2, 2, 2, 0x10101c)
            .glow(Face.FRONT, 1, 3, 2, 1, EYE).glow(Face.FRONT, 4, 3, 2, 1, EYE).paint(Face.FRONT, 2, 5, 3, 1, 0x20202c);
        head.box(-4.2f, 4, -4.2f, 8, 4, 8).color(ROBE_D).grad(ROBE_L, ROBE_D).bevel(0.15f);           // cowl
        head.box(-4.2f, -1, -4.6f, 8, 8, 2).color(ROBE_D).bevel(0.12f);
        Part jaw = head.add("jaw").at(0, 1.5f, 0.5f);
        jaw.box(-2.5f, -2, -3, 5, 2, 6).color(BONE).grad(BONE, 0xa8a8c0).bevel(0.12f).paint(Face.FRONT, 0, 1, 5, 1, 0x20202c);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 6.5f, 5, 0);
            arm.box(-1.5f, -7, -1.5f, 3, 7, 3).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.1f);
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -7, 0);
            hand.box(-1.5f, -4, -1.5f, 3, 4, 3).color(BONE).grad(BONE, 0xa8a8c0).bevel(0.12f);
            for (int i = 0; i < 3; i++) hand.box(-1.2f + i * 1.2f, -7, -0.5f, 1, 3, 1).color(BONE).flat();    // bony fingers
            hand.box(-1, -2, -1, 2, 2, 2).color(FIRE).flat().glow(Face.FRONT, 0, 0, 2, 2, FIRE);
        }
        // six flame orbs (top-level, positioned procedurally in a ring around the ghost)
        for (int i = 0; i < 6; i++) {
            Part o = m.add("orb" + i).at(0, 12, 0);
            o.box(-2, -2, -2, 4, 4, 4).color(FIRE).flat().glow(Face.FRONT, 0, 0, 4, 4, FIRE).glow(Face.TOP, 0, 0, 4, 4, FIRE_C)
                .glow(Face.LEFT, 0, 0, 4, 4, FIRE).glow(Face.RIGHT, 0, 0, 4, 4, FIRE).glow(Face.BACK, 0, 0, 4, 4, FIRE).glow(Face.BOTTOM, 0, 0, 4, 4, FIRE);
            o.box(-1, 2, -1, 2, 3, 2).color(FIRE_C).flat().glow(Face.FRONT, 0, 0, 2, 3, FIRE_C);          // flame tip
        }
        m.look("head", 50, 30).bob(1.3f, 0.1f);
        // orbit: radius grows when attacking moves converge/fire; ring spins faster as it is lit
        m.proc((r, c) -> {
            float spin = c.age * (c.move == null ? 3f : "divider".equals(c.move) ? 14f : "activate".equals(c.move) ? 9f : 5f);
            float rad = 15f;
            if ("divider".equals(c.move)) rad = 15f + 4f * (float) Math.sin(c.moveT * 0.35f);
            else if ("inferno".equals(c.move)) rad = 15f + 5f * (float) Math.sin(Math.min(c.moveT, 40f) * 0.2f);
            for (int i = 0; i < 6; i++) {
                double a = Math.toRadians(spin + i * 60);
                float lit = (c.var > i || "divider".equals(c.move) || "inferno".equals(c.move)) ? 1.25f : 0.8f;
                if ("activate".equals(c.move)) lit = c.moveT > i * 5 ? 1.25f : 0.5f;
                r.pos("orb" + i, (float) Math.cos(a) * rad, (float) Math.sin(c.age * 0.15f + i) * 1.5f, (float) Math.sin(a) * rad);
                r.scl("orb" + i, lit, lit, lit);
            }
        });
        Clip id = m.clip("idle", 60).loop();
        K.r(id, "torso", 0, 4, 0, 0, 30, 8, 0, 0, 60, 4, 0, 0);
        K.r(id, "head", 0, 0, 0, 0, 20, 4, 12, 0, 45, 4, -12, 0, 60, 0, 0, 0);
        K.r(id, "armR", 0, 6, 0, 10, 30, 12, 0, 16, 60, 6, 0, 10);
        K.r(id, "armL", 0, 6, 0, -10, 30, 12, 0, -16, 60, 6, 0, -10);
        K.r(id, "body", 0, 0, 0, 0, 30, 0, 4, 0, 60, 0, 0, 0);
        // ACTIVATE: rises from slumber, arms spread, head up, orbs ignite one by one
        Clip ac = m.clip("activate", 40);
        K.r(ac, "torso", 0, 32, 0, 0, 14, 20, 0, 0, 216, -14, 0, 0, 40, 0, 0, 0);
        K.r(ac, "head", 0, 40, 0, 0, 14, 25, 0, 0, 216, -40, 0, 0, 40, 0, 0, 0);
        K.r(ac, "armR", 0, 20, 0, 5, 16, -30, 0, 70, 30, -40, 0, 80, 40, 0, 0, 0);
        K.r(ac, "armL", 0, 20, 0, -5, 16, -30, 0, -70, 30, -40, 0, -80, 40, 0, 0, 0);
        K.r(ac, "jaw", 0, 0, 0, 0, 16, 40, 0, 0, 34, 40, 0, 0, 40, 0, 0, 0);
        K.p(ac, "body", 0, 0, -4, 0, 16, 0, 3, 0, 40, 0, 0, 0);
        // DIVIDER: lift both hands, hold in a tense pose while the ring spins up, then throw out six bolts (head jerks each time)
        Clip dv = m.clip("divider", 56);
        K.r(dv, "torso", 0, 4, 0, 0, 14, -20, 0, 0, 30, -18, 0, 0, 56, 0, 0, 0);
        K.r(dv, "head", 0, 0, 0, 0, 14, -30, 0, 0, 16, -30, 0, 0, 20, -20, 6, 0, 24, -30, -6, 0, 28, -20, 6, 0, 32, -30, -6, 0, 36, -20, 6, 0, 40, -30, -6, 0, 56, 0, 0, 0);
        K.r(dv, "armR", 0, 6, 0, 10, 14, -120, 0, 30, 20, -95, 0, 10, 24, -120, 0, 30, 28, -95, 0, 10, 32, -120, 0, 30, 36, -95, 0, 10, 40, -120, 0, 30, 56, 0, 0, 0);
        K.r(dv, "armL", 0, 6, 0, -10, 14, -120, 0, -30, 22, -95, 0, -10, 26, -120, 0, -30, 30, -95, 0, -10, 34, -120, 0, -30, 56, 0, 0, 0);
        K.r(dv, "jaw", 0, 0, 0, 0, 14, 40, 0, 0, 44, 40, 0, 0, 52, 0, 0, 0);
        K.p(dv, "body", 0, 0, 0, 0, 14, 0, 3, 0, 44, 0, 3, 0, 56, 0, 0, 0);
        // SEAR: right hand scoops a flame up then flings it forward
        Clip se = m.clip("sear", 32);
        K.r(se, "torso", 0, 4, 0, 0, 10, 12, -12, 0, 114, -10, 18, 0, 32, 0, 0, 0);
        K.r(se, "armR", 0, 6, 0, 10, 10, 30, 0, 20, 114, -105, 10, 0, 22, -100, 10, 0, 32, 0, 0, 0);
        K.r(se, "armL", 0, 6, 0, -10, 10, 20, 0, -30, 32, 0, 0, 0);
        K.r(se, "head", 0, 0, 0, 0, 10, 10, -10, 0, 114, -8, 12, 0, 32, 0, 0, 0);
        K.p(se, "body", 0, 0, 0, 0, 10, 0, 0, -2, 114, 0, 0, 3, 32, 0, 0, 0);
        // TACKLE: whole ghost lunges forward claws-first twice
        Clip ta = m.clip("tackle", 38);
        K.r(ta, "torso", 0, 4, 0, 0, 8, -20, 0, 0, 108, 30, 0, 0, 14, 8, 0, 0, 20, -20, 0, 0, 114, 30, 0, 0, 38, 0, 0, 0);
        K.r(ta, "armR", 0, 6, 0, 10, 8, 30, 0, 30, 108, -100, -10, -10, 14, -30, 0, 10, 20, 30, 0, 30, 114, -100, -10, -10, 38, 0, 0, 0);
        K.r(ta, "armL", 0, 6, 0, -10, 8, 30, 0, -30, 108, -100, 10, 10, 14, -30, 0, -10, 20, 30, 0, -30, 114, -100, 10, 10, 38, 0, 0, 0);
        K.p(ta, "body", 0, 0, 0, 0, 8, 0, 0, -4, 108, 0, -1, 7, 14, 0, 0, 0, 20, 0, 0, -3, 114, 0, -1, 7, 38, 0, 0, 0);
        K.r(ta, "jaw", 0, 0, 0, 0, 8, 35, 0, 0, 108, 0, 0, 0, 20, 35, 0, 0, 114, 0, 0, 0);
        // INFLAME: crossed arms burst open, body flares and swells
        Clip inf = m.clip("inflame", 36);
        K.r(inf, "armR", 0, 6, 0, 10, 12, -90, -50, -20, 216, -40, 0, 75, 36, 0, 0, 0);
        K.r(inf, "armL", 0, 6, 0, -10, 12, -90, 50, 20, 216, -40, 0, -75, 36, 0, 0, 0);
        K.r(inf, "head", 0, 0, 0, 0, 12, 25, 0, 0, 216, -35, 0, 0, 36, 0, 0, 0);
        K.s(inf, "torso", 0, 1, 1, 1, 12, 0.92f, 0.92f, 0.92f, 216, 1.18f, 1.18f, 1.18f, 36, 1, 1, 1);
        K.p(inf, "body", 0, 0, 0, 0, 12, 0, -1, 0, 216, 0, 3, 0, 36, 0, 0, 0);
        // INFERNO: ghost rears up, arms wide, ring blazes, hands flick six burning bolts
        Clip fe = m.clip("inferno", 60);
        K.r(fe, "torso", 0, 4, 0, 0, 14, -22, 0, 0, 52, -18, 0, 0, 60, 0, 0, 0);
        K.r(fe, "head", 0, 0, 0, 0, 14, -40, 0, 0, 52, -35, 0, 0, 60, 0, 0, 0);
        K.r(fe, "armR", 0, 6, 0, 10, 14, -150, 0, 60, 22, -110, 0, 50, 26, -150, 0, 60, 30, -110, 0, 50, 34, -150, 0, 60, 52, -150, 0, 60, 60, 0, 0, 0);
        K.r(fe, "armL", 0, 6, 0, -10, 14, -150, 0, -60, 24, -110, 0, -50, 28, -150, 0, -60, 32, -110, 0, -50, 36, -150, 0, -60, 52, -150, 0, -60, 60, 0, 0, 0);
        K.r(fe, "jaw", 0, 0, 0, 0, 14, 45, 0, 0, 52, 45, 0, 0, 60, 0, 0, 0);
        K.p(fe, "body", 0, 0, 0, 0, 14, 0, 5, 0, 52, 0, 6, 0, 60, 0, 0, 0);
        K.s(fe, "torso", 0, 1, 1, 1, 14, 1.12f, 1.12f, 1.12f, 52, 1.12f, 1.12f, 1.12f, 60, 1, 1, 1);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -14, 0, 5).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 10, 0).rot("head", 10, 0, 0, 0);
        Clip d = m.clip("death", 50);
        K.r(d, "torso", 0, 0, 0, 0, 12, -20, 0, 0, 50, 35, 0, 0);
        K.r(d, "head", 0, 0, 0, 0, 12, -40, 0, 0, 50, 40, 10, 0);
        K.r(d, "jaw", 0, 0, 0, 0, 12, 45, 0, 0, 50, 45, 0, 0);
        K.r(d, "armR", 0, 0, 0, 0, 14, -140, 0, 60, 50, -20, 0, 80);
        K.r(d, "armL", 0, 0, 0, 0, 14, -140, 0, -60, 50, -20, 0, -80);
        K.p(d, "body", 0, 0, 0, 0, 20, 0, 2, 0, 50, 0, -8, 0);
        K.s(d, "body", 0, 1, 1, 1, 30, 1, 1, 1, 50, 0.4f, 0.4f, 0.4f);
        return m;
    }

    public static MobDef def() {
        ProjKind.orb("hexaghost_flame", 0x4be8b8, 3).trail(ParticleTypes.SOUL_FIRE_FLAME).life(30);
        MobDef d = MobDef.of("hexaghost", "Hexaghost", "ヘキサゴースト")
            .game(1, 1).size(1.6f, 3.0f).hp(250).speed(0.18f).style(Style.GHOST).boss().flying().fireImmune().egg(0x2c2c46, 0x4be8b8)
            .model(Hexaghost::model)
            .pattern(s -> {
                if (s.getTurn() == 0) return "activate";
                if (s.getTurn() == 1) return "divider";
                return new String[] {"sear", "tackle", "sear", "inflame", "tackle", "sear", "inferno"}[(s.getTurn() - 2) % 7];
            })
            .move(Move.of("activate", 40).intent(Intent.UNKNOWN).noTarget()
                .sound(10, SoundEvents.BLAZE_AMBIENT, 1.4f, 0.5f)
                .at(30, (s, t) -> { Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 40, 1.2); Fx.sound(s, SoundEvents.BEACON_ACTIVATE, 1.2f, 0.6f); s.setVar(6); }))
            .move(Move.of("sear", 32).intent(Intent.ATTACK_DEBUFF, 6, 1).ranged(14)
                .sound(8, SoundEvents.FIRECHARGE_USE, 1f, 0.8f)
                .at(15, Fx.hitTarget(6, v -> Fx.curse(v, Fx.Curse.BURN))))
            .move(Move.of("tackle", 38).intent(Intent.ATTACK, 5, 2).reach(4.0f)
                .sound(6, SoundEvents.GHAST_SHOOT, 1f, 0.8f)
                .at(8, Fx.dash(0.5, 0.0)).at(9, Fx.melee(5, 3.6, 110))
                .at(16, Fx.dash(0.5, 0.0)).at(17, Fx.melee(5, 3.6, 110)))
            .move(Move.of("inflame", 36).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(12, SoundEvents.BLAZE_SHOOT, 1.2f, 0.5f)
                .at(16, (s, t) -> { s.addStrength(2); s.addBlock(12); Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 40, 1.0); }));
        // Divider: 6 beams from the ring, D fixed at 7 (player HP 80 / 12 + 1)
        Move dv = Move.of("divider", 56).intent(Intent.ATTACK, 7, 6).ranged(16).sound(8, SoundEvents.BEACON_POWER_SELECT, 1.2f, 0.6f);
        for (int i = 0; i < 6; i++) dv.at(20 + i * 4, Fx.hitTarget(7, null));
        d.move(dv);
        // Inferno: 6 x 2 damage, each leaves a Burn; ring blazes
        Move inf = Move.of("inferno", 60).intent(Intent.ATTACK_DEBUFF, 2, 6).ranged(16).sound(10, SoundEvents.BLAZE_SHOOT, 1.4f, 0.5f);
        for (int i = 0; i < 6; i++) inf.at(20 + i * 5, Fx.hitTarget(2, v -> Fx.curse(v, Fx.Curse.BURN)));
        d.move(inf);
        return d.onMoveEnd(s -> s.setVar(Math.min(6, (s.getTurn() % 7) + 1)));
    }
}
