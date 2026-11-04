package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

import java.util.List;

/**
 * StS1 Act 1 Shield Gremlin: 12-15 HP. Protect (a random ally gains 7 Block; itself only if alone) every turn;
 * once it is the only enemy left it uses Shield Bash (6) every turn. Pink gremlin hiding behind a big curved blue shield.
 */
public final class ShieldGremlin {
    private ShieldGremlin() {}
    static final int SKIN = 0xeb9ec0, DARK = 0xb8648c, HORN = 0x5a80c8, EYE = 0x2a3a8a, SHIELD = 0x6f93d8, SHIELD_D = 0x3f5aa0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("shield_gremlin");
        GremlinKit.build(m, SKIN, DARK, HORN, EYE, false);
        // big curved kite shield on the left arm, held across the body (3 angled plates = curve)
        Part sh = m.byName.get("handL").add("shield").at(-1.5f, 0, 2).rot(0, 0, -15);
        sh.box(-3, -5, 0, 6, 12, 1).color(SHIELD).grad(0x9ab8f0, SHIELD_D).bevel(0.12f).noise(0.05f)
            .paint(Face.FRONT, 2, 0, 2, 12, 0x86a8ea).glow(Face.FRONT, 2, 5, 2, 2, 0xcfe0ff);
        Part wl = sh.add("wingL").at(3, 0, 0).rot(0, -35, 0);
        wl.box(0, -6, 0, 4, 11, 1).color(SHIELD).grad(0x9ab8f0, SHIELD_D).bevel(0.12f);
        wl.box(3, -9, 0, 1, 4, 1).color(SHIELD_D);                                    // swept point
        Part wr = sh.add("wingR").at(-3, 0, 0).rot(0, 35, 0);
        wr.box(-4, -4, 0, 4, 11, 1).color(SHIELD).grad(0x9ab8f0, SHIELD_D).bevel(0.12f);
        wr.box(-4, 7, 0, 1, 3, 1).color(SHIELD_D);
        m.byName.get("armL").rot(-60, -30, -20);
        GremlinKit.stdClips(m);
        // PROTECT: hoist the shield high, it flashes, then swing it outward to "throw" the block to an ally
        m.clip("protect", 28)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -70, 0, -10, Ease.OUT_BACK).rot("armL", 16, -70, 0, -10).rot("armL", 20, -20, 50, 20, Ease.IN_CUBIC).rot("armL", 28, 0, 0, 0)
            .rot("shield", 0, 0, 0, 0).rot("shield", 8, 0, 0, 15).rot("shield", 12, 0, 0, -10).rot("shield", 16, 0, 0, 15).rot("shield", 28, 0, 0, 0)
            .scl("shield", 0, 1, 1, 1).scl("shield", 10, 1.15f, 1.15f, 1.15f).scl("shield", 16, 1, 1, 1)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -12, 0, 0).rot("torso", 20, 5, 30, 0).rot("torso", 28, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1.5f, 0, Ease.OUT_BACK).pos("body", 16, 0, 1, 0).pos("body", 22, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -140, 0, -30).rot("armR", 18, -140, 0, -30).rot("armR", 28, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -20, 0, 0).rot("head", 20, 0, 30, 0).rot("head", 28, 0, 0, 0);
        // SHIELD BASH: tuck behind the shield, charge, ram the shield forward
        m.clip("shield_bash", 24)
            .pos("body", 0, 0, 0, 0).pos("body", 7, 0, -1.5f, -2).pos("body", 11, 0, 0, 4, Ease.IN_CUBIC).pos("body", 15, 0, 0, 4).pos("body", 24, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 7, 20, -20, 0).rot("torso", 11, 25, 25, 0, Ease.IN_CUBIC).rot("torso", 24, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 7, -10, 0, 0).rot("armL", 11, -40, 30, 0, Ease.IN_CUBIC).rot("armL", 15, -40, 30, 0).rot("armL", 24, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 7, 20, 0, 0).rot("head", 24, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 11, 35, 0, 0).rot("legR", 24, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 11, -45, 0, 0).rot("legL", 24, 0, 0, 0);
        return m;
    }

    static List<SpireMob> allies(SpireMob s) {
        List<SpireMob> l = s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(10), e -> e != s && e.isAlive());
        return l;
    }

    public static MobDef def() {
        return MobDef.of("shield_gremlin", "Shield Gremlin", "シールドグレムリン")
            .game(1, 1).size(0.7f, 1.1f).hp(13).speed(0.25f).style(Style.PILLAGER).egg(0xeb9ec0, 0x6f93d8)
            .model(ShieldGremlin::model).spawn(3, 1, 3, MobDef.Habitat.FOREST)
            .pattern(s -> allies(s).isEmpty() ? "shield_bash" : "protect")
            .move(Move.of("protect", 28).intent(Intent.DEFEND).noTarget()
                .at(10, (s, t) -> { Fx.sound(s, SoundEvents.ARMOR_EQUIP_IRON, 1f, 1.3f); Fx.particles(s, ParticleTypes.ENCHANT, 15, 0.4); })
                .at(20, (s, t) -> {
                    List<SpireMob> a = allies(s);
                    SpireMob who = a.isEmpty() ? s : a.get(s.rand().nextInt(a.size()));
                    who.addBlock(7);
                    Fx.sound(who, SoundEvents.ANVIL_PLACE, 0.5f, 1.4f);
                    Fx.particlesAt(s, ParticleTypes.GLOW, who.position().add(0, who.getBbHeight() * 0.6, 0), 12, 0.4, 0.02);
                }))
            .move(Move.of("shield_bash", 24).intent(Intent.ATTACK, 6, 1).reach(2.4f)
                .at(10, Fx.dash(0.5, 0.05))
                .at(11, Fx.melee(6, 2.6, 70))
                .sound(11, SoundEvents.SHIELD_BLOCK, 1f, 0.8f));
    }
}
