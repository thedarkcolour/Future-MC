package com.herobrine.future.blocks;

import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.Init;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class FlowerBlack extends BlockBush { //Adds black flower
    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);
    public FlowerBlack() {
        super(Material.field_151585_k);
        setRegistryName("FlowerBlack");
        func_149663_c(Init.MODID + ".FlowerBlack");
        func_149647_a(Init.futuretab);
        func_149672_a(SoundType.field_185850_c);
        func_180632_j(func_176194_O().func_177621_b());
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public void func_180655_c(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.func_177958_n() + rand.nextDouble() * 0.5D + 0.2D;
        double d1 = (double)pos.func_177956_o() + rand.nextDouble() * 0.3D + 0.2D;
        double d2 = (double)pos.func_177952_p() + rand.nextDouble() * 0.5D + 0.2D;
        worldIn.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if(Config.drose) {
            if (entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase) entityIn).func_70690_d(new PotionEffect(MobEffects.field_82731_v, 40));
                entityIn.func_70097_a(DamageSource.field_82727_n, 1.0F);
            }
        }
    }

    @Override
    public boolean func_176196_c(World worldIn, BlockPos pos) {
        return super.func_176196_c(worldIn, pos);
    }

    @Override
    protected boolean func_185514_i(IBlockState state) {
        return state.func_177230_c() == Blocks.field_150425_aM;
    }
}
