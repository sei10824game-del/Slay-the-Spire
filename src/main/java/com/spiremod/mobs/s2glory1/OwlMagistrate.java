package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Owl Magistrate. 234 HP. Cycle: Magistrate Scrutiny (16) -> Peck Assault (4 x6) -> Judicial Flight (gain Soar: takes 50% less damage)
 *  -> Verdict (33 + 4 Vulnerable, lands, removes Soar). */
public final class OwlMagistrate {
    private OwlMagistrate() {}
    static final int FEATH = 0x7a5a3c, FEATH_D = 0x4a3422, CREAM = 0xe8d8b0, ROBE = 0x1c1c24, WIG = 0xeeeeee, BEAK = 0xe0a830, EYE = 0xffd030;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_owl_magistrate");
        m.scale(1.5f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3, 9, 0);
            leg.box(-1, -9, -1, 2, 9, 2).color(0xc89a40);
            leg.box(-2.5f, -9, -3, 5, 1, 4).color(0xc89a40).bevel(0.1f);
            leg.box(-0.5f, -9, 1, 1, 1, 2).color(0x6a4a20);
        }
        Part body = m.add("body").at(0, 9, 0);
        body.box(-6, 0, -4.5f, 12, 13, 9).color(FEATH).grad(0x94704a, FEATH_D).noise(0.12f).bevel(0.1f);
        body.box(-4, 1, -4.8f, 8, 10, 1).color(CREAM).noise(0.1f);                                   // breast
        for (int i = 0; i < 4; i++) body.box(-3.5f, 2 + i * 2.2f, -5, 7, 1, 1).color(0xb89c70).flat(); // feather rows
        body.box(-6.4f, 8, -4.9f, 13, 5, 10).color(ROBE).grad(0x30303c, ROBE).inflate(0.1f);         // robe shoulders
        body.box(-2, 8, -5.2f, 4, 5, 1).color(0xf2f2f2).flat();                                       // jabot
        Part tail = body.add("tail").at(0, 2, 4.5f).rot(-30, 0, 0);
        tail.box(-3, -1, 0, 6, 10, 2).color(FEATH_D).grad(FEATH, FEATH_D);
        for (int s = -1; s <= 1; s += 2) {
            Part wing = body.add(s < 0 ? "wingR" : "wingL").at(s * 6, 11, 0);
            wing.box(s < 0 ? -3 : 0, -13, -3, 3, 14, 6).color(FEATH).grad(0x94704a, FEATH_D).noise(0.12f);
            wing.box(s < 0 ? -2.5f : -0.5f, -17, -2, 3, 5, 5).color(FEATH_D).noise(0.1f);
            Part tip = wing.add(s < 0 ? "tipR" : "tipL").at(0, -13, 0);
            tip.box(s < 0 ? -2.5f : -0.5f, -8, -2.5f, 3, 8, 5).color(0x3a2a1a).noise(0.1f);
        }
        Part head = body.add("head").at(0, 13, 0);
        head.box(-5, 0, -4, 10, 8, 8).color(FEATH).grad(0x94704a, FEATH_D).bevel(0.1f);
        head.box(-4.5f, 1, 3.6f, 9, 5, 1).color(CREAM).noise(0.06f)                                   // face disc
            .glow(Face.FRONT, 1, 2, 2, 2, EYE).glow(Face.FRONT, 6, 2, 2, 2, EYE).px(Face.FRONT, 1, 3, 0x201000).px(Face.FRONT, 7, 3, 0x201000);
        head.box(-4.8f, 5.5f, 3.8f, 4, 1, 1).color(0x2a2018).flat();                                  // brows
        head.box(0.8f, 5.5f, 3.8f, 4, 1, 1).color(0x2a2018).flat();
        Part beak = head.add("beak").at(0, 3, 4.4f);
        beak.box(-1, -2, 0, 2, 3, 3).color(BEAK).bevel(0.1f);
        beak.box(-0.5f, -3, 2, 1, 2, 1).color(0xb88020);
        Part wig = head.add("wig").at(0, 8, 0);
        wig.box(-5.5f, 0, -4.5f, 11, 3, 9).color(WIG).noise(0.15f).bevel(0.15f);
        for (int s = -1; s <= 1; s += 2) wig.box(s < 0 ? -6.5f : 5.5f, -6, -3, 1, 7, 6).color(WIG).noise(0.15f);
        Part gavel = m.byName.get("body").add("gavel").at(0, 0, 0); // judge's gavel hangs at the chest ribbon
        gavel.box(-1, 5, -6, 2, 3, 1).color(0xc89a40).flat();
        m.look("head", 70, 30);
        m.walkBiped("legR", "legL", null, null, 30f, 0f);
        m.proc((r, c) -> {
            if (c.phase == 1) { r.pos("body", 0, 8 + (float) Math.sin(c.age * 0.4f) * 1.5f, 0); r.rot("legR", -25, 0, 0); r.rot("legL", -25, 0, 0);
                float f = (float) Math.sin(c.age * 0.7f) * 25; r.rot("wingR", 0, 0, 50 + f); r.rot("wingL", 0, 0, -50 - f); }
        });
        m.clip("idle", 48).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 24, 1.03f, 1.04f, 1).scl("body", 48, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 0, 25, 8).rot("head", 22, 0, 25, 8).rot("head", 34, 0, -20, -6).rot("head", 44, 0, -20, -6).rot("head", 48, 0, 0, 0)
            .rot("wingR", 0, 0, 0, 4).rot("wingR", 24, 0, 0, 8).rot("wingR", 48, 0, 0, 4)
            .rot("wingL", 0, 0, 0, -4).rot("wingL", 24, 0, 0, -8).rot("wingL", 48, 0, 0, -4);
        // Scrutiny: lean back, glare, head snaps forward in a hard peck
        Clip a = m.clip("magistrate_scrutiny", 30);
        Kit.seq(a, "body", 30, 15, 8, -15, 0, 0, 15, 30, 0, 0, 22, 20, 0, 0);
        Kit.seq(a, "head", 30, 15, 8, -10, 0, 0, 15, 35, 0, 0, 22, 10, 0, 0);
        Kit.seq(a, "wingR", 30, -1, 8, 0, 0, 40, 15, 0, 0, 10);
        Kit.seq(a, "wingL", 30, -1, 8, 0, 0, -40, 15, 0, 0, -10);
        Kit.sp(a, "body", 30, 15, 8, 0, 0, -3, 15, 0, -1, 6);
        // Peck Assault: six rapid pecks
        Clip p = m.clip("peck_assault", 48);
        for (int t = 8; t <= 33; t += 5) { p.rot("head", t - 2, -10, 0, 0).rot("head", t, 40, 0, 0, Ease.IN_CUBIC); }
        p.rot("head", 0, 0, 0, 0).rot("head", 44, 0, 0, 0);
        for (int t = 8; t <= 33; t += 5) p.rot("body", t - 2, -4, 0, 0).rot("body", t, 14, 0, 0, Ease.IN_CUBIC);
        p.rot("body", 0, 0, 0, 0).rot("body", 44, 0, 0, 0);
        Kit.seq(p, "wingR", 48, -1, 6, 0, 0, 25, 36, 0, 0, 25);
        Kit.seq(p, "wingL", 48, -1, 6, 0, 0, -25, 36, 0, 0, -25);
        // Judicial Flight: crouch, huge wing beats, lift off
        Clip f = m.clip("judicial_flight", 36);
        Kit.seq(f, "wingR", 36, -1, 8, 0, 0, -10, 12, 0, 0, 80, 16, 0, 0, 10, 20, 0, 0, 80, 24, 0, 0, 10, 28, 0, 0, 80);
        Kit.seq(f, "wingL", 36, -1, 8, 0, 0, 10, 12, 0, 0, -80, 16, 0, 0, -10, 20, 0, 0, -80, 24, 0, 0, -10, 28, 0, 0, -80);
        Kit.seq(f, "body", 36, -1, 8, 14, 0, 0, 20, -14, 0, 0, 36, 0, 0, 0);
        Kit.sp(f, "body", 36, -1, 8, 0, -3, 0, 20, 0, 8, 0);
        Kit.seq(f, "head", 36, -1, 12, -25, 0, 0, 28, -25, 0, 0);
        // Verdict: wings spread high, dive down onto the target, talons forward
        Clip v = m.clip("verdict", 36);
        Kit.seq(v, "wingR", 36, 18, 8, 0, 0, 80, 14, 0, 0, 100, 18, 0, 0, 20, 28, 0, 0, 40);
        Kit.seq(v, "wingL", 36, 18, 8, 0, 0, -80, 14, 0, 0, -100, 18, 0, 0, -20, 28, 0, 0, -40);
        Kit.seq(v, "body", 36, 18, 8, -20, 0, 0, 14, -30, 0, 0, 18, 55, 0, 0, 28, 25, 0, 0);
        Kit.sp(v, "body", 36, 18, 8, 0, 4, 0, 14, 0, 12, -2, 18, 0, -1, 9, 28, 0, 0, 4);
        Kit.seq(v, "legR", 36, -1, 14, -50, 0, 0, 18, -80, 0, 0, 28, -40, 0, 0);
        Kit.seq(v, "legL", 36, -1, 14, -50, 0, 0, 18, -80, 0, 0, 28, -40, 0, 0);
        Kit.seq(v, "head", 36, 18, 14, -25, 0, 0, 18, 30, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -12, 0, 6).rot("body", 10, 0, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 3, -25, 12, 0).rot("head", 10, 0, 0, 0);
        m.clip("death", 32)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -20, 0, 0).rot("body", 24, 0, 0, 88, Ease.OUT_BOUNCE).rot("body", 32, 0, 0, 90)
            .pos("body", 0, 0, 0, 0).pos("body", 24, -3, -2, 0, Ease.OUT_BOUNCE).pos("body", 32, -3, -2, 0)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 12, 0, 0, 70).rot("wingR", 32, 0, 0, 40)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 12, 0, 0, -70).rot("wingL", 32, 0, 0, -40)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -35, 0, 0).rot("head", 32, 20, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 24, -50, 0, 0).rot("legL", 0, 0, 0, 0).rot("legL", 24, -60, 0, 0);
        return m;
    }

    public static MobDef def() {
        MobDef d = MobDef.of("s2_owl_magistrate", "Owl Magistrate", "オウルマジストレート")
            .game(2, 3).size(1.3f, 2.4f).hp(234).speed(0.26f).style(Style.BIRD).egg(0x7a5a3c, 0x1c1c24)
            .model(OwlMagistrate::model)
            .spawn(5, 1, 1, MobDef.Habitat.FOREST)
            .pattern(Pattern.cycle("magistrate_scrutiny", "peck_assault", "judicial_flight", "verdict"))
            .onHurt((s, src, dmg) -> s.getPhase() == 1 && src.getEntity() != null ? dmg * 0.5f : dmg)
            .move(Move.of("magistrate_scrutiny", 30).intent(Intent.ATTACK, 16, 1).reach(3.0f)
                .sound(8, SoundEvents.PARROT_IMITATE_PILLAGER, 1f, 0.6f).at(15, Fx.melee(16, 3.4, 80)))
            .move(Move.of("peck_assault", 48).intent(Intent.ATTACK, 4, 6).reach(3.0f)
                .atAll(new int[] {8, 13, 18, 23, 28, 33}, Kit.hit(4, 3.2, 80)));
        for (int t = 8; t <= 33; t += 5) d.moves.get("peck_assault").sound(t, SoundEvents.WOODEN_BUTTON_CLICK_ON, 0.8f, 1.6f);
        return d
            .move(Move.of("judicial_flight", 36).intent(Intent.BUFF).noTarget()
                .sound(8, SoundEvents.PHANTOM_FLAP, 1.2f, 0.7f).sound(16, SoundEvents.PHANTOM_FLAP, 1.2f, 0.7f).sound(24, SoundEvents.PHANTOM_FLAP, 1.2f, 0.7f)
                .at(14, (s, t) -> { s.setPhase(1); Fx.particles(s, ParticleTypes.CLOUD, 25, 0.8); }))
            .move(Move.of("verdict", 36).intent(Intent.ATTACK_DEBUFF, 33, 1).reach(7.0f)
                .sound(14, SoundEvents.ENDER_DRAGON_FLAP, 1.2f, 0.7f)
                .at(15, Fx.dash(0.8, -0.1))
                .at(18, (s, t) -> { Fx.meleeNow(s, 33, 3.6, 120, v -> Fx.vulnerable(v, 4 * Kit.TURN)); s.setPhase(0); Fx.ring(s, ParticleTypes.CLOUD, s.position(), 2.5, 24); }));
    }
}
