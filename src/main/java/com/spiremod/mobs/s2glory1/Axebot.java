package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;

/** StS2 Glory: Axebot. 70-78 HP, Stock 2 (re-summoned in its place on death, +10 max HP each time, opens with Boot Up).
 *  Cycle: Hammer Uppercut (14 + 2 Weak + 2 Frail) -> The One-Two (10 x2). */
public final class Axebot {
    private Axebot() {}
    static final int STEEL = 0x8c96a0, DARK = 0x4a525c, ORANGE = 0xd9822b, EYE = 0xff3a2a, HEAD = 0x6c7680;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_axebot");
        Kit.biped(m, HEAD, STEEL, DARK, ORANGE);
        m.scale(1.25f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.5f, 2, -3, 9, 7, 6).color(STEEL).grad(0xa6b0ba, DARK).bevel(0.2f);               // chest plate
        torso.box(-2, 3, -3.4f, 4, 4, 1).color(ORANGE).flat().glow(Face.FRONT, 1, 1, 2, 2, 0xffb040);   // furnace core
        body.box(-5, -4, -3, 10, 4, 6).color(DARK).bevel(0.2f);                                        // skirt plate
        for (int s = -1; s <= 1; s += 2) torso.add("pad" + (s < 0 ? "R" : "L")).at(s * 5.5f, 8.5f, 0).box(-2.5f, -1, -2.5f, 5, 3, 5).color(ORANGE).bevel(0.2f);
        head.box(-3.5f, 0, -3.5f, 7, 6, 7).color(HEAD).bevel(0.2f);                                    // helmet
        head.box(-3, 2, 3.4f, 6, 2, 1).color(0x1a1c20).glow(Face.FRONT, 0, 0, 6, 1, EYE);             // visor slit
        head.box(-0.5f, 6, -2, 1, 3, 4).color(ORANGE).flat();                                           // crest
        Part hand = m.byName.get("handR");
        Part axe = hand.add("axe").at(0, -2, 0).rot(-80, 0, 0);
        axe.box(-0.75f, -3, -0.75f, 2, 14, 2).color(0x5a4636);
        axe.box(-1, 9, -5, 2, 6, 10).color(0xb8c2cc).bevel(0.3f).noise(0.04f);                          // blade
        axe.box(-0.5f, 10, 5, 1, 4, 1).color(0xe8f0f8).flat();                                         // edge
        Kit.bipedBase(m);
        Kit.buff(m, "boot_up", 38, 16);
        Clip c = m.clip("hammer_uppercut", 34);
        Kit.seq(c, "armR", 34, 17, 6, 40, 20, 10, 17, -165, -10, 5, 26, -150, -10, 5);
        Kit.seq(c, "armL", 34, 17, 6, 30, 0, -10, 17, -150, 0, -10, 26, -140, 0, -10);
        Kit.seq(c, "torso", 34, 17, 6, 22, 10, 0, 17, -22, 0, 0, 26, -15, 0, 0);
        Kit.seq(c, "legR", 34, 17, 6, 20, 0, 0, 17, -30, 0, 0);
        Kit.sp(c, "body", 34, 17, 6, 0, -3, -1, 17, 0, 2, 4);
        Clip t = m.clip("the_one_two", 34); // two axe chops
        Kit.seq(t, "armR", 34, 12, 3, -165, 0, 0, 12, -40, 0, 0, 16, -50, 0, 0, 19, -165, 0, 0, 24, -40, 0, 0);
        Kit.seq(t, "armL", 34, 12, 3, -120, 0, 0, 12, -50, 0, 0, 19, -120, 0, 0, 24, -50, 0, 0);
        Kit.seq(t, "torso", 34, 12, 3, -16, 0, 0, 12, 24, 0, 0, 19, -16, 0, 0, 24, 24, 0, 0);
        Kit.sp(t, "body", 34, 12, 3, 0, 0, -2, 12, 0, -1, 4, 24, 0, -1, 5);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_axebot", "Axebot", "アックスボット")
            .game(2, 3).size(0.85f, 2.1f).hp(74).speed(0.24f).style(Style.METAL).egg(0x8c96a0, 0xd9822b)
            .model(Axebot::model)
            .spawn(6, 1, 1, MobDef.Habitat.MOUNTAIN)
            .pattern(s -> {
                int gen = s.count("gen"), t = s.getTurn();
                if (gen > 0 && t == 0) return "boot_up";
                return (gen > 0 ? t - 1 : t) % 2 == 0 ? "hammer_uppercut" : "the_one_two";
            })
            .move(Move.of("boot_up", 38).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(14, SoundEvents.IRON_GOLEM_REPAIR, 1f, 0.7f)
                .at(16, (s, t) -> { Kit.block(s, s.count("gen") > 1 ? 15 : 10); Kit.str(s, s.count("gen") > 1 ? 6 : 3); }))
            .move(Move.of("hammer_uppercut", 34).intent(Intent.ATTACK_DEBUFF, 14, 1).reach(3.0f)
                .sound(14, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.7f)
                .at(17, Fx.melee(14, 3.2, 90, v -> { Fx.weak(v, 2 * Kit.TURN); Fx.frail(v, 2 * Kit.TURN); })))
            .move(Move.of("the_one_two", 34).intent(Intent.ATTACK, 10, 2).reach(3.0f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.7f)
                .at(12, Kit.hit(10, 3.2, 100)).at(24, Kit.hit(10, 3.2, 100)))
            .onDeath((s, src) -> {
                int gen = s.count("gen");
                if (gen >= 2) return;
                SpireMob n = Fx.summon(s, "s2_axebot", 0, 0);
                if (n == null) return;
                n.setCount("gen", gen + 1);
                var a = n.getAttribute(Attributes.MAX_HEALTH);
                a.setBaseValue(a.getBaseValue() + 10 * (gen + 1));
                n.setHealth(n.getMaxHealth());
                Fx.particles(n, ParticleTypes.LARGE_SMOKE, 20, 0.5);
            });
    }
}
