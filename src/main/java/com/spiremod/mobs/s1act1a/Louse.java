package com.spiremod.mobs.s1act1a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 1 Louse (red & green). Curl Up 3-7: the first time it takes damage it curls into a ball and gains that much Block.
 * Bite 5-7 (fixed per louse). Red: Grow (+3 Str) 25% / Bite 75%. Green: Spit Web (2 Weak) 25% / Bite 75%. No move 3 times in a row.
 */
public final class Louse {
    private Louse() {}

    static final int LEG = 0x2a1c18, EYE = 0xfff2b0, MAND = 0x3a2a20;

    public static ModelSpec model(boolean red) {
        int shell = red ? 0xc8473a : 0x7aa64a, shellL = red ? 0xe8735a : 0xa3c86a, shellD = red ? 0x7d2620 : 0x46672a,
            belly = red ? 0xe9b9a0 : 0xd9e0a8;
        ModelSpec m = new ModelSpec(red ? "red_louse" : "green_louse");
        Part body = m.add("body").at(0, 2, 0);
        body.box(-4, 0, -2, 8, 6, 4).color(shell).grad(shellL, shellD).bevel(0.3f).band(1, 1, shellD);
        body.box(-3.5f, -0.5f, -2, 7, 1, 4).color(belly);
        body.box(-0.5f, 5.5f, -1.5f, 1, 1, 3).color(shellL).flat();                         // dorsal ridge
        // front shell plate + head
        Part f1 = body.add("f1").at(0, 5.5f, 2);
        f1.box(-3.5f, -5.5f, 0, 7, 5, 3).color(shell).grad(shellL, shellD).bevel(0.3f).band(1, 1, shellD);
        f1.box(-3, -6, 0, 6, 1, 3).color(belly);
        Part head = f1.add("head").at(0, -5, 3);
        head.box(-2.5f, 0, 0, 5, 3, 2).color(shellD).grad(shell, shellD).bevel(0.2f)
            .glow(Face.FRONT, 0, 1, 1, 1, EYE).glow(Face.FRONT, 4, 1, 1, 1, EYE).px(Face.FRONT, 2, 2, LEG);
        for (int s = -1; s <= 1; s += 2) {
            Part ant = head.add(s < 0 ? "antR" : "antL").at(s * 1.5f, 3, 1).rot(-30, 0, s * -30);
            ant.box(-0.5f, 0, -0.5f, 1, 4, 1).color(LEG);
            ant.box(-0.5f, 4, -0.5f, 1, 1, 1).color(shellL).flat();
            Part md = head.add(s < 0 ? "mandR" : "mandL").at(s * 1.2f, 0.5f, 2).rot(0, s * -15, 0);
            md.box(-0.5f, -0.5f, 0, 1, 1, 2).color(MAND);
        }
        // back plates + tail
        Part b1 = body.add("b1").at(0, 5.5f, -2);
        b1.box(-3.5f, -5.5f, -3, 7, 5, 3).color(shell).grad(shellL, shellD).bevel(0.3f).band(1, 1, shellD);
        b1.box(-3, -6, -3, 6, 1, 3).color(belly);
        Part b2 = b1.add("b2").at(0, -0.5f, -3);
        b2.box(-2.5f, -4.5f, -2, 5, 4, 2).color(shell).grad(shellL, shellD).bevel(0.3f);
        b2.box(-1.5f, -3.5f, -3, 3, 2, 1).color(shellD);
        // six little legs
        for (int i = 0; i < 3; i++) for (int s = -1; s <= 1; s += 2) {
            Part leg = body.add("leg" + i + (s < 0 ? "R" : "L")).at(s * 3.5f, 0.5f, 2.5f - i * 2.5f).rot(0, 0, s * -30);
            leg.box(-0.5f, -3, -0.5f, 1, 3, 1).color(LEG);
        }
        m.look("head", 30, 20);
        m.proc((r, c) -> { // scuttle; legs tucked away while curled
            boolean curled = "curl".equals(c.base) && c.move == null;
            for (int i = 0; i < 3; i++) { r.show("leg" + i + "R", !curled); r.show("leg" + i + "L", !curled); }
            if (c.swingAmt > 0.05f && !"curl".equals(c.base)) for (int i = 0; i < 3; i++) {
                float a = (float) Math.sin(c.swing * 1.8f + i * 2.1f) * 40 * c.swingAmt;
                r.rot("leg" + i + "R", a, 0, 0); r.rot("leg" + i + "L", -a, 0, 0);
            }
        });
        m.clip("idle", 30).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 15, 0, 0.4f, 0).pos("body", 30, 0, 0, 0)
            .rot("antR", 0, 0, 0, 0).rot("antR", 10, 15, 0, -10).rot("antR", 20, -10, 0, 5).rot("antR", 30, 0, 0, 0)
            .rot("antL", 0, 0, 0, 0).rot("antL", 10, -10, 0, 5).rot("antL", 20, 15, 0, -10).rot("antL", 30, 0, 0, 0)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 22, 0, 0, 0).rot("mandR", 25, 0, 25, 0).rot("mandR", 28, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 22, 0, 0, 0).rot("mandL", 25, 0, -25, 0).rot("mandL", 28, 0, 0, 0);
        // CURL (base stance after Curl Up): rolled into an armoured ball, rocking slightly
        Clip curl = m.clip("curl", 40).loop()
            .rot("f1", 0, 80, 0, 0).rot("f1", 40, 80, 0, 0).rot("head", 0, 80, 0, 0).rot("head", 40, 80, 0, 0)
            .rot("b1", 0, -80, 0, 0).rot("b1", 40, -80, 0, 0).rot("b2", 0, -80, 0, 0).rot("b2", 40, -80, 0, 0)
            .pos("body", 0, 0, -1.5f, 0).pos("body", 20, 0, -1.5f, 0).pos("body", 40, 0, -1.5f, 0)
            .rot("body", 0, 0, 0, 6).rot("body", 20, 0, 0, -6).rot("body", 40, 0, 0, 6);
        // BITE: rear the head up, scuttle-lunge forward, mandibles snap
        m.clip("bite", 24)
            .rot("f1", 0, 0, 0, 0).rot("f1", 8, -30, 0, 0).rot("f1", 12, 15, 0, 0, Ease.IN_CUBIC).rot("f1", 24, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -25, 0, 0).rot("head", 12, 20, 0, 0, Ease.IN_CUBIC).rot("head", 24, 0, 0, 0)
            .rot("b1", 0, 0, 0, 0).rot("b1", 8, 15, 0, 0).rot("b1", 12, -5, 0, 0).rot("b1", 24, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0.5f, -2).pos("body", 12, 0, 1.5f, 5, Ease.IN_CUBIC).pos("body", 16, 0, 0, 4).pos("body", 24, 0, 0, 0)
            .rot("mandR", 0, 0, 0, 0).rot("mandR", 8, 0, -40, 0).rot("mandR", 12, 0, 20, 0, Ease.IN_CUBIC).rot("mandR", 18, 0, 0, 0)
            .rot("mandL", 0, 0, 0, 0).rot("mandL", 8, 0, 40, 0).rot("mandL", 12, 0, -20, 0, Ease.IN_CUBIC).rot("mandL", 18, 0, 0, 0)
            .rot("antR", 0, 0, 0, 0).rot("antR", 12, 40, 0, 0).rot("antR", 24, 0, 0, 0)
            .rot("antL", 0, 0, 0, 0).rot("antL", 12, 40, 0, 0).rot("antL", 24, 0, 0, 0);
        if (red) // GROW: hunker, then swell up, shell plates flare, shiver
            m.clip("grow", 32)
                .scl("body", 0, 1, 1, 1).scl("body", 8, 1.1f, 0.8f, 1.1f).scl("body", 14, 0.95f, 1.3f, 0.95f, Ease.OUT_BACK)
                .scl("body", 18, 1.15f, 1.15f, 1.15f).scl("body", 21, 1.05f, 1.25f, 1.05f).scl("body", 24, 1.15f, 1.15f, 1.15f).scl("body", 32, 1, 1, 1)
                .rot("f1", 0, 0, 0, 0).rot("f1", 14, -15, 0, 0, Ease.OUT_BACK).rot("f1", 26, -12, 0, 0).rot("f1", 32, 0, 0, 0)
                .rot("b1", 0, 0, 0, 0).rot("b1", 14, 15, 0, 0, Ease.OUT_BACK).rot("b1", 26, 12, 0, 0).rot("b1", 32, 0, 0, 0)
                .rot("head", 0, 0, 0, 0).rot("head", 14, -30, 0, 0).rot("head", 17, -30, 10, 0).rot("head", 20, -30, -10, 0).rot("head", 23, -30, 10, 0).rot("head", 32, 0, 0, 0)
                .rot("antR", 0, 0, 0, 0).rot("antR", 14, -30, 0, -25).rot("antR", 32, 0, 0, 0)
                .rot("antL", 0, 0, 0, 0).rot("antL", 14, -30, 0, 25).rot("antL", 32, 0, 0, 0);
        else // SPIT WEB: rear up, head bobs back, then spits forward
            m.clip("spit_web", 30)
                .rot("body", 0, 0, 0, 0).rot("body", 10, -25, 0, 0).rot("body", 14, 10, 0, 0, Ease.IN_CUBIC).rot("body", 22, 5, 0, 0).rot("body", 30, 0, 0, 0)
                .rot("f1", 0, 0, 0, 0).rot("f1", 10, -15, 0, 0).rot("f1", 14, 10, 0, 0, Ease.IN_CUBIC).rot("f1", 30, 0, 0, 0)
                .rot("head", 0, 0, 0, 0).rot("head", 10, -30, 0, 0).rot("head", 14, 15, 0, 0, Ease.IN_CUBIC).rot("head", 30, 0, 0, 0)
                .scl("head", 0, 1, 1, 1).scl("head", 10, 1.2f, 1.25f, 0.9f).scl("head", 14, 0.9f, 0.85f, 1.25f).scl("head", 20, 1, 1, 1)
                .rot("mandR", 0, 0, 0, 0).rot("mandR", 12, 0, -45, 0).rot("mandR", 20, 0, 0, 0)
                .rot("mandL", 0, 0, 0, 0).rot("mandL", 12, 0, 45, 0).rot("mandL", 20, 0, 0, 0)
                .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 1.5f, -1).pos("body", 14, 0, 0, 1).pos("body", 30, 0, 0, 0);
        // CURL UP reaction (played as hurt when it curls): quick snap shut
        m.clip("hurt", 10)
            .rot("f1", 0, 0, 0, 0).rot("f1", 3, 25, 0, 0).rot("f1", 10, 0, 0, 0)
            .rot("b1", 0, 0, 0, 0).rot("b1", 3, -25, 0, 0).rot("b1", 10, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 3, 0, 0, -1.5f).pos("body", 10, 0, 0, 0);
        Clip death = m.clip("death", 24)
            .rot("body", 0, 0, 0, 0).rot("body", 8, 0, 0, 0).rot("body", 18, 0, 0, 180, Ease.IN_QUAD).rot("body", 24, 0, 0, 175, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 4, 0, Ease.OUT_QUAD).pos("body", 18, 0, 6, 0).pos("body", 24, 0, 5, 0);
        for (int i = 0; i < 3; i++) death.rot("leg" + i + "R", 0, 0, 0, 0).rot("leg" + i + "R", 6, 40, 0, 0).rot("leg" + i + "R", 10, -40, 0, 0).rot("leg" + i + "R", 14, 30, 0, 0).rot("leg" + i + "R", 24, 0, 0, -30)
            .rot("leg" + i + "L", 0, 0, 0, 0).rot("leg" + i + "L", 6, -40, 0, 0).rot("leg" + i + "L", 10, 40, 0, 0).rot("leg" + i + "L", 14, -30, 0, 0).rot("leg" + i + "L", 24, 0, 0, 30);
        return m;
    }

    static int bite(SpireMob s) { if (s.count("bite") == 0) s.setCount("bite", 5 + s.rand().nextInt(3)); return s.count("bite"); }

    static MobDef base(boolean red) {
        String id = red ? "red_louse" : "green_louse";
        String other = red ? "grow" : "spit_web";
        MobDef d = MobDef.of(id, red ? "Red Louse" : "Green Louse", red ? "レッドラウス" : "グリーンラウス")
            .game(1, 1).size(0.6f, 0.6f).hp(red ? 12 : 14).speed(0.3f).style(Style.SPIDER).egg(red ? 0xc8473a : 0x7aa64a, 0x2a1c18)
            .model(() -> model(red))
            .spawn(7, 1, 3, red ? MobDef.Habitat.FOREST : MobDef.Habitat.SWAMP)
            .pattern(Pattern.random().add("bite", 75).add(other, 25).maxRepeat("bite", 2).maxRepeat(other, 2))
            .move(Move.of("bite", 24).intent(Intent.ATTACK, 6, 1).reach(1.8f)
                .sound(8, SoundEvents.SPIDER_AMBIENT, 0.8f, 1.6f)
                .at(12, (s, t) -> Fx.meleeNow(s, bite(s), 2.6, 90, null)))
            // Curl Up: first damage -> gain 3-7 block (absorbs this hit) and roll into a ball for a while
            .onHurt((s, src, dmg) -> {
                if (dmg > 0 && !s.flags.contains("curled")) {
                    s.flags.add("curled"); s.addBlock(3 + s.rand().nextInt(5)); s.setBase("curl"); s.setCount("curlT", 60);
                    Fx.sound(s, SoundEvents.ARMOR_EQUIP_TURTLE, 1f, 1.4f);
                }
                return dmg;
            })
            .onTick(s -> { if (s.count("curlT") > 0 && s.inc("curlT") > 0 && s.count("curlT") >= 60 && "curl".equals(s.getBase())) {}
                if (s.count("curlT") > 0) { s.setCount("curlT", s.count("curlT") - 1); if (s.count("curlT") == 0) s.setBase("idle"); } })
            .onMoveEnd(s -> { if ("curl".equals(s.getBase())) { s.setBase("idle"); s.setCount("curlT", 0); } });
        if (red) d.move(Move.of("grow", 32).intent(Intent.BUFF).noTarget()
            .at(14, (s, t) -> { s.addStrength(3); Fx.sound(s, SoundEvents.SPIDER_AMBIENT, 1f, 0.5f); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 5, 0.3); }));
        else {
            ProjKind.orb("green_louse_web", 0xeef2e0, 2).trail(ParticleTypes.WHITE_ASH).life(40);
            d.move(Move.of("spit_web", 30).intent(Intent.DEBUFF).ranged(8)
                .sound(12, SoundEvents.LLAMA_SPIT, 1f, 1.3f)
                .at(14, (s, t) -> { if (t == null) return;
                    Fx.shootAt(s, "green_louse_web", Fx.rel(s, 0.5, 0, 0.3), t, 0, 0.9f, 0, null); Fx.weak(t, 8); }));
        }
        return d;
    }
    public static ModelSpec model() { return model(true); }
    public static MobDef def() { return base(true); }
    /** green variant (preview/validate as Louse$Green) */
    public static final class Green {
        public static ModelSpec model() { return Louse.model(false); }
        public static MobDef def() { return base(false); }
    }
}
