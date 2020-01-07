package thedarkcolour.futuremc.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.container.ContainerLoom;

// TODO
public class GuiLoom extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(FutureMC.ID,"textures/gui/loom.png");
    private final ContainerLoom container;

    public GuiLoom(ContainerLoom container) {
        super(container);
        this.container = container;
        container.setInventoryUpdateListener(this::onInventoryUpdate);
    }

    private void onInventoryUpdate() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format("container.Loom");
        fontRenderer.drawString(s, 8, 4, 4210752);
        fontRenderer.drawString(container.getPlayerInv().getDisplayName().getFormattedText(), 8, ySize - 94, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
/*
public class GuiLoom extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(FutureMC.ID,"textures/gui/loom.png");
    private static final int NUM_BANNER_RECIPES = (BannerPattern.values().length - 5 - 1 + 4 - 1) / 4;
    private static final List<EnumDyeColor> color = Lists.newArrayList(EnumDyeColor.GRAY, EnumDyeColor.WHITE);
    private static final ResourceLocation[] designs = Arrays.stream(BannerPattern.values())
            .filter(pattern -> pattern != BannerPattern.BASE)
            .map(pattern -> BannerTextures.BANNER_DESIGNS.getResourceLocation("b8" + pattern.getHashname() + "15", Lists.newArrayList(BannerPattern.BASE, pattern), color))
            .toArray(ResourceLocation[]::new);
    private final ContainerLoom container;
    private ResourceLocation bannerLocation;
    private boolean canScroll;
    private boolean field_214124_v;
    private boolean isBlank;
    private boolean clickedOnScroll;
    private ItemStack bannerStack = ItemStack.EMPTY;
    private ItemStack colorStack = ItemStack.EMPTY;
    private ItemStack patternStack = ItemStack.EMPTY;
    private int recipeIndexOffset = 1;
    private float sliderProgress;

    public GuiLoom(ContainerLoom container) {
        super(container);
        this.container = container;
        container.setInventoryUpdateListener(this::onInventoryUpdate);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format("container.Loom");
        this.fontRenderer.drawString(s, 8, 4, 4210752);
        this.fontRenderer.drawString(container.getPlayerInv().getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(BACKGROUND);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        Slot output = container.getLoomSlot(3);
        drawSlotBackgrounds(i, j);
        int scrollY = (int)(41.0F * sliderProgress);
        drawTexturedModalRect(i + 119, j + 13 + scrollY, 232 + (canScroll ? 0 : 12), 0, 12, 15);

        // Code for the output preview
        if (bannerLocation != null && !isBlank) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(i + 139, j + 52, 0);
            //GlStateManager.scale(24.0f, -24.0f, 1.0f);
            mc.getTextureManager().bindTexture(bannerLocation);
            zLevel = 20;
            drawTexturedModalRect(i + 141, j + 8, 20, 40, 20, 40);
            zLevel = 0;
            GlStateManager.popMatrix();
        } else if (!isBlank) {
            drawTexturedModalRect(i + output.xPos - 2, j + output.yPos - 2, xSize, 17, 17, 16);
        }

        if (canScroll) {
            int l = i + 60;
            int i1 = j + 13;
            int j1 = recipeIndexOffset + 16;

            for (int k1 = recipeIndexOffset; k1 < j1 && k1 < designs.length - 5; ++k1) {
                int l1 = k1 - recipeIndexOffset;
                int i2 = l + l1 % 4 * 14;
                int j2 = i1 + l1 / 4 * 14;
                mc.getTextureManager().bindTexture(BACKGROUND);
                int k2 = ySize;
                if (k1 == container.getSelectedIndex()) {
                    k2 += 14;
                } else if (mouseX >= i2 && mouseY >= j2 && mouseX < i2 + 14 && mouseY < j2 + 14) {
                    k2 += 28;
                }

                //GlStateManager.pushMatrix();
                //GlStateManager.scale(0.85f, 0.85f, 1.0f);

                drawTexturedModalRect(i2, j2, 0, k2, 14, 14);
                if (designs[k1] != null) {
                    mc.getTextureManager().bindTexture(designs[k1]);
                    drawTexturedModalRect(i2 + 4, j2 + 2, 5, 10, 20, 40);
                }

                //GlStateManager.popMatrix();
            }
        } else if (field_214124_v) {
            int l2 = i + 60;
            int i3 = j + 13;
            mc.getTextureManager().bindTexture(BACKGROUND);
            drawTexturedModalRect(l2, i3, 0, ySize, 14, 14);
            int j3 = container.getSelectedIndex();
            if (designs[j3] != null) {
                mc.getTextureManager().bindTexture(designs[j3]);
                drawTexturedModalRect(l2 + 4, i3 + 2, 5, 10, 20, 40);
            }
        }
    }

    public void drawSlotBackgrounds(int i, int j) {
        Slot banner = container.getLoomSlot(0);
        Slot color = container.getLoomSlot(1);
        Slot pattern = container.getLoomSlot(2);

        if (!banner.getHasStack()) {
            drawTexturedModalRect(i + banner.xPos, j + banner.yPos, xSize, 0, 16, 16);
        }

        if (!color.getHasStack()) {
            drawTexturedModalRect(i + color.xPos, j + color.yPos, xSize + 16, 0, 16, 16);
        }

        if (!pattern.getHasStack()) {
            drawTexturedModalRect(i + pattern.xPos, j + pattern.yPos, xSize + 32, 0, 16, 16);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clickedOnScroll = false;
        if (canScroll) {
            int i = guiLeft + 60;
            int j = guiTop + 13;
            int k = recipeIndexOffset + 16;

            for (int l = recipeIndexOffset; l < k; ++l) {
                int i1 = l - recipeIndexOffset;
                double x = mouseX - (double)(i + i1 % 4 * 14);
                double y = mouseY - (double)(j + i1 / 4 * 14);
                if (x >= 0.0D && y >= 0.0D && x < 14.0D && y < 14.0D && container.enchantItem(mc.player, l)) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    mc.playerController.sendEnchantPacket(container.windowId, l);
                    return;
                }
            }

            i = this.guiLeft + 119;
            j = this.guiTop + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 56)) {
                clickedOnScroll = true;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedOnScroll && canScroll) {
            int i = guiTop + 13;
            int j = i + 56;
            sliderProgress = (mouseY - i - 7.5F) / (j - i - 15.0F);
            sliderProgress = MathHelper.clamp(sliderProgress, 0.0F, 1.0F);
            recipeIndexOffset = (int)((sliderProgress * getHiddenRows()) + 0.5D) << 2;
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && canScroll) {
            i = (int) Math.signum(i);
            int h = getHiddenRows();
            sliderProgress = (float)((double)sliderProgress - i / (double)h);
            sliderProgress = MathHelper.clamp(sliderProgress, 0.0F, 1.0F);
            recipeIndexOffset = 1 + (int)((double)(sliderProgress * (float)h) + 0.5D) * 4;
        }
    }

    private int getHiddenRows() {
        return NUM_BANNER_RECIPES - 4;
    }

    @Override
    protected boolean hasClickedOutside(int param1, int param2, int param3, int param4) {
        return param1 < param3 || param2 < param4 || param1 >= param3 + xSize || param2 >= param4 + ySize;
    }

    private void onInventoryUpdate() {
        ItemStack itemstack = container.getOutput();
        if (itemstack.isEmpty()) {
            bannerLocation = null;
        } else {
            TileEntityBanner te = new TileEntityBanner();
            te.setItemValues(itemstack, false);
            bannerLocation = BannerTextures.BANNER_DESIGNS.getResourceLocation(te.getPatternResourceLocation(), te.getPatternList(), te.getColorList());
        }

        ItemStack stack = container.getBanner();
        ItemStack stack1 = container.getColor();
        ItemStack stack2 = container.getPattern();
        NBTTagCompound tag = stack.getOrCreateSubCompound("BlockEntityTag");
        isBlank = tag.hasKey("Patterns", 9) && !stack.isEmpty() && canFitPatterns(tag.getTagList("Patterns", 10));
        if (isBlank) {
            bannerLocation = null;
        }

        if (!ItemStack.areItemStacksEqual(stack, bannerStack) || !ItemStack.areItemStacksEqual(stack1, this.colorStack) || !ItemStack.areItemStacksEqual(stack2, this.patternStack)) {
            canScroll = !stack.isEmpty() && !stack1.isEmpty() && stack2.isEmpty() && !isBlank;
            field_214124_v = !isBlank && !stack2.isEmpty() && !stack.isEmpty() && !stack1.isEmpty();
        }

        bannerStack = stack.copy();
        colorStack = stack1.copy();
        patternStack = stack2.copy();
    }

    private boolean canFitPatterns(NBTTagList list) {
        int i = 0;

        for (NBTBase ignored : list) {
            ++i;
        }
        return i <= 6;
    }
}
*/