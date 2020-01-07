package thedarkcolour.futuremc.block

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.core.gui.Gui
import thedarkcolour.futuremc.FutureMC.TAB
import thedarkcolour.futuremc.config.FConfig

class BlockLoom : RotatableBlock("Loom", Material.WOOD, SoundType.WOOD) {
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!worldIn.isRemote && FConfig.villageAndPillage.loom.functionality) {
            Gui.LOOM.open(playerIn, worldIn, pos)
            return true
        }
        return false
    }

    init {
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else TAB
    }
}