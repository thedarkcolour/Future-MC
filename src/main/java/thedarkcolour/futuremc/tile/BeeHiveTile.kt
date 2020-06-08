//@file:Suppress("DEPRECATION")

package thedarkcolour.futuremc.tile

import net.minecraft.block.Block
import net.minecraft.block.BlockFire
import net.minecraft.block.material.Material
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
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.storage.AnvilChunkLoader
import sun.reflect.Reflection
import thedarkcolour.core.tile.InteractionTile
import thedarkcolour.futuremc.block.BeeHiveBlock
import thedarkcolour.futuremc.block.CampfireBlock
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

class BeeHiveTile : InteractionTile(), ITickable {
    private val bees: ArrayList<Bee> = ArrayList()
    private var flowerPos: BlockPos? = null
    var honeyLevel: Int = 0
        private set

    fun isNearFire(): Boolean {
        for (pos in BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (world.getBlockState(pos).block is BlockFire) {
                return true
            }
        }
        return false
    }

    private fun hasNoBees() = bees.isEmpty()

    fun isFullOfBees() = bees.size == 3

    // release the BEES
    fun angerBees(playerIn: EntityPlayer?, state: BeeState) {
        val bees = tryReleaseBee(state)

        if (playerIn != null) {
            for (bee in bees) {
                if (playerIn.squaredDistanceTo(bee) <= 16.0) {
                    if (!isSmoked()) {
                        bee.setBeeAttacker(playerIn)
                    } else {
                        bee.setCannotEnterHiveTicks(400)
                    }
                }
            }
        }
    }

    private fun tryReleaseBee(state: BeeState): ArrayList<EntityBee> {
        return ArrayList<EntityBee>().also { list ->
            bees.removeIf { releaseBee(it.data, list, state) }
        }
    }

    fun getBeeCount() = bees.size

    private fun isSmoked() = CampfireBlock.isLitInRange(world, pos, 5)

    fun tryEnterHive(entityIn: BeeEntity, isDelivering: Boolean, i: Int = 0): Nothing = TODO("DARK! FIX THE TILE ENTITY!")

    fun tryEnterHive(entityIn: EntityBee, isDelivering: Boolean, i: Int = 0) {
        if (bees.size < 3) {
            entityIn.dismountRidingEntity()
            entityIn.removePassengers()

            val tag = NBTTagCompound()
            entityIn.writeToNBTOptional(tag)
            bees.add(Bee(tag, i, if (isDelivering) 2400 else 600))

            if (world != null) {
                if (entityIn.hasFlower() && (!hasFlowerPos() || world.rand.nextBoolean())) {
                    flowerPos = entityIn.flowerPos!!
                }

                world.playSound(pos, FSounds.BEE_ENTER_HIVE, SoundCategory.BLOCKS, 1.0f, 1.0f)
            }
            entityIn.setDead()
        }
    }

