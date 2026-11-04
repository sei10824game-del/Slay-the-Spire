package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks / Glory Punch Construct (55 HP, Artifact 1). Fixed cycle: READY (10 Block) -> Fast Punch (5x2, Frail 1) -> Strong Punch (14).
 */
public final class PunchConstruct {
    private PunchConstruct() {}
    static final int IRON = 0x7a8590, IRON_D = 0x4a525c, GLOVE = 0xc83a3a, GLOVE_D = 0x8a2424, BRASS = 0xc8a050, EYE = 0xff7a30;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_punch_construct");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3.5f, 9, 0);
            leg.box(-2, -9, -2, 4, 9, 4).color(IRON_D).grad(IRON, IRON_D).noise(0.1f);
            leg.box(-2.5f, -9, -3, 5, 3, 7).color(BRASS).bevel(0.2f);             // boxing boot
            leg.box(-2.2f, -5, -2.2f, 4, 1, 4).inflate(0.2f).color(IRON).flat();  // knee ring
        }
        Part body = m.add("body").at(0, 9, 0);
        body.box(-5, 0, -3, 10, 3, 6).color(IRON_D).bevel(0.2f);
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-6, 0, -3.5f, 12, 9, 7).color(IRON).grad(0x9aa5b0, IRON_D).noise(0.12f).bevel(0.3f);
        torso.box(-6.2f, 0, -3.7f, 12, 2, 7).color(0x8a2424).flat().inflate(0.25f);   // red belt
        torso.box(-4, 3, 3.6f, 8, 4, 1).color(IRON_D).bevel(0.2f).glow(Face.FRONT, 1, 1, 6, 1, EYE);   // chest vent
        for (int i = 0; i < 4; i++) torso.box(-5.2f + (i % 2) * 8.4f, 6 + (i / 2) * 2, 3.7f, 2, 1, 1).color(BRASS).flat(); // rivets
        Part head = torso.add("head").at(0, 9, 0);
        head.box(-3.5f, 0, -3, 7, 6, 6).color(IRON).grad(0xaab5c0, IRON_D).bevel(0.3f);
        head.box(-3, 2, 2.8f, 6, 2, 1).color(0x1a1a22).flat().glow(Face.FRONT, 0, 0, 6, 1, EYE);        // visor slit
        head.box(-4, 5, -3.5f, 8, 2, 7).color(GLOVE).grad(0xe05050, GLOVE_D).bevel(0.3f);          // headgear
        head.box(-0.5f, 7, -3, 1, 2, 6).color(GLOVE_D).flat();
        Part antenna = head.add("antenna").at(3, 6, -2);
        antenna.box(-0.3f, 0, -0.3f, 1, 5, 1).color(BRASS);
        antenna.box(-1, 5, -1, 2, 2, 2).color(EYE).glow(Face.FRONT, 0, 0, 2, 2, EYE);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 7.5f, 8, 0);
            arm.box(-2, -4, -2, 4, 4, 4).color(IRON_D).bevel(0.3f);                            // shoulder ball
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -4, 0);
            fore.box(-1.5f, -5, -1.5f, 3, 5, 3).color(IRON).noise(0.1f);
            fore.box(-1, -5, -1, 2, 5, 2).color(BRASS).flat().inflate(0.3f);                 // piston
            Part glove = fore.add(s < 0 ? "gloveR" : "gloveL").at(0, -5, 0);
            glove.box(-3, -6, -3, 6, 6, 6).color(GLOVE).grad(0xe05050, GLOVE_D).bevel(0.5f).noise(0.06f);
            glove.box(-1.5f, -6.4f, 2, 3, 3, 1).color(0xf0e0d0).flat();                   // thumb highlight
        }
        m.look("head", 35, 20);
        m.walkBiped("legR", "legL", "armR", "armL", 32f, 18f);
        m.clip("idle", 30).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 7, 0, -0.8f, 0).pos("body", 15, 0, 0, 0).pos("body", 22, 0, -0.8f, 0).pos("body", 30, 0, 0, 0)
            .rot("armR", 0, -35, 0, 12).rot("armR", 15, -45, 0, 14).rot("armR", 30, -35, 0, 12)
            .rot("armL", 0, -40, 0, -12).rot("armL", 15, -30, 0, -14).rot("armL", 30, -40, 0, -12)
            .rot("foreR", 0, -80, 0, 0).rot("foreR", 15, -90, 0, 0).rot("foreR", 30, -80, 0, 0)
            .rot("foreL", 0, -80, 0, 0).rot("foreL", 15, -70, 0, 0).rot("foreL", 30, -80, 0, 0)
            .rot("torso", 0, 0, -5, 0).rot("torso", 15, 0, 5, 0).rot("torso", 30, 0, -5, 0);
        // READY: crouch, gloves slam together in front of the face (guard), clank
        m.clip("ready", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -2, 0).pos("body", 14, 0, -1, 1, Ease.OUT_BACK).pos("body", 30, 0, 0, 0)
            .rot("armR", 0, -35, 0, 12).rot("armR", 8, -30, 0, 40).rot("armR", 14, -80, 0, -15, Ease.OUT_BACK).rot("armR", 22, -80, 0, -15).rot("armR", 30, -35, 0, 12)
            .rot("armL", 0, -40, 0, -12).rot("armL", 8, -30, 0, -40).rot("armL", 14, -80, 0, 15, Ease.OUT_BACK).rot("armL", 22, -80, 0, 15).rot("armL", 30, -40, 0, -12)
            .rot("foreR", 0, -80, 0, 0).rot("foreR", 14, -100, 0, 0).rot("foreR", 30, -80, 0, 0)
            .rot("foreL", 0, -80, 0, 0).rot("foreL", 14, -100, 0, 0).rot("foreL", 30, -80, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 12, 0, 0).rot("head", 14, 0, 0, 0).rot("head", 30, 0, 0, 0)
            .scl("torso", 0, 1, 1, 1).scl("torso", 14, 1.1f, 0.95f, 1.1f).scl("torso", 30, 1, 1, 1);
        // FAST PUNCH: jab-jab with the torso twisting
        m.clip("fast_punch", 28)
            .rot("torso", 0, 0, 0, 0).rot("torso", 5, 0, 25, 0).rot("torso", 8, 0, -22, 0, Ease.IN_CUBIC).rot("torso", 13, 0, 25, 0).rot("torso", 16, 0, -22, 0, Ease.IN_CUBIC).rot("torso", 28, 0, 0, 0)
            .rot("armR", 0, -35, 0, 12).rot("armR", 5, -30, 0, 30).rot("armR", 8, -95, 0, -5, Ease.IN_CUBIC).rot("armR", 12, -40, 0, 20).rot("armR", 28, -35, 0, 12)
            .rot("armL", 0, -40, 0, -12).rot("armL", 11, -30, 0, -30).rot("armL", 15, -95, 0, 5, Ease.IN_CUBIC).rot("armL", 20, -40, 0, -20).rot("armL", 28, -40, 0, -12)
            .rot("foreR", 0, -80, 0, 0).rot("foreR", 5, -110, 0, 0).rot("foreR", 8, -5, 0, 0, Ease.IN_CUBIC).rot("foreR", 12, -90, 0, 0).rot("foreR", 28, -80, 0, 0)
            .rot("foreL", 0, -80, 0, 0).rot("foreL", 11, -110, 0, 0).rot("foreL", 15, -5, 0, 0, Ease.IN_CUBIC).rot("foreL", 20, -90, 0, 0).rot("foreL", 28, -80, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, 2).pos("body", 15, 0, 0, 2.5f).pos("body", 28, 0, 0, 0);
        // STRONG PUNCH: huge wind-up haymaker, step in, overhand
        m.clip("strong_punch", 34)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -10, 55, 0).rot("torso", 17, 25, -50, 0, Ease.IN_CUBIC).rot("torso", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -2, -3).pos("body", 17, 0, -1, 6, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0)
            .rot("armR", 0, -35, 0, 12).rot("armR", 12, 30, 40, 55).rot("armR", 17, -120, -20, -10, Ease.IN_CUBIC).rot("armR", 24, -110, -20, -10).rot("armR", 34, -35, 0, 12)
            .rot("foreR", 0, -80, 0, 0).rot("foreR", 12, -140, 0, 0).rot("foreR", 17, -3, 0, 0, Ease.IN_CUBIC).rot("foreR", 34, -80, 0, 0)
            .rot("armL", 0, -40, 0, -12).rot("armL", 12, -80, 0, -20).rot("armL", 34, -40, 0, -12)
            .rot("legR", 0, 0, 0, 0).rot("legR", 12, 15, 0, 0).rot("legR", 17, -35, 0, 0).rot("legR", 34, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 12, -15, 0, 0).rot("legL", 17, 25, 0, 0).rot("legL", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 10, -30, 0).rot("head", 17, 5, 25, 0).rot("head", 34, 0, 0, 0);
        U.hurt(m, "torso");
        m.clip("death", 34)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -12, 8, 10).rot("torso", 26, -35, 0, 20, Ease.OUT_BOUNCE).rot("torso", 34, -35, 0, 20)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, -6, -2, Ease.IN_QUAD).pos("body", 34, 0, -8, -3, Ease.OUT_BOUNCE)
            .rot("armR", 0, -35, 0, 12).rot("armR", 20, 30, 0, 70).rot("armL", 0, -40, 0, -12).rot("armL", 20, 20, 0, -70)
            .rot("head", 0, 0, 0, 0).rot("head", 20, 30, 25, 15).rot("legR", 0, 0, 0, 0).rot("legR", 24, -50, 0, 10).rot("legL", 0, 0, 0, 0).rot("legL", 24, -70, 0, -10);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_punch_construct", "Punch Construct", "ボクシングロボ")
            .game(2, 1).size(0.9f, 1.9f).hp(55).speed(0.22f).style(Style.METAL).egg(0x7a8590, 0xc83a3a)
            .model(PunchConstruct::model)
            .spawn(6, 1, 2, MobDef.Habitat.CAVE)
            .pattern(Pattern.cycle("ready", "fast_punch", "strong_punch"))
            .onSpawn(s -> s.setCount("artifact", 1))
            .onTick(U::artifact)
            .move(Move.of("ready", 30).intent(Intent.DEFEND).noTarget()
                .sound(12, SoundEvents.ANVIL_PLACE, 0.8f, 1.4f)
                .at(14, Fx.block(10)))
            .move(Move.of("fast_punch", 28).intent(Intent.ATTACK_DEBUFF, 5, 2).reach(2.4f)
                .sound(7, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.5f)
                .at(8, U.hit(5, 2.8, 70, v -> Fx.frail(v, 5))).at(15, U.hit(5, 2.8, 70, v -> Fx.frail(v, 5))))
            .move(Move.of("strong_punch", 34).intent(Intent.ATTACK, 14, 1).reach(2.6f)
                .sound(12, SoundEvents.IRON_GOLEM_ATTACK, 1.2f, 0.7f)
                .at(17, (s, t) -> { Fx.meleeNow(s, 14, 3.0, 90, v -> Fx.knock(s, v, 0.8)); Fx.particles(s, ParticleTypes.CRIT, 12, 0.6); }));
    }
}
