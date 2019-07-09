package com.herobrine.future.entity.trident;

import com.herobrine.future.enchantment.EnchantHelper;
import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityNewTrident extends EntityPiercingArrow {
    private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BYTE);
    private ItemStack thrownStack = new ItemStack(Init.TRIDENT);
    private boolean dealtDamage;
    public int returningTicks;

    public EntityNewTrident(World p_i50148_2_) {
        super(p_i50148_2_);
    }

    public EntityNewTrident(World worldIn, EntityLivingBase shooter, ItemStack thrownStack) {
        super(worldIn, shooter);
        this.thrownStack = thrownStack.copy();
        this.dataManager.set(LOYALTY_LEVEL, (byte) EnchantHelper.getLoyalty(thrownStack));
    }

    public EntityNewTrident(World worldIn, double x, double y, double z, ItemStack thrownStack) {
        super(worldIn, x, y, z);
        this.thrownStack = thrownStack.copy();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LOYALTY_LEVEL, (byte)0);
    }

    @Override
    public void onUpdate() {
        if (this.timeInGround > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getShooter();
        if ((this.dealtDamage || this.func_203047_q()) && entity != null) {
            int i = this.dataManager.get(LOYALTY_LEVEL);
            if (i > 0 && !this.shouldReturnToThrower()) {
                if (!this.world.isRemote && this.pickupStatus == EntityArrow.PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }

                this.setDead();
            } else if (i > 0) {
                this.func_203045_n(true);
                Vec3d vec3d = new Vec3d(entity.posX - this.posX, entity.posY + (double)entity.getEyeHeight() - this.posY, entity.posZ - this.posZ);
                this.posY += vec3d.y * 0.015D * (double)i;
                if (this.world.isRemote) {
                    this.lastTickPosY = this.posY;
                }

                double d0 = 0.05D * (double)i;
                this.setMotion(this.getMotion().scale(0.95D).add(vec3d.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(Sounds.TRIDENT_LOYALTY, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }

        super.onUpdate();
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getShooter();
        if (entity != null && entity.isEntityAlive()) {
            return !(entity instanceof EntityPlayerMP) || !isSpectatorEntity(entity);
        } else {
            return false;
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    @Override
    protected RayTraceResult func_213866_a(Vec3d p_213866_1_, Vec3d p_213866_2_) {
        return this.dealtDamage ? null : super.func_213866_a(p_213866_1_, p_213866_2_);
    }

    @Override
    protected void func_213868_a(RayTraceResult p_213868_1_) {
        Entity entity = p_213868_1_.entityHit;
        if(entity == null) return;

        float f = 8.0F;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase livingentity = (EntityLivingBase)entity;
            f += EnchantmentHelper.getModifierForCreature(this.thrownStack, livingentity.getCreatureAttribute());
        }

        Entity entity1 = this.getShooter();
        DamageSource damagesource = causeTridentDamage(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent soundevent = Sounds.TRIDENT_PIERCE;
        if (entity.attackEntityFrom(damagesource, f) && entity instanceof EntityLivingBase) {
            EntityLivingBase livingentity1 = (EntityLivingBase)entity;
            if (entity1 instanceof EntityLivingBase) {
                EnchantmentHelper.applyThornEnchantments(livingentity1, entity1);
                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)entity1, livingentity1);
            }

            this.arrowHit(livingentity1);
        }

        this.setMotion(multiplyVec(this.getMotion(), -0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.world instanceof WorldServer && this.world.isThundering() && EnchantHelper.hasChanneling(this.thrownStack)) {
            BlockPos blockpos = entity.getPosition();
            if (this.world.canSeeSky(blockpos)) {
                EntityLightningBolt lightningboltentity = new EntityLightningBolt(this.world, (double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D, false);
                this.world.addWeatherEffect(lightningboltentity);
                soundevent = Sounds.TRIDENT_CONDUCTIVIDAD;
                f1 = 5.0F;
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    @Override
    protected SoundEvent getGroundHitSound() {
        return Sounds.TRIDENT_IMPACT;
    }


    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        Entity entity = this.getShooter();
        if (entity == null || entity.getUniqueID() == entityIn.getUniqueID()) {
            super.onCollideWithPlayer(entityIn);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Trident", 10)) {
            this.thrownStack = new ItemStack(compound.getCompoundTag("Trident"));
        }

        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.dataManager.set(LOYALTY_LEVEL, (byte)EnchantHelper.getLoyalty(this.thrownStack));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Trident", this.thrownStack.writeToNBT(new NBTTagCompound()));
        compound.setBoolean("DealtDamage", this.dealtDamage);
    }


    protected void tryDespawn() {
        int i = this.dataManager.get(LOYALTY_LEVEL);
        if (this.pickupStatus != EntityArrow.PickupStatus.ALLOWED || i <= 0) {
            super.tryDespawn();
        }

    }

    @Override
    protected float getWaterDrag() {
        return 0.99F;
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    private static DamageSource causeTridentDamage(Entity trident, Entity indirectEntityIn) {
        return (new EntityDamageSourceIndirect("trident", trident, indirectEntityIn)).setProjectile();
    }
}
/*
public class EntityTrident extends EntityArrow {
    private static final Predicate<Entity> ARROW_TARGETS = entity -> EntitySelectors.NOT_SPECTATING.test(entity) && EntitySelectors.IS_ALIVE.test(entity) && entity.canBeCollidedWith();
    private ItemStack thrownStack;
    private boolean hasChanneled;
    private int ticksInGround = 0;
    private boolean isReturning;

    public EntityTrident(World world) {
        super(world);
    }

    public EntityTrident(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z);
        this.thrownStack = stack;
    }

    public EntityTrident(World world, EntityLivingBase shooter, ItemStack stack) {
        super(world, shooter);
        this.thrownStack = stack;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    @Override
    protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
        if (world.isRemote) {
            return null;
        }
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), ARROW_TARGETS::test );
        double d0 = 0.0D;

        for (Entity entity1 : list) {
            if (entity1 != this.shootingEntity) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

                if (raytraceresult != null) {
                    double d1 = start.squareDistanceTo(raytraceresult.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }
        return entity;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("ItemTrident", this.thrownStack.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("ItemTrident")) {
            this.thrownStack = read(compound.getCompoundTag("ItemTrident"));
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        Entity entity = result.entityHit;

        if(entity != null) {
            DamageSource source;

            if(!(this.shootingEntity instanceof EntityPlayer)) {
                source = causeTridentDamage(this, this);
            }
            else {
                source = causeTridentDamage(this, shootingEntity);
            }

            if(entity.attackEntityFrom(source, getDamageForTrident())) {
                if(entity instanceof EntityLivingBase) {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

                    if(this.shootingEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(entityLivingBase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments(entityLivingBase, this.shootingEntity);
                    }

                    this.arrowHit(entityLivingBase);

                    if(this.shootingEntity != null && entityLivingBase != this.shootingEntity && entityLivingBase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayer) {
                        ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }

                    this.playSound(Sounds.TRIDENT_PIERCE, 1.0F, 1.0F);

                    this.motionX *= -0.009999999776482582D;
                    this.motionY *= -0.10000000149011612D;
                    this.motionZ *= -0.009999999776482582D;
                    this.rotationYaw += 180.0F;
                    this.prevRotationYaw += 180.0F;

                    if(!this.hasChanneled && thrownStack != null && !thrownStack.isEmpty()) { // Enchantment handling
                        Map enchants = EnchantmentHelper.getEnchantments(thrownStack);

                        if(!enchants.isEmpty()) {
                            if (enchants.get(Enchantments.CONDUCTIVIDAD) != null) {
                                shootingEntity.world.addWeatherEffect(new EntityLightningBolt(shootingEntity.world, this.posX, this.posY, this.posZ, false));
                                this.playSound(Sounds.TRIDENT_CONDUCTIVIDAD, 5.0F, 1.0F);
                                this.hasChanneled = true;
                            }
                        }
                    }
                }
            }
            else {

                if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D) {
                    if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED) {
                        this.entityDropItem(this.getArrowStack(), 0.1F);
                    }
                    this.setDead();
                }
            }
        }

        else {
            this.motionX = (double)((float)(result.hitVec.x - this.posX));
            this.motionY = (double)((float)(result.hitVec.y - this.posY));
            this.motionZ = (double)((float)(result.hitVec.z - this.posZ));
            float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
            this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
            this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
            this.inGround = true;
            this.arrowShake = 7;

            this.playSound(Sounds.TRIDENT_IMPACT, 1.0F, 1.0F);

            if(!this.hasChanneled && EnchantHelper.hasChanneling(thrownStack)) { // Enchantment handling
                Entity living = shootingEntity == null ? this : shootingEntity;
                living.world.addWeatherEffect(new EntityLightningBolt(living.world, this.posX, this.posY, this.posZ, false));
                this.playSound(Sounds.TRIDENT_CONDUCTIVIDAD, 5.0F, 1.0F);
                this.hasChanneled = true;
            }
        }
    }

    @Override
    public void onUpdate() {
        if(this.inGround) {
            this.ticksInGround++;
        }

        if(shootingEntity instanceof EntityPlayer) {
            int loyalty = EnchantHelper.getLoyalty(thrownStack);

            if(0 < loyalty && loyalty < 4 && !((EntityPlayer) shootingEntity).isSpectator() && !(shootingEntity.isDead) && ticksInGround > 100 - (loyalty * 20)) {
                this.noClip = true;

                if(!isReturning) {
                    this.playSound(Sounds.TRIDENT_LOYALTY, 1.0F, 1.0F);
                    this.isReturning = true;
                }
                if(this.ticksInGround >= (100 - (loyalty * 20))){
                    double d0 = 0.02D * (double) loyalty;
                    Vec3d vec3d = new Vec3d(shootingEntity.posX - this.posX, shootingEntity.posY
                            + (double) shootingEntity.getEyeHeight() - this.posY, shootingEntity.posZ - this.posZ);
                    this.motionX += vec3d.x * d0 - this.motionX * 0.05D;
                    this.motionY += vec3d.y * d0 - this.motionY * 0.05D;
                    this.motionZ += vec3d.z * d0 - this.motionZ * 0.05D;

                    this.move(MoverType.SELF, this.motionX * 0.05D, this.motionY * 0.05D, this.motionZ * 0.05D);
                }
            }
        }

        if(this.world.getClosestPlayerToEntity(this, 1.5D) == this.shootingEntity && isReturning) {
            if(this.pickupStatus == PickupStatus.ALLOWED) {
                dropTridentStack();
            }
            this.setDead();
        }

        super.onUpdate();
    }

    @Override
    public boolean getIsCritical() {
        return false;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    private static ItemStack read(NBTTagCompound compound) {
        try {
            return new ItemStack(compound);
        } catch (RuntimeException runtimeexception) {
            FutureMC.LOGGER.debug("Tried to load invalid item: {}", compound, runtimeexception);
            return ItemStack.EMPTY;
        }
    }

    private float getDamageForTrident() {
        float level = EnchantHelper.getImpaling(this.thrownStack);
        float damage = 8F;

        if(level > 0) {
            damage += (level * 1.25);
        }
        return damage;
    }

    private void dropTridentStack() {
        if(thrownStack != null && !thrownStack.isEmpty()) {
            EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + (double) (float) 0.1, this.posZ, thrownStack);
            entityitem.setNoPickupDelay();

            if (captureDrops) {
                this.capturedDrops.add(entityitem);
            }
            else {
                this.world.spawnEntity(entityitem);
            }
        }
    }

    private static DamageSource causeTridentDamage(Entity trident, Entity indirectEntityIn) {
        return (new EntityDamageSourceIndirect("trident", trident, indirectEntityIn)).setProjectile();
    }
}*/