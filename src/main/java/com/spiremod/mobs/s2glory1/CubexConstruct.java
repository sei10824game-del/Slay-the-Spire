package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth/Glory: Cubex Construct. 65 HP, Artifact 1. Charge Up (+2 Str), then Repeater Blast (7, +2 Str) x2, Expel Blast (5 x2). */
public final class CubexConstruct {
    private CubexConstruct() {}
    static final int STONE = 0x7a8478, STONE_D = 0x4a544a, RUNE = 0x80ffa0, BRASS = 0xc09040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_cubex_construct");
        Part core = m.add("core").at(0, 14, 0);
        Box b = core.box(-7, -7, -7, 14, 14, 14).color(STONE).grad(0x94a090, STONE_D).noise(0.14f).bevel(0.3f);
        b.paint(Face.FRONT, 3, 3, 8, 2, 0x1a2018).glow(Face.FRONT, 4, 4, 2, 1, RUNE).glow(Face.FRONT, 8, 4, 2, 1, RUNE)
            .glow(Face.FRONT, 5, 8, 4, 1, RUNE).glow(Face.FRONT, 6, 9, 2, 2, RUNE)
            .glow(Face.LEFT, 4, 4, 6, 1, RUNE).glow(Face.LEFT, 4, 9, 6, 1, RUNE).glow(Face.RIGHT, 4, 4, 6, 1, RUNE).glow(Face.RIGHT, 4, 9, 6, 1, RUNE)
            .glow(Face.TOP, 5, 5, 4, 4, RUNE);
        core.box(-7.5f, -1, -7.5f, 15, 2, 15).color(BRASS).flat().bevel(0.2f);                                     // brass belt
        for (int s = -1; s <= 1; s += 2) {
            Part can = m.add(s < 0 ? "cannonR" : "cannonL").at(s * 11, 14, 0);
            can.box(-2.5f, -2.5f, -3, 5, 5, 8).color(STONE_D).grad(STONE, STONE_D).bevel(0.25f);
            can.box(-1.5f, -1.5f, 5, 3, 3, 4).color(0x30363a).bevel(0.2f);
            can.box(-1, -1, 9, 2, 2, 1).color(0x101010).glow(Face.FRONT, 0, 0, 2, 2, RUNE);
        }
        for (int i = 0; i < 4; i++) {
            Part o = m.add("orb" + i).at(0, 14, 0);
            o.box(-1.5f, -1.5f, -1.5f, 3, 3, 3).color(STONE).bevel(0.25f).glow(Face.FRONT, 1, 1, 1, 1, RUNE).glow(Face.TOP, 1, 1, 1, 1, RUNE);
        }
        m.look("core", 60, 30);
        m.proc((r, c) -> {
            float rad = 13f + (c.move != null && c.move.equals("charge_up") ? Math.min(6f, c.moveT * 0.3f) : 0f), sp = c.age * 0.12f;
            for (int i = 0; i < 4; i++) { float a = sp + i * 1.5708f; r.pos("orb" + i, (float) Math.cos(a) * rad, (float) Math.sin(sp * 0.7f + i) * 3f, (float) Math.sin(a) * rad); }
            float bob = (float) Math.sin(c.age * 0.1f) * 1.5f;
            for (String n : new String[] {"core", "cannonR", "cannonL"}) r.pos(n, 0, bob, 0);
        });
        m.clip("idle", 60).loop().rot("core", 0, 0, 0, 0).rot("core", 30, 3, 0, 3).rot("core", 60, 0, 0, 0);
        Clip c = m.clip("charge_up", 40);
        c.scl("core", 0, 1, 1, 1).scl("core", 16, 0.85f, 0.85f, 0.85f).scl("core", 22, 1.3f, 1.3f, 1.3f, Ease.OUT_BACK).scl("core", 40, 1, 1, 1);
        Kit.seq(c, "cannonR", 40, -1, 16, 0, 0, 0, 22, -30, 25, 0, 40, 0, 0, 0);
        Kit.seq(c, "cannonL", 40, -1, 16, 0, 0, 0, 22, -30, -25, 0, 40, 0, 0, 0);
        Kit.seq(c, "core", 40, -1, 10, 0, 40, 0, 22, 0, 360, 0, 40, 0, 360, 0);
        for (String n : new String[] {"repeater_blast", "expel_blast"}) {
            boolean two = n.startsWith("exp");
            Clip k = m.clip(n, two ? 36 : 30);
            int t1 = 12, t2 = 19;
            Kit.seq(k, "core", k.length, -1, 8, -10, 0, 0, t1, 6, 0, 0, two ? t2 : t1 + 6, two ? 6 : 0, 0, 0);
            Kit.sp(k, "cannonR", k.length, t1, t1 - 4, 0, 0, 1, t1, 0, 0, -3, t1 + 6, 0, 0, 0);
            Kit.sp(k, "cannonL", k.length, two ? t2 : -1, (two ? t2 : t1) - 4, 0, 0, 1, two ? t2 : t1, 0, 0, -3, (two ? t2 : t1) + 6, 0, 0, 0);
            Kit.seq(k, "cannonR", k.length, -1, t1 - 4, 0, -15, 0, t1, 0, 5, 0);
            Kit.seq(k, "cannonL", k.length, -1, (two ? t2 : t1) - 4, 0, 15, 0, two ? t2 : t1, 0, -5, 0);
        }
        m.clip("hurt", 10).rot("core", 0, 0, 0, 0).rot("core", 3, -14, 0, 8).rot("core", 10, 0, 0, 0);
        m.clip("death", 28)
            .rot("core", 0, 0, 0, 0).rot("core", 10, 20, 0, 15).rot("core", 28, 90, 0, 40)
            .pos("core", 0, 0, 0, 0).pos("core", 28, 0, -7, 0, Ease.OUT_BOUNCE)
            .pos("cannonR", 0, 0, 0, 0).pos("cannonR", 18, -6, -10, 0, Ease.OUT_BOUNCE).rot("cannonR", 0, 0, 0, 0).rot("cannonR", 18, 0, 0, 80)
            .pos("cannonL", 0, 0, 0, 0).pos("cannonL", 18, 6, -10, 0, Ease.OUT_BOUNCE).rot("cannonL", 0, 0, 0, 0).rot("cannonL", 18, 0, 0, -80);
        return m;
    }

    public static MobDef def() {
        MobDef d = MobDef.of("s2_cubex_construct", "Cubex Construct", "キュベックスコンストラクト")
            .game(2, 3).size(1.0f, 1.4f).hp(65).speed(0.2f).style(Style.METAL).egg(0x7a8478, 0x80ffa0)
            .model(CubexConstruct::model)
            .spawn(5, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.first("charge_up", Pattern.cycle("repeater_blast", "repeater_blast", "expel_blast")))
            .move(Move.of("charge_up", 40).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.BEACON_ACTIVATE, 1f, 1.6f)
                .at(22, (s, t) -> { Kit.str(s, 2); Fx.particles(s, ParticleTypes.HAPPY_VILLAGER, 20, 0.7); }));
        Move.Action shot = (s, t) -> {
            Kit.zap(5).run(s, t); Fx.sound(s, SoundEvents.SHULKER_SHOOT, 0.8f, 1.5f);
            Fx.particlesAt(s, ParticleTypes.HAPPY_VILLAGER, Fx.rel(s, 1.2, 0, 1), 6, 0.2, 0.05);
        };
        return d
            .move(Move.of("repeater_blast", 30).intent(Intent.ATTACK_BUFF, 7, 1).ranged(12)
                .at(12, (s, t) -> { Kit.zap(7).run(s, t); Fx.sound(s, SoundEvents.SHULKER_SHOOT, 0.9f, 1.2f); Fx.particles(s, ParticleTypes.HAPPY_VILLAGER, 8, 0.4); Kit.str(s, 2); }))
            .move(Move.of("expel_blast", 36).intent(Intent.ATTACK, 5, 2).ranged(12)
                .at(12, shot).at(19, shot));
    }
}
