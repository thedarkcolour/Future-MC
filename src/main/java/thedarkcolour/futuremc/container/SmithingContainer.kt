package thedarkcolour.futuremc.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.inventory.DarkInventory
import thedarkcolour.core.inventory.DarkInventorySlot
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe
import thedarkcolour.futuremc.registry.FSounds

class SmithingContainer(playerInv: InventoryPlayer, private val world: World, private val pos: BlockPos) : Container() {
    private var recipe: SmithingRecipe? = null
    private val darkInventory = object : DarkInventory(3) {
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
                if (!world.isRemote) {
                    get(0).shrink(1)
                    //get(1).shrink(recipe.materialCost)

                    world.playSound(null, pos, FSounds.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS, 1.0f, world.rand.nextFloat() * 0.1f + 0.9f)
                }
            }

            return stack
        }
    }

    init {
        addSlotToContainer(DarkInventorySlot(darkInventory, 0, 27, 47))
        addSlotToContainer(DarkInventorySlot(darkInventory, 1, 76, 47))
        addSlotToContainer(DarkInventorySlot(darkInventory, 2, 134, 47))
    }

    private fun updateResult() {

    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        TODO("not implemented")
    }
}