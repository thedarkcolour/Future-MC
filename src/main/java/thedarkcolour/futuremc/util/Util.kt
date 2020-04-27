package thedarkcolour.futuremc.util

import sun.reflect.ConstructorAccessor
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Constructor

/**
 * Creates an instance of the desired enum, but does not add it to the enum's class.
 *
 * @param enumClass Type of enum to create.
 * @param name Name of the enum. Should be in snake case.
 * @param args Arguments for the constructor of the Enum.
 *             Picks the correct constructor for the given arguments,
 *             and throws an exception if no constructor is found.
 */
fun <T : Enum<T>> instantiateEnum(enumClass: Class<T>, name: String, vararg args: Any): T {
    val constructors = enumClass.declaredConstructors.filterNotNull().map { constructor ->
        constructor.nonPrivate()

        constructorAccessorField.get(constructor) as ConstructorAccessor?
                ?: acquireAccessorMethod(constructor) as ConstructorAccessor
    }

    enumClass.declaredConstructors.forEach {
        println(it.parameterTypes.contentToString())
    }

    var enum: T? = null
    for (constructor in constructors) {
        @Suppress("UNCHECKED_CAST")
        try {
            println(constructor)
            // replace 0 with the index when actually adding to enum constants
            enum = constructor.newInstance(arrayOf(name, 0, *args)) as T
            break
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
    return enum ?: throw IllegalArgumentException("No matching enum constructor found for arguments: ${args.contentToString()}")
}

private val constructorAccessorField = Constructor::class.java.getDeclaredField("constructorAccessor").nonPrivate()
private val acquireAccessorMethod = Constructor::class.java.getDeclaredMethod("acquireConstructorAccessor").nonPrivate()

/**
 * Makes this [AccessibleObject] accessible in all scopes.
 * @receiver The field, method, constructor, etc. that is made public
 */
fun <T : AccessibleObject> T.nonPrivate(): T {
    isAccessible = true
    return this
}