@file:Suppress("DEPRECATION")

package thedarkcolour.futuremc.entity.bee

import net.minecraft.block.Block
import net.minecraft.block.BlockFlower.EnumFlowerType
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.*
import net.minecraft.entity.ai.EntityAIFollowParent
import net.minecraft.entity.ai.EntityAIMate
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityFlying
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.pathfinding.PathNavigate
import net.minecraft.pathfinding.PathNavigateFlying
import net.minecraft.pathfinding.PathNodeType
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.EnumDifficulty
import net.minecraft.world.World
import thedarkcolour.core.util.isAir
import thedarkcolour.core.util.lerp
import thedarkcolour.futuremc.entity.bee.ai.*
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.tile.BeeHiveTile
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.max
import kotlin.math.min

class BeeEntity(worldIn: World) : EntityAnimal(worldIn), EntityFlying {
    private var lastHurtBy: UUID? = null
    private var rollAmount = 0.0f
    private var rollAmountO = 0.0f
    private var ticksSinceSting = 0
    var ticksSincePollination = 0
    var cannotEnterHiveTicks = 0
    private var cropsGrownSincePollination = 0
    var findHiveCooldown = 0
    var findFlowerCooldown = 0
    var flowerPos: BlockPos? = null
    var hivePos: BlockPos? = null
    var pollinateAI: PollinateAI? = null
    lateinit var goToHiveAI: GoToHiveAI

    init {
        moveHelper = FlyHelper(this)
        lookHelper = LookHelper(this)

        setSize(0.7F, 0.7F)
        setPathPriority(PathNodeType.WATER, -1F)
    }

    override fun entityInit() {
        super.entityInit()
        this.dataManager.register(BEE_FLAGS, 0.toByte())
        this.dataManager.register(ANGER, 0)
    }

    override fun getBlockPathWeight(pos: BlockPos): Double {
        return if (world.isAir(pos)) 10.0 else 0.0
    }

    override fun initEntityAI() {
        tasks.addTask(0, StingAI(this, 1.399999976158142, true))
        tasks.addTask(1, EnterHiveAI(this))
        tasks.addTask(2, EntityAIMate(this, 1.0))
        tasks.addTask(3, TemptAI(this))
        pollinateAI = PollinateAI(this)
        tasks.addTask(4, pollinateAI!!)
        tasks.addTask(5, EntityAIFollowParent(this, 1.25))
        tasks.addTask(5, LocateHiveAI(this))
        goToHiveAI = GoToHiveAI(this)
        tasks.addTask(5, goToHiveAI)
        tasks.addTask(6, GoToFlowerAI(this))
        tasks.addTask(7, GrowCropsAI(this))
        tasks.addTask(8, WanderAI(this))
        tasks.addTask(9, EntityAISwimming(this))
        targetTasks.addTask(1, RevengeAI(this))
        targetTasks.addTask(2, SwarmAttackerAI(this))
    }


    fun doesFlowerExist(pos: BlockPos): Boolean {
        return isFlowerValid(world.getBlockState(pos))
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        if (hasHive()) {
            compound.setTag("HivePos", NBTUtil.createPosTag(hivePos))
        }
        if (hasFlower()) {
            compound.setTag("FlowerPos", NBTUtil.createPosTag(flowerPos))
        }
        compound.setBoolean("HasNectar", hasNectar())
        compound.setBoolean("HasStung", hasStung())
        compound.setInteger("TicksSincePollination", ticksSincePollination)
        compound.setInteger("CannotEnterHiveTicks", cannotEnterHiveTicks)
        compound.setInteger("CropsGrownSincePollination", cropsGrownSincePollination)
        compound.setInteger("Anger", getAnger())

        if (lastHurtBy != null) {
            compound.setString("HurtBy", lastHurtBy.toString())
        } else {
            compound.setString("HurtBy", "")
        }
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        hivePos = if (compound.hasKey("HivePos")) {
            NBTUtil.getPosFromTag(compound.getCompoundTag("HivePos"))
        } else null
        flowerPos = if (compound.hasKey("FlowerPos")) {
            NBTUtil.getPosFromTag(compound.getCompoundTag("FlowerPos"))
        } else null
        super.readEntityFromNBT(compound)
        setHasNectar(compound.getBoolean("HasNectar"))
        setHasStung(compound.getBoolean("HasStung"))
        setAnger(compound.getInteger("Anger"))
        ticksSincePollination = compound.getInteger("TicksSincePollination")
        cannotEnterHiveTicks = compound.getInteger("CannotEnterHiveTicks")
        cropsGrownSincePollination = compound.getInteger("CropsGrownSincePollination")

        val s = compound.getString("HurtBy")
        if (s.isNotEmpty()) {
            lastHurtBy = UUID.fromString(s)
            val player = world.getPlayerEntityByUUID(lastHurtBy!!)
            revengeTarget = player
            if (player != null) {
                attackingPlayer = player
                recentlyHit = revengeTimer
            }
        }
    }

