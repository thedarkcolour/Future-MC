package thedarkcolour.futuremc.block

import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.IFluidBlock
import thedarkcolour.core.block.InteractionBlock

class BlockWaterlogged : InteractionBlock("waterlogged_block"), IFluidBlock {
    // return null to have our own tile handling
    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return null
    }

    override fun drain(world: World?, pos: BlockPos?, doDrain: Boolean): FluidStack? {
        if ()
    }
}