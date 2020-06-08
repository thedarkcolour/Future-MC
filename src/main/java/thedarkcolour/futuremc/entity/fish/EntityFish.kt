package thedarkcolour.futuremc.entity.fish

import net.minecraft.block.material.Material
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.MoverType
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.pathfinding.PathNavigate
import net.minecraft.pathfinding.PathNavigateSwimmer
import net.minecraft.util.DamageSource.DROWN
import net.minecraft.util.EntitySelectors
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import thedarkcolour.futuremc.entity.WaterCreature
import thedarkcolour.futuremc.registry.FSounds
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

abstract class EntityFish(worldIn: World) : EntityCreature(worldIn), WaterCreature {
    abstract val flopSound: SoundEvent
    var fromBucket: Boolean
        get() = dataManager[FROM_BUCKET]
        set(value) {
            dataManager[FROM_BUCKET] = value
        }

    init {
        moveHelper = MoveHelper(this)
    }

    private class MoveHelper(private val fish: EntityFish) : EntityMoveHelper(fish) {
        override fun onUpdateMoveHelper() {
            if (fish.isInsideOfMaterial(Material.WATER)) {
                fish.motionY += 0.005
            }

            if (action == Action.MOVE_TO && !fish.getNavigator().noPath()) {
                val d0: Double = this.posX - fish.posX
                var d1: Double = this.posY - fish.posY
                val d2: Double = this.posZ - fish.posZ
                val d3 = sqrt(d0 * d0 + d1 * d1 + d2 * d2)
                d1 /= d3
                val f = (atan2(d2, d0) * (180F / PI.toFloat()).toDouble()).toFloat() - 90.0F
                fish.rotationYaw = limitAngle(fish.rotationYaw, f, 90.0F)
                fish.renderYawOffset = fish.rotationYaw
                val f1 =
                    (speed * fish.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).attributeValue).toFloat()
                fish.aiMoveSpeed = fish.aiMoveSpeed + 0.125F * (f1 - fish.aiMoveSpeed)
                fish.motionY += fish.aiMoveSpeed.toDouble() * d1 * 0.1
            } else {
                fish.aiMoveSpeed = 0.0F
            }
        }
    }

    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = 3.0
    }

    override fun entityInit() {
        super.entityInit()
        dataManager.register(FROM_BUCKET, false)
    }

    override fun initEntityAI() {
        super.initEntityAI()
        tasks.addTask(0, EntityAIPanic(this, 1.25))
        tasks.addTask(
            2,
            EntityAIAvoidEntity(this, EntityPlayer::class.java, EntitySelectors.NOT_SPECTATING, 8F, 1.6, 1.4)
        )
        tasks.addTask(4, AISwim(this))
    }

    private class AISwim(private val fish: EntityFish) : EntityAIWander(fish, 1.0, 40) {
        override fun getPosition(): Vec3d? {
            var vec3d = RandomPositionGenerator.findRandomTarget(fish, 10, 7)

            var i = 0
            while (vec3d != null && fish.world.getBlockState(BlockPos(vec3d)).material != Material.WATER && i++ < 10) {
                vec3d = RandomPositionGenerator.findRandomTarget(fish, 10, 7)
            }

            return vec3d
        }

        override fun shouldExecute(): Boolean {
            return super.shouldExecute() && fish.shouldSwimAway()
        }
    }

    override fun createNavigator(worldIn: World): PathNavigate {
        return PathNavigateSwimmer(this, worldIn)
    }

    override fun travel(strafe: Float, vertical: Float, forward: Float) {
        if (isServerWorld && isInWater) {
            moveRelative(0.01F, strafe, vertical, forward)
            move(MoverType.SELF, motionX, motionY, motionZ)
            motionX *= 0.9
            motionY *= 0.9
            motionZ *= 0.9
            if (attackTarget == null) {
                motionY -= 0.005
            }
        } else {
            super.travel(strafe, vertical, forward)
        }
    }

    open fun shouldSwimAway(): Boolean = true

    override fun getEyeHeight(): Float = height * 0.65F

    override fun canDespawn(): Boolean {
        return !fromBucket && !hasCustomName()
    }

    override fun getMaxSpawnedInChunk() = 8

    override fun canBreatheUnderwater() = true

    override fun getCanSpawnHere() = true

    override fun isNotColliding(): Boolean {
        return world.checkNoEntityCollision(entityBoundingBox, this)
    }

    override fun getTalkInterval() = 120

    override fun getExperiencePoints(player: EntityPlayer?): Int {
        return 1 + rand.nextInt(3)
    }

    abstract override fun getSwimSound(): SoundEvent

    override fun onEntityUpdate() {
        if (!isInWater && onGround && collidedVertically) {
            motionX += (rand.nextFloat() * 2.0F - 1.0F) * 0.05F
            motionY += 0.4
            motionZ += (rand.nextFloat() * 2.0F - 1.0F) * 0.05F
            onGround = false
            isAirBorne = true
            playSound(flopSound, soundVolume, soundPitch)
        }

        var i = air
        super.onEntityUpdate()

        if (isEntityAlive && !isInWater) {
            air = --i

            if (air == -20) {
                air = 0
                attackEntityFrom(DROWN, 2.0F)
            }
        } else {
            air = 300
        }
    }

    override fun processInteract(player: EntityPlayer, hand: EnumHand): Boolean {
        val stack = player.getHeldItem(hand)
        return if (stack.item == Items.WATER_BUCKET && isEntityAlive) {
            playSound(FSounds.BUCKET_FILL_FISH, 1.0f, 1.0f)
            stack.shrink(1)

            val fishBucket = fishBucket
            setBucketData(fishBucket)

            if (stack.isEmpty) {
                player.setHeldItem(hand, fishBucket)
            } else if (!player.inventory.addItemStackToInventory(fishBucket)) {
                player.dropItem(fishBucket, false)
            }
            setDead()
            true
        } else {
            super.processInteract(player, hand)
        }
    }

    abstract val fishBucket: ItemStack

    open fun setBucketData(stack: ItemStack) {
        if (hasCustomName()) {
            stack.setStackDisplayName(customNameTag)
        }
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        compound.setBoolean("FromBucket", fromBucket)
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)
        fromBucket = compound.getBoolean("FromBucket")
    }

    abstract override fun getLootTable(): ResourceLocation

    companion object {
        private val FROM_BUCKET = EntityDataManager.createKey(EntityFish::class.java, DataSerializers.BOOLEAN)
    }
}