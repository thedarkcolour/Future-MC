package thedarkcolour.futuremc.entity.drowned;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thedarkcolour.futuremc.entity.trident.EntityTrident;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.sound.Sounds;

import java.util.Random;

public class EntityDrowned extends EntityZombie implements IRangedAttackMob {
    private boolean field_204718_bx;
    private PathNavigateSwimmer waterNavigator;
    private PathNavigateGround groundNavigator;

    public EntityDrowned(World worldIn) {
        super(worldIn);
        this.stepHeight = 1.0F;
        this.moveHelper = new EntityDrowned.MoveHelper(this);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.waterNavigator = new PathNavigateSwimmer(this, worldIn);
        this.groundNavigator = new PathNavigateGround(this, worldIn);
    }

    @Override
    protected void applyEntityAI() {
        this.tasks.addTask(1, new EntityDrowned.AIGoToWater(this, 1.0D));
        this.tasks.addTask(2, new EntityDrowned.AITridentAttack(this, 1.0D, 40, 10.0F));
        this.tasks.addTask(2, new EntityDrowned.AIAttack(this, 1.0D, false));
        this.tasks.addTask(5, new EntityDrowned.AIGoToBeach(this, 1.0D));
        this.tasks.addTask(6, new EntityDrowned.AISwimUp(this, 1.0D, this.world.getSeaLevel()));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityDrowned.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 10, true, false, new AttackTargetPredicate(this)));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));
    }

    public boolean shouldAttack(EntityLivingBase p_204714_1_) {
        if (p_204714_1_ != null) {
            return !this.world.isDaytime() || p_204714_1_.isInWater();
        } else {
            return false;
        }
    }

    private boolean func_204715_dF() {
        if (this.field_204718_bx) {
            return true;
        } else {
            EntityLivingBase entitylivingbase = this.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isInWater();
        }
    }

    public void func_204713_s(boolean p_204713_1_) {
        this.field_204718_bx = p_204713_1_;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        EntityTrident entitytrident = new EntityTrident(this.world, this, new ItemStack(Init.TRIDENT));

        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entitytrident.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        entitytrident.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(Sounds.TRIDENT_THROW, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entitytrident);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {

    }

    protected boolean isCloseToPathTarget() {
        Path path = this.getNavigator().getPath();
        if (path != null) {
            PathPoint pathpoint = path.getTarget();
            double d0 = this.getDistanceSq(pathpoint.x, pathpoint.y, pathpoint.z);
            return d0 < 4.0D;
        }
        return false;
    }

    public static class AIGoToWater extends EntityAIBase {
        private final EntityDrowned entityIn;
        private double x, y, z;
        private final double speed;
        private final World world;

        public AIGoToWater(EntityDrowned entityIn, double speed) {
            this.entityIn = entityIn;
            this.speed = speed;
            this.world = entityIn.world;
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            if (!this.world.isDaytime()) {
                return false;
            }
            else if (this.entityIn.isInWater()) {
                return false;
            }
            else {
                Vec3d vec3d = this.getWaterPos();
                if (vec3d == null) {
                    return false;
                }
                else {
                    this.x = vec3d.x;
                    this.y = vec3d.y;
                    this.z = vec3d.z;
                    return true;
                }
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !this.entityIn.getNavigator().noPath();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            this.entityIn.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
        }

        private Vec3d getWaterPos() {
            Random random = this.entityIn.getRNG();
            //noinspection ConstantConditions
            BlockPos pos = new BlockPos(this.entityIn.posX, this.entityIn.getCollisionBoundingBox().minY, this.entityIn.posZ);

            for(int i = 0; i < 10; ++i) {
                BlockPos pos1 = pos.add(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
                if (this.world.getBlockState(pos1).getBlock() == Blocks.WATER) {
                    return new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ());
                }
            }

            return null;
        }
    }

    public static class AISwimUp extends EntityAIBase {
        private final EntityDrowned drowned;
        private final double speed;
        private final int targetY;
        private boolean obstructed;

        public AISwimUp(EntityDrowned drowned, double p_i48908_2_, int p_i48908_4_) {
            this.drowned = drowned;
            this.speed = p_i48908_2_;
            this.targetY = p_i48908_4_;
        }

        public boolean shouldExecute() {
            return !this.drowned.world.isDaytime() && this.drowned.isInWater() && this.drowned.posY < (double)(this.targetY - 2);
        }

        public boolean shouldContinueExecuting() {
            return this.shouldExecute() && !this.obstructed;
        }

        public void updateTask() {
            if (this.drowned.posY < (double)(this.targetY - 1) && (this.drowned.getNavigator().noPath() || this.drowned.isCloseToPathTarget())) {
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.drowned, 4, 8, new Vec3d(this.drowned.posX, this.targetY - 1, this.drowned.posZ));
                if (vec3d == null) {
                    this.obstructed = true;
                    return;
                }

                this.drowned.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }

        }

        public void startExecuting() {
            this.drowned.func_204713_s(true);
            this.obstructed = false;
        }

        public void resetTask() {
            this.drowned.func_204713_s(false);
        }
    }

    public static class AITridentAttack extends EntityAIAttackRanged {
        private final EntityDrowned attacker;

        public AITridentAttack(EntityDrowned attacker, double speed, int maxAttackTime, float maxAttackDistanceIn) {
            super(attacker, speed, maxAttackTime, maxAttackDistanceIn);
            this.attacker = attacker;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && this.attacker.getHeldItemMainhand().getItem() == Init.TRIDENT;
        }

        public void startExecuting() {
            super.startExecuting();
            this.attacker.setSwingingArms(true);
        }

        public void resetTask() {
            super.resetTask();
            this.attacker.setSwingingArms(false);
        }
    }

    public static class AIAttack extends EntityAIZombieAttack {
        private final EntityDrowned drowned;

        public AIAttack(EntityDrowned drowned, double speed, boolean longMemory) {
            super(drowned, speed, longMemory);
            this.drowned = drowned;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && this.drowned.shouldAttack(this.drowned.getAttackTarget());
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && this.drowned.shouldAttack(this.drowned.getAttackTarget());
        }
    }

    static class AIGoToBeach extends EntityAIMoveToBlock {
        private final EntityDrowned drowned;

        AIGoToBeach(EntityDrowned drowned, double p_i48911_2_) {
            super(drowned, p_i48911_2_, 8);
            this.drowned = drowned;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && !this.drowned.world.isDaytime() && this.drowned.isInWater() && this.drowned.posY >= (double)(this.drowned.world.getSeaLevel() - 3);
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting();
        }

        public void startExecuting() {
            this.drowned.func_204713_s(false);
            this.drowned.navigator = this.drowned.groundNavigator;
            super.startExecuting();
        }

        public void resetTask() {
            super.resetTask();
        }

        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            BlockPos blockpos = pos.up();
            return (worldIn.isAirBlock(blockpos) && worldIn.isAirBlock(blockpos.up())) && worldIn.getBlockState(pos).isTopSolid();
        }
    }

    public static class AttackTargetPredicate implements Predicate<EntityPlayer> {
        private final EntityDrowned entity;

        public AttackTargetPredicate(EntityDrowned drowned) {
            this.entity = drowned;
        }

        @Override
        public boolean apply(EntityPlayer playerIn) {
            return this.entity.shouldAttack(playerIn);
        }
    }

    static class MoveHelper extends EntityMoveHelper {
        private final EntityDrowned drowned;

        MoveHelper(EntityDrowned entity) {
            super(entity);
            this.drowned = entity;
        }

        public void onUpdateMoveHelper() {
            EntityLivingBase entitylivingbase = this.drowned.getAttackTarget();
            if (this.drowned.func_204715_dF() && this.drowned.isInWater()) {
                if (entitylivingbase != null && entitylivingbase.posY > this.drowned.posY || this.drowned.field_204718_bx) {
                    this.drowned.motionY += 0.002D;
                }

                if (this.action != EntityMoveHelper.Action.MOVE_TO || this.drowned.getNavigator().noPath()) {
                    this.drowned.setAIMoveSpeed(0.0F);
                    return;
                }

                double d0 = this.posX - this.drowned.posX;
                double d1 = this.posY - this.drowned.posY;
                double d2 = this.posZ - this.drowned.posZ;
                double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.drowned.rotationYaw = this.limitAngle(this.drowned.rotationYaw, f, 90.0F);
                this.drowned.renderYawOffset = this.drowned.rotationYaw;
                float f1 = (float)(this.speed * this.drowned.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                this.drowned.setAIMoveSpeed(this.drowned.getAIMoveSpeed() + (f1 - this.drowned.getAIMoveSpeed()) * 0.125F);
                this.drowned.motionY += (double)this.drowned.getAIMoveSpeed() * d1 * 0.1D;
                this.drowned.motionX += (double)this.drowned.getAIMoveSpeed() * d0 * 0.005D;
                this.drowned.motionZ += (double)this.drowned.getAIMoveSpeed() * d2 * 0.005D;
            } else {
                if (!this.drowned.onGround) {
                    this.drowned.motionY -= 0.008D;
                }

                super.onUpdateMoveHelper();
            }
        }
    }
}