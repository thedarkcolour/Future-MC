@file:JvmName("Util")

package thedarkcolour.core.util

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import net.minecraft.block.BlockDispenser
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.eventhandler.*
import net.minecraftforge.fml.common.registry.EntityRegistry
import net.minecraftforge.oredict.OreDictionary
import thedarkcolour.core.block.IProjectileDispenserBehaviour
import thedarkcolour.futuremc.FutureMC
import java.util.function.BiPredicate
import java.util.function.Consumer

/**
 * JVM [make] that does not get inlined
 */
fun <T> jmake(obj: T, consumer: Consumer<T>): T {
    consumer.accept(obj)
    return obj
}

inline fun <T> make(obj: T, consumer: (T) -> Unit): T {
    consumer(obj)
    return obj
}

/**
 * Gets the oredict name for the given stack.
 */
fun getOreName(stack: ItemStack): String {
    if (stack.isEmpty) {
        return ""
    }
    val ids = OreDictionary.getOreIDs(stack)
    return if (ids.isNotEmpty()) {
        OreDictionary.getOreName(ids[0])
    } else ""
}

/**
 * Used to avoid silly proxies.
 */
inline fun runOnClient(runnable: () -> Unit) {
    if (FutureMC.CLIENT) {
        runnable()
    }
}

/**
 * Creates an immutable map and fills it using the provided [contents]
 */
fun <K, V> immutableMapOf(contents: Consumer<ImmutableMap.Builder<K, V>>): ImmutableMap<K, V> {
    return make(ImmutableMap.Builder(), contents::accept).build()
}

/**
 * Creates an immutable list from the contents of the given list.
 * @param contents the contents to be held in the new list
 * @return an [ImmutableList]  with the given contents.
 */
fun <T> immutableListOf(contents: Iterable<T>): ImmutableList<T> {
    return ImmutableList.Builder<T>().addAll(contents).build()
}

/**
 * Returns a new instance of an extremely sophisticated data structure [PredicateArrayList] whose [java.util.List.contains] implementation
 * will return true if it contains a match for the specified [isEquivalent] predicate.
 * @param isEquivalent the test used to determine if two objects are equivalent
 * @param contents the initial contents to be contained in this list.
 */
fun <T> predicateArrayListOf(isEquivalent: BiPredicate<T, T>, contents: Consumer<PredicateArrayList<T>>): PredicateArrayList<T> {
    val list = PredicateArrayList(isEquivalent)
    return make(list, contents::accept)
}

fun <T> predicateArrayListOf(isEquivalent: BiPredicate<T, T>, contents: Array<T>): PredicateArrayList<T> {
    val list = PredicateArrayList(isEquivalent)
    return list.insertAll(*contents)
}

fun <T> predicateArrayListOf(contents: Array<out T>, isEquivalent: (T, T) -> Boolean): PredicateArrayList<T> {
    val list = PredicateArrayList(isEquivalent)
    return list.insertAll(*contents)
}

fun registerDispenserBehaviour(item: Item, behaviour: IProjectileDispenserBehaviour) {
    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, behaviour)
}

/**
 * Helper method to reduce verbosity when registering entities.
 */
fun registerEntity(name: String, entity: Class<out Entity>, trackingRange: Int, id: Int) {
    EntityRegistry.registerModEntity(ResourceLocation(FutureMC.ID, name), entity, name, id, FutureMC, trackingRange, 1, true)
}

/**
 * Helper method to reduce verbosity when registering entities.
 */
fun registerEntity(name: String, entity: Class<out Entity>, trackingRange: Int, id: Int, primary: Int, secondary: Int) {
    EntityRegistry.registerModEntity(ResourceLocation(FutureMC.ID, name), entity, "futuremc:$name", id, FutureMC, trackingRange, 1, true, primary, secondary)
}

/**
 * Helper method to reduce verbosity when registering entities.
 * @param T used to determine entity class based on the given factory
 */
inline fun <reified T : Entity> registerEntityModel(noinline factory: (RenderManager) -> Render<T>) {
    RenderingRegistry.registerEntityRenderingHandler(T::class.java, factory)
}

fun subscribe(target: Any) {
    MinecraftForge.EVENT_BUS.register(target)
}

/**
 * Registers a lambda to the event bus, much like in 1.13+.
 */
inline fun <reified E : Event> addListener(crossinline consumer: (E) -> Unit) {
    val constructor = E::class.java.getConstructor()
    constructor.isAccessible = true
    val event = constructor.newInstance()
    val loader = Loader.instance()

    val owner = loader.activeModContainer() ?: loader.minecraftModContainer

    val listener = IEventListener {
        val old = loader.activeModContainer()
        loader.setActiveModContainer(owner)
        try {
            (it as IContextSetter).setModContainer(owner)
        } catch (e: ClassCastException) {
            // ignore
        }
        consumer(it as E)
        loader.setActiveModContainer(old)
    }

    event.listenerList.register(make(EventBus::class.java.getDeclaredField("busID")) {
        it.isAccessible = true
    }.get(MinecraftForge.EVENT_BUS) as Int, EventPriority.NORMAL, listener)
}

fun ItemStack.getOrCreateTag(): NBTTagCompound {
    return tagCompound ?: NBTTagCompound()
}

// linear interpolation
fun lerp(a: Float, b: Float, c: Float): Float {
    return a + c * (b - a)
}

// for some reason #defaultInstance is client only
val Item.stack : ItemStack
    get() = ItemStack(this)