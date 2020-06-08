@file:Suppress("PackageDirectoryMismatch")

package net.minecraft.entity

import net.minecraft.block.BlockFence
import net.minecraft.block.BlockFenceGate
import net.minecraft.block.BlockWall
import net.minecraft.block.material.Material
import net.minecraft.crash.CrashReport
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumFacing
import net.minecraft.util.ReportedException
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.WorldServer
import net.minecraftforge.event.ForgeEventFactory
import java.util.*
import kotlin.math.abs
import kotlin.math.floor

object FireproofItemLogic {
    fun update(entity: EntityItem) {
        if (entity.item.isEmpty) {
            entity.setDead()
        } else {
            if (!entity.world.isRemote) {
                entity.setFlag(6, entity.isGlowing)
            }

            entity.extinguish()

            // onEntityUpdate
            customOnEntityUpdate(entity)

            if (entity.pickupDelay > 0 && entity.pickupDelay != 32767) {
                --entity.pickupDelay
            }

            entity.prevPosX = entity.posX
            entity.prevPosY = entity.posY
            entity.prevPosZ = entity.posZ
            val d0 = entity.motionX
            val d1 = entity.motionY
            val d2 = entity.motionZ
            if (entity.isInsideOfMaterial(Material.LAVA)) {
                floatInLava(entity)
            } else if (!entity.hasNoGravity()) {
                entity.motionY -= 0.03999999910593033
            }

            entity.noClip = if (entity.world.isRemote) {
                false
            } else {
                entity.pushOutOfBlocks(
                    entity.posX,
                    (entity.entityBoundingBox.minY + entity.entityBoundingBox.maxY) / 2.0,
                    entity.posZ
                )
            }

            entity.customEntityMove(MoverType.SELF, entity.motionX, entity.motionY, entity.motionZ)
            val flag =
                entity.prevPosX.toInt() != entity.posX.toInt() || entity.prevPosY.toInt() != entity.posY.toInt() || entity.prevPosZ.toInt() != entity.posZ.toInt()

            if (flag || entity.ticksExisted % 25 == 0) {
                if (!entity.world.isRemote) {
                    entity.searchForOtherItemsNearby()
                }
            }

            var f = 0.98f

            if (entity.onGround) {
                val underPos = BlockPos(floor(entity.posX), floor(entity.entityBoundingBox.minY) - 1, floor(
                    entity.posZ
                ))
                val underState = entity.world.getBlockState(underPos)
                f = underState.block.getSlipperiness(underState, entity.world, underPos, entity) * 0.98f
            }

            entity.motionX *= f.toDouble()
            entity.motionY *= 0.9800000190734863
            entity.motionZ *= f.toDouble()

            if (entity.onGround) {
                entity.motionY *= -0.5
            }

            if (entity.age != -32768) {
                ++entity.age
            }

            entity.handleWaterMovement()

            if (!entity.world.isRemote) {
                val d3 = entity.motionX - d0
                val d4 = entity.motionY - d1
                val d5 = entity.motionZ - d2
                val d6 = d3 * d3 + d4 * d4 + d5 * d5
                if (d6 > 0.01) {
                    entity.isAirBorne = true
                }
            }

            val item = entity.item

            if (!entity.world.isRemote && entity.age >= entity.lifespan) {
                val hook = ForgeEventFactory.onItemExpire(entity, item)
                if (hook < 0) entity.setDead() else entity.lifespan += hook
            }
            if (item.isEmpty) {
                entity.setDead()
            }
        }
    }

    private fun floatInLava(entity: EntityItem) {
        entity.motionX *= 0.95
        entity.motionY += (if (entity.motionY < 0.06) 5.0e-4 else 0.0)
        entity.motionZ *= 0.95
    }

