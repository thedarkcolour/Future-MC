package com.herobrine.future.blocks;

import com.herobrine.future.utils.blocks.IModel;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FlowerWhite extends BlockBush implements IModel { //Adds white flower
    public FlowerWhite() {
        super(Material.PLANTS);
        setRegistryName("FlowerWhite");
        setUnlocalizedName(Init.MODID + ".FlowerWhite");
        setCreativeTab(Init.futuretab);
        setSoundType(SoundType.PLANT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void models() {
        model(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos);
    }
}