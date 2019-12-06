package thedarkcolour.futuremc.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;

public class InteractionTileEntity extends TileEntity {
    public InteractionTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public boolean activated(BlockState state, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return false;
    }

    public void broken(BlockState state, PlayerEntity player, ItemStack stack) {

    }
}