package com.spiremod.mobs.s2hive2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Hive elite DECIMILLIPEDE: three segments (Front / Middle / Back, 40-46 HP each) fighting together.
 * Each cycles Bulk (6 + 2 Str) -> Writhe (5x2) -> Outgas (8 + 1 Weak), each segment starting on a different move.
 * Reattach: a killed segment revives with 25 HP after 2 turns while another segment lives, then resumes on a random move.
 * Visual: arched millipede body section, dark navy carapace plates, brown underside, thin rusty legs, raw pink flesh stumps at both ends.
 * ctx.var: 0 = front (antennae), 1 = middle, 2 = back (tail cerci).
 */
public final class Decimillipede {
    private Decimillipede() {}

    static final int PLATE = 0x1c2740, PLATE_L = 0x34476a, PLATE_D = 0x0e1424, BELLY = 0x7a4a2a, BELLY_D = 0x4e2c18,
        LEG = 0x8a3a22, LEG_D = 0x5a2212, FLESH = 0xd88a72, FLESH_D = 0xa85a4a, FLESH_L = 0xf0aa90;
    static final int SEGS = 5;
    static final float[] REST = {-50, 25, 25, 25, 25};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_decimillipede");
        Part body = m.add("body").at(0, 0, 0);
        Part prev = body;
        for (int i = 0; i < SEGS; i++) {
            Part sg = prev.add("s" + i).at(0, i == 0 ? 6.5f : 0, i == 0 ? -19 : 10).rot(REST[i], 0, 0);
            if (i == 0) sg.at(0, 6.5f, -19);
            // underside / core
            sg.box(-5.5f, -5.5f, 0, 11, 7, 11).color(BELLY).grad(BELLY, BELLY_D).noise(0.12f)
                .band(2, 1, BELLY_D).band(5, 1, BELLY_D);
            // carapace: overlapping armour plate that wraps the top and the flanks
            sg.box(-6.5f, -2, -0.5f, 13, 8, 10).color(PLATE).grad(PLATE_L, PLATE_D).bevel(0.35f).noise(0.06f)
                .paint(Face.TOP, 0, 0, 13, 1, PLATE_L).paint(Face.TOP, 0, 8, 13, 2, PLATE_D)
                .paint(Face.LEFT, 0, 6, 10, 2, PLATE_D).paint(Face.RIGHT, 0, 6, 10, 2, PLATE_D);
            sg.box(-6, 5.5f, 1, 12, 1, 7).color(PLATE_L).bevel(0.3f).noise(0.04f);               // ridge highlight
            sg.box(-7, -2.5f, 7.5f, 14, 8, 2).color(PLATE_D).bevel(0.2f);                          // plate lip (overlap)
            // bristle hairs (orange, like the art)
            sg.box(5.6f, -4, 2, 1, 3, 1).color(LEG).flat(); sg.box(-6.6f, -4, 6, 1, 3, 1).color(LEG).flat();
            // legs: two per side per segment
            for (int k = 0; k < 2; k++) for (int sd = -1; sd <= 1; sd += 2) {
                Part lg = sg.add("lg" + i + k + (sd < 0 ? "R" : "L")).at(sd * 5, -5, 2.5f + k * 5).rot(0, 0, sd * -35);
                lg.box(-0.5f, -6, -0.5f, 1, 6, 1).color(LEG).grad(LEG, LEG_D).noise(0.05f);
                Part ft = lg.add("lf" + i + k + (sd < 0 ? "R" : "L")).at(0, -6, 0).rot(0, 0, sd * 45);
                ft.box(-0.5f, -5, -0.5f, 1, 5, 1).color(LEG_D);
            }
            prev = sg;
        }
        // flesh stumps (raw torn ends)
        Part rear = m.byName.get("s0").add("rearFlesh").at(0, 0, 0);
        rear.box(-5.5f, -5, -3, 11, 10, 3).color(FLESH).grad(FLESH_L, FLESH_D).noise(0.15f).bevel(0.3f)
            .paint(Face.BACK, 3, 3, 4, 3, FLESH_D).paint(Face.BACK, 1, 1, 2, 2, FLESH_L);
        rear.box(-4, -3.5f, -4.5f, 4, 4, 2).color(FLESH_L).noise(0.1f).bevel(0.3f);
        rear.box(1, -1, -4, 4, 5, 2).color(FLESH).noise(0.1f).bevel(0.3f);
        rear.box(-2, 2.5f, -3.8f, 3, 3, 1).color(FLESH_D).bevel(0.3f);
        Part front = m.byName.get("s4").add("frontFlesh").at(0, 0, 10);
        front.box(-5.5f, -5, 0, 11, 10, 3).color(FLESH).grad(FLESH_L, FLESH_D).noise(0.15f).bevel(0.3f)
            .paint(Face.FRONT, 3, 3, 5, 4, FLESH_D).paint(Face.FRONT, 7, 1, 2, 2, FLESH_L);
        front.box(0, -3, 2.5f, 4, 4, 2).color(FLESH_L).noise(0.1f).bevel(0.3f);
        front.box(-4.5f, 0, 2, 4, 4, 2).color(FLESH).noise(0.1f).bevel(0.3f);
        front.box(-1.5f, -1.5f, 3.6f, 3, 3, 1).color(0x5a1e1e).flat().bevel(0.2f);            // maw pit
        // front-only antennae, back-only cerci
        for (int sd = -1; sd <= 1; sd += 2) {
            Part an = front.add("ant" + (sd < 0 ? "R" : "L")).at(sd * 3, 4, 2).rot(-40, 0, sd * -20);
            an.box(-0.5f, 0, -0.5f, 1, 9, 1).color(LEG).grad(LEG, LEG_D);
            Part an2 = an.add("ant2" + (sd < 0 ? "R" : "L")).at(0, 9, 0).rot(50, 0, 0);
            an2.box(-0.5f, 0, -0.5f, 1, 7, 1).color(LEG_D);
            Part ce = rear.add("cer" + (sd < 0 ? "R" : "L")).at(sd * 3, 2, -3).rot(-150, 0, sd * 20);
            ce.box(-0.5f, 0, -0.5f, 1, 7, 1).color(PLATE_L).grad(PLATE_L, PLATE_D);
            ce.box(-1, 0, -1, 2, 2, 2).color(PLATE).bevel(0.3f);
        }
        // gas sacs that swell for Outgas
        Part sac = m.byName.get("s2").add("sac").at(0, -5, 5);
        sac.box(-3, -2, -3, 6, 2, 6).color(0x9aae5a).grad(0xb8c870, 0x6a7a3a).noise(0.12f).bevel(0.3f);

