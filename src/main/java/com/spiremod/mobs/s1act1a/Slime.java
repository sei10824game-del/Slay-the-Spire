package com.spiremod.mobs.s1act1a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * StS1 Act 1 Acid Slime and Spike Slime, sizes S / M / L (size 0/1/2).
 * Acid L 65-69: Corrosive Spit 11 + 2 Slimed (30%, max 2 in a row) / Tackle 16 (40%, no repeat) / Lick 2 Weak (30%, no repeat). Split at 50%.
 * Acid M 28-32: Spit 7 + 1 Slimed / Tackle 10 / Lick 1 Weak (same AI, no split).  Acid S 8-12: Tackle 3 / Lick 1 Weak alternating.
 * Spike L 64-70: Flame Tackle 16 + 2 Slimed (30%) / Lick 2 Frail (70%), max 2 in a row. Split at 50%.
 * Spike M 28-32: Flame Tackle 8 + 1 Slimed / Lick 1 Frail.  Spike S 10-14: Tackle 5 forever.
 * Split: the large slime splits into two medium slimes, each with the large slime's current HP.
 */
public final class Slime {
    private Slime() {}

    static final float[] SCALE = {0.6f, 1.05f, 1.75f};
    static final String[] SUF = {"_s", "_m", "_l"};
    static final String[] SZ = {"(S)", "(M)", "(L)"};
    static final String[] SZJ = {"(小)", "(中)", "(大)"};

