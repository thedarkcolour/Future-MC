package com.herobrine.future.blocks;

import com.herobrine.future.utils.futureItems;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class flowerblack extends BlockBush { //Adds black flower
    public flowerblack() {
        super(Material.PLANTS);
        setRegistryName("flowerblack");
        setUnlocalizedName(futureItems.MODID + ".flowerblack");
        setCreativeTab(futureItems.futuretab);
        setSoundType(SoundType.PLANT);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT) //Particle
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
            double d0 = (double)pos.getX() + rand.nextDouble() * 0.5D + 0.2D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 0.3D + 0.2D;
            double d2 = (double)pos.getZ() + rand.nextDouble() * 0.5D + 0.2D;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) { {
            if (entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 40));
                entityIn.attackEntityFrom(DamageSource.WITHER, 1.0F);

            }
        }
    }
}