    override fun attackEntityAsMob(entityIn: Entity): Boolean {
        val flag = entityIn.attackEntityFrom(
            causeBeeDamage(this),
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).attributeValue.toFloat()
        )
        if (flag) {
            applyEnchantments(this, entityIn)
            if (entityIn is EntityLivingBase) {
                // TODO Stingers
                //((EntityLivingBase)entityIn).addStinger(((LivingEntity)entity_1).getStingers() + 1); Puts a stinger inside the hit entity, like arrows
                var duration = 0
                if (world.difficulty == EnumDifficulty.NORMAL) {
                    duration = 10
                } else if (world.difficulty == EnumDifficulty.HARD) {
                    duration = 18
                }

                if (duration > 0) {
                    entityIn.addPotionEffect(PotionEffect(MobEffects.POISON, duration * 20, 0))
                }
            }

            setHasStung(true)
            attackTarget = null
            playSound(FSounds.BEE_STING, 1.0f, 1.0f)
        }

        return flag
    }

    private fun causeBeeDamage(entityBee: BeeEntity): EntityDamageSource {
        return EntityDamageSource("sting", entityBee)
    }

    override fun onUpdate() {
        super.onUpdate()
        /*if (hasNectar()) {
            for (i in 0..(rand.nextInt(2) + 1)) {
                spawnParticle(FParticles.FALLING_BEE_NECTAR, posX - 0.3, posX + 0.3, posZ - 0.3f, posZ + 0.3f, getBodyHeight(0.5f))
            }
        }*/
        updateBodyPitch()
    }

    private fun spawnParticle(particleType: EnumParticleTypes, lastX: Double, nextX: Double, lastZ: Double, nextZ: Double, middle: Double) {
        world.spawnParticle(particleType, lerp(rand.nextDouble(), lastX, nextX), middle, lerp(rand.nextDouble(), lastX, lastZ), 0.0, 0.0, 0.0)
    }

    fun hasFlower() = flowerPos != null

    private fun getBodyHeight(partialTicks: Float): Double {
        return posY + height * partialTicks
    }

    private fun failedPollinatingTooLong(): Boolean {
        return ticksSincePollination > 3600
    }

    fun canEnterHive(): Boolean {
        return if (cannotEnterHiveTicks <= 0 && !pollinateAI!!.isRunning && !hasStung()) {
            val flag = hasNectar() || failedPollinatingTooLong() || world.isRaining || !world.isDaytime
            flag && !isHiveNearFire()
        } else {
            false
        }
    }

    fun getBodyPitch(partialTicks: Float): Float {
        return lerp(partialTicks, rollAmountO, rollAmount)
    }

    private fun updateBodyPitch() {
        rollAmountO = rollAmount

        rollAmount = if (this.isNearTarget()) {
            min(1.0f, rollAmount + 0.2f)
        } else {
            max(0.0f, rollAmount - 0.2f)
        }
    }

    override fun setRevengeTarget(entityIn: EntityLivingBase?) {
        super.setRevengeTarget(entityIn)

        if (entityIn != null) {
            lastHurtBy = entityIn.uniqueID
        }
    }

    override fun updateAITasks() {
        if (hasStung()) {
            ++ticksSinceSting

            if (ticksSinceSting % 5 == 0 && rand.nextInt(MathHelper.clamp(1200 - ticksSinceSting, 1, 1200)) == 0) {
                attackEntityFrom(DamageSource.GENERIC, health)
            }
        }

        if (isAngry()) {
            val anger = getAnger()
            val livingEntity = attackTarget
            setAnger(anger - 1)

            if (anger == 0 && livingEntity != null) {
                setBeeAttacker(livingEntity)
            }
        }

        if (!hasNectar()) {
            ++ticksSincePollination
        }
    }

    fun resetPollinationTicks() {
        ticksSincePollination = 0
    }

    private fun isHiveNearFire(): Boolean {
        return if (hasHive()) {
            false
        } else {
            val hive = world.getTileEntity(hivePos ?: return false)
            hive is BeeHiveTile && hive.isNearFire()
        }
    }

    fun isAngry() = getAnger() > 0

    private fun getAnger(): Int {
        return dataManager.get(ANGER)
    }

    private fun setAnger(anger: Int) {
        dataManager.set(ANGER, anger)
    }

    fun hasHive(): Boolean {
        return hivePos != null
    }

    fun getCropsGrownSincePollination(): Int {
        return cropsGrownSincePollination
    }

    private fun resetCropCounter() {
        cropsGrownSincePollination = 0
    }

    fun addCropCounter() {
        ++cropsGrownSincePollination
    }

    override fun onLivingUpdate() {
        super.onLivingUpdate()

        if (!world.isRemote) {
            if (cannotEnterHiveTicks > 0) {
                --cannotEnterHiveTicks
            }

            if (findHiveCooldown > 0) {
                --findHiveCooldown
            }

            if (findFlowerCooldown > 0) {
                --findFlowerCooldown
            }
/*
            if (isPollinating() && !hasPath()) {
                val f = if (rand.nextBoolean()) 2.0f else -2.0f
                val vec3d: Vec3d

                vec3d = if (hasFlower()) {
                    val pos = flowerPos!!.add(0.0, f.toDouble(), 0.0)
                    Vec3d(pos)
                } else {
                    positionVector.add(0.0, f.toDouble(), 0.0)
                }

                getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.4000000059604645)
            }
*/
            setNearTarget(isAngry() && !hasStung() && attackTarget != null && attackTarget!!.getDistanceSq(this) < 4.0)

            if (hasHive() && ticksExisted % 20 == 0 && !isHiveValid()) {
                hivePos = null
            }
        }
    }

    fun hasNectar(): Boolean {
        return getBeeFlag(0x8)
    }

    fun setHasNectar(hasNectar: Boolean) {
        setBeeFlag(0x8, hasNectar)
    }

    fun hasStung(): Boolean {
        return getBeeFlag(0x4)
    }

    private fun setHasStung(hasStung: Boolean) {
        setBeeFlag(0x4, hasStung)
    }

    private fun isNearTarget(): Boolean {
        return getBeeFlag(0x2)
    }

    private fun setNearTarget(isNearTarget: Boolean) {
        setBeeFlag(0x2, isNearTarget)
    }

    fun isTooFar(pos: BlockPos): Boolean {
        return !isWithinDistance(pos, 48)
    }
