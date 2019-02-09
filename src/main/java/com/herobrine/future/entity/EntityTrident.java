package com.herobrine.future.entity;

import com.herobrine.future.utils.Init;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class EntityTrident extends EntityArrow {
    private static final DataParameter<Integer> DURABILTY = EntityDataManager.func_187226_a(EntityTrident.class, DataSerializers.field_187192_b);

    public EntityTrident(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
    }

    @Override
    public void func_70186_c(double par1, double par3, double par5, float par7, float par8) {
        float f2 = MathHelper.func_76133_a(par1*par3*par5*par7*par8);
        par1 /= f2;
        par3 /= f2;
        par5 /= f2;
        par1 += this.field_70146_Z.nextGaussian() * (this.field_70146_Z.nextBoolean() ? -1 : 1) * 0.0007499999832361937D * par8;
        par3 += this.field_70146_Z.nextGaussian() * (this.field_70146_Z.nextBoolean() ? -1 : 1) * 0.0007499999832361937D * par8;
        par5 += this.field_70146_Z.nextGaussian() * (this.field_70146_Z.nextBoolean() ? -1 : 1) * 0.0007499999832361937D * par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        this.field_70159_w = par1;
        this.field_70181_x = par3;
        this.field_70179_y = par5;
        float f3 = MathHelper.func_76133_a(par1 * par1 + par5 * par5);
        this.field_70126_B = this.field_70177_z = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.field_70127_C = this.field_70125_A = (float) (Math.atan2(par3, f3) * 180.0D / Math.PI);
    }

    @Override
    protected ItemStack func_184550_j() {
        return new ItemStack(Init.trident);
    }

    @Override
    public void func_70100_b_(EntityPlayer entityIn) {
        if(!this.field_70170_p.field_72995_K && this.field_70254_i && this.field_70249_b <= 0) {
            boolean flag = this.field_70251_a == EntityArrow.PickupStatus.ALLOWED || this.field_70251_a == EntityArrow.PickupStatus.CREATIVE_ONLY && entityIn.field_71075_bZ.field_75098_d;

            if (this.field_70251_a == EntityArrow.PickupStatus.ALLOWED && !entityIn.field_71071_by.func_70441_a(func_184550_j())) {
                flag = false;
            }

            if (flag) {
                this.func_184185_a(SoundEvents.field_187638_cR, 0.2F, ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityIn.func_71001_a(this, 1);
                this.func_70106_y();
            }
        }
    }

    @Override
    protected void func_184549_a(RayTraceResult raytraceResultIn) {

    }
}
