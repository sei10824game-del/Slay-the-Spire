package com.spiremod.mobs.s2glory2;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Queen.def());
        out.accept(TorchHeadAmalgam.def());
        out.accept(TestSubject.def());
        out.accept(Aeonglass.def());
        out.accept(Doormaker.def());
    }
}
