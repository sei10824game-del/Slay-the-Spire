package com.spiremod.mobs.s1act3a;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Darkling.def());
        out.accept(OrbWalker.def());
        out.accept(Spiker.def());
        out.accept(Repulsor.def());
        out.accept(Exploder.def());
        out.accept(WrithingMass.def());
        out.accept(Transient.def());
        out.accept(GiantHead.def());
        out.accept(Nemesis.def());
        out.accept(Reptomancer.def());
        out.accept(Dagger.def());
        out.accept(Maw.def());
    }
}
