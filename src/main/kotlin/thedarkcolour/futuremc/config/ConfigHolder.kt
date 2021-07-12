package thedarkcolour.futuremc.config

import net.minecraftforge.common.ForgeConfigSpec.Builder
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent
import thedarkcolour.kotlinforforge.forge.registerConfig

object ConfigHolder {
    fun register(bus: IEventBus, modId: String) {
        bus.addListener(ConfigHolder::sync)

        registerConfig(ModConfig.Type.COMMON, Builder().configure(ConfigHolder::configure).right, "$modId.toml")
    }

    private fun configure(builder: Builder) {
        // load the entries
        FConfig.setup()

        FConfig.netherUpdate.addToConfig(builder)
    }

    private fun sync(event: ModConfigEvent) {
        val data = event.config.configData

        FConfig.netherUpdate.sync(data, "")
    }
}