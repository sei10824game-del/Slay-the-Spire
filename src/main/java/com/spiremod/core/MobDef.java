package com.spiremod.core;

import com.spiremod.spec.ModelSpec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Behaviour + metadata of one creature. Build with MobDef.of(...) in a creature class's `define()`.
 * See mobs/example/Cultist.java style (every creature = one class: static ModelSpec model() + static MobDef def()).
 */
public final class MobDef {
    public interface HurtHook { /** return the (possibly modified) damage */ float run(SpireMob self, DamageSource src, float amount); }
    public interface DeathHook { void run(SpireMob self, DamageSource src); }
    public interface TickHook { void run(SpireMob self); }

    public enum Habitat { ANY, FOREST, PLAINS, SWAMP, SNOW, DESERT, MOUNTAIN, JUNGLE, CAVE, SAVANNA, TAIGA, BEACH, NONE }

    public final String id, en, ja;
    public int game = 1, act = 1;           // StS1 / StS2, act number (4 = Act4 / endgame)
    public float w = 0.8f, h = 1.8f;
    public float hp = 30, speed = 0.25f, armor = 0, kbResist = 0, followRange = 24;
    public boolean minion;                  // dies when its leader (the summoner) dies
    public boolean flying, boss, elite, fireImmune, noPush, hoverFly;
    public Style style = Style.ZOMBIE;
    public int eggPrimary = 0x777777, eggSecondary = 0xCC3333;
    public Supplier<ModelSpec> model;
    public Pattern pattern;
    public final Map<String, Move> moves = new LinkedHashMap<>();
    public HurtHook onHurt; public DeathHook onDeath; public Consumer<SpireMob> onSpawn; public TickHook onTick, onClientTick;
    public Consumer<SpireMob> onMoveEnd;
    public String initialBase = "idle";
    // natural spawning: weight 0 = never (egg/summon only)
    public int spawnWeight = 0, groupMin = 1, groupMax = 1;
    public Habitat habitat = Habitat.ANY;
    public boolean spawnInDaylight;
    public int goldMin = 1, goldMax = 3;
    public float sizeVariance;
    public int deathTicks = -1;             // -1 = use ModelSpec.deathTicks
    public boolean canTargetHigh;           // flying/ranged mobs: allow target distance to be larger
    public boolean keepBlock;               // don't clear block at each move start
    public RegistryObject<EntityType<SpireMob>> type;

    private MobDef(String id, String en, String ja) { this.id = id; this.en = en; this.ja = ja; }

    /** id: lowercase_with_underscores; en/ja display names */
    public static MobDef of(String id, String en, String ja) { return new MobDef(id, en, ja); }

    public MobDef game(int game, int act) { this.game = game; this.act = act; return this; }
    public MobDef size(float w, float h) { this.w = w; this.h = h; return this; }
    /** StS max HP (Minecraft HP). */
    public MobDef hp(float hp) { this.hp = hp; return this; }
    public MobDef speed(float s) { speed = s; return this; }
    public MobDef armor(float a) { armor = a; return this; }
    public MobDef kbResist(float k) { kbResist = k; return this; }
    public MobDef follow(float r) { followRange = r; return this; }
    /** summoned helper: removed automatically when the creature that summoned it dies */
    public MobDef minion() { minion = true; return this; }
    public MobDef flying() { flying = true; return this; }
    public MobDef elite() { elite = true; goldMin = 4; goldMax = 9; return this; }
    public MobDef boss() { boss = true; elite = true; goldMin = 16; goldMax = 30; kbResist = Math.max(kbResist, 0.8f); return this; }
    public MobDef fireImmune() { fireImmune = true; return this; }
    public MobDef style(Style s) { style = s; return this; }
    public MobDef egg(int primary, int secondary) { eggPrimary = primary; eggSecondary = secondary; return this; }
    public MobDef model(Supplier<ModelSpec> m) { model = m; return this; }
    public MobDef pattern(Pattern p) { pattern = p; return this; }
    public MobDef move(Move m) { moves.put(m.id, m); return this; }
    public MobDef onHurt(HurtHook h) { onHurt = h; return this; }
    public MobDef onDeath(DeathHook h) { onDeath = h; return this; }
    public MobDef onSpawn(Consumer<SpireMob> h) { onSpawn = h; return this; }
    public MobDef onTick(TickHook h) { onTick = h; return this; }
    /** runs every tick on the CLIENT (particles etc.) */
    public MobDef onClientTick(TickHook h) { onClientTick = h; return this; }
    public MobDef onMoveEnd(Consumer<SpireMob> h) { onMoveEnd = h; return this; }
    public MobDef base(String clip) { initialBase = clip; return this; }
    public MobDef spawn(int weight, int min, int max, Habitat hab) { spawnWeight = weight; groupMin = min; groupMax = max; habitat = hab; return this; }
    public MobDef daylight() { spawnInDaylight = true; return this; }
    public MobDef gold(int min, int max) { goldMin = min; goldMax = max; return this; }
    public MobDef deathTicks(int t) { deathTicks = t; return this; }
    public MobDef keepBlock() { keepBlock = true; return this; }

    private int deathLen = -2;
    /** ticks the death animation plays before removal (from the "death" clip) */
    public int deathLen() { if (deathTicks >= 0) return deathTicks; if (deathLen == -2) { try { deathLen = model.get().deathTicks; } catch (Throwable t) { deathLen = 20; } } return deathLen; }
    public int tier() { return boss ? 2 : elite ? 1 : 0; }
}
