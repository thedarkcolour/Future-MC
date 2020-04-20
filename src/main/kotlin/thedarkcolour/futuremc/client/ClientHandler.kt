package thedarkcolour.futuremc.client

import net.minecraft.client.Minecraft
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.futuremc.registry.FBiomes
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FContainers
import thedarkcolour.futuremc.registry.FParticles
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.*

object ClientHandler {
    fun registerEvents() {
        MOD_BUS.addListener(::clientSetup)
        MOD_BUS.addListener(::registerParticleFactories)
        FORGE_BUS.addListener(::renderNetherFog)
        FORGE_BUS.addListener(::addNetherParticles)
    }

    @SubscribeEvent
    fun clientSetup(event: FMLClientSetupEvent) {
        FBlocks.setRenderLayers()
        FContainers.registerScreens()
    }

    @SubscribeEvent
    fun registerParticleFactories(event: ParticleFactoryRegisterEvent) = FParticles.registerParticleFactories()

    @SubscribeEvent
    fun addNetherParticles(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            val minecraft = Minecraft.getInstance()
            val world = minecraft.world ?: return
            val player = minecraft.player ?: return
            val random = world.random
            val xCenter = MathHelper.floor(player.x)
            val yCenter = MathHelper.floor(player.y)
            val zCenter = MathHelper.floor(player.z)
            // prevents unnecessary allocations
            val mutablePos = BlockPos.Mutable()

            // see function below for animateTick
            for (i in 0 until 667) {
                world.animate(xCenter, yCenter, zCenter, 16, random, mutablePos)
                world.animate(xCenter, yCenter, zCenter, 32, random, mutablePos)
            }
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun ClientWorld.animate(xCenter: Int, yCenter: Int, zCenter: Int, radius: Int, rand: Random, pos: BlockPos.Mutable) {
        val i = xCenter + rand.nextInt(radius) - rand.nextInt(radius)
        val j = yCenter + rand.nextInt(radius) - rand.nextInt(radius)
        val k = zCenter + rand.nextInt(radius) - rand.nextInt(radius)
        pos.setPos(i, j, k)
        val state = getBlockState(pos)

        if (!state.isFullCube(this, pos)) {
            FBiomes.getParticles(getBiome(pos))?.let { particles ->
                if (particles.shouldAddParticle(rand)) {
                    addParticle(
                            // null check happens in shouldAddParticle
                            particles.particleType, pos.x + rand.nextDouble(), pos.y + rand.nextDouble(), pos.z + rand.nextDouble(),
                            particles.getMotionX(rand), particles.getMotionY(rand), particles.getMotionZ(rand)
                    )
                }
            }
        }
    }

    @SubscribeEvent
    fun renderNetherFog(event: EntityViewRenderEvent.FogColors) {
        val particles = (FBiomes.getParticles(Minecraft.getInstance().world!!.getBiome(event.info.blockPos)) ?: return)
        event.red = particles.fogR
        event.green = particles.fogG
        event.blue = particles.fogB
    }
}