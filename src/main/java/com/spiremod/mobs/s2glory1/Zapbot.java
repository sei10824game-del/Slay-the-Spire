package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Zapbot (Fabricator minion). 18-23 HP, High Voltage 2 (+2 Str at the end of each of its turns). Zap (14) every turn. */
public final class Zapbot {
    private Zapbot() {}
    public static ModelSpec model() {
        ModelSpec m = Bots.drone("s2_zapbot", 0xd8b020, 0x6a5a18, 0x80e8ff);
        Part body = m.byName.get("body");
        for (int s = -1; s <= 1; s += 2) {
            Part coil = body.add("coil" + s).at(s * 3, 4, 0);
            coil.box(-1, 0, -1, 2, 3, 2).color(0xb0782a).bevel(0.2f); coil.box(-1.5f, 3, -1.5f, 3, 1, 3).color(0x80e8ff).flat().glow(Face.TOP, 0, 0, 3, 3, 0x80e8ff);
        }
        Clip c = m.clip("zap", 30);
        Kit.seq(c, "body", 30, 14, 6, -25, 0, 0, 14, 12, 0, 0, 22, 4, 0, 0);
        c.scl("body", 0, 1, 1, 1).scl("body", 12, 1.2f, 1.2f, 1.2f).scl("body", 14, 0.95f, 0.95f, 0.95f).scl("body", 30, 1, 1, 1);
        return m;
    }
    public static MobDef def() {
        return MobDef.of("s2_zapbot", "Zapbot", "ザップボット")
            .game(2, 3).size(0.6f, 1.0f).hp(21).speed(0.22f).flying().minion().style(Style.METAL).egg(0xd8b020, 0x80e8ff)
            .model(Zapbot::model).pattern(Pattern.cycle("zap"))
            .move(Move.of("zap", 30).intent(Intent.ATTACK, 14, 1).ranged(10)
                .sound(12, SoundEvents.LIGHTNING_BOLT_IMPACT, 0.4f, 1.8f)
                .at(14, (s, t) -> { Fx.hitTarget(14).run(s, t); Fx.particles(s, ParticleTypes.ELECTRIC_SPARK, 15, 0.4); }))
            .onMoveEnd(s -> s.addStrength(2));
    }
}
