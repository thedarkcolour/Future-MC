package com.herobrine.future.client.gui;

import com.herobrine.future.container.ContainerBarrel;
import com.herobrine.future.container.ContainerFurnaceAdvanced;
import com.herobrine.future.container.ContainerGrindstone;
import com.herobrine.future.container.ContainerLoom;
import com.herobrine.future.container.ContainerStonecutter;
import com.herobrine.future.tile.TileBarrel;
import com.herobrine.future.tile.TileFurnaceAdvanced;
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
    public static final int GUI_LOOM = 5;
    //public static final int GUI_LECTERN = 5;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World worldIn, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = worldIn.getTileEntity(pos);
        switch (ID) {
            case GUI_BARREL: {
                return new ContainerBarrel(player.inventory, (TileBarrel) te);
            }
            case GUI_FURNACE: {
                return new ContainerFurnaceAdvanced(player.inventory, (TileFurnaceAdvanced) te);
            }
            case GUI_GRINDSTONE: {
                return new ContainerGrindstone(player.inventory, worldIn, pos);
            }
            case GUI_STONECUTTER: {
                return new ContainerStonecutter(player.inventory, worldIn, pos);
            }
            case GUI_LOOM: {
                return new ContainerLoom(player.inventory, worldIn, pos);
            }
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World worldIn, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = worldIn.getTileEntity(pos);
        switch (ID) {
            case GUI_BARREL: {
                return new GuiBarrel(new ContainerBarrel(player.inventory, (TileBarrel) te));
            }
            case GUI_FURNACE: {
                return new GuiFurnaceAdvanced(player.inventory, (TileFurnaceAdvanced) te);
            }
            case GUI_GRINDSTONE: {
                return new GuiGrindstone(new ContainerGrindstone(player.inventory, worldIn, pos));
            }
            case GUI_STONECUTTER: {
                return new GuiStonecutter(new ContainerStonecutter(player.inventory, worldIn, pos));
            }
            case GUI_LOOM: {
                return new GuiLoom(new ContainerLoom(player.inventory, worldIn, pos));
            }
            default: return null;
        }
    }
}