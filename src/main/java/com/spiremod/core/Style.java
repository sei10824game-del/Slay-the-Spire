package com.spiremod.core;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

/** Vanilla sound presets for a creature. */
public enum Style {
    ZOMBIE(SoundEvents.ZOMBIE_AMBIENT, SoundEvents.ZOMBIE_HURT, SoundEvents.ZOMBIE_DEATH, SoundEvents.ZOMBIE_STEP),
    SLIME(SoundEvents.SLIME_SQUISH, SoundEvents.SLIME_HURT, SoundEvents.SLIME_DEATH, SoundEvents.SLIME_SQUISH_SMALL),
    SKELETON(SoundEvents.SKELETON_AMBIENT, SoundEvents.SKELETON_HURT, SoundEvents.SKELETON_DEATH, SoundEvents.SKELETON_STEP),
    SPIDER(SoundEvents.SPIDER_AMBIENT, SoundEvents.SPIDER_HURT, SoundEvents.SPIDER_DEATH, SoundEvents.SPIDER_STEP),
    METAL(SoundEvents.IRON_GOLEM_STEP, SoundEvents.IRON_GOLEM_HURT, SoundEvents.IRON_GOLEM_DEATH, SoundEvents.IRON_GOLEM_STEP),
    GHOST(SoundEvents.VEX_AMBIENT, SoundEvents.VEX_HURT, SoundEvents.VEX_DEATH, null),
    FIRE(SoundEvents.BLAZE_AMBIENT, SoundEvents.BLAZE_HURT, SoundEvents.BLAZE_DEATH, null),
    BEAST(SoundEvents.RAVAGER_AMBIENT, SoundEvents.RAVAGER_HURT, SoundEvents.RAVAGER_DEATH, SoundEvents.RAVAGER_STEP),
    BIRD(SoundEvents.PARROT_AMBIENT, SoundEvents.PARROT_HURT, SoundEvents.PARROT_DEATH, SoundEvents.PARROT_STEP),
    INSECT(SoundEvents.SILVERFISH_AMBIENT, SoundEvents.SILVERFISH_HURT, SoundEvents.SILVERFISH_DEATH, SoundEvents.SILVERFISH_STEP),
    MAGIC(SoundEvents.EVOKER_AMBIENT, SoundEvents.EVOKER_HURT, SoundEvents.EVOKER_DEATH, null),
    ENDER(SoundEvents.ENDERMAN_AMBIENT, SoundEvents.ENDERMAN_HURT, SoundEvents.ENDERMAN_DEATH, null),
    AQUATIC(SoundEvents.GUARDIAN_AMBIENT_LAND, SoundEvents.GUARDIAN_HURT_LAND, SoundEvents.GUARDIAN_DEATH_LAND, null),
    BRUTE(SoundEvents.PIGLIN_BRUTE_AMBIENT, SoundEvents.PIGLIN_BRUTE_HURT, SoundEvents.PIGLIN_BRUTE_DEATH, SoundEvents.PIGLIN_BRUTE_STEP),
    PILLAGER(SoundEvents.PILLAGER_AMBIENT, SoundEvents.PILLAGER_HURT, SoundEvents.PILLAGER_DEATH, SoundEvents.ZOMBIE_STEP),
    VINDICATOR(SoundEvents.VINDICATOR_AMBIENT, SoundEvents.VINDICATOR_HURT, SoundEvents.VINDICATOR_DEATH, SoundEvents.ZOMBIE_STEP),
    WITCH(SoundEvents.WITCH_AMBIENT, SoundEvents.WITCH_HURT, SoundEvents.WITCH_DEATH, null),
    SHULKER(SoundEvents.SHULKER_AMBIENT, SoundEvents.SHULKER_HURT, SoundEvents.SHULKER_DEATH, null),
    WARDEN(SoundEvents.WARDEN_AMBIENT, SoundEvents.WARDEN_HURT, SoundEvents.WARDEN_DEATH, SoundEvents.WARDEN_STEP),
    DRAGON(SoundEvents.ENDER_DRAGON_GROWL, SoundEvents.ENDER_DRAGON_HURT, SoundEvents.ENDER_DRAGON_DEATH, null),
    WOLF(SoundEvents.WOLF_GROWL, SoundEvents.WOLF_HURT, SoundEvents.WOLF_DEATH, SoundEvents.WOLF_STEP),
    FROG(SoundEvents.FROG_AMBIENT, SoundEvents.FROG_HURT, SoundEvents.FROG_DEATH, SoundEvents.FROG_STEP),
    SILENT(null, SoundEvents.GENERIC_HURT, SoundEvents.GENERIC_DEATH, null);

    public final SoundEvent ambient, hurt, death, step;
    Style(SoundEvent a, SoundEvent h, SoundEvent d, SoundEvent s) { ambient = a; hurt = h; death = d; step = s; }
}
