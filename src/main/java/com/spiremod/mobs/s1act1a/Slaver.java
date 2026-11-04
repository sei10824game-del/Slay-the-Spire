package com.spiremod.mobs.s1act1a;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * StS1 Act 1 Slavers, 46-50 HP.
 * Blue: Stab 12 / Rake 7 + Weak, 60/40, no move 3x in a row.
 * Red: Stab 13 first; until Entangle: 25% Entangle else Scrape(8+Vuln), Scrape, Stab cycle; after: Stab 55 / Scrape 45, no 3x. Entangle once.
 */
public final class Slaver {
    private Slaver() {}

    static final int SKIN = 0xb98a62, SKIN_D = 0x8a6244, IRON = 0x3a3a40, STEEL = 0xb8c0c8, PANTS = 0x4a3a30;

    public static ModelSpec model(boolean red) {
        int cloth = red ? 0xa83a2e : 0x3a6aa8, clothD = red ? 0x5e1f1a : 0x1f3a62, clothL = red ? 0xd0584a : 0x5a8ad0;
        ModelSpec m = new ModelSpec(red ? "red_slaver" : "blue_slaver");
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 9, 0);
            leg.box(-2, -9, -2, 4, 9, 4).color(PANTS).grad(0x5a483c, 0x3a2c24).noise(0.08f);
            leg.box(-2.3f, -9, -2.6f, 4, 3, 5).color(0x2a2018).bevel(0.15f).inflate(0.2f);
        }
        Part body = m.add("body").at(0, 9, 0);
        body.box(-4.5f, 0, -2.5f, 9, 3, 5).color(PANTS).grad(0x5a483c, 0x3a2c24);
        body.box(-4.7f, 1, -2.7f, 9, 2, 5).color(IRON).flat().inflate(0.2f);                       // belt
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-5, 0, -3, 10, 9, 6).color(SKIN).grad(0xcea074, SKIN_D).noise(0.06f).bevel(0.15f);   // bare chest
        torso.box(-5.2f, 4, -3.2f, 3, 5, 6).color(cloth).grad(clothL, clothD).inflate(0.2f);        // vest
        torso.box(2.2f, 4, -3.2f, 3, 5, 6).color(cloth).grad(clothL, clothD).inflate(0.2f);
        torso.box(-0.5f, 0, 2.6f, 1, 1, 1).color(IRON);
        Part head = torso.add("head").at(0, 9, 0);
        head.box(-3, 0, -3, 6, 6, 6).color(SKIN).noise(0.05f).bevel(0.1f)
            .glow(Face.FRONT, 1, 3, 1, 1, red ? 0xff5030 : 0xe0f0ff).glow(Face.FRONT, 4, 3, 1, 1, red ? 0xff5030 : 0xe0f0ff);
        head.box(-3.4f, 4, -3.4f, 7, 3, 7).color(cloth).grad(clothL, clothD).inflate(0.1f);         // headwrap
        head.box(-3.4f, 0, -3.4f, 7, 2, 1).color(red ? 0x3a1410 : 0x1a2a44).flat();                 // face scarf
        head.box(-1.5f, 1.5f, -3.5f, 3, 1, 1).color(0x1d1a18).flat();                               // scowl
        if (red) { head.box(-0.5f, 6, -3, 1, 2, 6).color(IRON).flat(); }                           // mohawk-like crest
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 6.5f, 8, 0);
            arm.box(-2, -6, -2, 4, 6, 4).color(SKIN).grad(0xcea074, SKIN_D).noise(0.06f).bevel(0.1f);
            arm.box(-2.3f, -6.3f, -2.3f, 4, 2, 4).color(IRON).flat();                                 // shackle
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1.5f, -3, -1.5f, 3, 3, 3).color(SKIN_D);
            if (s < 0) { // right hand: weapon. Blue = three-tined rake, Red = long spiked whip-mace
                Part w = hand.add("weapon").at(0, -2, 0).rot(-90, 0, 0);
                w.box(-0.5f, -2, -0.5f, 1, 12, 1).color(0x5a4028);
                if (red) { w.box(-1.5f, 10, -1.5f, 3, 3, 3).color(IRON).bevel(0.2f); w.box(-0.5f, 13, -0.5f, 1, 2, 1).color(STEEL).flat(); }
                else { w.box(-2.5f, 10, -0.5f, 5, 1, 1).color(IRON).flat();
                    for (int i = 0; i < 3; i++) w.box(-2.5f + i * 2, 11, -0.5f, 1, 3, 1).color(STEEL).flat(); }
            } else if (red) { // left hand: net / chain
                Part ch = hand.add("chain").at(0, -3, 0);
                ch.box(-0.5f, -6, -0.5f, 1, 6, 1).color(IRON).flat();
            }
        }
        m.look("head", 45, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 36f, 22f);
        m.clip("idle", 50).loop()
            .rot("torso", 0, 1, 0, 0).rot("torso", 25, -1, 0, 0).rot("torso", 50, 1, 0, 0)
            .rot("armR", 0, 6, 0, 8).rot("armR", 25, 10, 0, 10).rot("armR", 50, 6, 0, 8)
            .rot("armL", 0, 6, 0, -8).rot("armL", 25, 10, 0, -10).rot("armL", 50, 6, 0, -8)
            .rot("head", 0, 0, 0, 0).rot("head", 18, 0, 6, 2).rot("head", 38, 0, -6, -2).rot("head", 50, 0, 0, 0);
        // STAB: raise weapon behind head, lunge forward with full body
        m.clip("stab", 28)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -12, -30, 0).rot("torso", 14, 22, 28, 0, Ease.IN_CUBIC).rot("torso", 28, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -3).pos("body", 14, 0, -1.5f, 6, Ease.IN_CUBIC).pos("body", 28, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -150, 15, 20).rot("armR", 14, -95, -10, 0, Ease.IN_CUBIC).rot("armR", 20, -95, -10, 0).rot("armR", 28, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -40, 0, -25).rot("armL", 14, 30, 0, -10).rot("armL", 28, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 10, 25, 0).rot("head", 14, 8, -25, 0).rot("head", 28, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 10, 20, 0, 0).rot("legR", 14, -45, 0, 0).rot("legR", 28, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 10, -10, 0, 0).rot("legL", 14, 25, 0, 0).rot("legL", 28, 0, 0, 0);
        // RAKE / SCRAPE: wide sideways sweep dragging the weapon low along the ground
        m.clip(red ? "scrape" : "rake", 30)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 20, -45, 0).rot("torso", 15, 25, 50, 0, Ease.IN_CUBIC).rot("torso", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -3, -1).pos("body", 15, 0, -2, 4, Ease.IN_CUBIC).pos("body", 30, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -120, -35, 30).rot("armR", 15, -50, 30, -15, Ease.IN_CUBIC).rot("armR", 22, -45, 25, -10).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 30, 0, 30).rot("armL", 15, -20, 0, -30).rot("armL", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 15, 30, 0).rot("head", 15, 15, -30, 0).rot("head", 30, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 15, -30, 0, 0).rot("legR", 30, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 15, 20, 0, 0).rot("legL", 30, 0, 0, 0);
        if (red) // ENTANGLE: whirl the chain overhead then hurl the net forward
            m.clip("entangle", 40)
                .rot("torso", 0, 0, 0, 0).rot("torso", 10, -8, 0, 0).rot("torso", 26, 22, 0, 0, Ease.IN_CUBIC).rot("torso", 40, 0, 0, 0)
                .rot("armL", 0, 0, 0, 0).rot("armL", 6, -170, 0, -20).rot("armL", 12, -170, 40, -20).rot("armL", 18, -170, -40, -20).rot("armL", 24, -170, 40, -20).rot("armL", 28, -60, 0, 0, Ease.IN_CUBIC).rot("armL", 40, 0, 0, 0)
                .rot("armR", 0, 0, 0, 0).rot("armR", 8, -120, 0, 25).rot("armR", 28, -80, 0, 0).rot("armR", 40, 0, 0, 0)
                .rot("chain", 0, 0, 0, 0).rot("chain", 12, 0, 0, 40).rot("chain", 18, 0, 0, -40).rot("chain", 24, 0, 0, 40).rot("chain", 40, 0, 0, 0)
                .rot("head", 0, 0, 0, 0).rot("head", 10, -25, 0, 0).rot("head", 28, 10, 0, 0).rot("head", 40, 0, 0, 0)
                .pos("body", 0, 0, 0, 0).pos("body", 10, 0, 0, -2).pos("body", 28, 0, -1, 3).pos("body", 40, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 16, 0, 5).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -22, 0, 8).rot("head", 10, 0, 0, 0);
        m.clip("death", 32)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 20, 0, 5).rot("torso", 32, 10, 0, 5)
            .pos("body", 0, 0, 0, 0).pos("body", 16, 0, -4, -1, Ease.IN_QUAD).pos("body", 32, 0, -9, -3, Ease.OUT_BOUNCE)
            .rot("body", 0, 0, 0, 0).rot("body", 32, -30, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -100, 0, 40).rot("armR", 32, 30, 0, 60)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -70, 0, -40).rot("armL", 32, 20, 0, -60)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -25, 0, 0).rot("head", 32, 35, 15, 10)
            .rot("legR", 0, 0, 0, 0).rot("legR", 20, -70, 0, 10).rot("legR", 32, -85, 0, 10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 20, -55, 0, -10).rot("legL", 32, -80, 0, -10);
        return m;
    }

    static MobDef base(boolean red) {
        String id = red ? "red_slaver" : "blue_slaver";
        MobDef d = MobDef.of(id, red ? "Red Slaver" : "Blue Slaver", red ? "レッドスレイバー" : "ブルースレイバー")
            .game(1, 1).size(0.7f, 1.8f).hp(48).speed(0.26f).style(Style.PILLAGER).egg(red ? 0xa83a2e : 0x3a6aa8, 0xb98a62)
            .model(() -> model(red))
            .spawn(red ? 5 : 6, 1, 1, red ? MobDef.Habitat.PLAINS : MobDef.Habitat.FOREST)
            .move(Move.of("stab", 28).intent(Intent.ATTACK, red ? 13 : 12, 1).reach(2.6f)
                .sound(10, SoundEvents.PLAYER_ATTACK_SWEEP, 0.9f, 0.8f)
                .at(14, Fx.melee(red ? 13 : 12, 3.0, 70)));
        if (!red) {
            d.pattern(Pattern.random().add("stab", 60).add("rake", 40).maxRepeat("stab", 2).maxRepeat("rake", 2))
             .move(Move.of("rake", 30).intent(Intent.ATTACK_DEBUFF, 7, 1).reach(2.6f)
                .sound(10, SoundEvents.PLAYER_ATTACK_SWEEP, 0.9f, 0.6f)
                .at(15, Fx.melee(7, 3.0, 110, v -> Fx.weak(v, 6))));
        } else {
            d.pattern(s -> {
                String last = s.lastMove();
                if (last.isEmpty()) return "stab";
                boolean ent = s.count("entangled") > 0;
                if (!ent) {
                    if (s.rand().nextInt(100) < 25) return "entangle";
                    int c = s.count("cyc"); s.setCount("cyc", c + 1);
                    return c % 3 == 2 ? "stab" : "scrape";
                }
                String pick = s.rand().nextInt(100) < 55 ? "stab" : "scrape";
                if (s.repeatCount(pick) >= 2) pick = pick.equals("stab") ? "scrape" : "stab";
                return pick;
            })
            .move(Move.of("scrape", 30).intent(Intent.ATTACK_DEBUFF, 8, 1).reach(2.6f)
                .sound(10, SoundEvents.CHAIN_HIT, 1f, 0.7f)
                .at(15, Fx.melee(8, 3.0, 110, v -> Fx.vulnerable(v, 6))))
            .move(Move.of("entangle", 40).intent(Intent.STRONG_DEBUFF).ranged(8)
                .sound(10, SoundEvents.CHAIN_PLACE, 1f, 0.8f)
                .at(28, (s, t) -> { // Entangled: cannot attack effectively for a few seconds
                    s.inc("entangled");
                    if (t == null) return;
                    t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 4));
                    t.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 3));
                    Fx.slow(t, 5, 3); Fx.particles(s, ParticleTypes.CRIT, 15, 0.8); }));
        }
        return d;
    }
    public static ModelSpec model() { return model(false); }
    public static MobDef def() { return base(false); }
    public static final class Red {
        public static ModelSpec model() { return Slaver.model(true); }
        public static MobDef def() { return base(true); }
    }
}
