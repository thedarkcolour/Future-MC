package thedarkcolour.core.block;

import com.herobrine.future.FutureMC;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockBase extends Block {
    public BlockBase(String regName) {
        this(regName, Material.ROCK);
    }

    public BlockBase(String regName, Material material) {
        this(regName, material, SoundType.STONE);
    }

    public BlockBase(String regName, Material material, SoundType soundType) {
        super(material);
        setUnlocalizedName(FutureMC.ID + "." + regName);
        setRegistryName(regName);
        setSoundType(soundType);
        setHardness(3.0F);
    }

    public static AxisAlignedBB makeAABB(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        return new AxisAlignedBB(startX / 16D, startY / 16D, startZ / 16D, endX / 16D, endY / 16D, endZ / 16D);
    }
}