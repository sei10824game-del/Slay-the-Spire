package com.spiremod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.spiremod.core.ProjKind;
import com.spiremod.core.SpireProjectile;
import com.spiremod.spec.Ctx;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class SpireProjectileRenderer extends EntityRenderer<SpireProjectile> {
    private final Map<String, Baked> cache = new HashMap<>();
    public SpireProjectileRenderer(EntityRendererProvider.Context c) { super(c); }

    private Baked get(ProjKind k) { return cache.computeIfAbsent(k.id, id -> new Baked(k.model.get(), "proj_" + id)); }

    @Override public void render(SpireProjectile e, float yaw, float pt, PoseStack ps, MultiBufferSource buf, int light) {
        ProjKind k = e.kind(); if (k == null) return;
        Baked b = get(k);
        Vec3 v = e.getDeltaMovement();
        double h = Math.sqrt(v.x * v.x + v.z * v.z);
        float mcYaw = (float) Math.toDegrees(Math.atan2(-v.x, v.z));
        float pitch = (float) Math.toDegrees(Math.atan2(v.y, h));
        ps.pushPose();
        ps.mulPose(Axis.YP.rotationDegrees(180f - mcYaw));
        ps.mulPose(Axis.XP.rotationDegrees(pitch));
        ps.scale(-1f, -1f, 1f);
        ps.translate(0, -1.5, 0);
        Ctx c = new Ctx(); c.age = e.age + pt;
        b.posing.eval(c); b.apply();
        b.root.render(ps, buf.getBuffer(RenderType.entityCutoutNoCull(b.tex)), 15728880, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        b.root.render(ps, buf.getBuffer(RenderType.eyes(b.glowTex)), 15728880, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        ps.popPose();
    }

    @Override public ResourceLocation getTextureLocation(SpireProjectile e) { ProjKind k = e.kind(); return k == null ? null : get(k).tex; }
}
