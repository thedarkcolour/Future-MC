package thedarkcolour.futuremc.world.gen.chunk

import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks

open class ChunkGenConfig {
    protected var villageDistance = 32
    protected var villageSeparation = 8
    protected var monumentSpacing = 32
    protected var monumentSeparation = 5
    protected var strongholdDistance = 32
    protected var strongholdCount = 128
    protected var strongholdSpread = 3
    protected var endCityDistance = 20
    protected var mansionDistance = 80
    protected var defaultBlock: IBlockState = Blocks.STONE.defaultState
    protected var defaultFluid: IBlockState = Blocks.WATER.defaultState
}