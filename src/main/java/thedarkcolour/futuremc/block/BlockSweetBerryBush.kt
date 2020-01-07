package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.IGrowable
import net.minecraft.block.SoundType
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Biomes
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.IPlantable
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.init.FBlocks.SWEET_BERRY_BUSH
import thedarkcolour.futuremc.init.FItems.SWEET_BERRIES
import java.util.*

class BlockSweetBerryBush : BlockFlower("sweet_berry_bush"), IGrowable, IPlantable {
    init {
        soundType = SoundType.PLANT
        defaultState = getBlockState().baseState.withProperty(AGE, 1)
        tickRandomly = true
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, AGE)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(AGE)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) {
            return true
        }
        val item = playerIn.getHeldItem(EnumHand.MAIN_HAND).item
        if (item != Items.DYE || item != Item.getItemFromBlock(SWEET_BERRY_BUSH)) {
            if (worldIn.getBlockState(pos).block.getMetaFromState(state) == 2) {
                worldIn.setBlockState(pos, defaultState.withProperty(AGE, 1))
                Block.spawnAsEntity(worldIn, pos, ItemStack(SWEET_BERRIES))
            }
            if (worldIn.getBlockState(pos).block.getMetaFromState(state) == 3) {
                worldIn.setBlockState(pos, defaultState.withProperty(AGE, 1))
                Block.spawnAsEntity(worldIn, pos, ItemStack(SWEET_BERRIES, 3))
            }
        }
        return false
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        val canGrow = rand.nextInt(20) == 0
        if (worldIn.getLightFromNeighbors(pos) >= 8) {
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, canGrow)) {
                val age = state.getValue(AGE)
                if (age < 3) {
                    worldIn.setBlockState(pos, defaultState.withProperty(AGE, age + 1), 2)
                }
                ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos))
            }
        }
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (entityIn is EntityLivingBase && entityIn !is EntityBee) {
            entityIn.fallDistance = 0.0f
            entityIn.motionX *= 0.800000011920929
            entityIn.motionY *= 0.75
            entityIn.motionZ *= 0.800000011920929
            if (!worldIn.isRemote && state.getValue(AGE) > 0 && (entityIn.prevPosX != entityIn.posX || entityIn.prevPosZ != entityIn.posZ)) {
                val double_1 = Math.abs(entityIn.posX - entityIn.prevPosX)
                val double_2 = Math.abs(entityIn.posZ - entityIn.prevPosZ)
                if (double_1 >= 0.003000000026077032 || double_2 >= 0.003000000026077032) {
                    entityIn.attackEntityFrom(BERRY_BUSH_DAMAGE, 1.0f)
                }
            }
        }
    }

    override fun onBlockHarvested(worldIn: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) {
        if (!worldIn.isRemote) {
            if (worldIn.getBlockState(pos).block.getMetaFromState(state) == 2) {
                worldIn.setBlockState(pos, defaultState.withProperty(AGE, 1))
                Block.spawnAsEntity(worldIn, pos, ItemStack(SWEET_BERRIES))
            } else if (worldIn.getBlockState(pos).block.getMetaFromState(state) == 3) {
                worldIn.setBlockState(pos, defaultState.withProperty(AGE, 1))
                Block.spawnAsEntity(worldIn, pos, ItemStack(SWEET_BERRIES, 3))
            }
        }
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return if (meta < 4) {
            defaultState.withProperty(AGE, meta)
        } else {
            defaultState.withProperty(AGE, 0)
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return if (state.getValue(AGE) == 0) YOUNG else MATURE
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack {
        return ItemStack(SWEET_BERRIES)
    }

    override fun isBiomeValid(biome: Biome): Boolean {
        return biome == Biomes.TAIGA || biome == Biomes.TAIGA_HILLS || biome == Biomes.COLD_TAIGA || biome == Biomes.COLD_TAIGA_HILLS || biome == Biomes.MUTATED_REDWOOD_TAIGA || biome == Biomes.MUTATED_REDWOOD_TAIGA_HILLS || biome == Biomes.REDWOOD_TAIGA || biome == Biomes.REDWOOD_TAIGA_HILLS || biome == Biomes.MUTATED_TAIGA_COLD
    }

    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState): Boolean {
        return state.getValue(AGE) < 3
    }

    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean {
        return state.getValue(AGE) < 3
    }

    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) {
        val age = state.getValue(AGE)
        worldIn.setBlockState(pos, defaultState.withProperty(AGE, age + 1))
    }

    override val flowerChance: Double
        get() = FConfig.villageAndPillage.sweetBerryBush.spawnRate

    companion object {
        private val YOUNG = AxisAlignedBB(0.3, 0.0, 0.3, 0.7, 0.5, 0.7)
        private val MATURE = AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.975, 0.9)
        private val BERRY_BUSH_DAMAGE = DamageSource("berryBush")
        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 3)
    }
}