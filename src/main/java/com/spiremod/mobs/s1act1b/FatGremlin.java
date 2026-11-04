package com.spiremod.mobs.s1act1b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 1 Fat Gremlin: 13-17 HP. Smash (4 dmg + 1 Weak) every turn. Pot-bellied, hoists a huge plank mallet. */
public final class FatGremlin {
    private FatGremlin() {}
    static final int SKIN = 0xc4532f, DARK = 0x8a2f1c, HORN = 0x5cc9a4, EYE = 0xffe27a, WOOD = 0xbdb59a, WOOD_D = 0x8f8670;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("fat_gremlin");
        GremlinKit.build(m, SKIN, DARK, HORN, EYE, true);
        // rest pose: both arms raised, hammer hoisted overhead
        m.byName.get("armR").rot(-165, 0, -15);
        m.byName.get("armL").rot(-165, 0, 15);
        Part hammer = m.byName.get("handR").add("hammer").at(-1.5f, -1.5f, 0).rot(0, 0, 15);
        hammer.box(-0.5f, -15, -0.5f, 1, 17, 1).color(WOOD).grad(WOOD, WOOD_D).noise(0.08f);          // long pole
        hammer.box(-5, -19, -1.5f, 10, 4, 3).color(WOOD).grad(0xd4ccb0, WOOD_D).bevel(0.12f).noise(0.12f) // plank head
            .paint(Face.FRONT, 0, 1, 10, 1, WOOD_D).paint(Face.BACK, 0, 2, 10, 1, WOOD_D)
            .px(Face.FRONT, 0, 0, 0xf0ead8).px(Face.FRONT, 9, 3, 0xf0ead8);
        hammer.box(-1, -16, -1, 2, 2, 2).color(0x6a5a40);                                               // lashing
        GremlinKit.stdClips(m);
        // SMASH: heave the mallet back behind the head, hop, then slam it down in front with the whole body
        m.clip("smash", 30)
            .rot("armR", 0, 0, 0, 0).rot("armR", 10, -40, 0, 0, Ease.OUT_QUAD).rot("armR", 15, 88, 0, 10, Ease.IN_CUBIC).rot("armR", 21, 84, 0, 10).rot("armR", 30, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 10, -40, 0, 0, Ease.OUT_QUAD).rot("armL", 15, 88, 0, -10, Ease.IN_CUBIC).rot("armL", 21, 84, 0, -10).rot("armL", 30, 0, 0, 0)
            .rot("torso", 0, 0, 0, 0).rot("torso", 10, -18, 0, 0).rot("torso", 15, 22, 0, 0, Ease.IN_CUBIC).rot("torso", 21, 18, 0, 0).rot("torso", 30, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 6, 0, -1.5f, 0).pos("body", 11, 0, 2.5f, 0, Ease.OUT_QUAD).pos("body", 15, 0, -1.5f, 1.5f, Ease.IN_CUBIC).pos("body", 18, 0, -1f, 1.5f, Ease.OUT_BOUNCE).pos("body", 30, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 10, -20, 0, 0).rot("head", 15, 15, 0, 0).rot("head", 30, 0, 0, 0)
            .rot("jaw", 0, 0, 0, 0).rot("jaw", 12, 0, 0, 0).rot("jaw", 15, 35, 0, 0).rot("jaw", 22, 30, 0, 0).rot("jaw", 28, 0, 0, 0)
            .rot("legR", 0, 0, 0, 0).rot("legR", 15, -20, 0, -10).rot("legR", 30, 0, 0, 0)
            .rot("legL", 0, 0, 0, 0).rot("legL", 15, 15, 0, 10).rot("legL", 30, 0, 0, 0)
            .rot("hammer", 0, 0, 0, 0).rot("hammer", 15, 0, 0, 0).rot("hammer", 17, 8, 0, 0).rot("hammer", 19, -4, 0, 0).rot("hammer", 22, 0, 0, 0);
        m.clip("hurt", 10)
            .rot("torso", 0, 0, 0, 0).rot("torso", 3, -15, 0, 8).rot("torso", 10, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 3, -20, 10, 0).rot("head", 10, 0, 0, 0)
            .rot("hammer", 0, 0, 0, 0).rot("hammer", 3, 0, 0, -12).rot("hammer", 10, 0, 0, 0);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("fat_gremlin", "Fat Gremlin", "ファットグレムリン")
            .game(1, 1).size(0.8f, 1.1f).hp(15).speed(0.24f).style(Style.PILLAGER).egg(0xc4532f, 0x5cc9a4)
            .model(FatGremlin::model).spawn(4, 1, 3, MobDef.Habitat.FOREST)
            .pattern(Pattern.cycle("smash"))
            .move(Move.of("smash", 30).intent(Intent.ATTACK_DEBUFF, 4, 1).reach(2.6f)
                .sound(9, SoundEvents.PLAYER_ATTACK_SWEEP, 0.7f, 0.7f)
                .at(15, Fx.melee(4, 3.0, 80, v -> Fx.weak(v, 5)))
                .at(15, (s, t) -> { Fx.sound(s, SoundEvents.ANVIL_LAND, 0.5f, 1.3f); Fx.ring(s, ParticleTypes.CLOUD, Fx.rel(s, 1.6, 0, 0), 0.6, 10); }));
    }
}
