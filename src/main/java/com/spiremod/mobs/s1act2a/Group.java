package com.spiremod.mobs.s1act2a;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Byrd.def());
        out.accept(Snecko.def());
        out.accept(SphericGuardian.def());
        out.accept(ShelledParasite.def());
        out.accept(Chosen.def());
        out.accept(Centurion.def());
        out.accept(Mystic.def());
        out.accept(SnakePlant.def());
        out.accept(Mugger.def());
        out.accept(BanditBear.def());
        out.accept(Pointy.def());
        out.accept(Romeo.def());
    }
}
