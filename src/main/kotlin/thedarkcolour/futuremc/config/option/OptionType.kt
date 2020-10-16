package thedarkcolour.futuremc.config.option

/**
 * Used to create instances [Option] while still allowing immutable properties
 * in option. Also
 * @param V the class of this [OptionType]
 */
open class OptionType<V, C : Option<V>>(
    private val factory: (String, (String, V, String) -> C) -> Builder<V, C>,
    private val optionFactory: (String, V, String) -> C
) {
    fun configure(name: String): Builder<V, C> {
        return factory(name, optionFactory)
    }

    companion object {
        val BOOL = OptionType<Boolean, Option<Boolean>>(::Builder, ::Option)
        val INT = OptionType<Int, Option<Int>>(::Builder, ::Option)
        val STRING = OptionType<String, Option<String>>(::Builder, ::Option)
        val CATEGORY = OptionType(::Builder, ::CategoryOption)
    }

    /**
     * Used to create new instances of [Option].
     *
     * @param V the type of value the configured option represents.
     * @param C the [Option] class with the given [V] generic type.
     *          Used to make return types to prevent unsafe casting.
     * @param name name of the configured option
     * @param factory function to create a new instance of [C]
     */
    class Builder<V, C : Option<V>>(
        private val name: String, private val factory: (String, V, String) -> C
    ) {
        /**
         * The description of the configured option.
         */
        private var description = ""
        private var defaultValue: V? = null

        fun defaultValue(value: V): Builder<V, C> {
            defaultValue = value
            return this
        }

        fun description(desc: String): Builder<V, C> {
            description = desc
            return this
        }

        fun build(): C {
            return factory(name, defaultValue!!, description)
        }
    }
}