package com.herobrine.future.blocks.tile;

import com.herobrine.future.utils.Init;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiStonecutter extends GuiContainer {
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;

    private static final ResourceLocation background = new ResourceLocation(Init.MODID, "textures/gui/stonecutter.png");

    public GuiStonecutter(TileEntityStonecutter tileEntity, ContainerStonecutter container) {
        super(container);
        field_146999_f = WIDTH;
        field_147000_g = HEIGHT;
    }

    @Override
    protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {
        field_146297_k.func_110434_K().func_110577_a(background);
        func_73729_b(field_147003_i, field_147009_r, 0, 0, field_146999_f, field_147000_g);
    }



    @Override
    protected void func_146979_b(int mouseX, int mouseY) {
        this.field_146289_q.func_78276_b("Stonecutter", 8, 6, 4210752);
        this.field_146289_q.func_78276_b("Inventory", 8, this.field_147000_g - 92, 4210752);
    }

    @Override
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.func_191948_b(mouseX, mouseY);
    }
}
