package thedarkcolour.futuremc.registry

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

@Deprecated(level = DeprecationLevel.WARNING, message = "Extremely messy and confusing outside of an IDE")
operator fun <T : IForgeRegistryEntry<T>> IForgeRegistry<T>.invoke(t: T, enabled: Boolean): T {
    if (enabled) {
        register(t)
    }
    return t
}

@Deprecated(level = DeprecationLevel.WARNING, message = "Extremely messy and confusing outside of an IDE")
operator fun <T : IForgeRegistryEntry<T>, S : T> IForgeRegistry<T>.invoke(
    t: S,
    enabled: Boolean,
    specialRegistryHandling: (S) -> Unit
): T {
    if (enabled) {
        register(t)
        specialRegistryHandling(t)
    }
    return t
}

@Deprecated(level = DeprecationLevel.WARNING, message = "Extremely messy and confusing outside of an IDE")
operator fun <T : IForgeRegistryEntry<T>> IForgeRegistry<T>.invoke(t: T): T {
    register(t)
    return t
}

// todo come up with a nicer way of doing this
// maybe just remove altogether because it's not that much code anyways
fun registerTE(name: String, clazz: Class<out TileEntity>, enabled: Boolean) {
    if (enabled) {
        TileEntity.register(name, clazz)
    }
}