## Version 0.2.20
- Fix jungle bamboo generation on worlds with multiple biomes
- Fix scaffold teleportation behaviour bug
- Fix bonemeal issues with Cornflower and Lily of the Valley
- Fix Stripped Log issue with Oceanic Expanse

## Version 0.2.19
- Add missing "Next" text to the gamemode switcher
- Fix serverside crash with Trident

## Version 0.2.18
- Actually fix Honey Bottle
- Add option to disable 1.13 features when Oceanic Expanse is enabled (default true)
- Add campfire dispenser behavior (thank you zeng-github01)
- Fixed Smithing Recipes added by CraftTweaker not matching damaged tools

## Version 0.2.17
- Fix crashing on startup
- Stop bees from trying to fly while pollinating a flower
- Add overlay for gamemode switcher

## Version 0.2.16
- Barrels now have comparator output
- F3 + F4 now changes gamemode like in 1.14+ (graphic element is missing for now, but still a nice hotkey to have)
- Made Bamboo and Pandas more common

## Version 0.2.15
- Fixed crash with Loom

## Version 0.2.14
- Fixed crash with villager trading screen

## Version 0.2.13
- Fixed crash with Honey Bottle

## Version 0.2.12 - A few bugfixes
- Fixed Grindstone crash with broken mod enchantments #332
- Fixed Bees not leaving the hive unless blocked in #320 thanks to Slayer321
- Fixed missing scaffolding sounds
- Fixed scaffolding not breaking instantly
- Fixed incorrect dispenser behavior with shears
- Fixed bug with CarryOn duplicating Beehives
- Snowman now drop their pumpkins when being sheared (configurable) #326
- Walls now connect to glass like in Vanilla thanks to tyler-docherty
- Fixed typo in "Block of Netherite" (was "Netherite Block") thanks to SeanMC

## Version 0.2.11 - Item rarities + Bugfix:
- *Item rarities* - Vanilla items in 1.12 now have colored names like in 1.13+
- Fixed bamboo spawning
- Fixed Ancient Debris/Netherite spawning
- Fixed Smithing Table only upgrading undamaged items and not carrying over nbt/durability #305
- Fixed Beehives not entering when non-air block in front of entrance #303

## Version 0.2.10 - Scaffolding + fix a crash:
- *Scaffolding* - Now craftable and works like in vanilla
- Vines can now be climbed without a block behind them like in vanilla
- Fixed #300

## Version 0.2.9 - OTG compatibility, Dynamic Trees compat, bug fixes:
- Beehives now spawn on all trees added by Biome Bundle and should work for other biome packs on OpenTerrainGenerator
- CraftTweaker functions now use IIngredient instead of IItemStack wherever applicable
- Fixed (I hope): #86, #201, #202, #207, #218, #232, #250, #259, #263, #264, #266, #269, #286, #292, #295, #296

## Version 0.2.7.4 - Bugfix:
- Fixed a crash with villager trading crashing servers
- Try to fix a bug with MIA

## Version 0.2.7.3 - Bugfix:
- Fixed a bug that disabled Smithing table and Composter guis when stonecutter was disabled. Fixes #260
- Fixed a crash with Pandas
- Added Spanish (Spain) translations thanks to jahirtrap

## Version 0.2.7.2 - Bugfix:
- Fixed a server crash with netherite

## Version 0.2.7.1 - Bugfix:
- Added config option to disable 1.14 villager screen if another mod you prefer overrides it
- Fixed crashes with Loom and Stonecutter

