package thedarkcolour.futuremc.tile;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sun.reflect.Reflection;
import thedarkcolour.core.tile.InteractionTile;
import thedarkcolour.futuremc.block.BlockBeeHive;
import thedarkcolour.futuremc.entity.bee.EntityBee;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.sound.Sounds;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class TileBeeHive extends InteractionTile implements ITickable {
    private final List<TileBeeHive.Bee> bees = Lists.newArrayList();
    private BlockPos flowerPos;
    private int honeyLevel;

    public TileBeeHive() {
        flowerPos = BlockPos.ORIGIN;
    }

    public boolean isFullOfBees() {
        return bees.size() == 3;
    }

    public void angerBees(EntityPlayer playerIn, TileBeeHive.BeeState state) {
        List<Entity> list = tryReleaseBee(state);

        if (playerIn != null) {
            for (Entity entity : list) {
                if (entity instanceof EntityBee) {
                    EntityBee bee = (EntityBee) entity;
                    if (playerIn.getPosition().distanceSq(bee.getPosition()) <= 16.0D) {
                        if (!isBeingSmoked(world, getPos())) {
                            bee.setBeeAttacker(playerIn);
                        } else {
                            bee.setCannotEnterHiveTicks(400);
                        }
                    }
                }
            }
        }
    }

    private boolean isBeingSmoked(World worldIn, BlockPos pos) {
        for (int i = 1; i <= 5; ++i) {
            IBlockState state = worldIn.getBlockState(pos.down(i));
            if (state.getBlock() != Blocks.AIR) {
                return state.getBlock() == Init.CAMPFIRE;
            }
        }

        return false;
    }

    private List<Entity> tryReleaseBee(TileBeeHive.BeeState state) {
        List<Entity> list = Lists.newArrayList();
        this.bees.removeIf(entry -> releaseBee(entry.data, list, state));
        return list;
    }

    public void tryEnterHive(Entity entityIn, boolean isDelivering) {
        tryEnterHive(entityIn, isDelivering, 0);
    }

    public void tryEnterHive(Entity entityIn, boolean isDelivering, int i) {
        if (bees.size() < 3) {
            NBTTagCompound tag = new NBTTagCompound();
            entityIn.writeToNBT(tag);
            bees.add(new TileBeeHive.Bee(tag, i, isDelivering ? 2400 : 600));
            if (world != null) {
                if (entityIn instanceof EntityBee) {
                    EntityBee beeEntity_1 = (EntityBee)entityIn;
                    if (!hasFlowerPos() || beeEntity_1.hasFlower() && world.rand.nextBoolean()) {
                        flowerPos = beeEntity_1.getFlowerPos();
                    }
                }

                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), Sounds.BEE_ENTER_HIVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            entityIn.setDead();
        }
    }

    private boolean releaseBee(NBTTagCompound data, List<Entity> list, BeeState beeState) {
        if (world.isDaytime() && !world.isRainingAt(pos)) {
            data.removeTag("Passengers");
            data.removeTag("Leash");
            data.removeTag("UUID");
            Optional<BlockPos> optionalPos = Optional.empty();
            IBlockState state = world.getBlockState(pos);
            EnumFacing facing = state.getValue(BlockBeeHive.FACING);
            BlockPos pos1 = pos.offset(facing, 2);

            if (world.getBlockState(pos1).getCollisionBoundingBox(world, pos1) == Block.NULL_AABB) {
                optionalPos = Optional.of(pos1);
            }

            BlockPos blockPos;
            if (!optionalPos.isPresent()) {
                for (EnumFacing f : EnumFacing.HORIZONTALS) {
                    blockPos = pos.add(f.getXOffset() << 1, f.getYOffset(), f.getZOffset() << 1);
                    if (world.getBlockState(blockPos).getCollisionBoundingBox(world, blockPos) == Block.NULL_AABB) {
                        optionalPos = Optional.of(blockPos);
                        break;
                    }
                }
            }

            if (!optionalPos.isPresent()) {
                for (EnumFacing f : new EnumFacing[] {EnumFacing.UP, EnumFacing.DOWN}) {
                    blockPos = pos.add(f.getXOffset() << 1, f.getYOffset(), f.getZOffset() << 1);
                    if (world.getBlockState(blockPos).getCollisionBoundingBox(world, blockPos) == Block.NULL_AABB) {
                        optionalPos = Optional.of(blockPos);
                    }
                }
            }

            if (optionalPos.isPresent()) {
                blockPos = optionalPos.get();
                EntityBee entity = new EntityBee(world);
                entity.readFromNBT(data);
                entity.setPositionAndRotation(blockPos.getX(), blockPos.getY(), blockPos.getZ(), entity.rotationYaw, entity.rotationPitch);

                if (entity != null) {
                    if (hasFlowerPos() && !entity.hasFlower() && world.rand.nextFloat() < 0.9F) {
                        entity.setFlowerPos(flowerPos);
                    }

                    if (beeState == BeeState.WORKING) {
                        entity.onHoneyDelivered();
                        if (world.getTileEntity(pos) instanceof TileBeeHive) {
                            int i = world.rand.nextInt(100) == 0 ? 2 : 1;
                            setHoneyLevel(honeyLevel + i, true);
                        }
                    }

                    if (list != null) {
                        entity.resetPollinationTicks();
                        list.add(entity);
                    }
                }

                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), Sounds.BEE_EXIT_HIVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.spawnEntity(entity);
                return true; // return true in case of broken bee
            }
        }
        return false;
    }

    private boolean hasFlowerPos() {
        return flowerPos != BlockPos.ORIGIN;
    }

    public void tickBees() {
        Iterator<Bee> bees = this.bees.iterator();

        while (bees.hasNext()) {
            Bee bee = bees.next();
            if (bee.ticksInHive > bee.minOccupationTicks) {
                NBTTagCompound tag = bee.data;
                TileBeeHive.BeeState state = tag.getBoolean("HasNectar") ? BeeState.WORKING : BeeState.DELIVERED;

                if (releaseBee(tag, null, state)) {
                    bees.remove();
                }
            } else {
                ++bee.ticksInHive;
            }
        }
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            tickBees();
            if (bees.size() > 0 && world.rand.nextDouble() < 0.005D) {
                double x = pos.getX() + 0.5D;
                double y = pos.getY();
                double z = pos.getZ() + 0.5D;
                world.playSound(null, x, y, z, Sounds.BEE_WORK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public int getHoneyLevel() {
        return honeyLevel;
    }

    public void setHoneyLevel(int level, boolean updateState) {
        if (level > 5) level = 5;

        honeyLevel = level;

        if (updateState) {
            world.setBlockState(pos, getBlockType().getDefaultState()
                    .withProperty(BlockBeeHive.IS_FULL, level == 5)
                    .withProperty(BlockBeeHive.FACING, world.getBlockState(pos).getValue(BlockBeeHive.FACING)));
        }
    }

    public boolean isFull() {
        return honeyLevel >= 5;
    }

    @Override
    public boolean activated(IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        boolean action = false;
        if (isFull()) {
            if (stack.getItem() == Items.SHEARS) {
                world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, Sounds.BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                dropHoneyCombs(world, pos);
                stack.damageItem(1, playerIn);
                action = true;
            } else if (stack.getItem() == Items.GLASS_BOTTLE) {
                stack.shrink(1);
                world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    playerIn.setHeldItem(hand, new ItemStack(Init.HONEY_BOTTLE));
                } else if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Init.HONEY_BOTTLE))) {
                    playerIn.dropItem(new ItemStack(Init.HONEY_BOTTLE), false);
                }

                action = true;
            }
        }

        if (action) {
            emptyHoney(world, state, pos, playerIn);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void broken(IBlockState state, EntityPlayer playerIn) {
        if (!world.isRemote && playerIn.isCreative()) {
            ItemStack stack = new ItemStack(world.getBlockState(pos).getBlock());
            NBTTagCompound tag = new NBTTagCompound();

            if (!bees.isEmpty()) {
                tag.setTag("Bees", getBees());
            }

            tag.setInteger("HoneyLevel", getHoneyLevel());
            stack.setTagInfo("BlockEntityTag", tag);

            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            item.setDefaultPickupDelay();
            world.spawnEntity(item);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        bees.clear();
        NBTTagList list = compound.getTagList("Bees", 10);
        setHoneyLevel(compound.getInteger("HoneyLevel"), Reflection.getCallerClass(2) != TileEntity.class);

        for (NBTBase base : list) {
            NBTTagCompound tag = (NBTTagCompound) base;
            TileBeeHive.Bee bee = new TileBeeHive.Bee((NBTTagCompound) tag.getTag("EntityData"), tag.getInteger("TicksInHive"), tag.getInteger("MinOccupationTicks"));
            bees.add(bee);
        }
    }

    @Override
    protected void setWorldCreate(World worldIn) {
        super.setWorldCreate(worldIn);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Bees", getBees());
        compound.setTag("FlowerPos", NBTUtil.createPosTag(flowerPos));
        compound.setInteger("HoneyLevel", honeyLevel);
        return compound;
    }

    private NBTTagList getBees() {
        NBTTagList list = new NBTTagList();

        for (Bee bee : bees) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("EntityData", bee.data);
            tag.setInteger("TicksInHive", bee.ticksInHive);
            tag.setInteger("MinOccupationTicks", bee.minOccupationTicks);
            list.appendTag(tag);
        }

        return list;
    }

    public static void dropHoneyCombs(World worldIn, BlockPos pos) {
        for (int i = 0; i < 3; ++i) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Init.HONEY_COMB));
        }
    }

    public static void emptyHoney(World worldIn, IBlockState state, BlockPos pos, EntityPlayer playerIn) {
        worldIn.setBlockState(pos, state.withProperty(BlockBeeHive.IS_FULL, false), 3);
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileBeeHive) {
            TileBeeHive hive = (TileBeeHive)tile;
            hive.setHoneyLevel(0, true);
            hive.angerBees(playerIn, TileBeeHive.BeeState.DELIVERED);
        }
    }

    public int getNumberOfBees() {
        return bees.size();
    }

    static class Bee {
        private final NBTTagCompound data;
        private int ticksInHive;
        private final int minOccupationTicks;

        Bee(NBTTagCompound data, int ticksInHive, int minOccupationTicks) {
            this.data = data;
            this.ticksInHive = ticksInHive;
            this.minOccupationTicks = minOccupationTicks;
        }
    }

    public enum BeeState {
        WORKING,
        DELIVERED
    }
}