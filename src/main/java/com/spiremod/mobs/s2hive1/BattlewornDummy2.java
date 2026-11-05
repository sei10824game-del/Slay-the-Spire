package com.spiremod.mobs.s2hive1;

import com.spiremod.core.MobDef;
import com.spiremod.spec.ModelSpec;

/** preview/registration shims for the three Battle Friend V2.0 */
public final class BattlewornDummy2 {
    private BattlewornDummy2() {}
    public static ModelSpec model() { return BattlewornDummy.model(1); }
    public static MobDef def() { return BattlewornDummy.def(1); }
}
