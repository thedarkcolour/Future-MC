package thedarkcolour.futuremc.entity.trident

import com.google.common.base.Predicates
import com.google.common.collect.Lists
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.MoverType
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.init.Enchantments
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.network.play.server.SPacketChangeGameState
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.core.util.isAir
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.atan2
import kotlin.math.ceil

abstract class AbstractArrow(worldIn: World) : BaseProjectile(worldIn) {
    private var inState: IBlockState? = null
    protected var inGround = false
    protected var timeInGround = 0
    var status = EntityArrow.PickupStatus.DISALLOWED
    var arrowShake = 0
    private var ticksInGround = 0
    var damage = 2.0
    var knockback = 0
    private var hitSound = getHitEntitySound()
    private var piercedEntities: IntOpenHashSet? = null
    private var hitEntities: MutableList<Entity>? = null

    constructor(x: Double, y: Double, z: Double, worldIn: World) : this(worldIn) {
        setPosition(x, y, z)
    }

    constructor(shooter: EntityLivingBase, worldIn: World) : this(shooter.posX, shooter.eyeHeight - 0.1, shooter.posZ, worldIn) {
        setOwner(shooter)
        if (shooter is EntityPlayer) {
            status = EntityArrow.PickupStatus.ALLOWED
        }
    }

    fun setHitSound(sound: SoundEvent) {
        hitSound = sound
    }

    override fun isInRangeToRenderDist(distance: Double): Boolean {
        var d0 = entityBoundingBox.averageEdgeLength * 10.0

        if (java.lang.Double.isNaN(d0)) {
            d0 = 1.0
        }

        d0 *= 64.0 * getRenderDistanceWeight()
        return distance < d0 * d0
    }

