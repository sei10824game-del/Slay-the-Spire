package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/** Small shared helpers for this package (multi-hit melee without vanilla i-frames, StS turn -> seconds). */
final class Kit {
    private Kit() {}
    /** seconds per StS "turn" for debuff durations */
    static final int TURN = 3;

    /** melee hit that ignores vanilla invulnerability frames (so multi-hits each land) */
    static Move.Action hit(float sts, double range, double arc) {
        return (s, t) -> { if (t != null) t.invulnerableTime = 0; Fx.meleeNow(s, sts, range, arc, null); };
    }
    static Move.Action hit(float sts) { return hit(sts, 3.0, 110); }
    /** direct hit on the target (beams / unblockable reach) ignoring i-frames */
    static Move.Action zap(float sts) {
        return (s, t) -> { if (t != null) { t.invulnerableTime = 0; Fx.dmg(s, t, sts); } };
    }
    /** shoot that ignores i-frames on impact is not needed; plain gain-strength with puff */
    static void str(SpireMob s, int n) { s.addStrength(n); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.5); }
    static void block(SpireMob s, int n) { Fx.block(n).run(s, null); }
    static void effect(LivingEntity v, MobEffect e, int sec, int amp) { if (v != null) v.addEffect(new MobEffectInstance(e, sec * 20, amp)); }
    /** StS2 Tangled (attacks cost more): mining fatigue (slower attacks) + slight slow */
    static void tangled(LivingEntity v, int turns) { effect(v, MobEffects.DIG_SLOWDOWN, turns * TURN * 2, 1); effect(v, MobEffects.MOVEMENT_SLOWDOWN, turns * TURN, 0); }
    /** StS2 Infection status card (3 damage while held): short poison */
    static void infection(LivingEntity v) { effect(v, MobEffects.POISON, 4, 0); }
}
