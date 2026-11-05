package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Globe Head. 148 HP, Galvanic 6. Cycle: Shocking Slap (13 + 2 Frail) -> Thunder Strike (6 x3) -> Galvanic Burst (16, +2 Str).
 *  ponytail: Galvanic (Galvanized afflicted powers) has no player-card analogue; skipped. */
public final class GlobeHead {
    private GlobeHead() {}
    static final int SUIT = 0x3a4a6a, SUIT_D = 0x232c44, BRASS = 0xc89a40, GLASS = 0x9fe0f6, BOLT = 0xfff6a0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_globe_head");
        Kit.biped(m, 0x6a7a8a, SUIT, SUIT_D, BRASS);
        m.scale(1.3f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head");
        torso.box(-4.5f, 0, -3, 9, 9, 6).color(SUIT).grad(0x4a5e86, SUIT_D).bevel(0.15f);
        torso.box(-2, 2, -3.3f, 4, 5, 1).color(BRASS).flat().glow(Face.FRONT, 1, 1, 2, 3, 0x70d8ff);        // coil plate
        for (int s = -1; s <= 1; s += 2) torso.add("pad" + s).at(s * 5.5f, 8.5f, 0).box(-2.5f, -1, -2.5f, 5, 3, 5).color(BRASS).bevel(0.2f);
        head.box(-2, -2, -2, 4, 3, 4).color(BRASS).bevel(0.2f);                                              // collar
        Part globe = head.add("globe").at(0, 5, 0);
        globe.box(-6, -5, -6, 12, 12, 12).color(GLASS).grad(0xcff4ff, 0x68b8d8).bevel(0.35f).noise(0.03f).flat();
        globe.box(-0.5f, -4, -0.5f, 1, 9, 1).color(BOLT).flat().glow(Face.FRONT, 0, 0, 1, 9, BOLT);         // central arc
        globe.box(-3, 0, -0.5f, 6, 1, 1).color(BOLT).flat().glow(Face.FRONT, 0, 0, 6, 1, BOLT);
        globe.box(-2.5f, 3, -0.5f, 5, 1, 1).color(0x70d8ff).flat().glow(Face.FRONT, 0, 0, 5, 1, 0x70d8ff);
        globe.box(-2, 2, 5.4f, 1, 2, 1).color(0x2a3a5a).flat().glow(Face.FRONT, 0, 0, 1, 2, 0xffffff);      // eyes
        globe.box(1, 2, 5.4f, 1, 2, 1).color(0x2a3a5a).flat().glow(Face.FRONT, 0, 0, 1, 2, 0xffffff);
        for (int s = -1; s <= 1; s += 2) {
            Part hand = m.byName.get(s < 0 ? "handR" : "handL");
            hand.box(-1.5f, -3.5f, -1.5f, 3, 3, 3).color(BRASS).bevel(0.2f).glow(Face.BOTTOM, 0, 0, 3, 3, 0x70d8ff);
        }
        Kit.bipedBase(m);
        m.proc((r, c) -> r.scl("globe", 1f + 0.03f * (float) Math.sin(c.age * 0.5f), 1f + 0.03f * (float) Math.sin(c.age * 0.5f), 1f));
        Kit.sweep(m, "shocking_slap", 28, 14);
        Kit.jabs(m, "thunder_strike", 36, 12, 19, 26);
        Clip c = Kit.overhead(m, "galvanic_burst", 34, 17);
        c.scl("globe", 0, 1, 1, 1).scl("globe", 13, 1.2f, 1.2f, 1.2f).scl("globe", 17, 1.35f, 1.35f, 1.35f).scl("globe", 34, 1, 1, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_globe_head", "Globe Head", "グローブヘッド")
            .game(2, 3).size(0.95f, 2.3f).hp(148).speed(0.24f).style(Style.MAGIC).egg(0x3a4a6a, 0x9fe0f6)
            .model(GlobeHead::model)
            .spawn(5, 1, 1, MobDef.Habitat.PLAINS)
            .onClientTick(s -> { if (s.tickCount % 6 == 0) s.level().addParticle(ParticleTypes.ELECTRIC_SPARK, s.getX(), s.getY() + 2.0, s.getZ(), 0, 0, 0); })
            .pattern(Pattern.cycle("shocking_slap", "thunder_strike", "galvanic_burst"))
            .move(Move.of("shocking_slap", 28).intent(Intent.ATTACK_DEBUFF, 13, 1).reach(2.8f)
                .sound(12, SoundEvents.LIGHTNING_BOLT_IMPACT, 0.4f, 1.8f)
                .at(14, Fx.melee(13, 3.0, 100, v -> Fx.frail(v, 2 * Kit.TURN))))
            .move(Move.of("thunder_strike", 36).intent(Intent.ATTACK, 6, 3).reach(2.8f)
                .at(12, Kit.hit(6)).at(19, Kit.hit(6)).at(26, Kit.hit(6))
                .sound(11, SoundEvents.LIGHTNING_BOLT_IMPACT, 0.3f, 2f).sound(18, SoundEvents.LIGHTNING_BOLT_IMPACT, 0.3f, 2f).sound(25, SoundEvents.LIGHTNING_BOLT_IMPACT, 0.3f, 2f))
            .move(Move.of("galvanic_burst", 34).intent(Intent.ATTACK_BUFF, 16, 1).reach(3.0f)
                .sound(15, SoundEvents.LIGHTNING_BOLT_THUNDER, 0.6f, 1.5f)
                .at(17, (s, t) -> { Fx.meleeNow(s, 16, 3.3, 360, null); Kit.str(s, 2); Fx.particles(s, ParticleTypes.ELECTRIC_SPARK, 25, 0.8); }));
    }
}
