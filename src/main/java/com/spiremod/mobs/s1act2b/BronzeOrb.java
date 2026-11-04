package com.spiremod.mobs.s1act2b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 2 Bronze Automaton minion: Bronze Orb. 52-58 HP. Stasis (once, 25%: steals a card), Beam 8, Support Beam (12 Block to Automaton). */
public final class BronzeOrb {
    private BronzeOrb() {}
    static final int BR = 0xb98a3a, BR_L = 0xe8c470, BR_D = 0x6a4a1c, CYAN = 0x5fe8ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("bronze_orb");
        Part body = m.add("body").at(0, 12, 0);
        Part core = body.add("core").at(0, 0, 0);
        core.box(-5, -5, -5, 10, 10, 10).color(BR).grad(BR_L, BR_D).bevel(0.25f).noise(0.06f);
        core.box(-4, -6, -4, 8, 12, 8).color(BR).grad(BR_L, BR_D).bevel(0.25f);
        core.box(-6, -4, -4, 12, 8, 8).color(BR).grad(BR_L, BR_D).bevel(0.25f);
        Part eye = body.add("eye").at(0, 0, 5);
        eye.box(-2.5f, -2.5f, -0.5f, 5, 5, 1).color(CYAN).flat().glow(Face.FRONT, 0, 0, 5, 5, CYAN).paint(Face.FRONT, 1, 1, 3, 3, 0xffffff).paint(Face.FRONT, 2, 2, 1, 1, 0x103040);
        Part ring = body.add("ring").at(0, 0, 0);
        ring.box(-7, -0.5f, -7, 14, 1, 1).color(BR_D).flat();
        ring.box(-7, -0.5f, 6, 14, 1, 1).color(BR_D).flat();
        ring.box(-7, -0.5f, -7, 1, 1, 14).color(BR_D).flat();
        ring.box(6, -0.5f, -7, 1, 1, 14).color(BR_D).flat();
        for (int i = 0; i < 4; i++) { Part r = ring.add("rivet" + i).at(0, 0, 0).rot(0, i * 90, 0); r.box(-1, -1, -8, 2, 2, 2).color(BR_L).flat(); }
        m.bob(1.5f, 0.1f);
        m.proc((r, c) -> { r.rot("ring", c.age * 5, c.age * 3, 0); });
        m.look("body", 60, 40);
        m.clip("idle", 40).loop().scl("core", 0, 1, 1, 1).scl("core", 20, 1.04f, 1.04f, 1.04f).scl("core", 40, 1, 1, 1);
        m.clip("walk", 20).loop().rot("body", 0, 6, 0, 0).rot("body", 10, 12, 0, 0).rot("body", 20, 6, 0, 0);
        m.clip("beam", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -3, Ease.OUT_CUBIC).pos("body", 14, 0, 0, 2, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .scl("eye", 0, 1, 1, 1).scl("eye", 10, 1.5f, 1.5f, 1.5f).scl("eye", 14, 2, 2, 2).scl("eye", 30, 1, 1, 1)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -20, 0, 0).rot("body", 14, 10, 0, 0).rot("body", 30, 0, 0, 0);
        m.clip("support_beam", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 3, 0, Ease.OUT_BACK).pos("body", 30, 0, 0, 0)
            .scl("core", 0, 1, 1, 1).scl("core", 12, 1.2f, 1.2f, 1.2f).scl("core", 30, 1, 1, 1)
            .rot("body", 0, 0, 0, 0).rot("body", 12, 0, 0, 0).rot("body", 30, 0, 360, 0, Ease.IN_OUT_QUAD);
        m.clip("stasis", 36)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -2, 0, Ease.OUT_CUBIC).pos("body", 18, 0, 2, 3, Ease.OUT_BACK).pos("body", 36, 0, 0, 0)
            .scl("core", 0, 1, 1, 1).scl("core", 12, 0.8f, 0.8f, 0.8f).scl("core", 18, 1.3f, 1.3f, 1.3f).scl("core", 36, 1, 1, 1)
            .scl("eye", 0, 1, 1, 1).scl("eye", 18, 2.2f, 2.2f, 2.2f).scl("eye", 36, 1, 1, 1);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -22, 0, 12).rot("body", 10, 0, 0, 0);
        m.clip("death", 26)
            .pos("body", 0, 0, 0, 0).pos("body", 22, 0, -11, 0, Ease.IN_QUAD).pos("body", 26, 0, -11, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 22, 40, 0, 60)
            .scl("eye", 0, 1, 1, 1).scl("eye", 10, 0.2f, 0.2f, 0.2f).scl("core", 0, 1, 1, 1).scl("core", 26, 1.1f, 0.8f, 1.1f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("bronze_orb", "Bronze Orb", "ブロンズオーブ")
            .game(1, 2).size(0.9f, 1.0f).hp(55).speed(0.2f).style(Style.METAL).egg(0xb98a3a, 0x5fe8ff).minion().flying()
            .model(BronzeOrb::model)
            .pattern(s -> {
                if (s.count("stasis") == 0 && s.rand().nextInt(100) < 25) return "stasis";
                boolean beamed2 = s.lastMove().equals("beam") && s.moveAgo(1).equals("beam");
                int r = s.rand().nextInt(100);
                if (r < 70 && !beamed2) return "beam";
                return s.lastMove().equals("support_beam") ? "beam" : "support_beam";
            })
            .move(Move.of("stasis", 36).intent(Intent.STRONG_DEBUFF).ranged(10)
                .sound(12, SoundEvents.BEACON_POWER_SELECT, 1.2f, 1.5f)
                .at(18, (s, t) -> { s.inc("stasis"); if (t != null) { int n = Fx.steal(s, t, 6); Fx.slow(t, 4, 1); Fx.particlesAt(s, ParticleTypes.PORTAL, t.position().add(0, 1, 0), 30, 0.4, 0.3); } }))
            .move(Move.of("beam", 30).intent(Intent.ATTACK, 8, 1).ranged(10).faceUntil(16)
                .sound(10, SoundEvents.BEACON_AMBIENT, 1.2f, 1.8f)
                .at(14, (s, t) -> { Fx.sound(s, SoundEvents.BLAZE_SHOOT, 1f, 1.6f); Fx.particlesAt(s, ParticleTypes.ELECTRIC_SPARK, Fx.rel(s, 0, 0, 1.2), 15, 0.2, 0.1); if (t != null) Fx.dmg(s, t, 8); }))
            .move(Move.of("support_beam", 30).intent(Intent.DEFEND).noTarget()
                .sound(12, SoundEvents.BEACON_ACTIVATE, 1f, 1.6f)
                .at(14, (s, t) -> { SpireMob l = s.getLeader(); if (l != null) { l.addBlock(12); Fx.particlesAt(s, ParticleTypes.ENCHANTED_HIT, l.position().add(0, l.getBbHeight() * 0.5, 0), 25, 0.8, 0.1); } Fx.particles(s, ParticleTypes.ELECTRIC_SPARK, 12, 0.6); }));
    }
}
