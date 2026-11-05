package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Stabbot (Fabricator minion). 18-23 HP. Stab (11 + 1 Frail) every turn. */
public final class Stabbot {
    private Stabbot() {}
    public static ModelSpec model() {
        ModelSpec m = Bots.drone("s2_stabbot", 0xb83a3a, 0x5a1818, 0xffd040);
        Part blade = m.byName.get("body").add("blade").at(0, -1, 4);
        blade.box(-1, -1, 0, 2, 2, 3).color(0x6a6e76);
        blade.box(-0.5f, -0.5f, 3, 1, 1, 9).color(0xe0e8f0).flat().noise(0.03f);
        blade.box(-1.5f, -1.5f, 2, 3, 3, 1).color(0xd0a640).flat();
        Clip c = Bots.strike(m, "stab", 28, 13);
        Kit.sp(c, "blade", 28, 13, 6, 0, 0, -2, 13, 0, 0, 5, 20, 0, 0, 1);
        return m;
    }
    public static MobDef def() {
        return MobDef.of("s2_stabbot", "Stabbot", "スタブボット")
            .game(2, 3).size(0.6f, 1.0f).hp(21).speed(0.24f).flying().minion().style(Style.METAL).egg(0xb83a3a, 0xffd040)
            .model(Stabbot::model).pattern(Pattern.cycle("stab"))
            .move(Move.of("stab", 28).intent(Intent.ATTACK_DEBUFF, 11, 1).reach(2.4f)
                .sound(12, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.5f)
                .at(13, Fx.melee(11, 2.6, 80, v -> Fx.frail(v, Kit.TURN))));
    }
}
