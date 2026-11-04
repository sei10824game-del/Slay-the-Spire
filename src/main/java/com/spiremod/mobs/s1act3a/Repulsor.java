package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 3 Repulsor (29-35 HP). 80% Repulse (2 Dazed), 20% Bash (11), never Bash twice. Hovering violet crystal with a ring and one eye. */
public final class Repulsor {
    private Repulsor() {}
    static final int SH = 0x5b4a7a, SH_D = 0x302642, SH_L = 0x9a86c4, EYE = 0xb8ff6a, RING = 0xd8c070;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("repulsor");
        Part body = m.add("body").at(0, 14, 0);
        Spiker.ball(body, 5, SH, SH_L, SH_D);
        body.box(-2, 5, -2, 4, 3, 4).color(SH_L).bevel(0.2f);                       // crown crystal
        body.box(-1, -8, -1, 2, 3, 2).color(SH_D).bevel(0.2f);                      // keel
        Part eye = body.add("eye").at(0, 0, 6);
        eye.box(-3, -2, 0, 6, 4, 1).color(0x14101c).flat().glow(Face.FRONT, 1, 1, 4, 2, EYE).glow(Face.FRONT, 2, 0, 2, 4, EYE);
        Part ring = body.add("ring").at(0, 0, 0);
        ring.box(-9, -0.5f, -9, 18, 1, 2).color(RING).flat().bevel(0.1f);
        ring.box(-9, -0.5f, 7, 18, 1, 2).color(RING).flat().bevel(0.1f);
        ring.box(-9, -0.5f, -7, 2, 1, 14).color(RING).flat().bevel(0.1f);
        ring.box(7, -0.5f, -7, 2, 1, 14).color(RING).flat().bevel(0.1f);
        for (int s = -1; s <= 1; s += 2) {                                          // side fins
            Part fin = body.add(s < 0 ? "finR" : "finL").at(s * 6, 0, -1);
            fin.box(s < 0 ? -4 : 0, -3, -1, 4, 6, 2).color(SH_D).bevel(0.15f);
            fin.box(s < 0 ? -6 : 4, -1, -0.5f, 2, 2, 1).color(RING).flat();
        }
        m.bob(1.5f, 0.12f);
        m.proc((r, c) -> r.rot("ring", 0, c.age * 5, 0));
        m.clip("idle", 40).loop().rot("body", 0, 0, 0, 0).rot("body", 20, 3, 6, 0).rot("body", 40, 0, 0, 0)
            .rot("finR", 0, 0, 0, 8).rot("finR", 20, 0, 0, -8).rot("finR", 40, 0, 0, 8)
            .rot("finL", 0, 0, 0, -8).rot("finL", 20, 0, 0, 8).rot("finL", 40, 0, 0, -8);
        m.clip("walk", 20).loop().rot("body", 0, 8, 0, 0).rot("body", 10, 12, 0, 0).rot("body", 20, 8, 0, 0);
        // REPULSE: draw in (shrink, fins fold), then blast outward, ring flares
        m.clip("repulse", 34)
            .scl("body", 0, 1, 1, 1).scl("body", 12, 0.8f, 0.8f, 0.8f).scl("body", 16, 1.35f, 1.35f, 1.35f, Ease.OUT_BACK).scl("body", 34, 1, 1, 1)
            .scl("ring", 0, 1, 1, 1).scl("ring", 12, 0.7f, 1, 0.7f).scl("ring", 16, 1.7f, 1, 1.7f, Ease.OUT_BACK).scl("ring", 34, 1, 1, 1)
            .rot("finR", 0, 0, 0, 0).rot("finR", 12, 0, 0, 40).rot("finR", 16, 0, 0, -45, Ease.OUT_BACK).rot("finR", 34, 0, 0, 0)
            .rot("finL", 0, 0, 0, 0).rot("finL", 12, 0, 0, -40).rot("finL", 16, 0, 0, 45, Ease.OUT_BACK).rot("finL", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 2, -2).pos("body", 16, 0, -1, 3).pos("body", 34, 0, 0, 0);
        // BASH: tilt back, ram forward
        m.clip("bash", 28)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 3, -5).pos("body", 13, 0, -2, 10, Ease.IN_CUBIC).pos("body", 28, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 9, -30, 0, 0).rot("body", 13, 40, 0, 0, Ease.IN_CUBIC).rot("body", 28, 0, 0, 0)
            .rot("finR", 0, 0, 0, 0).rot("finR", 9, 0, 0, 30).rot("finR", 13, 0, 0, -20).rot("finR", 28, 0, 0, 0)
            .rot("finL", 0, 0, 0, 0).rot("finL", 9, 0, 0, -30).rot("finL", 13, 0, 0, 20).rot("finL", 28, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -22, 0, 12).rot("body", 10, 0, 0, 0);
        m.clip("death", 28).pos("body", 0, 0, 0, 0).pos("body", 8, 0, 2, 0).pos("body", 22, 0, -12, 0, Ease.IN_QUAD).pos("body", 28, 0, -12, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 22, 50, 80, 30).scl("body", 0, 1, 1, 1).scl("body", 8, 1.3f, 1.3f, 1.3f).scl("body", 28, 0.5f, 0.5f, 0.5f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("repulsor", "Repulsor", "リパルサー")
            .game(1, 3).size(0.9f, 1.4f).hp(32).speed(0.22f).style(Style.SHULKER).egg(0x5b4a7a, 0xb8ff6a)
            .model(Repulsor::model).flying()
            .spawn(6, 1, 2, MobDef.Habitat.MOUNTAIN)
            .pattern(Pattern.random().add("repulse", 80).add("bash", 20).maxRepeat("bash", 1))
            .move(Move.of("repulse", 34).intent(Intent.DEBUFF).ranged(10)
                .sound(10, SoundEvents.BEACON_POWER_SELECT, 1f, 1.4f)
                .at(16, (s, t) -> {
                    Fx.ring(s, ParticleTypes.CLOUD, s.position().add(0, 1.1, 0), 2.0, 24);
                    if (t != null) { Fx.curse(t, Fx.Curse.DAZED); Fx.curse(t, Fx.Curse.DAZED); Fx.knock(s, t, 0.4); }
                }))
            .move(Move.of("bash", 28).intent(Intent.ATTACK, 11, 1).reach(2.2f)
                .sound(8, SoundEvents.PHANTOM_SWOOP, 1f, 1.2f)
                .at(13, Fx.melee(11, 2.6, 80)));
    }
}
