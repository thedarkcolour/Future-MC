package thedarkcolour.futuremc.registry

import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.MutableRegistry
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.NetherFossilStructure
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.IStructurePieceType
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.feature.structure.NetherFossilPieces

object FStructures {
    val NETHER_FOSSIL = NetherFossilStructure { NoFeatureConfig.deserialize(it) }.setRegistryKey("nether_fossil")

    val NETHER_FOSSIL_PIECE = IStructurePieceType(NetherFossilPieces::Piece)

    fun registerStructures(structures: IForgeRegistry<Feature<*>>) {
        structures.register(NETHER_FOSSIL)

        registerStructurePiece(NETHER_FOSSIL_PIECE, ResourceLocation(FutureMC.ID, "nether_fossil_piece"))
    }

    private fun registerStructurePiece(structurePieceType: IStructurePieceType, registryName: ResourceLocation) {
        (Registry.STRUCTURE_PIECE as MutableRegistry).register(registryName, structurePieceType)
    }
}