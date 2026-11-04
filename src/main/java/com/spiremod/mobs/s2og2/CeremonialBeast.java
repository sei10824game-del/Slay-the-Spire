package com.spiremod.mobs.s2og2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth boss Ceremonial Beast (252 HP). Phase 1: Stamp (sets Plow 150), then Plow 18 + 2 Strength every turn.
 * When HP reaches the Plow threshold it is Stunned for a turn and loses all Strength. Phase 2 cycle: Beast Cry (Ringing:
 * one card/turn -> stand-in: Dazed on nearby foes) -> Stomp 15 -> Crush 17 + 3 Strength.
 */
public final class CeremonialBeast {
    private CeremonialBeast() {}

    static final int FUR = 0x3a3128, FUR_D = 0x211b16, FUR_L = 0x57493a, BONE = 0xe8e0c6, BONE_D = 0xb0a584, MOSS = 0x5d7f35, RUNE = 0x58e8d0, GOLD = 0xd9a93a, SASH = 0xb8372b;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_ceremonial_beast").scale(1.2f);
        // ---- legs
        String[] ln = {"FR", "FL", "BR", "BL"};
        for (int i = 0; i < 4; i++) {
            int s = (i % 2 == 0) ? -1 : 1; float z = i < 2 ? 10 : -10;
            Part leg = m.add("leg" + ln[i]).at(s * 7, 18, z);
            leg.box(-3.5f, -9, -3.5f, 7, 10, 8).color(FUR).grad(FUR_L, FUR_D).bevel(0.12f).noise(0.16f);
            Part shin = leg.add("shin" + ln[i]).at(0, -9, 0);
            shin.box(-2.5f, -9, -2.5f, 5, 9, 5).color(FUR_D).bevel(0.1f).noise(0.14f).band(5, 1, BONE_D);
            shin.box(-3, -9.5f, -3, 6, 2, 6).inflate(0.2f).color(BONE).flat();                        // bone anklet
            shin.box(-3, -12, -3.5f, 6, 3, 7).color(0x15110d).bevel(0.15f);                                // hoof
        }
        // ---- body
        Part body = m.add("body").at(0, 18, 0);
        body.box(-9, 0, -15, 18, 15, 30).color(FUR).grad(FUR_L, FUR_D).bevel(0.1f).noise(0.18f)
            .glow(Face.LEFT, 8, 3, 2, 8, RUNE).glow(Face.LEFT, 13, 5, 6, 2, RUNE).glow(Face.LEFT, 20, 3, 2, 8, RUNE)
            .glow(Face.RIGHT, 8, 3, 2, 8, RUNE).glow(Face.RIGHT, 13, 5, 6, 2, RUNE).glow(Face.RIGHT, 20, 3, 2, 8, RUNE);
        body.box(-8, 13, -14, 16, 3, 28).color(FUR_D).grad(FUR_L, FUR_D).noise(0.2f).inflate(0.2f);   // shaggy back
        body.box(-8.5f, 14, -13, 17, 2, 6).color(MOSS).noise(0.25f);
        body.box(-10, 5, 2, 20, 7, 3).color(SASH).flat().noise(0.05f).inflate(0.1f);                  // ceremonial sash
        body.box(-10, 5, 8, 20, 7, 1).color(GOLD).flat();
        for (int i = 0; i < 5; i++) body.box(-9.3f, -2 + (i % 2), -13 + i * 6, 2, 3, 3).color(BONE).flat(); // dangling charms
        Part tail = body.add("tail").at(0, 11, -15).rot(-30, 0, 0);
        tail.box(-2, 0, -2, 4, 9, 4).color(FUR).noise(0.2f);
        tail.box(-3, 8, -3, 6, 4, 6).color(FUR_D).noise(0.25f);
        // ---- plow yoke: a heavy bone-and-wood ram worn on the shoulders in phase 1
        Part plow = body.add("plow").at(0, 8, 15);
        plow.box(-12, -2, 0, 24, 10, 4).color(0x6a4a2c).grad(0x7d5934, 0x4a331d).bevel(0.15f).noise(0.14f);
        for (int i = -2; i <= 2; i++) plow.box(i * 4.5f - 1, -2, 4, 2, 4, 7).color(BONE).bevel(0.12f);
        plow.box(-12, 7, -1, 24, 2, 6).color(GOLD).flat();
        // ---- neck + head
        Part neck = body.add("neck").at(0, 10, 13).rot(35, 0, 0);
        neck.box(-5, 0, -5, 10, 15, 10).color(FUR).grad(FUR_L, FUR_D).bevel(0.1f).noise(0.2f);
        neck.box(-5.5f, 1, -6, 11, 12, 4).color(FUR_D).noise(0.25f);                                 // mane
        Part head = neck.add("head").at(0, 14, 0).rot(-35, 0, 0);
        head.box(-4, -1, -4, 8, 8, 9).color(BONE).grad(BONE, BONE_D).bevel(0.15f).noise(0.1f);        // skull cranium
        head.box(-3, 0.5f, 5, 6, 5, 7).color(BONE).grad(BONE, BONE_D).bevel(0.15f);                   // snout
        head.box(-2.2f, 1, 12, 4, 3, 1).color(0x2a2218).flat();                                          // nostril plate
        head.box(-4.2f, 3, 3, 8, 2, 3).color(BONE_D).bevel(0.2f);                                       // brow
        for (int s = -1; s <= 1; s += 2) {
            head.box(s * 3.8f - 0.5f, 3, 2.5f, 1, 3, 3).color(0x0c0c0c).glow(s < 0 ? Face.RIGHT : Face.LEFT, 0, 0, 1, 2, RUNE);   // eye sockets
            head.box(s * 4 - 1, 1, -2, 2, 3, 3).color(MOSS).noise(0.25f);
        }
        head.box(-3, 5, -1, 6, 1, 3).color(RUNE).flat().glow(Face.TOP, 0, 0, 6, 1, RUNE);               // forehead rune
        Part jaw = head.add("jaw").at(0, 0.5f, 3);
        jaw.box(-2.5f, -3, 0, 5, 3, 9).color(BONE_D).bevel(0.15f);
        for (int i = 0; i < 3; i++) jaw.box(-2.5f, 0, 1 + i * 3, 1, 2, 1).color(0xf4eed8).flat();
        // ---- antlers: huge branching bone, with bells
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part a0 = head.add("ant" + n).at(s * 2.5f, 7, -2).rot(-25, 0, s * -30);
            a0.box(-1.2f, 0, -1.2f, 3, 11, 3).color(BONE).bevel(0.1f).noise(0.1f);
            Part a1 = a0.add("antB" + n).at(0, 11, 0).rot(10, 0, s * -20);
            a1.box(-1, 0, -1, 2, 10, 2).color(BONE).bevel(0.1f);
            for (int k = 0; k < 3; k++) {
                Part t = a0.add("tine" + n + k).at(0, 3 + k * 3.5f, 0).rot(35 - k * 10, k * 20 * s, s * -55 + k * 8);
                t.box(-0.6f, 0, -0.6f, 1, 7 - k, 1).color(BONE).flat();
                Part bell = t.add("bell" + n + k).at(0, 7 - k, 0);
                bell.box(-1, -2, -1, 2, 2, 2).color(GOLD).flat().glow(Face.FRONT, 0, 0, 2, 1, 0xfff0a0);
            }
            Part t2 = a1.add("tipB" + n).at(0, 10, 0).rot(25, 0, s * -30);
            t2.box(-0.5f, 0, -0.5f, 1, 6, 1).color(BONE).flat();
        }
        m.look("head", 40, 25);
        m.walkQuad("legFR", "legFL", "legBR", "legBL", 28f);
        m.proc((rig, ctx) -> rig.show("plow", ctx.phase == 0));
        m.clip("idle", 50).loop()
            .rot("body", 0, 0, 0, 0).rot("body", 25, 1.5f, 0, 0).rot("body", 50, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 25, 4, 5, 0).rot("neck", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 25, -3, -4, 0).rot("head", 50, 0, 0, 0)
            .rot("tail", 0, 0, 0, 6).rot("tail", 25, 0, 0, -6).rot("tail", 50, 0, 0, 6);
        // STAMP: rear up on hind legs, roar, slam the front hooves down (sets Plow)
        m.clip("stamp", 46)
            .rot("body", 0, 0, 0, 0).rot("body", 14, -28, 0, 0, Ease.OUT_BACK).rot("body", 26, -26, 0, 0).rot("body", 30, 6, 0, 0, Ease.IN_CUBIC).rot("body", 46, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 6, -4).pos("body", 26, 0, 6, -4).pos("body", 30, 0, -1, 3, Ease.IN_CUBIC).pos("body", 46, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 14, -70, 0, 20).rot("legFR", 26, -70, 0, 20).rot("legFR", 30, 10, 0, 0, Ease.IN_CUBIC).rot("legFR", 46, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 14, -80, 0, -20).rot("legFL", 26, -80, 0, -20).rot("legFL", 30, 10, 0, 0, Ease.IN_CUBIC).rot("legFL", 46, 0, 0, 0)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 14, 25, 0, 0).rot("legBR", 46, 0, 0, 0)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 14, 25, 0, 0).rot("legBL", 46, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 14, -35, 0, 0).rot("neck", 26, -38, 0, 0).rot("neck", 30, 30, 0, 0, Ease.IN_CUBIC).rot("neck", 46, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 35, 0, 0).rot("jaw", 26, 35, 0, 0).rot("jaw", 30, 0, 0, 0).rot("jaw", 46, 0, 0, 0);
        // PLOW: lower the yoke, paw, charge forward and ram
        m.clip("plow", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 12, 10, 0, 0).rot("body", 18, -6, 0, 0, Ease.IN_CUBIC).rot("body", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -2, -4).pos("body", 18, 0, 1, 8, Ease.IN_CUBIC).pos("body", 40, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 12, 45, 0, 0).rot("neck", 18, 30, 0, 0).rot("neck", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 20, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 4, -50, 0, 0).rot("legFR", 8, 10, 0, 0).rot("legFR", 11, -50, 0, 0).rot("legFR", 14, 10, 0, 0).rot("legFR", 18, -35, 0, 0).rot("legFR", 40, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 6, 10, 0, 0).rot("legFL", 9, -50, 0, 0).rot("legFL", 12, 10, 0, 0).rot("legFL", 18, -35, 0, 0).rot("legFL", 40, 0, 0, 0)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 12, 35, 0, 0).rot("legBR", 18, -30, 0, 0).rot("legBR", 40, 0, 0, 0)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 12, 35, 0, 0).rot("legBL", 18, -30, 0, 0).rot("legBL", 40, 0, 0, 0);
        // STUN: loop base clip - slumped, head hung low, legs splayed
        m.clip("stun", 40).loop()
            .rot("body", 0, 8, 0, 4).rot("body", 20, 11, 0, -4).rot("body", 40, 8, 0, 4)
            .pos("body", 0, 0, -5, 0)
            .rot("neck", 0, 50, 0, 8).rot("neck", 20, 55, 0, -8).rot("neck", 40, 50, 0, 8)
            .rot("head", 0, 25, 0, 0).rot("jaw", 0, 20, 0, 0)
            .rot("legFR", 0, -15, 0, 25).rot("legFL", 0, -15, 0, -25).rot("legBR", 0, 15, 0, 20).rot("legBL", 0, 15, 0, -20);
        // BEAST CRY: head thrown back, jaw wide, antler bells shake
        Clip bc = m.clip("beast_cry", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -10, 0, 0, Ease.OUT_BACK).rot("body", 32, -8, 0, 0).rot("body", 40, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 12, -40, 0, 0, Ease.OUT_BACK).rot("neck", 32, -36, 0, 0).rot("neck", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -25, 0, 0).rot("head", 32, -25, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 40, 0, 0).rot("jaw", 32, 40, 0, 0).rot("jaw", 40, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 12, -20, 0, 8).rot("legFR", 40, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 12, -20, 0, -8).rot("legFL", 40, 0, 0, 0);
        for (int t = 14; t < 34; t += 4) { int k = (t / 4) % 2 == 0 ? 1 : -1;
            bc.rot("antR", t, 0, 0, 6 * k).rot("antL", t, 0, 0, -6 * k); }
        bc.rot("antR", 0, 0, 0, 0).rot("antL", 0, 0, 0, 0);
        // STOMP: lift one front hoof high, slam
        m.clip("stomp", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -10, 0, 0).rot("body", 14, 8, 0, 0, Ease.IN_CUBIC).rot("body", 30, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 10, -105, 0, 10, Ease.OUT_BACK).rot("legFR", 14, 15, 0, 0, Ease.IN_CUBIC).rot("legFR", 30, 0, 0, 0)
            .rot("shinFR", 0, 0, 0, 0).rot("shinFR", 10, 70, 0, 0).rot("shinFR", 14, 0, 0, 0, Ease.IN_CUBIC).rot("shinFR", 30, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 10, -20, 0, 0).rot("neck", 14, 25, 0, 0, Ease.IN_CUBIC).rot("neck", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 2, -2).pos("body", 14, 0, -1, 2, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0);
        // CRUSH: rear up high, both front legs hammer down
        m.clip("crush", 44)
            .rot("body", 0, 0, 0, 0).rot("body", 16, -36, 0, 0, Ease.OUT_BACK).rot("body", 24, -34, 0, 0).rot("body", 28, 10, 0, 0, Ease.IN_CUBIC).rot("body", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, 8, -6).pos("body", 24, 0, 8, -6).pos("body", 28, 0, -2, 5, Ease.IN_CUBIC).pos("body", 44, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 16, -95, 0, 25).rot("legFR", 24, -100, 0, 25).rot("legFR", 28, 15, 0, 0, Ease.IN_CUBIC).rot("legFR", 44, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 16, -95, 0, -25).rot("legFL", 24, -100, 0, -25).rot("legFL", 28, 15, 0, 0, Ease.IN_CUBIC).rot("legFL", 44, 0, 0, 0)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 16, 35, 0, 0).rot("legBR", 44, 0, 0, 0)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 16, 35, 0, 0).rot("legBL", 44, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 16, -35, 0, 0).rot("neck", 24, -35, 0, 0).rot("neck", 28, 40, 0, 0, Ease.IN_CUBIC).rot("neck", 44, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 16, 30, 0, 0).rot("jaw", 28, 0, 0, 0).rot("jaw", 44, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -5, 0, 3).rot("body", 10, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 3, -15, 0, 6).rot("neck", 10, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 20, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 44)
            .rot("body", 0, 0, 0, 0).rot("body", 14, -12, 0, 6).rot("body", 44, 0, 0, 80, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 44, 12, -12, 0, Ease.OUT_BOUNCE)
            .rot("neck", 0, 0, 0, 0).rot("neck", 14, -40, 0, 0).rot("neck", 44, 35, 25, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 45, 0, 0).rot("jaw", 44, 20, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 24, -40, 0, 30).rot("legFR", 44, -70, 0, 10)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 24, -20, 0, -30).rot("legFL", 44, -55, 0, -10)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 24, 25, 0, 10).rot("legBR", 44, 40, 0, 0)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 24, 20, 0, -10).rot("legBL", 44, 55, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_ceremonial_beast", "Ceremonial Beast", "儀式の獣")
            .game(2, 1).boss().size(2.2f, 3.3f).hp(252).speed(0.24f).style(Style.BEAST).egg(0x3a3128, 0xe8e0c6).kbResist(1.0f)
            .model(CeremonialBeast::model)
            .pattern(s -> {
                if (s.getPhase() == 0) return s.count("stamped") == 0 ? "stamp" : "plow";
                return new String[]{"beast_cry", "stomp", "crush"}[s.count("p2") % 3];
            })
            .move(Move.of("stamp", 46).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.RAVAGER_ROAR, 1.6f, 0.6f)
                .at(30, (s, t) -> {
                    s.setCount("stamped", 1); s.setCount("plow", 150);
                    Fx.sound(s, SoundEvents.ANVIL_LAND, 1.4f, 0.5f); Fx.ring(s, ParticleTypes.CAMPFIRE_COSY_SMOKE, s.position(), 3, 24);
                    Bits.say(s, "Plow: 150");
                }))
            .move(Move.of("plow", 40).intent(Intent.ATTACK_BUFF, 18, 1).reach(5f)
                .sound(6, SoundEvents.RAVAGER_STEP, 1.2f, 0.7f)
                .at(12, Fx.dash(0.9, 0.1))
                .at(18, (s, t) -> { Fx.meleeNow(s, 18, 4.0, 120, null); Fx.sound(s, SoundEvents.RAVAGER_ATTACK, 1.3f, 0.7f); Fx.particles(s, ParticleTypes.CLOUD, 20, 0.8); s.addStrength(2); }))
            .move(Move.of("beast_cry", 40).intent(Intent.DEBUFF).noTarget()
                .sound(12, SoundEvents.RAVAGER_ROAR, 1.8f, 0.8f).sound(18, SoundEvents.BELL_BLOCK, 2f, 0.7f).sound(24, SoundEvents.BELL_BLOCK, 2f, 0.9f)
                .at(16, (s, t) -> {   // Ringing: the real effect is "only 1 card this turn" -> approximated by Dazed + Slowness on every foe nearby
                    for (var v : Fx.foes(s, s.getBoundingBox().inflate(16))) { Fx.curse(v, Fx.Curse.DAZED); Fx.slow(v, 4, 0); }
                    Fx.ring(s, ParticleTypes.NOTE, s.position().add(0, 2, 0), 4, 20);
                }))
            .move(Move.of("stomp", 30).intent(Intent.ATTACK, 15, 1).reach(4f)
                .sound(8, SoundEvents.RAVAGER_STEP, 1.3f, 0.6f)
                .at(14, (s, t) -> { Fx.aoeAt(s, Fx.rel(s, 2.2, 0, 0), 15, 3.2, null); Fx.sound(s, SoundEvents.ANVIL_LAND, 1.2f, 0.5f); Fx.ring(s, ParticleTypes.CLOUD, Fx.rel(s, 2.2, 0, 0.1), 2.5, 24); }))
            .move(Move.of("crush", 44).intent(Intent.ATTACK_BUFF, 17, 1).reach(4.4f)
                .sound(12, SoundEvents.RAVAGER_ROAR, 1.4f, 0.5f)
                .at(28, (s, t) -> { Fx.aoeAt(s, Fx.rel(s, 2.4, 0, 0), 17, 3.6, null); Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 1.0f, 0.6f); Fx.ring(s, ParticleTypes.CLOUD, Fx.rel(s, 2.4, 0, 0.1), 3, 30); s.addStrength(3); }))
            .onHurt((s, src, dmg) -> {
                int thr = s.count("plow");
                if (s.getPhase() == 0 && thr > 0 && s.getHealth() - dmg <= thr) {         // Plow threshold reached: stunned, loses all Strength
                    s.setCount("plow", 0); s.setPhase(1);
                    s.addStrength(-s.getStrength());
                    s.stun(60); s.setBase("stun");
                    Fx.sound(s, SoundEvents.RAVAGER_STUNNED, 1.6f, 0.7f); Fx.particles(s, ParticleTypes.CRIT, 30, 1.0);
                }
                return dmg;
            })
            .onTick(s -> { if (s.getBase().equals("stun") && !s.isStunned()) s.setBase("idle"); })
            .onMoveEnd(s -> { if (s.getPhase() == 1) s.inc("p2"); });
    }
}
