package com.herobrine.future.block;

import com.herobrine.future.FutureMC;
import com.herobrine.future.init.FutureConfig;
import com.herobrine.future.init.Init;
import com.herobrine.future.client.gui.GuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLoom extends BlockRotatable {
    public BlockLoom() {
        super(new BlockProperties("Loom", Material.WOOD, SoundType.WOOD));
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.CREATIVE_TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(Init.isDebug) {
            if(!worldIn.isRemote) {
                playerIn.openGui(FutureMC.instance, GuiHandler.GUI_LOOM, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
        return false;
    }
}