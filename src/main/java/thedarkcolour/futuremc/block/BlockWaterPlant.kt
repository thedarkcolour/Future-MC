package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockDynamicLiquid
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.tile.TileWaterlogged

@Suppress("HasPlatformType")
abstract class BlockWaterPlant(name: String) : BlockDynamicLiquid(Material.WATER), AlternateBlock {
    init {
        setRegistryName("${FutureMC.ID}:$name")
        translationKey = "${FutureMC.ID}.$name"
    }

    override fun getSoundType(state: IBlockState, te: TileWaterlogged) = SoundType.PLANT

    override fun material(state: IBlockState, te: TileWaterlogged) = Material.PLANTS

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {

    }

    // waterlogged blocks are not destroyed by lava
    override fun checkForMixing(worldIn: World, pos: BlockPos, state: IBlockState) = false

    override fun hasTileEntity(state: IBlockState) = true

    override fun createTileEntity(world: World, state: IBlockState): TileEntity {
        return TileWaterlogged(FluidRegistry.WATER, this)
    }

    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.LIQUID

    open class AlternateState(material: Material) : BlockStateContainer.StateImplementation(null) {

    }
}