package thedarkcolour.futuremc.item

import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting
import kotlin.math.PI

fun getVectorForRotation(pitch: Float, yaw: Float): Vec3d {
    val f = MathHelper.cos(-yaw * 0.017453292f - PI.toFloat())
    val f1 = MathHelper.sin(-yaw * 0.017453292f - PI.toFloat())
    val f2 = -MathHelper.cos(-pitch * 0.017453292f)
    val f3 = MathHelper.sin(-pitch * 0.017453292f)
    return Vec3d((f1 * f2).toDouble(), f3.toDouble(), (f * f2).toDouble())
}

fun Vec3d.func214905a(float0: Double, float1: Double, float2: Double, float3: Double): Vec3d {
    var f0 = float3 * x + float0 * 0 + float1 * z - float2 * y
    var f1 = float3 * y - float0 * z + float1 * 0 + float2 * x
    var f2 = float3 * z + float0 * y - float1 * x + float2 * 0
    f0 *= -(float0)
    f1 *= -(float1)
    f2 *= -(float2)
    return Vec3d(f0, f1, f2)
}

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