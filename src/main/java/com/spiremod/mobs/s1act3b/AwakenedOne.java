package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 boss Awakened One: 300 HP, two Cultists at its side.
 * Phase 1: Curiosity (+1 Str per power played), Regenerate 10. Slash 20 / Soul Strike 6x4 (first move Slash, 25% Slash / 75% Soul Strike, no 3 in a row).
 * On "death": Rebirth (back to full 300 HP, debuffs cleared, loses Curiosity-less regen). Phase 2: Dark Echo 40 (first), then 50/50 Sludge 18 (+Void) / Tackle 10x3.
 * Look: tall hooded pale-faced wraith in a layered indigo robe, red eyes, curved horns; phase 2 grows black-purple wings and a glowing chest eye.
 */
public final class AwakenedOne {
    private AwakenedOne() {}

    static final int ROBE = 0x35395f, ROBE_L = 0x4d5488, ROBE_D = 0x1b1d38, TRIM = 0xc9b25a, SKIN = 0xb9c6d8, SKIN_D = 0x8798b0,
        EYE = 0xff2a2a, HORN = 0xe8e0c8, WING = 0x2a1a46, WING_L = 0x5b2f8f, GLOW = 0x9be86a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("awakened_one"); m.scale(1.2f);
        Part skirt = m.add("skirt").at(0, 0, 0);
        skirt.cbox(0, 14, 15, 9).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.2f).noise(0.08f);
        skirt.cbox(0, 18, 5, 12).color(ROBE_D).grad(ROBE, ROBE_D).bevel(0.3f).noise(0.1f);
        skirt.cbox(4, 18, 1, 12).inflate(0.2f).color(TRIM).flat();
        skirt.box(-1, 5, 4.6f, 2, 10, 1).color(TRIM).flat();                                  // front panel
        Part body = m.add("body").at(0, 14, 0);
        Part torso = body.add("torso").at(0, 0, 0);
        torso.cbox(0, 12, 12, 7).color(ROBE).grad(ROBE_L, ROBE).bevel(0.2f).noise(0.06f);
        torso.cbox(8, 14, 3, 9).color(ROBE_D).bevel(0.25f);                                   // mantle
        torso.cbox(8.5f, 15, 1, 10).inflate(0.1f).color(TRIM).flat();
        torso.box(-0.5f, 0, 3.6f, 1, 8, 1).color(TRIM).flat();
        torso.box(-2, 4, 3.55f, 4, 4, 1).color(0x120a22).glow(Face.FRONT, 1, 1, 2, 2, GLOW);   // chest eye (glows)
        Part head = torso.add("head").at(0, 12, 0);
        head.cbox(0, 6, 6, 6).color(SKIN).grad(SKIN, SKIN_D).bevel(0.1f)
            .glow(Face.FRONT, 0, 2, 2, 1, EYE).glow(Face.FRONT, 4, 2, 2, 1, EYE).paint(Face.FRONT, 2, 4, 2, 1, SKIN_D);
        head.cbox(5, 8, 3, 8).color(ROBE_D).grad(ROBE, ROBE_D).bevel(0.2f);                       // hood cap
        head.box(-4, -1, -4.5f, 8, 7, 3).color(ROBE_D).bevel(0.2f);                              // hood back
        head.box(-4, 0, -3, 1, 6, 5).color(ROBE_D).bevel(0.1f); head.box(3, 0, -3, 1, 6, 5).color(ROBE_D).bevel(0.1f);
                head.box(-4, 4.5f, -4, 8, 1, 1).color(TRIM).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part horn = head.add(s < 0 ? "hornR" : "hornL").at(s * 3.5f, 6, 0).rot(0, 0, -s * 25);
            horn.box(-1, 0, -1, 2, 5, 2).color(HORN).bevel(0.15f);
            Part tip = horn.add(s < 0 ? "hornR2" : "hornL2").at(0, 5, 0).rot(0, 0, -s * 30);
            tip.box(-0.5f, 0, -0.5f, 1, 4, 1).color(HORN).flat();
        }
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 8, 10, 0);
            arm.box(-2, -7, -2, 4, 7, 4).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.15f);
            arm.box(-2.3f, -7, -2.3f, 5, 1, 5).color(TRIM).flat();
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -7, 0);
            fore.box(-1.5f, -6, -1.5f, 3, 6, 3).color(ROBE_D).bevel(0.1f);
            Part hand = fore.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1.5f, -2.5f, -1.5f, 3, 3, 3).color(SKIN).bevel(0.1f);
            for (int f = -1; f <= 1; f++) hand.box(f * 1.1f - 0.4f, -6, -0.4f, 1, 4, 1).color(HORN).flat();   // claws
        }
        // phase-2 wings: a ragged fan of dark feathers
        Part wings = torso.add("wings").at(0, 8, -3.5f);
        for (int s = -1; s <= 1; s += 2) {
            Part w = wings.add(s < 0 ? "wingR" : "wingL").at(s * 3, 0, 0).rot(0, s * -20, s * -25);
            for (int i = 0; i < 4; i++) {
                Part f = w.add((s < 0 ? "wR" : "wL") + i).at(0, 0, 0).rot(0, 0, s * (8 + 14 * i));
                f.box(s < 0 ? -22 + i * 2 : 1, -1.5f, 0, 21 - i * 2, 3, 1).color(i % 2 == 0 ? WING : WING_L).grad(WING_L, WING).bevel(0.15f);
            }
        }
        m.look("head", 50, 25);
        m.bob(0.8f, 0.05f);
        m.proc((rig, c) -> {
            rig.show("wings", c.phase >= 1);
            rig.rot("skirt", (float) Math.sin(c.age * 0.09f) * 1.5f, 0, 0);
            if (c.phase >= 1) {
                float f = (float) Math.sin(c.age * 0.12f) * 8;
                rig.rot("wingR", 0, f, 0); rig.rot("wingL", 0, -f, 0);
            }
        });
        m.clip("idle", 60).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 30, -1.5f, 0, 0).rot("torso", 60, 1.5f, 0, 0)
            .rot("armR", 0, 5, 0, 5).rot("armR", 30, 9, 0, 8).rot("armR", 60, 5, 0, 5)
            .rot("armL", 0, 5, 0, -5).rot("armL", 30, 9, 0, -8).rot("armL", 60, 5, 0, -5)
            .rot("head", 0, 0, 0, 0).rot("head", 30, 3, 0, 0).rot("head", 60, 0, 0, 0);
        m.clip("walk", 24).loop()
            .rot("skirt", 0, 0, 0, -2).rot("skirt", 12, 0, 0, 2).rot("skirt", 24, 0, 0, -2)
            .rot("torso", 0, 4, 0, 0).rot("torso", 12, 6, 0, 0).rot("torso", 24, 4, 0, 0);
        // SLASH: rear back, claws overhead, rake down and across
        m.clip("slash", 32)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -18, -20, 0, Ease.OUT_CUBIC).rot("torso", 14, 28, 25, 0, Ease.IN_CUBIC).rot("torso", 32, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -160, 0, 20, Ease.OUT_CUBIC).rot("armR", 14, -35, -30, -10, Ease.IN_CUBIC).rot("armR", 22, -30, -30, -10).rot("armR", 32, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 10, -30, 0, 0).rot("foreR", 14, -5, 0, 0, Ease.IN_CUBIC).rot("foreR", 32, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -40, 0, -20).rot("armL", 14, 25, 0, -15).rot("armL", 32, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -15, -15, 0).rot("head", 14, 15, 15, 0).rot("head", 32, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -3).pos("body", 14, 0, -2, 5, Ease.IN_CUBIC).pos("body", 32, 0, 0, 0);
        // SOUL STRIKE: four fast alternating thrusts of glowing hands
        m.clip("soul_strike", 46)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -12, 0, 0).rot("torso", 14, 10, 15, 0).rot("torso", 19, 10, -15, 0).rot("torso", 24, 10, 15, 0).rot("torso", 29, 10, -15, 0).rot("torso", 46, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -60, 0, 25).rot("armR", 14, -95, -10, 0, Ease.IN_CUBIC).rot("armR", 18, -40, 0, 10).rot("armR", 24, -95, -10, 0, Ease.IN_CUBIC).rot("armR", 28, -40, 0, 10).rot("armR", 46, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -60, 0, -25).rot("armL", 12, -40, 0, -10).rot("armL", 19, -95, 10, 0, Ease.IN_CUBIC).rot("armL", 23, -40, 0, -10).rot("armL", 29, -95, 10, 0, Ease.IN_CUBIC).rot("armL", 35, -40, 0, -10).rot("armL", 46, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -2).pos("body", 14, 0, 0, 3).pos("body", 29, 0, 0, 4).pos("body", 46, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -20, 0, 0).rot("head", 14, 10, 0, 0).rot("head", 46, 0, 0, 0);
        // REBIRTH: collapse into a heap, then burst upward with the wings unfurling
        m.clip("rebirth", 64)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, 40, 0, 0).rot("torso", 36, 55, 0, 0).rot("torso", 50, -25, 0, 0, Ease.OUT_BACK).rot("torso", 64, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -8, 3).pos("body", 36, 0, -9, 4).pos("body", 50, 0, 3, 0, Ease.OUT_BACK).pos("body", 64, 0, 0, 0)
            .scl("skirt", 0, 1, 1, 1).scl("skirt", 30, 1.15f, 0.55f, 1.15f).scl("skirt", 36, 1.15f, 0.55f, 1.15f).scl("skirt", 50, 0.95f, 1.1f, 0.95f, Ease.OUT_BACK).scl("skirt", 64, 1, 1, 1)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, 30, 0, 40).rot("armR", 36, 40, 0, 50).rot("armR", 50, -150, 0, 70, Ease.OUT_BACK).rot("armR", 64, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, 30, 0, -40).rot("armL", 36, 40, 0, -50).rot("armL", 50, -150, 0, -70, Ease.OUT_BACK).rot("armL", 64, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, 35, 0, 0).rot("head", 36, 40, 0, 0).rot("head", 50, -45, 0, 0, Ease.OUT_BACK).rot("head", 64, 0, 0, 0);
        // DARK ECHO: arms spread, head thrown back, shriek, then both fists throw the blast forward
        m.clip("dark_echo", 52)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -22, 0, 0, Ease.OUT_BACK).rot("torso", 26, 22, 0, 0, Ease.IN_CUBIC).rot("torso", 52, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -40, 0, 120, Ease.OUT_BACK).rot("armR", 26, -90, 0, 5, Ease.IN_CUBIC).rot("armR", 40, -90, 0, 5).rot("armR", 52, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -40, 0, -120, Ease.OUT_BACK).rot("armL", 26, -90, 0, -5, Ease.IN_CUBIC).rot("armL", 40, -90, 0, -5).rot("armL", 52, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -50, 0, 0, Ease.OUT_BACK).rot("head", 26, 20, 0, 0, Ease.IN_CUBIC).rot("head", 52, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 2, -3).pos("body", 26, 0, -1, 4).pos("body", 52, 0, 0, 0);
        // SLUDGE: hunch, gather, spew forward
        m.clip("sludge", 42)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, 25, 0, 0).rot("torso", 20, -10, 0, 0).rot("torso", 24, 35, 0, 0, Ease.IN_CUBIC).rot("torso", 42, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -20, 0, 0).rot("head", 24, 40, 0, 0, Ease.IN_CUBIC).rot("head", 42, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -50, 0, 35).rot("armR", 24, -110, 0, 5, Ease.IN_CUBIC).rot("armR", 42, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -50, 0, -35).rot("armL", 24, -110, 0, -5, Ease.IN_CUBIC).rot("armL", 42, 0, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 14, 1.12f, 0.95f, 1.12f).scl("torso", 24, 0.95f, 1.05f, 0.95f).scl("torso", 42, 1, 1, 1);
        // TACKLE: three shoulder lunges, one after the other
        m.clip("tackle", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -10, -20, 0).rot("torso", 14, 30, 20, 0, Ease.IN_CUBIC).rot("torso", 19, -5, 0, 0).rot("torso", 24, 30, -20, 0, Ease.IN_CUBIC).rot("torso", 29, -5, 0, 0).rot("torso", 34, 30, 20, 0, Ease.IN_CUBIC).rot("torso", 50, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -3).pos("body", 14, 0, -1, 6, Ease.IN_CUBIC).pos("body", 19, 0, 0, 0).pos("body", 24, 0, -1, 6, Ease.IN_CUBIC).pos("body", 29, 0, 0, 0).pos("body", 34, 0, -1, 6, Ease.IN_CUBIC).pos("body", 50, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -40, 0, 40).rot("armR", 14, -80, -20, 0).rot("armR", 34, -80, -20, 0).rot("armR", 50, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -40, 0, -40).rot("armL", 14, -80, 20, 0).rot("armL", 34, -80, 20, 0).rot("armL", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 20, 0, 0).rot("head", 50, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -14, 0, 4).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -18, 0, 8).rot("head", 10, 0, 0, 0);
        m.clip("death", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -25, 0, 0).rot("torso", 50, 40, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 30, 0, -10, 2, Ease.OUT_BOUNCE).pos("body", 50, 0, -11, 3)
            .scl("skirt", 0, 1, 1, 1).scl("skirt", 30, 1.2f, 0.4f, 1.2f).scl("skirt", 50, 1.3f, 0.25f, 1.3f)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -150, 0, 40).rot("armR", 50, 30, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -150, 0, -40).rot("armL", 50, 30, 0, -70)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -40, 0, 0).rot("head", 50, 35, 10, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("awakened_one", "Awakened One", "覚醒せし者")
            .game(1, 3).size(1.4f, 3.0f).hp(300).speed(0.24f).kbResist(0.9f).style(Style.MAGIC).egg(0x35395f, 0xff2a2a)
            .model(AwakenedOne::model).boss()
            .onSpawn(s -> {
                if (!s.flags.contains("duo")) { s.flags.add("duo"); Fx.summon(s, "awakened_cultist", -4, -1); Fx.summon(s, "awakened_cultist", 4, -1); }
            })
            .onHurt((s, src, d) -> {
                if (s.getPhase() == 0) {
                    if (s.getHealth() - d <= 0) {           // Rebirth instead of dying
                        s.setPhase(1);
                        s.setHealth(s.getMaxHealth());
                        S3.cleanse(s);
                        s.setCount("curiosity", 0);
                        s.cancelMove();
                        s.forceNext("rebirth");
                        return 0;
                    }
                    // Curiosity (approximation): +1 Strength for every 10 hits taken
                    if (s.inc("hits") % 10 == 0) { s.addStrength(1); Fx.particles(s, ParticleTypes.ENCHANT, 12, 0.6); }
                }
                return d;
            })
            .onMoveEnd(s -> { if (s.getPhase() == 0 && !s.lastMove().equals("rebirth")) s.heal(10); })   // Regenerate 10
            .pattern(s -> {
                if (s.lastMove().equals("rebirth") || (s.getPhase() == 1 && s.lastMove().isEmpty())) return "dark_echo";
                if (s.getPhase() == 0) {
                    if (s.lastMove().isEmpty()) return "slash";
                    boolean slash = s.rand().nextInt(100) < 25;
                    if (slash) return s.repeatCount("slash") >= 2 ? "soul_strike" : "slash";
                    return s.repeatCount("soul_strike") >= 2 ? "slash" : "soul_strike";
                }
                boolean sludge = s.rand().nextBoolean();
                if (sludge) return s.repeatCount("sludge") >= 2 ? "tackle" : "sludge";
                return s.repeatCount("tackle") >= 2 ? "sludge" : "tackle";
            })
            .move(Move.of("slash", 32).intent(Intent.ATTACK, 20, 1).reach(3.2f)
                .sound(8, SoundEvents.PHANTOM_SWOOP, 1f, 0.6f)
                .at(14, Fx.melee(20, 3.6, 100)))
            .move(Move.of("soul_strike", 46).intent(Intent.ATTACK, 6, 4).reach(3.2f)
                .sound(8, SoundEvents.ALLAY_AMBIENT_WITH_ITEM, 1.2f, 0.5f)
                .at(14, S3.multi(Fx.melee(6, 3.4, 90))).at(19, S3.multi(Fx.melee(6, 3.4, 90)))
                .at(24, S3.multi(Fx.melee(6, 3.4, 90))).at(29, S3.multi(Fx.melee(6, 3.4, 90)))
                .at(14, (s, t) -> Fx.particles(s, ParticleTypes.SOUL, 6, 0.5)).at(24, (s, t) -> Fx.particles(s, ParticleTypes.SOUL, 6, 0.5)))
            .move(Move.of("rebirth", 64).intent(Intent.UNKNOWN).noTarget()
                .sound(2, SoundEvents.WITHER_SPAWN, 0.8f, 0.6f)
                .at(36, (s, t) -> { Fx.sound(s, SoundEvents.ENDER_DRAGON_GROWL, 1.4f, 0.6f); Fx.ring(s, ParticleTypes.SOUL_FIRE_FLAME, s.position().add(0, 0.2, 0), 2.5, 40);
                    Fx.particles(s, ParticleTypes.PORTAL, 60, 1.0); }))
            .move(Move.of("dark_echo", 52).intent(Intent.ATTACK, 40, 1).ranged(14)
                .sound(10, SoundEvents.ENDER_DRAGON_GROWL, 1.5f, 0.5f)
                .at(26, Fx.hitTarget(40))
                .at(26, (s, t) -> { Fx.ring(s, ParticleTypes.SONIC_BOOM, s.position().add(0, 1.5, 0), 1.5, 4); Fx.sound(s, SoundEvents.WARDEN_SONIC_BOOM, 1.2f, 0.8f); }))
            .move(Move.of("sludge", 42).intent(Intent.ATTACK_DEBUFF, 18, 1).ranged(8)
                .sound(14, SoundEvents.SLIME_SQUISH, 1.2f, 0.5f)
                .at(24, Fx.hitTarget(18, v -> Fx.curse(v, Fx.Curse.VOID)))
                .at(24, (s, t) -> Fx.particlesAt(s, ParticleTypes.SQUID_INK, Fx.rel(s, 1.8, 0, 1.8), 25, 0.4, 0.15)))
            .move(Move.of("tackle", 50).intent(Intent.ATTACK, 10, 3).reach(3.4f).moving()
                .sound(8, SoundEvents.PHANTOM_FLAP, 1f, 0.5f)
                .at(12, Fx.dash(0.7, 0.05)).at(14, S3.multi(Fx.melee(10, 3.6, 100)))
                .at(22, Fx.dash(0.7, 0.05)).at(24, S3.multi(Fx.melee(10, 3.6, 100)))
                .at(32, Fx.dash(0.7, 0.05)).at(34, S3.multi(Fx.melee(10, 3.6, 100))));
    }
}
