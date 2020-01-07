@file:Suppress("DEPRECATION")

package thedarkcolour.futuremc.tile

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTUtil
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ITickable
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.playSound
import net.minecraft.world.squaredDistanceTo
import sun.reflect.Reflection
import thedarkcolour.core.tile.InteractionTile
import thedarkcolour.core.util.make
import thedarkcolour.futuremc.block.BlockBeeHive
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.init.FBlocks
import thedarkcolour.futuremc.init.FItems
import thedarkcolour.futuremc.init.Sounds
import java.util.*
import kotlin.collections.ArrayList

class TileBeeHive : InteractionTile(), ITickable {
    private val bees: ArrayList<Bee> = ArrayList()
    private var flowerPos: BlockPos = BlockPos.ORIGIN
    var honeyLevel: Int = 0
            private set

    fun getNumberOfBees() = bees.size

    fun isFullOfBees() = bees.size == 3

    fun angerBees(playerIn: EntityPlayer?, state: BeeState) {
        val list: ArrayList<EntityBee> = tryReleaseBee(state)

        playerIn?.let {
            list.forEach { bee ->
                if (playerIn.squaredDistanceTo(bee) <= 16) {
                    if (isNotBeingSmoked(world, pos)) {
                        bee.setBeeAttacker(playerIn)
                    } else {
                        bee.setCannotEnterHiveTicks(400)
                    }
                }
            }
        }
    }

    private fun isNotBeingSmoked(worldIn: World, pos: BlockPos): Boolean {
        for (i in 1..5) {
            if (worldIn.getBlockState(pos.down(i)).block == FBlocks.CAMPFIRE) {
                return true
            }
        }

        return false
    }

    private fun tryReleaseBee(state: BeeState): ArrayList<EntityBee> {
        return make(ArrayList(), { list ->
            bees.removeIf { releaseBee(it.data, list, state) }
        })
    }

    fun tryEnterHive(entityIn: EntityBee, isDelivering: Boolean, i: Int = 0) {
        if (bees.size < 3) {
            val tag = NBTTagCompound()
            entityIn.writeToNBT(tag)
            bees.add(Bee(tag, i, if (isDelivering) 2400 else 600))
            world?.let {
                if (flowerPos == BlockPos.ORIGIN || entityIn.hasFlower() && it.rand.nextBoolean()) {
                    flowerPos = entityIn.flowerPos
                }

                world.playSound(pos, Sounds.BEE_ENTER_HIVE, SoundCategory.BLOCKS, 1.0f, 1.0f)
            }
            entityIn.setDead()
        }
    }

