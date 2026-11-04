package com.spiremod.mobs.s1act2b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS1 Act 2 elite: Gremlin Leader. 140-148 HP. Rally! (summons 2 gremlins), Encourage (+3 Str to gremlins, 6 Block to all), Stab (6 x3).
 * 0 gremlins: Rally 75% / Stab 25%.  1: Rally 50% / Encourage 25% / Stab 25%.  2+: Encourage 66% / Stab 34%.  No move twice in a row.
 */
public final class GremlinLeader {
    private GremlinLeader() {}
    static final int SKIN = 0x7a9a52, SKIN_D = 0x4e6a34, IRON = 0x6a6a72, IRON_D = 0x3a3a42, CLOTH = 0x6a2a2a, CLOTH_D = 0x421818, WOOD = 0x5a3a20, EYE = 0xffd020;
    static final String[] KIDS = {"mad_gremlin", "sneaky_gremlin", "fat_gremlin", "shield_gremlin"};

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("gremlin_leader");
        m.scale(1.6f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 3, 7, 0);
            leg.box(-2, -7, -2, 4, 7, 4).color(SKIN_D).grad(SKIN, SKIN_D).noise(0.08f);
            leg.box(-2.4f, -7, -3, 5, 2, 6).color(0x3a2a20).bevel(0.2f);
        }
        Part body = m.add("body").at(0, 7, 0);
        body.box(-5, 0, -3, 10, 3, 6).color(CLOTH_D).grad(CLOTH, CLOTH_D);
        Part torso = body.add("torso").at(0, 3, 0).rot(10, 0, 0);
        torso.box(-5.5f, 0, -3.5f, 11, 8, 7).color(CLOTH).grad(CLOTH, CLOTH_D).bevel(0.12f);
        torso.box(-5.7f, 4, -3.7f, 11, 2, 7).inflate(0.1f).color(IRON).grad(IRON, IRON_D).bevel(0.2f);   // iron bandolier belt
        torso.box(-6, 7, -4, 12, 3, 8).color(IRON).grad(IRON, IRON_D).bevel(0.25f);                         // spiked pauldron bar
        for (int i = 0; i < 4; i++) { torso.box(-5.5f + i * 3.4f, 10, -0.5f, 1, 2, 1).color(0xb0b0b8).flat(); }
        Part head = torso.add("head").at(0, 10, 0).rot(-10, 0, 0);
        head.box(-4, 0, -4, 8, 7, 8).color(SKIN).grad(SKIN, SKIN_D).bevel(0.15f).noise(0.08f)
            .glow(Face.FRONT, 1, 3, 2, 2, EYE).glow(Face.FRONT, 5, 3, 2, 2, EYE)
            .paint(Face.FRONT, 1, 2, 3, 1, 0x1a2410).paint(Face.FRONT, 4, 2, 3, 1, 0x1a2410)
            .paint(Face.FRONT, 2, 5, 4, 2, 0x2a1a10).px(Face.FRONT, 2, 5, 0xf0eed0).px(Face.FRONT, 5, 5, 0xf0eed0);
        head.box(-4.5f, 5, -4.5f, 9, 3, 9).color(IRON).grad(IRON, IRON_D).bevel(0.2f);                     // helmet
        head.box(-0.5f, 8, -4, 1, 2, 8).color(CLOTH).flat();                                                // crest
        for (int s = -1; s <= 1; s += 2) {
            head.box(s < 0 ? -8 : 4, 0, -0.5f, 4, 2, 1).color(SKIN).flat();                                  // big ears
            Part horn = head.add(s < 0 ? "hornR" : "hornL").at(s * 4.5f, 7, 0).rot(0, 0, s * -35);
            horn.box(-1, 0, -1, 2, 4, 2).color(0xe8e0c0).flat();
            horn.box(-0.5f, 4, -0.5f, 1, 2, 1).color(0xe8e0c0).flat();
        }
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 7.5f, 8, 0);
            arm.box(-2, -6, -2, 4, 6, 4).color(SKIN).grad(SKIN, SKIN_D);
            arm.box(-2.3f, -3, -2.3f, 5, 2, 5).color(IRON).flat();
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1.7f, -3, -1.7f, 3, 3, 3).color(SKIN_D);
            if (s < 0) { // big spiked polearm / trident in the right hand
                Part spear = hand.add("spear").at(0, -1.5f, 0).rot(-90, 0, 0);
                spear.box(-0.5f, -6, -0.5f, 1, 22, 1).color(WOOD).noise(0.1f);
                spear.box(-1.5f, 16, -0.5f, 3, 1, 1).color(IRON).flat();
                spear.box(-1.5f, 17, -0.5f, 1, 3, 1).color(0xc4cad4).flat();
                spear.box(0.5f, 17, -0.5f, 1, 3, 1).color(0xc4cad4).flat();
                spear.box(-0.5f, 17, -0.5f, 1, 4, 1).color(0xe0e4ec).flat();
            }
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 34f, 20f);
        m.clip("idle", 44).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 22, -2, 0, 0).rot("torso", 44, 2, 0, 0)
            .rot("head", 0, 0, 6, 0).rot("head", 22, 0, -6, 0).rot("head", 44, 0, 6, 0)
            .rot("armL", 0, 5, 0, -6).rot("armL", 22, 9, 0, -8).rot("armL", 44, 5, 0, -6)
            .rot("spear", 0, 0, 0, 0).rot("spear", 22, -6, 0, 3).rot("spear", 44, 0, 0, 0);
        // RALLY!: plant the spear, arm high, bellow with head back, stamp twice
        m.clip("rally", 44)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, 12, 0, 0).rot("torso", 18, -22, 0, 0, Ease.OUT_BACK).rot("torso", 34, -18, 0, 0).rot("torso", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, 20, 0, -20).rot("armL", 18, -175, 0, -25, Ease.OUT_BACK).rot("armL", 34, -170, 0, -28).rot("armL", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 18, -30, 0, 0, Ease.OUT_BACK).rot("armR", 44, 0, 0, 0)
            .rot("spear", 0, 0, 0, 0).rot("spear", 18, 25, 0, 0).rot("spear", 44, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, 20, 0, 0).rot("head", 18, -45, 0, 0, Ease.OUT_BACK).rot("head", 34, -40, 0, 0).rot("head", 44, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 12, -30, 0, 0).rot("legR", 16, 0, 0, 0).rot("legR", 44, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 24, -30, 0, 0).rot("legL", 28, 0, 0, 0).rot("legL", 44, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 10, 0, -1.5f, 0).pos("body", 18, 0, 1, 0, Ease.OUT_BACK).pos("body", 44, 0, 0, 0);
        // ENCOURAGE: sweep spear overhead in a circle, pump fist, shout
        m.clip("encourage", 38)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -10, -25, 0).rot("torso", 20, -10, 25, 0).rot("torso", 38, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -140, 0, 20, Ease.OUT_CUBIC).rot("armR", 20, -140, 0, -20).rot("armR", 38, 0, 0, 0)
            .rot("spear", 0, 0, 0, 0).rot("spear", 10, 130, 0, 0).rot("spear", 38, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -60, 0, -20).rot("armL", 14, -130, 0, -20, Ease.OUT_BACK).rot("armL", 20, -60, 0, -20).rot("armL", 24, -130, 0, -20, Ease.OUT_BACK).rot("armL", 38, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, -20, 0, 0).rot("head", 24, -20, 0, 0).rot("head", 38, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, 1, 0).pos("body", 24, 0, 1, 0).pos("body", 38, 0, 0, 0);
        // STAB: three quick spear thrusts at ticks 11, 18, 25
        var st = m.clip("stab", 38)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -8, -20, 0, Ease.OUT_CUBIC).rot("torso", 38, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -70, 20, 0, Ease.OUT_CUBIC)
            .rot("spear", 0, 0, 0, 0).rot("spear", 8, 60, 0, 0);
        for (int i = 0; i < 3; i++) {
            int t = 11 + i * 7;
            st.rot("armR", t, -100, -5, 0, Ease.IN_CUBIC).rot("armR", t + 3, -60, 10, 0).rot("spear", t, 90, 0, 0, Ease.IN_CUBIC).rot("spear", t + 3, 55, 0, 0)
              .pos("body", t, 0, -0.5f, 2.5f, Ease.IN_CUBIC).pos("body", t + 3, 0, 0, 0.5f)
              .rot("torso", t, 16, 10, 0, Ease.IN_CUBIC).rot("torso", t + 3, 4, -5, 0);
        }
        st.rot("armR", 38, 0, 0, 0).rot("spear", 38, 0, 0, 0).pos("body", 38, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 18, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 10).rot("head", 10, 0, 0, 0);
        m.clip("death", 32)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -3.5f, 0, Ease.IN_QUAD).pos("body", 32, 0, -7, 3, Ease.OUT_BOUNCE)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -14, 0, 0).rot("torso", 32, 70, 0, 0, Ease.IN_QUAD)
            .rot("legR", 0, 0, 0, 0).rot("legR", 16, -70, 0, 0).rot("legR", 32, -80, 0, 8)
            .rot("legL", 0, 0, 0, 0).rot("legL", 16, -60, 0, 0).rot("legL", 32, -80, 0, -8)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -90, 0, 30).rot("armR", 32, -140, 0, 40)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -30).rot("armL", 32, -140, 0, -40)
            .rot("head", 0, 0, 0, 0).rot("head", 32, 20, 20, 10);
        return m;
    }

    static String next(SpireMob s) {
        int n = s.minions().size(), r = s.rand().nextInt(100);
        String id;
        if (n == 0) id = r < 75 ? "rally" : "stab";
        else if (n == 1) id = r < 50 ? "rally" : r < 75 ? "encourage" : "stab";
        else id = r < 66 ? "encourage" : "stab";
        if (id.equals(s.lastMove())) id = n == 0 ? (id.equals("rally") ? "stab" : "rally") : id.equals("encourage") ? "stab" : "encourage";
        return id;
    }

    public static MobDef def() {
        return MobDef.of("gremlin_leader", "Gremlin Leader", "グレムリンリーダー")
            .game(1, 2).size(1.2f, 2.4f).hp(144).speed(0.24f).style(Style.ZOMBIE).egg(0x7a9a52, 0x6a6a72).elite()
            .model(GremlinLeader::model)
            .spawn(1, 1, 1, MobDef.Habitat.ANY)
            .pattern(GremlinLeader::next)
            .move(Move.of("rally", 44).intent(Intent.SUMMON).noTarget()
                .sound(16, SoundEvents.RAVAGER_ROAR, 1.2f, 1.4f)
                .at(20, (s, t) -> {
                    for (int i = 0; i < 2; i++) { double a = s.rand().nextDouble() * Math.PI * 2; Fx.summon(s, KIDS[s.rand().nextInt(KIDS.length)], Math.cos(a) * 3, Math.sin(a) * 3); }
                    Fx.particles(s, ParticleTypes.POOF, 25, 1.2); Fx.sound(s, SoundEvents.EVOKER_PREPARE_SUMMON, 1.2f, 0.8f); }))
            .move(Move.of("encourage", 38).intent(Intent.DEFEND_BUFF).noTarget()
                .sound(12, SoundEvents.VINDICATOR_CELEBRATE, 1.4f, 0.8f)
                .at(14, (s, t) -> {
                    s.addBlock(6);
                    for (SpireMob g : s.minions()) { g.addStrength(3); g.addBlock(6); Fx.particlesAt(s, ParticleTypes.ANGRY_VILLAGER, g.position().add(0, 1, 0), 6, 0.3, 0.02); }
                    Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 10, 0.8); }))
            .move(Move.of("stab", 38).intent(Intent.ATTACK, 6, 3).reach(3.6f)
                .sound(10, SoundEvents.TRIDENT_THROW, 1f, 1.1f).sound(17, SoundEvents.TRIDENT_THROW, 1f, 1.2f).sound(24, SoundEvents.TRIDENT_THROW, 1f, 1.3f)
                .at(12, Fx.melee(6, 4.4, 50)).at(19, Fx.melee(6, 4.4, 50)).at(26, Fx.melee(6, 4.4, 50)));
    }
}
