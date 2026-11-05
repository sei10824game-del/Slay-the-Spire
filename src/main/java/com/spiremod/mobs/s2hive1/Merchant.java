package com.spiremod.mobs.s2hive1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 The Merchant??? (165 HP, the fake Merchant fought via the event): Swipe (13), Spew Coins (2x8), Throw Relic (9 + Frail 1), Enrage (+2 Str).
 * Opens with Swipe; Swipe/Spew/Enrage lead into a different random move; Enrage at most once per 3 turns; Throw Relic -> Swipe or Spew.
 */
public final class Merchant {
    private Merchant() {}
    static final int CLOAK = 0x6a3a58, CLOAK_D = 0x3c2034, SKIN = 0xc8a07a, GOLD = 0xe8c030, PACK = 0x8a6a3a, RUG = 0xa02838;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_merchant_fake");
        Kit.hum(m, 7, 4, 2.5f, 9, 9, 6, 6, 6, 8, 3, CLOAK, CLOAK_D, SKIN, SKIN);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.5f, -1, -3.5f, 9, 11, 7).color(CLOAK).grad(0x8a4a74, CLOAK_D).bevel(0.1f).noise(0.1f).inflate(0.3f);
        torso.box(-4, 2, 3.2f, 8, 1, 1).color(GOLD).flat();                                                 // sash
        Part skirt = body.add("skirt").at(0, 1, 0);
        skirt.box(-5.5f, -8, -3.8f, 11, 7, 8).color(CLOAK_D).grad(CLOAK, CLOAK_D).noise(0.1f);
        // hood + big nose, shifty eyes
        head.box(-3.8f, 0, -3.8f, 8, 7, 8).color(CLOAK).grad(0x8a4a74, CLOAK_D).bevel(0.1f).inflate(0.1f).noise(0.1f);
        head.box(-3, 1, 3.2f, 6, 4, 1).color(SKIN).bevel(0.12f).glow(Face.FRONT, 0, 1, 1, 1, 0xffffff).glow(Face.FRONT, 5, 1, 1, 1, 0xffffff);
        head.box(-1, 1, 4, 2, 3, 2).color(0xb88862).bevel(0.1f);                                           // nose
        head.box(-3, 0, 3.4f, 6, 1, 1).color(0x302020).flat();                                              // moustache
        // giant backpack stuffed with knock-off relics
        Part pack = torso.add("pack").at(0, 0, -4);
        pack.box(-6, -2, -9, 12, 14, 9).color(PACK).grad(0xa88048, 0x6a4a28).bevel(0.15f).noise(0.12f);
        pack.box(-5, 11, -8, 10, 3, 7).color(0x7a5a30).bevel(0.15f);
        pack.box(-3, 12, -7, 3, 5, 3).color(GOLD).flat().bevel(0.1f);                                       // sticking-out relic
        pack.box(1, 12, -6, 3, 7, 2).color(0xc0c8d8).flat();
        pack.box(-6.4f, 3, -9.4f, 13, 1, 10).color(RUG).flat().inflate(0.1f);
        Part rug = pack.add("rug").at(0, 9, -5);                                                              // rolled-up rug on top
        rug.box(-6, 0, -2, 12, 4, 4).color(RUG).bevel(0.2f).paint(Face.FRONT, 0, 1, 12, 1, GOLD);
        Part pouch = body.add("pouch").at(3.5f, 1, 2.5f);
        pouch.box(-1.5f, -4, -1.5f, 3, 4, 3).color(0x4a3a20).bevel(0.15f).glow(Face.TOP, 0, 0, 3, 3, GOLD);
        // SWIPE: sweeping backhand
        Clip s = m.clip("swipe", 26);
        Kit.R(s, "armR", 6, -60, 70, 60, 0, 11, -80, -60, 20, 1, 20, -60, -60, 20, 0, 26, 0, 0, 0, 0);
        Kit.R(s, "torso", 6, 4, 35, 0, 0, 11, 6, -30, 0, 1, 26, 0, 0, 0, 0);
        Kit.P(s, "body", 6, 0, 0, -1.5f, 0, 11, 0, 0, 2.5f, 1, 26, 0, 0, 0, 0);
        // SPEW COINS: lean back, head thrown forward, jaw wide, rapid stream (shaking)
        Clip c = m.clip("spew_coins", 40);
        Kit.R(c, "torso", 8, -18, 0, 0, 0, 14, 16, 0, 0, 1, 34, 16, 0, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(c, "head", 8, -25, 0, 0, 0, 14, 30, 0, 0, 1, 34, 30, 0, 0, 0, 40, 0, 0, 0, 0);
        Kit.R(c, "armR", 8, 10, 0, 50, 0, 14, -30, 0, 40, 0, 34, -30, 0, 40, 0, 40, 0, 0, 0, 0);
        Kit.R(c, "armL", 8, 10, 0, -50, 0, 14, -30, 0, -40, 0, 34, -30, 0, -40, 0, 40, 0, 0, 0, 0);
        for (int i = 0; i < 8; i++) Kit.R(c, "pack", 14 + i * 2.5f, 0, 0, (i % 2 == 0) ? 4 : -4, 5);
        Kit.S(c, "torso", 14, 1, 1, 1, 0, 24, 1.1f, 1.05f, 1.1f, 0, 34, 1.1f, 1.05f, 1.1f, 0, 40, 1, 1, 1, 0);
        // THROW RELIC: reach into pack, overhand lob
        Clip t = m.clip("throw_relic", 32);
        Kit.R(t, "armR", 6, 20, 0, 20, 0, 10, 50, 0, 20, 0, 14, -170, 0, 15, 0, 17, -95, 0, 5, 1, 32, 0, 0, 0, 0);
        Kit.R(t, "torso", 6, 12, 0, 0, 0, 14, -22, 0, 0, 0, 17, 22, 0, 0, 1, 32, 0, 0, 0, 0);
        // ENRAGE: stamps, hat pulls down, swells
        Clip e = m.clip("enrage", 40);
        Kit.R(e, "torso", 8, 14, 0, 0, 0, 14, -12, 0, 0, 2, 40, 0, 0, 0, 0);
        Kit.R(e, "armR", 8, 0, 0, 20, 0, 16, -50, 0, 70, 2, 30, -50, 0, 65, 0, 40, 0, 0, 0, 0);
        Kit.R(e, "armL", 8, 0, 0, -20, 0, 16, -50, 0, -70, 2, 30, -50, 0, -65, 0, 40, 0, 0, 0, 0);
        Kit.R(e, "legR", 10, 0, 0, 0, 0, 14, -30, 0, 0, 0, 17, 0, 0, 0, 1, 40, 0, 0, 0, 0);
        Kit.S(e, "torso", 14, 1, 1, 1, 0, 18, 1.16f, 1.12f, 1.16f, 2, 40, 1, 1, 1, 0);
        Kit.R(e, "head", 8, 15, 0, 0, 0, 16, -15, 0, 0, 2, 22, 0, 0, 3, 5, 24, 0, 0, -3, 5, 26, 0, 0, 3, 5, 40, 0, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        ProjKind.orb("s2_merchant_relic", 0xf0d060, 4).trail(ParticleTypes.GLOW);
        ProjKind.orb("s2_merchant_coin", 0xffd830, 2).trail(ParticleTypes.CRIT);
        MobDef d = MobDef.of("s2_merchant_fake", "The Merchant???", "商人（？）")
            .game(2, 2).size(0.7f, 1.7f).hp(165).speed(0.25f).style(Style.PILLAGER).egg(CLOAK, GOLD)
            .model(Merchant::model).spawn(0, 1, 1, MobDef.Habitat.NONE)
            .pattern(s -> {
                int t = s.getTurn();
                String last = s.lastMove();
                if (t == 0) return "swipe";
                java.util.List<String> o = new java.util.ArrayList<>();
                if (last.equals("throw_relic")) { o.add("swipe"); o.add("spew_coins"); }
                else {
                    for (String id : new String[]{"swipe", "spew_coins", "enrage", "throw_relic"}) {
                        if (id.equals(last)) continue;
                        if (id.equals("enrage") && t - s.count("enr") < 3) continue;
                        o.add(id);
                    }
                }
                String r = o.get(s.rand().nextInt(o.size()));
                if (r.equals("enrage")) s.setCount("enr", t);
                return r;
            });
        d.move(Move.of("swipe", 26).intent(Intent.ATTACK, 13, 1).reach(2.4f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.9f).at(12, Fx.melee(13, 2.8, 110)))
            .move(Move.of("throw_relic", 32).intent(Intent.ATTACK_DEBUFF, 9, 1).ranged(10)
                .sound(16, SoundEvents.ITEM_PICKUP, 1f, 0.7f)
                .at(17, Fx.shoot("s2_merchant_relic", 9, 1.3f, 0, 1.4, 0.5, v -> Fx.frail(v, 5))))
            .move(Move.of("enrage", 40).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.VILLAGER_NO, 1.2f, 0.6f)
                .at(16, (s, t) -> { s.addStrength(2); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 8, 0.5); }));
        Move sp = Move.of("spew_coins", 40).intent(Intent.ATTACK, 2, 8).ranged(9);
        for (int i = 0; i < 8; i++) sp.at(14 + i * 3, Fx.shoot("s2_merchant_coin", 2, 1.5f, 6, 1.5, 0.5, null)).sound(14 + i * 3, SoundEvents.AMETHYST_BLOCK_CHIME, 0.7f, 1.8f);
        return d.move(sp);
    }
}
