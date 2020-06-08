package thedarkcolour.futuremc.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.item.ModeledItemBlock
import thedarkcolour.futuremc.registry.FBlocks

class SeagrassItem : ModeledItemBlock(FBlocks.SEAGRASS) {
    override fun placeBlockAt(
        stack: ItemStack,
        player: EntityPlayer,
        world: World,
        pos: BlockPos,
        side: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        newState: IBlockState
    ): Boolean {
        if (world.getBlockState(pos).block != Blocks.WATER) {
            return false
        }
        if (!world.setBlockState(pos, newState, 11)) {
            return false
        }

        val state = world.getBlockState(pos)
        if (state.block == block) {
            setTileEntityNBT(world, player, pos, stack)
            block.onBlockPlacedBy(world, pos, state, player, stack)
            if (player is EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger(player, pos, stack)
        }

        return true
    }
}