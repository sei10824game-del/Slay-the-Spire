package com.spiremod.mobs.s2ud1;

import com.spiremod.core.MobDef;
import com.spiremod.spec.ModelSpec;

/** StS2 Underdocks Calcified Cultist (39 HP): Incantation (Ritual 2), then Dark Strike 9. */
public final class CalcifiedCultist {
    private CalcifiedCultist() {}
    public static ModelSpec model() { return Cultists.build(false); }
    public static MobDef def() { return Cultists.def("s2_calcified_cultist", "Calcified Cultist", "石灰化カルティスト", false, 39, 2, 9, 0x6d6a78, 0xe8e2d0, MobDef.Habitat.CAVE); }
}
