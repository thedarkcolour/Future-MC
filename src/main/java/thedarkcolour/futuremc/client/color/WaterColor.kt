package thedarkcolour.futuremc.client.color

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.biome.Biome
import net.minecraftforge.registries.IRegistryDelegate

object WaterColor : IBlockColor {
    val BIOME_COLORS = Object2IntOpenHashMap<IRegistryDelegate<Biome>>()

    override fun colorMultiplier(state: IBlockState?, worldIn: IBlockAccess?, pos: BlockPos?, tintIndex: Int): Int {
        return if (worldIn != null && pos != null) {
            BIOME_COLORS[worldIn.getBiome(pos).delegate]!!
        } else {
            0x3f76e4
        }
    }
}