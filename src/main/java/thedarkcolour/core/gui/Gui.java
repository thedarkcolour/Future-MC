package thedarkcolour.core.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.container.*;

/**
 * @author TheDarkColour
 * <p>
 * Uses enum constants for GUIs instead of IDs.
 * Simplifies opening and adding new GUIs.
 * <p>
 * Call {@link Gui#open(EntityPlayer, World, BlockPos)} on the desired GUI to open it.
 * @see thedarkcolour.futuremc.block.BlockBarrel#onBlockActivated
 */
public enum Gui {
    BARREL(ContainerBarrel::new),
    FURNACE(ContainerFurnaceAdvanced::new),
    GRINDSTONE(ContainerGrindstone::new),
    STONECUTTER(ContainerStonecutter::new),
    LOOM(ContainerLoom::new),
    SMITHING_TABLE(ContainerSmithingTable::new),
    CARTOGRAPHY_TABLE(ContainerCartographyTable::new);

    private final ContainerSupplier<? extends ContainerBase> container;

    <T extends ContainerBase> Gui(ContainerSupplier<T> container) {
        this.container = container;
    }

    <T extends ContainerBase> Gui(TEContainerSupplier<T> container) {
        this.container = container;
    }

    public GuiContainer getGui(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        return getContainer(playerInv, worldIn, pos).getGuiContainer();
    }

    public GuiContainer getGui(InventoryPlayer playerInv, TileEntity te) {
        return getContainer(playerInv, te).getGuiContainer();
    }

    public ContainerBase getContainer(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        return container.get(playerInv, worldIn, pos);
    }

    public ContainerBase getContainer(InventoryPlayer playerInv, TileEntity te) {
        return container.get(playerInv, te);
    }

    public boolean isTile() {
        return container instanceof TEContainerSupplier<?>;
    }

    public void open(EntityPlayer playerIn, World worldIn, BlockPos pos) {
        playerIn.openGui(FutureMC.INSTANCE, ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    private static class Handler implements IGuiHandler {
        @Override
        public ContainerBase getServerGuiElement(int ID, EntityPlayer player, World worldIn, int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            Gui gui = values()[ID];

            if (gui.isTile()) {
                return gui.getContainer(player.inventory, worldIn.getTileEntity(pos));
            } else {
                return gui.getContainer(player.inventory, worldIn, pos);
            }
        }

        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World worldIn, int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            Gui gui = values()[ID];

            if (gui.isTile()) {
                return gui.getGui(player.inventory, worldIn.getTileEntity(pos));
            } else {
                return gui.getGui(player.inventory, worldIn, pos);
            }
        }
    }

    public static void registerGuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(FutureMC.INSTANCE, new Handler());
    }

    @FunctionalInterface
    private interface ContainerSupplier<T extends ContainerBase> {
        default T get(InventoryPlayer player, TileEntity te) {
            return null;
        }

        T get(InventoryPlayer player, World worldIn, BlockPos pos);
    }

    @FunctionalInterface
    private interface TEContainerSupplier<T extends ContainerBase> extends ContainerSupplier<T> {
        @Override
        default T get(InventoryPlayer player, World worldIn, BlockPos pos) {
            return null;
        }

        @Override
        T get(InventoryPlayer player, TileEntity te);
    }
}