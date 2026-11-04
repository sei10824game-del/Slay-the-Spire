package com.spiremod.core;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Helpers for Move actions. Damage values are StS numbers (scaled by Cfg.DMG automatically, + strength, - weak).
 * Most return a Move.Action so they can be passed straight to Move.at(tick, Fx.melee(11)).
 */
public final class Fx {
    private Fx() {}

    // ------------------------------------------------------------ targeting
    /** living things this creature considers enemies, inside box */
    public static List<LivingEntity> foes(SpireMob s, AABB box) {
        List<LivingEntity> out = new ArrayList<>();
        for (LivingEntity e : s.level().getEntitiesOfClass(LivingEntity.class, box)) if (e != s && Hostility.isSpireEnemy(e)) out.add(e);
        return out;
    }

    /** forward unit vector of the body (horizontal) */
    public static Vec3 forward(SpireMob s) {
        float r = s.yBodyRot * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(r), 0, Mth.cos(r));
    }
    /** world position `fwd` blocks ahead, `side` blocks to the entity's left, `up` blocks above feet */
    public static Vec3 rel(SpireMob s, double fwd, double side, double up) {
        Vec3 f = forward(s); Vec3 l = new Vec3(f.z, 0, -f.x);
        return s.position().add(f.scale(fwd)).add(l.scale(side)).add(0, up, 0);
    }

    // ------------------------------------------------------------ damage
    /** apply one hit of `sts` StS damage. returns true if it landed */
    public static boolean dmg(SpireMob s, LivingEntity v, float sts) {
        float d = (sts + s.getStrength()) * Cfg.DMG;
        if (s.hasEffect(MobEffects.WEAKNESS)) d *= Cfg.WEAK_MULT;
        if (d <= 0) return false;
        return v.hurt(s.damageSources().mobAttack(s), d);
    }
    /** same but with unscaled raw Minecraft damage (hp) */
    public static boolean raw(SpireMob s, LivingEntity v, float hp) { return v.hurt(s.damageSources().mobAttack(s), hp); }

    public static void knock(SpireMob s, LivingEntity v, double strength) {
        Vec3 d = v.position().subtract(s.position()); d = new Vec3(d.x, 0, d.z);
        if (d.lengthSqr() < 1e-4) d = forward(s);
        d = d.normalize().scale(strength);
        v.setDeltaMovement(v.getDeltaMovement().add(d.x, 0.25 + strength * 0.12, d.z)); v.hurtMarked = true;
    }

    /** frontal cone melee hit */
    public static Move.Action melee(float sts) { return melee(sts, 3.2, 110, null); }
    public static Move.Action melee(float sts, double range, double arcDeg) { return melee(sts, range, arcDeg, null); }
    public static Move.Action melee(float sts, double range, double arcDeg, Consumer<LivingEntity> onHit) {
        return (s, t) -> meleeNow(s, sts, range, arcDeg, onHit);
    }
    public static int meleeNow(SpireMob s, float sts, double range, double arcDeg, Consumer<LivingEntity> onHit) {
        Vec3 f = forward(s); int n = 0;
        double cos = Math.cos(Math.toRadians(arcDeg / 2));
        for (LivingEntity e : foes(s, s.getBoundingBox().inflate(range, 1.5, range))) {
            Vec3 d = e.position().subtract(s.position()); Vec3 dh = new Vec3(d.x, 0, d.z);
            double dist = Math.sqrt(dh.lengthSqr()) - e.getBbWidth() * 0.5 - s.getBbWidth() * 0.5;
            if (dist > range) continue;
            if (dh.lengthSqr() > 1e-4 && dh.normalize().dot(f) < cos) continue;
            if (dmg(s, e, sts)) { n++; if (onHit != null) onHit.accept(e); }
        }
        if (n > 0) s.level().playSound(null, s.getX(), s.getY(), s.getZ(), net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE, 0.8f, 0.8f);
        return n;
    }
    /** radial hit around the creature */
    public static Move.Action aoe(float sts, double radius) { return aoe(sts, radius, null); }
    public static Move.Action aoe(float sts, double radius, Consumer<LivingEntity> onHit) {
        return (s, t) -> aoeAt(s, s.position(), sts, radius, onHit);
    }
    public static int aoeAt(SpireMob s, Vec3 c, float sts, double radius, Consumer<LivingEntity> onHit) {
        int n = 0;
        for (LivingEntity e : foes(s, new AABB(c, c).inflate(radius, 2.5, radius))) {
            double dx = e.getX() - c.x, dz = e.getZ() - c.z;
            if (Math.sqrt(dx * dx + dz * dz) - e.getBbWidth() * 0.5 > radius) continue;
            if (dmg(s, e, sts)) { n++; if (onHit != null) onHit.accept(e); }
        }
        return n;
    }
    /** direct hit on the move's target regardless of position (beams, spells) */
    public static Move.Action hitTarget(float sts, Consumer<LivingEntity> onHit) {
        return (s, t) -> { if (t != null && dmg(s, t, sts) && onHit != null) onHit.accept(t); };
    }
    public static Move.Action hitTarget(float sts) { return hitTarget(sts, null); }

    // ------------------------------------------------------------ status
    public static void weak(LivingEntity v, int sec) { v.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, sec * 20)); }
    public static void vulnerable(LivingEntity v, int sec) { v.addEffect(new MobEffectInstance(ModEffects.VULNERABLE.get(), sec * 20)); }
    public static void frail(LivingEntity v, int sec) { v.addEffect(new MobEffectInstance(ModEffects.FRAIL.get(), sec * 20)); }
    public static void poison(LivingEntity v, int sec) { v.addEffect(new MobEffectInstance(MobEffects.POISON, sec * 20)); }
    public static void slow(LivingEntity v, int sec, int amp) { v.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, sec * 20, amp)); }
    public static void blind(LivingEntity v, int sec) { v.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, sec * 20)); }

    /** StS "status card" curses mapped to Minecraft */
    public enum Curse { SLIMED, DAZED, BURN, WOUND, VOID, DRAIN }
    public static void curse(LivingEntity v, Curse c) {
        switch (c) {
            case SLIMED: v.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0)); break;
            case DAZED: v.addEffect(new MobEffectInstance(ModEffects.DAZED.get(), 160)); v.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60)); break;
            case BURN: v.setSecondsOnFire(4); break;
            case WOUND: v.addEffect(new MobEffectInstance(MobEffects.WITHER, 60)); break;
            case VOID: v.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 200)); v.addEffect(new MobEffectInstance(MobEffects.HUNGER, 120)); break;
            case DRAIN: v.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120)); v.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60)); break;
        }
    }

    // ------------------------------------------------------------ self buffs
    public static Move.Action block(int n) { return (s, t) -> { s.addBlock(n); s.level().playSound(null, s.getX(), s.getY(), s.getZ(), net.minecraft.sounds.SoundEvents.ANVIL_PLACE, SoundSource.HOSTILE, 0.5f, 1.4f); }; }
    public static Move.Action strength(int n) { return (s, t) -> { s.addStrength(n); particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.4); }; }
    public static Move.Action heal(float hp) { return (s, t) -> { s.heal(hp); particles(s, ParticleTypes.HEART, 5, 0.4); }; }
    public static Move.Action heal(float hp, boolean unused) { return heal(hp); }

    // ------------------------------------------------------------ world fx
    public static void particles(SpireMob s, ParticleOptions p, int n, double spread) {
        if (s.level() instanceof ServerLevel l) l.sendParticles(p, s.getX(), s.getY() + s.getBbHeight() * 0.6, s.getZ(), n, spread, spread, spread, 0.02);
    }
    public static void particlesAt(SpireMob s, ParticleOptions p, Vec3 at, int n, double spread, double speed) {
        if (s.level() instanceof ServerLevel l) l.sendParticles(p, at.x, at.y, at.z, n, spread, spread, spread, speed);
    }
    /** expanding ring of particles on the ground (shockwaves, slams) */
    public static void ring(SpireMob s, ParticleOptions p, Vec3 c, double radius, int count) {
        if (!(s.level() instanceof ServerLevel l)) return;
        for (int i = 0; i < count; i++) { double a = i * Math.PI * 2 / count; l.sendParticles(p, c.x + Math.cos(a) * radius, c.y + 0.1, c.z + Math.sin(a) * radius, 1, 0.05, 0.05, 0.05, 0.02); }
    }
    public static void sound(SpireMob s, SoundEvent e, float vol, float pitch) {
        s.level().playSound(null, s.getX(), s.getY(), s.getZ(), e, SoundSource.HOSTILE, vol, pitch);
    }
    public static Move.Action sfx(SoundEvent e, float vol, float pitch) { return (s, t) -> sound(s, e, vol, pitch); }
    public static Move.Action sfx(SoundEvent e) { return sfx(e, 1f, 1f); }

    /** leap/dash toward the target (or forward). speed ~0.6 = short hop, 1.2 = long lunge */
    public static Move.Action dash(double speed, double up) {
        return (s, t) -> {
            Vec3 d = t != null ? new Vec3(t.getX() - s.getX(), 0, t.getZ() - s.getZ()) : forward(s);
            d = d.lengthSqr() < 1e-4 ? forward(s) : d.normalize();
            s.setDeltaMovement(d.x * speed, up, d.z * speed); s.hurtMarked = true;
        };
    }
    /** teleport-style hop: move creature to a point relative to the target */
    public static void teleportBehind(SpireMob s, LivingEntity t, double dist) {
        Vec3 d = t.getLookAngle(); d = new Vec3(d.x, 0, d.z).normalize();
        s.teleportTo(t.getX() - d.x * dist, t.getY(), t.getZ() - d.z * dist);
    }

    // ------------------------------------------------------------ summoning
    public static SpireMob summon(SpireMob owner, String id, double dx, double dz) {
        MobDef d = Catalog.get(id);
        if (d == null || !(owner.level() instanceof ServerLevel lvl)) return null;
        SpireMob m = d.type.get().create(lvl);
        if (m == null) return null;
        m.moveTo(owner.getX() + dx, owner.getY(), owner.getZ() + dz, owner.getYRot(), 0);
        m.finalizeSpawn((ServerLevelAccessor) lvl, lvl.getCurrentDifficultyAt(m.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        lvl.addFreshEntity(m);
        m.setLeader(owner);
        if (owner.getTarget() != null) m.setTarget(owner.getTarget());
        particlesAt(owner, ParticleTypes.POOF, m.position().add(0, 0.5, 0), 8, 0.3, 0.02);
        return m;
    }
    /** summon around a circle */
    public static Move.Action summonRing(String id, int n, double r) {
        return (s, t) -> { for (int i = 0; i < n; i++) { double a = i * Math.PI * 2 / n + s.rand().nextDouble(); summon(s, id, Math.cos(a) * r, Math.sin(a) * r); } };
    }
    public static int countAlive(SpireMob s, String id, double radius) {
        int n = 0;
        for (SpireMob m : s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(radius))) if (m != s && m.isAlive() && m.def.id.equals(id)) n++;
        return n;
    }

    /** steal up to `nuggets` gold nuggets worth (nugget=1, ingot=9, emerald=4) from a player; returns amount taken */
    public static int steal(SpireMob s, LivingEntity v, int nuggets) {
        if (!(v instanceof net.minecraft.world.entity.player.Player p)) return 0;
        int left = nuggets;
        for (net.minecraft.world.item.ItemStack st : p.getInventory().items) {
            int val = st.is(net.minecraft.world.item.Items.GOLD_NUGGET) ? 1 : st.is(net.minecraft.world.item.Items.GOLD_INGOT) ? 9 : st.is(net.minecraft.world.item.Items.EMERALD) ? 4 : 0;
            while (val > 0 && !st.isEmpty() && left > 0) { st.shrink(1); left -= val; }
            if (left <= 0) break;
        }
        int taken = nuggets - Math.max(0, left);
        s.stolenGold += taken;
        return taken;
    }
    /** vanish in a puff of smoke (escape moves) */
    public static void vanish(SpireMob s) { particles(s, ParticleTypes.POOF, 20, 0.4); sound(s, net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT, 1f, 1.5f); s.discard(); }

    // ------------------------------------------------------------ projectiles
    /** fire a projectile at the move's target from `up` blocks above feet, `fwd` ahead */
    public static Move.Action shoot(String kind, float stsDmg, float speed) { return shoot(kind, stsDmg, speed, 0, 1.2, 0.4, null); }
    public static Move.Action shoot(String kind, float stsDmg, float speed, float spreadDeg, double up, double fwd, Consumer<LivingEntity> onHit) {
        return (s, t) -> { if (t != null) shootAt(s, kind, rel(s, fwd, 0, up), t, stsDmg, speed, spreadDeg, onHit); };
    }
    public static SpireProjectile shootAt(SpireMob s, String kind, Vec3 from, LivingEntity t, float stsDmg, float speed, float spreadDeg, Consumer<LivingEntity> onHit) {
        SpireProjectile p = new SpireProjectile(s.level(), s, kind);
        p.setPos(from);
        Vec3 aim = new Vec3(t.getX(), t.getY() + t.getBbHeight() * 0.6, t.getZ()).subtract(from);
        aim = aim.normalize();
        if (spreadDeg > 0) {
            double yaw = (s.rand().nextDouble() - .5) * Math.toRadians(spreadDeg) * 2, pit = (s.rand().nextDouble() - .5) * Math.toRadians(spreadDeg) * 2;
            double c = Math.cos(yaw), sn = Math.sin(yaw);
            aim = new Vec3(aim.x * c - aim.z * sn, aim.y + pit, aim.x * sn + aim.z * c).normalize();
        }
        p.shoot(aim.x, aim.y, aim.z, speed, 0f);
        p.damage = stsDmg; p.onHit = onHit;
        s.level().addFreshEntity(p);
        return p;
    }
}
