package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 4 elite Spire Shield: 110 HP, Artifact 1, Back Attack.
 * Bash 12 (+ lose 1 Strength), Fortify (ALL enemies gain 30 Block), Smite 34.
 * AI: turn 1/2 = Bash and Fortify in random order, turn 3 = Smite; repeat.
 * Look: a hunched heap of indigo cloth veined in violet, a giant ornate bone shield on violet tendrils.
 */
public final class SpireShield {
    private SpireShield() {}

    static final int ROBE = 0x41539a, ROBE_L = 0x5a6dba, ROBE_D = 0x26305f, VEIN = 0x9a48c0, VEIN_D = 0x5f2a80,
        BONE = 0xe0cf8f, BONE_L = 0xf3e6b0, BONE_D = 0xa08a50, HOLE = 0x4a3a1c;

    /** draped cloth heap; returns the top (neck) part */
    static Part robe(ModelSpec m, String pre, int w0, float lean) {
        Part r0 = m.add(pre + "0").at(0, 0, -1);
        r0.cbox(0, w0 + 4, 5, w0 + 2).color(ROBE).grad(ROBE, ROBE_D).bevel(0.3f).noise(0.1f)
            .paint(Face.FRONT, 3, 0, 1, 5, VEIN_D).paint(Face.FRONT, w0 - 2, 1, 1, 4, VEIN_D).paint(Face.LEFT, 4, 0, 1, 5, VEIN_D);
        r0.box(-(w0 + 6) / 2f, 0, -3, 3, 2, 6).color(ROBE_D).bevel(0.3f);   // folds spilling on the floor
        r0.box((w0 + 2) / 2f, 0, 1, 3, 2, 5).color(ROBE_D).bevel(0.3f);
        r0.box(-3, 0, (w0 + 2) / 2f, 6, 1, 3).color(ROBE_D).bevel(0.3f);
        Part r1 = r0.add(pre + "1").at(0, 5, 0).rot(lean * 0.5f, 0, 0);
        r1.cbox(0, w0, 7, w0 - 2).color(ROBE).grad(ROBE_L, ROBE).bevel(0.25f).noise(0.1f)
            .paint(Face.FRONT, 2, 0, 1, 7, VEIN).paint(Face.FRONT, 3, 3, 2, 1, VEIN).paint(Face.RIGHT, 3, 0, 1, 6, VEIN).paint(Face.BACK, 5, 1, 1, 6, VEIN_D);
        Part r2 = r1.add(pre + "2").at(0, 7, 0).rot(lean, 0, 0);
        r2.cbox(0, w0 - 4, 7, w0 - 6).color(ROBE).grad(ROBE_L, ROBE).bevel(0.25f).noise(0.1f)
            .paint(Face.FRONT, 1, 0, 1, 7, VEIN).paint(Face.LEFT, 2, 2, 1, 5, VEIN);
        Part r3 = r2.add(pre + "3").at(0, 7, 0).rot(lean, 0, 0);
        r3.cbox(0, w0 - 8, 6, w0 - 9).color(ROBE_L).grad(ROBE_L, ROBE).bevel(0.25f);
        r3.cbox(5, w0 - 10, 2, w0 - 11).color(ROBE_D).bevel(0.3f); // hood rim
        return r3;
    }

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("spire_shield");
        Part neck = robe(m, "robe", 16, 14);
        // violet tendrils reaching from the hood to the back of the shield
        for (int s = -1; s <= 1; s += 2) {
            Part t = neck.add(s < 0 ? "tenR" : "tenL").at(s * 2, 4, 1).rot(-55, s * 15, 0);
            t.cbox(0, 2, 6, 2).color(VEIN).grad(VEIN, VEIN_D).bevel(0.2f);
            Part t2 = t.add(s < 0 ? "tenR2" : "tenL2").at(0, 6, 0).rot(-20, 0, s * -10);
            t2.cbox(0, 2, 5, 2).color(VEIN).bevel(0.2f);
        }
        // ---- the shield (pivot = grip, in front of the hood)
        Part sh = neck.add("shield").at(0, 6, 9).rot(-32, 0, 0);
        sh.box(-8, -12, 0, 16, 26, 3).color(BONE).grad(BONE_L, BONE_D).bevel(0.3f).noise(0.1f)
            .paint(Face.FRONT, 7, 0, 2, 26, BONE_L)                                     // central spine
            .paint(Face.FRONT, 2, 5, 4, 2, HOLE).paint(Face.FRONT, 10, 5, 4, 2, HOLE)    // eye-like holes
            .paint(Face.FRONT, 3, 11, 3, 2, HOLE).paint(Face.FRONT, 10, 11, 3, 2, HOLE)
            .paint(Face.FRONT, 4, 17, 2, 2, HOLE).paint(Face.FRONT, 10, 17, 2, 2, HOLE)
            .paint(Face.FRONT, 6, 22, 4, 1, HOLE);
        sh.box(-6, -15, 0.5f, 12, 3, 2).color(BONE_D).bevel(0.3f);                      // tapering lower point
        sh.box(-3, -18, 0.5f, 6, 3, 2).color(BONE_D).bevel(0.3f);
        sh.box(-10, 6, 0.5f, 2, 7, 2).color(BONE).bevel(0.25f);                          // flared upper "ears"
        sh.box(8, 6, 0.5f, 2, 7, 2).color(BONE).bevel(0.25f);
        sh.box(-7, 14, 0.5f, 3, 3, 2).color(BONE_L).bevel(0.25f);                        // crown points
        sh.box(4, 14, 0.5f, 3, 3, 2).color(BONE_L).bevel(0.25f);
        sh.box(-1.5f, 14, 0.5f, 3, 5, 2).color(BONE_L).bevel(0.25f);
        for (int i = 0; i < 4; i++)                                                      // horizontal ribs
            sh.box(-6 + (i % 2), -8 + i * 5.5f, 3, 12 - 2 * (i % 2), 2, 2).color(BONE_L).grad(BONE_L, BONE_D).bevel(0.3f);
        sh.box(-1, -10, 3, 2, 22, 2).color(BONE_L).bevel(0.2f);                          // raised spine
        sh.box(-4, -4, -2, 8, 6, 2).color(BONE_D).bevel(0.3f);                           // grip block on the back
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
            .rot("shield", 0, 0, 0, 0).rot("shield", 30, 3, 0, 1.5f).rot("shield", 60, 0, 0, 0)
            .rot("robe3", 0, 0, 0, 0).rot("robe3", 30, 4, 0, 0).rot("robe3", 60, 0, 0, 0);
        m.clip("walk", 24).loop()
            .pos("robe0", 0, 0, 0, 0).pos("robe0", 12, 0, 0.8f, 0).pos("robe0", 24, 0, 0, 0)
            .rot("shield", 0, -2, 0, 0).rot("shield", 12, 2, 0, 0).rot("shield", 24, -2, 0, 0);

