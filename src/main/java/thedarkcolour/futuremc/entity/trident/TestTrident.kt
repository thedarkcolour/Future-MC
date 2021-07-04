package thedarkcolour.futuremc.entity.trident

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSourceIndirect
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import thedarkcolour.futuremc.enchantment.EnchantHelper
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

class TestTrident : AbstractArrow {
    private var thrownStack = ItemStack(FItems.TRIDENT)
    private var dealtDamage = false
    var returningTicks = 0

    constructor(worldIn: World) : super(worldIn)

    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(x, y, z, worldIn)

    constructor(worldIn: World, shooter: EntityLivingBase, stack: ItemStack) : super(shooter, worldIn) {
        thrownStack = stack.copy()

        dataManager.set(LOYALTY, EnchantHelper.getLoyalty(thrownStack).toByte())
        dataManager.set(GLINT, thrownStack.hasEffect())
    }

    override fun entityInit() {
        super.entityInit()

        dataManager.register(LOYALTY, 0.toByte())
        dataManager.register(GLINT, false)
    }

    override fun onUpdate() {
        if (timeInGround > 4) {
            dealtDamage = true
        }

        val shooter = getOwner()
        if ((this.dealtDamage || getNoClip()) && shooter != null) {
            val loyalty = dataManager.get(LOYALTY)

            if (loyalty > 0 && !shouldReturnToThrower()) {
                if (!world.isRemote && status == EntityArrow.PickupStatus.ALLOWED) {
                    entityDropItem(getArrowStack(), 0.1f)
                }
                setDead()
            } else if (loyalty > 0) {
                setNoClip(true)
                val vector3d = Vec3d(shooter.posX - posX, shooter.eyeHeight - posY, shooter.posZ - posZ)
                posY += vector3d.y * 0.015 * loyalty.toDouble()
                if (world.isRemote) {
                    lastTickPosY = posY
                }
                val d0 = 0.05 * loyalty.toDouble()
                val motion = Vec3d(motionX, motionY, motionZ).scale(0.95).add(vector3d.normalize().scale(d0))
                motionX = motion.x
                motionY = motion.y
                motionZ = motion.z
                if (returningTicks == 0) {
                    playSound(FSounds.TRIDENT_LOYALTY, 10.0f, 1.0f)
                }
                ++returningTicks
            }
        }

        super.onUpdate()
    }

    private fun shouldReturnToThrower(): Boolean {
        val shooter = getOwner()
        return shooter != null && !shooter.isDead && (shooter !is EntityPlayerMP || !shooter.isSpectator)
    }

    override fun getArrowStack(): ItemStack {
        return thrownStack.copy()
    }

    fun hasGlint() = dataManager.get(GLINT)

    override fun rayTraceEntities(start: Vec3d, end: Vec3d): RayTraceResult? {
        return if (dealtDamage) {
            null
        } else {
            super.rayTraceEntities(start, end)
        }
    }

    override fun hitEntity(result: RayTraceResult) {
        val target = result.entityHit
        var f = 8.0f
        if (target is EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(thrownStack, target.creatureAttribute)
        }

        val shooter = getOwner()
        val source = causeTridentDamage(this, (shooter ?: this))
        dealtDamage = true
        val hitSound = FSounds.TRIDENT_IMPACT

        if (target.attackEntityFrom(source, f)) {
            if (target is EntityEnderman) {
                return
            }
            if (target is EntityLivingBase) {
                if (shooter is EntityLivingBase) {
                    EnchantmentHelper.applyThornEnchantments(target, shooter)
                    EnchantmentHelper.applyArthropodEnchantments(shooter, target)
                }
                arrowHit(target)
            }
        }

        motionX *= -0.01
        motionY *= -0.1
        motionZ *= -0.01

        var f1 = 1.0f

        if (world is WorldServer && world.isThundering && EnchantHelper.getChanneling(thrownStack)) {
            val blockpos = target.position
            if (world.canSeeSky(blockpos)) {
                world.addWeatherEffect(EntityLightningBolt(world, posX, posY, posZ, false))
                playSound(FSounds.TRIDENT_CONDUCTIVIDAD, 5.0f, 1.0f)
                f1 = 5.0f
            }
        }

        playSound(hitSound, f1, 1.0f)
    }

    override fun getHitEntitySound(): SoundEvent {
        return FSounds.TRIDENT_IMPACT
    }

    override fun onCollideWithPlayer(entityIn: EntityPlayer) {
        val shooter = getOwner()
        if (shooter == null || shooter.uniqueID == entityIn.uniqueID) {
            super.onCollideWithPlayer(entityIn)
        }
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)
        if (compound.hasKey("Trident", 10)) {
            thrownStack = ItemStack(compound.getCompoundTag("Trident"))
        }

        dealtDamage = compound.getBoolean("DealtDamage")
        dataManager.set(LOYALTY, EnchantHelper.getLoyalty(thrownStack).toByte())
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)

        compound.setTag("Trident", thrownStack.writeToNBT(NBTTagCompound()))
        compound.setBoolean("DealtDamage", dealtDamage)
    }

    override fun func_225516_i_() {
        val i = dataManager.get(LOYALTY)

        if (status != EntityArrow.PickupStatus.ALLOWED || i <= 0) {
            super.func_225516_i_()
        }
    }

    override fun getUnderwaterModifier(): Float {
        return 0.99f
    }

    override fun isInRangeToRender3d(x: Double, y: Double, z: Double): Boolean {
        return true
    }

    private fun causeTridentDamage(trident: Entity, indirectEntityIn: Entity): DamageSource {
        return EntityDamageSourceIndirect("trident", trident, indirectEntityIn).setProjectile()
    }

    companion object {
        val LOYALTY = EntityDataManager.createKey(TestTrident::class.java, DataSerializers.BYTE)
        val GLINT = EntityDataManager.createKey(TestTrident::class.java, DataSerializers.BOOLEAN)
    }
}