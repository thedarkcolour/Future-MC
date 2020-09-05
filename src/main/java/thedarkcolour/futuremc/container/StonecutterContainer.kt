package thedarkcolour.futuremc.container

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.gui.FContainer
import thedarkcolour.core.inventory.DarkInventory
import thedarkcolour.core.inventory.DarkInventorySlot
import thedarkcolour.futuremc.client.gui.StonecutterGui
import thedarkcolour.futuremc.compat.checkQuark
import thedarkcolour.futuremc.recipe.SimpleRecipe
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FSounds.STONECUTTER_CARVE

class StonecutterContainer(
    playerInv: InventoryPlayer,
    private val worldIn: World,
    private val pos: BlockPos
) : FContainer(playerInv) {
    // list of possible outputs
    var recipeList = emptyList<SimpleRecipe>()

    // tracks last time player took an item from output
    private var lastOnTake = 0L

    // tracks which result is clicked and what the expected output should be
    var selectedIndex = 0

    // ran on the client to refresh the possible output list
    var inventoryUpdateListener = {}

    // item handler. i understand it's a bit messy, i'll probably organize into separate methods.
    private val darkInventory = object : DarkInventory(2) {
        override fun onContentsChanged(slot: Int) {
            if (slot == 0) {
                updateRecipeList()
            }
            inventoryUpdateListener()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return slot == 0
        }

        override fun setStackInSlot(slot: Int, stack: ItemStack) {
            if (checkQuark()?.isDrawingInvalidSlotsOverlay() == false)
                super.setStackInSlot(slot, stack)
        }

        override fun onTake(playerIn: EntityPlayer, stack: ItemStack, slot: Int): ItemStack {
            if (slot == 0) {
                if (get(0).isEmpty) {
                    remove(1)
                }
            } else {
                get(0).shrink(1)
                if (!get(0).isEmpty) {
                    updateResult()
                } else {
                    recipeList = emptyList()
                    selectedIndex = -1
                }
                stack.item.onCreated(stack, playerIn.world, playerIn)
                val l = worldIn.totalWorldTime
                if (lastOnTake != l) {
                    worldIn.playSound(null, pos, STONECUTTER_CARVE, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    lastOnTake = l
                }
            }
            return stack
        }
    }

    init {
        addOwnSlots()
        addPlayerSlots(playerInv)
    }

    private fun addOwnSlots() {
        addSlotToContainer(DarkInventorySlot(darkInventory, 0, 20, 33))
        addSlotToContainer(DarkInventorySlot(darkInventory, 1, 143, 33).alwaysTakeAll(true))
    }

    private fun updateRecipeList() {
        val recipes = StonecutterRecipes.getRecipes(darkInventory[0])

        if (recipes.isNotEmpty()) {
            if (recipeList != recipes) {
                recipeList = recipes
                selectedIndex = -1

                darkInventory.remove(1)

                detectAndSendChanges()
            }
        } else {
            recipeList = emptyList()
            darkInventory.remove(1)

            detectAndSendChanges()
        }
    }

    private fun updateResult() {
        if (recipeList.isNotEmpty() && selectedIndex > -1) {
            darkInventory.setStackInSlot(1, recipeList[selectedIndex].output.copy())
        } else {
            darkInventory.setStackInSlot(1, ItemStack.EMPTY)
        }

        detectAndSendChanges()
    }

    override fun enchantItem(playerIn: EntityPlayer?, id: Int): Boolean {
        if (id >= 0 && id < recipeList.size) {
            selectedIndex = id
            updateResult()
            return true
        }

        return false
    }

    override fun canMergeSlot(stack: ItemStack, slot: Slot): Boolean {
        return false
    }

    override fun onContainerClosed(playerIn: EntityPlayer) {
        super.onContainerClosed(playerIn)
        val stack = darkInventory.getStackInSlot(0)

        if (!playerIn.isEntityAlive || playerIn is EntityPlayerMP && playerIn.hasDisconnected()) {
            if (!stack.isEmpty) {
                playerIn.entityDropItem(stack, 0.5f)
            }
        } else {
            if (!stack.isEmpty) {
                playerInv.placeItemBackInInventory(worldIn, stack)
            }
        }
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack? {
        var stack = ItemStack.EMPTY
        val slot = inventorySlots[index]
        if (slot != null && slot.hasStack) {
            val stack1 = slot.stack
            stack = stack1.copy()
            if (index == 1) {
                stack1.item.onCreated(stack1, playerIn.world, playerIn)
                if (!mergeItemStack(stack1, 2, 38, true)) {
                    return ItemStack.EMPTY
                }
                slot.onSlotChange(stack1, stack)
            } else if (index == 0) {
                if (!mergeItemStack(stack1, 2, 38, false)) {
                    return ItemStack.EMPTY
                }
            } else if (StonecutterRecipes.getRecipe(stack1) != null) {
                if (!mergeItemStack(stack1, 0, 1, false)) {
                    return ItemStack.EMPTY
                }
            } else if (index in 2..28) {
                if (!mergeItemStack(stack1, 29, 38, false)) {
                    return ItemStack.EMPTY
                }
            } else if (index in 29..37 && !mergeItemStack(stack1, 2, 29, false)) {
                return ItemStack.EMPTY
            }
            if (stack1.isEmpty) {
                slot.putStack(ItemStack.EMPTY)
            }
            slot.onSlotChanged()
            if (stack1.count == stack.count) {
                return ItemStack.EMPTY
            }
            slot.onTake(playerIn, stack1)
            detectAndSendChanges()
        }
        return stack
    }

    override fun getGuiContainer(): GuiContainer {
        return StonecutterGui(
            StonecutterContainer(
                playerInv,
                worldIn,
                pos
            )
        )
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return isBlockInRange(FBlocks.STONECUTTER, worldIn, pos, playerIn)
    }
}