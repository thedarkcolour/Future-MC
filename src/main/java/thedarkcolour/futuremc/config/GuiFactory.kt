package thedarkcolour.futuremc.config

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.client.gui.GuiConfig

class GuiFactory : IModGuiFactory {
    override fun initialize(minecraftInstance: Minecraft) {}

    override fun runtimeGuiCategories(): MutableSet<IModGuiFactory.RuntimeOptionCategoryElement>? {
        return null
    }

    override fun hasConfigGui() = true

    override fun createConfigGui(parentScreen: GuiScreen): GuiScreen {
        return GuiConfig(parentScreen, modid = FutureMC.ID, title = FutureMC.NAME).after()
    }
}