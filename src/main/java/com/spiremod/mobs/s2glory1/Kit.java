package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/** Shared helpers for the Glory-1 group: biped builder, clip templates, multi-hit helpers. */
final class Kit {
    private Kit() {}
    static final int TURN = 3; // seconds per StS turn for debuff durations

    // ---------------------------------------------------------------- gameplay helpers
    static Move.Action hit(float sts, double range, double arc) {
        return (s, t) -> { if (t != null) t.invulnerableTime = 0; Fx.meleeNow(s, sts, range, arc, null); };
    }
    static Move.Action hit(float sts) { return hit(sts, 3.0, 110); }
    static Move.Action zap(float sts) {
        return (s, t) -> { if (t != null) { t.invulnerableTime = 0; Fx.dmg(s, t, sts); } };
    }
    static void str(SpireMob s, int n) { s.addStrength(n); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.5); }
    static void block(SpireMob s, int n) { Fx.block(n).run(s, null); }
    static void effect(LivingEntity v, MobEffect e, int sec, int amp) { if (v != null) v.addEffect(new MobEffectInstance(e, sec * 20, amp)); }
    /** StS "Paper Cuts": lose max HP (transient modifier; ponytail: resets on relog) */
    static void paperCut(LivingEntity v, double hp) {
        var a = v.getAttribute(Attributes.MAX_HEALTH);
        if (a != null && a.getValue() > 6) a.addTransientModifier(new AttributeModifier(UUID.randomUUID(), "paper_cut", -hp, AttributeModifier.Operation.ADDITION));
    }

    // ---------------------------------------------------------------- clip helpers
    /** rotation frames {t,rx,ry,rz,...}; rest keys are added at 0 and len; the frame at tick `snap` uses IN_CUBIC */
    static void seq(Clip c, String p, int len, int snap, float... f) {
        if (f[0] != 0) c.rot(p, 0, 0, 0, 0);
        for (int i = 0; i < f.length; i += 4) c.rot(p, f[i], f[i + 1], f[i + 2], f[i + 3], f[i] == snap ? Ease.IN_CUBIC : Ease.SMOOTH);
        if (f[f.length - 4] != len) c.rot(p, len, 0, 0, 0);
    }
    /** position frames {t,x,y,z,...} */
    static void sp(Clip c, String p, int len, int snap, float... f) {
        if (f[0] != 0) c.pos(p, 0, 0, 0, 0);
        for (int i = 0; i < f.length; i += 4) c.pos(p, f[i], f[i + 1], f[i + 2], f[i + 3], f[i] == snap ? Ease.IN_CUBIC : Ease.SMOOTH);
        if (f[f.length - 4] != len) c.pos(p, len, 0, 0, 0);
    }

    // ---------------------------------------------------------------- biped builder
    /** parts: legR legL body torso head armR armL handR handL ; 28px tall at scale 1 */
    static void biped(ModelSpec m, int skin, int main, int dark, int trim) {
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 10, 0);
            leg.box(-1.5f, -10, -1.5f, 3, 10, 3).color(dark).grad(main, dark).bevel(0.1f);
            leg.box(-2, -10, -2.5f, 4, 2, 5).color(trim).bevel(0.15f);
        }
        Part body = m.add("body").at(0, 10, 0);
        body.box(-4, 0, -2.5f, 8, 3, 5).color(dark).bevel(0.1f);
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-4, 0, -2.5f, 8, 9, 5).color(main).grad(main, dark).bevel(0.1f);
        torso.box(-4.2f, 0, -2.7f, 8, 2, 5).color(trim).flat().inflate(0.25f);
        Part head = torso.add("head").at(0, 9, 0);
        head.box(-3, 0, -3, 6, 6, 6).color(skin).bevel(0.1f);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 5.5f, 8, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(main).bevel(0.1f);
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1, -3, -1, 2, 3, 2).color(skin);
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 38f, 25f);
    }

    /** idle (breathing), hurt, death for a biped built by biped() */
    static void bipedBase(ModelSpec m) {
        m.clip("idle", 48).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 24, -1.5f, 0, 0).rot("torso", 48, 1.5f, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 0, 8, 2).rot("head", 36, 0, -8, -2).rot("head", 48, 0, 0, 0)
            .rot("armR", 0, 3, 0, 5).rot("armR", 24, 7, 0, 7).rot("armR", 48, 3, 0, 5)
            .rot("armL", 0, 3, 0, -5).rot("armL", 24, 7, 0, -7).rot("armL", 48, 3, 0, -5);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 18, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 10).rot("head", 10, 0, 0, 0);
        m.clip("death", 30)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 25, 0, 0).rot("torso", 30, 8, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -4, 0, Ease.IN_QUAD).pos("body", 30, 0, -8, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -120, 0, 40).rot("armR", 30, 20, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -40).rot("armL", 30, 10, 0, -70)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 30, 30, 15, 10)
            .rot("legR", 0, 0, 0, 0).rot("legR", 18, -70, 0, 10).rot("legR", 30, -80, 0, 10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 18, -60, 0, -10).rot("legL", 30, -80, 0, -10);
    }

    // ---------------------------------------------------------------- biped attack templates
    /** two-handed overhead slam; the blow lands at tick `hit` */
    static Clip overhead(ModelSpec m, String name, int len, int hit) {
        Clip c = m.clip(name, len);
        seq(c, "torso", len, hit, hit - 9, -20, 0, 0, hit, 30, 0, 0, hit + 8, 20, 0, 0);
        seq(c, "armR", len, hit, hit - 9, -170, 0, -8, hit, -40, 0, 0, hit + 8, -30, 0, 0);
        seq(c, "armL", len, hit, hit - 9, -170, 0, 8, hit, -40, 0, 0, hit + 8, -30, 0, 0);
        seq(c, "head", len, hit, hit - 9, -18, 0, 0, hit, 14, 0, 0);
        sp(c, "body", len, hit, hit - 9, 0, 1, -3, hit, 0, -1.5f, 5);
        seq(c, "legR", len, hit, hit - 9, 15, 0, 0, hit, -28, 0, 0);
        seq(c, "legL", len, hit, hit - 9, -12, 0, 0, hit, 20, 0, 0);
        return c;
    }
    /** one-handed horizontal sweep with the right arm */
    static Clip sweep(ModelSpec m, String name, int len, int hit) {
        Clip c = m.clip(name, len);
        seq(c, "torso", len, hit, hit - 9, -4, -45, 0, hit, 8, 50, 0, hit + 9, 0, 30, 0);
        seq(c, "armR", len, hit, hit - 9, -70, -40, -30, hit, -90, 55, 10, hit + 9, -70, 40, 5);
        seq(c, "armL", len, hit, hit - 9, -20, 0, -25, hit, 20, 0, -10);
        seq(c, "head", len, hit, hit - 9, 0, 30, 0, hit, 0, -35, 0);
        sp(c, "body", len, hit, hit - 9, 0, 0, -2, hit, 0, 0, 4);
        seq(c, "legR", len, hit, hit - 9, 12, 0, 0, hit, -25, 0, 0);
        return c;
    }
    /** alternating jabs / punches; one per tick listed */
    static Clip jabs(ModelSpec m, String name, int len, int... hits) {
        Clip c = m.clip(name, len);
        List<Float> r = new ArrayList<>(), l = new ArrayList<>(), tw = new ArrayList<>();
        for (int i = 0; i < hits.length; i++) {
            int h = hits[i]; boolean right = i % 2 == 0; List<Float> a = right ? r : l;
            add(a, h - 4, -25, 0, 0); add(a, h, -100, right ? -8 : 8, 0); add(a, h + 3, -60, 0, 0);
            add(tw, h - 4, 0, right ? 25 : -25, 0); add(tw, h, 4, right ? -22 : 22, 0);
        }
        int hl = hits[hits.length - 1];
        if (!r.isEmpty()) seq(c, "armR", len, -1, arr(r));
        if (!l.isEmpty()) seq(c, "armL", len, -1, arr(l));
        seq(c, "torso", len, -1, arr(tw));
        sp(c, "body", len, -1, hits[0] - 4, 0, 0, -2, hits[0], 0, 0, 3, hl + 3, 0, 0, 1);
        return c;
    }
    /** roar / power-up: arms spread, head back, shudder */
    static Clip buff(ModelSpec m, String name, int len, int peak) {
        Clip c = m.clip(name, len);
        seq(c, "torso", len, -1, peak - 8, 12, 0, 0, peak, -14, 0, 0, peak + 8, -10, 0, 0);
        seq(c, "armR", len, -1, peak - 8, 20, 0, 12, peak, -130, 0, 50, peak + 8, -120, 0, 55);
        seq(c, "armL", len, -1, peak - 8, 20, 0, -12, peak, -130, 0, -50, peak + 8, -120, 0, -55);
        seq(c, "head", len, -1, peak - 8, 15, 0, 0, peak, -35, 0, 0, peak + 4, -30, 5, 0, peak + 7, -35, -5, 0, peak + 10, -30, 0, 0);
        sp(c, "body", len, -1, peak - 8, 0, -2, 0, peak, 0, 1, 0);
        return c;
    }
    /** point / cast / aim with the right arm */
    static Clip cast(ModelSpec m, String name, int len, int peak) {
        Clip c = m.clip(name, len);
        seq(c, "armR", len, peak, peak - 8, -30, 15, 10, peak, -95, 0, 0, peak + 10, -90, 0, 0);
        seq(c, "armL", len, -1, peak - 8, -20, 0, -15, peak, -40, 0, -10);
        seq(c, "torso", len, -1, peak - 8, 8, 18, 0, peak, -6, -10, 0);
        seq(c, "head", len, -1, peak - 8, -10, 10, 0, peak, 4, -5, 0);
        return c;
    }

    private static void add(List<Float> l, float a, float b, float c, float d) { l.add(a); l.add(b); l.add(c); l.add(d); }
    private static float[] arr(List<Float> l) { float[] a = new float[l.size()]; for (int i = 0; i < a.length; i++) a[i] = l.get(i); return a; }

    // ---------------------------------------------------------------- simple non-biped bodies
    /** lunge a part forward/back: used by blobs and machines */
    static Clip lunge(ModelSpec m, String name, int len, int hit, String part, float back, float fwd) {
        Clip c = m.clip(name, len);
        sp(c, part, len, hit, hit - 8, 0, 0.5f, back, hit, 0, -0.5f, fwd, hit + 6, 0, 0, fwd * 0.4f);
        seq(c, part, len, hit, hit - 8, -8, 0, 0, hit, 14, 0, 0, hit + 6, 5, 0, 0);
        return c;
    }
}
