package com.spiremod.mobs.s2og2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth elite Byrdonis (82 HP). A terror bird. Alternates Swoop (17) and Peck (3x3).
 * Territorial: at the end of each of its turns it gains 1 Strength.
 */
public final class Byrdonis {
    private Byrdonis() {}

    static final int FEA = 0x3f6a52, FEA_D = 0x274236, FEA_L = 0x5c8c6a, BELLY = 0xd2c294, BEAK = 0xe3a52e, BEAK_D = 0x6a4a1c, LEG = 0xc79a4a, CREST = 0xd2432f, EYE = 0xff3a2a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_byrdonis").scale(1.1f);
        // ---- legs (digitigrade): thigh -> shin -> foot with 3 toes + talon
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part leg = m.add("leg" + n).at(s * 4.5f, 24, -1);
            leg.box(-3, -8, -3, 6, 9, 7).color(FEA).grad(FEA_L, FEA_D).bevel(0.15f).noise(0.12f);
            Part shin = leg.add("shin" + n).at(0, -8, 3).rot(-35, 0, 0);
            shin.box(-1.5f, -9, -1.5f, 3, 9, 3).color(LEG).noise(0.1f).band(2, 1, BEAK_D).band(5, 1, BEAK_D);
            Part foot = shin.add("foot" + n).at(0, -9, 0).rot(30, 0, 0);
            foot.box(-1.5f, -1.5f, -1.5f, 3, 2, 3).color(LEG);
            for (int t = -1; t <= 1; t++) {
                foot.box(t * 2 - 0.5f, -1.5f, 1, 1, 1, 6).color(LEG).flat();
                foot.box(t * 2 - 0.5f, -1.5f, 6.5f, 1, 1, 2).color(0x2a2418).flat();   // claws
            }
            foot.box(-0.5f, -1.5f, -4, 1, 1, 3).color(LEG).flat();
        }
        // ---- body (horizontal)
        Part body = m.add("body").at(0, 24, 0);
        body.box(-6, -2, -9, 12, 12, 18).color(FEA).grad(FEA_L, FEA_D).bevel(0.12f).noise(0.12f)
            .paint(Face.FRONT, 2, 0, 8, 6, BELLY);
        body.box(-5, -2.5f, -8, 10, 3, 16).color(BELLY).grad(BELLY, 0xa8986a).noise(0.12f).inflate(0.15f);
        body.box(-6.5f, 5, -8, 13, 5, 14).color(FEA_D).grad(FEA, FEA_D).noise(0.15f).inflate(0.2f);   // dark back plumage
        // ruffle feathers on the back
        for (int i = 0; i < 4; i++) body.box(-4.5f, 9.5f, -8 + i * 4, 9, 2, 3).color(FEA_L).bevel(0.15f).noise(0.2f);
        // ---- tail feather fan
        Part tail = body.add("tail").at(0, 6, -9);
        for (int i = -2; i <= 2; i++) {
            Part f = tail.add("tf" + i).at(i * 2f, 0, 0).rot(-15 + Math.abs(i) * 4, i * 18, i * 8);
            f.box(-2.5f, 0, -10, 5, 1, 10).color(i % 2 == 0 ? FEA_D : FEA).noise(0.12f).inflate(0.1f);
            f.box(-2.5f, 0, -12, 5, 1, 2).color(CREST).flat();
        }
        // ---- neck + head
        Part neck = body.add("neck").at(0, 7, 6).rot(-30, 0, 0);
        neck.box(-3, 0, -3, 6, 9, 6).color(FEA).grad(FEA_L, FEA).bevel(0.12f).noise(0.12f);
        neck.box(-3.5f, -1, -3.5f, 7, 4, 7).color(BELLY).noise(0.15f).inflate(0.1f);                    // ruff
        Part neck2 = neck.add("neck2").at(0, 9, 0).rot(35, 0, 0);
        neck2.box(-2.5f, 0, -2.5f, 5, 6, 5).color(FEA).bevel(0.12f).noise(0.12f);
        Part head = neck2.add("head").at(0, 6, 0).rot(20, 0, 0);
        head.box(-3.5f, -1, -3.5f, 7, 8, 10).color(FEA).grad(FEA_L, FEA).bevel(0.15f).noise(0.1f);
        head.box(-3.7f, 0, -3.5f, 7, 2, 10).color(BELLY).flat().noise(0.08f);                          // pale cheek stripe
        for (int s = -1; s <= 1; s += 2) {
            head.box(s * 3.55f - 0.5f, 3, 2, 1, 2, 2).color(0x15100a).glow(s < 0 ? Face.RIGHT : Face.LEFT, 0, 0, 1, 1, EYE).flat();
            head.box(s * 3.8f - 0.6f, 5, 1, 1, 1, 4).color(FEA_D).flat();                              // angry brow
        }
        // crest
        for (int i = 0; i < 4; i++) {
            Part c = head.add("crest" + i).at(0, 7, -2 + i * 1.5f).rot(-10 - i * 10, 0, 0);
            c.box(-0.5f, 0, -1, 1, 4 + (i % 2) * 2, 2).color(i % 2 == 0 ? CREST : 0xea6a3a).flat();
        }
        // beak: huge hooked upper + lower
        Part beakU = head.add("beakU").at(0, 3, 6);
        beakU.box(-2, -1, 0, 4, 4, 7).color(BEAK).grad(0xf2c24a, BEAK).bevel(0.12f).noise(0.06f);
        beakU.box(-1.5f, -1, 7, 3, 3, 3).color(BEAK).bevel(0.12f);
        beakU.box(-1, -3, 8, 2, 3, 2).color(BEAK_D).flat();                                            // hooked tip
        Part beakL = head.add("beakL").at(0, 2, 6);
        beakL.box(-1.5f, -2.5f, 0, 3, 2, 7).color(0xc78a22).bevel(0.12f);
        // ---- wings (small, folded)
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part w = body.add("wing" + n).at(s * 6.5f, 6, 3).rot(10, 0, s * 8);
            w.box(-1.5f, -10, -3, 3, 11, 7).color(FEA_D).grad(FEA, FEA_D).noise(0.14f).bevel(0.12f);
            for (int i = 0; i < 3; i++) w.box(-1.2f, -14 - i, -3 + i * 2.2f, 2, 5, 2).color(i == 1 ? CREST : FEA_L).flat();
        }
        m.look("head", 50, 25);
        m.walkBiped("legR", "legL", "wingR", "wingL", 32f, 14f);
        m.clip("idle", 40).loop()
            .rot("body", 0, 0, 0, 0).rot("body", 20, 2, 0, 0).rot("body", 40, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 14, -6, 8, 0).rot("neck", 30, 6, -8, 0).rot("neck", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, 8, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 20, 0, 0, 8).rot("wingR", 40, 0, 0, 0)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 20, 0, 0, -8).rot("wingL", 40, 0, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 28, 0, 0, 0).rot("beakL", 31, 12, 0, 0).rot("beakL", 34, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 20, 0, 8, 0).rot("tail", 40, 0, 0, 0);
        // SWOOP: rear up, wings flare + shriek, then drop and slash down with the whole body
        m.clip("swoop", 34)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -14, 0, 0).rot("body", 16, 12, 0, 0, Ease.IN_CUBIC).rot("body", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 3, -3).pos("body", 16, 0, -4, 8, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 12, -40, 0, 0).rot("neck", 16, 55, 0, 0, Ease.IN_CUBIC).rot("neck", 22, 45, 0, 0).rot("neck", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 16, 30, 0, 0, Ease.IN_CUBIC).rot("head", 34, 0, 0, 0)
            .rot("beakU", 0, 0, 0, 0).rot("beakU", 12, -18, 0, 0).rot("beakU", 16, 0, 0, 0).rot("beakU", 34, 0, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 12, 40, 0, 0).rot("beakL", 16, 0, 0, 0).rot("beakL", 34, 0, 0, 0)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 10, 0, 0, 100, Ease.OUT_BACK).rot("wingR", 16, 20, 0, 70).rot("wingR", 34, 0, 0, 0)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 10, 0, 0, -100, Ease.OUT_BACK).rot("wingL", 16, 20, 0, -70).rot("wingL", 34, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 12, 22, 0, 0).rot("legR", 16, -20, 0, 0).rot("legR", 34, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 12, 22, 0, 0).rot("legL", 16, -20, 0, 0).rot("legL", 34, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 12, -25, 0, 0).rot("tail", 16, 20, 0, 0).rot("tail", 34, 0, 0, 0);
        // PECK x3: quick jackhammer dips at ticks 10, 17, 24
        Clip pk = m.clip("peck", 38)
            .rot("body", 0, 0, 0, 0).rot("body", 6, -8, 0, 0).rot("body", 36, 0, 0, 0)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 8, 0, 0, 35).rot("wingR", 30, 0, 0, 35).rot("wingR", 38, 0, 0, 0)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 8, 0, 0, -35).rot("wingL", 30, 0, 0, -35).rot("wingL", 38, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 6, -25, 0, 0);
        pk.rot("head", 0, 0, 0, 0).rot("head", 6, -20, 0, 0).rot("beakL", 0, 0, 0, 0).rot("beakL", 6, 25, 0, 0);
        int[] hit = {10, 17, 24};
        for (int h : hit) {
            pk.rot("neck", h - 3, -28, 0, 0).rot("neck", h, 58, 0, 0, Ease.IN_CUBIC).rot("neck", h + 2, 40, 0, 0)
              .rot("head", h - 3, -20, 0, 0).rot("head", h, 40, 0, 0, Ease.IN_CUBIC)
              .rot("beakL", h - 3, 25, 0, 0).rot("beakL", h, 0, 0, 0, Ease.IN_CUBIC)
              .pos("body", h - 3, 0, 1, -2).pos("body", h, 0, -2, 4, Ease.IN_CUBIC);
        }
        pk.rot("neck", 38, 0, 0, 0).rot("head", 38, 0, 0, 0).rot("beakL", 38, 0, 0, 0).pos("body", 0, 0, 0, 0).pos("body", 38, 0, 0, 0);
        m.clip("hurt", 10).rot("neck", 0, 0, 0, 0).rot("neck", 3, -28, 0, 8).rot("neck", 10, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 3, -10, 0, 4).rot("body", 10, 0, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 3, 40, 0, 0).rot("beakL", 10, 0, 0, 0)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 3, 0, 0, 60).rot("wingR", 10, 0, 0, 0)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 3, 0, 0, -60).rot("wingL", 10, 0, 0, 0);
        m.clip("death", 32)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -10, 0, 8).rot("body", 32, 0, 0, 88, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 32, 14, -16, 0, Ease.OUT_BOUNCE)
            .rot("neck", 0, 0, 0, 0).rot("neck", 12, -40, 20, 0).rot("neck", 32, 20, 40, 10)
            .rot("head", 0, 0, 0, 0).rot("head", 32, 30, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 14, 45, 0, 0).rot("beakL", 32, 30, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 20, 35, 0, 0).rot("legR", 32, 55, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 20, 50, 0, 0).rot("legL", 32, 20, 0, 0)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 14, 0, 0, 90).rot("wingR", 32, 0, 0, 40)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 14, 0, 0, -90).rot("wingL", 32, 0, 0, -40);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_byrdonis", "Byrdonis", "ビャードニス")
            .game(2, 1).elite().size(1.3f, 2.5f).hp(82).speed(0.3f).style(Style.BIRD).egg(0x3f6a52, 0xe3a52e).kbResist(0.4f)
            .model(Byrdonis::model)
            .spawn(1, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("swoop", "peck"))
            .move(Move.of("swoop", 34).intent(Intent.ATTACK, 17, 1).reach(3.2f)
                .sound(8, SoundEvents.PARROT_IMITATE_RAVAGER, 1.4f, 0.6f)
                .at(16, (s, t) -> { Fx.melee(17, 3.4, 90).run(s, t); Fx.particles(s, ParticleTypes.SWEEP_ATTACK, 3, 0.6); }))
            .move(Move.of("peck", 38).intent(Intent.ATTACK, 3, 3).reach(3.2f)
                .sound(4, SoundEvents.PARROT_IMITATE_VINDICATOR, 1f, 1.4f)
                .atAll(new int[]{10, 17, 24}, (s, t) -> { Fx.meleeNow(s, 3, 3.4, 80, null); Fx.sound(s, SoundEvents.WOOD_HIT, 1f, 1.5f); }))
            .onMoveEnd(s -> { s.addStrength(1); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 4, 0.5); }); // Territorial
    }
}
