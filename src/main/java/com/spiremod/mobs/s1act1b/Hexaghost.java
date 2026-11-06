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
    static final int PUR = 0xa850b4, PUR_D = 0x6a2e74, PUR_L = 0xd078d8, MAG = 0xff80e8, MAG_L = 0xffb0f4, CREAM = 0xfaf6c0, CREAM_L = 0xffffe0, CREAM_D = 0x7a5a60, FIRE = 0x4be8b8, FIRE_C = 0xe8fff4;

    public static ModelSpec model() {
        // StS1 Hexaghost: a swirling magenta-purple ghost-mass with a cream hexagonal crystal core, six flames orbiting it
        ModelSpec m = new ModelSpec("hexaghost").scale(1.7f);
        Part body = m.add("body").at(0, 15, 0);
        int[] pw = {8, 13, 18, 22, 25, 27, 28, 28, 27, 25, 22, 18, 13, 8};            // voxel sphere, 2px layers
        for (int i = 0; i < pw.length; i++) {
            int w = pw[i], y0 = -14 + i * 2;
            float mid = 1f - Math.abs(i - 6.5f) / 6.5f;                                  // 0 at the poles, 1 at the equator
            int col = mid > 0.55f ? MAG : mid > 0.3f ? PUR : PUR_D;
            body.box(-w / 2f, y0, -w / 2f, w, 2, w).color(col).grad(mid > 0.55f ? MAG_L : PUR_L, col).bevel(0.3f).noise(0.1f)
                .paint(Face.FRONT, 1, 0, Math.max(1, w / 3), 1, PUR_D).paint(Face.FRONT, w / 2, 1, Math.max(1, w / 4), 1, MAG_L)
                .glow(Face.FRONT, 0, 0, w, 2, col).glow(Face.LEFT, 0, 0, w, 2, col).glow(Face.RIGHT, 0, 0, w, 2, col).glow(Face.BACK, 0, 0, w, 2, col);   // self-lit ghost-mass
        }
        Part mid = body.add("mid").at(0, 0, 0);                                      // inner swirl bands that can rotate (visible as streaks)
        for (int i = 0; i < 4; i++) mid.box(-15 + i * 2, -3 + i * 2, 14.4f - i * 0.2f, 4, 1, 2).color(MAG_L).flat();
        Part core = body.add("core").at(0, 0, 14.2f);                                // cream hexagon: three rotated slabs
        for (int k = 0; k < 3; k++) {
            Part p = core.add("hex" + k).at(0, 0, 0).rot(0, 0, k * 60);
            p.box(-4, -7, 0, 8, 14, 2).color(CREAM).grad(CREAM_L, CREAM).flat().glow(Face.FRONT, 0, 0, 8, 14, CREAM);
        }
        core.box(-6, -1, -1, 12, 2, 1).color(CREAM_D).flat();                       // shaded lower edge
        core.box(-1, -5, 2, 2, 3, 1).color(CREAM_L).flat().glow(Face.FRONT, 0, 0, 2, 3, CREAM_L);
        // six flame orbs (top-level, positioned procedurally in a ring around the ghost)
        for (int i = 0; i < 6; i++) {
            Part o = m.add("orb" + i).at(0, 14, 0);
            o.box(-2, -2, -2, 4, 4, 4).color(FIRE).flat().glow(Face.FRONT, 0, 0, 4, 4, FIRE).glow(Face.TOP, 0, 0, 4, 4, FIRE_C)
                .glow(Face.LEFT, 0, 0, 4, 4, FIRE).glow(Face.RIGHT, 0, 0, 4, 4, FIRE).glow(Face.BACK, 0, 0, 4, 4, FIRE).glow(Face.BOTTOM, 0, 0, 4, 4, FIRE);
            o.box(-1, 2, -1, 2, 3, 2).color(FIRE_C).flat().glow(Face.FRONT, 0, 0, 2, 3, FIRE_C);          // flame tip
        }
        m.bob(1.3f, 0.1f);
        // orbit: radius grows when attacking moves converge/fire; ring spins faster as it is lit
        m.proc((r, c) -> {
            float spin = c.age * (c.move == null ? 3f : "divider".equals(c.move) ? 14f : "activate".equals(c.move) ? 9f : 5f);
            float rad = 22f;
            if ("divider".equals(c.move)) rad = 22f + 4f * (float) Math.sin(c.moveT * 0.35f);
            else if ("inferno".equals(c.move)) rad = 22f + 5f * (float) Math.sin(Math.min(c.moveT, 40f) * 0.2f);
            for (int i = 0; i < 6; i++) {
                double a = Math.toRadians(spin + i * 60);
                float lit = (c.var > i || "divider".equals(c.move) || "inferno".equals(c.move)) ? 1.25f : 0.8f;
                if ("activate".equals(c.move)) lit = c.moveT > i * 5 ? 1.25f : 0.5f;
                r.pos("orb" + i, (float) Math.cos(a) * rad, (float) Math.sin(c.age * 0.15f + i) * 1.5f, (float) Math.sin(a) * rad);
                r.scl("orb" + i, lit, lit, lit);
            }
        });
        Clip id = m.clip("idle", 60).loop();                                         // slow breathing swell, the core turns gently
        K.s(id, "body", 0, 1, 1, 1, 30, 1.04f, 1.04f, 1.04f, 60, 1, 1, 1);
        K.r(id, "body", 0, 0, 0, 0, 30, 0, 8, 2, 60, 0, 0, 0);
        K.r(id, "mid", 0, 0, 0, 0, 30, 0, -30, 0, 60, 0, -60, 0);
        K.r(id, "core", 0, 0, 0, 0, 30, 0, 0, 6, 60, 0, 0, 0);
        // ACTIVATE: shrink, then flare up as the six flames ignite
        Clip ac = m.clip("activate", 40);
        K.s(ac, "body", 0, 1, 1, 1, 8, 0.7f, 0.7f, 0.7f, 30, 1.18f, 1.18f, 1.18f, 40, 1, 1, 1);
        K.r(ac, "core", 0, 0, 0, 0, 30, 0, 0, 360, 40, 0, 0, 360);
        K.r(ac, "mid", 0, 0, 0, 0, 40, 0, 360, 0);
        // DIVIDER: gathers energy (draws back, swells, core spins up), then fires six beams
        Clip dv = m.clip("divider", 56);
        K.s(dv, "body", 0, 1, 1, 1, 18, 1.2f, 1.2f, 1.2f, 22, 0.9f, 0.9f, 0.9f, 40, 1.1f, 1.1f, 1.1f, 56, 1, 1, 1);
        K.p(dv, "body", 0, 0, 0, 0, 18, 0, 2, -4, 22, 0, 0, 3, 56, 0, 0, 0);
        K.r(dv, "core", 0, 0, 0, 0, 20, 0, 0, 540, 56, 0, 0, 1080);
        K.r(dv, "mid", 0, 0, 0, 0, 56, 0, 720, 0);
        K.r(dv, "body", 0, 0, 0, 0, 18, -18, 0, 0, 22, 14, 0, 0, 56, 0, 0, 0);
        // SEAR: pulls back and spits a ball of fire
        Clip se = m.clip("sear", 32);
        K.p(se, "body", 0, 0, 0, 0, 10, 0, 0, -5, 114, 0, 0, 5, 32, 0, 0, 0);
        K.s(se, "body", 0, 1, 1, 1, 10, 0.9f, 0.9f, 1.15f, 14, 1.1f, 1.1f, 0.85f, 32, 1, 1, 1);
        K.r(se, "core", 0, 0, 0, 0, 14, 0, 0, 120, 32, 0, 0, 180);
        // TACKLE: two lunges, spinning into the target
        Clip ta = m.clip("tackle", 38);
        K.p(ta, "body", 0, 0, 0, 0, 6, 0, 0, -4, 108, 0, 0, 6, 14, 0, 0, -1, 116, 0, 0, 6, 38, 0, 0, 0);
        K.r(ta, "body", 0, 0, 0, 0, 6, -15, 0, 0, 108, 25, 0, 360, 116, 25, 0, 720, 38, 0, 0, 720);
        K.s(ta, "body", 0, 1, 1, 1, 8, 1.15f, 1.15f, 0.85f, 38, 1, 1, 1);
        // INFLAME: swells and shakes while blazing
        Clip inf = m.clip("inflame", 36);
        K.s(inf, "body", 0, 1, 1, 1, 16, 1.25f, 1.25f, 1.25f, 28, 1.2f, 1.2f, 1.2f, 36, 1, 1, 1);
        K.r(inf, "body", 0, 0, 0, 0, 16, 0, 0, 6, 20, 0, 0, -6, 24, 0, 0, 6, 28, 0, 0, -4, 36, 0, 0, 0);
        K.r(inf, "core", 0, 0, 0, 0, 36, 0, 0, 720);
        // INFERNO: rises, spins the whole mass, ring blazes, six flaming bursts
        Clip fe = m.clip("inferno", 60);
        K.p(fe, "body", 0, 0, 0, 0, 14, 0, 5, 0, 52, 0, 6, 0, 60, 0, 0, 0);
        K.s(fe, "body", 0, 1, 1, 1, 14, 1.15f, 1.15f, 1.15f, 52, 1.15f, 1.15f, 1.15f, 60, 1, 1, 1);
        K.r(fe, "body", 0, 0, 0, 0, 60, 0, 1080, 0);
        K.r(fe, "core", 0, 0, 0, 0, 60, 0, 0, 1440);
        Clip hu = m.clip("hurt", 10);
        K.s(hu, "body", 0, 1, 1, 1, 3, 0.85f, 0.85f, 0.85f, 10, 1, 1, 1);
        K.r(hu, "body", 0, 0, 0, 0, 3, -10, 0, 8, 10, 0, 0, 0);
        Clip d = m.clip("death", 50);
        K.r(d, "body", 0, 0, 0, 0, 50, 0, 540, 0);
        K.r(d, "core", 0, 0, 0, 0, 50, 0, 0, 900);
        K.p(d, "body", 0, 0, 0, 0, 20, 0, 2, 0, 50, 0, -10, 0);
        K.s(d, "body", 0, 1, 1, 1, 20, 1.1f, 1.1f, 1.1f, 50, 0.1f, 0.1f, 0.1f);
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
