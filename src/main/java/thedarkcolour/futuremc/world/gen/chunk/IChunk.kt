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

/**
 * Base functionality for classes that
 * represent 16x256x16 sections in the world.
 */
interface IChunk {

    /**
     * Sets the `IBlockState` [state] at the position in this chunk
     * and returns the previous `IBlockState` or `null` if the
     * previous `IBlockState` is the same as [state].
     */
    fun setBlockState(pos: BlockPos, state: IBlockState, isMoving: Boolean): IBlockState?

    /**
     * Adds `TileEntity` [te] to this chunk at the specified [pos].
     */
    fun addTileEntity(pos: BlockPos, te: TileEntity)

    /**
     * Spawns an entity into this [IChunk].
     */
    fun spawnEntity(entity: Entity)

    /**
     * Returns an immutable list containing each position
     * in this [IChunk] that has a `TileEntity`.
     */
    fun getTileEntityPositions(): List<BlockPos>

    /**
     * Reads the block data at the position and returns it
     * or returns null if an error occurred saving block data.
     */
    fun getBlockData(pos: BlockPos): NBTTagCompound?

    /**
     * The (inlined to `long`) position of this [IChunk].
     */
    val pos: IChunkPos

    /**
     * The immutable map of structure starts in this [IChunk].
     */
    var structureStarts: StructureStarts

    /**
     * Add a structure start for the given [structure].
     */
    fun putStructureStart(structure: String, start: StructureStart)

    /**
     * The map of structure names to their
     * predetermined chunk positions in the world.
     */
    var structureStartRefs: StructureRefs

    /**
     * Returns the `LongSet` representing all
     * positions of the [structure] in the world.
     */
    fun getStructureRefs(structure: String): LongSet

    /**
     * The [BiomeMap] containing the biomes at
     * certain positions in this [IChunk].
     */
    val biomeMap: BiomeMap?

    /**
     * Removes the `TileEntity` from this chunk
     * if it exists at the specified [pos].
     */
    fun removeTileEntity(pos: BlockPos)

    /**
     * Returns the carving mask if supported by
     * this implementation of [IChunk]
     *
     * @throws UnsupportedOperationException
     *         if not supported by the current implementation
     */
    fun getCarvingMask(): BitSet

    /**
     * Returns an immutable set of the
     * scheduled block updates in this [IChunk].
     */
    val scheduledBlockUpdates: Set<NextTickListEntry>
}