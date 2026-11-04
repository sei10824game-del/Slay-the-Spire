package com.spiremod.mobs.example;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Cultist.def());
    }
}
