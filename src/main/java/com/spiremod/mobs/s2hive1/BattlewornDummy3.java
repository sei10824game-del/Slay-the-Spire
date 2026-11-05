package com.spiremod.mobs.s2hive1;

import com.spiremod.core.MobDef;
import com.spiremod.spec.ModelSpec;

/** preview/registration shims for the three Battle Friend V3.0 */
public final class BattlewornDummy3 {
    private BattlewornDummy3() {}
    public static ModelSpec model() { return BattlewornDummy.model(2); }
    public static MobDef def() { return BattlewornDummy.def(2); }
}
