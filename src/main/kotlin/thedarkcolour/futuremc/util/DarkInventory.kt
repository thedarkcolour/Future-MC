package thedarkcolour.futuremc.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemHandlerHelper
import java.util.*

open class DarkInventory constructor(size: Int, defaultName: String? = null) : IItemHandlerModifiable,
        INBTSerializable<CompoundNBT>, IInventory, Iterable<ItemStack> {
    private var stacks: Array<ItemStack>
    private val defaultName: TranslationTextComponent
    private val displayName: StringTextComponentMutable? = null

    init {
        stacks = Array(size) { ItemStack.EMPTY }
        this.defaultName = TranslationTextComponent(defaultName ?: "")
    }

    private fun setSize(size: Int) {
        if (size == stacks.size) {
            Arrays.fill(stacks, ItemStack.EMPTY)
        } else {
            stacks = Array(size) { ItemStack.EMPTY }
        }
    }

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        stacks[slot] = stack
        onContentsChanged(slot)
    }

    override fun getSlots() = stacks.size

    override fun getStackInSlot(slot: Int) = stacks[slot]

    operator fun get(index: Int): ItemStack {
        return getStackInSlot(index)
    }

    operator fun set(index: Int, stack: ItemStack) {
        return setStackInSlot(index, stack)
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

    /**
     * Used to determine if [playerIn] can take from [slot].
     * Only works if the Slot is [SlotDarkInventory]
     * @see SlotDarkInventory.canTakeStack
     */
    open fun canTakeStack(slot: Int, playerIn: PlayerEntity): Boolean {
        return true
    }

    /**
     * React when an item is taken by [playerIn] from a slot.
     * Only works if the Slot is [SlotDarkInventory]
     * @see SlotDarkInventory.onTake
     */
    open fun onTake(slot: Int, playerIn: PlayerEntity, stack: ItemStack): ItemStack {
        return stack
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

    /**
     * Specify how many items can fit into this slot.
     * The stack limit of the item is still respected.
     */
    override fun getSlotLimit(slot: Int) = 64

    /**
     * Determine if the stack can fit into this slot.
     */
    override fun isItemValid(slot: Int, stack: ItemStack) = true

    /**
     * Serialize to NBT for saving to chunk data.
     */
    override fun serializeNBT(): CompoundNBT {
        val nbtTagList = ListNBT()
        for (i in stacks.indices) {
            if (!stacks[i].isEmpty) {
                val itemTag = CompoundNBT()
                itemTag.putInt("Slot", i)
                stacks[i].write(itemTag)
                nbtTagList.add(itemTag)
            }
        }
        val nbt = CompoundNBT()
        nbt.put("Items", nbtTagList)
        nbt.putInt("Size", stacks.size)
        return nbt
    }

    /**
     * Read from serialized NBT
     */
    override fun deserializeNBT(tag: CompoundNBT) {
        setSize(if (tag.contains("Size", Constants.NBT.TAG_INT)) tag.getInt("Size") else stacks.size)
        val tagList = tag.getList("Items", Constants.NBT.TAG_COMPOUND)
        for (i in 0 until tagList.size) {
            val itemTags = tagList.getCompound(i)
            val slot = itemTags.getInt("Slot")
            if (slot >= 0 && slot < stacks.size) {
                stacks[slot] = ItemStack.read(itemTags)
            }
        }
    }

    /**
     * Iterable implementation for the contents of this [DarkInventory]
     */
    override fun iterator(): Iterator<ItemStack> {
        return stacks.iterator()
    }

    /**
     * React after contents have changed.
     */
    open fun onContentsChanged(slot: Int) {}

    open fun getVanillaInventory(range: IntRange = stacks.indices): Inventory {
        return Inventory(*(stacks.copyOfRange(range.first, range.last)))
    }

    override fun markDirty() {}

    override fun clear() {
        stacks = Array(stacks.size) { ItemStack.EMPTY }
    }

    override fun decrStackSize(index: Int, count: Int): ItemStack {
        stacks[index].shrink(count)
        return stacks[index]
    }

    override fun getSizeInventory(): Int {
        return stacks.size
    }

    override fun isEmpty(): Boolean {
        return stacks.all(ItemStack::isEmpty)
    }

    // Don't use this
    override fun isUsableByPlayer(player: PlayerEntity): Boolean {
        return true
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack) {
        stacks[index] = stack
    }

    override fun removeStackFromSlot(index: Int): ItemStack {
        val old = stacks[index]
        stacks[index] = ItemStack.EMPTY
        return if (old.isEmpty) ItemStack.EMPTY else old
    }
}