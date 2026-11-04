package com.spiremod.mobs.s2ud2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks BOSS: Lagavulin Matriarch (222 HP). Starts asleep with 12 Plating; sleeps 3 turns or until it takes damage through the plating, then loses it.
 * Cycle: Slash 19 / Disembowel 9x2 / Slash 12 + 12 Block / Soul Siphon (-2 Str, -2 Dex to the player, +2 Str).
 */
public final class LagavulinMatriarch {
    private LagavulinMatriarch() {}
    static final int SHELL = 0x6a5a78, SHELL_D = 0x3a2e48, SKIN = 0x8a9a7a, SKIN_D = 0x55654f, PLATE = 0xb8b0c8, EYE = 0xff7a30, CLAW = 0xe0dcc8, SOUL = 0x70e0d0;
    static final String[] CYC = {"slash", "disembowel", "slash2", "soul_siphon"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_lagavulin_matriarch");
        m.scale(1.7f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 5, 8, 0);
            leg.box(-2.5f, -8, -2.5f, 5, 8, 5).color(SKIN_D).grad(SKIN, SKIN_D).noise(0.15f).bevel(0.12f);
            leg.box(-3, -8, -4, 6, 2, 7).color(PLATE).noise(0.1f).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 8, 0);
        body.box(-6, 0, -4, 12, 5, 8).color(SKIN_D).grad(SKIN, SKIN_D).noise(0.15f).bevel(0.1f);
        Part torso = body.add("torso").at(0, 5, 0).rot(14, 0, 0);          // hunched forward
        torso.box(-7, 0, -4.5f, 14, 10, 9).color(SKIN).grad(SKIN, SKIN_D).noise(0.18f).bevel(0.1f)
            .paint(Face.FRONT, 1, 1, 12, 2, PLATE).paint(Face.FRONT, 3, 4, 8, 1, SHELL_D);
        // big spiral shell on the back
        Part shell = torso.add("shell").at(0, 5, -5);
        shell.box(-8, -6, -9, 16, 14, 9).color(SHELL).grad(0x84739a, SHELL_D).noise(0.2f).bevel(0.2f).band(4, 2, SHELL_D).band(9, 2, SHELL_D);
        shell.box(-5, 8, -7, 10, 4, 6).color(SHELL).grad(0x84739a, SHELL).bevel(0.2f);
        shell.box(-2.5f, 12, -5, 5, 3, 3).color(PLATE).bevel(0.2f).glow(Face.TOP, 1, 1, 3, 1, SOUL);
        for (int i = 0; i < 4; i++) shell.box(-8.4f + (i % 2) * 15.8f, -4 + i * 3, -7, 1, 2, 2).color(PLATE).flat();  // plating studs
        Part head = torso.add("head").at(0, 10, 2);
        head.box(-4, 0, -2, 8, 7, 8).color(SKIN).grad(SKIN, SKIN_D).noise(0.18f).bevel(0.12f)
            .glow(Face.FRONT, 1, 3, 2, 1, EYE).glow(Face.FRONT, 5, 3, 2, 1, EYE);
        head.box(-4.5f, 5, -2.5f, 9, 3, 9).color(PLATE).grad(PLATE, SHELL).bevel(0.15f);                  // helm
        head.box(-3.5f, -1, 5, 7, 3, 3).color(SKIN_D).bevel(0.15f);                                       // chin
        for (int s = -1; s <= 1; s += 2) head.box(s * 3.2f - 0.5f, -2, 6, 1, 3, 1).color(CLAW).flat();    // tusks
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 8.5f, 9, 0);
            arm.box(-2.5f, -9, -2.5f, 5, 9, 5).color(SKIN).grad(SKIN, SKIN_D).noise(0.15f).bevel(0.1f);
            arm.box(-3, -3, -3, 6, 3, 6).color(PLATE).bevel(0.15f);                                       // vambrace
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -9, 0);
            hand.box(-2, -4, -2, 4, 4, 4).color(SKIN_D).bevel(0.1f);
            for (int k = -1; k <= 1; k++) hand.box(k * 1.4f - 0.4f, -8, -0.5f, 1, 4, 1).color(CLAW).flat();   // three claws
        }
        m.look("head", 40, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 26f, 15f);
        m.clip("idle", 50).loop()
            .rot("torso", 0, 14, 0, 0).rot("torso", 25, 11, 0, 0).rot("torso", 50, 14, 0, 0)
            .rot("armR", 0, 6, 0, 8).rot("armR", 25, 10, 0, 10).rot("armR", 50, 6, 0, 8).rot("armL", 0, 6, 0, -8).rot("armL", 25, 10, 0, -10).rot("armL", 50, 6, 0, -8)
            .rot("head", 0, 0, 0, 0).rot("head", 25, 4, 5, 0).rot("head", 50, 0, 0, 0);
        // SLEEP: slumped over, head tucked, slow breathing, dim
        m.clip("sleep", 70).loop()
            .rot("torso", 0, 40, 0, 0).rot("torso", 35, 43, 0, 0).rot("torso", 70, 40, 0, 0)
            .pos("body", 0, 0, -3, 0).pos("body", 35, 0, -2.4f, 0).pos("body", 70, 0, -3, 0)
            .rot("head", 0, 35, 0, 0).rot("head", 35, 38, 0, 0).rot("head", 70, 35, 0, 0)
            .rot("armR", 0, 25, 0, 20).rot("armL", 0, 25, 0, -20).rot("legR", 0, -30, 0, 0).rot("legL", 0, -30, 0, 0)
            .scl("shell", 0, 1, 1, 1).scl("shell", 35, 1.04f, 1.04f, 1.04f).scl("shell", 70, 1, 1, 1);
        // WAKE: eyes snap open, rears up with a roar
        m.clip("wake", 50)
            .rot("torso", 0, 40, 0, 0).rot("torso", 16, 20, 0, 0).rot("torso", 26, -15, 0, 0, Ease.OUT_BACK).rot("torso", 50, 14, 0, 0)
            .pos("body", 0, 0, -3, 0).pos("body", 26, 0, 1, 0, Ease.OUT_BACK).pos("body", 50, 0, 0, 0)
            .rot("head", 0, 35, 0, 0).rot("head", 26, -35, 0, 0, Ease.OUT_BACK).rot("head", 50, 0, 0, 0)
            .rot("armR", 0, 25, 0, 20).rot("armR", 26, -140, 0, 60, Ease.OUT_BACK).rot("armR", 40, -130, 0, 60).rot("armR", 50, 6, 0, 8)
            .rot("armL", 0, 25, 0, -20).rot("armL", 26, -140, 0, -60, Ease.OUT_BACK).rot("armL", 40, -130, 0, -60).rot("armL", 50, 6, 0, -8)
            .rot("legR", 0, -30, 0, 0).rot("legR", 26, 0, 0, 0).rot("legL", 0, -30, 0, 0).rot("legL", 26, 0, 0, 0);
        // SLASH: big overhead-to-side claw rake with the right arm
        m.clip("slash", 34)
            .rot("torso", 0, 14, 0, 0).rot("torso", 10, 0, 35, 0).rot("torso", 15, 35, -40, 0, Ease.IN_CUBIC).rot("torso", 34, 14, 0, 0)
            .rot("armR", 0, 6, 0, 8).rot("armR", 10, -165, 0, 40).rot("armR", 15, -20, 0, -30, Ease.IN_CUBIC).rot("armR", 24, -10, 0, -25).rot("armR", 34, 6, 0, 8)
            .rot("armL", 0, 6, 0, -8).rot("armL", 10, 25, 0, -30).rot("armL", 15, -30, 0, -10).rot("armL", 34, 6, 0, -8)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -20, 25, 0).rot("head", 15, 20, -25, 0).rot("head", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -3).pos("body", 15, 0, -1, 5, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0);
        // DISEMBOWEL: two rapid cross-body gut rakes, left then right
        m.clip("disembowel", 38)
            .rot("armL", 0, 6, 0, -8).rot("armL", 8, 20, 0, 55).rot("armL", 11, -95, 0, -30, Ease.IN_CUBIC).rot("armL", 20, 6, 0, -8)
            .rot("armR", 0, 6, 0, 8).rot("armR", 20, 20, 0, -55).rot("armR", 24, -95, 0, 30, Ease.IN_CUBIC).rot("armR", 38, 6, 0, 8)
            .rot("torso", 0, 14, 0, 0).rot("torso", 8, 10, -25, 0).rot("torso", 11, 30, 25, 0, Ease.IN_CUBIC).rot("torso", 20, 10, 25, 0).rot("torso", 24, 30, -25, 0, Ease.IN_CUBIC).rot("torso", 38, 14, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 11, 15, 15, 0).rot("head", 24, 15, -15, 0).rot("head", 38, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 11, 0, 0, 4, Ease.IN_CUBIC).pos("body", 20, 0, 0, 0).pos("body", 24, 0, 0, 4, Ease.IN_CUBIC).pos("body", 38, 0, 0, 0);
        // SLASH2: raking slash then pulls the shell up as a guard
        m.clip("slash2", 38)
            .rot("armL", 0, 6, 0, -8).rot("armL", 10, -150, 0, -35).rot("armL", 14, -15, 0, 25, Ease.IN_CUBIC).rot("armL", 38, 6, 0, -8)
            .rot("torso", 0, 14, 0, 0).rot("torso", 10, -5, -30, 0).rot("torso", 14, 30, 30, 0, Ease.IN_CUBIC).rot("torso", 26, -4, 0, 0, Ease.OUT_BACK).rot("torso", 38, 14, 0, 0)
            .scl("shell", 0, 1, 1, 1).scl("shell", 28, 1.15f, 1.15f, 1.1f, Ease.OUT_BACK).scl("shell", 38, 1, 1, 1)
            .rot("armR", 0, 6, 0, 8).rot("armR", 22, -60, 0, 30).rot("armR", 30, -60, 0, 30).rot("armR", 38, 6, 0, 8)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 20, -20, 0).rot("head", 38, 0, 0, 0);
        // SOUL SIPHON: arms spread, head back, soul wisps drawn in
        m.clip("soul_siphon", 46)
            .rot("armR", 0, 6, 0, 8).rot("armR", 14, -80, 0, 75, Ease.OUT_BACK).rot("armR", 34, -80, 0, 75).rot("armR", 46, 6, 0, 8)
            .rot("armL", 0, 6, 0, -8).rot("armL", 14, -80, 0, -75, Ease.OUT_BACK).rot("armL", 34, -80, 0, -75).rot("armL", 46, 6, 0, -8)
            .rot("torso", 0, 14, 0, 0).rot("torso", 14, -10, 0, 0, Ease.OUT_BACK).rot("torso", 34, -8, 0, 0).rot("torso", 46, 14, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -40, 0, 0, Ease.OUT_BACK).rot("head", 34, -35, 0, 0).rot("head", 46, 0, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 22, 1.08f, 1.08f, 1.08f).scl("torso", 46, 1, 1, 1);
        m.clip("hurt", 10).rot("torso", 0, 14, 0, 0).rot("torso", 3, 24, 0, 5).rot("torso", 10, 14, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 3, -18, 0, 8).rot("head", 10, 0, 0, 0);
        m.clip("death", 50)
            .rot("torso", 0, 14, 0, 0).rot("torso", 20, 45, 0, 0).rot("torso", 50, 70, 0, 0, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 24, 0, -3, 0).pos("body", 50, 0, -6, 4, Ease.OUT_BOUNCE)
            .rot("head", 0, 0, 0, 0).rot("head", 30, 40, 0, 10).rot("armR", 0, 6, 0, 8).rot("armR", 36, 10, 0, 60).rot("armL", 0, 6, 0, -8).rot("armL", 36, 10, 0, -60)
            .rot("legR", 0, 0, 0, 0).rot("legR", 36, -60, 0, 0).rot("legL", 0, 0, 0, 0).rot("legL", 36, -50, 0, 0);
        return m;
    }

    static void wake(SpireMob s) {
        if (s.flags.add("awake")) { s.setBase("idle"); s.setCount("plating", 0); Fx.sound(s, SoundEvents.RAVAGER_ROAR, 1.5f, 0.6f); }
    }

    public static MobDef def() {
        return MobDef.of("s2_lagavulin_matriarch", "Lagavulin Matriarch", "ラガヴーリンの母")
            .game(2, 1).boss().size(1.7f, 3.0f).hp(222).speed(0.2f).style(Style.BEAST).egg(0x6a5a78, 0xff7a30)
            .model(LagavulinMatriarch::model).base("sleep")
            .pattern(s -> {
                if (!s.flags.contains("awake")) return "sleep";
                if (s.lastMove().equals("sleep")) return "wake";
                return CYC[s.count("ct") % 4];
            })
            .onSpawn(s -> s.setCount("plating", 12))
            .move(Move.of("sleep", 60).intent(Intent.SLEEP).noTarget())
            .move(Move.of("wake", 50).intent(Intent.BUFF).noTarget().sound(20, SoundEvents.RAVAGER_ROAR, 1.6f, 0.5f)
                .at(26, (s, t) -> Fx.particles(s, ParticleTypes.POOF, 20, 0.8)))
            .move(Move.of("slash", 34).intent(Intent.ATTACK, 19, 1).reach(3.4f).sound(10, SoundEvents.PLAYER_ATTACK_SWEEP, 1.2f, 0.6f).at(15, Fx.melee(19, 3.8, 120)))
            .move(Move.of("disembowel", 38).intent(Intent.ATTACK, 9, 2).reach(3.2f)
                .at(11, Fx.melee(9, 3.4, 100)).at(24, Fx.melee(9, 3.4, 100))
                .sound(10, SoundEvents.PLAYER_ATTACK_CRIT, 1f, 0.7f).sound(23, SoundEvents.PLAYER_ATTACK_CRIT, 1f, 0.7f))
            .move(Move.of("slash2", 38).intent(Intent.ATTACK_DEFEND, 12, 1).reach(3.4f).sound(10, SoundEvents.PLAYER_ATTACK_SWEEP, 1.2f, 0.6f)
                .at(14, Fx.melee(12, 3.8, 120)).at(24, Fx.block(12)))
            .move(Move.of("soul_siphon", 46).intent(Intent.DEBUFF).ranged(12)
                .sound(14, SoundEvents.SOUL_ESCAPE, 1.5f, 0.5f)
                .at(18, (s, t) -> {
                    for (var v : Fx.foes(s, s.getBoundingBox().inflate(12))) { Fx.weak(v, 20); Fx.frail(v, 20); Fx.ring(s, ParticleTypes.SOUL, v.position().add(0, 1, 0), 1.0, 14); }
                    s.addStrength(2);
                    Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 20, 0.7);
                }))
            .onMoveEnd(s -> {
                String l = s.lastMove();
                if (l.equals("sleep") && s.inc("slept") >= 3) wake(s);
                else if (!l.equals("sleep") && !l.equals("wake")) s.inc("ct");
            })
            .onHurt((s, src, dmg) -> {
                int pl = s.count("plating");
                if (!s.flags.contains("awake") && pl > 0) {          // Plating soaks damage while asleep; breaking through wakes it
                    float soak = Math.min(pl, dmg);
                    s.setCount("plating", pl - Math.round(soak));
                    if (dmg > soak) { wake(s); s.cancelMove(); }
                    return Math.max(0, dmg - soak);
                }
                return dmg;
            });
    }
}