// todo check this
    fun isHiveValid(): Boolean {
        return hasHive() && world.getTileEntity(hivePos!!) is BeeHiveTile
    }
/*
    private fun isPollinating(): Boolean {
        return getBeeFlag(0x1)
    }
*/
// todo check this
    fun setPollinating(isPollinating: Boolean) {
        setBeeFlag(0x1, isPollinating)
    }

    private fun setBeeFlag(flag: Int, bool: Boolean) {
        val b = dataManager.get(BEE_FLAGS)
        if (bool) {
            dataManager.set(BEE_FLAGS, b or flag.toByte())
        } else {
            dataManager.set(BEE_FLAGS, b and flag.inv().toByte())
        }
    }

    private fun getBeeFlag(i: Int): Boolean {
        return dataManager.get(BEE_FLAGS).toInt() and i != 0
    }

    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        attributeMap.registerAttribute(SharedMonsterAttributes.FLYING_SPEED)
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = 10.0
        getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).baseValue = 0.6000000238418579
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.30000001192092896
        attributeMap.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = 2.0
    }

    override fun createNavigator(worldIn: World): PathNavigate {
        val navigateFlying = object : PathNavigateFlying(this, worldIn) {
            override fun canEntityStandOnPos(pos: BlockPos): Boolean {
                return !worldIn.isAirBlock(pos.down())
            }

            override fun onUpdateNavigation() {
                if (!pollinateAI!!.isRunning) {
                    super.onUpdateNavigation()
                }
            }
        }
        navigateFlying.setCanOpenDoors(false)
        navigateFlying.setCanEnterDoors(true)
        navigateFlying.setCanFloat(false)

        return navigateFlying
    }

    override fun isBreedingItem(stack: ItemStack): Boolean {
        return isFlowerValid(Block.getBlockFromItem(stack.item).getStateFromMeta(stack.metadata))
    }

    fun isFlowerValid(state: IBlockState): Boolean {
        return FLOWERS.contains(state)
    }

    override fun playStepSound(pos: BlockPos, blockIn: Block) {}
