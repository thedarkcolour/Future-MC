package thedarkcolour.futuremc.block

import git.jbredwards.fluidlogged_api.common.block.IFluidloggable
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.common.Optional
import thedarkcolour.core.block.FBlock

@Optional.Interface(iface = "git.jbredwards.fluidlogged_api.common.block.IFluidloggable", modid = "fluidlogged_api")
class ChainBlock(properties: Properties) : FBlock(properties), IFluidloggable {
    override fun isPassable(worldIn: IBlockAccess?, pos: BlockPos?) = false
    override fun getRenderLayer() = BlockRenderLayer.CUTOUT

    override fun getBlockFaceShape(worldIn: IBlockAccess?, state: IBlockState?, pos: BlockPos?, face: EnumFacing): BlockFaceShape? {
        return if (face != EnumFacing.UP && face != EnumFacing.DOWN) BlockFaceShape.MIDDLE_POLE_THIN else BlockFaceShape.CENTER_SMALL
    }

    override fun isBlockNormalCube(state: IBlockState): Boolean = false
    override fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false
    override fun isFullBlock(state: IBlockState) = false
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isNormalCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false
    override fun canPlaceTorchOnTop(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false
    override fun isTopSolid(state: IBlockState) = false
}