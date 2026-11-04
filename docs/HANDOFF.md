# HANDOFF — resume here

Mod: Minecraft 1.20.1 Forge 47.3.0, id `spiremod`. Goal: ALL Slay the Spire 1 + 2 creatures, faithful moves/HP/AI/looks, choreographed attack animations
(research wiki: slaythespire.wiki.gg, sts2.untapped.gg), hostile to every non-Spire mob and vice versa (already implemented in `core/Hostility`).

## Done
- Framework (`spec/`, `core/`, `client/`, `tools/`): procedural boxes+textures, keyframe animation, SpireMob AI, projectiles, spawns, spawn eggs. Read `docs/CREATURE_GUIDE.md` first.
- Reference creature: `mobs/example/Cultist.java` (registered in `mobs/example/Group.java`).
- Build works: `JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 ./gradlew --no-daemon compileJava` (needs JDK 17: `apt-get install openjdk-17-jdk-headless`; `tools/classpath.txt` via `./gradlew printCp`).

## Partially done (written by interrupted agents, UNVERIFIED, NOT registered in any Group.java)
Run `tools/check.sh` / `tools/validate.sh` / `tools/preview.sh` on each, fix, then add `out.accept(X.def());` to the package's `Group.java`.
- s1act1a: JawWorm, Louse (red/green), Slime (`def(boolean acid,int size)`)
- s1act1b: FatGremlin, MadGremlin, ShieldGremlin, SneakyGremlin, GremlinKit
- s1act2a: Byrd, Snecko, SphericGuardian
- s1act2b: BronzeAutomaton, Champ
- s1act3a: Darkling, OrbWalker, Spiker
- s1act3b: SpireGrowth, SpireShield, S3
- s2og1: LeafSlime (defS/defM), Nibbit, Kit; s2og2: PhrogParasite, Bits; s2ud1: Seapunk; s2ud2: TerrorEel
- s2hive1: Bowlbug (`def(int kind)`), Kit; s2hive2: Decimillipede (`def(int v)`), H
- Removed incomplete `s2glory2/Queen.java` (needs a TorchHeadAmalgam minion class; recover with `git show 368d20b:src/main/java/com/spiremod/mobs/s2glory2/Queen.java`).

## Remaining assignments (package = work unit; ids: StS1 plain, StS2 `s2_` prefix)
- s1act1a: Acid/Spike slime splits, Fungi Beast, Blue Slaver, Red Slaver, Looter
- s1act1b: Gremlin Nob, Lagavulin (base clip `sleep`), Sentry, Gremlin Wizard, Slime Boss, The Guardian, Hexaghost
- s1act2a: Shelled Parasite, Chosen, Centurion, Mystic, Snake Plant, Mugger, Bandit Bear/Pointy/Romeo
- s1act2b: Book of Stabbing, Gremlin Leader, Taskmaster, Bronze Orb, The Collector, Torch Head
- s1act3a: Repulsor, Exploder, Writhing Mass, Transient, Giant Head, Nemesis, Reptomancer, Dagger, Maw
- s1act3b: Awakened One (+awakened_cultist), Time Eater, Donu, Deca, Spire Spear, Corrupt Heart
- s2og1 Overgrowth normals: Crawlers, Fuzzy Wurm Crawler, Shrinker Beetle, Ruby Raiders (Crossbow etc.), Flora, Shroom/Slime, Strangler, Inklets, Vine Shambler, Fogmog, Mawler, Cubex Construct, Wrigglers, Jaxfruit, Flyconid
- s2og2: Bygone Effigy, Byrdonis, Vantom, The Kin (+Follower), Ceremonial Beast
- s2ud1 Underdocks normals: Wildlife, Cultists, Corpse Slugs, Gremlin Mercs, Living Fog, Fossil Stalker, Two-Tailed Rats, Haunted Ship, Punch Construct, Sewer Clam, Toadpoles, Sludge Spinner
- s2ud2: Phantasmal Gardeners, Skulking Colony, Waterfall Giant, Lagavulin Matriarch, Soul Fysh
- s2hive1: Slumber Party, Hunter Killer, Construct Menagerie, Scrolls of Biting, Fabricator, Owl Magistrate, Slimed Berserker, Frog Knight, Lost and Forgotten, Axebot, Globe Head, Devoted Sculptor, Turret Operator, Battleworn Dummy, Merchant
- s2hive2: Infested Prism, Entomancer, Kaiser Crab, Knowledge Demon, The Insatiable
- s2glory1 (empty): all Glory normals + elites;  s2glory2: all Glory bosses/final bosses incl. Queen
(Enumerate StS2 creatures from https://slaythespire.wiki.gg/wiki/Slay_the_Spire_2:Overgrowth|Underdocks|Hive|Glory.)

## Finish steps (after creatures exist)
1. All Group.java registered; `tools/validate.sh <name> --all` (after gradle compile) -> PASS.
2. `tools/gen_assets.sh` (writes lang/en_us.json, ja_jp.json, spawn-egg item models). Add `pack.mcmeta` check.
3. `./gradlew build` -> jar in build/libs. In-game test (`runClient`/`runServer`, EULA needs user consent): hostility, spawns, intents, animations, boss bars.
4. Known risks to verify in-game: SpireMob death anim flip, flying mobs, Hostility goal injection on vanilla mobs, texture upload via DynamicTexture, minion cleanup.

## Parallel work recipe
One session/agent per package using the prompt: "Read docs/CREATURE_GUIDE.md + mobs/example/Cultist.java; finish package <pkg> assignment above; only edit that package; never run gradle/git; preview PNGs and iterate; register in Group.java."
