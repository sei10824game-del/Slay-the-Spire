package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Mugger. 48-52 HP. Mug 10 (steals gold) x2, then 50% Smoke Bomb (11 Block) / 50% Big Swipe 16 (steals gold, then Smoke Bomb), then Escape.
 */
public final class Mugger {
    private Mugger() {}
    static final int CLOAK = 0x3a3f4a, CLOAK_D = 0x1f2229, SKIN = 0xc79a76, SACK = 0x9a7a42, SCARF = 0xb33a3a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("mugger");
        Bip b = Bip.make(m, SKIN, CLOAK, CLOAK_D, 0x2a2d35, 0x201a14, 8, 8, 12, 9, 6);
        b.head.box(-3.6f, 2.5f, -3.6f, 7, 5, 7).color(CLOAK_D).grad(CLOAK, CLOAK_D);              // hood
        b.head.box(-3.6f, -0.5f, -4.6f, 7, 7, 2).color(CLOAK_D);
        b.head.box(-3.2f, -0.2f, 2.6f, 6, 3, 1).color(SCARF).bevel(0.1f);          // mask scarf
        b.head.box(-2.5f, 3.2f, 2.9f, 5, 1, 1).color(0x1a1010).glow(Face.FRONT, 0, 0, 1, 1, 0xffe060).glow(Face.FRONT, 4, 0, 1, 1, 0xffe060);
        b.torso.box(-4.3f, 0, -2.8f, 9, 2, 6).color(SCARF).flat().inflate(0.05f);   // sash
        Part cape = b.torso.add("cape").at(0, 10, -2.8f);
        cape.box(-4, -11, -1, 8, 11, 1).color(CLOAK_D).grad(CLOAK, CLOAK_D);
        Part sack = b.torso.add("sack").at(0, 4, -3);
        sack.box(-3, -3, -3, 6, 7, 3).color(SACK).grad(0xb8944f, 0x6a5228).noise(0.1f).bevel(0.2f);
        sack.box(-1.5f, 4, -2.5f, 3, 1, 2).color(0x5a4020);
        Part dag = b.handR.add("dagger").at(0, -2, 0).rot(-90, 0, 0);
        dag.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
        dag.box(-1.5f, 2, -0.5f, 3, 1, 1).color(0x8a8a90).flat();
        dag.box(-0.5f, 3, -0.4f, 1, 6, 1).color(0xc9cfd6).flat();
        m.clip("mug", 28)    // lunge in, slash, grab
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -10, -25, 0).rot("torso", 12, 24, 25, 0, Ease.IN_CUBIC).rot("torso", 28, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -2, -3).pos("body", 12, 0, -2, 6, Ease.IN_CUBIC).pos("body", 28, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, 35, 0, 25).rot("armR", 12, -110, -15, -5, Ease.IN_CUBIC).rot("armR", 18, -105, -15, -5).rot("armR", 28, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -40, 0, -20).rot("armL", 14, -90, 0, 15, Ease.IN_CUBIC).rot("armL", 20, -30, 0, 0).rot("armL", 28, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 12, -45, 0, 0).rot("legR", 28, 0, 0, 0).rot("legL", 0, 0, 0, 0).rot("legL", 12, 30, 0, 0).rot("legL", 28, 0, 0, 0);
        m.clip("big_swipe", 36)  // big overhead wind-up, heavy diagonal slash
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, -18, -30, 0).rot("torso", 18, 28, 35, 0, Ease.IN_CUBIC).rot("torso", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 1, -4).pos("body", 18, 0, -2, 7, Ease.IN_CUBIC).pos("body", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -170, 0, 20).rot("armR", 18, -40, 0, -20, Ease.IN_CUBIC).rot("armR", 28, -35, 0, -20).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -120, 0, -20).rot("armL", 18, 20, 0, -10).rot("armL", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -15, 0, 0).rot("head", 18, 15, 0, 0).rot("head", 36, 0, 0, 0);
        m.clip("smoke_bomb", 36)  // fling a bomb to the ground, crouch behind the smoke
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -10, 0, 0).rot("torso", 16, 30, 0, 0, Ease.IN_CUBIC).rot("torso", 36, 15, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -150, 0, 15).rot("armR", 16, -20, 0, 0, Ease.IN_CUBIC).rot("armR", 36, -60, 0, 30)
            .rot("armL", 0, 0, 0, 0).rot("armL", 16, -80, 0, -30).rot("armL", 36, -80, 0, -30)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, -2, 2).pos("body", 36, 0, -2, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 16, 20, 0, 0).rot("head", 36, 20, 0, 0);
        m.clip("escape", 24)      // crouch and spring away
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 30, 0, 0).rot("torso", 16, 10, 0, 0).rot("torso", 24, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -3, 0).pos("body", 16, 0, 5, -4, Ease.OUT_CUBIC)
            .rot("armR", 0, 0, 0, 0).rot("armR", 16, 60, 0, 10).rot("armL", 0, 0, 0, 0).rot("armL", 16, 60, 0, -10)
            .rot("legR", 0, 0, 0, 0).rot("legR", 16, 50, 0, 0).rot("legL", 0, 0, 0, 0).rot("legL", 16, -30, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("mugger", "Mugger", "マガー")
            .game(1, 2).size(0.6f, 1.75f).hp(50).speed(0.3f).style(Style.PILLAGER).egg(0x3a3f4a, 0xb33a3a)
            .model(Mugger::model).spawn(5, 1, 1, MobDef.Habitat.FOREST)
            .pattern(s -> {
                switch (s.getTurn()) {
                    case 0: case 1: return "mug";
                    case 2: return s.rand().nextBoolean() ? "smoke_bomb" : "big_swipe";
                    case 3: return s.lastMove().equals("big_swipe") ? "smoke_bomb" : "escape";
                    default: return "escape";
                }
            })
            .move(Move.of("mug", 28).intent(Intent.ATTACK, 10, 1).reach(2.4f)
                .sound(9, SoundEvents.PHANTOM_FLAP, 0.8f, 1.6f)
                .at(12, (s, t) -> Fx.meleeNow(s, 10, 2.8, 80, v -> Fx.steal(s, v, 3))))
            .move(Move.of("big_swipe", 36).intent(Intent.ATTACK, 16, 1).reach(2.6f)
                .sound(14, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.8f)
                .at(18, (s, t) -> Fx.meleeNow(s, 16, 3.0, 100, v -> Fx.steal(s, v, 6))))
            .move(Move.of("smoke_bomb", 36).intent(Intent.DEFEND).noTarget()
                .at(16, (s, t) -> { s.addBlock(11); Fx.particles(s, ParticleTypes.LARGE_SMOKE, 30, 0.8); Fx.sound(s, SoundEvents.FIRECHARGE_USE, 1f, 0.8f); }))
            .move(Move.of("escape", 24).intent(Intent.ESCAPE).noTarget().at(18, (s, t) -> Fx.vanish(s)));
    }
}
