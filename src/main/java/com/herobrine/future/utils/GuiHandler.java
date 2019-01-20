package com.herobrine.future.utils;

import com.herobrine.future.blocks.tile.ContainerBarrel;
import com.herobrine.future.blocks.tile.GuiBarrel;
import com.herobrine.future.blocks.tile.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_ID = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (ID == GUI_ID) {
            return new ContainerBarrel(player.inventory, (TileEntityBarrel) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (ID == GUI_ID) {
            TileEntityBarrel containerTileEntity = (TileEntityBarrel) te;
            return new GuiBarrel(containerTileEntity, new ContainerBarrel(player.inventory, containerTileEntity));
        }
        return null;
    }
}