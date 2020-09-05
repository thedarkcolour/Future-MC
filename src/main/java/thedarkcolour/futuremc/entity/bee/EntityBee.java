package thedarkcolour.futuremc.entity.bee;

import kotlin.collections.CollectionsKt;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
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
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thedarkcolour.futuremc.api.BeePollinationHandler;
import thedarkcolour.futuremc.api.BeePollinationHandlerJVM;
import thedarkcolour.futuremc.registry.FSounds;
import thedarkcolour.futuremc.tile.BeeHiveTile;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
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
    @Nullable
    private BlockPos flowerPos;
    @Nullable
    private BlockPos hivePos;

    public EntityBee(World worldIn) {
        super(worldIn);
        flowerPos = null;
        hivePos = null;
        moveHelper = new EntityFlyHelper(this);
        lookHelper = new LookHelper(this);

        setSize(0.7F, 0.7F);
        setSize(0.7F, 0.7F);

        setPathPriority(PathNodeType.WATER, -1.0F);
    }

    public static boolean isFlowerValid(IBlockState state) {
        return BeeEntity.FLOWERS.contains(state);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BEE_FLAGS, (byte)0);
        this.dataManager.register(ANGER, 0);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new AIFindHive(this));
        tasks.addTask(0, new AISting(this, 1.399999976158142D, true));
        tasks.addTask(1, new AIEnterHive(this));
        tasks.addTask(2, new EntityAIMate(this, 1.0D));
        tasks.addTask(3, new AITempt(this));
        tasks.addTask(4, new AIPollinate(this));
        tasks.addTask(5, new EntityAIFollowParent(this, 1.25D));
        tasks.addTask(5, new AIMoveToHive(this));
        tasks.addTask(6, new AIMoveToFlower(this));
        tasks.addTask(7, new AIGrowCrops(this));
        tasks.addTask(8, new AIWander(this));
        targetTasks.addTask(1, new AIRevenge(this));
        targetTasks.addTask(2, new AIFollowTarget(this));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (hivePos != null)
            compound.setTag("HivePos", NBTUtil.createPosTag(hivePos));
        if (flowerPos != null)
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
    public void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("HivePos"))
            hivePos = NBTUtil.getPosFromTag(compound.getCompoundTag("HivePos"));
        if (compound.hasKey("FlowerPos"))
            flowerPos = NBTUtil.getPosFromTag(compound.getCompoundTag("FlowerPos"));
        super.readEntityFromNBT(compound);
        setHasNectar(compound.getBoolean("HasNectar"));
        setHasStung(compound.getBoolean("HasStung"));
        setAnger(compound.getInteger("Anger"));
        ticksSincePollination = compound.getInteger("TicksSincePollination");
        cannotEnterHiveTicks = compound.getInteger("CannotEnterHiveTicks");
        cropsGrownSincePollination = compound.getInteger("CropsGrownSincePollination");

        String s = compound.getString("HurtBy");
        if (!s.isEmpty()) {
            targetPlayer = UUID.fromString(s);
            EntityPlayer player = world.getPlayerEntityByUUID(targetPlayer);
            setRevengeTarget(player);
            if (player != null) {
                attackingPlayer = player;
                recentlyHit = getRevengeTimer();
            }
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
            playSound(FSounds.BEE_STING, 1.0F, 1.0F);
        }

        return flag;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        updateBodyPitch();
    }

    @Nullable
    public BlockPos getFlowerPos() {
        return flowerPos;
    }

    @Nullable
    public BlockPos getHivePos() {
        return hivePos;
    }

    public boolean hasFlower() {
        return flowerPos != null;
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
        return lastPitch + partialTickTime * (currentPitch - lastPitch);
    }

    private void updateBodyPitch() {
        lastPitch = currentPitch;

        if (this.isNearTarget()) {
            currentPitch = Math.min(1.0F, currentPitch + 0.2F);
        } else {
            currentPitch = Math.max(0.0F, currentPitch - 0.24F);
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
            ++ticksSinceSting;
            if (ticksSinceSting % 5 == 0 && rand.nextInt(MathHelper.clamp(1200 - ticksSinceSting, 1, 1200)) == 0) {
                attackEntityFrom(DamageSource.GENERIC, getHealth());
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
        return hivePos != null;
    }

    public int getCropsGrownSincePollination() {
        return cropsGrownSincePollination;
    }

    public void resetCropCounter() {
        cropsGrownSincePollination = 0;
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
                    vec3d = getPositionVector().add(0.0D, f, 0.0D);
                }

                getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.4000000059604645D);
            }

            setNearTarget(isAngry() && !hasStung() && getAttackTarget() != null && getAttackTarget().getDistanceSq(this) < 4.0D);

            if (hasHive() && ticksExisted % 20 == 0 && !isHiveValid()) {
                hivePos = null;
            }
        }
    }

    private boolean isHiveValid() {
        if (!hasHive()) {
            return false;
        } else {
            TileEntity te = world.getTileEntity(hivePos);
            return te instanceof BeeHiveTile;
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
        navigateFlying.setCanEnterDoors(true);
        navigateFlying.setCanFloat(false);

        return navigateFlying;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return isFlowerValid(Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()));
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {}

    @Override
    protected SoundEvent getAmbientSound() {
        return isAngry() ? FSounds.BEE_AGGRESSIVE : FSounds.BEE_PASSIVE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return FSounds.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return FSounds.BEE_DEATH;
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
        return height * 0.5f;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {}

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}

    @Override
    protected boolean makeFlySound() {
        return true;
    }

    public void onHoneyDelivered() {
        setHasNectar(false);
        resetCropCounter();
    }

    private List<BlockPos> getBlockInRange(Predicate<BlockPos> predicate, int range) {
        List<BlockPos> list = CollectionsKt.arrayListOf();

        for (BlockPos pos : BlockPos.getAllInBoxMutable(
                getPosition().add(-range, -range, -range),
                getPosition().add(+range, +range, +range)
        )) {
            if (predicate.test(pos)) {
                list.add(pos.toImmutable());
            }
        }

        if (list.isEmpty()) {
            return CollectionsKt.emptyList();
        } else {
            // randomize the order of the contents
            // so bees don't always go to same flower
            Collections.shuffle(list);
            return list;
        }
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
        if (isEntityInvulnerable(source)) {
            return false;
        } else {
            Entity attacker = source.getTrueSource();
            if (attacker instanceof EntityPlayer && !((EntityPlayer)attacker).isCreative() && this.canEntityBeSeen(attacker)) {
                setPollinating(false);
                setBeeAttacker(attacker);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    private static final class AIEnterHive extends AINotAngry {
        private AIEnterHive(EntityBee entityBee) {
            super(entityBee);
        }

        @Override
        public boolean canBeeStart() {
            if (bee.hasNectar() && bee.hasHive() && !bee.hasStung() && bee.canEnterHive()) {
                if (bee.hivePos.distanceSq(bee.getPosition()) < 4.0D) {
                    TileEntity te = bee.world.getTileEntity(bee.hivePos);
                    if (te instanceof BeeHiveTile) {
                        BeeHiveTile hive = (BeeHiveTile)te;
                        if (!hive.isFullOfBees()) {
                            return true;
                        }

                        bee.hivePos = null;
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
            if (tile instanceof BeeHiveTile) {
                BeeHiveTile hive = (BeeHiveTile) tile;
                hive.tryEnterHive(bee, bee.hasNectar(), 0);
            }
        }
    }

    private static final class AISting extends EntityAIAttackMelee {
        private AISting(EntityBee creature, double speedIn, boolean useLongMemory) {
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

    public static final class AIGrowCrops extends AINotAngry {
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
                for (int i = 1; i <= 2; ++i) {
                    BlockPos pos = bee.getPosition().down(i);
                    IBlockState state = bee.world.getBlockState(pos);
                    Block block = state.getBlock();

                    // Mod compatibility hook
                    BeePollinationHandler handler = BeePollinationHandlerJVM.getHandler(block);

                    if (handler != null && handler.pollinateCrop(bee.world, pos, state, bee)) {
                        ++bee.cropsGrownSincePollination;
                    }
                }
            }
        }
    }

    private static final class AIFindHive extends AINotAngry {
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
            List<BlockPos> possibleHives = bee.getBlockInRange(pos -> bee.world.getTileEntity(pos) instanceof BeeHiveTile && !((BeeHiveTile) bee.world.getTileEntity(pos)).isFullOfBees(), 20);

            for (BlockPos hive : possibleHives) {
                TileEntity tile = bee.world.getTileEntity(hive);
                if (tile instanceof BeeHiveTile && !((BeeHiveTile)tile).isFullOfBees()) {
                    bee.hivePos = hive;
                }
            }
        }
    }

    private static final class AIPollinate extends AINotAngry {
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
                BlockPos flower = getFlower();
                if (flower != null) {
                    bee.flowerPos = flower;
                    bee.moveHelper.setMoveTo(flower.getX() + 0.5, flower.getY() + 0.5, flower.getZ() + 0.5, 1.2);
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
                BlockPos flower = getFlower();
                return flower != null;
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
                bee.playSound(FSounds.BEE_POLLINATE, 1.0F, 1.0F);
            }
        }

        @Nullable
        private BlockPos getFlower() {
            List<BlockPos> list = bee.getBlockInRange(pos -> isFlowerValid(bee.world.getBlockState(pos)), 5);
            if (list.isEmpty())
                return null;
            return list.get(bee.getRNG().nextInt(list.size()));
        }
    }

    private static final class LookHelper extends EntityLookHelper {
        private final EntityBee bee;

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

    private static final class AIMoveToFlower extends AIMoveToBlock {
        private AIMoveToFlower(EntityBee bee) {
            super(bee, 3);
        }

        @Override
        public boolean canBeeStart() {
            return bee.hasFlower() && bee.ticksSincePollination > 3600;
        }

        @Override
        public boolean canBeeContinue() {
            return this.canBeeStart() && super.canBeeContinue();
        }

        @Override
        public void resetTask() {
            if (!isFlowerValid(bee.world.getBlockState(bee.flowerPos))) {
                bee.flowerPos = null;
            }
        }

        @Override
        protected BlockPos getTargetPos() {
            return bee.flowerPos;
        }
    }

    private static final class AIMoveToHive extends AIMoveToBlock {
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

    private abstract static class AIMoveToBlock extends AINotAngry {
        protected boolean failedToFindPath = false;
        protected int range;

        private AIMoveToBlock(EntityBee bee, int range) {
            super(bee);
            this.range = range;
            setMutexBits(0x1);
        }

        @Nullable
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
                Vec3d vec3d = findVector(bee, 8, 6, new Vec3d(pos), 0.3141592741012573D);

                if (vec3d == null) {
                    vec3d = findVector(bee, 3, 3, new Vec3d(pos));
                }

                if (vec3d != null && !flag && bee.world.getBlockState(new BlockPos(vec3d)).getBlock() != Blocks.WATER) {
                    vec3d = findVector(bee, 8, 6, new Vec3d(pos));
                }

                if (vec3d == null) {
                    failedToFindPath = true;
                    return;
                }

                bee.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            }
        }
    }

    private static final class AIWander extends EntityAIBase {
        private final EntityBee bee;

        private AIWander(EntityBee bee) {
            setMutexBits(0x1);
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
            if (bee.hivePos != null && bee.hivePos.distanceSq(bee.getPosition()) >= 484) {
                vec3d = new Vec3d(bee.hivePos).subtract(new Vec3d(bee.getPosition())).normalize();

                bee.navigator.tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                return;
            }
            if (vec3d != null) {
                bee.navigator.tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            }
        }

        @Override
        public void resetTask() {
            bee.navigator.setPath(null, 1.0F);
        }
    }

    private static abstract class AINotAngry extends EntityAIBase {
        protected final EntityBee bee;

        private AINotAngry(EntityBee bee) {
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

    private static final class AIFollowTarget extends EntityAINearestAttackableTarget<EntityPlayer> {
        private AIFollowTarget(EntityBee entityBee) {
            super(entityBee, EntityPlayer.class, true);
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

    private static final class AIRevenge extends EntityAIHurtByTarget {
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

    private static final class AITempt extends EntityAIBase {
        private final EntityBee bee;
        private EntityPlayer closestPlayer;
        private int coolDown;

        private AITempt(EntityBee bee) {
            this.bee = bee;
            setMutexBits(0x3);
        }

        @Override
        public boolean shouldExecute() {
            if (coolDown > 0) {
                --coolDown;
                return false;
            } else {
                closestPlayer = bee.world.getClosestPlayerToEntity(bee, 10.0D);
                if (closestPlayer == null) {
                    return false;
                } else {
                    return isTempting(closestPlayer.getHeldItemMainhand()) || isTempting(closestPlayer.getHeldItemOffhand());
                }
            }
        }

        private static boolean isTempting(ItemStack stack) {
            return isFlowerValid(Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()));
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
        public void updateTask() {
            bee.getLookHelper().setLookPositionWithEntity(closestPlayer, 75.0F, 40.0F);
            if (bee.getDistanceSq(closestPlayer) < 6.25D) {
                bee.getNavigator().clearPath();
            } else {
                bee.getNavigator().tryMoveToEntityLiving(closestPlayer, 1.25D);
            }
        }
    }

    public static EntityDamageSource causeBeeDamage(EntityBee entityBee) {
        return new EntityDamageSource("sting", entityBee);
    }

    private static final class FlyHelper extends EntityMoveHelper {
        private final EntityBee entityBee;

        private FlyHelper(EntityBee entityBee) {
            super(entityBee);
            this.entityBee = entityBee;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (!entityBee.isPollinating()) {
                if (action == Action.MOVE_TO) {
                    action = Action.WAIT;
                    entity.setNoGravity(true);
                    double d0 = posX - entity.posX;
                    double d1 = posY - entity.posY;
                    double d2 = posZ - entity.posZ;
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d3 < 2.500000277905201E-7D) {
                        entity.setMoveVertical(0.0F);
                        entity.setMoveForward(0.0F);
                        return;
                    }

                    float f0 = (float)(MathHelper.atan2(d2, d0) * 57.2957763671875D) - 90.0F;
                    entity.rotationYaw = limitAngle(entity.rotationYaw, f0, 10.0f);
                    float f1;

                    if (entity.onGround) {
                        f1 = (float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                    } else {
                        f1 = (float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue());
                    }

                    entity.setAIMoveSpeed(f1);
                    double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                    float f2 = (float)(-(MathHelper.atan2(d1, d4) * 57.2957763671875D));
                    entity.rotationPitch = limitAngle(entity.rotationPitch, f2, 10.0f);
                    entity.setMoveVertical(d1 > 0.0D ? f1 : -f1);
                } else {
                    entity.setNoGravity(false);
                    entity.setMoveVertical(0.0F);
                    entity.setMoveForward(0.0F);
                }
            }
        }
    }

    public static Vec3d findVector(EntityCreature entityIn, int var1, int var2, Vec3d vec3d, double var4) {
        return findVector(entityIn, var1, var2, vec3d.subtract(entityIn.getPositionVector()), true, var4, entityIn::getBlockPathWeight);
    }

    public static Vec3d findVector(EntityCreature entityIn, int var1, int var2, Vec3d vec3d, boolean var4, double var5, ToDoubleFunction<BlockPos> function) {
        return findVector(entityIn, var1, var2, 0, vec3d, var4, var5, function, !var4, (pos) -> entityIn.world.getBlockState(pos).getMaterial().isSolid(), 0, 0, true);
    }

    public static Vec3d findVector(EntityCreature entityIn, int var1, int var2, int var3, Vec3d vec3d, boolean var5, double var6, ToDoubleFunction<BlockPos> function, boolean var8, Predicate<BlockPos> posPredicate, int var10, int var11, boolean var12) {
        PathNavigate navigator = entityIn.getNavigator();
        Random rand = entityIn.getRNG();
        boolean flag;

        if (entityIn.hasHome()) {
            flag = entityIn.getHomePosition().distanceSq(entityIn.getPosition()) < (((double)(entityIn.getMaximumHomeDistance() + (float)var1) + 1.0D) * ((double)(entityIn.getMaximumHomeDistance() + (float)var1) + 1.0D));
        } else {
            flag = false;
        }

        boolean flag1 = false;
        double d0 = Double.NEGATIVE_INFINITY;
        BlockPos pos = new BlockPos(entityIn);

        for (int i = 0; i < 10; ++i) {
            BlockPos pos1 = getRandomOffset(rand, var1, var2, var3, vec3d, var6);
            if (pos1 != null) {
                int x = pos1.getX();
                int y = pos1.getY();
                int z = pos1.getZ();
                BlockPos pos2;
                if (entityIn.hasHome() && var1 > 1) {
                    pos2 = entityIn.getHomePosition();
                    if (entityIn.posX > pos2.getX()) {
                        x -= rand.nextInt(var1 / 2);
                    } else {
                        x += rand.nextInt(var1 / 2);
                    }

                    if (entityIn.posZ > (double)pos2.getZ()) {
                        z -= rand.nextInt(var1 / 2);
                    } else {
                        z += rand.nextInt(var1 / 2);
                    }
                }

                pos2 = new BlockPos((double)x + entityIn.posX, (double)y + entityIn.posY, (double)z + entityIn.posZ);
                if ((!flag || entityIn.isWithinHomeDistanceFromPosition(pos2)) && (!var12 || navigator.canEntityStandOnPos(pos2))) {
                    if (var8) {
                        pos2 = findValidPositionAbove(pos2, rand.nextInt(var10 + 1) + var11, entityIn.world.getHeight(), posPredicate);
                    }

                    if (var5 || !(entityIn.world.getBlockState(pos2).getMaterial() == Material.WATER)) {
                        double double_3 = function.applyAsDouble(pos2);
                        if (double_3 > d0) {
                            d0 = double_3;
                            pos = pos2;
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

    @Nullable
    public static Vec3d findVector(@NotNull EntityCreature entityIn, int var1, int var2, @NotNull Vec3d vec3d) {
        Vec3d vec = vec3d.subtract(entityIn.getPositionVector());
        return findVector(entityIn, var1, var2, vec, true, Math.PI / 2.0f, entityIn::getBlockPathWeight);
    }

    @Nullable
    private static BlockPos getRandomOffset(Random random, int var1, int var2, int var3, Vec3d vec3d, double var5) {
        if (vec3d != null && var5 < Math.PI) {
            double d0 = MathHelper.atan2(vec3d.z, vec3d.x) - (Math.PI / 2.0f);
            double d1 = d0 + (double)(2.0F * random.nextFloat() - 1.0F) * var5;
            double d2 = Math.sqrt(random.nextDouble()) * MathHelper.SQRT_2 * var1;
            double d3 = -d2 * Math.sin(d1);
            double d4 = d2 * Math.cos(d1);
            if (Math.abs(d3) <= (double)var1 && Math.abs(d4) <= (double)var1) {
                int y = random.nextInt(2 * var2 + 1) - var2 + var3;
                return new BlockPos(d3, y, d4);
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

    private static BlockPos findValidPositionAbove(BlockPos pos, int aboveSolidAmount, int heightLimit, Predicate<BlockPos> condition) {
        if (aboveSolidAmount < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + aboveSolidAmount + ", expected >= 0");
        } else if (!condition.test(pos)) {
            return pos;
        } else {
            BlockPos blockPos = pos.up();
            while (blockPos.getY() < heightLimit && condition.test(blockPos)) {
                blockPos = blockPos.up();
            }

            MutableBlockPos pos1 = new MutableBlockPos(blockPos);
            MutableBlockPos pos2 = new MutableBlockPos(blockPos);
            while (pos1.getY() < heightLimit && pos1.getY() - pos.getY() < aboveSolidAmount) {
                // test the above block
                pos2.move(EnumFacing.UP);
                if (condition.test(pos2)) {
                    // if the check succeeds then return the real we already have
                    // because we don't change it until AFTER the check fails
                    // instead of moving the position back down
                    break;
                }
                // only increment the real position if the check was not successful
                pos1.setPos(pos2);
            }

            return pos1.toImmutable();
        }
    }
}