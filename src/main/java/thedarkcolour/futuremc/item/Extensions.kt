package thedarkcolour.futuremc.item

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting

fun ITextComponent.applyTextFormatting(vararg formatting: TextFormatting): ITextComponent {
    for (format in formatting) {
        val style = style
        if (format.isColor) {
            style.color = format
        }

        if (format.isFancyStyling) {
            when (format) {
                TextFormatting.OBFUSCATED -> style.setObfuscated(true)
                TextFormatting.BOLD -> style.setBold(true)
                TextFormatting.STRIKETHROUGH -> style.setStrikethrough(true)
                TextFormatting.UNDERLINE -> style.setUnderlined(true)
                TextFormatting.ITALIC -> style.setItalic(true)
                else -> {
                }
            }
        }
    }

    return this
}