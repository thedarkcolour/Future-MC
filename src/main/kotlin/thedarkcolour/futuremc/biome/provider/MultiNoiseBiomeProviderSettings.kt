package thedarkcolour.futuremc.biome.provider

import net.minecraft.world.biome.provider.IBiomeProviderSettings
import net.minecraft.world.storage.WorldInfo

class MultiNoiseBiomeProviderSettings(worldInfo: WorldInfo) : IBiomeProviderSettings {
    val seed = worldInfo.seed
}