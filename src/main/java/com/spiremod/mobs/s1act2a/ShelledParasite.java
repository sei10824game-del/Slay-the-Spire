package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Shelled Parasite. 68-72 HP, Plated Armor 14 (lost 1 per hit taken).
 * Double Strike 6x2, Suck 10 (heals the damage dealt), Fell 18 + Frail 2. Opens with Fell 50% (else Double Strike/Suck); no Fell twice, no 3x repeats.
 */
public final class ShelledParasite {
    private ShelledParasite() {}
    static final int SHELL = 0x4c7a6a, SHELL_D = 0x2b4a40, SKIN = 0xc9a07a, PLATE = 0x8aa7a0, MAW = 0x7a2330;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("shelled_parasite");
        Part body = m.add("body").at(0, 7, 0);
        body.cbox(-3, 12, 6, 14).color(SKIN).grad(0xdcb48c, 0x9a7656).noise(0.1f);
        Part shell = body.add("shell").at(0, 6, -1);
        shell.cbox(-1, 14, 8, 16).color(SHELL).grad(0x5f9482, SHELL_D).noise(0.12f).bevel(0.2f);
        shell.cbox(6.5f, 10, 3, 12).color(SHELL_D).grad(SHELL, SHELL_D);
        for (int i = 0; i < 3; i++) shell.box(-1, 7.5f, -7 + i * 5, 2, 2, 2).color(PLATE).flat();  // ridge spikes
        shell.box(-8, 2, -4, 1, 4, 8).color(PLATE).bevel(0.1f);
        shell.box(7, 2, -4, 1, 4, 8).color(PLATE).bevel(0.1f);
        Part head = body.add("head").at(0, 4, 7);
        head.box(-3, -3, 0, 6, 6, 5).color(SKIN).grad(0xdcb48c, 0x9a7656).bevel(0.1f)
            .glow(Face.FRONT, 0, 1, 1, 1, 0xffe060).glow(Face.FRONT, 5, 1, 1, 1, 0xffe060);
        Part jawU = head.add("jawU").at(0, -1, 5);
        jawU.box(-2, -1, 0, 4, 2, 2).color(MAW);
        Part jawL = head.add("jawL").at(0, -2, 5);
        jawL.box(-2, -1, 0, 4, 1, 2).color(MAW).glow(Face.TOP, 0, 0, 4, 1, 0xff6070);
        for (int i = 0; i < 2; i++) {
            Part t = head.add(i == 0 ? "feelerR" : "feelerL").at(i == 0 ? -2 : 2, 3, 2).rot(-30, 0, i == 0 ? -20 : 20);
            t.box(-0.5f, 0, -0.5f, 1, 4, 1).color(SKIN);
        }
        String[] n = {"legFR", "legFL", "legBR", "legBL"};
        for (int i = 0; i < 4; i++) {
            float x = (i % 2 == 0 ? -1 : 1) * 5.5f, z = i < 2 ? 4 : -4;
            Part l = m.add(n[i]).at(x, 7, z);
            l.box(-1, -7, -1, 2, 7, 2).color(SKIN).grad(0xdcb48c, 0x8a6a4a);
            l.box(-1.5f, -7, -1.5f, 3, 1, 4).color(0x6a4e36);
        }
        m.look("head", 40, 25);
        m.walkQuad("legFR", "legFL", "legBR", "legBL", 28f);
        m.proc((rig, c) -> rig.show("shell", true));
        m.clip("idle", 40).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, 0.8f, 0).pos("body", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 5, 12, 0).rot("head", 30, -5, -12, 0).rot("head", 40, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 20, 15, 0, 0).rot("jawL", 40, 0, 0, 0)
            .rot("feelerR", 0, -30, 0, -20).rot("feelerR", 20, -45, 0, -10).rot("feelerR", 40, -30, 0, -20)
            .rot("feelerL", 0, -30, 0, 20).rot("feelerL", 20, -45, 0, 10).rot("feelerL", 40, -30, 0, 20);
        // DOUBLE STRIKE: rear back on hind legs, two snapping jabs
        m.clip("double_strike", 32)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -20, 0, 0).rot("body", 12, 15, 0, 0, Ease.IN_CUBIC).rot("body", 16, -12, 0, 0).rot("body", 20, 20, 0, 0, Ease.IN_CUBIC).rot("body", 32, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1, -2).pos("body", 12, 0, -1, 4, Ease.IN_CUBIC).pos("body", 16, 0, 0, 0).pos("body", 20, 0, -1, 5, Ease.IN_CUBIC).pos("body", 32, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -25, 0, 0).rot("head", 12, 30, 0, 0, Ease.IN_CUBIC).rot("head", 16, -20, 0, 0).rot("head", 20, 35, 0, 0, Ease.IN_CUBIC).rot("head", 32, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 9, 40, 0, 0).rot("jawL", 12, 0, 0, 0).rot("jawL", 17, 40, 0, 0).rot("jawL", 20, 0, 0, 0)
            .rot("jawU", 0, 0, 0, 0).rot("jawU", 9, -25, 0, 0).rot("jawU", 12, 0, 0, 0).rot("jawU", 17, -25, 0, 0).rot("jawU", 20, 0, 0, 0);
        // SUCK: lunge and clamp on, pulsing gulps
        Clip suck = m.clip("suck", 40)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -3).pos("body", 13, 0, 0, 6, Ease.IN_CUBIC).pos("body", 32, 0, 0, 5).pos("body", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -20, 0, 0).rot("head", 13, 25, 0, 0, Ease.IN_CUBIC).rot("head", 32, 20, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 10, 45, 0, 0).rot("jawL", 13, 0, 0, 0);
        for (int i = 0; i < 4; i++) suck.scl("body", 16 + i * 4, 1.12f, 1.12f, 1.12f).scl("body", 18 + i * 4, 1, 1, 1);
        suck.scl("body", 40, 1, 1, 1);
        // FELL: hoist body up, shell flung wide, crash down with a heavy bite
        m.clip("fell", 36)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 6, -2, Ease.OUT_CUBIC).pos("body", 16, 0, -1, 6, Ease.IN_CUBIC).pos("body", 36, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -35, 0, 0).rot("body", 16, 40, 0, 0, Ease.IN_CUBIC).rot("body", 36, 0, 0, 0)
            .rot("shell", 0, 0, 0, 0).rot("shell", 12, 20, 0, 0).rot("shell", 16, -10, 0, 0).rot("shell", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -40, 0, 0).rot("head", 16, 45, 0, 0, Ease.IN_CUBIC).rot("head", 36, 0, 0, 0)
            .rot("jawL", 0, 0, 0, 0).rot("jawL", 12, 50, 0, 0).rot("jawL", 16, 0, 0, 0, Ease.IN_CUBIC)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 12, 40, 0, 0).rot("legFR", 18, 0, 0, 0)
            .rot("legFL", 0, 0, 0, 0).rot("legFL", 12, 40, 0, 0).rot("legFL", 18, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -15, 0, 8).rot("body", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 0).rot("head", 10, 0, 0, 0);
        m.clip("death", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -2, 0, Ease.IN_QUAD).pos("body", 30, 0, -4, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 20, 0, 0, 160, Ease.IN_QUAD).rot("body", 30, 0, 0, 170)
            .rot("head", 0, 0, 0, 0).rot("head", 20, 20, 0, 0)
            .rot("legFR", 0, 0, 0, 0).rot("legFR", 24, 0, 0, 40).rot("legFL", 0, 0, 0, 0).rot("legFL", 24, 0, 0, -40)
            .rot("legBR", 0, 0, 0, 0).rot("legBR", 24, 0, 0, 50).rot("legBL", 0, 0, 0, 0).rot("legBL", 24, 0, 0, -50);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("shelled_parasite", "Shelled Parasite", "シェルドパラサイト")
            .game(1, 2).size(1.0f, 0.9f).hp(70).speed(0.24f).style(Style.INSECT).egg(0x4c7a6a, 0xc9a07a)
            .model(ShelledParasite::model).keepBlock().spawn(6, 1, 2, MobDef.Habitat.JUNGLE)
            .onSpawn(s -> s.addBlock(14))
            // Plated Armor: each hit chips one plate
            .onHurt((s, src, dmg) -> { if (src.getEntity() != null && s.invulnerableTime <= 10) s.addBlock(-1); return dmg; })
            .pattern(s -> {
                String last = s.lastMove();
                double r = s.rand().nextDouble();
                String p;
                if (s.getTurn() == 0) p = r < 0.5 ? "fell" : (r < 0.75 ? "double_strike" : "suck");
                else if (last.equals("fell")) p = r < 0.5 ? "double_strike" : "suck";
                else p = r < 0.4 ? "fell" : (r < 0.7 ? "double_strike" : "suck");
                if (p.equals(last) && s.repeatCount(p) >= 2) p = last.equals("suck") ? "double_strike" : "suck";
                return p;
            })
            .move(Move.of("double_strike", 32).intent(Intent.ATTACK, 6, 2).reach(2.2f)
                .sound(8, SoundEvents.SPIDER_AMBIENT, 0.8f, 1.5f)
                .at(12, Fx.melee(6, 2.6, 80)).at(20, Fx.melee(6, 2.6, 80)))
            .move(Move.of("suck", 40).intent(Intent.ATTACK_BUFF, 10, 1).reach(2.2f)
                .at(13, (s, t) -> { if (Fx.meleeNow(s, 10, 2.6, 80, null) > 0) { s.heal(5); Fx.particles(s, ParticleTypes.HEART, 5, 0.4); } }))
            .move(Move.of("fell", 36).intent(Intent.ATTACK_DEBUFF, 18, 1).reach(2.4f)
                .sound(12, SoundEvents.RAVAGER_ROAR, 0.7f, 1.4f)
                .at(16, Fx.melee(18, 2.8, 90, v -> Fx.frail(v, 5))));
    }
}
