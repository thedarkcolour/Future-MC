package thedarkcolour.futuremc.compat.jei

import mezz.jei.api.IGuiHelper
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC

abstract class FRecipeCategory<T : IRecipeWrapper>(
    helper: IGuiHelper,
    texture: ResourceLocation,
    private val titleKey: String,
    u: Int, v: Int,
    width: Int, height: Int
) : IRecipeCategory<T> {
    private val background = helper.drawableBuilder(texture, u, v, width, height).build()

    override fun getUid() = titleKey

    override fun getTitle() = I18n.format(titleKey)

    override fun getModName() = FutureMC.ID

    override fun getBackground() = background
}