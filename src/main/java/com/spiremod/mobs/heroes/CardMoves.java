package com.spiremod.mobs.heroes;

import com.spiremod.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;

/**
 * Turns the wiki card text in CardData (regenerate with tools/gen_cards.py after card updates) into creature moves.
 * Recognised: "Deal N damage [M times] [to ALL enemies]", "Gain N Block", "Apply N Weak/Vulnerable/Poison", "Gain N Strength/Dexterity/Focus/
 * Thorns/Metallicize/Plated Armor", "Heal N". Cards with none of these are skipped. Numbers are the base (un-upgraded) values.
 */
final class CardMoves {
    private CardMoves() {}
    private static final java.util.regex.Pattern DMG = java.util.regex.Pattern.compile("Deal (\\d+) damage(?: (\\d+) times)?( to ALL enemies)?");
    private static final java.util.regex.Pattern BLK = java.util.regex.Pattern.compile("Gain (\\d+) Block");
    private static final java.util.regex.Pattern APPLY = java.util.regex.Pattern.compile("Apply (\\d+) (Weak|Vulnerable|Poison)( to ALL enemies)?");
    private static final java.util.regex.Pattern SELF = java.util.regex.Pattern.compile("Gain (\\d+) (Strength|Dexterity|Focus|Thorns|Metallicize|Plated Armor)");
    private static final java.util.regex.Pattern HEAL = java.util.regex.Pattern.compile("Heal (\\d+)");
    /** hand-written signature cards (kept in Heroes) are not generated */
    private static final Set<String> HAND = Set.of("strike", "defend", "bash", "neutralize", "survivor", "zap", "dualcast", "eruption", "vigilance",
        "falling_star", "venerate", "bodyguard", "unleash");

    static final class Fxs { int dmg, hits = 1, block, weak, vuln, poison, str, heal; boolean aoe, aoeDebuff; }

