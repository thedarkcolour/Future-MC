package thedarkcolour.futuremc

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import thedarkcolour.futuremc.client.ClientHandler
import thedarkcolour.futuremc.config.Config
import thedarkcolour.futuremc.config.Holder
import thedarkcolour.futuremc.events.Events
import thedarkcolour.futuremc.registry.RegistryEventHandler
import thedarkcolour.futuremc.world.FWorldType
import thedarkcolour.kotlinforforge.forge.DIST
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.registerConfig

@Suppress("HasPlatformType")
@Mod(FutureMC.ID)
object FutureMC {
    const val ID = "futuremc"
    val LOGGER = LogManager.getLogger()

    val DEBUG: Boolean = true

    init {

        RegistryEventHandler.registerEvents()
        Events.registerEvents()
        MOD_BUS.addListener(Holder::sync)

        if (DIST == Dist.CLIENT) {
            ClientHandler.registerEvents()
        }

        val specPair = ForgeConfigSpec.Builder().configure(Holder::configure)

        registerConfig(ModConfig.Type.COMMON, specPair.right, "futuremc.toml")

        if (DEBUG && Config.netherWorldType.value) {
            FWorldType
        }
    }
}