package thedarkcolour.futuremc.client.tesr

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer

class BellModel : ModelBase() {
    private val bell: ModelRenderer
    private val fixture: ModelRenderer

    init {
        textureWidth = 32
        bell = ModelRenderer(this, 0, 0)
        bell.addBox(-3.0f, -6.0f, -3.0f, 6, 7, 6)
        bell.setRotationPoint(8.0f, 12.0f, 8.0f)
        fixture = ModelRenderer(this, 0, 13)
        fixture.addBox(4.0f, 4.0f, 4.0f, 8, 2, 8)
        fixture.setRotationPoint(-8.0f, -12.0f, -8.0f)
        bell.addChild(fixture)
    }

    fun renderBell(rotateAngleX: Float, rotateAngleZ: Float) {
        bell.rotateAngleX = rotateAngleX
        bell.rotateAngleZ = rotateAngleZ
        bell.render(0.0625f)
    }
}