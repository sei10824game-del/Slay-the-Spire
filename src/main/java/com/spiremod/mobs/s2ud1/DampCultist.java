package com.spiremod.mobs.s2ud1;

import com.spiremod.core.MobDef;
import com.spiremod.spec.ModelSpec;

/** StS2 Underdocks Damp Cultist (52 HP): Incantation (Ritual 5), then Dark Strike 1 (snowballs). */
public final class DampCultist {
    private DampCultist() {}
    public static ModelSpec model() { return Cultists.build(true); }
    public static MobDef def() { return Cultists.def("s2_damp_cultist", "Damp Cultist", "湿ったカルティスト", true, 52, 5, 1, 0x3d6a6e, 0x8fd0e0, MobDef.Habitat.SWAMP); }
}
