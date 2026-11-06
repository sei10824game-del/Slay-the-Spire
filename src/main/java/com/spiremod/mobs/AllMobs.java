package com.spiremod.mobs;

import com.spiremod.core.MobDef;

import java.util.function.Consumer;

/** One line per group file. Each group file has `public static void collect(Consumer<MobDef> out)`. */
public final class AllMobs {
    private AllMobs() {}
    public static void collect(Consumer<MobDef> out) {
        com.spiremod.mobs.example.Group.collect(out);
        com.spiremod.mobs.s1act1a.Group.collect(out);
        com.spiremod.mobs.s1act1b.Group.collect(out);
        com.spiremod.mobs.s1act2a.Group.collect(out);
        com.spiremod.mobs.s1act2b.Group.collect(out);
        com.spiremod.mobs.s1act3a.Group.collect(out);
        com.spiremod.mobs.s1act3b.Group.collect(out);
        com.spiremod.mobs.s2og1.Group.collect(out);
        com.spiremod.mobs.s2og2.Group.collect(out);
        com.spiremod.mobs.s2ud1.Group.collect(out);
        com.spiremod.mobs.s2ud2.Group.collect(out);
        com.spiremod.mobs.s2hive1.Group.collect(out);
        com.spiremod.mobs.s2hive2.Group.collect(out);
        com.spiremod.mobs.s2glory1.Group.collect(out);
        com.spiremod.mobs.s2glory2.Group.collect(out);
        com.spiremod.mobs.heroes.Group.collect(out);
        // groups are added here
    }
}
