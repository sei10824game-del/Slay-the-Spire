package com.spiremod.mobs.s1act3a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 3 Darkling (48-56 HP). Life Link: at 0 HP it collapses into a puddle (untargetable) while another Darkling lives,
 * uses Regrow (nothing) then Reincarnate (revive at 50% HP). If every Darkling is down, they all die.
 * Turn 1: 50% Nip / 50% Harden. Then 30% Nip (D = 7..11), 40% Chomp (8x2), 30% Harden (12 block);
 * no Harden/Chomp twice in a row, no Nip three times.
 */
public final class Darkling {
    private Darkling() {}

    static final int DARK = 0x1f1a30, MID = 0x342b52, LIGHT = 0x5d5088, RIM = 0x8c7cc0, BONE = 0xe0d6bc, GUM = 0x7a2440, EYE = 0xffd23a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("darkling");
        // ---- hind legs (digitigrade, pivot at hip)
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3.5f, 7, -3);
            leg.box(-1.5f, -4, -2, 3, 5, 4).color(MID).grad(LIGHT, MID).bevel(0.15f);
            Part shin = leg.add(s < 0 ? "shinR" : "shinL").at(0, -4, 1).rot(-20, 0, 0);
            shin.box(-1, -3, -1, 2, 4, 2).color(DARK);
            shin.box(-1.5f, -3, -1, 3, 1, 4).color(DARK).paint(Face.FRONT, 0, 0, 3, 1, BONE);
        }
        // ---- body: hunched, carapaced
        Part body = m.add("body").at(0, 7, -1);
        body.box(-5, -1, -6, 10, 8, 11).color(MID).grad(LIGHT, DARK).bevel(0.2f).noise(0.12f);
        body.box(-4, -2, -5, 8, 2, 9).color(DARK).noise(0.05f);                                     // belly
        Part shell = body.add("shell").at(0, 6, -1);
        shell.box(-5.5f, 0, -5.5f, 11, 3, 11).color(DARK).grad(RIM, DARK).bevel(0.25f);            // carapace dome
        shell.box(-4, 3, -4, 8, 2, 8).color(MID).grad(RIM, MID).bevel(0.2f);
        for (int i = 0; i < 4; i++) {                                                                // dorsal spines
            Part sp = shell.add("spine" + i).at(0, 4, -3.5f + i * 2.4f).rot(-25 + i * 6, 0, 0);
            sp.box(-0.5f, 0, -0.5f, 1, 4 - (i == 0 ? 1 : 0), 1).color(BONE).flat().paint(Face.FRONT, 0, 0, 1, 1, 0xfff4dc);
            sp.box(-1, -1, -1, 2, 2, 2).color(DARK);
        }
        for (int s = -1; s <= 1; s += 2) {                                                           // side spikes
            Part sp = shell.add(s < 0 ? "sideR" : "sideL").at(s * 5.5f, 1, 0).rot(0, 0, s * -60);
            sp.box(-0.5f, 0, -0.5f, 1, 3, 1).color(BONE).flat();
        }
        // ---- head + jaws
        Part neck = body.add("neck").at(0, 3, 5);
        neck.box(-2.5f, -2, -1, 5, 5, 4).color(MID).grad(LIGHT, DARK);
        Part head = neck.add("head").at(0, 1, 3);
        head.box(-3.5f, -1, -1, 7, 5, 6).color(MID).grad(RIM, MID).bevel(0.2f)
            .glow(Face.FRONT, 1, 1, 2, 1, EYE).glow(Face.FRONT, 4, 1, 2, 1, EYE)
            .paint(Face.FRONT, 0, 0, 7, 1, DARK);
        head.box(-2.5f, -1, 5, 5, 3, 3).color(MID).grad(LIGHT, DARK);                              // snout
        head.box(-2.5f, -1.5f, 5, 5, 1, 3).color(BONE).flat();                                      // upper teeth row
        head.box(-3, 4, 0, 1, 3, 1).color(BONE).flat();                                              // horns
        head.box(2, 4, 0, 1, 3, 1).color(BONE).flat();
        Part jaw = head.add("jaw").at(0, -1, 0);
        jaw.box(-3, -2, 0, 6, 2, 8).color(DARK).paint(Face.TOP, 0, 0, 6, 8, GUM);
        jaw.box(-2.5f, 0, 5, 5, 1, 3).color(BONE).flat();                                           // lower fangs
        // ---- fore claws (pivot at shoulder)
        for (int s = -1; s <= 1; s += 2) {
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 5, 3, 3);
            arm.box(-1.5f, -6, -1.5f, 3, 7, 3).color(MID).grad(LIGHT, DARK).bevel(0.15f);
            Part claw = arm.add(s < 0 ? "clawR" : "clawL").at(0, -6, 0).rot(-30, 0, 0);
            claw.box(-1.5f, -3, -1, 3, 3, 3).color(DARK);
            claw.box(-1.5f, -4, 1, 1, 2, 2).color(BONE).flat();
            claw.box(0.5f, -4, 1, 1, 2, 2).color(BONE).flat();
        }
        m.look("head", 40, 25);
        m.walkQuad("armR", "armL", "legR", "legL", 32f);
        m.clip("idle", 40).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 20, 0, -0.6f, 0).pos("body", 40, 0, 0, 0)
            .rot("shell", 0, 0, 0, 0).rot("shell", 20, -3, 0, 0).rot("shell", 40, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 10, 4, 10, 0).rot("neck", 30, 4, -10, 0).rot("neck", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 24, 0, 0, 0).rot("jaw", 27, 18, 0, 0).rot("jaw", 30, 0, 0, 0).rot("jaw", 33, 14, 0, 0).rot("jaw", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 20, 5, 0, 3).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 20, 5, 0, -3).rot("armL", 40, 0, 0, 0);
        // NIP: quick coil then snapping jab of the head
        m.clip("nip", 22)
            .rot("body", 0, 0, 0, 0).rot("body", 7, -10, 0, 0).rot("body", 10, 14, 0, 0, Ease.IN_CUBIC).rot("body", 22, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 7, 0, 0, -2).pos("body", 10, 0, -1, 4, Ease.IN_CUBIC).pos("body", 22, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 7, -25, 0, 0).rot("neck", 10, 20, 0, 0, Ease.IN_CUBIC).rot("neck", 22, 0, 0, 0)
            .pos("head", 0, 0, 0, 0).pos("head", 7, 0, 0, -1.5f).pos("head", 10, 0, 0, 3, Ease.IN_CUBIC).pos("head", 15, 0, 0, 2.5f).pos("head", 22, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 7, 50, 0, 0).rot("jaw", 10, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 13, 15, 0, 0).rot("jaw", 15, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 10, 20, 0, 0).rot("legR", 22, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 10, 20, 0, 0).rot("legL", 22, 0, 0, 0);
        // CHOMP: rear up, jaw gaping, crash down biting twice
        m.clip("chomp", 36)
            .rot("body", 0, 0, 0, 0).rot("body", 9, -32, 0, 0).rot("body", 12, 18, 0, 0, Ease.IN_CUBIC).rot("body", 17, -18, 0, 0).rot("body", 22, 20, 0, 0, Ease.IN_CUBIC).rot("body", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 3, -1).pos("body", 12, 0, -1, 3, Ease.IN_CUBIC).pos("body", 17, 0, 2, 2).pos("body", 22, 0, -1.5f, 4, Ease.IN_CUBIC).pos("body", 36, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 9, 60, 0, 0).rot("jaw", 12, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 17, 60, 0, 0).rot("jaw", 22, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 28, 10, 0, 0).rot("jaw", 36, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 9, -20, 0, 0).rot("neck", 12, 25, 12, 0, Ease.IN_CUBIC).rot("neck", 17, -20, 0, 0).rot("neck", 22, 25, -12, 0, Ease.IN_CUBIC).rot("neck", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 9, -110, 0, -20).rot("armR", 12, -30, 0, 0, Ease.IN_CUBIC).rot("armR", 17, -100, 0, -20).rot("armR", 22, -30, 0, 0, Ease.IN_CUBIC).rot("armR", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 9, -110, 0, 20).rot("armL", 12, -30, 0, 0, Ease.IN_CUBIC).rot("armL", 17, -100, 0, 20).rot("armL", 22, -30, 0, 0, Ease.IN_CUBIC).rot("armL", 36, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 9, 25, 0, 0).rot("legR", 22, 15, 0, 0).rot("legR", 36, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 9, 25, 0, 0).rot("legL", 22, 15, 0, 0).rot("legL", 36, 0, 0, 0);
        // HARDEN: curl into its shell, spines flare outward, shudder
        m.clip("harden", 34)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, -2.5f, -1, Ease.OUT_BACK).pos("body", 26, 0, -2.5f, -1).pos("body", 34, 0, 0, 0)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 12, 0, 0).rot("body", 26, 12, 0, 0).rot("body", 34, 0, 0, 0)
            .scl("shell", 0, 1, 1, 1).scl("shell", 8, 1.2f, 1.25f, 1.2f, Ease.OUT_BACK).scl("shell", 26, 1.2f, 1.25f, 1.2f).scl("shell", 34, 1, 1, 1)
            .rot("neck", 0, 0, 0, 0).rot("neck", 8, 45, 0, 0).rot("neck", 26, 45, 0, 0).rot("neck", 34, 0, 0, 0)
            .pos("neck", 0, 0, 0, 0).pos("neck", 8, 0, -1, -3).pos("neck", 26, 0, -1, -3).pos("neck", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -60, -30, 40).rot("armR", 26, -60, -30, 40).rot("armR", 34, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -60, 30, -40).rot("armL", 26, -60, 30, -40).rot("armL", 34, 0, 0, 0)
            .rot("spine0", 0, 0, 0, 0).rot("spine0", 9, -25, 0, 0, Ease.OUT_BACK).rot("spine0", 26, -25, 0, 0).rot("spine0", 34, 0, 0, 0)
            .rot("spine3", 0, 0, 0, 0).rot("spine3", 9, 25, 0, 0, Ease.OUT_BACK).rot("spine3", 26, 25, 0, 0).rot("spine3", 34, 0, 0, 0)
            .rot("sideR", 0, 0, 0, 0).rot("sideR", 9, 0, 0, -25, Ease.OUT_BACK).rot("sideR", 26, 0, 0, -25).rot("sideR", 34, 0, 0, 0)
            .rot("sideL", 0, 0, 0, 0).rot("sideL", 9, 0, 0, 25, Ease.OUT_BACK).rot("sideL", 26, 0, 0, 25).rot("sideL", 34, 0, 0, 0)
            .rot("shell", 0, 0, 0, 0).rot("shell", 12, 0, 0, 0).rot("shell", 14, 0, 0, 4).rot("shell", 16, 0, 0, -4).rot("shell", 18, 0, 0, 3).rot("shell", 20, 0, 0, 0);
        // ---- collapsed puddle (Life Link half-death) : stance + Regrow (pulsing) + Reincarnate (rises)
        Clip dl = m.clip("dead", 40).loop(); deadPose(dl, 0, 1f); deadPose(dl, 40, 1f);
        dl.scl("shell", 0, 1, 1, 1).scl("shell", 20, 1.05f, 0.9f, 1.05f).scl("shell", 40, 1, 1, 1);
        Clip rg = m.clip("regrow", 40);
        deadPose(rg, 0, 1f); deadPose(rg, 40, 1f);
        rg.scl("shell", 0, 1, 1, 1).scl("shell", 10, 1.15f, 1.3f, 1.15f).scl("shell", 20, 0.95f, 0.9f, 0.95f).scl("shell", 30, 1.15f, 1.3f, 1.15f).scl("shell", 40, 1, 1, 1)
            .rot("spine1", 0, 0, 0, 0).rot("spine1", 20, -30, 0, 0).rot("spine1", 40, 0, 0, 0)
            .rot("spine2", 0, 0, 0, 0).rot("spine2", 20, 30, 0, 0).rot("spine2", 40, 0, 0, 0);
        Clip rc = m.clip("reincarnate", 40);
        deadPose(rc, 0, 1f); deadPose(rc, 14, 1f);
        rc.pos("body", 26, 0, 3, 0, Ease.OUT_BACK).scl("body", 26, 0.9f, 1.15f, 0.9f, Ease.OUT_BACK).rot("body", 26, -20, 0, 0)
            .rot("neck", 26, -40, 0, 0).rot("jaw", 26, 50, 0, 0).rot("armR", 26, -140, 0, -30).rot("armL", 26, -140, 0, 30)
            .rot("legR", 26, 0, 0, 0).rot("legL", 26, 0, 0, 0)
            .pos("body", 40, 0, 0, 0, Ease.OUT_BOUNCE).scl("body", 40, 1, 1, 1).rot("body", 40, 0, 0, 0).rot("neck", 40, 0, 0, 0).rot("jaw", 40, 0, 0, 0)
            .rot("armR", 40, 0, 0, 0).rot("armL", 40, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -12, 0, 6).rot("body", 10, 0, 0, 0)
            .rot("neck", 0, 0, 0, 0).rot("neck", 3, -25, 0, 0).rot("neck", 10, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 40, 0, 0).rot("jaw", 10, 0, 0, 0);
        Clip d = m.clip("death", 30);
        deadPose(d, 30, 1f);
        d.pos("body", 0, 0, 0, 0).rot("body", 0, 0, 0, 0).scl("body", 0, 1, 1, 1).rot("neck", 0, 0, 0, 0).rot("jaw", 0, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armL", 0, 0, 0, 0).rot("legR", 0, 0, 0, 0).rot("legL", 0, 0, 0, 0)
            .rot("neck", 10, -50, 0, 0).rot("jaw", 10, 60, 0, 0).pos("body", 10, 0, 2, 0);
        return m;
    }

    /** melted, spread-out heap */
    static void deadPose(Clip c, float t, float k) {
        c.pos("body", t, 0, -6 * k, 0).rot("body", t, 0, 0, 0).scl("body", t, 1.35f, 0.45f, 1.3f)
            .rot("neck", t, 60, 0, 0).rot("jaw", t, 30, 0, 0)
            .rot("armR", t, -80, 0, -70).rot("armL", t, -80, 0, 70)
            .rot("legR", t, -80, 0, -40).rot("legL", t, -80, 0, 40);
    }

    static boolean otherAlive(SpireMob s) {
        for (SpireMob o : s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(32)))
            if (o != s && o.isAlive() && o.def.id.equals("darkling") && o.getPhase() == 0) return true;
        return false;
    }

    static void nip(SpireMob s) { Fx.meleeNow(s, s.count("nipD"), 2.6, 80, null); }

    public static MobDef def() {
        return MobDef.of("darkling", "Darkling", "ダークリング")
            .game(1, 3).size(0.9f, 1.0f).hp(52).speed(0.27f).style(Style.SPIDER).egg(0x1f1a30, 0xffd23a)
            .model(Darkling::model)
            .spawn(6, 3, 3, MobDef.Habitat.CAVE)
            .pattern(s -> {
                if (s.getPhase() == 1) return s.count("dead") == 1 ? "regrow" : "reincarnate";
                if (s.getTurn() == 0) return s.rand().nextBoolean() ? "nip" : "harden";
                String last = s.lastMove();
                for (int i = 0; i < 20; i++) {
                    int r = s.rand().nextInt(100);
                    String id = r < 30 ? "nip" : r < 70 ? "chomp" : "harden";
                    if ((id.equals("harden") || id.equals("chomp")) && last.equals(id)) continue;
                    if (id.equals("nip") && s.repeatCount("nip") >= 2) continue;
                    return id;
                }
                return "nip";
            })
            .move(Move.of("nip", 22).intent(Intent.ATTACK, 9, 1).reach(2.2f)
                .sound(8, SoundEvents.SPIDER_AMBIENT, 0.8f, 1.5f)
                .at(10, (s, t) -> nip(s)))
            .move(Move.of("chomp", 36).intent(Intent.ATTACK, 8, 2).reach(2.2f)
                .sound(8, SoundEvents.RAVAGER_ROAR, 0.4f, 1.8f)
                .at(12, Fx.melee(8, 2.6, 90)).at(22, Fx.melee(8, 2.6, 90)))
            .move(Move.of("harden", 34).intent(Intent.DEFEND).noTarget()
                .at(9, Fx.block(12)))
            .move(Move.of("regrow", 40).intent(Intent.UNKNOWN).noTarget()
                .at(20, (s, t) -> Fx.particles(s, ParticleTypes.SQUID_INK, 10, 0.4))
                .at(39, (s, t) -> s.inc("dead")))
            .move(Move.of("reincarnate", 40).intent(Intent.BUFF).noTarget()
                .sound(14, SoundEvents.ZOMBIE_VILLAGER_CURE, 0.6f, 1.6f)
                .at(16, (s, t) -> { s.setPhase(0); s.setCount("dead", 0); s.setHealth(s.getMaxHealth() * 0.5f); s.setBase("idle");
                    Fx.particles(s, ParticleTypes.REVERSE_PORTAL, 30, 0.5); }))
            .onSpawn(s -> s.setCount("nipD", 7 + s.rand().nextInt(5)))
            .onHurt((s, src, amt) -> {
                if (s.flags.contains("truekill")) return amt;
                if (s.getPhase() == 1) return 0;                    // untargetable puddle
                if (amt >= s.getHealth() && otherAlive(s)) {
                    s.setHealth(1); s.setPhase(1); s.setCount("dead", 1); s.setBase("dead"); s.cancelMove(); s.addBlock(-s.getBlock());
                    Fx.particles(s, ParticleTypes.SQUID_INK, 25, 0.5);
                    Fx.sound(s, SoundEvents.SLIME_DEATH, 1f, 0.6f);
                    return 0;
                }
                return amt;
            })
            .onTick(s -> {
                if (s.getPhase() == 1 && s.tickCount % 10 == 0 && !otherAlive(s)) {
                    s.flags.add("truekill"); s.hurt(s.damageSources().genericKill(), Float.MAX_VALUE);
                }
            });
    }
}
