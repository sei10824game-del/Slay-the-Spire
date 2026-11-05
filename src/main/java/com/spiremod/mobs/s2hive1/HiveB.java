package com.spiremod.mobs.s2hive1;

import com.spiremod.core.*;
import com.spiremod.spec.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

/** Remaining StS2 Hive normals (stats/patterns from the wiki). Each nested class = model() + def(). */
public final class HiveB {
    private HiveB() {}
    static final MobDef.Habitat H = MobDef.Habitat.CAVE;

    // ------------------------------------------------------------------ Exoskeleton: 26 HP, Hard to Kill 9
    public static final class Exoskeleton {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_exoskeleton", 8, 6, 12, 5, 3, 0xc9b27a, 0x7a6234, 0xe8d6a0, 0x4a3a1c, p ->
                p[0].box(-3, 3, -5, 6, 2, 10).color(0xe8d6a0).bevel(0.2f));
            Bug.attack(m, "skitter", 26, 6, 9, 3, 5); Bug.attack(m, "mandibles", 28, 9, 13, 1, 0); Bug.rear(m, "enrage", 28, 12);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_exoskeleton", "Exoskeleton", "エクソスケルトン")
                .game(2, 2).size(0.7f, 0.7f).hp(26).speed(0.3f).style(Style.INSECT).egg(0xc9b27a, 0x4a3a1c)
                .model(Exoskeleton::model).spawn(5, 3, 4, H)
                .onSpawn(s -> s.setCount("slot", s.rand().nextInt(3)))
                .onHurt((s, src, dmg) -> Math.min(dmg, 9 * Cfg.DMG))
                .pattern(s -> {
                    String l = s.lastMove();
                    if (l.isEmpty()) return new String[]{"skitter", "mandibles", "enrage"}[s.count("slot")];
                    if (l.equals("mandibles")) return "enrage";
                    if (l.equals("skitter")) return "mandibles";   // only other option: never the same move twice
                    return s.rand().nextBoolean() ? "skitter" : "mandibles";
                })
                .move(Move.of("skitter", 26).intent(Intent.ATTACK, 1, 3).reach(2f)
                    .atAll(new int[]{9, 14, 19}, Fx.melee(1, 2.2, 80)))
                .move(Move.of("mandibles", 28).intent(Intent.ATTACK, 8, 1).reach(2f).at(13, Fx.melee(8, 2.2, 80)))
                .move(Move.of("enrage", 28).intent(Intent.BUFF).noTarget().at(14, Fx.strength(2)));
        }
    }

    // ------------------------------------------------------------------ Thieving Hopper: 79 HP, Escape Artist
    public static final class ThievingHopper {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_thieving_hopper", 9, 8, 12, 6, 2, 0x4f8a4a, 0x2a4a28, 0x7ab870, 0xe8c020, p -> {
                p[1].box(-4, 5, 1, 8, 2, 5).color(0x222222);               // little top hat
                p[1].box(-2.5f, 7, 2, 5, 4, 3).color(0x222222);
                p[0].box(-3, -3, -9, 6, 3, 3).color(0xe8c020);             // stolen loot sack
            });
            Bug.attack(m, "thievery", 32, 10, 15, 1, 0); Bug.rear(m, "flutter", 30, 12); Bug.attack(m, "hat_trick", 36, 12, 19, 1, 0);
            Bug.attack(m, "nab", 28, 9, 13, 1, 0); Bug.rear(m, "escape", 24, 10);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_thieving_hopper", "Thieving Hopper", "こそどろバッタ")
                .game(2, 2).size(0.9f, 1.0f).hp(79).speed(0.34f).style(Style.INSECT).egg(0x4f8a4a, 0xe8c020)
                .model(ThievingHopper::model).spawn(4, 1, 1, H)
                .pattern(Pattern.cycle("thievery", "flutter", "hat_trick", "nab", "escape"))
                .onHurt((s, src, dmg) -> {
                    if (!s.flags.contains("flutter")) return dmg;
                    if (s.inc("fl") >= 5) { s.flags.remove("flutter"); s.stun(40); }
                    return dmg * 0.5f;
                })
                .move(Move.of("thievery", 32).intent(Intent.ATTACK_DEBUFF, 17, 1).reach(2.4f)
                    .at(15, (s, t) -> Fx.meleeNow(s, 17, 2.6, 80, v -> Fx.steal(s, v, 15))))
                .move(Move.of("flutter", 30).intent(Intent.BUFF).noTarget().at(12, (s, t) -> { s.flags.add("flutter"); s.setCount("fl", 0); Fx.particles(s, ParticleTypes.CLOUD, 12, 0.5); }))
                .move(Move.of("hat_trick", 36).intent(Intent.ATTACK, 21, 1).reach(2.4f).at(19, Fx.melee(21, 2.6, 80)))
                .move(Move.of("nab", 28).intent(Intent.ATTACK, 14, 1).reach(2.4f).at(13, Fx.melee(14, 2.6, 80)))
                .move(Move.of("escape", 24).intent(Intent.ESCAPE).noTarget().at(10, (s, t) -> Fx.vanish(s)));
        }
    }

    // ------------------------------------------------------------------ Tunneler: 87 HP, burrows behind 32 Block
    public static final class Tunneler {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_tunneler", 12, 9, 18, 7, 3, 0x7a5a3c, 0x3e2c1a, 0xa07c58, 0xd8d0b0, p -> {
                for (int i = 0; i < 3; i++) p[1].box(-3 + i * 2.2f, 4, 2, 1, 3, 3).color(0xd8d0b0);   // digging claws on the head
                p[0].box(-5, 4, -6, 10, 2, 12).color(0x5a4228).bevel(0.2f).noise(0.2f);
            });
            Bug.attack(m, "bite", 30, 10, 15, 1, 0); Bug.rear(m, "burrow", 30, 10); Bug.attack(m, "attack_from_below", 36, 12, 20, 1, 0);
            Bug.rear(m, "emerging_strike", 36, 20);
            Clip b = m.clip("burrowed", 60).loop();
            Kit.P(b, "body", 0, 0, -6, 0, 0, 30, 0, -5.4f, 0, 0, 60, 0, -6, 0, 0);
            Kit.R(b, "head", 0, 0, 0, 0, 0, 30, 6, 0, 0, 0, 60, 0, 0, 0, 0);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_tunneler", "Tunneler", "トンネラー")
                .game(2, 2).size(1.2f, 1.0f).hp(87).speed(0.24f).style(Style.INSECT).egg(0x7a5a3c, 0xd8d0b0).keepBlock()
                .model(Tunneler::model).spawn(4, 1, 1, H)
                .pattern(s -> {
                    if (s.flags.contains("burrowed")) {
                        if (s.getBlock() <= 0) { s.flags.remove("burrowed"); s.setBase("idle"); return "emerging_strike"; }
                        return "attack_from_below";
                    }
                    return s.lastMove().equals("bite") ? "burrow" : "bite";
                })
                .move(Move.of("bite", 30).intent(Intent.ATTACK, 13, 1).reach(2.6f).at(15, Fx.melee(13, 2.8, 90)))
                .move(Move.of("burrow", 30).intent(Intent.DEFEND_BUFF).noTarget()
                    .at(12, (s, t) -> { s.addBlock(32); s.flags.add("burrowed"); s.setBase("burrowed"); Fx.particles(s, ParticleTypes.POOF, 20, 0.7); Fx.sound(s, SoundEvents.GRAVEL_BREAK, 1f, 0.6f); }))
                .move(Move.of("attack_from_below", 36).intent(Intent.ATTACK, 23, 1).reach(3f)
                    .at(20, (s, t) -> { Fx.particles(s, ParticleTypes.CAMPFIRE_COSY_SMOKE, 6, 0.4); Fx.melee(23, 3.0, 100).run(s, t); }))
                .move(Move.of("emerging_strike", 36).intent(Intent.STUN).noTarget());
        }
    }

    // ------------------------------------------------------------------ Chomper: 62 HP, Artifact 2, pair alternates Clamp/Screech
    public static final class Chomper {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_chomper", 9, 8, 11, 8, 2, 0x8a3a5a, 0x4a1c30, 0xc05a80, 0xf0f0e0, p -> {
                for (int i = 0; i < 4; i++) { p[1].box(-3.5f + i * 2.3f, -4, 6, 1, 2, 1).color(0xf0f0e0); p[1].box(-3.5f + i * 2.3f, 3, 6, 1, 2, 1).color(0xf0f0e0); }   // teeth
            });
            Bug.attack(m, "clamp", 30, 8, 12, 2, 8); Bug.rear(m, "screech", 30, 10);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_chomper", "Chomper", "チョンパー")
                .game(2, 2).size(0.9f, 0.9f).hp(62).speed(0.28f).style(Style.INSECT).egg(0x8a3a5a, 0xf0f0e0)
                .model(Chomper::model).spawn(4, 2, 2, H)
                .onSpawn(s -> s.setCount("off", s.rand().nextInt(2)))
                .pattern(s -> (s.getTurn() + s.count("off")) % 2 == 0 ? "clamp" : "screech")
                .move(Move.of("clamp", 30).intent(Intent.ATTACK, 8, 2).reach(2.2f).at(12, Fx.melee(8, 2.4, 80)).at(20, Fx.melee(8, 2.4, 80)))
                .move(Move.of("screech", 30).intent(Intent.DEBUFF).ranged(8)
                    .sound(10, SoundEvents.GHAST_SCREAM, 0.8f, 1.6f)
                    .at(10, (s, t) -> { if (t != null) for (int i = 0; i < 3; i++) Fx.curse(t, Fx.Curse.DAZED); Fx.particles(s, ParticleTypes.NOTE, 10, 0.6); }));
        }
    }

    // ------------------------------------------------------------------ Louse Progenitor: 135 HP, Curl Up 14
    public static final class LouseProgenitor {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_louse_progenitor", 16, 12, 22, 8, 3, 0x6a5a8a, 0x30284a, 0x9a88c0, 0xe8e0f8, p -> {
                for (int i = 0; i < 4; i++) p[0].box(-7, 2, -9 + i * 5, 14, 1, 2).color(0x30284a);   // segmented shell
            });
            Bug.attack(m, "web_cannon", 34, 12, 18, 1, 0); Bug.rear(m, "curl_and_grow", 36, 14); Bug.attack(m, "pounce", 34, 12, 19, 1, 0);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_louse_progenitor", "Louse Progenitor", "シラミの祖")
                .game(2, 2).size(1.4f, 1.1f).hp(135).speed(0.24f).style(Style.INSECT).egg(0x6a5a8a, 0xe8e0f8).keepBlock()
                .model(LouseProgenitor::model).spawn(3, 1, 1, H)
                .onHurt((s, src, dmg) -> { if (!s.flags.contains("curled")) { s.flags.add("curled"); s.addBlock(14); } return dmg; })
                .pattern(Pattern.cycle("web_cannon", "curl_and_grow", "pounce"))
                .move(Move.of("web_cannon", 34).intent(Intent.ATTACK_DEBUFF, 9, 1).ranged(8)
                    .at(18, Fx.melee(9, 6, 40, v -> { Fx.frail(v, 6); Fx.slow(v, 3, 0); })))
                .move(Move.of("curl_and_grow", 36).intent(Intent.DEFEND_BUFF).noTarget().at(14, Fx.block(14)).at(18, Fx.strength(5)))
                .move(Move.of("pounce", 34).intent(Intent.ATTACK, 14, 1).reach(3f).at(10, Fx.dash(0.6, 0.2)).at(19, Fx.melee(14, 3.0, 90)));
        }
    }

    // ------------------------------------------------------------------ Myte: 64 HP, pairs
    public static final class Myte {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_myte", 8, 7, 11, 6, 3, 0x8a9a3a, 0x4a5a1c, 0xb8c860, 0xf0a020, p ->
                p[1].box(-0.5f, 3, 3, 1, 5, 1).color(0xf0a020));                                  // antenna
            Bug.rear(m, "toxic_cornucopia", 32, 12); Bug.attack(m, "bite", 28, 9, 13, 1, 0); Bug.attack(m, "suck", 26, 8, 12, 1, 0);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_myte", "Myte", "マイト")
                .game(2, 2).size(0.8f, 0.7f).hp(64).speed(0.3f).style(Style.INSECT).egg(0x8a9a3a, 0xf0a020)
                .model(Myte::model).spawn(4, 2, 2, H)
                .onSpawn(s -> s.setCount("off", s.rand().nextBoolean() ? 0 : 2))
                .pattern(s -> new String[]{"toxic_cornucopia", "bite", "suck"}[(s.getTurn() + s.count("off")) % 3])
                .move(Move.of("toxic_cornucopia", 32).intent(Intent.DEBUFF).ranged(8)
                    .at(12, (s, t) -> { if (t != null) { Fx.poison(t, 4); Fx.curse(t, Fx.Curse.SLIMED); } Fx.particles(s, ParticleTypes.SPORE_BLOSSOM_AIR, 14, 0.5); }))
                .move(Move.of("bite", 28).intent(Intent.ATTACK, 13, 1).reach(2.2f).at(13, Fx.melee(13, 2.4, 80)))
                .move(Move.of("suck", 26).intent(Intent.ATTACK_BUFF, 4, 1).reach(2.2f).at(12, Fx.melee(4, 2.4, 80)).at(14, Fx.strength(2)));
        }
    }

    // ------------------------------------------------------------------ Ovicopter 127 HP + Tough Egg + Hatchling
    public static final class Ovicopter {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_ovicopter", 14, 11, 20, 8, 3, 0xd8c070, 0x806830, 0xf0e0a0, 0x60b8d8, p -> {
                for (int s = -1; s <= 1; s += 2) {
                    Part w = p[0].add(s < 0 ? "wingR" : "wingL").at(s * 7, 6, 0);
                    w.box(s < 0 ? -16 : 0, 0, -5, 16, 1, 10).color(0x60b8d8).glow(Face.TOP, 0, 0, 16, 10, 0x80d8f0);
                }
                p[0].box(-5, -9, -12, 10, 8, 10).color(0xf0e0a0).bevel(0.4f);   // egg sac abdomen
            });
            m.proc((rig, ctx) -> { rig.rot("wingR", 0, 0, (float) Math.sin(ctx.age * 1.6) * 25f); rig.rot("wingL", 0, 0, -(float) Math.sin(ctx.age * 1.6) * 25f); });
            Bug.rear(m, "lay_eggs", 34, 14); Bug.attack(m, "smash", 34, 12, 19, 1, 0); Bug.attack(m, "tenderizer", 30, 9, 14, 1, 0); Bug.rear(m, "nutritional_paste", 30, 12);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_ovicopter", "Ovicopter", "オビコプター")
                .game(2, 2).size(1.3f, 1.3f).hp(127).speed(0.26f).flying().style(Style.INSECT).egg(0xd8c070, 0x60b8d8)
                .model(Ovicopter::model).spawn(3, 1, 1, H)
                .pattern(s -> {
                    switch (s.lastMove()) {
                        case "": return "lay_eggs";
                        case "lay_eggs": return "smash";
                        case "smash": return "tenderizer";
                        case "tenderizer": return Fx.countAlive(s, "s2_tough_egg", 24) + Fx.countAlive(s, "s2_hatchling", 24) <= 3 ? "lay_eggs" : "nutritional_paste";
                        default: return "smash";
                    }
                })
                .move(Move.of("lay_eggs", 34).intent(Intent.SUMMON).noTarget().at(14, Fx.summonRing("s2_tough_egg", 3, 3.0)))
                .move(Move.of("smash", 34).intent(Intent.ATTACK, 16, 1).reach(3f).at(19, Fx.melee(16, 3.0, 90)))
                .move(Move.of("tenderizer", 30).intent(Intent.ATTACK_DEBUFF, 7, 1).reach(3f).at(14, Fx.melee(7, 3.0, 90, v -> Fx.vulnerable(v, 8))))
                .move(Move.of("nutritional_paste", 30).intent(Intent.BUFF).noTarget().at(12, Fx.strength(3)));
        }
    }

    public static final class ToughEgg {
        public static ModelSpec model() {
            ModelSpec m = new ModelSpec("s2_tough_egg");
            Part e = m.add("body").at(0, 5, 0);
            e.box(-4, -5, -4, 8, 10, 8).color(0xe8d8b0).grad(0xf8f0d0, 0xb8a070).bevel(0.5f).noise(0.12f).paint(Face.FRONT, 1, 3, 2, 2, 0x806830).paint(Face.FRONT, 5, 6, 2, 1, 0x806830);
            Clip i = m.clip("idle", 30).loop(); Kit.R(i, "body", 0, 0, 0, 0, 0, 8, 0, 0, 5, 0, 22, 0, 0, -5, 0, 30, 0, 0, 0, 0);
            Kit.hurt(m, "body", "body"); Clip w = m.clip("wait", 30).loop(); Kit.R(w, "body", 0, 0, 0, 0, 0, 15, 0, 0, 3, 0, 30, 0, 0, 0, 0);
            Clip h = m.clip("hatch", 24); Kit.S(h, "body", 0, 1, 1, 1, 0, 14, 1.25f, 0.85f, 1.25f, 2, 24, 1, 1, 1, 0); Kit.R(h, "body", 8, 0, 0, 14, 0, 16, 0, 0, -14, 0, 24, 0, 0, 0, 0);
            Clip d = m.clip("death", 14); Kit.S(d, "body", 0, 1, 1, 1, 0, 14, 1.4f, 0.1f, 1.4f, 1);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_tough_egg", "Tough Egg", "かたいタマゴ")
                .game(2, 2).size(0.5f, 0.7f).hp(16).speed(0f).minion().style(Style.SLIME).egg(0xe8d8b0, 0x806830)
                .model(ToughEgg::model).base("wait")
                .pattern(s -> s.getTurn() < 1 ? "wait" : "hatch")
                .move(Move.of("wait", 30).anim("wait").intent(Intent.NONE).noTarget())
                .move(Move.of("hatch", 24).intent(Intent.SUMMON).noTarget()
                    .at(16, (s, t) -> { Fx.summon(s, "s2_hatchling", 0, 0); s.kill(); }));
        }
    }

    public static final class Hatchling {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_hatchling", 5, 4, 7, 4, 2, 0xd8c070, 0x806830, 0xf0e0a0, 0xe8a030, null);
            Bug.attack(m, "nibble", 22, 6, 9, 1, 0);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_hatchling", "Hatchling", "ハッチリング")
                .game(2, 2).size(0.5f, 0.4f).hp(20).speed(0.34f).minion().style(Style.INSECT).egg(0xd8c070, 0xe8a030)
                .model(Hatchling::model).pattern(Pattern.cycle("nibble"))
                .move(Move.of("nibble", 22).intent(Intent.ATTACK, 4, 1).reach(1.8f).at(9, Fx.melee(4, 2.0, 80)));
        }
    }

    // ------------------------------------------------------------------ Spiny Toad: 118 HP
    public static final class SpinyToad {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_spiny_toad", 14, 10, 16, 8, 2, 0x5a8a4a, 0x2c4a24, 0x88b878, 0xe8e0a0, p -> {
                for (int i = 0; i < 5; i++) { p[0].box(-6 + i * 2.5f, 5, -6 + (i % 2) * 6, 1, 4, 1).color(0xe8e0a0); p[0].box(-5 + i * 2.5f, 5, 0, 1, 3, 1).color(0xe8e0a0); }
                p[1].box(-3, -3, 7, 6, 2, 2).color(0xd04a6a);                                    // tongue
            });
            Bug.rear(m, "protruding_spikes", 32, 14); Bug.attack(m, "spike_explosion", 38, 14, 22, 1, 0); Bug.attack(m, "tongue_lash", 34, 10, 16, 1, 0);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_spiny_toad", "Spiny Toad", "トゲガエル")
                .game(2, 2).size(1.3f, 1.0f).hp(118).speed(0.22f).style(Style.FROG).egg(0x5a8a4a, 0xe8e0a0)
                .model(SpinyToad::model).spawn(3, 1, 1, H)
                .pattern(Pattern.cycle("protruding_spikes", "spike_explosion", "tongue_lash"))
                .onHurt((s, src, amt) -> {
                    if (s.count("thorns") > 0 && src.getDirectEntity() instanceof LivingEntity a && a != s && a.distanceTo(s) < 4) Fx.raw(s, a, s.count("thorns") * Cfg.DMG);
                    return amt;
                })
                .move(Move.of("protruding_spikes", 32).intent(Intent.BUFF).noTarget().at(14, (s, t) -> { s.setCount("thorns", 5); Fx.particles(s, ParticleTypes.CRIT, 16, 0.6); }))
                .move(Move.of("spike_explosion", 38).intent(Intent.ATTACK, 23, 1).reach(4f)
                    .at(22, (s, t) -> { s.setCount("thorns", 0); Fx.aoe(23, 4.0).run(s, t); Fx.ring(s, ParticleTypes.CRIT, s.position(), 3.5, 24); }))
                .move(Move.of("tongue_lash", 34).intent(Intent.ATTACK, 17, 1).reach(4.2f).at(16, Fx.melee(17, 4.2, 40)));
        }
    }

    // ------------------------------------------------------------------ The Obscura 123 HP + Parafright 21 HP
    public static final class Obscura {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_the_obscura", 12, 14, 12, 9, 2, 0x4a3a6a, 0x201838, 0x7a68a0, 0xe8e0ff, p -> {
                p[1].box(-3, 0, 8, 6, 6, 1).color(0xe8e0ff).glow(Face.FRONT, 0, 0, 6, 6, 0xe8e0ff);   // lantern eye
                p[0].box(-1, 8, -2, 2, 8, 2).color(0x201838);
            });
            Bug.rear(m, "illusion", 36, 14); Bug.attack(m, "piercing_gaze", 32, 10, 16, 1, 0); Bug.rear(m, "wail", 32, 12); Bug.attack(m, "hardening_strike", 30, 9, 14, 1, 0);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_the_obscura", "The Obscura", "オブスキュラ")
                .game(2, 2).size(1.1f, 1.6f).hp(123).speed(0.22f).style(Style.GHOST).egg(0x4a3a6a, 0xe8e0ff)
                .model(Obscura::model).spawn(3, 1, 1, H)
                .pattern(Pattern.first("illusion", Pattern.random().add("piercing_gaze", 1).add("wail", 1).add("hardening_strike", 1)
                    .maxRepeat("piercing_gaze", 1).maxRepeat("wail", 1).maxRepeat("hardening_strike", 1)))
                .move(Move.of("illusion", 36).intent(Intent.SUMMON).noTarget().at(16, (s, t) -> Fx.summon(s, "s2_parafright", 2, 0)))
                .move(Move.of("piercing_gaze", 32).intent(Intent.ATTACK, 10, 1).ranged(6).at(16, Fx.hitTarget(10)))
                .move(Move.of("wail", 32).intent(Intent.BUFF).noTarget().at(14, (s, t) ->
                    s.level().getEntitiesOfClass(SpireMob.class, s.getBoundingBox().inflate(16)).forEach(a -> { if (a.isAlive()) a.addStrength(3); })))
                .move(Move.of("hardening_strike", 30).intent(Intent.ATTACK_DEFEND, 6, 1).reach(2.6f).at(14, Fx.melee(6, 2.8, 90)).at(14, Fx.block(6)));
        }
    }

    public static final class Parafright {
        public static ModelSpec model() {
            ModelSpec m = Bug.model("s2_parafright", 7, 9, 7, 6, 2, 0x8a78b8, 0x40305a, 0xb8a8e0, 0xffffff, null);
            m.bob(1.5f, 0.15f);
            Bug.attack(m, "slam", 30, 10, 16, 1, 0);
            return m;
        }
        public static MobDef def() {
            return MobDef.of("s2_parafright", "Parafright", "パラフライト")
                .game(2, 2).size(0.7f, 1.2f).hp(21).speed(0.26f).flying().minion().style(Style.GHOST).egg(0x8a78b8, 0xffffff)
                .model(Parafright::model).pattern(Pattern.cycle("slam"))
                .move(Move.of("slam", 30).intent(Intent.ATTACK, 16, 1).reach(2.4f).at(16, Fx.melee(16, 2.6, 80)));
        }
    }
}
