package thedarkcolour.core.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Container extends net.minecraft.inventory.Container {
    @SideOnly(Side.CLIENT)
    public abstract GuiContainer getGuiContainer();
}