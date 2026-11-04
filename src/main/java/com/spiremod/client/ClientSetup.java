package com.spiremod.client;

import com.spiremod.SpireMod;
import com.spiremod.core.Catalog;
import com.spiremod.core.MobDef;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpireMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers ev) {
        for (MobDef d : Catalog.ALL) ev.registerEntityRenderer(d.type.get(), ctx -> new SpireRenderer(ctx, d));
        ev.registerEntityRenderer(SpireMod.PROJECTILE.get(), SpireProjectileRenderer::new);
    }
}
