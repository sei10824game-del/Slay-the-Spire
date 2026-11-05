package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory elite (Knight Gang): Flail Knight. 101 HP. Opens with Ram (15), then random: Breaker (+3 Str, never twice in a row), Flail (9 x2), Ram (15). */
public final class FlailKnight {
    private FlailKnight() {}
    static final int PLATE = 0x8a909c, PLATE_D = 0x4a505c, RED = 0xa02a2a, GOLD = 0xc8a040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_flail_knight");
        Kit.biped(m, 0x7a6a5a, PLATE, PLATE_D, GOLD);
        m.scale(1.4f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.7f, 0, -3, 9, 9, 6).color(PLATE).grad(0xaab0bc, PLATE_D).bevel(0.2f);
        torso.box(-1, 1, -3.2f, 2, 7, 1).color(RED).flat();
        body.box(-4.7f, -5, -3, 9, 5, 6).color(RED).grad(0xc03a3a, 0x701818);                            // tabard
        head.box(-3.7f, 0, -3.7f, 7, 7, 7).color(PLATE).grad(0xaab0bc, PLATE_D).bevel(0.25f);             // great helm
        head.box(-3, 3, 3.6f, 6, 1, 1).color(0x101012).glow(Face.FRONT, 0, 0, 6, 1, 0xff5030);
        head.box(-0.5f, 7, -3, 1, 3, 6).color(RED).flat();
        for (int s = -1; s <= 1; s += 2) torso.add("pad" + s).at(s * 5.5f, 8.5f, 0).box(-2.7f, -1, -2.7f, 5, 3, 5).color(PLATE).bevel(0.25f);
        Part fl = m.byName.get("handR").add("flail").at(0, -2, 0).rot(-70, 0, 0);
        fl.box(-0.5f, -3, -0.5f, 1, 8, 1).color(0x5a4636);
        Part chain = fl.add("chain").at(0, 5, 0);
        chain.box(-0.5f, 0, -0.5f, 1, 7, 1).color(0x6a6e76).flat();
        Part ball = chain.add("ball").at(0, 7, 0);
        ball.box(-2.5f, 0, -2.5f, 5, 5, 5).color(PLATE_D).bevel(0.3f);
        for (int i = 0; i < 4; i++) ball.box(-0.5f, 1.5f, i < 2 ? (i == 0 ? 2.5f : -3.5f) : -0.5f, 1, 2, 1).color(0xcfd5dc).flat();
        ball.box(-3.5f, 1.5f, -0.5f, 1, 2, 1).color(0xcfd5dc).flat();
        ball.box(2.5f, 1.5f, -0.5f, 1, 2, 1).color(0xcfd5dc).flat();
        Part sh = m.byName.get("armL").add("shield").at(2, -4, 1);
        sh.box(-1, -6, -4, 2, 12, 8).color(PLATE_D).bevel(0.3f); sh.box(-1.4f, -2, -1, 1, 4, 2).color(RED).flat();
        Kit.bipedBase(m);
        m.clips.get("idle").rot("chain", 0, 0, 0, 8).rot("chain", 24, 0, 0, -8).rot("chain", 48, 0, 0, 8);
        Clip r = m.clip("ram", 32);
        Kit.seq(r, "torso", 32, 14, 8, -12, 0, 0, 14, 35, 0, 0, 24, 20, 0, 0);
        Kit.seq(r, "armL", 32, 14, 8, 20, 0, -10, 14, -100, 0, -10, 24, -80, 0, -10);
        Kit.seq(r, "armR", 32, 14, 8, 20, 0, 10, 14, -60, 0, 10);
        Kit.seq(r, "legR", 32, -1, 8, 20, 0, 0, 14, -35, 0, 0, 22, 10, 0, 0);
        Kit.seq(r, "legL", 32, -1, 8, -15, 0, 0, 14, 30, 0, 0, 22, -8, 0, 0);
        Kit.sp(r, "body", 32, 14, 8, 0, -2, -3, 14, 0, -1, 7, 24, 0, 0, 5);
        Kit.buff(m, "breaker", 36, 14);
        Clip f = m.clip("flail", 40);
        Kit.seq(f, "armR", 40, -1, 4, -80, -50, -10, 12, -100, 70, 10, 20, -80, 60, 10, 28, -100, -70, -10, 34, -80, -30, 0);
        Kit.seq(f, "torso", 40, -1, 4, 0, -50, 0, 12, 4, 55, 0, 20, 0, 55, 0, 28, 4, -55, 0, 34, 0, 0, 0);
        Kit.seq(f, "chain", 40, -1, 4, 0, 0, -30, 12, 0, 0, 40, 20, 0, 0, 40, 28, 0, 0, -40);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_flail_knight", "Flail Knight", "フレイルナイト")
            .game(2, 3).size(1.0f, 2.5f).hp(101).speed(0.23f).style(Style.BRUTE).egg(0x8a909c, 0xa02a2a).elite()
            .model(FlailKnight::model)
            .spawn(1, 1, 1, MobDef.Habitat.PLAINS)
            .pattern(Pattern.first("ram", Pattern.random().add("breaker", 1).add("flail", 1).add("ram", 1).maxRepeat("breaker", 1)))
            .move(Move.of("ram", 32).intent(Intent.ATTACK, 15, 1).reach(3.2f)
                .sound(12, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.6f).at(13, Fx.dash(0.5, 0.05)).at(14, Fx.melee(15, 3.2, 100)))
            .move(Move.of("breaker", 36).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.ANVIL_PLACE, 0.8f, 0.6f).at(14, (s, t) -> Kit.str(s, 3)))
            .move(Move.of("flail", 40).intent(Intent.ATTACK, 9, 2).reach(3.4f)
                .sound(11, SoundEvents.CHAIN_BREAK, 1f, 0.8f).sound(26, SoundEvents.CHAIN_BREAK, 1f, 0.8f)
                .at(12, Kit.hit(9, 3.6, 130)).at(28, Kit.hit(9, 3.6, 130)));
    }
}
