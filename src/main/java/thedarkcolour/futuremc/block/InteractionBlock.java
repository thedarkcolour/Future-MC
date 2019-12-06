package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thedarkcolour.futuremc.tile.InteractionTileEntity;

import javax.annotation.Nonnull;

public abstract class InteractionBlock extends Block {
    public InteractionBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.getTileEntity(pos) instanceof InteractionTileEntity) {
            return ((InteractionTileEntity) worldIn.getTileEntity(pos)).activated(state, player, handIn, hit);
        }
        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (worldIn.getTileEntity(pos) instanceof InteractionTileEntity) {
            ((InteractionTileEntity) worldIn.getTileEntity(pos)).broken(state, player, player.getHeldItemMainhand());
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity te = worldIn.getTileEntity(pos);
        return te != null && te.receiveClientEvent(id, param);
    }
}