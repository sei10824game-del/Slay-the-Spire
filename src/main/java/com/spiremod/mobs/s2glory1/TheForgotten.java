package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;

/** StS2 Glory: The Forgotten. 106 HP, Possess Speed. Alternates Miasma (steal 2 Dexterity, 8 Block, +2 Dex) and Dread (13 + its Dexterity). */
public final class TheForgotten {
    private TheForgotten() {}
    static final int CLOAK = 0x5a4a78, CLOAK_D = 0x30284a, BONE = 0xd8d0c0, EYE = 0xff8040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_the_forgotten");
        Kit.biped(m, BONE, CLOAK, CLOAK_D, 0x8a7aa8);
        m.scale(1.5f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.5f, 0, -3, 9, 9, 6).color(CLOAK).grad(0x7a6a98, CLOAK_D).noise(0.12f);
        body.box(-5, -9, -3.2f, 10, 11, 6).color(CLOAK_D).grad(CLOAK, 0x181228).noise(0.12f);
        body.box(-4, -14, -2.5f, 8, 6, 5).color(0x181228).grad(CLOAK_D, 0x100c1c);
        head.box(-3, 0, -3, 6, 6, 6).color(BONE).bevel(0.15f);
        head.box(-4.5f, -1, -4, 9, 9, 8).color(CLOAK_D).grad(CLOAK, CLOAK_D).noise(0.1f);                   // deep hood
        head.box(-2.5f, 1, 3.5f, 5, 4, 1).color(0x0a0612).glow(Face.FRONT, 0, 1, 1, 1, EYE).glow(Face.FRONT, 4, 1, 1, 1, EYE);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = m.byName.get(s < 0 ? "armR" : "armL");
            arm.box(-2, -8, -2, 4, 3, 4).color(CLOAK_D).inflate(0.2f);                                       // heavy sleeves
            Part hand = m.byName.get(s < 0 ? "handR" : "handL");
            for (int i = 0; i < 3; i++) hand.box(-1.5f + i, -6, -0.5f, 1, 3, 1).color(BONE).flat();         // claws
        }
        torso.box(-4.7f, 6, -3.2f, 9, 1, 1).color(0x8a7aa8).flat();                                          // chain
        torso.box(-1, 2, -3.3f, 2, 4, 1).color(0x8a7aa8).flat().glow(Face.FRONT, 0, 1, 2, 2, EYE);         // amulet
        Kit.bipedBase(m);
        m.proc((r, c) -> { r.show("legR", false); r.show("legL", false); r.pos("body", 0, 1.2f + (float) Math.sin(c.age * 0.1f + 1) * 1f, 0); });
        Clip mi = Kit.buff(m, "miasma", 40, 17);
        Kit.sweep(m, "dread", 30, 15);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_the_forgotten", "The Forgotten", "フォーゴットン")
            .game(2, 3).size(0.95f, 2.6f).hp(106).speed(0.22f).style(Style.GHOST).egg(0x5a4a78, 0xff8040)
            .model(TheForgotten::model)
            .spawn(4, 1, 1, MobDef.Habitat.TAIGA)
            .pattern(Pattern.cycle("miasma", "dread"))
            .move(Move.of("miasma", 40).intent(Intent.DEFEND_DEBUFF).reach(8f).ranged(8)
                .sound(14, SoundEvents.SOUL_ESCAPE, 1.2f, 0.4f)
                .at(17, (s, t) -> {
                    Fx.particles(s, ParticleTypes.SMOKE, 35, 1.1);
                    if (t != null) Kit.effect(t, MobEffects.MOVEMENT_SLOWDOWN, 2 * Kit.TURN * 2, 0);
                    Kit.block(s, 8); s.addStrength(2); // Dexterity stands in as Strength (Dread = 13 + Dex)
                }))
            .move(Move.of("dread", 30).intent(Intent.ATTACK, 13, 1).reach(3.0f)
                .sound(12, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.6f).at(15, Fx.melee(13, 3.2, 100)))
            .onDeath((s, src) -> { for (var v : Fx.foes(s, new AABB(s.blockPosition()).inflate(32))) v.removeEffect(MobEffects.MOVEMENT_SLOWDOWN); });
    }
}
