package thedarkcolour.futuremc.container

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.IMerchant
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.ContainerMerchant
import net.minecraft.inventory.Slot
import net.minecraft.inventory.SlotMerchantResult
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import thedarkcolour.futuremc.client.gui.GuiVillager

class ContainerVillager(val playerInv: InventoryPlayer, merchant: IMerchant, world: World?) : ContainerMerchant(playerInv, merchant, world) {
    init {
        inventorySlots.clear()
        inventoryItemStacks.clear()
        // Modify existing slots
        //getSlot(0).xPos = 136
        //getSlot(0).yPos = 37

        //getSlot(1).xPos = 162
        //getSlot(1).yPos = 37

        // Add to listeners
        val player = playerInv.player
        if (player is EntityPlayerMP) {
            listeners.add(player)
        }

        var index = 0

        addSlotToContainer(Slot(merchantInventory, index++, 136, 37))
        addSlotToContainer(Slot(merchantInventory, index++, 162, 37))
        addSlotToContainer(SlotMerchantResult(playerInv.player, merchant, merchantInventory, index++, 220, 37))

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

    override fun canMergeSlot(stack: ItemStack, slotIn: Slot): Boolean {
        return false
    }

    override fun onContainerClosed(playerIn: EntityPlayer) {
        val playerInv = playerInv

        if (!playerInv.itemStack.isEmpty) {
            playerIn.dropItem(playerInv.itemStack, false)
            playerInv.itemStack = ItemStack.EMPTY
        }

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

    fun getGuiContainer(): GuiContainer {
        return GuiVillager(ContainerVillager(playerInv, merchant, null))
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