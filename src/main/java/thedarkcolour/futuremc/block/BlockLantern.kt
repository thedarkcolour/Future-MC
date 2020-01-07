package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockBush
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.BlockBase
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.init.Sounds

class BlockLantern : BlockBase("lantern", Material.IRON) {
    init {
        setLightLevel(1f)
        setHardness(5f)
        soundType = Sounds.LANTERN
        setHarvestLevel("pickaxe", 0)
        defaultState = defaultState.withProperty(HANGING, false)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.TAB
    }

    override fun getStateForPlacement(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
        return if (canBlockStay(worldIn, pos)) {
            defaultState.withProperty(HANGING, facing == EnumFacing.DOWN)
        } else defaultState.withProperty(HANGING, isBlockInvalid(worldIn, pos.down()))
    }

    private fun isBlockInvalid(world: World, blockPos: BlockPos): Boolean {
        val state = world.getBlockState(blockPos)
        val block = state.block
        return block is BlockBush || world.isAirBlock(blockPos) || isPiston(state)
    }

    private fun isPiston(state: IBlockState): Boolean {
        return state.material == Material.PISTON
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos)
    }

    private fun canBlockStay(worldIn: World, pos: BlockPos): Boolean {
        return !(isBlockInvalid(worldIn, pos.down()) && isBlockInvalid(worldIn, pos.up()))
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!canBlockStay(worldIn, pos)) {
            dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockToAir(pos)
        }
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, HANGING)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(HANGING, meta != 1)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (state.getValue(HANGING)) 1 else 0
    }

    override fun hasComparatorInputOverride(state: IBlockState): Boolean = true

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        return if (blockState.getValue(HANGING)) 15 else 0
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return if (state.getValue(HANGING)) HANGING_AABB else SITTING_AABB
    }

    override fun getRenderLayer(): BlockRenderLayer = BlockRenderLayer.CUTOUT

    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing): BlockFaceShape = BlockFaceShape.UNDEFINED

    override fun isFullBlock(state: IBlockState): Boolean = false

    override fun isNormalCube(state: IBlockState, source: IBlockAccess, pos: BlockPos): Boolean = false

    override fun isFullCube(state: IBlockState): Boolean = false

    override fun isOpaqueCube(state: IBlockState): Boolean = false

    override fun isTopSolid(state: IBlockState): Boolean = false

    companion object {
        private val HANGING = PropertyBool.create("hanging")
        private val SITTING_AABB = makeAABB(5.0, 0.0, 5.0, 11.0, 9.0, 11.0)
        private val HANGING_AABB = makeAABB(5.0, 1.0, 5.0, 11.0, 10.0, 11.0)
    }
}