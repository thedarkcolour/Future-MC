package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.BlockLiquid
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fluids.BlockFluidBase
import net.minecraftforge.fluids.IFluidBlock
import thedarkcolour.core.block.InteractionBlock
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FParticles
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.tile.CampfireTile
import java.util.*

@Suppress("HasPlatformType")
class CampfireBlock(properties: Properties) : InteractionBlock(properties) {
    init {
        defaultState = getBlockState().baseState.withProperty(LIT, true).withProperty(FACING, EnumFacing.NORTH)
        useNeighborBrightness = true
    }

    override fun createTileEntity(worldIn: World?, state: IBlockState?): TileEntity {
        return CampfireTile()
    }

    override fun createBlockState() = BlockStateContainer(this, FACING, LIT, SIGNAL)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState
            .withProperty(LIT, meta and 4 != 0)
            .withProperty(FACING, EnumFacing.byHorizontalIndex(meta and -5))
            .withProperty(SIGNAL, meta and 8 != 0)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return (if (state.getValue(SIGNAL)) 8 else 0) or (if (state.getValue(LIT)) 4 else 0) or state.getValue(FACING).horizontalIndex
    }

    override fun getStateForPlacement(
        worldIn: World, pos: BlockPos, facing: EnumFacing,
        hitX: Float, hitY: Float, hitZ: Float,
        meta: Int, placer: EntityLivingBase
    ): IBlockState {
        val block = worldIn.getBlockState(pos.up()).block
        return defaultState.withProperty(FACING, placer.horizontalFacing.opposite)
            .withProperty(LIT, block !is BlockLiquid || block is IFluidBlock)
            .withProperty(SIGNAL, worldIn.getBlockState(pos.down()).block == Blocks.WHEAT)
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        if (worldIn.getTileEntity(pos) is CampfireTile) {
            (worldIn.getTileEntity(pos) as CampfireTile).dropAllItems()
        }
        super.breakBlock(worldIn, pos, state)
    }

    override fun harvestBlock(
        worldIn: World, player: EntityPlayer, pos: BlockPos, state: IBlockState, te: TileEntity?, stack: ItemStack
    ) {
        player.addExhaustion(0.005f)
        harvesters.set(player)
        val i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)
        dropBlockAsItem(worldIn, pos, state, i)
        harvesters.set(null)
    }

    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int) = Items.COAL
    override fun damageDropped(state: IBlockState) = 1
    override fun quantityDropped(random: Random) = 2

    override fun getLightValue(state: IBlockState): Int {
        return if (state.getValue(LIT)) 15 else 0
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        val block = worldIn.getBlockState(pos.up()).block
        if (state.getValue(LIT) && (block is BlockFluidBase || block is BlockLiquid)) {
            setLit(worldIn, pos, false)

            if (worldIn.isRemote) {
                for (i in 0..19) {
                    spawnSmokeParticles(worldIn, pos, state.getValue(SIGNAL), true)
                }
            }
        }

        val down = worldIn.getBlockState(pos.down())
        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(SIGNAL, down.block == Blocks.HAY_BLOCK))
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (FConfig.villageAndPillage.campfire.damage) {
            if (entityIn is EntityLivingBase && !entityIn.isImmuneToFire() && state.getValue(LIT)) {
                entityIn.attackEntityFrom(DamageSource.IN_FIRE, 1.0f)
            }
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = BOUNDING_BOX

    override fun getPickBlock(
        state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer
    ): ItemStack {
        return ItemStack(this)
    }

    override fun getRenderLayer() = BlockRenderLayer.CUTOUT

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        neighborChanged(state, worldIn, pos, worldIn.getBlockState(pos).block, pos)
    }

    override fun isBlockNormalCube(state: IBlockState): Boolean = false
    override fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false
    override fun isFullBlock(state: IBlockState) = false
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isNormalCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false
    override fun canPlaceTorchOnTop(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false
    override fun isTopSolid(state: IBlockState) = false
    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing) = BlockFaceShape.UNDEFINED

    override fun randomDisplayTick(
        state: IBlockState, worldIn: World, pos: BlockPos, rand: Random
    ) {
        if (state.getValue(LIT)) {
            if (rand.nextInt(10) == 0) {
                worldIn.playSound(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, FSounds.CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5f + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false)
            }

            if (rand.nextInt(5) == 0) {
                for (i in 0 until rand.nextInt(1) + 1) {
                    worldIn.spawnParticle(EnumParticleTypes.LAVA,
                        pos.x + 0.5, pos.y + 0.5, pos.z + 0.5,
                        (rand.nextFloat() / 2.0), 5.0E-5, (rand.nextFloat() / 2.0)
                    )
                }
            }
        }
    }

    companion object {
        private val BOUNDING_BOX = makeCube(0.0, 0.0, 0.0, 16.0, 7.0, 16.0) //new AxisAlignedBB(0,0,0,1,0.375,1);
        val FACING: PropertyDirection = BlockHorizontal.FACING
        val LIT: PropertyBool = PropertyBool.create("lit")
        val SIGNAL: PropertyBool = PropertyBool.create("signal")

        fun spawnSmokeParticles(worldIn: World, pos: BlockPos, isSignal: Boolean, spawnExtraSmoke: Boolean) {
            val rng = worldIn.rand
            val type = if (isSignal) {
                FParticles.CAMPFIRE_COSY_SMOKE
            } else {
                FParticles.CAMPFIRE_SIGNAL_SMOKE
            }
            worldIn.spawnParticle(
                type,
                pos.x + 0.5 + rng.nextDouble() / 3.0 * if (rng.nextBoolean()) 1.0 else -1.0,
                pos.y + rng.nextDouble() + rng.nextDouble(),
                pos.z + 0.5 + rng.nextDouble() / 3.0 * if (rng.nextBoolean()) 1.0 else -1.0,
                0.0,
                0.07,
                0.0
            )
            if (spawnExtraSmoke) {
                worldIn.spawnParticle(
                    EnumParticleTypes.SMOKE_NORMAL,
                    pos.x + 0.25 + rng.nextDouble() / 2.0 * (if (rng.nextBoolean()) 1 else -1).toDouble(),
                    pos.y.toDouble() + 0.4,
                    pos.z.toDouble() + 0.25 + rng.nextDouble() / 2.0 * (if (rng.nextBoolean()) 1 else -1).toDouble(),
                    0.0,
                    0.005,
                    0.0
                )
            }
        }

        fun isLitInRange(worldIn: World, pos: BlockPos, range: Int): Boolean {
            for (i in 0..range) {
                val down = pos.down(i)
                val state = worldIn.getBlockState(down)
                if (isLitCampfire(state)) {
                    return true
                }

                val box = state.getCollisionBoundingBox(worldIn, pos)
                val flag = box != null && BOUNDING_BOX.contains(Vec3d(box.minX, box.minY, box.minZ)) && BOUNDING_BOX.contains(Vec3d(box.maxX, box.maxY, box.maxZ))
                if (flag) {
                    return isLitCampfire(worldIn.getBlockState(down.down()))
                }
            }

            return false
        }

        private fun isLitCampfire(state: IBlockState): Boolean {
            return state.block is CampfireBlock && state.getValue(LIT)
        }

        fun setLit(worldIn: World, pos: BlockPos, lit: Boolean) {
            if (!lit) {
                if (worldIn.getTileEntity(pos) is CampfireTile) {
                    (worldIn.getTileEntity(pos) as CampfireTile?)!!.dropAllItems()
                }
            }
            worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(LIT, lit))
        }
    }
}