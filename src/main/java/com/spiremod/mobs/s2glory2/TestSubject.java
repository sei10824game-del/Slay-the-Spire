package com.spiremod.mobs.s2glory2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * StS2 Glory boss: Test Subject (Adaptable: revives twice). Phase 1 (100 HP, Enrage 2): Bite 20 / Skull Bash 14 + Vulnerable alternating.
 * Phase 2 (200 HP, Painful Stabs): Multi-Claw 10 x (3, +1 each use). Phase 3 (300 HP, Nemesis): Lacerate 10x3 -> Big Pounce 45 -> Burning Growl (Burn, +2 Str).
 * MC translation: Enrage = +2 Str whenever the target starts using an item (card-play analogue); Painful Stabs = Wither on hit; Burn = fire; Nemesis = Intangible every other move.
 */
public final class TestSubject {
    private TestSubject() {}

    static final String ID = "s2_test_subject";
    static final int SKIN = 0x93a08a, SKIN_D = 0x5f6a55, SKIN_L = 0xb4bea0, BAND = 0xd8d0b4, BAND_D = 0x9a9078, CLAW = 0xe6dec4, SPIKE = 0xb9bcc6,
        RED = 0xff3a30, STITCH = 0x1c1a16, GUM = 0x7a2a30, MASK = 0xe8e4ee, VIOLET = 0xb060ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec(ID).scale(1.2f);
        for (int s = -1; s <= 1; s += 2) { // hind legs: thick, digitigrade-ish
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 4, 10, 0);
            leg.box(-2.5f, -10, -2.5f, 5, 10, 5).color(SKIN).grad(SKIN_L, SKIN_D).noise(0.12f);
            leg.box(-2.8f, -7, -2.8f, 5, 2, 5).inflate(0.3f).color(BAND).flat().noise(0.08f);
            leg.box(-3, -10, -4, 6, 2, 7).color(SKIN_D).bevel(0.2f);
            for (int i = -1; i <= 1; i++) leg.box(i * 2 - 0.5f, -10, 3, 1, 2, 2).color(CLAW).flat();
        }
        Part body = m.add("body").at(0, 10, 0);
        body.box(-4.5f, 0, -3, 9, 4, 6).color(SKIN_D).grad(SKIN, SKIN_D);
        Part torso = body.add("torso").at(0, 4, 0).rot(22, 0, 0);          // hunched forward
        torso.box(-6, 0, -4, 12, 10, 8).color(SKIN).grad(SKIN_L, SKIN).noise(0.12f).bevel(0.1f)
            .paint(Face.FRONT, 2, 1, 8, 2, BAND).paint(Face.FRONT, 1, 4, 10, 2, BAND_D).paint(Face.FRONT, 3, 7, 6, 2, BAND)
            .px(Face.FRONT, 6, 0, STITCH).px(Face.FRONT, 6, 3, STITCH).px(Face.FRONT, 6, 6, STITCH).px(Face.FRONT, 6, 9, STITCH);
        torso.box(-6.2f, 2, -4.2f, 12, 2, 8).inflate(0.3f).color(BAND).flat().noise(0.06f);  // strapping
        // back spikes (phase 1: short; phase 2: extra long set; phase 3: burn crest)
        Part sp1 = torso.add("spikes1").at(0, 10, -3);
        for (int i = -1; i <= 1; i++) sp1.box(i * 3 - 0.5f, -2, -1.5f, 1, 5, 1).color(SPIKE).flat();
        Part sp2 = torso.add("spikes2").at(0, 10, -3);
        for (int i = -2; i <= 2; i++) { Part sp = sp2.add("spk" + (i + 2)).at(i * 2.4f, 0, -1).rot(-35 - Math.abs(i) * 8, 0, i * 10);
            sp.box(-0.5f, 0, -0.5f, 1, 9 - Math.abs(i), 1).grad(0xe8eaf0, SPIKE).flat(); }
        Part crest = torso.add("burn").at(0, 9, -3.5f);
        for (int i = -2; i <= 2; i++) G.fire(crest.box(i * 2.4f - 1, 0, -1, 2, 7 - Math.abs(i) * 2, 2), 0xff7a20).grad(0xffe070, 0xff5a10);
        // head, jaw
        Part head = torso.add("head").at(0, 9, 3).rot(-14, 0, 0);
        head.box(-3.5f, 0, -3, 7, 6, 7).color(SKIN).grad(SKIN_L, SKIN).bevel(0.1f).noise(0.12f)
            .glow(Face.FRONT, 1, 3, 1, 1, RED).glow(Face.FRONT, 5, 3, 1, 1, RED).paint(Face.FRONT, 0, 2, 3, 1, SKIN_D).paint(Face.FRONT, 4, 2, 3, 1, SKIN_D)
            .px(Face.FRONT, 3, 0, STITCH).px(Face.FRONT, 3, 1, STITCH);
        head.box(-3.7f, 4.5f, -3.2f, 7, 2, 7).inflate(0.2f).color(BAND_D).flat();                // head bandage
        head.box(-2, 0, 3.8f, 4, 3, 3).color(SKIN_L).bevel(0.1f);                               // snout
        Part jaw = head.add("jaw").at(0, 0.5f, 3);
        jaw.box(-2.5f, -2, 0, 5, 2, 5).color(SKIN_D).bevel(0.1f).paint(Face.TOP, 0, 0, 5, 5, GUM);
        for (int i = -1; i <= 1; i++) jaw.box(i * 1.6f - 0.4f, 0, 4, 1, 2, 1).color(CLAW).flat();
        for (int s = -1; s <= 1; s += 2) head.box(s * 1.8f - 0.4f, -2, 6.5f, 1, 3, 1).color(CLAW).flat();   // upper fangs
        Part mask = head.add("mask").at(0, 0, 0);
        mask.box(-3.8f, -1, 3.2f, 8, 8, 1).color(MASK).bevel(0.15f).glow(Face.FRONT, 1, 3, 2, 1, VIOLET).glow(Face.FRONT, 5, 3, 2, 1, VIOLET).paint(Face.FRONT, 3, 5, 2, 3, 0x6a6478);
        mask.box(-1, 7, 3.4f, 2, 3, 1).color(0x6a6478).flat();
        // arms with huge claws
        for (int s = -1; s <= 1; s += 2) {
            String sd = s < 0 ? "R" : "L";
            Part arm = torso.add("arm" + sd).at(s * 7.5f, 8, 0).rot(-10, 0, s * -6);
            arm.box(-2.5f, -8, -2.5f, 5, 8, 5).color(SKIN).grad(SKIN_L, SKIN_D).noise(0.12f);
            arm.box(-2.8f, -4, -2.8f, 5, 2, 5).inflate(0.3f).color(BAND).flat();
            Part fore = arm.add("fore" + sd).at(0, -8, 0).rot(-25, 0, 0);
            fore.box(-2, -8, -2, 4, 8, 4).color(SKIN_D).noise(0.12f);
            fore.box(-2.3f, -3, -2.3f, 4, 2, 4).inflate(0.3f).color(BAND_D).flat();
            Part hand = fore.add("hand" + sd).at(0, -8, 0);
            hand.box(-2.5f, -3, -2.5f, 5, 3, 5).color(SKIN).bevel(0.2f);
            for (int i = -1; i <= 1; i++) {
                hand.box(i * 1.8f - 0.5f, -9, 1.2f, 1, 6, 1).grad(CLAW, 0xa09878).flat();
                hand.box(i * 1.8f - 0.5f, -9, 0.2f, 1, 3, 1).color(CLAW).flat();
            }
        }
        m.look("head", 40, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 32f, 26f);
        m.proc((r, c) -> {
            r.show("spikes2", c.phase >= 1); r.show("spikes1", c.phase == 0);
            r.show("mask", c.phase >= 2); r.show("burn", c.phase >= 2);
            if (c.phase >= 2) { float f = 1 + 0.2f * (float) Math.sin(c.age * 0.9); r.scl("burn", 1, f, 1); }
        });
        m.clip("idle", 40).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 20, -2, 0, 0).rot("torso", 40, 2, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 0, 8, 0).rot("head", 28, 0, -8, 0).rot("head", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 20, 8, 0, 0).rot("jaw", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 20, 6, 0, 4).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 20, 6, 0, -4).rot("armL", 40, 0, 0, 0);
        // knocked out: slumped in a heap, twitching (phase transition)
        m.clip("ko", 40).loop()
            .pos("body", 0, 0, -6, 2).rot("body", 0, 35, 0, 0)
            .rot("torso", 0, 30, 0, 4).rot("torso", 20, 33, 0, 4).rot("torso", 40, 30, 0, 4)
            .rot("head", 0, 30, 10, 12).rot("armR", 0, 10, 0, 40).rot("armL", 0, 20, 0, -40)
            .rot("legR", 0, -70, 0, 12).rot("legL", 0, -50, 0, -12)
            .rot("jaw", 0, 20, 0, 0).rot("jaw", 20, 26, 0, 0).rot("jaw", 40, 20, 0, 0);
        // regenerate: convulses, rears up, roars (new body part grows in)
        m.clip("regenerate", 44)
            .rot("torso", 0, 30, 0, 0).rot("torso", 12, 10, 0, 0).rot("torso", 24, -30, 0, 0, Ease.OUT_BACK).rot("torso", 34, -22, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 24, -40, 0, 0, Ease.OUT_BACK).rot("head", 44, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 24, 50, 0, 0).rot("jaw", 36, 50, 0, 0).rot("jaw", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 24, -150, 0, 40, Ease.OUT_BACK).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 24, -150, 0, -40, Ease.OUT_BACK).rot("armL", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -2, 0).pos("body", 24, 0, 2, 0, Ease.OUT_BACK).pos("body", 44, 0, 0, 0);
        // bite: rear back, jaw wide, lunge and snap
        m.clip("bite", 30)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -14, 0, 0).rot("torso", 15, 30, 0, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -3).pos("body", 15, 0, -1, 6, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -30, 0, 0).rot("head", 15, 22, 0, 0, Ease.IN_CUBIC).rot("head", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 60, 0, 0).rot("jaw", 14, 70, 0, 0).rot("jaw", 16, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 22, 14, 0, 0).rot("jaw", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 30, 0, 20).rot("armR", 15, -50, 0, 0).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 30, 0, -20).rot("armL", 15, -50, 0, 0).rot("armL", 30, 0, 0, 0);
        // skull bash: head hammered down like a battering ram
        m.clip("skull_bash", 32)
            .rot("torso", 0, 0, 0, 0).rot("torso", 11, -22, 0, 0).rot("torso", 17, 42, 0, 0, Ease.IN_CUBIC).rot("torso", 26, 36, 0, 0).rot("torso", 32, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 11, 0, 0, -4).pos("body", 17, 0, -2, 7, Ease.IN_CUBIC).pos("body", 26, 0, -2, 7).pos("body", 32, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 11, -40, 0, 0).rot("head", 17, 30, 0, 0, Ease.IN_CUBIC).rot("head", 32, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 11, 40, 0, 40).rot("armR", 17, -30, 0, -10).rot("armR", 32, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 11, 40, 0, -40).rot("armL", 17, -30, 0, 10).rot("armL", 32, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 11, 20, 0, 0).rot("legR", 17, -35, 0, 0).rot("legR", 32, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 11, 20, 0, 0).rot("legL", 17, -35, 0, 0).rot("legL", 32, 0, 0, 0);
        for (int n = 3; n <= 9; n++) clawClip(m, "multi_claw_" + n, n);
        clawClip(m, "lacerate", 3);
        // big pounce: crouch, leap, crash down with both claws
        m.clip("big_pounce", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, 32, 0, 0).rot("torso", 22, -24, 0, 0, Ease.OUT_BACK).rot("torso", 30, 48, 0, 0, Ease.IN_CUBIC).rot("torso", 38, 40, 0, 0).rot("torso", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -5, -2).pos("body", 22, 0, 10, 4, Ease.OUT_CUBIC).pos("body", 30, 0, -3, 8, Ease.IN_CUBIC).pos("body", 38, 0, -3, 8).pos("body", 44, 0, 0, 0)
            .pos("legR", 0, 0, 0, 0).pos("legR", 14, 0, -4, -2).pos("legR", 22, 0, 10, 4, Ease.OUT_CUBIC).pos("legR", 30, 0, -3, 8, Ease.IN_CUBIC).pos("legR", 38, 0, -3, 8).pos("legR", 44, 0, 0, 0)
            .pos("legL", 0, 0, 0, 0).pos("legL", 14, 0, -4, -2).pos("legL", 22, 0, 10, 4, Ease.OUT_CUBIC).pos("legL", 30, 0, -3, 8, Ease.IN_CUBIC).pos("legL", 38, 0, -3, 8).pos("legL", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, 30, 0, 30).rot("armR", 22, -160, 0, 30).rot("armR", 30, -50, 0, -5, Ease.IN_CUBIC).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, 30, 0, -30).rot("armL", 22, -160, 0, -30).rot("armL", 30, -50, 0, 5, Ease.IN_CUBIC).rot("armL", 44, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 14, 45, 0, 0).rot("legR", 22, -50, 0, 0).rot("legR", 30, 10, 0, 0).rot("legR", 44, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 14, 45, 0, 0).rot("legL", 22, -50, 0, 0).rot("legL", 30, 10, 0, 0).rot("legL", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 22, -30, 0, 0).rot("head", 30, 25, 0, 0).rot("head", 44, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 22, 45, 0, 0).rot("jaw", 30, 5, 0, 0).rot("jaw", 44, 0, 0, 0);
        // burning growl: drops to all fours-ish, flames surge, head back roar
        m.clip("burning_growl", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 20, 0, 0).rot("torso", 22, -34, 0, 0, Ease.OUT_BACK).rot("torso", 38, -28, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 22, -45, 0, 0, Ease.OUT_BACK).rot("head", 38, -40, 0, 0).rot("head", 44, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 22, 60, 0, 0).rot("jaw", 38, 55, 0, 0).rot("jaw", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, 30, 0, 20).rot("armR", 22, -40, 0, 60, Ease.OUT_BACK).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 30, 0, -20).rot("armL", 22, -40, 0, -60, Ease.OUT_BACK).rot("armL", 44, 0, 0, 0)
            .scl("burn", 0, 1, 1, 1).scl("burn", 22, 1.5f, 1.8f, 1.5f, Ease.OUT_BACK).scl("burn", 44, 1, 1, 1);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -14, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -24, 10, 0).rot("head", 10, 0, 0, 0).rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 35, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -20, 0, 0).rot("torso", 40, 60, 0, 8, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 40, 0, -8, -3, Ease.IN_QUAD)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -35, 0, 0).rot("head", 40, 35, 12, 14)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 60, 0, 0).rot("jaw", 40, 30, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -100, 0, 40).rot("armR", 40, 10, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -80, 0, -40).rot("armL", 40, 10, 0, -70)
            .rot("legR", 0, 0, 0, 0).rot("legR", 40, -70, 0, 10).rot("legL", 0, 0, 0, 0).rot("legL", 40, -60, 0, -10);
        return m;
    }

    /** n alternating claw swings, one every 6 ticks starting at tick 10 */
    static void clawClip(ModelSpec m, String name, int n) {
        int len = 10 + n * 6 + 10;
        Clip c = m.clip(name, len)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 8, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 8, -14, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 30, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -120, 0, 50).rot("armL", 0, 0, 0, 0).rot("armL", 8, -120, 0, -50);
        for (int i = 0; i < n; i++) {
            int t = 10 + i * 6; boolean r = i % 2 == 0; String hit = r ? "armR" : "armL", other = r ? "armL" : "armR";
            c.rot(hit, t, -70, r ? -35 : 35, r ? -10 : 10, Ease.IN_CUBIC).rot(hit, t + 4, -115, 0, r ? 50 : -50)
             .rot(other, t, -105, 0, r ? -50 : 50).rot(other, t + 4, -105, 0, r ? -50 : 50)
             .rot("torso", t, 14, r ? -18 : 18, 0, Ease.IN_CUBIC).rot("torso", t + 4, 6, 0, 0)
             .pos("body", t, 0, 0, 2.5f, Ease.IN_CUBIC).pos("body", t + 4, 0, 0, 0)
             .rot("jaw", t, 10, 0, 0).rot("jaw", t + 3, 35, 0, 0);
        }
        int e = 10 + n * 6 + 4;
        c.rot("armR", len, 0, 0, 0).rot("armL", len, 0, 0, 0).rot("torso", len, 0, 0, 0).rot("head", len, 0, 0, 0).rot("jaw", len, 0, 0, 0).pos("body", len, 0, 0, 0)
         .rot("armR", e, -115, 0, 50).rot("armL", e, -105, 0, -50);
    }

    public static MobDef def() {
        MobDef d = MobDef.of(ID, "Test Subject", "実験体")
            .game(2, 3).size(1.5f, 2.3f).hp(100).speed(0.28f).style(Style.BEAST).egg(0x93a08a, 0xff3a30).boss().base("idle")
            .model(TestSubject::model)
            .pattern(s -> {
                switch (s.getPhase()) {
                    case 0: return s.lastMove().equals("bite") ? "skull_bash" : "bite";
                    case 1: return "multi_claw_" + Math.min(9, 3 + s.count("mc"));
                    default:
                        switch (s.lastMove()) { case "lacerate": return "big_pounce"; case "big_pounce": return "burning_growl"; default: return "lacerate"; }
                }
            })
            .onHurt((s, src, dmg) -> {
                if (src.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return dmg;
                if (s.count("ko") > 0) return 0f;                                         // down: cannot be targeted until it revives
                if (s.getPhase() == 2) return s.getVar() == 1 ? Math.min(dmg, Cfg.DMG) : dmg;   // Nemesis: Intangible
                if (dmg >= s.getHealth()) {                                               // Adaptable
                    var a = s.getAttribute(Attributes.MAX_HEALTH); if (a != null) a.setBaseValue(s.getPhase() == 0 ? 200 : 300);
                    s.setHealth(1f); s.setCount("ko", 60); s.setBase("ko"); s.stun(60);
                    Fx.sound(s, SoundEvents.RAVAGER_STUNNED, 1.5f, 0.6f);
                    return 0f;
                }
                return dmg;
            })
            .onTick(s -> {
                int ko = s.count("ko");
                if (ko > 0) {
                    s.setCount("ko", ko - 1);
                    float max = s.getPhase() == 0 ? 200 : 300;
                    s.setHealth(1 + (max - 1) * (60 - ko + 1) / 60f);
                    if (ko == 1) { revive(s); }
                    return;
                }
                if (s.getPhase() == 0 && s.getTarget() != null && s.tickCount % 2 == 0) { // Enrage: +2 Strength whenever the target "plays a skill"
                    boolean use = s.getTarget().isUsingItem(); boolean was = s.flags.contains("using");
                    if (use && !was) { s.addStrength(2); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.5); Fx.sound(s, SoundEvents.RAVAGER_ROAR, 0.6f, 1.6f); }
                    if (use) s.flags.add("using"); else s.flags.remove("using");
                }
            })
            .onMoveEnd(s -> {
                if (s.lastMove().startsWith("multi_claw")) s.inc("mc");
                if (s.getPhase() == 2) s.setVar(s.inc("nem") % 2 == 0 ? 1 : 0);       // Nemesis: Intangible at the end of every other turn
            })
            .onClientTick(s -> {
                if (s.getPhase() == 2 && s.getVar() == 1 && s.tickCount % 3 == 0)
                    s.level().addParticle(ParticleTypes.END_ROD, s.getX() + (s.getRandom().nextDouble() - .5), s.getY() + 1 + s.getRandom().nextDouble() * 1.5, s.getZ() + (s.getRandom().nextDouble() - .5), 0, 0.02, 0);
                if (s.getPhase() == 2 && s.tickCount % 4 == 0)
                    s.level().addParticle(ParticleTypes.FLAME, s.getX() + (s.getRandom().nextDouble() - .5), s.getY() + 2.4, s.getZ() + (s.getRandom().nextDouble() - .5), 0, 0.04, 0);
            });
        d.move(Move.of("bite", 30).intent(Intent.ATTACK, 20, 1).reach(2.6f)
            .sound(12, SoundEvents.WOLF_GROWL, 1.2f, 0.5f)
            .at(16, (s, t) -> { Fx.meleeNow(s, 20, 3.0, 90, null); Fx.sound(s, SoundEvents.PLAYER_ATTACK_CRIT, 1f, 0.7f); }))
            .move(Move.of("skull_bash", 32).intent(Intent.ATTACK_DEBUFF, 14, 1).reach(2.6f)
                .at(18, (s, t) -> { Fx.meleeNow(s, 14, 3.0, 90, v -> { Fx.vulnerable(v, 8); Fx.knock(s, v, 0.8); }); Fx.sound(s, SoundEvents.ANVIL_LAND, 0.7f, 0.5f); }))
            .move(Move.of("regenerate", 44).intent(Intent.BUFF).noTarget()
                .at(24, (s, t) -> { Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 30, 1.0); Fx.sound(s, SoundEvents.RAVAGER_ROAR, 1.6f, 0.6f); }))
            .move(Move.of("lacerate", 3 * 6 + 20).anim("lacerate").intent(Intent.ATTACK, 10, 3).reach(3.0f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.7f)
                .at(10, G.cone(10, 3.4, 130)).at(16, G.cone(10, 3.4, 130)).at(22, G.cone(10, 3.4, 130)))
            .move(Move.of("big_pounce", 44).intent(Intent.ATTACK, 45, 1).reach(8f).moving().faceUntil(22)
                .sound(14, SoundEvents.RAVAGER_ROAR, 1f, 0.8f)
                .at(20, Fx.dash(1.1, 0.55))
                .at(31, (s, t) -> { Fx.aoeAt(s, s.position().add(Fx.forward(s).scale(1.5)), 45, 3.2, v -> Fx.knock(s, v, 1.2));
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.8f, 0.8f); Fx.particles(s, ParticleTypes.CAMPFIRE_COSY_SMOKE, 10, 0.8); }))
            .move(Move.of("burning_growl", 44).intent(Intent.DEBUFF).noTarget()
                .sound(18, SoundEvents.RAVAGER_ROAR, 1.6f, 0.5f)
                .at(22, (s, t) -> { s.addStrength(3);
                    for (var v : Fx.foes(s, s.getBoundingBox().inflate(14, 4, 14))) Fx.curse(v, Fx.Curse.BURN);
                    Fx.particles(s, ParticleTypes.FLAME, 40, 1.2); }));
        for (int n = 3; n <= 9; n++) {
            Move mv = Move.of("multi_claw_" + n, 10 + n * 6 + 10).intent(Intent.ATTACK, 10, n).reach(3.0f).sound(8, SoundEvents.RAVAGER_ATTACK, 1f, 1.2f);
            for (int i = 0; i < n; i++) mv.at(10 + i * 6, G.cone(10, 3.4, 130, v -> Fx.curse(v, Fx.Curse.WOUND)));   // Painful Stabs
            d.move(mv);
        }
        return d;
    }

    static void revive(SpireMob s) {
        int ph = s.getPhase() + 1;
        s.setPhase(ph);
        float max = ph == 1 ? 200 : 300;
        var a = s.getAttribute(Attributes.MAX_HEALTH); if (a != null) a.setBaseValue(max);
        s.setHealth(max);
        s.addStrength(-s.getStrength());                       // every phase loses all statuses
        s.setCount("mc", 0); s.setCount("nem", 0); s.setVar(ph == 2 ? 1 : 0); s.flags.remove("using");
        s.setBase("idle"); s.forceNext("regenerate");
        Fx.sound(s, SoundEvents.WARDEN_SONIC_BOOM, 1.2f, 0.7f);
    }
}
