package thedarkcolour.futuremc.client.gui

import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.config.GuiConfig

class GuiConfig(parentScreen: GuiScreen, modid: String, title: String) : GuiConfig(parentScreen, modid, title) {
    fun after(): GuiConfig {
        //configElements.sortWith(Comparator.comparing(IConfigElement::getName))

        return this
    }
}