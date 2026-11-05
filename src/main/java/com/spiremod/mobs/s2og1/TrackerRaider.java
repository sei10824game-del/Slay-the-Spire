package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth Ruby Raider: Tracker Raider 21-25 HP. Track (2 Frail) first, then Unleash the Hounds (1 x8) every turn. */
public final class TrackerRaider {
    private TrackerRaider() {}

    public static ModelSpec model() {
        ModelSpec m = RaiderKit.base("s2_tracker_raider", 0x7a3a24, 0x9a6a46, false);
        Part head = m.byName.get("head"), torso = m.byName.get("torso");
        head.box(-3.6f, 5, -3.6f, 7, 2, 7).color(0x6a4a2c).noise(0.15f);                      // feathered tracker's headdress
        for (int i = 0; i < 3; i++) head.box(-3 + i * 2.5f, 7, 2.5f, 1, 4 + i % 2, 1).color(i == 1 ? RaiderKit.RUBY : 0x2a1a10).flat();
        head.box(-3.2f, 2, -3.8f, 6, 1, 1).color(0x2a1a10).flat();                              // war paint stripe
        torso.box(-4.4f, 2, -2.8f, 9, 5, 6).color(0x6a4a2c).noise(0.2f).inflate(0.1f);          // fur mantle
        Part horn = m.byName.get("handL").add("horn").at(0, -2, 0).rot(80, 0, -20);
        horn.box(-1.2f, -1, -1.2f, 2, 3, 2).color(0xe9e2c8).bevel(0.1f);
        horn.box(-1.7f, 2, -1.7f, 3, 4, 3).color(0xe9e2c8).bevel(0.1f).paint(Face.FRONT, 0, 0, 3, 1, RaiderKit.RUBY);
        horn.box(-1, 6, -1, 2, 2, 2).color(0xcdc6a8).flat();
        // two ruby-collared hounds at its feet (each = separate part so they can lunge)
        for (int s = -1; s <= 1; s += 2) {
            String n = s < 0 ? "houndR" : "houndL";
            Part h = m.add(n).at(s * 8, 0, 3);
            h.box(-2, 4, -5, 4, 4, 9).color(0x5a4a3a).grad(0x7a6850, 0x3a2e24).noise(0.15f).bevel(0.1f);
            h.box(-1.5f, 0, -4, 1, 4, 1).color(0x3a2e24);
            h.box(0.5f, 0, -4, 1, 4, 1).color(0x3a2e24);
            h.box(-1.5f, 0, 2, 1, 4, 1).color(0x3a2e24);
            h.box(0.5f, 0, 2, 1, 4, 1).color(0x3a2e24);
            Part hh = h.add(n + "Head").at(0, 7, 4);
            hh.box(-2, -2, 0, 4, 4, 3).color(0x6a5a46).bevel(0.1f).glow(Face.FRONT, 0, 1, 1, 1, RaiderKit.RUBY).glow(Face.FRONT, 3, 1, 1, 1, RaiderKit.RUBY);
            hh.box(-1, -2, 3, 2, 2, 2).color(0x3a2e24).paint(Face.FRONT, 0, 1, 2, 1, 0xf0f0e0);
            hh.box(-2, 2, 0, 1, 2, 1).color(0x3a2e24).flat();
            hh.box(1, 2, 0, 1, 2, 1).color(0x3a2e24).flat();
            Part tl = h.add(n + "Tail").at(0, 8, -5).rot(-40, 0, 0);
            tl.box(-0.5f, 0, -3, 1, 1, 3).color(0x5a4a3a);
            h.box(-2.2f, 7, 3, 4, 1, 1).color(RaiderKit.RUBY).flat().glow(Face.FRONT, 0, 0, 4, 1, RaiderKit.RUBY);
        }
        m.proc((r, c) -> {
            float w = (float) Math.sin(c.age * 0.35f) * 18;
            r.rot("houndRTail", 0, w, 0); r.rot("houndLTail", 0, -w, 0);
            r.rot("houndRHead", (float) Math.sin(c.age * 0.1f) * 8, 0, 0); r.rot("houndLHead", (float) Math.cos(c.age * 0.12f) * 8, 0, 0);
        });
        // TRACK: blows the horn, one arm points
        Clip c = m.clip("track", 34);
        Kit.R(c, "armL", 0, 0, 0, 0, 8, -140, 20, 10, 24, -140, 20, 10, 34, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 8, -20, 0, 0, 22, -20, 0, 0, 30, -110, 0, -20, 34, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 8, -18, 0, 0, 24, -18, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 8, -10, 0, 0, 24, -10, 0, 0, 34, 0, 0, 0);
        Kit.S(c, "torso", 0, 1, 1, 1, 10, 1.1f, 1.04f, 1.1f, 24, 1.1f, 1.04f, 1.1f, 34, 1, 1, 1);
        Kit.R(c, "houndRHead", 0, 0, 0, 0, 10, -25, 0, 0, 30, -25, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "houndLHead", 0, 0, 0, 0, 10, -25, 0, 0, 30, -25, 0, 0, 34, 0, 0, 0);
        // UNLEASH THE HOUNDS: whistle, hounds alternate lunging 4x each
        c = m.clip("unleash", 36);
        Kit.R(c, "armL", 0, 0, 0, 0, 6, -150, 0, 0, 12, -150, 0, 0, 36, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 6, -30, 0, 0, 10, -100, 0, -10, 36, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 6, -8, 0, 0, 10, 16, 0, 0, 36, 0, 0, 0);
        for (int h = 0; h < 2; h++) {
            String n = h == 0 ? "houndR" : "houndL";
            float[] k = new float[4 * 10];
            int i = 0;
            k[i++] = 0; k[i++] = 0; k[i++] = 0; k[i++] = 0;
            k[i++] = 6; k[i++] = 0; k[i++] = 0; k[i++] = -2;
            for (int j = 0; j < 4; j++) { // 4 lunges: 10+h*2+j*4 out, back in 2
                float t = 10 + h * 2 + j * 5;
                k[i++] = -t; k[i++] = 0; k[i++] = 1; k[i++] = 7;
                k[i++] = t + 2; k[i++] = 0; k[i++] = 0; k[i++] = 1;
                if (i >= k.length) break;
            }
            float[] kk = java.util.Arrays.copyOf(k, i);
            Kit.P(c, n, kk);
            Kit.P(c, n, 36, 0, 0, 0);
        }
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_tracker_raider", "Tracker Raider", "トラッカーレイダー")
            .game(2, 1).size(0.6f, 1.75f).hp(23).speed(0.27f).style(Style.PILLAGER).egg(0x7a3a24, 0x5a4a3a)
            .model(TrackerRaider::model)
            .spawn(5, 1, 2, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.seq(1, "track", "unleash"))
            .move(Move.of("track", 34).intent(Intent.DEBUFF).ranged(10)
                .sound(10, SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(0).value(), 0.6f, 1.2f)
                .at(14, (s, t) -> { if (t != null) Fx.frail(t, 2 * Kit.TURN); }))
            .move(Move.of("unleash", 36).intent(Intent.ATTACK, 1, 8).reach(3.2f)
                .sound(8, SoundEvents.WOLF_GROWL, 1.0f, 1.2f)
                .atAll(new int[] {10, 12, 15, 17, 20, 22, 25, 27}, Kit.hit(1, 3.4, 140)));
    }
}
