package thedarkcolour.futuremc.world.gen.structure

import it.unimi.dsi.fastutil.longs.LongSet
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockPos.MutableBlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.structure.StructureBoundingBox
import net.minecraftforge.common.BiomeManager
import thedarkcolour.futuremc.world.IWorld
import thedarkcolour.futuremc.world.gen.AbstractFeature
import thedarkcolour.futuremc.world.gen.ConfiguredFeature
import thedarkcolour.futuremc.world.gen.chunk.ChunkGenerator
import thedarkcolour.futuremc.world.gen.config.IFeatureConfig
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

typealias IStartFactory = (structure: AbstractStructure<*>, chunkX: Int, chunkZ: Int, bounds: StructureBoundingBox, references: Int, seed: Long) -> StructureStart
typealias StructureStarts = Map<String, StructureStart>
typealias StructureRefs = Map<String, LongSet>

/**
 * Base class for all structures.
 */
abstract class AbstractStructure<FC : IFeatureConfig> : AbstractFeature<FC>() {
    init {
        REGISTRY[structureName] = this
    }

    override fun configure(config: FC): ConfiguredFeature<AbstractStructure<FC>, FC> {
        return ConfiguredFeature(this, config)
    }

    abstract fun shouldStartAt(
        manager: BiomeManager,
        worldIn: World,
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        biome: Biome
    ): Boolean

    abstract fun getStartFactory(): IStartFactory

    abstract val structureName: String

    override fun place(worldIn: IWorld, generator: ChunkGenerator<*>, rand: Random, pos: BlockPos): Boolean {
        if (!worldIn.info.isMapFeaturesEnabled) {
            return false
        } else {
            val i = pos.x shr 4
            val j = pos.z shr 4
            val k = i shl 4
            val l = j shl 4
            var flag = false

            for (long in worldIn.getChunk(i, j).getStructureRefs(structureName)) {
                val chunkPos = IChunkPos(long)
                val start = getStructureStart(worldIn, chunkPos.x, chunkPos.z)

                if (start != null && start != StructureStart.Default) {
                    start.placeStructure(worldIn, rand, StructureBoundingBox(k, l, k + 15, l + 15),
                        IChunkPos.of(i, j)
                    )
                    flag = true
                }
            }

            return flag
        }
    }

    override fun place(
        worldIn: World,
        rand: Random,
        pos: BlockPos,
        structureStarts: StructureStarts?,
        structureRefs: StructureRefs?,
        config: FC
    ): Boolean {
        if (!worldIn.worldInfo.isMapFeaturesEnabled) {
            return false
        } else {
            val i = pos.x shr 4
            val j = pos.z shr 4
            val k = i shl 4
            val l = j shl 4
            var flag = false
            val references = structureRefs?.get(structureName) ?: return false

            for (long in references) {
                val chunkPos = IChunkPos(long)
                val start = getStructureStart(worldIn, chunkPos.x, chunkPos.z)

                if (start != null && start != StructureStart.Default) {
                    start.placeStructure(worldIn, rand, StructureBoundingBox(k, l, k + 15, l + 15),
                        IChunkPos.of(i, j)
                    )
                    flag = true
                }
            }

            return flag
        }
    }

    protected fun getStart(worldIn: World, pos: BlockPos, bool: Boolean): StructureStart {
        begin@ for (start in getStarts(worldIn, pos.x shr 4, pos.z shr 4)) {
            if (start.isValid && start.bounds.isVecInside(pos)) {
                if (!bool) {
                    return start
                }

                val iterator = start.components.iterator()

                while (true) {
                    if (!iterator.hasNext()) {
                        continue@begin
                    }
                    val piece = iterator.next()

                    if (piece.bounds.isVecInside(pos)) {
                        break
                    }
                }

                return start
            }
        }

        return StructureStart.Default
    }

    open fun findNearest(worldIn: World, pos: BlockPos, radius: Int, skipExisting: Boolean): BlockPos? {
        TODO()
    }

