package thedarkcolour.futuremc.data

import net.minecraft.data.DataGenerator
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.generators.BlockModelBuilder
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ExistingFileHelper
import thedarkcolour.futuremc.FutureMC

class BlockModelGenerator(gen: DataGenerator?, helper: ExistingFileHelper?) : BlockStateProvider(gen, FutureMC.ID, helper) {
    private val generatedModels = hashMapOf<ResourceLocation, BlockModelBuilder>()
    private val factory: (ResourceLocation) -> BlockModelBuilder = { output ->
        BlockModelBuilder(output, helper)
    }

    override fun registerStatesAndModels() {

    }
}