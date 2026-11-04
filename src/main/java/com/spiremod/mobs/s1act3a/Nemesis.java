package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 Nemesis (185 HP, elite). Intangible every other turn (damage capped to 1).
 * Turn 1: 50% Tri Attack (6x3) / 50% Debuff (3 Burns). Then Scythe (45) every 2+ turns, otherwise Tri Attack / Debuff. Cloaked fire wraith with a scythe.
 */
public final class Nemesis {
    private Nemesis() {}
    static final int CLOAK = 0x2a1e3a, CLOAK_D = 0x120a1c, CLOAK_L = 0x5a4478, FIRE = 0xff7a30, BONE = 0xe8e2d0, BLADE = 0xb0b8d0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("nemesis");
        Part body = m.add("body").at(0, 16, 0);
        Part torso = body.add("torso").at(0, 0, 0);
        torso.box(-5, 0, -3, 10, 14, 6).color(CLOAK).grad(CLOAK_L, CLOAK_D).noise(0.12f).bevel(0.15f);
        torso.box(-6, -10, -4, 12, 10, 8).color(CLOAK_D).grad(CLOAK, CLOAK_D).noise(0.1f);     // robe
        torso.box(-5, -18, -3, 10, 8, 6).color(CLOAK_D).grad(CLOAK_D, 0x050208);                // tattered hem
        torso.box(-4, 4, 3, 8, 2, 1).color(FIRE).flat().glow(Face.FRONT, 0, 0, 8, 2, FIRE);       // ember belt
        torso.box(-7, 12, -4, 14, 3, 8).color(CLOAK_D).bevel(0.2f);                              // shoulders
        Part head = torso.add("head").at(0, 14, 0);
        head.box(-4, 0, -4, 8, 9, 8).color(CLOAK_D).bevel(0.2f);
        head.box(-3, 1, 3.5f, 6, 7, 1).color(BONE).bevel(0.1f);                                   // mask
        head.box(-3, 1, 3.5f, 6, 7, 1).color(BONE).glow(Face.FRONT, 0, 2, 2, 2, FIRE).glow(Face.FRONT, 4, 2, 2, 2, FIRE).paint(Face.FRONT, 2, 5, 2, 2, 0x201010);
        head.box(-5, 8, -5, 10, 3, 10).color(CLOAK).inflate(0.1f);                                 // hood peak
        Part flame = head.add("flame").at(0, 11, 0);
        flame.box(-2, 0, -2, 4, 4, 4).color(FIRE).flat().glow(Face.FRONT, 0, 0, 4, 4, 0xffd060);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 7.5f, 12, 0);
            arm.box(-1.5f, -9, -1.5f, 3, 9, 3).color(CLOAK).bevel(0.1f);
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -9, 0);
            hand.box(-1, -3, -1, 2, 3, 2).color(BONE);
            if (s < 0) {   // scythe held in right hand, haft along z when at rest (blade sweeps up)
                Part sc = hand.add("scythe").at(0, -2, 0).rot(-90, 0, 0);
                sc.box(-0.5f, -6, -0.5f, 1, 26, 1).color(0x3a2a1e);
                sc.box(-0.5f, 18, -0.5f, 1, 2, 7).color(0x3a2a1e);
                sc.box(-0.5f, 19, 4, 1, 3, 10).color(BLADE).flat().noise(0.04f);
                sc.box(-0.5f, 18, 12, 1, 2, 6).color(0xe8eefc).flat();
            }
        }
        m.bob(1.5f, 0.1f);
        m.look("head", 40, 25);
        m.proc((r, c) -> { float f = 1 + 0.15f * (float) Math.sin(c.age * 0.5f); r.scl("flame", f, 1 + 0.4f * (f - 1) * 3, f); });
        m.clip("idle", 50).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 25, -2, 0, 0).rot("torso", 50, 2, 0, 0)
            .rot("armR", 0, 4, 0, 6).rot("armR", 25, 8, 0, 8).rot("armR", 50, 4, 0, 6)
            .rot("armL", 0, 4, 0, -6).rot("armL", 25, 8, 0, -8).rot("armL", 50, 4, 0, -6);
        m.clip("walk", 24).loop().rot("torso", 0, 10, 0, 0).rot("torso", 12, 14, 0, 0).rot("torso", 24, 10, 0, 0);
        // TRI ATTACK: three quick scythe slashes
        m.clip("tri_attack", 44)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, -120, 0, 20).rot("armR", 9, -40, 0, -20, Ease.IN_CUBIC).rot("armR", 15, -130, 0, -20).rot("armR", 18, -40, 0, 20, Ease.IN_CUBIC)
            .rot("armR", 24, -120, 0, 10).rot("armR", 27, -30, 0, -10, Ease.IN_CUBIC).rot("armR", 44, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 6, -10, -20, 0).rot("torso", 9, 15, 25, 0, Ease.IN_CUBIC).rot("torso", 15, -10, 20, 0).rot("torso", 18, 15, -25, 0, Ease.IN_CUBIC)
            .rot("torso", 24, -12, -10, 0).rot("torso", 27, 20, 10, 0, Ease.IN_CUBIC).rot("torso", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, 4).pos("body", 18, 0, 0, 5).pos("body", 27, 0, -1, 7).pos("body", 44, 0, 0, 0);
        m.clip("debuff", 36)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -150, 0, 30, Ease.OUT_BACK).rot("armR", 26, -150, 0, 30).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -150, 0, -30, Ease.OUT_BACK).rot("armL", 26, -150, 0, -30).rot("armL", 36, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -16, 0, 0).rot("torso", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 36, 0, 0, 0)
            .scl("flame", 0, 1, 1, 1).scl("flame", 14, 2.2f, 2.8f, 2.2f).scl("flame", 36, 1, 1, 1);
        m.clip("scythe", 48)
            .rot("armR", 0, 0, 0, 0).rot("armR", 18, -170, 0, 40).rot("armR", 24, -20, 0, -30, Ease.IN_CUBIC).rot("armR", 34, -10, 0, -30).rot("armR", 48, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 18, -25, -35, 0).rot("torso", 24, 30, 45, 0, Ease.IN_CUBIC).rot("torso", 34, 20, 40, 0).rot("torso", 48, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 18, 0, 1, -5).pos("body", 24, 0, -2, 9, Ease.IN_CUBIC).pos("body", 48, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 18, -60, 0, -50).rot("armL", 24, 30, 0, -20).rot("armL", 48, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -14, 0, 8).rot("torso", 10, 0, 0, 0);
        m.clip("death", 36).pos("body", 0, 0, 0, 0).pos("body", 36, 0, -12, 0, Ease.IN_QUAD)
            .rot("torso", 0, 0, 0, 0).rot("torso", 36, 20, 0, 10).scl("body", 0, 1, 1, 1).scl("body", 36, 1.2f, 0.2f, 1.2f)
            .rot("armR", 0, 0, 0, 0).rot("armR", 36, 0, 0, 60).rot("armL", 0, 0, 0, 0).rot("armL", 36, 0, 0, -60);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("nemesis", "Nemesis", "ネメシス")
            .game(1, 3).size(0.8f, 2.2f).hp(185).speed(0.24f).style(Style.GHOST).egg(0x2a1e3a, 0xff7a30).elite().fireImmune()
            .model(Nemesis::model).flying()
            .spawn(1, 1, 1, MobDef.Habitat.ANY)
            .pattern(s -> {
                boolean scythe = s.getTurn() > 0 && s.count("sinceScythe") >= 2 && s.rand().nextInt(100) < 100;
                if (scythe) return "scythe";
                if (s.lastMove().equals("tri_attack")) return "debuff";
                if (s.lastMove().equals("debuff")) return "tri_attack";
                return s.rand().nextBoolean() ? "tri_attack" : "debuff";
            })
            .move(Move.of("tri_attack", 44).intent(Intent.ATTACK, 6, 3).reach(2.6f)
                .sound(6, SoundEvents.BLAZE_SHOOT, 1f, 1.2f).sound(15, SoundEvents.BLAZE_SHOOT, 1f, 1.2f).sound(24, SoundEvents.BLAZE_SHOOT, 1f, 1.2f)
                .at(9, Fx.melee(6, 3.0, 110)).at(18, Fx.melee(6, 3.0, 110)).at(27, Fx.melee(6, 3.0, 110)))
            .move(Move.of("debuff", 36).intent(Intent.DEBUFF).ranged(10)
                .sound(12, SoundEvents.BLAZE_AMBIENT, 1.4f, 0.6f)
                .at(16, (s, t) -> { Fx.particles(s, ParticleTypes.FLAME, 30, 0.6); if (t != null) { Fx.curse(t, Fx.Curse.BURN); Fx.curse(t, Fx.Curse.BURN); Fx.curse(t, Fx.Curse.BURN); } }))
            .move(Move.of("scythe", 48).intent(Intent.ATTACK, 45, 1).reach(3.4f)
                .sound(18, SoundEvents.PLAYER_ATTACK_SWEEP, 1.4f, 0.5f)
                .at(24, Fx.melee(45, 4.0, 150)))
            .onSpawn(s -> s.flags.add("intangible"))
            .onHurt((s, src, amt) -> s.flags.contains("intangible") ? Math.min(amt, 1f) : amt)                  // Intangible
            .onMoveEnd(s -> {
                if (s.lastMove().equals("scythe")) s.setCount("sinceScythe", 0); else s.inc("sinceScythe");
                if (s.flags.contains("intangible")) s.flags.remove("intangible"); else s.flags.add("intangible");
                s.setVar(s.flags.contains("intangible") ? 1 : 0);
            });
    }
}
