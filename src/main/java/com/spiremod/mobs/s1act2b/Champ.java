package com.spiremod.mobs.s1act2b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 boss: The Champ. 420 HP. Hulking blue-steel knight with a gold crown, gold sword and red shield.
 * Phase 1: Taunt every 4th turn; otherwise Defensive Stance 15% (max 2), Gloat 15%, Face Slap 25%, Heavy Slash 45% (no repeats).
 * Below 50% HP: Anger (cleanse, +6 Str) then Execute (10x2) every third turn, random moves in between.
 */
public final class Champ {
    private Champ() {}

    static final int STEEL = 0x4f8aa0, STEEL_L = 0x8cc4d4, STEEL_D = 0x2c5566, STEEL_DD = 0x1b3540,
        GOLD = 0xf0b030, GOLD_D = 0xb07418, SHIELD = 0x6e2418, SHIELD_D = 0x461410, LEATHER = 0x3a2a20;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("champ");
        m.scale(1.9f);
        // ---------- legs (bent, armoured)
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part leg = m.add("leg" + n).at(s * 3.5f, 11, 0).rot(-12, 0, -s * 4);
            leg.box(-2.5f, -6, -2.5f, 5, 6, 5).color(STEEL).grad(STEEL_L, STEEL_D).bevel(0.2f);
            leg.box(-2.8f, -2, -2.8f, 5, 2, 5).inflate(0.3f).color(STEEL_D).flat();
            Part shin = leg.add("shin" + n).at(0, -6, 0).rot(16, 0, 0);
            shin.box(-2, -5, -2, 4, 5, 4).color(STEEL).grad(STEEL_L, STEEL_D).bevel(0.2f);
            shin.box(-2.4f, -1, -2.6f, 5, 2, 5).color(STEEL_L).bevel(0.3f); // knee cop
            shin.box(-2.5f, -6, -2, 5, 2, 7).color(STEEL_D).grad(STEEL, STEEL_DD).bevel(0.25f); // sabaton
        }
        // ---------- pelvis + tassets
        Part body = m.add("body").at(0, 11, 0);
        body.box(-5, -1, -3, 10, 4, 6).color(STEEL_D).grad(STEEL, STEEL_DD).bevel(0.15f);
        body.box(-5.5f, -3, -3.5f, 5, 3, 1).color(STEEL).bevel(0.3f);
        body.box(0.5f, -3, -3.5f, 5, 3, 1).color(STEEL).bevel(0.3f);
        body.box(-5.2f, 2, -3.2f, 10, 1, 6).inflate(0.2f).color(LEATHER).flat().px(Face.FRONT, 5, 0, GOLD);
        // ---------- torso (hunched forward, massive)
        Part torso = body.add("torso").at(0, 3, 0).rot(16, 0, 0);
        torso.box(-6, 0, -4, 12, 6, 8).color(STEEL).grad(STEEL, STEEL_D).bevel(0.15f)
            .band(1, 1, STEEL_D).band(3, 1, STEEL_D);
        torso.box(-7, 5, -4.5f, 14, 7, 9).color(STEEL).grad(STEEL_L, STEEL_D).bevel(0.12f)
            .paint(Face.FRONT, 6, 0, 2, 7, STEEL_D)
            .paint(Face.FRONT, 2, 1, 3, 2, STEEL_L).paint(Face.FRONT, 9, 1, 3, 2, STEEL_L);
        torso.box(-5, 11, -3.5f, 10, 2, 7).color(STEEL_D).bevel(0.2f);               // gorget
        torso.box(-6.5f, 6, -5, 6, 5, 1).color(STEEL_L).bevel(0.3f);                 // pec plates
        torso.box(0.5f, 6, -5, 6, 5, 1).color(STEEL_L).bevel(0.3f);
        torso.box(-1, 1, -4.6f, 2, 9, 1).color(STEEL_DD).flat();                     // centre ridge
        // ---------- head: small great-helm sunk between the shoulders, gold crown
        Part head = torso.add("head").at(0, 11, 1.5f).rot(-14, 0, 0);
        head.box(-3, 0, -3, 6, 7, 6).color(STEEL).grad(STEEL_L, STEEL_D).bevel(0.15f)
            .paint(Face.FRONT, 0, 2, 6, 1, STEEL_DD).paint(Face.FRONT, 0, 4, 6, 1, STEEL_DD)
            .paint(Face.FRONT, 2, 5, 2, 2, STEEL_DD);
        head.box(-3.3f, 2, -3.3f, 6, 1, 6).inflate(0.3f).color(STEEL_L).flat();       // brow rim
        head.box(-0.5f, 0, -3.6f, 1, 7, 1).color(STEEL_L).flat();                    // visor ridge
        Part eyes = head.add("eyes").at(0, 3.5f, -3.15f);
        eyes.box(-2.5f, 0, -0.1f, 5, 1, 1).flat().color(0x0c1418).glow(Face.FRONT, 0, 0, 2, 1, 0x7fd8ff).glow(Face.FRONT, 3, 0, 2, 1, 0x7fd8ff);
        Part rage = head.add("rage").at(0, 3.5f, -3.3f);
        rage.box(-2.5f, 0, -0.1f, 5, 1, 1).flat().inflate(0.15f).color(0x400000).glow(Face.FRONT, 0, 0, 2, 1, 0xff3020).glow(Face.FRONT, 3, 0, 2, 1, 0xff3020);
        Part crown = head.add("crown").at(0, 7, 0);
        crown.box(-3, 0, -3, 6, 1, 6).color(GOLD).grad(GOLD, GOLD_D).bevel(0.2f);
        for (int i = 0; i < 3; i++) {
            float cx = -2.5f + i * 2.25f;
            crown.box(cx, 1, -3, 1, i == 1 ? 2 : 1, 1).color(GOLD).flat();
            crown.box(cx, 1, 2, 1, 1, 1).color(GOLD).flat();
        }
        crown.box(-3, 1, -0.5f, 1, 1, 1).color(GOLD).flat();
        crown.box(2, 1, -0.5f, 1, 1, 1).color(GOLD).flat();
        crown.box(-0.5f, 0.2f, -3.3f, 1, 1, 1).color(0xd02030).flat().glow(Face.FRONT, 0, 0, 1, 1, 0xff4050);
        // ---------- arms: huge pauldrons, long reaching arms
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part arm = torso.add("arm" + n).at(s * 8.5f, 10, 0).rot(-10, 0, s * -8);
            arm.box(-2.5f, -7, -2.5f, 5, 7, 5).color(STEEL).grad(STEEL_L, STEEL_D).bevel(0.15f).band(4, 1, STEEL_D);
            arm.box(-4, -3, -4, 8, 5, 8).color(STEEL).grad(STEEL_L, STEEL_D).bevel(0.2f)          // pauldron
                .band(3, 1, STEEL_DD);
            arm.box(-4.5f, -4, -4.5f, 9, 1, 9).color(STEEL_D).flat();
            arm.box(s < 0 ? -4.5f : 2.5f, 0, -1.5f, 2, 3, 3).color(STEEL_L).bevel(0.3f);         // pauldron spike-ridge
            Part fore = arm.add("fore" + n).at(0, -7, 0).rot(-25, 0, 0);
            fore.box(-2, -6, -2, 4, 6, 4).color(STEEL).grad(STEEL, STEEL_D).bevel(0.15f);
            fore.box(-2.6f, -1.5f, -2.6f, 5, 2, 5).inflate(0.2f).color(STEEL_L).bevel(0.3f);   // couter
            Part fist = fore.add("fist" + n).at(0, -6, 0);
            fist.box(-2.5f, -3.5f, -2.5f, 5, 4, 5).color(STEEL_D).grad(STEEL, STEEL_DD).bevel(0.2f)
                .paint(Face.FRONT, 0, 1, 5, 1, STEEL_L);
        }
        // sword in the LEFT fist (blade along +y, part tipped forward)
        Part sword = m.byName.get("fistL").add("sword").at(0, -1.5f, 0).rot(80, 0, 0);
        sword.box(-0.5f, -3, -0.5f, 1, 4, 1).color(LEATHER).flat();
        sword.box(-1, -4, -1, 2, 1, 2).color(GOLD).flat();                       // pommel
        sword.box(-3, 1, -1, 6, 1, 2).color(GOLD).grad(GOLD, GOLD_D).bevel(0.2f); // crossguard
        sword.box(-1.5f, 2, -0.5f, 3, 14, 1).color(GOLD).grad(0xffd060, GOLD_D).bevel(0.15f)
            .paint(Face.FRONT, 1, 0, 1, 13, 0xffe48a).paint(Face.BACK, 1, 0, 1, 13, 0xffe48a);
        sword.box(-1, 16, -0.5f, 2, 2, 1).color(GOLD).flat();
        sword.box(-0.5f, 18, -0.5f, 1, 1, 1).color(0xffd060).flat();
        // kite shield on the RIGHT fist, facing forward
        Part shield = m.byName.get("fistR").add("shield").at(0, -1.5f, 3).rot(10, 0, 0);
        shield.box(-6, -9, 0, 12, 14, 1).color(SHIELD).grad(0x8a2e1e, SHIELD_D).bevel(0.25f).noise(0.1f)
            .paint(Face.FRONT, 5, 2, 2, 9, GOLD_D).paint(Face.FRONT, 2, 5, 8, 2, GOLD_D)
            .paint(Face.FRONT, 5, 5, 2, 2, GOLD);
        shield.box(-4, -12, 0, 8, 3, 1).color(SHIELD_D).grad(SHIELD, SHIELD_D).bevel(0.3f);
        shield.box(-2, -14, 0, 4, 2, 1).color(SHIELD_D).flat();
        shield.box(-6.3f, 4.5f, -0.2f, 12, 1, 1).inflate(0.25f).color(GOLD_D).flat();
        // ---------- procedural: phase 2 red eyes, breathing glow
        m.proc((r, c) -> {
            boolean angry = c.phase >= 1;
            r.show("rage", angry);
            r.show("eyes", !angry);
            if (angry) r.rot("torso", 0, (float) Math.sin(c.age * 1.7f) * 0.8f, 0); // seething tremor
        });
        m.look("head", 40, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 26f, 14f);
        m.proc((r, c) -> { float s = (float) Math.abs(Math.cos(c.swing * 0.6662f)) * c.swingAmt; r.pos("body", 0, -s * 1.2f, 0); r.rot("torso", 0, (float) Math.cos(c.swing * 0.6662f) * c.swingAmt * 8, 0); });

        // ---------- idle: heavy breathing, shoulders heave, sword tip sways
        m.clip("idle", 50).loop()
            .rot("torso", 0, 0, 0, 0).rot("torso", 25, -4, 0, 0).rot("torso", 50, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 25, 0, 0.5f, 0).pos("body", 50, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 25, -4, 0, -3).rot("armR", 50, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 25, -6, 0, 4).rot("armL", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 25, -5, 0, 0).rot("head", 50, 0, 0, 0)
            .rot("sword", 0, 0, 0, 0).rot("sword", 25, -6, 0, 3).rot("sword", 50, 0, 0, 0);

        // ---------- HEAVY SLASH: hoist the sword high over the head, lean back, cleave down into the ground
        m.clip("heavy_slash", 34)
            .rot("torso", 0, 0, 0, 0).rot("torso", 11, -26, 20, 0, Ease.OUT_CUBIC).rot("torso", 15, 30, -10, 0, Ease.IN_CUBIC).rot("torso", 22, 26, -8, 0).rot("torso", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 11, 0, 1, -1).pos("body", 15, 0, -2.5f, 3, Ease.IN_CUBIC).pos("body", 22, 0, -2.5f, 3).pos("body", 34, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 11, -190, 0, -10, Ease.OUT_CUBIC).rot("armL", 15, -40, 0, 0, Ease.IN_CUBIC).rot("armL", 22, -38, 0, 0).rot("armL", 34, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 11, -40, 0, 0).rot("foreL", 15, 20, 0, 0, Ease.IN_CUBIC).rot("foreL", 34, 0, 0, 0)
            .rot("sword", 0, 0, 0, 0).rot("sword", 11, 20, 0, 0).rot("sword", 15, 25, 0, 0).rot("sword", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 11, -30, 0, 25).rot("armR", 15, 20, 0, 10).rot("armR", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 11, -20, 0, 0).rot("head", 15, 15, 0, 0).rot("head", 34, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 11, 10, 0, 0).rot("legL", 15, -40, 0, 0, Ease.IN_CUBIC).rot("legL", 34, 0, 0, 0)
            .rot("shinL", 0, 0, 0, 0).rot("shinL", 15, 30, 0, 0).rot("shinL", 34, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 15, 25, 0, 0).rot("legR", 34, 0, 0, 0);

        // ---------- FACE SLAP: wind the shield arm across the chest, then a vicious backhand swat
        m.clip("face_slap", 28)
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, 0, -40, 0, Ease.OUT_CUBIC).rot("torso", 13, 8, 45, 0, Ease.IN_CUBIC).rot("torso", 19, 6, 40, 0).rot("torso", 28, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -80, 30, 70, Ease.OUT_CUBIC).rot("armR", 13, -85, -40, -60, Ease.IN_CUBIC).rot("armR", 19, -70, -45, -65).rot("armR", 28, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 9, -60, 0, 0).rot("foreR", 13, 10, 0, 0, Ease.IN_CUBIC).rot("foreR", 28, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 9, 0, 30, 0).rot("head", 13, 0, -15, 0).rot("head", 28, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, 20, 0, -15).rot("armL", 13, -20, 0, 20).rot("armL", 28, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 13, 0, -0.5f, 2, Ease.IN_CUBIC).pos("body", 28, 0, 0, 0);

        // ---------- DEFENSIVE STANCE: squat, plant the shield in front, sword braced behind it
        m.clip("defensive_stance", 34)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1, 0).pos("body", 13, 0, -3.5f, 0, Ease.IN_CUBIC).pos("body", 28, 0, -3, 0).pos("body", 34, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -8, 0, 0).rot("torso", 13, 14, 0, 0, Ease.IN_CUBIC).rot("torso", 28, 12, 0, 0).rot("torso", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -90, 20, 20).rot("armR", 13, -55, 30, -15, Ease.IN_CUBIC).rot("armR", 28, -55, 30, -15).rot("armR", 34, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 13, -20, 0, 0).rot("foreR", 34, 0, 0, 0)
            .rot("shield", 0, 0, 0, 0).rot("shield", 13, 0, -30, 0).rot("shield", 34, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 13, -40, -25, 10).rot("armL", 28, -40, -25, 10).rot("armL", 34, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 13, -30, 0, -10).rot("legR", 28, -30, 0, -10).rot("legR", 34, 0, 0, 0)
            .rot("shinR", 0, 0, 0, 0).rot("shinR", 13, 35, 0, 0).rot("shinR", 28, 35, 0, 0).rot("shinR", 34, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 13, 10, 0, 12).rot("legL", 28, 10, 0, 12).rot("legL", 34, 0, 0, 0)
            .rot("shinL", 0, 0, 0, 0).rot("shinL", 13, 30, 0, 0).rot("shinL", 28, 30, 0, 0).rot("shinL", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 13, 10, 0, 0).rot("head", 34, 0, 0, 0);

        // ---------- GLOAT: rear up, arms flung wide, head thrown back laughing (shakes)
        m.clip("gloat", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -24, 0, 0, Ease.OUT_BACK).rot("torso", 14, -20, 0, 0).rot("torso", 18, -26, 0, 0).rot("torso", 22, -20, 0, 0).rot("torso", 26, -26, 0, 0).rot("torso", 30, -22, 0, 0).rot("torso", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -40, 0, 70, Ease.OUT_BACK).rot("armR", 30, -45, 0, 75).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -110, 0, -60, Ease.OUT_BACK).rot("armL", 30, -120, 0, -65).rot("armL", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -30, 0, 0).rot("head", 14, -22, 6, 0).rot("head", 18, -32, -6, 0).rot("head", 22, -22, 6, 0).rot("head", 26, -32, -6, 0).rot("head", 40, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 1.5f, 0).pos("body", 30, 0, 1.5f, 0).pos("body", 40, 0, 0, 0);

        // ---------- TAUNT: point the sword at the foe, beckon with the shield arm, mocking head bobs
        m.clip("taunt", 40)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -95, -10, 0, Ease.OUT_BACK).rot("armL", 32, -95, -10, 0).rot("armL", 40, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 10, 25, 0, 0).rot("foreL", 32, 25, 0, 0).rot("foreL", 40, 0, 0, 0)
            .rot("sword", 0, 0, 0, 0).rot("sword", 10, -10, 0, 0).rot("sword", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -60, 0, 30).rot("armR", 32, -60, 0, 30).rot("armR", 40, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 12, -70, 0, 0).rot("foreR", 16, -20, 0, 0).rot("foreR", 20, -70, 0, 0).rot("foreR", 24, -20, 0, 0).rot("foreR", 28, -70, 0, 0).rot("foreR", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -10, 0, 15).rot("head", 18, 5, 0, -15).rot("head", 24, -10, 0, 15).rot("head", 30, 5, 0, -10).rot("head", 40, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -6, 20, 0).rot("torso", 32, -6, 20, 0).rot("torso", 40, 0, 0, 0);

        // ---------- ANGER: hunch, tremble, then a roar with arms flung out (enters phase 2)
        m.clip("anger", 46)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -2.5f, 0).pos("body", 20, 0, 2, 0, Ease.OUT_BACK).pos("body", 38, 0, 1.5f, 0).pos("body", 46, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, 35, 0, 0).rot("torso", 14, 33, 3, 0).rot("torso", 16, 35, -3, 0).rot("torso", 20, -30, 0, 0, Ease.OUT_BACK)
                .rot("torso", 24, -26, 4, 0).rot("torso", 28, -30, -4, 0).rot("torso", 32, -26, 4, 0).rot("torso", 38, -28, 0, 0).rot("torso", 46, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -20, 0, -20).rot("armR", 20, -30, 0, 95, Ease.OUT_BACK).rot("armR", 38, -35, 0, 90).rot("armR", 46, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -20, 0, 20).rot("armL", 20, -30, 0, -95, Ease.OUT_BACK).rot("armL", 38, -35, 0, -90).rot("armL", 46, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 12, -80, 0, 0).rot("foreR", 20, -40, 0, 0).rot("foreR", 46, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 12, -80, 0, 0).rot("foreL", 20, -40, 0, 0).rot("foreL", 46, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 25, 0, 0).rot("head", 20, -45, 0, 0, Ease.OUT_BACK).rot("head", 38, -40, 0, 0).rot("head", 46, 0, 0, 0)
            .scl("torso", 0, 1).scl("torso", 20, 1.08f).scl("torso", 38, 1.08f).scl("torso", 46, 1);

        // ---------- EXECUTE: two-hand-style double cleave: overhead chop, then a rising backswing chop
        m.clip("execute", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -28, 25, 0, Ease.OUT_CUBIC).rot("torso", 13, 32, -15, 0, Ease.IN_CUBIC)
                .rot("torso", 21, -10, -45, 0, Ease.OUT_CUBIC).rot("torso", 26, 25, 40, 0, Ease.IN_CUBIC).rot("torso", 34, 20, 30, 0).rot("torso", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, -195, 0, -10, Ease.OUT_CUBIC).rot("armL", 13, -55, 0, 0, Ease.IN_CUBIC)
                .rot("armL", 21, -120, 40, -70, Ease.OUT_CUBIC).rot("armL", 26, -80, -50, 30, Ease.IN_CUBIC).rot("armL", 34, -70, -45, 25).rot("armL", 44, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 9, -40, 0, 0).rot("foreL", 13, 15, 0, 0).rot("foreL", 21, -50, 0, 0).rot("foreL", 26, 0, 0, 0).rot("foreL", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -40, 0, 30).rot("armR", 13, 20, 0, 10).rot("armR", 21, -30, 0, 40).rot("armR", 26, 10, 0, 10).rot("armR", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 1.5f, -1).pos("body", 13, 0, -3, 3, Ease.IN_CUBIC).pos("body", 21, 0, -1, 3).pos("body", 26, 0, -3, 5, Ease.IN_CUBIC).pos("body", 34, 0, -2.5f, 5).pos("body", 44, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 13, -40, 0, 0, Ease.IN_CUBIC).rot("legL", 26, -45, 0, 0).rot("legL", 44, 0, 0, 0)
            .rot("shinL", 0, 0, 0, 0).rot("shinL", 13, 30, 0, 0).rot("shinL", 44, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 13, 25, 0, 0).rot("legR", 26, 30, 0, 0).rot("legR", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 9, -20, 0, 0).rot("head", 13, 15, 0, 0).rot("head", 26, 10, -20, 0).rot("head", 44, 0, 0, 0);

        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -12, 0, 5).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 0, 8).rot("head", 10, 0, 0, 0);
        // ---------- death: sword drops, falls to his knees, then topples forward
        m.clip("death", 50)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -5, 0, Ease.IN_QUAD).pos("body", 22, 0, -5, 0).pos("body", 40, 0, -8, 4, Ease.OUT_BOUNCE).pos("body", 50, 0, -8, 4)
            .rot("legR", 0, 0, 0, 0).rot("legR", 14, -80, 0, -10).rot("legR", 50, -85, 0, -10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 14, -80, 0, 10).rot("legL", 50, -85, 0, 10)
            .rot("shinR", 0, 0, 0, 0).rot("shinR", 14, 85, 0, 0).rot("shinR", 50, 90, 0, 0)
            .rot("shinL", 0, 0, 0, 0).rot("shinL", 14, 85, 0, 0).rot("shinL", 50, 90, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -15, 0, 0).rot("torso", 22, -15, 0, 0).rot("torso", 40, 70, 0, 0, Ease.IN_QUAD).rot("torso", 50, 68, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, 20, 0, -30).rot("armL", 40, -150, 0, -20).rot("armL", 50, -150, 0, -20)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, 20, 0, 30).rot("armR", 40, -150, 0, 20).rot("armR", 50, -150, 0, 20)
            .rot("sword", 0, 0, 0, 0).rot("sword", 8, 60, 0, 40).rot("sword", 50, 60, 0, 40)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -30, 0, 0).rot("head", 40, 20, 0, 20).rot("head", 50, 20, 0, 20);
        return m;
    }

    static final String[] RAND = {"defensive_stance", "gloat", "face_slap", "heavy_slash"};
    static final int[] W = {15, 15, 25, 45};

    static String roll(SpireMob s) {
        int r = s.rand().nextInt(100), acc = 0, i = 0;
        for (; i < 4; i++) { acc += W[i]; if (r < acc) break; }
        // same move twice / 3rd defensive stance -> next move in list
        for (int k = 0; k < 4; k++) {
            String id = RAND[(i + k) % 4];
            if (id.equals(s.lastMove())) continue;
            if (id.equals("defensive_stance") && s.count("forge") >= 2) continue;
            return id;
        }
        return "heavy_slash";
    }

    static String next(SpireMob s) {
        int turn = s.inc("turns");
        if (s.getPhase() == 0 && s.getHealth() < s.getMaxHealth() * 0.5f) return "anger";
        if (s.getPhase() >= 1) {
            if (s.lastMove().equals("anger")) return "execute";
            if (!s.lastMove().equals("execute") && !s.moveAgo(1).equals("execute")) return "execute";
            return roll(s);
        }
        if (turn % 4 == 0) return "taunt";
        return roll(s);
    }

    static void cleanse(SpireMob s) {
        s.getActiveEffects().stream().filter(e -> !e.getEffect().isBeneficial()).map(e -> e.getEffect()).toList().forEach(s::removeEffect);
    }

    public static MobDef def() {
        return MobDef.of("champ", "The Champ", "チャンプ")
            .game(1, 2).size(1.6f, 3.5f).hp(420).speed(0.24f).style(Style.METAL).egg(0x4f8aa0, 0xf0b030)
            .model(Champ::model).boss().kbResist(1f)
            .pattern(Champ::next)
            .move(Move.of("heavy_slash", 34).intent(Intent.ATTACK, 16, 1).reach(3.4f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 1.2f, 0.6f)
                .at(15, (s, t) -> { Fx.meleeNow(s, 16, 4.2, 90, null); Fx.particlesAt(s, ParticleTypes.CRIT, Fx.rel(s, 2.6, -0.4, 0.2), 18, 0.5, 0.2); Fx.sound(s, SoundEvents.ANVIL_LAND, 0.8f, 0.6f); }))
            .move(Move.of("face_slap", 28).intent(Intent.ATTACK_DEBUFF, 12, 1).reach(3f)
                .at(13, Fx.melee(12, 3.8, 120, v -> { Fx.frail(v, 8); Fx.vulnerable(v, 8); }))
                .sound(13, SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1.4f, 0.8f))
            .move(Move.of("defensive_stance", 34).intent(Intent.DEFEND_BUFF).noTarget()
                .at(13, (s, t) -> { s.inc("forge"); s.addBlock(15); s.inc("metallicize"); Fx.sound(s, SoundEvents.ANVIL_PLACE, 1f, 0.7f); Fx.particles(s, ParticleTypes.ENCHANTED_HIT, 20, 1.0); }))
            .move(Move.of("gloat", 40).intent(Intent.BUFF).noTarget()
                .sound(10, SoundEvents.RAVAGER_ROAR, 0.9f, 1.3f)
                .at(12, Fx.strength(2)))
            .move(Move.of("taunt", 40).intent(Intent.UNKNOWN).ranged(10)
                .sound(12, SoundEvents.VINDICATOR_CELEBRATE, 1.4f, 0.6f)
                .at(16, (s, t) -> { if (t != null) { Fx.weak(t, 8); Fx.vulnerable(t, 8); } }))
            .move(Move.of("anger", 46).intent(Intent.BUFF).noTarget()
                .sound(18, SoundEvents.RAVAGER_ROAR, 1.6f, 0.7f)
                .at(20, (s, t) -> { s.setPhase(1); cleanse(s); s.addStrength(6); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 20, 1.2); Fx.ring(s, ParticleTypes.FLAME, s.position().add(0, 0.2, 0), 2.5, 40); }))
            .move(Move.of("execute", 44).intent(Intent.ATTACK, 10, 2).reach(3.4f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 1.2f, 0.5f).sound(22, SoundEvents.PLAYER_ATTACK_SWEEP, 1.2f, 0.7f)
                .at(13, Fx.melee(10, 4.2, 90)).at(26, Fx.melee(10, 4.2, 130)))
            // Metallicize: block at the end of each of its turns
            .onMoveEnd(s -> { int m = s.count("metallicize"); if (m > 0) s.addBlock(5 * m); });
    }
}
