package thedarkcolour.futuremc.client.tesr.campfire;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.futuremc.block.BlockCampfire;
import thedarkcolour.futuremc.tile.TileCampfire;

@SideOnly(Side.CLIENT)
public class TESRCampfire extends TileEntitySpecialRenderer<TileCampfire> {
    @Override
    public void render(TileCampfire te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockCampfire.FACING);

        for(int i = 0; i < 4; ++i) {
            ItemStack stack = te.getBuffer().getStackInSlot(i);
            if (!stack.isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 0.5F, y + 0.44921875F, z + 0.5F);
                EnumFacing direction1 = EnumFacing.byHorizontalIndex((i + facing.getHorizontalIndex()) % 4);
                GlStateManager.rotate(-direction1.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(-0.3125F, -0.3125F, 0.0F);
                GlStateManager.scale(0.375F, 0.375F, 0.375F);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }
        }
    }
}