    public static ModelSpec model(boolean acid, int size) {
        int c = acid ? 0x6cbf3a : 0xa06cc8, cl = acid ? 0xb4ec74 : 0xd4a8f0, cd = acid ? 0x2f6a1c : 0x58367e, eye = acid ? 0x10200a : 0x1e1030;
        ModelSpec m = new ModelSpec((acid ? "acid_slime" : "spike_slime") + SUF[size]).scale(SCALE[size]);
        Part blob = m.add("blob").at(0, 0, 0);
        blob.box(-6, 0, -6, 12, 4, 12).color(c).grad(c, cd).bevel(0.2f).noise(0.06f)
            .paint(Face.TOP, 2, 2, 2, 2, cl).paint(Face.FRONT, 9, 1, 1, 1, cl);
        blob.box(-6.5f, 0, -6.5f, 13, 1, 13).color(cd).noise(0.05f);                                  // puddle rim
        Part mid = blob.add("mid").at(0, 4, 0);
        mid.box(-5, 0, -5, 10, 3, 10).color(c).grad(cl, c).bevel(0.15f).noise(0.06f).paint(Face.FRONT, 1, 0, 2, 1, cl);
        Part top = mid.add("top").at(0, 3, 0);
        top.box(-3.5f, 0, -3.5f, 7, 2, 7).color(cl).grad(cl, c).bevel(0.15f).paint(Face.TOP, 1, 1, 2, 2, 0xffffff);
        top.box(-1.5f, 2, -1.5f, 3, 1, 3).color(cl).flat();
        // face: big glossy eyes + wide mouth
        Part face = mid.add("face").at(0, 0.5f, 5);
        for (int s = -1; s <= 1; s += 2) {
            face.box(s < 0 ? -4 : 1.5f, 0, -0.5f, 2, 2, 1).color(eye).flat().px(Face.FRONT, s < 0 ? 0 : 0, 0, 0xffffff);
        }
        Part mouth = blob.add("mouth").at(0, 2.5f, 6);
        mouth.box(-3, -1, -0.5f, 6, 2, 1).color(0x1a0c10).flat().paint(Face.FRONT, 0, 0, 6, 1, cd);
        Part tongue = mouth.add("tongue").at(0, -0.5f, 0.3f);
        tongue.box(-1, -0.5f, 0, 2, 1, 5).color(0xd0607a).flat();
        tongue.box(-1.5f, -0.5f, 4, 3, 1, 2).color(0xe0708a).flat();
        if (acid) {
            Part sprout = top.add("sprout").at(0, 3, 0);
            sprout.box(-0.5f, 0, -0.5f, 1, 3, 1).color(c);
            sprout.box(-1, 3, -1, 2, 2, 2).color(cl).glow(Face.FRONT, 0, 0, 1, 1, 0xd8ff9a);
            for (int i = 0; i < 3; i++) { // acid drips on the flanks
                Part d = blob.add("drip" + i).at(-4 + i * 4, 4, (i == 1 ? 5.5f : 4.5f));
                d.box(-0.5f, -2 - i % 2, 0, 1, 2 + i % 2, 1).color(cl).flat();
            }
        } else {
            float[][] sp = {{0, 5, 0, 0, 0}, {-3, 4, 2, 25, 25}, {3, 4, 2, 25, -25}, {-3, 4, -2, -25, 25}, {3, 4, -2, -25, -25}, {0, 3, -4, -40, 0}, {-4.5f, 2, 0, 0, 45}, {4.5f, 2, 0, 0, -45}};
            for (int i = 0; i < sp.length; i++) {
                Part s = (i == 0 ? top : mid).add("spike" + i).at(sp[i][0], i == 0 ? 2 : sp[i][1] - 2, sp[i][2]).rot(sp[i][3], 0, sp[i][4]);
                s.box(-1, 0, -1, 2, 2, 2).color(0xd8cce6).grad(0xf0e8f8, 0xa898b8);
                s.box(-0.5f, 2, -0.5f, 1, 2, 1).color(0xf4eefa).flat();
            }
        }
        m.proc((r, x) -> r.show("tongue", "lick".equals(x.move)));
        m.clip("idle", 40).loop()
            .scl("blob", 0, 1, 1, 1).scl("blob", 20, 1.06f, 0.92f, 1.06f).scl("blob", 40, 1, 1, 1)
            .rot("top", 0, 0, 0, 3).rot("top", 20, 0, 0, -3).rot("top", 40, 0, 0, 3)
            .scl("mouth", 0, 1, 1, 1).scl("mouth", 30, 1, 1, 1).scl("mouth", 33, 1.1f, 1.6f, 1).scl("mouth", 36, 1, 1, 1);
        // walk = hop
        m.clip("walk", 16).loop()
            .scl("blob", 0, 1.15f, 0.8f, 1.15f).scl("blob", 4, 0.9f, 1.2f, 0.9f).scl("blob", 8, 1, 1, 1).scl("blob", 12, 1.1f, 0.9f, 1.1f).scl("blob", 16, 1.15f, 0.8f, 1.15f)
            .pos("blob", 0, 0, 0, 0).pos("blob", 4, 0, 2, 0).pos("blob", 8, 0, 3, 0).pos("blob", 12, 0, 0, 0).pos("blob", 16, 0, 0, 0);
        // TACKLE: squash low, launch forward stretched, splat into the target, wobble back
        tackle(m.clip("tackle", 26), 0);
        if (acid || size > 0) {
            // LICK: lean in, long tongue lashes out and slurps back
            m.clip("lick", 28)
                .rot("blob", 0, 0, 0, 0).rot("blob", 8, -8, 0, 0).rot("blob", 13, 12, 0, 0, Ease.IN_CUBIC).rot("blob", 20, 10, 0, 0).rot("blob", 28, 0, 0, 0)
                .scl("mouth", 0, 1, 1, 1).scl("mouth", 8, 1.2f, 2.2f, 1).scl("mouth", 20, 1.2f, 2f, 1).scl("mouth", 26, 1, 1, 1)
                .scl("tongue", 0, 1, 1, 0.2f).scl("tongue", 9, 1, 1, 0.3f).scl("tongue", 13, 1, 1, 2.6f, Ease.OUT_BACK).scl("tongue", 17, 1, 1, 2.4f).scl("tongue", 22, 1, 1, 0.3f)
                .rot("tongue", 0, 0, 0, 0).rot("tongue", 13, -10, 0, 0).rot("tongue", 15, 8, 12, 0).rot("tongue", 17, -5, -12, 0).rot("tongue", 22, 0, 0, 0)
                .pos("blob", 0, 0, 0, 0).pos("blob", 13, 0, 0, 2).pos("blob", 28, 0, 0, 0);
        }
        if (acid && size > 0) {
            // CORROSIVE SPIT: rear up swelling, then heave forward and hurl a glob from the mouth
            m.clip("corrosive_spit", 30)
                .scl("blob", 0, 1, 1, 1).scl("blob", 10, 0.85f, 1.3f, 0.85f).scl("blob", 14, 1.2f, 0.8f, 1.25f, Ease.IN_CUBIC).scl("blob", 20, 0.95f, 1.08f, 0.95f).scl("blob", 30, 1, 1, 1)
                .rot("blob", 0, 0, 0, 0).rot("blob", 10, -12, 0, 0).rot("blob", 14, 14, 0, 0, Ease.IN_CUBIC).rot("blob", 30, 0, 0, 0)
                .scl("mouth", 0, 1, 1, 1).scl("mouth", 10, 0.8f, 0.6f, 1).scl("mouth", 14, 1.4f, 2.6f, 1).scl("mouth", 22, 1, 1, 1)
                .rot("sprout", 0, 0, 0, 0).rot("sprout", 10, -30, 0, 0).rot("sprout", 14, 45, 0, 0).rot("sprout", 20, -15, 0, 0).rot("sprout", 30, 0, 0, 0);
        }
        if (!acid && size > 0) {
            // FLAME TACKLE: bristle spikes, coil, rocket into the target, spikes flare
            Clip ft = m.clip("flame_tackle", 30);
            tackle(ft, 4);
            for (int i = 0; i < 8; i++) ft.scl("spike" + i, 0, 1, 1, 1).scl("spike" + i, 10, 1.3f, 1.5f, 1.3f).scl("spike" + i, 15, 1.5f, 1.8f, 1.5f).scl("spike" + i, 30, 1, 1, 1);
        }
        if (size == 2) {
            // SPLIT: tremble, stretch wide, pinch in the middle
            m.clip("split", 30)
                .scl("blob", 0, 1, 1, 1).scl("blob", 6, 1.05f, 0.95f, 1).scl("blob", 8, 0.95f, 1.05f, 1).scl("blob", 10, 1.08f, 0.93f, 1).scl("blob", 12, 0.95f, 1.05f, 1)
                .scl("blob", 22, 1.7f, 0.65f, 1.05f, Ease.IN_CUBIC).scl("blob", 30, 1.9f, 0.55f, 1.1f)
                .scl("top", 0, 1, 1, 1).scl("top", 22, 0.4f, 1, 1).scl("top", 30, 0.2f, 1, 1)
                .rot("face", 0, 0, 0, 0).rot("face", 12, 0, 0, 0).rot("face", 30, 0, 0, 0);
        }
        m.clip("hurt", 10).scl("blob", 0, 1, 1, 1).scl("blob", 3, 1.25f, 0.75f, 1.25f).scl("blob", 6, 0.92f, 1.1f, 0.92f).scl("blob", 10, 1, 1, 1);
        m.clip("death", 24)
            .scl("blob", 0, 1, 1, 1).scl("blob", 4, 1.2f, 0.85f, 1.2f).scl("blob", 18, 1.6f, 0.2f, 1.6f, Ease.IN_QUAD).scl("blob", 24, 1.7f, 0.05f, 1.7f)
            .scl("face", 0, 1, 1, 1).scl("face", 8, 1, 0.3f, 1).scl("face", 24, 1, 0.3f, 1);
        return m;
    }

