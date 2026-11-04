package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

/** StS1 Act 1 Mad Gremlin: 20-24 HP, Angry 1 (gains 1 Strength whenever it takes attack damage). Scratch (4) every turn. */
public final class MadGremlin {
    private MadGremlin() {}
    static final int SKIN = 0xc8323c, DARK = 0x82192a, HORN = 0x86e0d6, EYE = 0xbff8ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("mad_gremlin");
        GremlinKit.build(m, SKIN, DARK, HORN, EYE, false);
        // tall curly antenna-horns
        for (int s = -1; s <= 1; s += 2) {
            Part a = m.byName.get("head").add(s < 0 ? "antR" : "antL").at(s * 1.5f, 5, -0.5f).rot(-10, 0, s * -15);
            a.box(-0.5f, 0, -0.5f, 1, 5, 1).color(HORN).grad(0xc8fff8, HORN);
            a.box(s < 0 ? -2.5f : 0.5f, 4, -0.5f, 2, 1, 1).color(HORN);
            a.box(s < 0 ? -2.5f : 1.5f, 2.5f, -0.5f, 1, 2, 1).color(HORN);
        }
        // cyan swirl claws: big curled hooks on each hand
        for (int s = -1; s <= 1; s += 2) {
            Part c = m.byName.get(s < 0 ? "handR" : "handL").add(s < 0 ? "clawR" : "clawL").at(0, -2, 0.5f);
            c.box(-1.5f, -2, -0.5f, 3, 1, 3).color(HORN).bevel(0.15f).glow(Face.TOP, 0, 0, 1, 1, 0xd8ffff);
            c.box(-1.5f, -2, 2.5f, 3, 3, 1).color(HORN).grad(0xc8fff8, HORN);
            c.box(-0.5f, 0, 1.5f, 1, 1, 1).color(0xd8ffff).flat();
        }
        m.byName.get("armR").rot(-35, 0, -25);
        m.byName.get("armL").rot(-35, 0, 25);
        m.byName.get("body").rot(8, 0, 0);
        GremlinKit.stdClips(m);
        // SCRATCH: crouch low with claws cocked, spring forward, rake right then left, skid back
        m.clip("scratch", 26)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, -1.8f, -1).pos("body", 10, 0, 1.5f, 3, Ease.OUT_QUAD).pos("body", 14, 0, 0, 3).pos("body", 26, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 6, 15, 30, 0).rot("torso", 10, 25, -35, 0, Ease.IN_CUBIC).rot("torso", 15, 25, 35, 0, Ease.IN_CUBIC).rot("torso", 26, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 6, -90, 0, -40).rot("armR", 10, -40, 0, 50, Ease.IN_CUBIC).rot("armR", 15, -30, 0, 40).rot("armR", 26, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 6, -80, 0, 30).rot("armL", 10, -90, 0, 35).rot("armL", 15, -40, 0, -55, Ease.IN_CUBIC).rot("armL", 26, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 6, -15, 0, 0).rot("head", 10, 0, 20, 0).rot("head", 15, 0, -20, 0).rot("head", 26, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 6, 30, 0, 0).rot("jaw", 15, 30, 0, 0).rot("jaw", 22, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 6, -40, 0, 0).rot("legR", 10, 30, 0, 0).rot("legR", 26, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 6, -40, 0, 0).rot("legL", 10, -50, 0, 0).rot("legL", 26, 0, 0, 0)
            .rot("antR", 0, 0, 0, 0).rot("antR", 11, 25, 0, -15).rot("antR", 16, -20, 0, 10).rot("antR", 26, 0, 0, 0)
            .rot("antL", 0, 0, 0, 0).rot("antL", 11, 25, 0, 15).rot("antL", 16, -20, 0, -10).rot("antL", 26, 0, 0, 0);
        // angry shake when hurt
        m.clip("hurt", 12)
            .rot("torso", 0, 0, 0, 0).rot("torso", 3, -18, 0, 10).rot("torso", 6, 5, 0, -10).rot("torso", 9, 0, 0, 8).rot("torso", 12, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 35, 0, 0).rot("jaw", 12, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 4, -60, 0, -30).rot("armR", 12, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 4, -60, 0, 30).rot("armL", 12, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("mad_gremlin", "Mad Gremlin", "マッドグレムリン")
            .game(1, 1).size(0.6f, 1.35f).hp(22).speed(0.3f).style(Style.PILLAGER).egg(0xc8323c, 0x86e0d6)
            .model(MadGremlin::model).spawn(4, 1, 3, MobDef.Habitat.FOREST)
            .pattern(Pattern.cycle("scratch"))
            .move(Move.of("scratch", 26).intent(Intent.ATTACK, 4, 1).reach(2.2f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 0.6f, 1.6f)
                .at(9, Fx.dash(0.45, 0.15))
                .at(11, Fx.melee(4, 2.6, 90)))
            .onHurt((s, src, dmg) -> {   // Angry 1
                if (dmg > 0 && src.getEntity() instanceof LivingEntity) { s.addStrength(1); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 3, 0.3); }
                return dmg;
            });
    }
}
