package thedarkcolour.futuremc.block.netherupdate

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import java.util.*

class NetherGoldOreBlock(properties: Properties) : FBlock(properties) {
    /**
     * Gathers how much experience this block drops when broken.
     *
     * @param state The current state
     * @param world The world
     * @param pos Block position
     * @param fortune
     * @return Amount of XP from breaking this block.
     */
    override fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int): Int {

        val rand = if (world is World) world.rand else Random()

        return rand.nextInt(2) + 1
    }


}