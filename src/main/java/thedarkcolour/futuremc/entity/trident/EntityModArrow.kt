@file:Suppress("LeakingThis")

package thedarkcolour.futuremc.entity.trident

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.MoverType
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.init.Enchantments
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.network.play.server.SPacketChangeGameState
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSourceIndirect
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.*

abstract class EntityModArrow protected constructor(worldIn: World) : EntityArrow(worldIn), IProjectile {
    private var xTile: Int = 0
    private var yTile: Int = 0
    private var zTile: Int = 0
    private var inTile: Block? = null
    private var inData: Byte = 0
    private var ticksInGround: Int = 0
    private var ticksInAir: Int = 0
    private var damage: Double = 0.toDouble()
    private var knockbackStrength: Int = 0

    protected open val waterDrag: Float
        get() = 0.6f

    init {
        xTile = -1
        yTile = -1
        zTile = -1
        pickupStatus = PickupStatus.DISALLOWED
        damage = 2.0
        setSize(0.5f, 0.5f)
    }

    protected constructor(worldIn: World, x: Double, y: Double, z: Double) : this(worldIn) {
        setPosition(x, y, z)
    }

    protected constructor(worldIn: World, shooter: EntityLivingBase) : this(
        worldIn,
        shooter.posX,
        shooter.posY + shooter.eyeHeight.toDouble() - 0.10000000149011612,
        shooter.posZ
    ) {
        shootingEntity = shooter

        if (shooter is EntityPlayer) {
            pickupStatus = PickupStatus.ALLOWED
        }
    }

    override fun isInRangeToRenderDist(distance: Double): Boolean {
        var d0 = entityBoundingBox.averageEdgeLength * 10.0

        if (java.lang.Double.isNaN(d0)) {
            d0 = 1.0
        }

        d0 *= 64.0 * Entity.getRenderDistanceWeight()
        return distance < d0 * d0
    }

    override fun entityInit() {
        dataManager.register(CRITICAL, 0.toByte())
        dataManager.register(PIERCE_LEVEL, 0.toByte())
    }

    override fun shoot(
        shooter: Entity,
        pitch: Float,
        yaw: Float,
        p_184547_4_: Float,
        velocity: Float,
        inaccuracy: Float
    ) {
        val f = -sin(yaw * 0.017453292f) * cos(pitch * 0.017453292f)
        val f1 = -sin(pitch * 0.017453292f)
        val f2 = cos(yaw * 0.017453292f) * cos(pitch * 0.017453292f)
        shoot(f.toDouble(), f1.toDouble(), f2.toDouble(), velocity, inaccuracy)
        motionX += shooter.motionX
        motionZ += shooter.motionZ

        if (!shooter.onGround) {
            motionY += shooter.motionY
        }
    }

