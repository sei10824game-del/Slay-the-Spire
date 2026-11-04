package com.spiremod.tools;

import com.google.gson.*;
import com.spiremod.core.*;

import java.io.File;
import java.nio.file.*;
import java.util.*;

/** Writes lang files + spawn-egg item models for every creature. Run via tools/gen_assets.sh */
public final class GenAssets {
    public static void main(String[] a) throws Exception {
        net.minecraft.SharedConstants.tryDetectVersion();
        net.minecraft.server.Bootstrap.bootStrap();
        Catalog.init();
        Path res = Paths.get(a.length > 0 ? a[0] : "src/main/resources/assets/spiremod");
        Files.createDirectories(res.resolve("lang")); Files.createDirectories(res.resolve("models/item"));
        Map<String, String> en = new TreeMap<>(), ja = new TreeMap<>();
        en.put("itemGroup.spiremod", "Slay the Spire Creatures"); ja.put("itemGroup.spiremod", "Slay the Spire クリーチャー");
        en.put("entity.spiremod.spire_projectile", "Spire Projectile"); ja.put("entity.spiremod.spire_projectile", "スパイアの飛び道具");
        en.put("effect.spiremod.vulnerable", "Vulnerable"); ja.put("effect.spiremod.vulnerable", "脆弱");
        en.put("effect.spiremod.frail", "Frail"); ja.put("effect.spiremod.frail", "虚弱");
        en.put("effect.spiremod.dazed", "Dazed"); ja.put("effect.spiremod.dazed", "朦朧");
        for (MobDef d : Catalog.ALL) {
            String sfx = d.game == 2 ? " (StS2)" : "";
            en.put("entity.spiremod." + d.id, d.en + (d.game == 2 && !d.en.contains("(") ? "" : ""));
            ja.put("entity.spiremod." + d.id, d.ja);
            en.put("item.spiremod." + d.id + "_spawn_egg", d.en + " Spawn Egg");
            ja.put("item.spiremod." + d.id + "_spawn_egg", d.ja + "のスポーンエッグ");
            JsonObject m = new JsonObject(); m.addProperty("parent", "minecraft:item/template_spawn_egg");
            Files.writeString(res.resolve("models/item/" + d.id + "_spawn_egg.json"), new GsonBuilder().setPrettyPrinting().create().toJson(m));
        }
        Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        Files.writeString(res.resolve("lang/en_us.json"), g.toJson(en));
        Files.writeString(res.resolve("lang/ja_jp.json"), g.toJson(ja));
        System.out.println("generated assets for " + Catalog.ALL.size() + " creatures");
    }
}
