package com.spiremod.mobs.heroes;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

import java.util.List;
import java.util.function.Consumer;

/**
 * The Downfall (StS1 fan expansion) villains as playable-hero creatures — NOT the boss versions (those are the regular boss mobs).
 * HP from each character class; cards = every parsable card of that villain in the Downfall source (tools/gen_downfall.py -> DownfallData).
 * ids: df_hexaghost, df_guardian, df_slimebound, df_champ, df_automaton, df_collector, df_gremlins, df_snecko, df_awakened_one.
 */
public final class Villains {
    private Villains() {}

    record V(String key, String en, String ja, int hp, int skin, int cloth, int trim, String style, Consumer<Part[]> props, float scale) {}

    static ModelSpec model(V v) {
        ModelSpec m = Heroes.humanoid("df_" + v.key, v.skin, v.cloth, v.cloth & 0x6f6f6f, v.trim, v.props);
        if (v.key.equals("hexaghost"))   // six ghostflames circle the body
            m.proc((rig, ctx) -> { for (int i = 0; i < 6; i++) { double a = ctx.age * 0.1 + i * Math.PI / 3; rig.pos("flame" + i, (float) Math.cos(a) * 11, 6f + (float) Math.sin(a * 2) * 2, (float) Math.sin(a) * 11); } });
        if (v.scale != 1f) m.scale(v.scale);
        return m;
    }

    static MobDef def(V v) {
        CardMoves.Fxs st = CardMoves.basic("DF", v.key, "Strike"), df = CardMoves.basic("DF", v.key, "Defend");
        int sd = st != null && st.dmg > 0 ? st.dmg : 6, db = df != null && df.block > 0 ? df.block : 5;
        MobDef d = Heroes.base("df_" + (v.key.equals("awakened") ? "awakened_one" : v.key), v.en, v.ja, 1, v.hp, 0.26f, Style.valueOf(v.style), v.cloth, v.trim, () -> model(v))
            .move(Heroes.strike(sd)).move(Heroes.defend(db));
        d.size(0.6f * v.scale, 1.8f * v.scale);
        return CardMoves.finish(d, "DF", v.key);
    }

    private static void box(Part p, float x, float y, float z, int dx, int dy, int dz, int c) { p.box(x, y, z, dx, dy, dz).color(c).bevel(0.15f); }
    private static void glow(Part p, float x, float y, float z, int dx, int dy, int dz, int c) { p.box(x, y, z, dx, dy, dz).color(c).glow(Face.FRONT, 0, 0, dx, dy, c); }

