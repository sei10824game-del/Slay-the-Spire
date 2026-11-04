package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Sludge Spinner (37-39 HP). Oil Spray (8 + Weak 1) first, then random of Oil Spray / Slam (11) / Rage (6, +3 Str),
 * never the same move twice in a row.
 */
public final class SludgeSpinner {
    private SludgeSpinner() {}
    static final int BODY = 0x5a4a6a, BODY_D = 0x35293f, SHELL = 0x7d6a3a, SHELL_D = 0x4a3d20, OIL = 0x1a1420, EYE = 0xffd23a;
    static final String[] IDS = {"oil_spray", "slam", "rage"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_sludge_spinner");
        Part base = m.add("base").at(0, 0, 0);
        base.box(-5, 0, -7, 10, 4, 14).color(BODY).grad(0x7a6a8c, BODY_D).noise(0.15f).bevel(0.3f);              // foot
        base.box(-5.4f, 0, -8, 11, 1, 2).color(OIL).flat().noise(0.1f);                                          // oil puddle at the rear
        Part head = base.add("head").at(0, 4, 5);
        head.box(-3, 0, 0, 6, 6, 5).color(BODY).grad(0x8a7a9c, BODY).noise(0.12f).bevel(0.25f);
        Part jaw = head.add("jaw").at(0, 1, 5);
        jaw.box(-2, -2, -4, 4, 2, 4).color(BODY_D).bevel(0.2f);
        jaw.box(-1, -1, -0.5f, 2, 1, 1).color(OIL).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part es = head.add(s < 0 ? "eyeR" : "eyeL").at(s * 2, 6, 2).rot(-10, 0, s * -15);
            es.box(-0.5f, 0, -0.5f, 1, 4, 1).color(BODY_D);
            es.box(-1.5f, 4, -1.5f, 3, 3, 3).color(0xe8e0c8).bevel(0.2f).glow(Face.FRONT, 0, 1, 3, 2, EYE).paint(Face.FRONT, 1, 1, 1, 1, 0x101010);
        }
        // spiral sludge shell on the back (spins)
        Part shell = base.add("shell").at(0, 4, -1);
        shell.box(-6, 0, -6, 12, 10, 12).color(SHELL).grad(0xa08a4a, SHELL_D).noise(0.2f).bevel(0.5f);
        shell.box(-5, 10, -5, 10, 2, 10).color(SHELL_D).noise(0.2f).bevel(0.4f);
        shell.box(-3, 12, -3, 6, 2, 6).color(SHELL).noise(0.15f);
        for (int i = 0; i < 4; i++) shell.box(-6.4f, 1.5f + i * 2.5f, -3 + (i % 2) * 2, 1, 1, 5).color(OIL).flat();      // spiral grooves
        shell.box(-3, 2, 5.6f, 6, 3, 1).color(OIL).flat().noise(0.1f).glow(Face.FRONT, 1, 1, 1, 1, 0x7a40c0);        // oil nozzle
        Part drip = base.add("drip").at(0, 6, 6);
        drip.cbox(-1, 2, 5, 2).color(OIL).flat();
        m.proc((r, c) -> {
            float spin = "rage".equals(c.move) ? c.age * 40 : "oil_spray".equals(c.move) && c.moveT > 8 && c.moveT < 26 ? c.age * 55 : c.age * 4;
            r.rot("shell", 0, spin, 0);
            r.show("drip", "oil_spray".equals(c.move) && c.moveT > 12 && c.moveT < 24);
        });
        m.look("head", 40, 25);
        m.clip("walk", 18).loop()
            .scl("base", 0, 1, 1, 1).scl("base", 4, 1, 0.92f, 1.12f).scl("base", 9, 1, 1, 1).scl("base", 13, 1, 0.92f, 1.12f).scl("base", 18, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 9, 6, 0, 0).rot("head", 18, 0, 0, 0);
        m.clip("idle", 40).loop()
            .scl("base", 0, 1, 1, 1).scl("base", 20, 1.04f, 0.96f, 1.04f).scl("base", 40, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 20, -5, 8, 0).rot("head", 40, 0, 0, 0)
            .rot("eyeR", 0, 0, 0, 0).rot("eyeR", 15, 10, 0, 8).rot("eyeR", 30, -6, 0, -4).rot("eyeR", 40, 0, 0, 0)
            .rot("eyeL", 0, 0, 0, 0).rot("eyeL", 20, 8, 0, -8).rot("eyeL", 36, -8, 0, 4).rot("eyeL", 40, 0, 0, 0);
        // OIL SPRAY: lean the shell toward the target, spin it up and spew oil from the nozzle
        m.clip("oil_spray", 34)
            .rot("base", 0, 0, 0, 0).rot("base", 8, 12, 0, 0).rot("base", 14, -10, 0, 0, Ease.OUT_BACK).rot("base", 26, -6, 0, 0).rot("base", 34, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 10, 0, 0).rot("head", 14, -25, 0, 0).rot("head", 26, -15, 0, 0).rot("head", 34, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 50, 0, 0, Ease.OUT_BACK).rot("jaw", 26, 40, 0, 0).rot("jaw", 34, 0, 0, 0)
            .pos("drip", 12, 0, 0, 0).pos("drip", 24, 0, -1, 7).scl("drip", 12, 0.5f, 0.5f, 0.5f).scl("drip", 18, 2.5f, 1.5f, 3f).scl("drip", 24, 3.5f, 1f, 4f);
        // SLAM: rear up and pound the shell down
        m.clip("slam", 32)
            .rot("base", 0, 0, 0, 0).rot("base", 10, -30, 0, 0, Ease.OUT_QUAD).rot("base", 15, 25, 0, 0, Ease.IN_CUBIC).rot("base", 22, 12, 0, 0).rot("base", 32, 0, 0, 0)
            .pos("base", 0, 0, 0, 0).pos("base", 10, 0, 3, -2).pos("base", 15, 0, -0.5f, 4, Ease.IN_CUBIC).pos("base", 32, 0, 0, 0)
            .scl("base", 0, 1, 1, 1).scl("base", 15, 1.15f, 0.85f, 1.1f).scl("base", 32, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -35, 0, 0).rot("head", 15, 30, 0, 0, Ease.IN_CUBIC).rot("head", 32, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 40, 0, 0).rot("jaw", 15, 0, 0, 0).rot("jaw", 32, 0, 0, 0);
        // RAGE: shake, shell spins madly (proc), swell and roar
        m.clip("rage", 36)
            .rot("base", 0, 0, 0, 0).rot("base", 6, 0, 0, 5).rot("base", 9, 0, 0, -5).rot("base", 12, 0, 0, 5).rot("base", 15, 0, 0, -5).rot("base", 18, 0, 0, 0)
            .scl("base", 0, 1, 1, 1).scl("base", 18, 1.2f, 1.2f, 1.2f, Ease.OUT_BACK).scl("base", 36, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -35, 0, 0, Ease.OUT_BACK).rot("head", 28, -30, 0, 0).rot("head", 36, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 60, 0, 0, Ease.OUT_BACK).rot("jaw", 28, 50, 0, 0).rot("jaw", 36, 0, 0, 0);
        U.hurt(m, "base");
        m.clip("death", 32)
            .rot("base", 0, 0, 0, 0).rot("base", 10, -12, 0, 15).rot("base", 24, 0, 0, 90, Ease.OUT_BOUNCE).rot("base", 32, 0, 0, 90)
            .pos("base", 0, 0, 0, 0).pos("base", 24, 3, 0, 0).scl("base", 32, 1.15f, 0.8f, 1.1f)
            .rot("head", 0, 0, 0, 0).rot("head", 20, 40, 0, 20).rot("jaw", 0, 0, 0, 0).rot("jaw", 16, 45, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_sludge_spinner", "Sludge Spinner", "スラッジスピナー")
            .game(2, 1).size(1.0f, 1.0f).hp(38).speed(0.2f).style(Style.SLIME).egg(0x5a4a6a, 0x7d6a3a)
            .model(SludgeSpinner::model)
            .spawn(6, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(s -> {
                if (s.getTurn() == 0) return "oil_spray";
                String l = s.lastMove(), pick;
                do { pick = IDS[s.rand().nextInt(3)]; } while (pick.equals(l));
                return pick;
            })
            .move(Move.of("oil_spray", 34).intent(Intent.ATTACK_DEBUFF, 8, 1).reach(7).ranged(7)
                .sound(12, SoundEvents.LLAMA_SPIT, 1.2f, 0.6f)
                .at(18, (s, t) -> { Fx.particles(s, ParticleTypes.SQUID_INK, 20, 0.5); Fx.hitTarget(8, v -> Fx.weak(v, 5)).run(s, t); }))
            .move(Move.of("slam", 32).intent(Intent.ATTACK, 11, 1).reach(2.4f)
                .sound(15, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.7f)
                .at(15, Fx.melee(11, 2.8, 90)))
            .move(Move.of("rage", 36).intent(Intent.ATTACK_BUFF, 6, 1).reach(2.4f)
                .sound(12, SoundEvents.RAVAGER_ROAR, 0.9f, 1.3f)
                .at(18, (s, t) -> { Fx.melee(6, 2.8, 120).run(s, t); s.addStrength(3); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 10, 0.6); }));
    }
}
