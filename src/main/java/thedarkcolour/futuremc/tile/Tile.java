package thedarkcolour.futuremc.tile;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import thedarkcolour.futuremc.FutureMC;

@ObjectHolder(FutureMC.ID)
public class Tile extends net.minecraft.tileentity.TileEntity {
    public static final TileEntityType<?> BEEHIVE = null;

    public Tile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
}