    @Suppress("NAME_SHADOWING")
    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) {
        var x = x
        var y = y
        var z = z
        val f = sqrt(x * x + y * y + z * z)
        x /= f
        y /= f
        z /= f
        x += rand.nextGaussian() * 0.007499999832361937 * inaccuracy.toDouble()
        y += rand.nextGaussian() * 0.007499999832361937 * inaccuracy.toDouble()
        z += rand.nextGaussian() * 0.007499999832361937 * inaccuracy.toDouble()
        x *= velocity.toDouble()
        y *= velocity.toDouble()
        z *= velocity.toDouble()
        motionX = x
        motionY = y
        motionZ = z
        val f1 = sqrt(x * x + z * z)
        rotationYaw = (atan2(x, z) * (180.0 / PI)).toFloat()
        rotationPitch = (atan2(y, f1) * (180.0 / PI)).toFloat()
        prevRotationYaw = rotationYaw
        prevRotationPitch = rotationPitch
        ticksInGround = 0
    }

    override fun setPositionAndRotationDirect(
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float,
        posRotationIncrements: Int,
        teleport: Boolean
    ) {
        setPosition(x, y, z)
        setRotation(yaw, pitch)
    }

    override fun setVelocity(x: Double, y: Double, z: Double) {
        motionX = x
        motionY = y
        motionZ = z

        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            val f = sqrt(x * x + z * z)
            rotationPitch = (atan2(y, f) * (180.0 / PI)).toFloat()
            rotationYaw = (atan2(x, z) * (180.0 / PI)).toFloat()
            prevRotationPitch = rotationPitch
            prevRotationYaw = rotationYaw
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch)
            ticksInGround = 0
        }
    }

    override fun onUpdate() {
        if (!world.isRemote) {
            setFlag(6, isGlowing)
        }

        onEntityUpdate()

        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            val f = sqrt(motionX * motionX + motionZ * motionZ)
            rotationYaw = (atan2(motionX, motionZ) * (180.0 / PI)).toFloat()
            rotationPitch = (atan2(motionY, f) * (180.0 / PI)).toFloat()
            prevRotationYaw = rotationYaw
            prevRotationPitch = rotationPitch
        }

        val blockpos = BlockPos(xTile, yTile, zTile)
        val iblockstate = world.getBlockState(blockpos)
        val block = iblockstate.block

        if (iblockstate.material != Material.AIR) {
            val box = iblockstate.getCollisionBoundingBox(world, blockpos)

            if (box != Block.NULL_AABB && box!!.offset(blockpos).contains(Vec3d(posX, posY, posZ))) {
                inGround = true
            }
        }

        if (arrowShake > 0) {
            --arrowShake
        }

        if (inGround) {
            val j = block.getMetaFromState(iblockstate)

            if ((block != inTile || j.toByte() != inData) && !world.collidesWithAnyBlock(entityBoundingBox.grow(0.05))) {
                inGround = false
                motionX *= (rand.nextFloat() * 0.2f).toDouble()
                motionY *= (rand.nextFloat() * 0.2f).toDouble()
                motionZ *= (rand.nextFloat() * 0.2f).toDouble()
                ticksInGround = 0
                ticksInAir = 0
            } else {
                ++ticksInGround

                if (ticksInGround >= 1200) {
                    setDead()
                }
            }

            ++timeInGround
        } else {
            timeInGround = 0
            ++ticksInAir
            var vec3d1 = Vec3d(posX, posY, posZ)
            var vec3d = Vec3d(posX + motionX, posY + motionY, posZ + motionZ)
            var result = world.rayTraceBlocks(vec3d1, vec3d, false, true, false)
            vec3d1 = Vec3d(posX, posY, posZ)
            vec3d = Vec3d(posX + motionX, posY + motionY, posZ + motionZ)

            if (result != null) {
                vec3d = Vec3d(result.hitVec.x, result.hitVec.y, result.hitVec.z)
            }

            val entity = findEntityOnPath(vec3d1, vec3d)

            if (entity != null) {
                result = RayTraceResult(entity)
            }

            if (result != null && result.entityHit is EntityPlayer) {
                val playerIn = result.entityHit as EntityPlayer

                if (shootingEntity is EntityPlayer && !(shootingEntity as EntityPlayer).canAttackPlayer(playerIn)) {
                    result = null
                }
            }

            if (result != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result)) {
                onHit(result)
            }

            if (isCritical) {
                for (k in 0..3) {
                    world.spawnParticle(
                        EnumParticleTypes.CRIT,
                        posX + motionX * k.toDouble() / 4.0,
                        posY + motionY * k.toDouble() / 4.0,
                        posZ + motionZ * k.toDouble() / 4.0,
                        -motionX,
                        -motionY + 0.2,
                        -motionZ
                    )
                }
            }

            posX += motionX
            posY += motionY
            posZ += motionZ
            val f4 = sqrt(motionX * motionX + motionZ * motionZ)
            rotationYaw = (atan2(motionX, motionZ) * (180.0 / PI)).toFloat()

            rotationPitch = (atan2(motionY, f4) * (180.0 / PI)).toFloat()
            while (rotationPitch - prevRotationPitch < -180.0f) {
                prevRotationPitch -= 360.0f
            }

            while (rotationPitch - prevRotationPitch >= 180.0f) {
                prevRotationPitch += 360.0f
            }

            while (rotationYaw - prevRotationYaw < -180.0f) {
                prevRotationYaw -= 360.0f
            }

            while (rotationYaw - prevRotationYaw >= 180.0f) {
                prevRotationYaw += 360.0f
            }

            rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2f
            rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2f
            var f1 = 0.99f

            if (isInWater) {
                for (i in 0..3) {
                    world.spawnParticle(
                        EnumParticleTypes.WATER_BUBBLE,
                        posX - motionX * 0.25,
                        posY - motionY * 0.25,
                        posZ - motionZ * 0.25,
                        motionX,
                        motionY,
                        motionZ
                    )
                }

                f1 = waterDrag
            }

            if (isWet) {
                extinguish()
            }

            motionX *= f1.toDouble()
            motionY *= f1.toDouble()
            motionZ *= f1.toDouble()

            if (!hasNoGravity()) {
                motionY -= 0.05000000074505806
            }

            setPosition(posX, posY, posZ)
            doBlockCollisions()
        }
    }

    override fun onHit(raytraceResultIn: RayTraceResult) {
        val entity = raytraceResultIn.entityHit

        if (entity != null) {
            val f = sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)
            var i = ceil(f * damage)

            if (isCritical) {
                i += rand.nextInt((i / 2 + 2).toInt())
            }

            val source = if (shootingEntity == null) {
                causeArrowDamage(this, this)
            } else {
                causeArrowDamage(this, shootingEntity)
            }

            if (isBurning && entity !is EntityEnderman) {
                entity.setFire(5)
            }

            if (entity.attackEntityFrom(source, i.toFloat())) {
                if (entity is EntityLivingBase) {

                    if (!world.isRemote) {
                        entity.arrowCountInEntity = entity.arrowCountInEntity + 1
                    }

                    if (knockbackStrength > 0) {
                        val f1 = sqrt(motionX * motionX + motionZ * motionZ)

                        if (f1 > 0.0f) {
                            entity.addVelocity(
                                motionX * knockbackStrength.toDouble() * 0.6000000238418579 / f1.toDouble(),
                                0.1,
                                motionZ * knockbackStrength.toDouble() * 0.6000000238418579 / f1.toDouble()
                            )
                        }
                    }

                    if (shootingEntity is EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(entity, shootingEntity!!)
                        EnchantmentHelper.applyArthropodEnchantments((shootingEntity as EntityLivingBase?)!!, entity)
                    }

                    arrowHit(entity)

                    if (shootingEntity != null && entity != shootingEntity && entity is EntityPlayer && shootingEntity is EntityPlayerMP) {
                        (shootingEntity as EntityPlayerMP).connection.sendPacket(SPacketChangeGameState(6, 0.0f))
                    }
                }

                playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f))

                if (entity !is EntityEnderman) {
                    setDead()
                }
            } else {
                motionX *= -0.10000000149011612
                motionY *= -0.10000000149011612
                motionZ *= -0.10000000149011612
                rotationYaw += 180.0f
                prevRotationYaw += 180.0f
                ticksInAir = 0

                if (!world.isRemote && motionX * motionX + motionY * motionY + motionZ * motionZ < 0.0010000000474974513) {
                    if (pickupStatus == PickupStatus.ALLOWED) {
                        entityDropItem(arrowStack, 0.1f)
                    }

                    setDead()
                }
            }
        } else {
            val pos = raytraceResultIn.blockPos
            xTile = pos.x
            yTile = pos.y
            zTile = pos.z
            val state = world.getBlockState(pos)
            inTile = state.block
            inData = inTile!!.getMetaFromState(state).toByte()
            motionX = (raytraceResultIn.hitVec.x - posX)
            motionY = (raytraceResultIn.hitVec.y - posY)
            motionZ = (raytraceResultIn.hitVec.z - posZ)
            val f2 = sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)
            posX -= motionX / f2 * 0.05000000074505806
            posY -= motionY / f2 * 0.05000000074505806
            posZ -= motionZ / f2 * 0.05000000074505806
            playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f))
            inGround = true
            arrowShake = 7
            isCritical = false

            if (state.material != Material.AIR) {
                inTile!!.onEntityCollision(world, pos, state, this)
            }
        }
    }

    override fun move(type: MoverType?, x: Double, y: Double, z: Double) {
        /*if (inGround) {
            xTile = floor(posX).toInt()
            yTile = floor(posY).toInt()
            zTile = floor(posZ).toInt()
        }*/
    }

    override fun arrowHit(living: EntityLivingBase?) {}
