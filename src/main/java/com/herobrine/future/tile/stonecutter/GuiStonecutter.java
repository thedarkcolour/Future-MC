package com.herobrine.future.tile.stonecutter;

import com.herobrine.future.network.ButtonMessage;
import com.herobrine.future.network.PacketHandler;
import com.herobrine.future.recipe.stonecutter.RecipeResults;
import com.herobrine.future.recipe.stonecutter.StonecutterRecipes;
import com.herobrine.future.utils.config.FutureConfig;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiStonecutter extends GuiContainer {
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private static TileEntityStonecutter te;

    private static final ResourceLocation background = new ResourceLocation(Init.MODID, "textures/gui/stonecutter.png");

    public GuiStonecutter(TileEntityStonecutter tileEntity, ContainerStonecutter containerIn) {
        super(containerIn);
        te = tileEntity;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString("Stonecutter", 8, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 92, 4210752);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        ItemStack stack = te.inputHandler.getStackInSlot(0);

        if (StonecutterRecipes.instance().isStackValid(stack)) {
            StonecutterRecipes recipes = StonecutterRecipes.instance();
            int results = recipes.getResultCount(stack);

            for (int i = 0; i < results; i++) {
                int j = 0;
                if (i / 4 >= j + 1) {
                    j++;
                }
                if (buttonList.size() < results) {
                    this.addButton(new ItemButton(i, guiLeft + 52 + i * 16 - j * 64, guiTop + 15 + j * 18,
                            isSelected(i + 1), RecipeResults.instance().getStackResult(stack, i), this));
                }
            }
        }
        else {
            buttonList.clear();
        }
    }

    private boolean isSelected(int buttonID) {
        if(te.getSelectedID() == buttonID) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        actionPerformedSec((ItemButton) button);
    }

    private void actionPerformedSec(ItemButton button) {
        int result = StonecutterRecipes.instance().getResultCount(te.inputHandler.getStackInSlot(0));
        if (result > 0) { // Continues if the item is valid.
            for (int i = 0; i < result; i++) { // Iterates through all buttons of the gui.
                if (button.id == i) { // If the button clicked is the current, select that button
                    button.selected = true;
                    if (FutureConfig.c.debug) System.out.println("Button " + i + " is now selected");
                    PacketHandler.INSTANCE.sendToServer(new ButtonMessage(1 + i));
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
        boolean selected;
        ItemStack stack;
        GuiStonecutter gui;
        int buttonID;
        ItemButton(int buttonID, int x, int y, boolean isSelected, ItemStack stack, GuiStonecutter guiIn) {
            super(buttonID, x, y, 16, 18, "");
            this.selected = isSelected;
            this.stack = stack;
            this.gui = guiIn;
            this.buttonID = buttonID;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(background);
                GlStateManager.enableBlend();
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

                if (!RecipeResults.instance().getStackResult(stack, buttonID).isEmpty()) {
                    RenderHelper.enableGUIStandardItemLighting();
                    gui.drawItemStack(stack, x, y); //Draws item onto button
                }
            }
        }
    }

    private void drawItemStack(ItemStack stack, int x, int y) {
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