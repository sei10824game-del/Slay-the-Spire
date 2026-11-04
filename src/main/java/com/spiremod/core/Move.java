package com.spiremod.core;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * One StS "move" (action the creature takes on its turn): a timed script plus an animation clip.
 * Move.of("chomp", 30).anim("chomp").intent(Intent.ATTACK, 11, 1).reach(3).at(14, Fx.melee(11)) ...
 */
public final class Move {
    public interface Action { void run(SpireMob self, LivingEntity target); }
    public static final class Timed { public final int tick; public final Action act; Timed(int t, Action a) { tick = t; act = a; } }

    public final String id;
    public final int length;
    public String anim;              // clip name in the ModelSpec (default = id)
    public String display;           // optional text for intent
    public float reach = 2.8f;       // distance to target at which the move may START (blocks)
    public Intent intent = Intent.UNKNOWN;
    public int dmg, hits = 1;        // intent display only
    public boolean needsTarget = true;
    public boolean stationary = true;   // stop navigating while the move plays
    public boolean faceTarget = true;   // body turns toward target during the move
    public int faceUntil = -1;          // tick after which body no longer tracks (-1 = whole move)
    public boolean needsLos = false;
    public final List<Timed> actions = new ArrayList<>();
    public Action onStart;

    private Move(String id, int length) { this.id = id; this.length = length; this.anim = id; }
    public static Move of(String id, int lengthTicks) { return new Move(id, lengthTicks); }

    public Move anim(String clip) { anim = clip; return this; }
    public Move reach(float r) { reach = r; return this; }
    /** StS intent. dmg/hits are the StS numbers (shown on the intent; the actual damage is what your actions apply). */
    public Move intent(Intent i, int dmg, int hits) { intent = i; this.dmg = dmg; this.hits = hits; return this; }
    public Move intent(Intent i) { intent = i; return this; }
    public Move noTarget() { needsTarget = false; reach = 64; return this; }
    public Move ranged(float r) { reach = r; needsLos = true; return this; }
    public Move moving() { stationary = false; return this; }
    public Move faceUntil(int tick) { faceUntil = tick; return this; }
    public Move noFace() { faceTarget = false; return this; }
    public Move start(Action a) { onStart = a; return this; }
    /** run an action at tick t after the move starts (0 = immediately) */
    public Move at(int tick, Action a) { actions.add(new Timed(tick, a)); return this; }
    public Move sound(int tick, SoundEvent s, float vol, float pitch) {
        return at(tick, (m, t) -> m.level().playSound(null, m.getX(), m.getY(), m.getZ(), s, SoundSource.HOSTILE, vol, pitch));
    }
    /** convenience: run on several ticks (e.g. multi-hit) */
    public Move atEach(int[] ticks, Consumer<Integer> unused) { return this; }
    public Move atAll(int[] ticks, Action a) { for (int t : ticks) at(t, a); return this; }

    public String intentText(SpireMob m) {
        StringBuilder sb = new StringBuilder(intent.symbol);
        if (dmg > 0) { int d = dmg + m.getStrength(); sb.append(d); if (hits > 1) sb.append("×").append(hits); }
        if (display != null) sb.append(' ').append(display);
        return sb.toString();
    }
}
