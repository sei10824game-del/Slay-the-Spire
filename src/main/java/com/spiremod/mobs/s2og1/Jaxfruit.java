package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth: Snapping Jaxfruit 31-33 HP. Energy Orb every turn: 3 damage and +2 Strength. */
public final class Jaxfruit {
    private Jaxfruit() {}

    static final int FRUIT = 0x6aa83a, FRUIT_L = 0x9ed25a, FRUIT_D = 0x36601e, LEAF = 0x2f7a3a, PETAL = 0xd8423a, PETAL_D = 0x8a1e22, ORB = 0xffb838, TOOTH = 0xf4f0d8, MAW = 0x3a1218;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_snapping_jaxfruit");
        Part root = m.add("root0").at(0, 0, 0);
        for (int i = 0; i < 5; i++) {      // ground leaves
            Part lf = root.add("leaf" + i).at(0, 2, 0).rot(-25, i * 72, 0);
            lf.box(-2, 0, 4, 4, 1, 8).color(LEAF).grad(0x4aa050, 0x1e5028).noise(0.1f).paint(Face.TOP, 1, 0, 1, 8, 0x9ed25a);
        }
        Part stem = root.add("stem").at(0, 2, 0);
        stem.box(-3, 0, -3, 6, 7, 6).color(FRUIT_D).grad(FRUIT, FRUIT_D).noise(0.1f).bevel(0.1f);
        Part bulb = stem.add("bulb").at(0, 7, 0);
        bulb.box(-6, 0, -6, 12, 6, 12).color(FRUIT).grad(FRUIT_L, FRUIT_D).noise(0.08f).bevel(0.25f)
            .paint(Face.LEFT, 3, 1, 1, 5, FRUIT_D).paint(Face.RIGHT, 3, 1, 1, 5, FRUIT_D).paint(Face.BACK, 5, 1, 1, 5, FRUIT_D);
        Part jawU = bulb.add("jawU").at(0, 6, 6);
        jawU.box(-6, 0, -12, 12, 4, 12).color(FRUIT).grad(FRUIT_L, FRUIT).bevel(0.25f).noise(0.08f).paint(Face.BOTTOM, 1, 1, 10, 10, MAW);
        for (int i = 0; i < 5; i++) jawU.box(-5 + i * 2.2f, -1.5f, -11.5f, 1, 2, 1).color(TOOTH).flat();
        jawU.box(-3, 4, -9, 6, 3, 6).color(FRUIT_L).bevel(0.2f);
        jawU.box(-1, 7, -7, 2, 2, 2).color(PETAL).glow(Face.TOP, 0, 0, 2, 2, PETAL);       // flower bud
        jawU.box(-4, 3.5f, -4.2f, 2, 2, 2).color(0xfff0a0).glow(Face.FRONT, 0, 0, 2, 2, 0xfff0a0);   // eyes
        jawU.box(2, 3.5f, -4.2f, 2, 2, 2).color(0xfff0a0).glow(Face.FRONT, 0, 0, 2, 2, 0xfff0a0);
        Part jawL = bulb.add("jawL").at(0, 6, 6);
        jawL.box(-6, -4, -12, 12, 4, 12).color(FRUIT_D).grad(FRUIT, FRUIT_D).bevel(0.25f).noise(0.08f).paint(Face.TOP, 1, 1, 10, 10, MAW);
        for (int i = 0; i < 4; i++) jawL.box(-4 + i * 2.6f, -0.5f, -11.5f, 1, 2, 1).color(TOOTH).flat();
        Part orb = bulb.add("orb").at(0, 6, 0);
        orb.box(-2.5f, -2.5f, -2.5f, 5, 5, 5).color(ORB).glow(Face.FRONT, 0, 0, 5, 5, ORB).glow(Face.TOP, 0, 0, 5, 5, ORB).glow(Face.LEFT, 0, 0, 5, 5, ORB).glow(Face.RIGHT, 0, 0, 5, 5, ORB);
        for (int s = -1; s <= 1; s += 2) {   // petals / thorny vines on the sides
            Part pt = bulb.add(s < 0 ? "petalR" : "petalL").at(s * 6, 3, 0).rot(0, 0, s * -20);
            pt.box(s < 0 ? -1 : 0, -1, -4, 1, 8, 8).color(PETAL).grad(0xe85a4a, PETAL_D).flat().bevel(0.1f);
        }
        m.look("bulb", 50, 20);
        Clip c = m.clip("idle", 36).loop();
        Kit.R(c, "bulb", 0, 0, 3, 0, 18, 0, -3, 0, 36, 0, 3, 0);
        Kit.R(c, "jawU", 0, -3, 0, 0, 18, 6, 0, 0, 36, -3, 0, 0);
        Kit.R(c, "jawL", 0, 3, 0, 0, 18, -4, 0, 0, 36, 3, 0, 0);
        Kit.S(c, "orb", 0, 1, 1, 1, 18, 1.2f, 1.2f, 1.2f, 36, 1, 1, 1);
        Kit.R(c, "petalR", 0, 0, 0, 0, 18, 0, 0, -10, 36, 0, 0, 0);
        Kit.R(c, "petalL", 0, 0, 0, 0, 18, 0, 0, 10, 36, 0, 0, 0);
        m.clip("walk", 16).loop();
        Kit.R(m.clip("walk", 16).loop(), "bulb", 0, 0, 0, 5, 8, 0, 0, -5, 16, 0, 0, 5);
        // ENERGY ORB: bulb draws back, jaws gape and an orb swells inside, then snaps shut as the orb is spat
        c = m.clip("energy_orb", 32);
        Kit.R(c, "bulb", 0, 0, 0, 0, 10, -22, 0, 0, -16, 28, 0, 0, 22, 8, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "jawU", 0, 0, 0, 0, 10, -40, 0, 0, 14, -42, 0, 0, -17, 5, 0, 0, 32, 0, 0, 0);
        Kit.R(c, "jawL", 0, 0, 0, 0, 10, 35, 0, 0, 14, 35, 0, 0, -17, -5, 0, 0, 32, 0, 0, 0);
        Kit.S(c, "orb", 0, 1, 1, 1, 10, 1.8f, 1.8f, 1.8f, 15, 2.6f, 2.6f, 2.6f, -16, 0.4f, 0.4f, 0.4f, 24, 1, 1, 1);
        Kit.P(c, "orb", 0, 0, 0, 0, 10, 0, 0, -2, 15, 0, 0, -6, -16, 0, 0, -10, 24, 0, 0, 0);
        Kit.S(c, "stem", 0, 1, 1, 1, 10, 1, 0.85f, 1, -16, 1, 1.15f, 1, 32, 1, 1, 1);
        Kit.hurt(m, "bulb", -15, 10);
        c = m.clip("death", 28);
        Kit.R(c, "bulb", 0, 0, 0, 0, 8, -20, 0, 0, 28, 60, 0, 10);
        Kit.R(c, "jawU", 0, 0, 0, 0, 8, -45, 0, 0, 28, -10, 0, 0);
        Kit.R(c, "jawL", 0, 0, 0, 0, 8, 40, 0, 0, 28, 20, 0, 0);
        Kit.S(c, "stem", 0, 1, 1, 1, 28, 1.15f, 0.2f, 1.15f);
        Kit.S(c, "orb", 0, 1, 1, 1, 14, 2f, 2f, 2f, 28, 0.01f, 0.01f, 0.01f);
        Kit.P(c, "bulb", 0, 0, 0, 0, 28, 0, -5, 0);
        return m;
    }

    public static MobDef def() {
        if (ProjKind.of("s2_jax_orb") == null) ProjKind.orb("s2_jax_orb", 0xffb838, 4).trail(ParticleTypes.FLAME);
        return MobDef.of("s2_snapping_jaxfruit", "Snapping Jaxfruit", "暴れジャックフルーツ")
            .game(2, 1).size(1.0f, 1.2f).hp(32).speed(0.1f).style(Style.SLIME).egg(0x6aa83a, 0xffb838)
            .model(Jaxfruit::model)
            .spawn(6, 1, 1, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("energy_orb"))
            .move(Move.of("energy_orb", 32).intent(Intent.ATTACK_BUFF, 3, 1).ranged(12)
                .sound(8, SoundEvents.BEACON_POWER_SELECT, 1.0f, 1.8f).sound(16, SoundEvents.BLAZE_SHOOT, 1.0f, 1.3f)
                .at(16, Fx.shoot("s2_jax_orb", 3, 1.3f, 0, 1.0, 0.8, null)).at(17, (s, t) -> Kit.str(s, 2)));
    }
}
