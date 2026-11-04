# Creature authoring guide (Slay the Spire mod, Forge 1.20.1)

Goal: every Slay the Spire 1 / 2 creature, **faithful to the original**: stats, EVERY move (numbers, order/AI pattern, intents),
passives, appearance (silhouette, colours, props, glowing parts) and **choreographed attack animations** (anticipation -> strike -> follow-through -> recovery,
secondary motion, per-move unique poses — NOT a generic "arm swings"). Research each creature on the wiki before writing it:
- StS1: https://slaythespire.wiki.gg/wiki/<Name>  (and Fandom `slay-the-spire.fandom.com`)
- StS2: https://slaythespire.wiki.gg/wiki/Slay_the_Spire_2:<Act or Name>  e.g. `...Slay_the_Spire_2:Overgrowth`, `:Hive`, `:Glory`, `:Underdocks`
Use WebFetch / Firecrawl / Tavily scrape tools. Also look for described animations ("leaps", "slams", "spins its head"...) and recall the in-game art.
Japanese names: use the official Japanese localisation when you know it, otherwise a sensible katakana transliteration.

## What you deliver per creature
One class `com.spiremod.mobs.<yourpkg>.<Name>` with two static methods (see `mobs/example/Cultist.java` = the reference; look at it first):
```java
public static ModelSpec model()   // pure look + animations, NO Minecraft calls
public static MobDef def()        // stats, moves, pattern, passives (may call Minecraft helpers)
```
plus a line `out.accept(<Name>.def());` in YOUR package's `Group.java` (already created for you). Variants (e.g. 3 slime sizes) can be one class with a parameter.
Quality bar: ModelSpec with 15-40 boxes for normal creatures (more for bosses), shading/detail painted with `paint/glow/band/grad`, a clip for EVERY move
(`Move.anim` = clip name), plus `idle`, `walk` (or `walkBiped/walkQuad` / proc for flyers), `hurt`, `death`. Bosses/elites should be visibly bigger and more intricate.

## Workflow (do it for every creature, in this order)
1. Research moves/passives/HP/looks. Write a short plan table in your head: move id | intent | dmg x hits | effect | when.
2. Write the class. 3. `tools/check.sh <agent> <your .java files>` (javac only; NEVER run gradle, git, or touch files outside your package).
4. `tools/validate.sh <agent> <fq.Class>` must print OK. 5. **Look at it**: `tools/preview.sh <agent> <fq.Class> /tmp/<agent>/x.png clip=<clip> times=0,6,12,18,24 views=f34,side,front w=<hitbox w> h=<hitbox h>`
then open the PNG with the Read tool. Check every attack clip at several times from at least two views, plus `clip=idle`, `clip=death times=0,10,20,30`, and `walk=1`.
Iterate until silhouettes read well and the motion is exaggerated/readable (aim for 2-3 revision passes on bosses and elites). `<agent>` = any unique short name (your out dir).
6. Register in your Group.java. Final check: `tools/check.sh <agent> <all your files + Group.java>` and `tools/validate.sh <agent> --group <fq.Group>`.
Do not wait for perfection on tiny minions: reach the quality bar quickly, then move on. Finish ALL creatures of your assignment.

## Coordinate system (DSL) — pixels, 16 px = 1 block
`x` = entity's LEFT (+), `y` = UP (+), `z` = FORWARD (+). Origin = ground centre. Top-level parts: pivot relative to ground centre. Child parts: pivot relative to parent pivot.
Box: `part.box(x0,y0,z0, dx,dy,dz)` min corner relative to the part's pivot + INTEGER sizes (`cbox(y0,dx,dy,dz)` = centred in x/z). So a leg with pivot at the hip:
`leg.box(-1.5f,-8,-1.5f, 3,8,3)` hangs 8px down. Rotations in degrees, right-handed: `rx+` = top tips forward / a hanging limb swings BACK (raise an arm forward/up: rx = -90..-170),
`ry+` = turns LEFT, `rz+` = rolls to the right (a hanging arm's bottom swings toward the entity's LEFT... i.e. rz+ on armR moves the hand inward/left). Test with preview, don't guess.
Rotation order Z,Y,X about the pivot. Pivot placement is everything for animation: put pivots at joints (shoulder, hip, neck, jaw hinge, tail root).
Entity height ≈ top of highest box * `spec.scale`/16 blocks; set `MobDef.size(w,h)` to match the visual size (Cultist 1.75 tall; Jaw Worm 1 tall;
big bosses 3-6 blocks). `ModelSpec.scale(s)` scales the whole model about the feet (handy for slime sizes / big bosses without huge pixel coords).
Texture: boxes are auto-packed; faces are painted procedurally (`color`, `grad(top,bottom)`, `noise`, `bevel`, `paint(Face,x,y,w,h,rgb)`, `px`, `band(y,h,rgb)`, `all(rgb)`)
and `glow(Face,x,y,w,h,rgb)` makes emissive pixels (eyes, flames, runes, energy). Face-local pixel coords: x right, y down as seen from outside the face; FRONT = +z side.
`flat()` disables directional shading; `inflate(g)` grows geometry only (use for layered clothing/armour plates).

