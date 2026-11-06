# HANDOFF — resume here

Mod: Minecraft 1.20.1 Forge 47.3.0, id `spiremod`. Goal: ALL Slay the Spire 1 + 2 creatures, faithful moves/HP/AI/looks, choreographed attack animations
(research wiki: slaythespire.wiki.gg, sts2.untapped.gg), hostile to every non-Spire mob and vice versa (already implemented in `core/Hostility`).

## Done
- Framework (`spec/`, `core/`, `client/`, `tools/`): procedural boxes+textures, keyframe animation, SpireMob AI, projectiles, spawns, spawn eggs. Read `docs/CREATURE_GUIDE.md` first.
- Reference creature: `mobs/example/Cultist.java` (registered in `mobs/example/Group.java`).
- Build works: `JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 ./gradlew --no-daemon compileJava` (needs JDK 17: `apt-get install openjdk-17-jdk-headless`; `tools/classpath.txt` via `./gradlew printCp`).

## Status
184 creatures (incl. 9 playable heroes in mobs/heroes: StS1 ironclad,silent,defect,watcher; StS2 s2_ironclad,s2_silent,s2_defect,s2_regent,s2_necrobinder + s2_osty minion) registered in all Group.java; `tools/validate.sh x --all` PASS; assets generated (src/main/resources/assets); `./gradlew build` produces build/libs/spiremod-1.0.0.jar.
Dedicated-server smoke test DONE (runServer + RCON): all 174 summon OK, ~2 min ticking with all fighting, 0 exceptions; fixed SpireMob ctor (def null in registerGoals / flying nav) and Donu/Deca spawn recursion. NOT done: client-side (rendering/animation/boss bar visuals) test needs a GUI Minecraft. Most agent-written creatures only previewed on 1 attack clip; idle/walk/death mostly unreviewed; several from memory (no wiki check), e.g. s1act1b, s1act2a, s1act3b.
StS2 lists on wiki Overgrowth/Underdocks/Hive/Glory are now covered (Hive extras in s2hive1/HiveB.java, Mawler in s2og1).
Known approximations: no card/Strength/Dex/Artifact effects in engine; mapped to MC effects.

## Finish steps (after creatures exist)
1. All Group.java registered; `tools/validate.sh <name> --all` (after gradle compile) -> PASS.
2. `tools/gen_assets.sh` (writes lang/en_us.json, ja_jp.json, spawn-egg item models). Add `pack.mcmeta` check.
3. `./gradlew build` -> jar in build/libs. In-game test (`runClient`/`runServer`, EULA needs user consent): hostility, spawns, intents, animations, boss bars.
4. Known risks to verify in-game: SpireMob death anim flip, flying mobs, Hostility goal injection on vanilla mobs, texture upload via DynamicTexture, minion cleanup.

## Parallel work recipe
One session/agent per package using the prompt: "Read docs/CREATURE_GUIDE.md + mobs/example/Cultist.java; finish package <pkg> assignment above; only edit that package; never run gradle/git; preview PNGs and iterate; register in Group.java."

## Hero card pool (wiki-synced)
Hero moves = hand-written signature cards (Heroes.java) + every parsable wiki card of their colour (CardMoves.java, ~35-55 moves each).
After a game update that changes cards: `python3 tools/gen_cards.py` (pulls the wiki Cargo `Cards` table -> heroes/CardData.java), then rebuild. Only damage/block/Weak/Vulnerable/Poison/Strength/Dexterity/Focus/Thorns/Metallicize/heal text is interpreted; other effects (draw, exhaust, orbs...) are ignored.

## Downfall villains (StS1 expansion, hero versions)
ids df_hexaghost, df_guardian, df_slimebound, df_champ, df_automaton, df_collector, df_gremlin, df_snecko, df_awakened_one (heroes/Villains.java). HP from the mod's character classes; moves = every parsable card in the Downfall source.
Regenerate card data from a Downfall checkout: `python3 tools/gen_downfall.py /path/to/DownfallSTS` (repo sei10824game-del/DownfallSTS) -> heroes/DownfallData.java. Villain-specific mechanics (Soulburn, stances, Stasis, Compile...) are ignored; only damage/block/Weak/Vulnerable/Poison/Strength/heal text is interpreted.
sei10824game-del/Downfall-2 is the StS2 version of Downfall (C#, per-character `localization/eng/cards.json`) — not used yet.
