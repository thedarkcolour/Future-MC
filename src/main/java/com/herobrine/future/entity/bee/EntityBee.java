package com.herobrine.future.entity.bee;

import com.herobrine.future.block.BlockBeeHive;
import com.herobrine.future.block.BlockBerryBush;
import com.herobrine.future.compat.crafttweaker.Bee;
import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import com.herobrine.future.tile.TileBeeHive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.core.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class EntityBee extends EntityAnimal implements EntityFlying {
    private static final DataParameter<Byte> BEE_FLAGS = EntityDataManager.createKey(EntityBee.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> ANGER = EntityDataManager.createKey(EntityBee.class, DataSerializers.VARINT);
    private UUID targetPlayer;
    private float currentPitch;
    private float lastPitch;
    private int ticksSinceSting;
    private int ticksSincePollination;
    private int cannotEnterHiveTicks;
    private int cropsGrownSincePollination;
    private BlockPos flowerPos;
    private BlockPos hivePos;

    public EntityBee(World worldIn) {
        super(worldIn);
        this.flowerPos = BlockPos.ORIGIN;
        this.hivePos = BlockPos.ORIGIN;
        this.moveHelper = new FlyHelper(this, 20, true);

        try {
            Field lookHelper = ReflectUtils.getNonFinalAccessibleField(EntityLiving.class, "lookHelper");
            lookHelper.set(this, new LookHelper(this));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BEE_FLAGS, (byte)0);
        this.dataManager.register(ANGER, 0);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AIEnterHive(this));
        this.tasks.addTask(1, new AIFindHive(this));
        this.tasks.addTask(1, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(2, new AITempt(this));
        this.tasks.addTask(3, new AIPollinate(this));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
        this.tasks.addTask(4, new AIMoveToHive(this));
        this.tasks.addTask(5, new AIMoveToFlower(this));
        this.tasks.addTask(6, new EntityAISting(this, 1.399999976158142D, true));
        this.tasks.addTask(7, new AIGrowCrops(this));
        this.tasks.addTask(8, new AIWander(this));
        this.targetTasks.addTask(1, new AIRevenge(this));
        this.targetTasks.addTask(2, new AIFollowTarget(this));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("HivePos", NBTUtil.createPosTag(hivePos));
        compound.setTag("FlowerPos", NBTUtil.createPosTag(flowerPos));
        compound.setBoolean("HasNectar", hasNectar());
        compound.setBoolean("HasStung", hasStung());
        compound.setInteger("TicksSincePollination", ticksSincePollination);
        compound.setInteger("CannotEnterHiveTicks", cannotEnterHiveTicks);
        compound.setInteger("CropsGrownSincePollination", cropsGrownSincePollination);
        compound.setInteger("Anger", getAnger());

        if (targetPlayer != null) {
            compound.setString("HurtBy", this.targetPlayer.toString());
        } else {
            compound.setString("HurtBy", "");
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(causeBeeDamage(this), (float)(this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
        if (flag) {
            applyEnchantments(this, entityIn);
            if (entityIn instanceof EntityLivingBase) {
                //((EntityLivingBase)entityIn).addStinger(((LivingEntity)entity_1).getStingers() + 1); Puts a stinger inside the hit entity, like arrows
                int duration = 0;
                if (world.getDifficulty() == EnumDifficulty.NORMAL) {
                    duration = 10;
                } else if (world.getDifficulty() == EnumDifficulty.HARD) {
                    duration = 18;
                }

                if (duration > 0) {
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, duration * 20, 0));
                }
            }

            setHasStung(true);
            setAttackTarget(null);
            playSound(Sounds.BEE_STING, 1.0F, 1.0F);
        }

        return flag;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        updateBodyPitch();
    }

    public BlockPos getFlowerPos() {
        return flowerPos;
    }

    public boolean hasFlower() {
        return flowerPos != BlockPos.ORIGIN;
    }

    public void setFlowerPos(BlockPos flowerPos) {
        this.flowerPos = flowerPos;
    }

    public boolean canEnterHive() {
        if (cannotEnterHiveTicks > 0) {
            return false;
        } else if (!hasHive()) {
            return false;
        } else {
            return hasNectar() || world.isDaytime() || world.isRainingAt(getPosition()) || ticksSincePollination > 3600;
        }
    }

    public void setCannotEnterHiveTicks(int cannotEnterHiveTicks) {
        this.cannotEnterHiveTicks = cannotEnterHiveTicks;
    }

    @SideOnly(Side.CLIENT)
    public float getBodyPitch(float partialTickTime) {
        return this.lastPitch + partialTickTime * (this.currentPitch - this.lastPitch);
    }

    private void updateBodyPitch() {
        this.lastPitch = this.currentPitch;

        if (this.isNearTarget()) {
            this.currentPitch = Math.min(1.0F, this.currentPitch + 0.2F);
        } else {
            this.currentPitch = Math.max(0.0F, this.currentPitch - 0.24F);
        }
    }

    @Override
    public void setRevengeTarget(EntityLivingBase entityIn) {
        super.setRevengeTarget(entityIn);
        if (entityIn != null) {
            targetPlayer = entityIn.getUniqueID();
        }
    }

    @Override
    protected void updateAITasks() {
        if (hasStung()) {
            ++this.ticksSinceSting;
            if (this.ticksSinceSting % 5 == 0 && this.rand.nextInt(MathHelper.clamp(1200 - this.ticksSinceSting, 1, 1200)) == 0) {
                this.attackEntityFrom(DamageSource.GENERIC, this.getHealth());
            }
        }

        if (isAngry()) {
            int anger = getAnger();
            setAnger(anger - 1);
            EntityLivingBase livingEntity_1 = getAttackTarget();
            if (anger == 0 && livingEntity_1 != null) {
                setBeeAttacker(livingEntity_1);
            }
        }

        if (!hasNectar()) {
            ++this.ticksSincePollination;
        }
    }

    public void resetPollinationTicks() {
        ticksSincePollination = 0;
    }

    public boolean isAngry() {
        return getAnger() > 0;
    }

    public int getAnger() {
        return dataManager.get(ANGER);
    }

    public void setAnger(int anger) {
        dataManager.set(ANGER, anger);
    }

    public boolean hasHive() {
        return hivePos != BlockPos.ORIGIN;
    }

    public int getCropsGrownSincePollination() {
        return cropsGrownSincePollination;
    }

    public void resetCropCounter() {
        cropsGrownSincePollination = 0;
    }

    public void addCropCounter() {
        ++cropsGrownSincePollination;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!world.isRemote) {
            if (cannotEnterHiveTicks > 0) {
                --cannotEnterHiveTicks;
            }
            if (isPollinating() && !hasPath()) {
                float f = rand.nextBoolean() ? 2.0F : -2.0F;
                Vec3d vec3d;

                if (hasFlower()) {
                    BlockPos pos = flowerPos.add(0.0D, f, 0.0D);
                    vec3d = new Vec3d(pos);
                } else {
                    vec3d = getPositionVector().addVector(0.0D, f, 0.0D);
                }

                getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.4000000059604645D);
            }

            setNearTarget(isAngry() && getAttackTarget() != null && getAttackTarget().getDistanceSq(this) < 4.0D);

            if (hasHive() && ticksExisted % 20 == 0 && !isHiveValid()) {
                hivePos = BlockPos.ORIGIN;
            }
        }
    }

    private boolean isHiveValid() {
        if (!hasHive()) {
            return false;
        } else {
            TileEntity te = world.getTileEntity(hivePos);
            return te instanceof TileBeeHive;
        }
    }

    public boolean hasNectar() {
        return getBeeFlag(0x8);
    }

    public void setHasNectar(boolean hasNectar) {
        setBeeFlag(0x8, hasNectar);
    }

    public boolean hasStung() {
        return getBeeFlag(0x4);
    }

    public void setHasStung(boolean hasStung) {
        setBeeFlag(0x4, hasStung);
    }

    public boolean isNearTarget() {
        return getBeeFlag(0x2);
    }

    public void setNearTarget(boolean isNearTarget) {
        setBeeFlag(0x2, isNearTarget);
    }

    public boolean isPollinating() {
        return getBeeFlag(0x1);
    }

    public void setPollinating(boolean isPollinating) {
        setBeeFlag(0x1, isPollinating);
    }

    public void setBeeFlag(int flag, boolean bool) {
        if (bool) {
            dataManager.set(BEE_FLAGS, (byte)(dataManager.get(BEE_FLAGS) | flag));
        } else {
            dataManager.set(BEE_FLAGS, (byte)(dataManager.get(BEE_FLAGS) & ~flag));
        }
    }

    public boolean getBeeFlag(int flag) {
        return (dataManager.get(BEE_FLAGS) & flag) != 0;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.6000000238418579D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    protected PathNavigate createNavigator(World worldIn) {
        PathNavigateFlying navigateFlying = new PathNavigateFlying(this, worldIn) {
            @Override
            public boolean canEntityStandOnPos(BlockPos pos) {
                return worldIn.getBlockState(pos.down()).getBlock() == Blocks.AIR;
            }
        };
        navigateFlying.setCanOpenDoors(false);
        navigateFlying.setCanEnterDoors(false);
        navigateFlying.setCanFloat(false);

        return navigateFlying;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return Bee.isFlowerValid(Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()));
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {}

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return Sounds.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Sounds.BEE_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityBee(world);
    }

    @Override
    public float getEyeHeight() {
        return isChild() ? height * 0.95F : 0.6F;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {}

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}

    public void onHoneyDelivered() {
        setHasNectar(false);
        resetCropCounter();
    }

    private Optional<BlockPos> getBlockInRange(Predicate<IBlockState> predicate, double range) {
        BlockPos pos = getPosition();
        BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();

        for(int y = 0; y <= range; y = y > 0 ? -y : 1 - y) {
            for(int int_2 = 0; int_2 < range; ++int_2) {
                for(int x = 0; x <= int_2; x = x > 0 ? -x : 1 - x) {
                    for(int z = x < int_2 && x > -int_2 ? int_2 : 0; z <= int_2; z = z > 0 ? -z : 1 - z) {
                        mutPos.setPos(pos).add(x, y - 1, z);
                        if (pos.distanceSq(mutPos) < range * range && predicate.test(this.world.getBlockState(mutPos))) {
                            return Optional.of(mutPos);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    public boolean setBeeAttacker(Entity entity) {
        setAnger(400 + rand.nextInt(400));
        if (entity instanceof EntityLivingBase) {
            setRevengeTarget((EntityLivingBase) entity);
        }

        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            Entity attacker = source.getTrueSource();
            if (attacker instanceof EntityPlayer && !((EntityPlayer)attacker).isCreative() && this.canEntityBeSeen(attacker)) {
                this.setPollinating(false);
                this.setBeeAttacker(attacker);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        hivePos = NBTUtil.getPosFromTag(compound.getCompoundTag("HivePos"));
        flowerPos = NBTUtil.getPosFromTag(compound.getCompoundTag("FlowerPos"));
        super.readEntityFromNBT(compound);
        setHasNectar(compound.getBoolean("HasNectar"));
        setHasStung(compound.getBoolean("HasStung"));
        setAnger(compound.getInteger("Anger"));
        ticksSincePollination = compound.getInteger("TicksSincePollination");
        cannotEnterHiveTicks = compound.getInteger("CannotEnterHiveTicks");
        cropsGrownSincePollination = compound.getInteger("CropsGrownSincePollination");

        String string_1 = compound.getString("HurtBy");
        if (!string_1.isEmpty()) {
            targetPlayer = UUID.fromString(string_1);
            EntityPlayer playerEntity_1 = world.getPlayerEntityByUUID(targetPlayer);
            setRevengeTarget(playerEntity_1);
            if (playerEntity_1 != null) {
                attackingPlayer = playerEntity_1;
                recentlyHit = getRevengeTimer();
            }
        }
    }

    private static abstract class NotAngryGoal extends EntityAIBase {
        protected final EntityBee bee;

        private NotAngryGoal(EntityBee bee) {
            this.bee = bee;
        }

        public abstract boolean canBeeStart();

        public abstract boolean canBeeContinue();

        @Override
        public boolean shouldExecute() {
            return this.canBeeStart() && !bee.isAngry();
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.canBeeContinue() && !bee.isAngry();
        }
    }

    private static class AIFindHive extends NotAngryGoal {
        private AIFindHive(EntityBee bee) {
            super(bee);
        }

        @Override
        public boolean canBeeStart() {
            return bee.ticksExisted % 10 == 0 && !bee.hasHive();
        }

        @Override
        public boolean canBeeContinue() {
            return false;
        }

        @Override
        public void startExecuting() {
            Optional<BlockPos> optionalPos = bee.getBlockInRange((state) -> state.getBlock() instanceof BlockBeeHive, 5);
            if (optionalPos.isPresent()) {
                BlockPos pos = optionalPos.get();
                TileEntity blockEntity_1 = bee.world.getTileEntity(pos);
                if (blockEntity_1 instanceof TileBeeHive && !((TileBeeHive)blockEntity_1).isFullOfBees()) {
                    bee.hivePos = pos;
                }
            }
        }
    }

    private static class AIPollinate extends NotAngryGoal {
        private int lastPollinationTicks = 0;
        private int pollinationTicks = 0;

        private AIPollinate(EntityBee bee) {
            super(bee);
            setMutexBits(0x1);
        }

        @Override
        public boolean canBeeStart() {
            if (bee.hasNectar()) {
                return false;
            } else if (bee.rand.nextFloat() < 0.7F) {
                return false;
            } else {
                Optional<BlockPos> optional_1 = getFlower();
                if (optional_1.isPresent()) {
                    bee.flowerPos = optional_1.get();
                    bee.getNavigator().tryMoveToXYZ(bee.flowerPos.getX(), bee.flowerPos.getY(), bee.flowerPos.getZ(), 1.2000000476837158D);
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean canBeeContinue() {
            if (completedPollination()) {
                return bee.rand.nextFloat() < 0.2F;
            } else if (bee.ticksExisted % 20 == 0) {
                Optional<BlockPos> optional_1 = getFlower();
                return optional_1.isPresent();
            } else {
                return true;
            }
        }

        private boolean completedPollination() {
            return pollinationTicks > 400;
        }

        @Override
        public void startExecuting() {
            bee.setPollinating(true);
            pollinationTicks = 0;
            lastPollinationTicks = 0;
        }

        @Override
        public void resetTask() {
            bee.setPollinating(false);
            if (completedPollination()) {
                bee.setHasNectar(true);
            }
        }

        @Override
        public void updateTask() {
            ++pollinationTicks;
            if (bee.rand.nextFloat() < 0.05F && pollinationTicks > lastPollinationTicks + 60) {
                lastPollinationTicks = pollinationTicks;
                bee.playSound(Sounds.BEE_POLLINATE, 1.0F, 1.0F);
            }
        }

        private Optional<BlockPos> getFlower() {
            return bee.getBlockInRange(Bee::isFlowerValid, 2.0D);
        }
    }

    private static class LookHelper extends EntityLookHelper {
        private EntityBee bee;

        private LookHelper(EntityBee bee) {
            super(bee);
            this.bee = bee;
        }

        @Override
        public void onUpdateLook() {
            if (!bee.isAngry()) {
                super.onUpdateLook();
            }
        }
    }

    private static class AIMoveToFlower extends MoveToBlockGoal {
        private AIMoveToFlower(EntityBee bee) {
            super(bee, 3);
        }

        @Override
        protected BlockPos getTargetPos() {
            return bee.flowerPos;
        }

        @Override
        public boolean canBeeStart() {
            return isHiveValid() && bee.ticksSincePollination > 3600;
        }

        private boolean isHiveValid() {
            return getTargetPos() != BlockPos.ORIGIN;
        }
    }

    private static class AIMoveToHive extends MoveToBlockGoal {
        private AIMoveToHive(EntityBee bee) {
            super(bee, 2);
        }

        @Override
        protected BlockPos getTargetPos() {
            return bee.hivePos;
        }

        @Override
        public boolean canBeeStart() {
            return bee.canEnterHive();
        }

        @Override
        public boolean canBeeContinue() {
            return canBeeStart() && super.canBeeContinue();
        }
    }

    private abstract static class MoveToBlockGoal extends NotAngryGoal {
        protected boolean failedToFindPath = false;
        protected int range;

        private MoveToBlockGoal(EntityBee bee, int range) {
            super(bee);
            this.range = range;
            setMutexBits(0x1);
        }

        protected abstract BlockPos getTargetPos();

        @Override
        public boolean canBeeContinue() {
            return (getTargetPos().distanceSq(bee.getPosition()) > range * range);
        }

        @Override
        public void updateTask() {
            BlockPos pos = getTargetPos();
            boolean flag = pos.distanceSq(bee.getPosition()) < 64.0D;

            if (bee.getNavigator().noPath()) {
                Vec3d vec3d = method_6377(bee, 8, 6, new Vec3d(pos), 0.3141592741012573D);

                if (vec3d == null) {
                    vec3d = method_6373(bee, 3, 3, new Vec3d(pos));
                }

                if (vec3d != null && !flag && bee.world.getBlockState(new BlockPos(vec3d)).getBlock() != Blocks.WATER) {
                    vec3d = method_6373(bee, 8, 6, new Vec3d(pos));
                }

                if (vec3d == null) {
                    failedToFindPath = true;
                    return;
                }

                bee.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            }
        }
    }

    private static class AIWander extends EntityAIBase {
        private final EntityBee bee;

        private AIWander(EntityBee bee) {
            setMutexBits(0x1);
            this.bee = bee;
        }

        @Override
        public boolean shouldExecute() {
            return bee.getNavigator().noPath() && bee.rand.nextInt(10) == 0;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !bee.getNavigator().noPath();
        }

        @Override
        public void startExecuting() {
            Vec3d vec3d = getRandomLocation();
            if (vec3d != null) {
                PathNavigate navigate = bee.getNavigator();
                Field f = ReflectUtils.getAccessibleField(PathNavigate.class, "pathSearchRange");
                Field g = ReflectUtils.getAccessibleField(ModifiableAttributeInstance.class, "cachedValue");

                try {
                    double v = g.getDouble(f.get(bee.getNavigator()));
                    g.setDouble(f.get(bee.getNavigator()), 1.0D);

                    navigate.setPath(navigate.getPathToPos(new BlockPos(vec3d)), 1.0D);

                    g.setDouble(f.get(bee.getNavigator()), v);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        private Vec3d getRandomLocation() {
            Vec3d vec3d_1 = bee.getLook(0.5F);
            Vec3d vec3d_2 = findVector(bee, 8, 7, 0, vec3d_1, true, 1.5707964D, bee::getBlockPathWeight, true, bee.getNavigator()::canEntityStandOnPos, 2, 1, true);
            return vec3d_2 != null ? vec3d_2 : findVector(bee, 8, 4, -2, vec3d_1, true, 1.5707963705062866D, bee::getBlockPathWeight, false, pos -> false, 0, 0, false);
        }
    }

    // TODO Custom tempting ai
    private static class AITempt extends EntityAIBase {
        private final double speed;
        private final EntityBee bee;
        private double lastPlayerX;
        private double lastPlayerY;
        private double lastPlayerZ;
        private double lastPlayerPitch;
        private double lastPlayerYaw;
        protected EntityPlayer closestPlayer;
        private int coolDown;
        private boolean active;

        private AITempt(EntityBee bee) {
            this.speed = 1.25D;
            this.bee = bee;
            setMutexBits(0x3);
        }

        @Override
        public boolean shouldExecute() {
            if (coolDown > 0) {
                --coolDown;
            } else {
                closestPlayer = bee.world.getClosestPlayer(bee.posX, bee.posY, bee.posZ, 10.0D, (entity) -> {
                    return
                });
                if (closestPlayer == null) {
                    return false;
                } else {
                    return isTempting(closestPlayer.getHeldItemMainhand()) || isTempting(closestPlayer.getHeldItemOffhand());
                }
            }
        }

        protected boolean isTempting(ItemStack stack) {
            return Bee.isFlowerValid(Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()));
        }

        @Override
        public void startExecuting() {
            this.lastPlayerX = this.closestPlayer.posX;
            this.lastPlayerY = this.closestPlayer.posY;
            this.lastPlayerZ = this.closestPlayer.posZ;
            this.active = true;
        }

        @Override
        public void resetTask() {
            closestPlayer = null;
            bee;
        }

        private boolean test(EntityLivingBase entity, EntityLivingBase entity1) {
            if (entity == entity1) {
                return false;
            } else if (entity1 instanceof EntityPlayer && ((EntityPlayer) entity1).isSpectator()) {
                return false;
            } else if (!entity1.isEntityAlive()) {
                return false;
            } else if (entity1.getIsInvulnerable()) {
                return false;
            } else {
                if (entity != null) {
                    double d = entity1.(livingEntity_1)
                }
            }
        }

        private static boolean getScaleDistance(EntityLivingBase a, EntityLivingBase b) {

        }
    }

    private static class AIEnterHive extends NotAngryGoal {
        AIEnterHive(EntityBee entityBee) {
            super(entityBee);
        }

        @Override
        public boolean canBeeStart() {
            if (bee.hasNectar() && bee.hasHive() && !bee.hasStung() && bee.canEnterHive()) {
                if (bee.hivePos.distanceSq(bee.getPosition()) < 4.0D) {
                    TileEntity blockEntity_1 = bee.world.getTileEntity(bee.hivePos);
                    if (blockEntity_1 instanceof TileBeeHive) {
                        TileBeeHive beeHiveBlockEntity_1 = (TileBeeHive)blockEntity_1;
                        if (!beeHiveBlockEntity_1.isFullOfBees()) {
                            return true;
                        }

                        bee.hivePos = BlockPos.ORIGIN;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean canBeeContinue() {
            return false;
        }

        @Override
        public void startExecuting() {
            TileEntity tile = bee.world.getTileEntity(bee.hivePos);
            if (tile instanceof TileBeeHive) {
                TileBeeHive hive = (TileBeeHive)tile;
                hive.tryEnterHive(bee, bee.hasNectar());
            }
        }
    }

    private static class EntityAISting extends EntityAIAttackMelee {
        private EntityAISting(EntityBee creature, double speedIn, boolean useLongMemory) {
            super(creature, speedIn, useLongMemory);
        }

        @Override
        public boolean shouldExecute() {
            return super.shouldExecute() && ((EntityBee)attacker).isAngry() && !((EntityBee)attacker).hasStung();
        }

        @Override
        public boolean shouldContinueExecuting() {
            return super.shouldExecute() && ((EntityBee)attacker).isAngry() && !((EntityBee)attacker).hasStung();
        }
    }

    private static class AIGrowCrops extends NotAngryGoal {
        private AIGrowCrops(EntityBee entityBee) {
            super(entityBee);
        }

        @Override
        public boolean canBeeStart() {
            if (bee.getCropsGrownSincePollination() >= 10) {
                return false;
            } else if (bee.rand.nextFloat() < 0.3F) {
                return false;
            } else {
                return bee.hasNectar() && bee.isHiveValid();
            }
        }

        @Override
        public boolean canBeeContinue() {
            return canBeeStart();
        }

        @Override
        public void updateTask() {
            if (bee.rand.nextInt(30) == 0) {
                for(int int_1 = 1; int_1 <= 2; ++int_1) {
                    BlockPos pos = bee.getPosition().down(int_1);
                    IBlockState state = bee.world.getBlockState(pos);
                    Block block = state.getBlock();
                    boolean flag = false;
                    PropertyInteger propertyInt = null;
                    if (Bee.canGrowBlock(block)) {
                        if (block instanceof BlockCrops) {
                            BlockCrops crops = (BlockCrops)block;
                            if (!crops.isMaxAge(state)) {
                                flag = true;
                                try {
                                    Method method = BlockCrops.class.getDeclaredMethod("getAgeProperty");
                                    method.setAccessible(true);
                                    propertyInt = (PropertyInteger) method.invoke(crops);

                                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            int int_3;
                            if (block instanceof BlockStem) {
                                int_3 = state.getValue(BlockStem.AGE);
                                if (int_3 < 7) {
                                    flag = true;
                                    propertyInt = BlockStem.AGE;
                                }
                            } else if (block == Init.BERRY_BUSH) {
                                int_3 = state.getValue(BlockBerryBush.AGE);
                                if (int_3 < 3) {
                                    flag = true;
                                    propertyInt = BlockBerryBush.AGE;
                                }
                            }
                        }

                        if (flag) {
                            bee.world.playEvent(2005, pos, 0);
                            bee.world.setBlockState(pos, state.withProperty(propertyInt, state.getValue(propertyInt) + 1));
                            bee.addCropCounter();
                        }
                    }
                }

            }
        }
    }

    public static EntityDamageSource causeBeeDamage(EntityBee entityBee) {
        return new EntityDamageSource("sting", entityBee);
    }

    private static class FlyHelper extends EntityMoveHelper {
        private final boolean b;
        private final float f;

        private FlyHelper(EntityBee entityBee, float f, boolean b) {
            super(entityBee);
            this.f = f;
            this.b = b;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO) {
                this.action = Action.WAIT;
                this.entity.setNoGravity(true);
                double d0 = this.posX - this.entity.posX;
                double d1 = this.posY - this.entity.posY;
                double d2 = this.posZ - this.entity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 < 2.500000277905201E-7D) {
                    this.entity.setMoveVertical(0.0F);
                    this.entity.setMoveForward(0.0F);
                    return;
                }

                float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 10.0F);
                float f1;

                if (this.entity.onGround) {
                    f1 = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                } else {
                    f1 = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue());
                }

                this.entity.setAIMoveSpeed(f1);
                double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                float f2 = (float)(-(MathHelper.atan2(d1, d4) * (180D / Math.PI)));
                this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, f2, this.f);
                this.entity.setMoveVertical(d1 > 0.0D ? f1 : -f1);
            } else {
                if (b) {
                    this.entity.setNoGravity(false);
                }

                this.entity.setMoveVertical(0.0F);
                this.entity.setMoveForward(0.0F);
            }
        }
    }

    private static class AIFollowTarget extends EntityAINearestAttackableTarget<EntityPlayer> {
        //private final EntityBee entityBee;

        private AIFollowTarget(EntityBee entityBee) {
            super(entityBee, EntityPlayer.class, true);
            //this.entityBee = entityBee;
        }

        @Override
        public boolean shouldExecute() {
            return canSting() && super.shouldExecute();
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (canSting() && taskOwner.getAttackTarget() != null) {
                return super.shouldContinueExecuting();
            } else {
                target = null;
                return false;
            }
        }

        private boolean canSting() {
            EntityBee bee = (EntityBee) taskOwner;
            return bee.isAngry() && !bee.hasStung();
        }
    }

    private static class AIRevenge extends EntityAIHurtByTarget {
        private AIRevenge(EntityBee entityBee) {
            super(entityBee, true);
        }

        @Override
        protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
            if (creatureIn instanceof EntityBee && taskOwner.canEntityBeSeen(entityLivingBaseIn) && ((EntityBee) creatureIn).setBeeAttacker(entityLivingBaseIn)) {
                creatureIn.setAttackTarget(entityLivingBaseIn);
            }
        }
    }

    public static Vec3d method_6377(EntityCreature entityIn, int var1, int var2, Vec3d vec3d, double var4) {
        return method_21758(entityIn, var1, var2, vec3d.subtract(entityIn.getPositionVector()), true, var4, entityIn::getBlockPathWeight);
    }

    public static Vec3d method_21758(EntityCreature entityIn, int var1, int var2, Vec3d vec3d, boolean var4, double var5, ToDoubleFunction<BlockPos> function) {
        return findVector(entityIn, var1, var2, 0, vec3d, var4, var5, function, !var4, (pos) -> entityIn.world.getBlockState(pos).getMaterial().isSolid(), 0, 0, true);
    }

    public static Vec3d findVector(EntityCreature entityIn, int var1, int var2, int var3, Vec3d vec3d, boolean var5, double var6, ToDoubleFunction<BlockPos> function, boolean var8, Predicate<BlockPos> posPredicate, int var10, int var11, boolean var12) {
        PathNavigate entityNavigation_1 = entityIn.getNavigator();
        Random random_1 = entityIn.getRNG();
        boolean flag;

        if (entityIn.hasHome()) {
            flag = entityIn.getHomePosition().distanceSq(entityIn.getPosition()) < (((double)(entityIn.getMaximumHomeDistance() + (float)var1) + 1.0D) * ((double)(entityIn.getMaximumHomeDistance() + (float)var1) + 1.0D));
        } else {
            flag = false;
        }

        boolean flag1 = false;
        double double_2 = Double.NEGATIVE_INFINITY;
        BlockPos pos = new BlockPos(entityIn);

        for(int int_6 = 0; int_6 < 10; ++int_6) {
            BlockPos blockPos_2 = method_6374(random_1, var1, var2, var3, vec3d, var6);
            if (blockPos_2 != null) {
                int x = blockPos_2.getX();
                int y = blockPos_2.getY();
                int z = blockPos_2.getZ();
                BlockPos blockPos;
                if (entityIn.hasHome() && var1 > 1) {
                    blockPos = entityIn.getHomePosition();
                    if (entityIn.posX > (double)blockPos.getX()) {
                        x -= random_1.nextInt(var1 / 2);
                    } else {
                        x += random_1.nextInt(var1 / 2);
                    }

                    if (entityIn.posZ > (double)blockPos.getZ()) {
                        z -= random_1.nextInt(var1 / 2);
                    } else {
                        z += random_1.nextInt(var1 / 2);
                    }
                }

                blockPos = new BlockPos((double)x + entityIn.posX, (double)y + entityIn.posY, (double)z + entityIn.posZ);
                if ((!flag || entityIn.isWithinHomeDistanceFromPosition(blockPos)) && (!var12 || entityNavigation_1.canEntityStandOnPos(blockPos))) {
                    if (var8) {
                        blockPos = method_21761(blockPos, random_1.nextInt(var10 + 1) + var11, entityIn.world.getHeight(), posPredicate);
                    }

                    if (var5 || !(entityIn.world.getBlockState(blockPos).getMaterial() == Material.WATER)) {
                        double double_3 = function.applyAsDouble(blockPos);
                        if (double_3 > double_2) {
                            double_2 = double_3;
                            pos = blockPos;
                            flag1 = true;
                        }
                    }
                }
            }
        }

        if (flag1) {
            return new Vec3d(pos);
        } else {
            return null;
        }
    }

    private static final float SQUARE_ROOT_OF_TWO = (float) Math.sqrt(2.0F);

    public static Vec3d method_6373(EntityCreature entityIn, int var1, int var2, Vec3d vec3d) {
        Vec3d vec3d_2 = vec3d.subtract(entityIn.getPositionVector());
        return method_21758(entityIn, var1, var2, vec3d_2, true, 1.5707963705062866D, entityIn::getBlockPathWeight);
    }

    private static BlockPos method_6374(Random random, int var1, int var2, int var3, Vec3d vec3d, double var5) {
        if (vec3d != null && var5 < 3.141592653589793D) {
            double d0 = MathHelper.atan2(vec3d.z, vec3d.x) - 1.5707963705062866D;
            double d1 = d0 + (double)(2.0F * random.nextFloat() - 1.0F) * var5;
            double d2 = Math.sqrt(random.nextDouble()) * SQUARE_ROOT_OF_TWO * (double)var1;
            double d3 = -d2 * Math.sin(d1);
            double d4 = d2 * Math.cos(d1);
            if (Math.abs(d3) <= (double)var1 && Math.abs(d4) <= (double)var1) {
                int int_7 = random.nextInt(2 * var2 + 1) - var2 + var3;
                return new BlockPos(d3, (double)int_7, d4);
            } else {
                return null;
            }
        } else {
            int x = random.nextInt(2 * var1 + 1) - var1;
            int y = random.nextInt(2 * var2 + 1) - var2 + var3;
            int z = random.nextInt(2 * var1 + 1) - var1;
            return new BlockPos(x, y, z);
        }
    }

    private static BlockPos method_21761(BlockPos pos, int aboveSolidAmount, int var2, Predicate<BlockPos> posPredicate) {
        if (aboveSolidAmount < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + aboveSolidAmount + ", expected >= 0");
        } else if (!posPredicate.test(pos)) {
            return pos;
        } else {
            BlockPos blockPos = pos.up();
            while (blockPos.getY() < var2 && posPredicate.test(blockPos)) {
                blockPos = blockPos.up();
            }

            BlockPos blockPos1;
            BlockPos blockPos2;
            for(blockPos1 = blockPos; blockPos1.getY() < var2 && blockPos1.getY() - blockPos.getY() < aboveSolidAmount; blockPos1 = blockPos2) {
                blockPos2 = blockPos1.up();
                if (posPredicate.test(blockPos2)) {
                    break;
                }
            }

            return blockPos1;
        }
    }
}
/*
    Mutex bits to Control Enum
    0x1 = 18405
    0x2 = 18406
    0x4 = 18407
    0x8 = 18408
 */