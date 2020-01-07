package thedarkcolour.futuremc.entity.panda;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import thedarkcolour.futuremc.init.FBlocks;
import thedarkcolour.futuremc.init.FItems;
import thedarkcolour.futuremc.init.Sounds;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class EntityPanda extends EntityAnimal {
    private static final DataParameter<Integer> HUNGRY_TICKS = EntityDataManager.createKey(EntityPanda.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SNEEZE_TICKS = EntityDataManager.createKey(EntityPanda.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> EATING_TICKS = EntityDataManager.createKey(EntityPanda.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> MAIN_GENE = EntityDataManager.createKey(EntityPanda.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> HIDDEN_GENE = EntityDataManager.createKey(EntityPanda.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> PANDA_FLAGS = EntityDataManager.createKey(EntityPanda.class, DataSerializers.BYTE);
    private boolean revenge;
    private boolean attacked;
    public int field_213608_bz;
    private Vec3d field_213600_bJ;
    private float scaredAnimationProgress;
    private float lastScaredAnimationProgress;
    private float lazyProgress;
    private float lastLazyProgress;
    private float rollingProgress;
    private float lastRollingProgress;
    private static final Predicate<EntityItem> BREEDING_ITEM = (entityItem) -> {
        Item item = entityItem.getItem().getItem();
        return (item == FItems.INSTANCE.getBAMBOO() || item == Item.getItemFromBlock(Blocks.CAKE)) && entityItem.isEntityAlive() && !entityItem.cannotPickup();
    };

    public EntityPanda(World worldIn) {
        super(worldIn);
        setSize(1.25F, 1.25F);
        moveHelper = new EntityPanda.MoveHelperController(this);
    }

    public int getHungryTicks() {
        return dataManager.get(HUNGRY_TICKS);
    }

    public void setHungryTicks(int hungryTicks) {
        dataManager.set(HUNGRY_TICKS, hungryTicks);
    }

    public boolean isSneezing() {
        return getPandaFlag(2);
    }

    public boolean isSitting() {
        return getPandaFlag(8);
    }

    public void setIsScared(boolean flag) {
        setPandaFlag(8, flag);
    }

    public boolean isLazing() {
        return getPandaFlag(16);
    }

    public void func_213542_s(boolean p_213542_1_) {
        setPandaFlag(16, p_213542_1_);
    }

    public boolean isEating() {
        return dataManager.get(EATING_TICKS) > 0;
    }

    public void setIsEating(boolean p_213534_1_) {
        dataManager.set(EATING_TICKS, p_213534_1_ ? 1 : 0);
    }

    private int getEatingTicks() {
        return dataManager.get(EATING_TICKS);
    }

    private void setEatingTicks(int p_213571_1_) {
        dataManager.set(EATING_TICKS, p_213571_1_);
    }

    public void func_213581_u(boolean flag) {
        setPandaFlag(2, flag);
        if (!flag) {
            setSneezeTicks(0);
        }

    }

    public int getSneezeTicks() {
        return dataManager.get(SNEEZE_TICKS);
    }

    public void setSneezeTicks(int ticks) {
        dataManager.set(SNEEZE_TICKS, ticks);
    }

    public EntityPanda.Type getMainGene() {
        return EntityPanda.Type.fromID(dataManager.get(MAIN_GENE));
    }
    
    public void setMainGene(EntityPanda.Type type) {
        if (type.getID() > 6) {
            type = EntityPanda.Type.getRandomType(rand);
        }

        dataManager.set(MAIN_GENE, (byte)type.getID());
    }

    public EntityPanda.Type getHiddenGene() {
        return EntityPanda.Type.fromID(dataManager.get(HIDDEN_GENE));
    }

    public void setHiddenGene(EntityPanda.Type type) {
        if (type.getID() > 6) {
            type = EntityPanda.Type.getRandomType(rand);
        }

        dataManager.set(HIDDEN_GENE, (byte)type.getID());
    }

    public boolean isRolling() {
        return getPandaFlag(4);
    }

    public void setRolling(boolean isRolling) {
        setPandaFlag(4, isRolling);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(HUNGRY_TICKS, 0);
        dataManager.register(SNEEZE_TICKS, 0);
        dataManager.register(MAIN_GENE, (byte)0);
        dataManager.register(HIDDEN_GENE, (byte)0);
        dataManager.register(PANDA_FLAGS, (byte)0);
        dataManager.register(EATING_TICKS, 0);
    }

    private boolean getPandaFlag(int i) {
        return (dataManager.get(PANDA_FLAGS) & i) != 0;
    }

    private void setPandaFlag(int bit, boolean flag) {
        byte b = dataManager.get(PANDA_FLAGS);
        if (flag) {
            dataManager.set(PANDA_FLAGS, (byte)(b | bit));
        } else {
            dataManager.set(PANDA_FLAGS, (byte)(b & ~bit));
        }

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("MainGene", getMainGene().getName());
        compound.setString("HiddenGene", getHiddenGene().getName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setMainGene(EntityPanda.Type.fromName(compound.getString("MainGene")));
        setHiddenGene(EntityPanda.Type.fromName(compound.getString("HiddenGene")));
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        EntityPanda EntityPanda = new EntityPanda(world);
        if(ageable instanceof EntityPanda) {
            EntityPanda.func_213545_a(this, (EntityPanda)ageable);
        }

        EntityPanda.adjustTraits();
        return EntityPanda;
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new PanicGoal(this, 2.0D));
        tasks.addTask(2, new AIMate(this, 1.0D));
        tasks.addTask(3, new EntityAIAttack(this, 1.2F, true));
        tasks.addTask(4, new EntityAITempt(this, 1.0D, FItems.INSTANCE.getBAMBOO(), false));
        tasks.addTask(6, new EntityAIAvoid<>(this, EntityPlayer.class, 8.0F, 2.0D, 2.0D));
        tasks.addTask(6, new EntityAIAvoid<>(this, EntityMob.class, 4.0F, 2.0D, 2.0D));
        tasks.addTask(7, new EntityAISit(this));
        tasks.addTask(8, new EntityAILieDown(this));
        tasks.addTask(8, new EntityAIChildPlay(this));
        tasks.addTask(9, new EntityAIWatch(this, EntityPlayer.class, 6.0F));
        tasks.addTask(10, new EntityAILookIdle(this));
        tasks.addTask(12, new AIRoll(this));
        tasks.addTask(13, new EntityAIFollowParent(this, 1.25D));
        tasks.addTask(14, new EntityAIWanderAvoidWater(this, 1.0D));
        targetTasks.addTask(1, new AIRevenge(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    public EntityPanda.Type getPandaType() {
        return EntityPanda.Type.func_221101_b(getMainGene(), getHiddenGene());
    }
    
    public boolean isLazy() {
        return getPandaType() == EntityPanda.Type.LAZY;
    }

    public boolean isWorried() {
        return getPandaType() == EntityPanda.Type.WORRIED;
    }

    public boolean isPlayful() {
        return getPandaType() == EntityPanda.Type.PLAYFUL;
    }

    public boolean isWeak() {
        return getPandaType() == EntityPanda.Type.WEAK;
    }

    public boolean isAggressive() {
        return getPandaType() == EntityPanda.Type.AGGRESSIVE;
    }

    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        playSound(Sounds.INSTANCE.getPANDA_BITE(), 1.0F, 1.0F);
        if (!isAggressive()) {
            attacked = true;
        }
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());

        if(flag) {
            applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
        } catch (NullPointerException ignored) {
            //  😈 😈 😈
        }

        if (isWorried()) {
            if (world.isThundering() && !isInWater()) {
                setIsScared(true);
                setIsEating(false);
            } else if (!isEating()) {
                setIsScared(false);
            }
        }

        if (getAttackTarget() == null) {
            revenge = false;
            attacked = false;
        }

        if (getHungryTicks() > 0) {
            if (getAttackTarget() != null) {
                faceEntity(getAttackTarget(), 90.0F, 90.0F);
            }

            if (getHungryTicks() == 29 || getHungryTicks() == 14) {
                playSound(Sounds.INSTANCE.getPANDA_CANNOT_BREED(), 1.0F, 1.0F);
            }

            setHungryTicks(getHungryTicks() - 1);
        }

        if (isSneezing()) {
            setSneezeTicks(getSneezeTicks() + 1);
            if (getSneezeTicks() > 20) {
                func_213581_u(false);
                sneeze();
            } else if (getSneezeTicks() == 1) {
                playSound(Sounds.INSTANCE.getPANDA_PRE_SNEEZE(), 1.0F, 1.0F);
            }
        }

        if (isRolling()) {
            try {
                func_213535_ey();
            } catch (NoSuchMethodError ignored) {
                //  😈 😈 😈
            }
        } else {
            field_213608_bz = 0;
        }

        if (isSitting()) {
            rotationPitch = 0.0F;
        }

        updateScaredAnimation();
        updateEatingAnimation();
        updateLazyAnimation();
        updateRollingAnimation();
    }

    public boolean isFrightened() {
        return isWorried() && world.isThundering();
    }

    private void updateEatingAnimation() {
        if (!isEating() && isSitting() && !isFrightened() && !getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() && rand.nextInt(80) == 1) {
            setIsEating(true);
        } else if (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() || !isSitting()) {
            setIsEating(false);
        }

        if (isEating()) {
            playEatingAnimation();
            if (!world.isRemote && getEatingTicks() > 80 && rand.nextInt(20) == 1) {
                if (getEatingTicks() > 100 && func_213548_j(getItemStackFromSlot(EntityEquipmentSlot.MAINHAND))) {
                    if (!world.isRemote) {
                        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }

                    setIsScared(false);
                }

                setIsEating(false);
                return;
            }

            setEatingTicks(getEatingTicks() + 1);
        }

    }

    private void playEatingAnimation() {
        if (getEatingTicks() % 5 == 0) {
            playSound(Sounds.INSTANCE.getPANDA_EAT(), 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

            for(int i = 0; i < 6; ++i) {
                Vec3d vec3d = new Vec3d(((double)rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double)rand.nextFloat() - 0.5D) * 0.1D);
                vec3d = vec3d.rotatePitch(-rotationPitch * ((float)Math.PI / 180F));
                vec3d = vec3d.rotateYaw(-rotationYaw * ((float)Math.PI / 180F));
                double d0 = (double)(-rand.nextFloat()) * 0.6D - 0.3D;
                Vec3d vec3d1 = new Vec3d(((double)rand.nextFloat() - 0.5D) * 0.8D, d0, 1.0D + ((double)rand.nextFloat() - 0.5D) * 0.4D);
                vec3d1 = vec3d1.rotateYaw(-renderYawOffset * ((float)Math.PI / 180F));
                vec3d1 = vec3d1.add(posX, posY + (double)getEyeHeight() + 1.0D, posZ);
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem()));
            }
        }

    }

    private void updateScaredAnimation() {
        lastScaredAnimationProgress = scaredAnimationProgress;
        if (isSitting()) {
            scaredAnimationProgress = Math.min(1.0F, scaredAnimationProgress + 0.15F);
        } else {
            scaredAnimationProgress = Math.max(0.0F, scaredAnimationProgress - 0.19F);
        }

    }

    private void updateLazyAnimation() {
        lastLazyProgress = lazyProgress;
        if (isLazing()) {
            lazyProgress = Math.min(1.0F, lazyProgress + 0.15F);
        } else {
            lazyProgress = Math.max(0.0F, lazyProgress - 0.19F);
        }

    }

    private void updateRollingAnimation() {
        lastRollingProgress = rollingProgress;
        if (isRolling()) {
            rollingProgress = Math.min(1.0F, rollingProgress + 0.15F);
        } else {
            rollingProgress = Math.max(0.0F, rollingProgress - 0.19F);
        }

    }

    public float func_213561_v(float p_213561_1_) {
        return func_219799_g(p_213561_1_, lastScaredAnimationProgress, scaredAnimationProgress);
    }

    public float func_213583_w(float p_213583_1_) {
        return func_219799_g(p_213583_1_, lastLazyProgress, lazyProgress);
    }

    public float func_213591_x(float p_213591_1_) {
        return func_219799_g(p_213591_1_, lastRollingProgress, rollingProgress);
    }

    private void func_213535_ey() {
        ++field_213608_bz;
        if (field_213608_bz > 32) {
            setRolling(false);
        } else {
            if (!world.isRemote) {
                Vec3d vec3d = new Vec3d(motionX, motionY, motionZ);
                if (field_213608_bz == 1) {
                    float f = rotationYaw * ((float)Math.PI / 180F);
                    float f1 = isChild() ? 0.1F : 0.2F;
                    field_213600_bJ = new Vec3d(vec3d.x + (double)(-MathHelper.sin(f) * f1), 0.0D, vec3d.z + (double)(MathHelper.cos(f) * f1));
                    Vec3d vec = field_213600_bJ.add(0.0D, 0.27D, 0.0D);
                    setVelocity(vec.x, vec.y, vec.z);
                } else if ((float)field_213608_bz != 7.0F && (float)field_213608_bz != 15.0F && (float)field_213608_bz != 23.0F) {
                    setVelocity(field_213600_bJ.x, vec3d.y, field_213600_bJ.z);
                } else {
                    setVelocity(0.0D, onGround ? 0.27D : vec3d.y, 0.0D);
                }
            }

        }
    }

    private void sneeze() {
        playSound(Sounds.INSTANCE.getPANDA_SNEEZE(), 1.0F, 1.0F);

        for(EntityPanda entityPanda : world.getEntitiesWithinAABB(EntityPanda.class, getEntityBoundingBox().grow(10.0D))) {
            if (!entityPanda.isChild() && entityPanda.onGround && !entityPanda.isInWater() && entityPanda.isOccupied()) {
                entityPanda.jump();
            }
        }

        if (!world.isRemote && rand.nextInt(700) == 0 && world.getGameRules().getBoolean("doMobLoot")) {
            entityDropItem(new ItemStack(Items.SLIME_BALL), 0);
        }
    }

    @Override
    protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
        if (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() && BREEDING_ITEM.test(itemEntity)) {
            ItemStack itemstack = itemEntity.getItem();
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack);
            inventoryHandsDropChances[EntityEquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            onItemPickup(itemEntity, itemstack.getCount());
            itemEntity.setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        setIsScared(false);
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
        data = super.onInitialSpawn(difficulty, data);
        setMainGene(EntityPanda.Type.getRandomType(rand));
        setHiddenGene(EntityPanda.Type.getRandomType(rand));
        modifyAttributes();
        if (data instanceof EntityPanda.PandaData) {
            if (rand.nextInt(5) == 0) {
                setGrowingAge(-24000);
            }
        } else {
            data = new EntityPanda.PandaData();
        }

        return data;
    }

    private void func_213545_a(EntityPanda entityPanda, EntityPanda ageable) {
        if (ageable == null) {
            if (rand.nextBoolean()) {
                setMainGene(entityPanda.pickMainOrHiddenGene());
                setHiddenGene(EntityPanda.Type.getRandomType(rand));
            } else {
                setMainGene(EntityPanda.Type.getRandomType(rand));
                setHiddenGene(entityPanda.pickMainOrHiddenGene());
            }
        } else if (rand.nextBoolean()) {
            setMainGene(entityPanda.pickMainOrHiddenGene());
            setHiddenGene(ageable.pickMainOrHiddenGene());
        } else {
            setMainGene(ageable.pickMainOrHiddenGene());
            setHiddenGene(entityPanda.pickMainOrHiddenGene());
        }

        if (rand.nextInt(32) == 0) {
            setMainGene(EntityPanda.Type.getRandomType(rand));
        }

        if (rand.nextInt(32) == 0) {
            setHiddenGene(EntityPanda.Type.getRandomType(rand));
        }
    }

    private EntityPanda.Type pickMainOrHiddenGene() {
        return rand.nextBoolean() ? getMainGene() : getHiddenGene();
    }

    public void modifyAttributes() {
        if (isWeak()) {
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        }

        if (isLazy()) {
            getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.07F);
        }

    }

    private void func_213586_eB() {
        if (!isInWater()) {
            setMoveForward(0.0F);
            getNavigator().clearPath();
            setIsScared(true);
        }

    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() instanceof ItemMonsterPlacer) {
            return super.processInteract(player, hand);
        } else if (isFrightened()) {
            return false;
        } else if (isLazing()) {
            func_213542_s(false);
            return true;
        } else if (isBreedingItem(itemstack)) {
            if (getAttackTarget() != null) {
                revenge = true;
            }

            if (isChild()) {
                consumeItemFromStack(player, itemstack);
                ageUp((int)((float)(-getGrowingAge() / 20) * 0.1F), true);
            } else if (!world.isRemote && getGrowingAge() == 0 && canBreed()) {
                consumeItemFromStack(player, itemstack);
                setInLove(player);
            } else {
                if (world.isRemote || isSitting() || isInWater()) {
                    return false;
                }

                func_213586_eB();
                setIsEating(true);
                ItemStack stack = getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                if (!stack.isEmpty() && !player.isCreative()) {
                    entityDropItem(stack, 0);
                }

                setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(itemstack.getItem(), 1));
                consumeItemFromStack(player, itemstack);
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean canBreed() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (isAggressive()) {
            return Sounds.INSTANCE.getPANDA_AGGRESSIVE_AMBIENT();
        } else {
            return isWorried() ? Sounds.INSTANCE.getPANDA_WORRIED_AMBIENT() : Sounds.INSTANCE.getPANDA_AMBIENT();
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        playSound(Sounds.INSTANCE.getPANDA_STEP(), 0.15F, 1.0F);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == FItems.INSTANCE.getBAMBOO();
    }

    private boolean func_213548_j(ItemStack p_213548_1_) {
        return isBreedingItem(p_213548_1_) || p_213548_1_.getItem() == Item.getItemFromBlock(Blocks.CAKE);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Sounds.INSTANCE.getPANDA_DEATH();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return Sounds.INSTANCE.getPANDA_HURT();
    }

    public boolean isOccupied() {
        return !isLazing() && !isFrightened() && !isEating() && !isRolling() && !isSitting();
    }

    private static class EntityAIAttack extends EntityAIAttackMelee {
        private final EntityPanda entityPanda;

        private EntityAIAttack(EntityPanda entityPanda, double speedIn, boolean useLongMemory) {
            super(entityPanda, speedIn, useLongMemory);
            this.entityPanda = entityPanda;
        }

        public boolean shouldExecute() {
            return entityPanda.isOccupied() && super.shouldExecute();
        }
    }

    private static class EntityAIAvoid<T extends EntityLivingBase> extends EntityAIAvoidEntity<T> {
        private final EntityPanda field_220875_i;

        private EntityAIAvoid(EntityPanda entityPanda, Class<T> entityToAvoid, float avoidDistance, double farSpeedIn, double nearSpeedIn) {
            super(entityPanda, entityToAvoid, (Entity entity) -> {
                if(entity instanceof EntityPlayer) {
                    return !((EntityPlayer) entity).isSpectator();
                }
                return true;
            }, avoidDistance, farSpeedIn, nearSpeedIn);
            field_220875_i = entityPanda;
        }

        public boolean shouldExecute() {
            return field_220875_i.isWorried() && field_220875_i.isOccupied() && super.shouldExecute();
        }
    }

    private static class EntityAIChildPlay extends EntityAIBase {
        private final EntityPanda entityPanda;

        private EntityAIChildPlay(EntityPanda p_i51448_1_) {
            entityPanda = p_i51448_1_;
        }

        public boolean shouldExecute() {
            if (entityPanda.isChild() && entityPanda.isOccupied()) {
                if (entityPanda.isWeak() && entityPanda.rand.nextInt(500) == 1) {
                    return true;
                } else {
                    return entityPanda.rand.nextInt(6000) == 1;
                }
            } else {
                return false;
            }
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            entityPanda.func_213581_u(true);
        }
    }

    private static class EntityAILieDown extends EntityAIBase {
        private final EntityPanda field_220828_a;
        private int field_220829_b;

        private EntityAILieDown(EntityPanda p_i51460_1_) {
            field_220828_a = p_i51460_1_;
        }

        public boolean shouldExecute() {
            return field_220829_b < field_220828_a.ticksExisted && field_220828_a.isLazy() && field_220828_a.isOccupied() && field_220828_a.rand.nextInt(400) == 1;
        }

        public boolean shouldContinueExecuting() {
            if (!field_220828_a.isInWater() && (field_220828_a.isLazy() || field_220828_a.rand.nextInt(600) != 1)) {
                return field_220828_a.rand.nextInt(2000) != 1;
            } else {
                return false;
            }
        }

        public void startExecuting() {
            field_220828_a.func_213542_s(true);
            field_220829_b = 0;
        }

        public void resetTask() {
            field_220828_a.func_213542_s(false);
            field_220829_b = field_220828_a.ticksExisted + 200;
        }
    }

    private static class AIMate extends EntityAIMate {
        private final EntityPanda panda;
        private int field_220694_f;

        private AIMate(EntityPanda entityPanda, double speedIn) {
            super(entityPanda, speedIn);
            panda = entityPanda;
        }

        public boolean shouldExecute() {
            if (super.shouldExecute() && panda.getHungryTicks() == 0) {
                if (!inRangeOfBamboo()) {
                    if (field_220694_f <= panda.ticksExisted) {
                        panda.setHungryTicks(32);
                        field_220694_f = panda.ticksExisted + 600;

                        if (panda.isServerWorld()) {
                            EntityPlayer playerIn = panda.world.getClosestPlayer(panda.posX, panda.posY, panda.posZ, 8.0D, EntitySelectors.NOT_SPECTATING);
                            panda.setAttackTarget(playerIn);
                        }
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        private boolean inRangeOfBamboo() {
            BlockPos pos = new BlockPos(panda);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 8; ++j) {
                    for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            mutablePos.setPos(pos).add(k, i, l);
                            if (panda.world.getBlockState(mutablePos).getBlock() == FBlocks.INSTANCE.getBAMBOO()) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    private static class MoveHelperController extends EntityMoveHelper {
        private final EntityPanda panda;

        private MoveHelperController(EntityPanda panda) {
            super(panda);
            this.panda = panda;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (panda.isOccupied()) {
                super.onUpdateMoveHelper();
            }
        }
    }

    private static class PandaData implements IEntityLivingData { }

    static class PanicGoal extends EntityAIPanic {
        private final EntityPanda field_220740_f;

        PanicGoal(EntityPanda entityPanda, double speedIn) {
            super(entityPanda, speedIn);
            field_220740_f = entityPanda;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            if (!field_220740_f.isBurning()) {
                return false;
            } else {
                BlockPos blockpos = getRandomPos(field_220740_f.world, field_220740_f);
                if (blockpos != null) {
                    randPosX = blockpos.getX();
                    randPosY = blockpos.getY();
                    randPosZ = blockpos.getZ();
                    return true;
                } else {
                    return findRandomPosition();
                }
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (field_220740_f.isSitting()) {
                field_220740_f.getNavigator().clearPath();
                return false;
            } else {
                return super.shouldContinueExecuting();
            }
        }

        private BlockPos getRandomPos(World worldIn, EntityPanda entityPanda) {
            BlockPos pos = new BlockPos(entityPanda);
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = (float) (5 * 5 * 4 * 2);
            BlockPos pos1 = null;
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (int l = i - 5; l <= i + 5; ++l) {
                for (int i1 = j - 4; i1 <= j + 4; ++i1) {
                    for (int j1 = k - 5; j1 <= k + 5; ++j1) {
                        mutablePos.setPos(l, i1, j1);
                        IBlockState iblockstate = worldIn.getBlockState(mutablePos);

                        if (iblockstate.getMaterial() == Material.WATER) {
                            float f1 = (float) ((l - i) * (l - i) + (i1 - j) * (i1 - j) + (j1 - k) * (j1 - k));

                            if (f1 < f) {
                                f = f1;
                                pos1 = new BlockPos(mutablePos);
                            }
                        }
                    }
                }
            }

            return pos1;
        }
    }

    private static class AIRevenge extends EntityAIHurtByTarget {
        private final EntityPanda panda;

        private AIRevenge(EntityPanda entityPanda) {
            super(entityPanda, true);
            panda = entityPanda;
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (!panda.revenge && !panda.attacked) {
                return super.shouldContinueExecuting();
            } else {
                panda.setAttackTarget(null);
                return false;
            }
        }
    }

    private static class AIRoll extends EntityAIBase {
        private final EntityPanda panda;

        private AIRoll(EntityPanda panda) {
            this.panda = panda;
            setMutexBits(7);
        }

        public boolean shouldExecute() {
            if ((panda.isChild() || panda.isPlayful()) && panda.onGround) {
                if (!panda.isOccupied()) {
                    return false;
                } else {
                    float f = panda.rotationYaw * ((float)Math.PI / 180F);
                    int i = 0;
                    int j = 0;
                    float f1 = -MathHelper.sin(f);
                    float f2 = MathHelper.cos(f);
                    if ((double)Math.abs(f1) > 0.5D) {
                        i = (int)((float)i + f1 / Math.abs(f1));
                    }

                    if ((double)Math.abs(f2) > 0.5D) {
                        j = (int)((float)j + f2 / Math.abs(f2));
                    }

                    if (panda.world.getBlockState((new BlockPos(panda)).add(i, -1, j)).getBlock().getMaterial(null) == Material.AIR) {
                        return true;
                    } else if (panda.isPlayful() && panda.rand.nextInt(60) == 1) {
                        return true;
                    } else {
                        return panda.rand.nextInt(500) == 1;
                    }
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            panda.setRolling(true);
        }

        @Override
        public boolean isInterruptible() {
            return false;
        }
    }

    private static class EntityAISit extends EntityAIBase {
        private final EntityPanda panda;
        private int field_220832_b;

        private EntityAISit(EntityPanda panda) {
            setMutexBits(1);
            this.panda = panda;
        }

        @Override
        public boolean shouldExecute() {
            if (field_220832_b <= panda.ticksExisted && !panda.isChild() && !panda.isInWater() && panda.isOccupied() && panda.getHungryTicks() <= 0) {
                List<EntityItem> list = panda.world
                        .getEntitiesWithinAABB(EntityItem.class, panda.getEntityBoundingBox().grow(6.0D, 6.0D, 6.0D), EntityPanda.BREEDING_ITEM::test);
                return !list.isEmpty() || !panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty();
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (!panda.isInWater() && (panda.isLazy() || panda.rand.nextInt(600) != 1)) {
                return panda.rand.nextInt(2000) != 1;
            } else {
                return false;
            }
        }

        @Override
        public void updateTask() {
            if (!panda.isSitting() && !panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) {
                panda.func_213586_eB();
            }
        }

        @Override
        public void startExecuting() {
            List<EntityItem> list = panda.world.getEntitiesWithinAABB(EntityItem.class, panda.getEntityBoundingBox().grow(8.0D, 8.0D, 8.0D), EntityPanda.BREEDING_ITEM::test);
            if (!list.isEmpty() && panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) {
                panda.getNavigator().tryMoveToEntityLiving(list.get(0), 1.2F);
            } else if (!panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) {
                panda.func_213586_eB();
            }

            field_220832_b = 0;
        }

        @Override
        public void resetTask() {
            ItemStack itemstack = panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty()) {
                panda.entityDropItem(itemstack, 0.0F);
                panda.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                int i = panda.isLazy() ? panda.rand.nextInt(50) + 10 : panda.rand.nextInt(150) + 10;
                field_220832_b = panda.ticksExisted + i * 20;
            }

            panda.setIsScared(false);
        }
    }

    public void adjustTraits() {
        if (isWeak()) {
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        }

        if (isLazy()) {
            getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.07F);
        }
    }

    public enum Type {
        NORMAL(0, "normal", false),
        LAZY(1, "lazy", false),
        WORRIED(2, "worried", false),
        PLAYFUL(3, "playful", false),
        BROWN(4, "brown", true),
        WEAK(5, "weak", true),
        AGGRESSIVE(6, "aggressive", false);

        private static final EntityPanda.Type[] orderedTypes = Arrays.stream(values()).sorted(Comparator.comparingInt(EntityPanda.Type::getID)).toArray(Type[]::new);
        private final int id;
        private final String name;
        private final boolean isRare;

        Type(int id, String name, boolean p_i51468_5_) {
            this.id = id;
            this.name = name;
            this.isRare = p_i51468_5_;
        }

        public int getID() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isRare() {
            return isRare;
        }

        private static EntityPanda.Type func_221101_b(EntityPanda.Type main, EntityPanda.Type hidden) {
            if (main.isRare()) {
                return main == hidden ? main : NORMAL;
            } else {
                return main;
            }
        }

        public static EntityPanda.Type fromID(int id) {
            if (id < 0 || id >= orderedTypes.length) {
                id = 0;
            }

            return orderedTypes[id];
        }

        public static EntityPanda.Type fromName(String name) {
            for(EntityPanda.Type pandaType : values()) {
                if (pandaType.name.equals(name)) {
                    return pandaType;
                }
            }

            return NORMAL;
        }

        public static EntityPanda.Type getRandomType(Random random) {
            int i = random.nextInt(16);
            if (i == 0) {
                return LAZY;
            } else if (i == 1) {
                return WORRIED;
            } else if (i == 2) {
                return PLAYFUL;
            } else if (i == 4) {
                return AGGRESSIVE;
            } else if (i < 9) {
                return WEAK;
            } else {
                return i < 11 ? BROWN : NORMAL;
            }
        }
    }

    static class EntityAIWatch extends EntityAIWatchClosest {
        private final EntityPanda panda;

        EntityAIWatch(EntityPanda entityPanda, Class<? extends EntityLivingBase> targetEntity, float maxDistance) {
            super(entityPanda, targetEntity, maxDistance);
            panda = entityPanda;
        }

        public boolean shouldExecute() {
            return panda.isOccupied() && super.shouldExecute();
        }
    }

    public static float func_219799_g(float p_219799_0_, float p_219799_1_, float p_219799_2_) {
        return p_219799_1_ + p_219799_0_ * (p_219799_2_ - p_219799_1_);
    }
}