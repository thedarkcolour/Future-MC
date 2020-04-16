package thedarkcolour.futuremc.util

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponent

class StringTextComponentMutable(var text: String) : TextComponent() {
    override fun shallowCopy(): ITextComponent {
        val copy = StringTextComponentMutable(text)
        copy.style = style.createShallowCopy()
        getSiblings().forEach {
            copy.appendSibling(it.shallowCopy())
        }
        return copy
    }

    override fun getUnformattedComponentText() = text
}