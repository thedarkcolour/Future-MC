package thedarkcolour.futuremc.client.tesr.campfire

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.EnumFacing
import thedarkcolour.futuremc.block.BlockCampfire.Companion.FACING
import thedarkcolour.futuremc.tile.TileCampfire

class TESRCampfire : TileEntitySpecialRenderer<TileCampfire>() {
    override fun render(te: TileCampfire, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        val facing = try {
            te.world.getBlockState(te.pos).getValue(FACING)
        } catch (ignored: IllegalArgumentException) {
            EnumFacing.NORTH
        }
        for (i in 0..3) {
            val stack = te.inventory.getStackInSlot(i)
            if (!stack.isEmpty) {
                GlStateManager.pushMatrix()
                GlStateManager.translate(x + 0.5f, y + 0.44921875f, z + 0.5f)
                val direction1 = EnumFacing.byHorizontalIndex((i + facing.horizontalIndex) % 4)
                GlStateManager.rotate(-direction1.horizontalAngle, 0.0f, 1.0f, 0.0f)
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f)
                GlStateManager.translate(-0.3125f, -0.3125f, 0.0f)
                GlStateManager.scale(0.375f, 0.375f, 0.375f)
                Minecraft.getMinecraft().renderItem.renderItem(stack, ItemCameraTransforms.TransformType.FIXED)
                GlStateManager.popMatrix()
            }
        }
    }
}