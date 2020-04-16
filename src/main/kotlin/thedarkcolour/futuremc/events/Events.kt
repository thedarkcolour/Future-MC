package thedarkcolour.futuremc.events

import net.minecraft.block.Blocks
import net.minecraft.inventory.container.IContainerProvider
import net.minecraft.inventory.container.SimpleNamedContainerProvider
import net.minecraft.util.ActionResultType
import net.minecraft.util.IWorldPosCallable
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import thedarkcolour.futuremc.config.Config
import thedarkcolour.futuremc.container.SmithingTableContainer

object Events {
    private val SMITHING_TABLE_NAME = TranslationTextComponent("container.futuremc.smithing_table")

    @SubscribeEvent
    fun openSmithingScreen(event: PlayerInteractEvent.RightClickBlock) {
        if (Config.smithingTable.value) {
            val world = event.world
            val pos = event.pos
            val player = event.player

            if (world.getBlockState(pos).block == Blocks.SMITHING_TABLE) {
                if (!world.isRemote) {
                    player.openContainer(SimpleNamedContainerProvider(IContainerProvider { windowID, playerInv, _ ->
                        SmithingTableContainer(windowID, playerInv, IWorldPosCallable.of(world, pos))
                    }, SMITHING_TABLE_NAME))
                }
                event.cancellationResult = ActionResultType.SUCCESS
                event.isCanceled = true
            }
        }
    }
}