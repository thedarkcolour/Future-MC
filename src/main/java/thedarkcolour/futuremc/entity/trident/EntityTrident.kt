package thedarkcolour.futuremc.entity.trident

import net.minecraft.dispenser.IPosition
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.MoverType
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.SPacketChangeGameState
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSourceIndirect
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import thedarkcolour.futuremc.enchantment.EnchantHelper
import thedarkcolour.futuremc.init.Sounds

class EntityTrident : EntityModArrow {
    private var thrownStack: ItemStack = ItemStack.EMPTY
    private var hasChanneled: Boolean = false
    private var ticksInGround = 0
    private var isReturning: Boolean = false

    override val waterDrag: Float
        get() = 0.99f

    @Suppress("unused")
    constructor(world: World) : super(world)

    constructor(world: World, pos: IPosition, stack: ItemStack) : super(world, pos.x, pos.y, pos.z) {
        thrownStack = stack
        pickupStatus = PickupStatus.ALLOWED
    }

    constructor(world: World, shooter: EntityLivingBase, stack: ItemStack) : super(world, shooter) {
        thrownStack = stack
    }

    override fun getArrowStack(): ItemStack {
        return thrownStack.copy()
    }

    override fun findEntityOnPath(start: Vec3d, end: Vec3d): Entity? {
        if (world.isRemote) {
            return null
        }
        var entity: Entity? = null
        val list = world.getEntitiesInAABBexcluding(this, entityBoundingBox.expand(motionX, motionY, motionZ).grow(1.0), ARROW_TARGETS::invoke)
        var d0 = 0.0

        for (entity1 in list) {
            if (entity1 != shootingEntity) {
                val box = entity1.entityBoundingBox.grow(0.30000001192092896)
                val raytraceresult = box.calculateIntercept(start, end)

                if (raytraceresult != null) {
                    val d1 = start.squareDistanceTo(raytraceresult.hitVec)

                    if (d1 < d0 || d0 == 0.0) {
                        entity = entity1
                        d0 = d1
                    }
                }
            }
        }
        return entity
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        compound.setTag("ItemTrident", thrownStack.writeToNBT(NBTTagCompound()))
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)
        if (compound.hasKey("ItemTrident")) {
            thrownStack = ItemStack(compound.getCompoundTag("ItemTrident"))
        }
    }

    override fun onHit(raytraceResultIn: RayTraceResult) {
        val entity = raytraceResultIn.entityHit

        if (entity != null) {

            val source: DamageSource = if (shootingEntity !is EntityPlayer) {
                causeTridentDamage(this, this)
            } else {
                causeTridentDamage(this, shootingEntity)
            }

            if (entity.attackEntityFrom(source, getDamageForTrident(entity))) {
                if (entity is EntityLivingBase) {

                    if (shootingEntity is EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(entity, shootingEntity!!)
                        EnchantmentHelper.applyArthropodEnchantments(entity, shootingEntity!!)
                    }

                    arrowHit(entity)

                    if (shootingEntity != null && entity != shootingEntity && entity is EntityPlayer && shootingEntity is EntityPlayer) {
                        (shootingEntity as EntityPlayerMP).connection.sendPacket(SPacketChangeGameState(6, 0.0f))
                    }

                    playSound(Sounds.TRIDENT_PIERCE, 1.0f, 1.0f)

                    motionX *= -0.009999999776482582
                    motionY *= -0.10000000149011612
                    motionZ *= -0.009999999776482582
                    rotationYaw += 180.0f
                    prevRotationYaw += 180.0f

                    if (!hasChanneled && !thrownStack.isEmpty) { // Enchantment handling
                        if (EnchantHelper.hasChanneling(thrownStack)) {
                            if (world.isThundering) {
                                shootingEntity!!.world.addWeatherEffect(EntityLightningBolt(shootingEntity!!.world, posX, posY, posZ, false))
                                playSound(Sounds.TRIDENT_CONDUCTIVIDAD, 5.0f, 1.0f)
                                hasChanneled = true
                            }
                        }
                    }
                }
            } else {
                if (!world.isRemote && motionX * motionX + motionY * motionY + motionZ * motionZ < 0.0010000000474974513) {
                    if (pickupStatus == PickupStatus.ALLOWED) {
                        entityDropItem(arrowStack, 0.1f)
                    }
                    setDead()
                }
            }
        } else {
            motionX = (raytraceResultIn.hitVec.x - posX)
            motionY = (raytraceResultIn.hitVec.y - posY)
            motionZ = (raytraceResultIn.hitVec.z - posZ)
            val f2 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)
            posX -= motionX / f2.toDouble() * 0.05000000074505806
            posY -= motionY / f2.toDouble() * 0.05000000074505806
            posZ -= motionZ / f2.toDouble() * 0.05000000074505806
            inGround = true
            arrowShake = 7

            playSound(Sounds.TRIDENT_IMPACT, 1.0f, 1.0f)

            if (!hasChanneled && EnchantHelper.hasChanneling(thrownStack)) { // Enchantment handling
                val living = if (shootingEntity == null) this else shootingEntity
                living.world.addWeatherEffect(EntityLightningBolt(living.world, posX, posY, posZ, false))
                playSound(Sounds.TRIDENT_CONDUCTIVIDAD, 5.0f, 1.0f)
                hasChanneled = true
            }
        }
    }

    override fun onUpdate() {
        if (inGround) {
            ticksInGround++
        }

        if (shootingEntity is EntityPlayer) {
            val loyalty = EnchantHelper.getLoyalty(thrownStack)

            if (loyalty in 1..3 && !(shootingEntity as EntityPlayer).isSpectator && !shootingEntity!!.isDead && ticksInGround > 100 - loyalty * 20) {
                noClip = true

                if (!isReturning) {
                    playSound(Sounds.TRIDENT_LOYALTY, 1.0f, 1.0f)
                    isReturning = true
                }
                if (ticksInGround >= 100 - loyalty * 20) {
                    val d0 = 0.02 * loyalty.toDouble()
                    val vec3d = Vec3d(shootingEntity!!.posX - posX, shootingEntity!!.posY + shootingEntity!!.eyeHeight.toDouble() - posY, shootingEntity!!.posZ - posZ)
                    motionX += vec3d.x * d0 - motionX * 0.05
                    motionY += vec3d.y * d0 - motionY * 0.05
                    motionZ += vec3d.z * d0 - motionZ * 0.05

                    move(MoverType.SELF, motionX * 0.05, motionY * 0.05, motionZ * 0.05)
                }
            }
        }

        if (world.getClosestPlayerToEntity(this, 1.5) == shootingEntity && isReturning) {
            if (pickupStatus == PickupStatus.ALLOWED) {
                dropTridentStack()
            }
            setDead()
        }

        super.onUpdate()
    }

    override fun getIsCritical(): Boolean {
        return false
    }

    override fun isBurning(): Boolean {
        return false
    }

    override fun setFire(seconds: Int) {
        // Fireproof
    }

    private fun getDamageForTrident(target: Entity): Float {
        val level = EnchantHelper.getImpaling(thrownStack).toFloat()
        var damage = 8f

        if (level > 0 && target.isWet) {
            damage += (level * 1.25).toFloat()
        }
        return damage
    }

    private fun dropTridentStack() {
        thrownStack?.let {
            if (!it.isEmpty) {
                val item = EntityItem(world, posX, posY + 0.1.toFloat().toDouble(), posZ, thrownStack!!)
                item.setNoPickupDelay()

                if (captureDrops) {
                    capturedDrops.add(item)
                } else {
                    world.spawnEntity(item)
                }
            }
        }
    }

    companion object {
        private fun causeTridentDamage(trident: Entity, indirectEntityIn: Entity): DamageSource {
            return EntityDamageSourceIndirect("trident", trident, indirectEntityIn).setProjectile()
        }
    }
}