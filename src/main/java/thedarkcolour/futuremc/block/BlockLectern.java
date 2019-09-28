package thedarkcolour.futuremc.block;

import io.netty.buffer.Unpooled;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.client.gui.GuiLectern;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.tile.TileLectern;

public class BlockLectern extends BlockRotatable implements ITileEntityProvider {
    public BlockLectern() {
        super("Lectern", Material.WOOD);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.REDSTONE : FutureMC.TAB);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileLectern();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileLectern) {
            TileLectern lectern = (TileLectern) tile;
            if (!lectern.getBook().isEmpty()) {
                openForPlayer(playerIn, hand, lectern.getBook());
                return true;
            }
        }
        Item item = playerIn.getHeldItem(hand).getItem();
        if (item == Items.WRITABLE_BOOK || item == Items.WRITTEN_BOOK) {
            playerIn.getHeldItem(hand).shrink(1);
        }
        return false;
    }

    public static void openForPlayer(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if(player instanceof EntityPlayerMP) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeEnumValue(hand);
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomPayload("MC|BOpen", packetbuffer));
        }
        else if(player instanceof EntityPlayerSP) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiLectern(player, stack));
        }
    }
}