    static void tackle(Clip c, int d) {
        c.scl("blob", 0, 1, 1, 1).scl("blob", 8 + d, 1.3f, 0.65f, 1.3f).scl("blob", 12 + d, 0.8f, 1.1f, 1.45f, Ease.IN_CUBIC).scl("blob", 15 + d, 1.35f, 0.7f, 1.1f)
            .scl("blob", 19 + d, 0.92f, 1.1f, 0.92f).scl("blob", 26 + d, 1, 1, 1)
            .pos("blob", 0, 0, 0, 0).pos("blob", 8 + d, 0, 0, -2).pos("blob", 12 + d, 0, 3, 7, Ease.IN_CUBIC).pos("blob", 15 + d, 0, 0, 8).pos("blob", 26 + d, 0, 0, 0)
            .rot("blob", 0, 0, 0, 0).rot("blob", 8 + d, -10, 0, 0).rot("blob", 12 + d, 15, 0, 0).rot("blob", 15 + d, 5, 0, 0).rot("blob", 26 + d, 0, 0, 0)
            .scl("face", 0, 1, 1, 1).scl("face", 8 + d, 1, 0.4f, 1).scl("face", 12 + d, 1, 1.2f, 1).scl("face", 20 + d, 1, 1, 1);
    }

    static String id(boolean acid, int size) { return (acid ? "acid_slime" : "spike_slime") + SUF[size]; }

