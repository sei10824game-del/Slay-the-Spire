package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth: Flyconid 47-49 HP, a hovering fly-winged mushroom.
 * T1: Frail Spores (2/3) or Smash (1/3). After: Weakening Spores (Vulnerable 2) 3/6, Frail Spores (8 + Frail 2) 2/6, Smash (11) 1/6; never the same twice.
 */
public final class Flyconid {
    private Flyconid() {}

    static final int CAP = 0xc0622e, CAP_L = 0xe08a4a, CAP_D = 0x7a3418, SPOT = 0xf2e6c4, STEM = 0xe8d9b8, STEM_D = 0xb09a74, GILL = 0x8a5a3a, EYE = 0xffe14a, WING = 0xc8e4ee;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_flyconid");
        Part body = m.add("body").at(0, 14, 0);
        body.box(-3, -8, -3, 6, 9, 6).color(STEM).grad(STEM, STEM_D).bevel(0.1f).noise(0.05f)
            .glow(Face.FRONT, 1, 3, 1, 1, EYE).glow(Face.FRONT, 4, 3, 1, 1, EYE).paint(Face.FRONT, 2, 5, 2, 1, 0x6a3a22);
        body.box(-3.3f, -2, -3.3f, 7, 1, 7).color(STEM_D).flat();                       // ring on stem
        Part cap = body.add("cap").at(0, 1, 0);
        cap.box(-8, 0, -8, 16, 2, 16).color(GILL).grad(GILL, CAP_D).noise(0.15f);       // gills
        cap.box(-9, 2, -9, 18, 3, 18).color(CAP).grad(CAP_L, CAP).bevel(0.2f).noise(0.1f)
            .paint(Face.TOP, 3, 3, 3, 3, SPOT).paint(Face.TOP, 11, 5, 3, 3, SPOT).paint(Face.TOP, 7, 12, 4, 4, SPOT).paint(Face.TOP, 13, 12, 2, 2, SPOT).paint(Face.TOP, 2, 12, 2, 2, SPOT);
        cap.box(-6, 5, -6, 12, 3, 12).color(CAP_L).grad(CAP_L, CAP).bevel(0.2f).paint(Face.TOP, 4, 4, 2, 2, SPOT);
        cap.box(-3, 8, -3, 6, 1, 6).color(CAP_L).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part wing = body.add(s < 0 ? "wingR" : "wingL").at(s * 3, -2, -3);
            wing.box(s < 0 ? -9 : 0, 0, -1, 9, 1, 7).color(WING).flat().noise(0.05f).paint(Face.TOP, 0, 3, 9, 1, 0x9ec0cc);
            wing.box(s < 0 ? -7 : 0, 0.2f, 5, 7, 1, 4).color(WING).flat();
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 3.5f, -3, 0).rot(10, 0, s * 18);
            arm.box(-1, -6, -1, 2, 6, 2).color(STEM).grad(STEM, STEM_D);
            arm.box(-1.4f, -7, -1.4f, 3, 2, 3).color(GILL).bevel(0.1f);
        }
        for (int i = -1; i <= 1; i++) {
            Part ten = body.add("ten" + (i + 1)).at(i * 1.8f, -8, 0);
            ten.box(-0.5f, -5, -0.5f, 1, 5, 1).color(STEM_D).flat();
            Part t2 = ten.add("ten" + (i + 1) + "b").at(0, -5, 0);
            t2.box(-0.5f, -4, -0.5f, 1, 4, 1).color(STEM_D).flat();
        }
        m.look("body", 40, 25);
        m.bob(1.5f, 0.12f);
        m.proc((r, c) -> {
            float f = (float) Math.sin(c.age * 1.6f) * 40;
            r.rot("wingR", 0, 0, -f); r.rot("wingL", 0, 0, f);
            r.rot("ten0", (float) Math.sin(c.age * 0.2f) * 12, 0, 0); r.rot("ten1", (float) Math.sin(c.age * 0.2f + 1) * 12, 0, 0); r.rot("ten2", (float) Math.sin(c.age * 0.2f + 2) * 12, 0, 0);
        });
        Clip c = m.clip("idle", 40).loop();
        Kit.R(c, "cap", 0, 0, 0, 0, 20, 0, 0, 4, 40, 0, 0, 0);
        Kit.R(c, "body", 0, 2, 0, 0, 20, -2, 0, 0, 40, 2, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 20, 8, 0, 4, 40, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 20, 8, 0, -4, 40, 0, 0, 0);
        m.clip("walk", 20).loop();
        // WEAKENING SPORES: cap tilts back and shudders, spore puff blown out the front
        c = m.clip("weakening_spores", 36);
        Kit.R(c, "cap", 0, 0, 0, 0, 8, -18, 0, 0, 12, -20, 3, 4, 16, -20, -3, -4, 20, -20, 3, 4, 24, 14, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "body", 0, 0, 0, 0, 8, -14, 0, 0, 24, 12, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 8, -50, 0, 30, 24, -60, 0, 40, 36, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 8, -50, 0, -30, 24, -60, 0, -40, 36, 0, 0, 0);
        Kit.S(c, "cap", 0, 1, 1, 1, 10, 1.15f, 0.9f, 1.15f, 24, 0.9f, 1.1f, 0.9f, 36, 1, 1, 1);
        // FRAIL SPORES: leans forward and exhales a dense cloud (cap pumps twice)
        c = m.clip("frail_spores", 36);
        Kit.R(c, "body", 0, 0, 0, 0, 8, -10, 0, 0, 14, 18, 0, 0, 26, 14, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "cap", 0, 0, 0, 0, 8, -14, 0, 0, 14, 22, 0, 0, 36, 0, 0, 0);
        Kit.S(c, "cap", 0, 1, 1, 1, 8, 1.1f, 0.9f, 1.1f, 14, 0.85f, 1.15f, 0.85f, 20, 1.1f, 0.9f, 1.1f, 25, 0.9f, 1.1f, 0.9f, 36, 1, 1, 1);
        Kit.R(c, "armR", 0, 0, 0, 0, 14, -100, 0, 40, 28, -90, 0, 40, 36, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 14, -100, 0, -40, 28, -90, 0, -40, 36, 0, 0, 0);
        // SMASH: rises, slams the heavy cap straight down like a hammer
        c = m.clip("smash", 32);
        Kit.P(c, "body", 0, 0, 0, 0, 10, 0, 4, -2, -15, 0, -3, 6, 32, 0, 0, 0);
        Kit.R(c, "body", 0, 0, 0, 0, 10, -25, 0, 0, -15, 40, 0, 0, 22, 25, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "cap", 0, 0, 0, 0, 10, -20, 0, 0, -15, 25, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 10, -150, 0, 30, -15, -40, 0, 20, 32, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 10, -150, 0, -30, -15, -40, 0, -20, 32, 0, 0, 0);
        Kit.hurt(m, "body", -14, 8);
        c = m.clip("death", 30);
        Kit.R(c, "body", 0, 0, 0, 0, 10, -25, 0, 10, 30, 90, 0, 20);
        Kit.P(c, "body", 0, 0, 0, 0, 20, 0, -10, 0, 1030, 0, -13, 0);
        Kit.S(c, "cap", 0, 1, 1, 1, 24, 1.1f, 0.8f, 1.1f, 30, 1.2f, 0.6f, 1.2f);
        Kit.R(c, "armR", 0, 0, 0, 0, 20, -40, 0, 70, 30, 0, 0, 80);
        Kit.R(c, "armL", 0, 0, 0, 0, 20, -40, 0, -70, 30, 0, 0, -80);
        return m;
    }

    static Move.Action spores(int dmg, boolean frail, int color) {
        return (s, t) -> {
            net.minecraft.world.phys.Vec3 p = Fx.rel(s, 1.5, 0, 1.0);
            Fx.particlesAt(s, ParticleTypes.SPORE_BLOSSOM_AIR, p, 40, 0.8, 0.05);
            Fx.particlesAt(s, ParticleTypes.MYCELIUM, p, 25, 0.9, 0.05);
            if (t == null) return;
            Fx.particlesAt(s, ParticleTypes.SPORE_BLOSSOM_AIR, t.position().add(0, 1, 0), 30, 0.5, 0.02);
            if (dmg > 0) { t.invulnerableTime = 0; Fx.dmg(s, t, dmg); Fx.frail(t, 2 * Kit.TURN); } else Fx.vulnerable(t, 2 * Kit.TURN);
        };
    }

    public static MobDef def() {
        return MobDef.of("s2_flyconid", "Flyconid", "フライコニド")
            .game(2, 1).size(0.9f, 1.6f).hp(48).speed(0.2f).flying().style(Style.INSECT).egg(0xc0622e, 0xe8d9b8)
            .model(Flyconid::model)
            .spawn(6, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.random().add("weakening_spores", 3).add("frail_spores", 2).add("smash", 1)
                .notFirst("weakening_spores").maxRepeat("weakening_spores", 1).maxRepeat("frail_spores", 1).maxRepeat("smash", 1))
            .move(Move.of("weakening_spores", 36).intent(Intent.DEBUFF).ranged(8)
                .sound(14, SoundEvents.PUFFER_FISH_BLOW_OUT, 1.2f, 0.6f).at(24, spores(0, false, 0)))
            .move(Move.of("frail_spores", 36).intent(Intent.ATTACK_DEBUFF, 8, 1).ranged(8)
                .sound(14, SoundEvents.PUFFER_FISH_BLOW_OUT, 1.2f, 0.8f).at(15, spores(8, true, 0)))
            .move(Move.of("smash", 32).intent(Intent.ATTACK, 11, 1).reach(2.8f)
                .sound(15, SoundEvents.ANVIL_LAND, 0.6f, 1.4f).at(16, Fx.melee(11, 3.0, 100)));
    }
}
