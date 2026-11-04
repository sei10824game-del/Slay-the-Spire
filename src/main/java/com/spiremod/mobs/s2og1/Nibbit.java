package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth: Nibbit. 42-46 HP. Teal newt-like beast with a green dorsal sail, translucent side fins and a toothy maw.
 * Cycle: Butt (12) -> Hesitant Slice (6 + 5 Block) -> Hiss (+2 Strength) -> ...
 */
public final class Nibbit {
    private Nibbit() {}

    static final int SKIN = 0x2b9c8a, SKIN_D = 0x1d6e63, BELLY = 0x58c2a6, CREST = 0x86b35a, CREST_D = 0x5c8a3c,
        FIN = 0x5fb0b8, STRIPE = 0x1a5c55, TOOTH = 0xeef0e0, MOUTH = 0x3a1a20, EYE = 0xf2e15a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_nibbit");
        Part body = m.add("body").at(0, 7, 0);
        body.box(-5, 0, -8, 10, 8, 16).color(SKIN).grad(0x33ad98, SKIN_D).bevel(0.12f)
            .paint(Face.LEFT, 2, 1, 2, 6, STRIPE).paint(Face.LEFT, 7, 1, 2, 6, STRIPE).paint(Face.LEFT, 12, 1, 2, 5, STRIPE)
            .paint(Face.RIGHT, 2, 1, 2, 6, STRIPE).paint(Face.RIGHT, 7, 1, 2, 6, STRIPE).paint(Face.RIGHT, 12, 1, 2, 5, STRIPE);
        body.box(-4, -1, -6, 8, 2, 12).color(BELLY).noise(0.05f);               // pale belly
        Part crest = body.add("crest").at(0, 8, 0);
        crest.box(-1, 0, -7, 2, 2, 14).color(CREST).grad(0xa6cf72, CREST_D);
        crest.box(-0.5f, 1.5f, -5, 1, 3, 10).color(CREST).flat().paint(Face.LEFT, 0, 0, 10, 1, 0xb8de86).paint(Face.RIGHT, 0, 0, 10, 1, 0xb8de86);
        crest.box(-0.5f, 4, -2, 1, 1, 5).color(0xa6cf72).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part fin = body.add(s < 0 ? "finR" : "finL").at(s * 5, 6, 3).rot(0, s * -30, s * 40);
            fin.box(s < 0 ? -1 : 0, -1, -9, 1, 4, 10).color(FIN).flat().noise(0.04f)
                .paint(s < 0 ? Face.RIGHT : Face.LEFT, 0, 0, 10, 1, 0x8fd2d8).paint(s < 0 ? Face.RIGHT : Face.LEFT, 3, 1, 1, 3, 0x3f8f98).paint(s < 0 ? Face.RIGHT : Face.LEFT, 6, 1, 1, 3, 0x3f8f98);
        }
        // head + jaw
        Part head = body.add("head").at(0, 4, 7);
        head.box(-4, -2, 0, 8, 6, 6).color(SKIN).grad(0x33ad98, SKIN_D).bevel(0.1f)
            .glow(Face.LEFT, 2, 1, 2, 1, EYE).glow(Face.RIGHT, 2, 1, 2, 1, EYE);
        head.box(-3, -2, 6, 6, 4, 4).color(SKIN).grad(0x33ad98, SKIN_D)             // snout
            .paint(Face.FRONT, 0, 3, 6, 1, TOOTH).px(Face.FRONT, 1, 3, MOUTH).px(Face.FRONT, 3, 3, MOUTH).px(Face.FRONT, 5, 3, MOUTH)
            .paint(Face.LEFT, 0, 3, 4, 1, TOOTH).paint(Face.RIGHT, 0, 3, 4, 1, TOOTH);
        head.box(-3.5f, -2.5f, 2, 7, 1, 8).color(MOUTH).flat();                      // dark palate
        head.box(-1, 4, 0, 2, 2, 4).color(CREST).flat();                             // head crest start
        Part jaw = head.add("jaw").at(0, -2, 1);
        jaw.box(-3.5f, -2, -1, 7, 2, 9).color(BELLY).grad(BELLY, SKIN_D)
            .paint(Face.TOP, 0, 0, 7, 9, MOUTH).paint(Face.TOP, 0, 0, 1, 9, TOOTH).paint(Face.TOP, 6, 0, 1, 9, TOOTH).paint(Face.TOP, 0, 8, 7, 1, TOOTH);
        // legs (pivot at hips), splayed reptile legs
        String[] legs = {"legFR", "legFL", "legBR", "legBL"};
        float[][] lp = {{-5, 5, 5}, {5, 5, 5}, {-5, 5, -5}, {5, 5, -5}};
        for (int i = 0; i < 4; i++) {
            float sx = Math.signum(lp[i][0]);
            Part leg = body.add(legs[i]).at(lp[i][0], 0, lp[i][2]).rot(0, 0, sx * -12);
            leg.box(-1.5f, -6, -1.5f, 3, 6, 3).color(SKIN_D).grad(SKIN, SKIN_D);
            leg.box(-2, -7, -1, 4, 1, 4).color(SKIN_D).flat().px(Face.FRONT, 0, 0, TOOTH).px(Face.FRONT, 2, 0, TOOTH);
        }
        Part tail = body.add("tail").at(0, 4, -8);
        tail.box(-3, -3, -8, 6, 6, 8).color(SKIN).grad(0x33ad98, SKIN_D).paint(Face.TOP, 0, 0, 6, 8, SKIN_D).paint(Face.LEFT, 3, 1, 2, 4, STRIPE).paint(Face.RIGHT, 3, 1, 2, 4, STRIPE);
        Part tail2 = tail.add("tail2").at(0, 0, -8);
        tail2.box(-2, -2, -7, 4, 4, 7).color(SKIN).grad(0x33ad98, SKIN_D);
        tail2.box(-0.5f, 2, -6, 1, 2, 6).color(CREST).flat();
        Part tail3 = tail2.add("tail3").at(0, 0, -7);
        tail3.box(-1, -1, -5, 2, 2, 5).color(SKIN_D);
        tail3.box(-0.5f, -3, -6, 1, 6, 4).color(FIN).flat().noise(0.04f);           // tail fin

        m.look("head", 35, 25);
        m.walkQuad("legFR", "legFL", "legBR", "legBL", 40);
        m.proc((r, c) -> {
            float w = (float) Math.sin(c.swing * 0.6662f) * c.swingAmt;
            r.rot("tail", 0, w * 18, 0); r.rot("tail2", 0, w * 22, 0); r.rot("body", 0, -w * 5, 0);
        });
        m.clip("walk", 20).loop().rot("head", 0, 0, 0, 0).rot("head", 10, 4, 0, 0).rot("head", 20, 0, 0, 0);
        m.clip("idle", 48).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 24, 1.03f, 1.04f, 1).scl("body", 48, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -4, 8, 0).rot("head", 34, -2, -8, 0).rot("head", 48, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 30, 0, 0, 0).rot("jaw", 34, 18, 0, 0).rot("jaw", 38, 0, 0, 0)
            .rot("tail", 0, 0, -10, 0).rot("tail", 24, 0, 10, 0).rot("tail", 48, 0, -10, 0)
            .rot("tail2", 0, 0, -12, 0).rot("tail2", 24, 0, 12, 0).rot("tail2", 48, 0, -12, 0)
            .rot("tail3", 0, 0, -15, 0).rot("tail3", 24, 0, 15, 0).rot("tail3", 48, 0, -15, 0)
            .rot("finR", 0, 0, 0, 0).rot("finR", 24, 0, 0, 10).rot("finR", 48, 0, 0, 0)
            .rot("finL", 0, 0, 0, 0).rot("finL", 24, 0, 0, -10).rot("finL", 48, 0, 0, 0);
        // BUTT: haunches back, head ducks, explosive headbutt lunge, bounce back
        m.clip("butt", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -1.5f, -4).pos("body", 14, 0, -0.5f, 9, Ease.IN_CUBIC).pos("body", 18, 0, 0, 7).pos("body", 30, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -8, 0, 0).rot("body", 14, 14, 0, 0, Ease.IN_CUBIC).rot("body", 18, 6, 0, 0, Ease.OUT_BACK).rot("body", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 25, 0, 0).rot("head", 14, 35, 0, 0).rot("head", 17, -10, 0, 0, Ease.OUT_BACK).rot("head", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 25, 0, 0).rot("jaw", 13, 0, 0, 0).rot("jaw", 30, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 10, -30, 0, 0).rot("legFR", 14, 40, 0, 0).rot("legFR", 30, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 10, -30, 0, 0).rot("legFL", 14, 40, 0, 0).rot("legFL", 30, 0, 0, 0)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 10, -25, 0, 0).rot("legBR", 14, 45, 0, 0).rot("legBR", 30, 0, 0, 0)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 10, -25, 0, 0).rot("legBL", 14, 45, 0, 0).rot("legBL", 30, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 10, 20, 0, 0).rot("tail", 14, -25, 0, 0).rot("tail", 30, 0, 0, 0)
            .rot("crest", 0, 0, 0, 0).rot("crest", 14, -8, 0, 0).rot("crest", 20, 4, 0, 0).rot("crest", 30, 0, 0, 0);
        // HESITANT SLICE: creeps in, flinches back, creeps again, quick sideways bite-slash, scuttles back guarded
        m.clip("hesitant_slice", 34)
            .pos("body", 0, 0, 0, 0).pos("body", 5, 0, 0, 3).pos("body", 9, 0, 0, -1).pos("body", 14, 0, 0, 4).pos("body", 18, 0, 0, 6, Ease.IN_CUBIC).pos("body", 26, 0, 0, -3).pos("body", 34, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 5, 4, 0, 0).rot("body", 9, -4, 0, 0).rot("body", 14, 2, 20, 0).rot("body", 18, 4, -25, 0, Ease.IN_CUBIC).rot("body", 26, -3, 0, 0).rot("body", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 5, 8, 0, 0).rot("head", 9, -12, 0, 0).rot("head", 14, 0, 30, 15).rot("head", 18, 5, -35, -15, Ease.IN_CUBIC).rot("head", 26, -5, 0, 0).rot("head", 34, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 35, 0, 0).rot("jaw", 18, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 34, 0, 0, 0)
            .rot("finR", 0, 0, 0, 0).rot("finR", 9, 0, 0, -25).rot("finR", 18, 0, 0, 10).rot("finR", 26, 0, 0, -20).rot("finR", 34, 0, 0, 0)
            .rot("finL", 0, 0, 0, 0).rot("finL", 9, 0, 0, 25).rot("finL", 18, 0, 0, -10).rot("finL", 26, 0, 0, 20).rot("finL", 34, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 14, 0, -25, 0).rot("tail", 18, 0, 30, 0).rot("tail", 34, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 5, -20, 0, 0).rot("legFR", 9, 15, 0, 0).rot("legFR", 14, -20, 0, 0).rot("legFR", 22, 20, 0, 0).rot("legFR", 34, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 5, 15, 0, 0).rot("legFL", 9, -15, 0, 0).rot("legFL", 14, 20, 0, 0).rot("legFL", 22, -20, 0, 0).rot("legFL", 34, 0, 0, 0);
        // HISS: rears up, sail and fins flare, jaw gapes wide, tremble
        m.clip("hiss", 38)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -22, 0, 0, Ease.OUT_BACK).rot("body", 28, -20, 0, 0).rot("body", 38, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 2, -2).pos("body", 28, 0, 2, -2).pos("body", 38, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -15, 0, 0).rot("head", 14, -10, 6, 4).rot("head", 18, -10, -6, -4).rot("head", 22, -10, 6, 4).rot("head", 26, -10, -6, -4).rot("head", 38, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 50, 0, 0, Ease.OUT_BACK).rot("jaw", 28, 45, 0, 0).rot("jaw", 38, 0, 0, 0)
            .scl("crest", 0, 1, 1, 1).scl("crest", 10, 1, 1.8f, 1.05f, Ease.OUT_BACK).scl("crest", 28, 1, 1.7f, 1.05f).scl("crest", 38, 1, 1, 1)
            .rot("finR", 0, 0, 0, 0).rot("finR", 10, 0, 25, -45, Ease.OUT_BACK).rot("finR", 28, 0, 25, -40).rot("finR", 38, 0, 0, 0)
            .rot("finL", 0, 0, 0, 0).rot("finL", 10, 0, -25, 45, Ease.OUT_BACK).rot("finL", 28, 0, -25, 40).rot("finL", 38, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 10, -30, 0, -10).rot("legFR", 28, -30, 0, -10).rot("legFR", 38, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 10, -30, 0, 10).rot("legFL", 28, -30, 0, 10).rot("legFL", 38, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 10, -15, 0, 0).rot("tail", 16, -15, 15, 0).rot("tail", 22, -15, -15, 0).rot("tail", 28, -15, 10, 0).rot("tail", 38, 0, 0, 0)
            .rot("tail3", 0, 0, 0, 0).rot("tail3", 12, 0, 30, 0).rot("tail3", 16, 0, -30, 0).rot("tail3", 20, 0, 30, 0).rot("tail3", 24, 0, -30, 0).rot("tail3", 38, 0, 0, 0);
        m.clip("hurt", 10)
            .rot("body", 0, 0, 0, 0).rot("body", 3, -6, 0, 6).rot("body", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 10, 0).rot("head", 10, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 30, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 32)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -15, 0, 0).rot("body", 22, 0, 0, 85, Ease.OUT_BOUNCE).rot("body", 32, 0, 0, 88)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 2, 0).pos("body", 22, -2, -1, 0, Ease.OUT_BOUNCE).pos("body", 32, -2, -1, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -35, 0, 0).rot("head", 32, 15, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 50, 0, 0).rot("jaw", 32, 30, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 22, -40, 0, -60).rot("legFR", 32, -40, 0, -60)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 22, -40, 0, -60).rot("legFL", 32, -40, 0, -60)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 22, 40, 0, -60).rot("legBR", 32, 40, 0, -60)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 22, 40, 0, -60).rot("legBL", 32, 40, 0, -60)
            .rot("tail", 0, 0, 0, 0).rot("tail", 22, 0, 25, 0).rot("tail", 32, 0, 20, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_nibbit", "Nibbit", "ニビット")
            .game(2, 1).size(0.9f, 0.9f).hp(44).speed(0.27f).style(Style.FROG).egg(0x2b9c8a, 0x86b35a)
            .model(Nibbit::model)
            .spawn(8, 1, 2, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("butt", "hesitant_slice", "hiss"))
            .move(Move.of("butt", 30).intent(Intent.ATTACK, 12, 1).reach(2.6f)
                .sound(9, SoundEvents.RAVAGER_STEP, 0.7f, 1.4f)
                .at(12, Fx.dash(0.55, 0.05)).at(14, Fx.melee(12, 2.8, 80)))
            .move(Move.of("hesitant_slice", 34).intent(Intent.ATTACK_DEFEND, 6, 1).reach(2.4f)
                .sound(17, SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.3f)
                .at(18, Fx.melee(6, 2.6, 100)).at(24, (s, t) -> Kit.block(s, 5)))
            .move(Move.of("hiss", 38).intent(Intent.BUFF).noTarget()
                .sound(8, SoundEvents.CAT_HISS, 1.4f, 0.6f)
                .at(12, (s, t) -> Kit.str(s, 2)));
    }
}
