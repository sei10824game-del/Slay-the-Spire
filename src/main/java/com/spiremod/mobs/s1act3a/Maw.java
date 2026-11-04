package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 Maw (300 HP). Turn 1 Roar (3 Weak + 3 Frail). Then: 50% Nom (5 x turns/2 hits, never twice), else Slam (25) after Nom/Drool, else Drool (+3 Strength).
 * A hulking pink worm-mouth: giant toothy jaws on stubby legs.
 */
public final class Maw {
    private Maw() {}
    static final int HIDE = 0x8a5a6a, HIDE_D = 0x4a2a38, HIDE_L = 0xc08a96, GUM = 0xd0506a, TONGUE = 0xe86a80, TOOTH = 0xefe6cc, EYE = 0xffe070;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("maw");
        m.scale(1.3f);
        for (int s = -1; s <= 1; s += 2) for (int f = 0; f < 2; f++) {
            Part leg = m.add("leg" + (s < 0 ? "R" : "L") + f).at(s * 9, 6, f == 0 ? 6 : -7);
            leg.box(-2.5f, -6, -2.5f, 5, 6, 5).color(HIDE_D).noise(0.12f).bevel(0.15f);
            leg.box(-3, -6, -3, 6, 2, 7).color(0x2a1620).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 6, 0);
        body.box(-11, 0, -13, 22, 14, 26).color(HIDE).grad(HIDE_L, HIDE_D).noise(0.15f).bevel(0.25f);      // lower body
        body.box(-9, 14, -11, 18, 4, 22).color(HIDE_L).noise(0.15f).bevel(0.25f);
        for (int i = 0; i < 4; i++) body.box(-1, 18, -9 + i * 5, 2, 3, 2).color(HIDE_D).bevel(0.15f);          // dorsal studs
        for (int s = -1; s <= 1; s += 2) {
            Part eye = body.add(s < 0 ? "eyeR" : "eyeL").at(s * 6, 16, 12);
            eye.box(-2.5f, -2, 0, 5, 4, 1).color(0x201018).flat().glow(Face.FRONT, 1, 1, 3, 2, EYE);
        }
        // upper jaw (fixed) over the maw, lower jaw hinged at the back
        body.box(-10, 8, 12, 20, 8, 4).color(HIDE).bevel(0.2f).noise(0.12f);                                   // upper lip
        for (int i = 0; i < 6; i++) body.box(-9 + i * 3.4f, 5, 15, 2, 3, 1).color(TOOTH).flat();                 // upper teeth
        body.box(-9, 5, 8, 18, 3, 7).color(GUM).noise(0.1f);                                                    // upper gum / palate
        Part jaw = body.add("jaw").at(0, 4, 4);
        jaw.box(-10, -4, -6, 20, 4, 18).color(HIDE_D).grad(HIDE, HIDE_D).noise(0.12f).bevel(0.15f);
        jaw.box(-9, 0, -4, 18, 1, 16).color(GUM);
        Part tongue = jaw.add("tongue").at(0, 1, -2);
        tongue.box(-5, 0, 0, 10, 2, 12).color(TONGUE).noise(0.1f).bevel(0.2f);
        for (int i = 0; i < 6; i++) jaw.box(-9 + i * 3.4f, 1, 11, 2, 3, 1).color(TOOTH).flat();                  // lower teeth
        m.walkQuad("legR0", "legL0", "legR1", "legL1", 28f);
        m.clip("idle", 50).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 25, 1.03f, 1.03f, 1.03f).scl("body", 50, 1, 1, 1)
            .rot("jaw", 0, 4, 0, 0).rot("jaw", 25, 10, 0, 0).rot("jaw", 50, 4, 0, 0)
            .pos("tongue", 0, 0, 0, 0).pos("tongue", 25, 0, 0, 2).pos("tongue", 50, 0, 0, 0);
        m.clip("roar", 46)
            .rot("body", 0, 0, 0, 0).rot("body", 14, -18, 0, 0).rot("body", 20, -22, 0, 0, Ease.OUT_BACK).rot("body", 36, -20, 0, 0).rot("body", 46, 0, 0, 0)
            .rot("jaw", 0, 4, 0, 0).rot("jaw", 14, 50, 0, 0).rot("jaw", 20, 62, 0, 0, Ease.OUT_BACK).rot("jaw", 36, 55, 0, 0).rot("jaw", 46, 4, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 20, 1.12f, 1.12f, 1.12f).scl("body", 46, 1, 1, 1)
            .rot("tongue", 0, 0, 0, 0).rot("tongue", 22, -15, 0, 0).rot("tongue", 46, 0, 0, 0)
            .rot("body", 24, -22, 3, 0).rot("body", 28, -22, -3, 0).rot("body", 32, -22, 3, 0);
        m.clip("drool", 34)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 8, 0, 0).rot("body", 24, 10, 0, 0).rot("body", 34, 0, 0, 0)
            .rot("jaw", 0, 4, 0, 0).rot("jaw", 10, 30, 0, 0).rot("jaw", 24, 36, 0, 0).rot("jaw", 34, 4, 0, 0)
            .rot("tongue", 0, 0, 0, 0).rot("tongue", 14, 12, 0, 0).rot("tongue", 20, -6, 0, 0).rot("tongue", 26, 12, 0, 0).rot("tongue", 34, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 14, 1.1f, 0.94f, 1.1f).scl("body", 34, 1, 1, 1);
        m.clip("slam", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 16, -26, 0, 0).rot("body", 21, 26, 0, 0, Ease.IN_CUBIC).rot("body", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, 5, -4).pos("body", 21, 0, -4, 8, Ease.IN_CUBIC).pos("body", 40, 0, 0, 0)
            .rot("jaw", 0, 4, 0, 0).rot("jaw", 16, 55, 0, 0).rot("jaw", 21, -4, 0, 0, Ease.IN_CUBIC).rot("jaw", 40, 4, 0, 0);
        m.clip("nom", 44)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 8, 0, 0).rot("body", 12, -6, 0, 0).rot("body", 44, 0, 0, 0)
            .rot("jaw", 0, 4, 0, 0).rot("jaw", 6, 48, 0, 0).rot("jaw", 9, -2, 0, 0, Ease.IN_CUBIC).rot("jaw", 14, 40, 0, 0).rot("jaw", 17, -2, 0, 0, Ease.IN_CUBIC)
            .rot("jaw", 22, 40, 0, 0).rot("jaw", 25, -2, 0, 0, Ease.IN_CUBIC).rot("jaw", 30, 40, 0, 0).rot("jaw", 33, -2, 0, 0, Ease.IN_CUBIC).rot("jaw", 44, 4, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, 5).pos("body", 17, 0, 0, 5).pos("body", 25, 0, 0, 5).pos("body", 33, 0, 0, 5).pos("body", 44, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -8, 0, 5).rot("body", 10, 0, 0, 0).rot("jaw", 0, 4, 0, 0).rot("jaw", 3, 30, 0, 0).rot("jaw", 10, 4, 0, 0);
        m.clip("death", 40).pos("body", 0, 0, 0, 0).pos("body", 40, 0, -5, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 40, 0, 0, 35).rot("jaw", 0, 4, 0, 0).rot("jaw", 30, 50, 0, 0)
            .rot("tongue", 0, 0, 0, 0).rot("tongue", 40, 20, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("maw", "Maw", "マウ")
            .game(1, 3).size(2.4f, 2.0f).hp(300).speed(0.2f).style(Style.BEAST).egg(0x8a5a6a, 0xd0506a)
            .model(Maw::model)
            .spawn(2, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(s -> {
                if (s.getTurn() == 0) return "roar";
                String last = s.lastMove();
                if (s.rand().nextInt(100) < 50 && !last.equals("nom")) return "nom";
                return last.equals("slam") ? "drool" : (last.equals("nom") || last.equals("drool") ? "slam" : "drool");
            })
            .move(Move.of("roar", 46).intent(Intent.STRONG_DEBUFF).ranged(14)
                .sound(14, SoundEvents.RAVAGER_ROAR, 1.6f, 0.6f)
                .at(20, (s, t) -> { for (var v : Fx.foes(s, s.getBoundingBox().inflate(14))) { Fx.weak(v, 6); Fx.frail(v, 6); } }))
            .move(Move.of("drool", 34).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.SLIME_SQUISH, 1.2f, 0.5f)
                .at(16, Fx.strength(3)))
            .move(Move.of("slam", 40).intent(Intent.ATTACK, 25, 1).reach(3.4f)
                .sound(16, SoundEvents.RAVAGER_ROAR, 1f, 1f).sound(21, SoundEvents.GENERIC_EXPLODE, 0.6f, 0.7f)
                .at(21, Fx.melee(25, 3.8, 120)))
            .move(Move.of("nom", 44).intent(Intent.ATTACK, 5, 2).reach(3.2f)
                .sound(9, SoundEvents.RAVAGER_ATTACK, 1f, 1.1f).sound(17, SoundEvents.RAVAGER_ATTACK, 1f, 1.1f).sound(25, SoundEvents.RAVAGER_ATTACK, 1f, 1.1f).sound(33, SoundEvents.RAVAGER_ATTACK, 1f, 1.1f)
                .at(9, (s, t) -> Fx.melee(5, 3.6, 120).run(s, t))
                .at(17, (s, t) -> Fx.melee(5, 3.6, 120).run(s, t))
                .at(25, (s, t) -> { if (s.getTurn() >= 3) Fx.melee(5, 3.6, 120).run(s, t); })
                .at(33, (s, t) -> { if (s.getTurn() >= 5) Fx.melee(5, 3.6, 120).run(s, t); }));
    }
}
