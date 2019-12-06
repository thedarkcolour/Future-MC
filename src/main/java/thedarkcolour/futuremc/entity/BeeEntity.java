package thedarkcolour.futuremc.entity;

import net.minecraft.block.BeetrootBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import thedarkcolour.futuremc.block.Blocks;
import thedarkcolour.futuremc.sound.Sounds;
import thedarkcolour.futuremc.tile.BeeHiveTileEntity;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class BeeEntity extends AnimalEntity implements IFlyingAnimal {
    private static final DataParameter<Byte> BEE_FLAGS = EntityDataManager.createKey(BeeEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> ANGER = EntityDataManager.createKey(BeeEntity.class, DataSerializers.VARINT);
    private UUID targetPlayer;
    private float currentPitch;
    private float lastPitch;
    private int ticksSinceSting;
    private int ticksSincePollination;
    private int cannotEnterHiveTicks;
    private int cropsGrownSincePollination;
    private BlockPos flowerPos;
    private BlockPos hivePos;

    public BeeEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
        flowerPos = BlockPos.ZERO;
        hivePos = BlockPos.ZERO;
        moveController = new BeeEntity.FlyController(this);
        lookController = new BeeEntity.LookController(this);

        setPathPriority(PathNodeType.WATER, -1.0F);
    }

    public static boolean canGrowBlock(Block block) {
        return block == Blocks.WHEAT || block == Blocks.CARROTS || block == Blocks.POTATOES || block == Blocks.BEETROOTS
                || block == Blocks.MELON_STEM || block == Blocks.PUMPKIN_STEM || block == Blocks.SWEET_BERRY_BUSH;
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(BEE_FLAGS, (byte) 0);
        dataManager.register(ANGER, 0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new BeeEntity.FindHiveGoal(this));
        goalSelector.addGoal(0, new BeeEntity.StingGoal(this, 1.399999976158142D, true));
        goalSelector.addGoal(1, new BeeEntity.EnterHiveGoal(this));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(3, new BeeEntity.TemptGoal(this));
        goalSelector.addGoal(4, new BeeEntity.PollinateGoal(this));
        goalSelector.addGoal(5, new FollowParentGoal(this, 1.25));
        goalSelector.addGoal(5, new BeeEntity.MoveToHiveGoal(this));
        goalSelector.addGoal(6, new BeeEntity.MoveToFlowerGoal(this));
        goalSelector.addGoal(7, new BeeEntity.GrowCropsGoal(this));
        goalSelector.addGoal(8, new WanderGoal(this));
        targetSelector.addGoal(1, new BeeEntity.RevengeGoal(this));
        targetSelector.addGoal(2, new FollowTargetGoal(this));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("HivePos", NBTUtil.writeBlockPos(hivePos));
        compound.put("FlowerPos", NBTUtil.writeBlockPos(flowerPos));
        compound.putBoolean("HasNectar", hasNectar());
        compound.putBoolean("HasStung", hasStung());
        compound.putInt("TicksSincePollination", ticksSincePollination);
        compound.putInt("CannotEnterHiveTicks", cannotEnterHiveTicks);
        compound.putInt("CropsGrownSincePollination", cropsGrownSincePollination);
        compound.putInt("Anger", getAnger());

        if (targetPlayer != null) {
            compound.putString("HurtBy", targetPlayer.toString());
        } else {
            compound.putString("HurtBy", "");
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        hivePos = NBTUtil.readBlockPos(compound.getCompound("HivePos"));
        flowerPos = NBTUtil.readBlockPos(compound.getCompound("FlowerPos"));
        super.readAdditional(compound);
        setHasNectar(compound.getBoolean("HasNectar"));
        setHasStung(compound.getBoolean("HasStung"));
        setAnger(compound.getInt("Anger"));
        ticksSincePollination = compound.getInt("TicksSincePollination");
        cannotEnterHiveTicks = compound.getInt("CannotEnterHiveTicks");
        cropsGrownSincePollination = compound.getInt("NumCropsGrownSincePollination");
        String s = compound.getString("HurtBy");
        if (!s.isEmpty()) {
            targetPlayer = UUID.fromString(s);
            PlayerEntity player = world.getPlayerByUuid(targetPlayer);
            setRevengeTarget(player);

            if (player != null) {
                attackingPlayer = player;
                recentlyHit = getRevengeTimer();
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(causeBeeDamage(this), (float)(getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
        if (flag) {
            applyEnchantments(this, entityIn);
            if (entityIn instanceof LivingEntity) {
                int duration = 0;
                if (world.getDifficulty() == Difficulty.NORMAL) {
                    duration = 10;
                } else if (world.getDifficulty() == Difficulty.HARD) {
                    duration = 18;
                }

                if (duration > 0) {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.POISON, duration * 20, 0));
                }
            }

            setHasStung(true);
            setAttackTarget(null);
            playSound(Sounds.BEE_STING, 1.0F, 1.0F);
        }

        return flag;
    }

    @Override
    public void tick() {
        super.tick();
        // Particles if the bee has nectar
        updateBodyPitch();
    }

    // Particle spawning method

    public BlockPos getFlowerPos() {
        return flowerPos;
    }

    public boolean hasFlower() {
        return flowerPos != BlockPos.ZERO;
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

    public float getBodyPitch(float partialTicks) {
        return MathHelper.lerp(partialTicks, lastPitch, currentPitch);
    }

    public void updateBodyPitch() {
        lastPitch = currentPitch;
        if (isNearTarget()) {
            currentPitch = Math.min(1.0F, currentPitch + 0.2F);
        } else {
            currentPitch = Math.max(0.0F, currentPitch - 0.24F);
        }
    }

    @Override
    public void setRevengeTarget(LivingEntity entityLiving) {
        super.setRevengeTarget(entityLiving);
        if (entityLiving != null) {
            targetPlayer = entityLiving.getUniqueID();
        }
    }

    @Override
    protected void updateAITasks() {
        if (hasStung()) {
            ++ticksSinceSting;
            if (ticksSinceSting % 5 == 0 && rand.nextInt(MathHelper.clamp(1200 - ticksSinceSting, 1, 1200)) == 0) {
                attackEntityFrom(DamageSource.GENERIC, getHealth());
            }
        }

        if (isAngry()) {
            int anger = getAnger();
            setAnger(anger - 1);
            LivingEntity livingEntity = getAttackTarget();
            if (anger == 0 && livingEntity != null) {
                setBeeAttacker(livingEntity);
            }
        }

        if (!hasNectar()) {
            ++ticksSincePollination;
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
        return hivePos != BlockPos.ZERO;
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
    public void livingTick() {
        super.livingTick();

        if (!world.isRemote) {
            if (cannotEnterHiveTicks > 0) {
                --cannotEnterHiveTicks;
            }
            if (isPollinating() && !hasPath()) {
                float f = rand.nextBoolean() ? 2.0F : -2.0F;
                Vec3d vec3d;

                if (hasFlower()) {
                    BlockPos pos = flowerPos.add(0, f, 0);
                    vec3d = new Vec3d(pos);
                } else {
                    vec3d = getPositionVec().add(0, f, 0);
                }

                getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.4000000059604645D);
            }

            setNearTarget(isAngry() && !hasStung() && getAttackTarget() != null && getAttackTarget().getDistanceSq(this) < 4.0D);

            if (hasHive() && ticksExisted % 20 == 0 && !isHiveValid()) {
                hivePos = BlockPos.ZERO;
            }
        }
    }

    private boolean isHiveValid() {
        if (!hasHive()) {
            return false;
        } else {
            TileEntity te = world.getTileEntity(hivePos);
            return te instanceof BeeHiveTileEntity;
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
        byte b = dataManager.get(BEE_FLAGS);
        if (bool) {
            dataManager.set(BEE_FLAGS, (byte)(b | flag));
        } else {
            dataManager.set(BEE_FLAGS, (byte)(b & ~flag));
        }
    }

    public boolean getBeeFlag(int i) {
        return (dataManager.get(BEE_FLAGS) & i) != 0;
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.6000000238418579D);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator navigateFlying = new FlyingPathNavigator(this, worldIn) {
            @Override
            public boolean canEntityStandOnPos(BlockPos pos) {
                return worldIn.getBlockState(pos.down()).isAir();
            }
        };
        navigateFlying.setCanOpenDoors(false);
        navigateFlying.setCanEnterDoors(true);
        navigateFlying.setCanSwim(false);

        return navigateFlying;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().isIn(ItemTags.SMALL_FLOWERS);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {}

    @Override
    protected SoundEvent getAmbientSound() {
        return isAngry() ? Sounds.BEE_AGGRESSIVE : Sounds.BEE_PASSIVE;
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
    public AgeableEntity createChild(AgeableEntity ageable) {
        return (AgeableEntity) EntityTypes.BEE.create(world);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return isChild() ? sizeIn.height * 0.5F : sizeIn.height * 0.5F;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {}

    @Override
    public void fall(float distance, float damageMultiplier) {}

    @Override
    protected boolean makeFlySound() {
        return true;
    }

    public void onHoneyDelivered() {
        setHasNectar(false);
        resetCropCounter();
    }

    private Optional<BlockPos> getBlockInRange(Predicate<BlockPos> predicate, int range) {
        BlockPos pos = getPosition().add(-range, -range, -range);

        for (int x = 0; x < ((range << 1) + 1); ++x) {
            for (int y = 0; y < ((range << 1) + 1); ++y) {
                for (int z = 0; z < ((range << 1) + 1); ++z) {
                    if (predicate.test(pos.add(x, y, z))) {
                        return Optional.of(pos.add(x, y, z));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public boolean setBeeAttacker(Entity entityIn) {
        setAnger(400 + rand.nextInt(400));
        if (entityIn instanceof LivingEntity) {
            setRevengeTarget((LivingEntity) entityIn);
        }

        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        } else {
            Entity attacker = source.getTrueSource();
            if (attacker instanceof PlayerEntity && !((PlayerEntity)attacker).isCreative() && this.canEntityBeSeen(attacker)) {
                setPollinating(false);
                setBeeAttacker(attacker);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    public BlockPos getHivePos() {
        return hivePos;
    }

    private static class EnterHiveGoal extends NotAngryGoal {
        private EnterHiveGoal(BeeEntity entityBee) {
            super(entityBee);
        }

        @Override
        public boolean canBeeStart() {
            if (bee.hasNectar() && bee.hasHive() && !bee.hasStung() && bee.canEnterHive()) {
                if (bee.hivePos.distanceSq(bee.getPosition()) < 4.0D) {
                    TileEntity te = bee.world.getTileEntity(bee.hivePos);
                    if (te instanceof BeeHiveTileEntity) {
                        BeeHiveTileEntity hive = (BeeHiveTileEntity)te;
                        if (hive.canEnter()) {
                            return true;
                        }

                        bee.hivePos = BlockPos.ZERO;
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
            if (tile instanceof BeeHiveTileEntity) {
                BeeHiveTileEntity hive = (BeeHiveTileEntity) tile;
                hive.tryEnterHive(bee, bee.hasNectar());
            }
        }
    }

    private static class StingGoal extends MeleeAttackGoal {
        private StingGoal(BeeEntity creature, double speedIn, boolean useLongMemory) {
            super(creature, speedIn, useLongMemory);
        }

        @Override
        public boolean shouldExecute() {
            return super.shouldExecute() && ((BeeEntity)attacker).isAngry() && !((BeeEntity)attacker).hasStung();
        }

        @Override
        public boolean shouldContinueExecuting() {
            return super.shouldExecute() && ((BeeEntity)attacker).isAngry() && !((BeeEntity)attacker).hasStung();
        }
    }

    private static class GrowCropsGoal extends NotAngryGoal {
        private GrowCropsGoal(BeeEntity entityBee) {
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
        public void tick() {
            if (bee.rand.nextInt(30) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    BlockPos pos = bee.getPosition().down(i);
                    BlockState state = bee.world.getBlockState(pos);
                    Block block = state.getBlock();
                    boolean canGrow = false;
                    IntegerProperty ageProperty = null;
                    if (canGrowBlock(block)) {
                        if (block instanceof CropsBlock) {
                            CropsBlock crops = (CropsBlock)block;
                            if (!crops.isMaxAge(state)) {
                                canGrow = true;

                                if (block instanceof BeetrootBlock) {
                                    ageProperty = BeetrootBlock.BEETROOT_AGE;
                                } else {
                                    ageProperty = crops.getAgeProperty();
                                }
                            }
                        } else {
                            int age;
                            if (block instanceof StemBlock) {
                                age = state.get(StemBlock.AGE);
                                if (age < 7) {
                                    canGrow = true;
                                    ageProperty = StemBlock.AGE;
                                }
                            } else if (block == Blocks.SWEET_BERRY_BUSH) {
                                age = state.get(SweetBerryBushBlock.AGE);
                                if (age < 3) {
                                    canGrow = true;
                                    ageProperty = SweetBerryBushBlock.AGE;
                                }
                            }
                        }

                        if (ageProperty != null && canGrow) {
                            bee.world.playEvent(2005, pos, 0);
                            bee.world.setBlockState(pos, state.with(ageProperty, state.get(ageProperty) + 1));
                            bee.addCropCounter();
                        }
                    }
                }

            }
        }
    }

    private static class FindHiveGoal extends NotAngryGoal {
        private FindHiveGoal(BeeEntity bee) {
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
            Optional<BlockPos> optionalPos = bee.getBlockInRange(pos -> bee.world.getTileEntity(pos) instanceof BeeHiveTileEntity && ((BeeHiveTileEntity) bee.world.getTileEntity(pos)).canEnter(), 10);
            if (optionalPos.isPresent()) {
                BlockPos pos = optionalPos.get();
                TileEntity tile = bee.world.getTileEntity(pos);
                if (tile instanceof BeeHiveTileEntity && ((BeeHiveTileEntity) tile).canEnter()) {
                    bee.hivePos = pos;
                }
            }
        }
    }

    private static class PollinateGoal extends NotAngryGoal {
        private static final Predicate<BlockState> TEST = state -> state.isIn(BlockTags.SMALL_FLOWERS);
        private int lastPollinationTicks = 0;
        private int pollinationTicks = 0;

        private PollinateGoal(BeeEntity bee) {
            super(bee);
            setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canBeeStart() {
            if (bee.hasNectar()) {
                return false;
            } else if (bee.rand.nextFloat() < 0.7F) {
                return false;
            } else {
                Optional<BlockPos> flower = getFlower();
                if (flower.isPresent()) {
                    bee.flowerPos = flower.get();
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
                Optional<BlockPos> flower = getFlower();
                return flower.isPresent();
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
        public void tick() {
            ++pollinationTicks;
            if (bee.rand.nextFloat() < 0.05F && pollinationTicks > lastPollinationTicks + 60) {
                lastPollinationTicks = pollinationTicks;
                bee.playSound(Sounds.BEE_POLLINATE, 1.0F, 1.0F);
            }
        }
        private Optional<BlockPos> getFlower() {
            return bee.getBlockInRange(pos -> bee.world.getBlockState(pos).isIn(BlockTags.SMALL_FLOWERS), 2);
        }
    }

    private static class LookController extends net.minecraft.entity.ai.controller.LookController {
        private BeeEntity bee;

        private LookController(BeeEntity bee) {
            super(bee);
            this.bee = bee;
        }

        @Override
        public void tick() {
            if (!bee.isAngry()) {
                super.tick();
            }
        }
    }

    private static class MoveToFlowerGoal extends MoveToBlockGoal {
        private MoveToFlowerGoal(BeeEntity bee) {
            super(bee, 3);
        }

        @Override
        public boolean canBeeStart() {
            return isHiveValid() && bee.ticksSincePollination > 3600;
        }

        @Override
        public boolean canBeeContinue() {
            return canBeeStart() && super.canBeeContinue();
        }

        @Override
        public void resetTask() {
            if (!bee.world.getBlockState(bee.flowerPos).isIn(BlockTags.SMALL_FLOWERS)) {
                bee.flowerPos = BlockPos.ZERO;
            }
        }

        @Override
        protected BlockPos getTargetPos() {
            return bee.flowerPos;
        }

        private boolean isHiveValid() {
            return getTargetPos() != BlockPos.ZERO;
        }
    }

    private static class MoveToHiveGoal extends MoveToBlockGoal {
        private MoveToHiveGoal(BeeEntity bee) {
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

        private MoveToBlockGoal(BeeEntity bee, int range) {
            super(bee);
            this.range = range;
            setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        protected abstract BlockPos getTargetPos();

        @Override
        public boolean canBeeContinue() {
            return (getTargetPos().distanceSq(bee.getPosition()) > range * range);
        }

        @Override
        public void tick() {
            BlockPos pos = getTargetPos();
            boolean flag = pos.distanceSq(bee.getPosition()) < 64.0D;

            if (bee.getNavigator().noPath()) {
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetTowardsScaled(bee, 8, 6, new Vec3d(pos), 0.3141592741012573D);

                if (vec3d == null) {
                    vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(bee, 3, 3, new Vec3d(pos));
                }

                if (vec3d != null && !flag && bee.world.getBlockState(new BlockPos(vec3d)).getBlock() != Blocks.WATER) {
                    vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(bee, 8, 6, new Vec3d(pos));
                }

                if (vec3d == null) {
                    failedToFindPath = true;
                    return;
                }

                bee.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            }
        }
    }

    private static class WanderGoal extends Goal {
        private final BeeEntity bee;

        private WanderGoal(BeeEntity bee) {
            setMutexFlags(EnumSet.of(Flag.MOVE));
            this.bee = bee;
        }

        @Override
        public boolean shouldExecute() {
            return bee.navigator.noPath() && bee.rand.nextInt(10) == 0;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !bee.navigator.noPath();
        }

        @Override
        public void startExecuting() {
            Vec3d vec3d = RandomPositionGenerator.findRandomTarget(bee, 8, 7);
            if (vec3d != null) {
                bee.navigator.tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            }
        }

        @Override
        public void resetTask() {
            bee.navigator.setPath(null, 1.0F);
        }
    }

    private static abstract class NotAngryGoal extends Goal {
        protected final BeeEntity bee;

        private NotAngryGoal(BeeEntity bee) {
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

    private static class FollowTargetGoal extends NearestAttackableTargetGoal<PlayerEntity> {
        private FollowTargetGoal(BeeEntity entityBee) {
            super(entityBee, PlayerEntity.class, true);
        }

        @Override
        public boolean shouldExecute() {
            return canSting() && super.shouldExecute();
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (canSting() && goalOwner.getAttackTarget() != null) {
                return super.shouldContinueExecuting();
            } else {
                target = null;
                return false;
            }
        }

        private boolean canSting() {
            BeeEntity bee = (BeeEntity) goalOwner;
            return bee.isAngry() && !bee.hasStung();
        }
    }

    private static class RevengeGoal extends HurtByTargetGoal {
        private RevengeGoal(BeeEntity entityBee) {
            super(entityBee);
            setCallsForHelp(BeeEntity.class);
        }

        @Override
        protected void setAttackTarget(MobEntity creatureIn, LivingEntity entityLivingBaseIn) {
            if (creatureIn instanceof BeeEntity && goalOwner.canEntityBeSeen(entityLivingBaseIn) && ((BeeEntity) creatureIn).setBeeAttacker(entityLivingBaseIn)) {
                creatureIn.setAttackTarget(entityLivingBaseIn);
            }
        }
    }

    private static class TemptGoal extends Goal {
        private final BeeEntity bee;
        protected PlayerEntity closestPlayer;
        private int coolDown;

        private TemptGoal(BeeEntity bee) {
            this.bee = bee;
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            if (coolDown > 0) {
                --coolDown;
                return false;
            } else {
                closestPlayer = bee.world.getClosestPlayer(bee, 10.0D);
                if (closestPlayer == null) {
                    return false;
                } else {
                    return isTempting(closestPlayer.getHeldItemMainhand()) || isTempting(closestPlayer.getHeldItemOffhand());
                }
            }
        }

        protected boolean isTempting(ItemStack stack) {
            return stack.getItem().isIn(ItemTags.SMALL_FLOWERS);
        }

        @Override
        public boolean shouldContinueExecuting() {
            return shouldExecute();
        }

        @Override
        public void resetTask() {
            closestPlayer = null;
            bee.getNavigator().clearPath();
            coolDown = 100;
        }

        @Override
        public void tick() {
            bee.getLookController().setLookPositionWithEntity(closestPlayer, 75.0F, 40.0F);
            if (bee.getDistanceSq(closestPlayer) < 6.25D) {
                bee.getNavigator().clearPath();
            } else {
                bee.getNavigator().tryMoveToEntityLiving(closestPlayer, 1.25D);
            }
        }
    }

    public static EntityDamageSource causeBeeDamage(BeeEntity entityBee) {
        return new EntityDamageSource("sting", entityBee);
    }

    private static class FlyController extends MovementController {
        private FlyController(BeeEntity entityBee) {
            super(entityBee);
        }

        @Override
        public void tick() {
            if (action == Action.MOVE_TO) {
                action = Action.WAIT;
                mob.setNoGravity(true);
                double d0 = posX - mob.posX;
                double d1 = posY - mob.posY;
                double d2 = posZ - mob.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 < 2.500000277905201E-7D) {
                    mob.setMoveVertical(0.0F);
                    mob.setMoveForward(0.0F);
                    return;
                }

                float f0 = (float)(MathHelper.atan2(d2, d0) * 57.2957763671875D) - 90.0F;
                mob.rotationYaw = limitAngle(mob.rotationYaw, f0, 90.0F);
                float f1;

                if (mob.onGround) {
                    f1 = (float)(speed * mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                } else {
                    f1 = (float)(speed * mob.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue());
                }

                mob.setAIMoveSpeed(f1);
                double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                float f2 = (float)(-(MathHelper.atan2(d1, d4) * 57.2957763671875D));
                mob.rotationPitch = limitAngle(mob.rotationPitch, f2, 20);
                mob.setMoveVertical(d1 > 0.0D ? f1 : -f1);
            } else {
                mob.setMoveVertical(0.0F);
                mob.setMoveForward(0.0F);
            }
        }
    }
}