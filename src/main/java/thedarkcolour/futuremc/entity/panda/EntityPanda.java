package thedarkcolour.futuremc.entity.panda;

import com.google.common.base.Predicates;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import thedarkcolour.core.util.UtilKt;
import thedarkcolour.futuremc.registry.FBlocks;
import thedarkcolour.futuremc.registry.FItems;
import thedarkcolour.futuremc.registry.FParticles;
import thedarkcolour.futuremc.registry.FSounds;

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
    public int rolls;
    private Vec3d rollVelocity;
    private float scaredAnimation;
    private float lastScaredAnimation;
    private float lazyAnimation;
    private float lastLazyAnimation;
    private float rollingAnimation;
    private float lastRollingAnimation;
    private EntityAIWatch lookTask;
    private static final Predicate<EntityItem> BREEDING_ITEM = (entityItem) -> {
        Item item = entityItem.getItem().getItem();
        return (item == FItems.INSTANCE.getBAMBOO() || item == Item.getItemFromBlock(Blocks.CAKE)) && entityItem.isEntityAlive() && !entityItem.cannotPickup();
    };

    public EntityPanda(World worldIn) {
        super(worldIn);
        setSize(1.25f, 1.25f);
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

    public void setSneezing(boolean flag) {
        setPandaFlag(2, flag);

        if (!flag) {
            setSneezeTicks(0);
        }
    }

    public boolean isSitting() {
        return getPandaFlag(8);
    }

    public void setSitting(boolean sitting) {
        setPandaFlag(8, sitting);
    }

    public boolean isLazing() {
        return getPandaFlag(16);
    }

    public void setIsLazing(boolean lazing) {
        setPandaFlag(16, lazing);
    }

    public boolean isEating() {
        return dataManager.get(EATING_TICKS) > 0;
    }

    public void setIsEating(boolean isEating) {
        dataManager.set(EATING_TICKS, isEating ? 1 : 0);
    }

    private int getEatingTicks() {
        return dataManager.get(EATING_TICKS);
    }

    private void setEatingTicks(int eatingTicks) {
        dataManager.set(EATING_TICKS, eatingTicks);
    }

    public int getSneezeTicks() {
        return dataManager.get(SNEEZE_TICKS);
    }

    public void setSneezeTicks(int ticks) {
        dataManager.set(SNEEZE_TICKS, ticks);
    }

    public PandaType getMainGene() {
        return PandaType.byId(dataManager.get(MAIN_GENE));
    }

    public void setMainGene(PandaType type) {
        if (type.getID() > 6) {
            type = PandaType.pickRandomType(rand);
        }

        dataManager.set(MAIN_GENE, (byte) type.getID());
    }

    public PandaType getHiddenGene() {
        return PandaType.byId(dataManager.get(HIDDEN_GENE));
    }

    public void setHiddenGene(PandaType type) {
        if (type.getID() > 6) {
            type = PandaType.pickRandomType(rand);
        }

        dataManager.set(HIDDEN_GENE, (byte) type.getID());
    }

    public void setGenes(PandaType main, PandaType hidden) {
        setMainGene(main);
        setHiddenGene(hidden);
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
        dataManager.register(MAIN_GENE, (byte) 0);
        dataManager.register(HIDDEN_GENE, (byte) 0);
        dataManager.register(PANDA_FLAGS, (byte) 0);
        dataManager.register(EATING_TICKS, 0);
    }

    private boolean getPandaFlag(int i) {
        return (dataManager.get(PANDA_FLAGS) & i) != 0;
    }

    private void setPandaFlag(int bit, boolean flag) {
        byte b = dataManager.get(PANDA_FLAGS);
        if (flag) {
            dataManager.set(PANDA_FLAGS, (byte) (b | bit));
        } else {
            dataManager.set(PANDA_FLAGS, (byte) (b & ~bit));
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
        setMainGene(PandaType.byName(compound.getString("MainGene")));
        setHiddenGene(PandaType.byName(compound.getString("HiddenGene")));
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        EntityPanda panda = new EntityPanda(world);

        if (ageable instanceof EntityPanda) {
            panda.chooseGenes(this, (EntityPanda) ageable);
        }

        panda.adjustTraits();

        return panda;
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new PanicGoal(this, 2.0));
        tasks.addTask(2, new AIMate(this, 1.0));
        tasks.addTask(3, new AIAttack(this, 1.2f, true));
        tasks.addTask(4, new EntityAITempt(this, 1.0, FItems.INSTANCE.getBAMBOO(), false));
        tasks.addTask(6, new AIAvoid<>(this, EntityPlayer.class, 8.0f, 2.0, 2.0));
        tasks.addTask(6, new AIAvoid<>(this, EntityMob.class, 4.0f, 2.0, 2.0));
        tasks.addTask(7, new EntityAISit(this));
        tasks.addTask(8, new EntityAILieDown(this));
        tasks.addTask(8, new EntityAIChildPlay(this));
        tasks.addTask(9, lookTask = new EntityAIWatch(this, EntityPlayer.class, 6.0f));
        tasks.addTask(10, new EntityAILookIdle(this));
        tasks.addTask(12, new AIRoll(this));
        tasks.addTask(13, new EntityAIFollowParent(this, 1.25D));
        tasks.addTask(14, new EntityAIWanderAvoidWater(this, 1.0));
        targetTasks.addTask(1, new AIRevenge(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
    }

    public PandaType getPandaType() {
        return PandaType.pickType(getMainGene(), getHiddenGene());
    }

    public boolean isLazy() {
        return getPandaType() == PandaType.LAZY;
    }

    public boolean isWorried() {
        return getPandaType() == PandaType.WORRIED;
    }

    public boolean isPlayful() {
        return getPandaType() == PandaType.PLAYFUL;
    }

    public boolean isWeak() {
        return getPandaType() == PandaType.WEAK;
    }

    public boolean isAggressive() {
        return getPandaType() == PandaType.AGGRESSIVE;
    }

    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        playSound(FSounds.PANDA_BITE, 1.0f, 1.0f);
        if (!isAggressive()) {
            attacked = true;
        }
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());

        if (flag) {
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
                setSitting(true);
                setIsEating(false);
            } else if (!isEating()) {
                setSitting(false);
            }
        }

        if (getAttackTarget() == null) {
            revenge = false;
            attacked = false;
        }

        if (getHungryTicks() > 0) {
            if (getAttackTarget() != null) {
                faceEntity(getAttackTarget(), 90.0f, 90.0f);
            }

            if (getHungryTicks() == 29 || getHungryTicks() == 14) {
                playSound(FSounds.PANDA_CANNOT_BREED, 1.0f, 1.0f);
            }

            setHungryTicks(getHungryTicks() - 1);
        }

        if (isSneezing()) {
            setSneezeTicks(getSneezeTicks() + 1);
            if (getSneezeTicks() > 20) {
                setSneezing(false);
                sneeze();
            } else if (getSneezeTicks() == 1) {
                playSound(FSounds.INSTANCE.getPANDA_PRE_SNEEZE(), 1.0f, 1.0f);
            }
        }

        if (isRolling()) {
            try {
                roll();
            } catch (NoSuchMethodError ignored) {
                //  😈 😈 😈
            }
        } else {
            rolls = 0;
        }

        if (isSitting()) {
            rotationPitch = 0.0f;
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
                if (getEatingTicks() > 100 && isEdiblePanda(getItemStackFromSlot(EntityEquipmentSlot.MAINHAND))) {
                    if (!world.isRemote) {
                        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }

                    setSitting(false);
                }

                setIsEating(false);
            } else {
                setEatingTicks(getEatingTicks() + 1);
            }
        }
    }

    private void playEatingAnimation() {
        if (getEatingTicks() % 5 == 0) {
            playSound(FSounds.PANDA_EAT, 0.5f + 0.5f * (float) rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);

            for (int i = 0; i < 6; ++i) {
                Vec3d vec3d = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) rand.nextFloat() - 0.5D) * 0.1D);
                vec3d = vec3d.rotatePitch(-rotationPitch * ((float) Math.PI / 180f));
                vec3d = vec3d.rotateYaw(-rotationYaw * ((float) Math.PI / 180f));
                double d0 = (double) (-rand.nextFloat()) * 0.6D - 0.3D;
                Vec3d vec3d1 = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.8D, d0, 1.0 + ((double) rand.nextFloat() - 0.5D) * 0.4D);
                vec3d1 = vec3d1.rotateYaw(-renderYawOffset * ((float) Math.PI / 180f));
                vec3d1 = vec3d1.add(posX, posY + (double) getEyeHeight() + 1.0, posZ);
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem()));
            }
        }

    }

    private void updateScaredAnimation() {
        lastScaredAnimation = scaredAnimation;

        if (isSitting()) {
            scaredAnimation = Math.min(1.0f, scaredAnimation + 0.15f);
        } else {
            scaredAnimation = Math.max(0.0f, scaredAnimation - 0.19F);
        }

    }

    private void updateLazyAnimation() {
        lastLazyAnimation = lazyAnimation;

        if (isLazing()) {
            lazyAnimation = Math.min(1.0f, lazyAnimation + 0.15f);
        } else {
            lazyAnimation = Math.max(0.0f, lazyAnimation - 0.19F);
        }

    }

    private void updateRollingAnimation() {
        lastRollingAnimation = rollingAnimation;

        if (isRolling()) {
            rollingAnimation = Math.min(1.0f, rollingAnimation + 0.15f);
        } else {
            rollingAnimation = Math.max(0.0f, rollingAnimation - 0.19F);
        }

    }

    public float getScaredAnimation(float partialTicks) {
        return UtilKt.lerp(partialTicks, lastScaredAnimation, scaredAnimation);
    }

    public float getLazyAnimation(float partialTicks) {
        return UtilKt.lerp(partialTicks, lastLazyAnimation, lazyAnimation);
    }

    public float getRollingAnimation(float partialTicks) {
        return UtilKt.lerp(partialTicks, lastRollingAnimation, rollingAnimation);
    }

    private void roll() {
        ++rolls;
        
        if (rolls > 32) {
            setRolling(false);
        } else {
            if (!world.isRemote) {
                if (rolls == 1) {
                    float degreesYaw = rotationYaw * ((float) Math.PI / 180f);
                    float motionMod = isChild() ? 0.1f : 0.2f;
                    rollVelocity = new Vec3d(motionX + - MathHelper.sin(degreesYaw) * motionMod, 0.0, motionZ + MathHelper.cos(degreesYaw) * motionMod);

                    motionX += -MathHelper.sin(degreesYaw) * motionMod;
                    motionY = 0.27;
                    motionZ += MathHelper.cos(degreesYaw) * motionMod;
                } else if (rolls != 7 && rolls != 15 && rolls != 23) {
                    motionX = rollVelocity.x;
                    motionZ = rollVelocity.z;
                } else {
                    if (onGround) {
                        motionY = 0.27;
                    }
                }
            }

        }
    }

    private void sneeze() {
        world.spawnParticle(FParticles.PANDA_SNEEZE,
                posX - (width + 1.0f) * 0.5 * MathHelper.sin((float) (this.rotationYaw * (Math.PI / 180f))),
                getEyeHeight() - 0.1f,
                posZ - (width + 1.0f) * 0.5 * MathHelper.sin((float) (this.rotationYaw * (Math.PI / 180f))),
                motionX, 0, motionZ
        );
        playSound(FSounds.PANDA_SNEEZE, 1.0f, 1.0f);

        for (EntityPanda entityPanda : world.getEntitiesWithinAABB(EntityPanda.class, getEntityBoundingBox().grow(10.0))) {
            if (!entityPanda.isChild() && entityPanda.onGround && !entityPanda.isInWater() && entityPanda.isNotBusy()) {
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
            inventoryHandsDropChances[EntityEquipmentSlot.MAINHAND.getIndex()] = 2.0f;
            onItemPickup(itemEntity, itemstack.getCount());
            itemEntity.setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        setSitting(false);
        return super.attackEntityFrom(source, amount);
    }

    // Passes on data throughout a pack of pandas
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
        data = super.onInitialSpawn(difficulty, data);

        setGenes(PandaType.pickRandomType(rand), PandaType.pickRandomType(rand));
        adjustTraits();

        if (data instanceof EntityPanda.PandaData) {
            if (rand.nextInt(5) == 0) {
                setGrowingAge(-24000);
            }
        } else {
            data = new EntityPanda.PandaData();
        }

        return data;
    }

    private void chooseGenes(EntityPanda parent1, EntityPanda parent2) {
        if (parent2 == null) {
            if (rand.nextBoolean()) {
                setGenes(parent1.pickRandomGene(), PandaType.pickRandomType(rand));
            } else {
                setGenes(PandaType.pickRandomType(rand), parent1.pickRandomGene());
            }
        } else if (rand.nextBoolean()) {
            setGenes(parent1.pickRandomGene(), parent2.pickRandomGene());
        } else {
            setGenes(parent2.pickRandomGene(), parent1.pickRandomGene());
        }

        if (rand.nextInt(32) == 0) {
            setMainGene(PandaType.pickRandomType(rand));
        }

        if (rand.nextInt(32) == 0) {
            setHiddenGene(PandaType.pickRandomType(rand));
        }
    }

    private PandaType pickRandomGene() {
        return rand.nextBoolean() ? getMainGene() : getHiddenGene();
    }

    private void sit() {
        if (!isInWater()) {
            setMoveForward(0.0f);
            getNavigator().clearPath();
            setSitting(true);
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
            setIsLazing(false);
            return true;
        } else if (isBreedingItem(itemstack)) {
            if (getAttackTarget() != null) {
                revenge = true;
            }

            if (isChild()) {
                consumeItemFromStack(player, itemstack);
                ageUp((int) ((float) (-getGrowingAge() / 20) * 0.1f), true);
            } else if (!world.isRemote && getGrowingAge() == 0 && canFallInLove()) {
                consumeItemFromStack(player, itemstack);
                setInLove(player);
            } else {
                if (world.isRemote || isSitting() || isInWater()) {
                    return false;
                }

                sit();
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

    private boolean canFallInLove() {
        return inLove <= 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (isAggressive()) {
            return FSounds.PANDA_AGGRESSIVE_AMBIENT;
        } else {
            return isWorried() ? FSounds.PANDA_WORRIED_AMBIENT : FSounds.PANDA_AMBIENT;
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        playSound(FSounds.PANDA_STEP, 0.15f, 1.0f);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == FItems.INSTANCE.getBAMBOO();
    }

    private boolean isEdiblePanda(ItemStack stack) {
        return isBreedingItem(stack) || stack.getItem() == Item.getItemFromBlock(Blocks.CAKE);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return FSounds.PANDA_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return FSounds.PANDA_HURT;
    }

    public boolean isNotBusy() {
        return !isLazing() && !isFrightened() && !isEating() && !isRolling() && !isSitting();
    }

    private static class AIAttack extends EntityAIAttackMelee {
        private final EntityPanda panda;

        private AIAttack(EntityPanda panda, double speedIn, boolean useLongMemory) {
            super(panda, speedIn, useLongMemory);
            this.panda = panda;
        }

        public boolean shouldExecute() {
            return panda.isNotBusy() && super.shouldExecute();
        }
    }

    private static class AIAvoid<T extends EntityLivingBase> extends EntityAIAvoidEntity<T> {
        private final EntityPanda panda;

        private AIAvoid(EntityPanda panda, Class<T> entityToAvoid, float avoidDistance, double farSpeedIn, double nearSpeedIn) {
            super(panda, entityToAvoid, (Entity entity) -> {
                if (entity instanceof EntityPlayer) {
                    return !((EntityPlayer) entity).isSpectator();
                }
                return true;
            }, avoidDistance, farSpeedIn, nearSpeedIn);
            this.panda = panda;
        }

        public boolean shouldExecute() {
            return panda.isWorried() && panda.isNotBusy() && super.shouldExecute();
        }
    }

    private static class EntityAIChildPlay extends EntityAIBase {
        private final EntityPanda panda;

        private EntityAIChildPlay(EntityPanda panda) {
            this.panda = panda;
        }

        public boolean shouldExecute() {
            if (panda.isChild() && panda.isNotBusy()) {
                if (panda.isWeak() && panda.rand.nextInt(500) == 1) {
                    return true;
                } else {
                    return panda.rand.nextInt(6000) == 1;
                }
            } else {
                return false;
            }
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            panda.setSneezing(true);
        }
    }

    private static class EntityAILieDown extends EntityAIBase {
        private final EntityPanda panda;
        private int cooldownTime;

        private EntityAILieDown(EntityPanda panda) {
            this.panda = panda;
        }

        public boolean shouldExecute() {
            return cooldownTime < panda.ticksExisted && panda.isLazy() && panda.isNotBusy() && panda.rand.nextInt(400) == 1;
        }

        public boolean shouldContinueExecuting() {
            if (!panda.isInWater() && (panda.isLazy() || panda.rand.nextInt(600) != 1)) {
                return panda.rand.nextInt(2000) != 1;
            } else {
                return false;
            }
        }

        public void startExecuting() {
            panda.setIsLazing(true);
            cooldownTime = 0;
        }

        public void resetTask() {
            panda.setIsLazing(false);
            cooldownTime = panda.ticksExisted + 200;
        }
    }

    private static class AIMate extends EntityAIMate {
        private final EntityPanda panda;
        private int cooldownTime;

        private AIMate(EntityPanda entityPanda, double speedIn) {
            super(entityPanda, speedIn);
            panda = entityPanda;
        }

        public boolean shouldExecute() {
            if (super.shouldExecute() && panda.getHungryTicks() == 0) {
                if (!inRangeOfBamboo()) {
                    if (cooldownTime <= panda.ticksExisted) {
                        panda.setHungryTicks(32);
                        cooldownTime = panda.ticksExisted + 600;

                        if (panda.isServerWorld()) {
                            EntityPlayer playerIn = panda.world.getClosestPlayer(panda.posX, panda.posY, panda.posZ, 8.0, EntitySelectors.NOT_SPECTATING);
                            panda.lookTask.setTarget(playerIn);
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

            for (int y = 0; y < 3; ++y) {
                for (int j = 0; j < 8; ++j) {
                    for (int x = 0; x <= j; x = x > 0 ? -x : 1 - x) {
                        for (int z = x < j && x > -j ? j : 0; z <= j; z = z > 0 ? -z : 1 - z) {
                            mutablePos.setPos(pos).add(x, y, z);
                            if (panda.world.getBlockState(mutablePos).getBlock() == FBlocks.BAMBOO) {
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
            if (panda.isNotBusy()) {
                super.onUpdateMoveHelper();
            }
        }
    }

    private static class PandaData implements IEntityLivingData {}

    static class PanicGoal extends EntityAIPanic {
        private final EntityPanda panda;

        PanicGoal(EntityPanda entityPanda, double speedIn) {
            super(entityPanda, speedIn);
            panda = entityPanda;
        }

        public boolean shouldExecute() {
            if (!panda.isBurning()) {
                return false;
            } else {
                BlockPos blockpos = getRandPos(panda.world, panda, 5, 4);
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
            if (panda.isSitting()) {
                panda.getNavigator().clearPath();
                return false;
            } else {
                return super.shouldContinueExecuting();
            }
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
                if (!panda.isNotBusy()) {
                    return false;
                } else {
                    float f = panda.rotationYaw * ((float) Math.PI / 180f);
                    int i = 0;
                    int j = 0;
                    float f1 = -MathHelper.sin(f);
                    float f2 = MathHelper.cos(f);
                    if ((double) Math.abs(f1) > 0.5D) {
                        i = (int) ((float) i + f1 / Math.abs(f1));
                    }

                    if ((double) Math.abs(f2) > 0.5D) {
                        j = (int) ((float) j + f2 / Math.abs(f2));
                    }

                    if (panda.world.getBlockState(new BlockPos(panda.posX + i, panda.posY - 1, panda.posZ + j)).getMaterial() == Material.AIR) {
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
        private int cooldownTime;

        private EntityAISit(EntityPanda panda) {
            setMutexBits(1);
            this.panda = panda;
        }

        @Override
        public boolean shouldExecute() {
            if (cooldownTime <= panda.ticksExisted && !panda.isChild() && !panda.isInWater() && panda.isNotBusy() && panda.getHungryTicks() <= 0) {
                List<EntityItem> list = panda.world.getEntitiesWithinAABB(EntityItem.class, panda.getEntityBoundingBox().grow(6.0, 6.0, 6.0), EntityPanda.BREEDING_ITEM::test);
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
                panda.sit();
            }
        }

        @Override
        public void startExecuting() {
            List<EntityItem> list = panda.world.getEntitiesWithinAABB(EntityItem.class, panda.getEntityBoundingBox().grow(8.0, 8.0, 8.0), EntityPanda.BREEDING_ITEM::test);
            if (!list.isEmpty() && panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) {
                panda.getNavigator().tryMoveToEntityLiving(list.get(0), 1.2f);
            } else if (!panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) {
                panda.sit();
            }

            cooldownTime = 0;
        }

        @Override
        public void resetTask() {
            ItemStack itemstack = panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty()) {
                panda.entityDropItem(itemstack, 0.0f);
                panda.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                int i = panda.isLazy() ? panda.rand.nextInt(50) + 10 : panda.rand.nextInt(150) + 10;
                cooldownTime = panda.ticksExisted + i * 20;
            }

            panda.setSitting(false);
        }
    }

    private static class EntityAIWatch extends EntityAIWatchClosest {
        private final EntityPanda panda;

        public EntityAIWatch(EntityPanda panda, Class<? extends EntityLivingBase> targetEntity, float maxDistance) {
            super(panda, targetEntity, maxDistance);
            this.panda = panda;
        }

        public void setTarget(EntityLivingBase entity) {
            this.closestEntity = entity;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return closestEntity != null && super.shouldContinueExecuting();
        }

        public boolean shouldExecute() {
            if (panda.rand.nextFloat() >= chance) {
                return false;
            } else {
                if (closestEntity == null) {
                    if (watchedClass == EntityPlayer.class) {
                        this.closestEntity = this.entity.world.getClosestPlayer(this.entity.posX, this.entity.posY, this.entity.posZ, this.maxDistance, Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.notRiding(this.entity)));
                    } else {
                        this.closestEntity = this.entity.world.findNearestEntityWithinAABB(this.watchedClass, this.entity.getEntityBoundingBox().grow(this.maxDistance, 3.0, this.maxDistance), this.entity);
                    }
                }

                return panda.isNotBusy() && closestEntity != null;
            }
        }

        @Override
        public void updateTask() {
            if (closestEntity != null) {
                super.updateTask();
            }
        }
    }

    public void adjustTraits() {
        if (isWeak()) {
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
        }

        if (isLazy()) {
            getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.07F);
        }
    }

    public static class PandaType {
        public static final Int2ObjectMap<PandaType> VALUES = new Int2ObjectOpenHashMap<>();
        
        // Vanilla types
        public static final PandaType NORMAL = new PandaType(0, "normal", false);
        public static final PandaType LAZY = new PandaType(1, "lazy", false);
        public static final PandaType WORRIED = new PandaType(2, "worried", false);
        public static final PandaType PLAYFUL = new PandaType(3, "playful", false);
        public static final PandaType BROWN = new PandaType(4, "brown", true);
        public static final PandaType WEAK = new PandaType(5, "weak", true);
        public static final PandaType AGGRESSIVE = new PandaType(6, "aggressive", false);
        
        private final int id;
        private final String name;
        private final boolean rare;

        public PandaType(int id, String name, boolean rare) {
            this.id = id;
            this.name = name;
            this.rare = rare;
            
            VALUES.put(id, this);
        }

        public int getID() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isRare() {
            return rare;
        }

        public static PandaType pickType(PandaType main, PandaType hidden) {
            if (main.isRare()) {
                return main == hidden ? main : NORMAL;
            } else {
                return main;
            }
        }

        public static PandaType byId(int id) {
            if (id < 0 || id >= VALUES.size()) {
                id = 0;
            }

            return VALUES.get(id);
        }

        public static PandaType byName(String name) {
            for (PandaType pandaType : VALUES.values()) {
                if (pandaType.name.equals(name)) {
                    return pandaType;
                }
            }

            return NORMAL;
        }

        public static PandaType pickRandomType(Random rand) {
            int i = rand.nextInt(16);
            
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
}