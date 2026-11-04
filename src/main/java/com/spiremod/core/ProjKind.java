package com.spiremod.core;

import com.spiremod.spec.Face;
import com.spiremod.spec.ModelSpec;
import com.spiremod.spec.Part;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/** A kind of projectile (look + physics + trail). Register in a creature class: ProjKind.orb("hexa_fire", 0xFF8800, 5).trail(...). */
public final class ProjKind {
    public static final Map<String, ProjKind> ALL = new LinkedHashMap<>();

    public final String id;
    public final Supplier<ModelSpec> model;
    public double gravity = 0, drag = 0.99; public int life = 100; public float hitRadius = 0.3f;
    public ParticleOptions trail; public int trailEvery = 1;
    public boolean pierce, homing;
    public BiConsumer<SpireProjectile, Vec3> onImpact;   // server: called when it ends (entity/block hit, or expiry if onExpire)
    public boolean impactOnExpire;
    public boolean spin;

    private ProjKind(String id, Supplier<ModelSpec> m) { this.id = id; this.model = m; ALL.put(id, this); }

    public static ProjKind of(String id) { return ALL.get(id); }
    /** fully custom model; centre the model on the origin (0,0,0) and point it along +z (= direction of flight) */
    public static ProjKind custom(String id, Supplier<ModelSpec> m) { return new ProjKind(id, m); }

    /** glowing cube-orb */
    public static ProjKind orb(String id, int color, int size) {
        return new ProjKind(id, () -> {
            ModelSpec m = new ModelSpec("proj_" + id);
            Part p = m.add("core").at(0, 0, 0);
            p.cbox(-size / 2f, size, size, size).color(color).flat().noise(0.04f).all(color);
            for (Face f : Face.values()) {}
            Part o = m.add("halo").at(0, 0, 0);
            int s2 = size + 2; o.cbox(-s2 / 2f, s2, s2, s2).color(dim(color, 0.55f)).flat().noise(0.0f).all(dim(color, 0.55f));
            m.proc((r, c) -> { r.rot("halo", c.age * 9, c.age * 13, c.age * 7); r.rot("core", c.age * -11, c.age * 6, 0); });
            return m;
        }).spinning();
    }

    /** elongated glowing bolt / dart / needle (length along flight) */
    public static ProjKind bolt(String id, int color, int length, int thick) {
        return new ProjKind(id, () -> {
            ModelSpec m = new ModelSpec("proj_" + id);
            Part p = m.add("core").at(0, 0, 0);
            p.box(-thick / 2f, -thick / 2f, -length / 2f, thick, thick, length).color(color).flat().all(color);
            p.box(-thick / 2f - 0.5f, -thick / 2f - 0.5f, length / 2f - 2, thick + 1, thick + 1, 2).color(dim(color, 0.6f)).flat();
            return m;
        });
    }

    private static int dim(int c, float f) { return ((int) (((c >> 16) & 255) * f) << 16) | ((int) (((c >> 8) & 255) * f) << 8) | (int) ((c & 255) * f); }

    public ProjKind spinning() { spin = true; return this; }
    public ProjKind gravity(double g) { gravity = g; return this; }
    public ProjKind life(int l) { life = l; return this; }
    public ProjKind trail(ParticleOptions p) { trail = p; return this; }
    public ProjKind pierce() { pierce = true; return this; }
    public ProjKind homing() { homing = true; return this; }
    public ProjKind radius(float r) { hitRadius = r; return this; }
    public ProjKind impact(BiConsumer<SpireProjectile, Vec3> f) { onImpact = f; return this; }
    public ProjKind impactOnExpire() { impactOnExpire = true; return this; }
}
