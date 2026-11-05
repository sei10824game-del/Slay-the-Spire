package com.spiremod.mobs.s2hive1;

import com.spiremod.core.MobDef;
import com.spiremod.spec.ModelSpec;

/** preview/registration shims for the three Battle Friend versions */
public final class BattlewornDummy1 {
    private BattlewornDummy1() {}
    public static ModelSpec model() { return BattlewornDummy.model(0); }
    public static MobDef def() { return BattlewornDummy.def(0); }
}
