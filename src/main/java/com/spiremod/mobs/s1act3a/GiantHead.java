package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 Giant Head (500 HP, elite). Slow: takes 10% more damage per hit this turn. Glare (Weak), Count (13),
 * then from turn 4 "It Is Time" 30 growing by 5 each use. Colossal floating stone face.
 */
public final class GiantHead {
    private GiantHead() {}
    static final int STONE = 0x8a8478, STONE_D = 0x4a463e, STONE_L = 0xbab4a2, MOSS = 0x5a6a3c, EYE = 0xffd060;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("giant_head");
        m.scale(1.4f);
        Part head = m.add("head").at(0, 22, 0);
        head.box(-14, -14, -12, 28, 30, 24).color(STONE).grad(STONE_L, STONE_D).noise(0.14f).bevel(0.2f);
        head.box(-8, 16, -8, 16, 3, 16).color(STONE).noise(0.1f);                                // crown
        head.box(-3, 19, -3, 6, 3, 6).color(MOSS).noise(0.2f);
        head.box(-15, -10, -4, 1, 14, 10).color(STONE_D).noise(0.1f);                             // ears
        head.box(14, -10, -4, 1, 14, 10).color(STONE_D).noise(0.1f);
        head.box(-3, -3, 12, 6, 9, 3).color(STONE_L).bevel(0.2f).noise(0.1f);                    // nose
        for (int s = -1; s <= 1; s += 2) {
            Part eye = head.add(s < 0 ? "eyeR" : "eyeL").at(s * 7, 6, 12);
            eye.box(-3.5f, -3, 0, 7, 6, 1).color(0x1a1610).flat().glow(Face.FRONT, 2, 1, 3, 4, EYE).glow(Face.FRONT, 1, 2, 5, 2, EYE);
            Part lid = eye.add(s < 0 ? "lidR" : "lidL").at(0, 3, 0.5f);
            lid.box(-4, -2, 0, 8, 2, 1).color(STONE_D).noise(0.1f);
            Part brow = head.add(s < 0 ? "browR" : "browL").at(s * 7, 11, 12);
            brow.box(-5, 0, 0, 10, 2, 2).color(STONE_D).bevel(0.15f);
        }
        Part jaw = head.add("jaw").at(0, -6, 0);
        jaw.box(-11, -8, -10, 22, 8, 22).color(STONE).grad(STONE, STONE_D).noise(0.14f).bevel(0.2f);
        jaw.box(-9, -1, 11, 18, 2, 1).color(0x120e0a).flat();                                    // mouth slit
        for (int i = 0; i < 6; i++) jaw.box(-8.5f + i * 3, 0, 11, 2, 2, 1).color(STONE_L).flat();
        m.look("head", 25, 15);
        m.bob(1.5f, 0.06f);
        m.clip("idle", 80).loop().rot("head", 0, 0, 0, 0).rot("head", 40, 2, 4, 0).rot("head", 80, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 40, 4, 0, 0).rot("jaw", 80, 0, 0, 0);
        m.clip("walk", 30).loop().rot("head", 0, 0, 0, 3).rot("head", 15, 0, 0, -3).rot("head", 30, 0, 0, 3);
        m.clip("glare", 36)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 14, 0, 0).rot("head", 16, -12, 0, 0, Ease.OUT_BACK).rot("head", 36, 0, 0, 0)
            .scl("head", 0, 1, 1, 1).scl("head", 16, 1.08f, 1.08f, 1.08f).scl("head", 36, 1, 1, 1)
            .pos("browR", 0, 0, 0, 0).pos("browR", 10, 0, -2, 0).pos("browR", 28, 0, -2, 0).pos("browR", 36, 0, 0, 0)
            .pos("browL", 0, 0, 0, 0).pos("browL", 10, 0, -2, 0).pos("browL", 28, 0, -2, 0).pos("browL", 36, 0, 0, 0);
        m.clip("count", 40)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -10, 0, 0).rot("head", 15, 16, 0, 0, Ease.IN_CUBIC).rot("head", 40, 0, 0, 0)
            .pos("head", 0, 0, 0, 0).pos("head", 10, 0, 2, -5).pos("head", 15, 0, -2, 12, Ease.IN_CUBIC).pos("head", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 25, 0, 0).rot("jaw", 15, -4, 0, 0, Ease.IN_CUBIC).rot("jaw", 40, 0, 0, 0);
        m.clip("it_is_time", 56)
            .rot("head", 0, 0, 0, 0).rot("head", 18, -22, 0, 0).rot("head", 28, -26, 0, 0).rot("head", 32, 30, 0, 0, Ease.IN_CUBIC).rot("head", 56, 0, 0, 0)
            .pos("head", 0, 0, 0, 0).pos("head", 18, 0, 8, -8).pos("head", 28, 0, 10, -8).pos("head", 32, 0, -6, 16, Ease.IN_CUBIC).pos("head", 56, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 18, 35, 0, 0).rot("jaw", 28, 38, 0, 0).rot("jaw", 32, -6, 0, 0, Ease.IN_CUBIC).rot("jaw", 56, 0, 0, 0)
            .scl("head", 0, 1, 1, 1).scl("head", 28, 1.12f, 1.12f, 1.12f).scl("head", 32, 1.2f, 1.2f, 1.2f).scl("head", 56, 1, 1, 1);
        m.clip("hurt", 10).rot("head", 0, 0, 0, 0).rot("head", 3, -8, 0, 4).rot("head", 10, 0, 0, 0);
        m.clip("death", 44).pos("head", 0, 0, 0, 0).pos("head", 14, 0, 4, 0).pos("head", 44, 0, -20, 0, Ease.OUT_BOUNCE)
            .rot("head", 0, 0, 0, 0).rot("head", 44, 30, 0, 25).rot("jaw", 0, 0, 0, 0).rot("jaw", 30, 35, 0, 0)
            .scl("head", 0, 1, 1, 1).scl("head", 44, 1, 0.85f, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("giant_head", "Giant Head", "巨大な頭")
            .game(1, 3).size(2.6f, 3.2f).hp(500).speed(0.15f).style(Style.METAL).egg(0x8a8478, 0xffd060).elite()
            .model(GiantHead::model).flying()
            .spawn(1, 1, 1, MobDef.Habitat.MOUNTAIN)
            .pattern(s -> {
                if (s.getTurn() >= 3) return "it_is_time";
                if (s.repeatCount(s.lastMove()) >= 2) return s.lastMove().equals("glare") ? "count" : "glare";
                return s.rand().nextBoolean() ? "glare" : "count";
            })
            .move(Move.of("glare", 36).intent(Intent.DEBUFF).ranged(10)
                .sound(10, SoundEvents.WARDEN_HEARTBEAT, 1.5f, 0.6f)
                .at(16, (s, t) -> { if (t != null) { Fx.weak(t, 6); Fx.particlesAt(s, ParticleTypes.SOUL_FIRE_FLAME, t.position().add(0, 1, 0), 12, 0.4, 0.02); } }))
            .move(Move.of("count", 40).intent(Intent.ATTACK, 13, 1).reach(3.6f)
                .sound(10, SoundEvents.WARDEN_ATTACK_IMPACT, 1f, 0.7f)
                .at(15, Fx.melee(13, 4.0, 110)))
            .move(Move.of("it_is_time", 56).intent(Intent.ATTACK, 30, 1).reach(3.8f)
                .sound(14, SoundEvents.WARDEN_SONIC_CHARGE, 1.5f, 0.7f).sound(32, SoundEvents.GENERIC_EXPLODE, 0.7f, 0.5f)
                .at(32, (s, t) -> { Fx.melee(30 + 5 * s.count("time"), 4.2, 120).run(s, t); s.inc("time"); }))
            .onHurt((s, src, amt) -> { int n = s.count("slow"); s.setCount("slow", n + 1); return amt * (1f + 0.1f * n); })   // Slow
            .onMoveEnd(s -> s.setCount("slow", 0));
    }
}
