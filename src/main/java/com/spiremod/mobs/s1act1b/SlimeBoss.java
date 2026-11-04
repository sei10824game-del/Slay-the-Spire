package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * StS1 Act 1 Boss Slime Boss: 140 HP. Goop Spray (3 Slimed) -> Preparing -> Slam (35), repeat.
 * Split: when it falls to half HP it interrupts and splits into a Spike Slime (L) and an Acid Slime (L), each with the boss's current HP.
 */
public final class SlimeBoss {
    private SlimeBoss() {}
    static final int C = 0x78c43a, CL = 0xc4f070, CD = 0x3a7a22, EYE = 0x14200a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("slime_boss").scale(2.6f);
        Part blob = m.add("blob").at(0, 0, 0);
        blob.box(-8, 0, -8, 16, 5, 16).color(C).grad(C, CD).bevel(0.2f).noise(0.07f).paint(Face.TOP, 3, 3, 3, 2, CL);
        blob.box(-8.6f, 0, -8.6f, 17, 1, 17).color(CD).noise(0.05f);                                      // puddle rim
        Part mid = blob.add("mid").at(0, 5, 0);
        mid.box(-7, 0, -7, 14, 5, 14).color(C).grad(CL, C).bevel(0.15f).noise(0.07f).paint(Face.FRONT, 2, 1, 3, 1, CL);
        Part top = mid.add("top").at(0, 5, 0);
        top.box(-5, 0, -5, 10, 3, 10).color(CL).grad(CL, C).bevel(0.15f);
        top.box(-3, 3, -3, 6, 2, 6).color(CL).grad(0xe8ff9a, CL).bevel(0.15f);
        top.box(-1.5f, 5, -1.5f, 3, 1, 3).color(0xf2ffc4).flat();
        // crown of goop globs on the head
        for (int i = 0; i < 5; i++) {
            Part g = top.add("glob" + i).at(-4 + i * 2, 3, i % 2 == 0 ? 2 : -2);
            g.box(-1, 0, -1, 2, 2 + (i % 3), 2).color(CL).flat().glow(Face.TOP, 0, 0, 2, 2, 0xe8ff9a);
        }
        // face: two big angry eyes with brows
        for (int s = -1; s <= 1; s += 2) {
            Part eye = mid.add(s < 0 ? "eyeR" : "eyeL").at(s * 3.5f, 2.5f, 7);
            eye.box(-2, -1.5f, -0.5f, 4, 3, 1).color(0xf4ffe0).flat().px(Face.FRONT, s < 0 ? 2 : 1, 1, EYE).px(Face.FRONT, s < 0 ? 2 : 1, 2, EYE);
            Part brow = eye.add(s < 0 ? "browR" : "browL").at(0, 2, 0.1f).rot(0, 0, s * 20);
            brow.box(-2.5f, 0, -0.5f, 5, 1, 1).color(CD).flat();
        }
        Part mouth = blob.add("mouth").at(0, 3.5f, 8.2f);
        mouth.box(-5, -2, -0.5f, 10, 4, 1).color(0x1a0c10).flat().paint(Face.FRONT, 0, 0, 10, 1, CD)
            .paint(Face.FRONT, 1, 0, 1, 1, 0xf0f0d0).paint(Face.FRONT, 4, 0, 1, 1, 0xf0f0d0).paint(Face.FRONT, 8, 0, 1, 1, 0xf0f0d0);
        Part tongue = mouth.add("tongue").at(0, -1.5f, 0.3f);
        tongue.box(-1.5f, -0.5f, 0, 3, 1, 6).color(0xd0607a).flat();
        for (int i = 0; i < 4; i++) {                                                                     // drips down the flanks
            Part d = blob.add("drip" + i).at(-6 + i * 4, 10, i % 2 == 0 ? 7.6f : -7.6f);
            d.box(-0.7f, -3 - i % 2 * 2, -0.5f, 1, 3 + i % 2 * 2, 1).color(CL).flat();
        }
        m.proc((r, x) -> r.show("tongue", "goop_spray".equals(x.move)));
        m.clip("idle", 50).loop()
            .scl("blob", 0, 1, 1, 1).scl("blob", 25, 1.04f, 0.94f, 1.04f).scl("blob", 50, 1, 1, 1)
            .rot("top", 0, 0, 0, 3).rot("top", 25, 0, 0, -3).rot("top", 50, 0, 0, 3)
            .scl("mouth", 0, 1, 1, 1).scl("mouth", 36, 1, 1, 1).scl("mouth", 40, 1, 1.5f, 1).scl("mouth", 44, 1, 1, 1);
        m.clip("walk", 20).loop()
            .scl("blob", 0, 1.1f, 0.85f, 1.1f).scl("blob", 5, 0.95f, 1.1f, 0.95f).scl("blob", 10, 1, 1, 1).scl("blob", 15, 1.08f, 0.9f, 1.08f).scl("blob", 20, 1.1f, 0.85f, 1.1f)
            .pos("blob", 0, 0, 0, 0).pos("blob", 5, 0, 2, 0).pos("blob", 10, 0, 3, 0).pos("blob", 15, 0, 0, 0).pos("blob", 20, 0, 0, 0);
        // GOOP SPRAY: swell up, then heave forward and spew three globs from the open maw
        Clip g = m.clip("goop_spray", 36);
        K.s(g, "blob", 0, 1, 1, 1, 12, 0.85f, 1.3f, 0.85f, 116, 1.25f, 0.8f, 1.3f, 24, 1.1f, 0.9f, 1.15f, 36, 1, 1, 1);
        K.r(g, "blob", 0, 0, 0, 0, 12, -14, 0, 0, 116, 16, 0, 0, 36, 0, 0, 0);
        K.s(g, "mouth", 0, 1, 1, 1, 12, 0.8f, 0.7f, 1, 116, 1.5f, 2.6f, 1, 28, 1.3f, 2.2f, 1, 36, 1, 1, 1);
        K.s(g, "tongue", 0, 1, 1, 0.3f, 14, 1, 1, 0.5f, 116, 1, 1, 1.4f, 30, 1, 1, 0.5f, 36, 1, 1, 0.3f);
        K.r(g, "top", 0, 0, 0, 0, 12, -10, 0, 0, 116, 15, 0, 0, 36, 0, 0, 0);
        // PREPARING: sink down, quiver with rising tension, bulge ready to leap
        Clip pr = m.clip("preparing", 30);
        K.s(pr, "blob", 0, 1, 1, 1, 8, 1.2f, 0.75f, 1.2f, 12, 1.15f, 0.8f, 1.15f, 16, 1.25f, 0.7f, 1.25f, 20, 1.18f, 0.78f, 1.18f, 30, 1.3f, 0.65f, 1.3f);
        K.s(pr, "top", 0, 1, 1, 1, 20, 1, 1, 1, 30, 0.8f, 1.2f, 0.8f);
        K.s(pr, "eyeR", 0, 1, 1, 1, 14, 1.4f, 1.4f, 1.4f, 30, 1.5f, 1.5f, 1.5f);
        K.s(pr, "eyeL", 0, 1, 1, 1, 14, 1.4f, 1.4f, 1.4f, 30, 1.5f, 1.5f, 1.5f);
        K.r(pr, "browR", 0, 0, 0, 0, 14, 0, 0, 25, 30, 0, 0, 25);
        K.r(pr, "browL", 0, 0, 0, 0, 14, 0, 0, -25, 30, 0, 0, -25);
        // SLAM: spring up high, crash flat onto the ground and wobble back
        Clip sl = m.clip("slam", 42);
        K.s(sl, "blob", 0, 1.3f, 0.65f, 1.3f, 8, 0.8f, 1.5f, 0.8f, 16, 0.85f, 1.35f, 0.85f, 120, 1.5f, 0.6f, 1.5f, 25, 1.55f, 0.55f, 1.55f, 32, 0.92f, 1.1f, 0.92f, 42, 1, 1, 1);
        K.p(sl, "blob", 0, 0, 0, 0, 8, 0, 8, 2, 18, 0, 14, 5, 120, 0, 0, 7, 25, 0, 0, 7, 42, 0, 0, 0);
        K.r(sl, "blob", 0, 0, 0, 0, 8, -10, 0, 0, 18, 8, 0, 0, 120, 4, 0, 0, 42, 0, 0, 0);
        K.s(sl, "mouth", 0, 1, 1, 1, 18, 1.2f, 2f, 1, 120, 1.6f, 0.5f, 1, 42, 1, 1, 1);
        // SPLIT: tremble, stretch wide, pinch the middle
        m.clip("split", 30)
            .scl("blob", 0, 1, 1, 1).scl("blob", 6, 1.05f, 0.95f, 1).scl("blob", 8, 0.95f, 1.05f, 1).scl("blob", 10, 1.08f, 0.93f, 1).scl("blob", 12, 0.95f, 1.05f, 1)
            .scl("blob", 22, 1.7f, 0.65f, 1.05f, Ease.IN_CUBIC).scl("blob", 30, 1.9f, 0.55f, 1.1f)
            .scl("top", 0, 1, 1, 1).scl("top", 22, 0.4f, 1, 1).scl("top", 30, 0.2f, 1, 1);
        m.clip("hurt", 10).scl("blob", 0, 1, 1, 1).scl("blob", 3, 1.18f, 0.82f, 1.18f).scl("blob", 6, 0.95f, 1.07f, 0.95f).scl("blob", 10, 1, 1, 1);
        m.clip("death", 30)
            .scl("blob", 0, 1, 1, 1).scl("blob", 5, 1.15f, 0.9f, 1.15f).scl("blob", 24, 1.7f, 0.18f, 1.7f, Ease.IN_QUAD).scl("blob", 30, 1.8f, 0.05f, 1.8f)
            .scl("mid", 0, 1, 1, 1).scl("mid", 10, 1, 0.5f, 1).scl("mid", 30, 1, 0.2f, 1);
        return m;
    }

    public static MobDef def() {
        ProjKind.orb("slime_boss_goop", 0x9be04a, 4).gravity(0.02).trail(ParticleTypes.ITEM_SLIME).life(60);
        return MobDef.of("slime_boss", "Slime Boss", "スライムボス")
            .game(1, 1).size(2.8f, 2.6f).hp(140).speed(0.2f).style(Style.SLIME).boss().egg(0x78c43a, 0xc4f070)
            .model(SlimeBoss::model)
            .pattern(Pattern.cycle("goop_spray", "preparing", "slam"))
            .move(Move.of("goop_spray", 36).intent(Intent.DEBUFF).ranged(12)
                .sound(12, SoundEvents.SLIME_SQUISH, 1.6f, 0.5f)
                .at(16, Fx.shoot("slime_boss_goop", 1, 0.8f, 0, 2.0, 1.4, v -> Fx.curse(v, Fx.Curse.SLIMED)))
                .at(19, Fx.shoot("slime_boss_goop", 1, 0.8f, 12, 2.0, 1.4, v -> Fx.curse(v, Fx.Curse.SLIMED)))
                .at(22, Fx.shoot("slime_boss_goop", 1, 0.8f, -12, 2.0, 1.4, v -> Fx.curse(v, Fx.Curse.SLIMED))))
            .move(Move.of("preparing", 30).intent(Intent.UNKNOWN).noTarget()
                .sound(8, SoundEvents.SLIME_SQUISH, 1.4f, 0.4f))
            .move(Move.of("slam", 42).intent(Intent.ATTACK, 35, 1).reach(5.5f)
                .sound(8, SoundEvents.SLIME_JUMP, 1.6f, 0.5f)
                .at(8, Fx.dash(0.55, 0.65))
                .at(21, (s, t) -> {
                    Fx.aoeAt(s, s.position(), 35, 3.6, null);
                    Fx.ring(s, ParticleTypes.ITEM_SLIME, s.position(), 3.0, 36);
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 1f, 0.6f);
                }))
            .move(Move.of("split", 30).intent(Intent.UNKNOWN).noTarget()
                .sound(4, SoundEvents.SLIME_SQUISH, 1.8f, 0.4f)
                .at(29, (s, t) -> {
                    float cur = s.getHealth();
                    String[] kids = {"spike_slime_l", "acid_slime_l"};
                    for (int i = 0; i < 2; i++) {
                        double sg = i == 0 ? 1 : -1;
                        SpireMob k = Fx.summon(s, kids[i], Fx.forward(s).z * sg * 1.6, -Fx.forward(s).x * sg * 1.6);
                        if (k != null) { k.getAttribute(Attributes.MAX_HEALTH).setBaseValue(cur); k.setHealth(cur); }
                    }
                    Fx.particles(s, ParticleTypes.ITEM_SLIME, 50, 1.5); Fx.sound(s, SoundEvents.SLIME_DEATH, 1.8f, 0.5f);
                    s.discard();
                }))
            .onHurt((s, src, dmg) -> {
                if (!s.flags.contains("split") && dmg > 0 && s.getHealth() - dmg > 0 && s.getHealth() - dmg <= s.getMaxHealth() / 2) {
                    s.flags.add("split"); s.cancelMove(); s.forceNext("split");
                }
                return dmg;
            });
    }
}
