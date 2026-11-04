package com.spiremod.core;

import com.spiremod.SpireMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Consumer;

/** Generic projectile used by every ranged attack (orbs, darts, fireballs, spores...). Look/physics come from ProjKind. */
public class SpireProjectile extends Projectile {
    public static final EntityDataAccessor<String> D_KIND = SynchedEntityData.defineId(SpireProjectile.class, EntityDataSerializers.STRING);
    public float damage;                    // StS damage (server)
    public Consumer<LivingEntity> onHit;    // server, extra on-hit
    public int age;
    public ProjKind kind;

    public SpireProjectile(EntityType<? extends SpireProjectile> t, Level l) { super(t, l); }
    public SpireProjectile(Level l, SpireMob owner, String kindId) {
        super(SpireMod.PROJECTILE.get(), l);
        setOwner(owner); setKind(kindId);
    }

    public void setKind(String id) { entityData.set(D_KIND, id); kind = ProjKind.of(id); }
    public ProjKind kind() { if (kind == null || !kind.id.equals(entityData.get(D_KIND))) kind = ProjKind.of(entityData.get(D_KIND)); return kind; }

    @Override protected void defineSynchedData() { entityData.define(D_KIND, ""); }
    @Override public boolean shouldBeSaved() { return false; }
    @Override public Packet<ClientGamePacketListener> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override protected void addAdditionalSaveData(CompoundTag t) {}
    @Override protected void readAdditionalSaveData(CompoundTag t) {}
    @Override protected boolean canHitEntity(Entity e) {
        if (e instanceof SpireMob) return false;
        if (e instanceof LivingEntity le && !Hostility.isSpireEnemy(le)) return false;
        return super.canHitEntity(e);
    }

    @Override public void tick() {
        super.tick();
        ProjKind k = kind(); if (k == null) { discard(); return; }
        age++;
        Vec3 mv = getDeltaMovement();
        HitResult hr = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hr.getType() != HitResult.Type.MISS && !level().isClientSide) onHit(hr);
        if (isRemoved()) return;
        setPos(getX() + mv.x, getY() + mv.y, getZ() + mv.z);
        mv = mv.scale(k.drag).add(0, -k.gravity, 0);
        if (k.homing && getOwner() instanceof SpireMob o && o.getTarget() != null) {
            LivingEntity t = o.getTarget();
            Vec3 want = new Vec3(t.getX(), t.getY() + t.getBbHeight() * .6, t.getZ()).subtract(position()).normalize().scale(mv.length());
            mv = mv.add(want.subtract(mv).scale(0.12));
        }
        setDeltaMovement(mv);
        ProjectileUtil.rotateTowardsMovement(this, 1f);
        if (level().isClientSide) {
            if (k.trail != null && age % Math.max(1, k.trailEvery) == 0) level().addParticle(k.trail, getX(), getY(), getZ(), 0, 0, 0);
        } else if (age > k.life) {
            if (k.impactOnExpire && k.onImpact != null) k.onImpact.accept(this, position());
            discard();
        }
    }

    @Override protected void onHitEntity(EntityHitResult r) {
        super.onHitEntity(r);
        if (level().isClientSide || !(r.getEntity() instanceof LivingEntity v)) return;
        if (getOwner() instanceof SpireMob o) { if (Fx.dmg(o, v, damage) && onHit != null) onHit.accept(v); }
        else v.hurt(damageSources().thrown(this, getOwner()), damage * Cfg.DMG);
        ProjKind k = kind();
        if (k != null && k.onImpact != null) k.onImpact.accept(this, position());
        if (k == null || !k.pierce) discard();
    }

    @Override protected void onHitBlock(net.minecraft.world.phys.BlockHitResult r) {
        super.onHitBlock(r);
        ProjKind k = kind();
        if (!level().isClientSide) {
            if (k != null && k.onImpact != null) k.onImpact.accept(this, r.getLocation());
            ((net.minecraft.server.level.ServerLevel) level()).sendParticles(ParticleTypes.POOF, getX(), getY(), getZ(), 3, 0.1, 0.1, 0.1, 0.01);
            discard();
        }
    }
}
