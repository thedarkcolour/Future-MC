package thedarkcolour.futuremc.config.option

import com.electronwill.nightconfig.core.CommentedConfig
import net.minecraftforge.common.ForgeConfigSpec

class CategoryOption(name: String, default: Unit, desc: String) : Option<Unit>(name, default, desc), Iterable<Option<*>> {
    val options = ArrayList<Option<*>>(0)

    fun add(option: Option<*>) {
        options.add(option)
    }

    override fun sync(data: CommentedConfig, parentName: String) {
        for (option in this) {
            val name = if (parentName.isEmpty()) {
                this.name
            } else {
                "$parentName.$name"
            }
            option.sync(data, name)
        }
    }

    override fun getOptions(): Array<Option<*>> {
        val list = arrayListOf<Option<*>>()

        for (option in options) {
            list.addAll(option.getOptions())
        }

        return list.toTypedArray()
    }

    override fun addToConfig(builder: ForgeConfigSpec.Builder) {
        builder.comment(desc)
        builder.push(name)

        for (option in this) {
            option.addToConfig(builder)
        }

        builder.pop()
    }

    override fun iterator(): Iterator<Option<*>> {
        options.sortWith(Comparator.comparing(Option<*>::name).reversed())
        return options.iterator()
    }
}