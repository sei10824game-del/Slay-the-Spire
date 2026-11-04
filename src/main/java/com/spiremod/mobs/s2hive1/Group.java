package com.spiremod.mobs.s2hive1;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Bowlbug.def(0));
        out.accept(Bowlbug.def(1));
        out.accept(Bowlbug.def(2));
        out.accept(Bowlbug.def(3));
    }
}
