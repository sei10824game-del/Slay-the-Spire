package com.spiremod.mobs.s1act1a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 1 Fungi Beast: 22-28 HP. Bite 6 / Grow +3 Str (60/40, no 3x Bite, no 2x Grow). Spore Cloud: Vulnerable on death. */
public final class FungiBeast {
    private FungiBeast() {}

    static final int FUR = 0x6b5a4a, FUR_D = 0x40352b, FUR_L = 0x8a7660, CAP = 0x5aa0a8, CAP_D = 0x2f6470, SPOT = 0xcfe8a0, TAIL = 0x8c6a5a, TOOTH = 0xe8e0c0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("fungi_beast");
        for (int i = 0; i < 4; i++) {
            boolean front = i < 2; int s = (i % 2 == 0) ? -1 : 1;
            Part leg = m.add((front ? "f" : "b") + (s < 0 ? "R" : "L")).at(s * 3.5f, 4, front ? 4 : -4);
            leg.box(-1, -4, -1, 2, 4, 2).color(FUR_D).noise(0.1f);
            leg.box(-1.2f, -4, -1.6f, 2, 1, 3).color(TOOTH).flat();
        }
        Part body = m.add("body").at(0, 4, 0);
        body.box(-4, 0, -6, 8, 6, 12).color(FUR).grad(FUR_L, FUR_D).noise(0.12f).bevel(0.2f);
        body.box(-3.5f, -0.5f, -5, 7, 1, 10).color(0xc0a888);                       // belly
        // fungus growth on the back
        Part cap1 = body.add("cap1").at(-1.5f, 6, -2);
        cap1.box(-0.7f, 0, -0.7f, 1, 2, 1).color(0xd8d0b0);
        cap1.box(-3, 2, -3, 6, 2, 6).color(CAP).grad(CAP, CAP_D).bevel(0.3f).glow(Face.TOP, 1, 1, 1, 1, SPOT).glow(Face.TOP, 4, 3, 1, 1, SPOT);
        Part cap2 = body.add("cap2").at(2.2f, 6, 2.5f).rot(0, 0, 12);
        cap2.box(-0.5f, 0, -0.5f, 1, 1, 1).color(0xd8d0b0);
        cap2.box(-2, 1, -2, 4, 2, 4).color(CAP).grad(CAP, CAP_D).bevel(0.3f).glow(Face.TOP, 1, 1, 1, 1, SPOT);
        Part cap3 = body.add("cap3").at(2.5f, 5.5f, -5).rot(0, 0, 20);
        cap3.box(-1.5f, 0, -1.5f, 3, 2, 3).color(CAP_D).bevel(0.3f).glow(Face.TOP, 1, 0, 1, 1, SPOT);
        body.box(-3.9f, 4, 1, 3, 2, 4).color(CAP_D).noise(0.2f);                    // fungal patch
        // head
        Part head = body.add("head").at(0, 3, 6);
        head.box(-3, -2.5f, 0, 6, 5, 5).color(FUR).grad(FUR_L, FUR_D).noise(0.1f).bevel(0.2f);
        head.box(-1.5f, -2, 5, 3, 3, 2).color(FUR_L).bevel(0.2f).px(Face.FRONT, 0, 0, 0x2a1c18).px(Face.FRONT, 2, 0, 0x2a1c18);   // snout
        head.box(-2, 2.5f, 0.5f, 4, 1, 3).color(CAP).flat().glow(Face.TOP, 1, 0, 1, 1, SPOT);                                     // sprouting cap
        for (int s = -1; s <= 1; s += 2) {
            Part ear = head.add(s < 0 ? "earR" : "earL").at(s * 2.5f, 2.5f, 1).rot(0, 0, s * -20);
            ear.box(-1, 0, -0.5f, 2, 2, 1).color(FUR_D).bevel(0.1f);
        }
        head.box(-2.6f, 0.2f, 4.9f, 1, 1, 1).color(0xffe070).flat().glow(Face.FRONT, 0, 0, 1, 1, 0xffe070);
        head.box(1.6f, 0.2f, 4.9f, 1, 1, 1).color(0xffe070).flat().glow(Face.FRONT, 0, 0, 1, 1, 0xffe070);
        Part jaw = head.add("jaw").at(0, -2, 5);
        jaw.box(-1.5f, -1, -2, 3, 1, 3).color(TOOTH).bevel(0.1f);
        // two tails
        for (int s = -1; s <= 1; s += 2) {
            Part t1 = body.add(s < 0 ? "tailR" : "tailL").at(s * 1.5f, 3, -6).rot(-25, s * 20, 0);
            t1.box(-0.5f, -0.5f, -4, 1, 1, 4).color(TAIL).noise(0.1f);
            Part t2 = t1.add(s < 0 ? "tail2R" : "tail2L").at(0, 0, -4).rot(-20, 0, 0);
            t2.box(-0.4f, -0.4f, -4, 1, 1, 4).color(TAIL);
            t2.box(-0.8f, -0.8f, -5, 2, 2, 1).color(CAP).glow(Face.BACK, 0, 0, 1, 1, SPOT);
        }
        m.look("head", 35, 20);
        m.walkQuad("fR", "fL", "bR", "bL", 35f);
        m.clip("idle", 40).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, 0.4f, 0).pos("body", 40, 0, 0, 0)
            .rot("tailR", 0, 0, 0, 0).rot("tailR", 20, 0, 15, 0).rot("tailR", 40, 0, 0, 0)
            .rot("tailL", 0, 0, 0, 0).rot("tailL", 20, 0, -15, 0).rot("tailL", 40, 0, 0, 0)
            .rot("cap1", 0, 0, 0, 0).rot("cap1", 20, 0, 0, 4).rot("cap1", 40, 0, 0, 0)
            .rot("earR", 0, 0, 0, 0).rot("earR", 28, 0, 0, 0).rot("earR", 31, 0, 0, -20).rot("earR", 34, 0, 0, 0);
        // BITE: rear up, lunge, snap
        m.clip("bite", 24)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -22, 0, 0).rot("body", 12, 14, 0, 0, Ease.IN_CUBIC).rot("body", 24, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 1, -2).pos("body", 12, 0, -0.5f, 4, Ease.IN_CUBIC).pos("body", 24, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -15, 0, 0).rot("head", 12, 25, 0, 0, Ease.IN_CUBIC).rot("head", 24, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 35, 0, 0).rot("jaw", 12, -5, 0, 0, Ease.IN_CUBIC).rot("jaw", 14, 20, 0, 0).rot("jaw", 24, 0, 0, 0)
            .rot("tailR", 0, 0, 0, 0).rot("tailR", 12, 20, 25, 0).rot("tailR", 24, 0, 0, 0)
            .rot("tailL", 0, 0, 0, 0).rot("tailL", 12, 20, -25, 0).rot("tailL", 24, 0, 0, 0);
        // GROW: puff up, caps swell, shudder
        m.clip("grow", 34)
            .scl("body", 0, 1, 1, 1).scl("body", 8, 1.1f, 0.8f, 1.05f).scl("body", 16, 0.95f, 1.25f, 0.98f, Ease.OUT_BACK).scl("body", 22, 1.12f, 1.12f, 1.12f).scl("body", 34, 1, 1, 1)
            .scl("cap1", 0, 1, 1, 1).scl("cap1", 16, 1.8f, 1.6f, 1.8f, Ease.OUT_BACK).scl("cap1", 34, 1, 1, 1)
            .scl("cap2", 0, 1, 1, 1).scl("cap2", 18, 1.7f, 1.5f, 1.7f, Ease.OUT_BACK).scl("cap2", 34, 1, 1, 1)
            .scl("cap3", 0, 1, 1, 1).scl("cap3", 20, 1.7f, 1.5f, 1.7f, Ease.OUT_BACK).scl("cap3", 34, 1, 1, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -30, 0, 0, Ease.OUT_BACK).rot("head", 19, -30, 8, 0).rot("head", 23, -30, -8, 0).rot("head", 34, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 30, 0, 0).rot("jaw", 28, 30, 0, 0).rot("jaw", 34, 0, 0, 0)
            .rot("tailR", 0, 0, 0, 0).rot("tailR", 16, -30, 30, 0).rot("tailR", 34, 0, 0, 0)
            .rot("tailL", 0, 0, 0, 0).rot("tailL", 16, -30, -30, 0).rot("tailL", 34, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, 12, 0, 6).rot("body", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 0, 0).rot("head", 10, 0, 0, 0);
        m.clip("death", 26)
            .scl("body", 0, 1, 1, 1).scl("body", 10, 1.2f, 0.7f, 1.2f, Ease.OUT_QUAD).scl("body", 14, 1.4f, 0.5f, 1.4f).scl("body", 26, 1.3f, 0.45f, 1.3f)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 25, 0, 10).rot("head", 26, 30, 0, 12)
            .scl("cap1", 0, 1, 1, 1).scl("cap1", 10, 2, 1.8f, 2).scl("cap1", 14, 0.2f, 0.2f, 0.2f)
            .scl("cap2", 0, 1, 1, 1).scl("cap2", 10, 2, 1.8f, 2).scl("cap2", 14, 0.2f, 0.2f, 0.2f)
            .scl("cap3", 0, 1, 1, 1).scl("cap3", 10, 2, 1.8f, 2).scl("cap3", 14, 0.2f, 0.2f, 0.2f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("fungi_beast", "Fungi Beast", "キノコビースト")
            .game(1, 1).size(0.9f, 0.7f).hp(25).speed(0.26f).style(Style.BEAST).egg(0x6b5a4a, 0x5aa0a8)
            .model(FungiBeast::model)
            .spawn(8, 1, 3, MobDef.Habitat.FOREST)
            .pattern(Pattern.random().add("bite", 60).add("grow", 40).maxRepeat("bite", 2).maxRepeat("grow", 1))
            .move(Move.of("bite", 24).intent(Intent.ATTACK, 6, 1).reach(2.0f)
                .sound(8, SoundEvents.FOX_AGGRO, 0.8f, 0.9f)
                .at(12, Fx.melee(6, 2.6, 80)))
            .move(Move.of("grow", 34).intent(Intent.BUFF).noTarget()
                .at(16, (s, t) -> { s.addStrength(3); Fx.sound(s, SoundEvents.SLIME_SQUISH, 1f, 0.6f); Fx.particles(s, ParticleTypes.SPORE_BLOSSOM_AIR, 20, 0.6); }))
            .onDeath((s, src) -> { // Spore Cloud
                Fx.particles(s, ParticleTypes.SPORE_BLOSSOM_AIR, 40, 1.2);
                for (var e : Fx.foes(s, s.getBoundingBox().inflate(3, 1.5, 3))) Fx.vulnerable(e, 5);
            });
    }
}