    static final List<V> ALL = List.of(
        new V("hexaghost", "Hexaghost (Downfall)", "ヘキサゴースト(ダウンフォール)", 66, 0xb8c8d8, 0x2a2438, 0x60ffb0, "GHOST", p -> {
            box(p[1], -4.5f, 5, -4.5f, 9, 4, 9, 0x1a1428);                                    // hood
            glow(p[1], -2, 3, 4, 1, 1, 1, 0x60ffb0); glow(p[1], 1, 3, 4, 1, 1, 1, 0x60ffb0);
            for (int i = 0; i < 6; i++) glow(p[0].add("flame" + i).at(0, 14, 0), -1, -1, -1, 2, 3, 2, 0x70ff90);
        }, 1f),
        new V("guardian", "The Guardian (Downfall)", "ガーディアン(ダウンフォール)", 80, 0xc8b878, 0x8a7a50, 0xe8c040, "METAL", p -> {
            box(p[0], -6.5f, 8, -3, 13, 4, 6, 0xc8a840);                                      // heavy pauldrons
            box(p[1], -4.5f, 6, -4.5f, 9, 3, 9, 0xe8c040); glow(p[1], -2, 3, 4, 4, 1, 1, 0xffd060);
            box(p[2], -2, -14, -2, 4, 4, 4, 0x5a4a2c);                                        // stone fists
            box(p[3], -2, -14, -2, 4, 4, 4, 0x5a4a2c);
            box(p[0], -2, 3, 2.5f, 4, 4, 1, 0xe8c040);                                        // chest core
        }, 1.1f),
        new V("slimebound", "Slime Boss (Downfall)", "スライムボス(ダウンフォール)", 65, 0x8ad060, 0x6ab048, 0xc8f080, "SLIME", p -> {
            box(p[1], -3, 8, -3, 6, 4, 6, 0x9ae070);                                          // slime blob crown
            glow(p[1], -2, 3, 4, 1, 2, 1, 0x203010); glow(p[1], 1, 3, 4, 1, 2, 1, 0x203010);
            box(p[0], -5, -2, -3, 10, 5, 6, 0x6ab048);                                        // dripping hem
            box(p[0], -3, 9, -3, 3, 3, 3, 0x9ae070);
        }, 1f),
        new V("champ", "The Champ (Downfall)", "チャンプ(ダウンフォール)", 80, 0xd09070, 0xc03030, 0xf0c030, "BRUTE", p -> {
            box(p[0], -6, 9, -3, 12, 3, 6, 0xd09070);                                         // bare shoulders
            box(p[0], -4.5f, 0, -3, 9, 3, 6, 0xf0c030);                                       // championship belt
            glow(p[0], -1.5f, 0.5f, 3, 3, 2, 1, 0xfff0a0);
            box(p[1], -4.5f, 6, -4.5f, 9, 2, 9, 0xc03030);                                    // headband
            box(p[2], -2, -12, -2, 4, 3, 4, 0xf0f0f0); box(p[3], -2, -12, -2, 4, 3, 4, 0xf0f0f0);   // wrist wraps
        }, 1.1f),
        new V("automaton", "Bronze Automaton (Downfall)", "ブロンズオートマトン(ダウンフォール)", 70, 0xc8905a, 0xb87333, 0x60e0ff, "METAL", p -> {
            box(p[1], -4, 8, -4, 8, 3, 8, 0xd8a050); glow(p[1], -2.5f, 3, 4, 5, 2, 1, 0x60e0ff);
            glow(p[0].add("core").at(0, 6, 3), -2, -2, 0, 4, 4, 1, 0x60e0ff);
            box(p[0], -6, 8, -2, 3, 3, 4, 0x8a5a2a); box(p[0], 3, 8, -2, 3, 3, 4, 0x8a5a2a);
            box(p[0], -2, 12, -2, 4, 4, 4, 0xd8a050);                                         // gear
        }, 1f),
        new V("collector", "The Collector (Downfall)", "コレクター(ダウンフォール)", 65, 0xe8d0a0, 0x7a5a2a, 0xe8c040, "WITCH", p -> {
            box(p[1], -6, 7, -6, 12, 2, 12, 0x5a3a18); box(p[1], -3.5f, 8, -3.5f, 7, 6, 7, 0x5a3a18);   // wide hat
            glow(p[1], -2, 3, 4, 1, 1, 1, 0xff9020); glow(p[1], 1, 3, 4, 1, 1, 1, 0xff9020);
            box(p[2], -0.5f, -13, 1, 1, 12, 1, 0x4a3a1c); glow(p[2], -1.5f, -16, 0.5f, 3, 3, 3, 0xff8020);   // torch staff
            box(p[0], -5, -4, -3, 10, 6, 6, 0x5a3a18);
        }, 1f),
        new V("gremlin", "The Gremlins (Downfall)", "グレムリンズ(ダウンフォール)", 16, 0x8a9a7a, 0x6a5a4a, 0xd0c060, "SILENT", p -> {
            box(p[1], -7, 3, -1, 3, 3, 1, 0x8a9a7a); box(p[1], 4, 3, -1, 3, 3, 1, 0x8a9a7a);   // big pointed ears
            glow(p[1], -2, 3, 4, 1, 2, 1, 0xff4030); glow(p[1], 1, 3, 4, 1, 2, 1, 0xff4030);
            box(p[2], -1, -14, 1, 1, 6, 1, 0xc8c8d0);                                         // shiv
        }, 0.7f),
        new V("snecko", "The Snecko (Downfall)", "スネッコ(ダウンフォール)", 85, 0xa8d070, 0x6a3a8a, 0xf0d040, "SILENT", p -> {
            glow(p[1], -3.5f, 3, 4, 3, 3, 1, 0xf0f040); glow(p[1], 0.5f, 3, 4, 3, 3, 1, 0xf0f040);   // huge staring eyes
            box(p[1], -2, 8, -2, 4, 2, 4, 0xc040a0);                                          // crest
            for (int i = 0; i < 4; i++) box(p[0], -2 + 0, -2 - i * 3, -5 - i * 2, 4 - (i > 1 ? 1 : 0) , 4, 4, i % 2 == 0 ? 0x6a3a8a : 0xa8d070);   // tail
        }, 1f),
        new V("awakened", "The Awakened One (Downfall)", "覚醒者(ダウンフォール)", 70, 0xb0a0c8, 0x2a1a3a, 0xc040ff, "WITCH", p -> {
            box(p[1], -4.5f, 4, -4.5f, 9, 6, 9, 0x1a0e28); glow(p[1], -2, 3, 4, 1, 1, 1, 0xff3030); glow(p[1], 1, 3, 4, 1, 1, 1, 0xff3030);
            for (int s = -1; s <= 1; s += 2) { Part w = p[0].add(s < 0 ? "wingR" : "wingL").at(s * 3, 9, 3); box(w, s < 0 ? -12 : 0, -2, 0, 12, 12, 1, 0x3a2050); }
            box(p[0], -4.5f, -4, -3, 9, 6, 6, 0x1a0e28);
        }, 1.05f)
    );

    // entry points used by Group
    public static List<MobDef> defs() { return ALL.stream().map(Villains::def).toList(); }
}
