package thedarkcolour.futuremc.registry

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntitySpawnPlacementRegistry
import net.minecraft.entity.EnumCreatureType
import net.minecraft.item.Item
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
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
import net.minecraftforge.registries.IForgeRegistryModifiable
import thedarkcolour.core.util.runOnClient
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.block.villagepillage.CampfireBlock
import thedarkcolour.futuremc.client.particle.CampfireParticle
import thedarkcolour.futuremc.client.particle.SoulFlameParticle
import thedarkcolour.futuremc.command.FastGiveCommand
import thedarkcolour.futuremc.compat.checkBetterWithMods
import thedarkcolour.futuremc.config.FConfig.updateAquatic
import thedarkcolour.futuremc.config.FConfig.useVanillaCreativeTabs
import thedarkcolour.futuremc.entity.fish.cod.EntityCod
import thedarkcolour.futuremc.entity.fish.pufferfish.EntityPufferfish
import thedarkcolour.futuremc.entity.fish.salmon.EntitySalmon
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish
import thedarkcolour.futuremc.item.ItemGroup

object RegistryEventHandler {
    @SubscribeEvent
    fun onBlockRegistry(event: RegistryEvent.Register<Block>) {
        FutureMC.GROUP = if (useVanillaCreativeTabs) {
            CreativeTabs.MISC
        } else {
            ItemGroup
        }

        FBlocks.registerBlocks(event.registry)

        checkBetterWithMods()?.addHeatSource(1, FBlocks.CAMPFIRE.blockState.validStates.filter { state ->
            state.getValue(CampfireBlock.LIT)
        })
    }

    @SubscribeEvent
    fun onItemRegistry(event: RegistryEvent.Register<Item>) = FItems.registerItems(event.registry)

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
    fun registerEnchantments(event: RegistryEvent.Register<Enchantment>) = FEnchantments.registerEnchantments(event)

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
/*
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
        }*/
    }

    @SubscribeEvent
    @Suppress("UNCHECKED_CAST")
    fun registerRecipes(event: RegistryEvent.Register<IRecipe>) {
        FRecipes.registerRecipes(event.registry as IForgeRegistryModifiable<IRecipe>)
    }

    @SubscribeEvent
    fun registerSounds(event: RegistryEvent.Register<SoundEvent>) {
        FSounds.registerSounds(event.registry)
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