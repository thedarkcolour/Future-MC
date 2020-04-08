package thedarkcolour.core.gui

import net.minecraft.client.gui.inventory.GuiContainer

abstract class GuiContainer<T : ContainerBase?>(protected var container: T) :
    GuiContainer(container)