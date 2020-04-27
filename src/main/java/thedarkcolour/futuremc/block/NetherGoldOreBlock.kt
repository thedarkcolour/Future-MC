package thedarkcolour.futuremc.block

import net.minecraft.block.OreBlock
import net.minecraft.util.math.MathHelper
import java.util.*

class NetherGoldOreBlock(properties: Properties) : OreBlock(properties) {
    override fun getExperience(rand: Random): Int {
        return MathHelper.nextInt(rand, 0, 1)
    }
}