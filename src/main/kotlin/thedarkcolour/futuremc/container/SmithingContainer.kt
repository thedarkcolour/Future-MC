package thedarkcolour.futuremc.container

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.CraftResultInventory
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.IWorldPosCallable
import net.minecraft.util.SoundCategory
import thedarkcolour.futuremc.recipe.SmithingRecipe
import thedarkcolour.futuremc.registry.FContainers
import thedarkcolour.futuremc.registry.FRecipes
import thedarkcolour.futuremc.registry.FSounds

@Suppress("LeakingThis")
class SmithingContainer(type: ContainerType<*>?, id: Int, playerInv: PlayerInventory, private val worldPos: IWorldPosCallable) : Container(type, id) {
    private var recipe: SmithingRecipe? = null
    private val output = CraftResultInventory()
    val input = object : Inventory(2) {
        override fun markDirty() {
            super.markDirty()
            onCraftMatrixChanged(this)
        }
    }

    constructor(id: Int, playerInv: PlayerInventory, worldPos: IWorldPosCallable) : this(FContainers.SMITHING_TABLE, id, playerInv, worldPos)

    constructor(id: Int, playerInv: PlayerInventory) : this(FContainers.SMITHING_TABLE, id, playerInv, IWorldPosCallable.DUMMY)

    init {
        addSlot(Slot(input, 0, 27, 47))
        addSlot(Slot(input, 1, 76, 47))
        addSlot(object : Slot(output, 2, 134, 47) {
            override fun isItemValid(stack: ItemStack): Boolean {
                return false
            }

            override fun onTake(playerIn: PlayerEntity, stack: ItemStack): ItemStack {
                return consumeInputs(stack)
            }
        })

        for (k in 0..2) {
            for (j in 0..8) {
                addSlot(Slot(playerInv, j + k * 9 + 9, 8 + j * 18, 84 + k * 18))
            }
        }

        for (k in 0..8) {
            addSlot(Slot(playerInv, k, 8 + k * 18, 142))
        }
    }

    fun consumeInputs(stack: ItemStack): ItemStack {
        worldPos.consume { worldIn, pos ->
            if (!worldIn.isRemote) {
                val aaaa = input.getStackFrom(1)
                aaaa.shrink(recipe!!.materialCost)
                input.setInventorySlotContents(1, aaaa)
                val aaaaa = input.getStackFrom(0)
                aaaaa.shrink(1)
                input.setInventorySlotContents(0, aaaaa)

                worldIn.playSound(null, pos, FSounds.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS, 1.0f, worldIn.random.nextFloat() * 0.1f + 0.9f)
            }
        }

        return stack
    }

    private fun canUse(state: BlockState): Boolean {
        return state.block == Blocks.SMITHING_TABLE
    }

    private fun updateResult() {
        val stack = input.getStackFrom(0)
        worldPos.consume { worldIn, _ ->
            recipe = worldIn.recipeManager.getRecipeNullable(FRecipes.SMITHING, input, worldIn)

            if (recipe != null) {
                val stack2 = recipe!!.recipeOutput.copy()
                val tag = stack.tag
                stack2.tag = tag
                output.setInventorySlotContents(0, stack2)
            } else {
                output.setInventorySlotContents(0, ItemStack.EMPTY)
            }
        }
    }

    override fun onCraftMatrixChanged(inventoryIn: IInventory) {
        super.onCraftMatrixChanged(inventoryIn)
        if (inventoryIn == input) {
            updateResult()
        }
    }

    override fun onContainerClosed(playerIn: PlayerEntity) {
        super.onContainerClosed(playerIn)
        worldPos.consume { worldIn, _ ->
            clearContainer(playerIn, worldIn, input)
        }
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean {
        return worldPos.applyOrElse( { worldIn, pos ->
            if (!canUse(worldIn.getBlockState(pos))) {
                false
            } else {
                playerIn.getDistanceSq(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64
            }
        }, true)
    }

    override fun transferStackInSlot(playerIn: PlayerEntity, index: Int): ItemStack {
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