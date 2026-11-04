package com.spiremod.mobs.s1act3b;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(SpireGrowth.def());
        out.accept(SpireShield.def());
        out.accept(AwakenedOne.def());
        out.accept(AwakenedCultist.def());
        out.accept(TimeEater.def());
        out.accept(Donu.def());
        out.accept(Deca.def());
        out.accept(SpireSpear.def());
        out.accept(CorruptHeart.def());
    }
}
