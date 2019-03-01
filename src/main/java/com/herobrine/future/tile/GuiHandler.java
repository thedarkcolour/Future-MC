package com.herobrine.future.tile;

import com.herobrine.future.tile.barrel.ContainerBarrel;
import com.herobrine.future.tile.barrel.GuiBarrel;
import com.herobrine.future.tile.barrel.TileEntityBarrel;
import com.herobrine.future.tile.stonecutter.ContainerStonecutter;
import com.herobrine.future.tile.stonecutter.GuiStonecutter;
import com.herobrine.future.tile.stonecutter.TileEntityStonecutter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    private static final int GUI_ID = 1;
    private static final int GUI_STONECUTTER = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_ID:
                return new ContainerBarrel(player.inventory, (TileEntityBarrel) te);
            case GUI_STONECUTTER:
                 return new ContainerStonecutter(player.inventory, (TileEntityStonecutter) te);
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_ID:
                TileEntityBarrel barrel = (TileEntityBarrel) te;
                return new GuiBarrel(new ContainerBarrel(player.inventory, barrel));
            case GUI_STONECUTTER:
                TileEntityStonecutter stonecutter = (TileEntityStonecutter) te;
                return new GuiStonecutter(stonecutter, new ContainerStonecutter(player.inventory, stonecutter));
            default: return null;
        }
    }
}