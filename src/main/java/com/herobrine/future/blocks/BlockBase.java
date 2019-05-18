package com.herobrine.future.blocks;

import com.herobrine.future.init.Init;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class BlockBase extends Block {
    public BlockBase(BlockProperties properties) {
        super(properties.material());
        setUnlocalizedName(Init.MODID + "." + properties.registryName());
        setRegistryName(properties.registryName());
        setSoundType(properties.soundType());
        setHardness(3.0F);
    }

    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
    }

    /**
     * Method that returns a proper AABB from block pixel coordinates. Makes AABBs much easier
     */
    public static AxisAlignedBB makeAABB(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        return new AxisAlignedBB(startX / 16D, startY / 16D, startZ / 16D, endX / 16D, endY / 16D, endZ / 16D);
    }

    // Reminder - Block destroy breaking happens at Block.harvestBlock and uses Block.spawnAsEntity(world, pos, stack) to spawn drops
}