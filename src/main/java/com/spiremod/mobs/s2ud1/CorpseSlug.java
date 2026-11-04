package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Corpse Slug (25-27 HP). Ravenous 4: when an ally dies it eats the corpse, is Stunned and gains 4 Strength.
 * Fixed cycle Whip Slap (3x2) -> Glomp (8) -> Goop (Frail 2); each slug starts at a random point of the cycle (staggered).
 */
public final class CorpseSlug {
    private CorpseSlug() {}
    static final int SKIN = 0xb7a98a, SKIN_D = 0x7d7058, BELLY = 0xd6c9a0, SLIME = 0x8aa84a, MOUTH = 0x5a1f2a;
    static final String[] CYCLE = {"whip_slap", "glomp", "goop"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_corpse_slug");
        Part body = m.add("body").at(0, 0, 0);
        body.box(-4, 0, -9, 8, 5, 13).color(SKIN).grad(0xc9bd9c, SKIN_D).noise(0.15f).bevel(0.2f);       // long mantle
        body.box(-3, 5, -8, 6, 2, 10).color(SKIN_D).noise(0.2f).bevel(0.2f);                       // humped back
        body.box(-4.5f, 0, -8, 9, 1, 15).color(BELLY).noise(0.1f);                                       // foot
        body.box(-4.2f, 1, -9.2f, 8, 2, 2).color(SLIME).flat().noise(0.1f);                               // slimy rear
        for (int i = 0; i < 4; i++) body.box(-2.5f, 7, -6 + i * 2.5f, 5, 1, 1).color(0xa6987a).noise(0.12f); // ridges
        Part head = body.add("head").at(0, 2, 4);
        head.box(-3, 0, 0, 6, 5, 5).color(SKIN).grad(0xc9bd9c, SKIN_D).noise(0.12f).bevel(0.2f);
        head.box(-2, 1, 4.7f, 5, 2, 1).color(MOUTH).flat();                                       // maw
        Part jaw = head.add("jaw").at(0, 1.5f, 5);
        jaw.box(-2, -2, -0.5f, 4, 2, 2).color(SKIN_D).bevel(0.15f);
        for (int s = -1; s <= 1; s += 2) {
            Part es = head.add(s < 0 ? "eyeR" : "eyeL").at(s * 1.8f, 5, 2).rot(-15, 0, s * -12);
            es.box(-0.5f, 0, -0.5f, 1, 4, 1).color(SKIN);
            es.box(-1, 4, -1, 2, 2, 2).color(0xe8e0c0).glow(Face.FRONT, 0, 0, 1, 1, 0xffd040).bevel(0.1f);
            Part wh = head.add(s < 0 ? "whipR" : "whipL").at(s * 3, 2.5f, 3).rot(-30, 0, s * 40);
            wh.box(-0.5f, -0.5f, 0, 1, 1, 5).color(SKIN_D);
            Part wh2 = wh.add(s < 0 ? "whipR2" : "whipL2").at(0, 0, 5).rot(10, 0, 0);
            wh2.box(-0.5f, -0.5f, 0, 1, 1, 5).color(SKIN_D);
            wh2.box(-1, -1, 4.5f, 2, 2, 2).color(SLIME).bevel(0.1f);
        }
        Part goo = m.add("goop").at(0, 3, 12);
        goo.cbox(-2, 4, 4, 4).color(SLIME).flat().noise(0.1f).glow(Face.FRONT, 1, 1, 2, 2, 0xd4f060);
        m.proc((r, c) -> r.show("goop", "goop".equals(c.move) && c.moveT > 13 && c.moveT < 26));
        m.look("head", 40, 25);
        m.clip("walk", 24).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 6, 1, 0.94f, 1.1f).scl("body", 12, 1, 1, 1).scl("body", 18, 1, 0.94f, 1.1f).scl("body", 24, 1, 1, 1)
            .pos("head", 0, 0, 0, 0).pos("head", 6, 0, 0.8f, 1).pos("head", 12, 0, 0, 0).pos("head", 18, 0, 0.8f, 1).pos("head", 24, 0, 0, 0);
        m.clip("idle", 40).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 20, 1.03f, 1.06f, 1).scl("body", 40, 1, 1, 1)
            .rot("eyeR", 0, 0, 0, 0).rot("eyeR", 14, 10, 0, 8).rot("eyeR", 28, -6, 0, -4).rot("eyeR", 40, 0, 0, 0)
            .rot("eyeL", 0, 0, 0, 0).rot("eyeL", 18, 8, 0, -8).rot("eyeL", 32, -8, 0, 5).rot("eyeL", 40, 0, 0, 0)
            .rot("whipR", 0, 0, 0, 0).rot("whipR", 20, 0, 8, 0).rot("whipR", 40, 0, 0, 0)
            .rot("whipL", 0, 0, 0, 0).rot("whipL", 20, 0, -8, 0).rot("whipL", 40, 0, 0, 0);
        // WHIP SLAP: both feelers rear back then lash forward alternately (two hits)
        m.clip("whip_slap", 28)
            .rot("body", 0, 0, 0, 0).rot("body", 5, -14, 0, 0).rot("body", 24, 0, 0, 0).pos("head", 0, 0, 0, 0).pos("head", 5, 0, 1.5f, -1.5f).pos("head", 24, 0, 0, 0)
            .rot("whipR", 0, 0, 0, 0).rot("whipR", 6, -60, 0, -40).rot("whipR", 9, 35, -25, 25, Ease.IN_CUBIC).rot("whipR", 15, 25, 0, 10).rot("whipR", 24, 0, 0, 0)
            .rot("whipR2", 0, 0, 0, 0).rot("whipR2", 6, -35, 0, 0).rot("whipR2", 9, 30, 0, 0, Ease.IN_CUBIC).rot("whipR2", 24, 0, 0, 0)
            .rot("whipL", 0, 0, 0, 0).rot("whipL", 10, -60, 0, 40).rot("whipL", 14, 35, 25, -25, Ease.IN_CUBIC).rot("whipL", 20, 25, 0, -10).rot("whipL", 28, 0, 0, 0)
            .rot("whipL2", 0, 0, 0, 0).rot("whipL2", 10, -35, 0, 0).rot("whipL2", 14, 30, 0, 0, Ease.IN_CUBIC).rot("whipL2", 28, 0, 0, 0);
        // GLOMP: rear up, then slam down on the target with the mouth wide
        m.clip("glomp", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -32, 0, 0, Ease.OUT_QUAD).rot("body", 15, 20, 0, 0, Ease.IN_CUBIC).rot("body", 21, 12, 0, 0).rot("body", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -2).pos("body", 15, 0, 0, 5, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 10, 1, 1.1f, 0.9f).scl("body", 15, 1.1f, 0.85f, 1.15f).scl("body", 30, 1, 1, 1)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 50, 0, 0).rot("jaw", 15, 5, 0, 0, Ease.IN_CUBIC).rot("jaw", 21, 25, 0, 0).rot("jaw", 30, 0, 0, 0)
            .rot("whipR", 0, 0, 0, 0).rot("whipR", 10, -40, 0, -20).rot("whipR", 30, 0, 0, 0).rot("whipL", 0, 0, 0, 0).rot("whipL", 10, -40, 0, 20).rot("whipL", 30, 0, 0, 0);
        // GOOP: lean back, swell, spit a glob
        m.clip("goop", 32)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -14, 0, 0).rot("body", 16, 8, 0, 0, Ease.IN_CUBIC).rot("body", 32, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 12, 1.15f, 1.15f, 0.95f).scl("body", 18, 0.92f, 0.95f, 1.05f).scl("body", 32, 1, 1, 1)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 20, 0, 0).rot("jaw", 15, 55, 0, 0, Ease.OUT_BACK).rot("jaw", 26, 40, 0, 0).rot("jaw", 32, 0, 0, 0)
            .pos("goop", 14, 0, 0, -3).pos("goop", 16, 0, 0, 0).pos("goop", 26, 0, -1, 8)
            .scl("goop", 14, 0.3f, 0.3f, 0.3f).scl("goop", 18, 1.2f, 1.2f, 1.2f, Ease.OUT_BACK).scl("goop", 26, 1.5f, 1.5f, 1.5f);
        U.hurt(m, "body");
        m.clip("death", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -15, 0, 8).rot("body", 24, 0, 0, 90, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 24, 4, 0, 0).scl("body", 30, 1.2f, 0.7f, 1.1f)
            .rot("eyeR", 0, 0, 0, 0).rot("eyeR", 20, 40, 0, 40).rot("eyeL", 0, 0, 0, 0).rot("eyeL", 20, 40, 0, -40)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 45, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_corpse_slug", "Corpse Slug", "屍ナメクジ")
            .game(2, 1).size(0.9f, 0.7f).hp(26).speed(0.2f).style(Style.SLIME).egg(0xb7a98a, 0x8aa84a)
            .model(CorpseSlug::model)
            .spawn(7, 2, 3, MobDef.Habitat.SWAMP)
            .pattern(s -> CYCLE[(s.getTurn() + s.count("off")) % 3])
            .onSpawn(s -> s.setCount("off", s.rand().nextInt(3)))
            // Ravenous: when another creature dies nearby, eat it -> stunned, +4 Strength
            .onTick(s -> {
                if (s.tickCount % 4 != 0) return;
                int n = 0;
                for (SpireMob o : s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(24))) if (o != s && o.isAlive()) n++;
                int prev = s.count("allies");
                if (prev > 0 && n < prev) { s.stun(40); s.addStrength(4); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 8, 0.5); Fx.sound(s, SoundEvents.GENERIC_EAT, 1.2f, 0.6f); }
                s.setCount("allies", n);
            })
            .move(Move.of("whip_slap", 28).intent(Intent.ATTACK, 3, 2).reach(2.8f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 0.9f, 1.4f)
                .at(9, U.hit(3, 3.2, 90, null)).at(14, U.hit(3, 3.2, 90, null)))
            .move(Move.of("glomp", 30).intent(Intent.ATTACK, 8, 1).reach(2.2f)
                .sound(12, SoundEvents.SLIME_JUMP, 1f, 0.7f)
                .at(15, Fx.melee(8, 2.6, 80)))
            .move(Move.of("goop", 32).intent(Intent.DEBUFF).reach(8).ranged(8)
                .sound(14, SoundEvents.SLIME_SQUISH, 1.2f, 0.8f)
                .at(18, (s, t) -> { if (t != null) { Fx.frail(t, 6); Fx.particlesAt(s, ParticleTypes.ITEM_SLIME, t.position().add(0, 1, 0), 14, 0.3, 0.05); } }));
    }
}
