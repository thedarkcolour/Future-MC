package thedarkcolour.core.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemHandlerHelper
import thedarkcolour.core.gui.TextComponentStringHolder
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * An iterable [net.minecraftforge.items.ItemStackHandler] with extra functionality.
 * Uses an array instead of a [net.minecraft.util.NonNullList]
 */
open class DarkInventory @JvmOverloads constructor(size: Int, defaultName: String? = null) : IItemHandlerModifiable,
    INBTSerializable<NBTTagCompound>, Iterable<ItemStack?> {
    private var stacks: Array<ItemStack>
    private val defaultName: TextComponentTranslation
    private val displayName: TextComponentStringHolder? = null

    init {
        stacks = Array(size) { ItemStack.EMPTY }
        this.defaultName = TextComponentTranslation(defaultName ?: "")
    }

    private fun setSize(size: Int) {
        if (size == stacks.size) {
            Arrays.fill(stacks, ItemStack.EMPTY)
        } else {
            stacks = Array(size) { ItemStack.EMPTY }
        }
    }

    fun getSize(): Int {
        return stacks.size
    }

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        stacks[slot] = stack
        onContentsChanged(slot)
    }

    override fun getSlots(): Int = stacks.size

    override fun getStackInSlot(slot: Int): ItemStack = stacks[slot]

    operator fun get(index: Int): ItemStack = stacks[index]

    operator fun set(index: Int, stack: ItemStack) {
        stacks[index] = stack
        onContentsChanged(index)
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty || !isItemValid(slot, stack)) {
            return stack
        }
        val existing = stacks[slot]
        var limit = getSlotLimit(slot)
        if (!existing.isEmpty) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                return stack
            }
            limit -= existing.count
        }
        if (limit <= 0) {
            return stack
        }
        val reachedLimit = stack.count > limit
        if (!simulate) {
            if (existing.isEmpty) {
                stacks[slot] = if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, limit) else stack
            } else {
                existing.grow(if (reachedLimit) limit else stack.count)
            }
            onContentsChanged(slot)
        }
        return if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, stack.count - limit) else ItemStack.EMPTY
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (amount == 0) {
            return ItemStack.EMPTY
        }
        val existing = stacks[slot]
        if (existing.isEmpty) return ItemStack.EMPTY
        val toExtract = amount.coerceAtMost(existing.maxStackSize)
        return if (existing.count <= toExtract) {
            if (!simulate) {
                stacks[slot] = ItemStack.EMPTY
                onContentsChanged(slot)
            }
            existing
        } else {
            if (!simulate) {
                stacks[slot] = ItemHandlerHelper.copyStackWithSize(existing, existing.count - toExtract)
                onContentsChanged(slot)
            }
            ItemHandlerHelper.copyStackWithSize(existing, toExtract)
        }
    }

    override fun getSlotLimit(slot: Int) = 64

    /**
     * Handles [DarkInventorySlot.isItemValid]
     */
    override fun isItemValid(slot: Int, stack: ItemStack) = true

    override fun serializeNBT(): NBTTagCompound {
        val nbtTagList = NBTTagList()
        for (i in stacks.indices) {
            if (!stacks[i].isEmpty) {
                val itemTag = NBTTagCompound()
                itemTag.setInteger("Slot", i)
                stacks[i].writeToNBT(itemTag)
                nbtTagList.appendTag(itemTag)
            }
        }
        val nbt = NBTTagCompound()
        nbt.setTag("Items", nbtTagList)
        nbt.setInteger("Size", stacks.size)
        return nbt
    }

    override fun deserializeNBT(tag: NBTTagCompound) {
        setSize(if (tag.hasKey("Size", Constants.NBT.TAG_INT)) tag.getInteger("Size") else stacks.size)
        val tagList = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND)
        for (i in 0 until tagList.tagCount()) {
            val itemTags = tagList.getCompoundTagAt(i)
            val slot = itemTags.getInteger("Slot")
            if (slot >= 0 && slot < stacks.size) {
                stacks[slot] = ItemStack(itemTags)
            }
        }
    }

    open fun onContentsChanged(slot: Int) {}

    /**
     * Sets the contents of this slot to [ItemStack.EMPTY].
     */
    fun remove(slot: Int) {
        stacks[slot] = ItemStack.EMPTY
    }

    /**
     * Creates a delegate to a certain slot in this inventory.
     *
     * @throws IllegalArgumentException if the slot is outside of the inventory's indices
     */
    fun delegate(slot: Int): ReadWriteProperty<Any?, ItemStack> {
        if (slot !in stacks.indices)
            throw IllegalArgumentException("Cannot delegate to slot $slot: Outside of inventory indices")

        return DarkInventoryDelegate(this, slot)
    }

    fun anyMatch(predicate: (ItemStack) -> Boolean): Boolean {
        for (stack in this) {
            if (predicate(stack)) {
                return true
            }
        }
        return false
    }

    open fun canTakeStack(playerIn: EntityPlayer, slot: Int): Boolean {
        return true
    }

    open fun onTake(playerIn: EntityPlayer, stack: ItemStack, slot: Int): ItemStack {
        return stack
    }

    override fun iterator(): MutableIterator<ItemStack> {
        return arrayListOf(*stacks).iterator()
    }

    /**
     * [ReadWriteProperty] delegate to a slot in a [DarkInventory].
     */
    private class DarkInventoryDelegate(private val inventory: DarkInventory, private val slot: Int) : ReadWriteProperty<Any?, ItemStack> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): ItemStack {
            return inventory[slot]
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: ItemStack) {
            inventory[slot] = value
        }
    }
}