package thedarkcolour.futuremc.container

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraftforge.items.SlotItemHandler
import thedarkcolour.core.gui.FContainer
import thedarkcolour.core.inventory.FInventory


class GrindstoneContainer(
    playerInv: InventoryPlayer,
    private val worldIn: World,
    private val pos: BlockPos
) : FContainer(playerInv) {
    // Split into separate class for neatness
    private val inventory = Inventory()

    // slot delegates
    private val inputA by inventory.delegate(0)
    private val inputB by inventory.delegate(1)
    private var output by inventory.delegate(2)

    init {
        addOwnSlots()
        addPlayerSlots(playerInv)
    }

    private fun addOwnSlots() {
        addSlotToContainer(SlotItemHandler(inventory, 0, 49, 19))
        addSlotToContainer(SlotItemHandler(inventory, 1, 49, 40))
        addSlotToContainer(OutputSlot(inventory, 2, 129, 34))
    }

    private fun handleCrafting() {
        // each input slot
        val inputA = inventory[0]
        val inputB = inventory[1]

        // Handle incompatible items
        if (!inputA.isItemEqualIgnoreDurability(inputB) && !(inputA.isEmpty || inputB.isEmpty)) {
            output = ItemStack.EMPTY
        }
        // Handle two compatible items
        else if (inputA.isItemEqualIgnoreDurability(inputB) && inputA.isItemEnchantable) {
            // durability cap of item
            val cap = inputA.maxDamage

            // total durability of both tools
            var sum = (cap - inputA.itemDamage) + (cap - inputB.itemDamage)

            // +20% repair durability bonus
            sum = (sum + MathHelper.floor(sum * 0.2)).coerceAtMost(cap)

            val result = inputA.copy()
            result.itemDamage = cap - sum
            result.setTagInfo("Enchantments", NBTTagList())

            // use Kotlin's add function to simplify
            val map = EnchantmentHelper.getEnchantments(inputA) + EnchantmentHelper.getEnchantments(inputB)

            for ((enchantment, level) in map) {
                if (enchantment.isCurse) {
                    result.addEnchantment(enchantment, level)
                }
            }

            output = result
        } else if (inputA.isItemEnchanted) { // disenchants an item
            disenchant(inputA, 0)
        } else if (inputB.isItemEnchanted) {
            disenchant(inputB, 1)
        }
    }

    // todo remember what this was supposed to do
    private fun disenchant(stack: ItemStack, slot: Int) {
        val result = stack.copy()
        result.setTagInfo("ench", NBTTagList())

        val map = EnchantmentHelper.getEnchantments(stack)

        for ((enchantment, level) in map) {
            if (enchantment.isCurse) {
                result.addEnchantment(enchantment, level)
            }
        }
    }

    private fun copyCurses(toItem: ItemStack, fromA: ItemStack, fromB: ItemStack = ItemStack.EMPTY) {
        val map = EnchantmentHelper.getEnchantments(fromA) + EnchantmentHelper.getEnchantments(fromB)

        for ((enchantment, level) in map) {
            if (enchantment.isCurse) {
                toItem.addEnchantment(enchantment, level)
            }
        }
    }

    private fun updateResult() {

    }

    override fun createGui(): Any {
        return TODO()
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return TODO()
    }

    private inner class OutputSlot(inv: FInventory, index: Int, x: Int, y: Int) : SlotItemHandler(inv, index, x, y) {
        override fun onTake(thePlayer: EntityPlayer, stack: ItemStack): ItemStack {
            //handleOutput() todo
            return stack
        }
    }

    private inner class Inventory : FInventory(2) {
        override fun onContentsChanged(slot: Int) {
            if (slot != 2) {
                handleCrafting()
                detectAndSendChanges()
            }
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return slot != 2
        }
    }
}