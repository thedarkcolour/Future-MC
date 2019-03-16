package com.herobrine.future.tile;

import com.herobrine.future.tile.advancedfurnace.ContainerAdvancedFurnace;
import com.herobrine.future.tile.advancedfurnace.GuiAdvancedFurnace;
import com.herobrine.future.tile.advancedfurnace.TileEntityAdvancedFurnace;
import com.herobrine.future.tile.barrel.ContainerBarrel;
import com.herobrine.future.tile.barrel.GuiBarrel;
import com.herobrine.future.tile.barrel.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    private static final int GUI_ID = 1;
    private static final int GUI_ADVANCED_FURNACE = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_ID:
                return new ContainerBarrel(player.inventory, (TileEntityBarrel) te);
            case GUI_ADVANCED_FURNACE:
                return new ContainerAdvancedFurnace(player.inventory, (TileEntityAdvancedFurnace) te);
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_ID:
                return new GuiBarrel(new ContainerBarrel(player.inventory, (TileEntityBarrel) te));
            case GUI_ADVANCED_FURNACE:
                return new GuiAdvancedFurnace(new ContainerAdvancedFurnace(player.inventory, (TileEntityAdvancedFurnace) te));
            default: return null;
        }
    }
}