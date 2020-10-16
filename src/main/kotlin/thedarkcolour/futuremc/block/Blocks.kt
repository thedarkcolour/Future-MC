package thedarkcolour.futuremc.block

import net.minecraft.block.*
import net.minecraft.block.PressurePlateBlock
import net.minecraft.block.StairsBlock
import net.minecraft.block.StoneButtonBlock
import net.minecraft.block.WoodButtonBlock

class PressurePlateBlock(sensitivity: Sensitivity, properties: Properties) : PressurePlateBlock(sensitivity, properties)
class StairsBlock(block: () -> BlockState, properties: Properties) : StairsBlock(block, properties)
class StoneButtonBlock(properties: Properties) : StoneButtonBlock(properties)
class WoodButtonBlock(properties: Properties) : WoodButtonBlock(properties)