package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth Ruby Raider: Brute Raider 30-33 HP. Alternates Beat (7) and Clap (+3 Strength), opening with Beat. */
public final class BruteRaider {
    private BruteRaider() {}

    public static ModelSpec model() {
        ModelSpec m = RaiderKit.base("s2_brute_raider", RaiderKit.CLOTH_D, 0xa86a48, false).scale(1.3f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head");
        torso.box(-4.5f, 0.5f, -3, 9, 7, 1).color(0xb87a58).grad(0xc08460, 0x90583a).noise(0.08f).inflate(0.1f)       // bare muscular chest
            .paint(Face.FRONT, 4, 0, 1, 7, 0x8a5238).paint(Face.FRONT, 1, 3, 3, 1, 0x8a5238).paint(Face.FRONT, 5, 3, 3, 1, 0x8a5238);
        torso.box(-5.5f, 6, -3.5f, 11, 2, 7).color(RaiderKit.LEATHER).bevel(0.15f);                                      // pauldron yoke
        for (int s = -1; s <= 1; s += 2) torso.box(s * 5.3f - 1, 8, -1, 2, 2, 2).color(RaiderKit.STEEL).flat();
        head.box(-3.2f, -1, -3.2f, 6.4f > 6 ? 7 : 7, 2, 7).color(0x7a4a30).flat().inflate(0.0f);                       // jaw beard shadow
        head.box(-3.5f, 1.5f, 3, 7, 2, 1).color(0x2a1810).flat();                                                          // back hair
        for (int s = -1; s <= 1; s += 2) {
            Part arm = m.byName.get(s < 0 ? "armR" : "armL");
            arm.box(-2, -4, -2, 4, 3, 4).color(0xa86a48).noise(0.08f).inflate(0.4f);                                       // big biceps
        }
        Part club = m.byName.get("handR").add("club").at(0, -2, 0).rot(100, 0, 0);
        club.box(-0.8f, -5, -0.8f, 2, 10, 2).color(RaiderKit.WOOD).noise(0.1f);
        club.box(-1.8f, 5, -1.8f, 4, 7, 4).color(0x7a5230).grad(0x8a6238, 0x4a2e18).bevel(0.15f).noise(0.1f);
        for (int i = 0; i < 4; i++) club.box(-0.5f + (i % 2) * 2 - 1, 6 + i * 1.6f, -2.6f + (i / 2) * 4.3f, 1, 1, 1).color(RaiderKit.RUBY).glow(Face.FRONT, 0, 0, 1, 1, RaiderKit.RUBY);
        // BEAT: huge overhead club smash
        Clip c = m.clip("beat", 30);
        Kit.R(c, "armR", 0, 0, 0, 0, 10, -160, 0, 10, -15, -20, -5, 0, 24, -15, 0, 0, 30, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 10, -120, 0, -25, -15, -30, 0, -10, 30, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 10, -18, 0, 0, -15, 32, 0, 0, 30, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 10, -22, 0, 0, -15, 18, 0, 0, 30, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 10, 0, 1, -2, -15, 0, -3, 5, 30, 0, 0, 0);
        // CLAP: lifts hands, roars with head thrown back, claps twice, beats chest
        c = m.clip("clap", 36);
        Kit.R(c, "armR", 0, 0, 0, 0, 8, -120, 30, 10, 12, -90, -35, 0, 15, -120, 30, 10, 19, -90, -35, 0, 36, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 8, -120, -30, -10, 12, -90, 35, 0, 15, -120, -30, -10, 19, -90, 35, 0, 36, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 8, -14, 0, 0, 24, -14, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 8, -35, 0, 0, 26, -30, 0, 0, 36, 0, 0, 0);
        Kit.S(c, "torso", 0, 1, 1, 1, 12, 1.12f, 1.05f, 1.12f, 19, 1.12f, 1.05f, 1.12f, 36, 1, 1, 1);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_brute_raider", "Brute Raider", "ブルートレイダー")
            .game(2, 1).size(0.9f, 2.2f).hp(32).speed(0.24f).style(Style.BRUTE).egg(0x6e1520, 0xa86a48)
            .model(BruteRaider::model)
            .spawn(5, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("beat", "clap"))
            .move(Move.of("beat", 30).intent(Intent.ATTACK, 7, 1).reach(3.0f)
                .sound(14, SoundEvents.PLAYER_ATTACK_STRONG, 1.2f, 0.6f).at(15, Fx.melee(7, 3.4, 110)))
            .move(Move.of("clap", 36).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, 1.0f, 0.6f).sound(19, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, 1.0f, 0.6f)
                .at(20, (s, t) -> { Kit.str(s, 3); Fx.particles(s, ParticleTypes.POOF, 10, 0.6); }));
    }
}
