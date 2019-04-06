package com.herobrine.future.tile;
import com.herobrine.future.tile.advancedfurnace.ContainerAdvancedFurnace;
import com.herobrine.future.tile.advancedfurnace.GuiAdvancedFurnace;
import com.herobrine.future.tile.advancedfurnace.TileAdvancedFurnace;
import com.herobrine.future.tile.barrel.ContainerBarrel;
import com.herobrine.future.tile.barrel.GuiBarrel;
import com.herobrine.future.tile.barrel.TileBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_BARREL = 1;
    public static final int GUI_FURNACE = 2;
    //private static final int GUI_STONECUTTER = 2; Rip stonecutter...

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_BARREL: {
                return new ContainerBarrel(player.inventory, (TileBarrel) te);
            }
            case GUI_FURNACE: {
                return new ContainerAdvancedFurnace(player.inventory, (TileAdvancedFurnace) te);
            }
            //case GUI_STONECUTTER:
                //return new ContainerStonecutter(player.inventory, (TileStonecutter) te);
            default: {
                return null; // Should never reach this point
            }
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUI_BARREL: {
                return new GuiBarrel(player.inventory, (TileBarrel) te);
            }
            case GUI_FURNACE: {
                return new GuiAdvancedFurnace(player.inventory, new ContainerAdvancedFurnace(player.inventory, (TileAdvancedFurnace) te));
            }
            //case GUI_STONECUTTER:
               // TileEntityStonecutter stonecutter = (TileStonecutter) te;
               // return new GuiStonecutter(stonecutter, new ContainerStonecutter(player.inventory, stonecutter));
            default: return null; // Should never reach this point
        }
    }
}