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
 * Uses enum constants for GUIs instead of IDs.
 * Simplifies opening and adding new GUIs.
 * Sadly there's no elegant way to redo this class in Kotlin.
 *
 * @author TheDarkColour
 *
 * @see thedarkcolour.futuremc.block.BarrelBlock#onBlockActivated
 */
public enum GuiType {
    BARREL(ContainerBarrel::new),
    FURNACE(ContainerFurnaceAdvanced::new),
    GRINDSTONE(ContainerGrindstone::new),
    STONECUTTER(StonecutterContainer::new),
    LOOM(ContainerLoom::new),
    SMITHING_TABLE(SmithingContainer::new),
    CARTOGRAPHY_TABLE(ContainerCartographyTable::new);

    /**
     * The container factory of this {@link GuiType}
     */
    private final ContainerFactory container;

    /**
     * Creates a {@link GuiType} for a block without a tile entity.
     *
     * @param container The container factory (and indirectly the gui factory)
     */
    GuiType(ContainerFactory container) {
        this.container = container;
    }

    /**
     * Creates a {@link GuiType} for a tile entity.
     *
     * @param container The container factory (and indirectly the gui factory)
     */
    GuiType(TEContainerFactory container) {
        this.container = container;
    }

    /**
     * Used internally in the {@link Handler} for opening a gui on the client.
     *
     * @param playerInv the player to open the gui for
     * @param worldIn the world
     * @param pos the position of this block or tile entity
     * @return a new GuiContainer for this {@link GuiType}
     */
    private GuiContainer getGui(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        return getContainer(playerInv, worldIn, pos).getGuiContainer();
    }

    /**
     * Used internally in the {@link Handler} for opening a gui on the client.
     *
     * @param playerInv the player to open the gui for
     * @param te the TileEntity that has World and BlockPos references
     * @return a new GuiContainer for this {@link GuiType}
     */
    private GuiContainer getGui(InventoryPlayer playerInv, TileEntity te) {
        return getContainer(playerInv, te).getGuiContainer();
    }

    /**
     * Used internally in the {@link Handler} for opening a container on the server.
     *
     * @param playerInv the inventory of the player
     * @param worldIn the World
     * @param pos the position of this block or tile entity
     * @return a new FContainer for this {@link GuiType}
     */
    private FContainer getContainer(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        return container.get(playerInv, worldIn, pos);
    }

    /**
     * Used internally in the {@link Handler} for opening a container on the server.
     *
     * @param playerInv the inventory of the player
     * @param te the TileEntity that has World and BlockPos references
     * @return a new FContainer for this {@link GuiType}
     */
    private FContainer getContainer(InventoryPlayer playerInv, TileEntity te) {
        return container.get(playerInv, te);
    }

    /**
     * @return if this {@link GuiType} requires a TileEntity
     */
    public boolean isTile() {
        return container instanceof TEContainerFactory;
    }

    /**
     * Opens the gui and handles the {@code !worldIn.isRemote } check.
     * @param playerIn the player to open the gui for
     * @param worldIn the world
     * @param pos the position of the block/tile entity
     * @return whether the hand opening animation should play
     */
    public boolean open(EntityPlayer playerIn, World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            playerIn.openGui(FutureMC.INSTANCE, ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    /**
     * Singleton {@link IGuiHandler} for client-server gui opening.
     */
    private static final class Handler implements IGuiHandler {
        private static final Handler INSTANCE = new Handler();

        private Handler() {
        }

        /**
         * Returns a Server side Container to be displayed to the user.
         *
         * @param ID The Gui ID Number
         * @param playerIn The player viewing the Gui
         * @param worldIn The current world
         * @param x X Position
         * @param y Y Position
         * @param z Z Position
         * @return A GuiScreen/Container to be displayed to the user, null if none.
         */
        @Override
        public FContainer getServerGuiElement(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            GuiType guiType = values()[ID];

            if (guiType.isTile()) {
                return guiType.getContainer(playerIn.inventory, worldIn.getTileEntity(pos));
            } else {
                return guiType.getContainer(playerIn.inventory, worldIn, pos);
            }
        }

        /**
         * Returns a Container to be displayed to the user. On the client side, this
         * needs to return a instance of GuiScreen On the server side, this needs to
         * return a instance of Container
         *
         * @param ID The Gui ID Number
         * @param playerIn The player viewing the Gui
         * @param worldIn The current world
         * @param x X Position
         * @param y Y Position
         * @param z Z Position
         * @return A GuiScreen/Container to be displayed to the user, null if none.
         */
        @Override
        public Object getClientGuiElement(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            GuiType guiType = values()[ID];

            if (guiType.isTile()) {
                return guiType.getGui(playerIn.inventory, worldIn.getTileEntity(pos));
            } else {
                return guiType.getGui(playerIn.inventory, worldIn, pos);
            }
        }
    }

    /**
     * Registers the internal IGuiHandler to the Forge network registry.
     */
    public static void registerGuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(FutureMC.INSTANCE, Handler.INSTANCE);
    }

    /**
     * Common ContainerFactory class that by default does not require a TileEntity.
     */
    @FunctionalInterface
    private interface ContainerFactory {
        /**
         * Creates a new {@link FContainer} for a TileEntity.
         *
         * @param player the player
         * @param te the TileEntity to create a new container with.
         * @return a new {@link FContainer} for a TileEntity or {@code null} if this {@link ContainerFactory} is NOT for a TileEntity
         */
        default FContainer get(InventoryPlayer player, TileEntity te) {
            return null;
        }

        /**
         * Creates a new {@link FContainer} for a block that does not require a TileEntity.
         *
         * @param player the player
         * @param worldIn the World
         * @param pos the position of this block or tile entity
         * @return {@code null} if this {@link ContainerFactory} is for a TileEntity or a new {@link FContainer}
         */
        FContainer get(InventoryPlayer player, World worldIn, BlockPos pos);
    }

    /**
     * Container supplier that requires a TileEntity.
     */
    @FunctionalInterface
    private interface TEContainerFactory extends ContainerFactory {
        @Override
        default FContainer get(InventoryPlayer player, World worldIn, BlockPos pos) {
            return null;
        }

        @Override
        FContainer get(InventoryPlayer player, TileEntity te);
    }
}