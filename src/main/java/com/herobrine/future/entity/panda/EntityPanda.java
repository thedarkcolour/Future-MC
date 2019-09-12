package com.herobrine.future.entity.panda;

import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    private float field_213603_bM;
    private float field_213604_bN;
    private float field_213605_bO;
    private float field_213606_bP;
    private static final Predicate<EntityItem> BREEDING_ITEM = (entityItem) -> {
        Item item = entityItem.getItem().getItem();
        return (item == Init.BAMBOO_ITEM || item == Item.getItemFromBlock(Blocks.CAKE)) && entityItem.isEntityAlive() && !entityItem.cannotPickup();
    };

    public EntityPanda(World worldIn) {
        super(worldIn);
        setSize(1.25F, 1.25F);
        this.moveHelper = new EntityPanda.MoveHelperController(this);
    }

    public int getHungryTicks() {
        return this.dataManager.get(HUNGRY_TICKS);
    }

    public void setHungryTicks(int p_213588_1_) {
        this.dataManager.set(HUNGRY_TICKS, p_213588_1_);
    }

    public boolean isSneezing() {
        return this.func_213547_u(2);
    }

    public boolean isSitting() {
        return this.func_213547_u(8);
    }

    public void setIsScared(boolean flag) {
        this.setPandaFlag(8, flag);
    }

    public boolean func_213567_dY() {
        return this.func_213547_u(16);
    }

    public void func_213542_s(boolean p_213542_1_) {
        this.setPandaFlag(16, p_213542_1_);
    }

    public boolean isEating() {
        return this.dataManager.get(EATING_TICKS) > 0;
    }

    public void setIsEating(boolean p_213534_1_) {
        this.dataManager.set(EATING_TICKS, p_213534_1_ ? 1 : 0);
    }

    private int getEatingTicks() {
        return this.dataManager.get(EATING_TICKS);
    }

    private void setEatingTicks(int p_213571_1_) {
        this.dataManager.set(EATING_TICKS, p_213571_1_);
    }

    public void func_213581_u(boolean flag) {
        this.setPandaFlag(2, flag);
        if (!flag) {
            this.func_213562_s(0);
        }

    }

    public int func_213585_ee() {
        return this.dataManager.get(SNEEZE_TICKS);
    }

    public void func_213562_s(int p_213562_1_) {
        this.dataManager.set(SNEEZE_TICKS, p_213562_1_);
    }

    public EntityPanda.Type getMainGene() {
        return EntityPanda.Type.fromID(this.dataManager.get(MAIN_GENE));
    }
    
    public void setMainGene(EntityPanda.Type pandaType) {
        if (pandaType.getID() > 6) {
            pandaType = EntityPanda.Type.getRandomType(this.rand);
        }

        this.dataManager.set(MAIN_GENE, (byte)pandaType.getID());
    }

    public EntityPanda.Type getHiddenGene() {
        return EntityPanda.Type.fromID(this.dataManager.get(HIDDEN_GENE));
    }

    public void setHiddenGene(EntityPanda.Type p_213541_1_) {
        if (p_213541_1_.getID() > 6) {
            p_213541_1_ = EntityPanda.Type.getRandomType(this.rand);
        }

        this.dataManager.set(HIDDEN_GENE, (byte)p_213541_1_.getID());
    }

    public boolean func_213564_eh() {
        return this.func_213547_u(4);
    }

    public void func_213576_v(boolean p_213576_1_) {
        this.setPandaFlag(4, p_213576_1_);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HUNGRY_TICKS, 0);
        this.dataManager.register(SNEEZE_TICKS, 0);
        this.dataManager.register(MAIN_GENE, (byte)0);
        this.dataManager.register(HIDDEN_GENE, (byte)0);
        this.dataManager.register(PANDA_FLAGS, (byte)0);
        this.dataManager.register(EATING_TICKS, 0);
    }

    private boolean func_213547_u(int i) {
        return (this.dataManager.get(PANDA_FLAGS) & i) != 0;
    }

    private void setPandaFlag(int bit, boolean flag) {
        byte b = this.dataManager.get(PANDA_FLAGS);
        if (flag) {
            this.dataManager.set(PANDA_FLAGS, (byte)(b | bit));
        } else {
            this.dataManager.set(PANDA_FLAGS, (byte)(b & ~bit));
        }

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("MainGene", this.getMainGene().getName());
        compound.setString("HiddenGene", this.getHiddenGene().getName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setMainGene(EntityPanda.Type.fromName(compound.getString("MainGene")));
        this.setHiddenGene(EntityPanda.Type.fromName(compound.getString("HiddenGene")));
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
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new PanicGoal(this, 2.0D));
        this.tasks.addTask(2, new AIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAttack(this, 1.2F, true));
        this.tasks.addTask(4, new EntityAITempt(this, 1.0D, Init.BAMBOO_ITEM, false));
        this.tasks.addTask(6, new EntityAIAvoid<>(this, EntityPlayer.class, 8.0F, 2.0D, 2.0D));
        this.tasks.addTask(6, new EntityAIAvoid<>(this, EntityMob.class, 4.0F, 2.0D, 2.0D));
        this.tasks.addTask(7, new EntityAISit(this));
        this.tasks.addTask(8, new EntityAILieDown(this));
        this.tasks.addTask(8, new EntityAIChildPlay(this));
        this.tasks.addTask(9, new EntityAIWatch(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.tasks.addTask(12, new AIRoll(this));
        this.tasks.addTask(13, new EntityAIFollowParent(this, 1.25D));
        this.tasks.addTask(14, new EntityAIWanderAvoidWater(this, 1.0D));
        this.targetTasks.addTask(1, new AIRevenge(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    public EntityPanda.Type getPandaType() {
        return EntityPanda.Type.func_221101_b(this.getMainGene(), this.getHiddenGene());
    }
    
    public boolean isLazy() {
        return this.getPandaType() == EntityPanda.Type.LAZY;
    }

    public boolean isWorried() {
        return this.getPandaType() == EntityPanda.Type.WORRIED;
    }

    public boolean isPlayful() {
        return this.getPandaType() == EntityPanda.Type.PLAYFUL;
    }

    public boolean isWeak() {
        return this.getPandaType() == EntityPanda.Type.WEAK;
    }

    public boolean isAggressive() {
        return this.getPandaType() == EntityPanda.Type.AGGRESSIVE;
    }

    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        this.playSound(Sounds.PANDA_BITE, 1.0F, 1.0F);
        if (!this.isAggressive()) {
            this.attacked = true;
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

        if (this.isWorried()) {
            if (this.world.isThundering() && !this.isInWater()) {
                this.setIsScared(true);
                this.setIsEating(false);
            } else if (!this.isEating()) {
                this.setIsScared(false);
            }
        }

        if (this.getAttackTarget() == null) {
            this.revenge = false;
            this.attacked = false;
        }

        if (this.getHungryTicks() > 0) {
            if (this.getAttackTarget() != null) {
                this.faceEntity(this.getAttackTarget(), 90.0F, 90.0F);
            }

            if (this.getHungryTicks() == 29 || this.getHungryTicks() == 14) {
                this.playSound(Sounds.PANDA_CANNOT_BREED, 1.0F, 1.0F);
            }

            this.setHungryTicks(this.getHungryTicks() - 1);
        }

        if (this.isSneezing()) {
            this.func_213562_s(this.func_213585_ee() + 1);
            if (this.func_213585_ee() > 20) {
                this.func_213581_u(false);
                this.sneeze();
            } else if (this.func_213585_ee() == 1) {
                this.playSound(Sounds.PANDA_PRE_SNEEZE, 1.0F, 1.0F);
            }
        }

        if (this.func_213564_eh()) {
            try {
                this.func_213535_ey();
            } catch (NoSuchMethodError ignored) {
                //  😈 😈 😈
            }
        } else {
            this.field_213608_bz = 0;
        }

        if (this.isSitting()) {
            this.rotationPitch = 0.0F;
        }

        this.updateScaredAnimation();
        this.updateEatingAnimation();
        this.updateLazyAnimation();
        this.updateRollingAnimation();
    }

    public boolean func_bruh() {
        return this.isWorried() && this.world.isThundering();
    }

    private void updateEatingAnimation() {
        if (!isEating() && isSitting() && !func_bruh() && !getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() && rand.nextInt(80) == 1) {
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
            playSound(Sounds.PANDA_EAT, 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

            for(int i = 0; i < 6; ++i) {
                Vec3d vec3d = new Vec3d(((double)rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double)rand.nextFloat() - 0.5D) * 0.1D);
                vec3d = vec3d.rotatePitch(-rotationPitch * ((float)Math.PI / 180F));
                vec3d = vec3d.rotateYaw(-rotationYaw * ((float)Math.PI / 180F));
                double d0 = (double)(-rand.nextFloat()) * 0.6D - 0.3D;
                Vec3d vec3d1 = new Vec3d(((double)rand.nextFloat() - 0.5D) * 0.8D, d0, 1.0D + ((double)rand.nextFloat() - 0.5D) * 0.4D);
                vec3d1 = vec3d1.rotateYaw(-renderYawOffset * ((float)Math.PI / 180F));
                vec3d1 = vec3d1.addVector(posX, posY + (double)getEyeHeight() + 1.0D, posZ);
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
        field_213604_bN = field_213603_bM;
        if (this.func_213567_dY()) {
            this.field_213603_bM = Math.min(1.0F, this.field_213603_bM + 0.15F);
        } else {
            this.field_213603_bM = Math.max(0.0F, this.field_213603_bM - 0.19F);
        }

    }

    private void updateRollingAnimation() {
        this.field_213606_bP = this.field_213605_bO;
        if (this.func_213564_eh()) {
            this.field_213605_bO = Math.min(1.0F, this.field_213605_bO + 0.15F);
        } else {
            this.field_213605_bO = Math.max(0.0F, this.field_213605_bO - 0.19F);
        }

    }

    @SideOnly(Side.CLIENT)
    public float func_213561_v(float p_213561_1_) {
        return func_219799_g(p_213561_1_, this.lastScaredAnimationProgress, this.scaredAnimationProgress);
    }

    @SideOnly(Side.CLIENT)
    public float func_213583_w(float p_213583_1_) {
        return func_219799_g(p_213583_1_, this.field_213604_bN, this.field_213603_bM);
    }

    @SideOnly(Side.CLIENT)
    public float func_213591_x(float p_213591_1_) {
        return func_219799_g(p_213591_1_, this.field_213606_bP, this.field_213605_bO);
    }

    private void func_213535_ey() {
        ++this.field_213608_bz;
        if (this.field_213608_bz > 32) {
            this.func_213576_v(false);
        } else {
            if (!this.world.isRemote) {
                Vec3d vec3d = new Vec3d(motionX, motionY, motionZ);
                if (this.field_213608_bz == 1) {
                    float f = this.rotationYaw * ((float)Math.PI / 180F);
                    float f1 = this.isChild() ? 0.1F : 0.2F;
                    this.field_213600_bJ = new Vec3d(vec3d.x + (double)(-MathHelper.sin(f) * f1), 0.0D, vec3d.z + (double)(MathHelper.cos(f) * f1));
                    Vec3d vec = this.field_213600_bJ.addVector(0.0D, 0.27D, 0.0D);
                    this.setVelocity(vec.x, vec.y, vec.z);
                } else if ((float)this.field_213608_bz != 7.0F && (float)this.field_213608_bz != 15.0F && (float)this.field_213608_bz != 23.0F) {
                    this.setVelocity(this.field_213600_bJ.x, vec3d.y, this.field_213600_bJ.z);
                } else {
                    this.setVelocity(0.0D, this.onGround ? 0.27D : vec3d.y, 0.0D);
                }
            }

        }
    }

    private void sneeze() {
        //Vec3d vec3d = new Vec3d(motionX, motionY, motionZ); // TODO Sneeze
        //this.world.addParticle(ParticleTypes.field_218421_R, this.posX - (double)(this.getWidth() + 1.0F) * 0.5D * (double)MathHelper.sin(this.renderYawOffset * ((float)Math.PI / 180F)), this.posY + (double)this.getEyeHeight() - (double)0.1F, this.posZ + (double)(this.getWidth() + 1.0F) * 0.5D * (double)MathHelper.cos(this.renderYawOffset * ((float)Math.PI / 180F)), vec3d.x, 0.0D, vec3d.z);
        this.playSound(Sounds.PANDA_SNEEZE, 1.0F, 1.0F);

        for(EntityPanda entityPanda : this.world.getEntitiesWithinAABB(EntityPanda.class, this.getEntityBoundingBox().grow(10.0D))) {
            if (!entityPanda.isChild() && entityPanda.onGround && !entityPanda.isInWater() && entityPanda.func_213537_eq()) {
                entityPanda.jump();
            }
        }

        if (!this.world.isRemote && this.rand.nextInt(700) == 0 && this.world.getGameRules().getBoolean("doMobLoot")) {
            this.entityDropItem(new ItemStack(Items.SLIME_BALL), 0);
        }
    }

    @Override
    protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
        if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() && BREEDING_ITEM.test(itemEntity)) {
            ItemStack itemstack = itemEntity.getItem();
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack);
            this.inventoryHandsDropChances[EntityEquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.onItemPickup(itemEntity, itemstack.getCount());
            itemEntity.setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        this.setIsScared(false);
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
        data = super.onInitialSpawn(difficulty, data);
        this.setMainGene(EntityPanda.Type.getRandomType(this.rand));
        this.setHiddenGene(EntityPanda.Type.getRandomType(this.rand));
        this.modifyAttributes();
        if (data instanceof EntityPanda.PandaData) {
            if (this.rand.nextInt(5) == 0) {
                this.setGrowingAge(-24000);
            }
        } else {
            data = new EntityPanda.PandaData();
        }

        return data;
    }

    private void func_213545_a(EntityPanda entityPanda, EntityPanda ageable) {
        if (ageable == null) {
            if (this.rand.nextBoolean()) {
                this.setMainGene(entityPanda.pickMainOrHiddenGene());
                this.setHiddenGene(EntityPanda.Type.getRandomType(this.rand));
            } else {
                this.setMainGene(EntityPanda.Type.getRandomType(this.rand));
                this.setHiddenGene(entityPanda.pickMainOrHiddenGene());
            }
        } else if (this.rand.nextBoolean()) {
            this.setMainGene(entityPanda.pickMainOrHiddenGene());
            this.setHiddenGene(ageable.pickMainOrHiddenGene());
        } else {
            this.setMainGene(ageable.pickMainOrHiddenGene());
            this.setHiddenGene(entityPanda.pickMainOrHiddenGene());
        }

        if (this.rand.nextInt(32) == 0) {
            this.setMainGene(EntityPanda.Type.getRandomType(this.rand));
        }

        if (this.rand.nextInt(32) == 0) {
            this.setHiddenGene(EntityPanda.Type.getRandomType(this.rand));
        }
    }

    private EntityPanda.Type pickMainOrHiddenGene() {
        return this.rand.nextBoolean() ? this.getMainGene() : this.getHiddenGene();
    }

    public void modifyAttributes() {
        if (this.isWeak()) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        }

        if (this.isLazy()) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.07F);
        }

    }

    private void func_213586_eB() {
        if (!this.isInWater()) {
            this.setMoveForward(0.0F);
            this.getNavigator().clearPath();
            this.setIsScared(true);
        }

    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() instanceof ItemMonsterPlacer) {
            return super.processInteract(player, hand);
        } else if (this.func_bruh()) {
            return false;
        } else if (this.func_213567_dY()) {
            func_213542_s(false);
            return true;
        } else if (this.isBreedingItem(itemstack)) {
            if (getAttackTarget() != null) {
                revenge = true;
            }

            if (this.isChild()) {
                consumeItemFromStack(player, itemstack);
                ageUp((int)((float)(-getGrowingAge() / 20) * 0.1F), true);
            } else if (!world.isRemote && getGrowingAge() == 0 && canBreed()) {
                consumeItemFromStack(player, itemstack);
                setInLove(player);
            } else {
                if (world.isRemote || isSitting() || isInWater()) {
                    return false;
                }

                this.func_213586_eB();
                this.setIsEating(true);
                ItemStack stack = getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                if (!stack.isEmpty() && !player.isCreative()) {
                    this.entityDropItem(stack, 0);
                }

                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(itemstack.getItem(), 1));
                this.consumeItemFromStack(player, itemstack);
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
        if (this.isAggressive()) {
            return Sounds.PANDA_AGGRESSIVE_AMBIENT;
        } else {
            return this.isWorried() ? Sounds.PANDA_WORRIED_AMBIENT : Sounds.PANDA_AMBIENT;
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(Sounds.PANDA_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Init.BAMBOO_ITEM;
    }

    private boolean func_213548_j(ItemStack p_213548_1_) {
        return this.isBreedingItem(p_213548_1_) || p_213548_1_.getItem() == Item.getItemFromBlock(Blocks.CAKE);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Sounds.PANDA_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return Sounds.PANDA_HURT;
    }

    public boolean func_213537_eq() {
        return !this.func_213567_dY() && !this.func_bruh() && !this.isEating() && !this.func_213564_eh() && !this.isSitting();
    }

    static class EntityAIAttack extends EntityAIAttackMelee {
        private final EntityPanda entityPanda;

        EntityAIAttack(EntityPanda entityPanda, double speedIn, boolean useLongMemory) {
            super(entityPanda, speedIn, useLongMemory);
            this.entityPanda = entityPanda;
        }

        public boolean shouldExecute() {
            return this.entityPanda.func_213537_eq() && super.shouldExecute();
        }
    }

    static class EntityAIAvoid<T extends EntityLivingBase> extends EntityAIAvoidEntity<T> {
        private final EntityPanda field_220875_i;

        EntityAIAvoid(EntityPanda entityPanda, Class<T> entityToAvoid, float avoidDistance, double farSpeedIn, double nearSpeedIn) {
            super(entityPanda, entityToAvoid, (Entity entity) -> {
                if(entity instanceof EntityPlayer) {
                    return !((EntityPlayer) entity).isSpectator();
                }
                return true;
            }, avoidDistance, farSpeedIn, nearSpeedIn);
            this.field_220875_i = entityPanda;
        }

        public boolean shouldExecute() {
            return this.field_220875_i.isWorried() && this.field_220875_i.func_213537_eq() && super.shouldExecute();
        }
    }

    static class EntityAIChildPlay extends EntityAIBase {
        private final EntityPanda entityPanda;

        EntityAIChildPlay(EntityPanda p_i51448_1_) {
            this.entityPanda = p_i51448_1_;
        }

        public boolean shouldExecute() {
            if (this.entityPanda.isChild() && this.entityPanda.func_213537_eq()) {
                if (this.entityPanda.isWeak() && this.entityPanda.rand.nextInt(500) == 1) {
                    return true;
                } else {
                    return this.entityPanda.rand.nextInt(6000) == 1;
                }
            } else {
                return false;
            }
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            this.entityPanda.func_213581_u(true);
        }
    }

    static class EntityAILieDown extends EntityAIBase {
        private final EntityPanda field_220828_a;
        private int field_220829_b;

        EntityAILieDown(EntityPanda p_i51460_1_) {
            this.field_220828_a = p_i51460_1_;
        }

        public boolean shouldExecute() {
            return this.field_220829_b < this.field_220828_a.ticksExisted && this.field_220828_a.isLazy() && this.field_220828_a.func_213537_eq() && this.field_220828_a.rand.nextInt(400) == 1;
        }

        public boolean shouldContinueExecuting() {
            if (!this.field_220828_a.isInWater() && (this.field_220828_a.isLazy() || this.field_220828_a.rand.nextInt(600) != 1)) {
                return this.field_220828_a.rand.nextInt(2000) != 1;
            } else {
                return false;
            }
        }

        public void startExecuting() {
            this.field_220828_a.func_213542_s(true);
            this.field_220829_b = 0;
        }

        public void resetTask() {
            this.field_220828_a.func_213542_s(false);
            this.field_220829_b = this.field_220828_a.ticksExisted + 200;
        }
    }

    static class AIMate extends EntityAIMate {
        private final EntityPanda panda;
        private int field_220694_f;

        AIMate(EntityPanda entityPanda, double speedIn) {
            super(entityPanda, speedIn);
            this.panda = entityPanda;
        }

        public boolean shouldExecute() {
            if (super.shouldExecute() && this.panda.getHungryTicks() == 0) {
                if (!this.func_220691_h()) {
                    if (this.field_220694_f <= this.panda.ticksExisted) {
                        this.panda.setHungryTicks(32);
                        this.field_220694_f = this.panda.ticksExisted + 600;

                        if (this.panda.isServerWorld()) {
                            EntityPlayer playerIn = this.panda.world.getClosestPlayerToEntity(panda, 8);
                            this.panda.setAttackTarget(playerIn);
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

        private boolean func_220691_h() {
            BlockPos pos = new BlockPos(this.panda);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < 8; ++j) {
                    for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            mutablePos.setPos(pos).add(k, i, l);
                            if (this.panda.world.getBlockState(mutablePos).getBlock() == Init.BAMBOO_STALK) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    static class MoveHelperController extends EntityMoveHelper {
        private final EntityPanda field_220672_i;

        MoveHelperController(EntityPanda entityPanda) {
            super(entityPanda);
            this.field_220672_i = entityPanda;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.field_220672_i.func_213537_eq()) {
                super.onUpdateMoveHelper();
            }
        }
    }

    static class PandaData implements IEntityLivingData {
        private PandaData() {
        }
    }

    static class PanicGoal extends EntityAIPanic {
        private final EntityPanda field_220740_f;

        PanicGoal(EntityPanda entityPanda, double speedIn) {
            super(entityPanda, speedIn);
            this.field_220740_f = entityPanda;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            if (!this.field_220740_f.isBurning()) {
                return false;
            } else {
                BlockPos blockpos = getRandomPos(this.field_220740_f.world, this.field_220740_f);
                if (blockpos != null) {
                    this.randPosX = blockpos.getX();
                    this.randPosY = blockpos.getY();
                    this.randPosZ = blockpos.getZ();
                    return true;
                } else {
                    return this.findRandomPosition();
                }
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (this.field_220740_f.isSitting()) {
                this.field_220740_f.getNavigator().clearPath();
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
            this.panda = entityPanda;
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (!this.panda.revenge && !this.panda.attacked) {
                return super.shouldContinueExecuting();
            } else {
                this.panda.setAttackTarget(null);
                return false;
            }
        }
    }

    private static class AIRoll extends EntityAIBase {
        private final EntityPanda panda;

        private AIRoll(EntityPanda p_i51452_1_) {
            this.panda = p_i51452_1_;
            setMutexBits(7);
        }

        public boolean shouldExecute() {
            if ((this.panda.isChild() || this.panda.isPlayful()) && this.panda.onGround) {
                if (!this.panda.func_213537_eq()) {
                    return false;
                } else {
                    float f = this.panda.rotationYaw * ((float)Math.PI / 180F);
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

                    if (this.panda.world.getBlockState((new BlockPos(this.panda)).add(i, -1, j)).getBlock().getMaterial(null) == Material.AIR) {
                        return true;
                    } else if (this.panda.isPlayful() && this.panda.rand.nextInt(60) == 1) {
                        return true;
                    } else {
                        return this.panda.rand.nextInt(500) == 1;
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
            this.panda.func_213576_v(true);
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
            this.setMutexBits(1);
            this.panda = panda;
        }

        @Override
        public boolean shouldExecute() {
            if (this.field_220832_b <= panda.ticksExisted && !panda.isChild() && !panda.isInWater() && panda.func_213537_eq() && panda.getHungryTicks() <= 0) {
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

            this.field_220832_b = 0;
        }

        @Override
        public void resetTask() {
            ItemStack itemstack = panda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty()) {
                panda.entityDropItem(itemstack, 0.0F);
                panda.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                int i = panda.isLazy() ? panda.rand.nextInt(50) + 10 : panda.rand.nextInt(150) + 10;
                this.field_220832_b = panda.ticksExisted + i * 20;
            }

            panda.setIsScared(false);
        }
    }

    public void adjustTraits() {
        if (this.isWeak()) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        }

        if (this.isLazy()) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.07F);
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
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public boolean isRare() {
            return this.isRare;
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
            this.panda = entityPanda;
        }

        public boolean shouldExecute() {
            return this.panda.func_213537_eq() && super.shouldExecute();
        }
    }

    public static float func_219799_g(float p_219799_0_, float p_219799_1_, float p_219799_2_) {
        return p_219799_1_ + p_219799_0_ * (p_219799_2_ - p_219799_1_);
    }
}