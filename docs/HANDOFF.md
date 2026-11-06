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
