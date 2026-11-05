package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;

/** StS2 Glory elite (Knight Gang): Magi Knight. 82 HP. Power Shield (6 + 5 Block) -> Dampen (your cards are Downgraded) -> loop Ram (10), Prep (5 Block), Magic Bomb (35). */
public final class MagiKnight {
    private MagiKnight() {}
    static final int ROBE = 0x4a3a8a, ROBE_D = 0x2a1e54, PLATE = 0xb0b8c8, GOLD = 0xe0b040, ORB = 0xc070ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_magi_knight");
        Kit.biped(m, 0xc8a888, ROBE, ROBE_D, GOLD);
        m.scale(1.35f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.5f, 0, -3, 9, 9, 6).color(ROBE).grad(0x6a56b0, ROBE_D).bevel(0.1f);
        torso.box(-3.5f, 3, -3.2f, 7, 5, 1).color(PLATE).grad(0xd0d8e8, 0x808898).bevel(0.25f);         // breastplate
        torso.box(-1, 4, -3.4f, 2, 2, 1).color(ORB).flat().glow(Face.FRONT, 0, 0, 2, 2, ORB);
        body.box(-5, -9, -3.2f, 10, 11, 6).color(ROBE_D).grad(ROBE, 0x1a1238).noise(0.08f);               // long robe
        head.box(-3, 0, -3, 6, 6, 6).color(0xc8a888).bevel(0.1f);
        head.box(-3.7f, 3, -3.7f, 7, 4, 7).color(PLATE).grad(0xd0d8e8, 0x808898).bevel(0.25f);           // helm
        head.box(-3, 1, 3, 6, 2, 1).color(0x1a1a24).glow(Face.FRONT, 0, 0, 2, 1, ORB).glow(Face.FRONT, 4, 0, 2, 1, ORB);
        head.box(-0.5f, 7, -2, 1, 5, 5).color(0xe0e0f0).flat();
        for (int s = -1; s <= 1; s += 2) torso.add("pad" + s).at(s * 5.5f, 8.5f, 0).box(-2.7f, -1, -2.7f, 5, 3, 5).color(PLATE).bevel(0.3f);
        Part staff = m.byName.get("handR").add("staff").at(0, -2, 0).rot(-10, 0, 0);
        staff.box(-0.75f, -14, -0.75f, 2, 34, 2).color(0x5a4636);
        Part orb = staff.add("orb").at(0, 21, 0);
        orb.box(-2.5f, -2.5f, -2.5f, 5, 5, 5).color(ORB).flat().glow(Face.FRONT, 0, 0, 5, 5, ORB).glow(Face.TOP, 0, 0, 5, 5, 0xe8c0ff).glow(Face.LEFT, 0, 0, 5, 5, ORB);
        Part sh = m.byName.get("handL").add("shield").at(0, -1, 1);
        sh.box(-1, -5, -4, 2, 10, 8).color(PLATE).bevel(0.3f); sh.box(-1.4f, -2, -1.5f, 1, 4, 3).color(ORB).flat().glow(Face.LEFT, 0, 0, 3, 4, ORB);
        Kit.bipedBase(m);
        m.proc((r, c) -> { float s = 1f + 0.12f * (float) Math.sin(c.age * 0.4f); r.scl("orb", s, s, s); });
        Kit.jabs(m, "power_shield", 32, 14).rot("armL", 0, 0, 0, 0).rot("armL", 14, -60, 0, 10).rot("armL", 28, 0, 0, 0);
        Kit.cast(m, "dampen", 40, 16);
        m.clips.get("dampen").rot("armR", 16, -150, 0, 0).rot("armR", 28, -140, 0, 0);
        Kit.sweep(m, "ram", 28, 13);
        Clip p = m.clip("prep", 28);
        Kit.seq(p, "armL", 28, -1, 8, -70, 0, 10, 14, -100, 0, 10, 22, -100, 0, 10);
        Kit.seq(p, "torso", 28, -1, 8, 8, 0, 0, 14, 8, 0, 0);
        Kit.sp(p, "body", 28, -1, 8, 0, -2, 0, 14, 0, -2, 0);
        Clip b = Kit.cast(m, "magic_bomb", 46, 28);
        Kit.seq(b, "armR", 46, 28, 6, -40, 0, 10, 20, -170, 0, 5, 28, -90, 0, 0, 38, -85, 0, 0);
        Kit.seq(b, "torso", 46, 28, 6, 10, 0, 0, 20, -18, 0, 0, 28, 20, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_magi_knight", "Magi Knight", "マギナイト")
            .game(2, 3).size(1.0f, 2.5f).hp(82).speed(0.23f).style(Style.MAGIC).egg(0x4a3a8a, 0xc070ff).elite()
            .model(MagiKnight::model)
            .spawn(1, 1, 1, MobDef.Habitat.PLAINS)
            .pattern(Pattern.seq(2, "power_shield", "dampen", "ram", "prep", "magic_bomb"))
            .move(Move.of("power_shield", 32).intent(Intent.ATTACK_DEFEND, 6, 1).reach(2.8f)
                .sound(12, SoundEvents.SHIELD_BLOCK, 1f, 0.9f)
                .at(14, (s, t) -> { Fx.meleeNow(s, 6, 3.0, 100, null); Kit.block(s, 5); }))
            .move(Move.of("dampen", 40).intent(Intent.STRONG_DEBUFF).ranged(10)
                .sound(14, SoundEvents.EVOKER_PREPARE_ATTACK, 1f, 1f)
                .at(16, (s, t) -> { Fx.particles(s, ParticleTypes.ENCHANT, 40, 1.0); if (t != null) { Kit.effect(t, MobEffects.WEAKNESS, 20, 0); Kit.effect(t, MobEffects.DIG_SLOWDOWN, 20, 0); } }))
            .move(Move.of("ram", 28).intent(Intent.ATTACK, 10, 1).reach(3.0f)
                .sound(11, SoundEvents.PLAYER_ATTACK_STRONG, 1f, 0.7f).at(13, Fx.melee(10, 3.2, 100)))
            .move(Move.of("prep", 28).intent(Intent.DEFEND).noTarget()
                .sound(12, SoundEvents.SHIELD_BLOCK, 1f, 0.7f).at(14, (s, t) -> Kit.block(s, 5)))
            .move(Move.of("magic_bomb", 46).intent(Intent.ATTACK, 35, 1).ranged(14)
                .sound(18, SoundEvents.EVOKER_CAST_SPELL, 1.2f, 0.8f)
                .at(22, (s, t) -> Fx.particlesAt(s, ParticleTypes.WITCH, Fx.rel(s, 0.6, 0, 3.4), 20, 0.4, 0.05))
                .at(28, (s, t) -> {
                    if (t == null) return;
                    Fx.particlesAt(s, ParticleTypes.EXPLOSION, t.position().add(0, 0.5, 0), 3, 0.4, 0.02);
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.8f, 1.3f);
                    Fx.aoeAt(s, t.position(), 35, 2.2, null);
                }));
    }
}
