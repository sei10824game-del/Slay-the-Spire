package com.spiremod.mobs.s1act3b;

import com.spiremod.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/** Shared helpers for this group (Artifact, ally buffs, Back Attack). */
final class S3 {
    private S3() {}
    /** seconds per StS "turn" when converting debuff durations */
    static final int TURN = 4;

    /** all living Spire creatures near s (including s) */
    static List<SpireMob> allies(SpireMob s, double r) {
        return s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(r), m -> m.isAlive());
    }

    /** Artifact: call from onTick. Negates harmful effects while charges ("artifact" counter) remain. */
    static void artifact(SpireMob s) {
        if (s.count("artifact") <= 0 || s.tickCount % 5 != 0) return;
        for (MobEffectInstance e : new ArrayList<>(s.getActiveEffects())) {
            if (e.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                s.removeEffect(e.getEffect());
                s.setCount("artifact", s.count("artifact") - 1);
                Fx.particles(s, ParticleTypes.ENCHANT, 20, 0.6);
                Fx.sound(s, net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, 1.2f, 0.8f);
                if (s.count("artifact") <= 0) return;
            }
        }
    }

    /** remove all harmful effects (Haste / Rebirth) */
    static void cleanse(SpireMob s) {
        for (MobEffectInstance e : new ArrayList<>(s.getActiveEffects()))
            if (e.getEffect().getCategory() == MobEffectCategory.HARMFUL) s.removeEffect(e.getEffect());
    }

    /** multi-hit helper: clears the target's hurt-cooldown so rapid StS multi-hits all land */
    static Move.Action multi(Move.Action a) {
        return (s, t) -> { if (t != null) t.invulnerableTime = 0; a.run(s, t); };
    }

    static boolean behind(SpireMob s, LivingEntity v) {
        Vec3 look = v.getLookAngle(); Vec3 toMe = s.position().subtract(v.position());
        return look.x * toMe.x + look.z * toMe.z < 0;
    }

    /** Back Attack (Spire Shield/Spear) melee: +50% damage when the victim is facing away from the attacker */
    static Move.Action backMelee(float sts, double range, double arc, java.util.function.Consumer<LivingEntity> onHit) {
        return (s, t) -> {
            if (t != null) t.invulnerableTime = 0;
            float d = sts;
            if (t != null && behind(s, t)) { d = sts * 1.5f + s.getStrength() * 0.5f; Fx.particlesAt(s, ParticleTypes.CRIT, t.position().add(0, 1, 0), 14, 0.3, 0.2); }
            Fx.meleeNow(s, d, range, arc, onHit);
        };
    }
}
