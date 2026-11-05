package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth: Twig Slime (S) 7-11 HP / (M) 26-28 HP. Amber-brown jelly with a bark cap bristling with twigs.
 * (S): Tackle (4) every turn.  (M): Sticky Shot (1 Slimed) first, then Chomp (11) 67% / Sticky Shot 33% (never Sticky twice).
 */
public final class TwigSlime {
    private TwigSlime() {}

    static final int JELLY = 0xb98a4a, JELLY_D = 0x7a5428, JELLY_L = 0xe0b872, BARK = 0x5a3e24, BARK_D = 0x3a2616, BARK_L = 0x8a6440, EYE = 0xd8e84a;

    public static ModelSpec model() { return build("s2_twig_slime_m", 1.0f); }
    public static ModelSpec modelS() { return build("s2_twig_slime_s", 0.6f); }

    static ModelSpec build(String id, float sc) {
        ModelSpec m = new ModelSpec(id).scale(sc);
        Part blob = m.add("blob").at(0, 0, 0);
        blob.box(-8, 0, -7, 16, 6, 14).color(JELLY).grad(JELLY_L, JELLY_D).noise(0.06f).bevel(0.1f);
        blob.box(-9, 0, -6, 18, 3, 12).color(JELLY_D).grad(JELLY, JELLY_D).noise(0.05f);
        blob.box(-7, 6, -6, 14, 3, 12).color(JELLY).grad(JELLY_L, JELLY);
        Part jaw = blob.add("jaw").at(0, 3, 7);
        jaw.box(-5, -2, -1, 10, 2, 2).color(0x2a1408).flat().paint(Face.FRONT, 0, 0, 1, 1, 0xf0e8c8).paint(Face.FRONT, 3, 0, 1, 1, 0xf0e8c8).paint(Face.FRONT, 6, 0, 1, 1, 0xf0e8c8).paint(Face.FRONT, 9, 0, 1, 1, 0xf0e8c8);
        Part eye = blob.add("eye").at(0, 6.5f, 7);
        eye.box(-1.5f, -1.5f, -0.5f, 3, 3, 1).color(EYE).glow(Face.FRONT, 0, 0, 3, 3, EYE).paint(Face.FRONT, 1, 1, 1, 1, 0x20200a);
        Part lid = eye.add("lid").at(0, 1.5f, 0.2f);
        lid.box(-2, -1, -0.5f, 4, 1, 1).color(JELLY_D).flat();
        Part cap = blob.add("cap").at(0, 9, 0);
        cap.box(-6, -1, -5, 12, 3, 10).color(BARK).grad(BARK_L, BARK_D).noise(0.2f);
        cap.box(-7.5f, -3, -6, 15, 2, 12).color(BARK_D).grad(BARK, BARK_D).noise(0.2f);
        float[][] tw = {{-4, -2, 20}, {-1, 2, -15}, {3, -1, 25}, {5, 3, -30}, {-5, 3, 35}, {1, -3, -5}, {0, 0, 10}};
        for (int i = 0; i < tw.length; i++) {
            Part t = cap.add("twig" + i).at(tw[i][0], 2, tw[i][1]).rot(i % 2 == 0 ? 18 : -18, i * 50, tw[i][2]);
            t.box(-0.5f, 0, -0.5f, 1, 4 + i % 3 * 2, 1).color(i % 2 == 0 ? BARK : BARK_L).flat();
            Part tip = t.add("twigTip" + i).at(0, 4 + i % 3 * 2, 0).rot(0, 0, i % 2 == 0 ? 35 : -35);
            tip.box(-0.5f, 0, -0.5f, 1, 3, 1).color(BARK_L).flat();
            if (i % 3 == 0) tip.box(-1, 3, -1, 2, 1, 2).color(0x6a9a3a).flat();   // a stray green bud
        }
        m.look("blob", 0, 0);
        Clip c = m.clip("idle", 40).loop();
        Kit.S(c, "blob", 0, 1, 1, 1, 20, 1.06f, 0.92f, 1.06f, 40, 1, 1, 1);
        Kit.R(c, "cap", 0, 0, 0, 0, 20, 0, 0, 3, 40, 0, 0, 0);
        Kit.P(c, "lid", 0, 0, 0, 0, 30, 0, 0, 0, 32, 0, -2, 0, 35, 0, 0, 0);
        c = m.clip("walk", 16).loop();
        Kit.S(c, "blob", 0, 1.1f, 0.85f, 1.1f, 6, 0.92f, 1.15f, 0.92f, 16, 1.1f, 0.85f, 1.1f);
        Kit.P(c, "blob", 0, 0, 0, 0, 6, 0, 3, 0, 12, 0, 0, 0, 16, 0, 0, 0);
        Kit.R(c, "cap", 0, 0, 0, 0, 8, 10, 0, 0, 16, 0, 0, 0);
        c = m.clip("tackle", 28);
        Kit.S(c, "blob", 0, 1, 1, 1, 9, 1.25f, 0.65f, 1.2f, 13, 0.85f, 1.25f, 1.1f, 17, 1.3f, 0.7f, 1.25f, 1022, 0.95f, 1.08f, 0.95f, 28, 1, 1, 1);
        Kit.P(c, "blob", 0, 0, 0, 0, 9, 0, 0, -2, 13, 0, 5, 5, -17, 0, 0, 8, 28, 0, 0, 0);
        Kit.R(c, "blob", 0, 0, 0, 0, 9, -8, 0, 0, 13, 12, 0, 0, 28, 0, 0, 0);
        Kit.R(c, "cap", 0, 0, 0, 0, 13, -25, 0, 0, 18, 25, 0, 0, 28, 0, 0, 0);
        c = m.clip("sticky_shot", 32);
        Kit.S(c, "blob", 0, 1, 1, 1, 10, 1.1f, 1.15f, 1.1f, 14, 1.15f, 1.2f, 1.15f, -17, 0.9f, 0.85f, 1.2f, 1024, 1.04f, 0.97f, 1, 32, 1, 1, 1);
        Kit.R(c, "blob", 0, 0, 0, 0, 12, -12, 0, 0, -17, 10, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "jaw", 0, 0, 0, 0, 12, 30, 0, 0, 17, 40, 0, 0, 26, 0, 0, 0);
        c = m.clip("chomp", 34);
        Kit.S(c, "blob", 0, 1, 1, 1, 10, 1.25f, 0.7f, 1.25f, 15, 0.9f, 1.3f, 1.0f, 18, 1.15f, 0.8f, 1.2f, 24, 1.0f, 1.0f, 1.0f, 34, 1, 1, 1);
        Kit.R(c, "jaw", 0, 0, 0, 0, 10, 20, 0, 0, 15, 70, 0, 0, -19, 0, 0, 0, 24, 0, 0, 0);
        Kit.P(c, "blob", 0, 0, 0, 0, 10, 0, 0, -2, 15, 0, 6, 2, -19, 0, 0, 6, 34, 0, 0, 0);
        Kit.R(c, "blob", 0, 0, 0, 0, 15, -15, 0, 0, -19, 15, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "cap", 0, 0, 0, 0, 15, -20, 0, 0, 20, 20, 0, 0, 34, 0, 0, 0);
        Kit.S(c, "jaw", 0, 1, 1, 1, 15, 1, 3, 1, 19, 1, 1, 1);
        Kit.S(m.clip("hurt", 10), "blob", 0, 1, 1, 1, 3, 1.2f, 0.75f, 1.2f, 10, 1, 1, 1);
        c = m.clip("death", 26);
        Kit.S(c, "blob", 0, 1, 1, 1, 6, 0.9f, 1.2f, 0.9f, 18, 1.7f, 0.25f, 1.7f, 26, 1.8f, 0.12f, 1.8f);
        Kit.P(c, "lid", 0, 0, 0, 0, 6, 0, -2, 0, 26, 0, -2, 0);
        Kit.R(c, "cap", 0, 0, 0, 0, 18, 30, 0, 20, 26, 40, 0, 30);
        return m;
    }

