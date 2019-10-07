package thedarkcolour.futuremc.entity.bee;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.futuremc.block.BlockBerryBush;
import thedarkcolour.futuremc.compat.crafttweaker.Bee;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.sound.Sounds;
import thedarkcolour.futuremc.tile.TileBeeHive;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class EntityBee extends EntityAnimal implements EntityFlying {
    private static final Field LOOK_HELPER_FIELD = ReflectionHelper.findField(EntityLiving.class, "lookHelper", "field_70749_g");
    private static final Method GET_AGE_PROPERTY = ReflectionHelper.findMethod(BlockCrops.class, "getAgeProperty", "func_185524_e");
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
        flowerPos = BlockPos.ORIGIN;
        hivePos = BlockPos.ORIGIN;
        moveHelper = new EntityBee.FlyHelper(this);

        setSize(0.7F, 0.7F);

        try {
            LOOK_HELPER_FIELD.set(this, new LookHelper(this));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        setPathPriority(PathNodeType.WATER, -1.0F);
    }

    // Keep method outside of CraftTweaker class to prevent Bee.class initialization (which crashes without CraftTweaker)
    public static boolean isFlowerValid(IBlockState state) {
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);

        if(block == Blocks.AIR || meta > 15)  {
            return false;
        }

        return Loader.isModLoaded("crafttweaker") ? Bee.FLOWERS.containsEquivalent(CraftTweakerMC.getBlock(block, meta)) :
                (block instanceof BlockFlower || block instanceof thedarkcolour.futuremc.block.BlockFlower);
    }

    public static boolean canGrowBlock(Block block) {
        return block == Blocks.WHEAT || block == Blocks.CARROTS || block == Blocks.POTATOES || block == Blocks.BEETROOTS || block == Blocks.MELON_STEM || block == Blocks.PUMPKIN_STEM || block == Init.SWEET_BERRY_BUSH;
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
                    vec3d = getPositionVector().add(0.0D, f, 0.0D);
                }

                getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.4000000059604645D);
            }

            setNearTarget(isAngry() && !hasStung() && getAttackTarget() != null && getAttackTarget().getDistanceSq(this) < 4.0D);

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

    public BlockPos getHivePos() {
        return hivePos;
    }

    private static class AIEnterHive extends AINotAngry {
        private AIEnterHive(EntityBee entityBee) {
            super(entityBee);
        }

        @Override
        public boolean canBeeStart() {
            if (bee.hasNectar() && bee.hasHive() && !bee.hasStung() && bee.canEnterHive()) {
                if (bee.hivePos.distanceSq(bee.getPosition()) < 4.0D) {
                    TileEntity te = bee.world.getTileEntity(bee.hivePos);
                    if (te instanceof TileBeeHive) {
                        TileBeeHive hive = (TileBeeHive)te;
                        if (!hive.isFullOfBees()) {
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
                TileBeeHive hive = (TileBeeHive) tile;
                hive.tryEnterHive(bee, bee.hasNectar());
            }
        }
    }

    private static class AISting extends EntityAIAttackMelee {
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

    private static class AIGrowCrops extends AINotAngry {
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
                for(int i = 1; i <= 2; ++i) {
                    BlockPos pos = bee.getPosition().down(i);
                    IBlockState state = bee.world.getBlockState(pos);
                    Block block = state.getBlock();
                    boolean canGrow = false;
                    PropertyInteger ageProperty = null;
                    if (canGrowBlock(block)) {
                        if (block instanceof BlockCrops) {
                            BlockCrops crops = (BlockCrops)block;
                            if (!crops.isMaxAge(state)) {
                                canGrow = true;

                                if (block instanceof BlockBeetroot) {
                                    ageProperty = BlockBeetroot.BEETROOT_AGE;
                                } else {
                                    try {
                                        ageProperty = (PropertyInteger) GET_AGE_PROPERTY.invoke(crops);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                            }
                        } else {
                            int age;
                            if (block instanceof BlockStem) {
                                age = state.getValue(BlockStem.AGE);
                                if (age < 7) {
                                    canGrow = true;
                                    ageProperty = BlockStem.AGE;
                                }
                            } else if (block == Init.SWEET_BERRY_BUSH) {
                                age = state.getValue(BlockBerryBush.AGE);
                                if (age < 3) {
                                    canGrow = true;
                                    ageProperty = BlockBerryBush.AGE;
                                }
                            }
                        }

                        if (ageProperty != null && canGrow) {
                            bee.world.playEvent(2005, pos, 0);
                            bee.world.setBlockState(pos, state.withProperty(ageProperty, state.getValue(ageProperty) + 1));
                            bee.addCropCounter();
                        }
                    }
                }

            }
        }
    }

    private static class AIFindHive extends AINotAngry {
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
            Optional<BlockPos> optionalPos = bee.getBlockInRange(pos -> bee.world.getTileEntity(pos) instanceof TileBeeHive && !((TileBeeHive) bee.world.getTileEntity(pos)).isFullOfBees(), 5);
            if (optionalPos.isPresent()) {
                BlockPos pos = optionalPos.get();
                TileEntity tile = bee.world.getTileEntity(pos);
                if (tile instanceof TileBeeHive && !((TileBeeHive)tile).isFullOfBees()) {
                    bee.hivePos = pos;
                }
            }
        }
    }

    private static class AIPollinate extends AINotAngry {
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
        public void updateTask() {
            ++pollinationTicks;
            if (bee.rand.nextFloat() < 0.05F && pollinationTicks > lastPollinationTicks + 60) {
                lastPollinationTicks = pollinationTicks;
                bee.playSound(Sounds.BEE_POLLINATE, 1.0F, 1.0F);
            }
        }
        private Optional<BlockPos> getFlower() {
            return bee.getBlockInRange(pos -> isFlowerValid(bee.world.getBlockState(pos)), 2);
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

    private static class AIMoveToFlower extends AIMoveToBlock {
        private AIMoveToFlower(EntityBee bee) {
            super(bee, 3);
        }

        @Override
        public boolean canBeeStart() {
            return isHiveValid() && bee.ticksSincePollination > 3600;
        }

        @Override
        public boolean canBeeContinue() {
            return this.canBeeStart() && super.canBeeContinue();
        }

        @Override
        public void resetTask() {
            if (!isFlowerValid(bee.world.getBlockState(bee.flowerPos))) {
                bee.flowerPos = BlockPos.ORIGIN;
            }
        }

        @Override
        protected BlockPos getTargetPos() {
            return bee.flowerPos;
        }

        private boolean isHiveValid() {
            return getTargetPos() != BlockPos.ORIGIN;
        }
    }

    private static class AIMoveToHive extends AIMoveToBlock {
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

    private static class AIWander extends EntityAIBase {
        private final EntityBee bee;

        private AIWander(EntityBee bee) {
            setMutexBits(0x1);
            this.bee = bee;
        }

        @Override
        public boolean shouldExecute() {
            return (bee.navigator.noPath() && bee.rand.nextInt(10) == 0) || !bee.hasHive();
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

    private static class AIFollowTarget extends EntityAINearestAttackableTarget<EntityPlayer> {
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

    private static class AITempt extends EntityAIBase {
        private final EntityBee bee;
        protected EntityPlayer closestPlayer;
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

        protected boolean isTempting(ItemStack stack) {
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

    private static class FlyHelper extends EntityMoveHelper {
        private FlyHelper(EntityBee entityBee) {
            super(entityBee);
        }

        @Override
        public void onUpdateMoveHelper() {
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
                entity.rotationYaw = limitAngle(entity.rotationYaw, f0, 90.0F);
                float f1;

                if (entity.onGround) {
                    f1 = (float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                } else {
                    f1 = (float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue());
                }

                entity.setAIMoveSpeed(f1);
                double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                float f2 = (float)(-(MathHelper.atan2(d1, d4) * 57.2957763671875D));
                entity.rotationPitch = limitAngle(entity.rotationPitch, f2, 20);
                entity.setMoveVertical(d1 > 0.0D ? f1 : -f1);
            } else {
                entity.setMoveVertical(0.0F);
                entity.setMoveForward(0.0F);
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

    public static Vec3d findVector(EntityCreature entityIn, int var1, int var2, Vec3d vec3d) {
        Vec3d vec = vec3d.subtract(entityIn.getPositionVector());
        return findVector(entityIn, var1, var2, vec, true, 1.5707963705062866D, entityIn::getBlockPathWeight);
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
                return new BlockPos(d3, int_7, d4);
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
    0x1 = MOVE
    0x2 = LOOK
    0x4 = JUMP
    0x8 = TARGET
 */