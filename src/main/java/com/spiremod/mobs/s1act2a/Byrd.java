package com.spiremod.mobs.s1act2a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 Byrd. 25-31 HP, Flight 3 (halves attack damage, -1 per hit, refills each turn; at 0 it is Grounded + Stunned).
 * Flying: Peck 1x5 (50%), Caw +1 Str (30%), Swoop 12 (20%); never opens with Swoop; no Caw/Swoop twice, no Peck 3x.
 * Grounded: Stunned -> Headbutt 3 -> Go Airborne (Flight 3) -> back to flying pattern.
 * Look: plump violet bird, cream belly, big orange beak, ragged wings, tuft crest.
 */
public final class Byrd {
    private Byrd() {}

    static final int PLUM = 0x6b4c8c, PLUM_D = 0x41305a, BELLY = 0xe8d7b0, BEAK = 0xf0a030, BEAK_D = 0xb06a18, LEG = 0xe08a2a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("byrd");
        Part body = m.add("body").at(0, 9, 0);
        body.cbox(-4.5f, 10, 9, 11).color(PLUM).grad(0x7f5ca4, PLUM_D).noise(0.12f).bevel(0.15f);
        body.box(-4, -4, 4.6f, 8, 7, 2).color(BELLY).grad(0xf4e6c4, 0xc8b48a).noise(0.08f);      // belly
        body.cbox(-3.5f, 8, 2, 9).color(PLUM_D).inflate(0.1f);                                     // under fluff
        Part tail = body.add("tail").at(0, 1, -5.5f).rot(-25, 0, 0);
        tail.box(-3, -0.5f, -6, 6, 1, 6).color(PLUM_D).grad(PLUM, 0x2a1f3c);
        tail.box(-4, -0.5f, -7, 2, 1, 3).color(0x2a1f3c);
        tail.box(2, -0.5f, -7, 2, 1, 3).color(0x2a1f3c);
        Part head = body.add("head").at(0, 4, 3);
        head.box(-4, 0, -3, 8, 7, 7).color(PLUM).grad(0x8a66b0, PLUM).noise(0.1f).bevel(0.12f)
            .paint(Face.FRONT, 1, 2, 2, 2, 0xffffff).px(Face.FRONT, 2, 3, 0x101010)
            .paint(Face.FRONT, 5, 2, 2, 2, 0xffffff).px(Face.FRONT, 5, 3, 0x101010)
            .paint(Face.FRONT, 0, 1, 3, 1, PLUM_D).paint(Face.FRONT, 5, 1, 3, 1, PLUM_D);       // angry brows
        Part crest = head.add("crest").at(0, 7, 0).rot(-20, 0, 0);
        crest.box(-0.5f, 0, -1, 1, 4, 1).color(PLUM_D);
        crest.box(-0.5f, 0, -3, 1, 3, 1).color(PLUM_D);
        crest.box(-0.5f, 0, 1, 1, 3, 1).color(0x8a66b0);
        Part beakU = head.add("beakU").at(0, 2.5f, 4);
        beakU.box(-2, 0, 0, 4, 2, 4).color(BEAK).grad(0xffc050, BEAK_D).bevel(0.12f);
        beakU.box(-1, -0.5f, 3, 2, 2, 2).color(BEAK_D);
        Part beakL = head.add("beakL").at(0, 2.5f, 4);
        beakL.box(-1.5f, -1.5f, 0, 3, 1, 3).color(BEAK_D);
        // wings: hinge at shoulder, extend sideways
        Part wL = body.add("wingL").at(5, 2.5f, 0);
        wL.box(0, -0.5f, -3.5f, 7, 1, 7).color(PLUM).grad(0x8a66b0, PLUM_D).bevel(0.15f);
        Part wLt = wL.add("wingLtip").at(7, 0, 0);
        wLt.box(0, -0.5f, -3, 5, 1, 5).color(PLUM_D);
        wLt.box(0, -0.5f, -4.5f, 4, 1, 2).color(0x2a1f3c);
        Part wR = body.add("wingR").at(-5, 2.5f, 0);
        wR.box(-7, -0.5f, -3.5f, 7, 1, 7).color(PLUM).grad(0x8a66b0, PLUM_D).bevel(0.15f);
        Part wRt = wR.add("wingRtip").at(-7, 0, 0);
        wRt.box(-5, -0.5f, -3, 5, 1, 5).color(PLUM_D);
        wRt.box(-4, -0.5f, -4.5f, 4, 1, 2).color(0x2a1f3c);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = body.add(s < 0 ? "legR" : "legL").at(s * 2, -4.5f, 0.5f);
            leg.box(-0.5f, -4, -0.5f, 1, 4, 1).color(LEG).flat();
            leg.box(-1.5f, -4.5f, -1, 3, 1, 3).color(LEG).flat();
        }
        // flight flapping vs. grounded folded wings (phase 1 = grounded)
        m.proc((rig, c) -> {
            if (c.deathT >= 0) return;
            if (c.phase == 0) {
                float f = (float) Math.sin(c.age * 0.9f);
                rig.rot("wingL", 0, 0, f * 38); rig.rot("wingR", 0, 0, -f * 38);
                rig.rot("wingLtip", 0, 0, f * 18); rig.rot("wingRtip", 0, 0, -f * 18);
                rig.pos("body", 0, 4 + (float) Math.sin(c.age * 0.9f - 1.2f) * 0.8f, 0);
                rig.rot("legL", 25, 0, 0); rig.rot("legR", 25, 0, 0);
            } else {
                rig.rot("wingL", 0, 15, -80); rig.rot("wingR", 0, -15, 80);
                rig.rot("wingLtip", 0, 0, -60); rig.rot("wingRtip", 0, 0, 60);
                rig.pos("body", 0, -0.5f, 0);
            }
        });
        m.look("head", 50, 30);
        m.clip("idle", 30).loop()
            .rot("head", 0, 0, 0, 0).rot("head", 8, 0, 18, 6).rot("head", 10, 0, 18, 6).rot("head", 20, 0, -15, -5).rot("head", 30, 0, 0, 0)
            .rot("body", 0, 4, 0, 0).rot("body", 15, 8, 0, 0).rot("body", 30, 4, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 15, 10, 0, 0).rot("tail", 30, 0, 0, 0);
        m.clip("walk", 16).loop()
            .rot("body", 0, 15, 0, 0).rot("body", 8, 20, 0, 4).rot("body", 16, 15, 0, 0)
            .rot("legL", 0, 30, 0, 0).rot("legL", 8, -20, 0, 0).rot("legL", 16, 30, 0, 0)
            .rot("legR", 0, -20, 0, 0).rot("legR", 8, 30, 0, 0).rot("legR", 16, -20, 0, 0);
        // PECK x5: draw head back, then five rapid jabs forward
        Clip pk = m.clip("peck", 44)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1, -3).pos("body", 12, 0, 0, 4, Ease.IN_CUBIC).pos("body", 36, 0, 0, 4).pos("body", 44, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -15, 0, 0).rot("body", 12, 25, 0, 0).rot("body", 36, 25, 0, 0).rot("body", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -30, 0, 0)
            .rot("beakL", 0, 0, 0, 0)
            .pos("head", 0, 0, 0, 0).pos("head", 8, 0, 0, -2);
        for (int i = 0; i < 5; i++) {
            int t = 12 + i * 5;
            pk.rot("head", t, 30, (i % 2 == 0 ? 8 : -8), 0, Ease.IN_CUBIC).rot("head", t + 3, -10, 0, 0)
              .pos("head", t, 0, -1, 4, Ease.IN_CUBIC).pos("head", t + 3, 0, 0, -1)
              .rot("beakL", t - 1, -30, 0, 0).rot("beakL", t, 0, 0, 0);
        }
        pk.rot("head", 44, 0, 0, 0).pos("head", 44, 0, 0, 0);
        // CAW: rear up, wings spread wide, head thrown back, beak gaping
        m.clip("caw", 36)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 10, 0, 0).rot("body", 14, -25, 0, 0, Ease.OUT_BACK).rot("body", 28, -20, 0, 0).rot("body", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 3, 0, Ease.OUT_BACK).pos("body", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 15, 0, 0).rot("head", 14, -35, 0, 0, Ease.OUT_BACK).rot("head", 18, -30, 10, 0).rot("head", 22, -30, -10, 0).rot("head", 26, -30, 10, 0).rot("head", 36, 0, 0, 0)
            .rot("beakU", 0, 0, 0, 0).rot("beakU", 12, 0, 0, 0).rot("beakU", 14, -20, 0, 0).rot("beakU", 28, -20, 0, 0).rot("beakU", 32, 0, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 12, 0, 0, 0).rot("beakL", 14, 45, 0, 0).rot("beakL", 28, 45, 0, 0).rot("beakL", 32, 0, 0, 0)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 14, 0, 0, 45, Ease.OUT_BACK).rot("wingL", 28, 0, 0, 40).rot("wingL", 36, 0, 0, 0)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 14, 0, 0, -45, Ease.OUT_BACK).rot("wingR", 28, 0, 0, -40).rot("wingR", 36, 0, 0, 0)
            .rot("crest", 0, 0, 0, 0).rot("crest", 14, 30, 0, 0).rot("crest", 18, -10, 0, 0).rot("crest", 36, 0, 0, 0)
            .rot("tail", 0, 0, 0, 0).rot("tail", 14, -30, 0, 0).rot("tail", 36, 0, 0, 0);
        // SWOOP: climb high and back, fold wings, dive forward beak-first, pull up
        m.clip("swoop", 40)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 12, -8, Ease.OUT_CUBIC).pos("body", 20, 0, 0, 14, Ease.IN_CUBIC).pos("body", 26, 0, 4, 18).pos("body", 40, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 14, -30, 0, 0).rot("body", 20, 55, 0, 0, Ease.IN_CUBIC).rot("body", 26, -20, 0, 0).rot("body", 40, 0, 0, 0)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 14, 0, 0, 60).rot("wingL", 18, 0, -50, -10, Ease.IN_CUBIC).rot("wingL", 24, 0, -50, -10).rot("wingL", 30, 0, 0, 50).rot("wingL", 40, 0, 0, 0)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 14, 0, 0, -60).rot("wingR", 18, 0, 50, 10, Ease.IN_CUBIC).rot("wingR", 24, 0, 50, 10).rot("wingR", 30, 0, 0, -50).rot("wingR", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 10, 0, 0).rot("head", 20, -20, 0, 0).rot("head", 40, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 18, 60, 0, 0).rot("legL", 30, 0, 0, 0).rot("legR", 0, 0, 0, 0).rot("legR", 18, 60, 0, 0).rot("legR", 30, 0, 0, 0);
        // STUNNED (grounded): crash-bounce, dizzy head circles
        m.clip("stunned", 40)
            .pos("body", 0, 0, 6, 0).pos("body", 6, 0, 0, 0, Ease.IN_QUAD).pos("body", 9, 0, 2, 0).pos("body", 12, 0, 0, 0, Ease.OUT_BOUNCE).pos("body", 40, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 6, 20, 0, 15).rot("body", 12, 0, 0, -10).rot("body", 24, 0, 0, 8).rot("body", 34, 0, 0, -5).rot("body", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 10, 0, 20).rot("head", 18, 10, 25, 0).rot("head", 24, 10, 0, -20).rot("head", 30, 10, -25, 0).rot("head", 36, 10, 0, 20).rot("head", 40, 0, 0, 0)
            .rot("crest", 0, 0, 0, 0).rot("crest", 12, 0, 0, 40).rot("crest", 24, 0, 0, -40).rot("crest", 40, 0, 0, 0);
        // HEADBUTT (grounded): crouch, hop forward head-first
        m.clip("headbutt", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -2, -2).pos("body", 13, 0, 4, 8, Ease.OUT_QUAD).pos("body", 16, 0, 0, 10, Ease.IN_QUAD).pos("body", 30, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 25, 0, 0).rot("body", 14, 50, 0, 0, Ease.IN_CUBIC).rot("body", 18, 20, 0, 0).rot("body", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 20, 0, 0).rot("head", 14, 35, 0, 0).rot("head", 18, -15, 0, 0, Ease.OUT_BACK).rot("head", 30, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 8, -30, 0, 0).rot("legL", 13, 50, 0, 0).rot("legL", 18, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 8, -30, 0, 0).rot("legR", 13, 50, 0, 0).rot("legR", 18, 0, 0, 0);
        // GO AIRBORNE: frantic wing beats, lift off
        m.clip("fly", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, -2, 0).pos("body", 22, 0, 6, 0, Ease.OUT_CUBIC).pos("body", 30, 0, 4, 0)
            .rot("wingL", 0, 0, 80, 80).rot("wingL", 4, 0, 0, 60).rot("wingL", 7, 0, 0, 120).rot("wingL", 10, 0, 0, 60).rot("wingL", 13, 0, 0, 120).rot("wingL", 16, 0, 0, 60).rot("wingL", 19, 0, 0, 120).rot("wingL", 22, 0, 0, 80).rot("wingL", 30, 0, 0, 80)
            .rot("wingR", 0, 0, -80, -80).rot("wingR", 4, 0, 0, -60).rot("wingR", 7, 0, 0, -120).rot("wingR", 10, 0, 0, -60).rot("wingR", 13, 0, 0, -120).rot("wingR", 16, 0, 0, -60).rot("wingR", 19, 0, 0, -120).rot("wingR", 22, 0, 0, -80).rot("wingR", 30, 0, 0, -80)
            .rot("body", 0, 0, 0, 0).rot("body", 6, 20, 0, 0).rot("body", 22, -10, 0, 0).rot("body", 30, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 12, 40, 0, 0).rot("legR", 0, 0, 0, 0).rot("legR", 12, 40, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -20, 0, 10).rot("body", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, -10).rot("head", 10, 0, 0, 0)
            .rot("crest", 0, 0, 0, 0).rot("crest", 3, 40, 0, 0).rot("crest", 10, 0, 0, 0);
        m.clip("death", 30)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, 3, 0).pos("body", 18, 0, -5, 0, Ease.IN_QUAD).pos("body", 30, 0, -5, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 6, -20, 0, 0).rot("body", 18, 0, 0, 90, Ease.IN_QUAD).rot("body", 30, 0, 0, 90)
            .rot("wingL", 0, 0, 0, 0).rot("wingL", 10, 0, 0, 60).rot("wingL", 30, 0, 0, -20)
            .rot("wingR", 0, 0, 0, 0).rot("wingR", 10, 0, 0, -60).rot("wingR", 30, 0, 0, 20)
            .rot("head", 0, 0, 0, 0).rot("head", 18, 30, 0, 0).rot("head", 30, 30, 0, 0)
            .rot("beakL", 0, 0, 0, 0).rot("beakL", 18, 30, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 18, -60, 0, 0).rot("legR", 0, 0, 0, 0).rot("legR", 18, -70, 0, 0);
        return m;
    }

    static boolean isAttack(net.minecraft.world.damagesource.DamageSource src) { return src.getEntity() != null; }

    public static MobDef def() {
        return MobDef.of("byrd", "Byrd", "バード")
            .game(1, 2).size(0.8f, 1.0f).hp(28).speed(0.3f).style(Style.BIRD).egg(0x6b4c8c, 0xf0a030)
            .model(Byrd::model).flying().spawn(6, 2, 3, MobDef.Habitat.MOUNTAIN)
            .onSpawn(s -> s.setCount("flight", 3))
            .pattern(s -> {
                if (s.getPhase() == 1) return s.lastMove().equals("headbutt") ? "fly" : "headbutt";
                double r = s.rand().nextDouble();
                String last = s.lastMove();
                if (s.getTurn() == 0) return r < 0.375 ? "caw" : "peck";
                String pick = r < 0.5 ? "peck" : r < 0.8 ? "caw" : "swoop";
                if (pick.equals("peck") && s.repeatCount("peck") >= 2) pick = s.rand().nextBoolean() ? "caw" : "swoop";
                if (pick.equals(last) && !pick.equals("peck")) pick = "peck";
                if (pick.equals("peck") && s.repeatCount("peck") >= 2) pick = last.equals("caw") ? "swoop" : "caw";
                return pick;
            })
            .move(Move.of("peck", 44).intent(Intent.ATTACK, 1, 5).reach(2.2f)
                .atAll(new int[]{12, 17, 22, 27, 32}, Fx.melee(1, 2.6, 80)))
            .move(Move.of("caw", 36).intent(Intent.BUFF).noTarget()
                .sound(14, SoundEvents.PARROT_IMITATE_PHANTOM, 1.2f, 1.6f).at(15, Fx.strength(1)))
            .move(Move.of("swoop", 40).intent(Intent.ATTACK, 12, 1).reach(3.2f)
                .sound(14, SoundEvents.PHANTOM_SWOOP, 1f, 1.3f).at(16, Fx.dash(0.7, -0.1)).at(20, Fx.melee(12, 2.8, 90)))
            .move(Move.of("stunned", 40).intent(Intent.STUN).noTarget())
            .move(Move.of("headbutt", 30).intent(Intent.ATTACK, 3, 1).reach(2.0f)
                .at(12, Fx.dash(0.45, 0.25)).at(15, Fx.melee(3, 2.4, 90)))
            .move(Move.of("fly", 30).intent(Intent.BUFF).noTarget()
                .sound(4, SoundEvents.PARROT_FLY, 1.2f, 0.8f)
                .at(10, (s, t) -> { s.setPhase(0); s.setNoGravity(true); s.setDeltaMovement(0, 0.45, 0); s.hurtMarked = true; s.setCount("flight", 3); Fx.particles(s, ParticleTypes.CLOUD, 10, 0.5); }))
            // Flight: -50% attack damage, 1 stack lost per hit; at 0 -> grounded + stunned
            .onHurt((s, src, dmg) -> {
                if (s.getPhase() != 0 || !isAttack(src) || s.invulnerableTime > 10) return dmg;
                int f = s.count("flight") - 1;
                s.setCount("flight", f);
                if (f <= 0) {
                    s.setPhase(1); s.setNoGravity(false); s.cancelMove(); s.forceNext("stunned");
                    Fx.particles(s, ParticleTypes.CRIT, 12, 0.5); Fx.sound(s, SoundEvents.PARROT_HURT, 1.2f, 0.6f);
                }
                return dmg * 0.5f;
            })
            .onMoveEnd(s -> { if (s.getPhase() == 0) s.setCount("flight", 3); });
    }
}
