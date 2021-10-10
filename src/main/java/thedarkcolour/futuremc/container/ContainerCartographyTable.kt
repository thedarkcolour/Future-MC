package thedarkcolour.futuremc.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.gui.FContainer
import thedarkcolour.futuremc.client.gui.GuiCartographyTable

class ContainerCartographyTable(
    playerInv: InventoryPlayer,
    private val world: World,
    private val pos: BlockPos
) : FContainer(playerInv) {
    init {
        addOwnSlots()
        addPlayerSlots(playerInv)
    }

    override fun createGui(): Any {
        return GuiCartographyTable(ContainerCartographyTable(playerInv, world, pos))
    }

    private fun addOwnSlots() {

    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return thedarkcolour.core.util.TODO()
        // return isBlockInRange(FBlocks.CARTOGRAPHY_TABLE, world, pos, playerIn)
    }
}