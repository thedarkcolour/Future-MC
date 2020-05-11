package thedarkcolour.futuremc.block

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.*
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.registry.FItems

class BlockBambooSapling(properties: Properties) : FBlock(properties) {

    override fun getPickBlock(
        state: IBlockState,
        target: RayTraceResult,
        world: World,
        pos: BlockPos,
        player: EntityPlayer
    ): ItemStack {
        return FItems.BAMBOO.stack
    }

    override fun getOffset(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): Vec3d {
        val i = MathHelper.getCoordinateRandom(pos.x, 0, pos.z)
        return Vec3d(((i shr 16 and 15L) / 15.0f - 0.5) * 0.5, 0.0, ((i shr 24 and 15L) / 15.0f - 0.5) * 0.5)
    }

    //override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
    //    super.updateTick(worldIn, pos, state, rand)
    //}

    override fun getCollisionBoundingBox(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        val vec3d = state.getOffset(worldIn, pos)
        return AABB.offset(vec3d)
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return getCollisionBoundingBox(state, source, pos)
    }

    companion object {
        private val AABB = makeCube(4.0, 0.0, 4.0, 12.0, 12.0, 12.0)
    }
}