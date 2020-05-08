package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.registries.IForgeRegistryEntry
import thedarkcolour.core.block.InteractionBlock
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FBlocks.CORNFLOWER
import thedarkcolour.futuremc.registry.FBlocks.LILY_OF_THE_VALLEY
import thedarkcolour.futuremc.registry.FBlocks.WITHER_ROSE
import thedarkcolour.futuremc.registry.FItems.SWEET_BERRIES
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.tile.TileComposter
import java.util.*

class ComposterBlock(properties: Properties) : InteractionBlock(properties) {
    init {
        defaultState = defaultState.withProperty(LEVEL, 0)
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, LEVEL)
    }

    override fun addCollisionBoxToList(
        state: IBlockState, worldIn: World, pos: BlockPos,
        entityBox: AxisAlignedBB, collidingBoxes: List<AxisAlignedBB>, entityIn: Entity?, isActualState: Boolean
    ) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS)
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST)
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH)
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST)
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH)
        if (state.getValue(LEVEL) > 0) {
            val level = if (state.getValue(LEVEL) == 8) 6 else state.getValue(LEVEL) - 1
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                makeAABB(2.0, 2.0, 2.0, 14.0, 3.0 + 2.0 * level, 14.0)
            )
        }
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(LEVEL, meta)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(LEVEL)
    }

    override fun onBlockActivated(
        worldIn: World,
        pos: BlockPos,
        state: IBlockState,
        playerIn: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {
        val stack = playerIn.getHeldItem(hand)
        if (worldIn.getTileEntity(pos) is TileComposter) {
            val te = worldIn.getTileEntity(pos) as TileComposter?
            if (canCompost(stack, state)) {
                spawnBonemealParticles(worldIn, pos)
                if (!worldIn.isRemote) {
                    te!!.addItem(stack, !playerIn.isCreative)
                    return true
                }
            }
            if (!worldIn.isRemote) {
                if (state.getValue(LEVEL) == 8) {
                    te!!.extractBoneMeal()
                }
            }
        }
        return true
    }

    private fun spawnBonemealParticles(worldIn: World, pos: BlockPos) {
        val random = worldIn.rand
        val d0 = 0.53125
        val d1 = 0.13125
        val d2 = 0.7375
        for (i in 0..9) {
            val d3 = random.nextGaussian() * 0.02
            val d4 = random.nextGaussian() * 0.02
            val d5 = random.nextGaussian() * 0.02
            worldIn.spawnParticle(
                EnumParticleTypes.VILLAGER_HAPPY,
                pos.x + d1 + d2 * random.nextFloat().toDouble(),
                pos.y.toDouble() + d0 + random.nextFloat().toDouble() * (1.0 - d0),
                pos.z.toDouble() + d1 + d2 * random.nextFloat().toDouble(),
                d3,
                d4,
                d5
            )
        }
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote) {
            if (worldIn.getBlockState(pos).getValue(LEVEL) == 7) {
                worldIn.setBlockState(pos, defaultState.withProperty(LEVEL, 8))
                worldIn.playSound(null, pos, FSounds.COMPOSTER_READY, SoundCategory.BLOCKS, 1f, 1f)
                (worldIn.getTileEntity(pos) as TileComposter?)!!.inventory.setStackInSlot(
                    0,
                    ItemStack(Items.DYE, 1, 15)
                )
            }
        }
    }

    override fun isFullBlock(state: IBlockState): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    override fun isTopSolid(state: IBlockState): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    override fun createTileEntity(worldIn: World?, state: IBlockState?): TileEntity {
        return TileComposter()
    }

    object ItemsForComposter {
        private val VALID_ITEMS = HashMap<ItemStack, Int>()
        private fun add(registryObject: IForgeRegistryEntry<*>, rarity: Rarity) {
            if (registryObject is Block) {
                add(ItemStack(registryObject), rarity)
            } else {
                add(ItemStack(registryObject as Item), rarity)
            }
        }

        fun add(stack: ItemStack, rarity: Rarity) {
            add(stack, rarity.chance)
        }

        @kotlin.jvm.JvmStatic
        fun add(stack: ItemStack, rarity: Int) {
            VALID_ITEMS[stack] = rarity
        }

        @kotlin.jvm.JvmStatic
        fun getChance(stack: ItemStack): Int {
            if (stack.isEmpty) return -1
            val item =
                VALID_ITEMS.keys.stream().filter { itemStack: ItemStack -> itemStack.isItemEqual(stack) }.findFirst()
            return if (item.isPresent) VALID_ITEMS[item.get()]!! else -1
        }

        @kotlin.jvm.JvmStatic
        fun remove(stack: ItemStack?) {
            VALID_ITEMS.remove(stack)
        }

        @kotlin.jvm.JvmStatic
        fun clear() {
            VALID_ITEMS.clear()
        }

        enum class Rarity(val chance: Int) {
            COMMON(30), UNCOMMON(50), RARE(65), EPIC(85), LEGENDARY(100);

        }

        init {
            if (FConfig.villageAndPillage.composter) { // COMMON
                add(Items.BEETROOT_SEEDS, Rarity.COMMON)
                add(ItemStack(Blocks.TALLGRASS, 1, 1), Rarity.COMMON)
                add(ItemStack(Blocks.LEAVES, 1, 0), Rarity.COMMON)
                add(ItemStack(Blocks.LEAVES, 1, 1), Rarity.COMMON)
                add(ItemStack(Blocks.LEAVES, 1, 2), Rarity.COMMON)
                add(ItemStack(Blocks.LEAVES, 1, 3), Rarity.COMMON)
                add(ItemStack(Blocks.LEAVES2, 1, 0), Rarity.COMMON)
                add(ItemStack(Blocks.LEAVES2, 1, 1), Rarity.COMMON)
                add(Items.MELON_SEEDS, Rarity.COMMON)
                add(Items.PUMPKIN_SEEDS, Rarity.COMMON)
                add(ItemStack(Blocks.SAPLING, 1, 0), Rarity.COMMON)
                add(ItemStack(Blocks.SAPLING, 1, 1), Rarity.COMMON)
                add(ItemStack(Blocks.SAPLING, 1, 2), Rarity.COMMON)
                add(ItemStack(Blocks.SAPLING, 1, 3), Rarity.COMMON)
                add(ItemStack(Blocks.SAPLING, 1, 4), Rarity.COMMON)
                add(ItemStack(Blocks.SAPLING, 1, 5), Rarity.COMMON)
                add(SWEET_BERRIES, Rarity.COMMON)
                add(Items.WHEAT_SEEDS, Rarity.COMMON)
                // UNCOMMON
                add(Items.MELON, Rarity.UNCOMMON)
                add(Items.REEDS, Rarity.UNCOMMON)
                add(Blocks.CACTUS, Rarity.UNCOMMON)
                add(Blocks.VINE, Rarity.UNCOMMON)
                add(ItemStack(Blocks.DOUBLE_PLANT, 1, 2), Rarity.UNCOMMON)
                // RARE
                add(Items.APPLE, Rarity.RARE)
                add(Items.BEETROOT, Rarity.RARE)
                add(Items.CARROT, Rarity.RARE)
                add(ItemStack(Items.DYE, 1, 3), Rarity.RARE)
                add(ItemStack(Blocks.TALLGRASS, 1, 2), Rarity.RARE)
                add(ItemStack(Blocks.DOUBLE_PLANT, 1, 3), Rarity.RARE)
                add(Blocks.RED_FLOWER, Rarity.RARE)
                add(Blocks.YELLOW_FLOWER, Rarity.RARE)
                add(LILY_OF_THE_VALLEY, Rarity.RARE)
                add(CORNFLOWER, Rarity.RARE)
                add(WITHER_ROSE, Rarity.RARE)
                add(Blocks.DOUBLE_PLANT, Rarity.RARE)
                add(ItemStack(Blocks.DOUBLE_PLANT, 1, 1), Rarity.RARE)
                add(ItemStack(Blocks.DOUBLE_PLANT, 1, 4), Rarity.RARE)
                add(ItemStack(Blocks.DOUBLE_PLANT, 1, 5), Rarity.RARE)
                add(Blocks.WATERLILY, Rarity.RARE)
                add(Blocks.MELON_BLOCK, Rarity.RARE)
                add(Blocks.BROWN_MUSHROOM, Rarity.RARE)
                add(Blocks.RED_MUSHROOM, Rarity.RARE)
                add(Items.POTATO, Rarity.RARE)
                add(Blocks.PUMPKIN, Rarity.RARE)
                add(Items.WHEAT, Rarity.RARE)
                // EPIC
                add(Items.BAKED_POTATO, Rarity.EPIC)
                add(Items.BREAD, Rarity.EPIC)
                add(Items.COOKIE, Rarity.EPIC)
                add(Blocks.HAY_BLOCK, Rarity.EPIC)
                // LEGENDARY
                add(Items.CAKE, Rarity.LEGENDARY)
                add(Items.PUMPKIN_PIE, Rarity.LEGENDARY)
            }
        }
    }

    @Suppress("HasPlatformType")
    companion object {
        val LEVEL = PropertyInteger.create("level", 0, 8)
        private val AABB_LEGS = makeAABB(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)
        private val AABB_WALL_NORTH = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.125)
        private val AABB_WALL_SOUTH = AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 1.0, 1.0)
        private val AABB_WALL_EAST = AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 1.0, 1.0)
        private val AABB_WALL_WEST = AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 1.0, 1.0)

        fun canCompost(stack: ItemStack, state: IBlockState): Boolean {
            return ItemsForComposter.getChance(stack) != -1 && !isFull(state)
        }

        fun isFull(state: IBlockState): Boolean {
            return state.getValue(LEVEL) >= 7
        }
    }
}