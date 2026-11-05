package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth Ruby Raider: Axe Raider 20-22 HP. Cycle: Swing (5 + 5 Block), Swing, Big Swing (12). */
public final class AxeRaider {
    private AxeRaider() {}

    public static ModelSpec model() {
        ModelSpec m = RaiderKit.base("s2_axe_raider", RaiderKit.CLOTH, RaiderKit.SKIN, false);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), handR = m.byName.get("handR"), armL = m.byName.get("armL");
        // horned helmet + shoulder pad
        head.box(-3.4f, 6, -3.4f, 7, 2, 7).color(RaiderKit.STEEL).grad(0xd8dee6, 0x70777f).bevel(0.15f);
        head.box(-5, 6.5f, -0.5f, 2, 1, 1).color(0xe9e2c8).flat();
        head.box(5 - 2, 6.5f, -0.5f, 2, 1, 1).color(0xe9e2c8).flat();
        head.box(-5, 7.5f, -0.5f, 1, 2, 1).color(0xe9e2c8).flat();
        head.box(4, 7.5f, -0.5f, 1, 2, 1).color(0xe9e2c8).flat();
        armL.box(-2.4f, -0.5f, -2.4f, 5, 2, 5).color(RaiderKit.LEATHER).bevel(0.15f).inflate(0.1f);
        armL.box(-0.5f, 0.5f, 2.4f, 1, 1, 1).color(RaiderKit.RUBY).glow(Face.FRONT, 0, 0, 1, 1, RaiderKit.RUBY);
        // wooden buckler on the left forearm
        Part sh = m.byName.get("handL").add("buckler").at(1.5f, -1.5f, 0);
        sh.box(-0.5f, -3, -3, 1, 6, 6).color(RaiderKit.WOOD).grad(0x8a6238, 0x4a2e18).bevel(0.15f).paint(Face.RIGHT, 2, 2, 2, 2, RaiderKit.RUBY);
        // axe
        Part ax = handR.add("axe").at(0, -2, 0).rot(100, 0, 0);
        ax.box(-0.6f, -5, -0.6f, 1, 15, 1).color(RaiderKit.WOOD).noise(0.1f);
        ax.box(-0.6f, 8, -0.8f, 1, 2, 5).color(RaiderKit.STEEL).grad(0xe6ebf0, 0x808890).bevel(0.1f);
        ax.box(-0.5f, 7, 3.4f, 1, 4, 2).color(0xdde3ea).flat();
        ax.box(-0.5f, 7.5f, -1.8f, 1, 2, 1).color(RaiderKit.GOLD).flat();
        Clip c = m.clip("swing", 26);
        Kit.R(c, "armR", 0, 0, 0, 0, 8, -140, 0, 12, -11, -35, -5, -5, 20, -25, 0, 0, 26, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 8, -6, 20, 0, -11, 14, -28, 0, 26, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 8, -30, 0, -15, 11, 10, 0, -10, 26, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 8, 0, 15, 0, -11, 8, -22, 0, 26, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 8, 0, 0, -1.5f, -11, 0, -0.5f, 3, 26, 0, 0, 0);
        Kit.R(c, "legR", 0, 0, 0, 0, 8, 15, 0, 0, 11, -35, 0, 0, 26, 0, 0, 0);
        Kit.R(c, "legL", 0, 0, 0, 0, 8, -10, 0, 0, 11, 25, 0, 0, 26, 0, 0, 0);
        c = m.clip("big_swing", 38);
        Kit.R(c, "armR", 0, 0, 0, 0, 14, -170, 0, 15, 1019, -10, -10, -10, 28, -5, 0, 0, 38, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 14, -150, 0, -20, -19, -20, 0, -10, 38, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 14, -22, 0, 0, -19, 35, 0, 0, 30, 20, 0, 0, 38, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 14, -25, 0, 0, -19, 20, 0, 0, 38, 0, 0, 0);
        Kit.P(c, "body", 0, 0, 0, 0, 14, 0, 2, -2, -19, 0, -3, 6, 30, 0, -3, 6, 38, 0, 0, 0);
        Kit.R(c, "legR", 0, 0, 0, 0, 14, 20, 0, 0, -19, -45, 0, 0, 38, 0, 0, 0);
        Kit.R(c, "legL", 0, 0, 0, 0, 14, -25, 0, 0, -19, 40, 0, 0, 38, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_axe_raider", "Axe Raider", "アックスレイダー")
            .game(2, 1).size(0.6f, 1.75f).hp(21).speed(0.27f).style(Style.VINDICATOR).egg(0xa8242e, 0xb8bfc8)
            .model(AxeRaider::model)
            .spawn(6, 1, 2, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("swing", "swing", "big_swing"))
            .move(Move.of("swing", 26).intent(Intent.ATTACK_DEFEND, 5, 1).reach(2.6f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 0.9f, 1.0f).at(11, Kit.hit(5, 3.0, 100)).at(11, (s, t) -> Kit.block(s, 5)))
            .move(Move.of("big_swing", 38).intent(Intent.ATTACK, 12, 1).reach(2.8f)
                .sound(15, SoundEvents.PLAYER_ATTACK_STRONG, 1.2f, 0.7f).at(19, Fx.dash(0.3, 0.0)).at(19, Kit.hit(12, 3.2, 110)));
    }
}
