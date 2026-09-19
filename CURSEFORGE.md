# QuestAPI

A questing framework for Minecraft 26.3, built for Fabric and NeoForge. Quests can be written in Java or dropped in as datapack JSON, objectives track real game state instead of a bolted-on tracker, and the persistence and networking layers are already done for you.

## What's included

- Ten objective types: collect, consume, deliver, mine a block, place a block, craft, kill, tame, visit a dimension, visit a location. Most read straight from vanilla stats or inventory counts; block placement and taming have no vanilla event on either loader, so those two hook the one place in the game code where that's possible identically on both.
- Item, experience, and command rewards. Claim state lives server-side and gets checked before anything is granted, so a lost network packet or a server crash mid-claim can't hand out a second copy of the loot.
- Prerequisites: another quest's completion, an advancement, holding an item, or a minimum XP level.
- A quest book GUI with a category sidebar, a scrollable quest list, and a detail panel showing objective progress and reward previews. Built on vanilla widgets, no custom render pipeline.
- Progress persists through deaths, logouts, and restarts in a proper world-level save file. The client never decides a quest is complete on its own.

## Datapack quests

Drop a file in `data/<namespace>/questapi/quests/` and it loads at server start, in the same shape the Java API produces:

```json
{
  "id": "mypack:first_diamond",
  "title": { "text": "First Diamond" },
  "description": { "text": "Every miner's proudest moment." },
  "icon": { "id": "minecraft:diamond" },
  "category": "mypack:main",
  "objectives": [
    { "type": "questapi:collect_item", "item": "minecraft:diamond", "amount": 1 }
  ],
  "rewards": [
    { "type": "questapi:experience", "points": 50 }
  ],
  "auto_activate": true
}
```

Java-defined and JSON-defined quests sit in the same registry, so a modpack can add its own quests without writing a mod.

## For mod developers

The registry, the manager, and the GUI are three separate layers. Build your own screen against the same synced data if the default one doesn't fit, or register a new objective, reward, or condition type without touching this module's code.

```java
QuestApi.registry().registerQuest(Quest.builder(id)
    .title(Component.literal("First Diamond"))
    .objective(new CollectItemObjective(Items.DIAMOND, 1))
    .reward(new ExperienceReward(50))
    .autoActivate(true)
    .build());
```

The GitHub README has the full API reference and a Java/JSON walkthrough: [github.com/ryankshah/questapi](https://github.com/ryankshah/questapi)

## Development mode

Setting `dev=true` in `config/questapi.properties` loads an example quest tree (Java and JSON both) and the `/quests reset|unlock|progress` debug commands. Leave it `false` for a normal install; none of that content or those commands touch production quests.

## Loaders and requirements

Fabric and NeoForge, both targeting Minecraft 26.3 on Java 25. The multiloader split only touches networking registration and platform hooks; quest logic runs the same on both.

## License

CC0-1.0. Do whatever you want with it.

## Source and issues

[github.com/ryankshah/questapi](https://github.com/ryankshah/questapi)