    public static MobDef def(boolean acid, int size) {
        final int[] hp = acid ? new int[] {10, 30, 67} : new int[] {12, 30, 67};
        float sc = SCALE[size];
        MobDef d = MobDef.of(id(acid, size), (acid ? "Acid Slime " : "Spike Slime ") + SZ[size], (acid ? "アシッドスライム" : "スパイクスライム") + SZJ[size])
            .game(1, 1).size(0.8f * sc, 0.65f * sc).hp(hp[size]).speed(0.18f + 0.04f * (2 - size)).style(Style.SLIME)
            .egg(acid ? 0x6cbf3a : 0xa06cc8, acid ? 0x2f6a1c : 0xd8cce6)
            .model(() -> model(acid, size))
            .spawn(size == 0 ? 5 : size == 1 ? 6 : 4, 1, size == 0 ? 2 : 1, MobDef.Habitat.SWAMP);
        float reach = 1.2f + 0.6f * sc;
        if (acid) {
            int[] spit = {0, 7, 11}, tack = {3, 10, 16}, weak = {1, 1, 2};
            d.move(Move.of("tackle", 26).intent(Intent.ATTACK, tack[size], 1).reach(reach)
                .sound(9, SoundEvents.SLIME_JUMP, 1f, 1.2f - 0.2f * size)
                .at(12, Fx.dash(0.35, 0.15)).at(14, Fx.melee(tack[size], 1.6 + 0.5 * sc, 120)));
            d.move(Move.of("lick", 28).intent(Intent.DEBUFF).reach(reach + 0.6f)
                .sound(12, SoundEvents.SLIME_SQUISH, 1f, 1.5f)
                .at(13, (s, t) -> { if (t != null && s.distanceTo(t) < 4) { Fx.weak(t, 4 * weak[size]); Fx.particlesAt(s, ParticleTypes.ITEM_SLIME, t.position().add(0, 1, 0), 8, 0.3, 0.05); } }));
            if (size > 0) {
                ProjKind.orb("acid_slime_glob", 0x9be04a, 2 + size).gravity(0.03).trail(ParticleTypes.ITEM_SLIME).life(60);
                d.move(Move.of("corrosive_spit", 30).intent(Intent.ATTACK_DEBUFF, spit[size], 1).ranged(10)
                    .sound(13, SoundEvents.LLAMA_SPIT, 1.2f, 0.8f)
                    .at(14, Fx.shoot("acid_slime_glob", spit[size], 0.8f, 0, 0.25 * sc, 0.5 * sc, v -> { for (int i = 0; i < size; i++) Fx.curse(v, Fx.Curse.SLIMED); })));
                d.pattern(Pattern.random().add("corrosive_spit", 30).add("tackle", 40).add("lick", 30)
                    .maxRepeat("corrosive_spit", 2).maxRepeat("tackle", 1).maxRepeat("lick", 1));
            } else d.pattern(s -> s.getTurn() == 0 ? (s.rand().nextBoolean() ? "tackle" : "lick") : s.lastMove().equals("tackle") ? "lick" : "tackle");
        } else {
            int[] ft = {5, 8, 16}, frail = {1, 1, 2};
            if (size == 0) {
                d.move(Move.of("tackle", 26).intent(Intent.ATTACK, 5, 1).reach(reach)
                    .sound(9, SoundEvents.SLIME_JUMP, 1f, 1.3f)
                    .at(12, Fx.dash(0.35, 0.15)).at(14, Fx.melee(5, 1.6 + 0.5 * sc, 120)));
                d.pattern(s -> "tackle");
            } else {
                d.move(Move.of("flame_tackle", 30).intent(Intent.ATTACK_DEBUFF, ft[size], 1).reach(reach)
                    .sound(12, SoundEvents.SLIME_JUMP, 1f, 0.9f)
                    .at(16, Fx.dash(0.4, 0.15))
                    .at(18, (s, t) -> { Fx.meleeNow(s, ft[size], 1.6 + 0.5 * sc, 120, v -> { for (int i = 0; i < size; i++) Fx.curse(v, Fx.Curse.SLIMED); });
                        Fx.particles(s, ParticleTypes.FLAME, 12, 0.4 * sc); Fx.sound(s, SoundEvents.FIRECHARGE_USE, 0.8f, 1.2f); }));
                d.move(Move.of("lick", 28).intent(Intent.DEBUFF).reach(reach + 0.6f)
                    .sound(12, SoundEvents.SLIME_SQUISH, 1f, 1.5f)
                    .at(13, (s, t) -> { if (t != null && s.distanceTo(t) < 4) { Fx.frail(t, 4 * frail[size]); Fx.particlesAt(s, ParticleTypes.ITEM_SLIME, t.position().add(0, 1, 0), 8, 0.3, 0.05); } }));
                d.pattern(Pattern.random().add("flame_tackle", 30).add("lick", 70).maxRepeat("flame_tackle", 2).maxRepeat("lick", 2));
            }
        }
        if (size == 2) {
            String child = id(acid, 1);
            d.move(Move.of("split", 30).intent(Intent.UNKNOWN).noTarget()
                .sound(4, SoundEvents.SLIME_SQUISH, 1.5f, 0.5f)
                .at(29, (s, t) -> {
                    float cur = s.getHealth();
                    for (int i = -1; i <= 1; i += 2) {
                        SpireMob k = Fx.summon(s, child, Fx.forward(s).z * i * 0.9, -Fx.forward(s).x * i * 0.9);
                        if (k != null) { k.getAttribute(Attributes.MAX_HEALTH).setBaseValue(cur); k.setHealth(cur); }
                    }
                    Fx.particles(s, ParticleTypes.ITEM_SLIME, 30, 0.8); Fx.sound(s, SoundEvents.SLIME_DEATH, 1.5f, 0.6f);
                    s.discard();
                }))
                // Split: when at or below half HP, cancel what it was doing and split next
                .onHurt((s, src, dmg) -> {
                    if (!s.flags.contains("split") && dmg > 0 && s.getHealth() - dmg > 0 && s.getHealth() - dmg <= s.getMaxHealth() / 2) {
                        s.flags.add("split"); s.cancelMove(); s.forceNext("split");
                    }
                    return dmg;
                });
        }
        return d;
    }

    public static final class AcidS { public static ModelSpec model() { return Slime.model(true, 0); } public static MobDef def() { return Slime.def(true, 0); } }
    public static final class AcidM { public static ModelSpec model() { return Slime.model(true, 1); } public static MobDef def() { return Slime.def(true, 1); } }
    public static final class AcidL { public static ModelSpec model() { return Slime.model(true, 2); } public static MobDef def() { return Slime.def(true, 2); } }
    public static final class SpikeS { public static ModelSpec model() { return Slime.model(false, 0); } public static MobDef def() { return Slime.def(false, 0); } }
    public static final class SpikeM { public static ModelSpec model() { return Slime.model(false, 1); } public static MobDef def() { return Slime.def(false, 1); } }
    public static final class SpikeL { public static ModelSpec model() { return Slime.model(false, 2); } public static MobDef def() { return Slime.def(false, 2); } }
}
