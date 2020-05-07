package thedarkcolour.futuremc.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import thedarkcolour.core.gui.GuiContainer;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.compat.Compat;
import thedarkcolour.futuremc.config.FConfig;
import thedarkcolour.futuremc.container.ContainerStonecutter;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipe;

import java.io.IOException;

public class GuiStonecutter extends GuiContainer<ContainerStonecutter> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(FutureMC.ID, "textures/gui/stonecutter.png");
    private float sliderProgress;
    private boolean clickedOnScroll;
    private int recipeIndexOffset = 0;
    private boolean hasItemsInInputSlot;

    public GuiStonecutter(ContainerStonecutter container) {
        super(container);
        container.setInventoryUpdateListener(this::onInventoryUpdate);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(I18n.format("container.stonecutter"), 8, 6, 4210752);
        fontRenderer.drawString(getContainer().getPlayerInv().getDisplayName().getUnformattedText(), 8, ySize - 94, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(BACKGROUND);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        int scrollY = (int) (41.0F * sliderProgress);
        drawTexturedModalRect(i + 119, j + 15 + scrollY, 176 + (canScroll() ? 0 : 12), 0, 12, 15);

        if (Compat.isModLoaded(Compat.JEI)) {
            drawRecipeButton(mouseX, mouseY, i, j);
        }
        if (getContainer().getCurrentRecipe() != null) {
            int x = guiLeft + 52;
            int y = guiTop + 14;
            int k = recipeIndexOffset + 12;
            drawRecipesBackground(mouseX, mouseY, x, y, k);
            drawRecipesItems(x, y, k);
        }
    }

    private void drawRecipesBackground(int mouseX, int mouseY, int left, int top, int recipeIndexOffsetMax) {
        for (int i = recipeIndexOffset; i < recipeIndexOffsetMax && i < getContainer().getCurrentRecipe().getTotalOutputs(); ++i) {
            int j = i - recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            int j1 = ySize;
            if (i == getContainer().getSelectedIndex()) {
                j1 += 18;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                j1 += 36;
            }

            drawTexturedModalRect(k, i1 - 1, 0, j1, 16, 18);
        }
    }

    private void drawRecipesItems(int left, int top, int recipeIndexOffsetMax) {
        RenderHelper.enableGUIStandardItemLighting();
        StonecutterRecipe recipe = getContainer().getCurrentRecipe();

        for (int i = recipeIndexOffset; i < recipeIndexOffsetMax && i < getContainer().getCurrentRecipe().getTotalOutputs(); ++i) {
            int j = i - recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            itemRender.renderItemAndEffectIntoGUI(recipe.getOutput(i), k, i1);
        }

        RenderHelper.disableStandardItemLighting();
    }

    private void drawRecipeButton(int mouseX, int mouseY, int i, int j) {
        if (FConfig.INSTANCE.getVillageAndPillage().stonecutter.recipeButton) {
            int x = guiLeft + 143;
            int y = guiTop + 8;
            int textureY = 166;
            if (mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16) {
                textureY += 16;
            }
            drawTexturedModalRect(i + 143, j + 8, 16, textureY, 16, 16);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clickedOnScroll = false;
        if (hasItemsInInputSlot) {
            int i = guiLeft + 52;
            int j = guiTop + 14;
            int k = recipeIndexOffset + 12;

            for (int l = recipeIndexOffset; l < k; ++l) {
                int i1 = l - recipeIndexOffset;
                double x = mouseX - (double) (i + i1 % 4 * 16);
                double y = mouseY - (double) (j + i1 / 4 * 18);
                if (x >= 0.0D && y >= 0.0D && x < 16.0D && y < 18.0D && getContainer().enchantItem(mc.player, l)) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    mc.playerController.sendEnchantPacket(getContainer().windowId, l);
                    return;
                }
            }

            i = this.guiLeft + 119;
            j = this.guiTop + 9;
            if (mouseX >= (double) i && mouseX < (double) (i + 12) && mouseY >= (double) j && mouseY < (double) (j + 54)) {
                clickedOnScroll = true;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedOnScroll && canScroll()) {
            int i = guiTop + 14;
            int j = i + 54;
            sliderProgress = (mouseY - i - 7.5F) / (j - i - 15.0F);
            sliderProgress = MathHelper.clamp(sliderProgress, 0.0F, 1.0F);
            recipeIndexOffset = (int) ((sliderProgress * getHiddenRows()) + 0.5D) << 2;
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && canScroll()) {
            i = (int) Math.signum(i);
            int h = getHiddenRows();
            sliderProgress = (float) ((double) sliderProgress - i / (double) h);
            sliderProgress = MathHelper.clamp(sliderProgress, 0.0F, 1.0F);
            recipeIndexOffset = (int) ((double) (sliderProgress * (float) h) + 0.5D) << 2;
        }
    }

    private boolean canScroll() {
        return getContainer().getCurrentRecipe() != null && hasItemsInInputSlot && getContainer().getCurrentRecipe().getTotalOutputs() > 12;
    }

    private int getHiddenRows() {
        return (getContainer().getRecipeListSize() + 4 - 1) / 4 - 3;
    }

    private void onInventoryUpdate() {
        hasItemsInInputSlot = getContainer().hasRecipe();

        if (!hasItemsInInputSlot) {
            sliderProgress = 0.0F;
            recipeIndexOffset = 0;
        }
    }
}