    /**
     * Finds the nearest structure in a FMC world.
     */
    open fun findNearest(worldIn: World, generator: ChunkGenerator<*>, pos: BlockPos, radius: Int, skipExisting: Boolean): BlockPos? {
        if (TODO()) {//!generator.biomeProvider.hasStructure(this)) {
            return null
        } else {
            val i = pos.x shr 4
            val j = pos.z shr 4
            val rand = WorldGenRandom()

            for (k in 0 .. radius) {
                for (l in -k .. k) {
                    val flag = l == -k || l == k

                    for (i1 in -k .. k) {
                        val flag1 = i1 == -k || i1 == k

                        if (flag || flag1) {
                            val chunkPos = getStartingPositionForPosition(generator, rand, i, j, l, i1)
                            val start = getStructureStart(worldIn, chunkPos.x, chunkPos.z)

                            if (start != null && start.isValid) {
                                if (skipExisting && start.isRefsBelowMax) {
                                    start.addRefCounter()

                                    return start.pos
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected open fun getStartingPositionForPosition(generator: ChunkGenerator<*>, rand: Random, x: Int, z: Int, spacingX: Int, spacingZ: Int): IChunkPos {
        return IChunkPos.of(x + spacingX, z + spacingZ)
    }

    private fun getStarts(worldIn: World, i: Int, z: Int): Array<StructureStart> {
        TODO("not implemented")
    }

    private fun getStructureStart(worldIn: World, chunkX: Int, chunkZ: Int): StructureStart? {
        TODO()
    }

    private fun getStructureStart(worldIn: IWorld, chunkX: Int, chunkZ: Int): StructureStart? {
        TODO()
    }

    companion object {
        val REGISTRY = HashMap<String, AbstractStructure<*>>()
    }
}

/**
 * Inlined form of ChunkPos.
 * I felt it was time to give inlined classes a try.
 */
inline class IChunkPos(val long: Long) {
    val x get(): Int = long.toInt()
    val z get(): Int = (long shr 32).toInt()

    /**
     * Returns a new `BlockPos` at the
     * starting position of this [IChunkPos].
     */
    fun toBlockPos(): BlockPos {
        return BlockPos(x shl 4, 0, z shl 4)
    }

    companion object {
        /**
         * Returns a new [IChunkPos] with
         * the [x] and [z] coordinates.
         */
        fun of(x: Int, z: Int): IChunkPos {
            return IChunkPos((x.toLong()) or (z.toLong() shl 32))
        }

        fun from(chunk: Chunk): IChunkPos {
            return of(chunk.x, chunk.z)
        }
    }
}

/**
 * Random for use in world generation.
 */
class WorldGenRandom : Random {
    private var usageCount = 0

    constructor() : super()

    constructor(seed: Long) : super(seed)

    fun skip(bits: Int) {
        for (i in 0 until bits) {
            next(1)
        }
    }

    override fun next(bits: Int): Int {
        ++usageCount
        return super.next(bits)
    }

    fun setChunkSeed(x: Int, z: Int): Long {
        val i = x * 341873128712L + z.toLong() * 132897987541L
        setSeed(i)
        return i
    }

    fun setDecorationSeed(baseSeed: Long, x: Int, z: Int): Long {
        setSeed(baseSeed)
        val i = nextLong() or 1L
        val j = nextLong() or 1L
        val k = x.toLong() * i + z.toLong() * j xor baseSeed
        setSeed(k)
        return k
    }

    fun setFeatureSeed(baseSeed: Long, x: Int, z: Int): Long {
        val i = baseSeed + x + (10000 * z)
        setSeed(i)
        return i
    }

    fun setLargeFeatureSeed(baseSeed: Long, x: Int, z: Int): Long {
        setSeed(baseSeed)
        val i = nextLong()
        val j = nextLong()
        val k = x.toLong() * i xor z.toLong() * j xor baseSeed
        setSeed(k)
        return k
    }

    fun setLargeFeatureSeedWithSalt(baseSeed: Long, x: Int, z: Int, salt: Int): Long {
        val i = x * 341873128712L + z * 132897987541L + baseSeed + salt
        setSeed(i)
        return i
    }
}

abstract class StructureStart(
    private val structure: AbstractStructure<*>,
    private val chunkX: Int,
    private val chunkZ: Int,
    var bounds: StructureBoundingBox,
    private var refs: Int,
    seed: Long
) {
    private val rand = WorldGenRandom()
    val components = ArrayList<StructurePiece>()

    init {
        rand.setLargeFeatureSeed(seed, chunkX, chunkZ)
    }

    abstract fun init(worldIn: World, rand: Random, bounds: StructureBoundingBox)

    fun placeStructure(worldIn: World, rand: Random, bounds: StructureBoundingBox, pos: IChunkPos) {
        synchronized(components) {
            val iterator = components.iterator()

            while (iterator.hasNext()) {
                val piece = iterator.next()

                if (piece.bounds.intersectsWith(bounds) && !piece.place(worldIn, rand, bounds, pos)) {
                    iterator.remove()
                }
            }

            recalculateStructureSize()
        }
    }

    fun placeStructure(worldIn: IWorld, rand: Random, bounds: StructureBoundingBox, pos: IChunkPos) {
        synchronized(components) {
            val iterator = components.iterator()

            while (iterator.hasNext()) {
                val piece = iterator.next()

                if (piece.bounds.intersectsWith(bounds) && !piece.place(worldIn, rand, bounds, pos)) {
                    iterator.remove()
                }
            }

            recalculateStructureSize()
        }
    }

    protected open fun recalculateStructureSize() {
        bounds = StructureBoundingBox.getNewBoundingBox()

        for (piece in  components) {
            bounds.expandTo(piece.bounds)
        }
    }

    val isValid get() = components.isNotEmpty()

    open val pos = BlockPos(chunkX shl 4, 0, chunkZ shl 4)

    val isRefsBelowMax get() = refs < maxRefCount

    fun addRefCounter() = ++refs

    val maxRefCount get() = 1

    /**
     * Default structure instance.
     */
    object Default : StructureStart(AbstractFeature.MINESHAFT, 0, 0, StructureBoundingBox.getNewBoundingBox(), 0, 0L) {
        override fun init(worldIn: World, rand: Random, bounds: StructureBoundingBox) {
        }
    }
}

abstract class StructurePiece protected constructor(
    val structurePieceType: IStructurePieceType,
    val componentType: Int
) {
    lateinit var bounds: StructureBoundingBox
    var coordBaseMode: EnumFacing? = null
        private set
    private var mirror = Mirror.NONE
    var rotation = Rotation.NONE
        private set

    constructor(
        structurePieceType: IStructurePieceType,
        nbt: NBTTagCompound
    ) : this(structurePieceType, nbt.getInteger("GD")) {
        if (nbt.hasKey("BB")) {
            bounds = StructureBoundingBox(nbt.getIntArray("BB"))
        }

        val i = nbt.getInteger("O")
        setCoordBaseMode(if (i == -1) null else EnumFacing.byHorizontalIndex(i))
    }

    open fun buildComponent(piece: StructurePiece, list: List<StructurePiece>, rand: Random) {
    }

    abstract fun place(worldIn: World, rand: Random, structureBoundingBox: StructureBoundingBox, chunkPos: IChunkPos): Boolean

    abstract fun place(worldIn: IWorld, rand: Random, structureBoundingBox: StructureBoundingBox, chunkPos: IChunkPos): Boolean

    private fun getXWithOffset(x: Int, z: Int): Int {
        return when (coordBaseMode) {
            EnumFacing.NORTH, EnumFacing.SOUTH -> bounds.minX + x
            EnumFacing.WEST -> bounds.maxX - z
            EnumFacing.EAST -> bounds.minX + z
            else -> x
        }
    }

    private fun getYWithOffset(y: Int): Int {
        return if (coordBaseMode == null) y else bounds.minY + y
    }

    private fun getZWithOffset(x: Int, z: Int): Int {
        return when(coordBaseMode) {
            EnumFacing.NORTH -> bounds.maxZ - z
            EnumFacing.SOUTH -> bounds.minZ + z
            EnumFacing.WEST, EnumFacing.EAST -> bounds.minZ + x
            else -> z
        }
    }

    protected fun setBlockState(
        worldIn: World,
        state: IBlockState,
        x: Int,
        y: Int,
        z: Int,
        boundingBox: StructureBoundingBox
    ) {
        val pos = BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z))
        @Suppress("NAME_SHADOWING")
        var state = state

        if (boundingBox.isVecInside(pos)) {
            if (mirror != Mirror.NONE) {
                state = state.withMirror(mirror)
            }

            if (rotation != Rotation.NONE) {
                state = state.withRotation(rotation)
            }

            worldIn.setBlockState(pos, state, 2)
        }
    }

    private fun getBlockStateFromPos(worldIn: World, x: Int, y: Int, z: Int, boundingBox: StructureBoundingBox): IBlockState {
        val pos = BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z))

        return if (!boundingBox.isVecInside(pos)) {
            Blocks.AIR.defaultState
        } else worldIn.getBlockState(pos)
    }

    private fun getSkyBrightness(worldIn: World, x: Int, y: Int, z: Int, boundingBox: StructureBoundingBox): Boolean {
        val i = getXWithOffset(x, z)
        val j = getYWithOffset(y)
        val k = getZWithOffset(x, z)
        val pos = BlockPos(i, j, k)

        return if (!boundingBox.isVecInside(pos)) {
            false
        } else j < worldIn.getHeight(i, k)
    }

    private fun fillWithAir(
        worldIn: World,
        boundingBox: StructureBoundingBox,
        minX: Int,
        minY: Int,
        minZ: Int,
        maxX: Int,
        maxY: Int,
        maxZ: Int
    ) {
        for (i in minY..maxY) {
            for (j in minX..maxX) {
                for (k in minZ..maxZ) {
                    setBlockState(worldIn, Blocks.AIR.defaultState, j, i, k, boundingBox)
                }
            }
        }
    }

    private fun setCoordBaseMode(facing: EnumFacing?) {
        coordBaseMode = facing

        when (facing) {
            EnumFacing.SOUTH -> {
                mirror = Mirror.NONE
                rotation = Rotation.NONE
            }
            EnumFacing.WEST -> {
                mirror = Mirror.NONE
                rotation = Rotation.NONE
            }
            EnumFacing.EAST -> {
                mirror = Mirror.NONE
                rotation = Rotation.NONE
            }
            else -> {
                mirror = Mirror.NONE
                rotation = Rotation.NONE
            }
        }
    }

    companion object {
        fun findFittingPiece(pieces: List<StructurePiece>, structureBoundingBox: StructureBoundingBox): StructurePiece? {
            for (piece in pieces) {
                if (piece::bounds.isInitialized && piece.bounds.intersectsWith(structureBoundingBox)) {
                    return piece
                }
            }

            return null
        }

        @Suppress("DuplicatedCode")
        fun isLiquidInStructureBoundingBox(worldIn: World, structureBoundingBox: StructureBoundingBox): Boolean {
            val i  = max(structureBoundingBox.minX - 1, structureBoundingBox.minX)
            val j  = max(structureBoundingBox.minY - 1, structureBoundingBox.minY)
            val k  = max(structureBoundingBox.minZ - 1, structureBoundingBox.minZ)
            val l  = min(structureBoundingBox.maxX + 1, structureBoundingBox.maxX)
            val i1 = min(structureBoundingBox.maxY + 1, structureBoundingBox.maxY)
            val j1 = min(structureBoundingBox.maxZ + 1, structureBoundingBox.maxZ)
            val mutable = MutableBlockPos()

            for (k1 in i..l) {
                for (l1 in k..j1) {
                    if (worldIn.getBlockState(mutable.setPos(k1, j, l1)).material.isLiquid) {
                        return true
                    }

                    if (worldIn.getBlockState(mutable.setPos(k1, i1, l1)).material.isLiquid) {
                        return true
                    }
                }
            }

            for (i2 in i..l) {
                for (k2 in j..i1) {
                    if (worldIn.getBlockState(mutable.setPos(i2, j, k)).material.isLiquid) {
                        return true
                    }

                    if (worldIn.getBlockState(mutable.setPos(i2, i1, j1)).material.isLiquid) {
                        return true
                    }
                }
            }

            for (j2 in k..j1) {
                for (l2 in j..i1) {
                    if (worldIn.getBlockState(mutable.setPos(i, l2, j2)).material.isLiquid) {
                        return true
                    }

                    if (worldIn.getBlockState(mutable.setPos(l, l2, j2)).material.isLiquid) {
                        return true
                    }
                }
            }

            return false
        }
    }
}

interface IStructurePieceType