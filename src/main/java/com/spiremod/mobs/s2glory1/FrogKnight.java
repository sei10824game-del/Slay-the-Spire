package com.spiremod.mobs.s2glory1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS2 Glory: Frog Knight. 191 HP, Plating 15 (block that shrinks 1 per turn). Tongue Lash 13 + 2 Frail, Strike Down Evil 21,
 *  For the Queen +5 Str, Beetle Charge 35 (once, below half HP). */
public final class FrogKnight {
    private FrogKnight() {}
    static final int FROG = 0x4f9a3c, FROG_D = 0x2f6a2a, BELLY = 0xcfe0a0, PLATE = 0xaeb6c2, PLATE_D = 0x6e7886, GOLD = 0xd6a936, TONGUE = 0xd8506a;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("s2_frog_knight");
        Kit.biped(m, FROG, PLATE, FROG_D, GOLD);
        m.scale(1.3f);
        Part torso = m.byName.get("torso"), head = m.byName.get("head"), body = m.byName.get("body");
        torso.box(-4.5f, 0, -3, 9, 9, 6).color(PLATE).grad(0xcfd6e0, PLATE_D).bevel(0.2f);
        torso.box(-3.5f, -1, -2.8f, 7, 2, 5).color(BELLY).noise(0.06f);
        torso.box(-1, 2, -3.2f, 2, 6, 1).color(GOLD).flat();                                                  // crest stripe
        body.box(-4.5f, -3, -3, 9, 3, 6).color(PLATE_D).bevel(0.2f);
        head.box(-4.5f, 0, -3.5f, 9, 5, 7).color(FROG).grad(0x62b44c, FROG_D).bevel(0.1f);                    // wide frog head
        head.box(-4.5f, 3, -1, 3, 3, 3).color(FROG).glow(Face.FRONT, 1, 1, 1, 1, 0xfff070);                  // bulging eye R
        head.box(1.5f, 3, -1, 3, 3, 3).color(FROG).glow(Face.FRONT, 1, 1, 1, 1, 0xfff070);                   // eye L
        head.box(-4, 4.5f, -4, 8, 3, 8).color(PLATE).grad(0xcfd6e0, PLATE_D).bevel(0.2f);                    // helmet
        head.box(-0.5f, 7.5f, -3, 1, 4, 6).color(0xc2362f).flat();                                           // plume
        head.box(-3, 0.5f, 3.4f, 6, 1, 1).color(0x2a4a20).flat();                                             // mouth line
        Part tongue = head.add("tongue").at(0, 1.2f, 3.5f).scale(1, 1, 0.12f);
        tongue.box(-1, -0.5f, 0, 2, 1, 16).color(TONGUE).flat().glow(Face.TOP, 0, 0, 2, 16, 0xff7a90);
        Part sword = m.byName.get("handR").add("sword").at(0, -2, 0).rot(-80, 0, 0);
        sword.box(-0.5f, -2, -0.5f, 1, 4, 1).color(0x5a4636); sword.box(-2, 2, -0.5f, 4, 1, 1).color(GOLD).flat();
        sword.box(-1, 3, -0.3f, 2, 14, 1).color(0xdfe6ee).flat().noise(0.03f);
        Part shield = m.byName.get("handL").add("shield").at(0, -1, 0);
        shield.box(-1, -5, -4, 2, 10, 8).color(PLATE).bevel(0.3f); shield.box(-1.3f, -2, -1, 1, 4, 2).color(GOLD).flat();
        Kit.bipedBase(m);
        Clip c = m.clip("tongue_lash", 28);
        Kit.seq(c, "head", 28, 11, 8, 8, 0, 0, 11, -12, 0, 0, 18, -5, 0, 0);
        c.scl("tongue", 0, 1, 1, 1).scl("tongue", 8, 1, 1, 1).scl("tongue", 11, 1, 1, 8.5f, Ease.IN_CUBIC).scl("tongue", 17, 1, 1, 8.5f).scl("tongue", 22, 1, 1, 1);
        Kit.seq(c, "torso", 28, 11, 8, -10, 0, 0, 11, 12, 0, 0);
        Kit.seq(c, "armR", 28, 11, 8, 10, 0, 20, 11, -25, 0, 10);
        Kit.overhead(m, "strike_down_evil", 32, 17);
        Kit.buff(m, "for_the_queen", 40, 16);
        Clip ch = m.clip("beetle_charge", 40);
        Kit.seq(ch, "torso", 40, 18, 8, -18, 0, 0, 18, 40, 0, 0, 30, 30, 0, 0);
        Kit.seq(ch, "armR", 40, 18, 8, 40, 0, 20, 18, -150, 0, 0, 30, -140, 0, 0);
        Kit.seq(ch, "armL", 40, 18, 8, 40, 0, -20, 18, -40, 0, -20);
        Kit.seq(ch, "legR", 40, -1, 8, 30, 0, 0, 18, -60, 0, 0, 24, 40, 0, 0, 30, -40, 0, 0);
        Kit.seq(ch, "legL", 40, -1, 8, -20, 0, 0, 18, 55, 0, 0, 24, -45, 0, 0, 30, 30, 0, 0);
        Kit.sp(ch, "body", 40, 18, 8, 0, -3, -3, 18, 0, 2, 9, 30, 0, 0, 12);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("s2_frog_knight", "Frog Knight", "フロッグナイト")
            .game(2, 3).size(1.0f, 2.2f).hp(191).speed(0.24f).style(Style.FROG).egg(0x4f9a3c, 0xaeb6c2)
            .model(FrogKnight::model)
            .spawn(5, 1, 1, MobDef.Habitat.SWAMP)
            .onSpawn(s -> { s.setCount("plate", 15); s.addBlock(15); })
            .pattern(s -> {
                if (s.getTurn() == 0) return "tongue_lash";
                switch (s.lastMove()) {
                    case "tongue_lash": return "strike_down_evil";
                    case "strike_down_evil": return "for_the_queen";
                    case "for_the_queen": return s.getHealth() < s.getMaxHealth() / 2 && s.count("beetle") == 0 ? "beetle_charge" : "tongue_lash";
                    default: return "tongue_lash";
                }
            })
            .move(Move.of("tongue_lash", 28).intent(Intent.ATTACK_DEBUFF, 13, 1).reach(5.0f)
                .sound(10, SoundEvents.FROG_TONGUE, 1.2f, 0.9f)
                .at(12, Fx.hitTarget(13, v -> Fx.frail(v, 2 * Kit.TURN))))
            .move(Move.of("strike_down_evil", 32).intent(Intent.ATTACK, 21, 1).reach(3.0f)
                .sound(14, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.6f)
                .at(17, Fx.melee(21, 3.3, 100)))
            .move(Move.of("for_the_queen", 40).intent(Intent.BUFF).noTarget()
                .sound(14, SoundEvents.FROG_AMBIENT, 1.6f, 0.5f)
                .at(16, (s, t) -> { Kit.str(s, 5); Fx.particles(s, ParticleTypes.CRIT, 15, 0.6); }))
            .move(Move.of("beetle_charge", 40).intent(Intent.ATTACK, 35, 1).reach(8.0f).moving()
                .sound(10, SoundEvents.RAVAGER_ROAR, 0.8f, 1.3f)
                .at(16, Fx.dash(0.9, 0.1)).at(19, Fx.melee(35, 3.6, 110)))
            .onMoveEnd(s -> {
                if (s.lastMove().equals("beetle_charge")) s.inc("beetle");
                int p = Math.max(0, s.count("plate") - 1); s.setCount("plate", p);
                if (p > 0) s.addBlock(p);
            });
    }
}
