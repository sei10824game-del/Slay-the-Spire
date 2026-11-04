package com.spiremod.core;

/** StS-style intent shown above the creature's head. */
public enum Intent {
    ATTACK("⚔", 0xFF5555), ATTACK_DEBUFF("⚔☠", 0xFF55AA), ATTACK_BUFF("⚔↑", 0xFF8855),
    ATTACK_DEFEND("⚔⛨", 0xFFAA55), DEFEND("⛨", 0x55AAFF), DEFEND_BUFF("⛨↑", 0x55CCFF),
    DEFEND_DEBUFF("⛨☠", 0x77AAFF), BUFF("↑", 0xFFDD55), DEBUFF("☠", 0xBB77FF), STRONG_DEBUFF("☠☠", 0xDD55FF),
    SUMMON("✦", 0x77FFAA), STUN("❄", 0xAAAAAA), SLEEP("Zzz", 0x9999CC), ESCAPE("→", 0xCCCCCC), UNKNOWN("?", 0xFFFFFF), NONE("", 0xFFFFFF);

    public final String symbol; public final int color;
    Intent(String s, int c) { symbol = s; color = c; }
}
