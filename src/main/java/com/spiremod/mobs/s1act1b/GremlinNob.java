package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 1 Elite Gremlin Nob: 82-86 HP. Turn 1 Bellow (Enrage: +2 Strength whenever the player plays a Skill).
 * Then 67% Bull Rush (14), 33% Skull Bash (6 + 2 Vulnerable); never Bull Rush three times in a row.
 * Mod: the Skill trigger is approximated by "every 3rd hit taken gives +2 Strength".
 */
public final class GremlinNob {
    private GremlinNob() {}
    static final int SKIN = 0xc0202c, SKIN_D = 0x7a1018, CLOTH = 0x4a0e18, BONE = 0xe8e0c8, EYE = 0xffe040, GOLD = 0xe8c040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("gremlin_nob").scale(1.5f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3.5f, 8, 0);
            leg.box(-2.5f, -8, -2.5f, 5, 8, 5).color(SKIN).grad(SKIN, SKIN_D).noise(0.08f).bevel(0.12f);
            leg.box(-3, -8, -3, 6, 2, 7).color(SKIN_D).bevel(0.15f);
            leg.box(-2.5f, -7.5f, 3.5f, 1, 1, 1).color(BONE); leg.box(0.5f, -7.5f, 3.5f, 1, 1, 1).color(BONE);
        }
        Part body = m.add("body").at(0, 8, 0);
        body.box(-5, 0, -3, 10, 4, 6).color(CLOTH).grad(0x5a4560, CLOTH);
        body.box(-5.3f, -3, -3.3f, 10, 5, 6).color(CLOTH).grad(CLOTH, 0x241a28).inflate(0.3f).band(0, 1, BONE);   // loincloth
        Part torso = body.add("torso").at(0, 4, 0);
        torso.box(-7, 0, -4, 14, 9, 8).color(SKIN).grad(0xe04858, SKIN_D).bevel(0.12f).noise(0.08f)
            .paint(Face.FRONT, 2, 1, 4, 3, 0xd03040).paint(Face.FRONT, 8, 1, 4, 3, 0xd03040).paint(Face.FRONT, 6, 3, 2, 5, SKIN_D);   // pecs, abs line
        torso.box(-8, 8, -4.5f, 16, 2, 9).color(SKIN).grad(0xe04858, SKIN).bevel(0.1f);                               // trapezius / shoulder bar
        torso.box(-7.3f, 0.5f, -4.3f, 14, 2, 8).color(0x5a3c2a).flat().noise(0.05f).inflate(0.3f);                // belt strap
        torso.box(-0.5f, 4, -4.4f, 1, 5, 1).color(BONE).flat();                                                    // bone charm
        Part head = torso.add("head").at(0, 9, 0.5f);
        head.box(-4, 0, -4, 8, 7, 8).color(SKIN).grad(0xe04858, SKIN).bevel(0.15f).noise(0.07f)
            .paint(Face.FRONT, 0, 1, 8, 2, SKIN_D)                                                                 // brutal brow
            .glow(Face.FRONT, 1, 3, 2, 1, EYE).glow(Face.FRONT, 5, 3, 2, 1, EYE)
            .px(Face.FRONT, 2, 3, 0x200808).px(Face.FRONT, 5, 3, 0x200808);
        head.box(-1.5f, 1.5f, 4, 3, 2, 1).color(SKIN_D).bevel(0.2f);                                               // nose
        Part jaw = head.add("jaw").at(0, 1.5f, 0.5f);
        jaw.box(-3.5f, -2.5f, -3, 7, 3, 7).color(SKIN).grad(SKIN, SKIN_D).bevel(0.12f)
            .paint(Face.FRONT, 0, 0, 7, 1, 0x2a0a0a).px(Face.FRONT, 1, 0, BONE).px(Face.FRONT, 5, 0, BONE).px(Face.FRONT, 3, 0, BONE);
        jaw.box(-3, 0.5f, 3.2f, 1, 3, 1).color(BONE).grad(BONE, 0xb8a888).bevel(0.15f);                           // tusks
        jaw.box(2, 0.5f, 3.2f, 1, 3, 1).color(BONE).grad(BONE, 0xb8a888).bevel(0.15f);
        for (int s = -1; s <= 1; s += 2) {
            Part ear = head.add(s < 0 ? "earR" : "earL").at(s * 4, 4, 0).rot(0, 0, s * -30);
            ear.box(s < 0 ? -4 : 0, -1, -1, 4, 2, 2).color(SKIN).bevel(0.15f);
            Part horn = head.add(s < 0 ? "hornR" : "hornL").at(s * 2.5f, 7, -1).rot(0, 0, s * -25);
            horn.box(-1, 0, -1, 2, 6, 2).color(GOLD).grad(0xfff0a0, GOLD).bevel(0.2f); horn.box(-1, 6, -2, 2, 3, 2).color(GOLD).bevel(0.2f);
        }
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 9.5f, 8, 0);
            arm.box(-2.5f, -3, -2.5f, 5, 4, 5).color(SKIN).grad(0xe04858, SKIN).bevel(0.12f);                       // shoulder
            arm.box(-2, -9, -2, 4, 6, 4).color(SKIN).grad(SKIN, SKIN_D).bevel(0.1f).noise(0.08f);
            arm.box(-2.4f, -6.5f, -2.4f, 4, 2, 4).color(0x4a3a2a).flat().inflate(0.4f);                              // wrist strap
            arm.box(-0.5f, 1, -0.5f, 1, 2, 1).color(BONE).flat();                                                    // shoulder spike
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -9, 0);
            hand.box(-2.5f, -5, -2.5f, 5, 5, 5).color(SKIN).grad(SKIN, SKIN_D).bevel(0.12f).noise(0.08f);
            hand.box(-2.5f, -5.5f, 1.8f, 5, 1, 1).color(SKIN_D);                                                     // knuckle ridge
        }
        m.look("head", 45, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 35f, 30f);
        m.clip("idle", 40).loop()
            .rot("torso", 0, 3, 0, 0).rot("torso", 20, 6, 0, 0).rot("torso", 40, 3, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, -0.6f, 0).pos("body", 40, 0, 0, 0)
            .rot("head", 0, 8, 0, 0).rot("head", 14, 10, 6, 0).rot("head", 28, 8, -6, 0).rot("head", 40, 8, 0, 0)
            .rot("armR", 0, 10, 0, 10).rot("armR", 20, 14, 0, 14).rot("armR", 40, 10, 0, 10)
            .rot("armL", 0, 10, 0, -10).rot("armL", 20, 14, 0, -14).rot("armL", 40, 10, 0, -10)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 25, 0, 0, 0).rot("jaw", 28, 10, 0, 0).rot("jaw", 31, 0, 0, 0).rot("jaw", 40, 0, 0, 0);
        // BELLOW: hunch, then throw the chest out, head back, roar with fists clenched down and shuddering
        Clip b = m.clip("bellow", 44);
        K.r(b, "torso", 0, 0, 0, 0, 10, 28, 0, 0, 216, -22, 0, 0, 36, -18, 0, 0, 44, 0, 0, 0);
        K.r(b, "head", 0, 0, 0, 0, 10, 30, 0, 0, 216, -48, 0, 0, 36, -42, 0, 0, 44, 0, 0, 0);
        K.r(b, "jaw", 0, 0, 0, 0, 16, 0, 0, 0, 18, 45, 0, 0, 36, 45, 0, 0, 42, 0, 0, 0);
        K.r(b, "armR", 0, 0, 0, 0, 10, 20, 0, 25, 216, -35, 0, 35, 22, -30, 0, 30, 26, -35, 0, 38, 30, -30, 0, 30, 44, 0, 0, 0);
        K.r(b, "armL", 0, 0, 0, 0, 10, 20, 0, -25, 216, -35, 0, -35, 22, -30, 0, -30, 26, -35, 0, -38, 30, -30, 0, -30, 44, 0, 0, 0);
        K.p(b, "body", 0, 0, 0, 0, 10, 0, -2.5f, 0, 216, 0, 1, 0, 44, 0, 0, 0);
        K.r(b, "legR", 0, 0, 0, 0, 10, 12, 0, 0, 44, 0, 0, 0);
        K.r(b, "legL", 0, 0, 0, 0, 10, -8, 0, 0, 44, 0, 0, 0);
        K.r(b, "hornR", 0, 0, 0, 0, 16, 0, 0, 0, 20, 0, 0, 12, 44, 0, 0, 0);
        K.r(b, "hornL", 0, 0, 0, 0, 16, 0, 0, 0, 20, 0, 0, -12, 44, 0, 0, 0);
        // BULL RUSH: lower the head, paw back, then a heavy shoulder-first charge
        Clip r = m.clip("bull_rush", 32);
        K.r(r, "torso", 0, 0, 0, 0, 10, 35, -20, 0, 114, 30, 25, 0, 20, 28, 20, 0, 32, 0, 0, 0);
        K.r(r, "head", 0, 0, 0, 0, 10, 20, 10, 0, 114, 10, -20, 0, 32, 0, 0, 0);
        K.r(r, "armR", 0, 0, 0, 0, 10, 40, 0, 30, 114, -100, -20, -10, 22, -90, -20, -10, 32, 0, 0, 0);
        K.r(r, "armL", 0, 0, 0, 0, 10, 50, 0, -10, 114, 50, 0, 10, 32, 0, 0, 0);
        K.p(r, "body", 0, 0, 0, 0, 10, 0, -2, -3, 114, 0, -1, 7, 22, 0, -1, 7, 32, 0, 0, 0);
        K.r(r, "legR", 0, 0, 0, 0, 10, 35, 0, 0, 114, -55, 0, 0, 32, 0, 0, 0);
        K.r(r, "legL", 0, 0, 0, 0, 10, -20, 0, 0, 114, 40, 0, 0, 32, 0, 0, 0);
        // SKULL BASH: wind the head back and headbutt downward, tusks first
        Clip sb = m.clip("skull_bash", 30);
        K.r(sb, "torso", 0, 0, 0, 0, 10, -18, 0, 0, 114, 38, 0, 0, 20, 30, 0, 0, 30, 0, 0, 0);
        K.r(sb, "head", 0, 0, 0, 0, 10, -42, 0, 0, 114, 38, 0, 0, 20, 30, 0, 0, 30, 0, 0, 0);
        K.r(sb, "jaw", 0, 0, 0, 0, 10, 25, 0, 0, 112, 0, 0, 0, 30, 0, 0, 0);
        K.r(sb, "armR", 0, 0, 0, 0, 10, 25, 0, 20, 114, -10, 0, 10, 30, 0, 0, 0);
        K.r(sb, "armL", 0, 0, 0, 0, 10, 25, 0, -20, 114, -10, 0, -10, 30, 0, 0, 0);
        K.p(sb, "body", 0, 0, 0, 0, 10, 0, 0, -2, 114, 0, -2, 4, 20, 0, -2, 4, 30, 0, 0, 0);
        m.clip("hurt", 10)
            .rot("torso", 0, 0, 0, 0).rot("torso", 3, -12, 0, 5).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 8, 0).rot("head", 10, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 30, 0, 0).rot("jaw", 10, 0, 0, 0);
        Clip d = m.clip("death", 36);
        K.r(d, "torso", 0, 0, 0, 0, 8, -20, 0, 0, 36, 30, 0, 0);
        K.r(d, "head", 0, 0, 0, 0, 10, -40, 0, 0, 36, 30, 20, 0);
        K.r(d, "jaw", 0, 0, 0, 0, 10, 40, 0, 0, 36, 30, 0, 0);
        K.r(d, "armR", 0, 0, 0, 0, 10, -80, 0, 40, 36, 10, 0, 70);
        K.r(d, "armL", 0, 0, 0, 0, 10, -80, 0, -40, 36, 10, 0, -70);
        K.r(d, "body", 0, 0, 0, 0, 12, 0, 0, 0, 36, -80, 0, 8);
        K.p(d, "body", 0, 0, 0, 0, 20, 0, -3, -2, 36, 0, -8, -4);
        K.r(d, "legR", 0, 0, 0, 0, 20, -30, 0, 8, 36, -50, 0, 10);
        K.r(d, "legL", 0, 0, 0, 0, 20, -50, 0, -8, 36, -40, 0, -10);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("gremlin_nob", "Gremlin Nob", "グレムリンノブ")
            .game(1, 1).size(1.1f, 2.4f).hp(84).speed(0.27f).style(Style.BRUTE).elite().egg(0xb8854a, 0xe8dcc0)
            .model(GremlinNob::model).spawn(1, 1, 1, MobDef.Habitat.FOREST)
            .pattern(s -> {
                if (s.getTurn() == 0) return "bellow";
                if (s.repeatCount("bull_rush") >= 2) return "skull_bash";
                return s.rand().nextInt(100) < 67 ? "bull_rush" : "skull_bash";
            })
            .move(Move.of("bellow", 44).intent(Intent.BUFF).noTarget()
                .sound(16, SoundEvents.RAVAGER_ROAR, 1.6f, 0.6f)
                .at(18, (s, t) -> { Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 20, 0.8); Fx.ring(s, ParticleTypes.CLOUD, s.position(), 2.5, 24); s.flags.add("enrage"); }))
            .move(Move.of("bull_rush", 32).intent(Intent.ATTACK, 14, 1).reach(3.2f)
                .sound(8, SoundEvents.RAVAGER_STEP, 1f, 0.7f)
                .at(11, Fx.dash(0.65, 0.05)).at(13, Fx.melee(14, 3.2, 90)))
            .move(Move.of("skull_bash", 30).intent(Intent.ATTACK_DEBUFF, 6, 1).reach(2.8f)
                .sound(14, SoundEvents.ANVIL_LAND, 0.7f, 1.2f)
                .at(14, Fx.melee(6, 2.8, 80, v -> Fx.vulnerable(v, 6))))
            .onHurt((s, src, dmg) -> {
                if (s.flags.contains("enrage") && dmg > 0 && s.inc("hits") % 3 == 0) { s.addStrength(2); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.5); }
                return dmg;
            });
    }
}
