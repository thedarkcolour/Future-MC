package thedarkcolour.futuremc.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBanner
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.tileentity.BannerPattern
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.SlotItemHandler
import thedarkcolour.core.gui.ContainerBase
import thedarkcolour.core.inventory.DarkInventory
import thedarkcolour.core.util.anyMatch
import thedarkcolour.core.util.getOreNames
import thedarkcolour.core.util.janyMatch
import thedarkcolour.futuremc.client.gui.GuiLoom
import thedarkcolour.futuremc.item.ItemBannerPattern
import thedarkcolour.futuremc.item.ItemBannerPattern.Companion.getBannerPattern
import thedarkcolour.futuremc.registry.FBlocks.LOOM

class ContainerLoom(private val playerInv: InventoryPlayer, private val world: World, private val pos: BlockPos) :
    ContainerBase() {
    private val handler: DarkInventory = object : DarkInventory(4) {
        override fun onContentsChanged(slot: Int) {
            if (slot != 3) {
                handleCrafting()
            }
            inventoryUpdateListener()
        }
    }
    private val slots = arrayOf(
        object : SlotItemHandler(handler, 0, 13, 26) {
            override fun isItemValid(stack: ItemStack): Boolean {
                return stack.item is ItemBanner
            }
        },
        object : SlotItemHandler(handler, 1, 33, 26) {
            override fun isItemValid(stack: ItemStack): Boolean {
                return getOreNames(stack).anyMatch { it.startsWith("dye") }
            }
        },
        object : SlotItemHandler(handler, 2, 23, 45) {
            override fun isItemValid(stack: ItemStack): Boolean {
                return stack.item is ItemBannerPattern
            }
        },
        object : SlotItemHandler(handler, 3, 143, 57) {
            override fun isItemValid(stack: ItemStack) = false

            override fun onTake(thePlayer: EntityPlayer, stack: ItemStack): ItemStack {
                color.shrink(1)
                banner.shrink(1)
                return stack
            }
        }
    )
    private var selectedIndex = 0
    private var inventoryUpdateListener = {}

    init {
        addOwnSlots()
        addPlayerSlots()
    }

    private fun addOwnSlots() {
        for (slot in slots) {
            addSlotToContainer(slot)
        }
    }

    private fun addPlayerSlots() {
        for (row in 0..2) {
            for (col in 0..8) {
                val x = col * 18 + 8
                val y = row * 18 + 84
                addSlotToContainer(Slot(playerInv, col + row * 9 + 9, x, y))
            }
        }
        for (col in 0..8) {
            val x = 9 + col * 18 - 1
            val y = 58 + 70 + 14
            addSlotToContainer(Slot(playerInv, col, x, y))
        }
    }

    val banner: ItemStack
        get() = handler.getStackInSlot(0)

    var color: ItemStack
        get() = handler.getStackInSlot(1)
        set(stack) {
            handler.setStackInSlot(1, stack)
        }

    var pattern: ItemStack
        get() = handler.getStackInSlot(2)
        set(stack) {
            handler.setStackInSlot(2, stack)
        }

    var output: ItemStack
        get() = handler.getStackInSlot(3)
        set(stack) {
            handler.setStackInSlot(3, stack)
        }

    fun getLoomSlot(index: Int): Slot {
        return slots[index]
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return isBlockInRange(LOOM, world, pos, playerIn)
    }

    override fun enchantItem(playerIn: EntityPlayer, id: Int): Boolean {
        if (id > 0 && id <= GuiLoom.BASIC_PATTERNS.size) {
            selectedIndex = id
            updateRecipeResultSlot()
            return true
        }
        return false
    }

    private fun handleCrafting() {
        val banner = banner
        val pattern = pattern
        if (output.isEmpty || !banner.isEmpty && !color.isEmpty && selectedIndex > 0 && (selectedIndex < GuiLoom.BASIC_PATTERNS.size - 5 || !pattern.isEmpty)) {
            if (!pattern.isEmpty && pattern.item is ItemBannerPattern) {
                val tag = banner.getOrCreateSubCompound("BlockEntityTag")
                val flag =
                    tag.hasKey("Patterns", 9) && !banner.isEmpty && tag.getTagList("Patterns", 10).tagCount() >= 6
                selectedIndex = if (flag) {
                    0
                } else {
                    getBannerPattern(pattern).ordinal
                }
            }
        } else {
            output = ItemStack.EMPTY
            selectedIndex = 0
        }
        updateRecipeResultSlot()
        detectAndSendChanges()
    }

    private fun updateRecipeResultSlot() {
        if (selectedIndex > 0) {
            val banner = banner
            val color = color
            var stack = ItemStack.EMPTY
            if (!banner.isEmpty && !color.isEmpty) {
                stack = banner.copy()
                stack.count = 1
                val nbt = stack.getOrCreateSubCompound("BlockEntityTag")
                val list: NBTTagList
                list = if (nbt.hasKey("Patterns", 9)) {
                    nbt.getTagList("Patterns", 10)
                } else {
                    NBTTagList()
                }
                val tag = NBTTagCompound()
                var selectedIndex = selectedIndex
                val bannerPatterns = if (pattern.isEmpty) {
                    --selectedIndex
                    GuiLoom.BASIC_PATTERNS
                } else {
                    BannerPattern.values()
                }
                tag.setString("Pattern", bannerPatterns[selectedIndex].hashname)
                tag.setInteger("Color", getColorForStack(color).dyeDamage)
                list.appendTag(tag)
                nbt.setTag("Patterns", list)
            }
            if (!ItemStack.areItemStacksEqual(stack, output)) {
                output = stack
            }
        } else {
            output = ItemStack.EMPTY
        }
    }

    fun setInventoryUpdateListener(listenerIn: () -> Unit) {
        inventoryUpdateListener = listenerIn
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        var itemstack = ItemStack.EMPTY
        val slot = inventorySlots[index]
        if (slot != null && slot.hasStack) {
            val itemstack1 = slot.stack
            itemstack = itemstack1.copy()
            if (index == 3) {
                if (!mergeItemStack(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY
                }
                slot.onSlotChange(itemstack1, itemstack)
            } else if (index != 1 && index != 0 && index != 2) {
                if (itemstack1.item is ItemBanner) {
                    if (!mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (janyMatch(
                        getOreNames(itemstack1)
                    ) { oreName -> oreName.startsWith("dye") }
                ) {
                    if (!mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (itemstack1.item is ItemBannerPattern) {
                    if (!mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (index in 4..30) {
                    if (!mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (index in 31..39 && !mergeItemStack(itemstack1, 4, 31, false)) {
                    return ItemStack.EMPTY
                }
            } else if (!mergeItemStack(itemstack1, 4, 40, false)) {
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
        if (!playerIn.isEntityAlive || playerIn is EntityPlayerMP && playerIn.hasDisconnected()) {
            for (i in 0..2) {
                val stack = handler.getStackInSlot(i)
                if (!stack.isEmpty) {
                    playerIn.entityDropItem(stack, 0.5f)
                }
            }
        } else {
            for (i in 0..2) {
                if (!handler.getStackInSlot(i).isEmpty) {
                    playerInv.placeItemBackInInventory(world, handler.getStackInSlot(i))
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getGuiContainer(): GuiLoom {
        return GuiLoom(ContainerLoom(playerInv, world, pos))
    }

    fun getSelectedIndex(): Int {
        return selectedIndex
    }

    companion object {
        fun getColorForStack(stack: ItemStack): EnumDyeColor {
            for (s in getOreNames(stack)) {
                if (s.startsWith("dye")) {
                    when (s.replaceFirst("dye".toRegex(), "")) {
                        "Black" -> return EnumDyeColor.BLACK
                        "Blue" -> return EnumDyeColor.BLUE
                        "Brown" -> return EnumDyeColor.BROWN
                        "Red" -> return EnumDyeColor.RED
                        "Green" -> return EnumDyeColor.GREEN
                        "Purple" -> return EnumDyeColor.PURPLE
                        "Cyan" -> return EnumDyeColor.CYAN
                        "LightGray" -> return EnumDyeColor.SILVER
                        "Gray" -> return EnumDyeColor.GRAY
                        "Pink" -> return EnumDyeColor.PINK
                        "Lime" -> return EnumDyeColor.LIME
                        "Yellow" -> return EnumDyeColor.YELLOW
                        "LightBlue" -> return EnumDyeColor.LIGHT_BLUE
                        "Magenta" -> return EnumDyeColor.MAGENTA
                        "Orange" -> return EnumDyeColor.ORANGE
                    }
                }
            }
            return EnumDyeColor.WHITE
        }
    }
}