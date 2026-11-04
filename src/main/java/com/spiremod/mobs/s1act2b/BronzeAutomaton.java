package com.spiremod.mobs.s1act2b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * StS1 Act 2 boss: Bronze Automaton. 300 HP, Artifact 3.
 * Spawn Orbs, then loops Flail (7x2), Boost (+3 Str, 9 Block), Flail, Boost, HYPER BEAM (45), Stunned.
 */
public final class BronzeAutomaton {
    private BronzeAutomaton() {}

    static final int BR = 0xc08a3a, BR_L = 0xeac06a, BR_D = 0x7a5220, BR_DD = 0x4a3014, BLUE = 0x5fe8ff, BLUE_D = 0x1a6a8a, VERD = 0x5f9a80;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("bronze_automaton");
        m.scale(2.1f);
        // ---------- squat column legs
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part leg = m.add("leg" + n).at(s * 4, 9, 0);
            leg.box(-2.5f, -5, -2.5f, 5, 5, 5).color(BR).grad(BR_L, BR_D).bevel(0.2f).band(2, 1, BR_D);
            leg.box(-2, -8, -2, 4, 3, 4).color(BR_D).grad(BR, BR_DD);
            leg.box(-3, -9, -3.5f, 6, 2, 7).color(BR).grad(BR_L, BR_D).bevel(0.25f)      // round foot plate
                .paint(Face.FRONT, 1, 0, 4, 1, VERD);
            leg.box(-2.8f, -1.5f, -2.8f, 5, 2, 5).inflate(0.3f).color(BR_L).flat();       // knee ring
        }
        // ---------- body: pelvis + barrel chest with blue core
        Part body = m.add("body").at(0, 9, 0);
        body.box(-5, 0, -3.5f, 10, 3, 7).color(BR_D).grad(BR, BR_DD).bevel(0.15f);
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-4, 0, -3, 8, 3, 6).color(BR_D).grad(BR, BR_D).band(1, 1, BR_DD);     // waist rings
        torso.box(-7, 3, -4.5f, 14, 10, 9).color(BR).grad(BR_L, BR_D).bevel(0.12f).noise(0.1f)
            .paint(Face.FRONT, 0, 0, 14, 1, BR_L).paint(Face.FRONT, 0, 9, 14, 1, BR_DD)
            .paint(Face.FRONT, 1, 2, 1, 6, VERD).paint(Face.FRONT, 12, 3, 1, 5, VERD)
            .paint(Face.BACK, 6, 1, 2, 8, BR_D);
        torso.box(-8, 9, -4, 16, 5, 8).color(BR).grad(BR_L, BR_D).bevel(0.2f);           // shoulder yoke
        torso.box(-3, 5, -5.2f, 6, 6, 1).color(BR_L).bevel(0.4f);                        // core housing
        Part core = torso.add("core").at(0, 8, -5.3f);
        core.box(-2, -2, -0.6f, 4, 4, 1).flat().color(BLUE_D).glow(Face.FRONT, 0, 0, 4, 4, BLUE).glow(Face.FRONT, 1, 1, 2, 2, 0xe8ffff);
        for (int s = -1; s <= 1; s += 2) torso.box(s < 0 ? -6.5f : 4.5f, 4, -5, 2, 4, 1).color(BR_D).flat()
            .glow(Face.FRONT, 0, 1, 2, 1, BLUE).glow(Face.FRONT, 0, 3, 2, 1, BLUE);          // vents
        // ---------- head: tall sun-disc helm with a single glowing slit
        Part head = torso.add("head").at(0, 14, 0);
        head.box(-2, 0, -2, 4, 2, 4).color(BR_D).flat();                                // neck
        head.box(-4, 2, -3.5f, 8, 6, 7).color(BR).grad(BR_L, BR_D).bevel(0.15f)
            .paint(Face.FRONT, 0, 0, 8, 1, BR_L).paint(Face.FRONT, 1, 2, 6, 2, BR_DD);
        Part eye = head.add("eye").at(0, 4.5f, -3.6f);
        eye.box(-3, -0.5f, -0.2f, 6, 1, 1).flat().color(BLUE_D).glow(Face.FRONT, 0, 0, 6, 1, BLUE).glow(Face.FRONT, 2, 0, 2, 1, 0xe8ffff);
        Part disc = head.add("disc").at(0, 6, 1);
        disc.box(-7, 0, 0, 14, 8, 1).color(BR).grad(BR_L, BR_D).bevel(0.3f)
            .paint(Face.FRONT, 6, 1, 2, 6, BR_D).paint(Face.FRONT, 1, 3, 12, 2, BR_D);
        disc.box(-5, 8, 0, 10, 2, 1).color(BR_L).bevel(0.3f);
        disc.box(-2, 10, 0, 4, 2, 1).color(BR_L).flat();
        disc.box(-0.5f, 12, 0, 1, 2, 1).color(BLUE_D).flat().glow(Face.FRONT, 0, 0, 1, 2, BLUE);
        for (int s = -1; s <= 1; s += 2) disc.box(s < 0 ? -9 : 7, 1, 0, 2, 5, 1).color(BR_D).flat();
        // ---------- arms: massive, ending in three-fingered bronze claws
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part arm = torso.add("arm" + n).at(s * 9.5f, 11.5f, 0).rot(0, 0, -s * 12);
            arm.box(-4, -3, -4, 8, 6, 8).color(BR).grad(BR_L, BR_D).bevel(0.2f).band(3, 1, BR_D);   // orb shoulder
            arm.box(-2, -9, -2, 4, 7, 4).color(BR_D).grad(BR, BR_DD).band(2, 1, BR_DD).band(5, 1, BR_DD);
            Part fore = arm.add("fore" + n).at(0, -9, 0).rot(-20, 0, 0);
            fore.box(-3, -8, -3, 6, 8, 6).color(BR).grad(BR_L, BR_D).bevel(0.15f)
                .paint(Face.FRONT, 2, 2, 2, 4, VERD).glow(Face.FRONT, 2, 6, 2, 1, BLUE);
            fore.box(-3.4f, -1, -3.4f, 6, 2, 6).inflate(0.4f).color(BR_L).flat();
            Part hand = fore.add("hand" + n).at(0, -8, 0);
            hand.box(-2, -2, -2, 4, 2, 4).color(BR_D).flat();
            for (int f = -1; f <= 1; f++) {
                Part fin = hand.add("fin" + n + (f + 1)).at(f * 1.4f, -2, f == 0 ? -1.2f : 0.6f).rot(f == 0 ? -15 : 10, 0, f * 15);
                fin.box(-0.5f, -4, -0.5f, 1, 4, 1).color(BR).flat().bevel(0.2f);
                fin.box(-0.5f, -5, -0.8f, 1, 1, 1).color(BR_L).flat();
            }
        }
        // ---------- HYPER BEAM: a huge glowing beam out of the core (only shown during the blast)
        Part beam = torso.add("beam").at(0, 8, -5);
        beam.box(-2, -2, -40, 4, 4, 40).flat().color(BLUE).glow(Face.TOP, 0, 0, 4, 40, BLUE).glow(Face.BOTTOM, 0, 0, 4, 40, BLUE)
            .glow(Face.LEFT, 0, 0, 40, 4, BLUE).glow(Face.RIGHT, 0, 0, 40, 4, BLUE).glow(Face.FRONT, 0, 0, 4, 4, 0xe8ffff);
        beam.box(-1, -1, -42, 2, 2, 42).flat().inflate(0.2f).color(0xe8ffff).all(0xe8ffff).glow(Face.TOP, 0, 0, 2, 42, 0xffffff).glow(Face.LEFT, 0, 0, 42, 2, 0xffffff).glow(Face.RIGHT, 0, 0, 42, 2, 0xffffff).glow(Face.BOTTOM, 0, 0, 2, 42, 0xffffff);
        m.proc((r, c) -> {
            boolean firing = "hyper_beam".equals(c.move) && c.moveT >= 24 && c.moveT < 40;
            r.show("beam", firing);
            if (firing) { float k = 1 + (float) Math.sin(c.age * 2.5f) * 0.15f; r.scl("beam", k, k, 1); }
            // the core pulses; dims when stunned
            boolean stunned = "stunned".equals(c.move);
            r.show("eye", !(stunned && c.moveT > 8));
            float p = stunned ? 0.85f : 1 + (float) Math.sin(c.age * 0.25f) * 0.08f;
            r.scl("core", p, p, 1);
        });
        m.look("head", 35, 20);
        m.walkBiped("legR", "legL", "armR", "armL", 22f, 10f);
        m.proc((r, c) -> { float s = (float) Math.cos(c.swing * 0.6662f) * c.swingAmt; r.rot("body", 0, 0, s * 4); r.pos("body", 0, -Math.abs(s) * 0.8f, 0); });

        // ---------- idle: whirring, slow mechanical sway, disc tilts, fingers twitch
        m.clip("idle", 60).loop()
            .rot("torso", 0, 0, 0, 0).rot("torso", 30, 2, 4, 0).rot("torso", 60, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 15, 0, -6, 0, Ease.STEP).rot("head", 45, 0, 6, 0, Ease.STEP).rot("head", 60, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 30, -5, 0, -4).rot("armR", 60, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 30, -5, 0, 4).rot("armL", 60, 0, 0, 0)
            .rot("finR0", 0, 0, 0, 0).rot("finR0", 20, 0, 0, 0).rot("finR0", 23, 0, 0, 25).rot("finR0", 26, 0, 0, 0).rot("finR0", 60, 0, 0, 0)
            .rot("finL2", 0, 0, 0, 0).rot("finL2", 40, 0, 0, 0).rot("finL2", 43, 0, 0, -25).rot("finL2", 46, 0, 0, 0).rot("finL2", 60, 0, 0, 0);

        // ---------- SPAWN ORBS: arms swept up and outward, core flares, disc spins a notch
        m.clip("spawn_orbs", 40)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -150, 0, -40, Ease.OUT_BACK).rot("armR", 20, -90, 0, -70, Ease.IN_CUBIC).rot("armR", 32, -85, 0, -70).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -150, 0, 40, Ease.OUT_BACK).rot("armL", 20, -90, 0, 70, Ease.IN_CUBIC).rot("armL", 32, -85, 0, 70).rot("armL", 40, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -12, 0, 0).rot("torso", 20, 6, 0, 0).rot("torso", 40, 0, 0, 0)
            .rot("disc", 0, 0, 0, 0).rot("disc", 20, 0, 0, 0).rot("disc", 28, 0, 0, 90, Ease.OUT_BACK).rot("disc", 34, 0, 0, 0)
            .scl("core", 0, 1).scl("core", 18, 1.6f).scl("core", 22, 2.0f).scl("core", 40, 1);

        // ---------- FLAIL: left claw swings across, then right claw — torso twists with each hit
        m.clip("flail", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 0, 35, 0, Ease.OUT_CUBIC).rot("torso", 12, 10, -35, 0, Ease.IN_CUBIC)
                .rot("torso", 16, 0, -40, 0).rot("torso", 20, 10, 35, 0, Ease.IN_CUBIC).rot("torso", 28, 5, 30, 0).rot("torso", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -80, 30, -40, Ease.OUT_CUBIC).rot("armL", 12, -90, -60, 50, Ease.IN_CUBIC).rot("armL", 20, -20, -20, 20).rot("armL", 36, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 8, -60, 0, 0).rot("foreL", 12, 0, 0, 0).rot("foreL", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -20, 20, -20).rot("armR", 16, -80, -30, 40, Ease.OUT_CUBIC).rot("armR", 20, -90, 60, -50, Ease.IN_CUBIC).rot("armR", 28, -80, 55, -45).rot("armR", 36, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 16, -60, 0, 0).rot("foreR", 20, 0, 0, 0).rot("foreR", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 0, 2).pos("body", 20, 0, 0, 2.5f).pos("body", 36, 0, 0, 0);

        // ---------- BOOST: crouch, pistons pump, vents flare, rises taller (Strength + Block)
        m.clip("boost", 36)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -2.5f, 0).pos("body", 14, 0, -1.5f, 0).pos("body", 17, 0, -2.5f, 0).pos("body", 22, 0, 1.5f, 0, Ease.OUT_BACK).pos("body", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -40, 0, 20).rot("armR", 22, -20, 0, -35, Ease.OUT_BACK).rot("armR", 30, -20, 0, -35).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -40, 0, -20).rot("armL", 22, -20, 0, 35, Ease.OUT_BACK).rot("armL", 30, -20, 0, 35).rot("armL", 36, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 10, -90, 0, 0).rot("foreR", 22, -100, 0, 0).rot("foreR", 36, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 10, -90, 0, 0).rot("foreL", 22, -100, 0, 0).rot("foreL", 36, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 15, 0, 0).rot("torso", 22, -8, 0, 0).rot("torso", 36, 0, 0, 0)
            .scl("torso", 0, 1).scl("torso", 22, 1.07f).scl("torso", 30, 1.07f).scl("torso", 36, 1)
            .scl("core", 0, 1).scl("core", 22, 1.8f).scl("core", 36, 1);

        // ---------- HYPER BEAM: long charge (lean back, arms braced wide, core swells) then a massive blast
        m.clip("hyper_beam", 54)
            .rot("torso", 0, 0, 0, 0).rot("torso", 20, -18, 0, 0).rot("torso", 23, -20, 0, 0).rot("torso", 24, 8, 0, 0, Ease.IN_CUBIC)
                .rot("torso", 28, 4, 1.5f, 0).rot("torso", 32, 6, -1.5f, 0).rot("torso", 36, 4, 1.5f, 0).rot("torso", 40, 5, 0, 0).rot("torso", 54, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, -1, 1).pos("body", 24, 0, -1.5f, -2.5f, Ease.OUT_CUBIC).pos("body", 40, 0, -1.5f, -3).pos("body", 54, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 20, -60, 0, -55).rot("armR", 24, -40, 0, -70).rot("armR", 40, -40, 0, -70).rot("armR", 54, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 20, -60, 0, 55).rot("armL", 24, -40, 0, 70).rot("armL", 40, -40, 0, 70).rot("armL", 54, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 20, -70, 0, 0).rot("foreR", 54, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 20, -70, 0, 0).rot("foreL", 54, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 20, -15, 0, 0).rot("head", 24, 5, 0, 0).rot("head", 54, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 24, -15, 0, -8).rot("legR", 40, -15, 0, -8).rot("legR", 54, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 24, 15, 0, 8).rot("legL", 40, 15, 0, 8).rot("legL", 54, 0, 0, 0)
            .scl("core", 0, 1).scl("core", 6, 1.3f).scl("core", 10, 1.1f).scl("core", 14, 1.6f).scl("core", 18, 1.3f).scl("core", 22, 2.2f).scl("core", 40, 2.0f).scl("core", 54, 1);

        // ---------- STUNNED: overheated: slumps, arms hang dead, head droops, twitches
        m.clip("stunned", 60)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, 28, 0, 6, Ease.OUT_BOUNCE).rot("torso", 50, 28, 0, 6).rot("torso", 60, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -2, 0, Ease.OUT_BOUNCE).pos("body", 50, 0, -2, 0).pos("body", 60, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 30, 0, 15).rot("head", 30, 30, 0, 15).rot("head", 32, 22, 0, 5).rot("head", 34, 30, 0, 15).rot("head", 50, 30, 0, 15).rot("head", 60, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, 25, 0, 15).rot("armR", 50, 25, 0, 15).rot("armR", 60, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, 25, 0, -15).rot("armL", 40, 25, 0, -15).rot("armL", 42, 10, 0, -5).rot("armL", 44, 25, 0, -15).rot("armL", 60, 0, 0, 0)
            .rot("foreR", 0, 0, 0, 0).rot("foreR", 12, 20, 0, 0).rot("foreR", 60, 0, 0, 0)
            .rot("foreL", 0, 0, 0, 0).rot("foreL", 12, 20, 0, 0).rot("foreL", 60, 0, 0, 0);

        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -10, 0, 4).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -12, 8, 0).rot("head", 10, 0, 0, 0);
        // ---------- death: shudders, the disc falls off, collapses backwards
        m.clip("death", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 4, 4, 4, 0).rot("torso", 8, -4, -4, 0).rot("torso", 12, 4, 4, 0).rot("torso", 16, -4, -4, 0).rot("torso", 36, -70, 0, 10, Ease.IN_QUAD).rot("torso", 50, -68, 0, 10)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, 0, 0).pos("body", 36, 0, -6, -3, Ease.OUT_BOUNCE).pos("body", 50, 0, -6, -3)
            .pos("disc", 0, 0, 0, 0).pos("disc", 18, 0, 0, 0).pos("disc", 34, 3, -14, 8, Ease.OUT_BOUNCE).pos("disc", 50, 3, -14, 8)
            .rot("disc", 0, 0, 0, 0).rot("disc", 18, 0, 0, 0).rot("disc", 34, -80, 0, 30).rot("disc", 50, -80, 0, 30)
            .rot("armR", 0, 0, 0, 0).rot("armR", 36, -60, 0, -50).rot("armR", 50, -60, 0, -50)
            .rot("armL", 0, 0, 0, 0).rot("armL", 36, -50, 0, 60).rot("armL", 50, -50, 0, 60)
            .rot("legR", 0, 0, 0, 0).rot("legR", 36, -40, 0, 0).rot("legR", 50, -40, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 36, -30, 0, 0).rot("legL", 50, -30, 0, 0)
            .scl("core", 0, 1).scl("core", 16, 1.5f).scl("core", 30, 0.2f).scl("core", 50, 0.2f);
        return m;
    }

    /** Artifact: negates the next n debuffs (counter "artifact" holds charges left) */
    static void artifact(SpireMob s) {
        if (s.tickCount % 4 != 0 || s.count("artifact") <= 0) return;
        for (var e : s.getActiveEffects()) {
            if (!e.getEffect().isBeneficial()) {
                s.removeEffect(e.getEffect());
                s.setCount("artifact", s.count("artifact") - 1);
                Fx.particles(s, ParticleTypes.ENCHANT, 20, 1.0);
                Fx.sound(s, SoundEvents.AMETHYST_BLOCK_CHIME, 1f, 1.4f);
                return;
            }
        }
    }

    static void beamLine(SpireMob s, LivingEntity t) {
        if (t == null) return;
        Vec3 a = Fx.rel(s, 0.8, 0, 2.3), b = t.position().add(0, t.getBbHeight() * 0.5, 0);
        for (int i = 0; i <= 24; i++) Fx.particlesAt(s, i % 3 == 0 ? ParticleTypes.END_ROD : ParticleTypes.ELECTRIC_SPARK, a.lerp(b, i / 24.0), 3, 0.15, 0.02);
        Fx.particlesAt(s, ParticleTypes.EXPLOSION, b, 1, 0.1, 0);
    }

    public static MobDef def() {
        return MobDef.of("bronze_automaton", "Bronze Automaton", "ブロンズオートマトン")
            .game(1, 2).size(2.0f, 4.4f).hp(300).speed(0.2f).style(Style.METAL).egg(0xc08a3a, 0x5fe8ff)
            .model(BronzeAutomaton::model).boss().kbResist(1f)
            .onSpawn(s -> s.setCount("artifact", 3))
            .onTick(BronzeAutomaton::artifact)
            .pattern(Pattern.seq(1, "spawn_orbs", "flail", "boost", "flail", "boost", "hyper_beam", "stunned"))
            .move(Move.of("spawn_orbs", 40).intent(Intent.SUMMON).noTarget()
                .sound(14, SoundEvents.BEACON_ACTIVATE, 1.5f, 1.2f)
                .at(22, (s, t) -> { Fx.summon(s, "bronze_orb", Fx.rel(s, 0.5, 3, 0).x - s.getX(), Fx.rel(s, 0.5, 3, 0).z - s.getZ());
                                    Fx.summon(s, "bronze_orb", Fx.rel(s, 0.5, -3, 0).x - s.getX(), Fx.rel(s, 0.5, -3, 0).z - s.getZ());
                                    Fx.particles(s, ParticleTypes.ELECTRIC_SPARK, 30, 1.5); }))
            .move(Move.of("flail", 36).intent(Intent.ATTACK, 7, 2).reach(3.5f)
                .sound(9, SoundEvents.CHAIN_BREAK, 1.2f, 0.6f).sound(17, SoundEvents.CHAIN_BREAK, 1.2f, 0.7f)
                .at(12, Fx.melee(7, 4.4, 130)).at(20, Fx.melee(7, 4.4, 130)))
            .move(Move.of("boost", 36).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(10, SoundEvents.PISTON_EXTEND, 1.3f, 0.6f).sound(16, SoundEvents.PISTON_CONTRACT, 1.3f, 0.6f)
                .at(22, (s, t) -> { s.addStrength(3); s.addBlock(9); Fx.particles(s, ParticleTypes.CLOUD, 20, 1.2); Fx.sound(s, SoundEvents.FIRE_EXTINGUISH, 1f, 0.8f); }))
            .move(Move.of("hyper_beam", 54).intent(Intent.ATTACK, 45, 1).ranged(14).faceUntil(22)
                .sound(2, SoundEvents.BEACON_POWER_SELECT, 1.6f, 0.5f).sound(14, SoundEvents.BEACON_AMBIENT, 2f, 1.6f)
                .at(8, (s, t) -> Fx.particlesAt(s, ParticleTypes.ELECTRIC_SPARK, Fx.rel(s, 0.8, 0, 2.3), 15, 0.4, 0.05))
                .at(18, (s, t) -> Fx.particlesAt(s, ParticleTypes.END_ROD, Fx.rel(s, 0.8, 0, 2.3), 25, 0.5, 0.1))
                .at(24, (s, t) -> { Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 1.6f, 1.4f); Fx.sound(s, SoundEvents.BEACON_DEACTIVATE, 2f, 0.5f); beamLine(s, t); if (t != null) Fx.dmg(s, t, 45); })
                .at(30, (s, t) -> beamLine(s, t)).at(36, (s, t) -> beamLine(s, t)))
            .move(Move.of("stunned", 60).intent(Intent.STUN).noTarget()
                .sound(4, SoundEvents.IRON_GOLEM_DAMAGE, 1f, 0.5f)
                .at(10, (s, t) -> Fx.particles(s, ParticleTypes.LARGE_SMOKE, 25, 1.0))
                .at(30, (s, t) -> Fx.particles(s, ParticleTypes.ELECTRIC_SPARK, 15, 1.0)));
    }
}
