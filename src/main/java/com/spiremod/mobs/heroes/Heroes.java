package com.spiremod.mobs.heroes;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Consumer;

/**
 * The playable characters, as Spire creatures that fight with their STARTER DECK + starter relic.
 * StS1 and StS2 versions are separate entries (ids: ironclad / s2_ironclad ...) because their cards differ.
 * Card -> move mapping: Strike 6 dmg, Defend 5 block, then the character's unique starter cards.
 */
public final class Heroes {
    private Heroes() {}

    /** one hero = id + model + def, so Group can iterate and each stays tiny */
    public record Spec(String id, java.util.function.Supplier<ModelSpec> model, java.util.function.Supplier<MobDef> mk) {
        public MobDef def() { return mk.get(); }
    }

    // ---------------------------------------------------------------- shared humanoid rig
    static ModelSpec humanoid(String name, int skin, int cloth, int clothD, int trim, Consumer<Part[]> props) {
        ModelSpec m = new ModelSpec(name);
        for (int s = -1; s <= 1; s += 2) {
            Part leg = m.add(s < 0 ? "legR" : "legL").at(s * 2.2f, 12, 0);
            leg.box(-2, -12, -2, 4, 12, 4).color(clothD).grad(cloth, clothD).bevel(0.12f);
            leg.box(-2.3f, -12, -2.3f, 5, 3, 5).color(0x2a2018);   // boot
        }
        Part torso = m.add("torso").at(0, 12, 0);
        torso.box(-4, 0, -2.5f, 8, 12, 5).color(cloth).grad(cloth, clothD).bevel(0.15f).noise(0.08f);
        torso.box(-4.2f, 0, -2.7f, 8, 2, 5).color(trim);                                      // belt/sash
        Part head = torso.add("head").at(0, 12, 0);
        head.box(-4, 0, -4, 8, 8, 8).color(skin).grad(skin, skin).bevel(0.1f);
        head.box(-3, 3.5f, 3.9f, 2, 1, 1).color(0x101010).flat();
        head.box(1, 3.5f, 3.9f, 2, 1, 1).color(0x101010).flat();
        Part[] arms = new Part[2];
        for (int s = -1; s <= 1; s += 2) {
            Part arm = torso.add(s < 0 ? "armR" : "armL").at(s * 5.5f, 11, 0);
            arm.box(-1.5f, -10, -1.5f, 3, 10, 3).color(cloth).grad(cloth, clothD).bevel(0.12f);
            arm.box(-1.5f, -11.5f, -1.5f, 3, 2, 3).color(skin);
            arms[s < 0 ? 0 : 1] = arm;
        }
        if (props != null) props.accept(new Part[]{torso, head, arms[0], arms[1]});
        m.walkBiped("legR", "legL", "armR", "armL", 32f, 24f);
        m.look("head", 40, 20);
        Clip i = m.clip("idle", 48).loop();
        i.rot("armR", 0, 3, 0, 5).rot("armR", 24, 6, 0, 7).rot("armR", 48, 3, 0, 5);
        i.rot("armL", 0, 3, 0, -5).rot("armL", 24, 6, 0, -7).rot("armL", 48, 3, 0, -5);
        i.pos("torso", 0, 0, 0, 0).pos("torso", 24, 0, 0.5f, 0).pos("torso", 48, 0, 0, 0);
        Clip h = m.clip("hurt", 10);
        h.rot("torso", 0, 0, 0, 0).rot("torso", 3, 18, 0, 6).rot("torso", 10, 0, 0, 0);
        h.rot("head", 0, 0, 0, 0).rot("head", 3, -20, 0, 0).rot("head", 10, 0, 0, 0);
        Clip d = m.clip("death", 30);
        d.rot("torso", 0, 0, 0, 0).rot("torso", 12, -14, 0, 0).rot("torso", 26, 0, 0, 0);
        d.rot("legR", 0, 0, 0, 0).rot("legR", 26, -90, 0, 0, Ease.OUT_BOUNCE);
        d.rot("legL", 0, 0, 0, 0).rot("legL", 26, -90, 0, 0, Ease.OUT_BOUNCE);
        d.pos("torso", 0, 0, 0, 0).pos("torso", 26, 0, -9, 4);
        d.rot("armR", 0, 0, 0, 0).rot("armR", 20, -40, 0, 50).rot("armL", 0, 0, 0, 0).rot("armL", 20, -40, 0, -50);
        // STRIKE: big overhead slash with a body twist
        Clip st = m.clip("strike", 26);
        st.rot("armR", 0, 0, 0, 0).rot("armR", 8, -160, 0, 0).rot("armR", 12, 30, 0, 0, Ease.IN_CUBIC).rot("armR", 16, 10, 0, 0).rot("armR", 26, 0, 0, 0);
        st.rot("torso", 0, 0, 0, 0).rot("torso", 8, -12, -20, 0).rot("torso", 12, 14, 24, 0, Ease.IN_CUBIC).rot("torso", 26, 0, 0, 0);
        st.pos("torso", 8, 0, 0, -1.5f).pos("torso", 12, 0, -0.5f, 2.5f).pos("torso", 26, 0, 0, 0);
        // DEFEND: guard crossed in front, brace
        Clip df = m.clip("defend", 24);
        df.rot("armR", 0, 0, 0, 0).rot("armR", 6, -85, 0, 40, Ease.OUT_BACK).rot("armR", 18, -85, 0, 40).rot("armR", 24, 0, 0, 0);
        df.rot("armL", 0, 0, 0, 0).rot("armL", 6, -85, 0, -40, Ease.OUT_BACK).rot("armL", 18, -85, 0, -40).rot("armL", 24, 0, 0, 0);
        df.rot("torso", 0, 0, 0, 0).rot("torso", 6, 12, 0, 0).rot("torso", 18, 12, 0, 0).rot("torso", 24, 0, 0, 0);
        df.pos("torso", 6, 0, -1, 0).pos("torso", 18, 0, -1, 0).pos("torso", 24, 0, 0, 0);
        // SKILL: arms thrown up, channel, release
        Clip sk = m.clip("skill", 30);
        sk.rot("armR", 0, 0, 0, 0).rot("armR", 8, -150, 0, 25, Ease.OUT_BACK).rot("armR", 22, -150, 0, 25).rot("armR", 30, 0, 0, 0);
        sk.rot("armL", 0, 0, 0, 0).rot("armL", 8, -150, 0, -25, Ease.OUT_BACK).rot("armL", 22, -150, 0, -25).rot("armL", 30, 0, 0, 0);
        sk.rot("torso", 0, 0, 0, 0).rot("torso", 8, -10, 0, 0).rot("torso", 22, -10, 0, 0).rot("torso", 30, 0, 0, 0);
        sk.rot("head", 0, 0, 0, 0).rot("head", 8, -22, 0, 0).rot("head", 22, -22, 0, 0).rot("head", 30, 0, 0, 0);
        // THRUST (jab/stab/cast forward) used by quick signature cards
        Clip th = m.clip("thrust", 20);
        th.rot("armR", 0, 0, 0, 0).rot("armR", 5, -40, 0, 0).rot("armR", 8, -95, 0, 0, Ease.IN_CUBIC).rot("armR", 20, 0, 0, 0);
        th.rot("torso", 0, 0, 0, 0).rot("torso", 5, -8, 14, 0).rot("torso", 8, 12, -10, 0, Ease.IN_CUBIC).rot("torso", 20, 0, 0, 0);
        th.pos("torso", 8, 0, 0, 3).pos("torso", 20, 0, 0, 0);
        return m;
    }