        // BASH: draw the shield back, then ram it forward with the whole body
        m.clip("bash", 28)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 9, -18, 0, 0).rot("robe2", 13, 22, 0, 0, Ease.IN_CUBIC).rot("robe2", 28, 0, 0, 0)
            .rot("robe1", 0, 0, 0, 0).rot("robe1", 9, -8, 0, 0).rot("robe1", 13, 12, 0, 0, Ease.IN_CUBIC).rot("robe1", 28, 0, 0, 0)
            .pos("shield", 0, 0, 0, 0).pos("shield", 9, 0, 1, -5).pos("shield", 13, 0, -1, 7, Ease.IN_CUBIC).pos("shield", 18, 0, -1, 6).pos("shield", 28, 0, 0, 0)
            .rot("shield", 0, 0, 0, 0).rot("shield", 9, -12, 15, 0).rot("shield", 13, 22, -5, 0, Ease.IN_CUBIC).rot("shield", 28, 0, 0, 0)
            .pos("robe0", 0, 0, 0, 0).pos("robe0", 9, 0, 0, -2).pos("robe0", 13, 0, 0, 4, Ease.IN_CUBIC).pos("robe0", 28, 0, 0, 0);
        // FORTIFY: raise the shield high, then plant it before the body like a wall (rest pose glows)
        m.clip("fortify", 34)
            .rot("shield", 0, 0, 0, 0).rot("shield", 10, -40, 0, 0, Ease.OUT_BACK).rot("shield", 18, 30, 0, 0, Ease.IN_CUBIC).rot("shield", 26, 28, 0, 0).rot("shield", 34, 0, 0, 0)
            .pos("shield", 0, 0, 0, 0).pos("shield", 10, 0, 6, -2).pos("shield", 18, 0, -6, 2, Ease.IN_CUBIC).pos("shield", 26, 0, -6, 2).pos("shield", 34, 0, 0, 0)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 10, -14, 0, 0).rot("robe2", 18, 10, 0, 0, Ease.IN_CUBIC).rot("robe2", 34, 0, 0, 0)
            .scl("robe0", 0, 1, 1, 1).scl("robe0", 18, 1, 1, 1).scl("robe0", 20, 1.1f, 0.85f, 1.1f).scl("robe0", 34, 1, 1, 1)
            .rot("tenL", 0, 0, 0, 0).rot("tenL", 10, -30, 0, 0).rot("tenL", 34, 0, 0, 0).rot("tenR", 0, 0, 0, 0).rot("tenR", 10, -30, 0, 0).rot("tenR", 34, 0, 0, 0);
        // SMITE: heave the shield far above and behind, hang, then crash it down edge-first
        m.clip("smite", 40)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 16, -30, 0, 0, Ease.OUT_CUBIC).rot("robe2", 21, -32, 0, 0).rot("robe2", 24, 30, 0, 0, Ease.IN_CUBIC).rot("robe2", 30, 26, 0, 0).rot("robe2", 40, 0, 0, 0)
            .rot("robe3", 0, 0, 0, 0).rot("robe3", 16, -25, 0, 0).rot("robe3", 24, 20, 0, 0, Ease.IN_CUBIC).rot("robe3", 40, 0, 0, 0)
            .rot("robe1", 0, 0, 0, 0).rot("robe1", 16, -10, 0, 0).rot("robe1", 24, 14, 0, 0, Ease.IN_CUBIC).rot("robe1", 40, 0, 0, 0)
            .rot("shield", 0, 0, 0, 0).rot("shield", 16, -95, 0, 0, Ease.OUT_CUBIC).rot("shield", 21, -100, 0, 4).rot("shield", 24, 55, 0, 0, Ease.IN_CUBIC).rot("shield", 27, 48, 0, 0, Ease.OUT_BOUNCE).rot("shield", 40, 0, 0, 0)
            .pos("shield", 0, 0, 0, 0).pos("shield", 16, 0, 8, -4).pos("shield", 24, 0, -2, 6, Ease.IN_CUBIC).pos("shield", 40, 0, 0, 0)
            .scl("robe0", 0, 1, 1, 1).scl("robe0", 16, 0.92f, 1.12f, 0.92f).scl("robe0", 24, 1.15f, 0.82f, 1.15f, Ease.IN_CUBIC).scl("robe0", 40, 1, 1, 1);
        m.clip("hurt", 10)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 3, -10, 0, 5).rot("robe2", 10, 0, 0, 0)
            .rot("shield", 0, 0, 0, 0).rot("shield", 3, -8, 6, 0).rot("shield", 10, 0, 0, 0);
        m.clip("death", 40)
            .rot("shield", 0, 0, 0, 0).rot("shield", 12, -20, 10, 10).rot("shield", 28, 70, 10, 20, Ease.OUT_BOUNCE).rot("shield", 40, 72, 10, 20)
            .pos("shield", 0, 0, 0, 0).pos("shield", 28, 0, -14, 6, Ease.OUT_BOUNCE)
            .rot("robe2", 0, 0, 0, 0).rot("robe2", 20, 40, 0, 0).rot("robe2", 40, 50, 0, 10)
            .rot("robe3", 0, 0, 0, 0).rot("robe3", 20, 40, 0, 0)
            .scl("robe0", 0, 1, 1, 1).scl("robe0", 30, 1.2f, 0.5f, 1.2f).scl("robe0", 40, 1.25f, 0.35f, 1.25f)
            .pos("robe0", 0, 0, 0, 0).pos("robe0", 40, 0, -1, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("spire_shield", "Spire Shield", "スパイアシールド")
            .game(1, 4).size(1.4f, 2.6f).hp(110).speed(0.2f).kbResist(0.9f).style(Style.METAL).egg(0x41539a, 0xe0cf8f)
            .model(SpireShield::model).elite()
            .spawn(1, 1, 1, MobDef.Habitat.MOUNTAIN)
            .onSpawn(s -> {
                s.setCount("artifact", 1);
                if (!s.flags.contains("paired")) { s.flags.add("paired"); Fx.summon(s, "spire_spear", 3.5, -3.5); } // the duo arrives together
            })
            .onTick(S3::artifact)
            .pattern(s -> {
                int t = s.getTurn() % 3;
                if (t == 2) return "smite";
                if (t == 0) return s.rand().nextBoolean() ? "fortify" : "bash";
                return s.lastMove().equals("bash") ? "fortify" : "bash";
            })
            .move(Move.of("bash", 28).intent(Intent.ATTACK_DEBUFF, 12, 1).reach(2.8f)
                .sound(9, SoundEvents.ARMOR_EQUIP_IRON, 1f, 0.6f)
                .at(13, S3.backMelee(12, 3.4, 80, v -> { Fx.weak(v, 20); Fx.knock(null, v, 0); }))
                .at(13, Fx.sfx(SoundEvents.ANVIL_LAND, 0.7f, 0.7f)))
            .move(Move.of("fortify", 34).intent(Intent.DEFEND).noTarget()
                .at(18, (s, t) -> {
                    for (SpireMob a : S3.allies(s, 16)) { a.addBlock(30); Fx.particlesAt(s, ParticleTypes.WAX_ON, a.position().add(0, a.getBbHeight() * 0.6, 0), 18, 0.6, 0.05); }
                    Fx.sound(s, SoundEvents.ANVIL_PLACE, 1f, 0.6f);
                    Fx.ring(s, ParticleTypes.END_ROD, s.position().add(0, 0.2, 0), 2.0, 24);
                }))
            .move(Move.of("smite", 40).intent(Intent.ATTACK, 34, 1).reach(3.0f)
                .sound(14, SoundEvents.IRON_GOLEM_ATTACK, 1.2f, 0.6f)
                .at(24, S3.backMelee(34, 3.6, 90, null))
                .at(24, (s, t) -> { Fx.sound(s, SoundEvents.ANVIL_LAND, 1.1f, 0.5f); Fx.particlesAt(s, ParticleTypes.EXPLOSION, Fx.rel(s, 2.0, 0, 0.3), 2, 0.3, 0); }));
    }
}
