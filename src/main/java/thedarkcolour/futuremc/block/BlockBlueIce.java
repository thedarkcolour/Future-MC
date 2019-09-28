package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;

import java.util.Random;

public class BlockBlueIce extends Block {
    public BlockBlueIce() {
        super(Material.PACKED_ICE);
        setTranslationKey(FutureMC.ID + ".BlueIce");
        setRegistryName("blue_ice");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : FutureMC.TAB);
        setDefaultSlipperiness(0.989F);
        setHardness(2.8F);
        setSoundType(SoundType.GLASS);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public String getHarvestTool(IBlockState state) {
        return "pickaxe";
    }
}