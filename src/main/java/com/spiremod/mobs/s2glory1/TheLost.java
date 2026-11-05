package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;

/** StS2 Glory: The Lost (with The Forgotten). 93 HP, Possess Strength. Alternates Debilitating Smog (steal 2 Strength) and Eye Lasers (4 x2). */
public final class TheLost {
    private TheLost() {}
    static final int GHOST = 0x6f8c9c, GHOST_D = 0x384a58, EYE = 0x80ffe8;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_the_lost");
        Kit.biped(m, 0xb8d0d8, GHOST, GHOST_D, 0x90b0c0);
        m.scale(1.4f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-3.5f, 0, -2.5f, 7, 9, 5).color(GHOST).grad(0x88a4b4, GHOST_D);                       // thin ribcage robe
        for (int i = 0; i < 4; i++) torso.box(-3, 2 + i * 1.8f, -2.7f, 6, 1, 1).color(0xc8e0e8).flat().noise(0.05f); // ribs
        body.box(-4, -8, -2.5f, 8, 9, 5).color(GHOST_D).grad(GHOST, 0x1c2a34);                           // tapering rags
        body.box(-3, -13, -2, 6, 6, 4).color(0x1c2a34).grad(GHOST_D, 0x101820);
        head.box(-3.5f, 0, -3.5f, 7, 7, 7).color(0xc8e0e8).bevel(0.15f).noise(0.1f);                       // skull
        head.box(-3, 2, 3.4f, 6, 2, 1).color(0x101820).glow(Face.FRONT, 0, 0, 2, 2, EYE).glow(Face.FRONT, 4, 0, 2, 2, EYE);
        head.box(-2, 0, 3.4f, 4, 1, 1).color(0x101820).flat();
        head.box(-4, 4, -4, 8, 4, 8).color(GHOST_D).grad(GHOST, GHOST_D);                                // hood
        for (int s = -1; s <= 1; s += 2) {
            m.byName.get(s < 0 ? "armR" : "armL").box(-1, -10, -1, 2, 4, 2).color(0xc8e0e8);                // bony forearm
            m.byName.get(s < 0 ? "handR" : "handL").box(-1.5f, -6, -0.5f, 3, 3, 1).color(0xc8e0e8).glow(Face.BOTTOM, 0, 0, 3, 1, EYE);
        }
        Kit.bipedBase(m);
        m.proc((r, c) -> { r.show("legR", false); r.show("legL", false); r.pos("body", 0, 1.5f + (float) Math.sin(c.age * 0.12f) * 1.2f, 0); });
        Clip s = Kit.buff(m, "debilitating_smog", 40, 17);
        Clip e = Kit.cast(m, "eye_lasers", 36, 14);
        Kit.seq(e, "head", 36, 14, 8, -25, 0, 0, 13, -35, 0, 0, 14, 10, 0, 0, 26, 8, 0, 0);
        Kit.seq(e, "torso", 36, -1, 8, 8, 0, 0, 14, -10, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_the_lost", "The Lost", "ロスト")
            .game(2, 3).size(0.8f, 2.4f).hp(93).speed(0.22f).style(Style.GHOST).egg(0x6f8c9c, 0x80ffe8)
            .model(TheLost::model)
            .spawn(4, 1, 1, MobDef.Habitat.TAIGA)
            .pattern(Pattern.cycle("debilitating_smog", "eye_lasers"))
            .move(Move.of("debilitating_smog", 40).intent(Intent.DEBUFF).reach(8f).ranged(8)
                .sound(14, SoundEvents.SOUL_ESCAPE, 1.2f, 0.6f)
                .at(17, (s, t) -> {
                    Fx.particles(s, ParticleTypes.SOUL, 30, 1.0);
                    if (t != null) Kit.effect(t, MobEffects.WEAKNESS, 2 * Kit.TURN * 2, 0);
                    Kit.str(s, 2);
                }))
            .move(Move.of("eye_lasers", 36).intent(Intent.ATTACK, 4, 2).ranged(12)
                .sound(14, SoundEvents.GUARDIAN_ATTACK, 0.6f, 1.6f).sound(19, SoundEvents.GUARDIAN_ATTACK, 0.6f, 1.6f)
                .at(14, (s, t) -> { Kit.zap(4).run(s, t); Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 6, 0.3); })
                .at(19, (s, t) -> { Kit.zap(4).run(s, t); Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 6, 0.3); }))
            .onDeath((s, src) -> { for (var v : Fx.foes(s, new AABB(s.blockPosition()).inflate(32))) v.removeEffect(MobEffects.WEAKNESS); });
    }
}
