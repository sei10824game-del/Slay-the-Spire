package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Living Fog minion: Gas Bomb (7 HP). Explode: 8 damage, then dies. */
public final class GasBomb {
    private GasBomb() {}
    static final int GAS = 0xa6c85a, GAS_D = 0x5c7a30, SKULL = 0xe8e8d0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_gas_bomb");
        Part body = m.add("body").at(0, 5, 0);
        body.cbox(-5, 10, 10, 10).color(GAS).grad(0xc8e87a, GAS_D).noise(0.2f).bevel(0.8f);
        body.cbox(-4, 8, 11, 8).color(GAS_D).noise(0.2f).bevel(0.6f).inflate(0.2f);
        body.box(-3.5f, -1, 5.2f, 7, 3, 1).color(0x1a2a14).flat().glow(Face.FRONT, 0, 0, 3, 3, 0xe8ff70).glow(Face.FRONT, 4, 0, 3, 3, 0xe8ff70);   // eyes
        body.box(-3, -4, 5.2f, 6, 2, 1).color(SKULL).flat().paint(Face.FRONT, 1, 0, 1, 2, 0x1a2a14).paint(Face.FRONT, 3, 0, 1, 2, 0x1a2a14);        // jagged mouth
        Part fuse = body.add("fuse").at(0, 5, 0);
        fuse.box(-1, 0, -1, 2, 3, 2).color(0x3a3a3a).bevel(0.2f);
        fuse.box(-0.5f, 3, -0.5f, 1, 3, 1).color(0x8a6a3a);
        fuse.box(-1, 6, -1, 2, 2, 2).color(0xffa030).glow(Face.FRONT, 0, 0, 2, 2, 0xffd040);
        for (int i = 0; i < 4; i++) {
            Part w = m.add("wisp" + i).at(0, 0, 0);
            w.box(-1.5f, -1.5f, -1.5f, 3, 3, 3).color(GAS).noise(0.3f).bevel(0.5f);
        }
        m.proc((r, c) -> { for (int i = 0; i < 4; i++) { double a = c.age * 0.12 + i * Math.PI / 2; r.pos("wisp" + i, (float) Math.cos(a) * 7, 4 + (float) Math.sin(c.age * 0.15 + i) * 2, (float) Math.sin(a) * 7); } });
        m.bob(1.5f, 0.12f);
        m.clip("idle", 20).loop().scl("body", 0, 1, 1, 1).scl("body", 10, 1.08f, 0.94f, 1.08f).scl("body", 20, 1, 1, 1)
            .rot("fuse", 0, 0, 0, 10).rot("fuse", 10, 0, 0, -10).rot("fuse", 20, 0, 0, 10);
        m.clip("walk", 16).loop().pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1.5f, 0).pos("body", 16, 0, 0, 0);
        m.clip("explode", 30)
            .scl("body", 0, 1, 1, 1).scl("body", 8, 0.8f, 0.8f, 0.8f).scl("body", 14, 1.3f, 1.3f, 1.3f).scl("body", 20, 1.7f, 1.7f, 1.7f, Ease.OUT_BACK).scl("body", 26, 0.1f, 0.1f, 0.1f, Ease.IN_CUBIC)
            .rot("body", 0, 0, 0, 0).rot("body", 4, 0, 0, 6).rot("body", 6, 0, 0, -6).rot("body", 8, 0, 0, 6).rot("body", 10, 0, 0, -6).rot("body", 12, 0, 0, 6).rot("body", 14, 0, 0, -6).rot("body", 18, 0, 0, 6).rot("body", 22, 0, 0, -6)
            .scl("fuse", 0, 1, 1, 1).scl("fuse", 10, 0.5f, 0.5f, 0.5f);
        U.hurt(m, "body");
        m.clip("death", 14).scl("body", 0, 1, 1, 1).scl("body", 5, 1.5f, 1.5f, 1.5f, Ease.OUT_BACK).scl("body", 14, 0.05f, 0.05f, 0.05f, Ease.IN_CUBIC);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_gas_bomb", "Gas Bomb", "ガスボム")
            .game(2, 1).size(0.6f, 0.8f).hp(7).speed(0.3f).flying().minion().style(Style.GHOST).egg(0xa6c85a, 0xffa030)
            .model(GasBomb::model)
            .pattern(Pattern.cycle("explode"))
            .move(Move.of("explode", 30).intent(Intent.ATTACK, 8, 1).reach(2.2f)
                .sound(8, SoundEvents.TNT_PRIMED, 1f, 1.4f)
                .at(22, (s, t) -> { Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.7f, 1.6f); Fx.particles(s, ParticleTypes.EXPLOSION, 2, 0.4); Fx.particles(s, ParticleTypes.LARGE_SMOKE, 25, 0.8);
                    Fx.aoeAt(s, s.position(), 8, 2.8, null); s.kill(); }));
    }
}
