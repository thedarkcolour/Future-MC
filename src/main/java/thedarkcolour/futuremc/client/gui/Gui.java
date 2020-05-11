package thedarkcolour.futuremc.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import thedarkcolour.core.gui.FContainer;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.container.*;

/**
 * @author TheDarkColour
 * <p>
 * Uses enum constants for GUIs instead of IDs.
 * Simplifies opening and adding new GUIs.
 * <p>
 * Call {@link Gui#open(EntityPlayer, World, BlockPos)} on the enum to open its associated container/gui.
 * @see thedarkcolour.futuremc.block.BarrelBlock#onBlockActivated
 */
public enum Gui {
    BARREL(ContainerBarrel::new),
    FURNACE(ContainerFurnaceAdvanced::new),
    GRINDSTONE(ContainerGrindstone::new),
    STONECUTTER(StonecutterContainer::new),
    LOOM(ContainerLoom::new),
    SMITHING_TABLE(SmithingContainer::new),
    CARTOGRAPHY_TABLE(ContainerCartographyTable::new);

    private final ContainerSupplier<? extends FContainer> container;

    <T extends FContainer> Gui(ContainerSupplier<T> container) {
        this.container = container;
    }

    <T extends FContainer> Gui(TEContainerSupplier<T> container) {
        this.container = container;
    }

    public GuiContainer getGui(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        return getContainer(playerInv, worldIn, pos).getGuiContainer();
    }

    public GuiContainer getGui(InventoryPlayer playerInv, TileEntity te) {
        return getContainer(playerInv, te).getGuiContainer();
    }

    public FContainer getContainer(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        return container.get(playerInv, worldIn, pos);
    }

    public FContainer getContainer(InventoryPlayer playerInv, TileEntity te) {
        return container.get(playerInv, te);
    }

    public boolean isTile() {
        return container instanceof TEContainerSupplier<?>;
    }

    public boolean open(EntityPlayer playerIn, World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            playerIn.openGui(FutureMC.INSTANCE, ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    private static final class Handler implements IGuiHandler {
        private static final Handler INSTANCE = new Handler();

        private Handler() {
        }

        @Override
        public FContainer getServerGuiElement(int ID, EntityPlayer player, World worldIn, int x, int y, int z) {
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
        NetworkRegistry.INSTANCE.registerGuiHandler(FutureMC.INSTANCE, Handler.INSTANCE);
    }

    @FunctionalInterface
    private interface ContainerSupplier<T extends FContainer> {
        default T get(InventoryPlayer player, TileEntity te) {
            return null;
        }

        T get(InventoryPlayer player, World worldIn, BlockPos pos);
    }

    @FunctionalInterface
    private interface TEContainerSupplier<T extends FContainer> extends ContainerSupplier<T> {
        @Override
        default T get(InventoryPlayer player, World worldIn, BlockPos pos) {
            return null;
        }

        @Override
        T get(InventoryPlayer player, TileEntity te);
    }
}