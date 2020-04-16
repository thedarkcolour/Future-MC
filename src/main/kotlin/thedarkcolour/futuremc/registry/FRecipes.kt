package thedarkcolour.futuremc.registry

import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.recipe.SmithingRecipe

@Suppress("HasPlatformType")
object FRecipes {
    val SMITHING_SERIALIZER = SmithingRecipe.Serializer().setRegistryKey("smithing")

    val SMITHING = IRecipeType.register<SmithingRecipe>("futuremc:smithing")

    fun registerRecipeSerializers(serializers: IForgeRegistry<IRecipeSerializer<*>>) {
        serializers.register(SMITHING_SERIALIZER)
    }
}