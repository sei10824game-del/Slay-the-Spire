package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth: Leaf Slime (S) 11-15 HP / (M) 32-35 HP. Blue-teal jelly dome wearing a mossy, grassy cap, one sleepy orange eye.
 * (S): random Tackle (3) / Goop (1 Slimed), never the same twice.  (M): Sticky Shot (2 Slimed) first, then alternates Clump Shot (8) / Sticky Shot.
 */
public final class LeafSlime {
    private LeafSlime() {}

    static final int JELLY = 0x6fa6b4, JELLY_D = 0x3f7584, JELLY_L = 0x9cd0da, MOSS = 0x7a9a32, MOSS_D = 0x4e6a1c, MOSS_L = 0xa6c04a,
        EYE = 0xe39a3a, PUPIL = 0x2a2a2a, LID = 0x4c8796;

    /** medium model (small = same, scaled) */
    public static ModelSpec model() { return build("s2_leaf_slime_m", 1.0f); }
    public static ModelSpec modelS() { return build("s2_leaf_slime_s", 0.62f); }

    static ModelSpec build(String id, float sc) {
        ModelSpec m = new ModelSpec(id).scale(sc);
        Part blob = m.add("blob").at(0, 0, 0);
        blob.box(-8, 0, -7, 16, 6, 14).color(JELLY).grad(JELLY_L, JELLY_D).noise(0.05f).bevel(0.1f)
            .paint(Face.FRONT, 2, 4, 12, 1, 0x34606c);                                  // mouth crease
        blob.box(-9, 0, -6, 18, 3, 12).color(JELLY_D).grad(JELLY, JELLY_D).noise(0.04f);  // puddle rim
        blob.box(-7, 6, -6, 14, 3, 12).color(JELLY).grad(JELLY_L, JELLY);                 // upper dome
        Part eye = blob.add("eye").at(-1, 5, 7);
        eye.box(-3, -2, -0.5f, 6, 4, 1).color(EYE).flat().paint(Face.FRONT, 0, 0, 6, 1, 0xb06a20)
            .glow(Face.FRONT, 1, 1, 2, 2, EYE).paint(Face.FRONT, 3, 1, 2, 2, PUPIL);
        Part lid = eye.add("lid").at(0, 2, 0.2f);
        lid.box(-3.5f, -2, -0.5f, 7, 2, 1).color(LID).flat().paint(Face.FRONT, 0, 1, 7, 1, 0x2b5864);
        Part cap = blob.add("cap").at(0, 9, 0);
        cap.box(-6, -1, -5, 12, 3, 10).color(MOSS).grad(MOSS_L, MOSS_D).noise(0.18f);
        cap.box(-7.5f, -3, -6, 15, 2, 12).color(MOSS_D).grad(MOSS, MOSS_D).noise(0.2f);     // moss drape
        cap.box(-4, 2, -3, 8, 1, 6).color(MOSS_L).noise(0.2f);
        // grass blades and the big curled leaf on top
        float[][] blades = {{-4, -2}, {-1, 2}, {3, -1}, {5, 3}, {-5, 3}, {1, -3}};
        for (int i = 0; i < blades.length; i++) {
            Part b = cap.add("blade" + i).at(blades[i][0], 2, blades[i][1]).rot((i % 2 == 0 ? 15 : -15), i * 40, (i % 3 - 1) * 15);
            b.box(-0.5f, 0, -0.5f, 1, 3 + i % 3, 1).color(i % 2 == 0 ? MOSS_L : MOSS).flat();
        }
        Part leaf = cap.add("leaf").at(1, 2, -1).rot(-20, 0, -10);
        leaf.box(-1, 0, -1.5f, 2, 4, 3).color(MOSS).grad(MOSS_L, MOSS_D);
        Part leafTip = leaf.add("leafTip").at(0, 4, 0).rot(-35, 0, 0);
        leafTip.box(-0.5f, 0, -1, 1, 4, 2).color(MOSS_L).flat();

        m.clip("idle", 40).loop()
            .scl("blob", 0, 1, 1, 1).scl("blob", 20, 1.06f, 0.92f, 1.06f).scl("blob", 40, 1, 1, 1)
            .rot("cap", 0, 0, 0, 0).rot("cap", 20, 0, 0, 3).rot("cap", 40, 0, 0, 0)
            .rot("leaf", 0, 0, 0, 0).rot("leaf", 20, 6, 0, 8).rot("leaf", 40, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 30, 0, 0, 0).pos("lid", 32, 0, -2, 0).pos("lid", 35, 0, 0, 0);
        m.clip("walk", 16).loop()
            .scl("blob", 0, 1.1f, 0.85f, 1.1f).scl("blob", 6, 0.92f, 1.15f, 0.92f).scl("blob", 16, 1.1f, 0.85f, 1.1f)
            .pos("blob", 0, 0, 0, 0).pos("blob", 6, 0, 3, 0, Ease.OUT_QUAD).pos("blob", 12, 0, 0, 0, Ease.IN_QUAD).pos("blob", 16, 0, 0, 0)
            .rot("leaf", 0, 0, 0, 0).rot("leaf", 8, 15, 0, 0).rot("leaf", 16, 0, 0, 0);
        // TACKLE: squash down, spring forward in a belly-flop, splat and jiggle
        m.clip("tackle", 30)
            .scl("blob", 0, 1, 1, 1).scl("blob", 9, 1.25f, 0.65f, 1.2f).scl("blob", 13, 0.85f, 1.25f, 1.1f, Ease.OUT_QUAD).scl("blob", 17, 1.3f, 0.7f, 1.25f).scl("blob", 22, 0.95f, 1.08f, 0.95f, Ease.OUT_ELASTIC).scl("blob", 30, 1, 1, 1)
            .pos("blob", 0, 0, 0, 0).pos("blob", 9, 0, 0, -2).pos("blob", 13, 0, 5, 5, Ease.OUT_QUAD).pos("blob", 17, 0, 0, 8, Ease.IN_QUAD).pos("blob", 30, 0, 0, 0)
            .rot("blob", 0, 0, 0, 0).rot("blob", 9, -8, 0, 0).rot("blob", 13, 12, 0, 0).rot("blob", 17, 4, 0, 0).rot("blob", 30, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 9, 0, -1.5f, 0).pos("lid", 13, 0, 0, 0)
            .rot("leaf", 0, 0, 0, 0).rot("leaf", 13, -30, 0, 0).rot("leaf", 18, 30, 0, 0).rot("leaf", 30, 0, 0, 0);
        // GOOP / STICKY SHOT: swell up with a gulp, lean back, then spit a glob forward
        m.clip("sticky_shot", 32)
            .scl("blob", 0, 1, 1, 1).scl("blob", 10, 1.1f, 1.15f, 1.1f).scl("blob", 14, 1.15f, 1.2f, 1.15f).scl("blob", 17, 0.9f, 0.85f, 1.2f, Ease.IN_CUBIC).scl("blob", 24, 1.04f, 0.97f, 1, Ease.OUT_ELASTIC).scl("blob", 32, 1, 1, 1)
            .rot("blob", 0, 0, 0, 0).rot("blob", 12, -12, 0, 0).rot("blob", 17, 10, 0, 0, Ease.IN_CUBIC).rot("blob", 32, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 10, 0, 0.8f, 0).pos("lid", 17, 0, -2, 0).pos("lid", 26, 0, 0, 0)
            .rot("cap", 0, 0, 0, 0).rot("cap", 12, -6, 0, 0).rot("cap", 18, 10, 0, 0).rot("cap", 32, 0, 0, 0);
        // CLUMP SHOT (M): the mossy cap shoots up and a clump of moss is hurled
        m.clip("clump_shot", 34)
            .scl("blob", 0, 1, 1, 1).scl("blob", 10, 1.25f, 0.7f, 1.25f).scl("blob", 15, 0.85f, 1.3f, 0.85f, Ease.OUT_BACK).scl("blob", 24, 1.02f, 0.98f, 1.02f).scl("blob", 34, 1, 1, 1)
            .pos("cap", 0, 0, 0, 0).pos("cap", 10, 0, -2, 0).pos("cap", 15, 0, 5, 2, Ease.OUT_BACK).pos("cap", 24, 0, 0, 0, Ease.OUT_BOUNCE)
            .rot("cap", 0, 0, 0, 0).rot("cap", 10, -10, 0, 0).rot("cap", 15, 25, 0, 0).rot("cap", 24, 0, 0, 0)
            .rot("leaf", 0, 0, 0, 0).rot("leaf", 15, 40, 0, 0).rot("leaf", 26, -15, 0, 0).rot("leaf", 34, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 10, 0, -2, 0).pos("lid", 15, 0, 1, 0).pos("lid", 34, 0, 0, 0);
        m.clip("hurt", 10)
            .scl("blob", 0, 1, 1, 1).scl("blob", 3, 1.2f, 0.75f, 1.2f).scl("blob", 10, 1, 1, 1)
            .pos("lid", 0, 0, 0, 0).pos("lid", 3, 0, -2, 0).pos("lid", 10, 0, 0, 0);
        m.clip("death", 26)
            .scl("blob", 0, 1, 1, 1).scl("blob", 6, 0.9f, 1.2f, 0.9f).scl("blob", 18, 1.7f, 0.25f, 1.7f, Ease.IN_QUAD).scl("blob", 26, 1.8f, 0.12f, 1.8f)
            .pos("lid", 0, 0, 0, 0).pos("lid", 6, 0, -2, 0).pos("lid", 26, 0, -2, 0)
            .rot("leaf", 0, 0, 0, 0).rot("leaf", 18, 70, 0, 30).rot("leaf", 26, 80, 0, 40);
        return m;
    }

