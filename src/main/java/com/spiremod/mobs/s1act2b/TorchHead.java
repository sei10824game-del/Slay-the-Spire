package com.spiremod.mobs.s1act2b;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

/** StS1 Act 2 Collector minion: Torch Head. 38-40 HP, Tackle 7 every turn. A living torch: wooden stick body, burning cage head. */
public final class TorchHead {
    private TorchHead() {}
    static final int WOOD = 0x6a4426, WOOD_D = 0x3e2614, IRON = 0x4a4a52, FIRE = 0xff9a20, FIRE_Y = 0xffe060;

    public static ModelSpec model() {
        ModelSpec m = new ModelSpec("torch_head");
        m.scale(1.0f);
        Part body = m.add("body").at(0, 8, 0);
        body.box(-1.5f, -7, -1.5f, 3, 14, 3).color(WOOD).grad(WOOD, WOOD_D).noise(0.12f).bevel(0.1f);
        body.box(-2, -3, -2, 4, 1, 4).color(IRON).flat();
        body.box(-2, 3, -2, 4, 1, 4).color(IRON).flat();
        Part head = body.add("head").at(0, 7, 0);
        head.box(-3, 0, -3, 6, 2, 6).color(IRON).grad(0x6a6a72, IRON).bevel(0.2f);   // cup
        head.box(-3.4f, 2, -3.4f, 1, 4, 1).color(IRON).flat();
        head.box(2.4f, 2, -3.4f, 1, 4, 1).color(IRON).flat();
        head.box(-3.4f, 2, 2.4f, 1, 4, 1).color(IRON).flat();
        head.box(2.4f, 2, 2.4f, 1, 4, 1).color(IRON).flat();
        Part flame = head.add("flame").at(0, 2, 0);
        flame.box(-2.5f, 0, -2.5f, 5, 4, 5).color(FIRE).grad(FIRE_Y, 0xe0501a).glow(Face.FRONT, 0, 0, 5, 4, FIRE).flat();
        flame.box(-1.5f, 4, -1.5f, 3, 3, 3).color(FIRE_Y).flat().glow(Face.FRONT, 0, 0, 3, 3, FIRE_Y);
        flame.box(-0.5f, 7, -0.5f, 1, 2, 1).color(FIRE_Y).flat().glow(Face.FRONT, 0, 0, 1, 2, 0xffffff);
        Part eyes = head.add("eyes").at(0, 3, 2.5f);
        eyes.box(-2, 0, 0, 1, 1, 1).color(0x201000).flat().glow(Face.FRONT, 0, 0, 1, 1, 0xffffff);
        eyes.box(1, 0, 0, 1, 1, 1).color(0x201000).flat().glow(Face.FRONT, 0, 0, 1, 1, 0xffffff);
        for (int s = -1; s <= 1; s += 2) {
            Part arm = body.add(s < 0 ? "armR" : "armL").at(s * 2f, 3, 0);
            arm.box(-0.5f, -5, -0.5f, 1, 5, 1).color(WOOD_D).noise(0.1f);
            arm.box(-0.5f, -6, -0.5f, 1, 1, 1).color(IRON).flat();
        }
        m.bob(1.2f, 0.12f);
        m.look("head", 40, 25);
        m.clip("idle", 30).loop()
            .rot("flame", 0, 0, 0, 3).rot("flame", 10, 0, 0, -3).rot("flame", 20, 0, 0, 3).rot("flame", 30, 0, 0, 3)
            .scl("flame", 0, 1, 1, 1).scl("flame", 8, 1.05f, 1.15f, 1.05f).scl("flame", 16, 0.95f, 0.95f, 0.95f).scl("flame", 30, 1, 1, 1)
            .rot("armR", 0, 5, 0, 10).rot("armR", 15, 12, 0, 14).rot("armR", 30, 5, 0, 10)
            .rot("armL", 0, 5, 0, -10).rot("armL", 15, 12, 0, -14).rot("armL", 30, 5, 0, -10);
        m.clip("walk", 20).loop()
            .rot("body", 0, 8, 0, 0).rot("body", 10, 14, 0, 0).rot("body", 20, 8, 0, 0)
            .rot("armR", 0, 30, 0, 0).rot("armR", 10, -30, 0, 0).rot("armR", 20, 30, 0, 0)
            .rot("armL", 0, -30, 0, 0).rot("armL", 10, 30, 0, 0).rot("armL", 20, -30, 0, 0);
        // TACKLE: lean back, flame flares, ram forward head-first, bounce off
        m.clip("tackle", 26)
            .rot("body", 0, 0, 0, 0).rot("body", 8, -25, 0, 0, Ease.OUT_CUBIC).rot("body", 12, 55, 0, 0, Ease.IN_CUBIC).rot("body", 18, 40, 0, 0).rot("body", 26, 0, 0, 0)
            .pos("body", 0, 0, 0, 0).pos("body", 8, 0, 0, -2).pos("body", 12, 0, -1, 5, Ease.IN_CUBIC).pos("body", 26, 0, 0, 0)
            .scl("flame", 0, 1, 1, 1).scl("flame", 8, 1.3f, 1.4f, 1.3f).scl("flame", 12, 0.8f, 1.6f, 0.8f).scl("flame", 26, 1, 1, 1)
            .rot("armR", 0, 0, 0, 0).rot("armR", 8, -90, 0, 40).rot("armR", 12, 30, 0, 10).rot("armR", 26, 0, 0, 0)
            .rot("armL", 0, 0, 0, 0).rot("armL", 8, -90, 0, -40).rot("armL", 12, 30, 0, -10).rot("armL", 26, 0, 0, 0)
            .rot("head", 0, 0, 0, 0).rot("head", 8, -20, 0, 0).rot("head", 12, 25, 0, 0).rot("head", 26, 0, 0, 0);
        m.clip("hurt", 10).rot("body", 0, 0, 0, 0).rot("body", 3, -20, 0, 8).rot("body", 10, 0, 0, 0);
        m.clip("death", 24)
            .rot("body", 0, 0, 0, 0).rot("body", 14, 0, 0, 90, Ease.IN_QUAD).rot("body", 24, 0, 0, 92)
            .pos("body", 0, 0, 0, 0).pos("body", 14, 0, -6, 0, Ease.IN_QUAD).pos("body", 24, 0, -6, 0)
            .scl("flame", 0, 1, 1, 1).scl("flame", 14, 1.2f, 1.2f, 1.2f).scl("flame", 24, 0.1f, 0.1f, 0.1f);
        return m;
    }

    public static MobDef def() {
        return MobDef.of("torch_head", "Torch Head", "トーチヘッド")
            .game(1, 2).size(0.6f, 1.1f).hp(39).speed(0.24f).style(Style.GHOST).egg(0xff9a20, 0x6a4426).minion().flying()
            .model(TorchHead::model)
            .pattern(Pattern.cycle("tackle"))
            .move(Move.of("tackle", 26).intent(Intent.ATTACK, 7, 1).reach(2.2f)
                .sound(8, SoundEvents.BLAZE_SHOOT, 0.7f, 1.5f)
                .at(12, Fx.melee(7, 2.6, 70)))
            .onClientTick(s -> { if (s.tickCount % 3 == 0) s.level().addParticle(ParticleTypes.FLAME, s.getX(), s.getY() + s.getBbHeight() + 0.1, s.getZ(), 0, 0.02, 0); });
    }
}
