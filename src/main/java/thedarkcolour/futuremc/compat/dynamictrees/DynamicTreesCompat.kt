package thedarkcolour.futuremc.compat.dynamictrees

import com.ferreusveritas.dynamictrees.trees.Species
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object DynamicTreesCompat {
    fun addListeners() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun modifyOakSpecies(event: RegistryEvent.Register<Species>) {
        for (species in Species.REGISTRY) {
            species.addGenFeature(BeeHiveFeature)
        }
    }
}