package thedarkcolour.futuremc.entity.trident

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer

class ModelTrident : ModelBase() {
    private val modelRenderer = ModelRenderer(this, 0, 0)

    init {
        textureWidth = 32
        modelRenderer.addBox(-0.5f, -4.0f, -0.5f, 1, 31, 1, 0.0f)

        val model = ModelRenderer(this, 4, 0)
        model.addBox(-1.5f, 0.0f, -0.5f, 3, 2, 1)
        modelRenderer.addChild(model)

        val model1 = ModelRenderer(this, 4, 3)
        model1.addBox(-2.5f, -3.0f, -0.5f, 1, 4, 1)
        modelRenderer.addChild(model1)

        val model2 = ModelRenderer(this, 4, 3)
        model2.mirror = true
        model2.addBox(1.5f, -3.0f, -0.5f, 1, 4, 1)
        modelRenderer.addChild(model2)
    }

    fun renderer() {
        modelRenderer.render(0.0625f)
    }
}