package com.herobrine.future.blocks;

import com.herobrine.future.utils.IModel;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public abstract class BlockFlower extends BlockBush implements IModel {
    public BlockFlower() { // Re-did flowers to allow for easier creation and better generation
        super();
    }

    @SideOnly(Side.CLIENT)
    public void models() {
        model(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        if(this == Init.flowerblack) {
            return state.getBlock() == Blocks.SOUL_SAND;
        }
        else return super.canSustainBush(state);
    }

    public abstract boolean isBiomeValid(Biome biome);

    public abstract boolean getSpawnChance(Random random);

    public abstract boolean getChunkChance(Random random);
}
