package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Spheric Guardian. 20 HP, Barricade (block is never cleared), Artifact 3, starts with 40 Block.
 * Activate (+25 Block) -> Debuff Attack (10 + 5 Frail) -> alternates Slam (10x2) / Harden (10 + 15 Block).
 * Look: floating stone-and-bronze sphere with a glowing blue cyclops eye, two orbiting shield plates.
 */
public final class SphericGuardian {
    private SphericGuardian() {}

    static final int STONE = 0x7d8794, STONE_D = 0x4a525e, BRONZE = 0xc69a45, BRONZE_D = 0x7d5a22, EYE = 0x6fe4ff, EYE_C = 0xe8ffff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("spheric_guardian");
        Part core = m.add("core").at(0, 15, 0);
        Part ball = core.add("ball");
        // rounded sphere out of three crossing slabs + a core
        ball.cbox(-9, 18, 18, 18).color(STONE).grad(0x9aa4b0, STONE_D).noise(0.12f).bevel(0.25f);
        ball.cbox(-7, 22, 14, 14).color(STONE).grad(0x96a0ac, STONE_D).noise(0.12f).bevel(0.2f);
        ball.cbox(-11, 14, 22, 14).color(STONE).grad(0xa6b0bc, 0x3e4650).noise(0.12f).bevel(0.2f);
        ball.cbox(-7, 14, 14, 22).color(STONE).grad(0x96a0ac, STONE_D).noise(0.12f).bevel(0.2f)
            .paint(Face.FRONT, 0, 6, 14, 2, BRONZE_D);
        // bronze equator band
        ball.cbox(-1.5f, 23, 3, 23).color(BRONZE).grad(0xe0b860, BRONZE_D).flat().noise(0.08f)
            .paint(Face.FRONT, 3, 1, 1, 1, 0x5a3a12).paint(Face.FRONT, 19, 1, 1, 1, 0x5a3a12)
            .paint(Face.LEFT, 3, 1, 1, 1, 0x5a3a12).paint(Face.RIGHT, 19, 1, 1, 1, 0x5a3a12);
        // vertical bronze meridian
        ball.cbox(-11.5f, 3, 23, 19).color(BRONZE).grad(0xe0b860, BRONZE_D).flat().inflate(-0.2f);
        // top/bottom caps
        ball.cbox(10.5f, 6, 2, 6).color(BRONZE_D).bevel(0.2f);
        ball.cbox(-12.5f, 6, 2, 6).color(BRONZE_D).bevel(0.2f);
        Part spike = ball.add("spike").at(0, 12.5f, 0);
        spike.cbox(0, 2, 3, 2).color(0xe9c66a).flat();
        // eye socket + glowing eye
        Part eye = ball.add("eye").at(0, 0, 11);
        eye.box(-5, -5, 0, 10, 10, 1).color(BRONZE_D).bevel(0.3f);
        eye.box(-4, -4, 0.6f, 8, 8, 1).color(0x14202c)
            .glow(Face.FRONT, 1, 1, 6, 6, 0x2aa8d8).glow(Face.FRONT, 2, 2, 4, 4, EYE).glow(Face.FRONT, 3, 3, 2, 2, EYE_C);
        Part lid = eye.add("lid").at(0, 4.5f, 1);
        lid.box(-5, -1, 0, 10, 2, 1).color(STONE_D).bevel(0.2f);
        // orbiting shields (two rings of two plates each)
        for (int r = 0; r < 2; r++) {
            Part ring = core.add(r == 0 ? "ringA" : "ringB").rot(0, r * 90, 0);
            for (int s = -1; s <= 1; s += 2) {
                Part pl = ring.add((r == 0 ? "plA" : "plB") + (s < 0 ? "R" : "L")).at(s * 15, 0, 0);
                pl.box(-1, -6, -5, 2, 12, 10).color(BRONZE).grad(0xe8c46a, BRONZE_D).bevel(0.3f)
                    .glow(s < 0 ? Face.RIGHT : Face.LEFT, 4, 5, 2, 2, EYE);
                pl.box(-1.5f, -2, -2, 3, 4, 4).color(STONE_D).bevel(0.2f);
            }
        }
        // ambient: hover bob, slow plate orbit, eye pulse
        m.proc((rig, c) -> {
            if (c.deathT >= 0) return;
            rig.pos("core", 0, (float) Math.sin(c.age * 0.09f) * 1.2f, 0);
            rig.rot("ringA", 0, c.age * 2.2f, 0);
            rig.rot("ringB", 0, -c.age * 1.6f, 8);
            float p = 1f + (float) Math.sin(c.age * 0.2f) * 0.04f;
            rig.scl("eye", p, p, 1);
        });
        m.look("ball", 25, 20);
        m.clip("idle", 40).loop()
            .rot("ball", 0, 0, 0, 0).rot("ball", 20, 0, 0, 4).rot("ball", 40, 0, 0, 0)
            .rot("lid", 0, 0, 0, 0).rot("lid", 30, 0, 0, 0).rot("lid", 32, 0, 0, 0).pos("lid", 0, 0, 0, 0).pos("lid", 30, 0, 0, 0).pos("lid", 32, 0, -8, 0).pos("lid", 35, 0, 0, 0);
        m.clip("walk", 20).loop().rot("ball", 0, 8, 0, 0).rot("ball", 10, 10, 0, 0).rot("ball", 20, 8, 0, 0);
        // ACTIVATE: plates retract, whirl around fast and slam in close to the sphere; eye flares
        m.clip("activate", 36)
            .pos("plAR", 0, 0, 0, 0).pos("plAR", 8, -5, 0, 0).pos("plAR", 20, 7, 0, 0, Ease.IN_CUBIC).pos("plAR", 28, 6, 0, 0).pos("plAR", 36, 0, 0, 0)
            .pos("plAL", 0, 0, 0, 0).pos("plAL", 8, 5, 0, 0).pos("plAL", 20, -7, 0, 0, Ease.IN_CUBIC).pos("plAL", 28, -6, 0, 0).pos("plAL", 36, 0, 0, 0)
            .pos("plBR", 0, 0, 0, 0).pos("plBR", 8, -5, 0, 0).pos("plBR", 20, 7, 0, 0, Ease.IN_CUBIC).pos("plBR", 28, 6, 0, 0).pos("plBR", 36, 0, 0, 0)
            .pos("plBL", 0, 0, 0, 0).pos("plBL", 8, 5, 0, 0).pos("plBL", 20, -7, 0, 0, Ease.IN_CUBIC).pos("plBL", 28, -6, 0, 0).pos("plBL", 36, 0, 0, 0)
            .rot("ringA", 0, 0, 0, 0).rot("ringA", 20, 0, 540, 0, Ease.IN_QUAD).rot("ringA", 36, 0, 720, 0, Ease.OUT_CUBIC)
            .rot("ringB", 0, 0, 0, 0).rot("ringB", 20, 0, -540, 0, Ease.IN_QUAD).rot("ringB", 36, 0, -720, 0, Ease.OUT_CUBIC)
            .scl("eye", 0, 1, 1, 1).scl("eye", 18, 0.7f, 0.7f, 1).scl("eye", 22, 1.5f, 1.5f, 1, Ease.OUT_BACK).scl("eye", 36, 1, 1, 1)
            .pos("core", 0, 0, 0, 0).pos("core", 18, 0, 3, 0).pos("core", 22, 0, -1, 0, Ease.IN_CUBIC).pos("core", 36, 0, 0, 0)
            .scl("ball", 0, 1, 1, 1).scl("ball", 21, 1, 1, 1).scl("ball", 23, 1.08f, 0.92f, 1.08f).scl("ball", 30, 1, 1, 1, Ease.OUT_ELASTIC);
        // DEBUFF ATTACK: rears back, eye glares (frail beam), rolls forward and rams
        m.clip("attack_debuff", 34)
            .rot("ball", 0, 0, 0, 0).rot("ball", 10, -30, 0, 0).rot("ball", 16, -40, 0, 0).rot("ball", 20, 50, 0, 0, Ease.IN_CUBIC).rot("ball", 26, 40, 0, 0).rot("ball", 34, 0, 0, 0)
            .pos("core", 0, 0, 0, 0).pos("core", 10, 0, 2, -5).pos("core", 16, 0, 3, -6).pos("core", 20, 0, -2, 10, Ease.IN_CUBIC).pos("core", 26, 0, -1, 8).pos("core", 34, 0, 0, 0)
            .scl("eye", 0, 1, 1, 1).scl("eye", 10, 1.6f, 1.6f, 1, Ease.OUT_BACK).scl("eye", 16, 1.5f, 1.5f, 1).scl("eye", 20, 0.9f, 0.9f, 1).scl("eye", 34, 1, 1, 1)
            .pos("plAR", 0, 0, 0, 0).pos("plAR", 20, 4, 0, 0).pos("plAR", 34, 0, 0, 0)
            .pos("plAL", 0, 0, 0, 0).pos("plAL", 20, -4, 0, 0).pos("plAL", 34, 0, 0, 0);
        // SLAM x2: two hard rams with a backswing between
        m.clip("slam", 40)
            .pos("core", 0, 0, 0, 0).pos("core", 8, 0, 4, -6).pos("core", 12, 0, -3, 11, Ease.IN_CUBIC).pos("core", 17, 0, 1, 2)
            .pos("core", 23, 0, 5, -5).pos("core", 27, 0, -4, 12, Ease.IN_CUBIC).pos("core", 32, 0, -1, 6).pos("core", 40, 0, 0, 0)
            .rot("ball", 0, 0, 0, 0).rot("ball", 8, -25, 0, 0).rot("ball", 12, 70, 0, 0, Ease.IN_CUBIC).rot("ball", 17, 20, 0, 0)
            .rot("ball", 23, -30, 0, 0).rot("ball", 27, 80, 0, 0, Ease.IN_CUBIC).rot("ball", 32, 30, 0, 0).rot("ball", 40, 0, 0, 0)
            .scl("ball", 0, 1, 1, 1).scl("ball", 12, 1, 1, 1).scl("ball", 13, 1.1f, 1.1f, 0.85f).scl("ball", 16, 1, 1, 1)
            .scl("ball", 27, 1, 1, 1).scl("ball", 28, 1.1f, 1.1f, 0.85f).scl("ball", 31, 1, 1, 1);
        // HARDEN: single body-check, then plates lock tight against the shell
        m.clip("harden", 38)
            .pos("core", 0, 0, 0, 0).pos("core", 8, 0, 3, -6).pos("core", 12, 0, -2, 10, Ease.IN_CUBIC).pos("core", 18, 0, 0, 3).pos("core", 38, 0, 0, 0)
            .rot("ball", 0, 0, 0, 0).rot("ball", 8, -20, 0, 0).rot("ball", 12, 60, 0, 0, Ease.IN_CUBIC).rot("ball", 20, 0, 0, 0).rot("ball", 38, 0, 0, 0)
            .pos("plAR", 0, 0, 0, 0).pos("plAR", 18, 0, 0, 0).pos("plAR", 24, 6, 0, 0, Ease.IN_CUBIC).pos("plAR", 32, 6, 0, 0).pos("plAR", 38, 0, 0, 0)
            .pos("plAL", 0, 0, 0, 0).pos("plAL", 18, 0, 0, 0).pos("plAL", 24, -6, 0, 0, Ease.IN_CUBIC).pos("plAL", 32, -6, 0, 0).pos("plAL", 38, 0, 0, 0)
            .pos("plBR", 0, 0, 0, 0).pos("plBR", 18, 0, 0, 0).pos("plBR", 24, 6, 0, 0, Ease.IN_CUBIC).pos("plBR", 32, 6, 0, 0).pos("plBR", 38, 0, 0, 0)
            .pos("plBL", 0, 0, 0, 0).pos("plBL", 18, 0, 0, 0).pos("plBL", 24, -6, 0, 0, Ease.IN_CUBIC).pos("plBL", 32, -6, 0, 0).pos("plBL", 38, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 22, 0, 0, 0).pos("lid", 25, 0, -6, 0).pos("lid", 32, 0, -6, 0).pos("lid", 36, 0, 0, 0);
        m.clip("hurt", 10).rot("ball", 0, 0, 0, 0).rot("ball", 2, -18, 0, 8).rot("ball", 10, 0, 0, 0)
            .pos("core", 0, 0, 0, 0).pos("core", 2, 0, 1, -2).pos("core", 10, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 2, 0, -6, 0).pos("lid", 8, 0, 0, 0);
        // death: eye dims, plates fall away, sphere drops and rolls
        m.clip("death", 34)
            .scl("eye", 0, 1, 1, 1).scl("eye", 10, 0.3f, 0.3f, 1).scl("eye", 34, 0.1f, 0.1f, 1)
            .pos("core", 0, 0, 0, 0).pos("core", 12, 0, -4, 0, Ease.IN_QUAD).pos("core", 20, 0, -4, 0, Ease.OUT_BOUNCE).pos("core", 34, 0, -4, 6)
            .rot("ball", 0, 0, 0, 0).rot("ball", 20, 10, 0, 0).rot("ball", 34, 120, 0, 0)
            .pos("plAR", 0, 0, 0, 0).pos("plAR", 14, -4, -10, 0, Ease.IN_QUAD).pos("plAR", 34, -6, -10, 0)
            .pos("plAL", 0, 0, 0, 0).pos("plAL", 14, 4, -10, 0, Ease.IN_QUAD).pos("plAL", 34, 6, -10, 0)
            .pos("plBR", 0, 0, 0, 0).pos("plBR", 14, -4, -10, 0, Ease.IN_QUAD).pos("plBR", 34, -6, -10, 0)
            .pos("plBL", 0, 0, 0, 0).pos("plBL", 14, 4, -10, 0, Ease.IN_QUAD).pos("plBL", 34, 6, -10, 0)
            .rot("plAR", 0, 0, 0, 0).rot("plAR", 14, 0, 0, 70).rot("plAL", 0, 0, 0, 0).rot("plAL", 14, 0, 0, -70)
            .rot("plBR", 0, 0, 0, 0).rot("plBR", 14, 0, 0, 70).rot("plBL", 0, 0, 0, 0).rot("plBL", 14, 0, 0, -70);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("spheric_guardian", "Spheric Guardian", "スフィアガーディアン")
            .game(1, 2).size(1.5f, 1.75f).hp(20).speed(0.2f).style(Style.METAL).egg(0x7d8794, 0xc69a45)
            .model(SphericGuardian::model).spawn(5, 1, 1, MobDef.Habitat.MOUNTAIN)
            .keepBlock().kbResist(0.9f)
            .onSpawn(s -> { s.addBlock(40); s.setCount("artifact", 3); })
            .pattern(Pattern.seq(2, "activate", "attack_debuff", "slam", "harden"))
            .move(Move.of("activate", 36).intent(Intent.DEFEND).noTarget()
                .sound(8, SoundEvents.BEACON_ACTIVATE, 0.8f, 1.6f)
                .at(22, Fx.block(25)))
            .move(Move.of("attack_debuff", 34).intent(Intent.ATTACK_DEBUFF, 10, 1).reach(2.6f)
                .sound(10, SoundEvents.BEACON_POWER_SELECT, 0.8f, 1.8f)
                .at(20, Fx.melee(10, 3.0, 90, v -> Fx.frail(v, 10))))
            .move(Move.of("slam", 40).intent(Intent.ATTACK, 10, 2).reach(2.6f)
                .at(12, Fx.melee(10, 3.0, 90)).at(27, Fx.melee(10, 3.0, 90)))
            .move(Move.of("harden", 38).intent(Intent.ATTACK_DEFEND, 10, 1).reach(2.6f)
                .at(12, Fx.melee(10, 3.0, 90)).at(24, Fx.block(15)))
            // Artifact 3: cleanse the first three harmful effects that land on it
            .onTick(s -> {
                if (s.count("artifact") > 0 && s.tickCount % 5 == 0)
                    for (var e : new java.util.ArrayList<>(s.getActiveEffects()))
                        if (e.getEffect().getCategory() == net.minecraft.world.effect.MobEffectCategory.HARMFUL && s.count("artifact") > 0) {
                            s.removeEffect(e.getEffect()); s.setCount("artifact", s.count("artifact") - 1);
                            Fx.particles(s, ParticleTypes.END_ROD, 8, 0.6);
                        }
            });
    }
}
