package com.herobrine.future.entity.trident;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.herobrine.future.MainFuture;
import com.herobrine.future.enchantment.EnchantHelper;
import com.herobrine.future.enchantment.Enchantments;
import com.herobrine.future.sound.Sounds;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class EntityTrident extends EntityArrow {
    @SuppressWarnings({"unchecked", "Guava"})
    private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, Entity::canBeCollidedWith);
    private ItemStack thrownStack;
    private boolean hasChanneled;
    private int ticksInGround = 0;
    private boolean isReturning;

    @SuppressWarnings("unused")
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
    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity entity = raytraceResultIn.entityHit;

        if(entity != null) {
            DamageSource source;

            if(!(this.shootingEntity instanceof EntityPlayer)) {
                source = DamageSource.causeArrowDamage(this, this);
            }
            else {
                source = DamageSource.causeArrowDamage(this, shootingEntity);
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
                            if (enchants.get(Enchantments.CHANNELING) != null) {
                                shootingEntity.world.addWeatherEffect(new EntityLightningBolt(shootingEntity.world, this.posX, this.posY, this.posZ, false));
                                this.playSound(Sounds.TRIDENT_CHANNELING, 5.0F, 1.0F);
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
            this.motionX = (double)((float)(raytraceResultIn.hitVec.x - this.posX));
            this.motionY = (double)((float)(raytraceResultIn.hitVec.y - this.posY));
            this.motionZ = (double)((float)(raytraceResultIn.hitVec.z - this.posZ));
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
                this.playSound(Sounds.TRIDENT_CHANNELING, 5.0F, 1.0F);
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
                    //vec3d = vec3d.normalize();
                    this.motionX += vec3d.x * d0 - this.motionX * 0.05D;
                    this.motionY += vec3d.y * d0 - this.motionY * 0.05D;
                    this.motionZ += vec3d.z * d0 - this.motionZ * 0.05D;

                    this.move(MoverType.SELF, this.motionX * 0.05D, this.motionY * 0.05D, this.motionZ * 0.05D);
                }
            }
        }

        if(this.world.getClosestPlayerToEntity(this, 1.5D) == this.shootingEntity && isReturning) {
            if(this.pickupStatus == PickupStatus.ALLOWED) {
                dropTridentStack(0.1F);
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
            MainFuture.logger.debug("Tried to load invalid item: {}", compound, runtimeexception);
            return ItemStack.EMPTY;
        }
    }

    public ItemStack getThrownStack() {
        return thrownStack;
    }

    private float getDamageForTrident() {
        float level = EnchantHelper.getImpaling(this.thrownStack);
        float damage = 8F;

        if(level > 0) {
            damage += (level * 1.25);
        }
        return damage;
    }

    private void dropTridentStack(float offsetY) {
        if(thrownStack == null || thrownStack.isEmpty()) {
        }
        else {
            EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + (double)offsetY, this.posZ, thrownStack);
            entityitem.setNoPickupDelay();

            if (captureDrops) {
                this.capturedDrops.add(entityitem);
            }
            else {
                this.world.spawnEntity(entityitem);
            }
        }
    }
}
