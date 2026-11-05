package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Fabricator. 150 HP. While it has fewer than 4 living allies it picks Fabricate (summons 1 defensive bot: Guardbot/Noisebot + 1 aggressive bot:
 *  Zapbot/Stabbot) or Fabricating Strike (18 + 1 aggressive bot) at random; otherwise Disintegrate (11). Never repeats the same bot type twice in a row. */
public final class Fabricator {
    private Fabricator() {}
    static final int HULL = 0x7a8088, HULL_D = 0x40464e, ORANGE = 0xd8822a, CORE = 0x70e0ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_fabricator");
        m.scale(1.6f);
        for (int s = -1; s <= 1; s += 2) for (int z = -1; z <= 1; z += 2) {
            Part leg = m.add("leg" + (s < 0 ? "R" : "L") + (z < 0 ? "B" : "F")).at(s * 6, 8, z * 5);
            leg.box(-1.5f, -8, -1.5f, 3, 8, 3).color(HULL_D).grad(HULL, HULL_D).bevel(0.2f);
            leg.box(-2.5f, -8, -2.5f, 5, 1, 5).color(ORANGE).flat().bevel(0.2f);
        }
        Part body = m.add("body").at(0, 8, 0);
        body.box(-9, 0, -7, 18, 9, 14).color(HULL).grad(0x9aa0a8, HULL_D).bevel(0.2f).noise(0.1f);        // main housing
        body.box(-9.4f, 3, -7.4f, 19, 2, 15).color(ORANGE).flat().noise(0.05f);                              // warning stripe
        body.box(-6, 1, 7, 12, 6, 1).color(0x101418).glow(Face.FRONT, 1, 1, 10, 4, 0x40c0e0);               // front screen
        for (int i = 0; i < 5; i++) body.box(-5 + i * 2.5f, 2, 7.2f, 1, 3, 1).color(0xc0f4ff).flat();       // screen bars
        Part dome = body.add("dome").at(0, 9, 0);
        dome.box(-7, 0, -6, 14, 5, 12).color(HULL).grad(0xb0b6be, HULL_D).bevel(0.25f);
        dome.box(-3, 5, -3, 6, 4, 6).color(CORE).flat().glow(Face.FRONT, 0, 0, 6, 4, CORE).glow(Face.TOP, 0, 0, 6, 6, 0xffffff).glow(Face.LEFT, 0, 0, 6, 4, CORE);
        dome.box(-0.5f, 9, -0.5f, 1, 5, 1).color(HULL_D); dome.box(-1, 14, -1, 2, 2, 2).color(0xff4040).flat().glow(Face.FRONT, 0, 0, 2, 2, 0xff4040);
        for (int s = -1; s <= 1; s += 2) {
            Part hatch = body.add(s < 0 ? "hatchR" : "hatchL").at(s * 9, 9, 0);
            hatch.box(s < 0 ? -1 : 0, -9, -6, 1, 9, 12).color(HULL_D).grad(HULL, HULL_D).bevel(0.25f);
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 10, 5, 3);
            arm.box(-1.5f, -2, -1.5f, 3, 4, 3).color(HULL_D);
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -2, 0);
            fore.box(-1.5f, -10, -1.5f, 3, 10, 3).color(HULL).grad(0xa0a6ae, HULL_D).bevel(0.2f);
            Part tool = fore.add(s < 0 ? "toolR" : "toolL").at(0, -10, 0);
            if (s < 0) { tool.box(-2, -3, -2, 4, 3, 4).color(ORANGE).bevel(0.2f); tool.box(-3, -6, -0.5f, 2, 3, 1).color(0xc0c6ce).flat(); tool.box(1, -6, -0.5f, 2, 3, 1).color(0xc0c6ce).flat(); }
            else { tool.box(-1.5f, -3, -1.5f, 3, 3, 3).color(HULL_D); tool.box(-0.5f, -6, -0.5f, 1, 3, 1).color(0xffe080).flat().glow(Face.FRONT, 0, 0, 1, 3, 0xffe080); }
        }
        m.look("dome", 50, 20);
        m.bob(0.8f, 0.15f);
        m.clip("idle", 60).loop()
            .rot("armR", 0, 5, 0, 5).rot("armR", 30, 12, 0, 8).rot("armR", 60, 5, 0, 5)
            .rot("armL", 0, 12, 0, -8).rot("armL", 30, 5, 0, -5).rot("armL", 60, 12, 0, -8)
            .rot("foreR", 0, -10, 0, 0).rot("foreR", 20, -25, 0, 0).rot("foreR", 40, -10, 0, 0).rot("foreR", 60, -10, 0, 0)
            .rot("foreL", 0, -20, 0, 0).rot("foreL", 30, -5, 0, 0).rot("foreL", 60, -20, 0, 0)
            .rot("dome", 0, 0, 0, 0).rot("dome", 30, 0, 12, 0).rot("dome", 60, 0, 0, 0);
        m.walkQuad("legRF", "legLF", "legRB", "legLB", 30);
        for (String n : new String[] {"fabricate", "fabricating_strike"}) {
            boolean strike = n.endsWith("strike");
            Clip c = m.clip(n, strike ? 38 : 44);
            Kit.seq(c, "hatchR", c.length, -1, 6, 0, 0, 0, 16, 0, 0, 80, c.length - 8, 0, 0, 80);
            Kit.seq(c, "hatchL", c.length, -1, 6, 0, 0, 0, 16, 0, 0, -80, c.length - 8, 0, 0, -80);
            Kit.seq(c, "armR", c.length, strike ? 20 : -1, 8, strike ? -30 : -20, 0, 20, strike ? 20 : 20, strike ? 60 : -60, 0, 0);
            Kit.seq(c, "foreR", c.length, strike ? 20 : -1, 8, -40, 0, 0, strike ? 20 : 20, strike ? -10 : -60, 0, 0);
            Kit.seq(c, "armL", c.length, -1, 8, -20, 0, -20, 20, -50, 0, -10, c.length - 8, -40, 0, -10);
            c.scl("dome", 0, 1, 1, 1).scl("dome", 12, 1, 1.15f, 1).scl("dome", 18, 1, 0.9f, 1).scl("dome", c.length, 1, 1, 1);
            Kit.sp(c, "body", c.length, strike ? 20 : -1, 10, 0, -1, strike ? -3 : 0, 20, 0, strike ? -1 : 1, strike ? 6 : 0);
        }
        Clip d = m.clip("disintegrate", 36);
        Kit.seq(d, "dome", 36, -1, 8, -25, 0, 0, 14, -8, 0, 0, 26, -8, 0, 0);
        Kit.seq(d, "armR", 36, -1, 8, -60, 0, 20, 14, -90, 0, 10, 28, -90, 0, 10);
        Kit.seq(d, "armL", 36, -1, 8, -60, 0, -20, 14, -90, 0, -10, 28, -90, 0, -10);
        d.scl("dome", 0, 1, 1, 1).scl("dome", 14, 1.15f, 1.15f, 1.15f).scl("dome", 28, 1.15f, 1.15f, 1.15f).scl("dome", 36, 1, 1, 1);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -8, 0, 6).rot("body", 10, 0, 0, 0);
        m.clip("death", 36)
            .rot("body", 0, 0, 0, 0).rot("body", 14, 8, 0, 6).rot("body", 36, 14, 0, 12)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 2, 0).pos("body", 30, 0, -5, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 20, -30, 0, 50).rot("armL", 0, 0, 0, 0).rot("armL", 20, -30, 0, -50)
            .rot("hatchR", 0, 0, 0, 0).rot("hatchR", 12, 0, 0, 60).rot("hatchL", 0, 0, 0, 0).rot("hatchL", 12, 0, 0, -60)
            .scl("dome", 0, 1, 1, 1).scl("dome", 12, 1.2f, 1.2f, 1.2f).scl("dome", 36, 0.5f, 0.5f, 0.5f);
        return m;
    }

    static void spawn(SpireMob s, String[] ids, String key, double dx) {
        int last = s.count(key);
        int pick = last == 0 ? s.rand().nextInt(2) : (last == 1 ? 1 : 0);
        s.setCount(key, pick + 1);
        Fx.summon(s, ids[pick], dx, 1.8);
    }
    static final String[] DEF = {"s2_guardbot", "s2_noisebot"}, AGG = {"s2_zapbot", "s2_stabbot"};

    public static MobDef def() {
        return MobDef.of("s2_fabricator", "Fabricator", "ファブリケーター")
            .game(2, 3).size(1.6f, 2.8f).hp(150).speed(0.2f).style(Style.METAL).egg(0x7a8088, 0xd8822a)
            .model(Fabricator::model)
            .spawn(4, 1, 1, MobDef.Habitat.MOUNTAIN)
            .pattern(s -> s.minions().size() < 4 ? (s.rand().nextBoolean() ? "fabricate" : "fabricating_strike") : "disintegrate")
            .move(Move.of("fabricate", 44).intent(Intent.SUMMON).noTarget()
                .sound(12, SoundEvents.PISTON_EXTEND, 1f, 0.6f)
                .at(22, (s, t) -> { spawn(s, DEF, "ld", -2.0); spawn(s, AGG, "la", 2.0); Fx.particles(s, ParticleTypes.CLOUD, 20, 1.0); }))
            .move(Move.of("fabricating_strike", 38).intent(Intent.ATTACK, 18, 1).reach(3.4f)
                .sound(18, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.6f)
                .at(20, Fx.melee(18, 3.6, 100))
                .at(24, (s, t) -> { spawn(s, AGG, "la", 2.0); Fx.particles(s, ParticleTypes.CLOUD, 12, 0.8); }))
            .move(Move.of("disintegrate", 36).intent(Intent.ATTACK, 11, 1).ranged(10)
                .sound(14, SoundEvents.BEACON_POWER_SELECT, 1.2f, 1.6f)
                .at(16, (s, t) -> {
                    Fx.hitTarget(11).run(s, t);
                    if (t != null) for (int i = 1; i <= 8; i++) Fx.particlesAt(s, ParticleTypes.END_ROD, s.position().add(0, 2.4, 0).lerp(t.position().add(0, 1, 0), i / 8.0), 2, 0.05, 0.0);
                }));
    }
}
