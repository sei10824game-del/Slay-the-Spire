package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth Ruby Raider: Assassin Raider 18-23 HP. Killshot (10) every turn. */
public final class AssassinRaider {
    private AssassinRaider() {}

    public static ModelSpec model() {
        ModelSpec m = RaiderKit.base("s2_assassin_raider", 0x4a1220, 0x7a5236, true);
        Part head = m.byName.get("head");
        head.box(-3.2f, 0.5f, -3.9f, 6, 3, 1).color(0x1a0a10).flat().inflate(0.1f).glow(Face.FRONT, 1, 1, 1, 1, RaiderKit.RUBY).glow(Face.FRONT, 4, 1, 1, 1, RaiderKit.RUBY); // mask + glowing eyes
        Part tail = head.add("scarf").at(0, 4, 3.8f).rot(15, 0, 0);
        tail.box(-1.5f, -7, 0, 3, 8, 1).color(RaiderKit.CLOTH).grad(RaiderKit.RUBY, RaiderKit.CLOTH_D);
        for (int s = -1; s <= 1; s += 2) {
            Part hand = m.byName.get(s < 0 ? "handR" : "handL");
            Part d = hand.add(s < 0 ? "daggerR" : "daggerL").at(0, -2.5f, 0).rot(100, 0, 0);
            d.box(-0.5f, -2, -0.5f, 1, 3, 1).color(RaiderKit.LEATHER);
            d.box(-1.2f, 1, -0.5f, 2, 1, 1).color(RaiderKit.GOLD).flat();
            d.box(-0.4f, 2, -0.3f, 1, 7, 1).color(0xcfd6de).flat();
        }
        Part quiver = m.byName.get("torso").add("sash").at(0, 4, 0);
        quiver.box(-4.3f, -1, -2.8f, 9, 1, 6).color(RaiderKit.RUBY).flat().inflate(0.1f);
        m.proc((r, c) -> r.rot("scarf", (float) Math.sin(c.age * 0.2f) * 6 + c.swingAmt * 25, 0, 0));
        // KILLSHOT: drops into a crouch, vanishes forward in a streak with both blades crossing, recovers low
        Clip c = m.clip("killshot", 30);
        Kit.R(c, "torso", 0, 0, 0, 0, 10, 28, 0, 0, -14, 35, 0, 0, 20, 20, 0, 0, 30, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 10, 0, -3, -2, -14, 0, -2, 9, 22, 0, -1, 7, 30, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 10, 20, 0, 40, -14, -95, -25, -10, 22, -80, -20, -5, 30, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 10, 20, 0, -40, -14, -95, 25, 10, 22, -80, 20, 5, 30, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 10, 10, 0, 0, -14, 8, 0, 0, 30, 0, 0, 0);
        Kit.R(c, "legR", 0, 0, 0, 0, 10, 55, 0, 0, -14, -55, 0, 0, 30, 0, 0, 0);
        Kit.R(c, "legL", 0, 0, 0, 0, 10, -35, 0, 0, -14, 45, 0, 0, 30, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_assassin_raider", "Assassin Raider", "アサシンレイダー")
            .game(2, 1).size(0.6f, 1.7f).hp(20).speed(0.32f).style(Style.PILLAGER).egg(0x4a1220, 0xe0344a)
            .model(AssassinRaider::model)
            .spawn(5, 1, 2, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("killshot"))
            .move(Move.of("killshot", 30).intent(Intent.ATTACK, 10, 1).reach(4.5f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.6f)
                .at(13, Fx.dash(0.9, 0.0)).at(15, Kit.hit(10, 3.2, 100)));
    }
}