    // ---------------------------------------------------------------- shared move builders
    static Move strike(int dmg) {
        return Move.of("strike", 26).intent(Intent.ATTACK, dmg, 1).reach(2.6f)
            .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1f).at(12, Fx.melee(dmg, 2.8, 100));
    }
    static Move defend(int blk) {
        return Move.of("defend", 24).intent(Intent.DEFEND).noTarget()
            .sound(6, SoundEvents.ARMOR_EQUIP_IRON, 1f, 1f).at(8, Fx.block(blk));
    }
    static MobDef base(String id, String en, String ja, int game, int hp, float speed, Style st, int c1, int c2, java.util.function.Supplier<ModelSpec> model) {
        return MobDef.of(id, en, ja).game(game, 1).size(0.6f, 1.8f).hp(hp).speed(speed).style(st).egg(c1, c2)
            .model(model).spawn(1, 1, 1, MobDef.Habitat.PLAINS);
    }
    static Pattern deck(int strikes, int defends, String... uniq) {
        Pattern.Random r = Pattern.random().add("strike", strikes).add("defend", defends).maxRepeat("defend", 1);
        for (String u : uniq) r.add(u, 1).maxRepeat(u, 1);
        return r;
    }

    // ================================================================ IRONCLAD (80 HP) Burning Blood: heal 6 after combat
    static ModelSpec ironclad(String n, int armor, int trim) {
        return humanoid(n, 0xd2a077, armor, armor & 0x7f7f7f, trim, p -> {
            p[1].box(-4.5f, 5, -4.5f, 9, 4, 9).color(0x606068).bevel(0.2f);                   // helm
            p[1].box(-6.5f, 7, -1, 2, 4, 2).color(0xe8e0c0); p[1].box(4.5f, 7, -1, 2, 4, 2).color(0xe8e0c0);   // horns
            p[2].add("sword").at(0, -11, 0).box(-0.8f, -1, 0, 2, 3, 3).color(0x4a3a2a);
            p[2].box(-0.8f, -12, -1, 2, 2, 14).color(0xc8c8d0).bevel(0.1f);               // blade
            p[0].box(-5, 8, -3, 10, 3, 6).color(0x606068).bevel(0.2f);                          // pauldrons
        });
    }
    static void burningBlood(MobDef d) {
        d.onTick(s -> {
            if (s.getTarget() != null) s.flags.add("fight");
            else if (s.flags.remove("fight")) { s.heal(6); Fx.particles(s, ParticleTypes.FLAME, 8, 0.4); }
        });
    }
    static MobDef ironcladDef(String id, String en, int game, int armor) {
        MobDef d = base(id, en, "アイアンクラッド", game, 80, 0.26f, Style.ZOMBIE, armor, 0x606068, () -> ironclad(id, armor, 0xb02020))
            .pattern(deck(5, 4, "bash"))
            .move(strike(6)).move(defend(5))
            .move(Move.of("bash", 30).anim("strike").intent(Intent.ATTACK_DEBUFF, 8, 1).reach(2.8f)
                .sound(12, SoundEvents.ANVIL_LAND, 0.6f, 1.2f).at(12, Fx.melee(8, 2.8, 100, v -> Fx.vulnerable(v, 6))));
        burningBlood(d);
        return d;
    }

    // ================================================================ SILENT (70 HP) Ring of the Snake: draws extra -> acts faster
    static ModelSpec silent(String n, int cloak, int trim) {
        return humanoid(n, 0xe0b8a0, cloak, cloak & 0x7f7f7f, trim, p -> {
            p[1].box(-4.6f, 2, -4.6f, 10, 8, 10).color(cloak).grad(cloak, cloak & 0x6f6f6f).bevel(0.2f);   // hood
            p[1].box(-3, 1, 4.6f, 6, 4, 1).color(0x101810);                                      // shadowed face
            p[1].box(-2.4f, 3, 5.2f, 2, 1, 1).color(0xff4040).glow(Face.FRONT, 0, 0, 2, 1, 0xff4040);
            p[1].box(0.8f, 3, 5.2f, 2, 1, 1).color(0xff4040).glow(Face.FRONT, 0, 0, 2, 1, 0xff4040);
            p[2].box(-0.6f, -12, 0, 1, 1, 7).color(0xc8d0c8);                              // daggers
            p[3].box(-0.6f, -12, 0, 1, 1, 7).color(0xc8d0c8);
            p[0].box(-4.5f, 0, 2.6f, 9, 12, 1).color(cloak & 0x5f5f5f);                          // back cloak
        });
    }
    static MobDef silentDef(String id, String en, int game, int cloak, boolean s2) {
        return base(id, en, "サイレント", game, 70, 0.32f, Style.SILENT, cloak, 0x1a1a1a, () -> silent(id, cloak, 0x20402a))
            .pattern(deck(5, 5, "neutralize", "survivor"))
            .move(strike(6)).move(defend(5))
            .move(Move.of("neutralize", 20).anim("thrust").intent(Intent.ATTACK_DEBUFF, 3, 1).reach(2.6f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1.6f).at(8, Fx.melee(3, 2.8, 100, v -> Fx.weak(v, 6))))
            .move(Move.of("survivor", 24).anim("defend").intent(Intent.DEFEND).noTarget().at(8, Fx.block(8)));
    }

    // ================================================================ DEFECT (75 HP) Cracked Core: starts with a Lightning orb
    static ModelSpec defect(String n, int body, int glow) {
        return humanoid(n, 0xc0c8d8, body, body & 0x6f6f7f, glow, p -> {
            p[1].box(-3.5f, 0, -3.5f, 7, 7, 7).color(0xd8e0f0).grad(0xf0f4ff, 0xa0a8c0).bevel(0.3f);    // smooth head
            p[1].box(-2.5f, 3, 3.6f, 5, 2, 1).color(glow).glow(Face.FRONT, 0, 0, 5, 2, glow);        // visor
            p[0].box(-2, 4, -3, 4, 4, 1).color(glow).glow(Face.BACK, 0, 0, 4, 4, glow);                 // core
            for (int i = 0; i < 3; i++) p[0].add("orb" + i).at(0, 14, 0).box(-1.5f, -1.5f, -1.5f, 3, 3, 3).color(glow).glow(Face.FRONT, 0, 0, 3, 3, glow);
        });
    }
    static ModelSpec defectModel(String n, int body, int glow) {
        ModelSpec m = defect(n, body, glow);
        m.proc((rig, ctx) -> {
            for (int i = 0; i < 3; i++) {
                double a = ctx.age * 0.15 + i * Math.PI * 2 / 3;
                rig.pos("orb" + i, (float) Math.cos(a) * 9, 4f, (float) Math.sin(a) * 9);
                rig.show("orb" + i, i < ctx.var + 1);
            }
        });
        return m;
    }
    static MobDef defectDef(String id, String en, int game, int body, int glow) {
        return base(id, en, "ディフェクト", game, 75, 0.26f, Style.METAL, body, glow, () -> defectModel(id, body, glow))
            .pattern(deck(4, 4, "zap", "dualcast"))
            .move(strike(6)).move(defend(5))
            .move(Move.of("zap", 24).anim("skill").intent(Intent.BUFF).noTarget()
                .sound(10, SoundEvents.LIGHTNING_BOLT_THUNDER, 0.3f, 2f)
                .at(10, (s, t) -> { s.setCount("orbs", Math.min(3, s.count("orbs") + 1)); s.setVar(s.count("orbs")); Fx.particles(s, ParticleTypes.ELECTRIC_SPARK, 14, 0.5); }))
            .move(Move.of("dualcast", 22).anim("thrust").intent(Intent.ATTACK, 16, 1).ranged(8)
                .sound(8, SoundEvents.LIGHTNING_BOLT_IMPACT, 0.4f, 2f)
                .at(9, (s, t) -> { if (s.count("orbs") > 0) { s.setCount("orbs", s.count("orbs") - 1); s.setVar(s.count("orbs")); Fx.hitTarget(16).run(s, t); } else Fx.hitTarget(8).run(s, t); }))
            .onSpawn(s -> { s.setCount("orbs", 1); s.setVar(1); })
            .onTick(s -> {   // passive: Lightning orbs zap the nearest foe every 2 s
                if (s.tickCount % 40 != 0 || s.count("orbs") == 0 || s.getTarget() == null) return;
                if (s.distanceTo(s.getTarget()) < 8) for (int i = 0; i < s.count("orbs"); i++) Fx.dmg(s, s.getTarget(), 3);
            });
    }

    // ================================================================ WATCHER (72 HP, StS1 only) Pure Water: Miracle; stances
    static ModelSpec watcher(String n) {
        return humanoid(n, 0xe0c0a8, 0x7a3a9a, 0x3e1c52, 0xe8d070, p -> {
            p[1].box(-4.4f, 4.5f, -4.4f, 9, 4, 9).color(0xe8d070).bevel(0.2f);                            // golden headband
            p[1].box(-4.2f, 3, 3.6f, 8, 2, 1).color(0xf0f0f0).flat();                                     // blindfold
            p[0].box(-5, 8, -3, 10, 2, 6).color(0xe8d070).bevel(0.2f);                                    // mantle
            p[0].box(-4.5f, -4, -3, 9, 6, 6).color(0x5a2a74).bevel(0.15f);                                // robe skirt
            p[2].box(-0.6f, -12, 0, 1, 1, 1).color(0xe8d070);
        });
    }
    static MobDef watcherDef() {
        return base("watcher", "Watcher", "ウォッチャー", 1, 72, 0.26f, Style.WITCH, 0x7a3a9a, 0xe8d070, () -> watcher("watcher"))
            .pattern(deck(4, 4, "eruption", "vigilance"))
            .move(Move.of("strike", 26).intent(Intent.ATTACK, 6, 1).reach(2.6f)
                .sound(8, SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1f).at(12, (s, t) -> Fx.meleeNow(s, s.flags.contains("wrath") ? 12 : 6, 2.8, 100, null)))
            .move(defend(5))
            .move(Move.of("eruption", 28).anim("strike").intent(Intent.ATTACK, 9, 1).reach(2.8f)
                .sound(12, SoundEvents.BLAZE_SHOOT, 1f, 0.7f)
                .at(12, (s, t) -> { Fx.meleeNow(s, s.flags.contains("wrath") ? 18 : 9, 2.8, 100, null); s.flags.add("wrath"); Fx.particles(s, ParticleTypes.ANGRY_VILLAGER, 10, 0.5); }))
            .move(Move.of("vigilance", 24).anim("defend").intent(Intent.DEFEND).noTarget()
                .at(8, (s, t) -> { s.addBlock(8); s.flags.remove("wrath"); Fx.particles(s, ParticleTypes.END_ROD, 12, 0.5); }));
    }

    // ================================================================ REGENT (75 HP, StS2) Divine Right: starts with 3 Stars
    static ModelSpec regent(String n) {
        return humanoid(n, 0xe8c8a8, 0xd8b030, 0x6a4a10, 0x4a2a8a, p -> {
            p[1].box(-4.4f, 8, -4.4f, 9, 2, 9).color(0xf0d040).bevel(0.2f);                               // crown band
            for (int i = 0; i < 5; i++) p[1].box(-4 + i * 2, 10, -0.5f, 2, 3, 2).color(0xf0d040);   // crown points
            p[0].box(-5, -2, 2.6f, 10, 14, 1).color(0x4a2a8a).grad(0x6a3ab0, 0x2a1858);                   // royal cape
            p[2].box(-0.7f, -12, -0.7f, 1, 1, 9).color(0xf0d040).bevel(0.1f);                       // sceptre
            for (int i = 0; i < 3; i++) p[0].add("star" + i).at(0, 14, 0).box(-1.2f, -1.2f, -1.2f, 2, 2, 2).color(0xfff0a0).glow(Face.FRONT, 0, 0, 2, 2, 0xfff0a0);
        });
    }
    static MobDef regentDef() {
        ModelSpec[] cache = new ModelSpec[1];
        return base("s2_regent", "The Regent", "リージェント", 2, 75, 0.26f, Style.MAGIC, 0xd8b030, 0x4a2a8a, () -> {
                ModelSpec m = regent("s2_regent");
                m.proc((rig, ctx) -> { for (int i = 0; i < 3; i++) { double a = -ctx.age * 0.12 + i * Math.PI * 2 / 3; rig.pos("star" + i, (float) Math.cos(a) * 8, 2f, (float) Math.sin(a) * 8); rig.show("star" + i, i < Math.max(1, ctx.var)); } });
                return m;
            })
            .pattern(deck(4, 4, "falling_star", "venerate"))
            .onSpawn(s -> { s.setCount("stars", 3); s.setVar(3); })
            .move(strike(6)).move(defend(5))
            .move(Move.of("falling_star", 26).anim("skill").intent(Intent.ATTACK_DEBUFF, 7, 1).ranged(10)
                .sound(14, SoundEvents.AMETHYST_BLOCK_CHIME, 1f, 0.6f)
                .at(14, (s, t) -> { if (t == null) return; Fx.particlesAt(s, ParticleTypes.END_ROD, t.position().add(0, 3, 0), 12, 0.3, 0.2);
                    Fx.dmg(s, t, 7 + s.count("stars")); Fx.weak(t, 4); Fx.vulnerable(t, 4); }))
            .move(Move.of("venerate", 24).anim("skill").intent(Intent.BUFF).noTarget()
                .at(10, (s, t) -> { s.setCount("stars", Math.min(5, s.count("stars") + 2)); s.setVar(Math.min(3, s.count("stars"))); Fx.particles(s, ParticleTypes.END_ROD, 14, 0.6); }));
    }

    // ================================================================ NECROBINDER (66 HP, StS2) Bound Phylactery: summons Osty
    static ModelSpec necro(String n) {
        return humanoid(n, 0xd8d0e0, 0x3a3a52, 0x1a1a2a, 0x9a6ad0, p -> {
            p[1].box(-4.6f, 6, -4.6f, 9, 3, 9).color(0xe8e0f0).bevel(0.2f);                                // bone-white tiara
            p[1].box(-2.4f, 3, 4.1f, 2, 2, 1).color(0x9a6ad0).glow(Face.FRONT, 0, 0, 2, 2, 0x9a6ad0);
            p[1].box(0.8f, 3, 4.1f, 2, 2, 1).color(0x9a6ad0).glow(Face.FRONT, 0, 0, 2, 2, 0x9a6ad0);
            p[0].box(-4.5f, -6, -3, 9, 8, 6).color(0x1a1a2a).bevel(0.15f);                                 // long skirt
            for (int i = 0; i < 4; i++) p[3].box(-1.7f, -9 + i * 2, -1.7f, 3, 1, 3).color(0xe8e0d0);   // left-arm bandages
            p[2].box(-0.6f, -12, 0, 1, 1, 1).color(0x9a6ad0).glow(Face.FRONT, 0, 0, 1, 1, 0x9a6ad0);
        });
    }
    static MobDef necroDef() {
        return base("s2_necrobinder", "The Necrobinder", "ネクロバインダー", 2, 66, 0.26f, Style.WITCH, 0x3a3a52, 0x9a6ad0, () -> necro("s2_necrobinder"))
            .pattern(deck(4, 4, "bodyguard", "unleash"))
            .onSpawn(s -> Fx.summon(s, "s2_osty", 1.2, 0.5))
            .move(strike(6)).move(defend(5))
            .move(Move.of("bodyguard", 26).anim("skill").intent(Intent.SUMMON).noTarget()
                .at(10, (s, t) -> { if (Fx.countAlive(s, "s2_osty", 16) == 0) Fx.summon(s, "s2_osty", 1.2, 0.5); else s.addBlock(7); }))
            .move(Move.of("unleash", 24).anim("thrust").intent(Intent.ATTACK, 6, 1).ranged(8)
                .at(9, (s, t) -> { Fx.hitTarget(6).run(s, t); Fx.particles(s, ParticleTypes.SOUL, 10, 0.5);
                    for (SpireMob o : s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(16))) if (o.def.id.equals("s2_osty") && t != null) { o.setTarget(t); Fx.dmg(o, t, 6); } }));
    }

    public static ModelSpec ostyModel() {
        ModelSpec m = new ModelSpec("s2_osty");
        Part hand = m.add("hand").at(0, 4, 0);
        hand.box(-2.5f, -2, -2.5f, 5, 5, 5).color(0xe8e0d0).bevel(0.3f);
        for (int i = 0; i < 4; i++) { Part f = hand.add("f" + i).at(-2 + i * 1.4f, 3, 0); f.box(-0.5f, 0, -0.5f, 1, 4, 1).color(0xe8e0d0); }
        hand.box(-2.7f, -1, -2.7f, 6, 1, 5).color(0x9a6ad0).glow(Face.TOP, 0, 0, 5, 1, 0x9a6ad0);
        m.bob(1.5f, 0.12f);
        Clip i = m.clip("idle", 30).loop(); i.rot("f0", 0, 0, 0, 0).rot("f0", 15, 25, 0, 0).rot("f0", 30, 0, 0, 0);
        Clip h = m.clip("hurt", 8); h.rot("hand", 0, 0, 0, 0).rot("hand", 3, 25, 0, 15).rot("hand", 8, 0, 0, 0);
        Clip d = m.clip("death", 20); d.rot("hand", 0, 0, 0, 0).rot("hand", 20, 90, 0, 180); d.pos("hand", 0, 0, 0, 0).pos("hand", 20, 0, -4, 0);
        Clip a = m.clip("smack", 20);
        a.rot("hand", 0, 0, 0, 0).rot("hand", 6, -40, 0, 0).rot("hand", 9, 50, 0, 0, Ease.IN_CUBIC).rot("hand", 20, 0, 0, 0);
        a.pos("hand", 9, 0, 0, 4).pos("hand", 20, 0, 0, 0);
        return m;
    }
    static MobDef ostyDef() {
        return MobDef.of("s2_osty", "Osty", "オスティ").game(2, 1).size(0.5f, 0.6f).hp(12).speed(0.34f).flying().minion().style(Style.GHOST)
            .egg(0xe8e0d0, 0x9a6ad0).model(Heroes::ostyModel).pattern(Pattern.cycle("smack"))
            .move(Move.of("smack", 20).intent(Intent.ATTACK, 5, 1).reach(2f).at(9, Fx.melee(5, 2.2, 90)));
    }

    // ---------------------------------------------------------------- registry
    public static final List<Spec> ALL = List.of(
        new Spec("ironclad", () -> ironclad("ironclad", 0x8a2a2a, 0xb02020), () -> ironcladDef("ironclad", "Ironclad (StS1)", 1, 0x8a2a2a)),
        new Spec("silent", () -> silent("silent", 0x2f6a3a, 0x20402a), () -> silentDef("silent", "The Silent (StS1)", 1, 0x2f6a3a, false)),
        new Spec("defect", () -> defectModel("defect", 0x305a9a, 0x40d0ff), () -> defectDef("defect", "The Defect (StS1)", 1, 0x305a9a, 0x40d0ff)),
        new Spec("watcher", () -> watcher("watcher"), Heroes::watcherDef),
        new Spec("s2_ironclad", () -> ironclad("s2_ironclad", 0xa83a2a, 0xe08030), () -> ironcladDef("s2_ironclad", "Ironclad (StS2)", 2, 0xa83a2a)),
        new Spec("s2_silent", () -> silent("s2_silent", 0x3a8a5a, 0x2a5a3a), () -> silentDef("s2_silent", "The Silent (StS2)", 2, 0x3a8a5a, true)),
        new Spec("s2_defect", () -> defectModel("s2_defect", 0x3a6ac0, 0x60e0ff), () -> defectDef("s2_defect", "The Defect (StS2)", 2, 0x3a6ac0, 0x60e0ff)),
        new Spec("s2_regent", () -> regent("s2_regent"), Heroes::regentDef),
        new Spec("s2_necrobinder", () -> necro("s2_necrobinder"), Heroes::necroDef),
        new Spec("s2_osty", Heroes::ostyModel, Heroes::ostyDef)
    );

    // per-class entry points so tools/preview.sh and validate.sh can address each one
    public static final class Ironclad { public static ModelSpec model() { return ALL.get(0).model().get(); } public static MobDef def() { return ALL.get(0).def(); } }
    public static final class Silent { public static ModelSpec model() { return ALL.get(1).model().get(); } public static MobDef def() { return ALL.get(1).def(); } }
    public static final class Defect { public static ModelSpec model() { return ALL.get(2).model().get(); } public static MobDef def() { return ALL.get(2).def(); } }
    public static final class Watcher { public static ModelSpec model() { return ALL.get(3).model().get(); } public static MobDef def() { return ALL.get(3).def(); } }
    public static final class Regent { public static ModelSpec model() { return ALL.get(7).model().get(); } public static MobDef def() { return ALL.get(7).def(); } }
    public static final class Necrobinder { public static ModelSpec model() { return ALL.get(8).model().get(); } public static MobDef def() { return ALL.get(8).def(); } }
}