## Animation (`Clip`)
`m.clip("name", lengthTicks)` then chain keys: `.rot(part, tick, rx,ry,rz[, Ease])`, `.pos(part,tick,x,y,z[,Ease])`, `.scl(part,tick,sx,sy,sz)`. Values are OFFSETS from rest pose.
Keys interpolate with the easing given on the arriving key (default SMOOTH). Use IN_CUBIC for wind-up->strike snaps, OUT_BACK for overshoot, OUT_ELASTIC/OUT_BOUNCE for settles.
`.loop()` for looping clips. A channel you don't key is untouched. Clips used as moves OVERRIDE the idle on the channels they key (blended in 3 ticks); end the clip near rest
(creature then blends back). Standard names: `idle`(loop), `walk`(loop, one stride cycle, driven by speed), `hurt`(additive flinch, ~10 ticks), `death` (its length = death duration, creature is removed afterwards),
`spawn` (optional). Extra loop clips can be selected as stance with `SpireMob.setBase("sleep")` (e.g. Lagavulin asleep) and `MobDef.base("sleep")` for the initial one.
Procedural extras: `m.proc((rig, ctx) -> { rig.rot("orb1", 0, ctx.age*6, 0); rig.pos(...); rig.scl(...); rig.show("shell", ctx.phase==0); })` (ctx: age, swing, swingAmt, hpFrac, phase, var, move, moveT...).
`m.walkBiped(legR,legL,armR,armL,legAmp,armAmp)`, `m.walkQuad(fr,fl,br,bl,amp)`, `m.bob(amp,speed)` for hovering things, `m.look("head",maxYaw,maxPitch)` head tracking.
Timing: 20 ticks = 1 s. StS attack anims are ~0.5-1.2 s: wind-up 6-12 ticks, strike 2-4 ticks, hold/recover 8-14 ticks. Keep anticipation visible!

## Behaviour (`MobDef`, `Move`, `Pattern`, `Fx`)
```java
MobDef.of("jaw_worm", "Jaw Worm", "ジョーワーム").game(1, 1).size(1.0f, 0.9f).hp(42).speed(0.25f).style(Style.INSECT).egg(0x6f8a5a, 0xc9b27a)
  .model(JawWorm::model).spawn(8, 1, 1, MobDef.Habitat.PLAINS)          // weight 0 (default) = egg/summon only
  .pattern(Pattern.random().add("chomp",25).add("thrash",30).add("bellow",45).maxRepeat("bellow",1).maxRepeat("thrash",2).notFirst("thrash"))
  .move(Move.of("chomp", 28).intent(Intent.ATTACK, 11, 1).reach(2.5f).at(14, Fx.melee(11)))
  .move(Move.of("bellow", 30).intent(Intent.DEFEND_BUFF).noTarget().at(14, (s,t)->{ s.addStrength(3); s.addBlock(6); }))
```
* HP = the StS HP (Ascension 0; use the lower number or the middle of the range). Damage numbers = StS numbers; the engine scales damage x0.5 (`Cfg.DMG`) and adds strength.
  `Move.intent(Intent, dmg, hits)` is only the displayed intent; the real hits are the actions you schedule with `at(tick, action)` (one `Fx.melee` per hit; tick offsets inside the move length).
* Pattern: `Pattern.cycle(ids)`, `Pattern.seq(loopFrom, ids)`, `Pattern.first(id, then)`, `Pattern.random().add(id,weight).maxRepeat(id,n).notFirst(id).notAfter(id,prev)` or any lambda `s -> id`
  using `s.getTurn()`, `s.lastMove()`, `s.repeatCount(id)`, `s.rand()`, `s.getPhase()`, `s.count(key)/inc(key)`. Reproduce the real wiki AI (percent weights, repeat limits, scripted openers).
* Move reach: melee default 2.8 blocks (`reach(r)`), ranged: `.ranged(12)` (needs line of sight), self-buffs: `.noTarget()` (execute at once when it has an enemy). `.moving()` lets it keep walking during the move
  (default is to stand still); `.noFace()`/`.faceUntil(tick)` control body tracking. Use `Fx.dash(speed, up)` at the strike tick for leaps/charges.