    private fun releaseBee(tag: NBTTagCompound, list: MutableList<EntityBee>?, beeState: BeeState): Boolean {
        if ((!world.isDaytime && world.isRainingAt(pos)) && beeState != BeeState.EMERGENCY) {
            return false
        } else {
            tag.removeTag("Passengers")
            tag.removeTag("Leash")
            tag.removeTag("UUIDLeast")
            tag.removeTag("UUIDMost")

            val state = world.getBlockState(pos)
            val direction = state.getValue(BeeHiveBlock.FACING)
            val pos1 = pos.offset(direction)
            val flag = isExitBlocked(world, pos1)

            if (flag && beeState != BeeState.EMERGENCY) {
                return false
            } else {
                val entity = AnvilChunkLoader.readWorldEntity(tag, world, false)

                if (entity != null) {
                    val width = entity.width
                    val d0 = if (flag) 0.0 else 0.55 + (width / 2.0f)
                    val d1 = pos.x + 0.5 + d0 * direction.xOffset
                    val d2 = pos.y + 0.5 - (entity.height / 2.0f)
                    val d3 = pos.z + 0.5 + d0 * direction.zOffset
                    entity.setLocationAndAngles(d1, d2, d3, entity.rotationYaw, entity.rotationPitch)

                    if (entity !is EntityBee) {
                        return false
                    } else {
                        if (hasFlowerPos() && !entity.hasFlower() && world.rand.nextFloat() < 0.9f) {
                            entity.flowerPos = flowerPos
                        }

                        if (beeState == BeeState.HONEY_DELIVERED) {
                            entity.onHoneyDelivered()

                            setHoneyLevel(honeyLevel + if (world.rand.nextInt(100) == 0) 2 else 1, true)
                        }

                        entity.resetPollinationTicks()
                        list?.add(entity)
                    }

                    val pos = pos
                    world.playSound(null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), FSounds.ENTITY_BEE_EXIT_HIVE, SoundCategory.BLOCKS, 1.0f, 1.0f)

                    return world.spawnEntity(entity)
                }
            }
        }

