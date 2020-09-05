package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.futuremc.client.gui.GuiType
import thedarkcolour.futuremc.tile.TileBarrel
import java.util.*

class BarrelBlock(properties: Properties) : FBlock(properties), ITileEntityProvider {
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

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        val te = worldIn.getTileEntity(pos)

        if (te is TileBarrel) {
            te.updateOpenState()
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

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        return TileBarrel()
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)))
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)))
    }

    companion object {
        @JvmField val FACING: PropertyDirection = PropertyDirection.create("facing")
        @JvmField val OPEN: PropertyBool = PropertyBool.create("open")
    }
}