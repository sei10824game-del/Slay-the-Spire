package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

/**
 * StS1 Act 1 Boss The Guardian: 240 HP. Offensive mode: Charging Up (9 Block), Fierce Bash (32), Vent Steam (2 Weak + 2 Vulnerable),
 * Whirlwind (5 x4), repeat. Mode Shift 30: after taking 30 damage it curls into Defensive mode (Close Up: Sharp Hide 3).
 * Defensive: Roll Attack (9), Twin Slam (8 x2) which returns it to Offensive mode (Mode Shift +10) starting with Whirlwind.
 */
public final class TheGuardian {
    private TheGuardian() {}
    static final int ST = 0xd8b858, ST_D = 0x8a7430, ST_L = 0xf0dc8c, GOLD = 0x4a4c9a, CORE = 0xffa030;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("the_guardian").scale(2.1f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 5, 6, 0);
            leg.box(-3, -6, -3, 6, 6, 6).color(ST).grad(ST, ST_D).bevel(0.14f).noise(0.08f);
            leg.box(-3.5f, -6, -3.5f, 7, 2, 8).color(ST_D).bevel(0.15f);
            leg.box(-3.3f, -3, -3.3f, 6, 1, 6).color(GOLD).flat().inflate(0.3f);
        }
        Part body = m.add("body").at(0, 6, 0);
        body.box(-6, 0, -4, 12, 4, 8).color(ST_D).grad(ST, ST_D).bevel(0.14f);
        Part torso = body.add("torso").at(0, 4, 0);
        torso.box(-8, 0, -5, 16, 12, 10).color(ST).grad(ST_L, ST_D).bevel(0.14f).noise(0.08f)
            .paint(Face.FRONT, 0, 6, 16, 1, ST_D);
        torso.box(-8.4f, 2, -5.4f, 16, 1, 10).color(GOLD).flat().inflate(0.4f);                        // gold band
        Part core = torso.add("core").at(0, 7, 5);
        core.box(-3, -3, -0.5f, 6, 6, 1).color(0x302018).bevel(0.2f)
            .glow(Face.FRONT, 1, 1, 4, 4, CORE).glow(Face.FRONT, 2, 2, 2, 2, 0xfff0b0);                 // glowing chest eye-core
        for (int i = 0; i < 4; i++) { // back plates
            Part pl = torso.add("plate" + i).at(-6 + i * 4, 10, -5);
            pl.box(-1.5f, -2, -1, 3, 8, 2).color(ST_L).grad(ST_L, ST).bevel(0.15f);
        }
        // spikes shown in defensive mode (Sharp Hide)
        for (int i = 0; i < 8; i++) {
            Part sp = torso.add("spike" + i).at(-7 + (i % 4) * 4.6f, i < 4 ? 11 : 4, i < 4 ? -4 : -5.5f).rot(i < 4 ? -40 : -85, 0, 0);
            sp.box(-1, 0, -1, 2, 5, 2).color(0xd8dce6).grad(0xf4f6fa, ST).bevel(0.1f);
        }
        Part head = torso.add("head").at(0, 12, 0.5f);
        head.box(-4, 0, -4, 8, 6, 8).color(ST).grad(ST_L, ST).bevel(0.15f).noise(0.07f)
            .paint(Face.FRONT, 1, 2, 6, 1, 0x20140c).glow(Face.FRONT, 1, 2, 2, 1, CORE).glow(Face.FRONT, 5, 2, 2, 1, CORE);
        head.box(-4.5f, 5, -4.5f, 9, 2, 9).color(GOLD).grad(0xe0c060, GOLD).bevel(0.15f);                // crown rim
        head.box(-1, 7, -1, 2, 3, 2).color(GOLD).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 11.5f, 10, 0);
            arm.box(-4, -3, -4, 8, 6, 8).color(ST_L).grad(ST_L, ST).bevel(0.14f);                        // pauldron
            arm.box(-3, -12, -3, 6, 9, 6).color(ST).grad(ST, ST_D).bevel(0.12f).noise(0.08f);
            arm.box(-3.4f, -8, -3.4f, 6, 2, 6).color(GOLD).flat().inflate(0.4f);
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -12, 0);
            hand.box(-4, -7, -4, 8, 7, 8).color(ST_D).grad(ST, ST_D).bevel(0.14f).noise(0.08f);
            for (int i = 0; i < 3; i++) hand.box(-3 + i * 2.6f, -7.5f, 3.5f, 2, 2, 1).color(0xd8dce6).flat();   // knuckle spikes
        }
        m.look("head", 40, 20);
        m.walkBiped("legR", "legL", "armR", "armL", 28f, 20f);
        m.proc((r, c) -> {
            for (int i = 0; i < 8; i++) r.show("spike" + i, c.phase == 1);
            float k = 1f + 0.08f * (float) Math.sin(c.age * 0.2f); r.scl("core", k, k, 1);
        });
        Clip id = m.clip("idle", 50).loop();
        K.r(id, "torso", 0, 2, 0, 0, 25, 4, 0, 0, 50, 2, 0, 0);
        K.r(id, "head", 0, 0, 0, 0, 20, 0, 10, 0, 40, 0, -10, 0, 50, 0, 0, 0);
        K.r(id, "armR", 0, 8, 0, 6, 25, 12, 0, 8, 50, 8, 0, 6);
        K.r(id, "armL", 0, 8, 0, -6, 25, 12, 0, -8, 50, 8, 0, -6);
        // DEFENSIVE stance: legs fold, torso hunched, head sunk, arms wrapped over the chest, shell-like ball
        Clip df = m.clip("defensive", 50).loop();
        K.r(df, "torso", 0, 38, 0, 0, 25, 42, 0, 0, 50, 38, 0, 0);
        K.r(df, "head", 0, 40, 0, 0, 50, 40, 0, 0);
        K.p(df, "head", 0, 0, -6, -2, 50, 0, -6, -2);
        K.r(df, "armR", 0, -60, 25, 55, 50, -60, 25, 55);
        K.r(df, "armL", 0, -60, -25, -55, 50, -60, -25, -55);
        K.r(df, "legR", 0, -70, 0, 12, 50, -70, 0, 12);
        K.r(df, "legL", 0, -70, 0, -12, 50, -70, 0, -12);
        K.p(df, "body", 0, 0, -3, 0, 25, 0, -2.4f, 0, 50, 0, -3, 0);
        K.s(df, "torso", 0, 1.05f, 0.9f, 1.05f, 50, 1.05f, 0.9f, 1.05f);
        // CHARGING UP: crouch, arms drawn in, chest core flares, shudders as it stores armour
        Clip cu = m.clip("charging_up", 36);
        K.r(cu, "torso", 0, 2, 0, 0, 12, 22, 0, 0, 24, 18, 0, 0, 36, 0, 0, 0);
        K.r(cu, "armR", 0, 8, 0, 6, 12, -50, 0, -30, 24, -55, 0, -35, 36, 0, 0, 0);
        K.r(cu, "armL", 0, 8, 0, -6, 12, -50, 0, 30, 24, -55, 0, 35, 36, 0, 0, 0);
        K.r(cu, "head", 0, 0, 0, 0, 12, 18, 0, 0, 36, 0, 0, 0);
        K.p(cu, "body", 0, 0, 0, 0, 12, 0, -2.5f, 0, 24, 0, -2.5f, 0, 36, 0, 0, 0);
        K.s(cu, "core", 0, 1, 1, 1, 16, 2f, 2f, 1, 24, 1.6f, 1.6f, 1, 36, 1, 1, 1);
        K.s(cu, "torso", 0, 1, 1, 1, 14, 0.95f, 1.06f, 0.95f, 18, 1.05f, 0.95f, 1.05f, 36, 1, 1, 1);
        // FIERCE BASH: haul both fists overhead and drive them down in one colossal hammer
        Clip fb = m.clip("fierce_bash", 44);
        K.r(fb, "torso", 0, 2, 0, 0, 16, -24, 0, 0, 120, 38, 0, 0, 32, 32, 0, 0, 44, 0, 0, 0);
        K.r(fb, "head", 0, 0, 0, 0, 16, -22, 0, 0, 120, 14, 0, 0, 44, 0, 0, 0);
        K.r(fb, "armR", 0, 8, 0, 6, 16, -175, 0, 14, 120, -60, 0, -5, 32, -55, 0, -5, 44, 0, 0, 0);
        K.r(fb, "armL", 0, 8, 0, -6, 16, -175, 0, -14, 120, -60, 0, 5, 32, -55, 0, 5, 44, 0, 0, 0);
        K.p(fb, "body", 0, 0, 0, 0, 16, 0, 2, -3, 120, 0, -3, 5, 32, 0, -3, 5, 44, 0, 0, 0);
        K.r(fb, "legR", 0, 0, 0, 0, 16, 18, 0, 0, 120, -26, 0, 0, 44, 0, 0, 0);
        K.r(fb, "legL", 0, 0, 0, 0, 16, -22, 0, 0, 120, 20, 0, 0, 44, 0, 0, 0);
        // VENT STEAM: lean back, steam bursts from the plates, arms thrown wide
        Clip vs = m.clip("vent_steam", 40);
        K.r(vs, "torso", 0, 2, 0, 0, 12, -20, 0, 0, 30, -14, 0, 0, 40, 0, 0, 0);
        K.r(vs, "head", 0, 0, 0, 0, 12, -35, 0, 0, 30, -30, 0, 0, 40, 0, 0, 0);
        K.r(vs, "armR", 0, 8, 0, 6, 12, -30, 0, 85, 30, -30, 0, 85, 40, 0, 0, 0);
        K.r(vs, "armL", 0, 8, 0, -6, 12, -30, 0, -85, 30, -30, 0, -85, 40, 0, 0, 0);
        K.s(vs, "torso", 0, 1, 1, 1, 12, 1.1f, 1.1f, 1.1f, 16, 0.96f, 0.96f, 0.96f, 20, 1.1f, 1.1f, 1.1f, 24, 0.96f, 0.96f, 0.96f, 40, 1, 1, 1);
        // WHIRLWIND: arms flung out, whole body spins in place
        Clip ww = m.clip("whirlwind", 44);
        K.r(ww, "armR", 0, 8, 0, 6, 8, 0, 0, 90, 38, 0, 0, 90, 44, 0, 0, 0);
        K.r(ww, "armL", 0, 8, 0, -6, 8, 0, 0, -90, 38, 0, 0, -90, 44, 0, 0, 0);
        K.r(ww, "body", 0, 0, 0, 0, 8, 0, 0, 0, 36, 0, 1080, 0, 44, 0, 1080, 0);
        K.r(ww, "torso", 0, 2, 0, 0, 8, 10, 0, 0, 38, 10, 0, 0, 44, 0, 0, 0);
        // CLOSE UP: arms clamp inward, torso folds forward, spikes erupt
        Clip cl = m.clip("close_up", 36);
        K.r(cl, "torso", 0, 2, 0, 0, 14, 45, 0, 0, 36, 38, 0, 0);
        K.r(cl, "head", 0, 0, 0, 0, 14, 40, 0, 0, 36, 40, 0, 0);
        K.p(cl, "head", 0, 0, 0, 0, 14, 0, -6, -2, 36, 0, -6, -2);
        K.r(cl, "armR", 0, 8, 0, 6, 14, -60, 25, 55, 36, -60, 25, 55);
        K.r(cl, "armL", 0, 8, 0, -6, 14, -60, -25, -55, 36, -60, -25, -55);
        K.r(cl, "legR", 0, 0, 0, 0, 14, -70, 0, 12, 36, -70, 0, 12);
        K.r(cl, "legL", 0, 0, 0, 0, 14, -70, 0, -12, 36, -70, 0, -12);
        K.p(cl, "body", 0, 0, 0, 0, 14, 0, -3, 0, 36, 0, -3, 0);
        // ROLL ATTACK: spiky ball tips forward and rolls over the target
        Clip ro = m.clip("roll_attack", 34);
        K.r(ro, "body", 0, 0, 0, 0, 10, -20, 0, 0, 114, 120, 0, 0, 20, 300, 0, 0, 34, 360, 0, 0);
        K.p(ro, "body", 0, 0, -3, 0, 10, 0, -3, -4, 114, 0, 3, 8, 20, 0, 3, 8, 34, 0, -3, 0);
        K.r(ro, "torso", 0, 38, 0, 0, 34, 38, 0, 0);
        K.r(ro, "head", 0, 40, 0, 0, 34, 40, 0, 0);
        K.p(ro, "head", 0, 0, -6, -2, 34, 0, -6, -2);
        K.r(ro, "armR", 0, -60, 25, 55, 34, -60, 25, 55);
        K.r(ro, "armL", 0, -60, -25, -55, 34, -60, -25, -55);
        K.r(ro, "legR", 0, -70, 0, 12, 34, -70, 0, 12);
        K.r(ro, "legL", 0, -70, 0, -12, 34, -70, 0, -12);
        // TWIN SLAM: uncurl in a burst, both fists smash down twice
        Clip tw = m.clip("twin_slam", 48);
        K.r(tw, "torso", 0, 38, 0, 0, 8, -10, 0, 0, 120, 30, 0, 0, 16, 10, 0, 0, 22, -16, 0, 0, 126, 32, 0, 0, 48, 0, 0, 0);
        K.r(tw, "head", 0, 40, 0, 0, 8, -10, 0, 0, 20, 10, 0, 0, 48, 0, 0, 0);
        K.p(tw, "head", 0, 0, -6, -2, 8, 0, 0, 0, 48, 0, 0, 0);
        K.r(tw, "armR", 0, -60, 25, 55, 8, -170, 0, 14, 112, -60, 0, -5, 16, -140, 0, 12, 122, -60, 0, -5, 40, -50, 0, 0, 48, 0, 0, 0);
        K.r(tw, "armL", 0, -60, -25, -55, 8, -170, 0, -14, 112, -60, 0, 5, 16, -140, 0, -12, 122, -60, 0, 5, 40, -50, 0, 0, 48, 0, 0, 0);
        K.r(tw, "legR", 0, -70, 0, 12, 8, 0, 0, 0, 48, 0, 0, 0);
        K.r(tw, "legL", 0, -70, 0, -12, 8, 0, 0, 0, 48, 0, 0, 0);
        K.p(tw, "body", 0, 0, -3, 0, 8, 0, 1, -1, 112, 0, -2, 4, 16, 0, 1, 0, 122, 0, -2, 4, 48, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -8, 0, 4).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -14, 6, 0).rot("head", 10, 0, 0, 0);
        Clip d = m.clip("death", 44);
        K.r(d, "torso", 0, 0, 0, 0, 14, -15, 0, 0, 44, 30, 0, 0);
        K.r(d, "head", 0, 0, 0, 0, 14, -35, 0, 0, 44, 40, 20, 0);
        K.r(d, "armR", 0, 0, 0, 0, 14, -60, 0, 50, 44, 20, 0, 80);
        K.r(d, "armL", 0, 0, 0, 0, 14, -60, 0, -50, 44, 20, 0, -80);
        K.r(d, "legR", 0, 0, 0, 0, 24, -40, 0, 8, 44, -70, 0, 10);
        K.r(d, "legL", 0, 0, 0, 0, 24, -60, 0, -8, 44, -70, 0, -10);
        K.p(d, "body", 0, 0, 0, 0, 24, 0, -3, -2, 44, 0, -6, -4);
        K.s(d, "core", 0, 1, 1, 1, 20, 1.5f, 1.5f, 1, 44, 0.1f, 0.1f, 1);
        return m;
    }

    static void enterDefense(SpireMob s) {
        s.setPhase(1); s.setBase("defensive"); s.setCount("dmg", 0);
        s.cancelMove(); s.forceNext("close_up");
    }

    public static MobDef def() {
        return MobDef.of("the_guardian", "The Guardian", "ガーディアン")
            .game(1, 1).size(2.2f, 3.3f).hp(240).speed(0.18f).style(Style.METAL).boss().egg(0x8a8f9a, 0xffa030)
            .model(TheGuardian::model)
            .pattern(s -> {
                String l = s.lastMove();
                if (s.getPhase() == 1) return l.equals("roll_attack") ? "twin_slam" : "roll_attack";
                switch (l) {
                    case "charging_up": return "fierce_bash";
                    case "fierce_bash": return "vent_steam";
                    case "vent_steam": return "whirlwind";
                    case "twin_slam": return "whirlwind";
                    default: return "charging_up";
                }
            })
            .move(Move.of("charging_up", 36).intent(Intent.DEFEND).noTarget()
                .sound(10, SoundEvents.BEACON_ACTIVATE, 1f, 0.6f)
                .at(18, (s, t) -> { s.addBlock(9); Fx.sound(s, SoundEvents.ANVIL_PLACE, 1f, 0.7f); Fx.particles(s, ParticleTypes.ENCHANT, 25, 0.8); }))
            .move(Move.of("fierce_bash", 44).intent(Intent.ATTACK, 32, 1).reach(4.0f)
                .sound(14, SoundEvents.IRON_GOLEM_ATTACK, 1.4f, 0.5f)
                .at(21, (s, t) -> { Fx.meleeNow(s, 32, 4.2, 120, null); Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 1f, 0.7f); Fx.ring(s, ParticleTypes.CLOUD, Fx.rel(s, 2.5, 0, 0.1), 2.0, 20); }))
            .move(Move.of("vent_steam", 40).intent(Intent.STRONG_DEBUFF).noTarget()
                .sound(14, SoundEvents.FIRE_EXTINGUISH, 1.5f, 0.7f)
                .at(16, (s, t) -> {
                    Fx.particles(s, ParticleTypes.CLOUD, 50, 1.6);
                    for (LivingEntity v : Fx.foes(s, s.getBoundingBox().inflate(6))) { Fx.weak(v, 8); Fx.vulnerable(v, 8); }
                }))
            .move(Move.of("whirlwind", 44).intent(Intent.ATTACK, 5, 4).reach(4.0f)
                .sound(8, SoundEvents.PHANTOM_FLAP, 1.2f, 0.5f)
                .at(12, Fx.aoe(5, 4.2)).at(18, Fx.aoe(5, 4.2)).at(24, Fx.aoe(5, 4.2)).at(30, Fx.aoe(5, 4.2)))
            .move(Move.of("close_up", 36).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(12, SoundEvents.ANVIL_PLACE, 1.2f, 0.5f)
                .at(16, (s, t) -> { Fx.particles(s, ParticleTypes.CRIT, 30, 1.0); Fx.sound(s, SoundEvents.IRON_GOLEM_DAMAGE, 1.2f, 0.6f); }))
            .move(Move.of("roll_attack", 34).intent(Intent.ATTACK, 9, 1).reach(4.0f)
                .sound(8, SoundEvents.IRON_GOLEM_STEP, 1.2f, 0.5f)
                .at(11, Fx.dash(0.6, 0.05)).at(16, Fx.melee(9, 3.5, 120)))
            .move(Move.of("twin_slam", 48).intent(Intent.ATTACK, 8, 2).reach(4.0f)
                .sound(12, SoundEvents.IRON_GOLEM_ATTACK, 1.4f, 0.5f)
                .at(14, Fx.aoe(8, 3.8)).at(25, Fx.aoe(8, 3.8))
                .at(26, (s, t) -> { s.setPhase(0); s.setBase("idle"); s.inc("shifts"); Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.8f, 0.8f); }))
            .onHurt((s, src, dmg) -> {
                if (s.getPhase() == 1) {                                         // Sharp Hide 3
                    if (src.getEntity() instanceof LivingEntity e && e != s && dmg > 0) Fx.raw(s, e, 3 * Cfg.DMG);
                } else if (dmg > 0 && s.getHealth() - dmg > 0) {                // Mode Shift
                    int total = s.count("dmg") + Math.round(dmg / Cfg.DMG);
                    s.setCount("dmg", total);
                    if (total >= 30 + 10 * s.count("shifts")) enterDefense(s);
                }
                return dmg;
            });
    }
}
