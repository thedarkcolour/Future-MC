package thedarkcolour.futuremc.block

import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.futuremc.client.gui.GuiType
import thedarkcolour.futuremc.tile.TileBarrel

class BarrelBlock(properties: Properties) : FBlock(properties) {
    init {
        defaultState = defaultState.withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false)
    }

    override fun onBlockActivated(
        worldIn: World, pos: BlockPos, state: IBlockState, player: EntityPlayer,
        hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float
    ): Boolean {
        if (worldIn.getTileEntity(pos) !is TileBarrel) return false
        GuiType.BARREL.open(player, worldIn, pos)
        return true
    }

    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        val tile = world.getTileEntity(pos)
        if (tile is TileBarrel) {
            for (stack in tile.inventory) {
                if (!stack.isEmpty) {
                    spawnAsEntity(world, pos, stack)
                }
            }
            world.removeTileEntity(pos)
        }
    }



    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING, OPEN)
    }

    override fun getStateForPlacement(
        worldIn: World,
        pos: BlockPos,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        meta: Int,
        placer: EntityLivingBase
    ): IBlockState {
        return defaultState.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer))
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState
            .withProperty(FACING, EnumFacing.byIndex(if (meta > 5) meta - 6 else meta))
            .withProperty(OPEN, meta > 5)
    }

    override fun getMetaFromState(state: IBlockState): Int =
        state.getValue(FACING).index

    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = true

    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = 5

    override fun hasTileEntity(state: IBlockState) = true
    override fun createTileEntity(world: World, state: IBlockState) = TileBarrel()

    companion object {
        private val FACING = PropertyDirection.create("facing")
        private val OPEN = PropertyBool.create("open")
    }
}