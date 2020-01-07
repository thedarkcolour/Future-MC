package thedarkcolour.futuremc.block

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.core.gui.Gui

class UtilityBlock(gui: Gui) : RotatableBlock(gui.name.toLowerCase(), Material.WOOD, SoundType.WOOD)