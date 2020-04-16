package thedarkcolour.futuremc.container

import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.IWorldPosCallable
import net.minecraft.util.SoundCategory
import thedarkcolour.futuremc.recipe.SmithingRecipe
import thedarkcolour.futuremc.registry.FContainers
import thedarkcolour.futuremc.registry.FRecipes
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.util.DarkInventory
import thedarkcolour.futuremc.util.SlotDarkInventory

class SmithingTableContainer(windowID: Int, private val playerInv: PlayerInventory, private val worldPos: IWorldPosCallable
) : Container(FContainers.SMITHING_TABLE, windowID) {
    private var recipe: SmithingRecipe? = null
    /*private val recipes = HashMap<Item, Item>(9).also { map ->
        map[Items.DIAMOND_CHESTPLATE] = FItems.NETHERITE_CHESTPLATE
        map[Items.DIAMOND_LEGGINGS] = FItems.NETHERITE_LEGGINGS
        map[Items.DIAMOND_HELMET] = FItems.NETHERITE_HELMET
        map[Items.DIAMOND_BOOTS] = FItems.NETHERITE_BOOTS
        map[Items.DIAMOND_SWORD] = FItems.NETHERITE_SWORD
        map[Items.DIAMOND_AXE] = FItems.NETHERITE_AXE
        map[Items.DIAMOND_PICKAXE] = FItems.NETHERITE_PICKAXE
        map[Items.DIAMOND_HOE] = FItems.NETHERITE_HOE
        map[Items.DIAMOND_SHOVEL] = FItems.NETHERITE_SHOVEL
    }*/

    private val darkInventory = object : DarkInventory(3) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return slot != 2
        }

        override fun onContentsChanged(slot: Int) {
            if (slot != 2) {
                contentsChanged()
            }
        }

        override fun canTakeStack(slot: Int, playerIn: PlayerEntity): Boolean {
            return if (slot == 2) {
                return recipe != null
            } else true
        }

        override fun onTake(slot: Int, playerIn: PlayerEntity, stack: ItemStack): ItemStack {
            if (slot == 2) {
                this[0] = ItemStack.EMPTY
                this[1].shrink(1)
                worldPos.apply { world, pos ->
                    world.playSound(null, pos, FSounds.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS, 1.0f, world.random.nextFloat() * 0.1f + 0.9f)
                }
            }
            return stack
        }
    }

    constructor(windowID: Int, playerInv: PlayerInventory) : this(windowID, playerInv, IWorldPosCallable.DUMMY)

    init {
        addOwnSlots()
        addPlayerSlots()
    }

    private fun addOwnSlots() {
        addSlot(SlotDarkInventory(darkInventory, 0, 27, 47))
        addSlot(SlotDarkInventory(darkInventory, 1, 76, 47))
        addSlot(SlotDarkInventory(darkInventory, 2, 134, 47))
    }

    private fun addPlayerSlots() {
        for (row in 0..2) {
            for (col in 0..8) {
                val x = col * 18 + 8
                val y = row * 18 + 84
                addSlot(Slot(playerInv, col + row * 9 + 9, x, y))
            }
        }
        for (col in 0..8) {
            val x = 9 + col * 18 - 1
            val y = 58 + 70 + 14
            addSlot(Slot(playerInv, col, x, y))
        }
    }

    fun contentsChanged() {
        val stack = darkInventory[0]
        worldPos.consume { worldIn, _ ->
            recipe = worldIn.recipeManager.getRecipeNullable(FRecipes.SMITHING, darkInventory, worldIn)

            if (recipe != null) {
                val stack2 = recipe!!.recipeOutput.copy()
                val tag = stack.tag
                stack2.tag = tag
                darkInventory[2] = stack2
            } else {
                darkInventory[2] = ItemStack.EMPTY
            }
        }
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean {
        return isWithinUsableDistance(worldPos, playerIn, Blocks.SMITHING_TABLE)
    }

    override fun onContainerClosed(playerIn: PlayerEntity) {
        super.onContainerClosed(playerIn)
        darkInventory[2] = ItemStack.EMPTY
        worldPos.consume { worldIn, _ ->
            clearContainer(playerIn, worldIn, darkInventory)
        }
    }

    override fun transferStackInSlot(playerIn: PlayerEntity, index: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot = inventorySlots[index]
        if (slot != null && slot.hasStack) {
            val itemStack2 = slot.stack
            itemStack = itemStack2.copy()
            if (index == 2) {
                if (!this.mergeItemStack(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY
                }
                slot.onSlotChange(itemStack2, itemStack)
            } else if (index != 0 && index != 1) {
                if (index in 3..38 && !this.mergeItemStack(itemStack2, 0, 2, false)) {
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