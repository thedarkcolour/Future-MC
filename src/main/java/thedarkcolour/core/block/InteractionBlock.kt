package thedarkcolour.core.block

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.tile.InteractionTile

abstract class InteractionBlock(properties: Properties) : FBlock(properties) {
    override fun hasTileEntity(state: IBlockState?): Boolean {
        return true
    }

    abstract override fun createTileEntity(worldIn: World?, state: IBlockState?): TileEntity

    override fun onBlockActivated(
        worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer,
        hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float
    ): Boolean {
        val tile = worldIn.getTileEntity(pos)

        return if (tile is InteractionTile) {
            tile.activated(state, playerIn, hand, facing, hitX, hitY, hitZ)
        } else false
    }

    override fun onBlockHarvested(worldIn: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) {
        val tile = worldIn.getTileEntity(pos)

        if (tile is InteractionTile) {
            tile.broken(state, player)
        }
    }

    override fun eventReceived(state: IBlockState, worldIn: World, pos: BlockPos, id: Int, param: Int): Boolean {
        val te = worldIn.getTileEntity(pos)
        return te != null && te.receiveClientEvent(id, param)
    }
}