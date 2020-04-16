package thedarkcolour.futuremc.client.screen

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import java.util.function.BiFunction

class ConfigScreen(private val mc: Minecraft, private val parent: Screen, titleIn: ITextComponent) : Screen(titleIn) {
    var entryList: ConfigEntriesList = ConfigEntriesList(this, mc, width, height, 32, height - 64, 36)
    var needsRefresh = false

    override fun init() {
        mc.keyboardListener.enableRepeatEvents(true)

        if (needsRefresh) {
            entryList.refresh()
        }
    }

    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
        renderBackground()
        entryList.render(mouseX, mouseY, partialTicks)
        drawCenteredString(font, title.formattedText, width / 2, 8, 0xffffff)
        super.render(mouseX, mouseY, partialTicks)
        // render tool tips for entries
    }

    override fun onClose() {
        mc.displayGuiScreen(parent)

    }

    class Factory : BiFunction<Minecraft, Screen, Screen> {
        private val title = StringTextComponent("Future MC Config")

        override fun apply(mc: Minecraft, parent: Screen): ConfigScreen {
            return ConfigScreen(mc, parent, title)
        }
    }
}