    private fun customOnEntityUpdate(entity: EntityItem) {
        entity.world.profiler.startSection("entityBaseTick")
        if (entity.isRiding && entity.ridingEntity!!.isDead) {
            entity.dismountRidingEntity()
        }

        if (entity.rideCooldown > 0) {
            --entity.rideCooldown
        }

        entity.prevDistanceWalkedModified = entity.distanceWalkedModified
        entity.prevPosX = entity.posX
        entity.prevPosY = entity.posY
        entity.prevPosZ = entity.posZ
        entity.prevRotationPitch = entity.rotationPitch
        entity.prevRotationYaw = entity.rotationYaw

        updatePortal(entity)

        entity.spawnRunningParticles()
        entity.handleWaterMovement()

        if (entity.posY < -64.0) {
            entity.outOfWorld()
        }

        entity.firstUpdate = false
        entity.world.profiler.endSection()
    }

    private fun updatePortal(entity: EntityItem) {
        if (entity.world is WorldServer) {
            if (entity.inPortal) {
                val server = entity.world.minecraftServer
                val i = entity.maxInPortalTime

                if (server!!.allowNether && !entity.isRiding && entity.portalCounter++ >= i) {
                    entity.world.profiler.startSection("portal")
                    entity.portalCounter = i
                    entity.timeUntilPortal = entity.portalCooldown
                    val j = if (entity.world.provider.dimensionType.id == -1) {
                        0
                    } else {
                        -1
                    }
                    entity.changeDimension(j)
                    entity.inPortal = false
                }
            } else {
                if (entity.portalCounter > 0) {
                    entity.portalCounter -= 4
                }
                if (entity.portalCounter < 0) {
                    entity.portalCounter = 0
                }
            }
            entity.decrementTimeUntilPortal()
            entity.world.profiler.endSection()
        }
    }

