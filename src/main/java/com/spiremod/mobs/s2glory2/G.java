package com.spiremod.mobs.s2glory2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.util.function.Consumer;

/** Small helpers shared by the Glory bosses. */
final class G {
    private G() {}

    /** make every face of a box emissive (flames, sand, energy) */
    static Box fire(Box b, int rgb) {
        b.color(rgb).flat();
        for (Face f : Face.values()) b.glow(f, 0, 0, f.w(b.dx, b.dy, b.dz), f.h(b.dx, b.dy, b.dz), rgb);
        return b;
    }

    /** cone melee ignoring i-frames, for multi-hit moves */
    static Move.Action cone(float sts, double range, double arc, Consumer<LivingEntity> onHit) {
        return (s, t) -> {
            Vec3 f = Fx.forward(s); double cos = Math.cos(Math.toRadians(arc / 2));
            for (LivingEntity e : Fx.foes(s, s.getBoundingBox().inflate(range, 1.5, range))) {
                Vec3 d = e.position().subtract(s.position()); Vec3 dh = new Vec3(d.x, 0, d.z);
                if (Math.sqrt(dh.lengthSqr()) - e.getBbWidth() * 0.5 - s.getBbWidth() * 0.5 > range) continue;
                if (dh.lengthSqr() > 1e-4 && dh.normalize().dot(f) < cos) continue;
                e.invulnerableTime = 0;
                if (Fx.dmg(s, e, sts) && onHit != null) onHit.accept(e);
            }
        };
    }
    static Move.Action cone(float sts, double range, double arc) { return cone(sts, range, arc, null); }

    /** single ranged hit on the target ignoring i-frames, with a particle line */
    static Move.Action beam(float sts, net.minecraft.core.particles.ParticleOptions p, double fwd, double up) {
        return (s, t) -> {
            if (t == null) return;
            Vec3 a = Fx.rel(s, fwd, 0, up), b = t.position().add(0, t.getBbHeight() * 0.6, 0);
            for (int i = 0; i <= 20; i++) Fx.particlesAt(s, p, a.lerp(b, i / 20.0), 1, 0.03, 0);
            t.invulnerableTime = 0; Fx.dmg(s, t, sts);
        };
    }
}
