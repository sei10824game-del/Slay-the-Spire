package com.spiremod.mobs.s1act1b;

import com.spiremod.spec.*;

/** Shared little-gremlin body (big horned head, spindly limbs) + standard idle/hurt/death clips. */
final class GremlinKit {
    private GremlinKit() {}

    /**
     * Parts: legR legL body torso head jaw hornR hornL armR armL handR handL.
     * fat = round pot belly (Fat Gremlin). Head front = +z.
     */
    static void build(ModelSpec m, int skin, int dark, int horn, int eye, boolean fat) {
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 1.5f, 5, 0);
            leg.box(-1, -5, -1, 2, 5, 2).color(skin).grad(skin, dark).noise(0.08f);
            leg.box(-1.2f, -5, -1.2f, 2, 1, 3).color(dark).bevel(0.15f);       // splayed foot
            leg.box(-0.7f, -5, 1.6f, 1, 1, 1).color(0x2a1a18);                  // toe claw
        }
        Part body = m.add("body").at(0, 5, 0);
        body.box(-2.5f, 0, -1.5f, 5, 2, 3).color(dark).grad(skin, dark);
        Part torso = body.add("torso").at(0, 1.5f, 0);
        if (fat) {
            torso.box(-4, 0, -3.5f, 8, 7, 7).color(skin).grad(skin, dark).bevel(0.22f).noise(0.06f)
                .paint(Face.FRONT, 2, 4, 4, 1, dark).px(Face.FRONT, 4, 3, dark);              // belly fold + navel
        } else {
            torso.box(-2, 0, -1.5f, 4, 5, 3).color(skin).grad(skin, dark).bevel(0.15f).noise(0.06f)
                .paint(Face.FRONT, 1, 1, 2, 1, dark);                                          // ribs/chest shade
        }
        float top = fat ? 6.5f : 5;
        Part head = torso.add("head").at(0, top, fat ? 0.5f : 0.3f);
        head.box(-3, 0, -3, 6, 5, 6).color(skin).grad(skin, dark).bevel(0.18f).noise(0.06f)
            .paint(Face.FRONT, 0, 0, 6, 1, dark)                                  // heavy brow
            .glow(Face.FRONT, 1, 1, 2, 1, eye).glow(Face.FRONT, 3, 1, 2, 1, eye)
            .px(Face.FRONT, 2, 1, 0x101010).px(Face.FRONT, 3, 1, 0x101010);       // pupils toward centre
        head.box(-1, 1.5f, 3, 2, 2, 1).color(dark).bevel(0.2f);                    // nose snout
        Part jaw = head.add("jaw").at(0, 1, 1);
        jaw.box(-2.5f, -1.5f, -1.5f, 5, 2, 4).color(skin).grad(skin, dark)
            .paint(Face.FRONT, 0, 0, 5, 1, 0x2a0f10).px(Face.FRONT, 1, 0, 0xf0ead8).px(Face.FRONT, 3, 0, 0xf0ead8);
        for (int s = -1; s <= 1; s += 2) {
            Part h = head.add(s < 0 ? "hornR" : "hornL").at(s * 3, 4, 0).rot(0, 0, s * -10);
            h.box(s < 0 ? -2 : 0, -0.5f, -0.5f, 2, 1, 1).color(horn).bevel(0.2f);
            h.box(s < 0 ? -3 : 1, 0, -0.5f, 2, 1, 1).color(horn).grad(0xffffff & (horn | 0x202020), horn);
            h.box(s < 0 ? -3.5f : 2.5f, 1, -0.5f, 1, 2, 1).color(horn).bevel(0.2f);
            h.box(s < 0 ? -3f : 2f, 2.5f, -0.5f, 1, 1, 1).color(horn);            // curl tip
            // ear
            Part ear = head.add(s < 0 ? "earR" : "earL").at(s * 3, 2.5f, -0.5f).rot(0, s * -20, s * 25);
            ear.box(s < 0 ? -2 : 0, -0.5f, -0.5f, 2, 1, 1).color(dark);
        }
        for (int s = -1; s <= 1; s += 2) {
            float sx = fat ? 4.6f : 2.6f;
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * sx, top - 1, 0);
            arm.box(-0.5f, -5, -0.5f, 1, 5, 1).color(skin).grad(skin, dark);
            arm.box(-0.8f, -1.5f, -0.8f, 2, 2, 2).color(skin).bevel(0.2f);         // shoulder knob
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -5, 0);
            hand.box(-1, -2, -1, 2, 2, 2).color(dark).bevel(0.2f);
            hand.box(-1, -2.6f, 0.4f, 1, 1, 1).color(0x2a1a18);                     // claw
        }
        m.look("head", 50, 25);
        m.walkBiped("legR", "legL", "armR", "armL", 45f, 30f);
    }

    /** jittery gremlin idle, flinch, collapse */
    static void stdClips(ModelSpec m) {
        m.clip("idle", 24).loop()
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, 0.6f, 0).pos("body", 12, 0, 0, 0).pos("body", 18, 0, 0.6f, 0).pos("body", 24, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 6, -4, 10, 4).rot("head", 14, 2, -12, -4).rot("head", 24, 0, 0, 0)
            .rot("armR", 0, -8, 0, -12).rot("armR", 12, -18, 0, -18).rot("armR", 24, -8, 0, -12)
            .rot("armL", 0, -8, 0, 12).rot("armL", 12, -18, 0, 18).rot("armL", 24, -8, 0, 12)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 16, 0, 0, 0).rot("jaw", 18, 18, 0, 0).rot("jaw", 21, 0, 0, 0).rot("jaw", 24, 0, 0, 0)
            .rot("hornR", 0, 0, 0, 0).rot("hornR", 12, 0, 0, 6).rot("hornR", 24, 0, 0, 0)
            .rot("hornL", 0, 0, 0, 0).rot("hornL", 12, 0, 0, -6).rot("hornL", 24, 0, 0, 0);
        m.clip("hurt", 10)
            .rot("torso", 0, 0, 0, 0).rot("torso", 3, -20, 0, 8).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 10, 0).rot("head", 10, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 3, 30, 0, 0).rot("jaw", 10, 0, 0, 0);
        m.clip("death", 26)
            .rot("body", 0, 0, 0, 0).rot("body", 6, -15, 0, 0).rot("body", 20, -85, 0, 10, Ease.IN_QUAD).rot("body", 26, -80, 0, 10, Ease.OUT_BOUNCE)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, 2, 0).pos("body", 20, 0, -3, -2, Ease.IN_QUAD)
            .rot("head", 0, 0, 0, 0).rot("head", 6, -30, 0, 0).rot("head", 26, 20, 30, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 6, 35, 0, 0).rot("jaw", 26, 25, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -150, 0, -30).rot("armR", 26, -170, 0, -60)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -150, 0, 30).rot("armL", 26, -170, 0, 60)
            .rot("legR", 0, 0, 0, 0).rot("legR", 20, -60, 0, 0).rot("legR", 26, -90, 0, -10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 20, -40, 0, 0).rot("legL", 26, -90, 0, 10);
    }
}
