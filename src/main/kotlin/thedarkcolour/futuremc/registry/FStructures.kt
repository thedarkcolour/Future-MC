package thedarkcolour.futuremc.registry

import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.MutableRegistry
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.NetherFossilStructure
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.IStructurePieceType
import net.minecraft.world.gen.feature.template.IStructureProcessorType
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.feature.structure.AgingProcessor
import thedarkcolour.futuremc.feature.structure.NetherFossilPieces
import thedarkcolour.futuremc.feature.structure.RuinedPortalStructurePiece

object FStructures {
    val NETHER_FOSSIL = NetherFossilStructure { NoFeatureConfig.deserialize(it) }.setRegistryKey("nether_fossil")

    val NETHER_FOSSIL_PIECE = IStructurePieceType(NetherFossilPieces::Piece)
    val RUINED_PORTAL_PIECE = IStructurePieceType(::RuinedPortalStructurePiece)

    val AGING_PROCESSOR = IStructureProcessorType(::AgingProcessor)

    fun registerStructures(structures: IForgeRegistry<Feature<*>>) {
        structures.register(NETHER_FOSSIL)

        registerStructurePiece(NETHER_FOSSIL_PIECE, "nether_fossil_piece")
        registerStructurePiece(RUINED_PORTAL_PIECE, "ruined_portal_piece")

        registerStructureProcessor(AGING_PROCESSOR, "block_age")
    }

    private fun registerStructurePiece(structurePieceType: IStructurePieceType, registryName: String) {
        (Registry.STRUCTURE_PIECE as MutableRegistry)
            .register(ResourceLocation(FutureMC.ID, registryName), structurePieceType)
    }

    private fun registerStructureProcessor(structureProcessorType: IStructureProcessorType, registryName: String) {
        (Registry.STRUCTURE_PROCESSOR as MutableRegistry)
            .register(ResourceLocation(FutureMC.ID, registryName), structureProcessorType)
    }
}