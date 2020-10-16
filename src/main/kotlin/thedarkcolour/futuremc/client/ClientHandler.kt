package thedarkcolour.futuremc.client

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Vector3f
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.futuremc.biome.BiomeEffects
import thedarkcolour.futuremc.registry.FBiomes
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FContainers
import thedarkcolour.futuremc.registry.FParticles
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.*
import kotlin.math.floor

object ClientHandler {
    fun registerEvents() {
        MOD_BUS.addListener(::clientSetup)
        MOD_BUS.addListener(::registerParticleFactories)
        FORGE_BUS.addListener(::renderNetherFog)
        FORGE_BUS.addListener(::addNetherParticles)
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        FBlocks.setRenderLayers()
        FContainers.registerScreens()
    }

    private fun registerParticleFactories(event: ParticleFactoryRegisterEvent) = FParticles.registerParticleFactories()

    private fun addNetherParticles(event: TickEvent.ClientTickEvent) {
        val minecraft = Minecraft.getInstance()

        if (!minecraft.isGamePaused && event.phase == TickEvent.Phase.END) {
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

    private fun World.animate(
        xCenter: Int,
        yCenter: Int,
        zCenter: Int,
        radius: Int,
        rand: Random,
        pos: BlockPos.Mutable
    ) {
        val i = xCenter + rand.nextInt(radius) - rand.nextInt(radius)
        val j = yCenter + rand.nextInt(radius) - rand.nextInt(radius)
        val k = zCenter + rand.nextInt(radius) - rand.nextInt(radius)
        pos.setPos(i, j, k)
        val state = getBlockState(pos)

        if (!state.isFullCube(this, pos)) {
            FBiomes.getBiomeEffects(getBiome(pos))?.let { particles ->
                if (particles.shouldAddParticle(rand)) {
                    // null check happens in shouldAddParticle
                    // do not use mXZ so that movement is still erratic
                    addParticle(
                        particles.particleType, pos.x + rand.nextDouble(), pos.y + rand.nextDouble(), pos.z + rand.nextDouble(),
                        particles.getMotionXZ(rand), particles.getMotionY(rand), particles.getMotionXZ(rand)
                    )
                }
            }
        }
    }

    @SubscribeEvent
    fun renderNetherFog(event: EntityViewRenderEvent.FogColors) {
        val particles =
            FBiomes.getBiomeEffects(Minecraft.getInstance().world!!.biomeAccess.getBiome(event.info.blockPos))
                ?: return

        event.red = particles.fogR.toFloat()
        event.green = particles.fogG.toFloat()
        event.blue = particles.fogB.toFloat()
    }

    private val DENSITY_CURVE = doubleArrayOf(0.0, 1.0, 4.0, 6.0, 4.0, 1.0, 0.0)

    private fun sample(pos: Vec3d, effectSampler: (Int, Int, Int) -> BiomeEffects): Vector3f {
        val i = floor(pos.x).toInt()
        val j = floor(pos.y).toInt()
        val k = floor(pos.z).toInt()
        val d = pos.x - i.toDouble()
        val e = pos.y - j.toDouble()
        val f = pos.z - k.toDouble()
        var g = 0.0
        var vec3d = Vec3d.ZERO

        for (l in 0..5) {
            val h = MathHelper.lerp(d, DENSITY_CURVE[l + 1], DENSITY_CURVE[l])
            val m = i - 2 + l

            for (n in 0..5) {
                val o = MathHelper.lerp(e, DENSITY_CURVE[n + 1], DENSITY_CURVE[n])
                val p = j - 2 + n

                for (q in 0..5) {
                    val r = MathHelper.lerp(f, DENSITY_CURVE[q + 1], DENSITY_CURVE[q])
                    val s = k - 2 + q

                    val t = h * o * r
                    g += t
                    val effects = effectSampler(m, p, s)
                    vec3d = vec3d.add(effects.fogR, effects.fogG, effects.fogB).mul(t, t, t)
                }
            }
        }

        val u = 1.0 / g
        return Vector3f((vec3d.x * u).toFloat(), (vec3d.y * u).toFloat(), (vec3d.z * u).toFloat())
    }
}