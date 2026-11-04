package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * StS1 Act 2 Snecko. 114-120 HP. Opens with Perplexing Glare (Confused), then Bite 15 (60%) / Tail Whip 8 + 2 Vulnerable (40%), never Bite 3x.
 * Confused (random card costs) is mapped to Nausea + Dazed: the screen swims and the player's footing is unreliable.
 * Look: big upright serpent-lizard on a coiled tail, huge bulbous head with one giant yellow slit eye, wide jaws.
 */
public final class Snecko {
    private Snecko() {}

    static final int SKIN = 0x3e8f74, SKIN_D = 0x24584a, BELLY = 0xdcc47a, BELLY_D = 0xa8904a, EYE = 0xffd23a, SPOT = 0x2c6b8a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("snecko");
        // coiled tail on the ground
        Part base = m.add("base").at(0, 0, 0);
        base.box(-7, 0, -8, 14, 4, 13).color(SKIN).grad(SKIN, SKIN_D).noise(0.12f).bevel(0.2f)
            .paint(Face.TOP, 2, 3, 2, 2, SPOT).paint(Face.TOP, 9, 7, 2, 2, SPOT);
        base.box(-5, 3.5f, -6, 10, 2, 9).color(SKIN_D).bevel(0.2f);
        Part t1 = base.add("tail1").at(-5, 2, -7).rot(0, -40, 0);
        t1.box(-2, -2, -7, 4, 4, 7).color(SKIN).grad(SKIN, SKIN_D).bevel(0.15f).px(Face.TOP, 1, 2, SPOT);
        Part t2 = t1.add("tail2").at(0, 0, -7).rot(0, -50, 0);
        t2.box(-1.5f, -1.5f, -6, 3, 3, 6).color(SKIN).grad(SKIN, SKIN_D).bevel(0.15f);
        Part t3 = t2.add("tail3").at(0, 0, -6).rot(0, -40, 0);
        t3.box(-1, -1, -5, 2, 2, 5).color(SKIN_D);
        t3.box(-0.5f, -0.5f, -7, 1, 1, 2).color(0x1a3a30);
        // torso rising from the coil
        Part body = m.add("body").at(0, 4, 0);
        body.box(-5, 0, -4, 10, 10, 8).color(SKIN).grad(0x4aa384, SKIN_D).noise(0.12f).bevel(0.15f)
            .paint(Face.LEFT, 2, 3, 2, 2, SPOT).paint(Face.RIGHT, 4, 5, 2, 2, SPOT);
        body.box(-3.5f, 0.5f, 3.4f, 7, 9, 1).color(BELLY).grad(BELLY, BELLY_D)
            .band(2, 1, BELLY_D).band(5, 1, BELLY_D).band(8, 1, BELLY_D);
        Part chest = body.add("chest").at(0, 10, 0);
        chest.box(-4, 0, -3.5f, 8, 7, 7).color(SKIN).grad(0x52ac8c, SKIN).bevel(0.15f);
        chest.box(-3, 0, 3, 6, 7, 1).color(BELLY).grad(BELLY, BELLY_D).band(3, 1, BELLY_D);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = chest.add(s < 0 ? "armR" : "armL").at(s * 4.5f, 5, 1).rot(-30, 0, s * -10);
            arm.box(-1, -5, -1, 2, 5, 2).color(SKIN).bevel(0.1f);
            Part claw = arm.add(s < 0 ? "clawR" : "clawL").at(0, -5, 0);
            claw.box(-1.5f, -2, -0.5f, 3, 2, 2).color(SKIN_D);
            claw.box(-1.5f, -3, 1, 1, 2, 1).color(0xe8e0c0).flat();
            claw.box(0.5f, -3, 1, 1, 2, 1).color(0xe8e0c0).flat();
        }
        Part neck = chest.add("neck").at(0, 7, 0);
        neck.box(-2.5f, 0, -2.5f, 5, 4, 5).color(SKIN).grad(SKIN, SKIN_D);
        neck.box(-2, 0, 2.2f, 4, 4, 1).color(BELLY);
        // huge head
        Part head = neck.add("head").at(0, 3, 1);
        head.box(-6, 0, -5, 12, 10, 11).color(SKIN).grad(0x5ab894, SKIN_D).noise(0.12f).bevel(0.2f)
            .paint(Face.TOP, 2, 2, 3, 2, SPOT).paint(Face.TOP, 7, 6, 2, 2, SPOT).paint(Face.LEFT, 2, 2, 2, 2, SPOT);
        head.box(-4, 9.5f, -3, 8, 2, 7).color(SKIN_D).bevel(0.2f);               // brow ridge
        Part eye = head.add("eye").at(0, 6, 5.5f);
        eye.box(-3.5f, -3.5f, 0, 7, 7, 1).color(0xf0e0a0)
            .glow(Face.FRONT, 1, 1, 5, 5, EYE).glow(Face.FRONT, 0, 2, 7, 3, 0xffe060)
            .paint(Face.FRONT, 3, 0, 1, 7, 0x101010).paint(Face.FRONT, 0, 0, 1, 1, SKIN_D).paint(Face.FRONT, 6, 0, 1, 1, SKIN_D)
            .paint(Face.FRONT, 0, 6, 1, 1, SKIN_D).paint(Face.FRONT, 6, 6, 1, 1, SKIN_D);
        Part lid = eye.add("lid").at(0, 3.5f, 0.5f);
        lid.box(-4, -1, 0, 8, 1, 1).color(SKIN_D);
        Part snout = head.add("snout").at(0, 1, 5);
        snout.box(-5, 0, 0, 10, 2, 4).color(SKIN).grad(SKIN, SKIN_D).bevel(0.15f)
            .px(Face.TOP, 2, 2, 0x102018).px(Face.TOP, 7, 2, 0x102018);
        snout.box(-4.5f, -1, 1, 1, 1, 1).color(0xf4f0e0).flat();
        snout.box(3.5f, -1, 1, 1, 1, 1).color(0xf4f0e0).flat();
        snout.box(-1.5f, -1, 3, 1, 1, 1).color(0xf4f0e0).flat();
        snout.box(0.5f, -1, 3, 1, 1, 1).color(0xf4f0e0).flat();
        Part jaw = head.add("jaw").at(0, 1, -2);
        jaw.box(-5, -2.5f, 0, 10, 3, 10).color(BELLY).grad(BELLY, BELLY_D).bevel(0.15f)
            .paint(Face.TOP, 1, 1, 8, 7, 0xa0384a);
        jaw.box(-4.5f, 0.5f, 9, 1, 1, 1).color(0xf4f0e0).flat();
        jaw.box(3.5f, 0.5f, 9, 1, 1, 1).color(0xf4f0e0).flat();
        Part tongue = jaw.add("tongue").at(0, 0.6f, 6);
        tongue.box(-1, 0, 0, 2, 1, 4).color(0xd04a6a).flat();
        m.look("head", 45, 25);
        m.proc((rig, c) -> { // slow tail-tip flick
            if (c.deathT >= 0) return;
            rig.rot("tail3", 0, (float) Math.sin(c.age * 0.15f) * 20, 0);
        });
        m.clip("idle", 60).loop()
            .rot("body", 0, 0, 0, 0).rot("body", 15, 0, 0, 5).rot("body", 45, 0, 0, -5).rot("body", 60, 0, 0, 0)
            .rot("chest", 0, 0, 0, 0).rot("chest", 15, 0, 0, 5).rot("chest", 45, 0, 0, -5).rot("chest", 60, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 15, 0, 0, -9).rot("head", 45, 0, 0, 9).rot("head", 60, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 40, 0, 0, 0).pos("lid", 42, 0, -6, 0).pos("lid", 45, 0, 0, 0)
            .rot("tongue", 0, 0, 0, 0).scl("tongue", 0, 1, 1, 1).scl("tongue", 20, 1, 1, 1).scl("tongue", 23, 1, 1, 2).scl("tongue", 26, 1, 1, 1);
        m.clip("walk", 20).loop()
            .rot("tail1", 0, 0, 15, 0).rot("tail1", 10, 0, -15, 0).rot("tail1", 20, 0, 15, 0)
            .rot("tail2", 0, 0, -20, 0).rot("tail2", 10, 0, 20, 0).rot("tail2", 20, 0, -20, 0)
            .rot("body", 0, 0, 0, 4).rot("body", 10, 0, 0, -4).rot("body", 20, 0, 0, 4)
            .pos("base", 0, 0, 0, 0).pos("base", 5, 0, 0.6f, 0).pos("base", 10, 0, 0, 0).pos("base", 15, 0, 0.6f, 0).pos("base", 20, 0, 0, 0);
        // PERPLEXING GLARE: rears up, eye swells and the head weaves hypnotic figure-eights
        m.clip("glare", 50)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -12, 0, 0, Ease.OUT_BACK).rot("body", 44, -10, 0, 0).rot("body", 50, 0, 0, 0)
            .rot("chest", 0, 0, 0, 0).rot("chest", 10, -8, 0, 0).rot("chest", 44, -6, 0, 0).rot("chest", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 15, 0, 0).rot("head", 16, 15, 25, 15).rot("head", 22, 15, 0, -15).rot("head", 28, 15, -25, 15)
            .rot("head", 34, 15, 0, -15).rot("head", 40, 15, 25, 15).rot("head", 46, 10, 0, 0).rot("head", 50, 0, 0, 0)
            .scl("eye", 0, 1, 1, 1).scl("eye", 10, 1.5f, 1.5f, 1.5f, Ease.OUT_BACK).scl("eye", 22, 1.35f, 1.35f, 1.3f).scl("eye", 30, 1.6f, 1.6f, 1.6f).scl("eye", 44, 1.5f, 1.5f, 1.5f).scl("eye", 50, 1, 1, 1)
            .rot("eye", 0, 0, 0, 0).rot("eye", 16, 0, 0, 90).rot("eye", 28, 0, 0, 270).rot("eye", 40, 0, 0, 360).rot("eye", 50, 0, 0, 360)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -60, 0, 30).rot("armR", 44, -55, 0, 30).rot("armR", 50, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -60, 0, -30).rot("armL", 44, -55, 0, -30).rot("armL", 50, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 15, 0, 0).rot("jaw", 44, 12, 0, 0).rot("jaw", 50, 0, 0, 0);
        // BITE: coils back with jaws agape, lunges head-first and SNAPS
        m.clip("bite", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -18, 0, 0).rot("body", 14, 28, 0, 0, Ease.IN_CUBIC).rot("body", 20, 22, 0, 0).rot("body", 30, 0, 0, 0)
            .rot("chest", 0, 0, 0, 0).rot("chest", 10, -10, 0, 0).rot("chest", 14, 18, 0, 0, Ease.IN_CUBIC).rot("chest", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -25, 0, 0).rot("head", 14, -5, 0, 0, Ease.IN_CUBIC).rot("head", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -3).pos("body", 14, 0, 0, 5, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 50, 0, 0, Ease.OUT_BACK).rot("jaw", 14, 55, 0, 0).rot("jaw", 15, -4, 0, 0, Ease.IN_CUBIC).rot("jaw", 20, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -40, 0, 30).rot("armR", 14, -90, 0, 10).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -40, 0, -30).rot("armL", 14, -90, 0, -10).rot("armL", 30, 0, 0, 0)
            .rot("tail1", 0, 0, 0, 0).rot("tail1", 14, 0, 20, 0).rot("tail1", 30, 0, 0, 0);
        // TAIL WHIP: wind the torso, whole body spins a full turn so the coiled tail lashes around
        m.clip("tail_whip", 36)
            .rot("base", 0, 0, 0, 0).rot("base", 10, 0, 40, 0).rot("base", 20, 0, -320, 0, Ease.IN_CUBIC).rot("base", 30, 0, -360, 0, Ease.OUT_CUBIC).rot("base", 36, 0, -360, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 0, 30, 6).rot("body", 20, 0, -300, -8, Ease.IN_CUBIC).rot("body", 30, 0, -360, 0, Ease.OUT_CUBIC).rot("body", 36, 0, -360, 0)
            .rot("tail1", 0, 0, 0, 0).rot("tail1", 10, 0, 30, 0).rot("tail1", 18, 0, 45, 15).rot("tail1", 22, 0, -20, 0).rot("tail1", 36, 0, 0, 0)
            .rot("tail2", 0, 0, 0, 0).rot("tail2", 10, 0, 40, 0).rot("tail2", 18, 0, 50, 0).rot("tail2", 24, 0, -30, 0).rot("tail2", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 0, -20, 0).rot("head", 20, 0, 30, 0).rot("head", 36, 0, 0, 0);
        m.clip("hurt", 10).rot("chest", 0, 0, 0, 0).rot("chest", 3, -14, 0, 6).rot("chest", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 0, -8).rot("head", 10, 0, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 3, 0, -6, 0).pos("lid", 9, 0, 0, 0);
        m.clip("death", 40)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -15, 0, 0).rot("body", 28, 0, 0, 80, Ease.IN_QUAD).rot("body", 34, 0, 0, 72).rot("body", 40, 0, 0, 78)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -30, 0, 0).rot("head", 28, 20, 0, 10).rot("head", 40, 25, 0, 10)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 28, 30, 0, 0)
            .pos("lid", 0, 0, 0, 0).pos("lid", 30, 0, -3, 0)
            .scl("eye", 0, 1, 1, 1).scl("eye", 30, 1, 0.6f, 1)
            .rot("armR", 0, 0, 0, 0).rot("armR", 28, -70, 0, 40).rot("armL", 0, 0, 0, 0).rot("armL", 28, -50, 0, -30);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("snecko", "Snecko", "スネッコ")
            .game(1, 2).size(1.2f, 2.3f).hp(117).speed(0.24f).style(Style.FROG).egg(0x3e8f74, 0xffd23a)
            .model(Snecko::model).spawn(3, 1, 1, MobDef.Habitat.JUNGLE).kbResist(0.4f)
            .pattern(Pattern.first("glare", Pattern.random().add("bite", 60).add("tail_whip", 40).maxRepeat("bite", 2)))
            .move(Move.of("glare", 50).intent(Intent.STRONG_DEBUFF).ranged(10)
                .sound(8, SoundEvents.EVOKER_PREPARE_WOLOLO, 1f, 0.7f)
                .at(16, (s, t) -> {
                    if (t == null) return;
                    t.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 20));
                    Fx.curse(t, Fx.Curse.DAZED);
                    Fx.particlesAt(s, ParticleTypes.ENTITY_EFFECT, t.position().add(0, 1.6, 0), 30, 0.4, 0.5);
                    Fx.particlesAt(s, ParticleTypes.END_ROD, Fx.rel(s, 0.6, 0, 2.0), 10, 0.2, 0.05);
                }))
            .move(Move.of("bite", 30).intent(Intent.ATTACK, 15, 1).reach(2.8f)
                .sound(12, SoundEvents.EVOKER_FANGS_ATTACK, 1f, 0.8f).at(14, Fx.melee(15, 3.2, 70)))
            .move(Move.of("tail_whip", 36).intent(Intent.ATTACK_DEBUFF, 8, 1).reach(2.6f)
                .sound(14, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.6f)
                .at(20, Fx.aoe(8, 3.0, v -> { Fx.vulnerable(v, 10); Fx.knock(null, v, 0); })));
    }
}
