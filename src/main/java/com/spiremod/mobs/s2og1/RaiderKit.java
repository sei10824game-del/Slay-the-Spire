package com.spiremod.mobs.s2og1;

import com.spiremod.spec.*;

/** Shared humanoid rig for the Ruby Raiders (red-wrapped jungle bandits with ruby ornaments). */
final class RaiderKit {
    private RaiderKit() {}
    static final int RUBY = 0xe0344a, CLOTH = 0xa8242e, CLOTH_D = 0x6e1520, LEATHER = 0x3a2a24, SKIN = 0xb07a52, SKIN_D = 0x8a5a38, GOLD = 0xd0a238, STEEL = 0xb8bfc8, WOOD = 0x6a4a2c;

    /** Builds legs/body/torso/head/arms and idle/walk/hurt/death. Parts: legR legL body torso head armR armL handR handL. */
    static ModelSpec base(String id, int cloth, int skin, boolean hood) {
        ModelSpec m = new ModelSpec(id);
        int clothD = (cloth >> 1) & 0x7f7f7f;
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.5f, 8, 0);
            leg.box(-1.6f, -8, -1.6f, 3, 8, 3).color(clothD).grad(cloth, clothD).noise(0.06f);
            leg.box(-2, -8, -2.2f, 4, 2, 4).color(LEATHER).bevel(0.15f);
            leg.box(-1.9f, -4.5f, -1.9f, 4, 1, 4).color(GOLD).flat();
        }
        Part body = m.add("body").at(0, 8, 0);
        body.box(-4, 0, -2.5f, 8, 3, 5).color(LEATHER).grad(cloth, LEATHER);
        Part torso = body.add("torso").at(0, 3, 0);
        torso.box(-4, 0, -2.5f, 8, 8, 5).color(cloth).grad(cloth, clothD).noise(0.06f).bevel(0.1f);
        torso.box(-4.2f, 0, -2.7f, 8, 2, 5).color(LEATHER).flat().inflate(0.25f);      // belt
        torso.box(-1, 0.3f, -3, 2, 2, 1).color(RUBY).glow(Face.FRONT, 0, 0, 2, 2, RUBY); // belt gem
        torso.box(-4.3f, 2, -0.5f, 8, 6, 1).color(LEATHER).flat().inflate(0.3f);       // cross-strap
        Part head = torso.add("head").at(0, 8, 0);
        head.box(-3, 0, -3, 6, 6, 6).color(skin).bevel(0.1f).noise(0.05f)
            .paint(Face.FRONT, 1, 3, 1, 1, 0x1a1008).paint(Face.FRONT, 4, 3, 1, 1, 0x1a1008).paint(Face.FRONT, 2, 5, 2, 1, SKIN_D);
        head.box(-3.4f, 4, -3.4f, 7, 3, 7).color(cloth).grad(cloth, clothD);             // headband / wrap
        head.box(-0.5f, 4.2f, -3.8f, 1, 2, 1).color(RUBY).glow(Face.FRONT, 0, 0, 1, 2, RUBY);
        if (hood) head.box(-3.8f, -0.5f, -3.8f, 8, 8, 8).color(clothD).grad(cloth, clothD).inflate(0.1f);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 5.5f, 7, 0);
            arm.box(-1.5f, -6, -1.5f, 3, 6, 3).color(skin).noise(0.05f).bevel(0.1f);
            arm.box(-1.8f, -3, -1.8f, 4, 3, 4).color(cloth).inflate(0.1f);
            arm.box(-1.8f, -6.2f, -1.8f, 4, 1, 4).color(GOLD).flat();
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -6, 0);
            hand.box(-1.2f, -3, -1.2f, 2, 3, 2).color(skin);
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 38f, 25f);
        Clip c = m.clip("idle", 44).loop();
        Kit.R(c, "torso", 0, 1.5f, 0, 0, 22, -1.5f, 0, 0, 44, 1.5f, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 12, 0, 8, 0, 34, 0, -8, 0, 44, 0, 0, 0);
        Kit.R(c, "armR", 0, 5, 0, 6, 22, 9, 0, 8, 44, 5, 0, 6);
        Kit.R(c, "armL", 0, 5, 0, -6, 22, 9, 0, -8, 44, 5, 0, -6);
        c = m.clip("hurt", 10);
        Kit.R(c, "torso", 0, 0, 0, 0, 3, 18, 0, 6, 10, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 3, -25, 0, 10, 10, 0, 0, 0);
        c = m.clip("death", 30);
        Kit.R(c, "torso", 0, 0, 0, 0, 8, 25, 0, 0, 30, 8, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 14, 0, -4, 0, 1030, 0, -8, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 10, -120, 0, 40, 30, 20, 0, 70);
        Kit.R(c, "armL", 0, 0, 0, 0, 10, -90, 0, -40, 30, 10, 0, -70);
        Kit.R(c, "head", 0, 0, 0, 0, 12, -30, 0, 0, 30, 30, 15, 10);
        Kit.R(c, "legR", 0, 0, 0, 0, 18, -70, 0, 10, 30, -80, 0, 10);
        Kit.R(c, "legL", 0, 0, 0, 0, 18, -60, 0, -10, 30, -80, 0, -10);
        return m;
    }
}
