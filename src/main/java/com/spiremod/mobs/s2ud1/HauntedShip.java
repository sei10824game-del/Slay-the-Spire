package com.spiremod.mobs.s2ud1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/**
 * StS2 Underdocks Haunted Ship (63 HP). Haunt first (5 Dazed into the deck + Weak 3), then alternates Swipe (13) / Stomp (4x3).
 */
public final class HauntedShip {
    private HauntedShip() {}
    static final int WOOD = 0x5a4a44, WOOD_D = 0x33292a, WOOD_L = 0x7d6a5c, SAIL = 0xb8c8c0, GHOST = 0x7fe8d0, ROPE = 0x8a7a50, IRON = 0x4a525c;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_haunted_ship");
        Part hull = m.add("hull").at(0, 6, 0);
        hull.box(-9, 0, -14, 18, 8, 28).color(WOOD).grad(WOOD_L, WOOD_D).noise(0.2f).bevel(0.4f);              // hull
        hull.box(-7, -3, -11, 14, 3, 22).color(WOOD_D).noise(0.2f).bevel(0.4f);                                // keel
        hull.box(-5, -5, -8, 10, 2, 16).color(WOOD_D).noise(0.2f);
        for (int i = 0; i < 4; i++) hull.box(-9.4f, 4, -11 + i * 7, 1, 4, 1).color(WOOD_D).flat();                // planking seams
        hull.box(-6, 8, -14, 12, 3, 28).color(WOOD_L).noise(0.2f).bevel(0.3f);                                 // gunwale
        hull.box(-8, 8, -14, 2, 3, 28).color(WOOD).noise(0.2f); hull.box(6, 8, -14, 2, 3, 28).color(WOOD).noise(0.2f);
        for (int i = 0; i < 3; i++) for (int s = -1; s <= 1; s += 2) hull.box(s * 9.4f - 0.5f, 3, -9 + i * 8, 1, 3, 3).color(0x0a1a18).flat().glow(Face.FRONT, 0, 0, 1, 2, GHOST); // glowing portholes
        // stern castle
        hull.box(-7, 11, -14, 14, 6, 9).color(WOOD).grad(WOOD_L, WOOD_D).noise(0.2f).bevel(0.3f);
        hull.box(-3, 13, -14.5f, 6, 3, 1).color(0x0a1a18).flat().glow(Face.BACK, 1, 0, 4, 2, GHOST);
        // bowsprit
        hull.box(-1, 7, 14, 2, 2, 10).color(WOOD_D).noise(0.2f).bevel(0.2f);
        Part fig = hull.add("figurehead").at(0, 6, 24);
        fig.box(-3, -3, 0, 6, 6, 4).color(GHOST).grad(0xc8fff0, 0x3fb89c).bevel(0.4f)
            .glow(Face.FRONT, 1, 1, 1, 1, 0xffffff).glow(Face.FRONT, 4, 1, 1, 1, 0xffffff);                     // ghostly bow face
        fig.box(-1.5f, -4.5f, 3.5f, 3, 2, 2).color(0x0a1a18).flat();
        // masts and sails
        Part mast = hull.add("mast").at(0, 11, 2);
        mast.box(-1.5f, 0, -1.5f, 3, 30, 3).color(WOOD_D).noise(0.2f).bevel(0.2f);
        mast.box(-9, 20, -1, 18, 2, 2).color(WOOD).noise(0.2f);
        mast.box(-7, 28, -1, 14, 2, 2).color(WOOD).noise(0.2f);
        Part sail = mast.add("sail").at(0, 20, 0);
        sail.box(-8, -10, -0.5f, 16, 10, 1).color(SAIL).grad(0xdbe8e2, 0x7a9a90).noise(0.2f);
        for (int i = 0; i < 4; i++) sail.box(-8 + i * 4, -13, -0.5f, 3, 3, 1).color(SAIL).grad(0x9ab8ac, 0x5a7a70).noise(0.25f); // ragged hem
        sail.box(-3, -7, 0.4f, 6, 4, 1).color(0x1c2a26).flat().glow(Face.FRONT, 1, 1, 4, 1, GHOST).glow(Face.FRONT, 1, 3, 4, 1, GHOST);   // skull mark
        Part sail2 = mast.add("sail2").at(0, 28, 0);
        sail2.box(-6, -7, -0.5f, 12, 7, 1).color(SAIL).grad(0xdbe8e2, 0x7a9a90).noise(0.2f);
        for (int i = 0; i < 3; i++) sail2.box(-6 + i * 4, -9, -0.5f, 3, 2, 1).color(SAIL).noise(0.25f);
        Part flag = mast.add("flag").at(0, 30, 0);
        flag.box(-0.5f, -1, -5, 1, 3, 6).color(0x1c1c24).noise(0.1f);
        // spectral arms: anchor-swipe arm (right) and hammer arm (left), chains + ghostly hands
        for (int s = -1; s <= 1; s += 2) {
            Part arm = hull.add(s < 0 ? "armR" : "armL").at(s * 9, 7, 8);
            arm.box(-1.5f, -2, -1.5f, 3, 3, 3).color(IRON).bevel(0.3f);
            Part chain = arm.add(s < 0 ? "chainR" : "chainL").at(0, -1, 0);
            for (int i = 0; i < 4; i++) chain.box(-1, -3 - i * 3, -0.5f, 2, 2, 1).color(i % 2 == 0 ? IRON : 0x7a828c).noise(0.1f);
            Part end = chain.add(s < 0 ? "anchor" : "fist").at(0, -13, 0);
            if (s < 0) { // anchor
                end.box(-0.7f, -8, -0.7f, 2, 9, 2).color(IRON).noise(0.1f);
                end.box(-4, -8, -0.7f, 8, 2, 2).color(IRON).noise(0.1f).bevel(0.2f);
                end.box(-5, -7, -0.7f, 2, 3, 2).color(IRON).bevel(0.2f); end.box(3, -7, -0.7f, 2, 3, 2).color(IRON).bevel(0.2f);
                end.box(-1.5f, 1, -0.7f, 3, 1, 2).color(IRON);
            } else { // ghostly fist
                end.box(-3, -6, -3, 6, 6, 6).color(GHOST).grad(0xc8fff0, 0x3fb89c).bevel(0.5f).glow(Face.FRONT, 1, 1, 4, 1, 0xffffff);
            }
        }
        // ghost wisps around
        for (int i = 0; i < 4; i++) m.add("wisp" + i).at(0, 0, 0).cbox(0, 2, 2, 2).color(GHOST).flat().glow(Face.FRONT, 0, 0, 2, 2, GHOST);
        m.proc((r, c) -> {
            r.rot("sail", (float) Math.sin(c.age * 0.1f) * 4, (float) Math.sin(c.age * 0.07f) * 6, 0);
            r.rot("sail2", (float) Math.sin(c.age * 0.13f + 1) * 5, 0, 0);
            r.rot("flag", 0, (float) Math.sin(c.age * 0.2f) * 15, 0);
            for (int i = 0; i < 4; i++) {
                double a = c.age * 0.07 + i * Math.PI / 2;
                r.pos("wisp" + i, (float) Math.cos(a) * 13, 6 + (float) Math.sin(c.age * 0.1 + i) * 4 + i * 3, (float) Math.sin(a) * 17);
            }
        });
        m.bob(1.2f, 0.1f);
        m.clip("idle", 60).loop()
            .rot("hull", 0, 0, 0, 2).rot("hull", 30, 2, 0, -2).rot("hull", 60, 0, 0, 2)
            .pos("hull", 0, 0, 0, 0).pos("hull", 30, 0, 1.2f, 0).pos("hull", 60, 0, 0, 0)
            .rot("armR", 0, 0, 0, 5).rot("armR", 30, 0, 0, 12).rot("armR", 60, 0, 0, 5)
            .rot("armL", 0, 0, 0, -5).rot("armL", 30, 0, 0, -12).rot("armL", 60, 0, 0, -5);
        m.clip("walk", 30).loop()
            .rot("hull", 0, 3, 0, 4).rot("hull", 15, -3, 0, -4).rot("hull", 30, 3, 0, 4)
            .pos("hull", 0, 0, 0, 0).pos("hull", 15, 0, 1.5f, 0).pos("hull", 30, 0, 0, 0);
        // HAUNT: rears, hull glows, sails billow, moaning wail
        m.clip("haunt", 44)
            .rot("hull", 0, 0, 0, 0).rot("hull", 14, -14, 0, 0, Ease.OUT_BACK).rot("hull", 30, -10, 0, 0).rot("hull", 44, 0, 0, 0)
            .pos("hull", 0, 0, 0, 0).pos("hull", 14, 0, 4, -2).pos("hull", 30, 0, 3, -1).pos("hull", 44, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 14, -50, 0, 50, Ease.OUT_BACK).rot("armR", 34, -50, 0, 50).rot("armR", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 14, -50, 0, -50, Ease.OUT_BACK).rot("armL", 34, -50, 0, -50).rot("armL", 44, 0, 0, 0)
            .scl("sail", 0, 1, 1, 1).scl("sail", 14, 1.2f, 1.1f, 2.5f).scl("sail", 34, 1.2f, 1.1f, 2.5f).scl("sail", 44, 1, 1, 1)
            .scl("figurehead", 0, 1, 1, 1).scl("figurehead", 14, 1.5f, 1.5f, 1.5f, Ease.OUT_BACK).scl("figurehead", 34, 1.4f, 1.4f, 1.4f).scl("figurehead", 44, 1, 1, 1);
        // SWIPE: the anchor swings out and sweeps across
        m.clip("swipe", 34)
            .rot("hull", 0, 0, 0, 0).rot("hull", 10, 0, -22, 6).rot("hull", 15, 0, 28, -6, Ease.IN_CUBIC).rot("hull", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 5).rot("armR", 10, -20, 40, 70, Ease.OUT_QUAD).rot("armR", 15, -95, -70, -60, Ease.IN_CUBIC).rot("armR", 22, -80, -50, -45).rot("armR", 34, 0, 0, 5)
            .rot("chainR", 0, 0, 0, 0).rot("chainR", 10, -20, 0, 0).rot("chainR", 15, -40, 0, 0).rot("chainR", 34, 0, 0, 0)
            .rot("anchor", 0, 0, 0, 0).rot("anchor", 10, 0, 0, 25).rot("anchor", 15, 0, 0, -30).rot("anchor", 34, 0, 0, 0)
            .rot("armL", 0, 0, 0, -5).rot("armL", 12, 15, 0, -30).rot("armL", 34, 0, 0, -5);
        // STOMP: rears up and slams down three times
        m.clip("stomp", 44)
            .pos("hull", 0, 0, 0, 0).pos("hull", 6, 0, 5, -1).pos("hull", 9, 0, -1, 2, Ease.IN_CUBIC).pos("hull", 14, 0, 5, 0).pos("hull", 17, 0, -1, 2, Ease.IN_CUBIC).pos("hull", 22, 0, 5, 0).pos("hull", 25, 0, -1, 2, Ease.IN_CUBIC).pos("hull", 44, 0, 0, 0)
            .rot("hull", 0, 0, 0, 0).rot("hull", 6, -16, 0, 0).rot("hull", 9, 8, 0, 0, Ease.IN_CUBIC).rot("hull", 14, -16, 0, 0).rot("hull", 17, 8, 0, 0, Ease.IN_CUBIC).rot("hull", 22, -16, 0, 0).rot("hull", 25, 8, 0, 0, Ease.IN_CUBIC).rot("hull", 44, 0, 0, 0)
            .rot("armL", 0, 0, 0, -5).rot("armL", 6, -90, 0, -10).rot("armL", 9, 10, 0, -5, Ease.IN_CUBIC).rot("armL", 14, -90, 0, -10).rot("armL", 17, 10, 0, -5, Ease.IN_CUBIC).rot("armL", 22, -90, 0, -10).rot("armL", 25, 10, 0, -5, Ease.IN_CUBIC).rot("armL", 44, 0, 0, -5)
            .rot("armR", 0, 0, 0, 5).rot("armR", 6, -90, 0, 10).rot("armR", 9, 10, 0, 5, Ease.IN_CUBIC).rot("armR", 14, -90, 0, 10).rot("armR", 17, 10, 0, 5, Ease.IN_CUBIC).rot("armR", 22, -90, 0, 10).rot("armR", 25, 10, 0, 5, Ease.IN_CUBIC).rot("armR", 44, 0, 0, 5);
        m.clip("hurt", 10).rot("hull", 0, 0, 0, 0).rot("hull", 3, 6, 0, 8).rot("hull", 10, 0, 0, 0);
        m.clip("death", 44)
            .rot("hull", 0, 0, 0, 0).rot("hull", 16, 10, 0, 18).rot("hull", 38, 25, 0, 70, Ease.OUT_BOUNCE).rot("hull", 44, 25, 0, 72)
            .pos("hull", 0, 0, 0, 0).pos("hull", 38, 0, -2, 0, Ease.OUT_BOUNCE).scl("sail", 0, 1, 1, 1).scl("sail", 30, 1, 0.3f, 1)
            .scl("figurehead", 0, 1, 1, 1).scl("figurehead", 30, 0.1f, 0.1f, 0.1f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_haunted_ship", "Haunted Ship", "幽霊船")
            .game(2, 1).size(1.8f, 2.4f).hp(63).speed(0.2f).style(Style.GHOST).egg(0x5a4a44, 0x7fe8d0)
            .model(HauntedShip::model)
            .spawn(4, 1, 1, MobDef.Habitat.SWAMP)
            .pattern(Pattern.seq(1, "haunt", "swipe", "stomp"))
            .move(Move.of("haunt", 44).intent(Intent.DEBUFF).reach(8)
                .sound(12, SoundEvents.GHAST_AMBIENT, 1.4f, 0.5f)
                .at(16, (s, t) -> { Fx.particles(s, ParticleTypes.SOUL, 30, 1.2); if (t != null) { Fx.weak(t, 8); for (int i = 0; i < 3; i++) Fx.curse(t, Fx.Curse.DAZED); } }))
            .move(Move.of("swipe", 34).intent(Intent.ATTACK, 13, 1).reach(3.6f)
                .sound(10, SoundEvents.CHAIN_PLACE, 1.4f, 0.6f)
                .at(15, (s, t) -> { if (t != null) t.invulnerableTime = 0; Fx.meleeNow(s, 13, 4.2, 130, v -> Fx.knock(s, v, 0.6)); }))
            .move(Move.of("stomp", 44).intent(Intent.ATTACK, 4, 3).reach(3.2f)
                .sound(9, SoundEvents.ANVIL_LAND, 0.8f, 0.5f)
                .atAll(new int[]{9, 17, 25}, (s, t) -> { Fx.sound(s, SoundEvents.ANVIL_LAND, 0.9f, 0.5f); Fx.particles(s, ParticleTypes.SOUL_FIRE_FLAME, 10, 1.2); U.hit(4, 3.8, 360, null).run(s, t); }));
    }
}
