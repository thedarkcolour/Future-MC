package com.herobrine.future.entity;

import com.herobrine.future.FutureJava;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

import static net.minecraft.item.ItemStack.EMPTY;

public class EntityTrident extends EntityArrow {
    private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BYTE);
    private ItemStack thrownStack;
    private boolean dealtDamage;
    int returningTicks;

    public EntityTrident(World worldIn) {
        super(worldIn);
        this.thrownStack = new ItemStack(Init.trident);
    }

    public EntityTrident(World world, EntityLivingBase shooter, ItemStack stack) {
        super(world, shooter);
        this.thrownStack = new ItemStack(Init.trident);
        this.dataManager.set(LOYALTY_LEVEL, (byte) 0);
    }

    public EntityTrident(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.thrownStack = new ItemStack(Init.trident);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LOYALTY_LEVEL, (byte) 0);
        this.dataManager.register(CRITICAL, (byte) 0);
    }

    @Override
    public void onUpdate() {
        if (this.timeInGround > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getShooter();
        if ((this.dealtDamage || this.canCrit()) && entity != null) {
            int loyalty = this.dataManager.get(LOYALTY_LEVEL);
            if (loyalty > 0) {
                if (!this.world.isRemote && this.pickupStatus == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }

                this.setDead();
            } else if (loyalty > 0) {
                this.hasClip(true);
                Vec3d vec3d = new Vec3d(entity.posX - this.posX, entity.posY + (double)entity.getEyeHeight() - this.posY, entity.posZ - this.posZ);
                this.posY += vec3d.y * 0.015D * (double)loyalty;
                if (this.world.isRemote) {
                    this.lastTickPosY = this.posY;
                }

                vec3d = vec3d.normalize();
                double d = 0.05D * (double)loyalty;
                this.motionX += vec3d.x * d - this.motionX * 0.05D;
                this.motionY += vec3d.y * d - this.motionY * 0.05D;
                this.motionZ += vec3d.z * d - this.motionZ * 0.05D;
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }

        super.onUpdate();
    }

    @Nullable
    @Override
    protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
        return this.dealtDamage ? null : super.findEntityOnPath(start, end);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity entity = raytraceResultIn.entityHit;
        float f = 8.0F;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase lvt_4_1_ = (EntityLivingBase)entity;
            f += EnchantmentHelper.getModifierForCreature(this.thrownStack, lvt_4_1_.getCreatureAttribute());
        }

        Entity entity1 = this.getShooter();
        DamageSource source = DamageSource.causeArrowDamage(this, (entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent lvt_6_1_ = SoundEvents.ENTITY_ARROW_HIT;
        /*if (entity.attackEntityFrom(source, f) && entity instanceof EntityLivingBase) {
            EntityLivingBase target = (EntityLivingBase)entity;
            if (entity1 instanceof EntityLivingBase) {
                EnchantmentHelper.applyThornEnchantments(target, entity1);
                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)entity1, target);
            }

            this.arrowHit(target);
        }*/

        this.motionX *= -0.009999999776482582D;
        this.motionY *= -0.10000000149011612D;
        this.motionZ *= -0.009999999776482582D;
        float lvt_7_2_ = 1.0F;/*
        if (this.world.isThundering() && EnchantmentHelper.hasChanneling(this.thrownStack)) {
            BlockPos lvt_8_1_ = entity.getPosition();
            if (this.world.canSeeSky(lvt_8_1_)) {
                EntityLightningBolt lvt_9_1_ = new EntityLightningBolt(this.world, (double)lvt_8_1_.getX() + 0.5D, (double)lvt_8_1_.getY(), (double)lvt_8_1_.getZ() + 0.5D, false);
                lvt_9_1_.setCaster(lvt_4_2_ instanceof EntityPlayerMP ? (EntityPlayerMP)lvt_4_2_ : null);
                this.world.addWeatherEffect(lvt_9_1_);
                lvt_6_1_ = SoundEvents.ITEM_TRIDENT_THUNDER;
                lvt_7_2_ = 5.0F;
            }
        }*/

        this.playSound(lvt_6_1_, lvt_7_2_, 1.0F);
    }

    protected SoundEvent getHitGroundSound() {
        return SoundEvents.ENTITY_ARROW_HIT;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        Entity entity = this.getShooter();
        if (entity == null || entity.getUniqueID() == entityIn.getUniqueID()) {
            super.onCollideWithPlayer(entityIn);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Trident", 10)) {
            this.thrownStack = read(compound.getCompoundTag("Trident"));
        }

        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.dataManager.set(LOYALTY_LEVEL, (byte)0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Trident", this.thrownStack.writeToNBT(new NBTTagCompound()));
        compound.setBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    //
    //    This section adds missing methods used in 1.13 code
    //    May not have good names for each method
    //

    public void hasClip(boolean bool) {
        this.noClip = bool;
        this.func_203047_q(2, bool);
    }

    public boolean canCrit() {
        if (!this.world.isRemote) {
            return this.noClip;
        } else {
            return true;//(this.dataManager.get(CRITICAL) & 2) != 0;
        }
    }

    protected void func_203047_q(int p_203049_1_, boolean p_203049_2_) {
        byte b0 = this.dataManager.get(CRITICAL);
        if (p_203049_2_) {
            this.dataManager.set(CRITICAL, (byte)(b0 | p_203049_1_));
        } else {
            this.dataManager.set(CRITICAL, (byte)(b0 & ~p_203049_1_));
        }

    }

    @Nullable
    public Entity getShooter() {                                                                                           // Maybe problematic?
        return this.shootingEntity != null && this.world instanceof WorldServer ? ((WorldServer)this.world).getEntityFromUuid(this.shootingEntity.getUniqueID()) : null;
    }

    @Override
    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    public static ItemStack read(NBTTagCompound compound) {
        try {
            return new ItemStack(compound);
        } catch (RuntimeException runtimeexception) {
            FutureJava.logger.debug("Tried to load invalid item: {}", compound, runtimeexception);
            return EMPTY;
        }
    }
}