* Actions (lambdas `(SpireMob self, LivingEntity target) -> ...`): `Fx.melee(dmg[,range,arcDeg,onHit])`, `Fx.aoe(dmg,radius[,onHit])`, `Fx.hitTarget(dmg[,onHit])` (beams/spells), `Fx.shoot(kind,dmg,speed)`,
  `Fx.shootAt(...)`, `Fx.block(n)`, `Fx.strength(n)`, `Fx.heal(hp)`, `Fx.summon(self,id,dx,dz)` / `Fx.summonRing(id,n,r)`, `Fx.countAlive`, `Fx.sound`, `Fx.particles`, `Fx.ring`, `Fx.knock`,
  `Fx.rel(self,fwd,side,up)` (world point relative to body), `Fx.foes(self, aabb)`, debuffs `Fx.weak/vulnerable/frail/poison/slow/blind(victim, seconds)`, status cards `Fx.curse(v, Fx.Curse.SLIMED|DAZED|BURN|WOUND|VOID|DRAIN)`,
  thievery `Fx.steal(self, victim, nuggets)` + `Fx.vanish(self)` (escape), `Fx.teleportBehind`. Direct state: `s.addStrength/addBlock/heal/setPhase/setVar/setBase/stun(ticks)/forceNext(id)/flags/count/inc/minions()/getLeader()`.
  `s.addStrength(n)` also changes the damage shown/dealt. Block is StS-style (absorbs damage, cleared when the creature starts its next move; `MobDef.keepBlock()` to keep it).
* Passives/hooks: `.onHurt((s, src, dmg) -> newDmg)` (Curl Up, Angry, Thorns via `src.getEntity()`, Malleable, Mode Shift, Split when hp < 50%, Artifact...), `.onDeath`, `.onSpawn`, `.onTick` (server, every tick),
  `.onClientTick` (client particles/ambience, `s.level().addParticle`), `.onMoveEnd` (Ritual, Enrage ticks...). Boss: `.boss()` (boss bar, big drops), elite `.elite()`. Minions: `.minion()` (dies with summoner), `.flying()` flyers.
* Projectiles: define a kind ONCE inside def() (runs on both sides): `ProjKind.orb("fungi_spore", 0x8ac060, 3).trail(ParticleTypes.SPORE_BLOSSOM_AIR)`; `ProjKind.bolt(...)`, `.gravity(0.04)`, `.homing()`, `.impact((proj,pos)->...)`,
  or `ProjKind.custom(id, () -> modelSpec)` (model centred on origin, pointing +z). Ids must be unique (prefix with the creature id). Fire with `Fx.shoot("kind", dmg, speed)` at a tick.
* Natural spawning: `.spawn(weight, min, max, Habitat)` — normals weight 4-10 in a fitting biome (FOREST/PLAINS/SWAMP/SNOW/DESERT/MOUNTAIN/JUNGLE/CAVE/TAIGA/ANY), elites 1-2, bosses and minions 0 (spawn eggs only).
* Sounds: `.style(Style.X)` picks hurt/death/ambient vanilla sounds; add per-move sounds with `Move.sound(tick, SoundEvents.X, vol, pitch)` / `Fx.sound`.
* Ids: lowercase snake_case, globally unique. StS1 ids plain (`jaw_worm`); StS2 ids prefixed `s2_` (`s2_nibbit`). `.game(1|2, act)`.
* Intents: choose the Intent matching the wiki (ATTACK, ATTACK_DEBUFF, ATTACK_BUFF, ATTACK_DEFEND, DEFEND, DEFEND_BUFF, BUFF, DEBUFF, STRONG_DEBUFF, SUMMON, STUN, SLEEP, ESCAPE, UNKNOWN).
* Multi-hit moves: schedule one `Fx.melee`/shot per hit at its own tick, each synced to a visible strike in the clip. Phase/form changes: `s.setPhase(n)` + `ctx.phase` in `proc` (hide/show parts via `rig.show`).
* Never put Minecraft statics (SoundEvents, ParticleTypes...) in `model()`; keep them in `def()` and lambdas. Don't use reflection, threads, files, or static mutable state except ProjKind registration.
* If you are missing an engine feature, build it with the hooks above rather than editing core files; note it in your final report if impossible.

## Final report (keep it SHORT)
List of creatures done (ids), anything you couldn't do faithfully, and any core feature you wished for. No long prose.
