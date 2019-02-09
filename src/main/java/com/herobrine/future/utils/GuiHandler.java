package com.herobrine.future.utils;

import com.herobrine.future.blocks.tile.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_ID = 1;
    public static final int GUI_STONECUTTER = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.func_175625_s(pos);
        if (ID == GUI_ID) return new ContainerBarrel(player.field_71071_by, (TileEntityBarrel) te);
        if (ID == GUI_STONECUTTER) return new ContainerStonecutter(player.field_71071_by, te.func_145831_w(), te.func_174877_v());
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.func_175625_s(pos);
        if (ID == GUI_ID) {
            TileEntityBarrel containerTileEntity = (TileEntityBarrel) te;
            return new GuiBarrel(containerTileEntity, new ContainerBarrel(player.field_71071_by, containerTileEntity));
        }
        if (ID == GUI_STONECUTTER) {
            TileEntityStonecutter containerTileEntity = (TileEntityStonecutter) te;
            return new GuiStonecutter(containerTileEntity, new ContainerStonecutter(player.field_71071_by, te.func_145831_w(), te.func_174877_v()));
        }
        return null;
    }
}