## Version 0.2.7 - Villager Gui, Fluidlogging compat, and more translations:
- Composter - Fixed a bug with CraftTweaker that messed with assigned chances
- CraftTweaker - Fixed bugs with Blast Furnace and Smoker
- Fluidlogged - Walls are now fluidloggable
- Grindstone - Fixed a dupe glitch with stackable enchanted items. Fixes #252
- Lantern - Fixed a bug with lanterns switching position, does this fix #?
- Netherite Armor - Now has Knockback Resistance.
- Panda - Fixed breeding bug
- Villager Gui - Trading with any villager will now use the gui screen from 1.14.
- Wooden Trapdoors - Fixed textures on the Spruce, Birch, Jungle, and Acacia variants.
- Soul Fire Torch - Fixed broken recipe. Fixes #237
- Stripped Wood - Fixed stripped logs not being craftable into planks.
- Stripped Wood - Fixed stripped logs not being smeltable into charcoal.
- Translations! Future MC can now be played in: Argentinian Spanish, Venezuelan Spanish, Colombian Spanish, Japanese, French, Chinese simplified, and Russian.
- Thanks to Lucian, Sasha, Robolightning, daifucu, Samlegamer, and yilangyo for making all the translations

## Version 0.2.6.1 - Bug fix:
- Beehives - Fixed excessive beehive spawning. Fixes #220

## Version 0.2.6 - Bug fix:
 Beehives - Removed broken Dynamic Trees compat

## Version 0.2.5 - Bug fixes and minor additions:
 Seagrass - Removed from config file. Fixes #195
 Smoker - Now works properly with CraftTweaker. Fixes #217
 Soul Torch & Soul Lantern - Now craftable in survival. Fixes #200
 Stonecutter - Added some missing default recipes. Fixes #209
 Stripped Wood - Attempting to strip a log whose stripped variant is disabled in the config no longer causes a crash. Fixes #216
 Suspicious Stew - No longer causes a crash when disabled in the config. Fixes #194
 Trident - Drops from Elder Guardians as intended. Fixes #198
 Trident - Enchantments now work correctly. Fixes #212
 World Type - No longer shows test world type. Fixes #208

## Version 0.2.4.1 - Bug fixes:
 Barrel - Fixed an issue that made opening animations and sounds weird in multiplayer.
 Future MC - Fixed a crash with Forge Gradle 3.0.
 Smithing Table - Fixed a multiplayer error with sounds.
 Stonecutter - No longer deletes items if Quark is not installed.
 Trapdoors - Now craft properly.
 Wood - Fixed ore dictionary entries for wood blocks (6 sided bark blocks).

## Version 0.2.4 - Bug fixes and more mod compat:
 *Smithing Table JEI Compatibility* - Recipes for the Smithing Table are now visible in JEI. Should reduce a bit of confusion.
 *Composter Table JEI Compatibility* - Items now show their chance of adding a layer of compost in JEI.
 *Pigstep* - New music disc from 1.16. Creative only at the moment
 Ancient Debris - Fixed generation only occurring in areas with positive X coordinates. Fixes #159
 Ancient Debris - Now appears in creative menu. Fixes #181
 Bamboo - Added a biome check to world generation to fix it from spreading out of Jungle biomes. Fixes #177
 Barrels - Now open like in 1.14.
 Barrels - Contents now save properly. Fixes #175
 Chains - Now use the sounds from 1.16.
 Fish - No longer spawn in insane numbers. Fixes #137
 Lanterns - Improved placement to work underneath hoppers and in a few other cases where it would previously fail. Fixes #178
 Lanterns - Can now be placed underneath and on top of trapdoors.
 Pam's Harvestcraft - Seeds are now compostable.
 Pufferfish - No longer kicks you from server. Fixes #182
 Netherite Tools - Adjusted values to match 1.16. Fixes #163
 Smooth Stone - Now correctly gives 6 slabs instead of 3 when crafting. #158
 Stonecutter - Mossy stone now crafts into Mossy stone wall instead of Cobblestone wall. Fixes #170
 Stonecutter - Added a few missing recipes. Fixes #172
 Stonecutter - Added recipes for Seared Bricks when Tinkers Construct is installed.
 Sweet Berry Bush - No longer drops berries when broken in creative mode. Fixes #167

