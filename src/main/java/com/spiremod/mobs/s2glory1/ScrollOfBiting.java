package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Scroll of Biting. 31-38 HP, Paper Cuts 2 (unblocked damage costs max HP). Chomp (14) -> More Teeth (+2 Str) -> Chew (5 x2);
 *  after Chew: Chomp or Chew (weight 2). Each scroll in a group starts on a different move. */
public final class ScrollOfBiting {
    private ScrollOfBiting() {}
    static final int PAPER = 0xe6d6a6, PAPER_D = 0xb89c64, ROLL = 0x7a4a2a, INK = 0x4a3820, GUM = 0xa02a3a, TOOTH = 0xf8f4e0;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_scroll_of_biting");
        Part body = m.add("body").at(0, 11, 0);
        // upper half (head) : parchment + roller, sits on top
        Part head = body.add("head").at(0, 0, 0);
        head.box(-5, 0, -1, 10, 9, 2).color(PAPER).grad(0xf2e6bc, PAPER_D).noise(0.12f).bevel(0.15f);
        head.box(-6, 8, -2, 12, 3, 4).color(ROLL).grad(0x94603a, ROLL).bevel(0.2f);                       // top roller
        head.box(-6.6f, 8, -1, 1, 3, 2).color(0xd0a640).flat();
        head.box(5.6f, 8, -1, 1, 3, 2).color(0xd0a640).flat();
        head.box(-4, 2, 0.9f, 3, 3, 1).color(0x1a0a0a).glow(Face.FRONT, 0, 0, 3, 3, 0xff4a30);              // eyes
        head.box(1, 2, 0.9f, 3, 3, 1).color(0x1a0a0a).glow(Face.FRONT, 0, 0, 3, 3, 0xff4a30);
        head.box(-4, 5.2f, 0.9f, 3, 1, 1).color(INK).flat();                                               // angry brows
        head.box(1, 5.2f, 0.9f, 3, 1, 1).color(INK).flat();
        for (int i = 0; i < 5; i++) head.box(-4 + i * 2, -1.5f, 0.9f, 1, 2, 1).color(TOOTH).flat();       // upper fangs
        head.box(-4.5f, -0.1f, 0.8f, 9, 1, 1).color(GUM).flat();
        Part jaw = body.add("jaw").at(0, 0, 0);
        jaw.box(-5, -8, -1, 10, 8, 2).color(PAPER).grad(PAPER_D, 0xcdb27a).noise(0.12f).bevel(0.15f);
        jaw.box(-6, -10, -2, 12, 3, 4).color(ROLL).grad(ROLL, 0x5a3418).bevel(0.2f);                       // bottom roller
        jaw.box(-6.6f, -10, -1, 1, 3, 2).color(0xd0a640).flat();
        jaw.box(5.6f, -10, -1, 1, 3, 2).color(0xd0a640).flat();
        for (int i = 0; i < 5; i++) jaw.box(-3 + i * 2, 0, 0.9f, 1, 2, 1).color(TOOTH).flat();             // lower fangs
        jaw.box(-4.5f, -1, 0.8f, 9, 1, 1).color(GUM).flat();
        for (int i = 0; i < 4; i++) jaw.box(-3.5f, -3 - i * 1.4f, 1.0f, 7 - (i % 2), 1, 1).color(INK).flat(); // text lines
        Part rib = jaw.add("ribbon").at(0, -10, 0);
        rib.box(-0.5f, -6, -0.5f, 1, 6, 1).color(0xc2362f).flat();
        Part rib2 = rib.add("ribbon2").at(0, -6, 0);
        rib2.box(-0.5f, -4, -0.5f, 1, 4, 1).color(0xc2362f).flat();
        m.look("head", 40, 25);
        m.bob(1.2f, 0.18f);
        m.clip("idle", 40).loop()
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 14, -6, 0, 0).rot("jaw", 20, 0, 0, 0).rot("jaw", 40, 0, 0, 0)
            .rot("body", 0, 0, 0, -5).rot("body", 20, 0, 0, 5).rot("body", 40, 0, 0, -5)
            .rot("ribbon", 0, 0, 0, 15).rot("ribbon", 20, 0, 0, -15).rot("ribbon", 40, 0, 0, 15)
            .rot("ribbon2", 0, 0, 0, -20).rot("ribbon2", 20, 0, 0, 20).rot("ribbon2", 40, 0, 0, -20);
        Clip c = m.clip("chomp", 28);
        Kit.seq(c, "jaw", 28, 13, 6, 40, 0, 0, 13, -4, 0, 0, 16, 10, 0, 0);
        Kit.seq(c, "head", 28, 13, 6, -22, 0, 0, 13, 24, 0, 0, 20, 5, 0, 0);
        Kit.sp(c, "body", 28, 13, 6, 0, 0, -3, 13, 0, -1, 7);
        Clip t = m.clip("more_teeth", 36);
        Kit.seq(t, "jaw", 36, -1, 6, 25, 0, 0, 12, 50, 0, 0, 28, 50, 0, 0);
        Kit.seq(t, "head", 36, -1, 6, -10, 0, 0, 12, -30, 0, 0, 28, -28, 0, 0);
        t.scl("body", 0, 1, 1, 1).scl("body", 14, 1.25f, 1.25f, 1.25f, Ease.OUT_BACK).scl("body", 22, 1.25f, 1.25f, 1.25f).scl("body", 36, 1, 1, 1);
        t.rot("body", 10, 0, 0, 0).rot("body", 16, 0, 0, 8).rot("body", 20, 0, 0, -8).rot("body", 24, 0, 0, 8).rot("body", 28, 0, 0, 0);
        Clip h = m.clip("chew", 36);
        for (int k = 0; k < 2; k++) {
            int b = 8 + k * 10;
            h.rot("jaw", b, 45, 0, 0).rot("jaw", b + 4, -4, 0, 0, Ease.IN_CUBIC).rot("jaw", b + 6, 12, 0, 0);
            h.rot("head", b, -15, 0, 0).rot("head", b + 4, 18, 0, 0, Ease.IN_CUBIC);
            h.pos("body", b, 0, 0, -2).pos("body", b + 4, 0, -1, 5, Ease.IN_CUBIC);
        }
        h.rot("jaw", 0, 0, 0, 0).rot("jaw", 36, 0, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 36, 0, 0, 0).pos("body", 0, 0, 0, 0).pos("body", 36, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -12, 0, 25).rot("body", 10, 0, 0, 0).rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 30, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 26)
            .rot("body", 0, 0, 0, 0).rot("body", 14, 30, 0, 180).rot("body", 26, 90, 0, 200)
            .pos("body", 0, 0, 0, 0).pos("body", 26, 0, -9, 0, Ease.OUT_BOUNCE)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 60, 0, 0).rot("jaw", 26, 20, 0, 0)
            .scl("body", 0, 1, 1, 1).scl("body", 26, 1, 0.6f, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_scroll_of_biting", "Scroll of Biting", "噛みつく巻物")
            .game(2, 3).size(0.8f, 1.3f).hp(34).speed(0.2f).flying().style(Style.INSECT).egg(0xe6d6a6, 0xa02a3a)
            .model(ScrollOfBiting::model)
            .spawn(8, 3, 4, MobDef.Habitat.ANY)
            .onSpawn(s -> s.setCount("off", s.rand().nextInt(3)))
            .pattern(s -> {
                String[] o = {"chomp", "more_teeth", "chew"};
                if (s.getTurn() == 0) return o[s.count("off")];
                switch (s.lastMove()) {
                    case "chomp": return "more_teeth";
                    case "more_teeth": return "chew";
                    default: return s.rand().nextInt(3) == 0 ? "chomp" : "chew";
                }
            })
            .move(Move.of("chomp", 28).intent(Intent.ATTACK, 14, 1).reach(2.2f)
                .sound(12, SoundEvents.FOX_BITE, 1f, 0.7f)
                .at(13, Fx.melee(14, 2.6, 90, v -> Kit.paperCut(v, 1))))
            .move(Move.of("more_teeth", 36).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.EVOKER_FANGS_ATTACK, 0.8f, 1.5f).at(14, (s, t) -> Kit.str(s, 2)))
            .move(Move.of("chew", 36).intent(Intent.ATTACK, 5, 2).reach(2.2f)
                .sound(12, SoundEvents.FOX_BITE, 0.8f, 0.9f).sound(22, SoundEvents.FOX_BITE, 0.8f, 0.9f)
                .at(12, (s, t) -> { if (t != null) t.invulnerableTime = 0; Fx.meleeNow(s, 5, 2.6, 90, v -> Kit.paperCut(v, 1)); })
                .at(22, (s, t) -> { if (t != null) t.invulnerableTime = 0; Fx.meleeNow(s, 5, 2.6, 90, v -> Kit.paperCut(v, 1)); }));
    }
}
