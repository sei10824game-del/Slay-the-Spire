package com.spiremod.core;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Spire creatures hate everything that is not a Spire creature, and everything that is not a Spire creature hates them. */
@Mod.EventBusSubscriber(modid = com.spiremod.SpireMod.ID)
public final class Hostility {
    private Hostility() {}

    /** valid target for a Spire creature */
    public static boolean isSpireEnemy(LivingEntity e) {
        if (e instanceof SpireMob || e instanceof ArmorStand || !e.isAlive()) return false;
        if (e instanceof Player p) return !p.isCreative() && !p.isSpectator();
        return true;
    }

    @SubscribeEvent
    public static void join(EntityJoinLevelEvent ev) {
        if (ev.getLevel().isClientSide() || !(ev.getEntity() instanceof Mob mob) || mob instanceof SpireMob) return;
        if (mob.getTags().contains("spiremod_hostile")) return;
        mob.addTag("spiremod_hostile");
        mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, SpireMob.class, 10, true, false, e -> e.isAlive()));
        if (mob instanceof PathfinderMob pm && mob.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
            boolean hasAttack = false;
            for (var w : mob.goalSelector.getAvailableGoals()) {
                Goal g = w.getGoal();
                String n = g.getClass().getSimpleName();
                if (n.contains("Attack") || n.contains("Melee") || n.contains("Ranged") || n.contains("Shoot")) { hasAttack = true; break; }
            }
            if (!hasAttack) mob.goalSelector.addGoal(3, new MeleeAttackGoal(pm, 1.2, true));
        }
    }

    /** give every vanilla creature a base attack damage so melee works on cows, villagers, ... */
    @SubscribeEvent
    public static void attrs(EntityAttributeModificationEvent ev) {
        for (EntityType<? extends LivingEntity> t : ev.getTypes()) {
            if (!ev.has(t, Attributes.ATTACK_DAMAGE)) ev.add(t, Attributes.ATTACK_DAMAGE, 2.0);
        }
    }
}
