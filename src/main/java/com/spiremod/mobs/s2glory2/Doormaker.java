package com.spiremod.mobs.s2glory2;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * StS2 Glory boss (deprecated in the beta, replaced by Aeonglass; kept for completeness): Doormaker, 489 HP.
 * Fight opens with it as an unkillable Door; Dramatic Open reveals it. Then cycle Hunger 30 -> Scrutiny 24 -> Grasp 10x2 (+3 Str).
 * Each move swaps the active curse: Hunger (cards exhaust) / Scrutiny (no extra draw) / Grasp (lose HP per card played).
 * MC translation: Hunger = Hunger effect, Scrutiny = Mining Fatigue, Grasp = chip damage; the curse shown in `var` (0 hunger, 1 scrutiny, 2 grasp).
 */
public final class Doormaker {
    private Doormaker() {}

    static final String ID = "s2_doormaker";
    static final int WOOD = 0x6a4326, WOOD_D = 0x3a2314, IRON = 0x4a4c56, ROBE = 0x1c1626, ROBE_L = 0x30244a, BONE = 0xd8d0c0, PORTAL = 0x7a40ff, GOLD = 0xb89040;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec(ID).scale(1.3f);
        Part all = m.add("all").at(0, 0, 0);
        // doorframe (always there) + glowing void when open
        Part frame = all.add("frame").at(0, 0, -6);
        frame.box(-14, 0, -2, 4, 44, 4).grad(WOOD, WOOD_D).bevel(0.15f).noise(0.1f);
        frame.box(10, 0, -2, 4, 44, 4).grad(WOOD, WOOD_D).bevel(0.15f).noise(0.1f);
        frame.box(-14, 44, -2, 28, 4, 4).color(WOOD).bevel(0.15f).noise(0.1f);
        frame.box(-5, 48, -2, 10, 2, 4).color(GOLD).flat();
        Part portal = all.add("portal").at(0, 0, -5);
        G.fire(portal.box(-10, 0, 0, 20, 44, 1), PORTAL).grad(0xc0a0ff, 0x401890);
        // the door leaf, hinged on the left
        Part leaf = all.add("leaf").at(-10, 0, -3);
        leaf.box(0, 0, 0, 20, 44, 3).color(WOOD).grad(0x7a5030, WOOD_D).noise(0.12f).bevel(0.12f)
            .paint(Face.FRONT, 5, 0, 1, 44, WOOD_D).paint(Face.FRONT, 10, 0, 1, 44, WOOD_D).paint(Face.FRONT, 15, 0, 1, 44, WOOD_D);
        leaf.box(0, 8, 3, 20, 3, 1).color(IRON).flat().noise(0.05f);
        leaf.box(0, 32, 3, 20, 3, 1).color(IRON).flat().noise(0.05f);
        leaf.box(13, 20, 3, 4, 4, 1).color(GOLD).bevel(0.2f);                         // knocker ring
        G.fire(leaf.box(14, 21, 4, 2, 2, 1), 0x2a1040);
        G.fire(leaf.box(9, 24, 3, 2, 3, 1), PORTAL);                                  // keyhole
        // the Doormaker himself: gaunt, long-limbed, rising from the doorway
        Part fig = all.add("figure").at(0, 0, 2);
        fig.box(-8, 0, -5, 16, 5, 10).grad(ROBE_L, ROBE).bevel(0.12f).noise(0.08f);
        fig.box(-6.5f, 5, -4, 13, 6, 8).grad(ROBE_L, ROBE).bevel(0.12f);
        fig.box(-8.2f, 0, -5.2f, 16, 1, 10).inflate(0.3f).color(GOLD).flat();
        Part torso = fig.add("torso").at(0, 11, 0);
        torso.box(-5, 0, -3.5f, 10, 12, 7).grad(ROBE_L, ROBE).bevel(0.1f).noise(0.08f);
        torso.box(-1, 0, 3.4f, 2, 12, 1).color(GOLD).flat();
        for (int i = 0; i < 5; i++) torso.box(-4, 1 + i * 2.2f, 3.5f, 8, 1, 1).color(BONE).flat();   // ribs showing through
        Part head = torso.add("head").at(0, 12, 0);
        head.box(-3.5f, 0, -3.5f, 7, 9, 7).color(BONE).grad(0xe8e0d0, 0xb0a890).bevel(0.15f)
            .paint(Face.FRONT, 0, 0, 7, 1, 0x7a705a);
        G.fire(head.box(-2, 4, 3.4f, 4, 3, 1), PORTAL);                                // keyhole eye
        head.box(-0.5f, 1, 3.5f, 1, 3, 1).color(0x120c18).flat();
        Part eyeG = head.add("eyeG").at(0, 5.5f, 3.9f);
        G.fire(eyeG.box(-3, -0.5f, 0, 6, 1, 1), 0xff4040);                              // scrutiny: glowing slit
        Part jaw = head.add("jaw").at(0, 1, 2);
        jaw.box(-2.5f, -3, -1, 5, 3, 4).color(0xa09880).bevel(0.15f);
        for (int i = -1; i <= 1; i++) jaw.box(i * 1.6f - 0.4f, 0, 2, 1, 2, 1).color(BONE).flat();
        Part mouthG = head.add("mouthG").at(0, 1, 3.6f);
        G.fire(mouthG.box(-2, -1, 0, 4, 2, 1), 0xff7a20);                               // hunger: furnace mouth
        // crown of keys
        for (int i = -2; i <= 2; i++) head.box(i * 1.6f - 0.5f, 9, -0.5f, 1, 5 - Math.abs(i), 1).color(GOLD).flat();
        for (int s = -1; s <= 1; s += 2) {
            String sd = s < 0 ? "R" : "L";
            Part arm = torso.add("arm" + sd).at(s * 6.5f, 10, 0).rot(0, 0, s * -8);
            arm.box(-1.5f, -10, -1.5f, 3, 10, 3).color(ROBE_L).bevel(0.1f);
            Part fore = arm.add("fore" + sd).at(0, -10, 0).rot(-25, 0, 0);
            fore.box(-1.2f, -10, -1.2f, 2, 10, 2).color(BONE).noise(0.1f);
            Part hand = fore.add("hand" + sd).at(0, -10, 0);
            hand.box(-2, -3, -2, 4, 3, 4).color(0xb8b0a0).bevel(0.2f);
            for (int i = -1; i <= 1; i++) { Part f = hand.add("fi" + sd + i).at(i * 1.3f, -3, 0); f.box(-0.4f, -5, -0.5f, 1, 5, 1).color(BONE).flat(); }
            Part gl = hand.add("handG" + sd).at(0, -1.5f, 2.2f);
            G.fire(gl.box(-2, -1, 0, 4, 2, 1), 0xb060ff);                               // grasp: glowing palms
        }
        m.look("head", 40, 25);
        m.proc((r, c) -> {
            boolean open = c.phase >= 1 || ("dramatic_open".equals(c.move) && c.moveT >= 16);
            r.show("figure", open); r.show("portal", c.phase >= 1 || "dramatic_open".equals(c.move));
            if (c.phase >= 1) r.rot("leaf", 0, -115, 0);
            r.show("mouthG", c.var == 0 && open); r.show("eyeG", c.var == 1); r.show("handGR", c.var == 2); r.show("handGL", c.var == 2);
        });
        m.bob(0.5f, 0.06f);
        m.clip("idle", 60).loop()
            .rot("torso", 0, 2, 0, 0).rot("torso", 30, -2, 0, 0).rot("torso", 60, 2, 0, 0)
            .rot("head", 0, 0, 6, 0).rot("head", 30, 0, -6, 0).rot("head", 60, 0, 6, 0)
            .rot("armR", 0, 4, 0, 6).rot("armR", 30, 10, 0, 8).rot("armR", 60, 4, 0, 6)
            .rot("armL", 0, 4, 0, -6).rot("armL", 30, 10, 0, -8).rot("armL", 60, 4, 0, -6);
        m.clip("walk", 30).loop().pos("all", 0, 0, 0.6f, 0).pos("all", 15, 0, 0, 0).pos("all", 30, 0, 0.6f, 0).rot("torso", 0, 6, 0, 0);
        // DRAMATIC OPEN: the door shudders, knocks, bursts open and the figure unfolds into the doorway
        m.clip("dramatic_open", 56)
            .rot("leaf", 0, 0, 0, 0).rot("leaf", 4, 0, -3, 0).rot("leaf", 8, 0, 2, 0).rot("leaf", 12, 0, -4, 0).rot("leaf", 16, 0, 0, 0).rot("leaf", 30, 0, -115, 0, Ease.OUT_BACK).rot("leaf", 56, 0, -115, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 12, 0, 0, 0).pos("all", 30, 0, 0, 2).pos("all", 56, 0, 0, 0)
            .rot("torso", 0, 40, 0, 0).rot("torso", 30, 40, 0, 0).rot("torso", 44, -14, 0, 0, Ease.OUT_BACK).rot("torso", 56, 0, 0, 0)
            .rot("head", 0, 30, 0, 0).rot("head", 30, 30, 0, 0).rot("head", 44, -30, 0, 0, Ease.OUT_BACK).rot("head", 56, 0, 0, 0)
            .rot("armR", 0, 0, 0, 40).rot("armR", 30, 0, 0, 40).rot("armR", 44, -150, 0, 60, Ease.OUT_BACK).rot("armR", 56, 0, 0, 0)
            .rot("armL", 0, 0, 0, -40).rot("armL", 30, 0, 0, -40).rot("armL", 44, -150, 0, -60, Ease.OUT_BACK).rot("armL", 56, 0, 0, 0)
            .scl("portal", 0, 1, 1, 1);
        // HUNGER: lunges and gapes, the furnace mouth swallowing
        m.clip("hunger", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 12, -16, 0, 0).rot("torso", 20, 32, 0, 0, Ease.IN_CUBIC).rot("torso", 32, 26, 0, 0).rot("torso", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 12, -30, 0, 0).rot("head", 20, 22, 0, 0, Ease.IN_CUBIC).rot("head", 40, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 70, 0, 0).rot("jaw", 19, 80, 0, 0).rot("jaw", 22, 0, 0, 0, Ease.IN_CUBIC).rot("jaw", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 12, -100, 0, 40).rot("armR", 20, -70, 0, -20, Ease.IN_CUBIC).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 12, -100, 0, -40).rot("armL", 20, -70, 0, 20, Ease.IN_CUBIC).rot("armL", 40, 0, 0, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 20, 0, 0, 5, Ease.IN_CUBIC).pos("all", 40, 0, 0, 0);
        // SCRUTINY: head swivels to stare, eyes flare, one crushing palm slam
        m.clip("scrutiny", 34)
            .rot("head", 0, 0, 0, 0).rot("head", 8, 10, 30, 0).rot("head", 14, 10, -30, 0).rot("head", 20, 22, 0, 0, Ease.IN_CUBIC).rot("head", 34, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -12, 0, 0).rot("torso", 18, 28, 0, 0, Ease.IN_CUBIC).rot("torso", 34, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -170, 0, 15, Ease.OUT_BACK).rot("armR", 18, -40, 0, 0, Ease.IN_CUBIC).rot("armR", 26, -35, 0, 0).rot("armR", 34, 0, 0, 0)
            .scl("eyeG", 0, 1, 1, 1).scl("eyeG", 12, 2.2f, 3, 1).scl("eyeG", 34, 1, 1, 1);
        // GRASP: two clawing sweeps with glowing palms (hits at 12 and 22), then a triumphant roar
        m.clip("grasp", 40)
            .rot("torso", 0, 0, 0, 0).rot("torso", 8, -8, 40, 0).rot("torso", 12, 12, -30, 0, Ease.IN_CUBIC).rot("torso", 18, -8, -40, 0).rot("torso", 22, 12, 30, 0, Ease.IN_CUBIC).rot("torso", 40, 0, 0, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -110, 20, 70).rot("armR", 12, -100, 0, -50, Ease.IN_CUBIC).rot("armR", 18, -90, 0, 20).rot("armR", 22, -100, 0, -40, Ease.IN_CUBIC).rot("armR", 40, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -110, -20, -70).rot("armL", 12, -100, 0, 50, Ease.IN_CUBIC).rot("armL", 18, -90, 0, -20).rot("armL", 22, -100, 0, 40, Ease.IN_CUBIC).rot("armL", 40, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 28, -20, 0, 0).rot("head", 40, 0, 0, 0).rot("jaw", 0, 0, 0, 0).rot("jaw", 28, 50, 0, 0).rot("jaw", 40, 0, 0, 0)
            .pos("all", 0, 0, 0, 0).pos("all", 12, 0, 0, 2).pos("all", 22, 0, 0, 3).pos("all", 40, 0, 0, 0);
        m.clip("hurt", 10).rot("torso", 0, 0, 0, 0).rot("torso", 3, -12, 0, 4).rot("torso", 10, 0, 0, 0).rot("head", 0, 0, 0, 0).rot("head", 3, -20, 8, 0).rot("head", 10, 0, 0, 0);
        m.clip("death", 50)
            .rot("torso", 0, 0, 0, 0).rot("torso", 16, -20, 0, 0).rot("torso", 50, 60, 0, 0, Ease.OUT_BOUNCE)
            .rot("head", 0, 0, 0, 0).rot("head", 16, -40, 0, 0).rot("head", 50, 40, 10, 0)
            .rot("armR", 0, 0, 0, 0).rot("armR", 16, -150, 0, 40).rot("armR", 50, 0, 0, 70).rot("armL", 0, 0, 0, 0).rot("armL", 16, -150, 0, -40).rot("armL", 50, 0, 0, -70)
            .rot("leaf", 0, 0, 0, 0).rot("leaf", 50, 0, 100, 0)
            .scl("portal", 0, 1, 1, 1).scl("portal", 50, 1, 0.05f, 1)
            .pos("figure", 0, 0, 0, 0).pos("figure", 50, 0, -22, 0, Ease.IN_QUAD);
        return m;
    }

    public static MobDef def() {
        return MobDef.of(ID, "Doormaker", "ドアメーカー")
            .game(2, 3).size(1.8f, 4.0f).hp(489).speed(0.2f).style(Style.GHOST).egg(0x6a4326, 0x7a40ff).boss().kbResist(1f)
            .model(Doormaker::model)
            .pattern(s -> {
                switch (s.lastMove()) {
                    case "": return "dramatic_open";
                    case "dramatic_open": case "grasp": return "hunger";
                    case "hunger": return "scrutiny";
                    default: return "grasp";
                }
            })
            .onHurt((s, src, dmg) -> s.getPhase() == 0 && !src.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY) ? 0f : dmg)   // the Door cannot be hurt
            .onMoveEnd(s -> {
                switch (s.lastMove()) {                                                 // each move swaps the active curse
                    case "dramatic_open": s.setPhase(1); s.setVar(0); break;
                    case "hunger": s.setVar(1); break;
                    case "scrutiny": s.setVar(2); break;
                    case "grasp": s.setVar(0); break;
                }
            })
            .onTick(s -> {
                if (s.getPhase() < 1 || s.tickCount % 40 != 0) return;
                for (var v : Fx.foes(s, s.getBoundingBox().inflate(18, 6, 18))) {
                    switch (s.getVar()) {
                        case 0: v.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 1)); break;
                        case 1: v.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 1)); break;
                        default: Fx.raw(s, v, 1f); break;
                    }
                    Fx.particlesAt(s, ParticleTypes.PORTAL, v.position().add(0, 1, 0), 10, 0.4, 0.2);
                }
            })
            .move(Move.of("dramatic_open", 56).intent(Intent.UNKNOWN).noTarget()
                .sound(8, SoundEvents.WOODEN_DOOR_OPEN, 2f, 0.5f).sound(30, SoundEvents.IRON_DOOR_OPEN, 2f, 0.5f)
                .at(30, (s, t) -> { Fx.particles(s, ParticleTypes.REVERSE_PORTAL, 60, 1.5); Fx.sound(s, SoundEvents.ENDERMAN_SCREAM, 1.5f, 0.4f); }))
            .move(Move.of("hunger", 40).intent(Intent.ATTACK, 30, 1).reach(3.2f)
                .sound(10, SoundEvents.RAVAGER_ROAR, 1.2f, 0.5f)
                .at(21, (s, t) -> { Fx.meleeNow(s, 30, 3.6, 100, v -> Fx.knock(s, v, 0.8)); Fx.sound(s, SoundEvents.GENERIC_EAT, 1.5f, 0.5f); Fx.particles(s, ParticleTypes.FLAME, 14, 0.6); }))
            .move(Move.of("scrutiny", 34).intent(Intent.ATTACK, 24, 1).reach(3.2f)
                .sound(8, SoundEvents.ELDER_GUARDIAN_CURSE, 1f, 0.8f)
                .at(19, (s, t) -> { Fx.meleeNow(s, 24, 3.6, 100, v -> Fx.knock(s, v, 0.6)); Fx.sound(s, SoundEvents.ANVIL_LAND, 0.8f, 0.5f); }))
            .move(Move.of("grasp", 40).intent(Intent.ATTACK_BUFF, 10, 2).reach(3.4f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.6f)
                .at(12, G.cone(10, 3.8, 140)).at(22, G.cone(10, 3.8, 140))
                .at(28, (s, t) -> { s.addStrength(3); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 10, 0.8); Fx.sound(s, SoundEvents.RAVAGER_ROAR, 1.2f, 0.9f); }))
            .onClientTick(s -> {
                if (s.getPhase() >= 1 && s.tickCount % 3 == 0) s.level().addParticle(ParticleTypes.PORTAL, s.getX() + (s.getRandom().nextDouble() - .5) * 1.2, s.getY() + 1 + s.getRandom().nextDouble() * 2.5, s.getZ() - 0.5, 0, 0.02, 0);
            });
    }
}
