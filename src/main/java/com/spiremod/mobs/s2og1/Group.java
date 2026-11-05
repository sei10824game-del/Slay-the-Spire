package com.spiremod.mobs.s2og1;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Mawler.def());
        out.accept(AssassinRaider.def());
        out.accept(AxeRaider.def());
        out.accept(BruteRaider.def());
        out.accept(CrossbowRaider.def());
        out.accept(EyeWithTeeth.def());
        out.accept(Flyconid.def());
        out.accept(Fogmog.def());
        out.accept(FuzzyWurm.def());
        out.accept(Inklet.def());
        out.accept(Jaxfruit.def());
        out.accept(ShrinkerBeetle.def());
        out.accept(Strangler.def());
        out.accept(TrackerRaider.def());
        out.accept(VineShambler.def());
        out.accept(Wriggler.def());
        out.accept(TwigSlime.defS());
        out.accept(TwigSlime.defM());
        out.accept(LeafSlime.defS());
        out.accept(LeafSlime.defM());
        out.accept(Nibbit.def());
    }
}
