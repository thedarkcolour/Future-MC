package thedarkcolour.futuremc.block

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.tile.TileSeagrassRenderer

open class BlockWaterPlant(name: String) : BlockStaticLiquid(Material.WATER) {
    private val flowing = Flowing(name)

    init {
        setRegistryName("${FutureMC.ID}:$name")
        translationKey = "${FutureMC.ID}.$name"
        soundType = SoundType.PLANT
        defaultState = defaultState.withProperty(LEVEL, 0)
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        worldIn.setBlockState(
            pos,
            flowing.defaultState.withProperty(BlockLiquid.LEVEL, state.getValue(BlockLiquid.LEVEL)),
            2
        )
        worldIn.scheduleUpdate(pos, flowing, tickRate(worldIn))
    }

    override fun removedByPlayer(
        state: IBlockState,
        world: World,
        pos: BlockPos,
        player: EntityPlayer,
        willHarvest: Boolean
    ): Boolean {
        return world.setBlockState(
            pos,
            Blocks.WATER.defaultState.withProperty(BlockLiquid.LEVEL, state.getValue(BlockLiquid.LEVEL)),
            if (world.isRemote) 11 else 3
        )
    }

    // waterlogged blocks are not destroyed by lava
    override fun checkForMixing(worldIn: World, pos: BlockPos, state: IBlockState) = true

    override fun hasTileEntity(state: IBlockState) = true
    override fun createTileEntity(world: World, state: IBlockState) = TileSeagrassRenderer()
    override fun canCollideCheck(state: IBlockState, hitIfLiquid: Boolean) = true

    inner class Flowing(name: String) : BlockDynamicLiquid(Material.WATER) {
        init {
            setRegistryName("${FutureMC.ID}:${name}_flowing")
            translationKey = "${FutureMC.ID}.${name}_flowing"
            soundType = SoundType.PLANT
        }

        override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
            return this@BlockWaterPlant.getBoundingBox(state, source, pos)
        }

        override fun hasTileEntity(state: IBlockState) = true
        override fun createTileEntity(world: World, state: IBlockState) = TileSeagrassRenderer()
    }
}