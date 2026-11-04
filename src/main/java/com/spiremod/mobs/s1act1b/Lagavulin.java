package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 1 Elite Lagavulin: 109-111 HP. Starts ASLEEP with 8 Metallicize (gains 8 Block each turn). Wakes after 3 turns
 * or the moment it takes damage (losing Metallicize, stunned for a turn). Awake: Attack (18), Attack (18), Siphon Soul
 * (-1 Dexterity, -1 Strength to the player), repeat.
 */
public final class Lagavulin {
    private Lagavulin() {}
    static final int SKIN = 0xb8b4c4, SKIN_D = 0x6a6680, ARMOR = 0x5a6a86, ARMOR_D = 0x2e3a52, ARMOR_L = 0x8aa0c0, EYE = 0xffd830;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("lagavulin").scale(1.45f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 4, 7, 0);
            leg.box(-2.5f, -7, -2.5f, 5, 7, 5).color(ARMOR).grad(ARMOR, ARMOR_D).bevel(0.14f).noise(0.08f);
            leg.box(-3, -7, -3, 6, 2, 7).color(ARMOR_D).bevel(0.15f);
            leg.box(-3, -2.5f, -3, 6, 2, 6).color(ARMOR_L).flat().inflate(0.3f).bevel(0.2f);                       // knee plate
        }
        Part body = m.add("body").at(0, 7, 0);
        body.box(-5, 0, -3.5f, 10, 4, 7).color(ARMOR_D).grad(ARMOR, ARMOR_D);
        Part torso = body.add("torso").at(0, 4, 0).rot(15, 0, 0);                                                  // hunch at rest
        torso.box(-6.5f, 0, -4, 13, 9, 8).color(SKIN).grad(SKIN, SKIN_D).bevel(0.12f).noise(0.08f)
            .paint(Face.FRONT, 3, 1, 7, 6, 0x9c98ae);
        torso.box(-7, 5, -4.5f, 14, 4, 9).color(ARMOR).grad(ARMOR_L, ARMOR).bevel(0.15f).noise(0.06f).inflate(0.2f);   // breastplate
        torso.box(-0.5f, 1, -4.8f, 1, 8, 1).color(ARMOR_L).flat();
        // big domed shell on the back with spikes
        Part shell = torso.add("shell").at(0, 4, -4);
        shell.box(-7, -1, -6, 14, 9, 6).color(ARMOR).grad(ARMOR_L, ARMOR_D).bevel(0.16f).noise(0.08f);
        shell.box(-5, 8, -5.5f, 10, 2, 5).color(ARMOR).grad(ARMOR_L, ARMOR).bevel(0.16f);
        for (int i = 0; i < 5; i++) { Part sp = shell.add("spike" + i).at(-5 + i * 2.5f, 7 + (i % 2), -6).rot(-50, 0, 0); sp.box(-0.5f, 0, -0.5f, 1, 3, 1).color(0xcfd6e6).flat(); }
        Part head = torso.add("head").at(0, 9, 1.5f).rot(10, 0, 0);
        head.box(-3.5f, 0, -3.5f, 7, 6, 7).color(SKIN).grad(SKIN, SKIN_D).bevel(0.15f).noise(0.07f);
        head.box(-4, 3, -4, 8, 4, 8).color(ARMOR).grad(ARMOR_L, ARMOR_D).bevel(0.15f).inflate(0.1f);              // helm
        head.box(-0.5f, 7, -3, 1, 2, 6).color(ARMOR_L).flat();                                                      // crest
        Part eyes = head.add("eyes").at(0, 3, 3.6f);
        eyes.box(-3, 0, -0.1f, 2, 1, 1).color(EYE).flat().glow(Face.FRONT, 0, 0, 2, 1, EYE);
        eyes.box(1, 0, -0.1f, 2, 1, 1).color(EYE).flat().glow(Face.FRONT, 0, 0, 2, 1, EYE);
        Part jaw = head.add("jaw").at(0, 1.5f, 0.5f);
        jaw.box(-2.5f, -2, -3, 5, 2, 6).color(SKIN_D).bevel(0.12f).paint(Face.FRONT, 0, 1, 5, 1, 0x20181c);
        jaw.box(-2, 0, 2.6f, 1, 1, 1).color(0xf0ead8); jaw.box(1, 0, 2.6f, 1, 1, 1).color(0xf0ead8);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 8.5f, 8, 0);
            arm.box(-3, -2, -3, 6, 4, 6).color(ARMOR).grad(ARMOR_L, ARMOR).bevel(0.14f);                            // pauldron
            arm.box(-1.8f, -9, -1.8f, 4, 7, 4).color(SKIN).grad(SKIN, SKIN_D).bevel(0.1f);
            arm.box(-2.3f, -6, -2.3f, 4, 3, 4).color(ARMOR_D).flat().inflate(0.4f);
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -9, 0);
            hand.box(-2.5f, -5, -2.5f, 5, 5, 5).color(SKIN).grad(SKIN, SKIN_D).bevel(0.12f);
            hand.box(-2.5f, -5.5f, 2, 5, 1, 1).color(0xcfd6e6).flat();
        }
        m.look("head", 40, 20);
        m.walkBiped("legR", "legL", "armR", "armL", 30f, 20f);
        m.proc((r, c) -> r.show("eyes", c.phase > 0));
        // asleep: slumped forward over the knees, slow breathing
        Clip sl = m.clip("sleep", 60).loop();
        K.r(sl, "torso", 0, 40, 0, 0, 30, 43, 0, 0, 60, 40, 0, 0);
        K.r(sl, "head", 0, 40, 0, 0, 30, 36, 3, 0, 60, 40, 0, 0);
        K.p(sl, "body", 0, 0, -2.5f, 0, 30, 0, -2.2f, 0, 60, 0, -2.5f, 0);
        K.r(sl, "armR", 0, 25, 0, 10, 30, 28, 0, 12, 60, 25, 0, 10);
        K.r(sl, "armL", 0, 25, 0, -10, 30, 28, 0, -12, 60, 25, 0, -10);
        K.r(sl, "legR", 0, -25, 0, 0, 60, -25, 0, 0);
        K.r(sl, "legL", 0, -25, 0, 0, 60, -25, 0, 0);
        K.s(sl, "shell", 0, 1, 1, 1, 30, 1.03f, 1.03f, 1.03f, 60, 1, 1, 1);
        Clip id = m.clip("idle", 50).loop();
        K.r(id, "torso", 0, 4, 0, 0, 25, 8, 0, 0, 50, 4, 0, 0);
        K.r(id, "head", 0, 0, 0, 0, 15, 4, 10, 0, 35, 4, -10, 0, 50, 0, 0, 0);
        K.r(id, "armR", 0, 5, 0, 8, 25, 9, 0, 10, 50, 5, 0, 8);
        K.r(id, "armL", 0, 5, 0, -8, 25, 9, 0, -10, 50, 5, 0, -8);
        K.p(id, "body", 0, 0, 0, 0, 25, 0, -0.6f, 0, 50, 0, 0, 0);
        // WAKE: jolt, haul the body upright, roar, eyes light up
        Clip wk = m.clip("wake", 40);
        K.r(wk, "torso", 0, 40, 0, 0, 8, 50, 0, 0, 216, -10, 0, 0, 40, 0, 0, 0);
        K.r(wk, "head", 0, 40, 0, 0, 8, 50, 0, 0, 216, -35, 0, 0, 30, -25, 0, 0, 40, 0, 0, 0);
        K.r(wk, "jaw", 0, 0, 0, 0, 16, 0, 0, 0, 18, 40, 0, 0, 32, 40, 0, 0, 40, 0, 0, 0);
        K.r(wk, "armR", 0, 25, 0, 10, 16, -40, 0, 50, 22, -40, 0, 50, 40, 0, 0, 0);
        K.r(wk, "armL", 0, 25, 0, -10, 16, -40, 0, -50, 22, -40, 0, -50, 40, 0, 0, 0);
        K.r(wk, "legR", 0, -25, 0, 0, 14, 0, 0, 0);
        K.r(wk, "legL", 0, -25, 0, 0, 14, 0, 0, 0);
        K.p(wk, "body", 0, 0, -2.5f, 0, 10, 0, 1.5f, 0, 216, 0, 2, 0, 40, 0, 0, 0);
        // ATTACK: raise both fists high, then drop them in a heavy overhead hammer blow
        Clip at = m.clip("attack", 30);
        K.r(at, "torso", 0, 4, 0, 0, 11, -22, 0, 0, 114, 35, 0, 0, 22, 30, 0, 0, 30, 0, 0, 0);
        K.r(at, "head", 0, 0, 0, 0, 11, -25, 0, 0, 114, 20, 0, 0, 30, 0, 0, 0);
        K.r(at, "armR", 0, 5, 0, 8, 11, -170, 0, 15, 114, -55, 0, -5, 22, -50, 0, -5, 30, 0, 0, 0);
        K.r(at, "armL", 0, 5, 0, -8, 11, -170, 0, -15, 114, -55, 0, 5, 22, -50, 0, 5, 30, 0, 0, 0);
        K.p(at, "body", 0, 0, 0, 0, 11, 0, 1, -2, 114, 0, -2, 4, 22, 0, -2, 4, 30, 0, 0, 0);
        K.r(at, "jaw", 0, 0, 0, 0, 11, 30, 0, 0, 114, 0, 0, 0);
        // SIPHON SOUL: arms spread, head back, glowing eyes, hunches inward to drain
        Clip ss = m.clip("siphon_soul", 44);
        K.r(ss, "torso", 0, 4, 0, 0, 14, -18, 0, 0, 34, -14, 0, 0, 44, 0, 0, 0);
        K.r(ss, "head", 0, 0, 0, 0, 14, -35, 0, 0, 34, -30, 0, 0, 44, 0, 0, 0);
        K.r(ss, "armR", 0, 5, 0, 8, 14, -95, -40, 70, 24, -80, -20, 40, 34, -30, 0, 15, 44, 0, 0, 0);
        K.r(ss, "armL", 0, 5, 0, -8, 14, -95, 40, -70, 24, -80, 20, -40, 34, -30, 0, -15, 44, 0, 0, 0);
        K.r(ss, "jaw", 0, 0, 0, 0, 14, 35, 0, 0, 34, 35, 0, 0, 44, 0, 0, 0);
        K.s(ss, "torso", 0, 1, 1, 1, 14, 1.08f, 1.08f, 1.08f, 30, 1.08f, 1.08f, 1.08f, 44, 1, 1, 1);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 14, 0, 4).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 6, 0).rot("head", 10, 0, 0, 0);
        Clip d = m.clip("death", 36);
        K.r(d, "torso", 0, 0, 0, 0, 12, -25, 0, 0, 36, 50, 0, 0);
        K.r(d, "head", 0, 0, 0, 0, 12, -35, 0, 0, 36, 35, 10, 0);
        K.r(d, "jaw", 0, 0, 0, 0, 12, 40, 0, 0, 36, 20, 0, 0);
        K.r(d, "armR", 0, 0, 0, 0, 14, -60, 0, 40, 36, 15, 0, 55);
        K.r(d, "armL", 0, 0, 0, 0, 14, -60, 0, -40, 36, 15, 0, -55);
        K.r(d, "legR", 0, 0, 0, 0, 20, -50, 0, 6, 36, -80, 0, 8);
        K.r(d, "legL", 0, 0, 0, 0, 20, -60, 0, -6, 36, -85, 0, -8);
        K.p(d, "body", 0, 0, 0, 0, 20, 0, -3, -2, 36, 0, -6, -4);
        return m;
    }

    static void wake(SpireMob s) {
        if (s.getPhase() > 0) return;
        s.setPhase(1); s.setBase("idle"); s.setCount("metal", 0);
        s.addBlock(-s.getBlock());
        s.cancelMove(); s.forceNext("wake");
        Fx.sound(s, SoundEvents.RAVAGER_ROAR, 1.5f, 0.6f);
    }

    public static MobDef def() {
        return MobDef.of("lagavulin", "Lagavulin", "ラガヴーリン")
            .game(1, 1).size(1.2f, 2.0f).hp(110).speed(0.24f).style(Style.BRUTE).elite().egg(0x5a6a86, 0xffd830)
            .model(Lagavulin::model).spawn(1, 1, 1, MobDef.Habitat.MOUNTAIN).base("sleep")
            .pattern(s -> {
                if (s.getPhase() == 0) return "sleep_wait";
                String l = s.lastMove();
                if (l.equals("wake") || l.equals("siphon_soul") || l.equals("sleep_wait")) return "attack";
                return s.repeatCount("attack") >= 2 ? "siphon_soul" : "attack";
            })
            .move(Move.of("sleep_wait", 60).anim("sleep").intent(Intent.SLEEP).noTarget())
            .move(Move.of("wake", 40).intent(Intent.STUN).noTarget()
                .at(14, (s, t) -> Fx.ring(s, ParticleTypes.CLOUD, s.position(), 2.5, 20)))
            .move(Move.of("attack", 30).intent(Intent.ATTACK, 18, 1).reach(2.8f)
                .sound(11, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.6f)
                .at(14, Fx.melee(18, 3.0, 80)))
            .move(Move.of("siphon_soul", 44).intent(Intent.STRONG_DEBUFF).reach(8).ranged(8)
                .sound(14, SoundEvents.WITHER_AMBIENT, 1f, 0.7f)
                .at(22, (s, t) -> { if (t != null) { Fx.weak(t, 12); Fx.frail(t, 12); Fx.particlesAt(s, ParticleTypes.SOUL, t.position().add(0, 1, 0), 18, 0.4, 0.08); } }))
            .onMoveEnd(s -> {
                if (s.getPhase() == 0 && s.lastMove().equals("sleep_wait")) {
                    s.addBlock(8);                                   // Metallicize 8
                    if (s.inc("slept") >= 3) wake(s);
                }
            })
            .onHurt((s, src, dmg) -> { if (s.getPhase() == 0 && dmg > 0) wake(s); return dmg; });
    }
}
