package thedarkcolour.futuremc.config.option

import com.electronwill.nightconfig.core.CommentedConfig
import net.minecraftforge.common.ForgeConfigSpec
import thedarkcolour.futuremc.FutureMC

/**
 * Config option that wraps builder functions and remembers its default value.
 *
 * @param V the type of value this option represents
 * @param name name of the option
 * @param default the initial value of this option
 * @param desc description of this option
 * @author TheDarkColour
 */
open class Option<V>(val name: String, private val default: V, val desc: String) {
    /**
     * The current value of this option. This should only be modified during the
     * [net.minecraftforge.fml.config.ModConfig.ModConfigEvent] event.
     */
    var value: V = default

    /**
     * Translation key that is currently only used by the [ForgeConfigSpec.Builder].
     * When a configuration screen is implemented, this will be used for localization.
     */
    val translation: String
        get() = "config.${FutureMC.ID}.${name}"

    /**
     * Used to sync during [net.minecraftforge.fml.config.ModConfig.ModConfigEvent]
     *
     * @param data from the synced forge config
     * @param parentName the path of this option, includes parent categories
     */
    open fun sync(data: CommentedConfig, parentName: String) {
        this.value = data.get("$parentName.$name")
    }

    /**
     * Returns a single array with all options represented by this option.
     * If this option is [CategoryOption], then the returned array includes all
     * child options, including the children of [CategoryOption], and so on.
     */
    open fun getOptions(): Array<Option<*>> {
        return arrayOf(this)
    }

    /**
     * Adds this option to a [ForgeConfigSpec] config.
     * If this option is [CategoryOption], then a new category is created
     * with the [name] and [desc] of the category.
     */
    open fun addToConfig(builder: ForgeConfigSpec.Builder) {
        builder.comment(desc)
        builder.translation(translation)
        builder.define(name, default)
    }
}