package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Fossil Stalker (51-53 HP). Suck 3: gains 3 Strength each time it lands an attack.
 * Latch (12) first, then equal chance Latch / Tackle (9 + Frail 1) / Lash (3x2).
 */
public final class FossilStalker {
    private FossilStalker() {}
    static final int BONE = 0xd9cfae, BONE_D = 0x9c9070, BONE_L = 0xf0e8cc, FLESH = 0x6a3a44, GLOW = 0xff9a40;
    static final String[] IDS = {"latch", "tackle", "lash"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_fossil_stalker");
        float[][] lp = {{-3.5f, 6}, {3.5f, 6}, {-3.5f, -6}, {3.5f, -6}};
        String[] ln = {"legFR", "legFL", "legBR", "legBL"};
        for (int i = 0; i < 4; i++) {
            Part leg = m.add(ln[i]).at(lp[i][0], 10, lp[i][1]);
            leg.box(-1.5f, -5, -1.5f, 3, 5, 3).color(BONE).grad(BONE_L, BONE_D).noise(0.12f).bevel(0.2f);
            Part shin = leg.add(ln[i] + "2").at(0, -5, 0);
            shin.box(-1, -5, -1, 2, 5, 2).color(BONE_D).noise(0.12f);
            shin.box(-2, -5, -2.5f, 4, 1, 5).color(BONE).bevel(0.2f);
            shin.box(-1.5f, -5, 2.5f, 1, 1, 2).color(BONE_L).flat(); shin.box(0.5f, -5, 2.5f, 1, 1, 2).color(BONE_L).flat(); // claws
        }
        Part body = m.add("body").at(0, 10, 0);
        body.box(-4, 0, -8, 8, 3, 16).color(BONE_D).noise(0.15f);                                     // spine
        body.box(-3, -2, -7, 6, 2, 14).color(FLESH).noise(0.15f);                                      // hollow belly
        for (int i = 0; i < 5; i++) {                                                                  // ribs
            body.box(-5, 0, -7 + i * 3, 1, 5, 1).color(BONE).bevel(0.15f);
            body.box(4, 0, -7 + i * 3, 1, 5, 1).color(BONE).bevel(0.15f);
            body.box(-5, 4, -7 + i * 3, 10, 1, 1).color(BONE_L).noise(0.1f);
            body.box(-4, -4, -7 + i * 3, 1, 4, 1).color(BONE_D);
            body.box(3, -4, -7 + i * 3, 1, 4, 1).color(BONE_D);
        }
        for (int i = 0; i < 6; i++) body.box(-1, 3, -7 + i * 2.8f, 2, 2, 2).color(BONE_L).noise(0.1f).bevel(0.2f);  // vertebra spikes
        Part neck = body.add("neck").at(0, 2, 8);
        neck.box(-2, -1, 0, 4, 4, 5).color(BONE).grad(BONE_L, BONE_D).bevel(0.2f);
        Part neck2 = neck.add("neck2").at(0, 1, 5);
        neck2.box(-1.5f, -1, 0, 3, 3, 4).color(BONE_D).noise(0.12f);
        Part head = neck2.add("head").at(0, 0, 4);
        head.box(-3, -2, 0, 6, 5, 6).color(BONE).grad(BONE_L, BONE_D).noise(0.12f).bevel(0.3f);        // skull
        head.box(-2, -1, 6, 4, 3, 2).color(BONE_D).bevel(0.2f);
        head.box(-2.5f, 1, 1, 1, 1, 3).color(0x1a1218).glow(Face.FRONT, 0, 0, 1, 1, GLOW);               // eye sockets
        head.box(1.5f, 1, 1, 1, 1, 3).color(0x1a1218).glow(Face.FRONT, 0, 0, 1, 1, GLOW);
        for (int s = -1; s <= 1; s += 2) head.box(s * 3 - 0.5f, 3, 0, 1, 3, 2).color(BONE_L).bevel(0.15f);          // horns
        Part jaw = head.add("jaw").at(0, -1, 1);
        jaw.box(-2.5f, -2, 0, 5, 2, 6).color(BONE_D).bevel(0.2f);
        jaw.box(-2.5f, 0, 1, 5, 1, 5).color(FLESH).flat();
        for (int i = 0; i < 4; i++) jaw.box(-2 + i * 1.3f, 0.5f, 5.2f, 1, 2, 1).color(BONE_L).flat();   // teeth
        Part sucker = jaw.add("sucker").at(0, -1, 6);                                                    // leech-like latching mouth
        sucker.box(-1.5f, -1.5f, 0, 3, 3, 2).color(FLESH).grad(0xa0505a, FLESH).bevel(0.3f).glow(Face.FRONT, 1, 1, 1, 1, 0xff5060);
        Part tail = body.add("tail").at(0, 1, -8);
        tail.box(-1.5f, -1, -5, 3, 3, 5).color(BONE).bevel(0.2f);
        Part tail2 = tail.add("tail2").at(0, 0, -5);
        tail2.box(-1, -1, -5, 2, 2, 5).color(BONE_D).noise(0.12f);
        tail2.box(-2, -2, -8, 4, 4, 3).color(BONE_L).bevel(0.3f);
        m.look("neck", 35, 20);
        m.walkQuad("legFR", "legBL", "legFL", "legBR", 38);
        m.clip("idle", 44).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 22, 0, -0.5f, 0).pos("body", 44, 0, 0, 0)
            .rot("neck", 0, 4, 0, 0).rot("neck", 22, -4, 6, 0).rot("neck", 44, 4, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 22, 6, -10, 0).rot("head", 44, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 22, 10, 0, 0).rot("jaw", 44, 0, 0, 0)
            .rot("tail", 0, 0, 10, 0).rot("tail", 22, 0, -10, 0).rot("tail", 44, 0, 10, 0)
            .rot("tail2", 0, 0, -15, 0).rot("tail2", 22, 0, 15, 0).rot("tail2", 44, 0, -15, 0)
            .rot("sucker", 0, 0, 0, 0).rot("sucker", 22, 0, 0, 20).rot("sucker", 44, 0, 0, 0);
        // LATCH: crouch, pounce at the victim, jaws + sucker clamp on, hold, shake
        m.clip("latch", 38)
            .rot("body", 0, 0, 0, 0).rot("body", 9, 12, 0, 0).rot("body", 14, -18, 0, 0, Ease.IN_CUBIC).rot("body", 30, -10, 0, 0).rot("body", 38, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, -3, -2).pos("body", 14, 0, 2, 6, Ease.IN_CUBIC).pos("body", 22, 0, 0, 5).pos("body", 38, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 9, 20, 0, 0).rot("neck", 14, -20, 0, 0, Ease.IN_CUBIC).rot("neck", 24, -10, 0, 0).rot("neck", 38, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 10, 0, 0).rot("head", 20, 8, 12, 0).rot("head", 24, 8, -12, 0).rot("head", 28, 8, 12, 0).rot("head", 38, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 9, 55, 0, 0, Ease.OUT_BACK).rot("jaw", 14, 3, 0, 0, Ease.IN_CUBIC).rot("jaw", 30, 5, 0, 0).rot("jaw", 38, 0, 0, 0)
            .scl("sucker", 0, 1, 1, 1).scl("sucker", 14, 1.5f, 1.5f, 1.5f).scl("sucker", 30, 1.3f, 1.3f, 1.3f).scl("sucker", 38, 1, 1, 1)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 9, 30, 0, 0).rot("legFR", 14, -50, 0, 0).rot("legFR", 38, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 9, 30, 0, 0).rot("legFL", 14, -50, 0, 0).rot("legFL", 38, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 9, -20, 20, 0).rot("tail", 14, 15, -20, 0).rot("tail", 38, 0, 0, 0);
        // TACKLE: lower the skull and shoulder-barge
        m.clip("tackle", 32)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -6, 0, 0).rot("body", 15, 10, 0, 0, Ease.IN_CUBIC).rot("body", 32, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -1, -4).pos("body", 15, 0, -1, 6, Ease.IN_CUBIC).pos("body", 22, 0, 0, 5).pos("body", 32, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 10, -25, 0, 0).rot("neck", 15, 35, 0, 0, Ease.IN_CUBIC).rot("neck", 32, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 15, 20, 0, 0).rot("head", 32, 0, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 10, -30, 0, 0).rot("legFR", 15, 40, 0, 0).rot("legFR", 32, 0, 0, 0)
            .rot("legBL", 0, 0, 0, 0).rot("legBL", 10, 30, 0, 0).rot("legBL", 15, -40, 0, 0).rot("legBL", 32, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 15, 0, 30, 0).rot("tail", 32, 0, 0, 0);
        // LASH: tail whips side to side twice (3x2)
        m.clip("lash", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 6, 0, 30, 0).rot("body", 10, 0, -25, 0, Ease.IN_CUBIC).rot("body", 15, 0, 30, 0).rot("body", 19, 0, -25, 0, Ease.IN_CUBIC).rot("body", 30, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 6, 0, -60, 0).rot("tail", 10, 0, 60, 0, Ease.IN_CUBIC).rot("tail", 15, 0, -60, 0).rot("tail", 19, 0, 60, 0, Ease.IN_CUBIC).rot("tail", 30, 0, 0, 0)
            .rot("tail2", 0, 0, 0, 0).rot("tail2", 6, 0, -50, 0).rot("tail2", 10, 0, 70, 0).rot("tail2", 15, 0, -50, 0).rot("tail2", 19, 0, 70, 0).rot("tail2", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 30, 0, 0).rot("jaw", 19, 30, 0, 0).rot("jaw", 30, 0, 0, 0);
        U.hurt(m, "body");
        m.clip("death", 36)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -8, 0, 10).rot("body", 28, 0, 0, 25, Ease.OUT_BOUNCE).rot("body", 36, 0, 0, 25)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -3, 0, Ease.IN_QUAD).pos("body", 30, 0, -7, 0, Ease.OUT_BOUNCE)
            .rot("neck", 0, 0, 0, 0).rot("neck", 20, 40, 10, 0).rot("head", 0, 0, 0, 0).rot("head", 24, 25, 0, 15)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 24, 50, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 20, 0, 0, -50).rot("legFL", 0, 0, 0, 0).rot("legFL", 20, 0, 0, 50)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 20, 0, 0, -50).rot("legBL", 0, 0, 0, 0).rot("legBL", 20, 0, 0, 50);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_fossil_stalker", "Fossil Stalker", "化石のストーカー")
            .game(2, 1).size(1.1f, 1.5f).hp(52).speed(0.27f).style(Style.SKELETON).egg(0xd9cfae, 0xff9a40)
            .model(FossilStalker::model)
            .spawn(5, 1, 1, MobDef.Habitat.CAVE)
            .pattern(Pattern.first("latch", s -> IDS[s.rand().nextInt(3)]))
            .move(Move.of("latch", 38).intent(Intent.ATTACK, 12, 1).reach(2.6f)
                .sound(9, SoundEvents.SKELETON_AMBIENT, 1f, 0.5f)
                .at(15, bite(12, 2.8, 80, null)))
            .move(Move.of("tackle", 32).intent(Intent.ATTACK_DEBUFF, 9, 1).reach(2.6f)
                .sound(10, SoundEvents.RAVAGER_STEP, 1f, 0.8f)
                .at(15, bite(9, 3.0, 90, v -> Fx.frail(v, 5))))
            .move(Move.of("lash", 30).intent(Intent.ATTACK, 3, 2).reach(3.0f)
                .sound(7, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.0f)
                .at(10, bite(3, 3.6, 160, null)).at(19, bite(3, 3.6, 160, null)));
    }

    /** one melee hit; Suck: each landed hit grants +3 Strength */
    static Move.Action bite(float dmg, double range, double arc, java.util.function.Consumer<net.minecraft.world.entity.LivingEntity> extra) {
        return (s, t) -> {
            if (t != null) t.invulnerableTime = 0;
            if (Fx.meleeNow(s, dmg, range, arc, extra) > 0) { s.addStrength(3); Fx.particles(s, ParticleTypes.SOUL, 6, 0.5); }
        };
    }
}
