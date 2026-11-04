package com.spiremod.mobs.s1act2b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 elite: Book of Stabbing. 160-164 HP. Painful Stabs: each unblocked hit adds a Wound.
 * Multi-Stab 6 x N (N starts at 2, +1 every use), Single Stab 21. Opens with Multi-Stab; then 85% Multi / 15% Single;
 * never Single twice in a row, never Multi three times in a row.
 */
public final class BookOfStabbing {
    private BookOfStabbing() {}
    static final int COVER = 0x7a1c24, COVER_D = 0x4a0e14, GOLD = 0xd8a63a, PAGE = 0xe8dcb8, PAGE_D = 0xb8a880, STEEL = 0xc4cad4, EYE = 0xff3030;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("book_of_stabbing");
        m.scale(1.3f);
        Part body = m.add("body").at(0, 14, 0);
        Part spine = body.add("spine").at(0, 0, 0);
        spine.box(-1.5f, -10, -7, 3, 20, 14).color(COVER_D).grad(COVER, COVER_D).bevel(0.2f);
        // open front cover = a pair of covers hinged at the spine, forming the toothy "mouth"
        Part coverU = body.add("coverU").at(0, 8, 0);       // hinge at the top of the book, swinging open
        Part coverL = body.add("coverL").at(0, -8, 0);
        // we orient the book standing up, face on +z: pages block is the body, covers hinge top/bottom
        Part pages = body.add("pages").at(0, 0, 0);
        pages.box(-5.5f, -7, -6, 11, 14, 12).color(PAGE).grad(PAGE, PAGE_D).noise(0.06f)
            .band(2, 1, PAGE_D).band(5, 1, PAGE_D).band(8, 1, PAGE_D).band(11, 1, PAGE_D);
        coverU.box(-6, -1, -6.5f, 12, 3, 13).color(COVER).grad(COVER, COVER_D).bevel(0.2f);
        coverL.box(-6, -2, -6.5f, 12, 3, 13).color(COVER).grad(COVER, COVER_D).bevel(0.2f);
        coverU.box(-6.2f, 0, -6.7f, 12, 1, 13).inflate(0.1f).color(GOLD).flat();
        coverL.box(-6.2f, -1, -6.7f, 12, 1, 13).inflate(0.1f).color(GOLD).flat();
        // front cover with face: gold clasp, glowing eyes, fanged edge
        Part face = body.add("face").at(0, 0, 6);
        face.box(-5.5f, -7, 0, 11, 14, 2).color(COVER).grad(COVER, COVER_D).bevel(0.25f)
            .glow(Face.FRONT, 2, 4, 2, 2, EYE).glow(Face.FRONT, 7, 4, 2, 2, EYE)
            .paint(Face.FRONT, 1, 3, 4, 1, 0x1a0408).paint(Face.FRONT, 6, 3, 4, 1, 0x1a0408)
            .paint(Face.FRONT, 3, 9, 5, 1, 0x1a0408).paint(Face.FRONT, 0, 0, 11, 1, GOLD).paint(Face.FRONT, 0, 13, 11, 1, GOLD)
            .px(Face.FRONT, 3, 10, PAGE).px(Face.FRONT, 5, 10, PAGE).px(Face.FRONT, 7, 10, PAGE);
        face.box(-1, -1, 2, 2, 2, 1).color(GOLD).flat();      // clasp
        // blades bristling from the pages: pivots at the book, blades point outward in several directions
        float[][] bl = {{0, 90, 4, 2}, {0, -90, 4, -2}, {-90, 0, 0, 7}, {90, 0, 0, -7}, {-45, 45, 3, 5}, {-45, -45, -3, 5}, {45, 45, 3, -5}, {45, -45, -3, -5}, {0, 0, 5, 0}, {-30, 180, 2, 3}};
        for (int i = 0; i < bl.length; i++) {
            Part b = body.add("blade" + i).at(0, 0, 0).rot(bl[i][0], bl[i][1], 0);
            b.box(-0.8f, 4.5f, -0.3f, 1, 2, 1).color(0x4a3a2a).flat();
            b.box(-1, 6.5f, -0.3f, 2, 6, 1).color(STEEL).grad(0xffffff, 0x8a92a0).flat();
            b.box(-0.5f, 12.5f, -0.3f, 1, 2, 1).color(0xffffff).flat();
        }
        m.bob(1.5f, 0.08f);
        m.look("body", 50, 30);
        m.clip("idle", 40).loop()
            .rot("body", 0, 0, 0, 3).rot("body", 20, 0, 0, -3).rot("body", 40, 0, 0, 3)
            .rot("coverU", 0, 0, 0, 0).rot("coverU", 20, -8, 0, 0).rot("coverU", 40, 0, 0, 0)
            .rot("coverL", 0, 0, 0, 0).rot("coverL", 20, 8, 0, 0).rot("coverL", 40, 0, 0, 0);
        m.clip("walk", 20).loop()
            .rot("body", 0, 6, 0, 4).rot("body", 10, 6, 0, -4).rot("body", 20, 6, 0, 4);
        // MULTI-STAB: rear back, blades flare, then rapid forward jabs (each 3 ticks, up to 10) with the whole book lunging
        var ms = m.clip("multi_stab", 42)
            .rot("body", 0, 0, 0, 0).rot("body", 9, -30, 0, 0, Ease.OUT_CUBIC)
            .pos("body", 0, 0, 0, 0).pos("body", 9, 0, 0, -4, Ease.OUT_CUBIC)
            .scl("body", 0, 1, 1, 1).scl("body", 9, 1.1f, 1.1f, 1.1f)
            .rot("coverU", 0, 0, 0, 0).rot("coverU", 9, -35, 0, 0).rot("coverL", 0, 0, 0, 0).rot("coverL", 9, 35, 0, 0);
        for (int i = 0; i < 10; i++) {
            int t = 10 + i * 3;
            ms.rot("body", t, 18, 0, 0, Ease.IN_CUBIC).pos("body", t, 0, 0, 3, Ease.IN_CUBIC)
              .rot("body", t + 2, -8, 0, 0).pos("body", t + 2, 0, 0, -1);
        }
        ms.rot("body", 42, 0, 0, 0).pos("body", 42, 0, 0, 0).scl("body", 42, 1, 1, 1).rot("coverU", 42, 0, 0, 0).rot("coverL", 42, 0, 0, 0);
        // SINGLE STAB: slow huge wind-up, giant lunge, long recovery
        m.clip("single_stab", 36)
            .rot("body", 0, 0, 0, 0).rot("body", 12, -40, 0, 0, Ease.OUT_CUBIC).rot("body", 16, 40, 0, 0, Ease.IN_CUBIC).rot("body", 26, 30, 0, 0).rot("body", 36, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 12, 3, -6, 0, Ease.OUT_CUBIC).pos("body", 16, -2, 8, 0, Ease.IN_CUBIC).pos("body", 26, -2, 7, 0).pos("body", 36, 0, 0, 0)
            .scl("blade4", 0, 1, 1, 1).scl("blade4", 12, 1.2f, 1.2f, 1.2f).scl("blade4", 16, 1.8f, 1.8f, 1.8f).scl("blade4", 36, 1, 1, 1)
            .scl("blade5", 0, 1, 1, 1).scl("blade5", 12, 1.2f, 1.2f, 1.2f).scl("blade5", 16, 1.8f, 1.8f, 1.8f).scl("blade5", 36, 1, 1, 1)
            .rot("coverU", 0, 0, 0, 0).rot("coverU", 12, -50, 0, 0).rot("coverU", 16, -5, 0, 0).rot("coverU", 36, 0, 0, 0)
            .rot("coverL", 0, 0, 0, 0).rot("coverL", 12, 50, 0, 0).rot("coverL", 16, 5, 0, 0).rot("coverL", 36, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -22, 0, 12).rot("body", 10, 0, 0, 0)
            .rot("coverU", 0, 0, 0, 0).rot("coverU", 3, -40, 0, 0).rot("coverU", 10, 0, 0, 0)
            .rot("coverL", 0, 0, 0, 0).rot("coverL", 3, 40, 0, 0).rot("coverL", 10, 0, 0, 0);
        m.clip("death", 36)
            .pos("body", 0, 0, 0, 0).pos("body", 24, 0, -9, 0, Ease.IN_QUAD).pos("body", 36, 0, -11, 0, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 24, 80, 20, 0, Ease.IN_QUAD).rot("body", 36, 90, 20, 0)
            .rot("coverU", 0, 0, 0, 0).rot("coverU", 24, -60, 0, 0).rot("coverL", 0, 0, 0, 0).rot("coverL", 24, 60, 0, 0);
        return m;
    }

    static String next(SpireMob s) {
        if (s.getTurn() <= 1 && s.lastMove().isEmpty()) return "multi_stab";
        boolean m2 = s.lastMove().equals("multi_stab") && s.moveAgo(1).equals("multi_stab");
        if (s.lastMove().equals("single_stab")) return "multi_stab";
        if (m2) return "single_stab";
        return s.rand().nextInt(100) < 15 ? "single_stab" : "multi_stab";
    }

    static Move.Action stab(int i) {
        return (s, t) -> {
            if (i >= s.count("stabs")) return;
            Fx.sound(s, SoundEvents.PLAYER_ATTACK_STRONG, 0.7f, 1.6f);
            Fx.meleeNow(s, 6, 3.4, 80, v -> Fx.curse(v, Fx.Curse.WOUND));
        };
    }

    public static MobDef def() {
        MobDef d = MobDef.of("book_of_stabbing", "Book of Stabbing", "ブック・オブ・スタビング")
            .game(1, 2).size(1.3f, 2.2f).hp(162).speed(0.22f).style(Style.GHOST).egg(0x7a1c24, 0xd8a63a).elite().flying()
            .model(BookOfStabbing::model)
            .spawn(1, 1, 1, MobDef.Habitat.ANY)
            .pattern(BookOfStabbing::next);
        Move multi = Move.of("multi_stab", 42).intent(Intent.ATTACK, 6, 2).reach(3.0f)
            .sound(9, SoundEvents.PHANTOM_BITE, 1f, 0.6f)
            .at(1, (s, t) -> { if (s.count("stabs") == 0) s.setCount("stabs", 2); else s.setCount("stabs", Math.min(10, s.count("stabs") + 1)); });
        for (int i = 0; i < 10; i++) multi.at(10 + i * 3 + 1, stab(i));
        return d.move(multi)
            .move(Move.of("single_stab", 36).intent(Intent.ATTACK, 21, 1).reach(3.6f)
                .sound(10, SoundEvents.PHANTOM_BITE, 1.2f, 0.5f)
                .at(16, (s, t) -> { Fx.sound(s, SoundEvents.TRIDENT_THROW, 1.3f, 0.6f); Fx.meleeNow(s, 21, 4.4, 60, v -> Fx.curse(v, Fx.Curse.WOUND)); Fx.particles(s, ParticleTypes.CRIT, 15, 0.6); }));
    }
}
