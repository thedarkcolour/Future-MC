package thedarkcolour.futuremc.client.tesr;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.block.villagepillage.FSignBlock;
import thedarkcolour.futuremc.tile.WoodenSignTile;

import java.util.List;
import java.util.Map;

public class WoodenSignRenderer extends TileEntitySpecialRenderer<WoodenSignTile> {
    private static final Map<String, ResourceLocation> SIGN_TEXTURES = ImmutableMap.of(
            "acacia", new ResourceLocation(FutureMC.ID, "textures/sign/acacia.png"),
            "birch", new ResourceLocation(FutureMC.ID, "textures/sign/birch.png"),
            "dark_oak", new ResourceLocation(FutureMC.ID, "textures/sign/dark_oak.png"),
            "jungle", new ResourceLocation(FutureMC.ID, "textures/sign/jungle.png"),
            "spruce", new ResourceLocation(FutureMC.ID, "textures/sign/spruce.png")
    );
    private final ModelSign model = new ModelSign();

    @Override
    public void render(WoodenSignTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Block block = te.getBlockType();
        GlStateManager.pushMatrix();

        if (block.getClass() == FSignBlock.Standing.class) {
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            float f1 = (float)(te.getBlockMetadata() * 360) / 16.0F;
            GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
            this.model.signStick.showModel = true;
        } else {
            int k = te.getBlockMetadata();
            float f2 = 0.0F;

            if (k == 2) {
                f2 = 180.0F;
            }

            if (k == 4) {
                f2 = 90.0F;
            }

            if (k == 5) {
                f2 = -90.0F;
            }

            GlStateManager.translate((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
            GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
            this.model.signStick.showModel = false;
        }

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(SIGN_TEXTURES.get(((FSignBlock.FSignType) block).getType()));
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.6666667F, -0.6666667F, -0.6666667F);
        this.model.renderSign();
        GlStateManager.popMatrix();
        FontRenderer fontrenderer = this.getFontRenderer();
        GlStateManager.translate(0.0F, 0.33333334F, 0.046666667F);
        GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
        GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
        GlStateManager.depthMask(false);

        if (destroyStage < 0) {
            for (int j = 0; j < te.signText.length; ++j) {
                if (te.signText[j] != null) {
                    ITextComponent itextcomponent = te.signText[j];
                    List<ITextComponent> list = GuiUtilRenderComponents.splitText(itextcomponent, 90, fontrenderer, false, true);
                    String s = list != null && !list.isEmpty() ? ((ITextComponent)list.get(0)).getFormattedText() : "";

                    if (j == te.lineBeingEdited) {
                        s = "> " + s + " <";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, 0);
                    } else {
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, 0);
                    }
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
