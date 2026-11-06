package com.spiremod.mobs.heroes;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Playable characters of Slay the Spire 1 and 2 (separate entries per game: their starter cards differ). */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        for (Heroes.Spec h : Heroes.ALL) out.accept(h.def());
    }
}