    static String slug(String n) { return "c_" + n.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_").replaceAll("_$", ""); }

    static Fxs parse(String text) {
        Fxs f = new Fxs();
        Matcher m = DMG.matcher(text);
        if (m.find()) { f.dmg = Integer.parseInt(m.group(1)); if (m.group(2) != null) f.hits = Math.min(7, Integer.parseInt(m.group(2))); f.aoe = m.group(3) != null; }
        m = BLK.matcher(text); if (m.find()) f.block = Integer.parseInt(m.group(1));
        m = APPLY.matcher(text);
        while (m.find()) {
            int n = Integer.parseInt(m.group(1));
            switch (m.group(2)) { case "Weak" -> f.weak += n; case "Vulnerable" -> f.vuln += n; default -> f.poison += n; }
            if (m.group(3) != null) f.aoeDebuff = true;
        }
        m = SELF.matcher(text);
        while (m.find()) { int n = Integer.parseInt(m.group(1)); if (m.group(2).equals("Strength")) f.str += n; else f.block += n * 2; }
        m = HEAL.matcher(text); if (m.find()) f.heal = Integer.parseInt(m.group(1));
        return f;
    }

    /** adds every parsable card of (game,color) as a move and makes the pattern pick among hand-written moves (weighted) and cards */
    static MobDef finish(MobDef d, int game, String color) { return finish(d, String.valueOf(game), color); }

    /** game: "1"/"2" = wiki CardData, "DF" = Downfall DownfallData (color = villain id) */
    static String[] rows(String game) { return game.equals("DF") ? DownfallData.ROWS : CardData.ROWS; }

    /** parsed effects of the named basic card (e.g. "Strike"), or null */
    static Fxs basic(String game, String color, String name) {
        for (String row : rows(game)) {
            String[] c = row.split("\\|", 7);
            if (c.length == 7 && c[0].equals(game) && c[1].equals(color) && c[2].equalsIgnoreCase(name)) return parse(c[6]);
        }
        return null;
    }

    static MobDef finish(MobDef d, String game, String color) {
        List<String> own = new ArrayList<>(d.moves.keySet());
        List<String> cards = new ArrayList<>();
        for (String row : rows(game)) {
            String[] c = row.split("\\|", 7);
            if (c.length < 7 || !c[0].equals(game) || !c[1].equals(color)) continue;
            String id = slug(c[2]);
            if (HAND.contains(id.substring(2)) || d.moves.containsKey(id)) continue;
            Fxs f = parse(c[6]);
            if (f.dmg + f.block + f.weak + f.vuln + f.poison + f.str + f.heal == 0) continue;
            d.move(build(id, f, c[4].equals("Power")));
            cards.add(id);
        }
        com.spiremod.core.Pattern.Random r = com.spiremod.core.Pattern.random();
        for (String id : own) r.add(id, id.equals("strike") ? 10 : id.equals("defend") ? 8 : 3).maxRepeat(id, 1);
        for (String id : cards) r.add(id, 1).maxRepeat(id, 1);
        return d.pattern(r);
    }

    private static Move build(String id, Fxs f, boolean power) {
        boolean atk = f.dmg > 0;
        int hits = atk ? f.hits : 1;
        String anim = atk ? (hits >= 4 ? "strike" : hits >= 2 ? "thrust" : "strike") : f.block > 0 && f.str == 0 && f.weak + f.vuln + f.poison == 0 ? "defend" : "skill";
        int len = switch (anim) { case "strike" -> 26; case "thrust" -> 20; case "defend" -> 24; default -> 30; };
        Intent in = atk ? (f.weak + f.vuln + f.poison > 0 ? Intent.ATTACK_DEBUFF : f.block > 0 ? Intent.ATTACK_DEFEND : Intent.ATTACK)
            : f.weak + f.vuln + f.poison > 0 ? Intent.DEBUFF : f.block > 0 && f.str == 0 ? Intent.DEFEND : Intent.BUFF;
        Move mv = Move.of(id, len).anim(anim);
        if (atk) mv.intent(in, f.dmg, hits).reach(f.aoe ? 3.5f : 2.8f);
        else if (in == Intent.DEBUFF) mv.intent(in).ranged(8);
        else mv.intent(in).noTarget();
        Consumer<LivingEntity> onHit = v -> debuff(v, f);
        int first = anim.equals("thrust") ? 8 : anim.equals("strike") ? 12 : 10;
        int gap = anim.equals("strike") && hits >= 4 ? 2 : anim.equals("thrust") ? 3 : 4;
        if (atk) {
            for (int i = 0; i < hits; i++) {
                final boolean last = i == hits - 1;
                int t = first + i * gap;
                if (f.aoe) mv.at(t, Fx.aoe(f.dmg, 3.5, last ? onHit : null));
                else mv.at(t, Fx.melee(f.dmg, 2.8, 100, last ? onHit : null));
            }
        } else if (f.weak + f.vuln + f.poison > 0) {
            mv.at(first, (s, t) -> { if (f.aoeDebuff) for (LivingEntity v : Fx.foes(s, s.getBoundingBox().inflate(8))) debuff(v, f); else if (t != null) debuff(t, f); });
        }
        mv.at(first, (s, t) -> {
            if (f.block > 0) s.addBlock(f.block);
            if (f.str > 0 && s.getStrength() < 6) s.addStrength(f.str);
            if (f.heal > 0) s.heal(f.heal);
            if (f.block > 0 || f.str > 0 || f.heal > 0) Fx.particles(s, power ? ParticleTypes.ENCHANT : ParticleTypes.CRIT, 8, 0.5);
        });
        return mv;
    }

    private static void debuff(LivingEntity v, Fxs f) {
        if (f.weak > 0) Fx.weak(v, 3 + 3 * f.weak);
        if (f.vuln > 0) Fx.vulnerable(v, 3 + 3 * f.vuln);
        if (f.poison > 0) Fx.poison(v, 2 + f.poison);
    }
}