        m.proc((r, c) -> {
            boolean move = c.swingAmt > 0.01f;
            for (int i = 0; i < SEGS; i++) for (int k = 0; k < 2; k++) for (int sd = -1; sd <= 1; sd += 2) {
                String n = "lg" + i + k + (sd < 0 ? "R" : "L");
                float ph = i * 1.3f + k * 2.1f + (sd < 0 ? 0 : 3.14f);
                float w = move ? (float) Math.sin(c.swing * 1.4f + ph) * 40 * Math.min(1, c.swingAmt * 2)
                               : (float) Math.sin(c.age * 0.25f + ph) * 6;
                r.rot(n, w, 0, 0);
            }
            r.show("antR", c.var == 0); r.show("antL", c.var == 0);
            r.show("cerR", c.var == 2); r.show("cerL", c.var == 2);
            r.rot("antR", (float) Math.sin(c.age * 0.2f) * 8, 0, 0); r.rot("antL", (float) Math.sin(c.age * 0.2f + 1) * 8, 0, 0);
            if (move) for (int i = 0; i < SEGS; i++) r.rot("s" + i, 0, (float) Math.sin(c.swing * 0.7f + i) * 5 * c.swingAmt, 0);
        });
        // walk: a little body undulation (legs are procedural)
        m.clip("walk", 20).loop().pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0.6f, 0).pos("body", 20, 0, 0, 0);
        // idle: peristaltic breathing along the segments
        Clip idle = m.clip("idle", 50).loop();
        for (int i = 0; i < SEGS; i++) {
            int o = i * 5;
            idle.rot("s" + i, 0, 0, 0, 0).rot("s" + i, 12 + o, i == 0 ? -2 : 2, 0, 0).rot("s" + i, 37 + o > 50 ? 50 : 37 + o, i == 0 ? 1 : -1, 0, 0).rot("s" + i, 50, 0, 0, 0);
        }
        idle.scl("sac", 0, 1, 1, 1).scl("sac", 25, 1.1f, 1.2f, 1.1f).scl("sac", 50, 1, 1, 1)
            .scl("frontFlesh", 0, 1, 1, 1).scl("frontFlesh", 25, 1.06f, 1.06f, 1.1f).scl("frontFlesh", 50, 1, 1, 1);

        // BULK: rear up tall (arch tightens, plates swell), then crash the front end down; gains Strength
        Clip b = m.clip("bulk", 36);
        b.rot("s0", 0, 0, 0, 0).rot("s0", 10, -20, 0, 0, Ease.OUT_CUBIC).rot("s0", 14, 8, 0, 0, Ease.IN_CUBIC).rot("s0", 22, 4, 0, 0).rot("s0", 36, 0, 0, 0)
            .rot("s1", 0, 0, 0, 0).rot("s1", 10, -12, 0, 0, Ease.OUT_CUBIC).rot("s1", 14, 10, 0, 0, Ease.IN_CUBIC).rot("s1", 22, 5, 0, 0).rot("s1", 36, 0, 0, 0)
            .rot("s2", 0, 0, 0, 0).rot("s2", 10, -6, 0, 0).rot("s2", 14, 12, 0, 0, Ease.IN_CUBIC).rot("s2", 22, 5, 0, 0).rot("s2", 36, 0, 0, 0)
            .rot("s3", 0, 0, 0, 0).rot("s3", 10, -8, 0, 0).rot("s3", 14, 14, 0, 0, Ease.IN_CUBIC).rot("s3", 22, 6, 0, 0).rot("s3", 36, 0, 0, 0)
            .rot("s4", 0, 0, 0, 0).rot("s4", 10, -14, 0, 0).rot("s4", 14, 20, 0, 0, Ease.IN_CUBIC).rot("s4", 22, 8, 0, 0).rot("s4", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 2, -3).pos("body", 14, 0, -1, 5, Ease.IN_CUBIC).pos("body", 22, 0, 0, 3).pos("body", 36, 0, 0, 0)
            .scl("s1", 0, 1, 1, 1).scl("s1", 10, 1.15f, 1.15f, 1).scl("s1", 24, 1.2f, 1.2f, 1, Ease.OUT_BACK).scl("s1", 36, 1, 1, 1)
            .scl("s2", 0, 1, 1, 1).scl("s2", 10, 1.15f, 1.15f, 1).scl("s2", 24, 1.25f, 1.25f, 1, Ease.OUT_BACK).scl("s2", 36, 1, 1, 1)
            .scl("s3", 0, 1, 1, 1).scl("s3", 10, 1.15f, 1.15f, 1).scl("s3", 24, 1.2f, 1.2f, 1, Ease.OUT_BACK).scl("s3", 36, 1, 1, 1);
        // WRITHE: violent S-shaped side thrash, two lashes (left then right)
        Clip w = m.clip("writhe", 34);
        float[] side = {0, 1, 1, 1, 1};
        for (int i = 0; i < SEGS; i++) {
            float a = i == 0 ? 6 : 16 * side[i];
            w.rot("s" + i, 0, 0, 0, 0).rot("s" + i, 6, 0, -a, i * 3).rot("s" + i, 10, 0, a * 1.3f, -i * 4, Ease.IN_CUBIC)
                .rot("s" + i, 16, 0, -a * 0.6f, 0).rot("s" + i, 20, 0, -a * 1.3f, i * 4, Ease.IN_CUBIC).rot("s" + i, 26, 0, a * 0.4f, 0).rot("s" + i, 34, 0, 0, 0);
        }
        w.pos("body", 0, 0, 0, 0).pos("body", 10, 2, 0, 3, Ease.IN_CUBIC).pos("body", 20, -2, 0, 3, Ease.IN_CUBIC).pos("body", 34, 0, 0, 0);
        // OUTGAS: contract like a bellows, sacs balloon, front stump puffs open and vents gas
        Clip g = m.clip("outgas", 36);
        g.scl("sac", 0, 1, 1, 1).scl("sac", 12, 1.8f, 2.6f, 1.8f, Ease.OUT_BACK).scl("sac", 16, 0.8f, 0.8f, 0.8f, Ease.IN_CUBIC).scl("sac", 36, 1, 1, 1)
            .scl("frontFlesh", 0, 1, 1, 1).scl("frontFlesh", 12, 0.8f, 0.8f, 0.9f).scl("frontFlesh", 15, 1.4f, 1.4f, 1.6f, Ease.OUT_BACK).scl("frontFlesh", 26, 1.1f, 1.1f, 1.1f).scl("frontFlesh", 36, 1, 1, 1)
            .rot("s0", 0, 0, 0, 0).rot("s0", 12, 10, 0, 0).rot("s0", 16, -8, 0, 0, Ease.IN_CUBIC).rot("s0", 36, 0, 0, 0)
            .rot("s2", 0, 0, 0, 0).rot("s2", 12, -10, 0, 0).rot("s2", 16, 4, 0, 0).rot("s2", 36, 0, 0, 0)
            .rot("s4", 0, 0, 0, 0).rot("s4", 12, -20, 0, 0).rot("s4", 16, -30, 0, 0, Ease.OUT_BACK).rot("s4", 26, -25, 0, 0).rot("s4", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -1.5f, -1).pos("body", 16, 0, 0.5f, 1).pos("body", 36, 0, 0, 0);
        // DEAD (base while waiting to Reattach): flattened, rolled on its side
        Clip dead = m.clip("dead", 40).loop();
        deadPose(dead, 0); deadPose(dead, 40);
        dead.scl("frontFlesh", 0, 1, 1, 1).scl("frontFlesh", 20, 1.08f, 1.08f, 1.08f).scl("frontFlesh", 40, 1, 1, 1);
        // REATTACH: twitch, roll back upright and re-arch
        Clip ra = m.clip("reattach", 40);
        deadPose(ra, 0); deadPose(ra, 10);
        ra.rot("body", 14, 0, 0, 20).rot("body", 18, 0, 0, 90);
        for (int i = 0; i < SEGS; i++) ra.rot("s" + i, 26, i == 0 ? -10 : 5, 0, 0, Ease.OUT_BACK).rot("s" + i, 40, 0, 0, 0);
        ra.rot("body", 26, 0, 0, -6, Ease.OUT_BACK).rot("body", 40, 0, 0, 0).pos("body", 26, 0, 1, 0, Ease.OUT_BACK).pos("body", 40, 0, 0, 0);
        m.clip("regrow", 40).loop(); // placeholder so the move has a clip; the dead base shows through
        m.byName.get("s0"); // (no-op)
        Clip regrow = m.clips.get("regrow");
        deadPose(regrow, 0); deadPose(regrow, 20); deadPose(regrow, 40);
        regrow.scl("frontFlesh", 0, 1, 1, 1).scl("frontFlesh", 10, 1.25f, 1.25f, 1.25f).scl("frontFlesh", 20, 1, 1, 1).scl("frontFlesh", 30, 1.25f, 1.25f, 1.25f).scl("frontFlesh", 40, 1, 1, 1)
            .scl("rearFlesh", 0, 1, 1, 1).scl("rearFlesh", 15, 1.25f, 1.25f, 1.25f).scl("rearFlesh", 30, 1, 1, 1);
        m.clip("hurt", 10)
            .rot("s1", 0, 0, 0, 0).rot("s1", 3, 8, 0, 4).rot("s1", 10, 0, 0, 0)
            .rot("s3", 0, 0, 0, 0).rot("s3", 3, -8, 0, -4).rot("s3", 10, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 3, 0, 0, -1.5f).pos("body", 10, 0, 0, 0);
        Clip d = m.clip("death", 30);
        d.rot("body", 0, 0, 0, 0).rot("body", 8, 0, 0, -15).rot("body", 20, 0, 0, 90, Ease.OUT_BOUNCE).rot("body", 30, 0, 0, 90)
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, -1, 0).pos("body", 30, 0, -3, 0);
        for (int i = 0; i < SEGS; i++) d.rot("s" + i, 0, 0, 0, 0).rot("s" + i, 8, i == 0 ? -10 : 6, 0, 0).rot("s" + i, 22, i == 0 ? 50 : -25, 0, 0, Ease.OUT_BOUNCE).rot("s" + i, 30, i == 0 ? 50 : -25, 0, 0);
        return m;
    }

    static void deadPose(Clip c, int t) {
        c.rot("body", t, 0, 0, 90).pos("body", t, 0, -1, 0);
        for (int i = 0; i < SEGS; i++) c.rot("s" + i, t, i == 0 ? 45 : -22, 0, 0);
    }

    static boolean otherAlive(SpireMob s) {
        for (SpireMob o : s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(40)))
            if (o != s && o.isAlive() && o.def.id.startsWith("s2_decimillipede") && o.getPhase() == 0) return true;
        return false;
    }

    static final String[] CYCLE = {"bulk", "writhe", "outgas"};

    public static MobDef def() { return def(0); }

    /** 0 = front (also spawns the other two), 1 = middle, 2 = back */
    public static MobDef def(int v) {
        String id = v == 0 ? "s2_decimillipede" : v == 1 ? "s2_decimillipede_middle" : "s2_decimillipede_back";
        String en = v == 0 ? "Decimillipede (Front)" : v == 1 ? "Decimillipede (Middle)" : "Decimillipede (Back)";
        String ja = v == 0 ? "万足ムカデ（前）" : v == 1 ? "万足ムカデ（中）" : "万足ムカデ（後）";
        MobDef d = MobDef.of(id, en, ja)
            .game(2, 2).size(1.7f, 1.5f).hp(v == 0 ? 42 : v == 1 ? 44 : 46).speed(0.24f).style(Style.SPIDER).egg(0x1c2740, 0xd88a72)
            .elite().model(Decimillipede::model)
            .pattern(s -> {
                if (s.getPhase() == 1) return s.count("dead") >= 2 ? "reattach" : "regrow";
                String mv = CYCLE[(s.count("start") + s.count("cyc")) % 3];
                s.inc("cyc");
                return mv;
            })
            .move(Move.of("bulk", 36).intent(Intent.ATTACK_BUFF, 6, 1).reach(2.4f)
                .sound(4, SoundEvents.SPIDER_AMBIENT, 1f, 0.5f)
                .at(14, H.cone(6, 2.8, 100))
                .at(15, (s, t) -> { Fx.particlesAt(s, ParticleTypes.CLOUD, Fx.rel(s, 1.6, 0, 0.1), 10, 0.4, 0.03); Fx.sound(s, SoundEvents.GENERIC_EXPLODE, 0.5f, 1.6f); })
                .at(24, (s, t) -> { s.addStrength(2); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 6, 0.6); }))
            .move(Move.of("writhe", 34).intent(Intent.ATTACK, 5, 2).reach(2.4f)
                .sound(6, SoundEvents.SILVERFISH_AMBIENT, 1f, 0.6f)
                .at(10, H.cone(5, 2.8, 140)).at(20, H.cone(5, 2.8, 140))
                .sound(20, SoundEvents.SILVERFISH_HURT, 1f, 0.5f))
            .move(Move.of("outgas", 36).intent(Intent.ATTACK_DEBUFF, 8, 1).reach(2.6f)
                .sound(14, SoundEvents.LLAMA_SPIT, 1.2f, 0.4f)
                .at(15, (s, t) -> {
                    for (int i = 1; i <= 4; i++) Fx.particlesAt(s, ParticleTypes.SNEEZE, Fx.rel(s, 0.9 + i * 0.5, 0, 0.6), 10, 0.25 + i * 0.12, 0.02);
                    H.cone(s, 8, 3.2, 90, null, e -> Fx.weak(e, 6));
                }))
            .move(Move.of("regrow", 40).intent(Intent.UNKNOWN).noTarget()
                .at(20, (s, t) -> Fx.particles(s, ParticleTypes.DAMAGE_INDICATOR, 4, 0.5))
                .at(39, (s, t) -> s.inc("dead")))
            .move(Move.of("reattach", 40).intent(Intent.BUFF).noTarget()
                .sound(10, SoundEvents.SLIME_SQUISH, 1.2f, 0.6f)
                .at(18, (s, t) -> { s.setPhase(0); s.setCount("dead", 0); s.setHealth(25); s.setBase("idle");
                    s.setCount("cyc", s.rand().nextInt(3));
                    Fx.particles(s, ParticleTypes.HEART, 6, 0.6); Fx.sound(s, SoundEvents.SLIME_BLOCK_PLACE, 1f, 0.5f); }))
            .onSpawn(s -> {
                s.setVar(v); s.setCount("start", v);
                if (v == 0 && s.getLeader() == null) {   // the front segment brings the rest of the body
                    SpireMob a = Fx.summon(s, "s2_decimillipede_middle", 2.2, 0);
                    SpireMob b = Fx.summon(s, "s2_decimillipede_back", -2.2, 0);
                    if (a != null) a.setLeader(null);
                    if (b != null) b.setLeader(null);
                }
            })
            .onHurt((s, src, amt) -> {
                if (s.flags.contains("truekill")) return amt;
                if (s.getPhase() == 1) return 0;
                if (amt - s.getBlock() * Cfg.DMG >= s.getHealth() && otherAlive(s)) {
                    s.setHealth(1); s.setPhase(1); s.setCount("dead", 0); s.setBase("dead"); s.cancelMove(); s.addBlock(-s.getBlock());
                    Fx.particles(s, ParticleTypes.DAMAGE_INDICATOR, 12, 0.6);
                    Fx.sound(s, SoundEvents.SPIDER_DEATH, 1f, 0.6f);
                    return 0;
                }
                return amt;
            })
            .onTick(s -> {
                if (s.getPhase() == 1 && s.tickCount % 10 == 0 && !otherAlive(s)) {
                    s.flags.add("truekill"); s.hurt(s.damageSources().genericKill(), Float.MAX_VALUE);
                }
            });
        if (v == 0) d.spawn(1, 1, 1, MobDef.Habitat.CAVE);
        return d;
    }
}
