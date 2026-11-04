package com.spiremod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.spiremod.SpireMod;
import com.spiremod.core.MobDef;
import com.spiremod.core.SpireMob;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class SpireRenderer extends MobRenderer<SpireMob, SpireModel> {
    private final MobDef def;
    private final Baked baked;

    public SpireRenderer(EntityRendererProvider.Context ctx, MobDef def) {
        this(ctx, def, new Baked(def.model.get(), def.id));
    }
    private SpireRenderer(EntityRendererProvider.Context ctx, MobDef def, Baked b) {
        super(ctx, new SpireModel(b), Math.max(0.3f, def.w * 0.55f * b.spec.scale));
        this.def = def; this.baked = b;
        addLayer(new RenderLayer<SpireMob, SpireModel>(this) {
            @Override public void render(PoseStack ps, MultiBufferSource buf, int light, SpireMob e, float s, float sa, float pt, float age, float hy, float hp) {
                if (e.isInvisible()) return;
                getParentModel().renderToBuffer(ps, buf.getBuffer(RenderType.eyes(baked.glowTex)), 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        });
    }

    @Override public ResourceLocation getTextureLocation(SpireMob e) { return baked.tex; }
    @Override protected void scale(SpireMob e, PoseStack ps, float pt) { float s = baked.spec.scale; ps.scale(s, s, s); }
    @Override protected float getFlipDegrees(SpireMob e) { return baked.spec.clips.containsKey("death") ? 0f : 90f; }

    @Override public void render(SpireMob e, float yaw, float pt, PoseStack ps, MultiBufferSource buf, int light) {
        super.render(e, yaw, pt, ps, buf, light);
        if (e.isDeadOrDying() || e.isInvisible()) return;
        if (this.entityRenderDispatcher.distanceToSqr(e) > 48 * 48) return;
        String intent = e.getEntityData().get(SpireMob.D_INTENT);
        int block = e.getBlock(), str = e.getStrength();
        if (intent.isEmpty() && block <= 0 && str == 0) return;
        Font font = getFont();
        ps.pushPose();
        ps.translate(0, e.getBbHeight() * Math.max(1f, baked.spec.scale) + 0.65, 0);
        ps.mulPose(this.entityRenderDispatcher.cameraOrientation());
        ps.scale(-0.03f, -0.03f, 0.03f);
        Matrix4f m = ps.last().pose();
        int bg = 0x40000000;
        float y = 0;
        if (!intent.isEmpty()) {
            int col = 0xFF000000 | e.getEntityData().get(SpireMob.D_INTENT_COLOR);
            font.drawInBatch(intent, -font.width(intent) / 2f, y, col, false, m, buf, Font.DisplayMode.SEE_THROUGH, bg, 15728880);
            y -= 10;
        }
        StringBuilder st = new StringBuilder();
        if (block > 0) st.append("⛨").append(block).append(' ');
        if (str != 0) st.append("↑").append(str);
        if (st.length() > 0) {
            String s = st.toString().trim();
            font.drawInBatch(s, -font.width(s) / 2f, y, block > 0 ? 0xFF66BBFF : 0xFFFFCC44, false, m, buf, Font.DisplayMode.SEE_THROUGH, bg, 15728880);
        }
        ps.popPose();
    }
}
