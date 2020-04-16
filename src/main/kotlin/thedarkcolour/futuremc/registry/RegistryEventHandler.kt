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
import net.minecraftforge.eventbus.api.SubscribeEvent

object RegistryEventHandler {
    @SubscribeEvent
    fun registerBiomes(event: Register<Biome>) = FBiomes.onBiomeRegistry(event.registry)

    @SubscribeEvent
    fun registerBlocks(event: Register<Block>) = FBlocks.registerBlocks(event.registry)

    @SubscribeEvent
    fun registerContainers(event: Register<ContainerType<*>>) = FContainers.registerContainers(event.registry)

    @SubscribeEvent
    fun registerFeatures(event: Register<Feature<*>>) = FFeatures.registerFeatures(event.registry)

    @SubscribeEvent
    fun registerItems(event: Register<Item>) = FItems.registerItems(event.registry)

    @SubscribeEvent
    fun registerParticles(event: Register<ParticleType<*>>) = FParticles.registerParticles(event.registry)

    @SubscribeEvent
    fun registerRecipeSerializers(event: Register<IRecipeSerializer<*>>) = FRecipes.registerRecipeSerializers(event.registry)

    @SubscribeEvent
    fun registerSounds(event: Register<SoundEvent>) = FSounds.registerSounds(event.registry)

    @SubscribeEvent
    fun registerSurfaceBuilders(event: Register<SurfaceBuilder<*>>) = FSurfaceBuilders.registerSurfaceBuilders(event.registry)
}