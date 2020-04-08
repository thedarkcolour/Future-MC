package thedarkcolour.futuremc.registry

import net.minecraft.block.Block
import net.minecraft.client.renderer.entity.RenderIronGolem
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntitySpawnPlacementRegistry
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Biomes
import net.minecraft.init.Blocks
import net.minecraft.init.MobEffects
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.EntityEntry
import net.minecraftforge.fml.common.registry.EntityRegistry
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import thedarkcolour.core.util.registerEntity
import thedarkcolour.core.util.registerEntityModel
import thedarkcolour.core.util.runOnClient
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.client.color.WaterColor
import thedarkcolour.futuremc.client.particle.CampfireParticle
import thedarkcolour.futuremc.client.particle.SoulFlameParticle
import thedarkcolour.futuremc.command.FastGiveCommand
import thedarkcolour.futuremc.config.FConfig.buzzyBees
import thedarkcolour.futuremc.config.FConfig.updateAquatic
import thedarkcolour.futuremc.config.FConfig.useVanillaCreativeTabs
import thedarkcolour.futuremc.config.FConfig.villageAndPillage
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.entity.bee.BeeRenderer
import thedarkcolour.futuremc.entity.fish.cod.EntityCod
import thedarkcolour.futuremc.entity.fish.cod.RenderCod
import thedarkcolour.futuremc.entity.fish.pufferfish.EntityPufferfish
import thedarkcolour.futuremc.entity.fish.pufferfish.RenderPufferfish
import thedarkcolour.futuremc.entity.fish.salmon.EntitySalmon
import thedarkcolour.futuremc.entity.fish.salmon.RenderSalmon
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish
import thedarkcolour.futuremc.entity.fish.tropical.RenderTropicalFish
import thedarkcolour.futuremc.entity.irongolem.LayerIronGolemCrack
import thedarkcolour.futuremc.entity.panda.EntityPanda
import thedarkcolour.futuremc.entity.panda.RenderPanda
import thedarkcolour.futuremc.entity.trident.EntityTrident
import thedarkcolour.futuremc.entity.trident.RenderTrident
import thedarkcolour.futuremc.item.ItemGroup
import thedarkcolour.futuremc.item.ItemSuspiciousStew

object RegistryEventHandler {
    @SubscribeEvent
    fun onBlockRegistry(event: RegistryEvent.Register<Block>) {
        FutureMC.TAB = if (useVanillaCreativeTabs) {
            CreativeTabs.MISC
        } else {
            ItemGroup()
        }

        FBlocks.init()
    }

    @SubscribeEvent
    fun onItemRegistry(event: RegistryEvent.Register<Item>) = FItems.init()

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun postItemRegistry(event: RegistryEvent.Register<Item>) {
        for (location in ForgeRegistries.ITEMS.keys) {
            FastGiveCommand.registryKeys.getOrPut(location.path, location::toString)
        }
    }

    @SubscribeEvent
    fun onEntityRegistry(event: RegistryEvent.Register<EntityEntry>) {
        if (updateAquatic.trident) {
            registerEntity("trident", EntityTrident::class.java, 32, 1)
        }
        //if (updateAquatic.drowned) {
        //    registerEntity("drowned", EntityDrowned::class.java, 36, 2, 9433559, 7969893)
        //    EntityRegistry.addSpawn(EntityDrowned::class.java, 5, 1, 1, EnumCreatureType.MONSTER, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.FROZEN_OCEAN)
        //    EntityRegistry.addSpawn(EntityDrowned::class.java, 100, 1, 1, EnumCreatureType.MONSTER, Biomes.RIVER, Biomes.FROZEN_RIVER)
        //}
        if (villageAndPillage.panda && villageAndPillage.bamboo.enabled) {
            registerEntity("panda", EntityPanda::class.java, 36, 3, 15198183, 1776418)
            EntityRegistry.addSpawn(
                EntityPanda::class.java,
                1,
                1,
                2,
                EnumCreatureType.CREATURE,
                Biomes.JUNGLE,
                Biomes.JUNGLE_HILLS,
                Biomes.MUTATED_JUNGLE
            )
        }
        if (buzzyBees.bee.enabled) {
            registerEntity("bee", BeeEntity::class.java, 32, 4, 16770398, 2500144)
        }
        if (updateAquatic.fish.cod.enabled) {
            registerEntity("cod", EntityCod::class.java, 32, 5, 12691306, 15058059)
        }
        if (updateAquatic.fish.pufferfish.enabled) {
            registerEntity("pufferfish", EntityPufferfish::class.java, 32, 6, 16167425, 3654642)
        }
        if (updateAquatic.fish.salmon.enabled) {
            registerEntity("salmon", EntitySalmon::class.java, 32, 7, 10489616, 951412)
        }
        if (updateAquatic.fish.tropicalFish.enabled) {
            registerEntity("tropical_fish", EntityTropicalFish::class.java, 32, 8, 15690005, 16775663)
        }

        runOnClient {
            if (updateAquatic.trident) {
                registerEntityModel(::RenderTrident)
            }
            if (villageAndPillage.panda && villageAndPillage.bamboo.enabled) {
                registerEntityModel(::RenderPanda)
            }
            if (buzzyBees.bee.enabled) {
                registerEntityModel(::BeeRenderer)
            }
            if (updateAquatic.fish.cod.enabled) {
                registerEntityModel(::RenderCod)
            }
            if (updateAquatic.fish.pufferfish.enabled) {
                registerEntityModel(::RenderPufferfish)
            }
            if (updateAquatic.fish.salmon.enabled) {
                registerEntityModel(::RenderSalmon)
            }
            if (updateAquatic.fish.tropicalFish.enabled) {
                registerEntityModel(::RenderTropicalFish)
            }
            // of course they do :)
            if (buzzyBees.ironGolems.doCrack) {
                registerEntityModel { manager ->
                    val renderer = RenderIronGolem(manager)
                    renderer.addLayer(LayerIronGolemCrack(renderer))
                    renderer
                }
            }
        }
    }

