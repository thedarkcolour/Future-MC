@file:Suppress("DEPRECATION")

package thedarkcolour.futuremc.entity.bee

import crafttweaker.api.minecraft.CraftTweakerMC
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
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
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.relauncher.ReflectionHelper
import thedarkcolour.futuremc.block.BlockSweetBerryBush
import thedarkcolour.futuremc.compat.crafttweaker.Bee
import thedarkcolour.futuremc.init.FBlocks
import thedarkcolour.futuremc.init.Sounds
import thedarkcolour.futuremc.tile.TileBeeHive
import java.lang.invoke.MethodHandles
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.*

class EntityBee(worldIn: World) : EntityAnimal(worldIn), EntityFlying {
    private var targetPlayer: UUID? = null
    private var currentPitch: Float = 0F
    private var lastPitch: Float = 0F
    private var ticksSinceSting: Int = 0
    private var ticksSincePollination: Int = 0
    private var cannotEnterHiveTicks: Int = 0
    private var cropsGrownSincePollination: Int = 0
    var flowerPos: BlockPos = BlockPos.ORIGIN
    var hivePos: BlockPos = BlockPos.ORIGIN; private set

    init {
        moveHelper = FlyHelper()
        lookHelper = LookHelper()

        setSize(0.7F, 0.7F)
        setPathPriority(PathNodeType.WATER, -1F)
    }

    fun isFlowerValid(state: IBlockState): Boolean {
        val block = state.block
        val meta = block.getMetaFromState(state)

        if (block == Blocks.AIR || meta > 15) {
            return false
        }

        return if (Loader.isModLoaded("crafttweaker")) {
            Bee.FLOWERS.containsEquivalent(CraftTweakerMC.getBlock(block, meta))
        } else {
            block is BlockFlower || block is thedarkcolour.futuremc.block.BlockFlower
        }
    }

    fun canGrowBlock(block: Block): Boolean {
        return block == Blocks.WHEAT || block == Blocks.CARROTS || block == Blocks.POTATOES || block == Blocks.BEETROOTS
                || block == Blocks.MELON_STEM || block == Blocks.PUMPKIN_STEM || block == FBlocks.SWEET_BERRY_BUSH
    }

    override fun entityInit() {
        super.entityInit()
        this.dataManager.register(BEE_FLAGS, 0.toByte())
        this.dataManager.register(ANGER, 0)
    }

    override fun initEntityAI() {
        tasks.addTask(0, AIFindHive())
        tasks.addTask(0, AISting(1.399999976158142, true))
        tasks.addTask(1, AIEnterHive())
        tasks.addTask(2, EntityAIMate(this, 1.0))
        tasks.addTask(3, AITempt())
        tasks.addTask(4, AIPollinate())
        tasks.addTask(5, EntityAIFollowParent(this, 1.25))
        tasks.addTask(5, AIMoveToHive())
        tasks.addTask(6, AIMoveToFlower())
        tasks.addTask(7, AIGrowCrops())
        tasks.addTask(8, AIWander())
        targetTasks.addTask(1, AIRevenge())
        targetTasks.addTask(2, AIFollowTarget())
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        compound.setTag("HivePos", NBTUtil.createPosTag(hivePos))
        compound.setTag("FlowerPos", NBTUtil.createPosTag(flowerPos))
        compound.setBoolean("HasNectar", hasNectar())
        compound.setBoolean("HasStung", hasStung())
        compound.setInteger("TicksSincePollination", ticksSincePollination)
        compound.setInteger("CannotEnterHiveTicks", cannotEnterHiveTicks)
        compound.setInteger("CropsGrownSincePollination", cropsGrownSincePollination)
        compound.setInteger("Anger", getAnger())

        if (targetPlayer != null) {
            compound.setString("HurtBy", targetPlayer.toString())
        } else {
            compound.setString("HurtBy", "")
        }
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        hivePos = NBTUtil.getPosFromTag(compound.getCompoundTag("HivePos"))
        flowerPos = NBTUtil.getPosFromTag(compound.getCompoundTag("FlowerPos"))
        super.readEntityFromNBT(compound)
        setHasNectar(compound.getBoolean("HasNectar"))
        setHasStung(compound.getBoolean("HasStung"))
        setAnger(compound.getInteger("Anger"))
        ticksSincePollination = compound.getInteger("TicksSincePollination")
        cannotEnterHiveTicks = compound.getInteger("CannotEnterHiveTicks")
        cropsGrownSincePollination = compound.getInteger("CropsGrownSincePollination")

        val s = compound.getString("HurtBy")
        if (s.isNotEmpty()) {
            targetPlayer = UUID.fromString(s)
            val player = world.getPlayerEntityByUUID(targetPlayer!!)
            revengeTarget = player
            if (player != null) {
                attackingPlayer = player
                recentlyHit = revengeTimer
            }
        }
    }

