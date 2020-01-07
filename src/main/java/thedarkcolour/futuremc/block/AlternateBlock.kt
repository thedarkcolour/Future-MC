package thedarkcolour.futuremc.block

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import thedarkcolour.futuremc.tile.TileWaterlogged

interface AlternateBlock {
    fun getSoundType(state: IBlockState, te: TileWaterlogged): SoundType
    fun material(state: IBlockState, te: TileWaterlogged): Material
    fun getAlternateState(te: TileWaterlogged): IBlockState
}