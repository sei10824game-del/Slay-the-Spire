package com.spiremod.mobs.s2og2;

import com.spiremod.core.SpireMob;
import com.spiremod.spec.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/** Small shared model / behaviour helpers for this package. */
final class Bits {
    private Bits() {}

    /** Segmented chain (worm, tail, tendril). Segment parts are named prefix0..prefix(n-1); boxes extend along +y (dir=1) or -y (dir=-1).
     *  Each child pivot sits at the end of its parent, rotated by (bx,by,bz) to curve the chain. Returns the last segment. */
    static Part chain(Part parent, String prefix, float x, float y, float z, float rx, float ry, float rz,
                      int n, int len, int w, int color, int band, float bx, float by, float bz, int dir) {
        Part p = parent.add(prefix + 0).at(x, y, z).rot(rx, ry, rz);
        for (int i = 0; i < n; i++) {
            int ww = Math.max(1, w - (i * w) / (n + 2));
            Box b = p.box(-ww / 2f, dir > 0 ? 0 : -len, -ww / 2f, ww, len, ww).color(color).bevel(0.15f).noise(0.08f);
            if (band >= 0) for (int k = 1; k < len; k += 2) b.band(k, 1, band);
            if (i < n - 1) p = p.add(prefix + (i + 1)).at(0, dir * len, 0).rot(bx, by, bz);
        }
        return p;
    }

    /** procedural wiggle for a chain made by chain() */
    static void wiggle(Rig r, String prefix, int n, float t, float amp, float phase) {
        for (int i = 0; i < n; i++) {
            if (!r.has(prefix + i)) return;
            r.rot(prefix + i, (float) Math.sin(t + phase + i * 0.9f) * amp, 0, (float) Math.cos(t * 0.8f + phase + i * 1.1f) * amp * 0.7f);
        }
    }

    /** speech line shown above the hotbar of players nearby (StS dialogue bubbles) */
    static void say(SpireMob s, String text) {
        for (Player p : s.level().getEntitiesOfClass(Player.class, s.getBoundingBox().inflate(24)))
            p.displayClientMessage(Component.literal("§o" + text), true);
    }
}
