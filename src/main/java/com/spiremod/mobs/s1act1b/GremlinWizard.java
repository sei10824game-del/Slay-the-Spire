package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 1 Gremlin Wizard: 21-25 HP. Charging, Charging, Ultimate Blast (25), repeat.
 * Blue-robed gremlin with a pointy hat and a staff whose orb swells as it charges (ctx.var = charges).
 */
public final class GremlinWizard {
    private GremlinWizard() {}
    static final int SKIN = 0x9ab8d8, DARK = 0x5a7aa6, HORN = 0xe8e0c8, EYE = 0xd040ff, ROBE = 0x3a3a9a, ROBE_D = 0x20205a, ORB = 0xb060ff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("gremlin_wizard");
        GremlinKit.build(m, SKIN, DARK, HORN, EYE, false);
        Part body = m.byName.get("body"), torso = m.byName.get("torso"), head = m.byName.get("head");
        Part skirt = body.add("skirt").at(0, 1, 0);
        skirt.box(-3.5f, -4, -2.5f, 7, 5, 5).color(ROBE).grad(ROBE, ROBE_D).bevel(0.12f).band(0, 1, 0xe8c040);
        torso.box(-2.3f, 0, -1.8f, 5, 4, 4).color(ROBE).grad(0x5050c0, ROBE).inflate(0.2f).bevel(0.1f);          // robe top
        torso.box(-0.5f, 0.5f, -2.1f, 1, 3, 1).color(0xe8c040).flat();
        Part hat = head.add("hat").at(0, 5, 0);
        hat.box(-4.5f, 0, -4.5f, 9, 1, 9).color(ROBE_D).bevel(0.2f);
        hat.box(-2.5f, 1, -2.5f, 5, 3, 5).color(ROBE).grad(0x5050c0, ROBE);
        Part tip = hat.add("tip").at(0, 4, 0).rot(-18, 0, 0);
        tip.box(-1.5f, 0, -1.5f, 3, 3, 3).color(ROBE).grad(ROBE, 0x4040b0);
        tip.box(-0.5f, 3, -0.5f, 1, 2, 1).color(0xe8c040).flat().glow(Face.TOP, 0, 0, 1, 1, 0xfff0a0);
        // staff in the right hand with a swelling orb
        Part staff = m.byName.get("handR").add("staff").at(0, -1, 0.5f).rot(-20, 0, 0);
        staff.box(-0.5f, -6, -0.5f, 1, 14, 1).color(0x5a3a22).noise(0.1f);
        Part orb = staff.add("orb").at(0, 9.5f, 0);
        orb.box(-1.5f, -1.5f, -1.5f, 3, 3, 3).color(ORB).flat().glow(Face.FRONT, 0, 0, 3, 3, ORB).glow(Face.TOP, 0, 0, 3, 3, 0xe0b0ff)
            .glow(Face.LEFT, 0, 0, 3, 3, ORB).glow(Face.RIGHT, 0, 0, 3, 3, ORB).glow(Face.BACK, 0, 0, 3, 3, ORB);
        for (int s = -1; s <= 1; s += 2) staff.box(s * 1.2f - 0.5f, 7.5f, -0.5f, 1, 3, 1).color(0x5a3a22);       // prongs
        GremlinKit.stdClips(m);
        m.proc((r, c) -> { float k = 1f + 0.45f * c.var + (float) Math.sin(c.age * 0.4f) * 0.08f; r.scl("orb", k, k, k); });
        m.byName.get("armR").rot(-35, 0, -10);
        // CHARGING: shake, staff pulled in, orb drinks in power, body trembling
        Clip ch = m.clip("charging", 30);
        K.r(ch, "armR", 0, 0, 0, 0, 8, -60, 0, -15, 22, -70, 0, -20, 30, 0, 0, 0);
        K.r(ch, "armL", 0, 0, 0, 0, 8, -100, 0, 25, 22, -110, 0, 30, 30, 0, 0, 0);
        K.r(ch, "head", 0, 0, 0, 0, 8, -25, 0, 0, 22, -30, 0, 0, 30, 0, 0, 0);
        K.r(ch, "torso", 0, 0, 0, 0, 8, -8, 0, 0, 12, -8, 0, 4, 14, -8, 0, -4, 16, -8, 0, 4, 18, -8, 0, -4, 22, -10, 0, 0, 30, 0, 0, 0);
        K.p(ch, "body", 0, 0, 0, 0, 10, 0, 1.2f, 0, 22, 0, 1.6f, 0, 30, 0, 0, 0);
        K.r(ch, "jaw", 0, 0, 0, 0, 8, 25, 0, 0, 22, 25, 0, 0, 30, 0, 0, 0);
        K.r(ch, "tip", 0, 0, 0, 0, 12, 12, 0, 0, 20, -14, 0, 0, 30, 0, 0, 0);
        // ULTIMATE BLAST: staff overhead, whole body arches back, then both arms slam the staff forward and the orb fires
        Clip ub = m.clip("ultimate_blast", 40);
        K.r(ub, "torso", 0, 0, 0, 0, 12, -25, 0, 0, 114, 22, 0, 0, 28, 18, 0, 0, 40, 0, 0, 0);
        K.r(ub, "head", 0, 0, 0, 0, 12, -35, 0, 0, 114, 15, 0, 0, 40, 0, 0, 0);
        K.r(ub, "armR", 0, 0, 0, 0, 12, -165, 0, -10, 114, -95, 0, -5, 28, -95, 0, -5, 40, 0, 0, 0);
        K.r(ub, "armL", 0, 0, 0, 0, 12, -150, 0, 20, 114, -95, 0, 5, 28, -95, 0, 5, 40, 0, 0, 0);
        K.r(ub, "staff", 0, 0, 0, 0, 12, -50, 0, 0, 114, 30, 0, 0, 28, 30, 0, 0, 40, 0, 0, 0);
        K.p(ub, "body", 0, 0, 0, 0, 12, 0, 2, -1, 114, 0, 0, 3, 28, 0, 0, 3, 40, 0, 0, 0);
        K.r(ub, "jaw", 0, 0, 0, 0, 12, 40, 0, 0, 30, 40, 0, 0, 40, 0, 0, 0);
        K.r(ub, "legR", 0, 0, 0, 0, 14, 25, 0, 0, 40, 0, 0, 0);
        K.r(ub, "legL", 0, 0, 0, 0, 14, -30, 0, 0, 40, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        ProjKind.orb("gremlin_wizard_blast", 0xb060ff, 6).trail(ParticleTypes.WITCH).life(40);
        return MobDef.of("gremlin_wizard", "Gremlin Wizard", "グレムリンウィザード")
            .game(1, 1).size(0.7f, 1.3f).hp(23).speed(0.22f).style(Style.WITCH).egg(0x3a3a9a, 0xb060ff)
            .model(GremlinWizard::model).spawn(3, 1, 2, MobDef.Habitat.FOREST)
            .pattern(Pattern.cycle("charging", "charging", "ultimate_blast"))
            .move(Move.of("charging", 30).intent(Intent.UNKNOWN).noTarget()
                .sound(8, SoundEvents.BEACON_AMBIENT, 0.8f, 1.6f)
                .at(16, (s, t) -> { s.setVar(s.getVar() + 1); Fx.particles(s, ParticleTypes.ENCHANT, 18, 0.5); }))
            .move(Move.of("ultimate_blast", 40).intent(Intent.ATTACK, 25, 1).ranged(14)
                .sound(14, SoundEvents.EVOKER_CAST_SPELL, 1.2f, 0.8f)
                .at(15, Fx.shoot("gremlin_wizard_blast", 25, 1.1f, 0, 1.0, 0.6, null))
                .at(16, (s, t) -> s.setVar(0)));
    }
}
