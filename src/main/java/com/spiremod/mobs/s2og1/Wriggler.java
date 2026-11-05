package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth: Wriggler 17-21 HP (Dense Vegetation event / Phrog Parasite's brood). Stunned on its first turn. Nasty Bite (6), Wriggle (1 Infection + 2 Strength). */
public final class Wriggler {
    private Wriggler() {}

    static final int HIDE = 0xd88a8a, HIDE_L = 0xf0b0a8, HIDE_D = 0x9a5456, SPOT = 0x7a3a3e, TOOTH = 0xf4ecd0, MOUTH = 0x4a1018, EYE = 0xfff070;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_wriggler").scale(0.8f);
        Part s1 = m.add("seg1").at(0, 5, 3);
        seg(s1, 9, 0);
        Part head = s1.add("head").at(0, 0, 4);
        head.box(-4, -3.5f, 0, 8, 8, 6).color(HIDE).grad(HIDE_L, HIDE_D).bevel(0.3f).noise(0.06f)
            .glow(Face.FRONT, 0, 1, 2, 2, EYE).glow(Face.FRONT, 6, 1, 2, 2, EYE).paint(Face.FRONT, 1, 2, 1, 1, 0x201000).paint(Face.FRONT, 6, 2, 1, 1, 0x201000);
        head.box(-3, -3, 6, 6, 3, 1).color(MOUTH).flat();
        Part jaw = head.add("jaw").at(0, -3, 5);
        jaw.box(-3.5f, -2, -0.5f, 7, 2, 3).color(HIDE_D).grad(HIDE, HIDE_D).paint(Face.FRONT, 0, 0, 1, 1, TOOTH).paint(Face.FRONT, 3, 0, 1, 1, TOOTH).paint(Face.FRONT, 6, 0, 1, 1, TOOTH);
        for (int i = 0; i < 4; i++) head.box(-3 + i * 1.8f, -0.5f, 5.8f, 1, 1, 1).color(TOOTH).flat();
        Part s2 = s1.add("seg2").at(0, 0, -5);
        seg(s2, 8, 1);
        Part s3 = s2.add("seg3").at(0, 0, -5);
        seg(s3, 7, 0);
        Part s4 = s3.add("seg4").at(0, 0, -5);
        seg(s4, 5, 1);
        Part tip = s4.add("tip").at(0, 0, -4);
        tip.box(-1.5f, -1.5f, -5, 3, 3, 5).color(HIDE_D).bevel(0.2f);
        m.look("head", 40, 25);
        m.proc((r, c) -> {
            float w = c.age * 0.28f, a = 1 + c.swingAmt * 2;
            r.rot("seg1", 0, (float) Math.sin(w) * 8 * a, 0); r.rot("seg2", 0, (float) Math.sin(w - 1) * 14 * a, 0);
            r.rot("seg3", 0, (float) Math.sin(w - 2) * 18 * a, 0); r.rot("seg4", 0, (float) Math.sin(w - 3) * 22 * a, 0);
            r.rot("tip", 0, (float) Math.sin(w - 4) * 26 * a, 0);
        });
        Clip c = m.clip("idle", 30).loop();
        Kit.R(c, "head", 0, 0, 0, 0, 10, -5, 10, 0, 20, 5, -10, 0, 30, 0, 0, 0);
        Kit.S(c, "seg2", 0, 1, 1, 1, 15, 1.08f, 1.08f, 1.08f, 30, 1, 1, 1);
        m.clip("walk", 16).loop();
        // NASTY BITE: coils back then snaps forward with a vicious chomp
        c = m.clip("nasty_bite", 24);
        Kit.R(c, "seg1", 0, 0, 0, 0, 7, -20, 0, 0, -11, 25, 0, 0, 24, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 7, -20, 0, 0, -11, 20, 0, 0, 24, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 7, 55, 0, 0, -12, 0, 0, 0, 24, 0, 0, 0);
        Kit.P(c, "seg1", 0, 0, 0, 0, 7, 0, 0, -3, -11, 0, -1, 6, 24, 0, 0, 0);
        Kit.S(c, "seg3", 0, 1, 1, 1, 7, 1.15f, 1.15f, 0.8f, 24, 1, 1, 1);
        // WRIGGLE: writhes wildly side to side, bloating as it grows stronger
        c = m.clip("wriggle", 34);
        Kit.R(c, "seg1", 0, 0, 0, 0, 6, 0, 35, 0, 12, 0, -35, 0, 18, 0, 35, 0, 24, 0, -35, 0, 34, 0, 0, 0);
        Kit.R(c, "seg3", 0, 0, 0, 0, 6, 0, -45, 0, 12, 0, 45, 0, 18, 0, -45, 0, 24, 0, 45, 0, 34, 0, 0, 0);
        Kit.R(c, "tip", 0, 0, 0, 0, 6, 0, 60, 0, 12, 0, -60, 0, 18, 0, 60, 0, 24, 0, -60, 0, 34, 0, 0, 0);
        Kit.S(c, "seg2", 0, 1, 1, 1, 14, 1.35f, 1.35f, 1.1f, 30, 1.35f, 1.35f, 1.1f, 34, 1, 1, 1);
        Kit.R(c, "head", 0, 0, 0, 0, 10, -25, 0, 0, 28, -25, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 10, 40, 0, 0, 28, 40, 0, 0, 34, 0, 0, 0);
        Kit.hurt(m, "head", -25, 12);
        c = m.clip("death", 24);
        Kit.S(c, "seg1", 0, 1, 1, 1, 24, 1.3f, 0.3f, 1.3f);
        Kit.S(c, "seg2", 0, 1, 1, 1, 24, 1.3f, 0.3f, 1.3f);
        Kit.S(c, "seg3", 0, 1, 1, 1, 24, 1.3f, 0.3f, 1.3f);
        Kit.R(c, "head", 0, 0, 0, 0, 10, -30, 0, 0, 24, 30, 0, 0);
        return m;
    }

    private static void seg(Part p, int w, int alt) {
        p.box(-w / 2f, -4, -3.5f, w, 8, 7).color(HIDE).grad(HIDE_L, HIDE_D).bevel(0.3f).noise(0.06f);
        p.box(-w / 2f - 0.3f, -4, -3.8f, w + 1, 8, 2).color(HIDE_D).flat().inflate(0.1f);       // ring crease
        p.box(-1, 4, -2, 2, 1, 4).color(alt == 0 ? SPOT : HIDE_D).flat();
        p.box(-1.5f, -4, -1.5f, 3, 1, 3).color(HIDE_L).flat();
    }

    public static MobDef def() {
        return MobDef.of("s2_wriggler", "Wriggler", "ウリグラー")
            .game(2, 1).size(0.9f, 0.7f).hp(19).speed(0.25f).style(Style.INSECT).egg(0xd88a8a, 0x7a3a3e)
            .model(Wriggler::model)
            .spawn(0, 1, 1, MobDef.Habitat.JUNGLE)
            .onSpawn(s -> s.stun(30))
            .pattern(Pattern.cycle("nasty_bite", "wriggle"))
            .move(Move.of("nasty_bite", 24).intent(Intent.ATTACK, 6, 1).reach(2.2f)
                .sound(11, SoundEvents.FROG_EAT, 1.0f, 0.8f).at(12, Fx.melee(6, 2.4, 90)))
            .move(Move.of("wriggle", 34).intent(Intent.BUFF).noTarget()
                .sound(6, SoundEvents.SLIME_SQUISH, 1.0f, 0.7f)
                .at(14, (s, t) -> { Kit.str(s, 2); Fx.particles(s, ParticleTypes.SPORE_BLOSSOM_AIR, 10, 0.4); if (s.getTarget() != null) Kit.infection(s.getTarget()); }));
    }
}