    private fun releaseBee(data: NBTTagCompound, list: MutableList<EntityBee>?, beeState: BeeState): Boolean {
        if (world.isDaytime && !world.isRainingAt(pos)) {
            data.removeTag("Passengers")
            data.removeTag("Leash")
            data.removeTag("UUIDLeast")
            data.removeTag("UUIDMost")
            var optionalPos = Optional.empty<BlockPos>()
            val state = world.getBlockState(pos)
            val facing = state.getValue(BlockBeeHive.FACING)
            val pos1 = pos.offset(facing, 2)

            if (world.getBlockState(pos1).getCollisionBoundingBox(world, pos1) == Block.NULL_AABB) {
                optionalPos = Optional.of(pos1)
            }

            var blockPos: BlockPos
            if (!optionalPos.isPresent) {
                for (f in EnumFacing.HORIZONTALS) {
                    blockPos = pos.add(f.xOffset shl 1, f.yOffset, f.zOffset shl 1)
                    if (world.getBlockState(blockPos).getCollisionBoundingBox(world, blockPos) == Block.NULL_AABB) {
                        optionalPos = Optional.of(blockPos)
                        break
                    }
                }
            }

            if (!optionalPos.isPresent) {
                for (f in arrayOf(EnumFacing.UP, EnumFacing.DOWN)) {
                    blockPos = pos.add(f.xOffset shl 1, f.yOffset, f.zOffset shl 1)
                    if (world.getBlockState(blockPos).getCollisionBoundingBox(world, blockPos) == Block.NULL_AABB) {
                        optionalPos = Optional.of(blockPos)
                    }
                }
            }

            if (optionalPos.isPresent) {
                blockPos = optionalPos.get()
                val bee = EntityBee(world)
                bee.readFromNBT(data)
                bee.setPositionAndRotation(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), bee.rotationYaw, bee.rotationPitch)

                if (hasFlowerPos() && !bee.hasFlower() && world.rand.nextFloat() < 0.9f) {
                    bee.flowerPos = flowerPos
                }

                if (beeState == BeeState.WORKING) {
                    bee.onHoneyDelivered()
                    if (world.getTileEntity(pos) is TileBeeHive) {
                        val i = if (world.rand.nextInt(100) == 0) 2 else 1
                        setHoneyLevel(honeyLevel + i, true)
                    }
                }

                list?.let {
                    bee.resetPollinationTicks()
                    it.add(bee)
                }

                world.playSound(null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), Sounds.BEE_EXIT_HIVE, SoundCategory.BLOCKS, 1.0f, 1.0f)
                world.spawnEntity(bee)
                return true // return true in case of broken bee
            }
        }

        return false
    }

    private fun hasFlowerPos(): Boolean = flowerPos != BlockPos.ORIGIN

    private fun tickBees() {
        val iterator = bees.iterator()

        while (iterator.hasNext()) {
            val bee = iterator.next()
            if (bee.ticksInHive > bee.minOccupationTicks) {
                val tag = bee.data
                val state = if (tag.getBoolean("HasNectar")) BeeState.WORKING else BeeState.DELIVERED

                if (releaseBee(tag, null, state)) {
                    iterator.remove()
                }
            } else {
                ++bee.ticksInHive
            }
        }
    }

    override fun update() {
        if (!world.isRemote) {
            tickBees()
            if (bees.isNotEmpty() && world.rand.nextDouble() < 0.005) {
                val x = pos.x + 0.5
                val y = pos.y.toDouble()
                val z = pos.z + 0.5
                world.playSound(x, y, z, Sounds.BEE_WORK, SoundCategory.BLOCKS, 1.0f, 1.0f)
            }
        }
    }

    fun setHoneyLevel(level: Int, updateState: Boolean) {
        honeyLevel = if (level >= 5) {
            5
        } else {
            level
        }

        if (updateState) {
            world.setBlockState(pos, getBlockType().defaultState
                    .withProperty(BlockBeeHive.IS_FULL, honeyLevel == 5)
                    .withProperty(BlockBeeHive.FACING, world.getBlockState(pos).getValue(BlockBeeHive.FACING)))
        }
    }

    private fun isFullOfHoney() = honeyLevel >= 5

    override fun activated(state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val stack = playerIn.getHeldItem(hand)
        var action = false
        if (isFullOfHoney()) {
            if (stack.item == Items.SHEARS) {
                world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, Sounds.BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0f, 1.0f)
                dropHoneyCombs(world, pos)
                stack.damageItem(1, playerIn)
                action = true
            } else if (stack.item == Items.GLASS_BOTTLE) {
                stack.shrink(1)
                world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f)
                if (stack.isEmpty) {
                    playerIn.setHeldItem(hand, ItemStack(FItems.HONEY_BOTTLE))
                } else if (!playerIn.inventory.addItemStackToInventory(ItemStack(FItems.HONEY_BOTTLE))) {
                    playerIn.dropItem(ItemStack(FItems.HONEY_BOTTLE), false)
                }

                action = true
            }
        }

        return if (action) {
            emptyHoney(world, state, pos, playerIn)
            true
        } else {
            false
        }
    }

    override fun broken(state: IBlockState, playerIn: EntityPlayer) {
        if (!world.isRemote && (playerIn.isCreative || EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, playerIn.heldItemMainhand) == 1)) {
            val item = EntityItem(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), getItemWithBees())
            item.setDefaultPickupDelay()
            world.spawnEntity(item)
        }
    }

    private fun getItemWithBees(): ItemStack {
        val stack = ItemStack(world.getBlockState(pos).block)
        val tag = NBTTagCompound()

        if (bees.isNotEmpty()) {
            tag.setTag("Bees", getBees())
        }

        tag.setInteger("HoneyLevel", honeyLevel)
        stack.setTagInfo("BlockEntityTag", tag)
        return stack
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        bees.clear()
        val list = compound.getTagList("Bees", 10)
        setHoneyLevel(compound.getInteger("HoneyLevel"), Reflection.getCallerClass(2) != TileEntity::class.java)

        for (base in list) {
            val tag = base as NBTTagCompound
            val bee = Bee(tag.getTag("EntityData") as NBTTagCompound, tag.getInteger("TicksInHive"), tag.getInteger("MinOccupationTicks"))
            bees.add(bee)
        }
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setTag("Bees", getBees())
        compound.setTag("FlowerPos", NBTUtil.createPosTag(flowerPos))
        compound.setInteger("HoneyLevel", honeyLevel)
        return compound
    }

    private fun getBees(): NBTTagList {
        val list = NBTTagList()

        for (bee in bees) {
            val tag = NBTTagCompound()
            tag.setTag("EntityData", bee.data)
            tag.setInteger("TicksInHive", bee.ticksInHive)
            tag.setInteger("MinOccupationTicks", bee.minOccupationTicks)
            list.appendTag(tag)
        }

        return list
    }

    private fun dropHoneyCombs(worldIn: World, pos: BlockPos) {
        for (i in 0..2) {
            Block.spawnAsEntity(worldIn, pos, ItemStack(FItems.HONEYCOMB))
        }
    }

    private fun emptyHoney(worldIn: World, state: IBlockState, pos: BlockPos, playerIn: EntityPlayer) {
        worldIn.setBlockState(pos, state.withProperty(BlockBeeHive.IS_FULL, false), 3)
        val tile = worldIn.getTileEntity(pos)

        if (tile is TileBeeHive) {
            val hive = tile as TileBeeHive?
            hive!!.setHoneyLevel(0, true)
            hive.angerBees(playerIn, BeeState.DELIVERED)
        }
    }

    private class Bee constructor(val data: NBTTagCompound, var ticksInHive: Int, val minOccupationTicks: Int) {
        init {
            data.setBoolean("Leashed", false)
        }
    }

    enum class BeeState {
        WORKING,
        DELIVERED
    }
}