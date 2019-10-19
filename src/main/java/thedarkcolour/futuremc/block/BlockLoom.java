package thedarkcolour.futuremc.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thedarkcolour.core.block.BlockRotatable;
import thedarkcolour.core.gui.Gui;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;

public class BlockLoom extends BlockRotatable {
    public BlockLoom() {
        super("Loom", Material.WOOD, SoundType.WOOD);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (FutureMC.DEBUG && !worldIn.isRemote) {
            Gui.LOOM.open(playerIn, worldIn, pos);
            return true;
        }
        return false;
    }
}