package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Living Shield. 55 HP, Rampart 25 (gains 25 Block each turn). Shield Slam (6) while a Turret Operator lives,
 *  then Smash (16 + 3 Str) every turn. */
public final class LivingShield {
    private LivingShield() {}
    static final int IRON = 0x7e8590, IRON_D = 0x4a4f58, BRASS = 0xc4973a, CLOTH = 0x6a3030;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_living_shield");
        Kit.biped(m, 0x8a7a68, IRON, IRON_D, BRASS);
        m.scale(1.3f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.5f, 0, -3, 9, 9, 6).color(IRON).grad(0x98a0ac, IRON_D).bevel(0.2f);
        body.box(-4.5f, -5, -3, 9, 5, 6).color(CLOTH).grad(0x8a4040, 0x4a2020);
        head.box(-3.5f, 0, -3.5f, 7, 7, 7).color(IRON).grad(0x98a0ac, IRON_D).bevel(0.2f);
        head.box(-2.5f, 2, 3.4f, 5, 1, 1).color(0x111418).glow(Face.FRONT, 0, 0, 5, 1, 0xffb040);
        // tower shield strapped to the left arm
        Part shield = m.byName.get("armL").add("shield").at(2, -4, 2);
        shield.box(-1, -10, -6, 2, 24, 12).color(IRON).grad(0x9aa2ae, IRON_D).bevel(0.3f).noise(0.1f);
        shield.box(-1.4f, -6, -3, 1, 16, 6).color(BRASS).flat().bevel(0.3f);
        shield.box(-1.6f, 1, -1.5f, 1, 3, 3).color(0xe8d070).flat();
        Part club = m.byName.get("handR").add("club").at(0, -2, 0).rot(-80, 0, 0);
        club.box(-0.75f, -2, -0.75f, 2, 6, 2).color(0x5a4636); club.box(-2, 4, -2, 4, 7, 4).color(IRON_D).bevel(0.3f);
        Kit.bipedBase(m);
        Kit.jabs(m, "shield_slam", 30, 13).rot("armL", 0, 0, 0, 0).rot("armL", 9, 20, 0, 5).rot("armL", 13, -95, 0, 5, Ease.IN_CUBIC).rot("armL", 30, 0, 0, 0);
        Kit.overhead(m, "smash", 32, 16);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_living_shield", "Living Shield", "リビングシールド")
            .game(2, 3).size(1.0f, 2.3f).hp(55).speed(0.2f).style(Style.METAL).egg(0x7e8590, 0xc4973a)
            .model(LivingShield::model)
            .spawn(4, 1, 1, MobDef.Habitat.PLAINS)
            .onSpawn(s -> s.addBlock(25))
            .pattern(s -> Fx.countAlive(s, "s2_turret_operator", 24) > 0 ? "shield_slam" : "smash")
            .move(Move.of("shield_slam", 30).intent(Intent.ATTACK, 6, 1).reach(2.6f)
                .sound(12, SoundEvents.SHIELD_BLOCK, 1f, 0.7f).at(13, Fx.melee(6, 2.8, 100)))
            .move(Move.of("smash", 32).intent(Intent.ATTACK_BUFF, 16, 1).reach(3.0f)
                .sound(14, SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.6f)
                .at(16, (s, t) -> { Fx.meleeNow(s, 16, 3.2, 100, null); Kit.str(s, 3); }))
            .onMoveEnd(s -> s.addBlock(25));
    }
}
