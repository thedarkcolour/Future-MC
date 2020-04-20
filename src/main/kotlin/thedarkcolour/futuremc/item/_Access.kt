package net.minecraft.entity

import net.minecraft.block.BlockState
import net.minecraft.crash.CrashReport
import net.minecraft.crash.ReportedException
import net.minecraft.entity.MoverType
import net.minecraft.entity.item.ItemEntity
import net.minecraft.tags.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraftforge.event.ForgeEventFactory

object _Access : ItemEntity(null, 0.0, 0.0, 0.0) {
    fun update(entity: ItemEntity) {
        if (entity.item.isEmpty) {
            entity.remove()
        } else {
            if (!entity.world.isRemote) {
                entity.setFlag(6, entity.isGlowing)
            }

            entity.inLava = false
            entity.extinguish()

            // baseTick
            entity.world.profiler.startSection("entityBaseTick")
            if (entity.isPassenger && !entity.ridingEntity!!.isAlive) {
                entity.stopRiding()
            }

            if (entity.rideCooldown > 0) {
                --entity.rideCooldown
            }

            entity.prevDistanceWalkedModified = entity.distanceWalkedModified
            entity.prevRotationPitch = entity.rotationPitch
            entity.prevRotationYaw = entity.rotationYaw
            entity.updatePortal()
            entity.handleWaterMovement()

            if (entity.y < -64.0) {
                entity.outOfWorld()
            }

            if (!entity.world.isRemote) {
                entity.setFlag(0, entity.fireTimer > 0)
            }

            entity.firstUpdate = false
            entity.world.profiler.endSection()
            // baseTick end

            if (entity.pickupDelay > 0 && entity.pickupDelay != 32767) {
                --entity.pickupDelay
            }
            entity.prevPosX = entity.x
            entity.prevPosY = entity.y
            entity.prevPosZ = entity.z
            val vec3d = entity.motion
            if (entity.areEyesInFluid(FluidTags.WATER)) {
                entity.applyFloatMotion()
            } else if (entity.areEyesInFluid(FluidTags.LAVA)) {
                entity.floatInLava()
            } else if (!entity.hasNoGravity()) {
                entity.motion = entity.motion.add(0.0, -0.04, 0.0)
            }
            if (entity.world.isRemote) {
                entity.noClip = false
            } else {
                entity.noClip = !entity.world.doesNotCollide(entity)
                if (entity.noClip) {
                    entity.pushOutOfBlocks(entity.x, (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0, entity.z)
                }
            }
            if (!entity.onGround || horizontalMag(entity.motion) > 1.0E-5f.toDouble() || (entity.ticksExisted + entity.entityId) % 4 == 0) {
                // move
                if (entity.noClip) {
                    entity.boundingBox = entity.boundingBox.offset(entity.motion)
                    entity.resetPositionToBB()
                } else {
                    entity.world.profiler.startSection("move")
                    if (entity.motionMultiplier.lengthSquared() > 1.0E-7) {
                        entity.motion = entity.motion.mul(entity.motionMultiplier)
                        entity.motionMultiplier = Vec3d.ZERO
                        entity.motion = Vec3d.ZERO
                    }
                    entity.motion = entity.adjustMovementForSneaking(entity.motion, MoverType.SELF)
                    val vec3d0: Vec3d = entity.getAllowedMovement(entity.motion)
                    if (vec3d0.lengthSquared() > 1.0E-7) {
                        entity.boundingBox = entity.boundingBox.offset(vec3d0)
                        entity.resetPositionToBB()
                    }
                    entity.world.profiler.endSection()
                    entity.world.profiler.startSection("rest")
                    entity.collidedHorizontally = !MathHelper.epsilonEquals(entity.motion.x, vec3d0.x) || !MathHelper.epsilonEquals(entity.motion.z, vec3d0.z)
                    entity.collidedVertically = entity.motion.y != vec3d0.y
                    entity.onGround = entity.collidedVertically && entity.motion.y < 0.0
                    entity.collided = entity.collidedHorizontally || entity.collidedVertically
                    val blockpos: BlockPos = entity.landingPos
                    val blockstate: BlockState = entity.world.getBlockState(blockpos)
                    entity.updateFallState(vec3d0.y, entity.onGround, blockstate, blockpos)
                    val vec3d1: Vec3d = entity.motion
                    if (entity.motion.x != vec3d0.x) {
                        entity.setMotion(0.0, vec3d1.y, vec3d1.z)
                    }
                    if (entity.motion.z != vec3d0.z) {
                        entity.setMotion(vec3d1.x, vec3d1.y, 0.0)
                    }
                    val block = blockstate.block
                    if (entity.motion.y != vec3d0.y) {
                        block.onLanded(entity.world, entity)
                    }
                    if (entity.onGround && !entity.bypassesSteppingEffects()) {
                        block.onEntityWalk(entity.world, blockpos, entity)
                    }
                    try {
                        entity.doBlockCollisions()
                        entity.inLava = false
                    } catch (throwable: Throwable) {
                        val crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision")
                        val crashreportcategory = crashreport.makeCategory("Entity being checked for collision")
                        entity.fillCrashReport(crashreportcategory)
                        throw ReportedException(crashreport)
                    }

                    val velocityMultipler = entity.velocityMultiplier.toDouble()
                    entity.motion = entity.motion.mul(velocityMultipler, 1.0, velocityMultipler)

                    if (entity.fireTimer <= 0) {
                        entity.fireTimer = -entity.fireImmuneTicks
                    }
                    entity.world.profiler.endSection()
                }
                // move end
                var f = 0.98f
                if (entity.onGround) {
                    val pos = BlockPos(entity.x, entity.y - 1.0, entity.z)
                    f = entity.world.getBlockState(pos).getSlipperiness(entity.world, pos, entity) * 0.98f
                }
                entity.motion = entity.motion.mul(f.toDouble(), 0.98, f.toDouble())
                if (entity.onGround) {
                    entity.motion = entity.motion.mul(1.0, -0.5, 1.0)
                }
            }
            val flag = MathHelper.floor(entity.prevPosX) != MathHelper.floor(entity.x) || MathHelper.floor(entity.prevPosY) != MathHelper.floor(entity.y) || MathHelper.floor(entity.prevPosZ) != MathHelper.floor(entity.z)
            val i = if (flag) 2 else 40
            if (entity.ticksExisted % i == 0) {
                if (!entity.world.isRemote && entity.func_213857_z()) {
                    entity.searchForOtherItemsNearby()
                }
            }
            if (entity.age != -32768) {
                ++entity.age
            }
            entity.isAirBorne = entity.isAirBorne || entity.handleWaterMovement()
            if (!entity.world.isRemote) {
                val d0 = entity.motion.subtract(vec3d).lengthSquared()
                if (d0 > 0.01) {
                    entity.isAirBorne = true
                }
            }
            val item = entity.item
            if (!entity.world.isRemote && entity.age >= entity.lifespan) {
                val hook = ForgeEventFactory.onItemExpire(entity, item)
                if (hook < 0) entity.remove() else entity.lifespan += hook
            }
            if (item.isEmpty) {
                entity.remove()
            }
        }
    }

    private fun ItemEntity.floatInLava() {
        val vec3d = motion
        setMotion(vec3d.x * 0.95, vec3d.y + (if (vec3d.y < 0.06) 5.0E-4 else 0.0), vec3d.z * 0.95)
    }
}