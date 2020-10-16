package thedarkcolour.futuremc.client.screen

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.list.ExtendedList
import net.minecraft.client.resources.I18n
import thedarkcolour.futuremc.config.Config
import thedarkcolour.futuremc.config.option.Option

class ConfigEntriesList(parent: ConfigScreen, mcIn: Minecraft, widthIn: Int, heightIn: Int, topIn: Int, bottomIn: Int, slotHeightIn: Int) :
        ExtendedList<ConfigEntriesList.Entry>(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn) {
    fun refresh() {
        Config.netherUpdate.getOptions()
    }

    class Entry(private val screen: ConfigScreen, private val option: Option<*>) : AbstractListEntry<Entry>() {
       override fun render(index: Int, x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int, selected: Boolean, partialTicks: Float) {
           val displayName = I18n.format(option.translation)
           val font = screen.minecraft.fontRenderer
           val i = (y + 32 + 3).toFloat()
           val j = (x + 1).toFloat()
           font.drawString(displayName, i, j, 16777215)
       }

       override fun mouseClicked(p_mouseClicked_1_: Double, p_mouseClicked_3_: Double, p_mouseClicked_5_: Int): Boolean {
           return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)
       }
   }
}