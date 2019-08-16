package com.herobrine.future.block;

import com.herobrine.future.tile.InteractionTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class InteractionBlock extends BlockBase {
    public InteractionBlock(String regKey, Material material) {
        super(new BlockProperties(regKey, material));

    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public abstract TileEntity createTileEntity(World world, IBlockState state);

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.getTileEntity(pos) instanceof InteractionTile) {
            return ((InteractionTile)worldIn.getTileEntity(pos)).activated(playerIn, hand, facing, hitX, hitY, hitZ);
        }
        return false;
    }
}