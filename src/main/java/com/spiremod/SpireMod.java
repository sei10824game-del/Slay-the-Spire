package com.spiremod;

import com.mojang.serialization.Codec;
import com.spiremod.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.List;

@Mod(SpireMod.ID)
public class SpireMod {
    public static final String ID = "spiremod";
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ID);
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ID);

    public static final RegistryObject<EntityType<SpireProjectile>> PROJECTILE = ENTITIES.register("spire_projectile", () ->
            EntityType.Builder.<SpireProjectile>of(SpireProjectile::new, MobCategory.MISC).sized(0.4f, 0.4f).clientTrackingRange(6).updateInterval(1).noSave().build(ID + ":spire_projectile"));
    public static final List<RegistryObject<Item>> EGGS = new ArrayList<>();

    public static final RegistryObject<Codec<SpireSpawns>> SPAWNS_CODEC = BIOME_MODS.register("spire_spawns", () -> SpireSpawns.CODEC);

    public SpireMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Catalog.init();
        for (MobDef def : Catalog.ALL) {
            def.type = ENTITIES.register(def.id, () -> EntityType.Builder.<SpireMob>of((t, l) -> new SpireMob(t, l, def), MobCategory.MONSTER)
                    .sized(def.w, def.h).clientTrackingRange(def.boss ? 12 : 8).updateInterval(2)
                    .build(ID + ":" + def.id));
            EGGS.add(ITEMS.register(def.id + "_spawn_egg", () -> new ForgeSpawnEggItem(def.type, def.eggPrimary, def.eggSecondary, new Item.Properties())));
        }
        RegistryObject<CreativeModeTab> tab = TABS.register("spire", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.spiremod"))
                .icon(() -> new ItemStack(EGGS.isEmpty() ? net.minecraft.world.item.Items.GOLD_INGOT : EGGS.get(0).get()))
                .displayItems((p, out) -> { for (RegistryObject<Item> e : EGGS) out.accept(e.get()); })
                .build());
        ENTITIES.register(bus); ITEMS.register(bus); TABS.register(bus); BIOME_MODS.register(bus); ModEffects.EFFECTS.register(bus);
    }
}
