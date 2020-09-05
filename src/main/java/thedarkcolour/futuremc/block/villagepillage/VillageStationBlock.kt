package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.futuremc.client.gui.GuiType

open class VillageStationBlock(properties: Properties, private val guiType: GuiType?, private val functionalityOption: Boolean) : RotatableBlock(properties) {
    override fun onBlockActivated(
        worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer,
        hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float
    ): Boolean {
        if (functionalityOption) {
            return guiType?.open(playerIn, worldIn, pos) ?: false
        }
        return false
    }
}