    @Suppress("DuplicatedCode")
    private fun EntityItem.customEntityMove(type: MoverType, motionX: Double, motionY: Double, motionZ: Double) {
        var x = motionX
        var y = motionY
        var z = motionZ

        if (noClip) {
            entityBoundingBox = entityBoundingBox.offset(motionX, motionY, motionZ)
            resetPositionToBB()
        } else {
            if (type == MoverType.PISTON) {
                val i = world.totalWorldTime

                if (i != pistonDeltasGameTime) {
                    Arrays.fill(pistonDeltas, 0.0)
                    pistonDeltasGameTime = i
                }

                if (x != 0.0) {
                    val j = EnumFacing.Axis.X.ordinal
                    val d0 =
                        MathHelper.clamp(x + pistonDeltas[j], -0.51, 0.51)
                    x = d0 - pistonDeltas[j]
                    pistonDeltas[j] = d0
                    if (abs(x) <= 9.999999747378752E-6) {
                        return
                    }
                } else if (y != 0.0) {
                    val l4 = EnumFacing.Axis.Y.ordinal
                    val d12 =
                        MathHelper.clamp(y + pistonDeltas[l4], -0.51, 0.51)
                    y = d12 - pistonDeltas[l4]
                    pistonDeltas[l4] = d12
                    if (abs(y) <= 9.999999747378752E-6) {
                        return
                    }
                } else {
                    if (z == 0.0) {
                        return
                    }
                    val i5 = EnumFacing.Axis.Z.ordinal
                    val d13 =
                        MathHelper.clamp(z + pistonDeltas[i5], -0.51, 0.51)
                    z = d13 - pistonDeltas[i5]
                    pistonDeltas[i5] = d13
                    if (abs(z) <= 9.999999747378752E-6) {
                        return
                    }
                }
            }
            world.profiler.startSection("move")

            if (isInWeb) {
                isInWeb = false
                x *= 0.25
                y *= 0.05000000074505806
                z *= 0.25
                this.motionX = 0.0
                this.motionY = 0.0
                this.motionZ = 0.0
            }
            var d2 = x
            val d3 = y
            var d4 = z
            if ((type == MoverType.SELF || type == MoverType.PLAYER) && onGround && isSneaking && this is EntityPlayer) {
                while (x != 0.0 && world.getCollisionBoxes(
                        this,
                        getEntityBoundingBox().offset(x, (-stepHeight).toDouble(), 0.0)
                    ).isEmpty()
                ) {
                    if (x < 0.05 && x >= -0.05) {
                        x = 0.0
                    } else if (x > 0.0) {
                        x -= 0.05
                    } else {
                        x += 0.05
                    }
                    d2 = x
                }
                while (z != 0.0 && world.getCollisionBoxes(
                        this,
                        getEntityBoundingBox().offset(0.0, (-stepHeight).toDouble(), z)
                    ).isEmpty()
                ) {
                    if (z < 0.05 && z >= -0.05) {
                        z = 0.0
                    } else if (z > 0.0) {
                        z -= 0.05
                    } else {
                        z += 0.05
                    }
                    d4 = z
                }
                while (x != 0.0 && z != 0.0 && world.getCollisionBoxes(
                        this,
                        getEntityBoundingBox().offset(x, (-stepHeight).toDouble(), z)
                    ).isEmpty()
                ) {
                    if (x < 0.05 && x >= -0.05) {
                        x = 0.0
                    } else if (x > 0.0) {
                        x -= 0.05
                    } else {
                        x += 0.05
                    }
                    d2 = x
                    if (z < 0.05 && z >= -0.05) {
                        z = 0.0
                    } else if (z > 0.0) {
                        z -= 0.05
                    } else {
                        z += 0.05
                    }
                    d4 = z
                }
            }
            val list1 =
                world.getCollisionBoxes(this, entityBoundingBox.expand(x, y, z))
            val axisalignedbb = entityBoundingBox
            if (y != 0.0) {
                var k = 0
                val l = list1.size
                while (k < l) {
                    y = list1[k].calculateYOffset(entityBoundingBox, y)
                    ++k
                }
                entityBoundingBox = entityBoundingBox.offset(0.0, y, 0.0)
            }
            if (x != 0.0) {
                var j5 = 0
                val l5 = list1.size
                while (j5 < l5) {
                    x = list1[j5].calculateXOffset(entityBoundingBox, x)
                    ++j5
                }
                if (x != 0.0) {
                    entityBoundingBox = entityBoundingBox.offset(x, 0.0, 0.0)
                }
            }
            if (z != 0.0) {
                var k5 = 0
                val i6 = list1.size
                while (k5 < i6) {
                    z = list1[k5].calculateZOffset(entityBoundingBox, z)
                    ++k5
                }
                if (z != 0.0) {
                    entityBoundingBox = entityBoundingBox.offset(0.0, 0.0, z)
                }
            }
            val flag = onGround || d3 != y && d3 < 0.0
            if (stepHeight > 0.0f && flag && (d2 != x || d4 != z)) {
                val d14 = x
                val d6 = y
                val d7 = z
                val axisalignedbb1 = entityBoundingBox
                entityBoundingBox = axisalignedbb
                y = stepHeight.toDouble()
                val list =
                    world.getCollisionBoxes(this, entityBoundingBox.expand(d2, y, d4))
                var axisalignedbb2 = entityBoundingBox
                val axisalignedbb3 = axisalignedbb2.expand(d2, 0.0, d4)
                var d8: Double = y
                var j1 = 0
                val k1 = list.size
                while (j1 < k1) {
                    d8 = list[j1].calculateYOffset(axisalignedbb3, d8)
                    ++j1
                }
                axisalignedbb2 = axisalignedbb2.offset(0.0, d8, 0.0)
                var d18 = d2
                var l1 = 0
                val i2 = list.size
                while (l1 < i2) {
                    d18 = list[l1].calculateXOffset(axisalignedbb2, d18)
                    ++l1
                }
                axisalignedbb2 = axisalignedbb2.offset(d18, 0.0, 0.0)
                var d19 = d4
                var j2 = 0
                val k2 = list.size
                while (j2 < k2) {
                    d19 = list[j2].calculateZOffset(axisalignedbb2, d19)
                    ++j2
                }
                axisalignedbb2 = axisalignedbb2.offset(0.0, 0.0, d19)
                var axisalignedbb4 = entityBoundingBox
                var d20: Double = y
                var l2 = 0
                val i3 = list.size
                while (l2 < i3) {
                    d20 = list[l2].calculateYOffset(axisalignedbb4, d20)
                    ++l2
                }
                axisalignedbb4 = axisalignedbb4.offset(0.0, d20, 0.0)
                var d21 = d2
                var j3 = 0
                val k3 = list.size
                while (j3 < k3) {
                    d21 = list[j3].calculateXOffset(axisalignedbb4, d21)
                    ++j3
                }
                axisalignedbb4 = axisalignedbb4.offset(d21, 0.0, 0.0)
                var d22 = d4
                var l3 = 0
                val i4 = list.size
                while (l3 < i4) {
                    d22 = list[l3].calculateZOffset(axisalignedbb4, d22)
                    ++l3
                }
                axisalignedbb4 = axisalignedbb4.offset(0.0, 0.0, d22)
                val d23 = d18 * d18 + d19 * d19
                val d9 = d21 * d21 + d22 * d22
                if (d23 > d9) {
                    x = d18
                    z = d19
                    y = -d8
                    entityBoundingBox = axisalignedbb2
                } else {
                    x = d21
                    z = d22
                    y = -d20
                    entityBoundingBox = axisalignedbb4
                }
                var j4 = 0
                val k4 = list.size
                while (j4 < k4) {
                    y = list[j4].calculateYOffset(entityBoundingBox, y)
                    ++j4
                }
                entityBoundingBox = entityBoundingBox.offset(0.0, y, 0.0)
                if (d14 * d14 + d7 * d7 >= x * x + z * z) {
                    x = d14
                    y = d6
                    z = d7
                    entityBoundingBox = axisalignedbb1
                }
            }
            world.profiler.endSection()
            world.profiler.startSection("rest")
            resetPositionToBB()
            collidedHorizontally = d2 != x || d4 != z
            collidedVertically = d3 != y
            onGround = collidedVertically && d3 < 0.0
            collided = collidedHorizontally || collidedVertically
            val j6 = MathHelper.floor(posX)
            val i1 = MathHelper.floor(posY - 0.20000000298023224)
            val k6 = MathHelper.floor(posZ)
            var blockpos = BlockPos(j6, i1, k6)
            var iblockstate = world.getBlockState(blockpos)
            if (iblockstate.material === Material.AIR) {
                val blockpos1 = blockpos.down()
                val iblockstate1 = world.getBlockState(blockpos1)
                val block1 = iblockstate1.block
                if (block1 is BlockFence || block1 is BlockWall || block1 is BlockFenceGate) {
                    iblockstate = iblockstate1
                    blockpos = blockpos1
                }
            }
            updateFallState(y, onGround, iblockstate, blockpos)
            if (d2 != x) {
                this.motionX = 0.0
            }
            if (d4 != z) {
                this.motionZ = 0.0
            }
            val block = iblockstate.block
            if (d3 != y) {
                block.onLanded(world, this)
            }
            try {
                doBlockCollisions()
            } catch (throwable: Throwable) {
                val crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision")
                val crashreportcategory =
                    crashreport.makeCategory("Entity being checked for collision")
                addEntityCrashInfo(crashreportcategory)
                throw ReportedException(crashreport)
            }
            val flag1 = isWet

            if (flag1 && isBurning) {
                playSound(
                    SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                    0.7f,
                    1.6f + (rand.nextFloat() - rand.nextFloat()) * 0.4f
                )
            }
            world.profiler.endSection()
        }
    }
}
