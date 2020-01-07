package thedarkcolour.futuremc.entity.drowned

import net.minecraft.block.material.Material
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Biomes
import net.minecraft.init.Items
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.pathfinding.PathNavigateGround
import net.minecraft.pathfinding.PathNavigateSwimmer
import net.minecraft.pathfinding.PathNodeType
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.EnumDifficulty
import net.minecraft.world.World
import thedarkcolour.core.util.lerp
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.entity.trident.EntityTrident
import thedarkcolour.futuremc.init.FItems
import thedarkcolour.futuremc.init.Sounds
import kotlin.math.*

class EntityDrowned(world: World) : EntityZombie(world), IRangedAttackMob {
    private val waterNavigator = PathNavigateSwimmer(this, world)
    private val groundNavigator = PathNavigateGround(this, world)
    private var swimmingUp = false
    private var swimAnimation = 0.0f
    private var lastSwimAnimation = 0.0f

    init {
        stepHeight = 1.0f
        moveHelper = MoveHelper(this)
        setPathPriority(PathNodeType.WATER, 0.0f)
    }

    private class MoveHelper(entity: EntityDrowned) : EntityMoveHelper(entity) {
        override fun onUpdateMoveHelper() {
            val target = entity.attackTarget
            entity as EntityDrowned

            if (entity.shouldSwim() && entity.isInWater) {
                if ((target != null && target.posY > entity.posY) || entity.swimmingUp) {
                    entity.posY += 0.002
                }

                if (action != Action.MOVE_TO || entity.navigator.noPath()) {
                    entity.aiMoveSpeed = 0.0f
                }
                val x = posX - entity.posX
                var y = posY - entity.posY
                val z = posZ - entity.posZ
                val distance = sqrt(x * x + y * y + z * z)
                y /= distance
                val f = atan2(x, z) * (180 / PI) - 90.0f
                entity.rotationYaw = limitAngle(entity.rotationYaw, f.toFloat(), 90.0f)
                entity.renderYawOffset = entity.rotationYaw
                val i = speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).attributeValue
                val j = lerp(0.125f, entity.aiMoveSpeed, i.toFloat())
                entity.aiMoveSpeed = j
                entity.motionX += j * x * 0.005
                entity.motionY += j * y * 0.1
                entity.motionZ += j * z * 0.005
            } else {
                if (entity.onGround) {
                    entity.motionY -= 0.008
                }
                super.onUpdateMoveHelper()
            }
        }
    }

    override fun getCanSpawnHere(): Boolean {
        val pos = position
        val biome = world.getBiome(pos)
        val flag = world.difficulty != EnumDifficulty.PEACEFUL && isValidLightLevel && world.getBlockState(pos).material == Material.WATER
        return if (biome != Biomes.RIVER && biome != Biomes.FROZEN_RIVER) {
            rand.nextInt(40) == 0 && waterIsDeepEnough(world, pos) && flag
        } else {
            rand.nextInt(15) == 0 && flag
        }
    }

    override fun onInitialSpawn(difficulty: DifficultyInstance, livingdata: IEntityLivingData?): IEntityLivingData? {
        val entityData = super.onInitialSpawn(difficulty, livingdata)
        if (getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty && rand.nextFloat() < 0.03) {
            setItemStackToSlot(EntityEquipmentSlot.OFFHAND, FItems.NAUTILUS_SHELL.stack)
            inventoryHandsDropChances[EntityEquipmentSlot.OFFHAND.index] = 2.0f
        }

        return entityData
    }

    private fun waterIsDeepEnough(world: World, pos: BlockPos): Boolean {
        return pos.y < world.seaLevel - 5
    }

    override fun getAmbientSound(): SoundEvent = Sounds.DROWNED_AMBIENT

    override fun getHurtSound(damageSourceIn: DamageSource): SoundEvent = Sounds.DROWNED_HURT

    override fun getDeathSound(): SoundEvent = Sounds.DROWNED_DEATH

    override fun getStepSound(): SoundEvent = Sounds.DROWNED_STEP

    override fun getSwimSound(): SoundEvent = Sounds.DROWNED_SWIM

    override fun getSkullDrop(): ItemStack = ItemStack.EMPTY

    override fun setBreakDoorsAItask(enabled: Boolean) {}

    override fun setEquipmentBasedOnDifficulty(difficulty: DifficultyInstance) {
        if (rand.nextFloat() > 0.9f) {
            if (rand.nextInt(16) < 10) {
                setItemStackToSlot(EntityEquipmentSlot.MAINHAND, FItems.TRIDENT.stack)
            } else {
                setItemStackToSlot(EntityEquipmentSlot.MAINHAND, Items.FISHING_ROD.stack)
            }
        }
    }

    override fun updateEquipmentIfNeeded(itemEntity: EntityItem) {
        val slot = EntityLiving.getSlotForItemStack(itemEntity.item)
        val existing = getItemStackFromSlot(slot)
        val candidate = itemEntity.item
        var flag = false

        if (existing.item == FItems.TRIDENT) {
            if (candidate.item == FItems.TRIDENT) {
                if (candidate.itemDamage < existing.itemDamage) {
                    flag = true
                }
            }
        } else if (candidate.item == FItems.TRIDENT) {
            flag = true
        }

        if (flag) {
            val d0: Double = when (slot.slotType) {
                EntityEquipmentSlot.Type.HAND -> inventoryHandsDropChances[slot.index].toDouble()
                EntityEquipmentSlot.Type.ARMOR -> inventoryArmorDropChances[slot.index].toDouble()
                else -> 0.0
            }
            if (!existing.isEmpty && (rand.nextFloat() - 0.1f).toDouble() < d0) {
                entityDropItem(existing, 0.0f)
            }
            setItemStackToSlot(slot, candidate)
            when (slot.slotType) {
                EntityEquipmentSlot.Type.HAND -> inventoryHandsDropChances[slot.index] = 2.0f
                EntityEquipmentSlot.Type.ARMOR -> inventoryArmorDropChances[slot.index] = 2.0f
                else -> {}
            }
            enablePersistence()
            onItemPickup(itemEntity, candidate.count)
            itemEntity.setDead()
        }
    }

    override fun isNotColliding(): Boolean {
        return world.checkNoEntityCollision(this.entityBoundingBox)
    }

    private var isSwimming
        get() = dataManager[SWIMMING]
        set(value) {
            dataManager[SWIMMING] = value
        }

    override fun isPushedByWater(): Boolean {
        return !isSwimming
    }

    override fun onEntityUpdate() {
        super.onEntityUpdate()

        updateSwimming()
        updateSwimmingAnimation()
    }

    private fun updateSwimming() {
        if (!world.isRemote) {
            if (isServerWorld && isInWater && shouldSwim()) {
                navigator = waterNavigator
                isSwimming = true
            } else {
                navigator = groundNavigator
                isSwimming = false
            }
        }
    }

    private fun updateSwimmingAnimation() {
        lastSwimAnimation = swimAnimation

        swimAnimation = if (swimIsDecreasing()) {
            min(1.0f, swimAnimation + 0.9f)
        } else {
            max(0.0f, swimAnimation - 0.9f)
        }
    }

    private fun swimIsDecreasing(): Boolean {
        return isSwimming || !isElytraFlying
    }

    override fun travel(strafe: Float, vertical: Float, forward: Float) {
        if (isServerWorld && isInWater && shouldSwim()) {
            moveRelative(0.01f, strafe, vertical, forward)
            move(MoverType.SELF, motionX, motionY, motionZ)
            motionX *= 0.9
            motionY *= 0.9
            motionZ *= 0.9
        } else {
            super.travel(strafe, vertical, forward)
        }
    }

    private fun shouldSwim(): Boolean {
        return swimmingUp || attackTarget?.isInWater ?: false
    }

    override fun entityInit() {
        super.entityInit()
        dataManager.register(DROWNING, false)
        dataManager.register(SWINGING_ARMS, false)
        dataManager.register(SWIMMING, false)
    }

    override fun applyEntityAI() {
        tasks.addTask(1, AIGoToWater(this))
        tasks.addTask(2, AITridentAttack(this))
        tasks.addTask(2, AIAttack(this))
        tasks.addTask(5, AIGoToBeach(this))
        tasks.addTask(6, AISwimUp(this))
        tasks.addTask(7, EntityAIWander(this, 1.0))
        targetTasks.addTask(1, EntityAIHurtByTarget(this, true, EntityDrowned::class.java))
        targetTasks.addTask(2, EntityAINearestAttackableTarget(this, EntityPlayer::class.java, 10, true, false, ::shouldAttack))
        targetTasks.addTask(3, EntityAINearestAttackableTarget(this, EntityVillager::class.java, false))
        targetTasks.addTask(3, EntityAINearestAttackableTarget(this, EntityIronGolem::class.java, true  ))
        //targetTasks.addTask(5, EntityAINearestAttackableTarget(this, EntityTurtle::class.java, 10, true, false, ::shouldAttack))
    }

    private class AIGoToWater(val entity: EntityDrowned) : EntityAIBase() {
        private val world = entity.world
        private var x: Double = 0.0
        private var y: Double = 0.0
        private var z: Double = 0.0

        init {
            mutexBits = 0x1
        }

        override fun shouldExecute(): Boolean {
            return if (!world.isDaytime) {
                false
            } else if (entity.isInWater) {
                false
            } else {
                val vec3d = findDestination()
                vec3d?.let { vec ->
                    x = vec.x
                    y = vec.y
                    z = vec.z
                    true
                } ?: false
            }
        }

        private fun findDestination(): Vec3d? {
            val rand = entity.rand
            val pos = BlockPos(entity.posX, entity.entityBoundingBox.minY, entity.posZ)

            for (i in 0..9) {
                val pos1 = pos.add(rand.nextInt(20) - 10, 2 - rand.nextInt(8), rand.nextInt(20) - 10)
                if (world.getBlockState(pos1).material == Material.WATER) {
                    return Vec3d(pos1)
                }
            }

            return null
        }

        override fun startExecuting() {
            entity.navigator.tryMoveToXYZ(x, y, z, 1.0)
        }

        override fun shouldContinueExecuting(): Boolean {
            return !entity.navigator.noPath()
        }
    }

    private class AITridentAttack(val entity: EntityDrowned) : EntityAIAttackRanged(entity, 1.0, 40, 10.0f) {
        override fun shouldExecute(): Boolean {
            return super.shouldExecute() && entity.heldItemMainhand.item == FItems.TRIDENT
        }

        override fun startExecuting() {
            super.startExecuting()
            entity.setSwingingArms(true)
            entity.activeHand = EnumHand.MAIN_HAND
        }

        override fun resetTask() {
            super.resetTask()
            entity.resetActiveHand()
            entity.setSwingingArms(false)
        }
    }

    private class AIAttack(val entity: EntityDrowned) : EntityAIZombieAttack(entity, 1.0, false) {
        override fun shouldExecute(): Boolean {
            return super.shouldExecute() && entity.shouldAttack(entity.attackTarget)
        }

        override fun shouldContinueExecuting(): Boolean {
            return super.shouldContinueExecuting() && entity.shouldAttack(entity.attackTarget)
        }
    }

    private class AIGoToBeach(val entity: EntityDrowned) : EntityAIMoveToBlock(entity, 1.0, 8) {
        override fun shouldMoveTo(worldIn: World, pos: BlockPos): Boolean {
            val up = pos.up()
            return worldIn.isAirBlock(up) && worldIn.isAirBlock(up.up()) && worldIn.getBlockState(pos).isSideSolid(worldIn, pos, EnumFacing.UP)
        }

        override fun shouldExecute(): Boolean {
            return super.shouldExecute() && !entity.world.isDaytime && entity.isInWater && entity.posY >= entity.world.seaLevel - 3
        }

        override fun startExecuting() {
            entity.swimmingUp = false
            entity.navigator = entity.groundNavigator
            super.startExecuting()
        }
    }

    private class AISwimUp(val entity: EntityDrowned) : EntityAIBase() {
        private val targetY = entity.world.seaLevel
        private var obstructed = false

        override fun shouldExecute(): Boolean {
            return !entity.world.isDaytime && entity.isInWater && entity.posY < targetY - 2
        }

        override fun startExecuting() {
            entity.swimmingUp = true
            obstructed = false
        }

        override fun updateTask() {
            if (entity.posY < targetY - 1 && entity.navigator.noPath() || entity.isCloseToPathTarget()) {
                val vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(entity, 4, 8, Vec3d(entity.posX, (targetY - 1).toDouble(), entity.posZ))
                if (vec3d == null) {
                    obstructed = true
                    return
                }

                entity.navigator.tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0)
            }
        }

        override fun shouldContinueExecuting(): Boolean {
            return shouldExecute() && !obstructed
        }

        override fun resetTask() {
            entity.swimmingUp = false
        }
    }

    private fun isCloseToPathTarget(): Boolean {
        val path = navigator.path
        if (path != null) {
            val pos = path.finalPathPoint
            if (pos != null) {
                val distance = getDistanceSq(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                if (distance < 4.0) {
                    return true
                }
            }
        }

        return false
    }

    fun shouldAttack(entity: EntityLivingBase?): Boolean {
        return if (entity != null) {
            !world.isDaytime || entity.isInWater
        } else {
            false
        }
    }

    override fun attackEntityWithRangedAttack(target: EntityLivingBase, distanceFactor: Float) {
        val trident = EntityTrident(world, this, FItems.TRIDENT.stack)
        val x = target.posX - posX
        val y = target.entityBoundingBox.minY + (target.height / 3.0f) - trident.posY
        val z = target.posZ - posZ
        val v = sqrt(x * x + z * z)
        trident.shoot(x, y + v * 0.2f, z, 1.6f, 14 - (world.difficulty.id * 4).toFloat())
        playSound(Sounds.TRIDENT_THROW, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.8f)
        world.spawnEntity(trident)
    }

    override fun setSwingingArms(swingingArms: Boolean) {
        dataManager[SWINGING_ARMS] = swingingArms
    }

    fun isSwingingArms() = dataManager[SWINGING_ARMS]

    fun getSwimAnimation(partialTicks: Float): Float {
        return lerp(partialTicks, lastSwimAnimation, swimAnimation)
    }

    companion object {
        private val DROWNING = EntityDataManager.createKey(EntityDrowned::class.java, DataSerializers.BOOLEAN)
        private val SWINGING_ARMS = EntityDataManager.createKey(EntityDrowned::class.java, DataSerializers.BOOLEAN)
        private val SWIMMING = EntityDataManager.createKey(EntityDrowned::class.java, DataSerializers.BOOLEAN)
    }
}