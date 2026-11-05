package com.spiremod.mobs.s2hive1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Battleworn Dummy event enemy "Battle Friend" V1.0/V2.0/V3.0 (75 / 150 / 300 HP). Always does nothing; the event's 3-turn
 * time limit is not modelled (needs event code). Variant 1..3 -> bigger, tougher-looking training dummy.
 */
public final class BattlewornDummy {
    private BattlewornDummy() {}
    static final int[] WOOD = {0xb08850, 0x8a96a4, 0x2a2a34};
    static final int[] WOOD_D = {0x6a4a28, 0x555e6c, 0x14141a};
    static final int[] TRIM = {0xd8c070, 0xe0e4ea, 0xd8a830};
    static final int RED = 0xc83030, WHITE = 0xf0eee0;

    static ModelSpec model(int v) {
        int c = WOOD[v], d = WOOD_D[v], g = TRIM[v];
        ModelSpec m = new ModelSpec("s2_battleworn_dummy_" + (v + 1));
        m.scale(1f + 0.18f * v);
        Part base = m.add("base").at(0, 0, 0);
        base.box(-7, 0, -7, 14, 3, 14).color(d).bevel(0.2f).noise(0.12f);
        base.box(-1.5f, 3, -1.5f, 3, 14, 3).color(d).bevel(0.15f);
        Part body = m.add("body").at(0, 14, 0);                            // sway pivot near the top of the post
        body.box(-4.5f, -4, -3.5f, 9, 16, 7).color(c).grad(c, d).bevel(0.2f).noise(0.14f);
        body.box(-4.8f, 2, -3.8f, 9, 2, 7).color(g).flat().inflate(0.1f);   // band
        // painted target on chest
        body.box(-3, 4, 3.4f, 6, 6, 1).color(WHITE).flat().paint(Face.FRONT, 1, 1, 4, 4, RED).paint(Face.FRONT, 2, 2, 2, 2, WHITE).paint(Face.FRONT, 2, 2, 1, 1, RED);
        Part head = body.add("head").at(0, 12, 0);
        head.box(-3, 0, -3, 6, 6, 6).color(c).grad(c, d).bevel(0.2f).noise(0.14f);
        head.box(-2, 2.5f, 3, 1, 1, 1).color(0x201810).flat().glow(Face.FRONT, 0, 0, 1, 1, v == 2 ? 0xff4040 : 0x60e0ff);
        head.box(1, 2.5f, 3, 1, 1, 1).color(0x201810).flat().glow(Face.FRONT, 0, 0, 1, 1, v == 2 ? 0xff4040 : 0x60e0ff);
        head.box(-3.4f, 6, -3.4f, 7, 2, 7).color(g).flat().bevel(0.2f);         // cap
        for (int s = -1; s <= 1; s += 2) {
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 5.5f, 9, 0);
            arm.box(-1.5f, -9, -1.5f, 3, 9, 3).color(c).grad(c, d).bevel(0.15f).noise(0.14f);
            arm.box(-2, -11, -2, 4, 3, 4).color(d).bevel(0.2f);                 // padded mitt
        }
        // damage: stuffing poking out, arrows/nails, plating by version
        body.box(-4, 8, -3, 2, 3, 2).color(0xe0d090).flat().noise(0.3f);
        if (v >= 1) body.box(-4.9f, -3, -3.9f, 9, 5, 8).color(0x9aa4b2).grad(0xc0c8d4, 0x707a88).bevel(0.25f).inflate(0.1f);
        if (v >= 2) {
            head.box(-3.4f, 0, -3.4f, 7, 6, 7).color(0x3a3a46).bevel(0.2f).inflate(0.1f).glow(Face.FRONT, 1, 3, 5, 1, 0xff3030);
            body.box(-1, 12, -1, 2, 6, 2).color(g).flat();
        }
        Clip i = m.clip("idle", 60).loop();
        Kit.R(i, "body", 0, 0, 0, 1.5f, 0, 30, 0, 0, -1.5f, 0, 60, 0, 0, 1.5f, 0);
        Kit.R(i, "armR", 0, 0, 0, 8, 0, 30, 0, 0, 12, 0, 60, 0, 0, 8, 0);
        Kit.R(i, "armL", 0, 0, 0, -8, 0, 30, 0, 0, -12, 0, 60, 0, 0, -8, 0);
        Clip n = m.clip("nothing", 40);
        Kit.R(n, "body", 6, 0, 0, 5, 0, 20, 0, 0, -5, 0, 32, 0, 0, 3, 0, 40, 0, 0, 0, 0);
        Kit.R(n, "head", 6, 0, 20, 0, 0, 20, 0, -20, 0, 0, 40, 0, 0, 0, 0);
        Kit.hurt(m, "body", "head");
        Clip d2 = m.clip("death", 30);
        Kit.R(d2, "body", 4, 0, 0, 10, 0, 18, 0, 0, 100, 4, 30, 0, 0, 92, 3);
        Kit.P(d2, "body", 18, 0, 0, 0, 0, 30, 0, -9, 0, 3);
        Kit.R(d2, "head", 8, 0, 0, 0, 0, 20, 20, 0, 40, 3, 30, 20, 0, 40, 0);
        Kit.R(d2, "armR", 10, 0, 0, 90, 0, 30, 0, 0, 120, 0);
        Kit.R(d2, "armL", 10, 0, 0, -90, 0, 30, 0, 0, -120, 0);
        return m;
    }

    static MobDef def(int v) {
        int[] hp = {75, 150, 300};
        String[] ver = {"V1.0", "V2.0", "V3.0"};
        MobDef d = MobDef.of("s2_battleworn_dummy_" + (v + 1), "Battle Friend " + ver[v], "バトルフレンド " + ver[v])
            .game(2, 3).size(0.9f + 0.15f * v, 1.8f + 0.3f * v).hp(hp[v]).speed(0f).style(Style.SKELETON).egg(WOOD[v], RED)
            .model(() -> model(v)).spawn(0, 1, 1, MobDef.Habitat.NONE)
            .pattern(s -> "nothing")
            .move(Move.of("nothing", 40).intent(Intent.NONE).noTarget().noFace()
                .at(14, (s, t) -> Fx.particles(s, ParticleTypes.CRIT, 2, 0.4)));
        d.noPush = true;
        return d;
    }
}
