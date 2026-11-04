package com.spiremod.mobs.s2ud1;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Seapunk.def());
        out.accept(CalcifiedCultist.def());
        out.accept(DampCultist.def());
        out.accept(CorpseSlug.def());
        out.accept(GremlinMerc.def());
        out.accept(SneakyGremlin.def());
        out.accept(FatGremlin.def());
        out.accept(LivingFog.def());
        out.accept(GasBomb.def());
        out.accept(FossilStalker.def());
        out.accept(TwoTailedRat.def());
        out.accept(HauntedShip.def());
        out.accept(PunchConstruct.def());
        out.accept(SewerClam.def());
        out.accept(Toadpole.def());
        out.accept(SludgeSpinner.def());
    }
}
