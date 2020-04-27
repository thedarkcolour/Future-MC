package thedarkcolour.futuremc.registry

import net.minecraft.client.gui.ScreenManager
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.client.screen.SmithingTableScreen
import thedarkcolour.futuremc.container.SmithingContainer

object FContainers {
    val SMITHING_TABLE = ContainerType(::SmithingContainer).setRegistryKey("smithing_table")

    fun registerContainers(containers: IForgeRegistry<ContainerType<*>>) {
        containers.register(SMITHING_TABLE)
    }

    fun registerScreens() {
        ScreenManager.registerFactory(SMITHING_TABLE, ::SmithingTableScreen)
    }
}