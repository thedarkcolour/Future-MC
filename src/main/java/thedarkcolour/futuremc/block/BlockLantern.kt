package thedarkcolour.futuremc.block

import net.minecraft.block.Block
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
import thedarkcolour.futuremc.registry.FSounds

class BlockLantern(regName: String) : BlockBase(regName, Material.IRON) {
    init {
        setLightLevel(if (regName == "soul_fire_lantern") 10.0f / 15.0f else 1.0f)
        setHardness(5f)
        soundType = FSounds.LANTERN
        setHarvestLevel("pickaxe", 0)
        defaultState = defaultState.withProperty(HANGING, false)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.TAB
    }

    override fun getStateForPlacement(
        worldIn: World,
        pos: BlockPos,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        meta: Int,
        placer: EntityLivingBase,
        hand: EnumHand
    ): IBlockState {
        return defaultState.withProperty(HANGING, isBlockInvalid(worldIn, pos, false))
    }

    private fun isBlockInvalid(world: World, blockPos: BlockPos, hanging: Boolean): Boolean {
        val facing = if (hanging) {
            EnumFacing.UP
        } else EnumFacing.DOWN
        val pos = blockPos.offset(facing)
        val state = world.getBlockState(pos)
        val block = state.block
        val faceShape = state.getBlockFaceShape(world, pos, facing.opposite)
        return isExceptBlockForAttachWithPiston(block)
                || !arrayOf(BlockFaceShape.SOLID, BlockFaceShape.CENTER_BIG, BlockFaceShape.CENTER, BlockFaceShape.CENTER_SMALL).any(faceShape::equals)
    }

    private fun isPiston(state: IBlockState): Boolean {
        return state.material == Material.PISTON
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return super.canPlaceBlockAt(worldIn, pos) && canPlaceBlock(worldIn, pos)
    }

    private fun canPlaceBlock(worldIn: World, pos: BlockPos): Boolean {
        return !(isBlockInvalid(worldIn, pos, false) && isBlockInvalid(worldIn, pos, true))
    }

    private fun isInvalidPosition(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
        return isBlockInvalid(worldIn, pos, state.getValue(HANGING))
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (isInvalidPosition(worldIn, pos, state)) {
            dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockToAir(pos)
        }
    }

    override fun createBlockState() = BlockStateContainer(this, HANGING)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(HANGING, meta != 1)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (state.getValue(HANGING)) 1 else 0
    }

    override fun hasComparatorInputOverride(state: IBlockState) = true

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        return if (blockState.getValue(HANGING)) 15 else 0
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return if (state.getValue(HANGING)) HANGING_AABB else SITTING_AABB
    }

    override fun getRenderLayer() = BlockRenderLayer.CUTOUT

    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing) =
        BlockFaceShape.UNDEFINED

    override fun isFullBlock(state: IBlockState) = false

    override fun isNormalCube(state: IBlockState, source: IBlockAccess, pos: BlockPos) = false

    override fun isFullCube(state: IBlockState) = false

    override fun isOpaqueCube(state: IBlockState) = false

    override fun isTopSolid(state: IBlockState) = false

    companion object {
        private val HANGING = PropertyBool.create("hanging")
        private val SITTING_AABB = makeAABB(5.0, 0.0, 5.0, 11.0, 9.0, 11.0)
        private val HANGING_AABB = makeAABB(5.0, 1.0, 5.0, 11.0, 10.0, 11.0)
    }
}