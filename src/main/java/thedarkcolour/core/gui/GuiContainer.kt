package thedarkcolour.core.gui

import net.minecraft.client.gui.inventory.GuiContainer

// todo use for more things besides a container field
abstract class GuiContainer<T : FContainer?>(protected var container: T) : GuiContainer(container)