    @SubscribeEvent
    fun onEnchantmentRegistry(event: RegistryEvent.Register<Enchantment>) = FEnchantments.init()

    // Go after other biomes so we can add fish to them
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onBiomeRegistryComplete(event: RegistryEvent.Register<Biome>) {
        val fishConfig = updateAquatic.fish
        val logMissing = fishConfig.logMissingValidBiomes
        val classes = arrayOf(
            EntityCod::class.java,
            EntityPufferfish::class.java,
            EntitySalmon::class.java,
            EntityTropicalFish::class.java
        ).iterator()
        val fishArray = arrayOf(fishConfig.cod, fishConfig.pufferfish, fishConfig.salmon, fishConfig.tropicalFish)

        for (fish in fishArray) {
            val fishClass = classes.next()
            if (fish.enabled) {
                for (spawnEntry in fish.validBiomes) {
                    val loc = ResourceLocation(spawnEntry.modid, spawnEntry.biome)
                    val biome = event.registry.getValue(loc)

                    if (biome == null) {
                        if (logMissing) {
                            FutureMC.LOGGER.warn("Tried to add missing biome '$loc' to FutureMC fish spawns")
                        }
                        break
                    } else {
                        EntityRegistry.addSpawn(
                            fishClass,
                            spawnEntry.weight,
                            spawnEntry.minGroupCount,
                            spawnEntry.maxGroupCount,
                            EnumCreatureType.WATER_CREATURE,
                            biome
                        )
                        EntitySpawnPlacementRegistry.setPlacementType(
                            fishClass,
                            EntityLiving.SpawnPlacementType.IN_WATER
                        )
                    }
                }
            }
        }

        for (biome in event.registry) {
            runOnClient {
                val waterColor = when (biome.waterColor) {
                    0xffffff -> 0x3f76e4 // regular water
                    0xe0ffae -> 0x617b64 // swamp water
                    else -> biome.waterColor
                }

                // Use the Obj2IntMap function
                @Suppress("ReplacePutWithAssignment")
                WaterColor.BIOME_COLORS.put(biome.delegate, waterColor)
            }
        }
    }

    @SubscribeEvent
    fun onRecipeRegistry(event: RegistryEvent.Register<IRecipe>) {
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER, 1, 8), PotionEffect(MobEffects.REGENERATION, 140, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(FBlocks.CORNFLOWER), PotionEffect(MobEffects.JUMP_BOOST, 100, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(FBlocks.LILY_OF_THE_VALLEY), PotionEffect(MobEffects.POISON, 220, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(FBlocks.WITHER_ROSE), PotionEffect(MobEffects.WITHER, 140, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER, 1, 4), PotionEffect(MobEffects.WEAKNESS, 160, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER, 1, 5), PotionEffect(MobEffects.WEAKNESS, 160, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER, 1, 6), PotionEffect(MobEffects.WEAKNESS, 160, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER, 1, 7), PotionEffect(MobEffects.WEAKNESS, 160, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER, 1, 3), PotionEffect(MobEffects.BLINDNESS, 140, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER, 1, 2), PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER), PotionEffect(MobEffects.NIGHT_VISION, 100, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.RED_FLOWER, 1, 1), PotionEffect(MobEffects.SATURATION, 100, 1))
        ItemSuspiciousStew.addRecipe(ItemStack(Blocks.YELLOW_FLOWER), PotionEffect(MobEffects.SATURATION, 100, 1))
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onTextureStitchEventPre(event: TextureStitchEvent.Pre) {
        arrayOf(
            ResourceLocation(FutureMC.ID, "particles/big_smoke_0"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_1"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_2"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_3"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_4"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_5"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_6"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_7"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_8"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_9"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_10"),
            ResourceLocation(FutureMC.ID, "particles/big_smoke_11")
        ).forEachIndexed { i, loc ->
            CampfireParticle.textures[i] = event.map.registerSprite(loc)
        }
        SoulFlameParticle.texture = event.map.registerSprite(ResourceLocation(FutureMC.ID, "particles/soul_fire_flame"))

        event.map.registerSprite(ResourceLocation(FutureMC.ID, "blocks/water_still"))
        event.map.registerSprite(ResourceLocation(FutureMC.ID, "blocks/water_flow"))
    }
}