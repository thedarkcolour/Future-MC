package thedarkcolour.futuremc.entity.fish.pufferfish

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityWaterMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.entity.WaterCreature
import thedarkcolour.futuremc.entity.fish.EntityFish
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

class EntityPufferfish(worldIn: World) : EntityFish(worldIn) {
    var inflateTimer = 0
    var deflateTimer = 0
    var puffState: Int
        get() {
            return dataManager[PUFF_STATE]
        }
        set(value) {
            dataManager[PUFF_STATE] = value
        }

    init {
        setSize(0.7F, 0.7F)
    }

    override fun entityInit() {
        super.entityInit()
        dataManager.register(PUFF_STATE, 0)
    }

    override fun notifyDataManagerChange(key: DataParameter<*>) {
        if (PUFF_STATE == key) {
            val f = getPuffSize()
            setSize(f, f)
        }
        super.notifyDataManagerChange(key)
    }

    private fun getPuffSize(): Float {
        return when (puffState) {
            0 -> 0.5f
            1 -> 0.7f
            else -> 1.0f
        }
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        compound.setInteger("PuffState", puffState)
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)
        puffState = compound.getInteger("PuffState")
    }

    override val fishBucket = ItemStack(FItems.PUFFERFISH_BUCKET)

    override fun initEntityAI() {
        super.initEntityAI()
        tasks.addTask(1, AIPuff(this))
    }

    override fun onUpdate() {
        if (!world.isRemote && isEntityAlive && isServerWorld) {
            if (inflateTimer > 0) {
                if (puffState == 0) {
                    playSound(FSounds.PUFFERFISH_INFLATE, soundVolume, soundPitch)
                    world.getEntitiesWithinAABB(
                        EntityLivingBase::class.java,
                        entityBoundingBox.grow(2.0),
                        ENEMY_MATCHER
                    )
                    puffState = 1
                } else if (inflateTimer > 40 && puffState == 1) {
                    playSound(FSounds.PUFFERFISH_INFLATE, soundVolume, soundPitch)
                    puffState = 2
                }
                ++inflateTimer
            } else if (puffState != 0) {
                if (deflateTimer > 60 && puffState == 2) {
                    playSound(FSounds.PUFFERFISH_DEFLATE, soundVolume, soundPitch)
                    puffState = 1
                } else if (deflateTimer > 100 && puffState == 1) {
                    playSound(FSounds.PUFFERFISH_DEFLATE, soundVolume, soundPitch)
                    puffState = 0
                }
                ++deflateTimer
            }
        }

        super.onUpdate()
    }

    override fun onLivingUpdate() {
        super.onLivingUpdate()
        if (isEntityAlive && puffState > 0) {
            for (mob in world.getEntitiesWithinAABB(
                EntityMob::class.java,
                entityBoundingBox.grow(0.3),
                ENEMY_MATCHER
            )) {
                if (mob.isEntityAlive) {
                    attack(mob)
                }
            }
        }
    }

    private fun attack(mob: EntityMob) {
        val i = puffState
        if (mob.attackEntityFrom(DamageSource.causeMobDamage(this), (1 + i).toFloat())) {
            mob.addPotionEffect(PotionEffect(MobEffects.POISON, 60 * i, 0))
            playSound(FSounds.PUFFERFISH_STING, 1.0f, 1.0f)
        }
    }

    override fun onCollideWithPlayer(playerIn: EntityPlayer) {
        val i: Int = puffState
        if (playerIn is EntityPlayerMP && i > 0 && playerIn.attackEntityFrom(
                DamageSource.causeMobDamage(this),
                (1 + i).toFloat()
            )
        ) {
            //entityIn.connection.sendPacket(SPacketSting(0F))
            playerIn.world.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, FSounds.PUFFERFISH_STING, SoundCategory.NEUTRAL, 1F, 1F)
            playerIn.addPotionEffect(PotionEffect(MobEffects.POISON, 60 * i, 0))
        }
    }

    override fun getDeathSound() = FSounds.PUFFERFISH_DEATH
    override fun getHurtSound(damageSourceIn: DamageSource?) = FSounds.PUFFERFISH_HURT
    override val flopSound = FSounds.PUFFERFISH_FLOP
    override fun getSwimSound() = FSounds.PUFFERFISH_SWIM

    override fun getLootTable() = LOOT_TABLE

    class AIPuff(private val pufferfish: EntityPufferfish) : EntityAIBase() {
        override fun shouldExecute(): Boolean {
            val list = pufferfish.world.getEntitiesWithinAABB(
                EntityLivingBase::class.java,
                pufferfish.entityBoundingBox.grow(2.0),
                ENEMY_MATCHER
            )
            return list.isNotEmpty()
        }

        override fun startExecuting() {
            pufferfish.inflateTimer = 1
            pufferfish.deflateTimer = 0
        }

        override fun resetTask() {
            pufferfish.inflateTimer = 0
        }
    }

    companion object {
        private val PUFF_STATE = EntityDataManager.createKey(EntityPufferfish::class.java, DataSerializers.VARINT)
        val LOOT_TABLE = ResourceLocation(FutureMC.ID, "entities/pufferfish")
        private val ENEMY_MATCHER = { entityLiving: EntityLivingBase? ->
            if (entityLiving == null) {
                false
            } else if (entityLiving !is EntityPlayer || (!entityLiving.isSpectator && !entityLiving.isCreative)) {
                entityLiving !is WaterCreature && entityLiving !is EntityWaterMob
            } else {
                false
            }
        }
    }
}