/*
    override fun findEntityOnPath(start: Vec3d, end: Vec3d): Entity? {
        if (world.isRemote) {
            return null
        }
        var entity: Entity? = null
        val list = world.getEntitiesInAABBexcluding(
            this,
            entityBoundingBox.expand(motionX, motionY, motionZ).grow(1.0),
            ARROW_TARGETS
        )
        var d0 = 0.0

        for (entity1 in list) {
            if (entity1 != shootingEntity) {
                val box = entity1.entityBoundingBox.grow(0.30000001192092896)
                val result = box.calculateIntercept(start, end)

                if (result != null) {
                    if (result.entityHit != null) return result.entityHit
                    val d1 = start.squareDistanceTo(result.hitVec)

                    if (d1 < d0 || d0 == 0.0) {
                        entity = entity1
                        d0 = d1
                    }
                }
            }
        }

        return entity
    }
*/
    override fun writeEntityToNBT(compound: NBTTagCompound) {
        compound.setInteger("xTile", xTile)
        compound.setInteger("yTile", yTile)
        compound.setInteger("zTile", zTile)
        compound.setShort("life", ticksInGround.toShort())
        inTile?.let {
            compound.setString("inTile", it.registryName.toString())
        }
        compound.setByte("inData", inData)
        compound.setByte("shake", arrowShake.toByte())
        compound.setByte("inGround", (if (inGround) 1 else 0).toByte())
        compound.setByte("pickup", pickupStatus.ordinal.toByte())
        compound.setDouble("damage", damage)
        compound.setBoolean("crit", isCritical)
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        xTile = compound.getInteger("xTile")
        yTile = compound.getInteger("yTile")
        zTile = compound.getInteger("zTile")
        ticksInGround = compound.getShort("life").toInt()

        inTile = if (compound.hasKey("inTile", 8)) {
            Block.getBlockFromName(compound.getString("inTile"))
        } else {
            null
        }

        inData = compound.getByte("inData") and 255.toByte()
        arrowShake = compound.getByte("shake").toInt() and 255
        inGround = compound.getByte("inGround").toInt() == 1

        if (compound.hasKey("damage", 99)) {
            damage = compound.getDouble("damage")
        }

        if (compound.hasKey("pickup", 99)) {
            pickupStatus = PickupStatus.getByOrdinal(compound.getByte("pickup").toInt())
        } else if (compound.hasKey("player", 99)) {
            pickupStatus = if (compound.getBoolean("player")) {
                PickupStatus.ALLOWED
            } else {
                PickupStatus.DISALLOWED
            }
        }

        isCritical = compound.getBoolean("crit")
    }

    override fun onCollideWithPlayer(entityIn: EntityPlayer) {
        if (!world.isRemote && inGround && arrowShake <= 0) {
            var flag =
                pickupStatus == PickupStatus.ALLOWED || pickupStatus == PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode

            if (pickupStatus == PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(arrowStack)) {
                flag = false
            }

            if (flag && canPickup(entityIn)) {
                entityIn.onItemPickup(this, 1)
                setDead()
            }
        }
    }

    open fun canPickup(entityIn: EntityPlayer): Boolean {
        return true
    }

    abstract override fun getArrowStack(): ItemStack

    override fun canTriggerWalking(): Boolean {
        return false
    }

    override fun setDamage(damageIn: Double) {
        damage = damageIn
    }

    override fun getDamage(): Double {
        return damage
    }

    override fun setKnockbackStrength(knockbackStrengthIn: Int) {
        knockbackStrength = knockbackStrengthIn
    }

    override fun canBeAttackedWithItem(): Boolean {
        return false
    }

    override fun getEyeHeight(): Float {
        return 0.0f
    }

    override fun setIsCritical(critical: Boolean) {
        setArrowFlag(1, critical)
    }

    fun setPierceLevel(level: Byte) {
        dataManager.set(PIERCE_LEVEL, level)
    }

    private fun setArrowFlag(flag: Int, bool: Boolean) {
        val critical = dataManager.get(CRITICAL)
        if (bool) {
            dataManager.set(CRITICAL, (critical or flag.toByte()))
        } else {
            dataManager.set(CRITICAL, (critical and flag.inv().toByte()))
        }
    }

    override fun getIsCritical(): Boolean {
        val b0 = dataManager.get(CRITICAL)
        return (b0 and 1.toByte()) != 0.toByte()
    }

    override fun setEnchantmentEffectsFromEntity(p_190547_1_: EntityLivingBase, p_190547_2_: Float) {
        val i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, p_190547_1_)
        val j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, p_190547_1_)
        setDamage((p_190547_2_ * 2.0f).toDouble() + rand.nextGaussian() * 0.25 + (world.difficulty.id.toFloat() * 0.11f).toDouble())

        if (i > 0) {
            setDamage(getDamage() + i.toDouble() * 0.5 + 0.5)
        }

        if (j > 0) {
            setKnockbackStrength(j)
        }

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, p_190547_1_) > 0) {
            setFire(100)
        }
    }

    fun func_213865_o(p_213865_1_: Boolean) {
        setArrowFlag(4, p_213865_1_)
    }

    companion object {
        private val CRITICAL = EntityDataManager.createKey(EntityModArrow::class.java, DataSerializers.BYTE)
        private val PIERCE_LEVEL = EntityDataManager.createKey(EntityModArrow::class.java, DataSerializers.BYTE)
        val ARROW_TARGETS = com.google.common.base.Predicate<Entity?> {
            it?.let {
                (it is EntityPlayer && it.isSpectator) && it.isEntityAlive && it.canBeCollidedWith()
            } ?: false
        }

        fun causeArrowDamage(arrow: EntityModArrow, indirectEntityIn: Entity?): DamageSource {
            return EntityDamageSourceIndirect("arrow", arrow, indirectEntityIn).setProjectile()
        }
    }
}