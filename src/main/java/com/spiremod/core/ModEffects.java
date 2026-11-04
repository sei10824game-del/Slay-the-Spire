package com.spiremod.core;

import com.spiremod.SpireMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** StS debuffs that have no vanilla equivalent. */
@Mod.EventBusSubscriber(modid = SpireMod.ID)
public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SpireMod.ID);

    private static class Simple extends MobEffect { Simple(MobEffectCategory c, int col) { super(c, col); } }

    /** takes 50% more damage */
    public static final RegistryObject<MobEffect> VULNERABLE = EFFECTS.register("vulnerable", () -> new Simple(MobEffectCategory.HARMFUL, 0xE0602A));
    /** weaker armor */
    public static final RegistryObject<MobEffect> FRAIL = EFFECTS.register("frail", () ->
            new Simple(MobEffectCategory.HARMFUL, 0x7FA0C8).addAttributeModifier(Attributes.ARMOR, "5b1a9a0e-2d6b-4c0e-9f1c-7a31c5d0f001", -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
    /** clogged mind: slower attacks */
    public static final RegistryObject<MobEffect> DAZED = EFFECTS.register("dazed", () ->
            new Simple(MobEffectCategory.HARMFUL, 0x8888AA).addAttributeModifier(Attributes.ATTACK_SPEED, "5b1a9a0e-2d6b-4c0e-9f1c-7a31c5d0f002", -0.35, AttributeModifier.Operation.MULTIPLY_TOTAL));

    @SubscribeEvent
    public static void hurt(LivingHurtEvent ev) {
        if (ev.getEntity().hasEffect(VULNERABLE.get())) ev.setAmount(ev.getAmount() * Cfg.VULN_MULT);
    }
}
