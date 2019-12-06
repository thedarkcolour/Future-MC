package thedarkcolour.futuremc.tile;

import net.minecraft.tileentity.TileEntityType;
import thedarkcolour.futuremc.block.Blocks;

//@ObjectHolder(FutureMC.ID)
public class TileEntityTypes {
    public static final TileEntityType<?> BEEHIVE = TileEntityType.Builder.create(BeeHiveTileEntity::new, Blocks.BEE_NEST, Blocks.BEEHIVE).build(null).setRegistryName("beehive");

    //public TileEntityTypes(TileEntityType<?> tileEntityTypeIn) {
    //    super(tileEntityTypeIn);
    //}
}