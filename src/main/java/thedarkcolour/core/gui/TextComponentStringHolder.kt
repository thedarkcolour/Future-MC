package thedarkcolour.core.gui

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentBase

class TextComponentStringHolder(var text: String) : TextComponentBase() {
    override fun createCopy(): ITextComponent {
        val copy = TextComponentStringHolder(text)
        copy.style = style.createShallowCopy()
        getSiblings().forEach {
            copy.appendSibling(it.createCopy())
        }
        return copy
    }

    override fun getUnformattedComponentText(): String = text
}