    override fun attackEntityAsMob(entityIn: Entity): Boolean {
        val flag = entityIn.attackEntityFrom(causeBeeDamage(this), this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).attributeValue.toFloat())
        if (flag) {
            applyEnchantments(this, entityIn)
            if (entityIn is EntityLivingBase) {
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
            playSound(Sounds.BEE_STING, 1.0f, 1.0f)
        }

        return flag
    }

    override fun onUpdate() {
        super.onUpdate()

        updateBodyPitch()
    }

    fun hasFlower(): Boolean {
        return flowerPos != BlockPos.ORIGIN
    }

    fun canEnterHive(): Boolean {
        return if (cannotEnterHiveTicks > 0) {
            false
        } else if (!hasHive()) {
            false
        } else {
            hasNectar() || world.isDaytime || world.isRainingAt(position) || ticksSincePollination > 3600
        }
    }

    fun setCannotEnterHiveTicks(cannotEnterHiveTicks: Int) {
        this.cannotEnterHiveTicks = cannotEnterHiveTicks
    }

    fun getBodyPitch(partialTickTime: Float): Float {
        return lastPitch + partialTickTime * (currentPitch - lastPitch)
    }

    private fun updateBodyPitch() {
        lastPitch = currentPitch

        currentPitch = if (this.isNearTarget()) {
            1.0f.coerceAtMost(currentPitch + 0.2f)
        } else {
            0.0f.coerceAtLeast(currentPitch - 0.24f)
        }
    }

