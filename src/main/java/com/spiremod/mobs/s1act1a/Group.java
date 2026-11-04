package com.spiremod.mobs.s1act1a;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(JawWorm.def());
        out.accept(Louse.def());
        out.accept(Louse.Green.def());
        out.accept(Slime.AcidS.def());
        out.accept(Slime.AcidM.def());
        out.accept(Slime.AcidL.def());
        out.accept(Slime.SpikeS.def());
        out.accept(Slime.SpikeM.def());
        out.accept(Slime.SpikeL.def());
    }
}
