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
import thedarkcolour.core.gui.FContainer
import thedarkcolour.core.inventory.FInventory
import thedarkcolour.core.inventory.FInventorySlot
import thedarkcolour.core.util.anyMatch
import thedarkcolour.core.util.getOreNames
import thedarkcolour.core.util.janyMatch
import thedarkcolour.futuremc.client.gui.GuiLoom
import thedarkcolour.futuremc.item.BannerPatternItem
import thedarkcolour.futuremc.item.BannerPatternItem.Companion.getBannerPattern
import thedarkcolour.futuremc.registry.FBlocks.LOOM

class ContainerLoom(
    playerInv: InventoryPlayer,
    private val world: World,
    private val pos: BlockPos
) : FContainer(playerInv) {
    private val handler = object : FInventory(4) {
        override fun onContentsChanged(slot: Int) {
            if (slot != 3) {
                handleCrafting()
            }
            inventoryUpdateListener()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                0 -> stack.item is ItemBanner
                1 -> isDye(stack)
                2 -> stack.item is BannerPatternItem
                else -> false
            }
        }

        override fun onTake(playerIn: EntityPlayer, stack: ItemStack, slot: Int): ItemStack {
            if (slot == 3) {
                color.shrink(1)
                banner.shrink(1)
            }
            return super.onTake(playerIn, stack, slot)
        }
    }
    private var selectedIndex = 0
    private var inventoryUpdateListener = {}
    private lateinit var bannerSlot: Slot
    private lateinit var dyeSlot: Slot
    private lateinit var patternSlot: Slot
    private lateinit var resultSlot: Slot

    init {
        addOwnSlots()
        addPlayerSlots(playerInv)
    }

    private fun addOwnSlots() {
        bannerSlot = addSlotToContainer(FInventorySlot(handler, 0, 13, 26))
        dyeSlot = addSlotToContainer(FInventorySlot(handler, 1, 33, 26))
        patternSlot = addSlotToContainer(FInventorySlot(handler, 2, 23, 45))
        resultSlot = addSlotToContainer(FInventorySlot(handler, 3, 143, 57))
    }

    // delegates
    val banner: ItemStack
        get() = handler[0]
    var color: ItemStack
        get() = handler[1]
        set(value) { handler[1] = value }
    var pattern: ItemStack
        get() = handler[2]
        set(value) { handler[2] = value }
    var output: ItemStack
        get() = handler[3]
        set(value) { handler[3] = value }

    fun getLoomSlot(index: Int): Slot {
        return when (index) {
            0 -> bannerSlot
            1 -> dyeSlot
            2 -> patternSlot
            3 -> resultSlot
            else -> throw IllegalArgumentException("Index $index must be in 0..3")
        }
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return isBlockInRange(LOOM, world, pos, playerIn)
    }

    override fun enchantItem(playerIn: EntityPlayer, id: Int): Boolean {
        if (id in 0 .. BASIC_PATTERNS.size) {
            selectedIndex = id
            updateRecipeResultSlot()
            return true
        }
        return false
    }

    private fun handleCrafting() {
        val banner = banner
        val pattern = pattern
        if (output.isEmpty || !banner.isEmpty && !color.isEmpty && selectedIndex > 0 && (selectedIndex < BASIC_PATTERNS.size - 5 || !pattern.isEmpty)) {
            if (pattern.item is BannerPatternItem) {
                val tag = banner.getOrCreateSubCompound("BlockEntityTag")
                val flag = tag.hasKey("Patterns", 9) && !banner.isEmpty && tag.getTagList("Patterns", 10).tagCount() >= 6
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

        if (pattern.isEmpty) {
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
                val list = if (nbt.hasKey("Patterns", 9)) {
                    nbt.getTagList("Patterns", 10)
                } else {
                    NBTTagList()
                }
                val tag = NBTTagCompound()
                var selectedIndex = selectedIndex
                val bannerPatterns = if (pattern.isEmpty) {
                    --selectedIndex
                    BASIC_PATTERNS
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
                } else if (itemstack1.item is BannerPatternItem) {
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
    override fun createGui(): Any {
        return GuiLoom(ContainerLoom(playerInv, world, pos))
    }

    fun getSelectedIndex(): Int {
        return selectedIndex
    }

    companion object {
        // Vanilla banner patterns that don't require a pattern to create
        val BASIC_PATTERNS = arrayOf(BannerPattern.SQUARE_BOTTOM_LEFT, BannerPattern.SQUARE_BOTTOM_RIGHT, BannerPattern.SQUARE_TOP_LEFT, BannerPattern.SQUARE_TOP_RIGHT, BannerPattern.STRIPE_BOTTOM, BannerPattern.STRIPE_TOP, BannerPattern.STRIPE_LEFT, BannerPattern.STRIPE_RIGHT, BannerPattern.STRIPE_CENTER, BannerPattern.STRIPE_MIDDLE, BannerPattern.STRIPE_DOWNRIGHT, BannerPattern.STRIPE_DOWNLEFT, BannerPattern.STRIPE_SMALL, BannerPattern.CROSS, BannerPattern.STRAIGHT_CROSS, BannerPattern.TRIANGLE_BOTTOM, BannerPattern.TRIANGLE_TOP, BannerPattern.TRIANGLES_BOTTOM, BannerPattern.TRIANGLES_TOP, BannerPattern.DIAGONAL_LEFT, BannerPattern.DIAGONAL_RIGHT, BannerPattern.DIAGONAL_LEFT_MIRROR, BannerPattern.DIAGONAL_RIGHT_MIRROR, BannerPattern.CIRCLE_MIDDLE, BannerPattern.RHOMBUS_MIDDLE, BannerPattern.HALF_VERTICAL, BannerPattern.HALF_HORIZONTAL, BannerPattern.HALF_VERTICAL_MIRROR, BannerPattern.HALF_HORIZONTAL_MIRROR, BannerPattern.BORDER, BannerPattern.CURLY_BORDER, BannerPattern.GRADIENT, BannerPattern.GRADIENT_UP, BannerPattern.BRICKS)

        fun isDye(stack: ItemStack): Boolean {
            return getOreNames(stack).anyMatch { it.startsWith("dye") }
        }

        fun getColorForStack(stack: ItemStack): EnumDyeColor {
            for (s in getOreNames(stack)) {
                if (s.startsWith("dye")) {
                    when (s) {
                        "dyeBlack" -> return EnumDyeColor.BLACK
                        "dyeBlue" -> return EnumDyeColor.BLUE
                        "dyeBrown" -> return EnumDyeColor.BROWN
                        "dyeRed" -> return EnumDyeColor.RED
                        "dyeGreen" -> return EnumDyeColor.GREEN
                        "dyePurple" -> return EnumDyeColor.PURPLE
                        "dyeCyan" -> return EnumDyeColor.CYAN
                        "dyeLightGray" -> return EnumDyeColor.SILVER
                        "dyeGray" -> return EnumDyeColor.GRAY
                        "dyePink" -> return EnumDyeColor.PINK
                        "dyeLime" -> return EnumDyeColor.LIME
                        "dyeYellow" -> return EnumDyeColor.YELLOW
                        "dyeLightBlue" -> return EnumDyeColor.LIGHT_BLUE
                        "dyeMagenta" -> return EnumDyeColor.MAGENTA
                        "dyeOrange" -> return EnumDyeColor.ORANGE
                    }
                }
            }
            return EnumDyeColor.WHITE
        }
    }
}