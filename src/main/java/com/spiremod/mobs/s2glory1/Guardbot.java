package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Guardbot (Fabricator minion). 16-20 HP. Guard: gives the Fabricator 15 Block every turn. */
public final class Guardbot {
    private Guardbot() {}
    public static ModelSpec model() {
        ModelSpec m = Bots.drone("s2_guardbot", 0x4a78b8, 0x1e3458, 0x70c0ff);
        Part plate = m.byName.get("body").add("plate").at(0, 0, 4.5f);
        plate.box(-5.5f, -6, 0, 11, 12, 2).color(0x8aa8d0).grad(0xb0c8e8, 0x4a6890).bevel(0.3f);
        plate.box(-2, -2, 2, 4, 4, 1).color(0x70c0ff).flat().glow(Face.FRONT, 0, 0, 4, 4, 0x70c0ff);
        Clip c = m.clip("guard", 30);
        Kit.sp(c, "plate", 30, 14, 8, 0, 0, -1, 14, 0, 0, 5, 24, 0, 0, 3);
        c.scl("plate", 0, 1, 1, 1).scl("plate", 14, 1.3f, 1.3f, 1.3f, Ease.OUT_BACK).scl("plate", 30, 1, 1, 1);
        Kit.seq(c, "body", 30, -1, 8, -8, 0, 0, 14, 8, 0, 0);
        return m;
    }
    public static MobDef def() {
        return MobDef.of("s2_guardbot", "Guardbot", "ガードボット")
            .game(2, 3).size(0.6f, 1.0f).hp(18).speed(0.2f).flying().minion().style(Style.METAL).egg(0x4a78b8, 0x70c0ff)
            .model(Guardbot::model).pattern(Pattern.cycle("guard"))
            .move(Move.of("guard", 30).intent(Intent.DEFEND).noTarget()
                .sound(14, SoundEvents.ANVIL_PLACE, 0.5f, 1.6f)
                .at(14, (s, t) -> { SpireMob l = s.getLeader(); (l != null ? l : s).addBlock(15); Fx.particles(l != null ? l : s, ParticleTypes.ENCHANTED_HIT, 15, 0.8); }));
    }
}
