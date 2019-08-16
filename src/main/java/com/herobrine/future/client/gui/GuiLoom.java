package com.herobrine.future.client.gui;

import com.google.common.collect.Lists;
import com.herobrine.future.FutureMC;
import com.herobrine.future.container.ContainerLoom;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class GuiLoom extends GuiContainer {
    private static final ResourceLocation background = new ResourceLocation(FutureMC.ID,"textures/gui/loom.png");
    private static final int field_214114_l = (BannerPattern.values().length - 5 - 1 + 4 - 1) / 4;
    private final ResourceLocation[] DESIGNS = new ResourceLocation[BannerPattern.values().length];
    private final ContainerLoom container;
    private ResourceLocation bannerLocation;
    private boolean canScroll;
    private boolean field_214124_v;
    private boolean field_214125_w;
    private boolean field_214127_y;
    private ItemStack bannerStack = ItemStack.EMPTY;
    private ItemStack colorStack = ItemStack.EMPTY;
    private ItemStack patternStack = ItemStack.EMPTY;
    private int field_214128_z = 1;
    private float currentScroll;
    private static final EnumDyeColor GRAY = EnumDyeColor.GRAY;
    private static final EnumDyeColor WHITE = EnumDyeColor.WHITE;
    private static final List<EnumDyeColor> color = Lists.newArrayList(GRAY, WHITE);
    private int index = 1;

    public GuiLoom(ContainerLoom container) {
        super(container);
        this.container = container;
        container.setRunnable(this::func_214111_b);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.index < BannerPattern.values().length) {
            BannerPattern bannerpattern = BannerPattern.values()[this.index];
            String s = "b" + GRAY.getDyeDamage();
            String s1 = bannerpattern.getHashname() + WHITE.getDyeDamage();
            this.DESIGNS[this.index] = BannerTextures.BANNER_DESIGNS.getResourceLocation(s + s1, Lists.newArrayList(BannerPattern.BASE, bannerpattern), color);
            ++this.index;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
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
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(background);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        Slot banner = this.container.getLoomSlot(0);
        Slot color = this.container.getLoomSlot(1);
        Slot pattern = this.container.getLoomSlot(2);
        Slot output = this.container.getLoomSlot(3);

        if(!banner.getHasStack()) {
            this.drawTexturedModalRect(i + banner.xPos, j + banner.yPos, this.xSize, 0, 16, 16);
        }

        if(!color.getHasStack()) {
            this.drawTexturedModalRect(i + color.xPos, j + color.yPos, this.xSize + 16, 0, 16, 16);
        }

        if (!pattern.getHasStack()) {
            this.drawTexturedModalRect(i + pattern.xPos, j + pattern.yPos, this.xSize + 32, 0, 16, 16);
        }

        int k = (int)(41.0F * this.currentScroll);
        this.drawTexturedModalRect(i + 119, j + 13 + k, 232 + (this.canScroll ? 0 : 12), 0, 12, 15);
        if (this.bannerLocation != null && !this.field_214125_w) {
            this.mc.getTextureManager().bindTexture(this.bannerLocation);
            zLevel = 20;
            this.drawTexturedModalRect(i + 141, j + 8, 20, 40, 20, 40);
            zLevel = 0;
        } else if (this.field_214125_w) {
            this.drawTexturedModalRect(i + output.xPos - 2, j + output.yPos - 2, this.xSize, 17, 17, 16);
        }

        if (this.canScroll) {
            int l = i + 60;
            int i1 = j + 13;
            int j1 = this.field_214128_z + 16;

            for(int k1 = this.field_214128_z; k1 < j1 && k1 < this.DESIGNS.length - 5; ++k1) {
                int l1 = k1 - this.field_214128_z;
                int i2 = l + l1 % 4 * 14;
                int j2 = i1 + l1 / 4 * 14;
                this.mc.getTextureManager().bindTexture(background);
                int k2 = this.ySize;
                if (k1 == this.container.func_217023_e()) {
                    k2 += 14;
                } else if (mouseX >= i2 && mouseY >= j2 && mouseX < i2 + 14 && mouseY < j2 + 14) {
                    k2 += 28;
                }

                this.drawTexturedModalRect(i2, j2, 0, k2, 14, 14);
                if (this.DESIGNS[k1] != null) {
                    this.mc.getTextureManager().bindTexture(this.DESIGNS[k1]);
                    drawTexturedModalRect(i2 + 4, j2 + 2, 5, 10, 20, 40);
                }
            }
        } else if (this.field_214124_v) {
            int l2 = i + 60;
            int i3 = j + 13;
            this.mc.getTextureManager().bindTexture(background);
            this.drawTexturedModalRect(l2, i3, 0, this.ySize, 14, 14);
            int j3 = this.container.func_217023_e();
            if (this.DESIGNS[j3] != null) {
                this.mc.getTextureManager().bindTexture(this.DESIGNS[j3]);
                drawTexturedModalRect(l2 + 4, i3 + 2, 5, 10, 20, 40);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.field_214127_y = false;
        if (this.canScroll) {
            int i = this.guiLeft + 60;
            int j = this.guiTop + 13;
            int k = this.field_214128_z + 16;

            for(int l = this.field_214128_z; l < k; ++l) {
                int i1 = l - this.field_214128_z;
                double d0 = mouseX - (double)(i + i1 % 4 * 14);
                double d1 = mouseY - (double)(j + i1 / 4 * 14);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 14.0D && d1 < 14.0D && this.container.enchantItem(this.mc.player, l)) {
                    //Minecraft.getInstance().getSoundHandler().play(Sounds.LOOM_SELECT_PATTERN, 1.0F); // TODO - Sounds
                    this.mc.playerController.sendEnchantPacket((this.container).windowId, l);
                }
            }

            i = this.guiLeft + 119;
            j = this.guiTop + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 56)) {
                this.field_214127_y = true;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }



    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        if (this.canScroll) {
            int i = field_214114_l - 4;
            int j = Mouse.getEventDWheel();
            this.currentScroll = (float)((double)this.currentScroll - j / (double)i);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            this.field_214128_z = 1 + (int)((double)(this.currentScroll * (float)i) + 0.5D) * 4;
        }
    }

    @Override
    protected boolean hasClickedOutside(int param1, int param2, int param3, int param4) {
        return param1 < param3 || param2 < param4 || param1 >= param3 + this.xSize || param2 >= param4 + this.ySize;
    }

    private void func_214111_b() {
        ItemStack itemstack = this.container.getOutput();
        if (itemstack.isEmpty()) {
            this.bannerLocation = null;
        } else {
            TileEntityBanner te = new TileEntityBanner();
            te.setItemValues(itemstack, false);
            this.bannerLocation = BannerTextures.BANNER_DESIGNS.getResourceLocation(te.getPatternResourceLocation(), te.getPatternList(), te.getColorList());
        }

        ItemStack stack3 = this.container.getBanner();
        ItemStack stack1 = this.container.getColor();
        ItemStack stack2 = this.container.getPattern();
        NBTTagCompound tag = stack3.getOrCreateSubCompound("BlockEntityTag");
        this.field_214125_w = tag.hasKey("Patterns", 9) && !stack3.isEmpty() && canFitPatterns(tag.getTagList("Patterns", 10));
        if (this.field_214125_w) {
            this.bannerLocation = null;
        }

        if (!ItemStack.areItemStacksEqual(stack3, this.bannerStack) || !ItemStack.areItemStacksEqual(stack1, this.colorStack) || !ItemStack.areItemStacksEqual(stack2, this.patternStack)) {
            this.canScroll = !stack3.isEmpty() && !stack1.isEmpty() && stack2.isEmpty() && !this.field_214125_w;
            this.field_214124_v = !this.field_214125_w && !stack2.isEmpty() && !stack3.isEmpty() && !stack1.isEmpty();
        }

        this.bannerStack = stack3.copy();
        this.colorStack = stack1.copy();
        this.patternStack = stack2.copy();
    }

    private boolean canFitPatterns(NBTTagList list) {
        int i = 0;

        for(NBTBase ignored : list) {
            i++;
        }
        return i >= 6;
    }
}