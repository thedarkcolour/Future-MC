package com.herobrine.future.entity.trident;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public abstract class EntityPiercingArrow extends Entity implements IProjectile {
    private static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(EntityPiercingArrow.class, DataSerializers.BYTE);
    protected static final DataParameter<Optional<UUID>> SHOOTER = EntityDataManager.createKey(EntityPiercingArrow.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Byte> PIERCING_LEVEL = EntityDataManager.createKey(EntityPiercingArrow.class, DataSerializers.BYTE);
    private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    private IBlockState inBlockState;
    protected boolean inGround;
    protected int timeInGround;
    public PickupStatus pickupStatus = PickupStatus.DISALLOWED;
    public int arrowShake;
    public UUID shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage = 2.0D;
    private int knockbackStrength;
    private SoundEvent groundHitSound = this.getGroundHitSound();

    private IntOpenHashSet field_213878_az;

    private List<Entity> field_213875_aA;

    public EntityPiercingArrow(World worldIn) {
        super(worldIn);
        this.pickupStatus = PickupStatus.DISALLOWED;
        this.damage = 2.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntityPiercingArrow(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityPiercingArrow(World worldIn, BlockPos pos) {
        this(worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    public EntityPiercingArrow(World world, EntityLivingBase shooter) {
        super(world);
        setShooter(shooter);
    }

    public void setGroundHitSound(SoundEvent event) {
        this.groundHitSound = event;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(CRITICAL, (byte)0);
        this.dataManager.register(SHOOTER, Optional.absent());
        this.dataManager.register(PIERCING_LEVEL, (byte)0);
    }

    public void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        motionX += shooter.motionX;
        motionY += shooter.onGround ? 0.0D : shooter.motionY;
        motionZ += shooter.motionZ;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(new Vec3d(this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy)).scale((double)velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(func_213296_b(vec3d));
        this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        this.setMotion(x, y, z);
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (double)(180F / (float)Math.PI));
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        boolean flag = this.func_203047_q();
        Vec3d vec3d = this.getMotion();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(func_213296_b(vec3d));
            this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
        IBlockState blockstate = this.world.getBlockState(pos);
        if (!blockstate.getBlock().isAir(blockstate, this.world, pos) && !flag) {
            AxisAlignedBB box = blockstate.getBoundingBox(this.world, pos);
            if (box != EMPTY_AABB) {
                if (box.offset(pos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                    this.inGround = true;
                }
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.isWet()) {
            this.extinguish();
        }

        if (this.inGround && !flag) {
            if (this.inBlockState != blockstate && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().grow(0.06D)).isEmpty()) {
                this.inGround = false;
                this.setMotion(vec3d.x *(double)(this.rand.nextFloat() * 0.2F), vec3d.y * (double)(this.rand.nextFloat() * 0.2F), vec3d.z * (double)(this.rand.nextFloat() * 0.2F));
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            } else if (!this.world.isRemote) {
                this.tryDespawn();
            }

            ++this.timeInGround;
        } else {
            this.timeInGround = 0;
            ++this.ticksInAir;
            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d2 = vec3d1.add(vec3d);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d2, false, true, false);
            if (raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS) {
                vec3d2 = raytraceresult.hitVec;
            }

            while(!this.isDead) {
                RayTraceResult entityraytraceresult = this.func_213866_a(vec3d1, vec3d2);
                if (entityraytraceresult != null) {
                    raytraceresult = entityraytraceresult;
                }

                if (raytraceresult != null && raytraceresult.entityHit != null) {
                    Entity entity = raytraceresult.entityHit;
                    Entity entity1 = this.getShooter();
                    if (entity instanceof EntityPlayer && entity1 instanceof EntityPlayer && !((EntityPlayer)entity1).canAttackPlayer((EntityPlayer)entity)) {
                        raytraceresult = null;
                        entityraytraceresult = null;
                    }
                }

                if (raytraceresult != null && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                    this.isAirBorne = true;
                }

                    if (entityraytraceresult == null || this.func_213874_s() <= 0) {
                    break;
                }

                raytraceresult = null;
            }

            vec3d = this.getMotion();
            double d1 = vec3d.x;
            double d2 = vec3d.y;
            double d0 = vec3d.z;
            if (this.getIsCritical()) {
                for(int i = 0; i < 4; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + d1 * (double)i / 4.0D, this.posY + d2 * (double)i / 4.0D, this.posZ + d0 * (double)i / 4.0D, -d1, -d2 + 0.2D, -d0);
                }
            }

            this.posX += d1;
            this.posY += d2;
            this.posZ += d0;
            float f4 = MathHelper.sqrt(func_213296_b(vec3d));
            if (flag) {
                this.rotationYaw = (float)(MathHelper.atan2(-d1, -d0) * (double)(180F / (float)Math.PI));
            } else {
                this.rotationYaw = (float)(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI));
            }

            for(this.rotationPitch = (float)(MathHelper.atan2(d2, (double)f4) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

            while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
            this.rotationYaw = lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
            float f1 = 0.99F;
            float f2 = 0.05F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    float f3 = 0.25F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - d1 * 0.25D, this.posY - d2 * 0.25D, this.posZ - d0 * 0.25D, d1, d2, d0);
                }

                f1 = this.getWaterDrag();
            }

            this.setMotion(vec3d.scale((double)f1));
            if (!this.hasNoGravity() && !flag) {
                Vec3d vec3d3 = this.getMotion();
                this.setMotion(vec3d3.x, vec3d3.y - (double)0.05F, vec3d3.z);
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    protected void tryDespawn() {
        ++this.ticksInGround;
        if (this.ticksInGround >= 1200) {
            this.setDead();
        }
    }

    private void onHit(RayTraceResult result) {
        RayTraceResult.Type type = result.typeOfHit;
        if (type == RayTraceResult.Type.ENTITY) {
            this.func_213868_a(result);
        } else if (type == RayTraceResult.Type.BLOCK) {
            IBlockState blockstate = this.world.getBlockState(result.getBlockPos());
            this.inBlockState = blockstate;
            Vec3d vec3d = result.hitVec.subtract(this.posX, this.posY, this.posZ);
            this.setMotion(vec3d);
            Vec3d vec3d1 = vec3d.normalize().scale((double)0.05F);
            this.posX -= vec3d1.x;
            this.posY -= vec3d1.y;
            this.posZ -= vec3d1.z;
            this.playSound(this.getGroundHitSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.inGround = true;
            this.arrowShake = 7;
            this.setIsCritical(false);
            this.func_213872_b((byte)0);
            //this.func_213869_a(SoundEvents.ENTITY_ARROW_HIT);
            this.func_213865_o(false);
            this.func_213870_w();
            blockstate.getBlock().onEntityCollidedWithBlock(this.world, result.getBlockPos(), blockstate, this);
        }
    }

    private void func_213870_w() {
        if (this.field_213875_aA != null) {
            this.field_213875_aA.clear();
        }

        if (this.field_213878_az != null) {
            this.field_213878_az.clear();
        }

    }

    protected void func_213868_a(RayTraceResult result) {
        Entity entity = result.entityHit;
        if(entity == null) return;

        float f = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        int i = MathHelper.ceil(Math.max((double)f * this.damage, 0.0D));
        if (this.func_213874_s() > 0) {
            if (this.field_213878_az == null) {
                this.field_213878_az = new IntOpenHashSet(5);
            }

            if (this.field_213875_aA == null) {
                this.field_213875_aA = Lists.newArrayListWithCapacity(5);
            }

            if (this.field_213878_az.size() >= this.func_213874_s() + 1) {
                this.setDead();
                return;
            }

            this.field_213878_az.add(entity.getEntityId());
        }

        if (this.getIsCritical()) {
            i += this.rand.nextInt(i / 2 + 2);
        }

        Entity entity1 = this.getShooter();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = causeArrowDamage(this, this);
        } else {
            damagesource = causeArrowDamage(this, entity1);
            if (entity1 instanceof EntityLivingBase) {
                ((EntityLivingBase)entity1).setLastAttackedEntity(entity);
            }
        }

        //int j = entity.onF();

        if (entity.attackEntityFrom(damagesource, (float)i)) {
            if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                entity.setFire(5);
            }
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase livingentity = (EntityLivingBase)entity;
                if (!this.world.isRemote && this.func_213874_s() <= 0) {
                    livingentity.setArrowCountInEntity(livingentity.getArrowCountInEntity() + 1);
                }

                if (this.knockbackStrength > 0) {
                    Vec3d vec3d = multiplyVec(getMotion(), 1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockbackStrength * 0.6D);
                    if (vec3d.lengthSquared() > 0.0D) {
                        livingentity.addVelocity(vec3d.x, 0.1D, vec3d.z);
                    }
                }

                if (!this.world.isRemote && entity1 instanceof EntityLivingBase) {
                    EnchantmentHelper.applyThornEnchantments(livingentity, entity1);
                    EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)entity1, livingentity);
                }

                this.arrowHit(livingentity);
                if (livingentity != entity1 && livingentity instanceof EntityPlayer && entity1 instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)entity1).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                }

                if (!entity.isEntityAlive() && this.field_213875_aA != null) {
                    this.field_213875_aA.add(livingentity);
                }
            }

            this.playSound(getGroundHitSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            if (this.func_213874_s() <= 0 && !(entity instanceof EntityEnderman)) {
                this.setDead();
            }
        } else {
            //entity.setFire(j);
            this.setMotion(this.getMotion().scale(-0.1D));
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            this.ticksInAir = 0;
            if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D) {
                if (this.pickupStatus == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }

                this.setDead();
            }
        }

    }

    private static DamageSource causeArrowDamage(EntityPiercingArrow projectile, Entity shooter) {
        return (new EntityDamageSourceIndirect("arrow", projectile, shooter)).setProjectile();
    }

    protected SoundEvent getGroundHitSound() {
        return SoundEvents.ENTITY_ARROW_HIT;
    }

    protected void arrowHit(EntityLivingBase living) {
    }

    protected RayTraceResult func_213866_a(Vec3d start, Vec3d end) {
        return func_221271_a(this.world, this, start, end, this.getEntityBoundingBox().expand(motionX, motionY, motionZ).grow(1.0D),
                entity -> !isSpectatorEntity(entity) && entity.isEntityAlive() && entity.canBeCollidedWith() && (entity != EntityPiercingArrow.this.getShooter() || EntityPiercingArrow.this.ticksInAir >= 5) && (EntityPiercingArrow.this.field_213878_az == null || !EntityPiercingArrow.this.field_213878_az.contains(entity.getEntityId())));
    }

    public static RayTraceResult func_221271_a(World p_221271_0_, Entity projectile, Vec3d p_221271_2_, Vec3d p_221271_3_, AxisAlignedBB p_221271_4_, com.google.common.base.Predicate<Entity> p_221271_5_) {
        return func_221269_a(p_221271_0_, projectile, p_221271_2_, p_221271_3_, p_221271_4_, p_221271_5_, Double.MAX_VALUE);
    }

    public static RayTraceResult func_221269_a(World worldIn, Entity projectile, Vec3d p_221269_2_, Vec3d p_221269_3_, AxisAlignedBB box, com.google.common.base.Predicate<Entity> predicate, double p_221269_6_) {
        double d0 = p_221269_6_;
        Entity entity = null;

        for(Entity entity1 : worldIn.getEntitiesInAABBexcluding(projectile, box, predicate)) {
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double)0.3F);
            java.util.Optional<Vec3d> optional = rayTrace(axisalignedbb, p_221269_2_, p_221269_3_);
            if (optional.isPresent()) {
                double d1 = p_221269_2_.squareDistanceTo(optional.get());
                if (d1 < d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }

        if (entity == null) {
            return null;
        } else {
            return new RayTraceResult(entity);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("life", (short)this.ticksInGround);
        if (this.inBlockState != null) {
            compound.setTag("inBlockState", NBTUtil.writeBlockState(new NBTTagCompound(), this.inBlockState));
        }

        compound.setByte("shake", (byte)this.arrowShake);
        compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        compound.setByte("pickup", (byte)this.pickupStatus.ordinal());
        compound.setDouble("damage", this.damage);
        compound.setBoolean("crit", this.getIsCritical());
        compound.setByte("PierceLevel", this.func_213874_s());
        if (this.shootingEntity != null) {
            compound.setUniqueId("OwnerUUID", this.shootingEntity);
        }

        compound.setBoolean("ShotFromCrossbow", this.func_213873_r());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.ticksInGround = compound.getShort("life");
        if (compound.hasKey("inBlockState", 10)) {
            this.inBlockState = NBTUtil.readBlockState(compound.getCompoundTag("inBlockState"));
        }

        this.arrowShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;
        if (compound.hasKey("damage", 99)) {
            this.damage = compound.getDouble("damage");
        }

        if (compound.hasKey("pickup", 99)) {
            this.pickupStatus = PickupStatus.getByOrdinal(compound.getByte("pickup"));
        } else if (compound.hasKey("player", 99)) {
            this.pickupStatus = compound.getBoolean("player") ? PickupStatus.ALLOWED : PickupStatus.DISALLOWED;
        }

        this.setIsCritical(compound.getBoolean("crit"));
        this.func_213872_b(compound.getByte("PierceLevel"));
        if (compound.hasUniqueId("OwnerUUID")) {
            this.shootingEntity = compound.getUniqueId("OwnerUUID");
        }

        this.func_213865_o(compound.getBoolean("ShotFromCrossbow"));
    }

    private void setShooter(EntityLivingBase shooter) {
        this.shootingEntity = shooter == null ? null : shooter.getUniqueID();
        if (shooter instanceof EntityPlayer) {
            this.pickupStatus = ((EntityPlayer)shooter).capabilities.isCreativeMode ? PickupStatus.CREATIVE_ONLY : PickupStatus.ALLOWED;
        }
    }

    public Entity getShooter() {
        return this.shootingEntity != null && this.world instanceof WorldServer ? ((WorldServer)this.world).getEntityFromUuid(this.shootingEntity) : null;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.world.isRemote && (this.inGround || this.func_203047_q()) && this.arrowShake <= 0) {
            boolean flag = this.pickupStatus == PickupStatus.ALLOWED || this.pickupStatus == PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode || this.func_203047_q() && this.getShooter().getUniqueID() == entityIn.getUniqueID();
            if (this.pickupStatus == PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack())) {
                flag = false;
            }

            if (flag) {
                entityIn.onItemPickup(this, 1);
                this.setDead();
            }

        }
    }

    protected abstract ItemStack getArrowStack();

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public void setDamage(double damageIn) {
        this.damage = damageIn;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return 0.0F;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public void setIsCritical(boolean critical) {
        this.func_203049_a(1, critical);
    }

    public void func_213872_b(byte p_213872_1_) {
        this.dataManager.set(PIERCING_LEVEL, p_213872_1_);
    }

    private void func_203049_a(int p_203049_1_, boolean p_203049_2_) {
        byte b0 = this.dataManager.get(CRITICAL);
        if (p_203049_2_) {
            this.dataManager.set(CRITICAL, (byte)(b0 | p_203049_1_));
        } else {
            this.dataManager.set(CRITICAL, (byte)(b0 & ~p_203049_1_));
        }

    }

    public boolean getIsCritical() {
        byte b0 = this.dataManager.get(CRITICAL);
        return (b0 & 1) != 0;
    }

    public boolean func_213873_r() {
        byte b0 = this.dataManager.get(CRITICAL);
        return (b0 & 4) != 0;
    }

    public byte func_213874_s() {
        return this.dataManager.get(PIERCING_LEVEL);
    }

    public void setEnchantmentEffectsFromEntity(EntityLivingBase p_190547_1_, float p_190547_2_) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, p_190547_1_);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, p_190547_1_);
        this.setDamage((double)(p_190547_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.world.getDifficulty().getDifficultyId() * 0.11F));
        if (i > 0) {
            this.setDamage(this.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0) {
            this.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, p_190547_1_) > 0) {
            this.setFire(100);
        }

    }

    protected float getWaterDrag() {
        return 0.6F;
    }

    public void func_203045_n(boolean noClip) {
        this.noClip = noClip;
        this.func_203049_a(2, noClip);
    }

    public boolean func_203047_q() {
        if (!this.world.isRemote) {
            return this.noClip;
        } else {
            return (this.dataManager.get(CRITICAL) & 2) != 0;
        }
    }

    public void func_213865_o(boolean p_213865_1_) {
        this.func_203049_a(4, p_213865_1_);
    }

    protected static boolean isSpectatorEntity(Entity entity) {
        if(entity instanceof EntityPlayer) {
            return ((EntityPlayer) entity).isSpectator();
        } else {
            return false;
        }
    }

    private double func_213296_b(Vec3d vec) {
        return vec.x * vec.x + vec.z * vec.z;
    }

    protected Vec3d getMotion() {
        return new Vec3d(motionX, motionY, motionZ);
    }

    protected void setMotion(double x, double y, double z) {
        motionX = x;
        motionY = y;
        motionZ = z;
    }

    protected void setMotion(Vec3d vec3d) {
        motionX = vec3d.x;
        motionY = vec3d.y;
        motionZ = vec3d.z;
    }

    protected static float lerp(float slide, float lowerBnd, float upperBnd) {
        return lowerBnd + slide * (upperBnd - lowerBnd);
    }

    protected static Vec3d multiplyVec(Vec3d vec, double x, double y, double z) {
        return new Vec3d(vec.x * x, vec.y * y, vec.z * z);
    }

    public static java.util.Optional<Vec3d> rayTrace(AxisAlignedBB box, Vec3d start, Vec3d end) {
        double[] doubles = new double[]{1.0D};
        double x = end.x - start.x;
        double y = end.y - start.y;
        double z = end.z - start.z;
        EnumFacing direction = getVec3dFacing(box, start, doubles, null, x, y, z);
        if (direction == null) {
            return java.util.Optional.empty();
        } else {
            double d3 = doubles[0];
            return java.util.Optional.of(start.addVector(d3 * x, d3 * y, d3 * z));
        }
    }

    private static EnumFacing getVec3dFacing(AxisAlignedBB aabb, Vec3d vec3d, double[] doubles, EnumFacing facing, double x, double y, double z) {
        if (x > 1.0E-7D) {
            facing = func_197740_a(doubles, facing, x, y, z, aabb.minX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ, EnumFacing.WEST, vec3d.x, vec3d.y, vec3d.z);
        } else if (x < -1.0E-7D) {
            facing = func_197740_a(doubles, facing, x, y, z, aabb.maxX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ, EnumFacing.EAST, vec3d.x, vec3d.y, vec3d.z);
        }

        if (y > 1.0E-7D) {
            facing = func_197740_a(doubles, facing, y, z, x, aabb.minY, aabb.minZ, aabb.maxZ, aabb.minX, aabb.maxX, EnumFacing.DOWN, vec3d.y, vec3d.z, vec3d.x);
        } else if (y < -1.0E-7D) {
            facing = func_197740_a(doubles, facing, y, z, x, aabb.maxY, aabb.minZ, aabb.maxZ, aabb.minX, aabb.maxX, EnumFacing.UP, vec3d.y, vec3d.z, vec3d.x);
        }

        if (z > 1.0E-7D) {
            facing = func_197740_a(doubles, facing, z, x, y, aabb.minZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY, EnumFacing.NORTH, vec3d.z, vec3d.x, vec3d.y);
        } else if (z < -1.0E-7D) {
            facing = func_197740_a(doubles, facing, z, x, y, aabb.maxZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY, EnumFacing.SOUTH, vec3d.z, vec3d.x, vec3d.y);
        }

        return facing;
    }

    private static EnumFacing func_197740_a(double[] p_197740_0_, EnumFacing facing, double x, double y, double z, double p_197740_8_, double p_197740_10_, double p_197740_12_, double p_197740_14_, double p_197740_16_, EnumFacing p_197740_18_, double p_197740_19_, double p_197740_21_, double p_197740_23_) {
        double d0 = (p_197740_8_ - p_197740_19_) / x;
        double d1 = p_197740_21_ + d0 * y;
        double d2 = p_197740_23_ + d0 * z;
        if (0.0D < d0 && d0 < p_197740_0_[0] && p_197740_10_ - 1.0E-7D < d1 && d1 < p_197740_12_ + 1.0E-7D && p_197740_14_ - 1.0E-7D < d2 && d2 < p_197740_16_ + 1.0E-7D) {
            p_197740_0_[0] = d0;
            return p_197740_18_;
        } else {
            return facing;
        }
    }
}