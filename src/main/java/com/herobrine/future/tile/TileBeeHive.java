package com.herobrine.future.tile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.herobrine.future.block.BlockBeeHive;
import com.herobrine.future.entity.bee.EntityBee;
import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thedarkcolour.core.tile.InteractionTile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TileBeeHive extends InteractionTile {
    private final Map<UUID, Bee> bees = Maps.newHashMap();
    private BlockPos flowerPos;
    private int honeyLevel;

    public TileBeeHive() {
        this.flowerPos = BlockPos.ORIGIN;
    }

    public boolean isFullOfBees() {
        return bees.size() == 3;
    }

    public void angerBees(EntityPlayer playerIn, TileBeeHive.BeeState hive) {
        List<Entity> list = this.tryReleaseBee(hive);

        if (playerIn != null) {
            for (Entity entity : list) {
                if (entity instanceof EntityBee) {
                    EntityBee bee = (EntityBee)entity;
                    if (playerIn.getPosition().distanceSq(bee.getPosition()) <= 16.0D) {
                        if (!this.isBeingSmoked(this.world, this.getPos())) {
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
        for(int i = 1; i <= 5; ++i) {
            IBlockState state = worldIn.getBlockState(pos.down(i));
            if (state.getBlock() != Blocks.AIR) {
                return state.getBlock() == Init.CAMPFIRE;
            }
        }

        return false;
    }

    private List<Entity> tryReleaseBee(TileBeeHive.BeeState state) {
        List<Entity> list = Lists.newArrayList();
        this.bees.values().removeIf(entry -> this.releaseBee(entry.data, list, state));
        return list;
    }

    public void tryEnterHive(Entity entityIn, boolean bool) {
        tryEnterHive(entityIn, bool, 0);
    }

    public void tryEnterHive(Entity entityIn, boolean bool, int i) {
        if (this.bees.size() < 3) {
            NBTTagCompound tag = new NBTTagCompound();
            entityIn.writeToNBT(tag);
            this.bees.put(entityIn.getUniqueID(), new TileBeeHive.Bee(tag, i, bool ? 2400 : 600));
            if (this.world != null) {
                if (entityIn instanceof EntityBee) {
                    EntityBee beeEntity_1 = (EntityBee)entityIn;
                    if (!this.hasFlowerPos() || beeEntity_1.hasFlower() && this.world.rand.nextBoolean()) {
                        this.flowerPos = beeEntity_1.getFlowerPos();
                    }
                }

                this.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), Sounds.BEE_ENTER_HIVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            entityIn.setDead();
        }
    }

    private boolean hasFlowerPos() {
        return flowerPos != BlockPos.ORIGIN;
    }

    private boolean releaseBee(NBTTagCompound data, List<Entity> list, BeeState beeState) {
        Optional<BlockPos> optionalPos = Optional.empty();
        IBlockState state = world.getBlockState(pos);

        if (state.getProperties().containsKey(BlockBeeHive.FACING)) {
            EnumFacing facing = state.getValue(BlockBeeHive.FACING);
            BlockPos pos = this.pos.add(facing.getFrontOffsetX() * 2, facing.getFrontOffsetY(), facing.getFrontOffsetZ() * 2);
            if (world.getBlockState(pos).getCollisionBoundingBox(this.world, pos) == Block.NULL_AABB) {
                optionalPos = Optional.of(pos);
            }
        }

        BlockPos blockPos;
        if (!optionalPos.isPresent()) {
            for (EnumFacing facing: EnumFacing.Plane.HORIZONTAL.facings()) {
                blockPos = pos.add(facing.getFrontOffsetX() * 2, facing.getFrontOffsetY(), facing.getFrontOffsetZ() * 2);
                if (world.getBlockState(pos).getCollisionBoundingBox(world, blockPos) == Block.NULL_AABB) {
                    optionalPos = Optional.of(blockPos);
                }
            }
        }

        if (!optionalPos.isPresent()) {
            for (EnumFacing facing: EnumFacing.Plane.VERTICAL.facings()) {
                blockPos = pos.add(facing.getFrontOffsetX() * 2, facing.getFrontOffsetY(), facing.getFrontOffsetZ() * 2);
                if (world.getBlockState(pos).getCollisionBoundingBox(world, blockPos) == Block.NULL_AABB) {
                    optionalPos = Optional.of(blockPos);
                }
            }
        }

        if (optionalPos.isPresent()) {
            BlockPos blockPos1 = optionalPos.get();
            EntityBee entity = new EntityBee(world);
            entity.readFromNBT(data);
            entity.setPositionAndRotation(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ(), entity.rotationYaw, entity.rotationPitch);

            if (entity != null) {
                if (entity instanceof EntityBee) {
                    if (this.hasFlowerPos() && !entity.hasFlower() && this.world.rand.nextFloat() < 0.9F) {
                        entity.onHoneyDelivered();
                        if (world.getBlockState(pos).getBlock() == Init.BEE_NEST) {
                            int honeyLevel = world.getBlockState(pos).getValue(BlockBeeHive.HONEY_LEVEL);

                            if (honeyLevel < 5) {
                                int i = this.world.rand.nextInt(100) == 0 ? 2 : 1;
                                if(honeyLevel + i > 5) {
                                    --i;
                                }

                                world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockBeeHive.HONEY_LEVEL, i + honeyLevel));
                            }
                        }
                    }

                    if (list != null) {
                        entity.resetPollinationTicks();
                        list.add(entity);
                    }
                }

                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), Sounds.BEE_EXIT_HIVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return this.world.spawnEntity(entity);
            }
        }

        return false;
    }

    public int getHoneyLevel() {
        return honeyLevel;
    }

    @Override
    public boolean activated(IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        int honeyLevel = state.getValue(BlockBeeHive.HONEY_LEVEL);
        boolean flag = false;
        if (honeyLevel >= 5) {
            if (stack.getItem() == Items.SHEARS) {
                world.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, Sounds.BEEHIVE_SHEAR, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                BlockBeeHive.dropHoneyComb(world, pos);
                stack.damageItem(1, playerIn);
                flag = true;
            } else if (stack.getItem() == Items.GLASS_BOTTLE) {
                stack.shrink(1);
                world.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    playerIn.setHeldItem(hand, new ItemStack(Init.HONEY_BOTTLE));
                } else if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Init.HONEY_BOTTLE))) {
                    playerIn.dropItem(new ItemStack(Init.HONEY_BOTTLE), false);
                }

                flag = true;
            }
        }

        if (flag) {
            BlockBeeHive.emptyHoney(world, state, pos, playerIn);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.bees.clear();
        NBTTagList list = compound.getTagList("Bees", 10);

        list.forEach(base -> {
            NBTTagCompound tag = (NBTTagCompound) base;
            TileBeeHive.Bee bee = new TileBeeHive.Bee((NBTTagCompound) tag.getTag("EntityData"), tag.getInteger("TicksInHive"), tag.getInteger("MinOccupationTicks"));
            bees.put(bee.data.getUniqueId("UUID"), bee);
        });
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Bees", this.getBees());
        compound.setTag("FlowerPos", NBTUtil.createPosTag(this.flowerPos));
        compound.setInteger("HoneyLevel", getHoneyLevel());
        return compound;
    }

    private NBTTagList getBees() {
        NBTTagList list = new NBTTagList();

        for (Bee bee : bees.values()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("EntityData", bee.data);
            tag.setInteger("TicksInHive", bee.ticksInHive);
            tag.setInteger("MinOccupationTicks", bee.minOccupationTicks);
            list.appendTag(tag);
        }

        return list;
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
        field_20428,
        field_20429
    }
}