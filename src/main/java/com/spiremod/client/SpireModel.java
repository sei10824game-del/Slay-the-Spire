package com.spiremod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.spiremod.core.SpireMob;
import com.spiremod.spec.Ctx;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;

public class SpireModel extends EntityModel<SpireMob> {
    public final Baked baked;
    private final Ctx ctx = new Ctx();

    public SpireModel(Baked b) { this.baked = b; }

    @Override public void setupAnim(SpireMob e, float swing, float swingAmt, float age, float headYaw, float headPitch) {
        float pt = Minecraft.getInstance().getPartialTick();
        long now = e.level().getGameTime();
        Ctx c = ctx;
        c.age = age; c.swing = swing; c.swingAmt = swingAmt; c.headYaw = headYaw; c.headPitch = headPitch; c.partial = pt;
        c.base = e.getBase();
        String a = e.getAnim();
        if (!a.isEmpty()) { c.move = a; c.moveT = now - e.getEntityData().get(SpireMob.D_ANIM_START) + pt; } else { c.move = null; c.moveT = 0; }
        if (e.clientFade != null && a.isEmpty()) { c.fade = e.clientFade; c.fadeT = now - e.clientFadeStart + pt; } else c.fade = null;
        c.hurtT = e.hurtTime > 0 ? 10 - (e.hurtTime - pt) : -1;
        c.deathT = e.deathTime > 0 || e.isDeadOrDying() ? e.deathTime + pt : -1;
        c.spawnT = e.spawnAge + pt;
        c.hpFrac = e.getHealth() / e.getMaxHealth();
        c.phase = e.getPhase(); c.var = e.getVar();
        baked.posing.eval(c);
        baked.apply();
    }

    @Override public void renderToBuffer(PoseStack ps, VertexConsumer vc, int light, int overlay, float r, float g, float b, float a) {
        baked.root.render(ps, vc, light, overlay, r, g, b, a);
    }
}
