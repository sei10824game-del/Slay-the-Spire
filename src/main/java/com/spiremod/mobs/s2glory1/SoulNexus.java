package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory elite: Soul Nexus. 234 HP. Opens with Soul Burn (29), then random Soul Burn / Maelstrom (6 x4) / Drain Life (18 + 2 Vuln + 2 Weak), never the same twice in a row. */
public final class SoulNexus {
    private SoulNexus() {}
    static final int CRYS = 0x4a2a7a, CRYS_D = 0x1e1038, SOUL = 0x90ffc0, RING = 0x6a5a8a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_soul_nexus");
        m.scale(1.7f);
        Part core = m.add("core").at(0, 20, 0);
        core.box(-5, -7, -5, 10, 14, 10).color(CRYS).grad(0x8a58c8, CRYS_D).bevel(0.3f).noise(0.1f)
            .glow(Face.FRONT, 3, 4, 4, 6, SOUL).glow(Face.BACK, 3, 4, 4, 6, SOUL).glow(Face.LEFT, 3, 4, 4, 6, SOUL).glow(Face.RIGHT, 3, 4, 4, 6, SOUL);
        core.box(-3, 7, -3, 6, 4, 6).color(CRYS).grad(0x9a68d8, CRYS).bevel(0.3f);
        core.box(-3, -11, -3, 6, 4, 6).color(CRYS_D).bevel(0.3f);
        core.box(-1, 11, -1, 2, 3, 2).color(0xb088f0).flat();
        core.box(-1, -14, -1, 2, 3, 2).color(CRYS_D).flat();
        Part ring1 = m.add("ring1").at(0, 20, 0);
        Part ring2 = m.add("ring2").at(0, 20, 0).rot(0, 0, 60);
        for (Part r : new Part[] {ring1, ring2}) {
            r.box(-13, -1, -1.5f, 5, 2, 3).color(RING).bevel(0.3f); r.box(8, -1, -1.5f, 5, 2, 3).color(RING).bevel(0.3f);
            r.box(-1.5f, -1, -13, 3, 2, 5).color(RING).bevel(0.3f); r.box(-1.5f, -1, 8, 3, 2, 5).color(RING).bevel(0.3f);
            r.box(-9.5f, -1, -9.5f, 3, 2, 3).color(RING).bevel(0.3f); r.box(6.5f, -1, 6.5f, 3, 2, 3).color(RING).bevel(0.3f);
            r.box(-9.5f, -1, 6.5f, 3, 2, 3).color(RING).bevel(0.3f); r.box(6.5f, -1, -9.5f, 3, 2, 3).color(RING).bevel(0.3f);
            r.box(-12.5f, -0.5f, -1, 1, 1, 2).color(SOUL).flat().glow(Face.FRONT, 0, 0, 1, 1, SOUL);
        }
        for (int i = 0; i < 6; i++) {
            Part sk = m.add("soul" + i).at(0, 20, 0);
            sk.box(-1.5f, -1.5f, -1.5f, 3, 3, 3).color(0xd8f8e8).flat().glow(Face.FRONT, 0, 0, 3, 3, SOUL).glow(Face.TOP, 0, 0, 3, 3, SOUL);
            sk.box(-1, -3.5f, -1, 2, 2, 2).color(SOUL).flat().glow(Face.FRONT, 0, 0, 2, 2, SOUL);
        }
        m.look("core", 60, 25);
        m.proc((r, c) -> {
            boolean maelstrom = "maelstrom".equals(c.move);
            float sp = maelstrom ? c.age * 0.5f : c.age * 0.1f, rad = maelstrom ? 17f : 14f;
            r.rot("ring1", c.age * (maelstrom ? 10f : 2f) % 360, c.age * (maelstrom ? 14f : 3f) % 360, 0);
            r.rot("ring2", c.age * -2.5f % 360, 0, c.age * 3f % 360);
            float bob = (float) Math.sin(c.age * 0.08f) * 2f;
            for (String n : new String[] {"core", "ring1", "ring2"}) r.pos(n, 0, bob, 0);
            for (int i = 0; i < 6; i++) { float a = sp + i * 1.0472f; r.pos("soul" + i, (float) Math.cos(a) * rad, bob + (float) Math.sin(a * 2f) * 4f, (float) Math.sin(a) * rad); r.rot("soul" + i, 0, -a * 57.3f, 0); }
        });
        m.clip("idle", 60).loop().rot("core", 0, 0, 0, 0).rot("core", 30, 0, 90, 4).rot("core", 60, 0, 180, 0);
        Clip b = m.clip("soul_burn", 36);
        b.scl("core", 0, 1, 1, 1).scl("core", 14, 0.75f, 0.75f, 0.75f).scl("core", 20, 1.4f, 1.4f, 1.4f, Ease.OUT_BACK).scl("core", 36, 1, 1, 1);
        Kit.seq(b, "core", 36, 20, 10, -20, 0, 0, 20, 25, 0, 0);
        Kit.sp(b, "core", 36, 20, 12, 0, 1, -3, 20, 0, -1, 4);
        Clip ma = m.clip("maelstrom", 46);
        ma.scl("core", 0, 1, 1, 1).scl("core", 8, 1.2f, 1.2f, 1.2f).scl("core", 38, 1.2f, 1.2f, 1.2f).scl("core", 46, 1, 1, 1);
        Kit.seq(ma, "core", 46, -1, 8, 0, 0, 0, 12, 0, 180, 0, 40, 0, 1080, 0);
        Clip d = m.clip("drain_life", 40);
        d.scl("core", 0, 1, 1, 1).scl("core", 16, 0.8f, 1.1f, 0.8f).scl("core", 22, 1.15f, 0.9f, 1.15f, Ease.OUT_BACK).scl("core", 40, 1, 1, 1);
        Kit.seq(d, "core", 40, 22, 10, 12, 0, 0, 22, -20, 0, 0, 30, -10, 0, 0);
        Kit.sp(d, "core", 40, 22, 12, 0, 0, -2, 22, 0, 0, 3);
        m.clip("hurt", 10).rot("core", 0, 0, 0, 0).rot("core", 3, -15, 0, 12).rot("core", 10, 0, 0, 0);
        m.clip("death", 36)
            .scl("core", 0, 1, 1, 1).scl("core", 14, 1.3f, 1.3f, 1.3f).scl("core", 30, 0.2f, 0.2f, 0.2f).scl("core", 36, 0.01f, 0.01f, 0.01f)
            .rot("core", 0, 0, 0, 0).rot("core", 36, 0, 720, 0)
            .pos("core", 0, 0, 0, 0).pos("core", 36, 0, -12, 0, Ease.IN_QUAD);
        return m;
    }

    public static MobDef def() {
        MobDef d = MobDef.of("s2_soul_nexus", "Soul Nexus", "ソウルネクサス")
            .game(2, 3).size(1.5f, 3.6f).hp(234).speed(0.2f).flying().style(Style.GHOST).egg(0x4a2a7a, 0x90ffc0).elite()
            .model(SoulNexus::model)
            .spawn(1, 1, 1, MobDef.Habitat.CAVE)
            .pattern(Pattern.first("soul_burn", Pattern.random().add("soul_burn", 1).add("maelstrom", 1).add("drain_life", 1)
                .maxRepeat("soul_burn", 1).maxRepeat("maelstrom", 1).maxRepeat("drain_life", 1)))
            .onClientTick(s -> { if (s.tickCount % 3 == 0) s.level().addParticle(ParticleTypes.SOUL, s.getX() + (s.getRandom().nextDouble() - 0.5), s.getY() + 1.5 + s.getRandom().nextDouble(), s.getZ() + (s.getRandom().nextDouble() - 0.5), 0, 0.03, 0); })
            .move(Move.of("soul_burn", 36).intent(Intent.ATTACK, 29, 1).ranged(12)
                .sound(16, SoundEvents.WITHER_SHOOT, 1.2f, 0.6f)
                .at(20, (s, t) -> {
                    Fx.hitTarget(29).run(s, t);
                    if (t != null) for (int i = 1; i <= 8; i++) Fx.particlesAt(s, ParticleTypes.SOUL_FIRE_FLAME, s.position().add(0, 2.2, 0).lerp(t.position().add(0, 1, 0), i / 8.0), 3, 0.1, 0.01);
                }));
        Move.Action wave = (s, t) -> {
            for (var v : Fx.foes(s, s.getBoundingBox().inflate(5, 3, 5))) v.invulnerableTime = 0;
            Fx.aoeAt(s, s.position(), 6, 5.0, null);
            Fx.ring(s, ParticleTypes.SOUL_FIRE_FLAME, s.position().add(0, 1, 0), 4.5, 30);
            Fx.sound(s, SoundEvents.SOUL_ESCAPE, 1f, 0.8f);
        };
        return d
            .move(Move.of("maelstrom", 46).intent(Intent.ATTACK, 6, 4).reach(5f)
                .at(14, wave).at(20, wave).at(26, wave).at(32, wave))
            .move(Move.of("drain_life", 40).intent(Intent.ATTACK_DEBUFF, 18, 1).ranged(12)
                .sound(18, SoundEvents.ELDER_GUARDIAN_CURSE, 0.8f, 1.5f)
                .at(22, (s, t) -> Fx.hitTarget(18, v -> { Fx.vulnerable(v, 2 * Kit.TURN); Fx.weak(v, 2 * Kit.TURN); Fx.particles(s, ParticleTypes.SOUL, 25, 0.8); }).run(s, t)));
    }
}
