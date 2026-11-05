package com.spiremod.mobs.s2glory1;

import com.spiremod.core.MobDef;
import java.util.function.Consumer;

/** StS2 Glory (act 3) normals + elites, part 1. */
public final class Group {
    private Group() {}
    public static void collect(Consumer<MobDef> out) {
        out.accept(Axebot.def());
        out.accept(DevotedSculptor.def());
        out.accept(Fabricator.def());
        out.accept(Zapbot.def());
        out.accept(Stabbot.def());
        out.accept(Guardbot.def());
        out.accept(Noisebot.def());
        out.accept(FrogKnight.def());
        out.accept(GlobeHead.def());
        out.accept(LivingShield.def());
        out.accept(TurretOperator.def());
        out.accept(OwlMagistrate.def());
        out.accept(ScrollOfBiting.def());
        out.accept(SlimedBerserker.def());
        out.accept(TheLost.def());
        out.accept(TheForgotten.def());
        out.accept(CubexConstruct.def());
        out.accept(FlailKnight.def());
        out.accept(SpectralKnight.def());
        out.accept(MagiKnight.def());
        out.accept(MechaKnight.def());
        out.accept(SoulNexus.def());
    }
}
