package thedarkcolour.futuremc.block;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;

public abstract class BlockFlower extends BlockBush {
    public BlockFlower(String regName) {
        setTranslationKey(FutureMC.ID + "." + regName);
        setRegistryName(regName);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);
    }

    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        if (this == Init.WITHER_ROSE) {
            return state.getBlock() == Blocks.SOUL_SAND;
        } else {
            return super.canSustainBush(state);
        }
    }

    public abstract boolean isBiomeValid(Biome biome);

    public abstract int getFlowerChance();
}