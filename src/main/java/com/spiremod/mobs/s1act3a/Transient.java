package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 Transient (999 HP). Fading 5: vanishes after 5 turns. Shifting: loses Strength when hit, regains at end of turn.
 * Attacks every turn for 30, +10 each turn (30,40,50,60,70). Tall dark wraith with glowing hollow eyes.
 */
public final class Transient {
    private Transient() {}
    static final int SHADE = 0x1e1e40, SHADE_D = 0x0c0c1e, SHADE_L = 0x40407a, EYE = 0xc8f0ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("transient");
        // StS1 Transient: a round navy body with a glowing yellow star, four long spindly limbs (arms raised, legs splayed) and a small hanging tail
        Part body = m.add("body").at(0, 19, 0);
        Part torso = body.add("torso").at(0, 0, 0);
        int[][] lay = {{8, 2}, {12, 2}, {14, 3}, {14, 3}, {12, 2}, {8, 2}};     // rounded blob: width, height
        int y = -7;
        for (int[] l : lay) { torso.box(-l[0] / 2f, y, -l[0] / 2f + 0.5f, l[0], l[1], l[0] - 1).color(SHADE).grad(SHADE_L, SHADE_D).bevel(0.3f).noise(0.12f); y += l[1]; }
        torso.box(-1, -11, -1.5f, 2, 4, 3).color(SHADE_D).bevel(0.2f);                      // dangling belly tail
        torso.box(-1, -14, -1, 2, 3, 2).color(SHADE_D).flat();
        torso.box(-0.5f, 1, 7.3f, 1, 5, 1).color(0xffd830).flat().glow(Face.FRONT, 0, 0, 1, 5, 0xffd830);       // slit above the star
        torso.box(-0.5f, -6, 7.3f, 1, 4, 1).color(0xffd830).flat().glow(Face.FRONT, 0, 0, 1, 4, 0xffd830);      // slit below
        torso.box(-0.75f, -3.5f, 7.4f, 2, 5, 1).color(0xffe870).flat().glow(Face.FRONT, 0, 0, 2, 5, 0xffe870);   // four-point star
        torso.box(-2.5f, -1.5f, 7.4f, 5, 2, 1).color(0xffe870).flat().glow(Face.FRONT, 0, 0, 5, 2, 0xffe870);
        Part head = torso.add("head").at(0, 7, 0);
        head.box(-1, 0, -1, 2, 1, 2).color(SHADE_D).flat();                                   // (no real head: look-target only)
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 6, 2, 0).rot(0, 0, s * 125);
            arm.box(-1, -9, -1, 2, 9, 2).color(SHADE).grad(SHADE_L, SHADE_D).bevel(0.15f);
            Part claw = arm.add(s < 0 ? "clawR" : "clawL").at(0, -9, 0).rot(0, 0, s * -130);
            claw.box(-1, -11, -1, 2, 11, 2).color(SHADE).bevel(0.15f);
            for (int f = -1; f <= 1; f++) claw.box(f * 1.2f - 0.5f, -15 + Math.abs(f), -0.5f, 1, 4, 1).color(SHADE_D).flat();   // curled fingers
            Part leg = torso.add(s < 0 ? "legR" : "legL").at(s * 4, -5, 0).rot(0, 0, s * 52);
            leg.box(-1, -9, -1, 2, 9, 2).color(SHADE).grad(SHADE_L, SHADE_D).bevel(0.15f);
            Part low = leg.add(s < 0 ? "lowR" : "lowL").at(0, -9, 0).rot(0, 0, s * -78);
            low.box(-1, -12, -1, 2, 12, 2).color(SHADE).bevel(0.15f);
            for (int f = -1; f <= 1; f++) low.box(f * 1.3f - 0.5f, -14, 0.5f + (f == 0 ? 1 : 0), 1, 2, 3).color(SHADE_D).flat();   // toes
        }
        m.bob(2f, 0.1f);
        m.look("head", 40, 25);
        m.proc((r, c) -> { float f = Math.max(0.35f, 1 - 0.13f * c.var); r.scl("body", 1, f < 1 ? 0.7f + 0.3f * f : 1, 1); });   // thins out as it fades
        m.clip("idle", 50).loop()
            .rot("torso", 0, 3, 0, 0).rot("torso", 25, -3, 0, 0).rot("torso", 50, 3, 0, 0)
            .rot("armR", 0, 6, 0, 12).rot("armR", 25, 10, 0, 18).rot("armR", 50, 6, 0, 12)
            .rot("armL", 0, 6, 0, -12).rot("armL", 25, 10, 0, -18).rot("armL", 50, 6, 0, -12);
        m.clip("walk", 24).loop().rot("torso", 0, 12, 0, 0).rot("torso", 12, 16, 0, 0).rot("torso", 24, 12, 0, 0);
        m.clip("attack", 34)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -22, 0, 0).rot("torso", 17, 38, 0, 0, Ease.IN_CUBIC).rot("torso", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 1, -5).pos("body", 17, 0, -2, 9, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -150, 0, 20).rot("armR", 17, -30, 0, -10, Ease.IN_CUBIC).rot("armR", 34, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -150, 0, -20).rot("armL", 17, -30, 0, 10, Ease.IN_CUBIC).rot("armL", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -25, 0, 0).rot("head", 17, 20, 0, 0).rot("head", 34, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -15, 0, 8).rot("torso", 10, 0, 0, 0);
        m.clip("death", 30).pos("body", 0, 0, 0, 0).pos("body", 30, 0, -6, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 10, 1, 1.1f, 1).scl("body", 30, 1.4f, 0.1f, 1.4f)
            .rot("armR", 0, 0, 0, 0).rot("armR", 30, 0, 0, 60).rot("armL", 0, 0, 0, 0).rot("armL", 30, 0, 0, -60);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("transient", "Transient", "トランジェント")
            .game(1, 3).size(1.2f, 2.0f).hp(999).speed(0.24f).style(Style.GHOST).egg(0x2a2540, 0xc8f0ff)
            .model(Transient::model).flying()
            .spawn(2, 1, 1, MobDef.Habitat.ANY)
            .pattern(s -> "attack")
            .move(Move.of("attack", 34).intent(Intent.ATTACK, 30, 1).reach(2.6f)
                .sound(11, SoundEvents.PHANTOM_SWOOP, 1f, 0.7f)
                .at(17, (s, t) -> Fx.melee(30, 3.2, 110).run(s, t)))
            .onHurt((s, src, amt) -> { s.addStrength(-1); s.inc("shifted"); return amt; })   // Shifting
            .onMoveEnd(s -> {
                s.addStrength(s.count("shifted")); s.setCount("shifted", 0);
                s.setVar(s.getTurn());
                if (s.getTurn() >= 5) { Fx.particles(s, ParticleTypes.SOUL, 40, 0.6); Fx.vanish(s); }   // Fading
                else s.addStrength(10);
            });
    }
}
