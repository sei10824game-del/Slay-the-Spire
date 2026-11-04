package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 3 Dagger (Reptomancer minion, 20-25 HP). Turn 1 Stab (9 + Wound), then Explode (25) and dies. Floating blade, tip toward +z. */
public final class Dagger {
    private Dagger() {}
    static final int BLADE = 0xb9c4d4, BLADE_D = 0x6c7a8e, GUARD = 0x6a3fa0, GRIP = 0x2e2236, GEM = 0x8aff6a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("dagger");
        Part body = m.add("body").at(0, 9, 0);
        Part k = body.add("k").at(0, 0, 0);
        k.box(-1, -0.5f, -2, 2, 1, 9).color(BLADE).grad(0xe6edf6, BLADE_D).bevel(0.15f);        // blade
        k.box(-0.5f, -1, -2, 1, 2, 9).color(BLADE_D).flat();                                // ridge
        k.box(-0.5f, -0.5f, 7, 1, 1, 2).color(0xe6edf6).flat();                                // tip
        k.box(-3, -1, -3, 6, 2, 2).color(GUARD).bevel(0.15f);                               // guard
        k.box(-1, -1, -7, 2, 2, 4).color(GRIP).noise(0.1f);                        // grip
        k.box(-1.2f, -1.2f, -9, 2, 2, 2).color(GRIP).bevel(0.2f).glow(Face.BACK, 0, 0, 2, 2, GEM); // pommel gem
        m.bob(1.5f, 0.15f);
        m.proc((r, c) -> r.rot("k", 0, 0, c.age * 3));
        m.clip("idle", 30).loop().rot("body", 0, -6, 0, 0).rot("body", 15, 6, 0, 0).rot("body", 30, -6, 0, 0);
        m.clip("walk", 20).loop().rot("body", 0, 12, 0, 0).rot("body", 10, 16, 0, 0).rot("body", 20, 12, 0, 0);
        // STAB: pull back, thrust
        m.clip("stab", 24)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1, -6).pos("body", 11, 0, 0, 8, Ease.IN_CUBIC).pos("body", 24, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -25, 0, 0).rot("body", 11, 15, 0, 0).rot("body", 24, 0, 0, 0);
        // EXPLODE: shudder, swell, burst
        m.clip("explode", 30)
            .scl("body", 0, 1, 1, 1).scl("body", 8, 0.8f, 0.8f, 0.8f).scl("body", 22, 1.9f, 1.9f, 1.9f, Ease.IN_QUAD).scl("body", 30, 2.4f, 2.4f, 2.4f)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 0, 0, 12).rot("body", 12, 0, 0, -12).rot("body", 14, 0, 0, 12).rot("body", 16, 0, 0, -12).rot("body", 18, 0, 0, 12).rot("body", 20, 0, 0, -12)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, 2, 0);
        m.clip("hurt", 8).rot("body", 0, 0, 0, 0).rot("body", 3, -25, 0, 15).rot("body", 8, 0, 0, 0);
        m.clip("death", 20).pos("body", 0, 0, 0, 0).pos("body", 20, 0, -8, 0, Ease.OUT_BOUNCE).rot("body", 0, 0, 0, 0).rot("body", 20, 70, 0, 30);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("dagger", "Dagger", "ダガー")
            .game(1, 3).size(0.5f, 0.7f).hp(22).speed(0.26f).style(Style.METAL).egg(0xb9c4d4, 0x6a3fa0)
            .model(Dagger::model).flying().minion()
            .pattern(Pattern.seq(1, "stab", "explode"))
            .move(Move.of("stab", 24).intent(Intent.ATTACK_DEBUFF, 9, 1).reach(2.2f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.6f)
                .at(11, Fx.melee(9, 2.6, 70, v -> Fx.curse(v, Fx.Curse.WOUND))))
            .move(Move.of("explode", 30).intent(Intent.ATTACK, 25, 1).reach(3f)
                .sound(6, SoundEvents.CREEPER_PRIMED, 1f, 1.4f)
                .at(24, (s, t) -> {
                    Fx.aoe(25, 3.0).run(s, t);
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.8f, 1.5f);
                    Fx.particles(s, ParticleTypes.EXPLOSION, 3, 0.5);
                    Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 20, 0.5);
                    s.discard();
                }));
    }
}
