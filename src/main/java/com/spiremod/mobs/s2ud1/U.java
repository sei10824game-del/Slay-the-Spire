package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import java.util.ArrayList;

/** Tiny shared helpers for the Underdocks creatures. */
final class U {
    private U() {}

    static void hurt(ModelSpec m, String p) {
        m.clip("hurt", 10).rot(p, 0, 0, 0, 0).rot(p, 3, -14, 0, 8).rot(p, 10, 0, 0, 0)
            .scl(p, 0, 1, 1, 1).scl(p, 3, 1.08f, 0.9f, 1.08f).scl(p, 10, 1, 1, 1);
    }

    /** topple sideways and squash; sink = how far the pivot drops (px) */
    static void death(ModelSpec m, String p, float roll, float sink) {
        m.clip("death", 30).rot(p, 0, 0, 0, 0).rot(p, 10, -12, 0, roll * 0.3f).rot(p, 22, 0, 0, roll, Ease.OUT_BOUNCE).rot(p, 30, 0, 0, roll)
            .pos(p, 0, 0, 0, 0).pos(p, 22, 0, -sink, 0, Ease.OUT_BOUNCE).pos(p, 30, 0, -sink, 0)
            .scl(p, 0, 1, 1, 1).scl(p, 30, 1.08f, 0.8f, 1.08f);
    }

    /** simple two-phase hop-walk for blobby things */
    static void hopWalk(ModelSpec m, String p) {
        m.clip("walk", 20).loop().pos(p, 0, 0, 0, 0).pos(p, 5, 0, 1.2f, 0).pos(p, 10, 0, 0, 0).pos(p, 15, 0, 1.2f, 0).pos(p, 20, 0, 0, 0)
            .rot(p, 0, 0, 0, 4).rot(p, 10, 0, 0, -4).rot(p, 20, 0, 0, 4);
    }

    /** multi-hit helper: clears the target's hurt cooldown so every StS hit lands */
    static Move.Action hit(float dmg, double range, double arc, java.util.function.Consumer<LivingEntity> onHit) {
        return (s, t) -> { if (t != null) t.invulnerableTime = 0; Fx.meleeNow(s, dmg, range, arc, onHit); };
    }

    /** Artifact: call from onTick; negates harmful effects while counter "artifact" > 0 */
    static void artifact(SpireMob s) {
        if (s.count("artifact") <= 0 || s.tickCount % 5 != 0) return;
        for (MobEffectInstance e : new ArrayList<>(s.getActiveEffects())) {
            if (e.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                s.removeEffect(e.getEffect());
                s.setCount("artifact", s.count("artifact") - 1);
                Fx.particles(s, ParticleTypes.ENCHANT, 20, 0.6);
                if (s.count("artifact") <= 0) return;
            }
        }
    }
}
