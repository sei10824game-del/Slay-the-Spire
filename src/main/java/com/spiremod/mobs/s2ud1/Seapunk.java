package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Seapunk (44-46 HP). A shaggy kelp ball with a gaping mouth, stubby legs and leaf-tipped stalks.
 * Fixed cycle: Sea Kick (11) -> Spinning Kick (2x4) -> Bubble Burp (7 Block, +1 Strength) -> ...
 */
public final class Seapunk {
    private Seapunk() {}

    static final int KELP = 0x7cc35a, KELP_D = 0x4f8f3a, KELP_L = 0xa6de7a, MOUTH = 0x4a2418, MOUTH_D = 0x2a120c, LEAF = 0x3fae5a, STALK = 0x6fb35c;
    static final int N_STALK = 8;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_seapunk");
        Part base = m.add("base");
        // ---- stubby kelp-root legs
        for (int s = -1; s <= 1; s += 2) {
            Part leg = base.add(s < 0 ? "legR" : "legL").at(s * 3, 5, 0);
            leg.box(-1, -5, -1, 2, 5, 2).color(KELP_D).noise(0.12f);
            leg.box(-1.5f, -5, -1, 3, 1, 4).color(KELP_D).bevel(0.2f);          // splayed toes
        }
        // ---- body: shaggy ball
        Part body = base.add("body").at(0, 5, 0);
        body.box(-6, 0, -5, 12, 11, 10).color(KELP).grad(KELP_L, KELP_D).noise(0.18f).bevel(0.2f)
            .paint(Face.FRONT, 2, 3, 8, 5, MOUTH).paint(Face.FRONT, 3, 4, 6, 3, MOUTH_D);
        body.box(-5, 1, -5.5f, 10, 9, 11).color(KELP_D).noise(0.2f).inflate(0.3f)
            .paint(Face.FRONT, 1, 2, 8, 5, MOUTH).paint(Face.FRONT, 2, 3, 6, 3, MOUTH_D);  // inner shag layer
        // fringe strands around the body
        float[][] fr = {{-6.5f, 2, 3}, {6.5f, 3, -2}, {-6.5f, 6, -3}, {6.5f, 7, 2}, {-3, 0, 5.5f}, {3, 0, 5.5f}, {0, 0, -5.5f}, {-4, 10.5f, 2}, {4, 10.5f, -1}};
        for (int i = 0; i < fr.length; i++) {
            Part f = body.add("fringe" + i).at(fr[i][0], fr[i][1], fr[i][2]).rot((i % 3 - 1) * 20, i * 40, (i % 2 == 0 ? 25 : -25));
            f.box(-0.5f, -3, -0.5f, 1, 4, 1).color(i % 2 == 0 ? KELP_L : KELP).noise(0.1f);
        }
        // mouth: upper lip ridge + lower jaw flap (opens for burp)
        Part jaw = body.add("jaw").at(0, 3, 5);
        jaw.box(-4, -1, -0.5f, 8, 2, 1).color(KELP).bevel(0.2f).paint(Face.BACK, 1, 0, 6, 2, MOUTH_D);
        for (int i = 0; i < 4; i++) jaw.box(-3.5f + i * 2, 1, 0, 1, 1, 1).color(0xe8e0b0).flat();         // little teeth
        // ---- leaf-tipped stalks fanning out from the top
        for (int i = 0; i < N_STALK; i++) {
            float a = -55 + i * (110f / (N_STALK - 1));
            Part st = body.add("stalk" + i).at((float) Math.sin(Math.toRadians(a)) * 4, 10, (i % 2 == 0 ? 1 : -1.5f)).rot((i % 2 == 0 ? 10 : -12), 0, -a);
            st.box(-0.5f, 0, -0.5f, 1, 9, 1).color(STALK).noise(0.05f);
            Part st2 = st.add("stalkT" + i).at(0, 9, 0).rot(0, 0, -a * 0.25f);
            st2.box(-0.5f, 0, -0.5f, 1, 6, 1).color(STALK);
            st2.box(-1, 6, -0.5f, 2, 3, 1).color(LEAF).grad(0x63d07a, LEAF).bevel(0.15f);
            st2.box(-0.5f, 9, -0.5f, 1, 1, 1).color(0x2f8a45);
        }
        // ---- bubble (only visible during Bubble Burp)
        Part bub = m.add("bubble").at(0, 9, 7);
        bub.cbox(-2.5f, 5, 5, 5).color(0xbfe8ff).flat().noise(0.02f).glow(Face.FRONT, 1, 1, 1, 1, 0xffffff);
        m.proc((r, c) -> {
            // stalks sway continuously, each out of phase; strong whip when moving
            for (int i = 0; i < N_STALK; i++) {
                float w = (float) Math.sin(c.age * 0.12f + i * 0.9f);
                r.rot("stalk" + i, w * 6, 0, w * 5);
                r.rot("stalkT" + i, w * 10, 0, (float) Math.cos(c.age * 0.15f + i) * 8);
            }
            r.show("bubble", "bubble_burp".equals(c.move) && c.moveT > 13 && c.moveT < 28);
        });
        m.walkBiped("legR", "legL", null, null, 45, 0);
        m.clip("walk", 20).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 5, 0, 1.2f, 0).pos("body", 10, 0, 0, 0).pos("body", 15, 0, 1.2f, 0).pos("body", 20, 0, 0, 0)
            .rot("body", 0, 0, 0, 6).rot("body", 10, 0, 0, -6).rot("body", 20, 0, 0, 6);
        m.clip("idle", 40).loop()
            .scl("body", 0, 1, 1, 1).scl("body", 20, 1.05f, 0.95f, 1.05f).scl("body", 40, 1, 1, 1)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 20, 12, 0, 0).rot("jaw", 40, 0, 0, 0);
        // ---- SEA KICK: crouch, hop & lean back, right leg snaps forward, land
        m.clip("sea_kick", 28)
            .pos("body", 0, 0, 0, 0).pos("body", 7, 0, -1.5f, -1).pos("body", 11, 0, 3, 3, Ease.OUT_QUAD).pos("body", 13, 0, 3.5f, 4).pos("body", 20, 0, 0, 1, Ease.OUT_BOUNCE).pos("body", 28, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 7, 12, 0, 0).rot("body", 12, -28, 0, 0, Ease.IN_CUBIC).rot("body", 17, -20, 0, 0).rot("body", 28, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 7, 1.12f, 0.85f, 1.12f).scl("body", 11, 0.92f, 1.1f, 0.92f).scl("body", 20, 1.1f, 0.9f, 1.1f).scl("body", 28, 1, 1, 1)
            .scl("legR", 0, 1, 1, 1).scl("legR", 8, 1, 1, 1).scl("legR", 12, 1.3f, 2f, 1.3f, Ease.OUT_BACK).scl("legR", 18, 1.2f, 1.8f, 1.2f).scl("legR", 24, 1, 1, 1)
            .pos("legR", 0, 0, 0, 0).pos("legR", 7, 0, -1.5f, -1).pos("legR", 12, 0, 4, 4).pos("legR", 20, 0, 0, 1).pos("legR", 28, 0, 0, 0)
            .pos("legL", 0, 0, 0, 0).pos("legL", 7, 0, -1.5f, -1).pos("legL", 12, 0, 3, 3).pos("legL", 20, 0, 0, 1).pos("legL", 28, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 8, 40, 0, 0).rot("legR", 12, -110, 0, 0, Ease.IN_CUBIC).rot("legR", 17, -100, 0, 0).rot("legR", 24, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 12, 25, 0, 0).rot("legL", 24, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 35, 0, 0).rot("jaw", 22, 0, 0, 0);
        // ---- SPINNING KICK: hop up and whirl 4 full turns with the leg stuck out (4 hits)
        m.clip("spinning_kick", 46)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, -1.5f, 0).pos("body", 10, 0, 4, 0, Ease.OUT_QUAD).pos("body", 34, 0, 4, 0).pos("body", 38, 0, 0, 0, Ease.OUT_BOUNCE).pos("body", 46, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 6, 10, 0, 0).rot("body", 10, 0, 0, 0).rot("base", 0, 0, 0, 0)
            .rot("base", 10, 0, 0, 0).rot("base", 16, 0, 360, 0, Ease.LINEAR).rot("base", 22, 0, 720, 0, Ease.LINEAR).rot("base", 28, 0, 1080, 0, Ease.LINEAR).rot("base", 34, 0, 1440, 0, Ease.LINEAR).rot("base", 40, 0, 1440, 0).rot("base", 46, 0, 1440, 0)
            .pos("legR", 0, 0, 0, 0).pos("legR", 10, 0, 4, 0, Ease.OUT_QUAD).pos("legR", 34, 0, 4, 0).pos("legR", 38, 0, 0, 0).pos("legR", 46, 0, 0, 0)
            .pos("legL", 0, 0, 0, 0).pos("legL", 10, 0, 4, 0, Ease.OUT_QUAD).pos("legL", 34, 0, 4, 0).pos("legL", 38, 0, 0, 0).pos("legL", 46, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 10, 0, 0, -85).rot("legR", 34, 0, 0, -85).rot("legR", 40, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 10, -20, 0, 20).rot("legL", 34, -20, 0, 20).rot("legL", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 30, 0, 0).rot("jaw", 34, 30, 0, 0).rot("jaw", 40, 0, 0, 0);
        // ---- BUBBLE BURP: swell up, mouth gapes, big bubble pops out and floats up, body deflates
        m.clip("bubble_burp", 34)
            .scl("body", 0, 1, 1, 1).scl("body", 12, 1.25f, 1.2f, 1.25f, Ease.OUT_QUAD).scl("body", 15, 0.85f, 0.9f, 0.85f, Ease.IN_CUBIC).scl("body", 22, 1.05f, 0.95f, 1.05f, Ease.OUT_ELASTIC).scl("body", 34, 1, 1, 1)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -12, 0, 0).rot("body", 15, 14, 0, 0).rot("body", 34, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 10, 0, 0).rot("jaw", 15, 60, 0, 0, Ease.OUT_BACK).rot("jaw", 24, 40, 0, 0).rot("jaw", 32, 0, 0, 0)
            .pos("bubble", 14, 0, -2, -2).pos("bubble", 16, 0, 0, 2, Ease.OUT_BACK).pos("bubble", 28, 0, 9, 4)
            .scl("bubble", 14, 0.3f, 0.3f, 0.3f).scl("bubble", 17, 1.1f, 1.1f, 1.1f, Ease.OUT_BACK).scl("bubble", 26, 1.3f, 1.3f, 1.3f).scl("bubble", 28, 1.8f, 1.8f, 1.8f)
            .rot("legR", 0, 0, 0, 0).rot("legR", 12, 0, 0, -15).rot("legR", 24, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 12, 0, 0, 15).rot("legL", 24, 0, 0, 0);
        m.clip("hurt", 10)
            .rot("body", 0, 0, 0, 0).rot("body", 3, 16, 0, 8).rot("body", 10, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 3, 1.12f, 0.88f, 1.12f).scl("body", 10, 1, 1, 1);
        m.clip("death", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -20, 0, 15).rot("body", 22, 0, 0, 80, Ease.OUT_BOUNCE).rot("body", 30, 0, 0, 85)
            .pos("body", 0, 0, 0, 0).pos("body", 22, -3, -3, 0, Ease.OUT_BOUNCE).pos("body", 30, -3, -3.5f, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 30, 1.1f, 0.7f, 1.1f)
            .rot("legR", 0, 0, 0, 0).rot("legR", 20, -60, 0, -30).rot("legL", 0, 0, 0, 0).rot("legL", 20, -60, 0, 30)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 50, 0, 0).rot("jaw", 30, 30, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_seapunk", "Seapunk", "シーパンク")
            .game(2, 1).size(0.9f, 1.4f).hp(45).speed(0.27f).style(Style.SLIME).egg(0x7cc35a, 0x4a2418)
            .model(Seapunk::model)
            .spawn(7, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(Pattern.cycle("sea_kick", "spinning_kick", "bubble_burp"))
            .move(Move.of("sea_kick", 28).intent(Intent.ATTACK, 11, 1).reach(2.0f)
                .sound(7, SoundEvents.SLIME_JUMP, 1f, 1.2f)
                .at(12, Fx.melee(11, 2.6, 80)))
            .move(Move.of("spinning_kick", 46).intent(Intent.ATTACK, 2, 4).reach(1.8f)
                .sound(10, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.3f)
                .atAll(new int[]{16, 22, 28, 34}, (s, t) -> { Fx.meleeNow(s, 2, 2.4, 360, null); Fx.particles(s, ParticleTypes.SPLASH, 6, 0.6); }))
            .move(Move.of("bubble_burp", 34).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(15, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1.5f, 0.6f)
                .at(15, (s, t) -> { s.addBlock(7); s.addStrength(1); Fx.particles(s, ParticleTypes.BUBBLE_POP, 20, 0.6); Fx.sound(s, SoundEvents.PLAYER_BURP, 1.2f, 0.6f); }));
    }
}
