package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Devoted Sculptor. 162 HP. Forbidden Incantation (Ritual 9) then Savage (12) every turn. */
public final class DevotedSculptor {
    private DevotedSculptor() {}
    static final int STONE = 0xb9b2a2, STONE_D = 0x7c7668, GOLD = 0xd0a640, BEAK = 0xe2b848, EYE = 0xff9a20;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_devoted_sculptor");
        Kit.biped(m, 0xc9c2b0, STONE, STONE_D, GOLD);
        m.scale(1.35f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.4f, 0, -2.9f, 9, 9, 6).color(STONE).grad(0xd6d0c0, STONE_D).noise(0.12f).bevel(0.15f);   // marble chest
        torso.box(-0.5f, 1, -3f, 1, 8, 1).color(0x5c564a).flat();                                      // crack
        torso.box(-4.5f, 4, -3.2f, 9, 1, 1).color(GOLD).flat();                                        // sash
        body.box(-5, -7, -3.2f, 10, 8, 6).color(STONE_D).grad(STONE, STONE_D).noise(0.12f);            // draped skirt
        head.box(-3.5f, 3, -3.5f, 7, 4, 7).color(STONE_D).grad(STONE, STONE_D);                         // hood
        Part mask = head.add("mask").at(0, 3, 3);
        mask.box(-2.5f, -3, 0, 5, 6, 1).color(0xe9e2c8).bevel(0.12f).glow(Face.FRONT, 0, 1, 1, 1, EYE).glow(Face.FRONT, 4, 1, 1, 1, EYE);
        mask.add("beakU").at(0, 0, 1).box(-1, -1, 0, 2, 2, 3).color(BEAK).bevel(0.1f);
        Part beakL = mask.add("beakL").at(0, -1, 1);
        beakL.box(-1, -1.5f, 0, 2, 1, 2).color(0xb8862a);
        Part f = head.add("feather").at(2, 6, -1).rot(-25, 0, 20);
        f.box(-0.5f, 0, -0.5f, 1, 5, 1).color(0x1d1a2b); f.box(-0.5f, 5, -0.5f, 1, 2, 1).color(0xc2362f).flat();
        Part chisel = m.byName.get("handR").add("chisel").at(0, -2, -1).rot(-90, 0, 0);
        chisel.box(-0.5f, -1, -0.5f, 1, 3, 1).color(0x3b2a1e); chisel.box(-0.5f, 2, -0.4f, 1, 6, 1).color(0xcfd5dc).flat();
        Part mallet = m.byName.get("handL").add("mallet").at(0, -2, -1).rot(-60, 0, 0);
        mallet.box(-0.5f, -2, -0.5f, 1, 7, 1).color(0x5a4636); mallet.box(-2, 5, -1.5f, 4, 3, 3).color(0x6a6458).bevel(0.2f);
        Kit.bipedBase(m);
        Kit.buff(m, "forbidden_incantation", 40, 16);
        m.clips.get("forbidden_incantation").rot("beakL", 0, 0, 0, 0).rot("beakL", 17, -35, 0, 0).rot("beakL", 21, -5, 0, 0).rot("beakL", 25, -35, 0, 0).rot("beakL", 29, -5, 0, 0).rot("beakL", 40, 0, 0, 0);
        Clip c = Kit.sweep(m, "savage", 26, 13);
        c.rot("beakL", 0, 0, 0, 0).rot("beakL", 12, -28, 0, 0).rot("beakL", 20, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_devoted_sculptor", "Devoted Sculptor", "献身の彫刻家")
            .game(2, 3).size(0.9f, 2.4f).hp(162).speed(0.25f).style(Style.BIRD).egg(0xb9b2a2, 0xd0a640)
            .model(DevotedSculptor::model)
            .spawn(6, 1, 1, MobDef.Habitat.MOUNTAIN)
            .pattern(Pattern.seq(1, "forbidden_incantation", "savage"))
            .move(Move.of("forbidden_incantation", 40).intent(Intent.BUFF).noTarget()
                .sound(15, SoundEvents.PARROT_IMITATE_WITCH, 1.4f, 0.6f)
                .at(17, (s, t) -> { Fx.sound(s, SoundEvents.PARROT_IMITATE_PHANTOM, 1.6f, 0.5f); Fx.particles(s, ParticleTypes.WITCH, 25, 0.6); s.flags.add("ritual"); }))
            .move(Move.of("savage", 26).intent(Intent.ATTACK, 12, 1).reach(2.6f)
                .sound(8, SoundEvents.PHANTOM_FLAP, 0.8f, 1.2f)
                .at(13, Fx.melee(12, 3.0, 90)))
            .onMoveEnd(s -> { if (s.flags.contains("ritual") && !s.lastMove().equals("forbidden_incantation")) s.addStrength(9); });
    }
}
