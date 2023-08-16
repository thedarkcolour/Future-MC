package thedarkcolour.core.util

import com.google.common.collect.ImmutableMap
import it.unimi.dsi.fastutil.objects.Object2DoubleMap
import net.minecraft.block.BlockDispenser
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.dispenser.BehaviorDefaultDispenseItem
import net.minecraft.dispenser.IBehaviorDispenseItem
import net.minecraft.dispenser.IBlockSource
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.registry.EntityRegistry
import net.minecraftforge.oredict.OreDictionary
import thedarkcolour.core.item.ModeledItem
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.client.ClientEvents
import thedarkcolour.futuremc.config.FConfig
import java.util.function.Consumer

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
fun registerDispenserBehaviour(item: Item, behaviour: IBehaviorDispenseItem) {
    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, behaviour)
}

/**
 * Shortcut function that only runs the behavior on server side.
 *
 * @param item the item to attach behavior to
 * @param behaviour the behaviour to attach (plus the existing behaviour if it exists)
 */
inline fun registerServerDispenserBehaviour(
    item: Item,
    crossinline behaviour: (World, IBlockSource, ItemStack, IBehaviorDispenseItem?) -> ItemStack
) {
    // only compute once
    val existing = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(item)

    registerDispenserBehaviour(item, object : BehaviorDefaultDispenseItem() {
        override fun dispenseStack(source: IBlockSource, stack: ItemStack): ItemStack {
            val worldIn = source.world

            return if (!worldIn.isRemote) {
                behaviour(worldIn, source, stack, existing)
            } else stack
        }
    })
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

fun <T> T.matchesAny(vararg any: T): Boolean {
    for (t in any) {
        if (this == t) {
            return true
        }
    }

    return false
}

inline fun <T> Iterable<T>.anyMatch(test: (T) -> Boolean): Boolean {
    forEach {
        if (test(it)) {
            return true
        }
    }

    return false
}

inline fun <T> janyMatch(iterable: Iterable<T>, test: (T) -> Boolean): Boolean {
    return iterable.anyMatch(test)
}

fun setItemModel(item: Item, meta: Int, string: String = item.registryName!!.toString()) {
    runOnClient {
        ClientEvents.models.add(Triple(item, meta, string))
    }
}

fun setItemName(item: Item, registryName: String, translationKey: String = "${FutureMC.ID}.$registryName") {
    item.translationKey = translationKey
    item.registryName = ResourceLocation(FutureMC.ID, registryName)
}

fun setBuiltinRenderer(builtin: ModeledItem.Builtin) {
    // required because Kotlin's `where`
    // clause is broken with pre-1.4 type inference
    builtin as Item

    runOnClient {
        builtin.tileEntityItemStackRenderer = object : TileEntityItemStackRenderer() {
            override fun renderByItem(itemStackIn: ItemStack, partialTicks: Float) {
                builtin.render(itemStackIn, partialTicks)
            }
        }
    }
}

fun <T : Item> T.setItemGroup(group: CreativeTabs): T {
    creativeTab = if (FConfig.useVanillaCreativeTabs) group else FutureMC.GROUP
    return this
}

@Suppress("FunctionName")
fun TODO() = false

fun <T> getDoubleOrDefault(map: Object2DoubleMap<T>, key: T, default: Double): Double {
    return if (map.containsKey(key)) {
        map.getDouble(key)
    } else default
}