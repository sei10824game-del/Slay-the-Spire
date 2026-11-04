package com.spiremod.mobs.s2ud2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Underdocks ELITE: Skulking Colony (75 HP, Hardened Shell 20: cannot lose more than 20 HP per turn). Zoom 14, Zoom 14, Inertia 9 + 2 Str, Piercing Stabs 7x2. */
public final class SkulkingColony {
    private SkulkingColony() {}
    static final int COR = 0xc95a78, COR_D = 0x7a2c48, TIP = 0xf0b0b8, ROCK = 0x5d5a6a, ROCK_D = 0x35333f, EYE = 0xffe070, LEG = 0x8c4a5e;
    static final String[] CYC = {"zoom", "zoom", "inertia", "stabs"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_skulking_colony");
        m.scale(1.25f);
        // eight crab-like coral legs
        for (int i = 0; i < 4; i++) for (int s = -1; s <= 1; s += 2) {
            Part l = m.add("leg" + (s < 0 ? "R" : "L") + i).at(s * 7, 9, -6 + i * 4).rot(0, 0, s * -10);
            l.box(s < 0 ? -7 : 0, -1, -1, 7, 2, 2).color(LEG).noise(0.1f).bevel(0.1f);
            Part lo = l.add("lowR" + i + s).at(s * 7, 0, 0);
            lo.box(-1, -9, -1, 2, 9, 2).color(COR_D).bevel(0.1f);
        }
        Part body = m.add("body").at(0, 9, 0);
        body.box(-7, 0, -8, 14, 7, 16).color(ROCK).grad(ROCK, ROCK_D).noise(0.2f).bevel(0.15f);
        // coral colony on top: lots of spikes
        int[][] sp = {{-5,7,-6,3,6},{-1,7,-5,3,9},{3,7,-6,3,5},{-4,7,0,3,7},{1,7,1,3,10},{4,7,3,2,6},{-6,7,4,2,5},{-2,7,5,3,6},{5,7,-1,2,8}};
        for (int i = 0; i < sp.length; i++) {
            int[] a = sp[i];
            Part c = body.add("coral" + i).at(a[0], a[1], a[2]).rot(i % 2 == 0 ? 8 : -8, 0, (i % 3 - 1) * 10);
            c.box(0, 0, 0, a[3], a[4], a[3]).color(COR).grad(COR_D, COR).noise(0.12f).bevel(0.12f);
            c.box(0.5f, a[4], 0.5f, a[3] - 1, 3, a[3] - 1).color(TIP).flat().glow(Face.TOP, 0, 0, 1, 1, 0xffd0d8);
        }
        // sleek head-ish front cluster with glowing eyes
        Part head = body.add("head").at(0, 4, 8);
        head.box(-5, -4, 0, 10, 8, 5).color(ROCK).grad(ROCK, ROCK_D).noise(0.15f).bevel(0.15f)
            .glow(Face.FRONT, 1, 2, 2, 2, EYE).glow(Face.FRONT, 7, 2, 2, 2, EYE).paint(Face.FRONT, 3, 6, 4, 1, 0x15131c);
        for (int s = -1; s <= 1; s += 2) {
            Part claw = body.add(s < 0 ? "clawR" : "clawL").at(s * 8, 3, 6);
            claw.box(-1.5f, -1.5f, 0, 3, 3, 8).color(COR).noise(0.1f).bevel(0.1f);
            claw.box(-1, -1, 8, 2, 2, 4).color(TIP).flat();
        }
        m.walkQuad("legR0", "legL0", "legR3", "legL3", 18f);
        m.clip("idle", 50).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 25, 0, 0.8f, 0).pos("body", 50, 0, 0, 0)
            .rot("coral1", 0, 8, 0, 0).rot("coral1", 25, 14, 0, 4).rot("coral1", 50, 8, 0, 0)
            .rot("coral4", 0, -8, 0, 5).rot("coral4", 25, -14, 0, -3).rot("coral4", 50, -8, 0, 5)
            .rot("clawR", 0, 0, 0, 0).rot("clawR", 25, 0, 10, 0).rot("clawR", 50, 0, 0, 0).rot("clawL", 0, 0, 0, 0).rot("clawL", 25, 0, -10, 0).rot("clawL", 50, 0, 0, 0);
        m.clip("walk", 16).loop();
        // ZOOM: skitter back, then a blur of a charge, body low
        m.clip("zoom", 26)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 3, -5).pos("body", 12, 0, -3, 14, Ease.IN_CUBIC).pos("body", 18, 0, -3, 14).pos("body", 26, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -15, 0, 0).rot("body", 12, 20, 0, 0, Ease.IN_CUBIC).rot("body", 26, 0, 0, 0)
            .rot("clawR", 0, 0, 0, 0).rot("clawR", 8, -30, -30, 0).rot("clawR", 12, 0, 20, 0, Ease.IN_CUBIC).rot("clawR", 26, 0, 0, 0)
            .rot("clawL", 0, 0, 0, 0).rot("clawL", 8, -30, 30, 0).rot("clawL", 12, 0, -20, 0, Ease.IN_CUBIC).rot("clawL", 26, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 15, 0, 0).rot("head", 26, 0, 0, 0);
        // INERTIA: gather momentum, spin on the spot (corals splay), buff pulse
        m.clip("inertia", 34)
            .rot("body", 0, 0, 0, 0).rot("body", 10, 0, 120, 0).rot("body", 20, 0, 360, 0, Ease.OUT_QUAD).rot("body", 34, 0, 360, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -3, 0).pos("body", 22, 0, 1, 0, Ease.OUT_BACK).pos("body", 34, 0, 0, 0)
            .rot("clawR", 0, 0, 0, 0).rot("clawR", 10, 0, 70, 0).rot("clawR", 20, 0, -20, 0).rot("clawR", 34, 0, 0, 0)
            .rot("clawL", 0, 0, 0, 0).rot("clawL", 10, 0, -70, 0).rot("clawL", 20, 0, 20, 0).rot("clawL", 34, 0, 0, 0)
            .rot("coral2", 0, 0, 0, 0).rot("coral2", 18, 25, 0, 20).rot("coral2", 34, 0, 0, 0)
            .rot("coral5", 0, 0, 0, 0).rot("coral5", 18, 25, 0, -20).rot("coral5", 34, 0, 0, 0);
        // PIERCING STABS: both claws jab twice
        m.clip("stabs", 32)
            .rot("clawR", 0, 0, 0, 0).rot("clawR", 6, -35, -25, 0).rot("clawR", 9, 15, 20, 0, Ease.IN_CUBIC).rot("clawR", 17, 0, 0, 0).rot("clawR", 21, -35, -25, 0).rot("clawR", 24, 15, 20, 0, Ease.IN_CUBIC).rot("clawR", 32, 0, 0, 0)
            .rot("clawL", 0, 0, 0, 0).rot("clawL", 10, -35, 25, 0).rot("clawL", 13, 15, -20, 0, Ease.IN_CUBIC).rot("clawL", 21, 0, 0, 0).rot("clawL", 25, -35, 25, 0).rot("clawL", 28, 15, -20, 0, Ease.IN_CUBIC).rot("clawL", 32, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, 3, Ease.IN_CUBIC).pos("body", 17, 0, 0, 0).pos("body", 24, 0, 0, 3, Ease.IN_CUBIC).pos("body", 32, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 9, 8, 0, 0).rot("body", 17, 0, 0, 0).rot("body", 24, 8, 0, 0).rot("body", 32, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -10, 0, 6).rot("body", 10, 0, 0, 0).pos("body", 0, 0, 0, 0).pos("body", 3, 0, 0, -2).pos("body", 10, 0, 0, 0);
        m.clip("death", 36)
            .pos("body", 0, 0, 0, 0).pos("body", 24, 0, -5, 0, Ease.OUT_BOUNCE).rot("body", 0, 0, 0, 0).rot("body", 24, 0, 0, 20)
            .rot("clawR", 0, 0, 0, 0).rot("clawR", 24, 0, 60, 0).rot("clawL", 0, 0, 0, 0).rot("clawL", 24, 0, -60, 0)
            .rot("coral1", 0, 0, 0, 0).rot("coral1", 30, 50, 0, 40).rot("coral4", 0, 0, 0, 0).rot("coral4", 30, -50, 0, -40).rot("coral7", 0, 0, 0, 0).rot("coral7", 30, 40, 0, 30)
            .scl("body", 0, 1, 1, 1).scl("body", 36, 1, 0.75f, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_skulking_colony", "Skulking Colony", "蠢く群生体")
            .game(2, 1).elite().size(1.8f, 1.6f).hp(75).speed(0.3f).style(Style.INSECT).egg(0xc95a78, 0x5d5a6a)
            .model(SkulkingColony::model)
            .spawn(1, 1, 1, MobDef.Habitat.BEACH)
            .pattern(s -> CYC[s.count("ct") % 4])
            .move(Move.of("zoom", 26).intent(Intent.ATTACK, 14, 1).reach(3.0f).sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.5f).at(12, (s, t) -> { Fx.dash(0.9, 0.1).run(s, t); Fx.meleeNow(s, 14, 3.4, 90, null); }))
            .move(Move.of("inertia", 34).intent(Intent.ATTACK_BUFF, 9, 1).reach(3.0f).at(16, Fx.aoe(9, 3.2)).at(20, Fx.strength(2)))
            .move(Move.of("stabs", 32).intent(Intent.ATTACK, 7, 2).reach(3.0f).at(9, Fx.melee(7, 3.4, 80)).at(24, Fx.melee(7, 3.4, 80))
                .sound(8, SoundEvents.TRIDENT_THROW, 0.8f, 1.2f).sound(23, SoundEvents.TRIDENT_THROW, 0.8f, 1.2f))
            .onMoveEnd(s -> s.inc("ct"))
            .onTick(s -> { if (s.tickCount % 40 == 0) s.setCount("shell", 0); })       // Hardened Shell resets each turn (2s)
            .onHurt((s, src, dmg) -> {
                float ok = Math.max(0, 20 - s.count("shell"));     // can lose at most 20 HP per turn
                float taken = Math.min(dmg, ok);
                s.setCount("shell", s.count("shell") + Math.round(taken));
                if (taken < dmg) Fx.particles(s, ParticleTypes.CRIT, 4, 0.4);
                return taken;
            });
    }
}
