package thedarkcolour.core.util

import com.google.common.collect.ImmutableMap
import it.unimi.dsi.fastutil.objects.Object2DoubleMap
import net.minecraft.block.BlockDispenser
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.dispenser.BehaviorDefaultDispenseItem
import net.minecraft.dispenser.IBehaviorDispenseItem
import net.minecraft.dispenser.IBlockSource
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.EventBus
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.IEventListener
import net.minecraftforge.fml.common.registry.EntityRegistry
import net.minecraftforge.oredict.OreDictionary
import thedarkcolour.futuremc.FutureMC
import java.util.function.Consumer

@Suppress("UNCHECKED_CAST")
fun <T> cast(obj: Any): T {
    return obj as T
}

@Suppress("SpellCheckingInspection")
fun <T> make(obj: T, consumer: Consumer<T>): T {
    consumer.accept(obj)
    return obj
}

/**
 * Gets the oredict names for the given stack.
 */
fun getOreNames(stack: ItemStack): List<String> {
    return OreDictionary.getOreIDs(stack).map(OreDictionary::getOreName)
}

/**
 * Avoids silly proxies.
 */
inline fun runOnClient(runnable: () -> Unit) {
    if (FutureMC.CLIENT) {
        runnable()
    }
}

/**
 * Creates an immutable map and fills it using the [contents]
 */
fun <K, V> immutableMapOf(contents: (ImmutableMap.Builder<K, V>) -> Unit): ImmutableMap<K, V> {
    return ImmutableMap.Builder<K, V>().also(contents::invoke).build()
}

/**
 * Adds new functionality to the item without removing the old functionality.
 *
 * Think using the OR binary operator.
 */
fun registerOrDispenserBehaviour(item: Item, behaviour: IBehaviorDispenseItem) {
    if (BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.keys.contains(item)) {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, IBehaviorDispenseItem { worldIn, stack ->
            val stack1 = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(item).dispense(worldIn, stack)
            behaviour.dispense(worldIn, stack1)
        })
    } else {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, behaviour)
    }
}

/**
 * Shortcut function that only runs the behaviour on server side
 */
inline fun registerServerDispenserBehaviour(item: Item, crossinline behaviour: (World, IBlockSource, ItemStack) -> ItemStack) {
    registerOrDispenserBehaviour(item, object : BehaviorDefaultDispenseItem() {
        override fun dispenseStack(source: IBlockSource, stack: ItemStack): ItemStack {
            val worldIn = source.world

            return if (!worldIn.isRemote) {
                behaviour(worldIn, source, stack)
            } else stack
        }
    })
}

/**
 * Helper method to reduce verbosity when registering entities.
 */
fun registerEntity(name: String, entity: Class<out Entity>, trackingRange: Int, id: Int) {
    EntityRegistry.registerModEntity(
        ResourceLocation(FutureMC.ID, name),
        entity,
        name,
        id,
        FutureMC,
        trackingRange,
        1,
        true
    )
}

/**
 * Helper method to reduce verbosity when registering entities.
 */
fun registerEntity(name: String, entity: Class<out Entity>, trackingRange: Int, id: Int, primary: Int, secondary: Int) {
    EntityRegistry.registerModEntity(
        ResourceLocation(FutureMC.ID, name),
        entity,
        "futuremc:$name",
        id,
        FutureMC,
        trackingRange,
        1,
        true,
        primary,
        secondary
    )
}

/**
 * Helper method to reduce verbosity when registering entities.
 * @param T used to determine entity class based on the given factory
 */
inline fun <reified T : Entity> registerEntityModel(noinline factory: (RenderManager) -> Render<T>) {
    RenderingRegistry.registerEntityRenderingHandler(T::class.java, factory)
}

/**
 * Subscribes [target] to event bus for game events.
 */
fun subscribe(target: Any) {
    MinecraftForge.EVENT_BUS.register(target)
}

/**
 * Registers a function to the event bus like in 1.13+.
 */
inline fun <reified E : Event> addListener(crossinline consumer: (E) -> Unit, priority: EventPriority = EventPriority.NORMAL) {
    val constructor = E::class.java.getConstructor()
    constructor.isAccessible = true
    val event = constructor.newInstance()

    val listener = IEventListener {
        consumer(it as E)
    }

    event.listenerList.register(EventBus::class.java.getDeclaredField("busID").also {
        it.isAccessible = true
    }.get(MinecraftForge.EVENT_BUS) as Int, priority, listener)
}

fun ItemStack.getOrCreateTag(): NBTTagCompound {
    return tagCompound ?: NBTTagCompound()
}

// linear interpolation
fun lerp(a: Float, b: Float, c: Float): Float {
    return b + a * (c - b)
}

// linear interpolation
fun lerp(a: Double, b: Double, c: Double): Double {
    return b + a * (c - b)
}

// for some reason #defaultInstance is client only
val Item.stack: ItemStack
    inline get() = ItemStack(this)

fun <T> T.matchesAny(vararg any: T): Boolean {
    for (t in any) {
        if (this == t) {
            return true
        }
    }

    return false
}

fun <T> Iterable<T>.anyMatch(test: (T) -> Boolean): Boolean {
    forEach {
        if (test(it)) {
            return@anyMatch true
        }
    }

    return false
}

fun <T> janyMatch(iterable: Iterable<T>, test: (T) -> Boolean): Boolean {
    return iterable.anyMatch(test)
}

val models = ArrayList<Triple<Item, Int, String>>()

fun setItemModel(item: Item, meta: Int, string: String = item.registryName!!.toString()) {
    runOnClient {
        models.add(Triple(item, meta, string))
    }
}

fun setItemName(item: Item, registryName: String, translationKey: String = "${FutureMC.ID}.$registryName") {
    item.translationKey = translationKey
    item.registryName = ResourceLocation(FutureMC.ID, registryName)
}

@Suppress("FunctionName")
fun TODO() = false

fun <T> getDoubleOrDefault(map: Object2DoubleMap<T>, key: T, default: Double): Double {
    return if (map.containsKey(key)) {
        map.getDouble(key)
    } else default
}