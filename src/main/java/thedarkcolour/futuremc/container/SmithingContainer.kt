package thedarkcolour.futuremc.container

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.gui.FContainer
import thedarkcolour.core.inventory.DarkInventory
import thedarkcolour.core.inventory.DarkInventorySlot
import thedarkcolour.futuremc.client.gui.SmithingGui
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipes
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FSounds

class SmithingContainer(playerInv: InventoryPlayer, private val world: World, private val pos: BlockPos) : FContainer(playerInv) {
    private var recipe: SmithingRecipe? = null
    private val inventory = object : DarkInventory(3) {
        override fun onContentsChanged(slot: Int) {
            if (slot != 2) {
                detectAndSendChanges()
                updateResult()
            }
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return false
        }

        override fun onTake(playerIn: EntityPlayer, stack: ItemStack, slot: Int): ItemStack {
            if (slot == 2) {
                consumeInputs()
            }

            return stack
        }
    }

    init {
        addOwnSlots()
        addPlayerSlots(playerInv)
    }

    private fun addOwnSlots() {
        addSlotToContainer(DarkInventorySlot(inventory, 0, 27, 47))
        addSlotToContainer(DarkInventorySlot(inventory, 1, 76, 47))
        addSlotToContainer(DarkInventorySlot(inventory, 2, 134, 47))
    }

    private fun updateResult() {
        val input = inventory[0]
        recipe = SmithingRecipes.getRecipe(input)
    }

    private fun consumeInputs() {
        if (!world.isRemote) {
            inventory[0].shrink(1)

            world.playSound(null, pos, FSounds.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS, 1.0f, world.rand.nextFloat() * 0.1f + 0.9f)
        }
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return isBlockInRange(FBlocks.SMITHING_TABLE, world, pos, playerIn)
    }

    override fun getGuiContainer(): GuiContainer {
        return SmithingGui(SmithingContainer(playerInv, world, pos))
    }
}