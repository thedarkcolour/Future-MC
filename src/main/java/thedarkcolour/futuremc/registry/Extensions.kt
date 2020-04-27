package thedarkcolour.futuremc.registry

import net.minecraftforge.registries.ForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.option.Option

fun <T : ForgeRegistryEntry<T>> IForgeRegistry<T>.register(item: T, option: Option<Boolean>) {
    if (option.value) {
        register(item)
    }
}

fun <T : ForgeRegistryEntry<*>> T.setRegistryKey(key: String): T {
    setRegistryName(FutureMC.ID, key)
    return this
}