package thedarkcolour.futuremc.world.gen.chunk

import it.unimi.dsi.fastutil.longs.LongSet
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.NextTickListEntry
import thedarkcolour.futuremc.world.gen.structure.IChunkPos
import thedarkcolour.futuremc.world.gen.structure.StructureRefs
import thedarkcolour.futuremc.world.gen.structure.StructureStart
import thedarkcolour.futuremc.world.gen.structure.StructureStarts
import java.util.*

class ChunkGenPreview(override val pos: IChunkPos): IChunk {
    override var biomeMap: BiomeMap? = null

    override fun setBlockState(pos: BlockPos, state: IBlockState, isMoving: Boolean): IBlockState? {
        TODO("not implemented")
    }

    override fun addTileEntity(pos: BlockPos, te: TileEntity) {
        TODO("not implemented")
    }

    override fun spawnEntity(entity: Entity) {
        TODO("not implemented")
    }

    override fun getTileEntityPositions(): List<BlockPos> {
        TODO("not implemented")
    }

    override fun getBlockData(pos: BlockPos): NBTTagCompound? {
        TODO("not implemented")
    }

    override var structureStarts: StructureStarts
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun putStructureStart(structure: String, start: StructureStart) {
        TODO("not implemented")
    }

    override var structureStartRefs: StructureRefs
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun getStructureRefs(structure: String): LongSet {
        TODO("not implemented")
    }

    override fun removeTileEntity(pos: BlockPos) {
        TODO("not implemented")
    }

    override fun getCarvingMask(): BitSet {
        TODO("not implemented")
    }

    override val scheduledBlockUpdates: Set<NextTickListEntry>
        get() = TODO("Not yet implemented")
}