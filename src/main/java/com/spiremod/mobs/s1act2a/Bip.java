package com.spiremod.mobs.s1act2a;

import com.spiremod.spec.*;

/** Shared humanoid rig for the s1act2a humanoids: parts legR/legL/body/torso/head/armR/armL/handR/handL + idle/walk/hurt/death. */
final class Bip {
    Part body, torso, head, armR, armL, handR, handL, legR, legL;
    int bw, legH, torsoH, armLen;

    static Bip make(ModelSpec m, int skin, int cloth, int clothD, int pants, int boots, int bw, int legH, int torsoH, int armLen, int hs) {
        Bip b = new Bip(); b.bw = bw; b.legH = legH; b.torsoH = torsoH; b.armLen = armLen;
        int lt = Math.round(bw * 0.375f), at = Math.max(2, Math.round(bw * 0.33f)), td = Math.round(bw * 0.6f);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * (bw / 2f - lt / 2f), legH, 0);
            leg.box(-lt / 2f, -legH, -lt / 2f, lt, legH, lt).color(pants).noise(0.08f);
            leg.box(-lt / 2f - 0.5f, -legH, -lt / 2f - 0.5f, lt + 1, 3, lt + 2).color(boots).bevel(0.12f);
            if (s < 0) b.legR = leg; else b.legL = leg;
        }
        b.body = m.add("body").at(0, legH, 0);
        b.body.box(-bw / 2f, 0, -td / 2f, bw, 3, td).color(clothD).grad(cloth, clothD);
        b.torso = b.body.add("torso").at(0, 3, 0);
        b.torso.box(-bw / 2f, 0, -td / 2f, bw, torsoH - 3, td).color(cloth).grad(cloth, clothD).bevel(0.1f).noise(0.06f);
        b.head = b.torso.add("head").at(0, torsoH - 3, 0);
        b.head.box(-hs / 2f, 0, -hs / 2f, hs, hs, hs).color(skin).noise(0.06f).bevel(0.1f);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = b.torso.add(s < 0 ? "armR" : "armL").at(s * (bw / 2f + at / 2f), torsoH - 4, 0);
            arm.box(-at / 2f, -armLen, -at / 2f, at, armLen - 2, at).color(cloth).bevel(0.1f);
            Part hand = arm.add(s < 0 ? "handR" : "handL").at(0, -armLen + 2, 0);
            hand.box(-at / 2f, -3, -at / 2f, at, 3, at).color(skin).noise(0.05f);
            if (s < 0) { b.armR = arm; b.handR = hand; } else { b.armL = arm; b.handL = hand; }
        }
        m.look("head", 50, 30);
        m.walkBiped("legR", "legL", "armR", "armL", 36f, 22f);
        m.clip("idle", 48).loop()
            .rot("torso", 0, 1.5f, 0, 0).rot("torso", 24, -1.5f, 0, 0).rot("torso", 48, 1.5f, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 14, 0, 8, 3).rot("head", 38, 0, -8, -3).rot("head", 48, 0, 0, 0)
            .rot("armR", 0, 4, 0, 6).rot("armR", 24, 8, 0, 8).rot("armR", 48, 4, 0, 6)
            .rot("armL", 0, 4, 0, -6).rot("armL", 24, 8, 0, -8).rot("armL", 48, 4, 0, -6);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, 18, 0, 6).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -25, 0, 10).rot("head", 10, 0, 0, 0);
        m.clip("death", 30)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, 25, 0, 0).rot("torso", 30, 10, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -4, 0, Ease.IN_QUAD).pos("body", 30, 0, -legH + 3, 0, Ease.OUT_BOUNCE)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -120, 0, 40).rot("armR", 30, 20, 0, 70)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -90, 0, -40).rot("armL", 30, 10, 0, -70)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 30, 30, 15, 10)
            .rot("legR", 0, 0, 0, 0).rot("legR", 18, -70, 0, 10).rot("legR", 30, -80, 0, 10)
            .rot("legL", 0, 0, 0, 0).rot("legL", 18, -60, 0, -10).rot("legL", 30, -80, 0, -10);
        return b;
    }
}