    static void kinds() {
        if (ProjKind.of("s2_leaf_goop") == null) ProjKind.orb("s2_leaf_goop", 0x7ab8c4, 3).gravity(0.025).trail(ParticleTypes.ITEM_SLIME);
        if (ProjKind.of("s2_leaf_clump") == null) ProjKind.orb("s2_leaf_clump", 0x7a9a32, 4).gravity(0.03).trail(ParticleTypes.COMPOSTER);
    }

    static Move.Action goop(int n) {
        return (s, t) -> { Fx.shoot("s2_leaf_goop", 0, 0.9f, 0, 0.5, 0.5, null).run(s, t); if (t != null) for (int i = 0; i < n; i++) Fx.curse(t, Fx.Curse.SLIMED); };
    }

    public static MobDef defS() {
        kinds();
        return MobDef.of("s2_leaf_slime_s", "Leaf Slime (S)", "リーフスライム (小)")
            .game(2, 1).size(0.7f, 0.55f).hp(13).speed(0.24f).style(Style.SLIME).egg(0x6fa6b4, 0x7a9a32)
            .model(LeafSlime::modelS)
            .spawn(6, 1, 2, MobDef.Habitat.SWAMP)
            .pattern(Pattern.random().add("tackle", 1).add("goop", 1).maxRepeat("tackle", 1).maxRepeat("goop", 1))
            .move(Move.of("tackle", 30).intent(Intent.ATTACK, 3, 1).reach(2.0f)
                .sound(10, SoundEvents.SLIME_JUMP_SMALL, 1f, 1.2f).at(12, Fx.dash(0.4, 0.15)).at(16, Fx.melee(3, 2.2, 90)))
            .move(Move.of("goop", 32).anim("sticky_shot").intent(Intent.DEBUFF).ranged(10)
                .sound(16, SoundEvents.SLIME_SQUISH_SMALL, 1.2f, 1.3f).at(17, goop(1)));
    }

    public static MobDef defM() {
        kinds();
        return MobDef.of("s2_leaf_slime_m", "Leaf Slime (M)", "リーフスライム (中)")
            .game(2, 1).size(1.1f, 0.9f).hp(33).speed(0.22f).style(Style.SLIME).egg(0x6fa6b4, 0x4e6a1c)
            .model(LeafSlime::model)
            .spawn(5, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(Pattern.cycle("sticky_shot", "clump_shot"))
            .move(Move.of("sticky_shot", 32).intent(Intent.DEBUFF).ranged(10)
                .sound(16, SoundEvents.SLIME_SQUISH, 1.2f, 1.0f).at(17, goop(2)))
            .move(Move.of("clump_shot", 34).intent(Intent.ATTACK, 8, 1).ranged(10)
                .sound(14, SoundEvents.SLIME_JUMP, 1.2f, 0.8f).at(15, Fx.shoot("s2_leaf_clump", 8, 1.0f, 0, 1.2, 0.3, null)));
    }
}
