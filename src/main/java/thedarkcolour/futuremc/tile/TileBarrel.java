package thedarkcolour.futuremc.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import thedarkcolour.core.inventory.DarkInventory;
import thedarkcolour.futuremc.block.villagepillage.BarrelBlock;
import thedarkcolour.futuremc.container.ContainerBarrel;
import thedarkcolour.futuremc.registry.FBlocks;
import thedarkcolour.futuremc.registry.FSounds;

public class TileBarrel extends TileEntity {
    private final DarkInventory inventory = new DarkInventory(27) {
        @Override
        public void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private int numPlayersUsing;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        String key = compound.hasKey("item") ? "item" : "items";

        if (compound.hasKey(key)) {
            inventory.deserializeNBT(compound.getCompoundTag(key));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", inventory.serializeNBT());
        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        }
        return super.getCapability(capability, facing);
    }

    public DarkInventory getInventory() {
        return inventory;
    }

    public void openForPlayer(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (numPlayersUsing < 0) {
                numPlayersUsing = 0;
            }

            ++numPlayersUsing;

            IBlockState state = world.getBlockState(pos);

            if (!state.getValue(BarrelBlock.OPEN)) {
                playSound(state, FSounds.BLOCK_BARREL_OPEN);
                setOpen(true, state);
            }

            scheduleUpdate();
        }
    }

    public void setOpen(boolean open, IBlockState state) {
        state = FBlocks.BARREL.getDefaultState()
                .withProperty(BarrelBlock.FACING, state.getValue(BarrelBlock.FACING))
                .withProperty(BarrelBlock.OPEN, open);
        world.setBlockState(pos, state);
    }

    public void closeForPlayer(EntityPlayer player) {
        if (!player.isSpectator()) {
            --numPlayersUsing;
        }
    }

    public void updateOpenState() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        numPlayersUsing = calculatePlayersUsing(x, y, z);
        IBlockState state = world.getBlockState(pos);

        if (numPlayersUsing > 0) {
            scheduleUpdate();
        } else {
            if (state.getBlock() != FBlocks.BARREL) {
                invalidate();
                return;
            }

            boolean flag = state.getValue(BarrelBlock.OPEN);

            if (flag) {
                playSound(state, FSounds.BLOCK_BARREL_CLOSE);
                setOpen(false, state);
            }
        }
    }

    public void scheduleUpdate() {
        world.scheduleUpdate(pos, world.getBlockState(pos).getBlock(), 5);
    }

    public void playSound(IBlockState state, SoundEvent event) {
        if (world.isRemote) return;

        Vec3i vec = state.getValue(BarrelBlock.FACING).getDirectionVec();
        double d0 = pos.getX() + 0.5D + vec.getX() / 2.0D;
        double d1 = pos.getY() + 0.5D + vec.getY() / 2.0D;
        double d2 = pos.getZ() + 0.5D + vec.getZ() / 2.0D;
        world.playSound(null, d0, d1, d2, event, SoundCategory.BLOCKS, 0.5f, world.rand.nextFloat() * 0.1f + 0.9f);
    }

    private int calculatePlayersUsing(int x, int y, int z) {
        int i = 0;

        for (EntityPlayer entityplayer : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - 5.0f, y - 5.0f, z - 5.0F, x + 6.0F, y + 16.0F, z + 6.0F))) {
            if (entityplayer.openContainer instanceof ContainerBarrel) {
                TileBarrel te = ((ContainerBarrel)entityplayer.openContainer).te;

                if (te.pos == this.pos) {
                    ++i;
                }
            }
        }

        return i;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }
}