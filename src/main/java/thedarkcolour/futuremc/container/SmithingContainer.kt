package thedarkcolour.futuremc.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.gui.FContainer
import thedarkcolour.core.inventory.FInventory
import thedarkcolour.core.inventory.FInventorySlot
import thedarkcolour.futuremc.client.gui.SmithingGui
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipes
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FSounds

class SmithingContainer(playerInv: InventoryPlayer, private val worldIn: World, private val pos: BlockPos) : FContainer(playerInv) {
    private var recipe: SmithingRecipe? = null
    private val inventory = object : FInventory(3) {
        override fun onContentsChanged(slot: Int) {
            if (slot != 2) {
                detectAndSendChanges()
                updateResult()
            }
        }

        override fun isItemValid(slot: Int, stack: ItemStack) = slot != 2

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
        addSlotToContainer(FInventorySlot(inventory, 0, 27, 47))
        addSlotToContainer(FInventorySlot(inventory, 1, 76, 47))
        addSlotToContainer(FInventorySlot(inventory, 2, 134, 47).alwaysTakeAll(true))
    }

    private fun updateResult() {
        val input = inventory[0]
        recipe = SmithingRecipes.getRecipe(input, inventory[1])

        if (recipe != null) {
            val result = recipe!!.output.copy()
            result.tagCompound = input.tagCompound
            if (input.isItemStackDamageable && result.isItemStackDamageable) {
                result.itemDamage = input.itemDamage
            }
            inventory[2] = result
        } else {
            inventory.remove(2)
        }
    }

    private fun consumeInputs() {
        if (!worldIn.isRemote) {
            inventory[1].shrink(1)
            inventory[0].shrink(1)

            detectAndSendChanges()

            worldIn.playSound(null, pos, FSounds.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS, 1.0f, worldIn.rand.nextFloat() * 0.1f + 0.9f)
        }
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return isBlockInRange(FBlocks.SMITHING_TABLE, worldIn, pos, playerIn)
    }

    override fun createGui(): Any {
        return SmithingGui(SmithingContainer(playerInv, worldIn, pos))
    }

    override fun onContainerClosed(playerIn: EntityPlayer) {
        super.onContainerClosed(playerIn)
        val stack = inventory.getStackInSlot(0)
        val stack1 = inventory.getStackInSlot(1)

        if (!playerIn.isEntityAlive || playerIn is EntityPlayerMP && playerIn.hasDisconnected()) {
            if (!stack.isEmpty) {
                playerIn.entityDropItem(stack, 0.5f)
            }
            if (!stack1.isEmpty) {
                playerIn.entityDropItem(stack1, 0.5f)
            }
        } else {
            if (!stack.isEmpty) {
                playerInv.placeItemBackInInventory(worldIn, stack)
            }
            if (!stack1.isEmpty) {
                playerInv.placeItemBackInInventory(worldIn, stack1)
            }
        }
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot = inventorySlots[index]

        if (slot.hasStack) {
            val itemStack2 = slot.stack
            itemStack = itemStack2.copy()
            if (index == 2) {
                if (!mergeItemStack(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY
                }
                slot.onSlotChange(itemStack2, itemStack)
            } else if (index != 0 && index != 1) {
                if (index in 3..38 && !mergeItemStack(itemStack2, 0, 2, false)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.mergeItemStack(itemStack2, 3, 39, false)) {
                return ItemStack.EMPTY
            }
            if (itemStack2.isEmpty) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }
            if (itemStack2.count == itemStack.count) {
                return ItemStack.EMPTY
            }
            slot.onTake(playerIn, itemStack2)
        }

        return itemStack
    }
}