package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Turret Operator. 41 HP. Cycle: Unload! (3 x5), Unload! (3 x5), Loading (+1 Str). */
public final class TurretOperator {
    private TurretOperator() {}
    static final int COAT = 0x8a5a2e, COAT_D = 0x5a3a1e, STEEL = 0x6e7680, GOG = 0x9fd8e8, SKIN = 0xd0a07a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_turret_operator");
        Kit.biped(m, SKIN, COAT, COAT_D, 0xd0a640);
        m.scale(1.0f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head");
        torso.box(-4, 0, -2.5f, 8, 9, 5).color(COAT).grad(0xa0703c, COAT_D);
        torso.box(-4.4f, 3, -2.9f, 9, 1, 6).color(0x2a1e14).flat();                                   // ammo belt
        for (int i = 0; i < 4; i++) torso.box(-3.5f + i * 2, 4, -3.1f, 1, 3, 1).color(0xe0c060).flat();
        head.box(-3.5f, 3.5f, -3.5f, 7, 3, 7).color(STEEL).bevel(0.2f);                              // helmet
        head.box(-3, 1.5f, 3, 6, 2, 1).color(0x1a2a30).glow(Face.FRONT, 0, 0, 6, 2, GOG);            // goggles
        Part gun = m.byName.get("armR").add("gun").at(0, -5, 2).rot(-90, 0, 0);
        gun.box(-2, -2, -2, 4, 5, 4).color(STEEL).bevel(0.2f);
        Part barrels = gun.add("barrels").at(0, 0.5f, 3);
        barrels.box(-1.5f, -1.5f, 0, 3, 3, 10).color(0x40464e).bevel(0.2f);
        barrels.box(-2, -2, 9, 4, 4, 2).color(0xd0a640).flat();
        barrels.box(-1, -1, 11, 2, 2, 1).color(0x201010).glow(Face.FRONT, 0, 0, 2, 2, 0xff8030);
        gun.box(-2.5f, -3, -1, 5, 3, 5).color(COAT_D);                                                 // ammo drum
        Kit.bipedBase(m);
        m.proc((r, c) -> { if (c.moveT > 8 && c.moveT < 28 && "unload".equals(c.move)) r.rot("barrels", 0, 0, c.age * 60); });
        Clip c = Kit.cast(m, "unload", 32, 9);
        Kit.seq(c, "armL", 32, -1, 6, -40, 0, -20, 9, -75, 0, -25, 26, -75, 0, -25);
        for (int t = 10; t <= 22; t += 3) c.rot("torso", t, -4, 4, 0).rot("torso", t + 1, -7, -3, 0);
        Clip l = m.clip("loading", 32);
        Kit.seq(l, "armR", 32, -1, 6, -40, 10, 0, 16, -60, 10, 5, 26, -40, 10, 0);
        Kit.seq(l, "armL", 32, -1, 6, -60, -20, -10, 12, -80, -20, 0, 18, -50, -20, -10, 26, -70, -20, 0);
        Kit.seq(l, "torso", 32, -1, 8, 14, 0, 0, 22, 14, 0, 0);
        Kit.seq(l, "head", 32, -1, 8, 25, 0, 0, 22, 25, 0, 0);
        return m;
    }

    public static MobDef def() {
        MobDef d = MobDef.of("s2_turret_operator", "Turret Operator", "タレットオペレーター")
            .game(2, 3).size(0.6f, 1.75f).hp(41).speed(0.22f).style(Style.PILLAGER).egg(0x8a5a2e, 0x6e7680)
            .model(TurretOperator::model)
            .spawn(5, 1, 1, MobDef.Habitat.PLAINS)
            .pattern(Pattern.cycle("unload", "unload", "loading"));
        Move.Action shot = (s, t) -> {
            Kit.zap(3).run(s, t);
            Fx.sound(s, SoundEvents.CROSSBOW_SHOOT, 0.5f, 1.8f);
            Fx.particlesAt(s, ParticleTypes.CRIT, Fx.rel(s, 1.6, 0, 1.2), 3, 0.1, 0.05);
        };
        Move u = Move.of("unload", 32).intent(Intent.ATTACK, 3, 5).ranged(14);
        for (int t = 10; t <= 22; t += 3) u.at(t, shot);
        return d.move(u)
            .move(Move.of("loading", 32).intent(Intent.BUFF).noTarget()
                .sound(12, SoundEvents.CROSSBOW_LOADING_END, 1f, 0.8f).at(20, (s, t) -> Kit.str(s, 1)));
    }
}
