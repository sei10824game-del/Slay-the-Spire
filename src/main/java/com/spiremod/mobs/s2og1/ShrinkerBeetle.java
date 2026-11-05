package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth: Shrinker Beetle 38-40 HP. Shrinker (Shrink: attacks -30%) first, then Chomp (7) / Stomp (13) alternating. */
public final class ShrinkerBeetle {
    private ShrinkerBeetle() {}

    // ================================================================= SHRINKER BEETLE
    static final int SHELL = 0x7a4e2a, SHELL_L = 0xa8743c, SHELL_D = 0x4a2c18, SPOT = 0xe6cf94, CHITIN = 0x2e2018, MAND = 0xc9a45a, GLO = 0xb86cff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_shrinker_beetle");
        Part body = m.add("body").at(0, 6, 0);
        body.box(-5, -1, -7, 10, 3, 14).color(CHITIN).bevel(0.1f);                       // underbelly
        for (int s = -1; s <= 1; s += 2) {
            Part ely = body.add(s < 0 ? "elyR" : "elyL").at(s * 0.5f, 2, 6);               // wing case hinged at the rear-top
            ely.box(s < 0 ? -6 : 0, 0, -14, 6, 6, 15).color(SHELL).grad(SHELL_L, SHELL_D).bevel(0.15f).noise(0.06f)
                .paint(Face.TOP, s < 0 ? 4 : 1, 2, 1, 1, SPOT).paint(Face.TOP, s < 0 ? 2 : 3, 7, 1, 1, SPOT).paint(Face.TOP, s < 0 ? 4 : 1, 11, 1, 1, SPOT)
                .paint(Face.TOP, s < 0 ? 5 : 0, 0, 1, 15, SHELL_D);
        }
        Part head = body.add("head").at(0, 2, 7);
        head.box(-3.5f, -2, 0, 7, 5, 5).color(SHELL_D).grad(SHELL, CHITIN).bevel(0.1f)
            .glow(Face.FRONT, 0, 1, 1, 1, GLO).glow(Face.FRONT, 6, 1, 1, 1, GLO);
        head.box(-2, 3, 1, 4, 1, 3).color(SHELL_L).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part mad = head.add(s < 0 ? "madR" : "madL").at(s * 2, -0.5f, 5);
            mad.box(-1, -1, 0, 2, 2, 4).color(MAND).bevel(0.1f);
            Part tip = mad.add(s < 0 ? "madTipR" : "madTipL").at(0, 0, 4).rot(0, s * -30, 0);
            tip.box(-0.5f, -0.5f, 0, 1, 1, 3).color(0xf0e0b0).flat();
            Part ant = head.add(s < 0 ? "antR" : "antL").at(s * 2, 3, 4).rot(-40, s * -15, s * -25);
            ant.box(-0.5f, 0, -0.5f, 1, 5, 1).color(CHITIN);
            Part bulb = ant.add(s < 0 ? "bulbR" : "bulbL").at(0, 5, 0);
            bulb.box(-1, 0, -1, 2, 2, 2).color(GLO).glow(Face.FRONT, 0, 0, 2, 2, GLO).glow(Face.TOP, 0, 0, 2, 2, GLO);
        }
        String[] ln = {"legFR", "legFL", "legMR", "legML", "legBR", "legBL"};
        float[] lz = {5, 5, 0, 0, -5, -5};
        for (int i = 0; i < 6; i++) {
            int s = i % 2 == 0 ? -1 : 1;
            Part leg = body.add(ln[i]).at(s * 5, 0, lz[i]).rot(0, 0, s * -35);
            leg.box(-1, -4, -1, 2, 4, 2).color(CHITIN);
            Part low = leg.add(ln[i] + "2").at(0, -4, 0).rot(0, 0, s * 35);
            low.box(-1, -5, -1, 2, 5, 2).color(SHELL_D);
        }
        m.look("head", 30, 20);
        m.walkQuad("legFR", "legFL", "legBR", "legBL", 38);
        m.proc((r, c) -> { float w = (float) Math.sin(c.swing * 0.6662f) * c.swingAmt * 38; r.rot("legMR", -w, 0, 0); r.rot("legML", w, 0, 0); });
        Clip c = m.clip("idle", 40).loop();
        Kit.R(c, "madR", 0, 0, 0, 0, 20, 0, 12, 0, 40, 0, 0, 0);
        Kit.R(c, "madL", 0, 0, 0, 0, 20, 0, -12, 0, 40, 0, 0, 0);
        Kit.R(c, "antR", 0, 0, 0, 0, 10, 10, 0, 6, 30, -8, 0, -4, 40, 0, 0, 0);
        Kit.R(c, "antL", 0, 0, 0, 0, 10, 10, 0, -6, 30, -8, 0, 4, 40, 0, 0, 0);
        Kit.S(c, "body", 0, 1, 1, 1, 20, 1.02f, 1.04f, 1.02f, 40, 1, 1, 1);
        // SHRINKER: rears up, wing cases flare open, antenna bulbs pulse violet
        c = m.clip("shrinker", 40);
        Kit.R(c, "body", 0, 0, 0, 0, 10, -28, 0, 0, 14, -26, 0, 0, 30, -26, 0, 0, 40, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 10, 0, 2, -3, 30, 0, 2, -3, 40, 0, 0, 0);
        Kit.R(c, "elyR", 0, 0, 0, 0, 10, 0, 0, 0, 1014, 0, 0, -48, 30, 0, 0, -44, 40, 0, 0, 0);
        Kit.R(c, "elyL", 0, 0, 0, 0, 10, 0, 0, 0, 1014, 0, 0, 48, 30, 0, 0, 44, 40, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 10, 22, 0, 0, 16, 10, 0, 0, 40, 0, 0, 0);
        Kit.R(c, "antR", 0, 0, 0, 0, 12, -30, 0, 30, 30, -30, 0, 30, 40, 0, 0, 0);
        Kit.R(c, "antL", 0, 0, 0, 0, 12, -30, 0, -30, 30, -30, 0, -30, 40, 0, 0, 0);
        Kit.S(c, "bulbR", 0, 1, 1, 1, 14, 2.2f, 2.2f, 2.2f, 20, 1.3f, 1.3f, 1.3f, 26, 2.2f, 2.2f, 2.2f, 40, 1, 1, 1);
        Kit.S(c, "bulbL", 0, 1, 1, 1, 14, 2.2f, 2.2f, 2.2f, 20, 1.3f, 1.3f, 1.3f, 26, 2.2f, 2.2f, 2.2f, 40, 1, 1, 1);
        Kit.R(c, "legFR", 0, 0, 0, 0, 10, -45, 0, -10, 30, -45, 0, -10, 40, 0, 0, 0);
        Kit.R(c, "legFL", 0, 0, 0, 0, 10, -45, 0, 10, 30, -45, 0, 10, 40, 0, 0, 0);
        // CHOMP: mandibles spread, head snaps down and clamps
        c = m.clip("chomp", 28);
        Kit.R(c, "head", 0, 0, 0, 0, 8, -22, 0, 0, -12, 28, 0, 0, 18, 18, 0, 0, 28, 0, 0, 0);
        Kit.R(c, "madR", 0, 0, 0, 0, 8, 0, 40, 0, -12, 0, -22, 0, 18, 0, -22, 0, 28, 0, 0, 0);
        Kit.R(c, "madL", 0, 0, 0, 0, 8, 0, -40, 0, -12, 0, 22, 0, 18, 0, 22, 0, 28, 0, 0, 0);
        Kit.R(c, "body", 0, 0, 0, 0, 8, -8, 0, 0, -12, 14, 0, 0, 28, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 8, 0, 0, -2, -12, 0, -1, 5, 20, 0, 0, 3, 28, 0, 0, 0);
        // STOMP: front legs rise high, then crash down; shell jolts
        c = m.clip("stomp", 36);
        Kit.R(c, "body", 0, 0, 0, 0, 14, -30, 0, 0, -18, 22, 0, 0, 36, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 14, 0, 2, -3, -18, 0, -1, 6, 26, 0, 0, 3, 36, 0, 0, 0);
        Kit.R(c, "legFR", 0, 0, 0, 0, 14, -110, 0, -20, -18, 20, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "legFL", 0, 0, 0, 0, 14, -110, 0, 20, -18, 20, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "legFR2", 0, 0, 0, 0, 14, -30, 0, 0, 18, 10, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "legFL2", 0, 0, 0, 0, 14, -30, 0, 0, 18, 10, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "elyR", 0, 0, 0, 0, 18, 0, 0, 0, 1021, 0, 0, -14, 36, 0, 0, 0);
        Kit.R(c, "elyL", 0, 0, 0, 0, 18, 0, 0, 0, 1021, 0, 0, 14, 36, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 14, -20, 0, 0, -18, 25, 0, 0, 36, 0, 0, 0);
        Kit.hurt(m, "body", -10, 6);
        c = m.clip("death", 30);
        Kit.R(c, "body", 0, 0, 0, 0, 10, -15, 0, 0, 24, 0, 0, 175, 1030, 0, 0, 180);
        Kit.P(c, "body", 0, 0, 0, 0, 24, 0, -2, 0, 30, 0, -3, 0);
        for (String l : ln) Kit.R(c, l, 0, 0, 0, 0, 24, -50, 0, 0, 30, -60, 0, 0);
        Kit.R(c, "elyR", 0, 0, 0, 0, 24, 0, 0, -30, 30, 0, 0, -30);
        Kit.R(c, "elyL", 0, 0, 0, 0, 24, 0, 0, 30, 30, 0, 0, 30);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_shrinker_beetle", "Shrinker Beetle", "シュリンカービートル")
            .game(2, 1).size(1.1f, 0.8f).hp(39).speed(0.25f).style(Style.INSECT).egg(0x7a4e2a, 0xb86cff)
            .model(ShrinkerBeetle::model)
            .spawn(7, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.seq(1, "shrinker", "chomp", "stomp"))
            .move(Move.of("shrinker", 40).intent(Intent.DEBUFF).ranged(9)
                .sound(12, SoundEvents.BEACON_AMBIENT, 1.0f, 1.8f)
                .at(16, (s, t) -> { Fx.particles(s, ParticleTypes.WITCH, 25, 0.8); if (t != null) { Kit.effect(t, net.minecraft.world.effect.MobEffects.WEAKNESS, 3 * Kit.TURN * 2, 0); Fx.particlesAt(s, ParticleTypes.PORTAL, t.position().add(0, 1, 0), 30, 0.5, 0.2); } }))
            .move(Move.of("chomp", 28).intent(Intent.ATTACK, 7, 1).reach(2.4f)
                .sound(12, SoundEvents.SPIDER_HURT, 0.9f, 1.4f).at(14, Fx.melee(7, 2.6, 80)))
            .move(Move.of("stomp", 36).intent(Intent.ATTACK, 13, 1).reach(2.6f)
                .sound(18, SoundEvents.IRON_GOLEM_STEP, 1.2f, 0.6f).at(19, Fx.dash(0.3, 0.05)).at(20, Fx.melee(13, 2.8, 90)));
    }

}
