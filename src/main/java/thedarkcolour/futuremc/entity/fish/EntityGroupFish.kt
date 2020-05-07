package thedarkcolour.futuremc.entity.fish

import net.minecraft.entity.IEntityLivingData
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.World
import java.util.stream.Stream

abstract class EntityGroupFish(worldIn: World) : EntityFish(worldIn) {
    var groupLeader: EntityGroupFish? = null
    var groupSize = 1
    open val maxGroupSize
        get() = super.getMaxSpawnedInChunk()

    override fun initEntityAI() {
        super.initEntityAI()
        tasks.addTask(5, AIFollowLeader(this))
    }

    class AIFollowLeader(private val fish: EntityGroupFish) : EntityAIBase() {
        private var navigateTimer: Int = 0
        private var randomInt: Int

        init {
            randomInt = genInt(fish)
        }

        private fun genInt(fish: EntityGroupFish): Int {
            return 200 + fish.rand.nextInt(200) % 20
        }

        override fun shouldExecute(): Boolean {
            return when {
                fish.isGroupLeader() -> {
                    false
                }
                fish.hasGroupLeader() -> {
                    true
                }
                randomInt > 0 -> {
                    --randomInt
                    false
                }
                else -> {
                    randomInt = this.genInt(fish)
                    val predicate = { fish: EntityGroupFish? -> fish!!.canGroupGrow() || !fish.hasGroupLeader() }
                    val list = fish.world.getEntitiesWithinAABB(
                        fish::class.java,
                        fish.entityBoundingBox.grow(8.0, 8.0, 8.0),
                        predicate::invoke
                    )
                    val groupFish: EntityGroupFish =
                        list.stream().filter(EntityGroupFish::canGroupGrow).findAny().orElse(fish)
                    groupFish.acceptMembers(list.stream().filter { !it.hasGroupLeader() })
                    fish.hasGroupLeader()
                }
            }
        }

        override fun shouldContinueExecuting(): Boolean {
            return fish.hasGroupLeader() && fish.inRangeOfGroupLeader()
        }

        override fun startExecuting() {
            navigateTimer = 0
        }

        override fun resetTask() {
            fish.leaveGroup()
        }

        override fun updateTask() {
            if (--navigateTimer <= 0) {
                navigateTimer = 10
                fish.moveToGroupLeader()
            }
        }
    }

    fun isGroupLeader(): Boolean {
        return groupSize > 1
    }

    fun hasGroupLeader(): Boolean {
        return groupLeader?.isEntityAlive ?: false
    }

    fun canGroupGrow(): Boolean {
        return isGroupLeader() && groupSize < maxGroupSize
    }

    fun acceptMembers(stream: Stream<EntityGroupFish>) {
        stream.limit((maxGroupSize - groupSize).toLong()).filter {
            it != this
        }.forEach {
            it.setLeader(this)
        }
    }

    private fun setLeader(groupLeaderIn: EntityGroupFish): EntityGroupFish {
        groupLeader = groupLeaderIn
        ++groupLeaderIn.groupSize
        return groupLeaderIn
    }

    fun inRangeOfGroupLeader(): Boolean {
        return getDistanceSq(groupLeader!!) <= 121.0
    }

    fun leaveGroup() {
        --groupLeader!!.groupSize
        groupLeader = null
    }

    fun moveToGroupLeader() {
        if (hasGroupLeader()) {
            getNavigator().tryMoveToEntityLiving(groupLeader!!, 1.0)
        }
    }

    override fun shouldSwimAway(): Boolean = !hasGroupLeader()

    override fun getMaxSpawnedInChunk(): Int {
        return maxGroupSize
    }

    override fun onInitialSpawn(difficulty: DifficultyInstance, livingdata: IEntityLivingData?): IEntityLivingData {
        super.onInitialSpawn(difficulty, livingdata)

        return if (livingdata == null) {
            GroupData(this)
        } else {
            setLeader((livingdata as GroupData).groupLeader)
            livingdata
        }
    }

    class GroupData(val groupLeader: EntityGroupFish) : IEntityLivingData

    override fun onUpdate() {
        super.onUpdate()
        if (isGroupLeader() && rand.nextInt(200) == 1) {
            val list = world.getEntitiesWithinAABB(this::class.java, entityBoundingBox.grow(8.0))
            if (list.size <= 1) {
                groupSize = 1
            }
        }
    }
}