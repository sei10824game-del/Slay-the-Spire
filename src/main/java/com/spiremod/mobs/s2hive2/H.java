package com.spiremod.mobs.s2hive2;

import com.spiremod.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

/** Small shared helpers for this group. */
final class H {
    private H() {}

    /** cone melee that ignores i-frames (multi-hit moves), with an optional per-victim damage bonus */
    static int cone(SpireMob s, float sts, double range, double arc, java.util.function.ToDoubleFunction<LivingEntity> bonus, Consumer<LivingEntity> onHit) {
        Vec3 f = Fx.forward(s); int n = 0;
        double cos = Math.cos(Math.toRadians(arc / 2));
        for (LivingEntity e : Fx.foes(s, s.getBoundingBox().inflate(range, 2, range))) {
            Vec3 d = e.position().subtract(s.position()); Vec3 dh = new Vec3(d.x, 0, d.z);
            double dist = Math.sqrt(dh.lengthSqr()) - e.getBbWidth() * 0.5 - s.getBbWidth() * 0.5;
            if (dist > range) continue;
            if (dh.lengthSqr() > 1e-4 && dh.normalize().dot(f) < cos) continue;
            e.invulnerableTime = 0;
            float b = bonus == null ? 0 : (float) bonus.applyAsDouble(e);
            if (Fx.dmg(s, e, sts + b)) { n++; if (onHit != null) onHit.accept(e); }
        }
        return n;
    }
    static Move.Action cone(float sts, double range, double arc) { return (s, t) -> cone(s, sts, range, arc, null, null); }
    static Move.Action cone(float sts, double range, double arc, Consumer<LivingEntity> onHit) { return (s, t) -> cone(s, sts, range, arc, null, onHit); }

    /** single hit on the target ignoring i-frames */
    static void hit(SpireMob s, LivingEntity t, float sts) { if (t != null) { t.invulnerableTime = 0; Fx.dmg(s, t, sts); } }

    /** particle line between two points */
    static void line(SpireMob s, ParticleOptions p, Vec3 a, Vec3 b, int n) {
        for (int i = 0; i <= n; i++) { Vec3 q = a.lerp(b, i / (double) n); Fx.particlesAt(s, p, q, 1, 0.02, 0); }
    }

    static List<Player> players(SpireMob s, double r) { return s.level().getEntitiesOfClass(Player.class, s.getBoundingBox().inflate(r), p -> p.isAlive() && !p.isSpectator() && !p.isCreative()); }

    static void say(SpireMob s, double r, String msg) { for (Player p : players(s, r)) p.displayClientMessage(Component.literal(msg), false); }
    static void bar(Player p, String msg) { p.displayClientMessage(Component.literal(msg), true); }

    static AABB around(SpireMob s, double r) { return s.getBoundingBox().inflate(r); }
}
