package thedarkcolour.futuremc.container

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.IMerchant
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryMerchant
import net.minecraft.inventory.Slot
import net.minecraft.inventory.SlotMerchantResult
import net.minecraft.item.ItemStack
import thedarkcolour.core.gui.FContainer
import thedarkcolour.futuremc.client.gui.GuiVillager

class ContainerVillager(playerInv: InventoryPlayer, val merchant: IMerchant) : FContainer(playerInv) {
    val merchantInventory = InventoryMerchant(playerInv.player, merchant)

    init {
        addSlotToContainer(Slot(merchantInventory, 0, 136, 37))
        addSlotToContainer(Slot(merchantInventory, 1, 162, 37))
        addSlotToContainer(SlotMerchantResult(playerInv.player, merchant, merchantInventory, 2, 220, 37))

        for (row in 0..2) {
            for (col in 0..8) {
                val x = col * 18 + 108
                val y = row * 18 + 84
                addSlotToContainer(Slot(playerInv, col + row * 9 + 9, x, y))
            }
        }

        for (row in 0..8) {
            val x = row * 18 + 108
            addSlotToContainer(Slot(playerInv, row, x, 142))
        }
    }

    override fun onCraftMatrixChanged(inventoryIn: IInventory) {
        merchantInventory.resetRecipeAndSlots()
        super.onCraftMatrixChanged(inventoryIn)
    }

    fun setTradeIndex(currentRecipe: Int) {
        merchantInventory.setCurrentRecipeIndex(currentRecipe)
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return merchant.customer == playerIn
    }

    override fun canMergeSlot(stack: ItemStack, slotIn: Slot): Boolean {
        return false
    }

    // Same as 1.12
    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        var itemstack = ItemStack.EMPTY
        val slot = inventorySlots[index]

        if (slot != null && slot.hasStack) {
            val itemstack1 = slot.stack
            itemstack = itemstack1.copy()
            if (index == 2) {
                if (!mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY
                }
                slot.onSlotChange(itemstack1, itemstack)
            } else if (index != 0 && index != 1) {
                if (index in 3..29) {
                    if (!mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (index in 30..38 && !mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY
                }
            } else if (!mergeItemStack(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY
            }
            if (itemstack1.isEmpty) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }
            if (itemstack1.count == itemstack.count) {
                return ItemStack.EMPTY
            }
            slot.onTake(playerIn, itemstack1)
        }

        return itemstack
    }

    override fun onContainerClosed(playerIn: EntityPlayer) {
        super.onContainerClosed(playerIn)
        merchant.customer = null

        if (!merchant.world.isRemote) {
            if (!playerIn.isEntityAlive || (playerIn is EntityPlayerMP && playerIn.hasDisconnected())) {
                var stack = merchantInventory.removeStackFromSlot(0)

                if (!stack.isEmpty) {
                    playerIn.entityDropItem(stack, 0.5f)
                }

                stack = merchantInventory.removeStackFromSlot(1)

                if (!stack.isEmpty) {
                    playerIn.entityDropItem(stack, 0.5f)
                }
            } else {
                playerInv.placeItemBackInInventory(playerIn.world, merchantInventory.removeStackFromSlot(0))
                playerInv.placeItemBackInInventory(playerIn.world, merchantInventory.removeStackFromSlot(1))
            }
        }
    }

    override fun getGuiContainer(): GuiContainer {
        return GuiVillager(ContainerVillager(playerInv, merchant))
    }

    fun moveAroundItems(tradeIndex: Int) {
        val trades = merchant.getRecipes(playerInv.player) ?: return

        if (trades.size > tradeIndex) {
            val stack = merchantInventory.getStackInSlot(0)
            if (!stack.isEmpty) {
                if (!mergeItemStack(stack, 3, 39, true)) {
                    return
                }

                merchantInventory.setInventorySlotContents(0, stack)
            }

            val stack1 = merchantInventory.getStackInSlot(1)
            if (!stack1.isEmpty) {
                if (!mergeItemStack(stack1, 3, 39, true)) {
                    return
                }

                merchantInventory.setInventorySlotContents(1, stack1)
            }

            if (merchantInventory.getStackInSlot(0).isEmpty && merchantInventory.getStackInSlot(1).isEmpty) {
                val stack2 = trades[tradeIndex].itemToBuy
                val stack3 = trades[tradeIndex].secondItemToBuy

                moveTradeItem(0, stack2)
                moveTradeItem(1, stack3)
            }
        }
    }

    private fun moveTradeItem(index: Int, stack: ItemStack) {
        if (!stack.isEmpty) {
            for (i in 3..38) {
                val itemStack = inventorySlots[i].stack

                if (!itemStack.isEmpty && areItemStacksEqual(stack, itemStack)) {
                    val merchantItem = merchantInventory.getStackInSlot(index)
                    val count = if (merchantItem.isEmpty) 0 else merchantItem.count
                    val addition = (stack.maxStackSize - count).coerceAtMost(itemStack.count)
                    val stack2 = itemStack.copy()
                    val newCount = count + addition
                    itemStack.shrink(addition)
                    stack2.count = newCount
                    merchantInventory.setInventorySlotContents(index, stack2)
                    if (newCount >= stack.maxStackSize) {
                        break
                    }
                }
            }
        }
    }

    private fun areItemStacksEqual(stack1: ItemStack, stack2: ItemStack): Boolean {
        return stack1.item === stack2.item && ItemStack.areItemStackTagsEqual(stack1, stack2)
    }

}