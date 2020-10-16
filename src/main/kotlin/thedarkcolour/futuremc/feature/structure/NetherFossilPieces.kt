package thedarkcolour.futuremc.feature.structure

import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Mirror
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Rotation
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.structure.StructurePiece
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor
import net.minecraft.world.gen.feature.template.PlacementSettings
import net.minecraft.world.gen.feature.template.TemplateManager
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.registry.FStructures
import java.util.*

object NetherFossilPieces {
    private val FOSSILS = arrayOf(
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_1"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_2"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_3"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_4"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_5"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_6"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_7"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_8"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_9"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_10"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_11"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_12"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_13"),
        ResourceLocation(FutureMC.ID, "nether_fossils/fossil_14")
    )

    fun start(manager: TemplateManager, components: MutableList<StructurePiece>, rand: SharedSeedRandom, pos: BlockPos) {
        val rot = Rotation.randomRotation(rand)
        components.add(Piece(manager, FOSSILS[rand.nextInt(FOSSILS.size)], pos, rot))
    }

    class Piece : TemplateStructurePiece {
        private val templateLocation: ResourceLocation
        private val pieceRotation: Rotation

        constructor(manager: TemplateManager, location: ResourceLocation, pos: BlockPos, rotation: Rotation) : super(FStructures.NETHER_FOSSIL_PIECE, 0) {
            this.templateLocation = location
            this.templatePosition = pos.up()
            this.pieceRotation = rotation
            setupPiece(manager)
        }

        constructor(manager: TemplateManager, compound: CompoundNBT) : super(FStructures.NETHER_FOSSIL_PIECE, compound) {
            this.templateLocation = ResourceLocation(compound.getString("Template"))
            this.pieceRotation = Rotation.valueOf(compound.getString("Rot"))
            setupPiece(manager)
        }

        override fun readAdditional(tagCompound: CompoundNBT) {
            super.readAdditional(tagCompound)
            tagCompound.putString("Template", templateLocation.toString())
            tagCompound.putString("Rot", pieceRotation.name)
        }

        private fun setupPiece(manager: TemplateManager) {
            val template = manager.getTemplateDefaulted(templateLocation)
            val settings = PlacementSettings().setRotation(pieceRotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK)
            setup(template, templatePosition, settings)
        }

        override fun handleDataMarker(p0: String, p1: BlockPos, p2: IWorld, p3: Random, p4: MutableBoundingBox) = Unit
    }
}