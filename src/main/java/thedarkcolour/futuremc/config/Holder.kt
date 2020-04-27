package thedarkcolour.futuremc.config

import net.minecraftforge.common.ForgeConfigSpec.Builder
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent

object Holder {
    fun configure(builder: Builder) {
        // load the entries
        Config.setup()

        Config.netherUpdate.addToConfig(builder)
    }

    fun sync(event: ModConfigEvent) {
        val data = event.config.configData

        Config.netherUpdate.sync(data, "")
    }
}