/*
    override fun getAmbientSound(): SoundEvent? {
        return null
    }
*/
    override fun getHurtSound(damageSourceIn: DamageSource?): SoundEvent? {
        return FSounds.BEE_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return FSounds.BEE_DEATH
    }

    override fun getSoundVolume(): Float {
        return 0.4f
    }

    override fun createChild(ageable: EntityAgeable): EntityAgeable? {
        return BeeEntity(world)
    }

    override fun getEyeHeight(): Float {
        return height * 0.5f
    }

    override fun fall(distance: Float, damageMultiplier: Float) {}

    override fun updateFallState(y: Double, onGroundIn: Boolean, state: IBlockState, pos: BlockPos) {}

    override fun makeFlySound(): Boolean {
        return true
    }

    fun onHoneyDelivered() {
        setHasNectar(false)
        resetCropCounter()
    }

    fun setBeeAttacker(entity: Entity): Boolean {
        setAnger(400 + rand.nextInt(400))
        if (entity is EntityLivingBase) {
            revengeTarget = entity
        }

        return true
    }

    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean {
        return if (isEntityInvulnerable(source)) {
            false
        } else {
            val attacker = source.trueSource
            if (attacker is EntityPlayer && !attacker.isCreative && this.canEntityBeSeen(attacker)) {
                pollinateAI?.isRunning = false
                setBeeAttacker(attacker)
            }

            super.attackEntityFrom(source, amount)
        }
    }

    override fun getCreatureAttribute(): EnumCreatureAttribute {
        return EnumCreatureAttribute.ARTHROPOD
    }

    override fun handleJumpWater() {
        motionY += 0.01
    }

    override fun handleJumpLava() {
        motionY += 0.01
    }

    fun isWithinDistance(flowerPos: BlockPos?, distance: Int): Boolean {
        return (flowerPos ?: return false).distanceSq(position) < distance * distance
    }

    fun getBlockInRange(range: Int, test: (BlockPos) -> Boolean): BlockPos? {
        for (pos in BlockPos.getAllInBoxMutable(position.add(-range, -range, -range), position.add(range, range, range))) {
            if (test(pos)) {
                return pos.toImmutable()
            }
        }

        return null
    }

    companion object {
        private val BEE_FLAGS = EntityDataManager.createKey(BeeEntity::class.java, DataSerializers.BYTE)
        private val ANGER = EntityDataManager.createKey(BeeEntity::class.java, DataSerializers.VARINT)

        @JvmField
        val FLOWERS = arrayListOf(
            FBlocks.CORNFLOWER.defaultState, FBlocks.LILY_OF_THE_VALLEY, FBlocks.WITHER_ROSE,
            Blocks.YELLOW_FLOWER.defaultState.withProperty(Blocks.YELLOW_FLOWER.typeProperty, EnumFlowerType.DANDELION),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.POPPY),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.BLUE_ORCHID),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.ALLIUM),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.HOUSTONIA),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.RED_TULIP),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.ORANGE_TULIP),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.WHITE_TULIP),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.PINK_TULIP),
            Blocks.RED_FLOWER.defaultState.withProperty(Blocks.RED_FLOWER.typeProperty, EnumFlowerType.OXEYE_DAISY)
        )
    }
}

/*
    Mutex bits index corresponds to a power of 2 flags can be |'d
 */