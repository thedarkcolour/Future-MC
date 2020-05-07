package thedarkcolour.futuremc.registry

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntitySpawnPlacementRegistry
import net.minecraft.entity.EnumCreatureType
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
import thedarkcolour.core.util.runOnClient
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.client.color.WaterColor
import thedarkcolour.futuremc.client.particle.CampfireParticle
import thedarkcolour.futuremc.client.particle.SoulFlameParticle
import thedarkcolour.futuremc.command.FastGiveCommand
import thedarkcolour.futuremc.config.FConfig.updateAquatic
import thedarkcolour.futuremc.config.FConfig.useVanillaCreativeTabs
import thedarkcolour.futuremc.entity.fish.cod.EntityCod
import thedarkcolour.futuremc.entity.fish.pufferfish.EntityPufferfish
import thedarkcolour.futuremc.entity.fish.salmon.EntitySalmon
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish
import thedarkcolour.futuremc.item.ItemGroup
import thedarkcolour.futuremc.item.ItemSuspiciousStew

object RegistryEventHandler {
    @SubscribeEvent
    fun onBlockRegistry(event: RegistryEvent.Register<Block>) {
        FutureMC.GROUP = if (useVanillaCreativeTabs) {
            CreativeTabs.MISC
        } else {
            ItemGroup
        }

        FBlocks.registerBlocks(event.registry)
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
        FEntities.registerEntities()
        runOnClient(FEntities::registerEntityRenderers)
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
                    val parts = spawnEntry.split(":")
                    val loc = ResourceLocation(parts[0], parts[1])
                    val biome = event.registry.getValue(loc)

                    if (biome == null) {
                        if (logMissing) {
                            FutureMC.LOGGER.warn("Tried to add missing biome '$loc' to FutureMC fish spawns")
                        }
                        break
                    } else {
                        EntityRegistry.addSpawn(fishClass, Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), EnumCreatureType.WATER_CREATURE, biome)
                        EntitySpawnPlacementRegistry.setPlacementType(fishClass, EntityLiving.SpawnPlacementType.IN_WATER)
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

                // Use the Obj2IntMap function because Kotlin is weird about the overloads
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
        CampfireParticle.textures = arrayOf(
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
        ).map(event.map::registerSprite).toTypedArray()

        SoulFlameParticle.texture = event.map.registerSprite(ResourceLocation(FutureMC.ID, "particles/soul_fire_flame"))

        //event.map.registerSprite(ResourceLocation(FutureMC.ID, "blocks/water_still"))
        //event.map.registerSprite(ResourceLocation(FutureMC.ID, "blocks/water_flow"))
    }
}