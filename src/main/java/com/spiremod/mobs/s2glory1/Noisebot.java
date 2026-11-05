package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Noisebot (Fabricator minion). 18-23 HP. Noise: shuffles 2 Dazed into your deck every turn. */
public final class Noisebot {
    private Noisebot() {}
    public static ModelSpec model() {
        ModelSpec m = Bots.drone("s2_noisebot", 0x4aa86a, 0x1e5a34, 0xff60d0);
        Part horn = m.byName.get("body").add("horn").at(0, 0, 4);
        horn.box(-1.5f, -1.5f, 0, 3, 3, 3).color(0x30363a);
        horn.box(-3, -3, 3, 6, 6, 2).color(0xc8c8d0).bevel(0.3f);
        horn.box(-4.5f, -4.5f, 5, 9, 9, 2).color(0xd8d8e0).bevel(0.3f).glow(Face.FRONT, 3, 3, 3, 3, 0xff60d0);
        Clip c = m.clip("noise", 34);
        horn.scale(1, 1, 1);
        c.scl("horn", 0, 1, 1, 1).scl("horn", 12, 0.8f, 0.8f, 0.8f).scl("horn", 15, 1.5f, 1.5f, 1.5f, Ease.OUT_BACK).scl("horn", 18, 1.2f, 1.2f, 1.2f).scl("horn", 21, 1.5f, 1.5f, 1.5f).scl("horn", 34, 1, 1, 1);
        Kit.seq(c, "body", 34, -1, 10, -12, 0, 0, 15, 8, 0, 6, 18, 8, 0, -6, 21, 8, 0, 6, 28, 0, 0, 0);
        return m;
    }
    public static MobDef def() {
        return MobDef.of("s2_noisebot", "Noisebot", "ノイズボット")
            .game(2, 3).size(0.6f, 1.0f).hp(21).speed(0.2f).flying().minion().style(Style.METAL).egg(0x4aa86a, 0xff60d0)
            .model(Noisebot::model).pattern(Pattern.cycle("noise"))
            .move(Move.of("noise", 34).intent(Intent.DEBUFF).ranged(10)
                .sound(14, SoundEvents.NOTE_BLOCK_BIT.get(), 1.4f, 0.6f)
                .at(15, (s, t) -> { Fx.particles(s, ParticleTypes.NOTE, 20, 0.8); if (t != null) { Fx.curse(t, Fx.Curse.DAZED); Fx.curse(t, Fx.Curse.DAZED); } }));
    }
}