    override fun setRevengeTarget(entityIn: EntityLivingBase?) {
        super.setRevengeTarget(entityIn)
        if (entityIn != null) {
            targetPlayer = entityIn.uniqueID
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
            setAnger(anger - 1)
            val livingEntity = attackTarget
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
        return hivePos != BlockPos.ORIGIN
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
            if (isPollinating() && !hasPath()) {
                val f = if (rand.nextBoolean()) 2.0f else -2.0f
                val vec3d: Vec3d

                vec3d = if (hasFlower()) {
                    val pos = flowerPos.add(0.0, f.toDouble(), 0.0)
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

    private fun isHiveValid(): Boolean {
        return if (!hasHive()) {
            false
        } else {
            val te = world.getTileEntity(hivePos)
            te is TileBeeHive
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
        return if (isAngry()) Sounds.BEE_AGGRESSIVE else Sounds.BEE_PASSIVE
    }

    override fun getHurtSound(damageSourceIn: DamageSource?): SoundEvent? {
        return Sounds.BEE_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return Sounds.BEE_DEATH
    }

    override fun getSoundVolume(): Float {
        return 0.4f
    }

    override fun createChild(ageable: EntityAgeable): EntityAgeable? {
        return EntityBee(world)
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

    private fun getBlockInRange(predicate: (BlockPos) -> Boolean, range: Int): Optional<BlockPos> {
        val pos = position.add(-range, -range, -range)

        for (x in 0 until (range shl 1) + 1) {
            for (y in 0 until (range shl 1) + 1) {
                for (z in 0 until (range shl 1) + 1) {
                    if (predicate(pos.add(x, y, z))) {
                        return Optional.of(pos.add(x, y, z))
                    }
                }
            }
        }
        return Optional.empty()
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

    inner class AIEnterHive : AINotAngry() {
        override fun canBeeStart(): Boolean {
            if (this@EntityBee.hasNectar() && this@EntityBee.hasHive() && !this@EntityBee.hasStung() && this@EntityBee.canEnterHive()) {
                if (this@EntityBee.hivePos.distanceSq(this@EntityBee.position) < 4.0) {
                    val te = this@EntityBee.world.getTileEntity(this@EntityBee.hivePos)
                    if (te is TileBeeHive) {
                        if (!te.isFullOfBees()) {
                            return true
                        }

                        this@EntityBee.hivePos = BlockPos.ORIGIN
                    }
                }
            }
            return false
        }

        override fun canBeeContinue(): Boolean {
            return false
        }
        override fun startExecuting() {
            val tile = this@EntityBee.world.getTileEntity(this@EntityBee.hivePos)
            if (tile is TileBeeHive) {
                tile.tryEnterHive(this@EntityBee, this@EntityBee.hasNectar())
            }
        }

    }

    inner class AISting(speedIn: Double, useLongMemory: Boolean) : EntityAIAttackMelee(this@EntityBee, speedIn, useLongMemory) {
        override fun shouldExecute(): Boolean {
            return super.shouldExecute() && (attacker as EntityBee).isAngry() && !(attacker as EntityBee).hasStung()
        }

        override fun shouldContinueExecuting(): Boolean {
            return super.shouldExecute() && (attacker as EntityBee).isAngry() && !(attacker as EntityBee).hasStung()
        }
    }

    inner class AIGrowCrops : AINotAngry() {

        override fun canBeeStart(): Boolean {
            return when {
                this@EntityBee.getCropsGrownSincePollination() >= 10 -> false
                this@EntityBee.rand.nextFloat() < 0.3f -> false
                else -> this@EntityBee.hasNectar() && this@EntityBee.isHiveValid()
            }
        }

        override fun canBeeContinue(): Boolean {
            return canBeeStart()
        }

        override fun updateTask() {
            if (this@EntityBee.rand.nextInt(30) == 0) {
                for (i in 1..2) {
                    val pos = this@EntityBee.position.down(i)
                    val state = this@EntityBee.world.getBlockState(pos)
                    val block = state.block
                    var canGrow = false
                    var ageProperty: PropertyInteger? = null
                    if (canGrowBlock(block)) {
                        if (block is BlockCrops) {
                            if (!block.isMaxAge(state)) {
                                canGrow = true

                                ageProperty = if (block is BlockBeetroot) {
                                    BlockBeetroot.BEETROOT_AGE
                                } else {
                                    try {
                                        GET_AGE_PROPERTY(block) as PropertyInteger
                                    } catch (e: IllegalAccessException) {
                                        e.printStackTrace()
                                        return
                                    } catch (e: InvocationTargetException) {
                                        e.printStackTrace()
                                        return
                                    }

                                }
                            }
                        } else {
                            val age: Int
                            if (block is BlockStem) {
                                age = state.getValue(BlockStem.AGE)
                                if (age < 7) {
                                    canGrow = true
                                    ageProperty = BlockStem.AGE
                                }
                            } else if (block == FBlocks.SWEET_BERRY_BUSH) {
                                age = state.getValue(BlockSweetBerryBush.AGE)
                                if (age < 3) {
                                    canGrow = true
                                    ageProperty = BlockSweetBerryBush.AGE
                                }
                            }
                        }

                        if (ageProperty != null && canGrow) {
                            this@EntityBee.world.playEvent(2005, pos, 0)
                            this@EntityBee.world.setBlockState(pos, state.withProperty(ageProperty, state.getValue(ageProperty) + 1))
                            this@EntityBee.addCropCounter()
                        }
                    }
                }

            }
        }
    }

    inner class AIFindHive : AINotAngry() {
        override fun canBeeStart(): Boolean {
            return this@EntityBee.ticksExisted % 10 == 0 && !this@EntityBee.hasHive()
        }

        override fun canBeeContinue(): Boolean {
            return false
        }

        override fun startExecuting() {
            val optionalPos = this@EntityBee.getBlockInRange({ pos -> this@EntityBee.world.getTileEntity(pos) is TileBeeHive && !(this@EntityBee.world.getTileEntity(pos) as TileBeeHive).isFullOfBees() }, 5)
            if (optionalPos.isPresent) {
                val pos = optionalPos.get()
                val tile = this@EntityBee.world.getTileEntity(pos)
                if (tile is TileBeeHive && !tile.isFullOfBees()) {
                    this@EntityBee.hivePos = pos
                }
            }
        }
    }

    inner class AIPollinate : AINotAngry() {
        private var lastPollinationTicks = 0
        private var pollinationTicks = 0
        private val flower: Optional<BlockPos>
            get() = this@EntityBee.getBlockInRange({ pos -> isFlowerValid(this@EntityBee.world.getBlockState(pos)) }, 2)

        init {
            mutexBits = 0x1
        }

        override fun canBeeStart(): Boolean {
            return if (this@EntityBee.hasNectar()) {
                false
            } else if (this@EntityBee.rand.nextFloat() < 0.7f) {
                false
            } else {
                val flower = flower
                if (flower.isPresent) {
                    this@EntityBee.flowerPos = flower.get()
                    this@EntityBee.getNavigator().tryMoveToXYZ(this@EntityBee.flowerPos.x.toDouble(), this@EntityBee.flowerPos.y.toDouble(), this@EntityBee.flowerPos.z.toDouble(), 1.2000000476837158)
                    true
                } else {
                    false
                }
            }
        }

        override fun canBeeContinue(): Boolean {
            return when {
                completedPollination() -> this@EntityBee.rand.nextFloat() < 0.2f
                this@EntityBee.ticksExisted % 20 == 0 -> flower.isPresent
                else -> true
            }
        }

        private fun completedPollination(): Boolean {
            return pollinationTicks > 400
        }

        override fun startExecuting() {
            this@EntityBee.setPollinating(true)
            pollinationTicks = 0
            lastPollinationTicks = 0
        }

        override fun resetTask() {
            this@EntityBee.setPollinating(false)
            if (completedPollination()) {
                this@EntityBee.setHasNectar(true)
            }
        }

        override fun updateTask() {
            ++pollinationTicks
            if (this@EntityBee.rand.nextFloat() < 0.05f && pollinationTicks > lastPollinationTicks + 60) {
                lastPollinationTicks = pollinationTicks
                this@EntityBee.playSound(Sounds.BEE_POLLINATE, 1.0f, 1.0f)
            }
        }
    }

    inner class LookHelper : EntityLookHelper(this@EntityBee) {
        override fun onUpdateLook() {
            if (!this@EntityBee.isAngry()) {
                super.onUpdateLook()
            }
        }
    }

    inner class AIMoveToFlower : AIMoveToBlock(3) {
        override val targetPos: BlockPos
            get() = this@EntityBee.flowerPos

        private val isHiveValid: Boolean
            get() = targetPos != BlockPos.ORIGIN

        override fun canBeeStart(): Boolean {
            return isHiveValid && this@EntityBee.ticksSincePollination > 3600
        }

        override fun canBeeContinue(): Boolean {
            return this.canBeeStart() && super.canBeeContinue()
        }

        override fun resetTask() {
            if (!isFlowerValid(this@EntityBee.world.getBlockState(this@EntityBee.flowerPos))) {
                this@EntityBee.flowerPos = BlockPos.ORIGIN
            }
        }
    }

    inner class AIMoveToHive : AIMoveToBlock(2) {
        override val targetPos: BlockPos
            get() = this@EntityBee.hivePos

        override fun canBeeStart(): Boolean {
            return this@EntityBee.canEnterHive()
        }

        override fun canBeeContinue(): Boolean {
            return canBeeStart() && super.canBeeContinue()
        }
    }

    abstract inner class AIMoveToBlock(private var range: Int) : AINotAngry() {
        private var failedToFindPath = false

        protected abstract val targetPos: BlockPos

        init {
            mutexBits = 0x1
        }

        override fun canBeeContinue(): Boolean {
            return targetPos.distanceSq(this@EntityBee.position) > range * range
        }

        override fun updateTask() {
            val pos = targetPos
            val flag = pos.distanceSq(this@EntityBee.position) < 64.0

            if (this@EntityBee.getNavigator().noPath()) {
                var vec3d: Vec3d? = findVector(this@EntityBee, 8, 6, Vec3d(pos), 0.3141592741012573)

                if (vec3d == null) {
                    vec3d = findVector(this@EntityBee, 3, 3, Vec3d(pos))
                }

                if (vec3d != null && !flag && this@EntityBee.world.getBlockState(BlockPos(vec3d)).block != Blocks.WATER) {
                    vec3d = findVector(this@EntityBee, 8, 6, Vec3d(pos))
                }

                if (vec3d == null) {
                    failedToFindPath = true
                    return
                }

                this@EntityBee.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0)
            }
        }
    }

    inner class AIWander : EntityAIBase() {
        init {
            mutexBits = 0x1
        }

        override fun shouldExecute(): Boolean {
            return this@EntityBee.navigator.noPath() && this@EntityBee.rand.nextInt(10) == 0 || !this@EntityBee.hasHive()
        }

        override fun shouldContinueExecuting(): Boolean {
            return !this@EntityBee.navigator.noPath()
        }

        override fun startExecuting() {
            val vec3d = RandomPositionGenerator.findRandomTarget(this@EntityBee, 8, 7)
            if (vec3d != null) {
                this@EntityBee.navigator.tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0)
            }
        }

        override fun resetTask() {
            this@EntityBee.navigator.setPath(null, 1.0)
        }
    }

    abstract inner class AINotAngry : EntityAIBase() {
        abstract fun canBeeStart(): Boolean

        abstract fun canBeeContinue(): Boolean

        override fun shouldExecute(): Boolean {
            return this.canBeeStart() && !this@EntityBee.isAngry()
        }

        override fun shouldContinueExecuting(): Boolean {
            return this.canBeeContinue() && !this@EntityBee.isAngry()
        }
    }

    inner class AIFollowTarget : EntityAINearestAttackableTarget<EntityPlayer>(this@EntityBee, EntityPlayer::class.java, true) {
        override fun shouldExecute(): Boolean {
            return canSting() && super.shouldExecute()
        }

        override fun shouldContinueExecuting(): Boolean {
            return if (canSting() && taskOwner.attackTarget != null) {
                super.shouldContinueExecuting()
            } else {
                target = null
                false
            }
        }

        private fun canSting(): Boolean {
            val bee = taskOwner as EntityBee
            return bee.isAngry() && !bee.hasStung()
        }
    }

    inner class AIRevenge : EntityAIHurtByTarget(this@EntityBee, true) {
        override fun setEntityAttackTarget(creatureIn: EntityCreature, entityLivingBaseIn: EntityLivingBase) {
            if (creatureIn is EntityBee && taskOwner.canEntityBeSeen(entityLivingBaseIn) && creatureIn.setBeeAttacker(entityLivingBaseIn)) {
                creatureIn.setAttackTarget(entityLivingBaseIn)
            }
        }
    }

    inner class AITempt : EntityAIBase() {
        private var closestPlayer: EntityPlayer? = null
        private var coolDown: Int = 0

        init {
            mutexBits = 0x3
        }

        override fun shouldExecute(): Boolean {
            return if (coolDown > 0) {
                --coolDown
                false
            } else {
                closestPlayer = this@EntityBee.world.getClosestPlayerToEntity(this@EntityBee, 10.0)
                if (closestPlayer == null) {
                    false
                } else {
                    isTempting(closestPlayer!!.heldItemMainhand) || isTempting(closestPlayer!!.heldItemOffhand)
                }
            }
        }

        private fun isTempting(stack: ItemStack): Boolean {
            return this@EntityBee.isFlowerValid(Block.getBlockFromItem(stack.item).getStateFromMeta(stack.metadata))
        }

        override fun shouldContinueExecuting(): Boolean {
            return shouldExecute()
        }

        override fun resetTask() {
            closestPlayer = null
            this@EntityBee.getNavigator().clearPath()
            coolDown = 100
        }

        override fun updateTask() {
            this@EntityBee.lookHelper.setLookPositionWithEntity(closestPlayer!!, 75.0f, 40.0f)
            if (this@EntityBee.getDistanceSq(closestPlayer!!) < 6.25) {
                this@EntityBee.getNavigator().clearPath()
            } else {
                this@EntityBee.getNavigator().tryMoveToEntityLiving(closestPlayer!!, 1.25)
            }
        }
    }

    private fun causeBeeDamage(entityBee: EntityBee): EntityDamageSource {
        return EntityDamageSource("sting", entityBee)
    }

    inner class FlyHelper : EntityMoveHelper(this@EntityBee) {
        override fun onUpdateMoveHelper() {
            if (action == Action.MOVE_TO) {
                action = Action.WAIT
                entity.setNoGravity(true)
                val d0 = posX - entity.posX
                val d1 = posY - entity.posY
                val d2 = posZ - entity.posZ
                val d3 = d0 * d0 + d1 * d1 + d2 * d2

                if (d3 < 2.500000277905201E-7) {
                    entity.setMoveVertical(0.0f)
                    entity.setMoveForward(0.0f)
                    return
                }

                val f0 = (atan2(d2, d0) * 57.2957763671875).toFloat() - 90.0f
                entity.rotationYaw = limitAngle(entity.rotationYaw, f0, 90.0f)
                val f1: Float = if (entity.onGround) {
                    (speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).attributeValue).toFloat()
                } else {
                    (speed * entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).attributeValue).toFloat()
                }

                entity.aiMoveSpeed = f1
                val d4 = sqrt(d0 * d0 + d2 * d2)
                val f2 = (-(MathHelper.atan2(d1, d4) * 57.2957763671875)).toFloat()
                entity.rotationPitch = limitAngle(entity.rotationPitch, f2, 20f)
                entity.setMoveVertical(if (d1 > 0.0) f1 else -f1)
            } else {
                entity.setMoveVertical(0.0f)
                entity.setMoveForward(0.0f)
            }
        }
    }

    fun findVector(entityIn: EntityCreature, var1: Int, var2: Int, vec3d: Vec3d, var4: Double): Vec3d? {
        return findVector(entityIn, var1, var2, vec3d.subtract(entityIn.positionVector), var4) { entityIn.getBlockPathWeight(it).toDouble() }
    }

    private fun findVector(entityIn: EntityCreature, var1: Int, var2: Int, vec3d: Vec3d, var6: Double, function: (BlockPos) -> Double): Vec3d? {
        val navigator = entityIn.navigator
        val rng = entityIn.rng
        val flag: Boolean = if (entityIn.hasHome()) {
            entityIn.homePosition.distanceSq(entityIn.position) < ((entityIn.maximumHomeDistance + var1.toFloat()).toDouble() + 1.0) * ((entityIn.maximumHomeDistance + var1.toFloat()).toDouble() + 1.0)
        } else {
            false
        }

        var flag1 = false
        var d0 = java.lang.Double.NEGATIVE_INFINITY
        var pos = BlockPos(entityIn)

        for (int_6 in 0..9) {
            val pos1 = fun6374(rng, var1, var2, vec3d, var6)
            if (pos1 != null) {
                var x = pos1.x
                val y = pos1.y
                var z = pos1.z
                var blockPos: BlockPos
                if (entityIn.hasHome() && var1 > 1) {
                    blockPos = entityIn.homePosition
                    if (entityIn.posX > blockPos.x.toDouble()) {
                        x -= rng.nextInt(var1 / 2)
                    } else {
                        x += rng.nextInt(var1 / 2)
                    }

                    if (entityIn.posZ > blockPos.z.toDouble()) {
                        z -= rng.nextInt(var1 / 2)
                    } else {
                        z += rng.nextInt(var1 / 2)
                    }
                }

                blockPos = BlockPos(x.toDouble() + entityIn.posX, y.toDouble() + entityIn.posY, z.toDouble() + entityIn.posZ)
                if ((!flag || entityIn.isWithinHomeDistanceFromPosition(blockPos)) && navigator.canEntityStandOnPos(blockPos)) {
                    if (entityIn.world.getBlockState(blockPos).material != Material.WATER) {
                        val d1 = function(blockPos)
                        if (d1 > d0) {
                            d0 = d1
                            pos = blockPos
                            flag1 = true
                        }
                    }
                }
            }
        }

        return if (flag1) {
            Vec3d(pos)
        } else {
            null
        }
    }

    fun findVector(entityIn: EntityCreature, var1: Int, var2: Int, vec3d: Vec3d): Vec3d? {
        return findVector(entityIn, var1, var2, vec3d.subtract(entityIn.positionVector), 1.5707963705062866) { entityIn.getBlockPathWeight(it).toDouble() }
    }

    private fun fun6374(random: Random, var1: Int, var2: Int, vec3d: Vec3d?, var5: Double): BlockPos? {
        if (vec3d != null && var5 < 3.141592653589793) {
            val d0 = MathHelper.atan2(vec3d.z, vec3d.x) - 1.5707963705062866
            val d1 = d0 + (2.0f * random.nextFloat() - 1.0f).toDouble() * var5
            val d2 = sqrt(random.nextDouble()) * SQUARE_ROOT_OF_TWO * var1.toDouble()
            val x = -d2 * sin(d1)
            val z = d2 * cos(d1)
            return if (abs(x) <= var1.toDouble() && abs(z) <= var1.toDouble()) {
                val y = random.nextInt(2 * var2 + 1) - var2
                BlockPos(x, y.toDouble(), z)
            } else {
                null
            }
        } else {
            val x = random.nextInt(2 * var1 + 1) - var1
            val y = random.nextInt(2 * var2 + 1) - var2
            val z = random.nextInt(2 * var1 + 1) - var1
            return BlockPos(x, y, z)
        }
    }

    companion object {
        private val GET_AGE_PROPERTY = MethodHandles.lookup().unreflect(ReflectionHelper.findMethod(BlockCrops::class.java, "getAgeProperty", "func_185524_e"))
        private val BEE_FLAGS = EntityDataManager.createKey(EntityBee::class.java, DataSerializers.BYTE)
        private val ANGER = EntityDataManager.createKey(EntityBee::class.java, DataSerializers.VARINT)
        private val SQUARE_ROOT_OF_TWO = sqrt(2.0)
    }
}

/*
    Mutex bits enum
    0x1 = MOVE
    0x2 = LOOK
    0x4 = JUMP
    0x8 = TARGET
 */