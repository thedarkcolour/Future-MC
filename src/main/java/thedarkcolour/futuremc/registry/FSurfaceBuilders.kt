package thedarkcolour.futuremc.registry

import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.surfacebuilder.NetherForestSurfaceBuilder
import thedarkcolour.futuremc.surfacebuilder.SoulSandValleySurfaceBuilder
import java.util.function.Function

object FSurfaceBuilders {
    val NETHER_FOREST = NetherForestSurfaceBuilder(Function(SurfaceBuilderConfig::deserialize)).setRegistryKey("nether_forest")
    val SOUL_SAND_VALLEY = SoulSandValleySurfaceBuilder(Function(SurfaceBuilderConfig::deserialize)).setRegistryKey("soul_sand_valley")

    fun registerSurfaceBuilders(builders: IForgeRegistry<SurfaceBuilder<*>>) {
        if (FutureMC.DEBUG) {
            builders.register(NETHER_FOREST)
        }
    }
}
