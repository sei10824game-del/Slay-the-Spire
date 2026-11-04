package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 3 Exploder (30 HP). Expunge-style: Slam 9 on turns 1-2, turn 3 Explode (30) and dies. Rust-red bomb with glowing cracks. */
public final class Exploder {
    private Exploder() {}
    static final int SHELL = 0x7a3a2c, SHELL_D = 0x3a1a16, SHELL_L = 0xb0584a, LAVA = 0xffa030, IRON = 0x4a4a52;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("exploder");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3.5f, 5, 0);
            leg.box(-1.5f, -5, -1.5f, 3, 5, 3).color(IRON).bevel(0.1f);
            leg.box(-2, -5, -2.5f, 4, 2, 5).color(SHELL_D).bevel(0.1f);
        }
        Part body = m.add("body").at(0, 5, 0);
        Spiker.ball(body, 7, SHELL, SHELL_L, SHELL_D);
        body.box(-4, 7, -4, 8, 2, 8).color(IRON).flat().bevel(0.1f).inflate(0.1f);   // iron band top
        for (int i = 0; i < 4; i++) {                                                               // glowing cracks around the shell
            Part c = body.add("crack" + i).at(0, 0, 0).rot(0, i * 90, 0);
            c.box(-1, -4, 7.5f, 2, 8, 1).color(LAVA).flat().glow(Face.FRONT, 0, 0, 2, 8, LAVA);
            c.box(-4, 1, 7.5f, 3, 1, 1).color(LAVA).flat().glow(Face.FRONT, 0, 0, 3, 1, LAVA);
        }
        Part fuse = body.add("fuse").at(0, 8.5f, 0);
        fuse.box(-0.5f, 0, -0.5f, 1, 4, 1).color(0x2a1f18);
        Part spark = fuse.add("spark").at(0, 4, 0);
        spark.box(-1, 0, -1, 2, 2, 2).color(LAVA).flat().glow(Face.TOP, 0, 0, 2, 2, 0xffe070);
        Part eye = body.add("eye").at(0, 1, 7);
        eye.box(-4, -1.5f, 0, 8, 3, 1).color(0x120a08).flat().glow(Face.FRONT, 1, 1, 2, 1, LAVA).glow(Face.FRONT, 5, 1, 2, 1, LAVA);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 8, 2, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(IRON).bevel(0.1f);
            arm.box(-2, -9, -2, 4, 3, 4).color(SHELL_D).bevel(0.2f);
        }
        m.walkBiped("legR", "legL", "armR", "armL", 30f, 20f);
        m.proc((r, c) -> { float p = 1 + 0.05f * (float) Math.sin(c.age * (0.3f + 0.25f * c.var)); r.scl("spark", p, p, p); });
        m.clip("idle", 30).loop().pos("body", 0, 0, 0, 0).pos("body", 15, 0, -0.8f, 0).pos("body", 30, 0, 0, 0)
            .rot("fuse", 0, 0, 0, 8).rot("fuse", 15, 0, 0, -8).rot("fuse", 30, 0, 0, 8);
        m.clip("slam", 26)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -150, 0, 10).rot("armR", 12, -10, 0, 0, Ease.IN_CUBIC).rot("armR", 26, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, -150, 0, -10).rot("armL", 12, -10, 0, 0, Ease.IN_CUBIC).rot("armL", 26, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 9, -14, 0, 0).rot("body", 12, 20, 0, 0, Ease.IN_CUBIC).rot("body", 26, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, -1, -1).pos("body", 12, 0, -2, 3).pos("body", 26, 0, 0, 0);
        // EXPLODE: crouch, shake harder and harder, balloon, burst
        m.clip("explode", 40)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -2, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 0, 0, 6).rot("body", 13, 0, 0, -6).rot("body", 16, 0, 0, 8).rot("body", 19, 0, 0, -8).rot("body", 22, 0, 0, 10).rot("body", 25, 0, 0, -10).rot("body", 28, 0, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 10, 0.9f, 0.9f, 0.9f).scl("body", 28, 1.8f, 1.8f, 1.8f, Ease.IN_QUAD).scl("body", 40, 2.6f, 2.6f, 2.6f)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -60, 0, 40).rot("armR", 28, -100, 0, 60)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -60, 0, -40).rot("armL", 28, -100, 0, -60);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -15, 0, 8).rot("body", 10, 0, 0, 0);
        m.clip("death", 24).pos("body", 0, 0, 0, 0).pos("body", 24, 0, -5, 0, Ease.OUT_BOUNCE).rot("body", 0, 0, 0, 0).rot("body", 24, 70, 0, 20)
            .rot("armR", 0, 0, 0, 0).rot("armR", 24, 0, 0, 50).rot("armL", 0, 0, 0, 0).rot("armL", 24, 0, 0, -50);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("exploder", "Exploder", "エクスプローダー")
            .game(1, 3).size(1.1f, 1.5f).hp(30).speed(0.24f).style(Style.METAL).egg(0x7a3a2c, 0xffa030)
            .model(Exploder::model)
            .spawn(6, 1, 2, MobDef.Habitat.MOUNTAIN)
            .pattern(Pattern.seq(2, "slam", "slam", "explode"))
            .move(Move.of("slam", 26).intent(Intent.ATTACK, 9, 1).reach(2.4f)
                .sound(10, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.8f)
                .at(12, Fx.melee(9, 2.8, 90)))
            .move(Move.of("explode", 40).intent(Intent.UNKNOWN).reach(3.5f)
                .sound(4, SoundEvents.CREEPER_PRIMED, 1.2f, 0.9f)
                .at(30, (s, t) -> {
                    Fx.aoe(30, 3.5).run(s, t);
                    Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 1.2f, 1f);
                    Fx.particles(s, ParticleTypes.EXPLOSION, 4, 0.8);
                    Fx.particles(s, ParticleTypes.FLAME, 30, 0.8);
                    s.discard();
                }))
            .onTick(s -> { if (s.getTurn() >= 2) s.setVar(Math.min(3, s.getTurn() - 1)); });
    }
}
