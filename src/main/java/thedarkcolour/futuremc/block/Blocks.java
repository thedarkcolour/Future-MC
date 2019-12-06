package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;

public class Blocks extends net.minecraft.block.Blocks {
    public static final Block BEE_NEST = new BeeHiveBlock(true);
    public static final Block BEEHIVE = new BeeHiveBlock(false);
    public static final Block HONEY_BLOCK = new HoneyBlockBlock();
    public static final Block HONEYCOMB_BLOCK = new Block(Block.Properties.create(Material.CLAY, DyeColor.ORANGE).sound(SoundType.CORAL).hardnessAndResistance(0.6F)).setRegistryName("honeycomb_block");
}