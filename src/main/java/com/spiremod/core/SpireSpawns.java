package com.spiremod.core;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

/** Adds every creature with spawnWeight > 0 to matching overworld biomes. */
public final class SpireSpawns implements BiomeModifier {
    public static final Codec<SpireSpawns> CODEC = Codec.unit(SpireSpawns::new);

    @Override public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD || !biome.is(BiomeTags.IS_OVERWORLD)) return;
        for (MobDef d : Catalog.ALL) {
            if (d.spawnWeight <= 0 || d.type == null || !matches(biome, d.habitat)) continue;
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(d.type.get(), d.spawnWeight, d.groupMin, d.groupMax));
        }
    }

    static boolean matches(Holder<Biome> b, MobDef.Habitat h) {
        switch (h) {
            case NONE: return false;
            case FOREST: return b.is(BiomeTags.IS_FOREST) || b.is(Tags.Biomes.IS_DENSE_OVERWORLD) && !b.is(BiomeTags.IS_JUNGLE);
            case PLAINS: return b.is(Tags.Biomes.IS_PLAINS) || b.is(Biomes.MEADOW);
            case SWAMP: return b.is(Tags.Biomes.IS_SWAMP);
            case SNOW: return b.is(Tags.Biomes.IS_COLD_OVERWORLD) || b.is(Tags.Biomes.IS_SNOWY);
            case DESERT: return b.is(Tags.Biomes.IS_DESERT) || b.is(BiomeTags.IS_BADLANDS);
            case MOUNTAIN: return b.is(BiomeTags.IS_MOUNTAIN) || b.is(BiomeTags.IS_HILL);
            case JUNGLE: return b.is(BiomeTags.IS_JUNGLE);
            case TAIGA: return b.is(BiomeTags.IS_TAIGA);
            case SAVANNA: return b.is(BiomeTags.IS_SAVANNA);
            case BEACH: return b.is(BiomeTags.IS_BEACH) || b.is(BiomeTags.IS_RIVER);
            default: return true;
        }
    }

    @Override public Codec<? extends BiomeModifier> codec() { return CODEC; }
}
