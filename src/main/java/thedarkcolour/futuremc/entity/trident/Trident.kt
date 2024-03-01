package thedarkcolour.futuremc.entity.trident

import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSourceIndirect
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import thedarkcolour.futuremc.enchantment.EnchantHelper
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.world.FWorldListener
import java.util.*
import kotlin.math.ceil

class Trident : EntityArrow {
    private var item = ItemStack(FItems.TRIDENT)
    private var canReturn = false
    // used for sound only
    private var isReturning = false
    private var shooterId: UUID? = null

    constructor(worldIn: World) : super(worldIn)
    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(worldIn, x, y, z)
    constructor(worldIn: World, shooter: EntityLivingBase, itemStack: ItemStack) : super(worldIn, shooter) {
        this.item = itemStack
        this.shooterId = shooter.persistentID
        dataManager.set(LOYALTY_LEVEL, EnchantHelper.getLoyalty(itemStack).toByte())
    }

    override fun getArrowStack(): ItemStack {
        return item.copy()
    }

    override fun entityInit() {
        super.entityInit()
        dataManager.register(LOYALTY_LEVEL, 0)
    }

    override fun onUpdate() {
        if (timeInGround > 4) {
            canReturn = true
        }

        if (shootingEntity == null && shooterId != null) {
            shootingEntity = FWorldListener.ENTITIES_BY_UUID.get(shooterId)
        } else if (shootingEntity == null || shootingEntity.isDead) {
            shootingEntity = null
        }

        val loyalty = dataManager.get(LOYALTY_LEVEL)
        val shooter = shootingEntity

        if (shooter != null && loyalty > 0 && canReturn) {
            if (validOwner()) {
                if (!world.isRemote && pickupStatus == PickupStatus.ALLOWED) {
                    entityDropItem(arrowStack, 0.1f)
                }
                setDead()
            } else {
                var vec = Vec3d(shooter.posX - this.posX, shooter.posY + shooter.eyeHeight - this.posY, shooter.posZ - this.posZ)
                noClip = true
                motionY = vec.y * 0.015 * loyalty

                if (world.isRemote) {
                    prevPosY = posY
                }
                vec = vec.normalize().scale(0.05 * loyalty)
                motionX = motionX * 0.95 + vec.x
                motionY = motionY * 0.95 + vec.y
                motionZ = motionZ * 0.95 + vec.z

                if (!isReturning) {
                    playSound(FSounds.TRIDENT_LOYALTY, 10.0f, 1.0f)
                    isReturning = true
                }
            }
        }

        // Skips the inGround checks in EntityArrow
        if (isReturning) {
            inGround = false
        }

        // Prevent tridents from despawning like arrows
        if (ticksInGround == 1199 && (pickupStatus == PickupStatus.ALLOWED || loyalty > 0)) {
            ticksInGround = 600
        }

        if (inWater) {
            for (i in 0..3) {
                world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * 0.25, posY - motionY * 0.25, posZ - motionZ * 0.25, motionX, motionY, motionZ)
            }

            motionX *= 0.98
            motionY *= 0.98
            motionZ *= 0.98
        }

        super.onUpdate()
    }

    // stop the trident getting slowed down
    override fun isInWater(): Boolean {
        return false
    }

    override fun onHit(raytraceResultIn: RayTraceResult) {
        if (!isReturning) {
            val target = raytraceResultIn.entityHit
            if (target != null) {
                onHitEntity(raytraceResultIn, target)
            } else {
                onHitBlock(raytraceResultIn, raytraceResultIn.blockPos)
            }
        }
    }

    private fun onHitEntity(raytraceResultIn: RayTraceResult, target: Entity) {
        val speed = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)
        val damage = ceil(damage.toFloat() * speed)
        if (target.attackEntityFrom(EntityDamageSourceIndirect("trident", this, target), damage)) {
            motionX *= -0.05
            motionY *= -0.1
            motionZ *= -0.05
            rotationYaw += 180f
            prevRotationYaw += 180f
            ticksInAir = 0
            playSound(FSounds.TRIDENT_PIERCE, 1.0f, 1.0f)
        }
        if (world.isThundering && EnchantHelper.getChanneling(arrowStack)) {
            world.addWeatherEffect(EntityLightningBolt(shootingEntity.world, this.posX, this.posY, this.posZ, false));
            this.playSound(FSounds.TRIDENT_CONDUCTIVIDAD, 5.0F, 1.0F);
        }
    }

    override fun setFire(seconds: Int) {
        // fireproof
    }

    private fun onHitBlock(raytraceResultIn: RayTraceResult, hitPos: BlockPos) {
        xTile = hitPos.x
        yTile = hitPos.y
        zTile = hitPos.z
        val state = world.getBlockState(hitPos)
        inTile = state.block
        inData = inTile.getMetaFromState(state)
        motionX = (raytraceResultIn.hitVec.x - posX)
        motionY = (raytraceResultIn.hitVec.y - posY)
        motionZ = (raytraceResultIn.hitVec.z - posZ)
        val speed = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)
        posX -= motionX / speed * 0.05
        posY -= motionY / speed * 0.05
        posZ -= motionZ / speed * 0.05
        playSound(FSounds.TRIDENT_IMPACT, 1.0f, 1.0f)
        inGround = true
        arrowShake = 7

        if (state.material != Material.AIR) {
            inTile.onEntityCollision(world, hitPos, state, this)
        }
    }

    override fun onCollideWithPlayer(entityIn: EntityPlayer) {
        // Override pickup logic of arrow
        if (!world.isRemote && (inGround || isReturning) && (arrowShake <= 0)) {
            if (shootingEntity != null && shooterId == entityIn.persistentID) {
                if (pickupStatus == PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(arrowStack)) {
                    return
                }
                if (pickupStatus == PickupStatus.ALLOWED || pickupStatus == PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode) {
                    entityIn.onItemPickup(this, 1)
                    setDead()
                }
            }
        }
    }

    // Shooter is not saved for arrows but we need this for loyalty
    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        shooterId = shootingEntity?.persistentID
        shooterId?.let { id -> compound.setUniqueId("Shooter", id) }
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)
        if (compound.hasUniqueId("Shooter")) {
            shooterId = compound.getUniqueId("Shooter")
        }
    }

    private fun validOwner(): Boolean {
        val shooter = shootingEntity
        return shooter != null && shooter.isEntityAlive && (shooter !is EntityPlayerMP || shooter.isSpectator)
    }

    companion object {
        private val LOYALTY_LEVEL = EntityDataManager.createKey(Trident::class.java, DataSerializers.BYTE)
        private val ENCHANTED = EntityDataManager.createKey(Trident::class.java, DataSerializers.BOOLEAN)
    }
}