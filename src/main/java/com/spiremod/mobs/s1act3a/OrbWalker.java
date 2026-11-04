package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;

/**
 * StS1 Act 3 Orb Walker (90-96 HP). Strength Up: +3 Strength at the end of each turn.
 * 40% Laser (10 dmg + Burn), 60% Claw (15 dmg); never the same move 3 times in a row.
 * Look: a heavy bronze-and-iron walker on four arched legs carrying a big glowing orb in its chassis.
 */
public final class OrbWalker {
    private OrbWalker() {}

    static final int IRON = 0x4b4f5c, IRON_D = 0x2c2e38, BRONZE = 0xa87a3a, BRONZE_D = 0x6e4e24, ORB = 0xff7a2a, ORB_HI = 0xffe08a, RIVET = 0xd8c08a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("orb_walker");
        Part body = m.add("body").at(0, 18, 0);
        body.box(-7, -3, -7, 14, 8, 14).color(IRON).grad(0x6a6f80, IRON_D).bevel(0.2f).noise(0.08f)
            .band(1, 1, BRONZE).paint(Face.FRONT, 0, 7, 14, 1, IRON_D);
        body.box(-6, 5, -6, 12, 3, 12).color(BRONZE).grad(0xd0a050, BRONZE_D).bevel(0.2f);         // dome
        body.box(-4, 8, -4, 8, 2, 8).color(BRONZE_D).grad(BRONZE, BRONZE_D);
        body.box(-1, 10, -1, 2, 3, 2).color(IRON_D);                                                  // antenna mast
        body.box(-1.5f, 13, -1.5f, 3, 1, 3).color(RIVET).flat();
        body.box(-6, -5, -6, 12, 2, 12).color(IRON_D);                                                 // underside
        for (int s = -1; s <= 1; s += 2) {                                                             // exhaust vents
            body.box(s < 0 ? -9 : 7, 0, -3, 2, 4, 6).color(BRONZE_D).bevel(0.2f)
                .glow(s < 0 ? Face.RIGHT : Face.LEFT, 1, 1, 4, 1, 0xff5a20).glow(s < 0 ? Face.RIGHT : Face.LEFT, 1, 3, 4, 1, 0xff5a20);
        }
        Part orb = body.add("orb").at(0, 1, 6);
        orb.box(-4, -4, -2, 8, 8, 4).color(ORB).flat().glow(Face.FRONT, 0, 0, 8, 8, ORB).glow(Face.FRONT, 2, 2, 4, 4, ORB_HI)
            .glow(Face.LEFT, 0, 0, 4, 8, ORB).glow(Face.RIGHT, 0, 0, 4, 8, ORB).glow(Face.TOP, 0, 0, 8, 4, ORB).glow(Face.BOTTOM, 0, 0, 8, 4, ORB);
        orb.box(-5, -5, -2.5f, 10, 2, 2).color(BRONZE).bevel(0.2f);                                  // bronze housing ring
        orb.box(-5, 3, -2.5f, 10, 2, 2).color(BRONZE).bevel(0.2f);
        orb.box(-5, -3, -2.5f, 1, 6, 2).color(BRONZE);
        orb.box(4, -3, -2.5f, 1, 6, 2).color(BRONZE);
        Part lid = orb.add("lid").at(0, 4, 2);                                                         // shutter
        lid.box(-4, -2, -1, 8, 2, 1).color(IRON_D).bevel(0.2f);
        Part beam = orb.add("beam").at(0, 0, 2);
        beam.box(-1, -1, 0, 2, 2, 60).color(ORB_HI).flat().all(ORB_HI).glow(Face.TOP, 0, 0, 2, 60, ORB_HI).glow(Face.BOTTOM, 0, 0, 2, 60, ORB_HI)
            .glow(Face.LEFT, 0, 0, 60, 2, ORB_HI).glow(Face.RIGHT, 0, 0, 60, 2, ORB_HI).glow(Face.FRONT, 0, 0, 2, 2, ORB_HI);
        // ---- four arched legs: hip on chassis corner, thigh goes out/up, shin plunges down
        String[] n = {"legFR", "legFL", "legBR", "legBL"};
        float[][] p = {{-6, 5}, {6, 5}, {-6, -5}, {6, -5}};
        for (int i = 0; i < 4; i++) {
            float sx = Math.signum(p[i][0]), sz = Math.signum(p[i][1]);
            Part leg = body.add(n[i]).at(p[i][0], 0, p[i][1]).rot(0, 0, -sx * 50);
            leg.box(-1.5f, -1.5f, -1.5f, 3, 3, 3).color(BRONZE).bevel(0.25f);
            leg.box(sx < 0 ? -9 : 0, -1, -1, 9, 2, 2).color(IRON).grad(0x6a6f80, IRON_D);
            Part shin = leg.add(n[i] + "s").at(sx * 9, 0, 0).rot(0, 0, sx * 50);
            shin.box(-1.5f, -1.5f, -1.5f, 3, 3, 3).color(BRONZE).bevel(0.25f).paint(Face.FRONT, 1, 1, 1, 1, RIVET);
            shin.box(-1, -16, -1, 2, 15, 2).color(IRON).grad(0x6a6f80, IRON_D);
            shin.box(-1.5f, -18, -1.5f, 3, 2, 3).color(BRONZE_D).bevel(0.2f);
            shin.box(-0.5f, -19, sz > 0 ? 0 : -1, 1, 1, 2).color(RIVET).flat();
        }
        m.walkQuad("legFR", "legFL", "legBR", "legBL", 22f);
        m.proc((r, c) -> {
            boolean las = "laser".equals(c.move) && c.moveT >= 16 && c.moveT < 26;
            r.show("beam", las);
            float pulse = 1 + 0.06f * (float) Math.sin(c.age * 0.25f);
            r.scl("orb", pulse, pulse, 1);
        });
        m.clip("idle", 40).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, -0.8f, 0).pos("body", 40, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 0, 4, 0).rot("body", 30, 0, -4, 0).rot("body", 40, 0, 0, 0)
            .rot("lid", 0, 0, 0, 0).rot("lid", 30, 0, 0, 0).rot("lid", 32, 0, 0, 0).pos("lid", 0, 0, 0, 0).pos("lid", 30, 0, 0, 0).pos("lid", 32, 0, -6, 0).pos("lid", 35, 0, 0, 0);
        // LASER: crouch, orb shutters open and swells (charge), recoil as the beam fires, vents flare
        m.clip("laser", 36)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -3, -1).pos("body", 16, 0, -2, -3, Ease.IN_CUBIC).pos("body", 26, 0, -2, -2).pos("body", 36, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -8, 0, 0).rot("body", 16, -4, 0, 0, Ease.IN_CUBIC).rot("body", 26, -6, 0, 0).rot("body", 36, 0, 0, 0)
            .scl("orb", 0, 1, 1, 1).scl("orb", 14, 1.35f, 1.35f, 1.3f).scl("orb", 16, 0.9f, 0.9f, 1, Ease.IN_CUBIC).scl("orb", 26, 1.1f, 1.1f, 1).scl("orb", 36, 1, 1, 1)
            .pos("lid", 0, 0, 0, 0).pos("lid", 6, 0, 3, 0, Ease.OUT_BACK).pos("lid", 28, 0, 3, 0).pos("lid", 36, 0, 0, 0)
            .scl("beam", 16, 0.3f, 0.3f, 0.2f).scl("beam", 18, 1.4f, 1.4f, 1, Ease.OUT_CUBIC).scl("beam", 24, 1, 1, 1).scl("beam", 26, 0.2f, 0.2f, 1)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 12, 0, 0, -10).rot("legFR", 36, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 12, 0, 0, 10).rot("legFL", 36, 0, 0, 0)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 12, 0, 0, -10).rot("legBR", 36, 0, 0, 0)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 12, 0, 0, 10).rot("legBL", 36, 0, 0, 0);
        // CLAW: shift weight back, rear the front-right leg high and spear it down
        m.clip("claw", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -14, 0, 8).rot("body", 14, 12, 0, -4, Ease.IN_CUBIC).rot("body", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 2, -2).pos("body", 14, 0, -2, 3, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 10, -40, 30, -55).rot("legFR", 14, 10, 0, -10, Ease.IN_CUBIC).rot("legFR", 20, 8, 0, -10).rot("legFR", 30, 0, 0, 0)
            .rot("legFRs", 0, 0, 0, 0).rot("legFRs", 10, -60, 0, 0).rot("legFRs", 14, -30, 0, 0, Ease.IN_CUBIC).rot("legFRs", 30, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 10, -20, 0, 15).rot("legFL", 14, -15, 0, 0).rot("legFL", 30, 0, 0, 0)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 10, 15, 0, 0).rot("legBR", 30, 0, 0, 0)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 10, 15, 0, 0).rot("legBL", 30, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -8, 0, 5).rot("body", 10, 0, 0, 0)
            .scl("orb", 0, 1, 1, 1).scl("orb", 3, 0.8f, 0.8f, 1).scl("orb", 10, 1, 1, 1);
        m.clip("death", 36)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 1, 0).pos("body", 26, 0, -14, 0, Ease.IN_QUAD).pos("body", 36, 0, -13, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 26, 10, 0, 14).rot("body", 36, 10, 0, 14)
            .scl("orb", 0, 1, 1, 1).scl("orb", 8, 1.4f, 1.4f, 1).scl("orb", 18, 0.2f, 0.2f, 0.5f)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 26, 0, 0, 45).rot("legFL", 0, 0, 0, 0).rot("legFL", 26, 0, 0, -40)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 26, 0, 0, 40).rot("legBL", 0, 0, 0, 0).rot("legBL", 26, 0, 0, -45);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("orb_walker", "Orb Walker", "オーブウォーカー")
            .game(1, 3).size(1.6f, 1.9f).hp(93).speed(0.24f).style(Style.METAL).egg(0x4b4f5c, 0xff7a2a)
            .model(OrbWalker::model)
            .spawn(5, 1, 1, MobDef.Habitat.MOUNTAIN)
            .pattern(Pattern.random().add("laser", 40).add("claw", 60).maxRepeat("laser", 2).maxRepeat("claw", 2))
            .move(Move.of("laser", 36).intent(Intent.ATTACK_DEBUFF, 10, 1).ranged(12)
                .sound(4, SoundEvents.BEACON_ACTIVATE, 1f, 1.6f)
                .sound(16, SoundEvents.FIRECHARGE_USE, 1.2f, 0.8f)
                .at(17, (s, t) -> {
                    if (t == null) return;
                    Vec3 a = Fx.rel(s, 0.6, 0, 1.15), b = t.position().add(0, t.getBbHeight() * 0.6, 0);
                    for (int i = 0; i <= 16; i++) Fx.particlesAt(s, ParticleTypes.FLAME, a.lerp(b, i / 16.0), 1, 0.03, 0.0);
                    Fx.hitTarget(10, v -> Fx.curse(v, Fx.Curse.BURN)).run(s, t);
                }))
            .move(Move.of("claw", 30).intent(Intent.ATTACK, 15, 1).reach(2.4f)
                .sound(8, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.8f)
                .at(14, Fx.melee(15, 2.8, 70)))
            .onMoveEnd(s -> s.addStrength(3));
    }
}