## Version 0.2.3.2 - Hotfix and more mod compat:
 *Tinkers Construct Compat* - Tinkers axes can now strip logs.
 Netherite Block - Now has a crafting recipe. Fixes #151
 Netherite - Fixed ore dictionary issues. Fixes #150
 Fixed a server crash. Fixes #152
 Quark Compat - Bark blocks are now strippable

## Version 0.2.3.1 - Hotfix:
 Fix server crash (reportedly didn't actually fix it)
 Hopefully fix Vivecraft incompatibility

## Version 0.2.3 - Bug fix + mod compatibility update:
- *Actually Additions Compat* - Bees now seek out the Black Lotus for pollen and grow Canola, Flax, Rice, and Coffee crops.
- *Better With Mods Compat* - Cauldrons now recognize campfires as a fire source.
- *Fixed World Corruption* - When updating from 0.1.13 to 0.2.2, the world is no longer corrupted. Fixes #147
- *Netherite Gear* - Lava resistant gear from 1.16 that is better than diamond.
- *Pam's Harvestcraft Compatibility* - Bees now grow crops from Pam's Harvestcraft.
- *Plants Compatibility* - Bees seek out the flowers from the Plants mod for pollen and grow the crops from Plants.
- *Portuguese Translation* - Added Portuguese translation
- *Smithing Table Functionality* - Smithing table can now be used to upgrade equipment. CraftTweaker compatibility!
- Barrel, Blast Furnace, Smoker - Fixed a dupe glitch. Fixes #141
- Bee - Fixed a crash with Beetroots. Fixes #142
- Bee Nests - Changed default biome spawning to match 1.15.
- Campfire - Fixed empty recipes not being removed.
- Honey Blocks - No longer stick to slime blocks (even without Quark). Fixes #146
- Lantern - Fixed placement a bit more to allow placement on trapdoors.
- Soul Torch & Lantern - Changed names to match new snapshots.
- Soul Torch - Fixed a particle that caused issues with mip maps.
- Trapdoors - Now craftable without Quark. Fixes #145
- Trapdoors - Added OreDictionary entries. Fixes #148
- Wood blocks - Now strippable. Fixes #144

## Version 0.2.2 - Bug fix update:
- Bee - Fixed a crash with beetroots, pumpkins, and melons. Fixes #139
- Composter - Optimized item rarity map and reduced memory footprint by 3 bytes per compostable item
- Future MC - Fixed the old remapper.
- Loom - Fixed a crash with banner pattern items
- Stonecutter - Fixed a dupe glitch
- Stonecutter - Fixed a glitch that allowed players to not take the entire output, deleting the remaining items

## Version 0.2.1 - MAJOR UPDATE ALERT!!!! - New things:
- *Banner Patterns* - Banner patterns can be used in the Loom.
- *Bell* - Rings just like in 1.14. Creative only at the moment because villagers don't trade them.
- *Cartography Table* - Decorative only.
- *Chain* - Decorative block from 1.16
- *Cod, Pufferfish, Salmon, Tropical Fish* - Fish mobs that spawn in the water! Fixes #110 and #115
- *Config* - Completely redone. You will have to set all the options again. Many options have been added to fine-tune Future MC for your needs.
- *Fish Buckets* - Collect fish and relocate them with buckets!
- *Loom Functionality* - Loom can be used to craft banners. Fixes #133
- *Smooth Sandstone & Smooth Red Sandstone* - Decorative blocks from 1.14
- *Trapdoors* - All vanilla wood variants have trapdoors now. Fixes #127
- *Wood Blocks* - All vanilla wood and stripped wood variants have wood blocks (the ones with all six sides bark)
- Bamboo - Fixed hitbox and added option for tight hitbox and fixed bug involving OptiFine.
- Bee - Changed CraftTweaker to use IBlockState instead of IItemStack. Fixes #92
- Bee - New AI! Fixes #102
- Bee - Adjusted eye level. Fixes #104
- Bee Nests - Simplified world gen config and added more default biomes.
- Bee Nests - Now generate if there's flowers nearby. Fixes #101
- Bee Nests & Beehives - Shears and bottles can now be used to collect honeycomb and honey.
- Blast Furnace & Smoker - Added comparator output. Fixes
- Blast Furnace & Smoker - Fixed CraftTweaker #80 and #72
- Blast Furnace & Smoker - Now detect ores and foods from other mods.
- Bounding Boxes Render Underwater - Fixes #132
- Campfire - Now has particles and crafttweaker recipes work properly now. Fixes #116
- Campfire - Renders sides properly when OptiFine is installed. Fixes #111
- Campfire - Cooking items in creative mode no longer consumes them from your hotbar. Fixes #107
- Campfire - No longer resets facing when ignited. Fixes #117
- Future MC - Changed modid from "minecraftfuture" to "futuremc". This shouldn't break any of your old worlds, but you will need to fix your crafttweaker scripts.
- Grindstone - No longer blocks light.
- Honey Block - Fixed the piston mechanics. Fixes #126
- Honey Bottle - Fixes #82
- Internal Refactors - Fixes #79, #106, #113 (partially), #121, #128, #131, #138
- Internal Refactors - Changed properties for many blocks to be more accurate to Minecraft 1.13+
- Iron Golem - Can now be repaired with iron ingots.
- Iron Golem - Now cracks as its health decreases.
- Lantern - Fixed placement to allow for placing on iron bars and other thin blocks
- Lantern - Adjusted sounds to match 1.14.
- Legacy Bamboo Jungle - Removed from the mod. Worlds that had this biome will still have bamboo jungles from FutureMC, they will just show up as plains in F3.
- Recipes - Changed recipe IDs to follow minecraft naming convention.
- Sounds - Added subtitles for all sounds. Fixes #97
- Stonecutter - Fixed a crash #78
- Stonecutter - Fixed a few incorrect recipes
- Stonecutter - Removes empty recipes during postInit. Fixes #76
- Suspicious Stew - Supports custom potion effects. Fixes #124
- Walls - Much more configurable, you can now disable individual types of walls.
- Wither Rose - Now drops correctly.
- Wither Rose - Now places on the correct blocks again.

## Version 0.2.03 - Bug fix update:
 Trapdoors - No longer have strange rotations.
 Fixed CraftTweaker issues when using remove functions on certain blocks
 Readjusted attributes of several blocks to be consistent with 1.13+
 Fixed BiomeTweaker crash #113
 Fixed Cascading worldgen lag #128
 Added Smooth Sandstone and Smooth Red Sandstone
 Changed block id of Mossy Stone Brick Wall and End Stone Brick Wall to be consistent with 1.13+

## Version 0.2.02 - Bug fix update:
 Bark blocks - Fixed models
 Campfire - Fixed missing crackle particles
 Trident - No longer passes through entities
 Fixed missing translation keys on some items

## Version 0.2.01 - Test update:
 Some things from the 0.2.0 update...
 Cod, Pufferfish, Salmon, Tropical Fish, Trapdoors, Loom functionality,
 Soul fire torch/lantern/soil, bamboo fixes, blast furnace/smoker changes,
 new modid, beehive/bee nest adjustments, campfire particles/sounds, new config,
 honey bottle adjustments, iron golem cracking, lantern sounds, smooth stone fix,
 stonecutter fixes, suspicious stew adjustments, wither rose fixes...

 Note that Bee AI has been broken in this test update.

## Version 0.2.0 - Big update! New things:
 *Cod* - Fish from 1.13 that spawns in ocean biomes.
 *Pufferfish* - Fish from 1.13 that spawns in deep ocean biomes and puffs up when threatened.
 *Salmon* - Fish from 1.13 that spawns in river and deep ocean biomes.
 *Tropical Fish* - Colorful fish that spawns in deep ocean biomes.
 *Wooden Trapdoors* - All wood variants have their own trapdoors from 1.13.
 *Loom Functionality* - You can now use the Loom to craft banners.
 *Soul Fire Torch* - Creative item and block from 1.15.
 *Soul Lantern* - Creative item and block from 1.15.
 *Soul Soil* - Creative item and block from 1.15.
 Bamboo - Added bamboo saplings from 1.14.
 Bamboo - Hitbox is now like 1.14's hitbox.
 Bamboo - Added config option to tighten the hitbox to its collision box or to leave it as it is in 1.14.
 Bee - Updated AI to match 1.15.2.
 Bee - Split classes into separate files to make code more readable.
 Bee - Changed CraftTweaker methods to use IBlockState instead of IBlock.
 Bee - Fixed a bug that prevented removal of certain flowers as pollinateable.
 Blast Furnace & Smoker - Now detect ores and foods from other mods.
 Blocks, Items, Tile entities - Adjusted the ids of certain things (they now follow naming convention) and made things more accurate (flowerblack -> wither_rose, flowerwhite -> lily_of_the_valley, sweetberry -> sweet_berries, etc.)
 Beehives and Bee nests - Can now be harvested by dispensers.
 Campfire - No longer crashes if you are burned to death on it while running OptiFine.
 Campfire - Added particles.
 Campfire - Added missing crackle sounds.
 Config - Completely redone. You may need to set some of your options again. It's in a new file called "futuremc.cfg", so you can look back if you want to migrate from "future-mc.cfg".
 Config - Categorized by minecraft version instead of "general" and "flowers". Options are now sorted alphabetically.
 Config - Added a bunch of new options, removed a few options that I feel do not fit in the mod.
 Config - Added much finer configuration for blocks and entities.
 Config - Fish spawning is configurable.
 Future MC - Changed modid from "minecraftfuture" to "futuremc". This shouldn't break any of your old worlds, but you will need to fix your crafttweaker scripts.
 Honey Bottle - Now shows hunger and saturation information for mods like Appleskin.
 Honey Bottle - No longer empties in creative after consumption.
 Iron Golem - Can now be repaired with iron ingots.
 Iron Golem - Now cracks as its health decreases.
 Legacy Bamboo Jungle - Removed from the mod. Worlds that had this biome will still have bamboo jungles from FutureMC, they will just show up as plains in F3.
 Lantern - Adjusted sounds to match 1.14.
 Panda - Removed some dangerous code.
 Smooth Stone - Adjusted hardness to match 1.14.
 Stonecutter - Fixed some blocks having the wrong amount of outputs.
 Suspicious Stew - Now supports custom potion effects.
 Trident - Updated sounds.
 Trapdoors - Added from 1.14
 Trapdoors - Each individual wood type can be enabled/disabled.
 Walls - Much more configurable, you can now disable individual types of walls.
 Wither Rose - Now drops correctly.
 Wither Rose - Now places on the correct blocks again.

 Future MC now depends on Shadowfacts' Forgelin.

## Version 0.1.13 - Patch update:
 Barrel - Fixed placement rotation that I messed up around 0.1.11.
 Bees, Blast Furnace, Campfire, Composter, Smoker, Stonecutter - Added removeAll functions. They aren't all called removeAll, so check the Wiki for the names.
 Bee Nests - Added config option to allow the nests to spawn in more biomes.
 Stonecutter - Now has functionality!
 Stonecutter - CraftTweaker compatibility!
 Stonecutter - JEI compatibility!

## Version 0.1.12 - Campfire Functionality and Honey Blocks! New things:
 *Honey Block* - A super sticky block from 1.15 that limits movement. You can also slide down the sides of it!
 *Honeycomb Block* - A decorative/storage block for Honeycombs.
 Campfire - Can now cook items.
 Campfire - Can now be rotated.
 Campfire - Can now be put out with a shovel.
 Campfire - JEI compatibility!
 Campfire - CraftTweaker compatibility, view on Wiki.
 Campfire - Now properly damages entities that stand on it.
 Blast Furnace and Smoker - JEI compatibility!
 Bees - Added CraftTweaker functions on the Wiki.
 Bees - No longer freeze without hive.
 Bee Hive - No longer causes issues with mods like CubicChunks or Biome Bundle.
 Bee Hive - Can now spawn on big trees.
 Bee Hive & Bee Nest - Can now be silk touch harvested with bees and honey still inside.
 Honey Bottle - Glass bottle stays in crafting table after use in recipes.

 Future MC now uses a coremod. It places hives on trees.

## Version 0.1.11 - Bees? New things:
 *Bees* - Cute flying insects from 1.15 snapshots that pollinate crops. Possible flower targets can be added and removed with CraftTweaker scripts.
 *Honeycomb* - Resource from 1.15 snapshot obtained from beehives.
 *Bee Hive and Bee House* - Blocks from 1.15 where bees come to make honey.
 *Honey Bottle* - New food from 1.15 snapshot collected from beehives.
 Trident - Now properly travels through water.
 Trident - No longer "bounces" in place when hitting certain blocks like snow layers.
 Trident - Now only applies impaling enchantment to mobs that are wet.
 Dyes, Bamboo, Sweet Berries - No longer show up in the dye oredictionary when not enabled.
 Blue Ice - Fixed sounds and hardness.
 Bamboo - Fixed hitbox bug with OptiFine.
 Added more language support.

## Version 0.1.10 - It's been a while:
 *Blue Ice* - Most slippery type of ice in the game.
 Dyes - Fixed names being switched between blue dye and white dye.
 Composter - CraftTweaker now uses IIngredients instead of IItemStacks.

## Version 0.1.9b - Patch Update:
 Grindstone - Actually fixed the dupe glitch this time.
 Panda - Now retaliates like in 1.14.

## Version 0.1.9 - Patch Update:
 Grindstone - Fixed dupe glitch.
 Grindstone - Fixed durability issue with enchanted items.
 Trident - Fixed NPE when disabled.
 Trident - Tweaked Channeling enchantment to behave like in 1.13.
 Added a way to show incomplete features of the mod, such as the Scaffolding block and Loom guiType. Check the README on GitHub!

## Version 0.1.8b - Quick Patch:
 Bamboo Jungle - Added config option to re-enable the old Bamboo Jungles.

## Version 0.1.8 - Patch Update:
 Bamboo - Now has its sounds from 1.14.
 Campfire - Now has crackling sounds.
 Composter - Now works on servers.
 Composter - Now has particle effects.
 Grindstone - No longer causes clientside crash when using on server.
 Grindstone - Now works on servers.
 Grindstone - Awards the correct amount of xp for enchantments, like in 1.14.
 Pandas - Now properly attacks.

## Version 0.1.7 - Pandas! New things:
 *Pandas* - Rare animal from 1.14 whose favorite food is bamboo.
 *Riptide* - Added the riptide enchantment to the Trident.
 Trident Enchantments - Now only work when the user is wet, just like in 1.13.
 Blast Furnace and Smoker - CraftTweaker compatibility!
 Composter - Items can now be inserted through automation, and bonemeal can also be collected through automation.

## Version 0.1.6 - Patch update:
 Composter - Adjusted the default list of valid items.
 Composter - CraftTweaker compatibility!
 Composter - Fixed NullPointerException with composter sounds.
 Bamboo - Adjusted behavior to be more like 1.14.
 Bamboo - Now regrows if destroyed.
 Bamboo - Now shows in creative tab.
 Bamboo - No longer tries to grow through blocks.
 Bamboo - Now generates in Jungles instead of my old biome.
 Grindstone - Items now keep other NBT data, such as names.
 Grindstone - Items can now be individually disenchanted.
 Grindstone - Enchanted Books can now be disenchanted.
 Smoker & Blast Furnace - Fixed buckets being consumed when using lava.
 Smoker & Blast Furnace - Fixed fuel left icon.
 Smoker & Blast Furnace - Fixed XP not given when smelting items.
 Bamboo Forest - Removed. Bamboo will be found in Jungles, like in 1.14.
 Bamboo Forest - Will be re-added in the future to be more like 1.14 Bamboo Forest.
 Barrel - Fixed an issue with the order of slots.

## Version 0.1.5 - Composter! New things:
 *Composter* - Block from 1.14 that allows you to compost things into bonemeal.
 *Vanilla Tabs* - New Config option! If enabled, Future MC items go into Vanilla Creative tabs instead of the Future MC tab.
 Berry Bush - Tweaked the bush damage.
 Stonecutter - Removed a GUI that was not supposed to be there.
 Loom - Fixed a crash with FoamFix.

## Version 0.1.4 - Patch update:
 Grindstone - Now works properly and Curses persist. Got rid of ghost client EXP orbs.
 Berry Bush - Now does damage when it has berries.

## Version 0.1.3 - Bamboo! New things:
 *Bamboo* - Plant from 1.14 that spawns in Bamboo Jungles.
 *Bamboo Jungle* - Biome from 1.14 where Bamboo can be found.
 *Grindstone* - Block from 1.14 that disenchants and repairs items.
 *Smooth Quartz* - Block from 1.14 that has a smooth texture on all sides.
 Smoker & Blast Furnace - Fixed bug with progress arrow.
 Trident - Can now be shot by dispensers.
 Campfire - Now re-lightable by using a Fire Charge.
 Campfire - Now uses current model from 1.14 instead of the old one.
 Campfire - Now drops 2 Charcoal instead of itself like in 1.14.
 Barrel - Disables in favor of the Charm mod.
 Lily of the Valley - Tweaked worldgen to match vanilla flowers.
 Cornflower - Tweaked worldgen to match vanilla flowers.
 Sweet Berry Bush - Tweaked worldgen to match vanilla flowers.
 Suspicious Stew - Fixed bug when eaten from creative menu.
 Lantern - No longer causes fences to attach to it.
 Campfire - Removed tile entity.
 *Deutsch* - Added German language support.
 *Pусский* - Added Russian language support, thanks to Mr_Krab on GitHub.

## Version 0.1.2c - Quick Patch:
 Fixed Campfire not burning out in rain when config option was enabled
 Allowed usage of mod in versions as old as Forge 14.23.5.2776
 Fixed config not working in game

## Version 0.1.2b - Quick Patch:
 Fixed stonecutter model config

## Version 0.1.2 - Better Furnaces! New things:
 *Blast Furnace* - Block from 1.14 that smelts ores at twice the speed of a furnace, and uses half the fuel! (efficient fuel is toggleable in config)
 *Smoker* - Block from 1.14 that cooks food at twice the speed of a furnace, and uses half the fuel! (efficient fuel is toggleable in config)
 *Smithing Table* - Decorative block from 1.14.
 *Fletching Table* - Decorative block from 1.14.
 Stonecutter - Back in the creative tab.
 Stonecutter - Added config option to use old model from Minecraft PE.
 Berry Bushes - No longer crash game when bonemealing past fourth stage.
 Suspicious Stew - Now gives a totally random effect. Can be set to use the crafted flower as its effect in the config.
 Sweet Berries - Added proper OreDictionary entries to work with Thermal Expansions' Phytogenic Insolator.
 *Español* - Things from this mod will now translate into Mexican Spanish.

## Version 0.1.1 - Tridents! New things:
 *Throw-able Tridents* - Tridents can now be thrown as ranged weapons.
 *Trident Enchantments* - Tridents now have 3 special enchantments: Loyalty, Channeling, and Impaling.
 Flowers - Adjusted natural generation to generate in rarer patches, because lonely flowers in every chunk was odd-looking.
 I will now have wiki pages for this mod's content.

## Version 0.1.0 - Planned changes:
 *Stonecutter Functionality* - Stonecutter will now be able to craft blocks.
 *New Config file* - Config file is now a proper Forge annotated config file that can be edited through Mod Options.
 Lanterns - Changed block placement to use a more efficient boolean property, and if possible, will hang from the ceiling block if the   below block is broken.
 Campfire - Now properly stays lit and unlit when the chunks unload.
 Config file - Fixed many broken options.
 Dyes - Switched to a single item ID with metadata. Oredictionary now properly implemented!
 Walls - No longer break instantly.
 Stripped Logs - Updated texture to use current log textures.
 ...more may be added! I am close to finishing the update.

## Version 0.0.9 - New things:
 *Walls* - Walls that were added in 1.14 are now craftable decorative blocks. 12 new wall variants!
 Trident - Animation now properly disabled by default.
 Stonecutter - Preparing to add functionality.

## Version 0.0.8 - Big update! New things:
  *Stripped Logs* - A wooden log that gets its fancy new look when activated with an Axe.
  Campfire - Patched this block, as it was broken before. Now works as intended!
  Trident - Preparing to add some functionality, might come in a few updates or so.

## Version 0.0.7 - Campfire! New things:
   *Campfire* - A decorative block that emits a small amount of light and smoke. Cheap to craft, early game light source.
   Stonecutter - Now renders correctly when placed next to solid blocks. Torches can no longer be placed on top of the stonecutter.
   Sorry this update took so long, I spent too long trying to add custom particles to the Campfire. I failed :(

## Version 0.0.6 - Berry Bushes! New things:
    *Sweet Berry Bush* - Generates in Taiga biomes, grows berries that can be harvested and eaten.
    *Sweet Berries* - New food item that can restore 2 hunger points, just like a Melon slice. Can be harvested from Sweet Berry Bushes.

## Version 0.0.5 - New things:
    Lily of the Valley - Now generates in forest biomes.
    Cornflower - Now generates in the Plains biome.
    *Loom* - New block that does not have functionality.

## Version 0.0.4 - Finally Done! New things:
    Server Proxy - Exists once again. Fixes issue # 1.
    Wither Rose - Now has an option in the config to toggle whether it does damage or not. Now placeable on soul sand.
    Barrel - Now craftable: Can store items like the one from 1.14 as well! Drops inventory when destroyed.
    Stonecutter - No longer breaks instantly.
    Lantern - No longer breaks instantly.
    Trident - Now has durability and deals damage.
    *Suspicious stew* - Food item that gives a potion effect when consumed. Now fully implemented!

## Version 0.0.3 - An update! New things:
    Lantern - No longer placeable on plants. Planning to limit placement even further so it cannot be placeable on slabs or stairs.
    Wither Rose - Now deals damage and inflicts Wither effect. Now emits particles.
    Dyes - Now craftable from their respective flowers and equivalents.
    *New Config File* - All features are toggleable in this config file.
    Many small refactors all around to accomadate config file.
    
## Version 0.0.2 - An update! New things:
    Lantern - Now properly hangs from blocks like the fence.
    *Trident* - A non-functional combat item that is planned to deal 9 damage and have an attack speed
                of 1.1.
    *Cornflower* - Decorative blue flower. Planned to be crafted into Blue Dye.
    *Blue Dye* - A substitute for Lapis Lazuli in crafting recipes that require a blue dye.
    *Lily of the Valley* - Decorative white flower. Planned to be crafted into White Dye.
    *White Dye* - A substitute for bonemeal in crafting recipes that require a white dye.
    *Wither Rose* - Harmful dark flower that is planned to inflict Wither when the player walks over it. 
                    Planned to be crafted into Black Dye.
    *Black Dye* - A substitute for Ink Sacs in recipes that require a black dye.

## Version 0.0.1 - First version! Blocks added:
    *Lantern* - An upgraded light source that can hang on the ceiling or sit on the floor.
    *Stonecutter* - A decorative block. Recipe not yet implemented.
