package com.spiremod.core;

/** Global balance knobs. StS damage numbers are multiplied by DMG; StS HP is used as-is (see MobDef.hp). */
public final class Cfg {
    private Cfg() {}
    public static final float DMG = 0.5f;          // StS damage -> Minecraft hearts*2
    public static final int MOVE_COOLDOWN = 8;     // ticks between moves
    public static final float WEAK_MULT = 0.75f;
    public static final float VULN_MULT = 1.5f;
}
