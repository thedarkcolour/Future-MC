package thedarkcolour.futuremc.client.render

import it.unimi.dsi.fastutil.doubles.DoubleList
import net.minecraft.util.EnumFacing.Axis

abstract class VoxelShape(private val part: Part) {
    private var projectionCache: Array<VoxelShape>? = null

    fun getStart(axis: Axis): Double {
        val i = part.getStart(axis)
        return if (i >= part.getSize(axis)) Double.POSITIVE_INFINITY else getValueUnchecked(axis, i)
    }

    fun getEnd(axis: Axis): Double {
        val i = part.getEnd(axis)
        return if (i <= 0) Double.NEGATIVE_INFINITY else getValueUnchecked(axis, i)
    }

    protected fun getValueUnchecked(axis: Axis, i: Int): Double {
        return getValues(axis).getDouble(i)
    }

    abstract fun getValues(axis: Axis): DoubleList

    fun isEmpty(): Boolean {
        return part.isEmpty()
    }

    fun forEachEdge(boxAction: DoubleBoxAction) {
        val x = getValues(Axis.X)
        val y = getValues(Axis.Y)
        val z = getValues(Axis.Z)
        part.forEachBox(true,
            IntBoxAction { minX, minY, minZ, maxX, maxY, maxZ ->
                boxAction(
                    x.getDouble(minX),
                    y.getDouble(minY),
                    z.getDouble(minZ),
                    x.getDouble(maxX),
                    y.getDouble(maxY),
                    z.getDouble(maxZ)
                )
            }
        )
    }

    abstract class Part(private val x: Int, private val y: Int, private val z: Int) {
        abstract fun getStart(axis: Axis): Int

        abstract fun getEnd(axis: Axis): Int

        fun getSize(axis: Axis): Int {
            return when (axis) {
                Axis.X -> x
                Axis.Y -> y
                Axis.Z -> z
            }
        }

        fun isEmpty(): Boolean {
            for (axis in Axis.values()) {
                if (getStart(axis) >= getEnd(axis)) {
                    return true
                }
            }
            return false
        }

        fun forEachBox(b: Boolean, boxAction: IntBoxAction) {
            TODO("not implemented")
        }
    }
}