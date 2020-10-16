package thedarkcolour.futuremc.registry

import net.minecraft.block.Block
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.Item
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.particles.ParticleType
import net.minecraft.util.SoundEvent
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraftforge.event.RegistryEvent.Register
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object RegistryEventHandler {
    fun registerEvents() {
        MOD_BUS.addGenericListener(::registerBiomes)
        MOD_BUS.addGenericListener(::registerBlocks)
        MOD_BUS.addGenericListener(::registerContainers)
        MOD_BUS.addGenericListener(::registerFeatures)
        MOD_BUS.addGenericListener(::registerItems)
        MOD_BUS.addGenericListener(::registerParticles)
        MOD_BUS.addGenericListener(::registerRecipeSerializers)
        MOD_BUS.addGenericListener(::registerSounds)
        MOD_BUS.addGenericListener(::registerSurfaceBuilders)
    }

    private fun registerBiomes(event: Register<Biome>) = FBiomes.onBiomeRegistry(event.registry)

    private fun registerBlocks(event: Register<Block>) = FBlocks.registerBlocks(event.registry)

    private fun registerContainers(event: Register<ContainerType<*>>) = FContainers.registerContainers(event.registry)

    private fun registerFeatures(event: Register<Feature<*>>) = FFeatures.registerFeatures(event.registry)

    private fun registerItems(event: Register<Item>) = FItems.registerItems(event.registry)

    private fun registerParticles(event: Register<ParticleType<*>>) = FParticles.registerParticles(event.registry)

    private fun registerRecipeSerializers(event: Register<IRecipeSerializer<*>>) = FRecipes.registerRecipeSerializers(event.registry)

    private fun registerSounds(event: Register<SoundEvent>) = FSounds.registerSounds(event.registry)

    private fun registerSurfaceBuilders(event: Register<SurfaceBuilder<*>>) = FSurfaceBuilders.registerSurfaceBuilders(event.registry)
}