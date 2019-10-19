package thedarkcolour.futuremc.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiLectern extends GuiScreenBook {
    private GuiButton buttonTakeBook;

    public GuiLectern(EntityPlayer player, ItemStack book) {
        super(player, book, true);
    }
}