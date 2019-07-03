package com.herobrine.future.tile;

import com.herobrine.future.tile.advancedfurnace.ContainerFurnaceAdvanced;
import com.herobrine.future.tile.advancedfurnace.GuiFurnaceAdvanced;
import com.herobrine.future.tile.advancedfurnace.TileFurnaceAdvanced;
import com.herobrine.future.tile.barrel.ContainerBarrel;
import com.herobrine.future.tile.barrel.GuiBarrel;
import com.herobrine.future.tile.barrel.TileBarrel;
import com.herobrine.future.tile.grindstone.ContainerGrindstone;
import com.herobrine.future.tile.grindstone.GuiGrindstone;
import com.herobrine.future.tile.stonecutter.ContainerStonecutter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_BARREL = 1;
    public static final int GUI_FURNACE = 2;
    public static final int GUI_GRINDSTONE = 3;
    public static final int GUI_STONECUTTER = 4;
    //public static final int GUI_LECTERN = 5;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_BARREL: {
                return new ContainerBarrel(player.inventory, (TileBarrel) te);
            }
            case GUI_FURNACE: {
                return new ContainerFurnaceAdvanced(player.inventory, (TileFurnaceAdvanced) te);
            }
            case GUI_GRINDSTONE: {
                return new ContainerGrindstone(player.inventory, world, pos);
            }
            case GUI_STONECUTTER: {
                return new ContainerStonecutter(player.inventory, world, pos);
            }
            default: return null;

        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_BARREL: {
                return new GuiBarrel(new ContainerBarrel(player.inventory, (TileBarrel) te));
            }
            case GUI_FURNACE: {
                return new GuiFurnaceAdvanced(player.inventory, (TileFurnaceAdvanced) te);
            }
            case GUI_GRINDSTONE: {
                return new GuiGrindstone(new ContainerGrindstone(player.inventory, world, pos));
            }
            /*case GUI_STONECUTTER: {
                return new GuiStonecutter(new ContainerStonecutter(player.inventory, world, pos));
            }*/
            default: return null;
        }
    }
}