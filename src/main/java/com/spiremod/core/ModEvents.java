package com.spiremod.core;

import com.spiremod.SpireMod;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpireMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    private ModEvents() {}

    @SubscribeEvent
    public static void attributes(EntityAttributeCreationEvent ev) {
        for (MobDef d : Catalog.ALL) ev.put(d.type.get(), SpireMob.attrs(d).build());
    }

    @SubscribeEvent
    public static void placements(SpawnPlacementRegisterEvent ev) {
        for (MobDef d : Catalog.ALL) {
            ev.register(d.type.get(), d.flying ? SpawnPlacements.Type.NO_RESTRICTIONS : SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (t, l, r, p, rnd) -> canSpawn(d, l, r, p, rnd), SpawnPlacementRegisterEvent.Operation.REPLACE);
        }
    }

    static boolean canSpawn(MobDef d, ServerLevelAccessor l, MobSpawnType r, BlockPos p, RandomSource rnd) {
        if (r == MobSpawnType.SPAWNER || r == MobSpawnType.SPAWN_EGG || r == MobSpawnType.COMMAND) return true;
        if (l.getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL) return false;
        if (d.spawnInDaylight) return Monster.checkAnyLightMonsterSpawnRules(null, l, r, p, rnd);
        return Monster.isDarkEnoughToSpawn(l, p, rnd) && net.minecraft.world.entity.Mob.checkMobSpawnRules(null, l, r, p, rnd);
    }
}
