package com.spiremod.mobs.s2og1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Overgrowth Ruby Raider: Crossbow Raider 18-21 HP. Alternates Reload (3 Block) and Fire! (14), opening with Reload. */
public final class CrossbowRaider {
    private CrossbowRaider() {}

    public static ModelSpec model() {
        ModelSpec m = RaiderKit.base("s2_crossbow_raider", 0x8a2a30, RaiderKit.SKIN, false);
        Part head = m.byName.get("head"), torso = m.byName.get("torso");
        head.box(-3.4f, 6, -3.4f, 7, 1, 7).color(RaiderKit.LEATHER).flat();                       // wide-brim leather cap
        head.box(-2.5f, 7, -2.5f, 5, 2, 5).color(RaiderKit.LEATHER).bevel(0.2f);
        head.box(-1, 7.5f, -2.9f, 2, 1, 1).color(RaiderKit.RUBY).glow(Face.FRONT, 0, 0, 2, 1, RaiderKit.RUBY);
        Part quiver = torso.add("quiver").at(2, 2, 3);
        quiver.rot(0, 0, 20).box(-1.5f, 0, -1, 3, 8, 2).color(RaiderKit.LEATHER).bevel(0.1f);
        for (int i = 0; i < 3; i++) quiver.box(-1 + i, 8, -0.5f, 1, 2, 1).color(RaiderKit.WOOD).flat().px(Face.TOP, 0, 0, RaiderKit.RUBY);
        // crossbow held in both hands: stock rests in right hand, prod at front
        Part bow = m.byName.get("handR").add("crossbow").at(0, -2, 0).rot(160, 0, 0);
        bow.box(-0.8f, -3, -0.8f, 2, 10, 2).color(RaiderKit.WOOD).noise(0.1f);                       // stock (along +y => forward)
        Part prod = bow.add("prod").at(0, 6, 0);
        prod.box(-5.5f, -0.5f, -0.5f, 11, 1, 1).color(0x3a2a1a).bevel(0.1f);
        prod.box(-5.8f, -0.4f, -0.4f, 1, 1, 1).color(RaiderKit.RUBY).glow(Face.FRONT, 0, 0, 1, 1, RaiderKit.RUBY);
        prod.box(4.8f, -0.4f, -0.4f, 1, 1, 1).color(RaiderKit.RUBY).glow(Face.FRONT, 0, 0, 1, 1, RaiderKit.RUBY);
        Part bolt = bow.add("bolt").at(0, 3, 1.1f);
        bolt.box(-0.3f, -1, -0.3f, 1, 8, 1).color(0xd8cfa8).flat();
        bolt.box(-0.7f, 7, -0.7f, 2, 2, 1).color(RaiderKit.STEEL).flat();
        Part crank = bow.add("crank").at(1, 0, 0);
        crank.box(0, -0.5f, -0.5f, 3, 1, 1).color(RaiderKit.STEEL).flat();
        m.byName.get("armL").rot(-70, 0, -15);                                                         // left arm braces the stock forward
        m.byName.get("armR").rot(-70, 0, 8);
        Clip c = m.clip("idle", 44).loop();
        Kit.R(c, "torso", 0, 1.5f, 0, 0, 22, -1.5f, 0, 0, 44, 1.5f, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 12, 0, 8, 0, 34, 0, -8, 0, 44, 0, 0, 0);
        Kit.R(c, "armR", 0, 0, 0, 0, 22, 2, 0, 2, 44, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 22, 2, 0, -2, 44, 0, 0, 0);
        c = m.clip("reload", 34);
        Kit.R(c, "crank", 0, 0, 0, 0, 8, 360, 0, 0, 20, 1080, 0, 0, 34, 1080, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 6, 0, 0, 10, 12, 10, 0, -15, 18, 0, 0, 10, 24, 10, 0, -15, 34, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 8, 10, 0, 0, 20, 12, 0, 0, 34, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 8, 20, 0, 0, 34, 0, 0, 0);
        Kit.S(c, "bolt", 0, 0.1f, 0.1f, 0.1f, 22, 0.1f, 0.1f, 0.1f, 1026, 1, 1, 1, 34, 1, 1, 1);
        Kit.P(c, "prod", 0, 0, 0, 0, 20, 0, -1.5f, 0, 34, 0, 0, 0);
        c = m.clip("fire", 28);
        Kit.R(c, "armR", 0, 0, 0, 0, 8, -10, 0, 0, -12, 10, 0, 0, 14, 25, 0, 0, 28, 0, 0, 0);
        Kit.R(c, "armL", 0, 0, 0, 0, 8, -5, 0, 0, -12, 10, 0, 0, 28, 0, 0, 0);
        Kit.R(c, "torso", 0, 0, 0, 0, 8, 6, -10, 0, -12, -14, 0, 0, 14, -6, 0, 0, 28, 0, 0, 0);
        Kit.R(c, "head", 0, 0, 0, 0, 8, 4, -12, 0, 28, 0, 0, 0);
        Kit.S(c, "bolt", 0, 1, 1, 1, 11, 1, 1, 1, 12, 0.01f, 0.01f, 0.01f, 28, 0.01f, 0.01f, 0.01f);
        Kit.P(c, "body", 0, 0, 0, 0, 12, 0, 0, -2.5f, 28, 0, 0, 0);
        Kit.R(c, "legR", 0, 0, 0, 0, 12, 15, 0, 0, 28, 0, 0, 0);
        Kit.R(c, "legL", 0, 0, 0, 0, 12, -25, 0, 0, 28, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        if (ProjKind.of("s2_raider_bolt") == null) ProjKind.bolt("s2_raider_bolt", 0xd8cfa8, 8, 1).gravity(0.01);
        return MobDef.of("s2_crossbow_raider", "Crossbow Raider", "クロスボウレイダー")
            .game(2, 1).size(0.6f, 1.75f).hp(20).speed(0.25f).style(Style.PILLAGER).egg(0x8a2a30, 0x3a2a24)
            .model(CrossbowRaider::model)
            .spawn(5, 1, 2, MobDef.Habitat.JUNGLE)
            .pattern(Pattern.cycle("reload", "fire"))
            .move(Move.of("reload", 34).intent(Intent.DEFEND).noTarget()
                .sound(8, SoundEvents.CROSSBOW_LOADING_START, 1.0f, 1f).sound(22, SoundEvents.CROSSBOW_LOADING_END, 1.0f, 1f)
                .at(22, (s, t) -> Kit.block(s, 3)))
            .move(Move.of("fire", 28).anim("fire").intent(Intent.ATTACK, 14, 1).ranged(14)
                .sound(12, SoundEvents.CROSSBOW_SHOOT, 1.2f, 1f)
                .at(12, Fx.shoot("s2_raider_bolt", 14, 1.8f, 0, 1.5, 0.6, null)));
    }
}
