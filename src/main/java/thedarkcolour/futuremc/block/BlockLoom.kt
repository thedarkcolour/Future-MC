package thedarkcolour.futuremc.block

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.core.gui.Gui

class BlockLoom(properties: Properties?) : RotatableBlock(properties) {
    override fun onBlockActivated(
        worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer,
        hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float
    ): Boolean {
        if (!worldIn.isRemote) {
            Gui.LOOM.open(playerIn, worldIn, pos)
            return true
        }
        return true
    }
}