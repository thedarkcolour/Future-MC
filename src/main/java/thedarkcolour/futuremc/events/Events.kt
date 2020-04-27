package thedarkcolour.futuremc.events

import net.minecraft.block.Blocks
import net.minecraft.inventory.container.SimpleNamedContainerProvider
import net.minecraft.util.ActionResultType
import net.minecraft.util.IWorldPosCallable
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import thedarkcolour.futuremc.command.LocateBiomeCommand
import thedarkcolour.futuremc.config.Config
import thedarkcolour.futuremc.container.SmithingContainer
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

object Events {
    private val SMITHING_TABLE_NAME = TranslationTextComponent("container.futuremc.smithing_table")

    fun registerEvents() {
        FORGE_BUS.addListener(::openSmithingScreen)
        FORGE_BUS.addListener(::onServerStart)
    }

    private fun openSmithingScreen(event: PlayerInteractEvent.RightClickBlock) {
        if (Config.smithingTable.value) {
            val world = event.world
            val pos = event.pos
            val player = event.player

            if (world.getBlockState(pos).block == Blocks.SMITHING_TABLE) {
                if (!world.isRemote) {
                    player.openContainer(SimpleNamedContainerProvider({ windowID, playerInv, _ ->
                        SmithingContainer(windowID, playerInv, IWorldPosCallable.of(world, pos))
                    }, SMITHING_TABLE_NAME))
                }
                event.cancellationResult = ActionResultType.SUCCESS
                event.isCanceled = true
            }
        }
    }

    private fun onServerStart(event: FMLServerStartingEvent) {
        if (Config.locateBiomeCommand.value) {
            LocateBiomeCommand.register(event.commandDispatcher)
        }
    }
}