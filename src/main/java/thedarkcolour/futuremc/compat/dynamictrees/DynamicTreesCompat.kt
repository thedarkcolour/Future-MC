package thedarkcolour.futuremc.compat.dynamictrees

import com.ferreusveritas.dynamictrees.trees.Species
import net.minecraftforge.event.RegistryEvent
import thedarkcolour.core.util.addListener

object DynamicTreesCompat {
    fun addListeners() {
        addListener(this::modifyOakSpecies)
    }

    private fun modifyOakSpecies(event: RegistryEvent.Register<Species>) {
        for (species in Species.REGISTRY) {
            species.addGenFeature(BeeHiveFeature)
        }
    }
}