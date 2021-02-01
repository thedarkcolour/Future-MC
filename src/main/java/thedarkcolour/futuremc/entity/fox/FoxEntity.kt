package thedarkcolour.futuremc.entity.fox

import com.google.common.base.Optional
import com.google.common.base.Predicate
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityChicken
import net.minecraft.entity.passive.EntityRabbit
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.world.World
import thedarkcolour.futuremc.entity.fish.EntityFish

class FoxEntity(worldIn: World) : EntityAnimal(worldIn) {
    private lateinit var attackAnimals: EntityAIBase
    //private lateinit var attackTurtles: EntityAIBase
    private lateinit var attackFish: EntityAIBase
    private var interestedAngle = 0.0f
    private var interestedAngleO = 0.0f
    private var crouchAmount = 0.0f
    private var crouchAmountO = 0.0f
    private var eatTicks = 0

    override fun entityInit() {
        super.entityInit()
        dataManager.register(TRUSTED_UUID_SECONDARY, Optional.absent())
        dataManager.register(TRUSTED_UUID_MAIN, Optional.absent())
        dataManager.register(FOX_TYPE, 0)
        dataManager.register(FOX_FLAGS, 0.toByte())
    }

    override fun initEntityAI() {
        attackAnimals = EntityAINearestAttackableTarget(this, EntityAnimal::class.java, 10, false, false, IS_PREY)
        //attackTurtles = EntityAINearestAttackableTarget(this, EntityTurtle::class.java)
        attackFish = EntityAINearestAttackableTarget(this, EntityFish::class.java, 20, false, false) { entity -> entity is EntityFish }
    }

    override fun createChild(p0: EntityAgeable): EntityAgeable? {
        TODO("not implemented")
    }

    @Suppress("SENSELESS_COMPARISON")
    companion object {
        private val FOX_TYPE = EntityDataManager.createKey(FoxEntity::class.java, DataSerializers.VARINT)
        private val FOX_FLAGS = EntityDataManager.createKey(FoxEntity::class.java , DataSerializers.BYTE)
        private val TRUSTED_UUID_SECONDARY = EntityDataManager.createKey(FoxEntity::class.java, DataSerializers.OPTIONAL_UNIQUE_ID)
        private val TRUSTED_UUID_MAIN = EntityDataManager.createKey(FoxEntity::class.java, DataSerializers.OPTIONAL_UNIQUE_ID)
        private val TRUSTED_TARGET_SELECTOR = { item: EntityItem ->
            !item.cannotPickup() && item.isEntityAlive
        }
        private val STALKABLE_PREY = { entity: Entity ->
            entity is EntityLivingBase && entity.lastAttackedEntity != null && entity.lastAttackedEntityTime < entity.ticksExisted + 600
        }
        private val IS_PREY = Predicate<Entity> { entity: Entity? ->
            entity is EntityChicken || entity is EntityRabbit
        }
        private val SHOULD_AVOID = { entity: Entity ->
            !entity.isSneaking && (entity !is EntityPlayer || !entity.isSpectator && !entity.isCreative)
        }
    }
}