package thedarkcolour.futuremc.tile

import net.minecraft.block.BlockLiquid
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.util.*
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fluids.BlockFluidBase
import thedarkcolour.core.inventory.FInventory
import thedarkcolour.core.item.DebuggerItem
import thedarkcolour.core.tile.InteractionTile
import thedarkcolour.futuremc.block.villagepillage.CampfireBlock
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipes

class CampfireTile : InteractionTile(), ITickable {
    val cookingTimes = IntArray(4)
    val cookingTotalTimes = IntArray(4)

    val inventory = object : FInventory(4) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return CampfireRecipes.getRecipe(stack) != null
        }

        override fun getSlotLimit(slot: Int): Int {
            return 1
        }
    }

    override fun update() {
        val lit = blockMetadata and 4 != 0
        if (lit) {
            if (!world.isRemote) {
                if (FConfig.villageAndPillage.campfire.functionality) {
                    if (lit) {
                        cookItems()
                    } else {
                        for (i in 0..3) {
                            if (cookingTimes[i] > 0) {
                                cookingTimes[i] = MathHelper.clamp(cookingTimes[i] - 2, 0, cookingTotalTimes[i])
                            }
                        }
                    }
                }
            } else {
                spawnParticles()
            }
        }
    }

    private fun cookItems() {
        for (i in 0..3) {
            val stack = inventory.getStackInSlot(i)
            if (!stack.isEmpty) {
                ++cookingTimes[i]
                if (cookingTimes[i] >= cookingTotalTimes[i]) {
                    val recipe = CampfireRecipes.getRecipe(stack)
                    if (recipe != null) {
                        val output = recipe.output.copy()
                        inventory.setStackInSlot(i, ItemStack.EMPTY)
                        drop(output)
                        cookingTimes[i] = 0
                    }
                }
            }
        }

        inventoryChanged()
    }

    private fun spawnParticles() {
        if (world != null) {
            val rand = world.rand
            if (rand.nextFloat() < 0.11f) {
                for (i in 0 until rand.nextInt(2) + 2) {
                    CampfireBlock.spawnSmokeParticles(world, pos, blockMetadata and 8 == 0, false)
                }
            }

            val l = EnumFacing.byHorizontalIndex(blockMetadata and -5).horizontalIndex

            for (j in 0 until inventory.getSize()) {
                if (!inventory[j].isEmpty && rand.nextFloat() < 0.2f) {
                    val direction = EnumFacing.byHorizontalIndex(Math.floorMod(j + l, 4))
                    val d0 = pos.x + 0.5 - (direction.xOffset * 0.3125f) + (direction.rotateY().xOffset * 0.3125f)
                    val d1 = pos.y + 0.5
                    val d2 = pos.z + 0.5 - (direction.zOffset * 0.3125f) + (direction.rotateY().zOffset * 0.3125f)
                    for (k in 0..3) {
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 5.0E-4, 0.0)
                    }
                }
            }
        }
    }

    override fun activated(
        state: IBlockState,
        playerIn: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {
        val stack = playerIn.getHeldItem(hand)
        if (stack.item is DebuggerItem) {
            return false
        }

        val block = world.getBlockState(pos.up()).block

        if (!state.getValue(CampfireBlock.LIT)) {
            if (stack.item == Items.FLINT_AND_STEEL || stack.item == Items.FIRE_CHARGE) {
                if (!((block is BlockLiquid) || (block is BlockFluidBase))) {
                    CampfireBlock.setLit(world, pos, true)
                    stack.damageItem(1, playerIn)
                }
            }
        } else if (stack.item.getToolClasses(stack).contains("shovel")) {
            CampfireBlock.setLit(world, pos, false)
            playerIn.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH,1.0F,1.0F)
        }

        if (state.getValue(CampfireBlock.LIT)) {
            if (!world.isRemote) {
                addItem(if (playerIn.isCreative) stack.copy() else stack)
            }
        }

        return true
    }

    private fun drop(stack: ItemStack) {
        if (!world.isRemote) {
            val d0 = world.rand.nextFloat() * 0.5f + 0.25
            val d1 = world.rand.nextFloat() * 0.5f + 0.25
            val d2 = world.rand.nextFloat() * 0.5f + 0.25
            val item = EntityItem(world, pos.x + d0, pos.y + d1, pos.z + d2, stack)
            item.setDefaultPickupDelay()
            world.spawnEntity(item)
        }
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        if (compound.hasKey("Buffer")) {
            inventory.deserializeNBT(compound.getTag("Buffer") as NBTTagCompound)
        }
        if (compound.hasKey("CookingTimes", 11)) {
            val arr = compound.getIntArray("CookingTimes")
            System.arraycopy(arr, 0, cookingTimes, 0, cookingTotalTimes.size.coerceAtMost(arr.size))
        }

        if (compound.hasKey("CookingTotalTimes", 11)) {
            val arr = compound.getIntArray("CookingTotalTimes")
            System.arraycopy(arr, 0, cookingTotalTimes, 0, cookingTotalTimes.size.coerceAtMost(arr.size))
        }
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setTag("Buffer", inventory.serializeNBT())
        compound.setIntArray("CookingTimes", cookingTimes)
        compound.setIntArray("CookingTotalTimes", cookingTotalTimes)
        return compound
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(NBTTagCompound())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(pos, 13, updateTag)
    }

    override fun onDataPacket(net: NetworkManager?, packet: SPacketUpdateTileEntity) {
        readFromNBT(packet.nbtCompound)
    }

    private fun addItem(stack: ItemStack) {
        for (i in 0..3) {
            if (inventory.getStackInSlot(i).isEmpty) {
                CampfireRecipes.getRecipe(stack)?.let {
                    cookingTotalTimes[i] = it.duration
                    cookingTimes[i] = 0
                    inventory.setStackInSlot(i, stack.splitStack(1))
                    inventoryChanged()
                    return
                }
            }
        }
    }

    fun dropAllItems() {
        // fix ghost items
        if (world.isRemote) return

        for (i in 0..3) {
            InventoryHelper.spawnItemStack(
                world,
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                inventory.getStackInSlot(i)
            )
        }
        inventoryChanged()
    }

    private fun inventoryChanged() {
        markDirty()
        getWorld().notifyBlockUpdate(getPos(), world.getBlockState(pos), world.getBlockState(pos), 3)
    }
}