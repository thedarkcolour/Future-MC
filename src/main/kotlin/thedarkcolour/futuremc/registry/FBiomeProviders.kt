package thedarkcolour.futuremc.registry

import net.minecraft.world.biome.provider.BiomeProviderType
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.biome.provider.MultiNoiseBiomeProvider
import thedarkcolour.futuremc.biome.provider.MultiNoiseBiomeProviderSettings
import java.util.function.Function

object FBiomeProviders {
    val MULTI_NOISE = BiomeProviderType(Function(::MultiNoiseBiomeProvider), Function(::MultiNoiseBiomeProviderSettings)).setRegistryKey("multi_noise")

    fun registerBiomeProviders(providers: IForgeRegistry<BiomeProviderType<*, *>>) {
        if (FutureMC.DEBUG)
            providers.register(MULTI_NOISE)
    }
}