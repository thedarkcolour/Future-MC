package thedarkcolour.core.gui;

public abstract class GuiContainer<T extends Container> extends net.minecraft.client.gui.inventory.GuiContainer {
    protected T container;

    public GuiContainer(net.minecraft.inventory.Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    public T getContainer() {
        return container;
    }
}