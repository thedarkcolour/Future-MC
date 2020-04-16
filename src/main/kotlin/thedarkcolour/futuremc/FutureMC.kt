package thedarkcolour.futuremc

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import thedarkcolour.futuremc.client.ClientHandler
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
        MOD_BUS.register(this)
        MOD_BUS.register(RegistryEventHandler)
        MOD_BUS.register(Holder)
        FORGE_BUS.register(Events)
        FORGE_BUS.register(Holder)

        if (DIST == Dist.CLIENT) {
            MOD_BUS.register(ClientHandler)
            FORGE_BUS.register(ClientHandler)
        }

        val specPair = ForgeConfigSpec.Builder().configure(Holder::configure)

        registerConfig(ModConfig.Type.COMMON, specPair.right, "futuremc.toml")

        if (DEBUG) {
            FWorldType
        }

        //LOADING_CONTEXT.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY) { ConfigScreen.Factory() }
    }
}