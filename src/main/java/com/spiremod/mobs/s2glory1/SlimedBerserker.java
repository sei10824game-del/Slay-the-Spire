package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;

/** StS2 Glory: Slimed Berserker. 266 HP. Cycle: Vomit Ichor (10 Slimed) -> Furious Pummeling (4 x4) -> Leeching Hug (3 Weak, +3 Str) -> Smother (30). */
public final class SlimedBerserker {
    private SlimedBerserker() {}
    static final int SLIME = 0x7fc04a, SLIME_D = 0x4a8a2a, SKIN = 0x9a6a6a, ICHOR = 0xb8e060, METAL = 0x5a5e66;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_slimed_berserker");
        Kit.biped(m, SKIN, SLIME, SLIME_D, METAL);
        m.scale(1.6f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-5.5f, 0, -3.5f, 11, 10, 7).color(SKIN).grad(0xb07a7a, 0x7a4a4a).noise(0.15f).bevel(0.15f);   // hulking torso
        torso.box(-5.8f, 4, -3.8f, 11, 7, 8).color(SLIME).grad(0x9ad45a, SLIME_D).noise(0.12f).inflate(0.1f);   // slime coat
        for (int i = 0; i < 4; i++) torso.box(-5 + i * 3, -3, -3.9f, 2, 5, 1).color(SLIME).flat().noise(0.05f);  // drips
        body.box(-5, -2, -3, 10, 5, 6).color(0x4a3a30).noise(0.1f);
        head.box(-3.5f, 0, -3, 7, 6, 6).color(SKIN).noise(0.12f);
        head.box(-3.8f, 3, -3.3f, 7, 4, 7).color(SLIME).grad(0x9ad45a, SLIME_D).noise(0.12f);                   // slime hair
        head.box(-3, 0.5f, 2.9f, 6, 2, 1).color(0x201010).glow(Face.FRONT, 0, 0, 6, 1, 0xffe040).px(Face.FRONT, 1, 1, 0x201010);
        Part jaw = head.add("jaw").at(0, 1, 0);
        jaw.box(-3, -2, -3, 6, 2, 6).color(0x7a4a4a); jaw.box(-2.5f, 0, 2.2f, 5, 1, 1).color(0xf0eee0).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part sp = torso.add("spike" + s).at(s * 6.5f, 9, 0);
            sp.box(-1.5f, 0, -1.5f, 3, 4, 3).color(METAL).bevel(0.2f); sp.box(-0.5f, 4, -0.5f, 1, 3, 1).color(0x9aa0aa).flat();
            Part hand = m.byName.get(s < 0 ? "handR" : "handL");
            hand.box(-2, -4, -2, 4, 4, 4).color(SKIN).noise(0.12f); hand.box(-2.3f, -3, -2.3f, 4, 3, 5).color(SLIME).noise(0.1f).inflate(0.1f);
            m.byName.get(s < 0 ? "armR" : "armL").box(-2, -6, -2, 4, 5, 4).color(SKIN).noise(0.12f).inflate(0.2f);
        }
        Kit.bipedBase(m);
        m.proc((r, c) -> { r.scl("torso", 1f, 1f + 0.02f * (float) Math.sin(c.age * 0.3f), 1f); });
        Clip v = m.clip("vomit_ichor", 36);
        Kit.seq(v, "torso", 36, 18, 8, 15, 0, 0, 16, -24, 0, 0, 18, 28, 0, 0, 28, 10, 0, 0);
        Kit.seq(v, "head", 36, 18, 8, 25, 0, 0, 16, -45, 0, 0, 18, 35, 0, 0);
        Kit.seq(v, "jaw", 36, -1, 12, 10, 0, 0, 16, 55, 0, 0, 30, 50, 0, 0);
        Kit.seq(v, "armR", 36, -1, 8, 20, 0, 10, 18, 40, 0, 10);
        Kit.seq(v, "armL", 36, -1, 8, 20, 0, -10, 18, 40, 0, -10);
        Kit.jabs(m, "furious_pummeling", 42, 12, 17, 22, 27);
        Clip h = m.clip("leeching_hug", 40);
        Kit.seq(h, "armR", 40, -1, 8, -50, 40, 20, 16, -85, -55, 10, 30, -85, -55, 10);
        Kit.seq(h, "armL", 40, -1, 8, -50, -40, -20, 16, -85, 55, -10, 30, -85, 55, -10);
        Kit.seq(h, "torso", 40, -1, 8, -8, 0, 0, 16, 20, 0, 0, 30, 20, 0, 0);
        Kit.seq(h, "head", 40, -1, 8, -10, 0, 0, 16, 15, 0, 0);
        Kit.overhead(m, "smother", 38, 20);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_slimed_berserker", "Slimed Berserker", "スライム狂戦士")
            .game(2, 3).size(1.2f, 2.9f).hp(266).speed(0.22f).style(Style.SLIME).egg(0x7fc04a, 0x9a6a6a)
            .model(SlimedBerserker::model)
            .spawn(4, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(Pattern.cycle("vomit_ichor", "furious_pummeling", "leeching_hug", "smother"))
            .move(Move.of("vomit_ichor", 36).intent(Intent.DEBUFF).reach(8f).ranged(8)
                .sound(16, SoundEvents.SLIME_SQUISH, 1.4f, 0.5f)
                .at(18, (s, t) -> {
                    for (int i = 0; i < 3; i++) Fx.particlesAt(s, ParticleTypes.ITEM_SLIME, Fx.rel(s, 1.5 + i * 0.5, 0, 2.2), 12, 0.4, 0.1);
                    if (t != null) { Fx.curse(t, Fx.Curse.SLIMED); Kit.effect(t, MobEffects.MOVEMENT_SLOWDOWN, 8, 1); }
                }))
            .move(Move.of("furious_pummeling", 42).intent(Intent.ATTACK, 4, 4).reach(2.8f)
                .at(12, Kit.hit(4)).at(17, Kit.hit(4)).at(22, Kit.hit(4)).at(27, Kit.hit(4))
                .sound(12, SoundEvents.SLIME_ATTACK, 1f, 0.6f).sound(17, SoundEvents.SLIME_ATTACK, 1f, 0.6f).sound(22, SoundEvents.SLIME_ATTACK, 1f, 0.6f).sound(27, SoundEvents.SLIME_ATTACK, 1f, 0.6f))
            .move(Move.of("leeching_hug", 40).intent(Intent.DEBUFF).reach(3.0f)
                .sound(16, SoundEvents.SLIME_SQUISH, 1.2f, 0.6f)
                .at(16, (s, t) -> {
                    for (var v : Fx.foes(s, s.getBoundingBox().inflate(3.2, 1, 3.2))) Fx.weak(v, 3 * Kit.TURN);
                    Kit.str(s, 3); Fx.particles(s, ParticleTypes.ITEM_SLIME, 20, 0.8);
                }))
            .move(Move.of("smother", 38).intent(Intent.ATTACK, 30, 1).reach(3.2f)
                .sound(18, SoundEvents.SLIME_JUMP, 1.4f, 0.4f).at(20, Fx.melee(30, 3.6, 110)));
    }
}
