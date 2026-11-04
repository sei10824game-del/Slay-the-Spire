package com.spiremod.mobs.s2ud2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Underdocks ELITE: Phantasmal Gardener (26-31 HP, Skittish 6: first hit each turn grants 6 Block). Cycle Bite 5 / Lash 7 / Flail 1x3 / Enlarge +2 Str; each of the four starts at a different step. */
public final class PhantasmalGardener {
    private PhantasmalGardener() {}
    static final int SK = 0x7fb8c8, SK_D = 0x3f7890, BEL = 0xd6f0e8, FIN = 0x9ae0d0, EYE = 0xfff080, TOOTH = 0xf4f4ff;
    static final String[] CYC = {"bite", "lash", "flail", "enlarge"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_phantasmal_gardener");
        m.scale(1.1f);
        Part body = m.add("body").at(0, 16, 0);
        body.box(-4, -4, -6, 8, 9, 12).color(SK).grad(SK, SK_D).noise(0.1f).bevel(0.15f).band(7, 2, BEL);
        body.box(-0.5f, 5, -5, 1, 4, 9).color(FIN).flat().glow(Face.LEFT, 0, 0, 9, 1, 0xd0fff0).glow(Face.RIGHT, 0, 0, 9, 1, 0xd0fff0);   // dorsal fin
        for (int s = -1; s <= 1; s += 2) {
            Part f = body.add(s < 0 ? "finR" : "finL").at(s * 4, -1, 1);
            f.box(s < 0 ? -5 : 0, -2, -2, 5, 1, 4).color(FIN).flat().bevel(0.2f);
        }
        Part neck = body.add("neck").at(0, 0, 6);
        neck.box(-3.5f, -3.5f, 0, 7, 8, 4).color(SK).grad(SK, SK_D).bevel(0.15f);
        Part head = neck.add("head").at(0, 0, 4);
        head.box(-3.5f, -1, 0, 7, 5, 6).color(SK).grad(SK, SK_D).bevel(0.15f)
            .glow(Face.LEFT, 4, 1, 1, 1, EYE).glow(Face.RIGHT, 1, 1, 1, 1, EYE);
        head.box(-4.1f, 0.5f, 1, 1, 2, 2).color(EYE).flat().glow(Face.LEFT, 0, 0, 1, 2, EYE);
        head.box(3.1f, 0.5f, 1, 1, 2, 2).color(EYE).flat().glow(Face.RIGHT, 0, 0, 1, 2, EYE);
        for (int i = 0; i < 3; i++) { head.box(-2.5f, -2, 1 + i * 1.7f, 1, 1, 1).color(TOOTH).flat(); head.box(1.5f, -2, 1 + i * 1.7f, 1, 1, 1).color(TOOTH).flat(); }
        Part jaw = head.add("jaw").at(0, -1, 0);
        jaw.box(-3, -2, 0, 6, 2, 6).color(BEL).grad(SK_D, BEL).bevel(0.15f);
        // lantern lure that hangs over the face (gardener's lamp)
        Part lure = head.add("lure").at(0, 5, 2).rot(-20, 0, 0);
        lure.box(-0.5f, 0, -0.5f, 1, 5, 1).color(SK_D);
        lure.box(-1.5f, 5, -1.5f, 3, 3, 3).color(0xfff6a0).flat().glow(Face.FRONT, 0, 0, 3, 3, 0xfff6a0).glow(Face.TOP, 0, 0, 3, 3, 0xfff6a0);
        // tail: three tapering segments
        Part t1 = body.add("t1").at(0, 0, -6);
        t1.box(-3, -3, -6, 6, 7, 6).color(SK).grad(SK, SK_D).bevel(0.15f).band(5, 1, BEL);
        Part t2 = t1.add("t2").at(0, 0, -6);
        t2.box(-2.2f, -2.2f, -6, 4, 5, 6).color(SK_D).grad(SK, SK_D).bevel(0.15f);
        Part t3 = t2.add("t3").at(0, 0, -6);
        t3.box(-1.2f, -1.2f, -5, 2, 3, 5).color(SK_D).bevel(0.15f);
        t3.box(-0.5f, -4, -9, 1, 9, 5).color(FIN).flat().glow(Face.LEFT, 0, 0, 5, 1, 0xd0fff0).glow(Face.RIGHT, 0, 0, 5, 1, 0xd0fff0);
        m.look("neck", 40, 25);
        m.bob(1.5f, 0.12f);
        m.proc((r, c) -> {
            float w = c.age * 0.18f;
            r.rot("t1", 0, (float) Math.sin(w) * 10, 0); r.rot("t2", 0, (float) Math.sin(w - 1) * 14, 0); r.rot("t3", 0, (float) Math.sin(w - 2) * 18, 0);
            r.rot("finR", 0, 0, (float) Math.sin(w * 1.5f) * 12); r.rot("finL", 0, 0, -(float) Math.sin(w * 1.5f) * 12);
        });
        m.clip("idle", 40).loop()
            .rot("neck", 0, 3, 0, 0).rot("neck", 20, -3, 0, 0).rot("neck", 40, 3, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 10, 0, 0).rot("jaw", 28, 0, 0, 0).rot("jaw", 40, 0, 0, 0)
            .rot("lure", 0, -20, 0, 6).rot("lure", 20, -10, 0, -6).rot("lure", 40, -20, 0, 6);
        m.clip("walk", 20).loop();
        // BITE: pull back, snap forward, jaws clamp
        m.clip("bite", 24)
            .rot("body", 0, 0, 0, 0).rot("body", 7, -16, 0, 0).rot("body", 11, 14, 0, 0, Ease.IN_CUBIC).rot("body", 24, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 7, 0, 1, -5).pos("body", 11, 0, -1, 7, Ease.IN_CUBIC).pos("body", 24, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 55, 0, 0).rot("jaw", 12, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 24, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 7, -18, 0, 0).rot("neck", 11, 20, 0, 0, Ease.IN_CUBIC).rot("neck", 24, 0, 0, 0)
            .rot("t1", 0, 0, 0, 0).rot("t1", 11, 0, 25, 0).rot("t1", 24, 0, 0, 0);
        // LASH: whip tail around in a big arc then head-butt sideways
        m.clip("lash", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 9, 0, -45, 0).rot("body", 14, 0, 60, 0, Ease.IN_CUBIC).rot("body", 30, 0, 0, 0)
            .rot("t1", 0, 0, 0, 0).rot("t1", 9, 0, 40, 0).rot("t1", 14, 0, -50, 0, Ease.IN_CUBIC).rot("t1", 30, 0, 0, 0)
            .rot("t2", 0, 0, 0, 0).rot("t2", 9, 0, 40, 0).rot("t2", 15, 0, -60, 0, Ease.IN_CUBIC).rot("t2", 30, 0, 0, 0)
            .rot("t3", 0, 0, 0, 0).rot("t3", 9, 0, 30, 0).rot("t3", 16, 0, -70, 0, Ease.IN_CUBIC).rot("t3", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 35, 0, 0).rot("jaw", 18, 0, 0, 0);
        // FLAIL: three quick jabbing nips, fins fluttering
        Clip fl = m.clip("flail", 30);
        fl.rot("neck", 0, 0, 0, 0).rot("jaw", 0, 0, 0, 0).pos("body", 0, 0, 0, 0);
        for (int i = 0; i < 3; i++) {
            int t = 5 + i * 7;
            fl.rot("neck", t, -14, i == 1 ? 20 : -20, 0).rot("neck", t + 3, 18, 0, 0, Ease.IN_CUBIC).rot("jaw", t, 40, 0, 0).rot("jaw", t + 3, 0, 0, 0, Ease.IN_CUBIC)
              .pos("body", t, 0, 0, -2).pos("body", t + 3, 0, 0, 3, Ease.IN_CUBIC);
        }
        fl.rot("neck", 30, 0, 0, 0).pos("body", 30, 0, 0, 0).rot("jaw", 30, 0, 0, 0);
        // ENLARGE: puffs up, glows, rises
        m.clip("enlarge", 36)
            .scl("body", 0, 1, 1, 1).scl("body", 12, 0.85f, 0.85f, 0.9f).scl("body", 20, 1.3f, 1.3f, 1.15f, Ease.OUT_BACK).scl("body", 30, 1.25f, 1.25f, 1.1f).scl("body", 36, 1, 1, 1)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, 5, 0, Ease.OUT_BACK).pos("body", 36, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 14, 20, 0, 0).rot("neck", 22, -30, 0, 0, Ease.OUT_BACK).rot("neck", 36, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 22, 45, 0, 0).rot("jaw", 32, 0, 0, 0)
            .rot("lure", 0, -20, 0, 0).rot("lure", 22, -70, 0, 0).rot("lure", 36, -20, 0, 0);
        m.clip("hurt", 10).rot("neck", 0, 0, 0, 0).rot("neck", 3, -20, 10, 0).rot("neck", 10, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 3, 0, 0, 12).rot("body", 10, 0, 0, 0).rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 35, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 20, 0, 0, 180, Ease.OUT_QUAD).rot("body", 30, 0, 0, 180)
            .pos("body", 0, 0, 0, 0).pos("body", 30, 0, -14, 0, Ease.OUT_BOUNCE)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 50, 0, 0).rot("t2", 0, 0, 0, 0).rot("t2", 30, 0, 50, 0).rot("t3", 0, 0, 0, 0).rot("t3", 30, 0, -60, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 30, 1, 0.8f, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_phantasmal_gardener", "Phantasmal Gardener", "幻影蟲")
            .game(2, 1).elite().flying().size(0.9f, 1.3f).hp(29).speed(0.22f).style(Style.AQUATIC).egg(0x7fb8c8, 0xfff080)
            .model(PhantasmalGardener::model)
            .spawn(1, 4, 4, MobDef.Habitat.BEACH)
            .onSpawn(s -> s.setCount("off", s.rand().nextInt(4)))
            .pattern(s -> CYC[(s.count("ct") + s.count("off")) % 4])
            .move(Move.of("bite", 24).intent(Intent.ATTACK, 5, 1).reach(2.4f).sound(6, SoundEvents.GUARDIAN_ATTACK, 0.6f, 1.6f).at(11, Fx.melee(5, 2.8, 70)))
            .move(Move.of("lash", 30).intent(Intent.ATTACK, 7, 1).reach(2.8f).sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.8f).at(14, Fx.melee(7, 3.2, 150)))
            .move(Move.of("flail", 30).intent(Intent.ATTACK, 1, 3).reach(2.4f)
                .at(8, Fx.melee(1, 2.8, 80)).at(15, Fx.melee(1, 2.8, 80)).at(22, Fx.melee(1, 2.8, 80))
                .sound(7, SoundEvents.PHANTOM_BITE, 0.6f, 1.5f).sound(14, SoundEvents.PHANTOM_BITE, 0.6f, 1.5f).sound(21, SoundEvents.PHANTOM_BITE, 0.6f, 1.5f))
            .move(Move.of("enlarge", 36).intent(Intent.BUFF).noTarget()
                .at(20, (s, t) -> { s.addStrength(2); Fx.sound(s, SoundEvents.PHANTOM_AMBIENT, 1.2f, 0.6f); Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 14, 0.5); }))
            .onMoveEnd(s -> { s.inc("ct"); s.flags.remove("skit"); })
            .onHurt((s, src, dmg) -> {
                if (dmg > 0 && s.flags.add("skit")) { s.addBlock(6); Fx.particles(s, ParticleTypes.SOUL, 6, 0.4); }   // Skittish 6
                return dmg;
            });
    }
}
