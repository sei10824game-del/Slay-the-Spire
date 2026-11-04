package com.spiremod.mobs.s2og2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth boss "The Kin": Kin Priest (190 HP) + two Kin Followers (see KinFollower).
 * Priest cycle: Orb of Frailty 8 + Frail -> Orb of Weakness 8 + Weak -> Soul Beam 3x3 -> Dark Ritual (+2 Strength).
 * Spawns its two followers on the first tick (the encounter is the three of them).
 */
public final class KinPriest {
    private KinPriest() {}

    static final int ROBE = 0x6a1f3a, ROBE_D = 0x3d1024, ROBE_L = 0x8c2e4e, GOLD = 0xd4a63a, BONE = 0xe6dcc0, BONE_D = 0xb3a684, SKIN = 0x4a3a52, EYE = 0xff5acd, ORB = 0xb34bff;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_kin_priest").scale(1.35f);
        // ---- layered robe, no visible legs
        Part skirt = m.add("skirt").at(0, 11, 0);
        skirt.box(-7, -11, -5, 14, 11, 10).color(ROBE_D).grad(ROBE, ROBE_D).bevel(0.1f).noise(0.1f);
        skirt.box(-7.6f, -11.5f, -5.6f, 15, 2, 11).color(GOLD).flat().noise(0.05f);
        skirt.box(-1, -10, 5.1f, 2, 9, 1).color(GOLD).flat();
        Part body = m.add("body").at(0, 11, 0);
        Part torso = body.add("torso").at(0, 0, 0);
        torso.box(-6, 0, -4, 12, 11, 8).color(ROBE).grad(ROBE_L, ROBE).bevel(0.12f).noise(0.1f)
            .glow(Face.FRONT, 5, 3, 2, 2, EYE).paint(Face.FRONT, 1, 8, 10, 1, GOLD);
        torso.box(-7, 8, -4.8f, 14, 3, 10).color(ROBE_D).grad(ROBE, ROBE_D).inflate(0.2f).noise(0.12f);      // mantle
        for (int i = 0; i < 5; i++) torso.box(-6 + i * 2.5f, 3, -5, 1, 7, 1).color(GOLD).flat();             // bead fringe
        // ---- head: pale bone mask under a tall horned hood
        Part head = torso.add("head").at(0, 11, 0);
        head.box(-4, 0, -4, 8, 8, 8).color(SKIN).bevel(0.12f);
        head.box(-4.6f, 3, -4.6f, 9, 6, 9).color(ROBE_D).grad(ROBE, ROBE_D).bevel(0.1f);                       // hood
        head.box(-4.6f, -0.5f, -4.6f, 3, 6, 9).color(ROBE_D).bevel(0.1f);
        head.box(1.6f, -0.5f, -4.6f, 3, 6, 9).color(ROBE_D).bevel(0.1f);
        head.box(-3.5f, 0.5f, 4.2f, 7, 7, 1).color(BONE).grad(BONE, BONE_D).bevel(0.2f)
            .glow(Face.FRONT, 1, 2, 2, 2, EYE).glow(Face.FRONT, 4, 2, 2, 2, EYE).paint(Face.FRONT, 3, 4, 1, 3, BONE_D).paint(Face.FRONT, 1, 5, 5, 1, 0x3a2030);
        Part crown = head.add("crown").at(0, 9, 0);
        crown.box(-3, 0, -3, 6, 3, 6).color(GOLD).flat().noise(0.08f);
        for (int i = -1; i <= 1; i++) crown.box(i * 2.3f - 0.5f, 3, -0.5f, 1, 6 - Math.abs(i) * 2, 1).color(GOLD).flat();
        for (int s = -1; s <= 1; s += 2) {                                                                      // bone antlers
            Part ant = head.add(s < 0 ? "antR" : "antL").at(s * 4, 7, -1).rot(0, 0, s * -30);
            ant.box(-0.7f, 0, -0.7f, 1, 7, 1).color(BONE).bevel(0.1f);
            Part tine = ant.add(s < 0 ? "tineR" : "tineL").at(0, 4, 0).rot(0, 0, s * -35);
            tine.box(-0.5f, 0, -0.5f, 1, 4, 1).color(BONE);
        }
        // ---- arms
        Part armR = torso.add("armR").at(-7.5f, 9, 0);
        armR.box(-2, -7, -2, 4, 7, 4).color(ROBE).bevel(0.1f);
        armR.box(-2.3f, -7.3f, -2.3f, 4, 2, 4).inflate(0.3f).color(GOLD).flat();
        Part handR = armR.add("handR").at(0, -7, 0);
        handR.box(-1.5f, -3, -1.5f, 3, 3, 3).color(SKIN);
        Part staff = handR.add("staff").at(0, -2, 0).rot(-90, 0, 0);
        staff.box(-0.7f, -12, -0.7f, 2, 30, 2).color(0x3a2a22).bevel(0.1f);
        staff.box(-2, 17, -2, 4, 4, 4).color(GOLD).flat();
        staff.box(-1.5f, 21, -1.5f, 3, 3, 3).color(ORB).flat().glow(Face.TOP, 0, 0, 3, 3, ORB);
        Part armL = torso.add("armL").at(7.5f, 9, 0);
        armL.box(-2, -7, -2, 4, 7, 4).color(ROBE).bevel(0.1f);
        armL.box(-2.3f, -7.3f, -2.3f, 4, 2, 4).inflate(0.3f).color(GOLD).flat();
        Part handL = armL.add("handL").at(0, -7, 0);
        handL.box(-1.5f, -3, -1.5f, 3, 3, 3).color(SKIN);
        for (int f = -1; f <= 1; f++) handL.box(f * 1.2f - 0.3f, -6, -0.5f, 1, 3, 1).color(SKIN).flat();      // clawed fingers
        Part orb = handL.add("orb").at(0, -4, 3);
        orb.box(-2.5f, -2.5f, -2.5f, 5, 5, 5).color(ORB).flat().glow(Face.FRONT, 1, 1, 3, 3, 0xf0d0ff).glow(Face.TOP, 1, 1, 3, 3, 0xf0d0ff);
        m.look("head", 50, 30);
        m.clip("walk", 24).loop()
            .rot("skirt", 0, 0, 3, 0).rot("skirt", 12, 0, -3, 0).rot("skirt", 24, 0, 3, 0)
            .rot("torso", 0, 5, 0, 3).rot("torso", 12, 5, 0, -3).rot("torso", 24, 5, 0, 3)
            .rot("armR", 0, 10, 0, 4).rot("armR", 12, -6, 0, 4).rot("armR", 24, 10, 0, 4);
        m.proc((rig, ctx) -> {
            rig.pos("body", 0, (float) Math.sin(ctx.age * 0.09f) * 1.2f, 0);
            rig.rot("orb", 0, ctx.age * 9, 0);
        });
        m.clip("idle", 50).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 25, -2, 3, 0).rot("torso", 50, 1.5f, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 25, 4, -8, 0).rot("head", 50, 0, 0, 0)
            .rot("armR", 0, 4, 0, 4).rot("armR", 25, 7, 0, 6).rot("armR", 50, 4, 0, 4)
            .rot("armL", 0, -30, 0, -6).rot("armL", 25, -34, 0, -8).rot("armL", 50, -30, 0, -6);
        // ORB (frailty/weakness): wind back, push the orb out with the clawed hand
        m.clip("orb", 34)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -10, 20, 0).rot("torso", 16, 14, -26, 0, Ease.IN_CUBIC).rot("torso", 34, 0, 0, 0)
            .rot("armL", 0, -30, 0, -6).rot("armL", 10, 20, 20, -40).rot("armL", 16, -100, -25, -8, Ease.IN_CUBIC).rot("armL", 24, -95, -25, -8).rot("armL", 34, -30, 0, -6)
            .rot("armR", 0, 4, 0, 4).rot("armR", 12, -45, 0, 30).rot("armR", 34, 4, 0, 4)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -12, 18, 0).rot("head", 16, 10, -20, 0).rot("head", 34, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -2).pos("body", 16, 0, 0, 3, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0);
        // SOUL BEAM: staff raised, head thrown back, eyes + staff fire a beam, three pulses
        m.clip("soul_beam", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -18, 0, 0, Ease.OUT_BACK).rot("torso", 36, -12, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("armR", 0, 4, 0, 4).rot("armR", 12, -150, 0, 20, Ease.OUT_BACK).rot("armR", 36, -150, 0, 20).rot("armR", 44, 4, 0, 4)
            .rot("armL", 0, -30, 0, -6).rot("armL", 12, -140, 0, -25).rot("armL", 36, -140, 0, -25).rot("armL", 44, -30, 0, -6)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -28, 0, 0).rot("head", 20, -24, 0, 0).rot("head", 24, -32, 0, 0).rot("head", 28, -24, 0, 0).rot("head", 32, -32, 0, 0).rot("head", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, 3, 0).pos("body", 36, 0, 3, 0).pos("body", 44, 0, 0, 0);
        // DARK RITUAL: both arms up, shudder, rock the robe
        m.clip("dark_ritual", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 14, 0, 0).rot("torso", 20, -16, 0, 0, Ease.OUT_BACK).rot("torso", 36, -12, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("armR", 0, 4, 0, 4).rot("armR", 10, 30, 0, 12).rot("armR", 20, -165, 0, 28, Ease.OUT_BACK).rot("armR", 36, -160, 0, 30).rot("armR", 44, 4, 0, 4)
            .rot("armL", 0, -30, 0, -6).rot("armL", 10, 30, 0, -12).rot("armL", 20, -165, 0, -28, Ease.OUT_BACK).rot("armL", 36, -160, 0, -30).rot("armL", 44, -30, 0, -6)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 22, 0, 0).rot("head", 20, -40, 0, 0, Ease.OUT_BACK).rot("head", 36, -35, 0, 0).rot("head", 44, 0, 0, 0)
            .rot("skirt", 0, 0, 0, 0).rot("skirt", 22, 0, 6, 0).rot("skirt", 26, 0, -6, 0).rot("skirt", 30, 0, 6, 0).rot("skirt", 34, 0, -6, 0).rot("skirt", 44, 0, 0, 0)
            .rot("crown", 0, 0, 0, 0).rot("crown", 20, -15, 0, 0).rot("crown", 44, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 18, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -22, 0, 10).rot("head", 10, 0, 0, 0);
        m.clip("death", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 22, 0, 0).rot("torso", 36, 45, 0, 0, Ease.IN_QUAD)
            .pos("body", 0, 0, 0, 0).pos("body", 36, 0, -9, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -130, 0, 40).rot("armR", 36, 20, 0, 60)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -100, 0, -40).rot("armL", 36, 20, 0, -60)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -35, 0, 0).rot("head", 36, 30, 10, 12)
            .scl("skirt", 0, 1, 1, 1).scl("skirt", 36, 1.25f, 0.5f, 1.25f, Ease.IN_QUAD);
        return m;
    }

    public static MobDef def() {
        ProjKind.orb("s2_kin_orb", ORB, 4).trail(ParticleTypes.WITCH);
        return MobDef.of("s2_kin_priest", "Kin Priest", "キンの司祭")
            .game(2, 1).boss().size(1.1f, 3.1f).hp(190).speed(0.22f).style(Style.MAGIC).egg(0x6a1f3a, 0xd4a63a)
            .model(KinPriest::model)
            .spawn(0, 1, 1, MobDef.Habitat.FOREST)
            .pattern(Pattern.cycle("orb_frailty", "orb_weakness", "soul_beam", "dark_ritual"))
            .move(Move.of("orb_frailty", 34).anim("orb").intent(Intent.ATTACK_DEBUFF, 8, 1).ranged(12)
                .sound(10, SoundEvents.EVOKER_PREPARE_ATTACK, 1.2f, 1.2f)
                .at(16, Fx.shoot("s2_kin_orb", 8, 1.0f, 0, 1.4, 0.8, v -> Fx.frail(v, 6))))
            .move(Move.of("orb_weakness", 34).anim("orb").intent(Intent.ATTACK_DEBUFF, 8, 1).ranged(12)
                .sound(10, SoundEvents.EVOKER_PREPARE_ATTACK, 1.2f, 0.9f)
                .at(16, Fx.shoot("s2_kin_orb", 8, 1.0f, 0, 1.4, 0.8, v -> Fx.weak(v, 6))))
            .move(Move.of("soul_beam", 44).intent(Intent.ATTACK, 3, 3).ranged(12).faceUntil(40)
                .sound(12, SoundEvents.WARDEN_SONIC_CHARGE, 1.3f, 1.3f)
                .atAll(new int[]{20, 26, 32}, (s, t) -> {
                    Fx.hitTarget(3).run(s, t);
                    if (t != null) Fx.particlesAt(s, ParticleTypes.SOUL_FIRE_FLAME, t.position().add(0, 1, 0), 14, 0.3, 0.05);
                    Fx.sound(s, SoundEvents.SOUL_ESCAPE, 1.2f, 1.4f);
                }))
            .move(Move.of("dark_ritual", 44).intent(Intent.BUFF).noTarget()
                .sound(18, SoundEvents.PARROT_IMITATE_WITCH, 1.5f, 0.6f)
                .at(22, (s, t) -> { s.addStrength(2); Fx.particles(s, ParticleTypes.WITCH, 40, 0.8); Bits.say(s, "KaQuaww!!"); }))
            .onTick(s -> {   // summon the two followers once (offset cycles: A starts Quick Slash, B starts Power Dance)
                if (s.tickCount > 5 && s.count("kin") == 0) {
                    s.setCount("kin", 1);
                    if (s.minions().isEmpty()) {
                        SpireMob a = Fx.summon(s, "s2_kin_follower", -2.6, 1.2), b = Fx.summon(s, "s2_kin_follower", 2.6, 1.2);
                        if (b != null) b.setVar(1);
                    }
                }
            });
    }
}
