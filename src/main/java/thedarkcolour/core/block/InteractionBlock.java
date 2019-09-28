package thedarkcolour.core.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thedarkcolour.core.tile.InteractionTile;

public abstract class InteractionBlock extends BlockBase {
    public InteractionBlock(String regName) {
        super(regName, Material.ROCK, SoundType.STONE);
    }

    public InteractionBlock(String regName, Material material) {
        super(regName, material, SoundType.STONE);
    }

    public InteractionBlock(String regName, Material material, SoundType soundType) {
        super(regName, material, soundType);
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
            return ((InteractionTile)worldIn.getTileEntity(pos)).activated(state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if(worldIn.getTileEntity(pos) instanceof InteractionTile) {
            ((InteractionTile)worldIn.getTileEntity(pos)).broken(state, player);
        }
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity te = worldIn.getTileEntity(pos);
        return te != null && te.receiveClientEvent(id, param);
    }
}