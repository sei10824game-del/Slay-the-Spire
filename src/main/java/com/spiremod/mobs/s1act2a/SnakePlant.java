package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Snake Plant. 75-79 HP, Malleable 3 (each time it is hit it gains Block equal to the counter, counter +1; reset when it acts).
 * Chomp 7x3 (65%), Enfeebling Spores (Frail 2 + Weak 2, 35%). No 3x Chomp... max 2 in a row; no Spores twice in a row.
 */
public final class SnakePlant {
    private SnakePlant() {}
    static final int LEAF = 0x4e8a3a, LEAF_D = 0x2c5224, PETAL = 0xd9b43a, PETAL_D = 0x8a6a1a, MAW = 0x8a2a3a, ROOT = 0x5a4430;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("snake_plant");
        Part base = m.add("base").at(0, 0, 0);
        base.cbox(0, 12, 5, 12).color(ROOT).grad(0x7a5c40, 0x3a2a1c).noise(0.15f).bevel(0.15f);
        for (int i = 0; i < 4; i++) {          // roots
            Part r = base.add("root" + i).at(i < 2 ? -5 : 5, 2, i % 2 == 0 ? 4 : -4).rot(0, 0, i < 2 ? 40 : -40);
            r.box(-1, 0, -1, 2, 6, 2).color(ROOT);
        }
        Part stalk = base.add("stalk").at(0, 5, 0);
        stalk.cbox(0, 4, 8, 4).color(LEAF).grad(0x66aa4c, LEAF_D).noise(0.1f);
        Part s2 = stalk.add("stalk2").at(0, 8, 0);
        s2.cbox(0, 3, 8, 3).color(LEAF).grad(0x66aa4c, LEAF_D).noise(0.1f);
        for (int i = 0; i < 2; i++) {          // side leaves
            int s = i == 0 ? -1 : 1;
            Part lf = stalk.add(i == 0 ? "leafR" : "leafL").at(s * 2, 4, 0).rot(0, 0, s * -50);
            lf.box(s > 0 ? 0 : -9, 0, -2, 9, 1, 4).color(LEAF).grad(0x7ac05a, LEAF_D).bevel(0.1f);
        }
        Part head = s2.add("head").at(0, 8, 0);
        head.cbox(0, 8, 7, 8).color(PETAL).grad(0xf0d060, PETAL_D).bevel(0.15f);
        Part jawU = head.add("jawU").at(0, 4, 3.5f);
        jawU.box(-3.5f, 0, 0, 7, 3, 1).color(MAW).glow(Face.BOTTOM, 0, 0, 7, 1, 0xff7080);
        for (int i = 0; i < 4; i++) jawU.box(-3 + i * 2, -1, 0, 1, 1, 1).color(0xf4efe0).flat();
        Part jawL = head.add("jawL").at(0, 4, 3.5f);
        jawL.box(-3.5f, -3, 0, 7, 3, 1).color(MAW);
        for (int i = 0; i < 4; i++) jawL.box(-2 + i * 2, 0, 0, 1, 1, 1).color(0xf4efe0).flat();
        head.box(-3, 5, 3.6f, 6, 2, 1).color(0x1a2a14).glow(Face.FRONT, 0, 0, 1, 2, 0xffe040).glow(Face.FRONT, 5, 0, 1, 2, 0xffe040); // eyes
        for (int i = 0; i < 6; i++) {          // petal collar
            double a = i * Math.PI / 3;
            Part p = head.add("petal" + i).at((float) Math.cos(a) * 4, 4, (float) Math.sin(a) * 4).rot(-30 * (float) Math.sin(a), 0, 30 * (float) Math.cos(a));
            p.box(-1.5f, 0, -1.5f, 3, 7, 1).color(PETAL).grad(0xf0d060, PETAL_D).flat();
        }
        m.look("head", 40, 20);
        m.clip("idle", 50).loop()
            .rot("stalk", 0, 3, 0, 3).rot("stalk", 25, 3, 0, -3).rot("stalk", 50, 3, 0, 3)
            .rot("stalk2", 0, -4, 0, -4).rot("stalk2", 25, 4, 0, 4).rot("stalk2", 50, -4, 0, -4)
            .rot("head", 0, 0, 6, 0).rot("head", 25, 0, -6, 0).rot("head", 50, 0, 6, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 20, 10, 0, 0).rot("jawL", 40, 0, 0, 0);
        m.clip("walk", 20).loop()
            .pos("base", 0, 0, 0, 0).pos("base", 5, 0, 3, 0).pos("base", 10, 0, 0, 0).pos("base", 15, 0, 3, 0).pos("base", 20, 0, 0, 0)
            .rot("stalk", 0, 12, 0, 8).rot("stalk", 10, 12, 0, -8).rot("stalk", 20, 12, 0, 8);
        Clip ch = m.clip("chomp", 50)
            .rot("stalk", 0, 0, 0, 0).rot("stalk", 8, -20, 0, 0).rot("stalk", 50, 0, 0, 0)
            .rot("stalk2", 0, 0, 0, 0).rot("stalk2", 8, -25, 0, 0).rot("stalk2", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -20, 0, 0)
            .pos("head", 0, 0, 0, 0);
        for (int i = 0; i < 3; i++) {
            int t = 12 + i * 10;
            ch.rot("head", t, 40, 0, 0, Ease.IN_CUBIC).rot("head", t + 5, -10, 0, 0).pos("head", t, 0, -2, 5, Ease.IN_CUBIC).pos("head", t + 5, 0, 0, -1)
              .rot("jawL", t - 4, 50, 0, 0).rot("jawL", t, 0, 0, 0, Ease.IN_CUBIC).rot("jawU", t - 4, -30, 0, 0).rot("jawU", t, 0, 0, 0, Ease.IN_CUBIC);
        }
        ch.rot("head", 50, 0, 0, 0).pos("head", 50, 0, 0, 0).rot("jawL", 0, 0, 0, 0).rot("jawU", 0, 0, 0, 0);
        Clip sp = m.clip("spores", 44)
            .rot("stalk", 0, 0, 0, 0).rot("stalk", 12, 18, 0, 0).rot("stalk", 20, -14, 0, 0, Ease.OUT_BACK).rot("stalk", 44, 0, 0, 0)
            .rot("stalk2", 0, 0, 0, 0).rot("stalk2", 12, 20, 0, 0).rot("stalk2", 20, -18, 0, 0, Ease.OUT_BACK).rot("stalk2", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, -35, 0, 0).rot("head", 38, -30, 0, 0).rot("head", 44, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 20, 60, 0, 0).rot("jawL", 38, 60, 0, 0).rot("jawL", 44, 0, 0, 0)
            .rot("jawU", 0, 0, 0, 0).rot("jawU", 20, -40, 0, 0).rot("jawU", 38, -40, 0, 0).rot("jawU", 44, 0, 0, 0);
        for (int i = 0; i < 6; i++) {
            sp.rot("petal" + i, 0, 0, 0, 0).rot("petal" + i, 20, -35, 0, 0).rot("petal" + i, 44, 0, 0, 0);
        }
        m.clip("hurt", 10).rot("stalk", 0, 0, 0, 0).rot("stalk", 3, 20, 0, 8).rot("stalk", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 0).rot("head", 10, 0, 0, 0);
        m.clip("death", 30)
            .rot("stalk", 0, 0, 0, 0).rot("stalk", 20, 0, 0, 80, Ease.IN_QUAD).rot("stalk", 30, 0, 0, 90)
            .rot("stalk2", 0, 0, 0, 0).rot("stalk2", 20, 20, 0, 30).rot("head", 0, 0, 0, 0).rot("head", 20, 40, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 15, 50, 0, 0)
            .pos("base", 0, 0, 0, 0).pos("base", 30, 0, -3, 0, Ease.OUT_BOUNCE);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("snake_plant", "Snake Plant", "スネークプラント")
            .game(1, 2).size(0.9f, 1.5f).hp(77).speed(0.2f).style(Style.INSECT).egg(0x4e8a3a, 0xd9b43a)
            .model(SnakePlant::model).keepBlock().spawn(5, 1, 1, MobDef.Habitat.JUNGLE)
            .onSpawn(s -> s.setCount("mall", 3))
            // Malleable: block grows each time it is struck
            .onHurt((s, src, dmg) -> {
                if (src.getEntity() != null && s.invulnerableTime <= 10) { s.addBlock(s.count("mall")); s.setCount("mall", s.count("mall") + 1); }
                return dmg;
            })
            .onMoveEnd(s -> s.setCount("mall", 3))
            .pattern(s -> {
                String l = s.lastMove();
                String p = s.rand().nextDouble() < 0.65 ? "chomp" : "spores";
                if (p.equals("chomp") && s.repeatCount("chomp") >= 2) p = "spores";
                if (p.equals("spores") && l.equals("spores")) p = "chomp";
                return p;
            })
            .move(Move.of("chomp", 50).intent(Intent.ATTACK, 7, 3).reach(2.4f)
                .atAll(new int[]{12, 22, 32}, Fx.melee(7, 2.8, 90))
                .sound(8, SoundEvents.RAVAGER_ATTACK, 0.7f, 1.6f))
            .move(Move.of("spores", 44).intent(Intent.STRONG_DEBUFF).noTarget()
                .sound(18, SoundEvents.PUFFER_FISH_BLOW_OUT, 1.2f, 0.7f)
                .at(22, (s, t) -> {
                    Fx.particles(s, ParticleTypes.SPORE_BLOSSOM_AIR, 40, 1.2);
                    for (var v : Fx.foes(s, s.getBoundingBox().inflate(5))) { Fx.weak(v, 6); Fx.frail(v, 6); }
                }));
    }
}