    static void kinds() {
        if (ProjKind.of("s2_twig_goop") == null) ProjKind.orb("s2_twig_goop", 0xb98a4a, 3).gravity(0.025).trail(ParticleTypes.ITEM_SLIME);
    }
    static Move.Action goop(int n) {
        return (s, t) -> { Fx.shoot("s2_twig_goop", 0, 0.9f, 0, 0.5, 0.5, null).run(s, t); if (t != null) for (int i = 0; i < n; i++) Fx.curse(t, Fx.Curse.SLIMED); };
    }

    public static MobDef defS() {
        return MobDef.of("s2_twig_slime_s", "Twig Slime (S)", "ツイッグスライム (小)")
            .game(2, 1).size(0.7f, 0.55f).hp(9).speed(0.24f).style(Style.SLIME).egg(0xb98a4a, 0x5a3e24)
            .model(TwigSlime::modelS)
            .spawn(6, 1, 2, MobDef.Habitat.SWAMP)
            .pattern(Pattern.cycle("tackle"))
            .move(Move.of("tackle", 28).intent(Intent.ATTACK, 4, 1).reach(2.0f)
                .sound(10, SoundEvents.SLIME_JUMP_SMALL, 1f, 1.2f).at(11, Fx.dash(0.4, 0.15)).at(15, Fx.melee(4, 2.2, 90)));
    }

    public static MobDef defM() {
        kinds();
        return MobDef.of("s2_twig_slime_m", "Twig Slime (M)", "ツイッグスライム (中)")
            .game(2, 1).size(1.1f, 0.9f).hp(27).speed(0.22f).style(Style.SLIME).egg(0xb98a4a, 0x3a2616)
            .model(TwigSlime::model)
            .spawn(5, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(Pattern.first("sticky_shot", Pattern.random().add("chomp", 67).add("sticky_shot", 33).maxRepeat("sticky_shot", 1)))
            .move(Move.of("sticky_shot", 32).intent(Intent.DEBUFF).ranged(10)
                .sound(16, SoundEvents.SLIME_SQUISH, 1.2f, 1.0f).at(17, goop(1)))
            .move(Move.of("chomp", 34).intent(Intent.ATTACK, 11, 1).reach(2.4f)
                .sound(14, SoundEvents.SLIME_JUMP, 1.2f, 0.7f).at(15, Fx.dash(0.35, 0.1)).at(19, Fx.melee(11, 2.6, 90)));
    }
}
