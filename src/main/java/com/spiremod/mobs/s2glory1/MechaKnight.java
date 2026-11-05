package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory elite: Mecha Knight. 300 HP, Artifact 3. Opens with Charge (25), then loops Flamethrower (8 + 4 Burn) -> Windup (15 Block, +5 Str) -> Heavy Cleave (35). */
public final class MechaKnight {
    private MechaKnight() {}
    static final int STEEL = 0x8e969e, STEEL_D = 0x484e56, ORANGE = 0xd8741e, EYE = 0xff5020;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_mecha_knight");
        Kit.biped(m, 0x30363c, STEEL, STEEL_D, ORANGE);
        m.scale(2.0f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-5.5f, -0.5f, -3.5f, 11, 10, 7).color(STEEL).grad(0xb0b8c0, STEEL_D).bevel(0.25f);
        torso.box(-2.5f, 2, -3.7f, 5, 5, 1).color(0x201410).glow(Face.FRONT, 1, 1, 3, 3, 0xff9030);        // furnace
        torso.box(-5.8f, 0, -3.8f, 12, 2, 8).color(ORANGE).flat().noise(0.05f);
        for (int i = 0; i < 3; i++) torso.box(-3 + i * 3, 9.5f, 1, 2, 3, 2).color(STEEL_D).bevel(0.2f);    // exhaust pipes
        body.box(-5, -2, -3, 10, 5, 6).color(STEEL_D).bevel(0.2f);
        head.box(-3.5f, 0, -3.5f, 7, 6, 7).color(STEEL).grad(0xb0b8c0, STEEL_D).bevel(0.3f);               // helm
        head.box(-3, 2, 3.4f, 6, 1, 1).color(0x101010).glow(Face.FRONT, 0, 0, 6, 1, EYE);
        head.box(-0.5f, 6, -3, 1, 4, 6).color(ORANGE).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part pad = torso.add("pad" + s).at(s * 6.5f, 9, 0);
            pad.box(-3, -1, -3, 6, 4, 6).color(STEEL).bevel(0.3f); pad.box(-0.5f, 3, -0.5f, 1, 3, 1).color(ORANGE).flat();
            m.byName.get(s < 0 ? "armR" : "armL").box(-2, -6, -2, 4, 4, 4).color(STEEL_D).bevel(0.2f).inflate(0.1f);
        }
        Part blade = m.byName.get("handR").add("blade").at(0, -2, 0).rot(-80, 0, 0);
        blade.box(-1, -3, -1, 2, 6, 2).color(STEEL_D); blade.box(-2, 3, -1, 4, 1, 2).color(ORANGE).flat();
        blade.box(-2, 4, -0.5f, 4, 18, 1).color(0xd0d8e0).flat().noise(0.04f);
        Part nozzle = m.byName.get("handL").add("nozzle").at(0, -3, 0).rot(-80, 0, 0);
        nozzle.box(-2, -3, -2, 4, 6, 4).color(STEEL_D).bevel(0.25f); nozzle.box(-1.5f, 3, -1.5f, 3, 4, 3).color(0x2a2a2e).glow(Face.TOP, 0, 0, 3, 3, 0xffa030);
        Kit.bipedBase(m);
        m.proc((r, c) -> { float s = 1f + 0.02f * (float) Math.sin(c.age * 0.5f); r.scl("torso", s, s, s); });
        Clip ch = m.clip("charge", 34);
        Kit.seq(ch, "torso", 34, 14, 8, -12, 0, 0, 14, 40, 0, 0, 26, 28, 0, 0);
        Kit.seq(ch, "armR", 34, 14, 8, 30, 0, 20, 14, -120, 0, -10, 26, -100, 0, -10);
        Kit.seq(ch, "armL", 34, 14, 8, 30, 0, -20, 14, -50, 0, -30);
        Kit.seq(ch, "legR", 34, -1, 8, 25, 0, 0, 14, -45, 0, 0, 20, 30, 0, 0);
        Kit.seq(ch, "legL", 34, -1, 8, -20, 0, 0, 14, 40, 0, 0, 20, -30, 0, 0);
        Kit.sp(ch, "body", 34, 14, 8, 0, -3, -3, 14, 0, -1, 9, 26, 0, 0, 6);
        Clip fl = Kit.cast(m, "flamethrower", 44, 12);
        fl.tracks.remove("armR");
        Kit.seq(fl, "armL", 44, -1, 6, -40, -10, -15, 12, -95, 0, -5, 32, -95, 0, -5);
        Kit.seq(fl, "torso", 44, -1, 6, 8, 0, 0, 12, -8, 12, 0, 22, -8, -12, 0, 32, -8, 0, 0);
        Clip w = m.clip("windup", 40);
        Kit.seq(w, "torso", 40, -1, 12, 14, 0, 0, 24, -16, 0, 0, 32, -10, 0, 0);
        Kit.seq(w, "armR", 40, -1, 12, 20, 0, 40, 24, -150, 0, 50, 32, -140, 0, 50);
        Kit.seq(w, "armL", 40, -1, 12, 20, 0, -40, 24, -150, 0, -50, 32, -140, 0, -50);
        Kit.seq(w, "head", 40, -1, 12, 20, 0, 0, 24, -35, 0, 0);
        w.scl("torso", 0, 1, 1, 1).scl("torso", 24, 1.12f, 1.12f, 1.12f, Ease.OUT_BACK).scl("torso", 40, 1, 1, 1);
        Kit.overhead(m, "heavy_cleave", 42, 22);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_mecha_knight", "Mecha Knight", "メカナイト")
            .game(2, 3).size(1.5f, 4.0f).hp(300).speed(0.2f).style(Style.METAL).egg(0x8e969e, 0xd8741e).elite().kbResist(0.7f)
            .model(MechaKnight::model)
            .spawn(1, 1, 1, MobDef.Habitat.MOUNTAIN)
            .pattern(Pattern.seq(1, "charge", "flamethrower", "windup", "heavy_cleave"))
            .move(Move.of("charge", 34).intent(Intent.ATTACK, 25, 1).reach(8f).moving()
                .sound(8, SoundEvents.IRON_GOLEM_STEP, 1.2f, 0.5f)
                .at(12, Fx.dash(0.8, 0.05)).at(14, Fx.melee(25, 4.0, 110)))
            .move(Move.of("flamethrower", 44).intent(Intent.ATTACK_DEBUFF, 8, 1).ranged(8)
                .sound(12, SoundEvents.BLAZE_SHOOT, 1.2f, 0.6f)
                .at(12, (s, t) -> { Fx.hitTarget(8, v -> Fx.curse(v, Fx.Curse.BURN)).run(s, t); })
                .atAll(new int[] {12, 15, 18, 21, 24, 27, 30}, (s, t) -> {
                    for (int i = 1; i <= 4; i++) Fx.particlesAt(s, ParticleTypes.FLAME, Fx.rel(s, 1.0 + i * 1.0, 0.3, 2.3), 4, 0.25, 0.04);
                }))
            .move(Move.of("windup", 40).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(16, SoundEvents.ANVIL_USE, 1f, 0.5f)
                .at(24, (s, t) -> { Kit.block(s, 15); Kit.str(s, 5); Fx.particles(s, ParticleTypes.LAVA, 12, 0.8); }))
            .move(Move.of("heavy_cleave", 42).intent(Intent.ATTACK, 35, 1).reach(4.0f)
                .sound(20, SoundEvents.IRON_GOLEM_ATTACK, 1.4f, 0.5f)
                .at(22, Fx.melee(35, 4.2, 120)));
    }
}
