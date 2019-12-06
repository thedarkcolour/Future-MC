package thedarkcolour.futuremc.tile;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import thedarkcolour.futuremc.block.BeeHiveBlock;
import thedarkcolour.futuremc.block.Blocks;
import thedarkcolour.futuremc.entity.BeeEntity;
import thedarkcolour.futuremc.entity.EntityTypes;
import thedarkcolour.futuremc.item.Items;
import thedarkcolour.futuremc.sound.Sounds;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class BeeHiveTileEntity extends InteractionTileEntity implements ITickableTileEntity {
    private final List<BeeHiveTileEntity.Bee> bees = Lists.newArrayList();
    private BlockPos flowerPos;

    public BeeHiveTileEntity() {
        super(TileEntityTypes.BEEHIVE);
        flowerPos = BlockPos.ZERO;
    }

    public boolean canEnter() {
        return bees.size() >= 3;
    }

    public void angerBees(PlayerEntity playerIn, BeeHiveTileEntity.BeeState state) {
        List<Entity> list = tryReleaseBee(state);

        if (playerIn != null) {
            for (Entity entity : list) {
                if (entity instanceof BeeEntity) {
                    BeeEntity bee = (BeeEntity) entity;
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
            BlockState state = worldIn.getBlockState(pos.down(i));
            if (state.getBlock() != Blocks.AIR) {
                return state.getBlock() == Blocks.CAMPFIRE;
            }
        }

        return false;
    }

    private List<Entity> tryReleaseBee(BeeHiveTileEntity.BeeState state) {
        List<Entity> list = Lists.newArrayList();
        bees.removeIf(entry -> releaseBee(entry.data, list, state));
        return list;
    }

    public void tryEnterHive(Entity entityIn, boolean isDelivering) {
        tryEnterHive(entityIn, isDelivering, 0);
    }

    public void tryEnterHive(Entity entityIn, boolean isDelivering, int i) {
        if (bees.size() < 3) {
            CompoundNBT tag = new CompoundNBT();
            entityIn.writeWithoutTypeId(tag);
            bees.add(new BeeHiveTileEntity.Bee(tag, i, isDelivering ? 2400 : 600));
            if (world != null) {
                if (entityIn instanceof BeeEntity) {
                    BeeEntity bee = (BeeEntity)entityIn;
                    if (!hasFlowerPos() || bee.hasFlower() && world.rand.nextBoolean()) {
                        flowerPos = bee.getFlowerPos();
                    }
                }

                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), Sounds.BEE_ENTER_HIVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            entityIn.remove();
        }
    }

    private boolean releaseBee(CompoundNBT data, List<Entity> list, BeeState beeState) {
        if (world.isDaytime() && !world.isRainingAt(pos)) {
            data.remove("Passengers");
            data.remove("Leash");
            data.remove("UUIDLeast");
            data.remove("UUIDMost");
            Optional<BlockPos> optionalPos = Optional.empty();
            BlockState state = world.getBlockState(pos);
            Direction facing = state.get(BeeHiveBlock.FACING);
            BlockPos pos1 = pos.offset(facing, 2);

            if (world.getBlockState(pos1).isAir()) {
                optionalPos = Optional.of(pos1);
            }

            BlockPos blockPos;
            if (!optionalPos.isPresent()) {
                for (Direction f : Direction.Plane.HORIZONTAL) {
                    blockPos = pos.add(f.getXOffset() << 1, f.getYOffset(), f.getZOffset() << 1);
                    if (world.getBlockState(blockPos).isAir()) {
                        optionalPos = Optional.of(blockPos);
                        break;
                    }
                }
            }

            if (!optionalPos.isPresent()) {
                for (Direction f : Direction.Plane.VERTICAL) {
                    blockPos = pos.add(f.getXOffset() << 1, f.getYOffset(), f.getZOffset() << 1);
                    if (world.getBlockState(blockPos).isAir()) {
                        optionalPos = Optional.of(blockPos);
                    }
                }
            }

            if (optionalPos.isPresent()) {
                blockPos = optionalPos.get();
                BeeEntity entity = (BeeEntity) EntityTypes.BEE.create(world);
                entity.read(data);
                entity.setPositionAndRotation(blockPos.getX(), blockPos.getY(), blockPos.getZ(), entity.rotationYaw, entity.rotationPitch);

                if (entity != null) {
                    if (hasFlowerPos() && !entity.hasFlower() && world.rand.nextFloat() < 0.9F) {
                        entity.setFlowerPos(flowerPos);
                    }

                    if (beeState == BeeState.WORKING) {
                        entity.onHoneyDelivered();
                        if (world.getTileEntity(pos) instanceof BeeHiveTileEntity) {
                            int i = world.rand.nextInt(100) == 0 ? 2 : 1;
                            setHoneyLevel(getHoneyLevel() + i);
                        }
                    }

                    if (list != null) {
                        entity.resetPollinationTicks();
                        list.add(entity);
                    }
                }

                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), Sounds.BEE_EXIT_HIVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.addEntity(entity);
                return true; // return true in case of broken bee
            }
        }
        return false;
    }

    private boolean hasFlowerPos() {
        return flowerPos != BlockPos.ZERO;
    }

    public void tickBees() {
        Iterator<Bee> bees = this.bees.iterator();

        while (bees.hasNext()) {
            Bee bee = bees.next();
            if (bee.ticksInHive > bee.minOccupationTicks) {
                CompoundNBT tag = bee.data;
                BeeHiveTileEntity.BeeState state = tag.getBoolean("HasNectar") ? BeeState.WORKING : BeeState.DELIVERED;

                if (releaseBee(tag, null, state)) {
                    bees.remove();
                }
            } else {
                ++bee.ticksInHive;
            }
        }
    }

    @Override
    public void tick() {
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

    public int getHoneyLevel() {
        return getBlockState().get(BeeHiveBlock.HONEY_LEVEL);
    }

    public void setHoneyLevel(int level) {
        if (level > 5) {
            level = 5;
        }

        world.setBlockState(pos, world.getBlockState(pos).with(BeeHiveBlock.HONEY_LEVEL, level).with(BeeHiveBlock.FACING, world.getBlockState(pos).get(BeeHiveBlock.FACING)));
    }

    public boolean isFull() {
        return getHoneyLevel() == 5;
    }

    @Override
    public boolean activated(BlockState state, PlayerEntity playerIn, Hand hand, BlockRayTraceResult result) {
        ItemStack stack = playerIn.getHeldItem(hand);
        boolean action = false;
        if (isFull()) {
            if (stack.getItem() == Items.SHEARS) {
                world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, Sounds.BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                dropHoneyCombs(world, pos);
                stack.damageItem(1, playerIn, playerEntity -> playerEntity.sendBreakAnimation(hand));
                action = true;
            } else if (stack.getItem() == Items.GLASS_BOTTLE) {
                stack.shrink(1);
                world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, Sounds.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.HONEY_BOTTLE));
                } else if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.HONEY_BOTTLE))) {
                    playerIn.dropItem(new ItemStack(Items.HONEY_BOTTLE), false);
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
    public void broken(BlockState state, PlayerEntity playerIn, ItemStack stack) {
        //if (!world.isRemote && (playerIn.isCreative() || EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 1)) {
        //    ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), getItemWithBees());
        //    item.setDefaultPickupDelay();
        //    world.addEntity(item);
        //}
    }

    public ItemStack getItemWithBees() {
        ItemStack stack = new ItemStack(world.getBlockState(pos).getBlock());
        CompoundNBT tag = new CompoundNBT();

        if (!bees.isEmpty()) {
            tag.put("Bees", getBees());
        }

        stack.setTagInfo("BlockEntityTag", tag);
        return stack;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        bees.clear();
        ListNBT list = compound.getList("Bees", 10);

        for (INBT inbt : list) {
            CompoundNBT tag = (CompoundNBT) inbt;
            BeeHiveTileEntity.Bee bee = new BeeHiveTileEntity.Bee((CompoundNBT) tag.get("EntityData"), tag.getInt("TicksInHive"), tag.getInt("MinOccupationTicks"));
            bees.add(bee);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("Bees", getBees());
        compound.put("FlowerPos", NBTUtil.writeBlockPos(flowerPos));
        return compound;
    }

    private ListNBT getBees() {
        ListNBT list = new ListNBT();

        for (Bee bee : bees) {
            CompoundNBT tag = new CompoundNBT();
            tag.put("EntityData", bee.data);
            tag.putInt("TicksInHive", bee.ticksInHive);
            tag.putInt("MinOccupationTicks", bee.minOccupationTicks);
            list.add(tag);
        }

        return list;
    }

    public static void dropHoneyCombs(World worldIn, BlockPos pos) {
        for (int i = 0; i < 3; ++i) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.HONEYCOMB));
        }
    }

    public static void emptyHoney(World worldIn, BlockState state, BlockPos pos, PlayerEntity playerIn) {
        worldIn.setBlockState(pos, state.with(BeeHiveBlock.HONEY_LEVEL, 0));
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof BeeHiveTileEntity) {
            BeeHiveTileEntity hive = (BeeHiveTileEntity)tile;
            hive.setHoneyLevel(0);
            hive.angerBees(playerIn, BeeHiveTileEntity.BeeState.DELIVERED);
        }
    }

    public int getNumberOfBees() {
        return bees.size();
    }

    private static class Bee {
        private final CompoundNBT data;
        private int ticksInHive;
        private final int minOccupationTicks;

        private Bee(CompoundNBT data, int ticksInHive, int minOccupationTicks) {
            data.remove("Leash");
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