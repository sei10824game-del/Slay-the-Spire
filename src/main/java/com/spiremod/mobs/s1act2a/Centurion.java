package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Centurion. 76-80 HP. Slash 12, Fury 6x3, Defend (15 Block to a random ally; only with an ally alive, else it attacks).
 * 65%: when allies live, Defend 35% / Fury 35% / Slash 30%; alone Slash 65% / Fury 35%. No 3x repeats.
 */
public final class Centurion {
    private Centurion() {}
    static final int ARMOR = 0xa8402e, ARMOR_D = 0x6a2418, BRONZE = 0xc99a4a, STEEL = 0xcfd3da, SKIN = 0xc79a76;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("centurion");
        Bip b = Bip.make(m, SKIN, BRONZE, 0x8a6428, 0x6a2418, 0x3a2a22, 10, 10, 13, 10, 6);
        b.torso.box(-5.3f, 1, -3.3f, 10, 11, 6).color(ARMOR).grad(0xc05a42, ARMOR_D).bevel(0.15f).inflate(0.2f);  // breastplate
        b.torso.box(-1, 2, 2.7f, 2, 8, 1).color(BRONZE).flat();
        for (int i = 0; i < 4; i++) b.torso.box(-5.4f, 0.5f + i * 0.0f, -3.4f, 10, 1, 6).color(BRONZE).flat().inflate(0.25f);
        Part skirt = b.body.add("skirt").at(0, 2, 0);
        for (int i = -2; i <= 2; i++) skirt.box(i * 2 - 1, -7, 2.8f, 2, 7, 1).color(i % 2 == 0 ? ARMOR : BRONZE).bevel(0.1f);
        b.head.box(-3.6f, -0.2f, -3.6f, 7, 5, 7).color(BRONZE).grad(0xe0b060, 0x8a6428).bevel(0.12f);          // helmet
        b.head.box(-0.5f, 5, -4, 1, 3, 8).color(ARMOR).flat();                                                 // crest
        b.head.box(-2.5f, 1.5f, 3.3f, 5, 1, 1).color(0x1a1010).glow(Face.FRONT, 1, 0, 1, 1, 0xffd060).glow(Face.FRONT, 3, 0, 1, 1, 0xffd060);
        b.armR.box(-3, -2, -2.5f, 5, 2, 5).color(BRONZE).flat().inflate(0.2f);   // pauldrons
        b.armL.box(-2, -2, -2.5f, 5, 2, 5).color(BRONZE).flat().inflate(0.2f);
        Part sword = b.handR.add("sword").at(0, -2, 0).rot(-90, 0, 0);
        sword.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e);
        sword.box(-2, 2, -0.5f, 4, 1, 1).color(BRONZE).flat();
        sword.box(-1, 3, -0.4f, 2, 10, 1).color(STEEL).flat().noise(0.03f);
        Part shield = b.handL.add("shield").at(-1.5f, -1, 1).rot(0, -90, 0);
        shield.box(-4.5f, -5, 0, 9, 10, 2).color(ARMOR).grad(0xc05a42, ARMOR_D).bevel(0.12f);
        shield.box(-1.5f, -1.5f, 2, 3, 3, 1).color(BRONZE).flat().inflate(0.15f);
        shield.box(-4.7f, -5.2f, -0.2f, 9, 1, 2).color(BRONZE).flat();
        m.clip("slash", 28)
            .rot("torso", 0, 0, 0, 0).rot("torso", 9, -6, -35, 0).rot("torso", 13, 14, 35, 0, Ease.IN_CUBIC).rot("torso", 28, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -160, 0, 15).rot("armR", 13, -50, 0, -10, Ease.IN_CUBIC).rot("armR", 20, -45, 0, -10).rot("armR", 28, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, -40, 0, -20).rot("armL", 13, -20, 0, -5).rot("armL", 28, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, -2).pos("body", 13, 0, -1, 4, Ease.IN_CUBIC).pos("body", 28, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 9, 5, -20, 0).rot("head", 13, 5, 20, 0).rot("head", 28, 0, 0, 0);
        Clip f = m.clip("fury", 50)
            .rot("armL", 0, 0, 0, 0).rot("armL", 6, -50, 0, -15).rot("armL", 44, -50, 0, -15).rot("armL", 50, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, 0, -2).pos("body", 50, 0, 0, 0);
        for (int i = 0; i < 3; i++) {
            int t = 8 + i * 10; boolean up = i != 1;
            f.rot("armR", t, up ? -165 : -100, 0, up ? 25 : 70).rot("armR", t + 5, -45, 0, up ? -10 : -60, Ease.IN_CUBIC)
             .rot("torso", t, -8, up ? -25 : 20, 0).rot("torso", t + 5, 12, up ? 25 : -20, 0, Ease.IN_CUBIC);
        }
        f.rot("armR", 50, 0, 0, 0).rot("torso", 0, 0, 0, 0).rot("torso", 50, 0, 0, 0);
        m.clip("defend", 32)   // raise shield, bellow, stomp
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -100, 0, -25, Ease.OUT_BACK).rot("armL", 26, -95, 0, -25).rot("armL", 32, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -150, 0, 20).rot("armR", 26, -140, 0, 20).rot("armR", 32, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -10, 0, 0).rot("torso", 14, 8, 0, 0, Ease.IN_CUBIC).rot("torso", 32, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -18, 0, 0).rot("head", 32, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 8, -35, 0, 0).rot("legR", 14, 0, 0, 0, Ease.IN_CUBIC);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("centurion", "Centurion", "センチュリオン")
            .game(1, 2).size(0.7f, 1.95f).hp(78).speed(0.24f).style(Style.METAL).egg(0xa8402e, 0xc99a4a)
            .model(Centurion::model).spawn(5, 1, 1, MobDef.Habitat.PLAINS)
            .pattern(s -> {
                boolean ally = !Mystic.allies(s).isEmpty();
                double r = s.rand().nextDouble();
                String p = ally ? (r < 0.35 ? "defend" : r < 0.7 ? "fury" : "slash") : (r < 0.65 ? "slash" : "fury");
                if (s.repeatCount(p) >= 2) p = p.equals("slash") ? "fury" : (ally ? "slash" : "slash");
                return p;
            })
            .move(Move.of("slash", 28).intent(Intent.ATTACK, 12, 1).reach(2.6f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.9f).at(13, Fx.melee(12, 3.0, 100)))
            .move(Move.of("fury", 50).intent(Intent.ATTACK, 6, 3).reach(2.6f)
                .atAll(new int[]{13, 23, 33}, Fx.melee(6, 3.0, 100)))
            .move(Move.of("defend", 32).intent(Intent.DEFEND).noTarget()
                .sound(14, SoundEvents.ANVIL_PLACE, 0.8f, 1.2f)
                .at(14, (s, t) -> {
                    var al = Mystic.allies(s);
                    if (al.isEmpty()) { s.addBlock(15); return; }
                    SpireMob a = al.get(s.rand().nextInt(al.size()));
                    a.addBlock(15); Fx.particles(a, ParticleTypes.ENCHANTED_HIT, 15, 0.5);
                }));
    }
}
