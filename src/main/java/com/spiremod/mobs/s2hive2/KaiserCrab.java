package com.spiremod.mobs.s2hive2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Hive boss KAISER CRAB: two giant claw-arms fighting at once. var 0 = CRUSHER (left, 209 HP, spawns the Rocket), var 1 = ROCKET (right, 199 HP).
 * Crusher cycle: Thrash 12 -> Enlarging Strike 4 (claw grows) -> Bug Sting 6x2 + Weak/Frail -> Adapt +2 Str -> Guarded Strike 12 + 18 Block.
 * Rocket cycle: Targeting Reticle 3 -> Precision Beam 18 -> Charge Up +2 Str -> Laser 31 -> Recharge (nothing).
 * Back Attack: +50% damage on targets that face away. Crab Rage: when the other claw dies, +6 Str and 99 Block.
 */
public final class KaiserCrab {
    private KaiserCrab() {}

    static final int SHELL = 0xc8502a, SHELL_L = 0xe87a48, SHELL_D = 0x7a2a16, BELLY = 0xe8c8a0, CYAN = 0x40e0ff, STEEL = 0x6a7480, STEEL_D = 0x3a4048;

    public static ModelSpec model() { return model(false); }

    public static ModelSpec model(boolean rocket) {
        ModelSpec m = new ModelSpec(rocket ? "s2_kaiser_crab_rocket" : "s2_kaiser_crab_crusher");
        int c = rocket ? 0x8a8e98 : SHELL, cl = rocket ? 0xb4bac8 : SHELL_L, cd = rocket ? STEEL_D : SHELL_D;
        // carapace half + eye stalks
        Part body = m.add("body").at(0, 0, 0);
        body.box(-16, 8, -12, 32, 14, 24).color(c).grad(cl, cd).bevel(0.3f).noise(0.12f);
        body.box(-14, 20, -10, 28, 4, 20).color(cl).grad(cl, c).bevel(0.3f);
        body.box(-12, 24, -6, 24, 2, 12).color(c).bevel(0.3f);
        for (int i = -2; i <= 2; i++) body.box(i * 5 - 1, 25.5f, -4, 2, 1, 8).color(cd).flat();
        body.box(-12, 4, -9, 24, 5, 18).color(BELLY).grad(BELLY, 0x8a6a4a).bevel(0.2f);
        for (int sd = -1; sd <= 1; sd += 2) {
            Part st = body.add(sd < 0 ? "eyeR" : "eyeL").at(sd * 6, 22, 11).rot(-20, 0, 0);
            st.box(-1, 0, -1, 2, 7, 2).color(c);
            st.box(-2, 7, -2, 4, 4, 4).color(0xfff0d0).flat().glow(Face.FRONT, 1, 1, 2, 2, rocket ? CYAN : 0xff3020);
        }
        // walking legs (side)
        for (int k = 0; k < 3; k++) for (int sd = -1; sd <= 1; sd += 2) {
            Part lg = m.add("lg" + k + (sd < 0 ? "R" : "L")).at(sd * 14, 12, -8 + k * 8).rot(0, 0, sd * -40);
            lg.box(-1.5f, -10, -1.5f, 3, 10, 3).color(c).grad(cl, cd);
            Part ft = lg.add("ft" + k + (sd < 0 ? "R" : "L")).at(0, -10, 0).rot(0, 0, sd * 70);
            ft.box(-1, -9, -1, 2, 9, 2).color(cd);
        }
        // the big arm: shoulder -> forearm -> pincer (upper fixed jaw, lower hinged jaw)
        Part sh = body.add("shoulder").at(0, 16, 12);
        sh.box(-5, -5, 0, 10, 10, 8).color(c).grad(cl, cd).bevel(0.3f);
        Part fore = sh.add("fore").at(0, 0, 8);
        fore.box(-6, -6, 0, 12, 12, 14).color(c).grad(cl, cd).bevel(0.3f).noise(0.12f);
        fore.box(-4, 6, 2, 8, 2, 10).color(cl).bevel(0.3f);
        Part claw = fore.add("claw").at(0, 0, 14);
        claw.box(-8, 0, 0, 16, 9, 16).color(c).grad(cl, cd).bevel(0.3f).noise(0.12f);                 // upper pincer
        claw.box(-3, 9, 4, 6, 2, 8).color(cl).bevel(0.25f);
        claw.box(-7, -1, 12, 14, 2, 4).color(0xf0eadc).flat().bevel(0.2f);                              // teeth ridge
        Part jaw = claw.add("jaw").at(0, 0, 0);
        jaw.box(-7, -8, 0, 14, 8, 15).color(c).grad(c, cd).bevel(0.3f).noise(0.12f);                   // lower pincer
        jaw.box(-6, -1, 11, 12, 2, 4).color(0xf0eadc).flat().bevel(0.2f);
        if (rocket) {
            Part bar = claw.add("barrel").at(0, 5, 16);
            bar.box(-3, -3, 0, 6, 6, 8).color(STEEL).grad(0x9aa4b4, STEEL_D).bevel(0.25f);
            bar.box(-2, -2, 7.8f, 4, 4, 1).color(CYAN).flat().glow(Face.FRONT, 0, 0, 4, 4, CYAN);
            for (int i = 0; i < 3; i++) fore.box(-6.2f, -4 + i * 4, 3, 1, 2, 8).color(CYAN).flat().glow(Face.LEFT, 0, 0, 8, 2, CYAN);
            body.box(-6, 23, -12, 12, 5, 4).color(STEEL).grad(0x9aa4b4, STEEL_D).bevel(0.25f);         // jet pack
            body.box(-4, 25, -12.3f, 3, 2, 1).color(CYAN).flat().glow(Face.BACK, 0, 0, 3, 2, CYAN);
        } else {
            for (int i = 0; i < 3; i++) claw.box(-6 + i * 5, 7, 11, 2, 3, 2).color(0xf0eadc).flat();     // spikes
        }
        m.proc((r, cx) -> {
            boolean mv = cx.swingAmt > 0.01f;
            for (int k = 0; k < 3; k++) for (int sd = -1; sd <= 1; sd += 2) {
                float ph = k * 1.7f + (sd < 0 ? 0 : 3.14f);
                r.rot("lg" + k + (sd < 0 ? "R" : "L"), 0, 0, mv ? (float) Math.sin(cx.swing * 1.2f + ph) * 12 * Math.min(1, cx.swingAmt * 2) : (float) Math.sin(cx.age * 0.15f + ph) * 2);
            }
            float g = 1 + cx.var * 0.0f; // var unused for look; claw growth driven by 'grow' counter in var >> 1
            float grow = 1 + Math.min(3, cx.var >> 1) * 0.12f;
            r.scl("claw", grow, grow, grow);
            r.rot("eyeR", 0, 0, (float) Math.sin(cx.age * 0.1f) * 6); r.rot("eyeL", 0, 0, -(float) Math.sin(cx.age * 0.1f + 1) * 6);
        });
        m.look("shoulder", 40, 15);
        m.clip("walk", 20).loop().pos("body", 0, 0, 0, 0).pos("body", 5, 0, 0.6f, 0).pos("body", 10, 0, 0, 0).pos("body", 15, 0, 0.6f, 0).pos("body", 20, 0, 0, 0);
        m.clip("idle", 50).loop()
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 25, -10, 0, 0).rot("jaw", 50, 0, 0, 0)
            .rot("shoulder", 0, -3, 0, 0).rot("shoulder", 25, 3, 0, 0).rot("shoulder", 50, -3, 0, 0)
            .rot("fore", 0, 0, 0, 0).rot("fore", 25, 0, 4, 0).rot("fore", 50, 0, 0, 0);
        // shared overhead slam / pinch helper clips
        slam(m, "thrash", 32, 55, 14); slam(m, "enlarging_strike", 30, 40, 13); slam(m, "guarded_strike", 34, 60, 15);
        // BUG STING: two stabbing pinch-jabs
        m.clip("bug_sting", 36)
            .rot("shoulder", 0, 0, 0, 0).rot("shoulder", 8, -25, 0, 0).rot("shoulder", 11, 25, 0, 0, Ease.IN_CUBIC).rot("shoulder", 17, -20, 0, 0).rot("shoulder", 21, 28, 0, 0, Ease.IN_CUBIC).rot("shoulder", 36, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 8, 35, 0, 0).rot("jaw", 11, -25, 0, 0, Ease.IN_CUBIC).rot("jaw", 17, 35, 0, 0).rot("jaw", 21, -25, 0, 0, Ease.IN_CUBIC).rot("jaw", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 11, 0, 0, 3, Ease.IN_CUBIC).pos("body", 17, 0, 0, -1).pos("body", 21, 0, 0, 3, Ease.IN_CUBIC).pos("body", 36, 0, 0, 0);
        // ADAPT / CHARGE UP / RECHARGE: crouch + flex
        m.clip("adapt", 34)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -3, 0).pos("body", 18, 0, 3, 0, Ease.OUT_BACK).pos("body", 34, 0, 0, 0)
            .rot("shoulder", 0, 0, 0, 0).rot("shoulder", 12, 20, 0, 0).rot("shoulder", 18, -50, 0, 0, Ease.OUT_BACK).rot("shoulder", 28, -45, 0, 0).rot("shoulder", 34, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 18, 40, 0, 0).rot("jaw", 24, 5, 0, 0).rot("jaw", 34, 0, 0, 0)
            .scl("claw", 0, 1, 1, 1).scl("claw", 18, 1.2f, 1.2f, 1.2f, Ease.OUT_BACK).scl("claw", 34, 1, 1, 1);
        m.clip("charge_up", 34)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -2, -2).pos("body", 20, 0, 2, 0, Ease.OUT_BACK).pos("body", 34, 0, 0, 0)
            .rot("shoulder", 0, 0, 0, 0).rot("shoulder", 14, 15, 0, 0).rot("shoulder", 20, -30, 0, 0, Ease.OUT_BACK).rot("shoulder", 34, 0, 0, 0)
            .rot("fore", 0, 0, 0, 0).rot("fore", 10, 0, 0, 25).rot("fore", 20, 0, 0, -25).rot("fore", 34, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 20, 25, 0, 0).rot("jaw", 34, 0, 0, 0);
        m.clip("recharge", 40)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -4, 0).pos("body", 34, 0, -4, 0).pos("body", 40, 0, 0, 0)
            .rot("shoulder", 0, 0, 0, 0).rot("shoulder", 10, 30, 0, 0).rot("shoulder", 34, 30, 0, 0).rot("shoulder", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 20, 0, 0).rot("jaw", 34, 20, 0, 0).rot("jaw", 40, 0, 0, 0);
        // ROCKET aiming clips
        if (rocket) { aim(m, "targeting_reticle", 26, 14, false); aim(m, "precision_beam", 34, 20, true); aim(m, "laser", 48, 30, true); }
        m.clip("hurt", 10).rot("shoulder", 0, 0, 0, 0).rot("shoulder", 3, 12, 0, 0).rot("shoulder", 10, 0, 0, 0).pos("body", 0, 0, 0, 0).pos("body", 3, 0, 0, -1.5f).pos("body", 10, 0, 0, 0);
        m.clip("death", 40)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, -3, 0, Ease.IN_QUAD).pos("body", 40, 0, -8, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 20, 0, 0, 10).rot("body", 40, 0, 0, 25, Ease.OUT_BOUNCE)
            .rot("shoulder", 0, 0, 0, 0).rot("shoulder", 14, -40, 0, 0).rot("shoulder", 40, 40, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 45, 0, 0).rot("jaw", 40, 30, 0, 0);
        return m;
    }

    /** raise the claw high, crash it down at tick hit */
    static void slam(ModelSpec m, String n, int len, float up, int hit) {
        m.clip(n, len)
            .rot("shoulder", 0, 0, 0, 0).rot("shoulder", hit - 5, -up, 0, 0, Ease.OUT_CUBIC).rot("shoulder", hit, 35, 0, 0, Ease.IN_CUBIC).rot("shoulder", hit + 8, 25, 0, 0).rot("shoulder", len, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", hit - 5, 40, 0, 0).rot("jaw", hit, -20, 0, 0, Ease.IN_CUBIC).rot("jaw", len, 0, 0, 0)
            .rot("fore", 0, 0, 0, 0).rot("fore", hit - 5, 0, 20, 0).rot("fore", hit, 0, -15, 0).rot("fore", len, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", hit - 5, 0, 2, -3).pos("body", hit, 0, -2, 5, Ease.IN_CUBIC).pos("body", len, 0, 0, 0)
            .scl("claw", 0, 1, 1, 1).scl("claw", hit, n.equals("enlarging_strike") ? 1.25f : 1, n.equals("enlarging_strike") ? 1.25f : 1, n.equals("enlarging_strike") ? 1.25f : 1).scl("claw", len, 1, 1, 1);
    }

    /** lift the barrel claw and aim: charge then fire */
    static void aim(ModelSpec m, String n, int len, int fire, boolean big) {
        m.clip(n, len)
            .rot("shoulder", 0, 0, 0, 0).rot("shoulder", fire - 6, -25, 0, 0).rot("shoulder", fire, big ? -32 : -26, 0, 0, Ease.IN_CUBIC).rot("shoulder", len, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", fire - 6, 35, 0, 0).rot("jaw", len - 4, 35, 0, 0).rot("jaw", len, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", fire - 4, 0, 0, -2).pos("body", fire, 0, 0, big ? -5 : -2, Ease.OUT_BACK).pos("body", len, 0, 0, 0)
            .scl("barrel", 0, 1, 1, 1).scl("barrel", fire - 4, 1.3f, 1.3f, 1.3f).scl("barrel", fire, big ? 1.8f : 1.2f, big ? 1.8f : 1.2f, 1.2f, Ease.OUT_BACK).scl("barrel", len, 1, 1, 1);
    }

    static void backAttack(SpireMob s, float dmg, double range, double arc, java.util.function.Consumer<net.minecraft.world.entity.LivingEntity> on) {
        H.cone(s, dmg, range, arc, e -> {
            net.minecraft.world.phys.Vec3 look = e.getLookAngle(); net.minecraft.world.phys.Vec3 d = s.position().subtract(e.position());
            return look.x * d.x + look.z * d.z < 0 ? dmg * 0.5 : 0;   // Back Attack: target faces away from us
        }, on);
    }

    static void rage(SpireMob s) {
        for (SpireMob o : s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(48)))
            if (o != s && o.isAlive() && o.def.id.startsWith("s2_kaiser_crab")) {
                o.addStrength(6); o.addBlock(99); Fx.particles(o, ParticleTypes.ANGRY_VILLAGER, 14, 0.9); Fx.sound(o, SoundEvents.RAVAGER_ROAR, 1.5f, 0.7f);
            }
    }

    public static MobDef def() { return def(0); }

    public static MobDef def(int v) {
        boolean rk = v == 1;
        MobDef d = MobDef.of(rk ? "s2_kaiser_crab_rocket" : "s2_kaiser_crab", rk ? "Kaiser Crab (Rocket)" : "Kaiser Crab (Crusher)", rk ? "カイザークラブ（ロケット）" : "カイザークラブ（クラッシャー）")
            .game(2, 2).size(3.6f, 2.4f).hp(rk ? 199 : 209).speed(0.16f).style(Style.AQUATIC).egg(rk ? 0x8a8e98 : SHELL, rk ? CYAN : 0xf0eadc)
            .model(() -> model(rk)).onDeath((s, src) -> rage(s));
        if (!rk) d.boss().spawn(0, 1, 1, MobDef.Habitat.ANY);
        else d.elite();
        d.onSpawn(s -> {
            if (!rk && s.getLeader() == null) { SpireMob r = Fx.summon(s, "s2_kaiser_crab_rocket", 6, 0); if (r != null) r.setLeader(null); }
        });
        if (!rk) {
            d.pattern(Pattern.cycle("thrash", "enlarging_strike", "bug_sting", "adapt", "guarded_strike"))
                .move(Move.of("thrash", 32).intent(Intent.ATTACK, 12, 1).reach(4f).sound(9, SoundEvents.RAVAGER_STEP, 1f, 0.6f)
                    .at(14, (s, t) -> { backAttack(s, 12, 4.2, 80, null); Fx.ring(s, ParticleTypes.POOF, Fx.rel(s, 3, 0, 0.1), 1.5, 14); Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.6f, 1.2f); }))
                .move(Move.of("enlarging_strike", 30).intent(Intent.ATTACK, 4, 1).reach(4f).sound(8, SoundEvents.PUFFER_FISH_BLOW_UP, 1f, 0.6f)
                    .at(13, (s, t) -> { backAttack(s, 4, 4.2, 80, null); s.setVar(s.getVar() + 2); Fx.particles(s, ParticleTypes.CRIT, 10, 0.8); }))
                .move(Move.of("bug_sting", 36).intent(Intent.ATTACK_DEBUFF, 6, 2).reach(4f).sound(8, SoundEvents.BEE_STING, 1f, 0.6f)
                    .at(11, (s, t) -> backAttack(s, 6, 4.2, 70, e -> { Fx.weak(e, 2); Fx.frail(e, 2); }))
                    .at(21, (s, t) -> backAttack(s, 6, 4.2, 70, e -> { Fx.weak(e, 2); Fx.frail(e, 2); })))
                .move(Move.of("adapt", 34).intent(Intent.BUFF).noTarget().sound(16, SoundEvents.RAVAGER_ROAR, 1f, 0.9f)
                    .at(18, (s, t) -> { s.addStrength(2); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 8, 0.8); }))
                .move(Move.of("guarded_strike", 34).intent(Intent.ATTACK_DEFEND, 12, 1).reach(4f).sound(10, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.6f)
                    .at(15, (s, t) -> { backAttack(s, 12, 4.2, 80, null); s.addBlock(18); }));
        } else {
            d.pattern(Pattern.cycle("targeting_reticle", "precision_beam", "charge_up", "laser", "recharge"))
                .move(Move.of("targeting_reticle", 26).intent(Intent.ATTACK, 3, 1).ranged(16).sound(10, SoundEvents.BEACON_ACTIVATE, 1f, 1.8f)
                    .at(14, (s, t) -> { if (t != null) { H.line(s, ParticleTypes.ELECTRIC_SPARK, Fx.rel(s, 3, 0, 1.6), t.position().add(0, 1, 0), 12); H.hit(s, t, 3); } }))
                .move(Move.of("precision_beam", 34).intent(Intent.ATTACK, 18, 1).ranged(16).sound(12, SoundEvents.BEACON_POWER_SELECT, 1.2f, 1.2f)
                    .at(20, (s, t) -> { if (t != null) { H.line(s, ParticleTypes.END_ROD, Fx.rel(s, 3, 0, 1.6), t.position().add(0, 1, 0), 16); Fx.sound(s, SoundEvents.GUARDIAN_ATTACK, 1f, 1.4f); H.hit(s, t, 18); } }))
                .move(Move.of("charge_up", 34).intent(Intent.BUFF).noTarget().sound(14, SoundEvents.BEACON_POWER_SELECT, 1.2f, 0.7f)
                    .at(20, (s, t) -> { s.addStrength(2); Fx.particles(s, ParticleTypes.ELECTRIC_SPARK, 20, 0.8); }))
                .move(Move.of("laser", 48).intent(Intent.ATTACK, 31, 1).ranged(18).sound(14, SoundEvents.BEACON_ACTIVATE, 1.5f, 0.6f)
                    .at(30, (s, t) -> { if (t != null) { H.line(s, ParticleTypes.END_ROD, Fx.rel(s, 3, 0, 1.6), t.position().add(0, 1, 0), 24); Fx.sound(s, SoundEvents.GUARDIAN_ATTACK, 1.5f, 0.6f); Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.8f, 1.5f); H.hit(s, t, 31); } }))
                .move(Move.of("recharge", 40).intent(Intent.STUN).noTarget().sound(8, SoundEvents.FIRE_EXTINGUISH, 1f, 0.8f)
                    .at(10, (s, t) -> Fx.particles(s, ParticleTypes.SMOKE, 14, 0.7)));
        }
        return d;
    }
}
