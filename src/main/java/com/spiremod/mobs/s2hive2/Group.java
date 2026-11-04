package com.spiremod.mobs.s2hive2;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Decimillipede.def());
        out.accept(Decimillipede.def(1));
        out.accept(Decimillipede.def(2));
        out.accept(InfestedPrism.def());
        out.accept(Entomancer.def());
        out.accept(KaiserCrab.def(0));
        out.accept(KaiserCrab.def(1));
        out.accept(KnowledgeDemon.def());
        out.accept(Insatiable.def());
    }
}
