package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.block.material.Material
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockPos.MutableBlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import thedarkcolour.core.util.isAir
import thedarkcolour.core.util.isInRange
import thedarkcolour.futuremc.entity.bee.BeeEntity
import java.util.*
import java.util.function.ToDoubleFunction
import kotlin.math.*

class WanderAI(private val bee: BeeEntity) : EntityAIBase() {
    init {
        mutexBits = 0x1
    }

    override fun shouldExecute(): Boolean {
        return bee.navigator.noPath() && bee.rng.nextInt(10) == 0
    }

    override fun shouldContinueExecuting(): Boolean {
        return !bee.navigator.noPath()
    }

    override fun startExecuting() {
        val vec3d = getRandomLocation()

        if (vec3d != null) {
            bee.navigator.tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0)
        }
    }

    private fun getRandomLocation(): Vec3d? {
        val vec3d = if (bee.isHiveValid() && !bee.isWithinDistance(bee.hivePos, 40)) {
            val vec3d1 = Vec3d(bee.hivePos!!)
            vec3d1.subtract(bee.positionVector).normalize()
        } else {
            bee.getLook(0.0f)
        }

        val vec3d2 = findAirTarget(bee, 8, 7, vec3d, PI / 2.0f, 2, 1)
        return vec3d2 ?: findGroundTarget(bee, 8, 4, -2, vec3d, PI / 2.0f)
    }

    companion object {
        @JvmStatic
        fun findAirTarget(bee: EntityCreature, i: Int, i1: Int, vec3d: Vec3d, d: Double, i2: Int, i3: Int): Vec3d? {
            return findTarget(bee, i, i1, 0, vec3d, false, d, getBlockPathWeightD(bee), true, i2, i3, true)
        }

        @JvmStatic
        fun findGroundTarget(bee: EntityCreature, i: Int, i1: Int, i2: Int, vec3d: Vec3d, d: Double): Vec3d? {
            return findTarget(bee, i, i1, i2, vec3d, true, d, getBlockPathWeightD(bee), true, 0, 0, false)
        }

        // used in GoToBlockAI.kt
        @JvmStatic
        fun findGroundTargetTowards(bee: EntityCreature, xz: Int, y: Int, i: Int, vector: Vec3d, d: Double): Vec3d? {
            val vec3d = vector.subtract(bee.posX, bee.posY, bee.posZ)
            return findTarget(bee, xz, y, i, vec3d, false, d, getBlockPathWeightD(bee), true, 0, 0, false)
        }

        private fun findTarget(
            bee: EntityCreature,
            i0: Int,
            i1: Int,
            i4: Int,
            vec3d: Vec3d,
            b: Boolean,
            d: Double,
            toDoubleFunction: ToDoubleFunction<BlockPos>,
            b1: Boolean,
            i2: Int,
            i3: Int,
            b2: Boolean
        ): Vec3d? {
            val navigator = bee.navigator
            val rand = bee.rng
            val flag = if (bee.maximumHomeDistance != -1.0f) {
                // see Extensions.kt
                bee.homePosition.isInRange(bee.positionVector, bee.maximumHomeDistance + i0 + 1.0)
            } else {
                false
            }

            var flag1 = false
            var d0 = Double.NEGATIVE_INFINITY
            var pos = BlockPos(bee)

            for (i in 0..9) {
                val pos1 = getRandomOffset(rand, i0, i1, i4, vec3d, d)

                if (pos1 != null) {
                    var j = pos1.x
                    val k = pos1.y
                    var l = pos1.z

                    if (bee.maximumHomeDistance != -1.0f && i0 > 1) {
                        val pos2 = bee.homePosition

                        if (bee.posX > pos2.x) {
                            j -= rand.nextInt(i0 / 2)
                        } else {
                            j += rand.nextInt(i0 / 2)
                        }

                        if (bee.posZ > pos2.z) {
                            l -= rand.nextInt(i0 / 2)
                        } else {
                            l += rand.nextInt(i0 / 2)
                        }
                    }

                    var pos3 = BlockPos(j + bee.posX, k + bee.posY, l + bee.posZ)

                    if (pos3.y in 0..bee.world.height && (!flag || bee.isWithinHomeDistanceFromPosition(pos3)) && (!b2 || navigator.canEntityStandOnPos(pos3))) {
                        if (b1) {
                            pos3 = findValidPositionAbove(pos3, rand.nextInt(i2 + 1) + i3, bee.world.height) { pos4: BlockPos ->
                                bee.world.getBlockState(pos4).material.isSolid
                            }
                        }

                        if (b || bee.world.getBlockState(pos3).material != Material.WATER) {
                            val node = navigator.nodeProcessor.getPathNodeType(bee.world, pos3.x, pos3.y, pos3.z)

                            if (bee.getPathPriority(node) == 0.0f) {
                                val d1 = toDoubleFunction.applyAsDouble(pos3)

                                if (d1 > d0) {
                                    d0 = d1
                                    pos = pos3
                                    flag1 = true
                                }
                            }
                        }
                    }
                }
            }

            return if (flag1) Vec3d(pos) else null
        }

        private fun getRandomOffset(
            rand: Random,
            i0: Int,
            i1: Int,
            i2: Int,
            vec3d: Vec3d?,
            d: Double
        ): BlockPos? {
            return if (vec3d != null && d < Math.PI) {
                val d3 = MathHelper.atan2(vec3d.z, vec3d.x) - (Math.PI.toFloat() / 2f).toDouble()
                val d4 = d3 + (2.0f * rand.nextFloat() - 1.0f).toDouble() * d
                val d0 = sqrt(rand.nextDouble()) * MathHelper.SQRT_2.toDouble() * i0.toDouble()
                val d1 = -d0 * sin(d4)
                val d2 = d0 * cos(d4)
                if (abs(d1) <= i0.toDouble() && abs(d2) <= i0.toDouble()) {
                    val l = rand.nextInt(2 * i1 + 1) - i1 + i2
                    BlockPos(d1, l.toDouble(), d2)
                } else {
                    null
                }
            } else {
                val i = rand.nextInt(2 * i0 + 1) - i0
                val j = rand.nextInt(2 * i1 + 1) - i1 + i2
                val k = rand.nextInt(2 * i0 + 1) - i0
                BlockPos(i, j, k)
            }
        }

        private fun findValidPositionAbove(
            previous: BlockPos,
            aboveSolidAmount: Int,
            heightLimit: Int,
            condition: (BlockPos) -> Boolean
        ): BlockPos {
            assert(aboveSolidAmount < 0) { "aboveSolidAmount was $aboveSolidAmount, expected >= 0" }

            if (!condition(previous)) {
                return previous
            } else {
                val pos = MutableBlockPos(previous).move(EnumFacing.UP)

                while (pos.y < heightLimit && condition(pos)) {
                    pos.move(EnumFacing.UP)
                }

                val pos1 = MutableBlockPos(pos)
                val pos2 = MutableBlockPos(pos1)

                // as long as we are in the height bound
                while (pos1.y < heightLimit && pos1.y - pos.y < aboveSolidAmount) {
                    // test the above block
                    pos2.move(EnumFacing.UP)
                    if (condition(pos2)) {
                        // if the check succeeds then return the real we already have
                        // because we don't change it until AFTER the check fails
                        // instead of moving the position back down
                        break
                    }
                    // only increment the real position if the check was not successful
                    pos1.setPos(pos2)
                }

                // enjoy my commentary.
                return pos1.toImmutable()
            }
        }

        /**
         * [ToDoubleFunction] used because extension functions don't work in member references
         */
        private fun getBlockPathWeightD(entity: EntityLivingBase): ToDoubleFunction<BlockPos> {
            return ToDoubleFunction { pos ->
                if (entity.world.isAir(pos)) 10.0 else 0.0
            }
        }
    }
}