        return false
    }

    private fun isExitBlocked(worldIn: World, pos: BlockPos): Boolean {
        return worldIn.getBlockState(pos).material != Material.AIR
    }

    private fun hasFlowerPos() = flowerPos != null

    private fun tickBees() {
        val iterator = bees.iterator()

        while (iterator.hasNext()) {
            val bee = iterator.next()
            if (bee.ticksInHive > bee.minOccupationTicks) {
                val tag = bee.data
                val state = if (tag.getBoolean("HasNectar")) BeeState.HONEY_DELIVERED else BeeState.BEE_RELEASED

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
            if (!hasNoBees() && world.rand.nextDouble() < 0.005) {
                val pos = pos
                val x = pos.x + 0.5
                val y = pos.y.toDouble()
                val z = pos.z + 0.5
                world.playSound(x, y, z, FSounds.BEE_WORK, SoundCategory.BLOCKS, 1.0f, 1.0f)
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        bees.clear()
        val list = compound.getTagList("Bees", 10)
        setHoneyLevel(compound.getInteger("HoneyLevel"), Reflection.getCallerClass(2) != TileEntity::class.java)

        for (base in list) {
            val tag = base as NBTTagCompound
            val bee = Bee(
                tag.getTag("EntityData") as NBTTagCompound,
                tag.getInteger("TicksInHive"),
                tag.getInteger("MinOccupationTicks")
            )
            bees.add(bee)
        }
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setTag("Bees", getBees())
        compound.setInteger("HoneyLevel", honeyLevel)
        if (hasFlowerPos())
            compound.setTag("FlowerPos", NBTUtil.createPosTag(flowerPos!!))
        return compound
    }

    fun setHoneyLevel(level: Int, updateState: Boolean) {
        honeyLevel = if (level >= 5) {
            5
        } else {
            level
        }

        if (updateState) {
            world.setBlockState(
                pos, getBlockType().defaultState
                    .withProperty(BeeHiveBlock.IS_FULL, honeyLevel == 5)
                    .withProperty(BeeHiveBlock.FACING, world.getBlockState(pos).getValue(BeeHiveBlock.FACING))
            )
        }
    }

    private fun isFullOfHoney() = honeyLevel >= 5

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
        var action = false
        if (isFullOfHoney()) {
            if (stack.item == Items.SHEARS) {
                world.playSound(
                    null,
                    playerIn.posX,
                    playerIn.posY,
                    playerIn.posZ,
                    FSounds.BEEHIVE_SHEAR,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f
                )
                dropHoneyCombs(world, pos)
                stack.damageItem(1, playerIn)
                action = true
            } else if (stack.item == Items.GLASS_BOTTLE) {
                stack.shrink(1)
                world.playSound(
                    null,
                    playerIn.posX,
                    playerIn.posY,
                    playerIn.posZ,
                    SoundEvents.ITEM_BOTTLE_FILL,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f
                )
                if (stack.isEmpty) {
                    playerIn.setHeldItem(hand, ItemStack(FItems.HONEY_BOTTLE))
                } else if (!playerIn.inventory.addItemStackToInventory(ItemStack(FItems.HONEY_BOTTLE))) {
                    playerIn.dropItem(ItemStack(FItems.HONEY_BOTTLE), false)
                }

                action = true
            }
        }

        return if (action) {
            emptyHoney(world, pos, playerIn)
            true
        } else {
            false
        }
    }

    fun dropHoneyCombs(worldIn: World, pos: BlockPos) {
        for (i in 0..2) {
            Block.spawnAsEntity(worldIn, pos, ItemStack(FItems.HONEYCOMB))
        }
    }

    fun emptyHoney(worldIn: World, pos: BlockPos, playerIn: EntityPlayer?) {
        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(BeeHiveBlock.IS_FULL, false), 3)
        val tile = worldIn.getTileEntity(pos)

        if (tile is BeeHiveTile) {
            tile.setHoneyLevel(0, true)
            tile.angerBees(playerIn, BeeState.HONEY_DELIVERED)
        }
    }

    override fun broken(state: IBlockState, playerIn: EntityPlayer) {
        if (!world.isRemote) {
            val isCreative = playerIn.isCreative

            if (isCreative || EnchantmentHelper.getEnchantmentLevel(
                    Enchantments.SILK_TOUCH,
                    playerIn.heldItemMainhand
                ) == 1
            ) {
                val item = EntityItem(
                    world,
                    pos.x.toDouble(),
                    pos.y.toDouble(),
                    pos.z.toDouble(),
                    getItemWithBees(isCreative) ?: return
                )
                item.setDefaultPickupDelay()

                world.spawnEntity(item)
            } else {
                if (!world.isRemote && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, playerIn.heldItemMainhand) == 0) {
                    angerBees(playerIn, BeeState.HONEY_DELIVERED)
                    world.updateComparatorOutputLevel(pos, blockType)
                }
                val nearbyBees = world.getEntitiesWithinAABB(EntityBee::class.java, AxisAlignedBB(pos).expand(8.0, 6.0, 8.0))

                if (nearbyBees.isNotEmpty()) {
                    val nearbyPlayers = world.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB(pos).expand(8.0, 6.0, 8.0))
                    val players = nearbyPlayers.size

                    // use the variable because we have it and
                    // it saves on method calls
                    if (players != 0) {
                        for (bee in nearbyBees) {
                            if (bee.attackTarget == null) {
                                bee.setBeeAttacker(nearbyPlayers[world.rand.nextInt(players)])
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getItemWithBees(isCreative: Boolean): ItemStack? {
        val stack = ItemStack(world.getBlockState(pos).block)
        val tag = NBTTagCompound()

        if (hasNoBees() && honeyLevel == 0) {
            return if (isCreative) null else stack
        }

        tag.setTag("Bees", getBees())
        tag.setInteger("HoneyLevel", honeyLevel)
        stack.setTagInfo("BlockEntityTag", tag)
        return stack
    }

    private fun getBees(): NBTTagList {
        return NBTTagList().also { bees.map(Bee::deserialize).forEach(it::appendTag) }
    }

    private class Bee constructor(val data: NBTTagCompound, var ticksInHive: Int, val minOccupationTicks: Int) {
        init {
            data.setBoolean("Leashed", false)
        }

        fun deserialize(): NBTTagCompound {
            val tag = NBTTagCompound()
            tag.setTag("EntityData", data)
            tag.setInteger("TicksInHive", ticksInHive)
            tag.setInteger("MinOccupationTicks", minOccupationTicks)
            return tag
        }
    }

    enum class BeeState {
        HONEY_DELIVERED,
        BEE_RELEASED,
        EMERGENCY,
    }
}