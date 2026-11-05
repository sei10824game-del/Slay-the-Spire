package com.spiremod.mobs.s2hive1;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** Registry of this group's creatures: add one out.accept(Name.def()); line per creature class. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(HiveB.Exoskeleton.def());
        out.accept(HiveB.ThievingHopper.def());
        out.accept(HiveB.Tunneler.def());
        out.accept(HiveB.Chomper.def());
        out.accept(HiveB.LouseProgenitor.def());
        out.accept(HiveB.Myte.def());
        out.accept(HiveB.Ovicopter.def());
        out.accept(HiveB.ToughEgg.def());
        out.accept(HiveB.Hatchling.def());
        out.accept(HiveB.SpinyToad.def());
        out.accept(HiveB.Obscura.def());
        out.accept(HiveB.Parafright.def());
        out.accept(BattlewornDummy1.def());
        out.accept(BattlewornDummy2.def());
        out.accept(BattlewornDummy3.def());
        out.accept(HunterKiller.def());
        out.accept(Merchant.def());
        out.accept(SlumberingBeetle.def());
        out.accept(Bowlbug.def(0));
        out.accept(Bowlbug.def(1));
        out.accept(Bowlbug.def(2));
        out.accept(Bowlbug.def(3));
    }
}
