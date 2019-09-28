package thedarkcolour.core.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.container.ContainerBarrel;
import thedarkcolour.futuremc.container.ContainerFurnaceAdvanced;
import thedarkcolour.futuremc.container.ContainerGrindstone;
import thedarkcolour.futuremc.container.ContainerLoom;
import thedarkcolour.futuremc.container.ContainerStonecutter;

/**
 * @author TheDarkColour
 *
 * Uses enum constants for GUIs instead of IDs.
 * Simplifies opening and adding new GUIs.
 *
 * Call {@link Gui#open(EntityPlayer, World, BlockPos)} on the desired GUI to open it.
 *
 * @see thedarkcolour.futuremc.block.BlockBarrel#onBlockActivated
 */
public enum Gui {
    BARREL         (ContainerBarrel::new            ),
    FURNACE        (ContainerFurnaceAdvanced::new   ),
    GRINDSTONE     (ContainerGrindstone::new        ),
    STONECUTTER    (ContainerStonecutter::new       ),
    LOOM           (ContainerLoom::new              );

    private final ContainerSupplier<? extends Container> container;

    <T extends Container> Gui(ContainerSupplier<T> container) {
        this.container = container;
    }

    <T extends Container> Gui(TEContainerSupplier<T> container) {
        this.container = container;
    }

    @SideOnly(Side.CLIENT)
    public GuiContainer getGui(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        return getContainer(playerInv, worldIn, pos).getGuiContainer();
    }

    @SideOnly(Side.CLIENT)
    public GuiContainer getGui(InventoryPlayer playerInv, TileEntity te) {
        return getContainer(playerInv, te).getGuiContainer();
    }

    public Container getContainer(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        return container.get(playerInv, worldIn, pos);
    }

    public Container getContainer(InventoryPlayer playerInv, TileEntity te) {
        return container.get(playerInv, te);
    }

    public boolean isTile() {
        return container instanceof TEContainerSupplier<?>;
    }

    public void open(EntityPlayer playerIn, World worldIn, BlockPos pos) {
        playerIn.openGui(FutureMC.instance, ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    private static class Handler implements IGuiHandler {
        @Override
        public Container getServerGuiElement(int ID, EntityPlayer player, World worldIn, int x, int y, int z) {
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

    public static void setup() {
        NetworkRegistry.INSTANCE.registerGuiHandler(FutureMC.instance, new Handler());
    }

    @FunctionalInterface
    private interface ContainerSupplier<T extends Container> {
        default T get(InventoryPlayer player, TileEntity te) {
            return null;
        }

        T get(InventoryPlayer player, World worldIn, BlockPos pos);
    }

    @FunctionalInterface
    private interface TEContainerSupplier<T extends Container> extends ContainerSupplier<T> {
        @Override
        default T get(InventoryPlayer player, World worldIn, BlockPos pos) {
            return null;
        }

        @Override
        T get(InventoryPlayer player, TileEntity te);
    }
}