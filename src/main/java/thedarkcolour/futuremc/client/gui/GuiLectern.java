package thedarkcolour.futuremc.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLectern extends GuiScreenBook {
    private GuiButton buttonTakeBook;

    public GuiLectern(EntityPlayer player, ItemStack book) {
        super(player, book, true);
    }

    @Override
    public void initGui() {
        super.initGui();
        //this.buttonTakeBook = this.addButton(new GuiButton(6,))
    }
}