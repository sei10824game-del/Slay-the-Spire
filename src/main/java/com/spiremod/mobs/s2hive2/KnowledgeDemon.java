package com.spiremod.mobs.s2hive2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Hive boss KNOWLEDGE DEMON (379 HP): floating many-eyed scholar-demon with an open tome.
 * Cycle: Curse of Knowledge (each player takes one of two debuffs; options change per use, only the first 3 times) -> Slap 17 -> Knowledge Overwhelming 8x3 -> Ponder 11 + heal 30 + 2 Str.
 * Curse mapping: Disintegration=poison, Mind Rot=blind, Sloth=slow, Waste Away=weak/frail.
 */
public final class KnowledgeDemon {
    private KnowledgeDemon() {}

    static final int ROBE = 0x3c1e5a, ROBE_D = 0x1e0e30, ROBE_L = 0x6a3a96, SKIN = 0x8a5a9a, GOLD = 0xe0b040, EYE = 0xff40c0, EYE2 = 0x60ffe0, PAGE = 0xe8dcb8;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_knowledge_demon");
        Part body = m.add("body").at(0, 18, 0);
        // flowing robe: tapering tiers that end in wisps (no legs)
        Part robe = body.add("robe").at(0, 0, 0);
        robe.box(-9, -6, -6, 18, 6, 12).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.2f).noise(0.1f);
        robe.box(-7, -12, -5, 14, 6, 10).color(ROBE).grad(ROBE, ROBE_D).bevel(0.2f);
        robe.box(-5, -18, -4, 10, 6, 8).color(ROBE_D).grad(ROBE, ROBE_D);
        robe.box(-3, -24, -3, 6, 6, 6).color(ROBE_D).grad(ROBE_D, 0x0a0518);
        robe.box(-9.3f, -6.6f, -6.3f, 18, 1, 12).color(GOLD).flat().inflate(0.3f);
        Part torso = body.add("torso").at(0, 0, 0);
        torso.box(-8, 0, -5, 16, 14, 10).color(ROBE).grad(ROBE_L, ROBE).bevel(0.15f).noise(0.1f);
        torso.box(-1, 1, -5.3f, 2, 12, 1).color(GOLD).flat();
        torso.box(-9, 11, -6, 18, 3, 12).color(ROBE_D).bevel(0.2f);                                  // mantle
        for (int i = 0; i < 4; i++) torso.box(-6 + i * 3.4f, 4 + (i % 2) * 4, 5.1f, 2, 2, 1).color(EYE).flat().glow(Face.BACK, 0, 0, 2, 2, EYE); // eyes on the back
        // head: tall hairless skull with huge central eye + horns
        Part head = torso.add("head").at(0, 14, 0);
        head.box(-5, 0, -5, 10, 11, 10).color(SKIN).grad(0xa874b8, SKIN).bevel(0.15f).noise(0.1f);
        head.box(-3, 4, 4.8f, 6, 5, 1).color(0xfff0f8).flat().glow(Face.FRONT, 1, 1, 4, 3, EYE).px(Face.FRONT, 2, 2, 0xffffff);
        head.box(-4.5f, 8, 4.8f, 2, 1, 1).color(EYE2).flat().glow(Face.FRONT, 0, 0, 2, 1, EYE2);
        head.box(2.5f, 8, 4.8f, 2, 1, 1).color(EYE2).flat().glow(Face.FRONT, 0, 0, 2, 1, EYE2);
        head.box(-3, 1, 4.8f, 6, 2, 1).color(0x1a0a20).flat().paint(Face.FRONT, 0, 0, 6, 1, 0xf0e8d0); // toothy maw
        for (int sd = -1; sd <= 1; sd += 2) {
            Part hn = head.add(sd < 0 ? "hornR" : "hornL").at(sd * 4, 9, 0).rot(0, 0, sd * -35);
            hn.box(-1.5f, 0, -1.5f, 3, 7, 3).color(0xd8c8a0).grad(0xf0e8d0, 0x8a7a50).bevel(0.2f);
            hn.box(-1, 7, -1, 2, 5, 2).color(0xd8c8a0).bevel(0.2f);
        }
        // halo of knowledge: ring of small glowing eyes behind the head
        Part halo = torso.add("halo").at(0, 21, -6);
        for (int i = 0; i < 8; i++) {
            double a = i * Math.PI / 4;
            halo.box((float) (Math.cos(a) * 11) - 1.5f, (float) (Math.sin(a) * 11) - 1.5f, -1, 3, 3, 2).color(GOLD).flat().glow(Face.BACK, 0, 0, 3, 3, i % 2 == 0 ? EYE : EYE2);
        }
        // four arms
        for (int s = -1; s <= 1; s += 2) for (int k = 0; k < 2; k++) {
            String nm = (s < 0 ? "armR" : "armL") + (k == 0 ? "" : "2");
            Part arm = torso.add(nm).at(s * 9, k == 0 ? 12 : 6, 0).rot(0, 0, k == 0 ? 0 : s * 10);
            arm.box(-1.5f, -8, -1.5f, 3, 8, 3).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.15f);
            arm.box(-2, -8.5f, -2, 4, 2, 4).color(GOLD).flat();
            Part hand = arm.add(nm + "h").at(0, -8, 0);
            hand.box(-1.5f, -5, -1.5f, 3, 5, 3).color(SKIN).grad(0xa874b8, SKIN).bevel(0.15f);
            for (int f = -1; f <= 1; f++) hand.box(f - 0.4f, -8, -0.6f, 1, 3, 1).color(SKIN);
        }
        // floating tome
        Part tome = m.add("tome").at(0, 20, 14);
        Part cov = tome.add("cover").at(0, 0, 0);
        cov.box(-6, -4, -1.5f, 12, 8, 1).color(0x5a1a2a).grad(0x7a2a3a, 0x3a0a18).bevel(0.2f);
        Part pgL = tome.add("pgL").at(0, 0, -0.5f).rot(0, 12, 0);
        pgL.box(-6, -3.6f, 0, 6, 7, 1).color(PAGE).grad(PAGE, 0xb8a888).noise(0.1f).glow(Face.FRONT, 1, 1, 4, 5, 0xffe0a0);
        Part pgR = tome.add("pgR").at(0, 0, -0.5f).rot(0, -12, 0);
        pgR.box(0, -3.6f, 0, 6, 7, 1).color(PAGE).grad(PAGE, 0xb8a888).noise(0.1f).glow(Face.FRONT, 1, 1, 4, 5, 0xffe0a0);
        tome.box(-0.5f, -4, -1, 1, 8, 2).color(GOLD).flat();
        // drifting eyeballs
        for (int i = 0; i < 3; i++) {
            Part e = m.add("orb" + i).at(0, 30, 0);
            Part ee = e.add("orbp" + i).at(14, 0, 0);
            ee.box(-2, -2, -2, 4, 4, 4).color(0xfff0f8).flat().bevel(0.2f).glow(Face.FRONT, 1, 1, 2, 2, EYE).glow(Face.LEFT, 1, 1, 2, 2, EYE).glow(Face.RIGHT, 1, 1, 2, 2, EYE);
        }
        m.proc((r, c) -> {
            r.rot("halo", 0, 0, c.age * 1.2f);
            for (int i = 0; i < 3; i++) { r.rot("orb" + i, 0, c.age * 3 + i * 120, 0); r.pos("orbp" + i, 0, (float) Math.sin(c.age * 0.1f + i) * 4, 0); }
            r.pos("tome", 0, (float) Math.sin(c.age * 0.08f) * 1.5f, 0);
            float fl = (float) Math.sin(c.age * 0.12f) * 8; r.rot("pgL", 0, 12 + fl, 0); r.rot("pgR", 0, -12 - fl, 0);
            r.rot("robe", (float) Math.sin(c.age * 0.07f) * 3, 0, (float) Math.sin(c.age * 0.05f) * 3);
        });
        m.look("head", 60, 30);
        m.bob(2.5f, 0.07f);
        m.clip("walk", 20).loop().rot("torso", 0, 6, 0, 0).rot("torso", 10, 8, 0, 0).rot("torso", 20, 6, 0, 0);
        m.clip("idle", 60).loop()
            .rot("torso", 0, 1, 0, 0).rot("torso", 30, -1, 0, 0).rot("torso", 60, 1, 0, 0)
            .rot("armR", 0, -10, 0, 8).rot("armR", 30, -20, 0, 12).rot("armR", 60, -10, 0, 8)
            .rot("armL", 0, -10, 0, -8).rot("armL", 30, -20, 0, -12).rot("armL", 60, -10, 0, -8)
            .rot("armR2", 0, 0, 0, 0).rot("armR2", 30, 8, 0, 6).rot("armR2", 60, 0, 0, 0)
            .rot("armL2", 0, 0, 0, 0).rot("armL2", 30, 8, 0, -6).rot("armL2", 60, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, 0, 10, 3).rot("head", 40, 0, -10, -3).rot("head", 60, 0, 0, 0);
        // CURSE OF KNOWLEDGE: all four arms spread wide, head back, halo flares, tome flips open
        m.clip("curse_of_knowledge", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, 10, 0, 0).rot("torso", 22, -14, 0, 0, Ease.OUT_BACK).rot("torso", 40, -8, 0, 0).rot("torso", 50, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, 10, 0, 10).rot("armR", 22, -100, 0, 60, Ease.OUT_BACK).rot("armR", 40, -95, 0, 55).rot("armR", 50, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, 10, 0, -10).rot("armL", 22, -100, 0, -60, Ease.OUT_BACK).rot("armL", 40, -95, 0, -55).rot("armL", 50, 0, 0, 0)
            .rot("armR2", 0, 0, 0, 0).rot("armR2", 22, -150, 0, 30, Ease.OUT_BACK).rot("armR2", 40, -145, 0, 28).rot("armR2", 50, 0, 0, 0)
            .rot("armL2", 0, 0, 0, 0).rot("armL2", 22, -150, 0, -30, Ease.OUT_BACK).rot("armL2", 40, -145, 0, -28).rot("armL2", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 22, -30, 0, 0, Ease.OUT_BACK).rot("head", 44, -20, 0, 0).rot("head", 50, 0, 0, 0)
            .scl("halo", 0, 1, 1, 1).scl("halo", 22, 1.6f, 1.6f, 1.6f, Ease.OUT_BACK).scl("halo", 50, 1, 1, 1)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -3, 0).pos("body", 22, 0, 6, 0, Ease.OUT_BACK).pos("body", 50, 0, 0, 0);
        // SLAP: backhand with the upper right arm
        m.clip("slap", 28)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -5, -40, 0).rot("torso", 12, 12, 50, 0, Ease.IN_CUBIC).rot("torso", 28, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -100, -30, 70).rot("armR", 12, -80, 40, -30, Ease.IN_CUBIC).rot("armR", 18, -70, 40, -25).rot("armR", 28, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -20, 0, -30).rot("armL", 28, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 5, -25, 0).rot("head", 12, 0, 20, 0).rot("head", 28, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -2).pos("body", 12, 0, 0, 5, Ease.IN_CUBIC).pos("body", 28, 0, 0, 0);
        // KNOWLEDGE OVERWHELMING: tome swells, three beams of eye-light fired in turn with matching arm thrusts
        Clip ko = m.clip("knowledge_overwhelming", 44);
        ko.pos("tome", 0, 0, 0, 0).pos("tome", 10, 0, 2, 5).pos("tome", 44, 0, 0, 0)
          .scl("tome", 0, 1, 1, 1).scl("tome", 10, 1.4f, 1.4f, 1.4f, Ease.OUT_BACK).scl("tome", 44, 1, 1, 1)
          .rot("head", 0, 0, 0, 0).rot("head", 10, -20, 0, 0).rot("head", 16, 12, 0, 0, Ease.IN_CUBIC).rot("head", 44, 0, 0, 0);
        String[] an = {"armR", "armL", "armR2"}; int[] ft = {16, 24, 32};
        for (int i = 0; i < 3; i++) {
            float sd = an[i].startsWith("armR") ? 1 : -1;
            ko.rot(an[i], 0, 0, 0, 0).rot(an[i], ft[i] - 5, -40, 0, sd * 30).rot(an[i], ft[i], -105, 0, sd * 5, Ease.IN_CUBIC).rot(an[i], ft[i] + 6, -95, 0, sd * 5).rot(an[i], 44, 0, 0, 0);
        }
        ko.rot("torso", 0, 0, 0, 0).rot("torso", 10, -12, 0, 0).rot("torso", 16, 14, 0, 0, Ease.IN_CUBIC).rot("torso", 24, 10, 0, 0).rot("torso", 32, 12, 0, 0).rot("torso", 44, 0, 0, 0);
        // PONDER: hands steepled at the chin, head bowed, then eyes flare open and the body swells (heal)
        m.clip("ponder", 44)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -110, -30, 10).rot("armR", 34, -110, -30, 10).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -110, 30, -10).rot("armL", 34, -110, 30, -10).rot("armL", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 25, 0, 0).rot("head", 26, 25, 0, 0).rot("head", 30, -25, 0, 0, Ease.OUT_BACK).rot("head", 44, 0, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 30, 1.12f, 1.12f, 1.12f, Ease.OUT_BACK).scl("torso", 44, 1, 1, 1)
            .scl("halo", 0, 1, 1, 1).scl("halo", 30, 1.5f, 1.5f, 1.5f, Ease.OUT_BACK).scl("halo", 44, 1, 1, 1)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -3, 0).pos("body", 30, 0, 5, 0, Ease.OUT_BACK).pos("body", 44, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 14, 0, 6).rot("torso", 10, 0, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 3, -22, 0, 8).rot("head", 10, 0, 0, 0);
        m.clip("death", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -20, 0, 0).rot("torso", 44, 30, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, 5, 0).pos("body", 44, 0, -14, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -120, 0, 60).rot("armR", 44, 15, 0, 60)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -120, 0, -60).rot("armL", 44, 15, 0, -60)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -35, 0, 0).rot("head", 44, 35, 10, 8)
            .scl("halo", 0, 1, 1, 1).scl("halo", 20, 1.8f, 1.8f, 1.8f).scl("halo", 44, 0.2f, 0.2f, 0.2f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_knowledge_demon", "Knowledge Demon", "知識の悪魔")
            .game(2, 2).size(1.6f, 3.0f).hp(379).speed(0.2f).style(Style.MAGIC).egg(0x3c1e5a, 0xff40c0)
            .boss().flying().model(KnowledgeDemon::model)
            .pattern(s -> {
                int n = s.count("n"); s.inc("n");
                int k = n % 4;
                if (n >= 12) k = 1 + (n - 12) % 3;           // after 3 curses the cycle drops Curse of Knowledge
                return new String[]{"curse_of_knowledge", "slap", "knowledge_overwhelming", "ponder"}[k];
            })
            .move(Move.of("curse_of_knowledge", 50).intent(Intent.STRONG_DEBUFF).noTarget()
                .sound(16, SoundEvents.EVOKER_PREPARE_ATTACK, 1.6f, 0.6f)
                .at(22, (s, t) -> {
                    int set = Math.min(2, s.count("curses")); s.inc("curses");
                    Fx.ring(s, ParticleTypes.ENCHANT, s.position().add(0, 2, 0), 3, 30); Fx.particles(s, ParticleTypes.WITCH, 30, 1.2);
                    Fx.sound(s, SoundEvents.ENDER_DRAGON_GROWL, 1.2f, 1.6f);
                    for (net.minecraft.world.entity.player.Player p : H.players(s, 32)) {
                        boolean a = s.rand().nextBoolean();
                        H.bar(p, "Curse of Knowledge: " + (set == 0 ? (a ? "Disintegration" : "Mind Rot") : set == 1 ? (a ? "Sloth" : "Waste Away") : (a ? "Disintegration" : "Sloth")));
                        if (set == 0) { if (a) Fx.poison(p, 8); else Fx.blind(p, 6); }
                        else if (set == 1) { if (a) Fx.slow(p, 8, 1); else { Fx.weak(p, 8); Fx.frail(p, 8); } }
                        else { if (a) Fx.poison(p, 8); else Fx.slow(p, 8, 1); }
                    }
                }))
            .move(Move.of("slap", 28).intent(Intent.ATTACK, 17, 1).reach(3.2f).sound(8, SoundEvents.PHANTOM_SWOOP, 1f, 0.7f)
                .at(12, Fx.melee(17, 3.4, 90, e -> {})))
            .move(Move.of("knowledge_overwhelming", 44).intent(Intent.ATTACK, 8, 3).ranged(14).sound(10, SoundEvents.EVOKER_CAST_SPELL, 1.4f, 0.8f)
                .at(16, K).at(24, K).at(32, K))
            .move(Move.of("ponder", 44).intent(Intent.ATTACK_BUFF, 11, 1).reach(3.4f).sound(12, SoundEvents.ENCHANTMENT_TABLE_USE, 1.5f, 0.6f)
                .at(30, (s, t) -> { H.cone(s, 11, 3.6, 120, null, null); s.heal(30); s.addStrength(2); Fx.particles(s, ParticleTypes.HAPPY_VILLAGER, 20, 1); Fx.particles(s, ParticleTypes.ENCHANT, 30, 1.2); }));
    }

    static final Move.Action K = (s, t) -> {
        Fx.sound(s, SoundEvents.GUARDIAN_ATTACK, 1f, 1.6f);
        if (t != null) { H.line(s, ParticleTypes.WITCH, Fx.rel(s, 1.2, 0, 2.4), t.position().add(0, t.getBbHeight() * 0.6, 0), 14); H.hit(s, t, 8); }
    };
}
