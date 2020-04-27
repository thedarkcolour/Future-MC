package thedarkcolour.futuremc.block

import net.minecraft.util.Direction
import thedarkcolour.futuremc.registry.FBlocks

class WeepingVinesBlock(properties: Properties) : VinesBlock(Direction.DOWN, makeCuboidShape(4.0, 9.0, 4.0, 12.0, 16.0, 12.0), FBlocks::WEEPING_VINES_PLANT, properties)
class WeepingVinesPlantBlock(properties: Properties) : VinesPlantBlock(makeCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0), FBlocks::WEEPING_VINES, properties)
class TwistingVinesBlock(properties: Properties) : VinesBlock(Direction.UP, makeCuboidShape(4.0, 0.0, 4.0, 12.0, 15.0, 12.0), FBlocks::TWISTING_VINES_PLANT, properties)
class TwistingVinesPlantBlock(properties: Properties) : VinesPlantBlock(makeCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0), FBlocks::TWISTING_VINES, properties)