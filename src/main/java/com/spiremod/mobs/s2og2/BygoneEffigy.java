package com.spiremod.mobs.s2og2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Overgrowth elite Bygone Effigy (127 HP). Starts asleep (turn 1 Sleep), then Wake (+10 Strength), then Slash 13 forever.
 * Slow: every hit taken during a turn makes it take 10% more damage from further hits that turn.
 */
public final class BygoneEffigy {
    private BygoneEffigy() {}

    static final int STONE = 0x8a9484, STONE_D = 0x4f5a4d, STONE_L = 0xaab4a0, MOSS = 0x5f8a3a, MOSS_D = 0x3d5e26, EYE = 0xffb83a, RUNE = 0x58e0c8;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_bygone_effigy").scale(1.15f);
        // ---- plinth the idol hovers over
        Part base = m.add("base").at(0, 0, 0);
        base.box(-12, 0, -9, 24, 4, 18).color(STONE_D).grad(STONE, STONE_D).bevel(0.2f).noise(0.12f);
        base.box(-10, 4, -7, 20, 3, 14).color(STONE).bevel(0.2f).noise(0.1f).paint(Face.FRONT, 3, 0, 14, 1, MOSS);
        base.box(-13, 0, -2, 4, 2, 6).color(MOSS_D).noise(0.2f);
        base.box(8, 0, 3, 5, 2, 5).color(MOSS).noise(0.2f);
        // ---- body (carved chest, hovers)
        Part body = m.add("body").at(0, 8, 0);
        body.box(-8, 0, -5, 16, 12, 10).color(STONE).grad(STONE_L, STONE_D).bevel(0.2f).noise(0.12f)
            .glow(Face.FRONT, 7, 3, 2, 2, RUNE).glow(Face.FRONT, 6, 6, 4, 1, RUNE).glow(Face.FRONT, 7, 8, 2, 2, RUNE)
            .paint(Face.FRONT, 0, 10, 16, 2, MOSS_D);
        body.box(-9, 9, -6, 18, 3, 12).color(STONE_D).inflate(0.2f).noise(0.1f); // collar slab
        // ---- the huge head
        Part head = body.add("head").at(0, 12, 0);
        head.box(-10, 5, -9, 20, 17, 18).color(STONE).grad(STONE_L, STONE).bevel(0.18f).noise(0.14f)
            .paint(Face.FRONT, 0, 0, 20, 2, STONE_D).paint(Face.LEFT, 2, 4, 3, 8, MOSS_D).paint(Face.RIGHT, 14, 3, 3, 9, MOSS_D);
        head.box(-11, 13, 4, 22, 3, 6).color(STONE_D).bevel(0.2f).noise(0.1f);                // heavy brow ridge
        head.box(-11, 13, -10, 22, 3, 3).color(STONE_D).bevel(0.2f);
        head.box(-8, 21, -8, 16, 3, 16).color(MOSS).grad(0x79a846, MOSS_D).noise(0.25f);     // mossy crown
        head.box(-3, 24, -2, 6, 2, 5).color(0x8cc152).noise(0.25f);
        head.box(-10.5f, 16, 5, 3, 7, 3).color(MOSS_D).noise(0.2f);                          // vines hanging down the side
        head.box(7.5f, 10, 5, 3, 12, 3).color(MOSS).noise(0.2f);
        head.box(-1, 11, 8.5f, 2, 5, 1).color(STONE_D).bevel(0.15f);                         // nose ridge
        head.box(-2, 7, 9, 4, 4, 1).color(STONE_D).noise(0.1f);
        head.box(-0.5f, 15.5f, 8.8f, 1, 2, 1).color(RUNE).flat().glow(Face.FRONT, 0, 0, 1, 1, RUNE); // third-eye rune
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            head.box(s * 5.5f - 2, 10, 8.4f, 4, 3, 1).color(0x1c1a14).glow(Face.FRONT, 1, 0, 2, 3, EYE);       // eyes
            Part lid = head.add("lid" + n).at(s * 5.5f, 14, 9.0f);
            lid.box(-2.5f, -5, 0, 5, 5, 2).color(STONE).grad(STONE_L, STONE).bevel(0.2f).noise(0.12f);
            Part ear = head.add("ear" + n).at(s * 10, 12, 0);
            ear.box(s > 0 ? 0 : -3, -3, -3, 3, 6, 6).color(STONE_D).bevel(0.2f);
        }
        // ---- jaw with teeth
        Part jaw = head.add("jaw").at(0, 5, -7);
        jaw.box(-8, -5, 0, 16, 5, 16).color(STONE_D).grad(STONE, STONE_D).bevel(0.2f).noise(0.12f);
        for (int i = 0; i < 5; i++) jaw.box(-7 + i * 3.2f, 0, 14, 2, 3, 2).color(0xe8e4cc).bevel(0.15f);
        head.box(-7.5f, 3, 8, 15, 2, 1).color(0x15130e);                                         // mouth void
        for (int i = 0; i < 4; i++) head.box(-6 + i * 3.4f, 2, 8.2f, 2, 3, 2).color(0xe8e4cc).bevel(0.15f);
        // ---- floating stone fists
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "R" : "L";
            Part arm = body.add("arm" + n).at(s * 13, 9, 1);
            arm.box(-3, -9, -3, 6, 9, 6).color(STONE).grad(STONE_L, STONE_D).bevel(0.2f).noise(0.14f).paint(Face.FRONT, 0, 0, 6, 2, MOSS);
            Part fist = arm.add("fist" + n).at(0, -9, 0);
            fist.box(-4, -7, -4, 8, 7, 8).color(STONE).bevel(0.2f).noise(0.14f);
            for (int f = -1; f <= 1; f++) fist.box(f * 2.6f - 0.7f, -11, 1.5f, 2, 5, 2).color(STONE_L).flat().bevel(0.2f); // claws/knuckle spikes
            fist.box(-4.2f, -7.3f, -4.2f, 8, 2, 8).inflate(0.2f).color(MOSS_D).noise(0.2f);
        }
        m.look("head", 40, 20);
        m.bob(0.8f, 0.07f);
        // ---- clips. lids hang over the eyes at rest (closed): scl y -> 0.12 opens them
        m.clip("idle", 48).loop()
            .scl("lidR", 0, 1, 0.12f, 1).scl("lidL", 0, 1, 0.12f, 1).scl("lidR", 48, 1, 0.12f, 1).scl("lidL", 48, 1, 0.12f, 1)
            .rot("head", 0, 0, 0, 0).rot("head", 24, 3, 3, 0).rot("head", 48, 0, 0, 0)
            .pos("armR", 0, 0, 0, 0).pos("armR", 24, 0, 1.5f, 0).pos("armR", 48, 0, 0, 0)
            .pos("armL", 0, 0, 1.5f, 0).pos("armL", 24, 0, 0, 0).pos("armL", 48, 0, 1.5f, 0)
            .rot("armR", 0, 5, 0, 8).rot("armL", 0, 5, 0, -8);
        m.clip("sleep", 60).loop()
            .rot("head", 0, 14, 0, 0).rot("head", 30, 17, 0, 0).rot("head", 60, 14, 0, 0)
            .pos("body", 0, 0, -6, 0).pos("body", 30, 0, -5, 0).pos("body", 60, 0, -6, 0)
            .rot("armR", 0, 15, 0, 10).rot("armL", 0, 15, 0, -10)
            .rot("jaw", 0, 4, 0, 0).rot("jaw", 30, 9, 0, 0).rot("jaw", 60, 4, 0, 0);
        m.clip("wake", 40)
            .rot("head", 0, 14, 0, 0).rot("head", 10, 16, 0, 0).rot("head", 18, -20, 0, 0, Ease.OUT_BACK).rot("head", 30, -8, 0, 0).rot("head", 40, 0, 0, 0)
            .pos("body", 0, 0, -6, 0).pos("body", 10, 0, -7, 0).pos("body", 18, 0, 3, 0, Ease.OUT_BACK).pos("body", 40, 0, 0, 0)
            .scl("lidR", 0, 1, 1, 1).scl("lidR", 12, 1, 1, 1).scl("lidR", 17, 1, 0.12f, 1, Ease.OUT_BACK)
            .scl("lidL", 0, 1, 1, 1).scl("lidL", 12, 1, 1, 1).scl("lidL", 17, 1, 0.12f, 1, Ease.OUT_BACK)
            .rot("jaw", 0, 4, 0, 0).rot("jaw", 18, 4, 0, 0).rot("jaw", 21, 38, 0, 0).rot("jaw", 32, 30, 0, 0).rot("jaw", 40, 0, 0, 0)
            .rot("armR", 0, 15, 0, 10).rot("armR", 18, 10, 0, 70, Ease.OUT_BACK).rot("armR", 32, 5, 0, 55).rot("armR", 40, 5, 0, 8)
            .rot("armL", 0, 15, 0, -10).rot("armL", 18, 10, 0, -70, Ease.OUT_BACK).rot("armL", 32, 5, 0, -55).rot("armL", 40, 5, 0, -8);
        m.clip("slash", 30)
            .rot("body", 0, 0, 0, 0).rot("body", 10, -6, -25, 0).rot("body", 14, 14, 30, 0, Ease.IN_CUBIC).rot("body", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -2, -4).pos("body", 14, 0, -2, 7, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("armR", 0, 5, 0, 8).rot("armR", 10, -150, -20, 40).rot("armR", 14, -75, 30, -25, Ease.IN_CUBIC).rot("armR", 20, -70, 30, -25).rot("armR", 30, 5, 0, 8)
            .rot("fistR", 0, 0, 0, 0).rot("fistR", 10, -20, 0, 0).rot("fistR", 14, 25, 0, 0).rot("fistR", 30, 0, 0, 0)
            .rot("armL", 0, 5, 0, -8).rot("armL", 10, 20, 0, -35).rot("armL", 16, -50, -20, 10, Ease.OUT_QUAD).rot("armL", 30, 5, 0, -8)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -12, -15, 0).rot("head", 14, 14, 12, 0, Ease.IN_CUBIC).rot("head", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 10, 25, 0, 0).rot("jaw", 14, 8, 0, 0).rot("jaw", 30, 0, 0, 0);
        m.clip("hurt", 10).rot("head", 0, 0, 0, 0).rot("head", 3, -14, 6, 4).rot("head", 10, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 3, 0, 0, -3).pos("body", 10, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 22, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 36)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -25, 0, 0).rot("head", 36, 35, 12, 18, Ease.OUT_BOUNCE)
            .pos("head", 0, 0, 0, 0).pos("head", 20, 0, -3, 3, Ease.IN_QUAD).pos("head", 36, 0, -14, 5, Ease.OUT_BOUNCE)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, 40, 0, 0).rot("jaw", 36, 55, 0, 0, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 24, 0, -6, 0, Ease.IN_QUAD).pos("body", 36, 0, -7, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 36, 20, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 36, 10, 0, 85).pos("armR", 0, 0, 0, 0).pos("armR", 24, -2, -3, 0, Ease.IN_QUAD).pos("armR", 36, -4, -4, 0, Ease.OUT_BOUNCE)
            .rot("armL", 0, 0, 0, 0).rot("armL", 36, 10, 0, -85).pos("armL", 0, 0, 0, 0).pos("armL", 24, 2, -3, 0, Ease.IN_QUAD).pos("armL", 36, 4, -4, 0, Ease.OUT_BOUNCE)
            .scl("lidR", 0, 1, 0.12f, 1).scl("lidR", 12, 1, 1, 1).scl("lidL", 0, 1, 0.12f, 1).scl("lidL", 12, 1, 1, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_bygone_effigy", "Bygone Effigy", "古の偶像")
            .game(2, 1).elite().size(1.5f, 2.2f).hp(127).speed(0.22f).style(Style.METAL).egg(0x8a9484, 0xffb83a).kbResist(0.6f)
            .model(BygoneEffigy::model).base("sleep")
            .spawn(1, 1, 1, MobDef.Habitat.FOREST)
            .pattern(Pattern.seq(2, "sleep_turn", "wake", "slash"))
            .move(Move.of("sleep_turn", 40).anim("sleep").intent(Intent.SLEEP).noTarget())
            .move(Move.of("wake", 40).intent(Intent.BUFF).noTarget()
                .sound(16, SoundEvents.IRON_GOLEM_REPAIR, 1.5f, 0.5f)
                .at(19, (s, t) -> { s.setBase("idle"); s.addStrength(10); Fx.sound(s, SoundEvents.WARDEN_ROAR, 1.2f, 0.9f); Fx.particles(s, ParticleTypes.CRIT, 40, 0.9); Bits.say(s, "Shall we begin?"); }))
            .move(Move.of("slash", 30).intent(Intent.ATTACK, 13, 1).reach(3.2f)
                .sound(8, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.6f)
                .at(14, Fx.melee(13, 3.4, 100)))
            .onHurt((s, src, dmg) -> {
                if (s.getBase().equals("sleep")) return dmg;               // asleep: no Slow yet
                int n = s.count("slow"); s.inc("slow");
                return dmg * (1f + 0.1f * n);                               // ponytail: per-hit stand-in for per-card Slow
            })
            .onMoveEnd(s -> s.setCount("slow", 0))
            .onClientTick(s -> { if (s.getBase().equals("sleep") && s.tickCount % 30 == 0) s.level().addParticle(ParticleTypes.CLOUD, s.getX(), s.getY() + 2.6, s.getZ(), 0, 0.05, 0); });
    }
}
