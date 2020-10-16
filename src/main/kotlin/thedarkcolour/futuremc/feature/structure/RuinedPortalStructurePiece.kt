package thedarkcolour.futuremc.feature.structure

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Mirror
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece
import net.minecraft.world.gen.feature.template.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.futuremc.registry.FStructures
import java.util.*

class RuinedPortalStructurePiece : TemplateStructurePiece {
    private val placement: VerticalPlacement
    private val properties: Properties
    private val templateLocation: ResourceLocation
    private val pieceRotation: Rotation
    private val mirror: Mirror

    constructor(
        pos: BlockPos,
        placement: VerticalPlacement,
        properties: Properties,
        location: ResourceLocation,
        template: Template,
        rotation: Rotation,
        mirror: Mirror,
        center: BlockPos
    ) : super(FStructures.RUINED_PORTAL_PIECE, 0) {
        this.templatePosition = pos
        this.placement = placement
        this.properties = properties
        this.templateLocation = location
        this.template = template
        this.pieceRotation = rotation
        this.mirror = mirror

        setupPiece(template, center)
    }

    constructor(manager: TemplateManager, nbt: CompoundNBT) : super(FStructures.RUINED_PORTAL_PIECE, nbt) {
        this.placement = VerticalPlacement.byId(nbt.getString("VerticalPlacement"))
        this.properties = Properties(nbt.getCompound("Properties"))
        this.templateLocation = ResourceLocation(nbt.getString("Template"))
        this.pieceRotation = Rotation.valueOf(nbt.getString("Rotation"))
        this.mirror = Mirror.valueOf(nbt.getString("Mirror"))
        val structure = manager.getTemplateDefaulted(templateLocation)

        setupPiece(structure, BlockPos(structure.size.x / 2, 0, structure.size.z / 2))
    }

    private fun setupPiece(template: Template, center: BlockPos) {
        val a = if (properties.airPocket) {
            BlockIgnoreStructureProcessor.STRUCTURE_BLOCK
        } else {
            BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK
        }
        val list = arrayListOf<RuleEntry>()
        list.add(createReplacementRule(Blocks.GOLD_BLOCK, 0.3f, Blocks.AIR))
        list.add(createLavaReplacementRule())
        if (!properties.cold) {
            list.add(createReplacementRule(Blocks.NETHERRACK, 0.07f, Blocks.MAGMA_BLOCK))
        }

        val data = PlacementSettings().setRotation(pieceRotation).setMirror(mirror).setCenterOffset(center).addProcessor(RuleStructureProcessor(list)).addProcessor(AgingProcessor(properties.mossiness))
    }

    private fun createReplacementRule(goldBlock: Block, fl: Float, air: Block): RuleEntry {
        TODO()
    }

    private fun createLavaReplacementRule(): RuleEntry {
        TODO()
    }

    override fun handleDataMarker(
        function: String,
        pos: BlockPos,
        worldIn: IWorld,
        rand: Random,
        sbb: MutableBoundingBox
    ) {
        TODO("not implemented")
    }

    companion object {
        val LOGGER: Logger = LogManager.getLogger()
    }

    enum class VerticalPlacement {
        ON_LAND_SURFACE,
        PARTLY_BURIED,
        ON_OCEAN_FLOOR,
        IN_MOUNTAIN,
        UNDERGROUND,
        IN_NETHER;

        val id = name.toLowerCase(Locale.ROOT)

        companion object {
            private val ID_2_ENUM = values().map { value -> value.id to value }.toMap()

            fun byId(id: String): VerticalPlacement {
                return ID_2_ENUM[id] ?: error("No matching placement found for NBT")
            }
        }
    }

    // todo make serializable
    class Properties(
        val cold: Boolean = false,
        val mossiness: Float = 0.2f,
        val airPocket: Boolean = false,
        val overgrown: Boolean = false,
        val vines: Boolean = false,
        val replaceWithBlackstone: Boolean = false,
    ) {
        constructor(serialized: CompoundNBT) : this(
            cold = serialized.getBoolean("cold"),
            mossiness = serialized.getFloat("mossiness"),
            airPocket = serialized.getBoolean("air_pocket"),
            overgrown = serialized.getBoolean("overgrown"),
            vines = serialized.getBoolean("vines"),
            replaceWithBlackstone = serialized.getBoolean("replace_with_blackstone")
        )
    }
}