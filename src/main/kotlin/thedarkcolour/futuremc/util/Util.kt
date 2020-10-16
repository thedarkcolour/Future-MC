package thedarkcolour.futuremc.util

import com.google.common.collect.AbstractIterator
import com.mojang.datafixers.OptionalDynamic
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import sun.reflect.ConstructorAccessor
import java.lang.reflect.Constructor
import kotlin.math.abs

/**
 * Hides an UNCHECKED_CAST warning.
 *
 * @param obj The object to perform the cast on
 * @param T The unchecked type to cast to
 */
// todo replace with a suppress warnings
@Suppress("UNCHECKED_CAST")
fun <T> cast(obj: Any): T {
    return obj as T
}

fun BlockPos.Mutable.clampAxis(axis: Direction.Axis, min: Int, max: Int): BlockPos.Mutable {
    return when (axis) {
        Direction.Axis.X -> setPos(MathHelper.clamp(x, min, max), y, z)
        Direction.Axis.Y -> setPos(x, MathHelper.clamp(y, min, max), z)
        Direction.Axis.Z -> setPos(x, y, MathHelper.clamp(z, min, max))
        else -> throw IllegalStateException("Unable to clamp axis")
    }
}

/**
 * Deserializes a `BlockState` from a dynamic.
 */
fun OptionalDynamic<*>.deserializeBlockState(): BlockState {
    return map { BlockState.deserialize(it) }.orElse(Blocks.AIR.defaultState)
}

fun iterateOutwards(center: BlockPos, xRange: Int, yRange: Int, zRange: Int): Iterable<BlockPos> {
    val i = xRange + yRange + zRange
    val j = center.x
    val k = center.y
    val l = center.z

    return Iterable {
        object : AbstractIterator<BlockPos>() {
            private val cursor = BlockPos.Mutable()
            private var currentDepth = 0
            private var maxX = 0
            private var maxY = 0
            private var x = 0
            private var y = 0
            private var zMirror = false

            override fun computeNext(): BlockPos {
                return if (zMirror) {
                    zMirror = false
                    cursor.z = l - (cursor.z - l)
                    cursor
                } else {
                    var blockPos: BlockPos.Mutable?
                    blockPos = null
                    while (blockPos == null) {
                        if (y > maxY) {
                            ++x
                            if (x > maxX) {
                                ++currentDepth
                                if (currentDepth > i) {
                                    return endOfData()
                                }
                                maxX = k.coerceAtMost(currentDepth)
                                x = -maxX
                            }
                            maxY = l.coerceAtMost(currentDepth - abs(x))
                            y = -maxY
                        }
                        val ix = x
                        val jx = y
                        val kx = currentDepth - abs(ix) - abs(jx)
                        if (kx <= zRange) {
                            zMirror = kx != 0
                            blockPos = cursor.setPos(j + ix, k + jx, l + kx)
                        }
                        ++y
                    }
                    blockPos
                }
            }
        }
    }
}

// todo remove

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
        constructor.isAccessible = true

        constructorAccessorField.get(constructor) as ConstructorAccessor?
                ?: acquireAccessorMethod(constructor) as ConstructorAccessor
    }

    @Suppress("PlatformExtensionReceiverOfInline")
    enumClass.declaredConstructors.forEach {
        println(it.parameterTypes.contentToString())
    }

    var enum: T? = null
    for (constructor in constructors) {
        try {
            println(constructor)
            // replace 0 with the index when actually adding to enum constants
            enum = cast(constructor.newInstance(arrayOf(name, 0, *args)))
            break
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
    return enum ?: throw IllegalArgumentException("No matching enum constructor found for arguments: ${args.contentToString()}")
}

private val constructorAccessorField = ObfuscationReflectionHelper.findField(Constructor::class.java, "constructorAccessor")
private val acquireAccessorMethod = ObfuscationReflectionHelper.findMethod(Constructor::class.java, "acquireConstructorAccessor")

fun makeCuboidShape(
    minX: Int, minY: Int, minZ: Int,
    maxX: Int, maxY: Int, maxZ: Int,
): VoxelShape {
   return Block.makeCuboidShape(
       minX.toDouble(),
       minY.toDouble(),
       minZ.toDouble(),
       maxX.toDouble(),
       maxY.toDouble(),
       maxZ.toDouble(),
   )
}