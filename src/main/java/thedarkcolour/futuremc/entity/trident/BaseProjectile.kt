package thedarkcolour.futuremc.entity.trident

import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData
import thedarkcolour.core.util.lerp
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

abstract class BaseProjectile(worldIn: World) : Entity(worldIn), IEntityAdditionalSpawnData {
    private var ownerUUID: UUID? = null
    private var ownerID = 0
    private var leftOwner = false

    init {
        setSize(0.5f, 0.5f)
    }

    open fun setOwner(entity: Entity?) {
        if (entity != null) {
            ownerUUID = entity.uniqueID
            ownerID = entity.entityId
        }
    }

    open fun getOwner(): Entity? {
        var world = world

        return if (ownerUUID != null && world is WorldServer) {
            world.getEntityFromUuid(ownerUUID)
        } else {
            if (ownerID != 0) world.getEntityByID(ownerID) else null
        }
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        if (ownerUUID != null) {
            compound.setUniqueId("Owner", ownerUUID)
        }

        if (leftOwner) {
            compound.setBoolean("LeftOwner", true)
        }
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        if (compound.hasUniqueId("Owner")) {
            ownerUUID = compound.getUniqueId("Owner")
        }

        leftOwner = compound.getBoolean("LeftOwner")
    }

    override fun onUpdate() {
        if (!leftOwner) {
            leftOwner = checkLeftOwner()
        }
        super.onUpdate()
    }

    private fun checkLeftOwner(): Boolean {
        val entity = getOwner()

        if (entity != null) {
            for (other in world.getEntitiesInAABBexcluding(this, entityBoundingBox.expand(motionX, motionY, motionZ).grow(1.0)) { entity ->
                (entity !is EntityPlayerMP || !entity.isSpectator) && entity?.canBeCollidedWith() == true
            }) {
                if (other.lowestRidingEntity == entity.lowestRidingEntity) {
                    return false
                }
            }
        }

        return true
    }

    open fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) {
        val length = sqrt(x * x + y * y + z * z)
        motionX = ((x / length) + rand.nextGaussian() + 0.0075f + inaccuracy) * velocity
        motionY = ((y / length) + rand.nextGaussian() + 0.0075f + inaccuracy) * velocity
        motionZ = ((z / length) + rand.nextGaussian() + 0.0075f + inaccuracy) * velocity
        rotateAtan2(motionX, motionY, motionZ)
    }

    fun shoot(shooter: Entity, pitch: Float, yaw: Float, zero: Float, velocity: Float, inaccuracy: Float) {
        val f = -sin(Math.toDegrees(yaw.toDouble())) * cos(Math.toDegrees(pitch.toDouble()))
        val f1 = -sin(Math.toDegrees((pitch + zero).toDouble()))
        val f2 = cos(Math.toDegrees(yaw.toDouble())) * cos(Math.toDegrees(pitch.toDouble()))
        shoot(f, f1, f2, velocity, inaccuracy)
        motionX += shooter.motionX
        motionZ += shooter.motionZ

        if (!shooter.onGround) {
            motionY += shooter.motionY
        }
    }

    open fun onImpact(result: RayTraceResult) {
        val type = result.typeOfHit
        if (type == RayTraceResult.Type.ENTITY) {
            hitEntity(result)
        } else {
            hitGround(result)
        }
    }

    protected open fun hitEntity(result: RayTraceResult) {}

    protected open fun hitGround(result: RayTraceResult) {
        val state = world.getBlockState(result.blockPos)
        state.block.onEntityCollision(world, result.blockPos, state, this)
    }

    override fun setVelocity(x: Double, y: Double, z: Double) {
        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            rotateAtan2(x, y, z)
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch)
        }
    }

    open fun canHit(entity: Entity): Boolean {
        return if (!entity.isDead && entity.canBeCollidedWith() && (entity !is EntityPlayerMP || !entity.isSpectator)) {
            val owner = getOwner()

            owner == null || leftOwner || !owner.isRidingSameEntity(entity)
        } else {
            false
        }
    }

    fun rotate() {
        val length = horizontalLength(motionX, motionZ)
        rotationPitch = lerpAngle(prevRotationPitch, Math.toDegrees(atan2(motionY, length)).toFloat())
        rotationYaw = lerpAngle(prevRotationYaw, Math.toDegrees(atan2(motionX, motionZ)).toFloat())
    }

    protected fun lerpAngle(previous: Float, current: Float): Float {
        var previous = previous
        while (current - previous < -180.0f) {
            previous -= 360.0f
        }
        while (current - previous >= 180.0f) {
            previous += 360.0f
        }
        return lerp(0.2f, previous, current)
    }

    fun horizontalLength(x: Double, z: Double): Double {
        return sqrt(x * x + z * z)
    }

    // this same stuff gets called several times
    fun rotateAtan2(x: Double, y: Double, z: Double) {
        val f = horizontalLength(x, z)
        rotationYaw = Math.toDegrees(atan2(x, z)).toFloat()
        rotationPitch = Math.toDegrees(atan2(y, f)).toFloat()
        prevRotationYaw = rotationYaw
        prevRotationPitch = rotationPitch
    }

    override fun writeSpawnData(data: ByteBuf) {
        data.writeInt(ownerID)
    }

    override fun readSpawnData(data: ByteBuf) {
        val owner = world.getEntityByID(data.readInt())

        if (owner != null) {
            setOwner(owner)
        }
    }
}