package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Mawler (72 HP). Opens Claw; then random Rip and Tear 14 / Roar (3 Vulnerable, once per fight) / Claw 4x2, never the same move twice. */
public final class Mawler {
    private Mawler() {}

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_mawler");
        Part body = m.add("body").at(0, 11, 0);
        body.box(-7, -6, -9, 14, 12, 18).color(0x6a8a3a).grad(0x8aaa50, 0x3a4a1c).bevel(0.3f).noise(0.12f);
        body.box(-6, -7, -8, 12, 1, 16).color(0xd8c890);
        Part head = body.add("head").at(0, 1, 9);
        Part jawU = head.add("jawU").at(0, 2, 0);
        jawU.box(-6, 0, 0, 12, 5, 9).color(0x4a6a2a).grad(0x6a8a3a, 0x3a4a1c).bevel(0.25f);
        Part jawL = head.add("jawL").at(0, 1, 0);
        jawL.box(-5.5f, -4, 0, 11, 4, 8).color(0x3a4a1c).bevel(0.25f);
        for (int i = 0; i < 5; i++) { jawU.box(-5 + i * 2.5f, -2, 7.5f, 1, 3, 1).color(0xf0f0e0); jawL.box(-5 + i * 2.5f, 0, 6.5f, 1, 3, 1).color(0xf0f0e0); }
        jawU.box(-5, 2, 8.5f, 3, 2, 1).color(0xffe040).glow(Face.FRONT, 0, 0, 3, 2, 0xffe040);
        jawU.box(2, 2, 8.5f, 3, 2, 1).color(0xffe040).glow(Face.FRONT, 0, 0, 3, 2, 0xffe040);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 8, 3, 3);
            arm.box(-2, -9, -2, 4, 9, 4).color(0x4a6a2a).bevel(0.2f);
            for (int c = 0; c < 3; c++) arm.box(-1.5f + c * 1.2f, -12, 0, 1, 3, 1).color(0xf0f0e0);
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 4.5f, 5, -4);
            leg.box(-2, -5, -2, 4, 5, 4).color(0x3a4a1c).bevel(0.2f);
        }
        m.walkBiped("legR", "legL", "armR", "armL", 30f, 20f);
        m.look("head", 25, 12);
        Clip i = m.clip("idle", 40).loop();
        i.rot("jawL", 0, 0, 0, 0).rot("jawL", 20, 8, 0, 0).rot("jawL", 40, 0, 0, 0);
        i.pos("body", 0, 0, 0, 0).pos("body", 20, 0, 0.7f, 0).pos("body", 40, 0, 0, 0);
        Clip h = m.clip("hurt", 10);
        h.rot("body", 0, 0, 0, 0).rot("body", 3, 14, 0, 6).rot("body", 10, 0, 0, 0);
        h.rot("head", 0, 0, 0, 0).rot("head", 3, -20, 0, 0).rot("head", 10, 0, 0, 0);
        Clip d = m.clip("death", 30);
        d.rot("body", 0, 0, 0, 0).rot("body", 10, -14, 0, 8).rot("body", 24, 0, 0, 90, Ease.OUT_BOUNCE).rot("body", 30, 0, 0, 90);
        d.pos("body", 0, 0, 0, 0).pos("body", 24, 0, -5, 0).pos("body", 30, 0, -5, 0);
        d.rot("jawL", 0, 0, 0, 0).rot("jawL", 16, 30, 0, 0).rot("jawL", 30, 30, 0, 0);
        Clip c = m.clip("claw", 30);
        c.rot("armR", 0, 0, 0, 0).rot("armR", 7, -150, 0, -20).rot("armR", 11, 30, 0, 10, Ease.IN_CUBIC).rot("armR", 14, -10, 0, 0).rot("armR", 30, 0, 0, 0);
        c.rot("armL", 0, 0, 0, 0).rot("armL", 15, -150, 0, 20).rot("armL", 19, 30, 0, -10, Ease.IN_CUBIC).rot("armL", 22, -10, 0, 0).rot("armL", 30, 0, 0, 0);
        c.rot("body", 0, 0, 0, 0).rot("body", 7, -10, 6, 0).rot("body", 11, 12, -6, 0).rot("body", 15, -10, -6, 0).rot("body", 19, 12, 6, 0).rot("body", 30, 0, 0, 0);
        Clip r = m.clip("rip_and_tear", 40);
        r.rot("body", 0, 0, 0, 0).rot("body", 12, -28, 0, 0).rot("body", 17, 30, 0, 0, Ease.IN_CUBIC).rot("body", 21, 18, 12, 0).rot("body", 25, 18, -12, 0).rot("body", 40, 0, 0, 0);
        r.rot("jawL", 0, 0, 0, 0).rot("jawL", 12, 48, 0, 0).rot("jawL", 17, -4, 0, 0, Ease.IN_CUBIC).rot("jawL", 40, 0, 0, 0);
        r.rot("jawU", 0, 0, 0, 0).rot("jawU", 12, -22, 0, 0).rot("jawU", 17, 4, 0, 0, Ease.IN_CUBIC).rot("jawU", 40, 0, 0, 0);
        r.rot("armR", 0, 0, 0, 0).rot("armR", 12, -70, 0, -30).rot("armR", 40, 0, 0, 0);
        r.rot("armL", 0, 0, 0, 0).rot("armL", 12, -70, 0, 30).rot("armL", 40, 0, 0, 0);
        Clip ro = m.clip("roar", 38);
        ro.rot("body", 0, 0, 0, 0).rot("body", 10, -24, 0, 0, Ease.OUT_BACK).rot("body", 28, -24, 0, 0).rot("body", 38, 0, 0, 0);
        ro.rot("head", 0, 0, 0, 0).rot("head", 10, -22, 0, 0).rot("head", 28, -22, 0, 0).rot("head", 38, 0, 0, 0);
        ro.rot("jawL", 0, 0, 0, 0).rot("jawL", 10, 55, 0, 0).rot("jawL", 28, 55, 0, 0).rot("jawL", 38, 0, 0, 0);
        ro.scl("body", 0, 1, 1, 1).scl("body", 12, 1.14f, 1.14f, 1.14f).scl("body", 28, 1.08f, 1.08f, 1.08f).scl("body", 38, 1, 1, 1);
        ro.rot("armR", 0, 0, 0, 0).rot("armR", 10, -40, 0, -50).rot("armR", 38, 0, 0, 0);
        ro.rot("armL", 0, 0, 0, 0).rot("armL", 10, -40, 0, 50).rot("armL", 38, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_mawler", "Mawler", "マウラー")
            .game(2, 1).size(1.2f, 1.3f).hp(72).speed(0.27f).style(Style.BEAST).egg(0x6a8a3a, 0xf0f0e0)
            .model(Mawler::model).spawn(4, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(s -> {
                String l = s.lastMove();
                if (l.isEmpty()) return "claw";
                java.util.List<String> o = new java.util.ArrayList<>(java.util.List.of("rip_and_tear", "claw"));
                if (!s.flags.contains("roared")) o.add("roar");
                o.remove(l);
                return o.get(s.rand().nextInt(o.size()));
            })
            .move(Move.of("claw", 30).intent(Intent.ATTACK, 4, 2).reach(2.8f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.8f).sound(16, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.8f)
                .at(10, Fx.melee(4, 3.0, 100)).at(18, Fx.melee(4, 3.0, 100)))
            .move(Move.of("rip_and_tear", 40).intent(Intent.ATTACK, 14, 1).reach(3f)
                .sound(14, SoundEvents.RAVAGER_ATTACK, 1f, 0.8f).at(10, Fx.dash(0.5, 0.1)).at(18, Fx.melee(14, 3.2, 90)))
            .move(Move.of("roar", 38).intent(Intent.DEBUFF).noTarget()
                .sound(8, SoundEvents.RAVAGER_ROAR, 1.3f, 0.7f)
                .at(10, (s, t) -> { s.flags.add("roared"); Fx.ring(s, ParticleTypes.CLOUD, s.position(), 4, 24);
                    for (var v : Fx.foes(s, s.getBoundingBox().inflate(10))) Fx.vulnerable(v, 8); }));
    }
}