    override fun entityInit() {
        dataManager.register(ARROW_FLAGS, 0.toByte())
        dataManager.register(PIERCING, 0.toByte())
    }

    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) {
        super.shoot(x, y, z, velocity, inaccuracy)
        ticksInGround = 0
    }

    override fun setPositionAndRotation(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
        setPosition(x, y, z)
        setRotation(yaw, pitch)
    }

    override fun setVelocity(x: Double, y: Double, z: Double) {
        super.setVelocity(x, y, z)
        ticksInGround = 0
    }

    override fun onUpdate() {
        super.onUpdate()
        val flag = getNoClip()
        var vector3d = Vec3d(motionX, motionY, motionZ)

        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            rotateAtan2(motionX, motionY, motionZ)
        }

        val pos = position
        val state = world.getBlockState(pos)

        if (!state.isAir(world, pos) && !flag) {
            val box = state.getCollisionBoundingBox(world, pos)

            if (box != Block.NULL_AABB && box!!.offset(pos).contains(Vec3d(posX, posY, posZ))) {
                inGround = true
            }
        }

        if (arrowShake > 0) {
            --arrowShake
        }

        if (isWet) {
            extinguish()
        }

        if (inGround && !flag) {
            if (inState != state && canFallThrough()) {
                doFallThrough()
            } else if (!world.isRemote) {
                func_225516_i_()
            }

            ++timeInGround
        } else {
            timeInGround = 0
            val vector3d2 = positionVector
            var vector3d3 = vector3d2.add(vector3d)
            var rayTraceResult = world.rayTraceBlocks(vector3d2, vector3d3, false, true, false)

            if (rayTraceResult != null && rayTraceResult.typeOfHit != RayTraceResult.Type.MISS) {
                vector3d3 = rayTraceResult.hitVec
            }

            while (!isDead) {
                var entityRayTraceResult = rayTraceEntities(vector3d2, vector3d3)

                if (entityRayTraceResult != null) {
                    rayTraceResult = entityRayTraceResult
                }

                if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
                    val entity = rayTraceResult.entityHit
                    val entity1 = getOwner();

                    if (entity is EntityPlayer && entity1 is EntityPlayer && !entity1.canAttackPlayer(entity)) {
                        rayTraceResult = null
                        entityRayTraceResult = null
                    }
                }

                if (rayTraceResult != null && rayTraceResult.typeOfHit != RayTraceResult.Type.MISS && !flag && !ForgeEventFactory.onProjectileImpact(this, rayTraceResult)) {
                    onImpact(rayTraceResult)
                    isAirBorne = true
                }

                if (entityRayTraceResult == null || getPiercing() <= 0) {
                    break
                }

                rayTraceResult = null
            }

            // Update motion
            val d3 = this.motionX
            val d4 = this.motionY
            val d0 = this.motionZ

            if (isCritical()) {
                for (i in 0..3) {
                    world.spawnParticle(EnumParticleTypes.CRIT, posX + d3 * i / 4.0, posY + d4 * i / 4.0, posZ + d0 * i / 4.0, -d3, -d4 + 0.2, -d0)
                }
            }

            val d5 = posX + d3
            val d1 = posY + d4
            val d2 = posZ + d0
            val f1 = horizontalLength(d3, d0)

            rotationYaw = if (flag) {
                Math.toDegrees(atan2(-d3, -d0)).toFloat()
            } else {
                Math.toDegrees(atan2(d3, d0)).toFloat()
            }

            rotationPitch = Math.toDegrees(atan2(d4, f1)).toFloat()
            rotationPitch = lerpAngle(prevRotationPitch, rotationPitch)
            rotationYaw = lerpAngle(prevRotationYaw, rotationYaw)

            var f2 = 0.99f

            if (isInWater) {
                for (j in 0..3) {
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d5 - d3 * 0.25, d1 - d4 * 0.25, d2 - d0 * 0.25, d3, d4, d0)
                }

                f2 = getUnderwaterModifier()
            }

            motionX *= f2.toDouble()
            motionY *= f2.toDouble()
            motionZ *= f2.toDouble()

            if (!hasNoGravity() && !flag) {
                motionY -= 0.05f
            }

            setPosition(d5, d1, d2)
            doBlockCollisions()
        }
    }

    fun canFallThrough(): Boolean {
        return inGround && !world.collidesWithAnyBlock(AxisAlignedBB(positionVector, positionVector))
    }

    fun doFallThrough() {
        inGround = false
        motionX *= rand.nextFloat() * 0.2f
        motionY *= rand.nextFloat() * 0.2f
        motionZ *= rand.nextFloat() * 0.2f
        ticksInGround = 0
    }

    override fun move(type: MoverType, x: Double, y: Double, z: Double) {
        super.move(type, x, y, z)

        if (type != MoverType.SELF && canFallThrough()) {
            doFallThrough()
        }
    }

    open fun func_225516_i_() {
        ++ticksInGround

        if (ticksInGround >= 1200) {
            setDead()
        }
    }

    private fun func_213870_w() {
        if (hitEntities != null) {
            hitEntities!!.clear()
        }
        if (piercedEntities != null) {
            piercedEntities!!.clear()
        }
    }

    override fun hitEntity(result: RayTraceResult) {
        super.hitEntity(result)
        val target = result.entityHit
        val f = Vec3d(motionX, motionY, motionZ).length()
        var i = ceil((f * damage).coerceAtMost(2.147483647E9)).toInt()

        if (getPiercing() > 0) {
            if (piercedEntities == null) {
                piercedEntities = IntOpenHashSet(5)
            }

            if (hitEntities == null) {
                hitEntities = Lists.newArrayListWithCapacity(5)
            }

            if (piercedEntities!!.size >= getPiercing() + 1) {
                setDead()
                return
            }

            piercedEntities!!.add(target.entityId)
        }

        if (isCritical()) {
            val j = rand.nextInt((i / 2 + 2).toInt()).toLong()
            i = (j + i.toLong()).coerceAtMost(2147483647L).toInt()
        }

        val shooter = getOwner()
        val source = causeArrowDamage(this, shooter ?: this)

        val flag = target is EntityEnderman
        val k = target.fire

        if (isBurning && !flag) {
            target.setFire(5)
        }

        if (target.attackEntityFrom(source, i.toFloat())) {
            if (flag) {
                return
            }

            if (target is EntityLivingBase) {

                if (!world.isRemote && getPiercing() <= 0) {
                    target.arrowCountInEntity += 1
                }

                if (knockback > 0) {
                    val vec = Vec3d(motionX, motionY, motionZ).normalize().scale(knockback + 0.6)

                    if (vec.lengthSquared() > 0.0) {
                        target.addVelocity(vec.x, 0.1, vec.z)
                    }
                }

                if (!world.isRemote && shooter is EntityLivingBase) {
                    EnchantmentHelper.applyThornEnchantments(target, shooter)
                    EnchantmentHelper.applyArthropodEnchantments(shooter, target)
                }

                arrowHit(target)

                if (shooter != null && target != shooter && target is EntityPlayer && shooter is EntityPlayerMP) {
                    shooter.connection.sendPacket(SPacketChangeGameState(6, 0.0f))
                }

                if (target.isDead && hitEntities != null) {
                    hitEntities!!.add(target)
                }
            }

            playSound(hitSound, 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f))
            if (getPiercing() <= 0) {
                setDead()
            }
        } else {
            target.fire = k
            motionX *= -0.1
            motionY *= -0.1
            motionZ *= -0.1
            rotationYaw += 180.0f
            prevRotationYaw += 180.0f
            if (!world.isRemote && (motionX * motionX + motionY * motionY + motionZ * motionZ) < 1.0E-7) {
                if (status == EntityArrow.PickupStatus.ALLOWED) {
                    entityDropItem(getArrowStack(), 0.1f)
                }

                setDead()
            }
        }
    }

    override fun hitGround(result: RayTraceResult) {
        inState = world.getBlockState(result.blockPos)
        super.hitGround(result)
        val hitVec = result.hitVec.subtract(posX, posY, posZ)
        motionX = hitVec.x
        motionY = hitVec.y
        motionZ = hitVec.z
        val vec = hitVec.normalize().scale(0.05)
        posX -= vec.x
        posY -= vec.y
        posZ -= vec.z
        playSound(getHitGroundSound(), 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f))
        inGround = true
        arrowShake = 7
        setCritical(false)
        setPiercing(0)
        setHitSound(SoundEvents.ENTITY_ARROW_HIT)
        setShotFromCrossbow(false)
        func_213870_w()
    }

    open fun getHitEntitySound(): SoundEvent {
        return SoundEvents.ENTITY_ARROW_HIT
    }

    fun getHitGroundSound(): SoundEvent {
        return hitSound
    }

    open fun arrowHit(living: EntityLivingBase) {}

    protected open fun rayTraceEntities(start: Vec3d, end: Vec3d): RayTraceResult? {
        var entity: Entity? = null
        val list = world.getEntitiesInAABBexcluding(this, entityBoundingBox.expand(motionX, motionY, motionZ).grow(1.0), ARROW_TARGETS)
        var d0 = 0.0

        for (i in list.indices) {
            val entity1 = list[i]
            val bounds = entity1.entityBoundingBox.grow(0.3)
            val result = bounds.calculateIntercept(start, end)

            if (result != null) {
                val d1: Double = start.squareDistanceTo(result.hitVec)

                if (d1 < d0 || d0 == 0.0) {
                    entity = entity1
                    d0 = d1
                }
            }
        }

        return if (entity == null) {
            null
        } else {
            RayTraceResult(entity)
        }
    }

    override fun canHit(entity: Entity): Boolean {
        return super.canHit(entity) && (piercedEntities == null || !piercedEntities!!.contains(entity.entityId))
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        compound.setShort("life", ticksInGround.toShort())
        if (inState != null) {
            compound.setTag("inBlockState", NBTUtil.writeBlockState(NBTTagCompound(), inState))
        }

        compound.setByte("shake", arrowShake.toByte())
        compound.setBoolean("inGround", inGround)
        compound.setByte("pickup", status.ordinal.toByte())
        compound.setDouble("damage", damage)
        compound.setBoolean("crit", isCritical())
        compound.setByte("PierceLevel", getPiercing())
        compound.setString("SoundEvent", hitSound!!.registryName.toString())
        compound.setBoolean("ShotFromCrossbow", getShotFromCrossbow())
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)

        ticksInGround = compound.getShort("life").toInt()
        if (compound.hasKey("inBlockState", 10)) {
            inState = NBTUtil.readBlockState(compound.getCompoundTag("inBlockState"))
        }

        arrowShake = compound.getByte("shake").coerceAtMost(255.toByte()).toInt()
        inGround = compound.getBoolean("inGround")
        if (compound.hasKey("damage", 99)) {
            damage = compound.getDouble("damage")
        }

        if (compound.hasKey("pickup", 99)) {
            status = EntityArrow.PickupStatus.getByOrdinal(compound.getByte("pickup").toInt())
        } else if (compound.hasKey("player", 99)) {
            status = if (compound.getBoolean("player")) {
                EntityArrow.PickupStatus.ALLOWED
            } else {
                EntityArrow.PickupStatus.DISALLOWED
            }
        }

        setCritical(compound.getBoolean("crit"))
        setPiercing(compound.getByte("PierceLevel"))
        if (compound.hasKey("SoundEvent", 8)) {
            hitSound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation(compound.getString("SoundEvent"))) ?: getHitEntitySound()
        }

        setShotFromCrossbow(compound.getBoolean("ShotFromCrossbow"))
    }

    override fun setOwner(entity: Entity?) {
        super.setOwner(entity)
        if (entity is EntityPlayer) {
            status = if (entity.capabilities.isCreativeMode) {
                EntityArrow.PickupStatus.CREATIVE_ONLY
            } else {
                EntityArrow.PickupStatus.ALLOWED
            }
        }
    }

    override fun onCollideWithPlayer(entityIn: EntityPlayer) {
        if (!world.isRemote && (inGround || getNoClip()) && arrowShake <= 0) {
            var flag = status != EntityArrow.PickupStatus.DISALLOWED && entityIn.capabilities.isCreativeMode || getNoClip() && getOwner()?.uniqueID == entityIn.uniqueID
            if (status == EntityArrow.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(getArrowStack())) {
                flag = false
            }
            if (flag) {
                entityIn.onItemPickup(this, 1)
                setDead()
            }
        }
    }

    abstract fun getArrowStack(): ItemStack

    override fun canTriggerWalking(): Boolean {
        return false
    }

    override fun canBeAttackedWithItem(): Boolean {
        return false
    }

    override fun getEyeHeight(): Float {
        return 0.13f
    }

    fun setCritical(critical: Boolean) = setArrowFlag(1, critical)

    fun setPiercing(level: Byte) = dataManager.set(PIERCING, level)

    fun setArrowFlag(flag: Int, value: Boolean) {
        val current = dataManager.get(ARROW_FLAGS)

        if (value) {
            dataManager.set(ARROW_FLAGS, current or flag.toByte())
        } else {
            dataManager.set(ARROW_FLAGS, current and flag.inv().toByte())
        }
    }

    fun isCritical(): Boolean {
        val b0 = dataManager.get(ARROW_FLAGS)
        return (b0 and 1) != 0.toByte()
    }

    fun getShotFromCrossbow(): Boolean {
        val b0 = dataManager.get(ARROW_FLAGS)
        return (b0 and 4) != 0.toByte()
    }

    fun getPiercing(): Byte {
        return dataManager.get(PIERCING)
    }

    fun setEnchantmentEffectsFromEntity(entity: EntityLivingBase, f: Float) {
        val i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, entity)
        val j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, entity)
        damage = f * 2.0f + rand.nextGaussian() * 0.25 + world.difficulty.id * 0.11f
        if (i > 0) {
            damage += i * 0.5 + 0.5
        }

        if (j > 0) {
            knockback = j
        }

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, entity) > 0) {
            setFire(100)
        }
    }

    protected open fun getUnderwaterModifier() = 0.6f

    fun setNoClip(noClip: Boolean) {
        this.noClip = noClip
        setArrowFlag(2, noClip)
    }

    fun getNoClip(): Boolean {
        return if (!world.isRemote) {
            noClip
        } else {
            (dataManager.get(ARROW_FLAGS) and 2) != 0.toByte()
        }
    }

    fun setShotFromCrossbow(bool: Boolean) {
        setArrowFlag(4, bool)
    }

    fun causeArrowDamage(arrow: Entity, indirectEntityIn: Entity?): DamageSource {
        return EntityDamageSourceIndirect("arrow", arrow, indirectEntityIn).setProjectile()
    }

    override fun writeSpawnData(data: ByteBuf) {
        super.writeSpawnData(data)

        data.writeDouble(posX)
        data.writeDouble(posY)
        data.writeDouble(posZ)
    }

    override fun readSpawnData(data: ByteBuf) {
        super.readSpawnData(data)

        val x = data.readDouble()
        val y = data.readDouble()
        val z = data.readDouble()
        setPosition(x, y, z)
    }

    companion object {
        private val ARROW_FLAGS = EntityDataManager.createKey(AbstractArrow::class.java, DataSerializers.BYTE)
        private val PIERCING = EntityDataManager.createKey(AbstractArrow::class.java, DataSerializers.BYTE)
        private val ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, com.google.common.base.Predicate { it!!.canBeCollidedWith() })
    }
}