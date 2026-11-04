package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Chosen. 95-99 HP. Turn 1 Hex (Dazed curse). Then alternates: Debilitate 10 + Vulnerable 2 or Drain (Weak 3, self +3 Str),
 * followed by Zap 21 or Poke 5x2.
 */
public final class Chosen {
    private Chosen() {}
    static final int ROBE = 0x3b4a6b, ROBE_D = 0x222b45, TRIM = 0xc9b45a, SKIN = 0xd8d2c0, ORB = 0x7ad0ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("chosen");
        Bip b = Bip.make(m, SKIN, ROBE, ROBE_D, ROBE_D, 0x1a1f33, 8, 8, 12, 9, 6);
        b.torso.box(-1, 2, 2.2f, 2, 8, 1).color(TRIM).flat();
        b.torso.box(-4.3f, 0, -3, 9, 2, 6).color(TRIM).flat().noise(0.05f).inflate(0.1f);
        Part skirt = b.body.add("skirt").at(0, 2, 0);
        skirt.box(-5, -9, -3.5f, 10, 7, 7).color(ROBE_D).grad(ROBE, ROBE_D);
        skirt.box(-5.5f, -10, -4, 11, 2, 8).color(TRIM).flat();
        b.head.box(-3.6f, 3, -3.6f, 7, 5, 7).color(ROBE_D).grad(ROBE, ROBE_D);                   // hood
        b.head.box(-3.6f, -0.5f, -4.6f, 7, 7, 2).color(ROBE_D);
        b.head.box(-3, -0.2f, 3.2f, 6, 4, 1).color(0x9ba0aa).glow(Face.FRONT, 1, 0, 1, 1, ORB).glow(Face.FRONT, 4, 0, 1, 1, ORB); // mask with glowing eyes
        Part shoulder = b.torso.add("mantle").at(0, 11, 0);
        shoulder.box(-6, -2, -3.5f, 12, 3, 7).color(TRIM).grad(0xe0cc70, 0x9a8a3a).noise(0.05f);
        for (Part h : new Part[]{b.handR, b.handL}) h.box(-1, -4, -1, 2, 1, 2).color(ORB).flat().glow(Face.FRONT, 0, 0, 2, 1, ORB);
        Part orb = b.handL.add("orb").at(0, -3, 1.5f);
        orb.box(-1.5f, -1.5f, -1.5f, 3, 3, 3).color(ORB).flat().glow(Face.FRONT, 0, 0, 3, 3, ORB).glow(Face.TOP, 0, 0, 3, 3, ORB);
        m.clip("hex", 44)   // arms spread, head back, orb pulses
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 10, 0, 0).rot("torso", 20, -12, 0, 0, Ease.OUT_BACK).rot("torso", 34, -8, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 20, 0, 20).rot("armR", 20, -140, 0, 35, Ease.OUT_BACK).rot("armR", 34, -135, 0, 38).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 20, 0, -20).rot("armL", 20, -140, 0, -35, Ease.OUT_BACK).rot("armL", 34, -135, 0, -38).rot("armL", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, -30, 0, 0).rot("head", 34, -30, 0, 0).rot("head", 44, 0, 0, 0)
            .scl("orb", 0, 1, 1, 1).scl("orb", 20, 2.2f, 2.2f, 2.2f).scl("orb", 44, 1, 1, 1);
        m.clip("debilitate", 30)  // slap a glowing palm forward
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -8, 25, 0).rot("torso", 13, 14, -25, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -40, 20, 20).rot("armR", 13, -100, -10, 0, Ease.IN_CUBIC).rot("armR", 20, -95, -10, 0).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, 20, 0, -15).rot("armL", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 13, 0, 0, 3, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0);
        m.clip("drain", 36)   // hands raise, pull energy inward, shudder
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -100, 0, 40).rot("armR", 24, -60, 0, 5, Ease.IN_CUBIC).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -100, 0, -40).rot("armL", 24, -60, 0, -5, Ease.IN_CUBIC).rot("armL", 36, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -10, 0, 0).rot("torso", 24, 12, 0, 0, Ease.OUT_BACK).rot("torso", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -20, 0, 0).rot("head", 24, 10, 0, 0).rot("head", 36, 0, 0, 0);
        m.clip("zap", 34)     // both hands thrust: beam
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -14, 0, 0).rot("torso", 16, 16, 0, 0, Ease.IN_CUBIC).rot("torso", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -150, 0, 10).rot("armR", 16, -90, 0, 5, Ease.IN_CUBIC).rot("armR", 26, -90, 0, 5).rot("armR", 34, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -150, 0, -10).rot("armL", 16, -90, 0, -5, Ease.IN_CUBIC).rot("armL", 26, -90, 0, -5).rot("armL", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -20, 0, 0).rot("head", 16, 10, 0, 0).rot("head", 34, 0, 0, 0);
        m.clip("poke", 30)    // two quick jabs
            .rot("torso", 0, 0, 0, 0).rot("torso", 6, -6, -20, 0).rot("torso", 9, 8, 25, 0, Ease.IN_CUBIC).rot("torso", 15, -6, 20, 0).rot("torso", 18, 8, -25, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, -50, 0, 15).rot("armR", 9, -95, 0, 0, Ease.IN_CUBIC).rot("armR", 13, -40, 0, 10).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -50, 0, -15).rot("armL", 18, -95, 0, 0, Ease.IN_CUBIC).rot("armL", 24, -40, 0, -10).rot("armL", 30, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("chosen", "Chosen", "チョーズン")
            .game(1, 2).size(0.6f, 1.9f).hp(97).speed(0.25f).style(Style.MAGIC).egg(0x3b4a6b, 0xc9b45a)
            .model(Chosen::model).spawn(5, 1, 1, MobDef.Habitat.TAIGA)
            .pattern(s -> {
                if (s.getTurn() == 0) return "hex";
                String l = s.lastMove();
                boolean second = l.equals("debilitate") || l.equals("drain");
                boolean a = s.rand().nextBoolean();
                return second ? (a ? "zap" : "poke") : (a ? "debilitate" : "drain");
            })
            .move(Move.of("hex", 44).intent(Intent.STRONG_DEBUFF).noTarget()
                .sound(18, SoundEvents.EVOKER_PREPARE_ATTACK, 1.2f, 0.7f)
                .at(20, (s, t) -> { if (t != null) Fx.curse(t, Fx.Curse.DAZED); Fx.particles(s, ParticleTypes.WITCH, 25, 0.6); }))
            .move(Move.of("debilitate", 30).intent(Intent.ATTACK_DEBUFF, 10, 1).reach(2.4f)
                .at(13, Fx.melee(10, 2.8, 80, v -> Fx.vulnerable(v, 8))))
            .move(Move.of("drain", 36).intent(Intent.DEBUFF).noTarget()
                .sound(12, SoundEvents.BEACON_POWER_SELECT, 1f, 0.6f)
                .at(24, (s, t) -> { if (t != null) Fx.weak(t, 8); s.addStrength(3); Fx.particles(s, ParticleTypes.ENCHANT, 25, 0.6); }))
            .move(Move.of("zap", 34).intent(Intent.ATTACK, 21, 1).ranged(8)
                .sound(14, SoundEvents.EVOKER_CAST_SPELL, 1f, 1.3f)
                .at(16, (s, t) -> { Fx.particles(s, ParticleTypes.ELECTRIC_SPARK, 20, 0.5); Fx.hitTarget(21).run(s, t); }))
            .move(Move.of("poke", 30).intent(Intent.ATTACK, 5, 2).reach(2.4f)
                .at(9, Fx.melee(5, 2.8, 80)).at(18, Fx.melee(5, 2.8, 80)));
    }
}
