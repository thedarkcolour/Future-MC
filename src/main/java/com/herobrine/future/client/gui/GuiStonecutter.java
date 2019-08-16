package com.herobrine.future.client.gui;

import com.herobrine.future.FutureMC;
import com.herobrine.future.container.ContainerStonecutter;
import com.herobrine.future.item.crafting.stonecutter.RecipeResults;
import com.herobrine.future.item.crafting.stonecutter.StonecutterRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class GuiStonecutter extends GuiContainer {
    private final ContainerStonecutter container;
    private static final ResourceLocation background = new ResourceLocation(FutureMC.ID,"textures/gui/stonecutter.png");

    public GuiStonecutter(ContainerStonecutter container) {
        super(container);
        this.container = container;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(background);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        this.fontRenderer.drawString(container.getPlayerInv().getDisplayName().getUnformattedText(), 8, this.ySize - 93, 4210752);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.Stonecutter"), 8, 6, 4210752);
        this.fontRenderer.drawString(container.getPlayerInv().getDisplayName().getUnformattedText(), 8, this.ySize - 93, 4210752);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        ItemStack stack = container.input.getStackInSlot(0);

        if (StonecutterRecipes.isStackValid(stack)) {
            int results = StonecutterRecipes.getResultCount(stack);

            for (int i = 0; i < results; i++) {
                int j = 0;
                if (i / 4 >= j + 1) {
                    j++;
                }
                if (buttonList.size() < results) {
                    this.addButton(new ItemButton(i, guiLeft + 52 + i * 16 - j * 64, guiTop + 15 + j * 18,
                            false, RecipeResults.getStackResult(stack, i), this));
                }
            }
        }
        else {
            buttonList.clear();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        actionPerformedSec((ItemButton) button);
    }

    private void actionPerformedSec(ItemButton button) {
        int result = StonecutterRecipes.getResultCount(container.input.getStackInSlot(0));
        if (result > 0) { // Continues if the item is valid.
            for (int i = 0; i < result; i++) { // Iterates through all buttons of the gui.
                if (button.id == i) { // If the button clicked is the current, select that button
                    button.selected = true;
                    for (int j = 0; j < result; j++) {
                        if (j != i) {
                            deselectButton(j); // deselects other buttons after press.
                        }
                    }
                }
            }
        }
    }

    private void deselectButton(int buttonID) {
        GuiButton button = this.buttonList.get(buttonID);
        ((ItemButton) button).selected = false;
    }

    @SideOnly(Side.CLIENT)
    public static class ItemButton extends GuiButton {
        protected boolean selected;
        private final ItemStack stack;
        private final GuiStonecutter gui;
        private final int buttonID;

        public ItemButton(int buttonID, int x, int y, boolean isSelected, ItemStack stack, GuiStonecutter guiIn) {
            super(buttonID, x, y, 16, 18, "");
            this.selected = isSelected;
            this.stack = stack;
            this.gui = guiIn;
            this.buttonID = buttonID;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                mc.getTextureManager().bindTexture(background);
                GlStateManager.enableBlend();
                GlStateManager.disableLighting();
                RenderHelper.disableStandardItemLighting();

                boolean mouseOver = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                int j = 0;
                int i = 166;

                if (this.selected) {
                    i += this.height;
                }
                else if (mouseOver) {
                    i += this.height * 2;
                }

                this.drawTexturedModalRect(this.x, this.y, j, i, this.width, this.height); //Draws background of button

                if (!RecipeResults.getStackResult(stack, buttonID).isEmpty()) {
                    RenderHelper.enableGUIStandardItemLighting();
                    gui.drawItemStack(stack, x, y); //Draws item onto button
                }
            }
        }
    }

    private void drawItemStack(ItemStack stack, int x, int y) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        int i = y + 1;

        this.itemRender.renderItemIntoGUI(stack, x, i);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, x, i, "");
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }
}