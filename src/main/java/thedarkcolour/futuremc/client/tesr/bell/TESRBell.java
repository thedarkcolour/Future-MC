package thedarkcolour.futuremc.client.tesr.bell;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.futuremc.tile.TileBell;

@SideOnly(Side.CLIENT)
public class TESRBell extends TileEntitySpecialRenderer<TileBell> {
    private static ResourceLocation textures = new ResourceLocation("futuremc:textures/entity/bell/bell_body.png");
    private final ModelBell model = new ModelBell();

    @Override
    public void render(TileBell te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        this.bindTexture(textures);
        GlStateManager.translate((float)x, (float)y, (float)z);
        float f = te.ringTime + partialTicks;
        float f1 = 0.0F;
        float f2 = 0.0F;
        if (te.isRinging) {
            float f3 = MathHelper.sin(f / (float)Math.PI) / (4.0F + f / 3.0F);
            if (te.facing == EnumFacing.NORTH) {
                f1 = -f3;
            } else if (te.facing == EnumFacing.SOUTH) {
                f1 = f3;
            } else if (te.facing == EnumFacing.EAST) {
                f2 = -f3;
            } else if (te.facing == EnumFacing.WEST) {
                f2 = f3;
            }
        }

        this.model.renderBell(f1, f2, 0.0625F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}