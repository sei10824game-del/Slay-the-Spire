package com.spiremod.core;

import com.spiremod.SpireMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;

/** The one entity class behind every Slay the Spire creature. Behaviour comes from its MobDef. */
public class SpireMob extends Monster {
    public static final EntityDataAccessor<String> D_ANIM = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Long> D_ANIM_START = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.LONG);
    public static final EntityDataAccessor<String> D_BASE = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> D_INTENT = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> D_PHASE = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> D_VAR = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> D_INTENT_COLOR = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> D_BLOCK = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> D_STRENGTH = SynchedEntityData.defineId(SpireMob.class, EntityDataSerializers.INT);

    public final MobDef def;
    private final ServerBossEvent bossEvent;
    // server-side AI state
    private final List<String> history = new ArrayList<>();
    private int turn;
    Move current, planned;
    int moveTick, cooldown = 20;
    private int stunTicks;
    private final Map<String, Integer> counters = new HashMap<>();
    public final Set<String> flags = new HashSet<>();
    public int spawnAge;
    private java.util.UUID leaderId;
    /** gold stolen by this creature (nuggets); dropped on death, kept when it escapes */
    public int stolenGold;
    // client-side fade tracking
    public String clientFade; public long clientFadeStart; private String lastClientAnim = "";

    public SpireMob(EntityType<? extends SpireMob> type, Level level, MobDef def) {
        super(type, level);
        this.def = def;
        this.xpReward = def.boss ? 100 : def.elite ? 30 : 10;
        this.bossEvent = def.boss ? new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_10) : null;
        if (def.flying) {
            this.moveControl = new FlyingMoveControl(this, 20, true);
            this.setNoGravity(true);
        }
        if (def.fireImmune) this.setPathfindingMalus(net.minecraft.world.level.pathfinder.BlockPathTypes.LAVA, 0f);
    }

    public static AttributeSupplier.Builder attrs(MobDef d) {
        AttributeSupplier.Builder b = Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, d.hp).add(Attributes.MOVEMENT_SPEED, d.speed).add(Attributes.ARMOR, d.armor)
                .add(Attributes.KNOCKBACK_RESISTANCE, d.kbResist).add(Attributes.FOLLOW_RANGE, d.followRange)
                .add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.ATTACK_KNOCKBACK, 0.4);
        if (d.flying) b.add(Attributes.FLYING_SPEED, d.speed * 1.4);
        return b;
    }

    @Override protected PathNavigation createNavigation(Level level) {
        if (def != null && def.flying) { FlyingPathNavigation n = new FlyingPathNavigation(this, level); n.setCanOpenDoors(false); n.setCanFloat(true); n.setCanPassDoors(true); return n; }
        return super.createNavigation(level);
    }

    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(D_ANIM, ""); entityData.define(D_ANIM_START, 0L); entityData.define(D_BASE, "idle");
        entityData.define(D_INTENT, ""); entityData.define(D_PHASE, 0); entityData.define(D_VAR, 0);
        entityData.define(D_INTENT_COLOR, 0xFFFFFF); entityData.define(D_BLOCK, 0); entityData.define(D_STRENGTH, 0);
    }

    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new SpireMoveGoal(this));
        if (def.flying) goalSelector.addGoal(6, new WaterAvoidingRandomFlyingGoal(this, 0.8));
        else goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10f));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this, SpireMob.class).setAlertOthers());
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, Hostility::isSpireEnemy));
    }

    // ---------------- leader / minions ----------------
    public void setLeader(SpireMob l) { leaderId = l == null ? null : l.getUUID(); }
    public SpireMob getLeader() {
        if (leaderId == null || !(level() instanceof ServerLevel sl)) return null;
        return sl.getEntity(leaderId) instanceof SpireMob m ? m : null;
    }
    /** all living minions of this creature (summoned with Fx.summon) */
    public List<SpireMob> minions() {
        List<SpireMob> out = new ArrayList<>();
        for (SpireMob m : level().getEntitiesOfClass(SpireMob.class, getBoundingBox().inflate(64))) if (m != this && m.isAlive() && getUUID().equals(m.leaderId)) out.add(m);
        return out;
    }
    // ---------------- accessors for defs ----------------
    public int getTurn() { return turn; }
    public RandomSource rand() { return random; }
    public String lastMove() { return history.isEmpty() ? "" : history.get(history.size() - 1); }
    public String moveAgo(int n) { int i = history.size() - 1 - n; return i < 0 ? "" : history.get(i); }
    /** how many times in a row `id` was the most recent move */
    public int repeatCount(String id) { int c = 0; for (int i = history.size() - 1; i >= 0 && history.get(i).equals(id); i--) c++; return c; }
    public int count(String key) { return counters.getOrDefault(key, 0); }
    public int inc(String key) { return counters.merge(key, 1, Integer::sum); }
    public void setCount(String key, int v) { counters.put(key, v); }
    public int getStrength() { return entityData.get(D_STRENGTH); }
    public void addStrength(int n) { entityData.set(D_STRENGTH, getStrength() + n); }
    void resetBlock() { entityData.set(D_BLOCK, 0); }
    void clearIntent() { entityData.set(D_INTENT, ""); }
    public int getBlock() { return entityData.get(D_BLOCK); }
    public void addBlock(int n) { entityData.set(D_BLOCK, Math.max(0, getBlock() + n)); }
    public int getPhase() { return entityData.get(D_PHASE); }
    public void setPhase(int p) { entityData.set(D_PHASE, p); }
    public int getVar() { return entityData.get(D_VAR); }
    public void setVar(int v) { entityData.set(D_VAR, v); }
    public String getBase() { return entityData.get(D_BASE); }
    /** set looping base stance clip ("idle", "sleep", "curl", ...) */
    public void setBase(String b) { entityData.set(D_BASE, b); }
    public String getAnim() { return entityData.get(D_ANIM); }
    public Move currentMove() { return current; }
    public boolean isStunned() { return stunTicks > 0; }
    /** interrupt: cancel current move and freeze for n ticks */
    public void stun(int ticks) { stunTicks = ticks; cancelMove(); }
    public void cancelMove() { if (current != null) { current = null; setAnimClip(""); } planned = null; cooldown = Math.max(cooldown, 10); }
    /** force the next move id */
    public void forceNext(String id) { Move m = def.moves.get(id); if (m != null) { planned = m; refreshIntent(); } }
    public LivingEntity foe() { return getTarget(); }

    void setAnimClip(String clip) {
        entityData.set(D_ANIM, clip);
        entityData.set(D_ANIM_START, level().getGameTime());
    }

    void refreshIntent() {
        if (planned == null) { entityData.set(D_INTENT, ""); return; }
        entityData.set(D_INTENT, planned.intentText(this));
        entityData.set(D_INTENT_COLOR, planned.intent.color);
    }

    // ---------------- lifecycle ----------------
    @Nullable @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor lvl, DifficultyInstance diff, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        SpawnGroupData r = super.finalizeSpawn(lvl, diff, type, data, tag);
        setBase(def.initialBase);
        if (def.onSpawn != null) def.onSpawn.accept(this);
        return r;
    }

    @Override public void tick() {
        super.tick();
        spawnAge++;
        if (level().isClientSide) {
            String a = getAnim();
            if (!a.equals(lastClientAnim)) {
                if (a.isEmpty() && !lastClientAnim.isEmpty()) { clientFade = lastClientAnim; clientFadeStart = level().getGameTime(); }
                lastClientAnim = a;
            }
            if (def.onClientTick != null) def.onClientTick.run(this);
        } else {
            if (stunTicks > 0) stunTicks--;
            if (def.minion && leaderId != null && tickCount % 10 == 0) {
                SpireMob l = getLeader();
                if (l == null || !l.isAlive()) hurt(damageSources().genericKill(), Float.MAX_VALUE);
            }
            if (def.onTick != null) def.onTick.run(this);
            if (bossEvent != null) bossEvent.setProgress(getHealth() / getMaxHealth());
        }
    }

    @Override protected void tickDeath() {
        ++deathTime;
        int len = def.deathLen();
        if (deathTime >= len && !level().isClientSide() && !isRemoved()) {
            level().broadcastEntityEvent(this, (byte) 60);
            remove(RemovalReason.KILLED);
        }
    }

    @Override public void die(DamageSource src) {
        if (!level().isClientSide && def.onDeath != null && !dead) def.onDeath.run(this, src);
        super.die(src);
    }

    @Override protected void dropCustomDeathLoot(DamageSource src, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(src, looting, recentlyHit);
        int n = def.goldMin + random.nextInt(Math.max(1, def.goldMax - def.goldMin + 1));
        if (def.boss) spawnAtLocation(new ItemStack(Items.GOLD_INGOT, n)); else spawnAtLocation(new ItemStack(Items.GOLD_NUGGET, n));
        if (stolenGold > 0) spawnAtLocation(new ItemStack(Items.GOLD_NUGGET, stolenGold));
        if (def.elite && !def.boss) spawnAtLocation(new ItemStack(Items.GOLD_INGOT, 1 + random.nextInt(2)));
    }

    @Override public boolean hurt(DamageSource src, float amount) {
        if (level().isClientSide) return super.hurt(src, amount);
        if (def.onHurt != null) amount = def.onHurt.run(this, src, amount);
        int b = getBlock();
        if (b > 0 && amount > 0 && !src.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            float scaled = b * Cfg.DMG;
            if (amount <= scaled) { entityData.set(D_BLOCK, b - Math.round(amount / Cfg.DMG)); playSound(net.minecraft.sounds.SoundEvents.SHIELD_BLOCK, 0.8f, 1f); return false; }
            amount -= scaled; entityData.set(D_BLOCK, 0);
        }
        if (amount <= 0) return false;
        return super.hurt(src, amount);
    }

    @Override public boolean removeWhenFarAway(double d) { return !def.boss && !hasCustomName() && def.spawnWeight > 0 && d > 128 * 128; }
    @Override public boolean canBeLeashed(Player p) { return false; }
    @Override public boolean fireImmune() { return def.fireImmune; }
    @Override public boolean isPushable() { return !def.noPush && super.isPushable(); }
    @Override protected boolean isSunBurnTick() { return false; }
    @Override public boolean causeFallDamage(float f, float m, DamageSource s) { return def.flying ? false : super.causeFallDamage(f, m, s); }
    @Override protected void checkFallDamage(double y, boolean g, BlockState s, BlockPos p) { if (!def.flying) super.checkFallDamage(y, g, s, p); }
    @Override public int getMaxSpawnClusterSize() { return Math.max(1, def.groupMax); }
    @Override public boolean isMaxGroupSizeReached(int n) { return n >= Math.max(1, def.groupMax); }
    @Override public Packet<ClientGamePacketListener> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override public Component getName() { return Component.translatable("entity." + SpireMod.ID + "." + def.id); }

    @Override protected SoundEvent getAmbientSound() { return def.style.ambient; }
    @Override protected SoundEvent getHurtSound(DamageSource s) { return def.style.hurt; }
    @Override protected SoundEvent getDeathSound() { return def.style.death; }
    @Override protected void playStepSound(BlockPos p, BlockState s) { if (def.style.step != null) playSound(def.style.step, 0.15f, 1f); }
    @Override protected float getSoundVolume() { return def.boss ? 1.4f : 1f; }

    @Override public void startSeenByPlayer(ServerPlayer p) { super.startSeenByPlayer(p); if (bossEvent != null) bossEvent.addPlayer(p); }
    @Override public void stopSeenByPlayer(ServerPlayer p) { super.stopSeenByPlayer(p); if (bossEvent != null) bossEvent.removePlayer(p); }
    @Override public void setCustomName(@Nullable Component n) { super.setCustomName(n); if (bossEvent != null) bossEvent.setName(getDisplayName()); }

    @Override public void addAdditionalSaveData(CompoundTag t) {
        super.addAdditionalSaveData(t);
        t.putInt("Turn", turn); t.putInt("Phase", getPhase()); t.putInt("Var", getVar()); t.putInt("Str", getStrength());
        t.putString("Base", getBase());
        CompoundTag c = new CompoundTag(); counters.forEach(c::putInt); t.put("Counters", c);
        t.putString("Flags", String.join(",", flags)); t.putInt("Stolen", stolenGold);
        if (leaderId != null) t.putUUID("Leader", leaderId);
    }
    @Override public void readAdditionalSaveData(CompoundTag t) {
        super.readAdditionalSaveData(t);
        turn = t.getInt("Turn"); setPhase(t.getInt("Phase")); setVar(t.getInt("Var")); entityData.set(D_STRENGTH, t.getInt("Str"));
        if (t.contains("Base")) setBase(t.getString("Base"));
        CompoundTag c = t.getCompound("Counters"); for (String k : c.getAllKeys()) counters.put(k, c.getInt(k));
        stolenGold = t.getInt("Stolen"); leaderId = t.hasUUID("Leader") ? t.getUUID("Leader") : null;
        flags.clear(); for (String s : t.getString("Flags").split(",")) if (!s.isEmpty()) flags.add(s);
        if (bossEvent != null) bossEvent.setName(getDisplayName());
    }

    // called by the move goal
    void recordMove(String id) { history.add(id); if (history.size() > 24) history.remove(0); turn++; }
}
