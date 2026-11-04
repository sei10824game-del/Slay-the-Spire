package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 4 elite Spire Spear: 160 HP, Artifact 1, Back Attack.
 * Burn Strike 5x2 (+2 Burns), Piercer (ALL enemies gain 2 Strength), Skewer 10x3.
 * AI: first move Burn Strike; turns 1/2 = Piercer and Burn Strike in the other order, turn 3 = Skewer; repeat.
 * Look: sibling of the Spire Shield, the same indigo cloth heap, bearing a monstrous bone spear on violet tendrils.
 */
public final class SpireSpear {
    private SpireSpear() {}

    static final int BONE = SpireShield.BONE, BONE_L = SpireShield.BONE_L, BONE_D = SpireShield.BONE_D, VEIN = SpireShield.VEIN, VEIN_D = SpireShield.VEIN_D;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("spire_spear");
        Part neck = SpireShield.robe(m, "robe", 15, 12);
        for (int s = -1; s <= 1; s += 2) {
            Part t = neck.add(s < 0 ? "tenR" : "tenL").at(s * 2, 4, 1).rot(-60, s * 20, 0);
            t.cbox(0, 2, 6, 2).color(VEIN).grad(VEIN, VEIN_D).bevel(0.2f);
            Part t2 = t.add(s < 0 ? "tenR2" : "tenL2").at(0, 6, 0).rot(-25, 0, s * -10);
            t2.cbox(0, 2, 5, 2).color(VEIN).bevel(0.2f);
        }
        // the spear: pivot at the grip, shaft points forward (+z) and slightly up
        Part sp = neck.add("spear").at(0, 5, 8).rot(40, 0, 0);
        sp.box(-1.5f, -14, -1.5f, 3, 40, 3).color(BONE).grad(BONE_L, BONE_D).bevel(0.25f).noise(0.08f);
        sp.box(-2.2f, -6, -2.2f, 4, 3, 4).color(VEIN).flat();                           // grip wrap
        sp.box(-2.2f, 4, -2.2f, 4, 2, 4).color(VEIN).flat();
        sp.box(-2.5f, 26, -2.5f, 5, 3, 5).color(BONE_D).bevel(0.2f);                    // collar
        sp.box(-3, 29, -1, 6, 8, 2).color(BONE_L).grad(BONE_L, BONE).bevel(0.2f);      // blade
        sp.box(-2, 37, -1, 4, 4, 2).color(BONE_L).bevel(0.15f);
        sp.box(-1, 41, -0.5f, 2, 4, 1).color(BONE_L).flat();                            // tip
        sp.box(-5, 24, -0.5f, 2, 5, 1).color(BONE).bevel(0.2f);                         // barbs
        sp.box(3, 24, -0.5f, 2, 5, 1).color(BONE).bevel(0.2f);
        sp.box(-1.5f, -17, -1.5f, 3, 3, 3).color(BONE_D).bevel(0.2f);                   // butt cap
        m.proc((rig, c) -> {
            float b = (float) Math.sin(c.age * 0.07f);
            rig.rot("robe1", b * 1.5f, 0, 0); rig.rot("robe2", b * 2f, 0, 0);
            rig.rot("tenR", (float) Math.sin(c.age * 0.11f) * 5, 0, 0); rig.rot("tenL", (float) Math.sin(c.age * 0.11f + 1.5f) * 5, 0, 0);
            if (c.swingAmt > 0.05f) {
                float w = (float) Math.sin(c.swing * 0.6f) * c.swingAmt;
                rig.rot("robe0", 0, 0, w * 5); rig.rot("robe2", 0, 0, -w * 6); rig.scl("robe0", 1 + w * 0.05f, 1 - Math.abs(w) * 0.05f, 1);
            }
        });
        m.clip("idle", 60).loop()
            .rot("spear", 0, 0, 0, 0).rot("spear", 30, 4, 0, 1.5f).rot("spear", 60, 0, 0, 0)
            .rot("robe3", 0, 0, 0, 0).rot("robe3", 30, 4, 0, 0).rot("robe3", 60, 0, 0, 0);
        m.clip("walk", 24).loop()
            .pos("robe0", 0, 0, 0, 0).pos("robe0", 12, 0, 0.8f, 0).pos("robe0", 24, 0, 0, 0)
            .rot("spear", 0, -2, 0, 0).rot("spear", 12, 2, 0, 0).rot("spear", 24, -2, 0, 0);
        // BURN STRIKE: two quick lunging stabs
        m.clip("burn_strike", 30)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 6, -14, 0, 0).rot("robe2", 10, 6, 0, 0, Ease.IN_CUBIC).rot("robe2", 14, -6, 0, 0).rot("robe2", 18, 6, 0, 0, Ease.IN_CUBIC).rot("robe2", 30, 0, 0, 0)
            .rot("spear", 0, 0, 0, 0).rot("spear", 6, -25, 8, 0).rot("spear", 10, -6, 0, 0, Ease.IN_CUBIC).rot("spear", 14, -22, -8, 0).rot("spear", 18, -6, 0, 0, Ease.IN_CUBIC).rot("spear", 30, 0, 0, 0)
            .pos("spear", 0, 0, 0, 0).pos("spear", 6, 0, 1, -6).pos("spear", 10, 0, 0, 8, Ease.IN_CUBIC).pos("spear", 14, 0, 1, -5).pos("spear", 18, 0, 0, 8, Ease.IN_CUBIC).pos("spear", 30, 0, 0, 0)
            .pos("robe0", 0, 0, 0, 0).pos("robe0", 10, 0, 0, 3).pos("robe0", 18, 0, 0, 3).pos("robe0", 30, 0, 0, 0);
        // PIERCER: spear thrust straight up, then jammed into the ground
        m.clip("piercer", 40)
            .rot("spear", 0, 0, 0, 0).rot("spear", 14, -110, 0, 0, Ease.OUT_CUBIC).rot("spear", 20, -112, 0, 3).rot("spear", 24, 25, 0, 0, Ease.IN_CUBIC).rot("spear", 40, 0, 0, 0)
            .pos("spear", 0, 0, 0, 0).pos("spear", 14, 0, 6, -3).pos("spear", 24, 0, -6, 6, Ease.IN_CUBIC).pos("spear", 40, 0, 0, 0)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 14, -22, 0, 0).rot("robe2", 24, 8, 0, 0, Ease.IN_CUBIC).rot("robe2", 40, 0, 0, 0)
            .scl("robe0", 0, 1, 1, 1).scl("robe0", 14, 0.93f, 1.1f, 0.93f).scl("robe0", 24, 1.12f, 0.85f, 1.12f, Ease.IN_CUBIC).scl("robe0", 40, 1, 1, 1);
        // SKEWER: three fast piercing lunges, last one deepest
        m.clip("skewer", 46)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 8, -16, 0, 0).rot("robe2", 12, 6, 0, 0, Ease.IN_CUBIC).rot("robe2", 17, -10, 0, 0).rot("robe2", 21, 6, 0, 0, Ease.IN_CUBIC).rot("robe2", 26, -14, 0, 0).rot("robe2", 30, 10, 0, 0, Ease.IN_CUBIC).rot("robe2", 46, 0, 0, 0)
            .rot("spear", 0, 0, 0, 0).rot("spear", 8, -35, 0, 0).rot("spear", 12, -6, 0, 0, Ease.IN_CUBIC).rot("spear", 17, -30, 0, 0).rot("spear", 21, -6, 0, 0, Ease.IN_CUBIC).rot("spear", 26, -40, 0, 0).rot("spear", 30, -4, 0, 0, Ease.IN_CUBIC).rot("spear", 46, 0, 0, 0)
            .pos("spear", 0, 0, 0, 0).pos("spear", 8, 0, 0, -6).pos("spear", 12, 0, 0, 9, Ease.IN_CUBIC).pos("spear", 17, 0, 0, -5).pos("spear", 21, 0, 0, 9, Ease.IN_CUBIC).pos("spear", 26, 0, 0, -8).pos("spear", 30, 0, 0, 12, Ease.IN_CUBIC).pos("spear", 46, 0, 0, 0)
            .pos("robe0", 0, 0, 0, 0).pos("robe0", 12, 0, 0, 3).pos("robe0", 21, 0, 0, 3).pos("robe0", 30, 0, 0, 5).pos("robe0", 46, 0, 0, 0);
        m.clip("hurt", 10)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 3, -10, 0, 5).rot("robe2", 10, 0, 0, 0)
            .rot("spear", 0, 0, 0, 0).rot("spear", 3, -8, 6, 0).rot("spear", 10, 0, 0, 0);
        m.clip("death", 40)
            .rot("spear", 0, 0, 0, 0).rot("spear", 12, -20, 10, 10).rot("spear", 28, 80, 10, 20, Ease.OUT_BOUNCE).rot("spear", 40, 82, 10, 20)
            .pos("spear", 0, 0, 0, 0).pos("spear", 28, 0, -14, 6, Ease.OUT_BOUNCE)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 20, 40, 0, 0).rot("robe2", 40, 50, 0, 10)
            .rot("robe3", 0, 0, 0, 0).rot("robe3", 20, 40, 0, 0)
            .scl("robe0", 0, 1, 1, 1).scl("robe0", 30, 1.2f, 0.5f, 1.2f).scl("robe0", 40, 1.25f, 0.35f, 1.25f)
            .pos("robe0", 0, 0, 0, 0).pos("robe0", 40, 0, -1, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("spire_spear", "Spire Spear", "スパイアスピア")
            .game(1, 4).size(1.4f, 2.6f).hp(160).speed(0.2f).kbResist(0.9f).style(Style.METAL).egg(0x41539a, 0x9a48c0)
            .model(SpireSpear::model).elite()
            .spawn(1, 1, 1, MobDef.Habitat.MOUNTAIN)
            .onSpawn(s -> s.setCount("artifact", 1))
            .onTick(S3::artifact)
            .pattern(s -> {
                int t = s.getTurn() % 3;
                if (s.getTurn() == 0) return "burn_strike";
                if (t == 2) return "skewer";
                return s.lastMove().equals("burn_strike") ? "piercer" : "burn_strike";
            })
            .move(Move.of("burn_strike", 30).intent(Intent.ATTACK_DEBUFF, 5, 2).reach(3.0f)
                .sound(6, SoundEvents.BLAZE_SHOOT, 0.8f, 0.7f)
                .at(10, S3.backMelee(5, 3.6, 70, v -> Fx.curse(v, Fx.Curse.BURN)))
                .at(18, S3.backMelee(5, 3.6, 70, v -> Fx.curse(v, Fx.Curse.BURN))))
            .move(Move.of("piercer", 40).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.TRIDENT_RIPTIDE_1, 1f, 0.6f)
                .at(24, (s, t) -> {
                    for (SpireMob a : S3.allies(s, 16)) { a.addStrength(2); Fx.particlesAt(s, ParticleTypes.ANGRY_VILLAGER, a.position().add(0, a.getBbHeight() * 0.7, 0), 8, 0.4, 0.05); }
                    Fx.sound(s, SoundEvents.ANVIL_LAND, 0.9f, 0.5f);
                    Fx.ring(s, ParticleTypes.CRIT, s.position().add(0, 0.2, 0), 2.0, 24);
                }))
            .move(Move.of("skewer", 46).intent(Intent.ATTACK, 10, 3).reach(3.4f)
                .sound(8, SoundEvents.TRIDENT_THROW, 1f, 0.6f)
                .at(12, S3.backMelee(10, 3.8, 70, null)).at(21, S3.backMelee(10, 3.8, 70, null)).at(30, S3.backMelee(10, 3.8, 70, null)));
    }
}
