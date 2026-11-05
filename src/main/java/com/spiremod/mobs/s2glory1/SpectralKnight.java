package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory elite (Knight Gang): Spectral Knight. 93 HP. Opens with Hex (your cards are Ethereal), then alternates Soul Slash (15) / Soul Flame (3 x3). */
public final class SpectralKnight {
    private SpectralKnight() {}
    static final int ARMOR = 0x3a5a78, ARMOR_D = 0x1c2c40, SOUL = 0x70e8ff, BONE = 0xd8e8f0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_spectral_knight");
        Kit.biped(m, BONE, ARMOR, ARMOR_D, SOUL);
        m.scale(1.4f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.5f, 0, -3, 9, 9, 6).color(ARMOR).grad(0x5a82a8, ARMOR_D).bevel(0.2f);
        torso.box(-1.5f, 2, -3.2f, 3, 4, 1).color(0x102030).glow(Face.FRONT, 0, 0, 3, 4, SOUL);          // soul window
        body.box(-4.5f, -7, -3, 9, 7, 6).color(ARMOR_D).grad(ARMOR, 0x0c1420);                            // tattered skirt
        body.box(-3.5f, -11, -2.5f, 7, 5, 5).color(0x0c1420).grad(ARMOR_D, 0x060a10);
        head.box(-3.5f, 0, -3.5f, 7, 7, 7).color(ARMOR).grad(0x5a82a8, ARMOR_D).bevel(0.25f);
        head.box(-2.5f, 2.5f, 3.4f, 5, 2, 1).color(0x081018).glow(Face.FRONT, 0, 0, 2, 2, SOUL).glow(Face.FRONT, 3, 0, 2, 2, SOUL);
        Part flame = head.add("flame").at(0, 7, 0);
        flame.box(-2, 0, -2, 4, 4, 4).color(SOUL).flat().glow(Face.FRONT, 0, 0, 4, 4, SOUL).glow(Face.TOP, 0, 0, 4, 4, 0xffffff);
        flame.box(-1, 4, -1, 2, 3, 2).color(0xb0f4ff).flat().glow(Face.FRONT, 0, 0, 2, 3, 0xb0f4ff);
        for (int s = -1; s <= 1; s += 2) torso.add("pad" + s).at(s * 5.5f, 8.5f, 0).box(-2.5f, -1, -2.5f, 5, 3, 5).color(ARMOR_D).bevel(0.25f);
        Part sw = m.byName.get("handR").add("sword").at(0, -2, 0).rot(-80, 0, 0);
        sw.box(-0.5f, -2, -0.5f, 1, 4, 1).color(ARMOR_D); sw.box(-2, 2, -0.5f, 4, 1, 1).color(SOUL).flat();
        sw.box(-1, 3, -0.3f, 2, 16, 1).color(SOUL).flat().glow(Face.FRONT, 0, 0, 2, 16, SOUL).noise(0.03f);
        Kit.bipedBase(m);
        m.proc((r, c) -> { r.show("legR", false); r.show("legL", false); r.pos("body", 0, 2f + (float) Math.sin(c.age * 0.12f) * 1.2f, 0);
            r.scl("flame", 1f, 1f + 0.2f * (float) Math.sin(c.age * 0.7f), 1f); });
        Kit.cast(m, "hex", 40, 16);
        m.clips.get("hex").rot("head", 14, -25, 0, 0).rot("head", 26, -25, 0, 0).rot("flame", 0, 0, 0, 0).rot("flame", 16, 0, 0, 0);
        Kit.sweep(m, "soul_slash", 30, 14);
        Clip f = Kit.cast(m, "soul_flame", 42, 12);
        Kit.seq(f, "armR", 42, -1, 6, -40, 0, 10, 12, -95, 0, 0, 30, -95, 0, 0);
        for (int t : new int[] {12, 19, 26}) f.rot("torso", t, -8, 0, 0).rot("torso", t + 3, 2, 0, 0);
        return m;
    }

    public static MobDef def() {
        MobDef d = MobDef.of("s2_spectral_knight", "Spectral Knight", "スペクトルナイト")
            .game(2, 3).size(1.0f, 2.6f).hp(93).speed(0.23f).style(Style.GHOST).egg(0x3a5a78, 0x70e8ff).elite()
            .model(SpectralKnight::model)
            .spawn(1, 1, 1, MobDef.Habitat.PLAINS)
            .pattern(Pattern.seq(1, "hex", "soul_slash", "soul_flame"))
            .move(Move.of("hex", 40).intent(Intent.STRONG_DEBUFF).reach(10).ranged(10)
                .sound(14, SoundEvents.SOUL_ESCAPE, 1.2f, 0.7f)
                .at(16, (s, t) -> { Fx.particles(s, ParticleTypes.SOUL, 30, 0.9); if (t != null) Fx.curse(t, Fx.Curse.VOID); }))
            .move(Move.of("soul_slash", 30).intent(Intent.ATTACK, 15, 1).reach(3.0f)
                .sound(12, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.6f).at(14, Fx.melee(15, 3.2, 100)));
        Move.Action fl = (s, t) -> { Kit.zap(3).run(s, t); Fx.sound(s, SoundEvents.BLAZE_SHOOT, 0.7f, 1.5f); Fx.particlesAt(s, ParticleTypes.SOUL_FIRE_FLAME, Fx.rel(s, 1.4, 0, 1.6), 8, 0.2, 0.05); };
        return d.move(Move.of("soul_flame", 42).intent(Intent.ATTACK, 3, 3).ranged(10).at(12, fl).at(19, fl).at(26, fl));
    }
}
