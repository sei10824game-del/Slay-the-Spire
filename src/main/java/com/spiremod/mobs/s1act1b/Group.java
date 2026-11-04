package com.spiremod.mobs.s1act1b;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(FatGremlin.def());
        out.accept(MadGremlin.def());
        out.accept(ShieldGremlin.def());
        out.accept(SneakyGremlin.def());
        out.accept(GremlinNob.def());
        out.accept(Lagavulin.def());
        out.accept(Sentry.def());
        out.accept(GremlinWizard.def());
        out.accept(SlimeBoss.def());
        out.accept(TheGuardian.def());
        out.accept(Hexaghost.def());
    }
}
