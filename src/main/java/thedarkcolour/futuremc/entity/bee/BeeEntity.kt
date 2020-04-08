@file:Suppress("DEPRECATION")

package thedarkcolour.futuremc.entity.bee

import net.minecraft.block.Block
import net.minecraft.block.BlockFlower.EnumFlowerType
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.*
import net.minecraft.entity.ai.EntityAIFollowParent
import net.minecraft.entity.ai.EntityAIMate
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
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.EnumDifficulty
import net.minecraft.world.World
import thedarkcolour.core.util.lerp
import thedarkcolour.futuremc.entity.bee.ai.*
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
    private var findHiveCooldown = 0
    var findFlowerCooldown = 0
    var flowerPos: BlockPos? = null
    var hivePos: BlockPos? = null
    private lateinit var pollinateAI: PollinateAI
    private lateinit var aiFindHive: FindHiveAI
    private lateinit var aiFindFlower: FindFlowerAI
    val random: Random
        get() = rand

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

    override fun getBlockPathWeight(pos: BlockPos): Float {
        val state = world.getBlockState(pos)
        return if (state.block.isAir(state, world, pos)) 10.0f else 0.0f
    }

    override fun initEntityAI() {
        tasks.addTask(0, StingAI(this, 1.399999976158142, true))
        tasks.addTask(1, EnterHiveAI(this))
        tasks.addTask(2, EntityAIMate(this, 1.0))
        tasks.addTask(3, TemptAI(this))
        pollinateAI = PollinateAI(this)
        tasks.addTask(4, pollinateAI)
        tasks.addTask(5, EntityAIFollowParent(this, 1.25))
        tasks.addTask(5, FindHiveAI(this))
        tasks.addTask(6, FindFlowerAI(this))
        tasks.addTask(7, GrowCropsAI(this))
        tasks.addTask(8, WanderAI(this))
        targetTasks.addTask(1, RevengeAI(this))
        targetTasks.addTask(2, FollowTargetAI(this))
    }

    fun isFlowerValid(state: IBlockState): Boolean {
        return FLOWERS.contains(state)
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
        compound.setBoolean("HasNectar", hasPollen())
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
        hivePos = null
        if (compound.hasKey("HivePos")) {
            hivePos = NBTUtil.getPosFromTag(compound.getCompoundTag("HivePos"))
        }
        flowerPos = null
        if (compound.hasKey("FlowerPos")) {
            flowerPos = NBTUtil.getPosFromTag(compound.getCompoundTag("FlowerPos"))
        }
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

        updateBodyPitch()
    }

    fun hasFlower(): Boolean {
        return flowerPos != null
    }

    fun shouldReturnToHive(): Boolean {
        return if (cannotEnterHiveTicks > 0) {
            false
        } else if (!hasHive()) {
            false
        } else {
            hasPollen() || world.isDaytime || world.isRainingAt(position) || ticksSincePollination > 3600
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

        if (!hasPollen()) {
            ++ticksSincePollination
        }
    }

    fun resetPollinationTicks() {
        ticksSincePollination = 0
    }

    fun isAngry(): Boolean {
        return getAnger() > 0
    }

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

            setNearTarget(isAngry() && !hasStung() && attackTarget != null && attackTarget!!.getDistanceSq(this) < 4.0)

            if (hasHive() && ticksExisted % 20 == 0 && !isHiveValid()) {
                hivePos = BlockPos.ORIGIN
            }
        }
    }

    fun isHiveValid(): Boolean {
        return hasHive() && world.getTileEntity(hivePos!!) is BeeHiveTile
    }

    fun hasPollen(): Boolean {
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

    private fun isPollinating(): Boolean {
        return getBeeFlag(0x1)
    }

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
                return worldIn.getBlockState(pos.down()).block == Blocks.AIR
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

    override fun playStepSound(pos: BlockPos, blockIn: Block) {}

    override fun getAmbientSound(): SoundEvent? {
        return if (isAngry()) FSounds.BEE_AGGRESSIVE else FSounds.BEE_PASSIVE
    }

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
        return if (isChild) height * 0.95f else 0.6f
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

    fun getBlockInRange(range: Int, test: (BlockPos) -> Boolean): BlockPos? {
        val pos = position.add(-range, -range, -range)

        for (x in 0 until (range shl 1) + 1) {
            for (y in 0 until (range shl 1) + 1) {
                for (z in 0 until (range shl 1) + 1) {
                    if (test(pos.add(x, y, z))) {
                        return pos.add(x, y, z)
                    }
                }
            }
        }
        return null
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
                setPollinating(false)
                setBeeAttacker(attacker)
            }

            super.attackEntityFrom(source, amount)
        }
    }

    override fun getCreatureAttribute(): EnumCreatureAttribute {
        return EnumCreatureAttribute.ARTHROPOD
    }

    fun isWithinDistance(flowerPos: BlockPos, distance: Int): Boolean {
        return flowerPos.distanceSq(position) < distance * distance
    }

    companion object {
        private val BEE_FLAGS = EntityDataManager.createKey(BeeEntity::class.java, DataSerializers.BYTE)
        private val ANGER = EntityDataManager.createKey(BeeEntity::class.java, DataSerializers.VARINT)

        @JvmField
        val FLOWERS = arrayListOf(
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