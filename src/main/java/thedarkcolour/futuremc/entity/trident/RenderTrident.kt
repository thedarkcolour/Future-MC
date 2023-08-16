package thedarkcolour.futuremc.entity.trident

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.math.MathHelper

class RenderTrident(renderManager: RenderManager) : Render<Trident>(renderManager) {
    private val modelTrident = ModelTrident()

    override fun doRender(
        entity: Trident,
        x: Double,
        y: Double,
        z: Double,
        entityYaw: Float,
        partialTicks: Float
    ) {
        this.bindEntityTexture(entity)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.pushMatrix()
        GlStateManager.disableLighting()
        GlStateManager.translate(x.toFloat(), y.toFloat(), z.toFloat())
        GlStateManager.rotate(
            entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f,
            0.0f,
            1.0f,
            0.0f
        )
        GlStateManager.rotate(
            entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 90.0f,
            0.0f,
            0.0f,
            1.0f
        )
        modelTrident.renderAsBuiltin()
        GlStateManager.popMatrix()

        super.doRender(entity, x, y, z, entityYaw, partialTicks)
        GlStateManager.enableLighting()

        val er = Minecraft.getMinecraft().entityRenderer
        er.disableLightmap()
        GlStateManager.disableLighting()

        bindEntityTexture(entity)
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toFloat(), y.toFloat(), z.toFloat())
        GlStateManager.rotate(
            entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f,
            0.0f,
            1.0f,
            0.0f
        )
        GlStateManager.rotate(
            entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks,
            0.0f,
            0.0f,
            1.0f
        )

        val f10 = 0.05625f
        GlStateManager.enableRescaleNormal()
        val f11 = entity.arrowShake.toFloat() - partialTicks

        if (f11 > 0.0f) {
            val f12 = -MathHelper.sin(f11 * 3.0f) * f11
            GlStateManager.rotate(f12, 0.0f, 0.0f, 1.0f)
        }

        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f)
        GlStateManager.scale(f10, f10, f10)
        GlStateManager.translate(-4.0f, 0.0f, 0.0f)

        GlStateManager.disableRescaleNormal()
        GlStateManager.popMatrix()

        GlStateManager.enableLighting()
        er.enableLightmap()
    }

    override fun getEntityTexture(entity: Trident) = ModelTrident.TEXTURE_LOCATION
}