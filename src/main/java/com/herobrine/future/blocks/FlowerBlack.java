package com.herobrine.future.blocks;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class FlowerBlack extends BlockFlower { //Adds black flower
    public FlowerBlack() {
        setRegistryName("FlowerBlack");
        setUnlocalizedName(Init.MODID + ".FlowerBlack");
        setCreativeTab(Init.futuretab);
        setSoundType(SoundType.PLANT);
        setDefaultState(getBlockState().getBaseState());
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + rand.nextDouble() * 0.5D + 0.2D;
        double d1 = (double)pos.getY() + rand.nextDouble() * 0.3D + 0.2D;
        double d2 = (double)pos.getZ() + rand.nextDouble() * 0.5D + 0.2D;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if(FutureConfig.b.witherrosedmg) {
            if (entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 40));
                entityIn.attackEntityFrom(DamageSource.WITHER, 1.0F);
            }
        }
    }

    @Override
    public boolean isBiomeValid(Biome biome) {
        return false;
    }

    @Override
    public boolean getSpawnChance(Random random) {
        return false;
    }

    @Override
    public boolean getChunkChance(Random random) {
        return false;
    }
}