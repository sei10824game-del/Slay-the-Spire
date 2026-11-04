package com.spiremod.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.spiremod.SpireMod;
import com.spiremod.spec.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

/** A ModelSpec baked into a vanilla ModelPart tree + generated textures. */
public final class Baked {
    public final ModelSpec spec;
    public final ModelPart root;
    public final ModelPart[] parts;
    public final ResourceLocation tex, glowTex;
    public final Posing posing;

    public Baked(ModelSpec spec, String key) {
        this.spec = spec;
        TexBuilder tb = TexBuilder.build(spec);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        for (Part p : spec.root.kids) addPart(rootDef, p, true);
        root = LayerDefinition.create(mesh, tb.w, tb.h).bakeRoot();
        parts = new ModelPart[spec.parts.size()];
        for (Part p : spec.root.kids) link(root, p);
        posing = new Posing(spec);
        tex = register(key, tb.w, tb.h, tb.argb, false);
        glowTex = register(key + "_glow", tb.w, tb.h, tb.glow, true);
    }

    private void addPart(PartDefinition parent, Part p, boolean top) {
        CubeListBuilder cb = CubeListBuilder.create();
        for (Box b : p.boxes) {
            cb.texOffs(b.u, b.v).addBox(b.x0, -(b.y0 + b.dy), -(b.z0 + b.dz), b.dx, b.dy, b.dz, new CubeDeformation(b.inflate));
        }
        PartPose pose = PartPose.offsetAndRotation(p.x, top ? 24 - p.y : -p.y, -p.z,
                (float) Math.toRadians(p.rx), (float) -Math.toRadians(p.ry), (float) -Math.toRadians(p.rz));
        PartDefinition pd = parent.addOrReplaceChild(p.name, cb, pose);
        for (Part k : p.kids) addPart(pd, k, false);
    }

    private void link(ModelPart parent, Part p) {
        ModelPart mp = parent.getChild(p.name);
        parts[p.index] = mp;
        for (Part k : p.kids) link(mp, k);
    }

    private ResourceLocation register(String key, int w, int h, int[] argb, boolean glow) {
        NativeImage img = new NativeImage(NativeImage.Format.RGBA, w, h, true);
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) {
            int c = argb[y * w + x];
            int a = glow ? ((c >>> 24) != 0 ? 255 : 0) : (c >>> 24);
            int abgr = (a << 24) | ((c & 0xFF) << 16) | (c & 0xFF00) | ((c >> 16) & 0xFF);
            img.setPixelRGBA(x, y, abgr);
        }
        ResourceLocation loc = new ResourceLocation(SpireMod.ID, "dyn/" + key);
        Minecraft.getInstance().getTextureManager().register(loc, new DynamicTexture(img));
        return loc;
    }

    /** write the current Posing into the ModelPart tree */
    public void apply() {
        Posing po = posing;
        for (Part p : spec.parts) {
            int i = p.index; ModelPart mp = parts[i];
            boolean top = p.parent == spec.root;
            mp.x = p.x + po.ox[i];
            mp.y = (top ? 24 : 0) - (p.y + po.oy[i]);
            mp.z = -(p.z + po.oz[i]);
            mp.xRot = (float) Math.toRadians(p.rx + po.orx[i]);
            mp.yRot = (float) -Math.toRadians(p.ry + po.ory[i]);
            mp.zRot = (float) -Math.toRadians(p.rz + po.orz[i]);
            mp.xScale = p.sx * po.osx[i]; mp.yScale = p.sy * po.osy[i]; mp.zScale = p.sz * po.osz[i];
            mp.visible = po.vis[i];
        }
    }
}
