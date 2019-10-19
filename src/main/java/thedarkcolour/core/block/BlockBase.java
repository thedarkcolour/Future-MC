package thedarkcolour.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import thedarkcolour.futuremc.FutureMC;

public class BlockBase extends Block {
    public BlockBase(String regName) {
        this(regName, Material.ROCK);
    }

    public BlockBase(String regName, Material material) {
        this(regName, material, SoundType.STONE);
    }

    public BlockBase(String regName, Material material, SoundType soundType) {
        super(material);
        setTranslationKey(FutureMC.ID + "." + regName);
        setRegistryName(regName);
        setSoundType(soundType);
        setHardness(3.0F);
    }

    public BlockBase(String regName, Material material, MapColor color, SoundType soundType) {
        super(material, color);
        setTranslationKey(FutureMC.ID + "." + regName);
        setRegistryName(regName);
        setSoundType(soundType);
        setHardness(3.0F);
    }

    public static AxisAlignedBB makeAABB(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        return new AxisAlignedBB(startX / 16D, startY / 16D, startZ / 16D, endX / 16D, endY / 16D, endZ / 16D);
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    public BlockBase setHardness(float hardness) {
        super.setHardness(hardness);
        return this;
    }
}