package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 Writhing Mass (160 HP). Reactive (re-rolls intent when hit), Malleable (gains growing Block when hit).
 * Moves: Flail 15 + Block, Wither 10 + Weak/Vuln, Multi-Strike 7x3, Strong Strike 32, Implant (Parasite). Fleshy purple blob of tentacles.
 */
public final class WrithingMass {
    private WrithingMass() {}
    static final int FLESH = 0x6a3d6e, FLESH_D = 0x3a1f3e, FLESH_L = 0xa56aa0, MAW = 0x2a0c1c, TOOTH = 0xe8dcc0, EYE = 0xffe040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("writhing_mass");
        m.scale(1.3f);
        Part body = m.add("body").at(0, 14, 0);
        body.box(-9, -12, -8, 18, 24, 16).color(FLESH).grad(FLESH_L, FLESH_D).noise(0.18f).bevel(0.25f);
        body.box(-7, 12, -6, 14, 3, 12).color(FLESH_L).noise(0.15f).bevel(0.25f);
        body.box(-10, -8, -6, 1, 14, 12).color(FLESH).noise(0.15f);
        body.box(9, -8, -6, 1, 14, 12).color(FLESH).noise(0.15f);
        // eyes, scattered
        float[][] eyes = {{-4, 6}, {3, 8}, {-1, 1}, {5, 2}, {-6, -2}};
        for (int i = 0; i < eyes.length; i++) {
            Part e = body.add("eye" + i).at(eyes[i][0], eyes[i][1], 8);
            e.box(-1.5f, -1.5f, 0, 3, 3, 1).color(0x2a1030).flat().glow(Face.FRONT, 0, 0, 3, 3, EYE).paint(Face.FRONT, 1, 1, 1, 1, 0x201020);
        }
        Part mouth = body.add("mouth").at(0, -6, 8);
        mouth.box(-5, -2, 0, 10, 4, 1).color(MAW).flat();
        for (int i = 0; i < 5; i++) mouth.box(-4.5f + i * 2, 0, 1, 1, 2, 1).color(TOOTH).flat();
        // tentacles: 3-segment chains
        float[][] tp = {{-9, 8, 0, -90}, {9, 8, 0, 90}, {-8, -2, 4, -110}, {8, -2, 4, 110}, {-7, 3, -6, -150}, {7, 3, -6, 150}};
        for (int i = 0; i < tp.length; i++) {
            Part t1 = body.add("t" + i).at(tp[i][0], tp[i][1], tp[i][2]).rot(0, tp[i][3] + (tp[i][3] > 0 ? -90 : 90) * 0, 0);
            Part a = t1.add("t" + i + "a").at(0, 0, 0).rot(0, 0, tp[i][0] < 0 ? 70 : -70);
            a.box(-2, -8, -2, 4, 8, 4).color(FLESH).noise(0.15f).bevel(0.15f);
            Part b = a.add("t" + i + "b").at(0, -8, 0).rot(0, 0, tp[i][0] < 0 ? -25 : 25);
            b.box(-1.5f, -8, -1.5f, 3, 8, 3).color(FLESH_D).bevel(0.15f);
            Part c = b.add("t" + i + "c").at(0, -8, 0).rot(0, 0, tp[i][0] < 0 ? -25 : 25);
            c.box(-1, -7, -1, 2, 7, 2).color(FLESH_L).bevel(0.1f);
        }
        m.proc((r, c) -> { for (int i = 0; i < 6; i++) { float w = (float) Math.sin(c.age * 0.12f + i) * 8; r.rot("t" + i + "b", 0, 0, w); r.rot("t" + i + "c", 0, 0, -w); } });
        m.clip("idle", 50).loop().scl("body", 0, 1, 1, 1).scl("body", 25, 1.04f, 0.97f, 1.04f).scl("body", 50, 1, 1, 1)
            .rot("mouth", 0, 0, 0, 0).rot("mouth", 25, 0, 0, 0);
        m.clip("walk", 24).loop().rot("body", 0, 0, 0, 4).rot("body", 12, 0, 0, -4).rot("body", 24, 0, 0, 4);
        // helper-like clips
        m.clip("flail", 32)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -14, 0, 0).rot("body", 14, 18, 0, 0, Ease.IN_CUBIC).rot("body", 32, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -3).pos("body", 14, 0, -1, 5, Ease.IN_CUBIC).pos("body", 32, 0, 0, 0)
            .rot("t0a", 0, 0, 0, 0).rot("t0a", 10, 0, 0, -40).rot("t0a", 14, 0, 0, 60, Ease.IN_CUBIC).rot("t0a", 32, 0, 0, 0)
            .rot("t1a", 0, 0, 0, 0).rot("t1a", 10, 0, 0, 40).rot("t1a", 14, 0, 0, -60, Ease.IN_CUBIC).rot("t1a", 32, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 20, 1, 1, 1).scl("body", 26, 1.1f, 1.05f, 1.1f).scl("body", 32, 1, 1, 1);
        m.clip("wither", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 10, 0, 0).rot("body", 13, -12, 0, 0, Ease.IN_CUBIC).rot("body", 30, 0, 0, 0)
            .pos("mouth", 0, 0, 0, 0).pos("mouth", 8, 0, -2, 0).pos("mouth", 13, 0, 2, 0).pos("mouth", 30, 0, 0, 0)
            .rot("t2a", 0, 0, 0, 0).rot("t2a", 8, 0, 0, -50).rot("t2a", 13, 0, 0, 30, Ease.IN_CUBIC).rot("t2a", 30, 0, 0, 0)
            .rot("t3a", 0, 0, 0, 0).rot("t3a", 8, 0, 0, 50).rot("t3a", 13, 0, 0, -30, Ease.IN_CUBIC).rot("t3a", 30, 0, 0, 0);
        m.clip("multi_strike", 40)
            .rot("t0a", 0, 0, 0, 0).rot("t0a", 6, 0, 0, -60).rot("t0a", 9, 0, 0, 55, Ease.IN_CUBIC).rot("t0a", 14, 0, 0, 5)
            .rot("t1a", 0, 0, 0, 0).rot("t1a", 14, 0, 0, 60).rot("t1a", 17, 0, 0, -55, Ease.IN_CUBIC).rot("t1a", 22, 0, 0, -5)
            .rot("t2a", 0, 0, 0, 0).rot("t2a", 22, 0, 0, -60).rot("t2a", 25, 0, 0, 55, Ease.IN_CUBIC).rot("t2a", 30, 0, 0, 5)
            .rot("t0a", 40, 0, 0, 0).rot("t1a", 40, 0, 0, 0).rot("t2a", 40, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 9, 8, -10, 0).rot("body", 17, 8, 10, 0).rot("body", 25, 8, -10, 0).rot("body", 40, 0, 0, 0);
        m.clip("strong_strike", 44)
            .rot("body", 0, 0, 0, 0).rot("body", 18, -28, 0, 0).rot("body", 22, 34, 0, 0, Ease.IN_CUBIC).rot("body", 32, 28, 0, 0).rot("body", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 18, 0, 5, -6).pos("body", 22, 0, -4, 9, Ease.IN_CUBIC).pos("body", 44, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 18, 0.9f, 1.18f, 0.9f).scl("body", 22, 1.1f, 0.85f, 1.1f).scl("body", 44, 1, 1, 1)
            .rot("t0a", 0, 0, 0, 0).rot("t0a", 18, 0, 0, -80).rot("t0a", 22, 0, 0, 70, Ease.IN_CUBIC).rot("t0a", 44, 0, 0, 0)
            .rot("t1a", 0, 0, 0, 0).rot("t1a", 18, 0, 0, 80).rot("t1a", 22, 0, 0, -70, Ease.IN_CUBIC).rot("t1a", 44, 0, 0, 0);
        m.clip("implant", 34)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -10, 0, 0).rot("body", 16, 16, 0, 0).rot("body", 34, 0, 0, 0)
            .pos("mouth", 0, 0, 0, 0).pos("mouth", 10, 0, -3, 0).pos("mouth", 22, 0, -3, 0).pos("mouth", 34, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 10, 1.15f, 0.9f, 1.15f).scl("body", 16, 0.9f, 1.15f, 0.9f).scl("body", 34, 1, 1, 1);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -10, 0, 8).rot("body", 10, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 3, 1.12f, 0.9f, 1.12f).scl("body", 10, 1, 1, 1);
        m.clip("death", 36).pos("body", 0, 0, 0, 0).pos("body", 36, 0, -10, 0, Ease.OUT_BOUNCE)
            .scl("body", 0, 1, 1, 1).scl("body", 12, 1.2f, 0.9f, 1.2f).scl("body", 36, 1.3f, 0.3f, 1.3f)
            .rot("t0a", 0, 0, 0, 0).rot("t0a", 36, 0, 0, 60).rot("t1a", 0, 0, 0, 0).rot("t1a", 36, 0, 0, -60);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("writhing_mass", "Writhing Mass", "のたうつ塊")
            .game(1, 3).size(2.4f, 2.6f).hp(160).speed(0.2f).style(Style.SLIME).egg(0x6a3d6e, 0xffe040).elite()
            .model(WrithingMass::model)
            .spawn(1, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(s -> {
                if (s.getTurn() == 0) { String[] f = {"multi_strike", "strong_strike", "flail", "wither"}; return f[s.rand().nextInt(4)]; }
                String[] all = {"flail", "wither", "multi_strike", "strong_strike", "implant"};
                for (int i = 0; i < 8; i++) {
                    String id = all[s.rand().nextInt(all.length)];
                    if (id.equals(s.lastMove())) continue;
                    if (id.equals("implant") && s.count("implanted") > 0) continue;
                    return id;
                }
                return "flail";
            })
            .move(Move.of("flail", 32).intent(Intent.ATTACK_DEFEND, 15, 1).reach(3f)
                .sound(11, SoundEvents.SLIME_ATTACK, 1f, 0.7f).at(14, Fx.melee(15, 3.2, 120)).at(14, Fx.block(16)))
            .move(Move.of("wither", 30).intent(Intent.ATTACK_DEBUFF, 10, 1).reach(3f)
                .sound(11, SoundEvents.SLIME_SQUISH, 1f, 0.6f)
                .at(13, Fx.melee(10, 3.2, 120, v -> { Fx.weak(v, 5); Fx.vulnerable(v, 5); })))
            .move(Move.of("multi_strike", 40).intent(Intent.ATTACK, 7, 3).reach(3f)
                .sound(8, SoundEvents.SLIME_ATTACK, 1f, 1f).sound(16, SoundEvents.SLIME_ATTACK, 1f, 1f).sound(24, SoundEvents.SLIME_ATTACK, 1f, 1f)
                .at(9, Fx.melee(7, 3.2, 120)).at(17, Fx.melee(7, 3.2, 120)).at(25, Fx.melee(7, 3.2, 120)))
            .move(Move.of("strong_strike", 44).intent(Intent.ATTACK, 32, 1).reach(3.2f)
                .sound(14, SoundEvents.RAVAGER_ROAR, 1f, 0.7f).sound(22, SoundEvents.GENERIC_EXPLODE, 0.5f, 0.6f)
                .at(22, Fx.melee(32, 3.4, 130)))
            .move(Move.of("implant", 34).intent(Intent.STRONG_DEBUFF).ranged(5)
                .sound(14, SoundEvents.SLIME_SQUISH, 1.2f, 0.5f)
                .at(16, (s, t) -> { s.inc("implanted"); if (t != null) { Fx.curse(t, Fx.Curse.DRAIN); Fx.curse(t, Fx.Curse.SLIMED); } }))
            .onSpawn(s -> s.setCount("malleable", 3))
            .onHurt((s, src, amt) -> {                          // Malleable: block grows each hit; Reactive: re-roll the next intent
                int b = s.count("malleable"); s.addBlock(b); s.setCount("malleable", b + 1);
                if (s.count("reacted") == 0) { s.inc("reacted"); String[] r = {"flail", "wither", "multi_strike", "strong_strike"}; s.forceNext(r[s.rand().nextInt(r.length)]); }
                return amt;
            })
            .onMoveEnd(s -> { s.setCount("reacted", 0); s.setCount("malleable", 3); });
    }
}
