package thedarkcolour.futuremc

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import thedarkcolour.futuremc.client.ClientHandler
import thedarkcolour.futuremc.command.LocateBiomeCommand
import thedarkcolour.futuremc.config.Config
import thedarkcolour.futuremc.config.Holder
import thedarkcolour.futuremc.events.Events
import thedarkcolour.futuremc.registry.RegistryEventHandler
import thedarkcolour.futuremc.world.FWorldType
import thedarkcolour.kotlinforforge.forge.DIST
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.registerConfig

@Mod(FutureMC.ID)
object FutureMC {
    const val ID = "futuremc"
    const val DEBUG = true

    init {
        RegistryEventHandler.registerEvents()
        MOD_BUS.addListener(Holder::sync)
        FORGE_BUS.addListener(Events::openSmithingScreen)
        FORGE_BUS.addListener(::onServerStart)

        if (DIST == Dist.CLIENT) {
            ClientHandler.registerEvents()
        }

        val specPair = ForgeConfigSpec.Builder().configure(Holder::configure)

        registerConfig(ModConfig.Type.COMMON, specPair.right, "futuremc.toml")

        if (Config.netherWorldType.value) {
            FWorldType
        }

        //LOADING_CONTEXT.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY) { ConfigScreen.Factory() }
    }

    private fun onServerStart(event: FMLServerStartingEvent) {
        if (Config.locateBiomeCommand.value) {
            LocateBiomeCommand.register(event.commandDispatcher)
        }
    }
}