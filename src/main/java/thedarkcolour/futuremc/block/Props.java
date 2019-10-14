package thedarkcolour.futuremc.block;

import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;

public class Props {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty HONEY_LEVEL = IntegerProperty.create("honey_level", 0, 5);
}