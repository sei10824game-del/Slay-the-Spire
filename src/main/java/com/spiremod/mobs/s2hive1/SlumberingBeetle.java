package com.spiremod.mobs.s2hive1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Slumbering Beetle (86 HP, Plating 15, Slumber 3). Asleep: Snore every turn. Wakes after 3 turns or after losing HP 3 times (loses Plating),
 * then Roll Out (16 + 2 Str) every turn.
 */
public final class SlumberingBeetle {
    private SlumberingBeetle() {}
    static final int SHELL = 0x5a6a3a, SHELL_D = 0x34401e, SHELL_L = 0x8a9a58, PLATE = 0xb8a868, BELLY = 0x8a7a50, LEG = 0x3a2e1c;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_slumbering_beetle");
        Part body = m.add("body").at(0, 9, 0);
        body.box(-8, -5, -10, 16, 11, 20).color(SHELL).grad(SHELL_L, SHELL_D).bevel(0.2f).noise(0.1f)
            .paint(Face.TOP, 7, 0, 2, 20, SHELL_D).paint(Face.TOP, 0, 6, 16, 1, SHELL_D).paint(Face.TOP, 0, 12, 16, 1, SHELL_D);   // carapace seams
        body.box(-7, -6, -9, 14, 1, 18).color(BELLY);
        // armour plating: stacked overlapping plates (the Plating buff)
        Part plating = body.add("plating").at(0, 6, 0);
        for (int i = 0; i < 4; i++) plating.box(-7 + i * 0.4f, 0, -9 + i * 4.5f, (int) (14 - i * 0.8f), 2, 4).color(PLATE).grad(0xd8c888, 0x8a7a48).bevel(0.25f).noise(0.12f);
        plating.box(-1, 2, -8, 2, 2, 16).color(0xd8c888).bevel(0.2f);
        Part head = body.add("head").at(0, -1, 10);
        head.box(-4, -3, 0, 8, 7, 6).color(SHELL_D).grad(SHELL, SHELL_D).bevel(0.2f);
        head.box(-4.4f, 1, 4.4f, 3, 1, 2).color(0x1c2010).flat();   // closed eye slits
        head.box(1.4f, 1, 4.4f, 3, 1, 2).color(0x1c2010).flat();
        for (int s = -1; s <= 1; s += 2) {
            Part md = head.add(s < 0 ? "mandR" : "mandL").at(s * 2, -2, 5.5f);
            md.box(-1, -1, 0, 2, 2, 4).color(PLATE).bevel(0.2f);
            md.box(s < 0 ? 0 : -1.2f, -1, 3, 1, 2, 2).color(0x8a7a48);
            Part an = head.add(s < 0 ? "antR" : "antL").at(s * 2.5f, 3.5f, 5);
            an.box(-0.5f, 0, 0, 1, 1, 6).color(LEG).flat();
        }
        Kit.legs(body, "leg", 7.5f, -3, new float[]{5, -1, -7}, 4, 9, 20, 18, SHELL_D, LEG);
        Kit.scuttle(m, "leg", 3, 20);
        m.look("head", 20, 10);
        // SLEEP (base stance): head down, legs tucked, slow deep breaths
        Clip z = m.clip("sleep", 80).loop();
        Kit.P(z, "body", 0, 0, -2.5f, 0, 0, 40, 0, -3.1f, 0, 0, 80, 0, -2.5f, 0, 0);
        Kit.S(z, "body", 0, 1, 1, 1, 0, 40, 1.03f, 1.05f, 1.03f, 0, 80, 1, 1, 1, 0);
        Kit.R(z, "head", 0, 30, 0, 0, 0, 40, 34, 0, 0, 0, 80, 30, 0, 0, 0);
        Kit.R(z, "plating", 0, 0, 0, 0, 0, 40, 1.5f, 0, 0, 0, 80, 0, 0, 0, 0);
        Clip i = m.clip("idle", 40).loop();
        Kit.R(i, "head", 0, 0, 0, 0, 0, 20, -4, 0, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(i, "mandR", 0, 0, 0, 0, 0, 20, 0, -12, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(i, "mandL", 0, 0, 0, 0, 0, 20, 0, 12, 0, 0, 40, 0, 0, 0, 0);
        Kit.P(i, "body", 0, 0, 0, 0, 0, 20, 0, 0.5f, 0, 0, 40, 0, 0, 0, 0);
        // SNORE: a big wheezing inhale and exhale
        Clip sn = m.clip("snore", 40);
        Kit.S(sn, "body", 4, 1, 1, 1, 0, 16, 1.1f, 1.14f, 1.1f, 4, 30, 0.97f, 0.96f, 0.97f, 0, 40, 1, 1, 1, 0);
        Kit.R(sn, "head", 0, 30, 0, 0, 0, 16, 22, 0, 0, 0, 30, 36, 0, 0, 0, 40, 30, 0, 0, 0);
        Kit.R(sn, "mandR", 16, 0, 0, 0, 0, 22, 0, -22, 0, 0, 34, 0, 0, 0, 0);
        Kit.R(sn, "mandL", 16, 0, 0, 0, 0, 22, 0, 22, 0, 0, 34, 0, 0, 0, 0);
        Kit.P(sn, "body", 0, 0, -2.5f, 0, 0, 16, 0, -1.5f, 0, 0, 30, 0, -3.5f, 0, 0, 40, 0, -2.5f, 0, 0);
        // ROLL OUT: rear up, curl into a ball, rocket forward spinning, uncurl
        Clip ro = m.clip("roll_out", 38);
        Kit.R(ro, "body", 6, -25, 0, 0, 0, 12, 20, 0, 0, 0, 14, 130, 0, 0, 1, 20, 420, 0, 0, 5, 26, 720, 0, 0, 4, 30, 720, 0, 0, 0, 38, 720, 0, 0, 0);
        Kit.P(ro, "body", 6, 0, 1, -3, 0, 12, 0, 2, -3, 0, 14, 0, 3, 0, 1, 26, 0, 3, 14, 5, 34, 0, 1, 14, 0, 38, 0, 0, 0, 0);
        Kit.R(ro, "head", 6, -20, 0, 0, 0, 12, 40, 0, 0, 0, 26, 40, 0, 0, 0, 38, 0, 0, 0, 0);
        Kit.S(ro, "body", 12, 1, 1, 1, 0, 16, 0.85f, 0.85f, 0.85f, 1, 30, 0.85f, 0.85f, 0.85f, 0, 36, 1, 1, 1, 2);
        Kit.hurt(m, "body", "head");
        Clip d = m.clip("death", 30);
        Kit.R(d, "body", 10, 0, 0, 40, 0, 22, 0, 0, 180, 3, 30, 0, 0, 180, 0);
        Kit.P(d, "body", 10, 0, 3, 0, 0, 22, 0, 4, 0, 0, 30, 0, 3, 0, 0);
        Kit.R(d, "plating", 14, 0, 0, 0, 0, 30, -40, 0, 50, 4);
        Kit.curlLegs(d, "leg", 3, 0, 22);
        return m;
    }

    static void wake(SpireMob s) {
        if (s.flags.contains("awake")) return;
        s.flags.add("awake");
        s.setBase("idle");
        s.addBlock(-s.getBlock());                       // loses Plating
        Fx.sound(s, SoundEvents.RAVAGER_ROAR, 0.8f, 1.5f);
        Fx.particles(s, ParticleTypes.POOF, 15, 0.6);
    }

    public static MobDef def() {
        return MobDef.of("s2_slumbering_beetle", "Slumbering Beetle", "眠れる甲虫")
            .game(2, 2).size(1.5f, 1.2f).hp(86).speed(0.2f).style(Style.INSECT).egg(SHELL, PLATE).keepBlock().base("sleep")
            .model(SlumberingBeetle::model).spawn(4, 1, 1, MobDef.Habitat.CAVE)
            .onSpawn(s -> s.addBlock(15))
            .onHurt((s, src, dmg) -> { if (!s.flags.contains("awake") && s.inc("hits") >= 3) wake(s); return dmg; })
            .pattern(s -> s.flags.contains("awake") ? "roll_out" : "snore")
            .move(Move.of("snore", 40).intent(Intent.SLEEP).noTarget()
                .sound(8, SoundEvents.PANDA_SNEEZE, 0.4f, 0.4f)
                .at(30, (s, t) -> { Fx.particles(s, ParticleTypes.CLOUD, 3, 0.3); if (s.inc("snores") >= 3) wake(s); }))
            .move(Move.of("roll_out", 38).intent(Intent.ATTACK_BUFF, 16, 1).reach(5f)
                .sound(14, SoundEvents.ARMOR_EQUIP_TURTLE, 1f, 0.6f)
                .at(14, Fx.dash(0.9, 0.1))
                .at(20, Fx.melee(16, 3.2, 120))
                .at(30, (s, t) -> { s.addStrength(2); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 5, 0.4); }));
    }
}
