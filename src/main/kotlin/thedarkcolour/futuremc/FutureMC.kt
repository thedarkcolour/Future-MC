package thedarkcolour.futuremc

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import thedarkcolour.futuremc.client.ClientHandler
import thedarkcolour.futuremc.config.ConfigHolder
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.events.Events
import thedarkcolour.futuremc.registry.RegistryEventHandler
import thedarkcolour.futuremc.world.FWorldType
import thedarkcolour.kotlinforforge.forge.DIST
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Suppress("HasPlatformType")
@Mod(FutureMC.ID)
object FutureMC {
    const val ID = "futuremc"
    val LOGGER = LogManager.getLogger()

    val DEBUG: Boolean = true

    init {

        RegistryEventHandler.registerEvents()
        Events.registerEvents()

        if (DIST == Dist.CLIENT) {
            ClientHandler.registerEvents()
        }

        ConfigHolder.register(MOD_BUS, ID)

        if (DEBUG && FConfig.netherWorldType.value) {
            FWorldType
        }
    }
}