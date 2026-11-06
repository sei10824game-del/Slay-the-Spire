package com.spiremod.mobs.s1act2b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 boss: The Collector. 282 HP. T1 Spawn (2 Torch Heads); T4 Mega Debuff (Weak/Vulnerable/Frail 3); otherwise
 * Revive (if a Torch Head is dead), Fireball 18 (not 3x in a row), Buff (+3 Str to allies, 15 Block to self).
 */
public final class Collector {
    private Collector() {}
    static final int ROBE = 0x1c7a78, ROBE_D = 0x0c4448, ROBE_L = 0x30a898, GOLD = 0xe0b040, GOLD_D = 0x9a7418, BONE = 0xe6dcc0, BONE_D = 0xa89c80, FIRE = 0xff9a20, EYE = 0x7fff9a, WOOD = 0x4a3220;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("collector");
        m.scale(1.8f);
        // legs hidden in the long robe; small feet
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 6, 0);
            leg.box(-1.5f, -6, -1.5f, 3, 6, 3).color(ROBE_D);
            leg.box(-2, -6, -2.5f, 4, 2, 5).color(0x2a2030).bevel(0.2f);
        }
        Part body = m.add("body").at(0, 6, 0);
        Part skirt = body.add("skirt").at(0, 2, 0);
        skirt.box(-5, -8, -3.5f, 10, 8, 7).color(ROBE_D).grad(ROBE, ROBE_D).bevel(0.1f);
        skirt.box(-6, -9, -4.5f, 12, 3, 9).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.15f);
        skirt.box(-6.2f, -9, -4.7f, 12, 1, 9).inflate(0.2f).color(GOLD).flat().noise(0.05f);
        Part torso = body.add("torso").at(0, 2, 0).rot(6, 0, 0);
        torso.box(-4.5f, 0, -3, 9, 10, 6).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.12f);
        torso.box(-1, 0, -3.2f, 2, 10, 1).color(GOLD).flat();                      // gold stole
        torso.box(-4.7f, 3, -3.2f, 9, 2, 6).inflate(0.2f).color(GOLD_D).flat();    // belt
        torso.box(-1.5f, 3.5f, -3.5f, 3, 3, 1).color(GOLD).flat().glow(Face.FRONT, 1, 1, 1, 1, EYE);
        torso.box(-6, 9, -3.5f, 12, 3, 7).color(ROBE_L).grad(ROBE_L, ROBE).bevel(0.25f);   // mantle
        torso.box(-6.3f, 11, -3.8f, 12, 1, 7).inflate(0.2f).color(GOLD).flat();
        // big hanging lantern cage on the back
        Part cage = torso.add("cage").at(0, 8, -3.5f);
        cage.box(-3, -4, -5, 6, 8, 5).color(GOLD_D).flat();
        cage.box(-2, -3, -4.5f, 4, 6, 4).color(FIRE).flat().glow(Face.BACK, 0, 0, 4, 6, FIRE).glow(Face.TOP, 0, 0, 4, 4, FIRE);
        // head: pale skull-mask with tall hood
        Part head = torso.add("head").at(0, 12, 0).rot(-6, 0, 0);
        head.box(-3.5f, 0, -3.5f, 7, 7, 7).color(BONE).grad(BONE, BONE_D).bevel(0.15f)
            .paint(Face.FRONT, 1, 2, 2, 2, 0x100818).paint(Face.FRONT, 4, 2, 2, 2, 0x100818)
            .glow(Face.FRONT, 1, 2, 1, 1, EYE).glow(Face.FRONT, 5, 2, 1, 1, EYE)
            .paint(Face.FRONT, 3, 4, 1, 2, 0x100818).paint(Face.FRONT, 1, 6, 5, 1, 0x100818)
            .px(Face.FRONT, 2, 6, BONE).px(Face.FRONT, 4, 6, BONE);
        head.box(-4.2f, 3, -3f, 8, 5, 7).color(ROBE_D).grad(ROBE_L, ROBE_D).bevel(0.15f);        // hood
        head.box(-4, 7, -2, 8, 3, 5).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.15f);
        head.box(-2.5f, 10, -1, 5, 3, 3).color(ROBE_D).bevel(0.15f);                              // hood peak
        head.box(-3.8f, 0.5f, -5, 8, 7, 2).color(ROBE_D).bevel(0.1f);
        head.box(-4, 6.5f, -3.6f, 8, 1, 1).color(GOLD).flat();                                    // brow band
        // arms: right holds the staff with a fire orb, left clutches a bell-like censer
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 7, 10, 0);
            arm.box(-2, -6, -2, 4, 6, 4).color(ROBE).grad(ROBE_L, ROBE_D).bevel(0.1f);
            arm.box(-3, -8, -3, 6, 3, 6).color(ROBE_D).bevel(0.2f);                                // wide sleeve
            arm.box(-3.2f, -8.2f, -3.2f, 6, 1, 6).inflate(0.2f).color(GOLD).flat();
            Part fore = arm.add(s < 0 ? "foreR" : "foreL").at(0, -6, 0);
            Part hand = fore.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1.3f, -3, -1.3f, 3, 3, 3).color(BONE_D);
            if (s < 0) {
                Part staff = hand.add("staff").at(0, -1.5f, 0).rot(-8, 0, 0);
                staff.box(-0.7f, -8, -0.7f, 1, 30, 1).color(WOOD).noise(0.1f);
                staff.box(-1.2f, 22, -1.2f, 2, 1, 2).color(GOLD).flat();
                Part orb = staff.add("orb").at(0, 25, 0);
                orb.box(-2, -2, -2, 4, 4, 4).color(FIRE).flat().glow(Face.FRONT, 0, 0, 4, 4, FIRE).glow(Face.TOP, 0, 0, 4, 4, 0xffe060).glow(Face.LEFT, 0, 0, 4, 4, FIRE).glow(Face.RIGHT, 0, 0, 4, 4, FIRE).glow(Face.BACK, 0, 0, 4, 4, FIRE);
                orb.box(-3, -0.5f, -3, 6, 1, 6).color(GOLD).flat();
                orb.box(-0.5f, 2, -0.5f, 1, 3, 1).color(GOLD).flat();
            }
        }
        m.proc((r, c) -> { r.pos("orb", 0, (float) Math.sin(c.age * 0.2f) * 0.6f, 0); r.scl("orb", 1 + (float) Math.sin(c.age * 0.5f) * 0.1f, 1 + (float) Math.sin(c.age * 0.5f) * 0.1f, 1 + (float) Math.sin(c.age * 0.5f) * 0.1f); });
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 20f, 12f);
        m.clip("idle", 60).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 30, -2, 0, 0).rot("torso", 60, 1.5f, 0, 0)
            .rot("head", 0, 0, 5, 0).rot("head", 30, 0, -5, 0).rot("head", 60, 0, 5, 0)
            .rot("armL", 0, 8, 0, -6).rot("armL", 30, 14, 0, -8).rot("armL", 60, 8, 0, -6)
            .rot("armR", 0, 4, 0, 4).rot("armR", 30, 7, 0, 5).rot("armR", 60, 4, 0, 4)
            .rot("skirt", 0, 0, 2, 0).rot("skirt", 30, 0, -2, 0).rot("skirt", 60, 0, 2, 0)
            .rot("cage", 0, 0, 0, 5).rot("cage", 30, 0, 0, -5).rot("cage", 60, 0, 0, 5);
        // SPAWN: staff slammed down, both arms flung up, head back, flames surge
        m.clip("spawn", 54)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, 14, 0, 0).rot("torso", 22, -18, 0, 0, Ease.OUT_BACK).rot("torso", 42, -14, 0, 0).rot("torso", 54, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -20, 0, 10).rot("armR", 22, -150, 0, 30, Ease.OUT_BACK).rot("armR", 42, -150, 0, 32).rot("armR", 54, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -20, 0, -10).rot("armL", 22, -150, 0, -30, Ease.OUT_BACK).rot("armL", 42, -150, 0, -32).rot("armL", 54, 0, 0, 0)
            .rot("staff", 0, 0, 0, 0).rot("staff", 22, 100, 0, 0).rot("staff", 54, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, 20, 0, 0).rot("head", 22, -40, 0, 0, Ease.OUT_BACK).rot("head", 42, -35, 0, 0).rot("head", 54, 0, 0, 0)
            .scl("cage", 0, 1, 1, 1).scl("cage", 22, 1.4f, 1.4f, 1.4f).scl("cage", 54, 1, 1, 1)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 0, -2, 0).pos("body", 22, 0, 2, 0, Ease.OUT_BACK).pos("body", 54, 0, 0, 0);
        // FIREBALL: swing the staff back, then thrust it forward; orb flares
        m.clip("fireball", 36)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -10, -25, 0, Ease.OUT_CUBIC).rot("torso", 15, 18, 20, 0, Ease.IN_CUBIC).rot("torso", 26, 14, 15, 0).rot("torso", 36, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -140, 0, 30, Ease.OUT_CUBIC).rot("armR", 15, -80, 0, -5, Ease.IN_CUBIC).rot("armR", 26, -75, 0, -5).rot("armR", 36, 0, 0, 0)
            .rot("staff", 0, 0, 0, 0).rot("staff", 10, 40, 0, 0).rot("staff", 15, 85, 0, 0).rot("staff", 36, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -60, 0, -25).rot("armL", 15, -110, 0, -10, Ease.OUT_BACK).rot("armL", 36, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 12, 20, 0).rot("head", 15, 0, -10, 0).rot("head", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -2).pos("body", 15, 0, -1, 3, Ease.IN_CUBIC).pos("body", 36, 0, 0, 0);
        // BUFF: arms spread, cage glows, shudder with raised staff
        m.clip("buff", 42)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -16, 0, 0, Ease.OUT_BACK).rot("torso", 30, -14, 0, 0).rot("torso", 42, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -45, 0, 80, Ease.OUT_BACK).rot("armR", 30, -45, 0, 85).rot("armR", 42, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -45, 0, -80, Ease.OUT_BACK).rot("armL", 30, -45, 0, -85).rot("armL", 42, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 30, -25, 0, 0).rot("head", 42, 0, 0, 0)
            .scl("cage", 0, 1, 1, 1).scl("cage", 14, 1.5f, 1.5f, 1.5f).scl("cage", 34, 1.3f, 1.3f, 1.3f).scl("cage", 42, 1, 1, 1)
            .rot("skirt", 0, 0, 0, 0).rot("skirt", 16, 0, 10, 0).rot("skirt", 22, 0, -10, 0).rot("skirt", 28, 0, 10, 0).rot("skirt", 42, 0, 0, 0);
        // MEGA DEBUFF: stoop, gather, then both hands thrust forward and unleash a curse; robe whips
        m.clip("mega_debuff", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, 24, 0, 0, Ease.OUT_CUBIC).rot("torso", 22, -12, 0, 0, Ease.OUT_BACK).rot("torso", 40, -8, 0, 0).rot("torso", 50, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, 10, 0, 60).rot("armR", 22, -100, 15, 10, Ease.OUT_BACK).rot("armR", 40, -100, 15, 10).rot("armR", 50, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, 10, 0, -60).rot("armL", 22, -100, -15, -10, Ease.OUT_BACK).rot("armL", 40, -100, -15, -10).rot("armL", 50, 0, 0, 0)
            .rot("staff", 0, 0, 0, 0).rot("staff", 22, 80, 0, 0).rot("staff", 50, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 28, 0, 0).rot("head", 22, -20, 0, 0).rot("head", 50, 0, 0, 0)
            .rot("skirt", 0, 0, 0, 0).rot("skirt", 22, 0, 15, 0).rot("skirt", 28, 0, -15, 0).rot("skirt", 34, 0, 10, 0).rot("skirt", 50, 0, 0, 0)
            .scl("cage", 0, 1, 1, 1).scl("cage", 22, 1.6f, 1.6f, 1.6f).scl("cage", 50, 1, 1, 1);
        m.clip("revive", 46)
            .rot("torso", 0, 0, 0, 0).rot("torso", 14, 18, 0, 0).rot("torso", 26, -10, 0, 0, Ease.OUT_BACK).rot("torso", 46, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -50, 0, 20).rot("armR", 26, -120, 0, 30, Ease.OUT_BACK).rot("armR", 46, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -80, 0, -10).rot("armL", 26, -130, 0, -30, Ease.OUT_BACK).rot("armL", 46, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 25, 0, 0).rot("head", 26, -30, 0, 0).rot("head", 46, 0, 0, 0)
            .rot("staff", 0, 0, 0, 0).rot("staff", 26, 50, 0, 0).rot("staff", 46, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 12, 0, 4).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 0, 8).rot("head", 10, 0, 0, 0);
        m.clip("death", 50)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, -3, 0, Ease.IN_QUAD).pos("body", 38, 0, -5, 4, Ease.OUT_BOUNCE).pos("body", 50, 0, -5, 4)
            .rot("torso", 0, 0, 0, 0).rot("torso", 16, -12, 0, 0).rot("torso", 38, 75, 0, 0, Ease.IN_QUAD).rot("torso", 50, 72, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 16, -50, 0, 0).rot("legR", 50, -70, 0, 5)
            .rot("legL", 0, 0, 0, 0).rot("legL", 16, -50, 0, 0).rot("legL", 50, -70, 0, -5)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -90, 0, 30).rot("armR", 50, -140, 0, 40)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -30).rot("armL", 50, -140, 0, -40)
            .rot("staff", 0, 0, 0, 0).rot("staff", 20, 70, 0, 30).rot("staff", 50, 90, 0, 50)
            .scl("cage", 0, 1, 1, 1).scl("cage", 20, 1.5f, 1.5f, 1.5f).scl("cage", 50, 0.1f, 0.1f, 0.1f)
            .rot("head", 0, 0, 0, 0).rot("head", 16, -30, 0, 0).rot("head", 50, 25, 15, 10);
        return m;
    }

    static int alive(SpireMob s) { return s.minions().size(); }

    static String next(SpireMob s) {
        int turn = s.inc("turns");
        if (turn == 1) return "spawn";
        if (turn >= 4 && s.count("mega") == 0) { s.inc("mega"); return "mega_debuff"; }
        int r = s.rand().nextInt(100);
        if (alive(s) < 2 && !s.lastMove().equals("revive") && s.count("spawned") > 0 && r < 60) return "revive";
        boolean fb2 = s.lastMove().equals("fireball") && s.moveAgo(1).equals("fireball");
        if (r < 70 && !fb2) return "fireball";
        return s.lastMove().equals("buff") ? "fireball" : "buff";
    }

    static void spawnHeads(SpireMob s) {
        int need = 2 - alive(s);
        for (int i = 0; i < need; i++) { double a = (i == 0 ? 1 : -1) * 0.9 + s.getYRot() * Math.PI / 180; Fx.summon(s, "torch_head", Math.cos(a) * 4, Math.sin(a) * 4); }
        s.inc("spawned");
        Fx.particles(s, ParticleTypes.FLAME, 30, 1.5); Fx.sound(s, SoundEvents.BLAZE_AMBIENT, 1.4f, 0.5f);
    }

    public static MobDef def() {
        return MobDef.of("the_collector", "The Collector", "コレクター")
            .game(1, 2).size(1.4f, 3.6f).hp(282).speed(0.22f).style(Style.GHOST).egg(0x4a2a6a, 0xe0b040)
            .model(Collector::model).boss().kbResist(1f)
            .pattern(Collector::next)
            .move(Move.of("spawn", 54).intent(Intent.SUMMON).noTarget()
                .sound(16, SoundEvents.EVOKER_PREPARE_SUMMON, 1.5f, 0.6f)
                .at(24, (s, t) -> spawnHeads(s)))
            .move(Move.of("revive", 46).intent(Intent.SUMMON).noTarget()
                .sound(14, SoundEvents.EVOKER_PREPARE_SUMMON, 1.5f, 0.8f)
                .at(26, (s, t) -> spawnHeads(s)))
            .move(Move.of("fireball", 36).intent(Intent.ATTACK, 18, 1).ranged(14).faceUntil(18)
                .sound(10, SoundEvents.BLAZE_SHOOT, 1.4f, 0.7f)
                .at(15, (s, t) -> { Fx.sound(s, SoundEvents.GHAST_SHOOT, 1.4f, 0.8f); Fx.sound(s, SoundEvents.FIRECHARGE_USE, 1.2f, 0.8f);
                    Fx.particlesAt(s, ParticleTypes.FLAME, Fx.rel(s, 2.4, -0.8, 2.2), 20, 0.3, 0.1);
                    if (t != null) { Fx.particlesAt(s, ParticleTypes.LAVA, t.position().add(0, 1, 0), 15, 0.4, 0.1); Fx.particlesAt(s, ParticleTypes.EXPLOSION, t.position().add(0, 1, 0), 2, 0.2, 0); Fx.dmg(s, t, 18); } }))
            .move(Move.of("buff", 42).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(12, SoundEvents.BEACON_POWER_SELECT, 1.4f, 0.6f)
                .at(16, (s, t) -> { s.addBlock(15); for (SpireMob g : s.minions()) { g.addStrength(3); Fx.particlesAt(s, ParticleTypes.ANGRY_VILLAGER, g.position().add(0, 1, 0), 8, 0.3, 0.02); }
                    Fx.particles(s, ParticleTypes.ENCHANT, 40, 1.2); }))
            .move(Move.of("mega_debuff", 50).intent(Intent.STRONG_DEBUFF).ranged(16).faceUntil(26)
                .sound(14, SoundEvents.WITHER_SPAWN, 1.2f, 1.6f).sound(22, SoundEvents.EVOKER_CAST_SPELL, 1.6f, 0.6f)
                .at(24, (s, t) -> { if (t != null) { Fx.weak(t, 6); Fx.vulnerable(t, 6); Fx.frail(t, 6); Fx.particlesAt(s, ParticleTypes.SOUL, t.position().add(0, 1, 0), 30, 0.5, 0.1); } Fx.particles(s, ParticleTypes.WITCH, 30, 1.2); }));
    }
}
