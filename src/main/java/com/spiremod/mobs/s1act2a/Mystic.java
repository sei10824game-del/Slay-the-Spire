package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Mystic (Healer). 48-56 HP. Heal: all allies +16 HP (used when an ally is below ~half HP, else not first);
 * Attack 8 + Frail 2; Buff: all allies +2 Strength. No 3x repeats.
 */
public final class Mystic {
    private Mystic() {}
    static final int ROBE = 0x7a4fa8, ROBE_D = 0x3f2766, GOLD = 0xe6c75a, SKIN = 0xa5c2a0, ORB = 0x8affb0;

    /** nearby living SpireMob allies (excluding self) */
    static List<SpireMob> allies(SpireMob s) {
        return s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(16), e -> e != s && e.isAlive());
    }

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("mystic");
        Bip b = Bip.make(m, SKIN, ROBE, ROBE_D, ROBE_D, 0x2a1a40, 8, 7, 12, 8, 6);
        Part skirt = b.body.add("skirt").at(0, 2, 0);
        skirt.box(-5, -9, -3.5f, 10, 8, 7).color(ROBE_D).grad(ROBE, ROBE_D);
        skirt.box(-5.5f, -10, -4, 11, 2, 8).color(GOLD).flat().noise(0.04f);
        b.torso.box(-1, 1, 2.1f, 2, 8, 1).color(GOLD).flat();
        b.head.box(-3.6f, 3, -3.6f, 7, 5, 7).color(ROBE).grad(0x9a6fc8, ROBE_D);                       // pointed hood
        b.head.box(-2, 8, -2, 4, 2, 4).color(ROBE_D).bevel(0.15f);
        b.head.box(-3.6f, -0.5f, -4.6f, 7, 7, 2).color(ROBE_D);
        b.head.box(-3, 1.5f, 3.2f, 6, 3, 1).color(0x1a1030).glow(Face.FRONT, 1, 0, 1, 1, ORB).glow(Face.FRONT, 4, 0, 1, 1, ORB);
        Part staff = b.handR.add("staff").at(0, -2, -1);
        staff.box(-0.5f, -6, -0.5f, 1, 16, 1).color(0x5a3a22).noise(0.08f);
        Part orb = staff.add("orb").at(0, 11, 0);
        orb.box(-2, 0, -2, 4, 4, 4).color(ORB).flat().glow(Face.FRONT, 0, 0, 4, 4, ORB).glow(Face.TOP, 0, 0, 4, 4, ORB).glow(Face.RIGHT, 0, 0, 4, 4, ORB).glow(Face.LEFT, 0, 0, 4, 4, ORB);
        orb.box(-2.5f, 4, -0.5f, 5, 1, 1).color(GOLD).flat();
        m.proc((rig, c) -> { float p = 1 + 0.12f * (float) Math.sin(c.age * 0.2f); rig.scl("orb", p, p, p); });
        m.clip("heal", 40)   // staff raised, orb flares, arms flung wide
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -130, 0, 10, Ease.OUT_BACK).rot("armR", 32, -125, 0, 10).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -60, 0, -70, Ease.OUT_BACK).rot("armL", 32, -55, 0, -70).rot("armL", 40, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 8, 0, 0).rot("torso", 16, -12, 0, 0, Ease.OUT_BACK).rot("torso", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 16, -30, 0, 0).rot("head", 34, -25, 0, 0).rot("head", 40, 0, 0, 0)
            .scl("orb", 0, 1, 1, 1).scl("orb", 16, 1.6f, 1.6f, 1.6f).scl("orb", 40, 1, 1, 1);
        m.clip("buff", 36)   // staff stamp, glow, arms to the sky
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -40, 0, 0).rot("armR", 12, -20, 0, 0, Ease.IN_CUBIC).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -170, 0, -20, Ease.OUT_BACK).rot("armL", 30, -165, 0, -22).rot("armL", 36, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 8, 0, 0).rot("torso", 12, -8, 0, 0).rot("torso", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -25, 0, 0).rot("head", 36, 0, 0, 0)
            .scl("orb", 0, 1, 1, 1).scl("orb", 12, 1.5f, 1.5f, 1.5f).scl("orb", 36, 1, 1, 1);
        m.clip("attack_debuff", 30)   // staff bonk
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -10, -20, 0).rot("torso", 13, 16, 20, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -170, 0, 15).rot("armR", 13, -60, 0, -5, Ease.IN_CUBIC).rot("armR", 22, -55, 0, -5).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, -50, 0, -25).rot("armL", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 13, 0, 0, 3, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("mystic", "Mystic", "ミスティック")
            .game(1, 2).size(0.6f, 1.7f).hp(52).speed(0.26f).style(Style.WITCH).egg(0x7a4fa8, 0x8affb0)
            .model(Mystic::model).spawn(5, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(s -> {
                boolean hurtAlly = false;
                for (SpireMob a : allies(s)) if (a.getHealth() < a.getMaxHealth() * 0.5f) hurtAlly = true;
                if (s.getHealth() < s.getMaxHealth() * 0.5f) hurtAlly = true;
                String l = s.lastMove();
                if (hurtAlly && !l.equals("heal")) return "heal";
                double r = s.rand().nextDouble();
                String p = r < 0.5 ? "attack_debuff" : "buff";
                if (p.equals(l) && s.repeatCount(p) >= 2) p = p.equals("buff") ? "attack_debuff" : "buff";
                return p;
            })
            .move(Move.of("heal", 40).intent(Intent.BUFF).noTarget()
                .sound(14, SoundEvents.EVOKER_CAST_SPELL, 1f, 1.4f)
                .at(16, (s, t) -> {
                    s.heal(16); Fx.particles(s, ParticleTypes.HEART, 8, 0.6);
                    for (SpireMob a : allies(s)) { a.heal(16); Fx.particles(a, ParticleTypes.HEART, 8, 0.6); }
                }))
            .move(Move.of("buff", 36).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.EVOKER_PREPARE_SUMMON, 1f, 1.2f)
                .at(13, (s, t) -> {
                    s.addStrength(2);
                    for (SpireMob a : allies(s)) { a.addStrength(2); Fx.particles(a, ParticleTypes.ANGRY_VILLAGER, 6, 0.4); }
                }))
            .move(Move.of("attack_debuff", 30).intent(Intent.ATTACK_DEBUFF, 8, 1).reach(2.6f)
                .at(13, Fx.melee(8, 3.0, 90, v -> Fx.frail(v, 5))));
    }
}
