package com.spiremod.mobs.s2ud2;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(TerrorEel.def());
        out.accept(PhantasmalGardener.def());
        out.accept(SkulkingColony.def());
        out.accept(WaterfallGiant.def());
        out.accept(LagavulinMatriarch.def());
        out.accept(SoulFysh.def());
    }
}
