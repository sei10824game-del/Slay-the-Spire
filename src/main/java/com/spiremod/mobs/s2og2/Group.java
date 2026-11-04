package com.spiremod.mobs.s2og2;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(PhrogParasite.def());
        out.accept(BygoneEffigy.def());
        out.accept(Byrdonis.def());
        out.accept(Vantom.def());
        out.accept(KinPriest.def());
        out.accept(KinFollower.def());
        out.accept(CeremonialBeast.def());
    }
}
