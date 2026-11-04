package com.spiremod.mobs.s1act2b;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(BronzeAutomaton.def());
        out.accept(Champ.def());
        out.accept(BookOfStabbing.def());
        out.accept(GremlinLeader.def());
        out.accept(Taskmaster.def());
        out.accept(BronzeOrb.def());
        out.accept(Collector.def());
        out.accept(TorchHead.def());
    }
}
