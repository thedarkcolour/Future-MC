package thedarkcolour.futuremc.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thedarkcolour.futuremc.block.BlockStrippedLog;
import thedarkcolour.futuremc.block.BlockWitherRose;
import thedarkcolour.futuremc.config.FConfig;
import thedarkcolour.futuremc.init.FBlocks;
import thedarkcolour.futuremc.init.FItems;
import thedarkcolour.futuremc.init.Sounds;

// TODO use Util#addListener
@Mod.EventBusSubscriber
public final class Events {
    @SubscribeEvent
    public static void eatHoneyBottleEvent(final PlaySoundAtEntityEvent event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase) event.getEntity();
            if (e.getActiveItemStack().getItem() == FItems.INSTANCE.getHONEY_BOTTLE()) {
                event.setSound(Sounds.INSTANCE.getHONEY_BOTTLE_DRINK());
            }
        }
    }

    @SubscribeEvent
    public static void stripLogEvent(final PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();

        if (FConfig.INSTANCE.getUpdateAquatic().strippedLogs.rightClickToStrip) {
            if (stack.getItem() instanceof ItemTool) {
                ItemTool tool = (ItemTool) stack.getItem();
                if (tool.getToolClasses(stack).contains("axe")) {
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    if (block == Blocks.LOG || block == Blocks.LOG2) {
                        IProperty<BlockLog.EnumAxis> axis = null;
                        IProperty<BlockPlanks.EnumType> variant = null;
                        for (IProperty prop : state.getPropertyKeys()) {
                            if (prop.getName().equals("axis")) {
                                //noinspection unchecked
                                axis = prop;
                            }
                            if (prop.getName().equals("variant")) {
                                //noinspection unchecked
                                variant = prop;
                            }
                        }
                        if (axis != null && variant != null) {
                            if (BlockStrippedLog.variants.contains(state.getValue(variant).toString())) {
                                player.swingArm(event.getHand());
                                world.playSound(player, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);

                                world.setBlockState(pos, getState(state, block).withProperty(axis, state.getValue(axis)));

                                stack.damageItem(1, player);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMobDeath(final LivingDeathEvent event) {
        EntityLivingBase entityIn = event.getEntityLiving();
        World worldIn = entityIn.world;
        if (!entityIn.isDead) {
            if (!worldIn.isRemote) {
                if (ForgeEventFactory.getMobGriefingEvent(worldIn, entityIn)) {
                    if (event.getSource().getTrueSource() instanceof EntityWither) {
                        BlockPos pos = new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ);
                        IBlockState state = worldIn.getBlockState(pos);
                        if (state.getBlock().isAir(state, worldIn, pos) && ((BlockWitherRose) FBlocks.INSTANCE.getWITHER_ROSE()).canBlockStay(worldIn, pos, state)) {
                            worldIn.setBlockState(pos, FBlocks.INSTANCE.getWITHER_ROSE().getDefaultState(), 3);
                        }
                    }
                } else {
                    EntityItem item = new EntityItem(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ);
                    worldIn.spawnEntity(item);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJump(final LivingEvent.LivingJumpEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        int x = MathHelper.floor(entity.posX);
        int y = MathHelper.floor(entity.posY - 0.20000000298023224D);
        int z = MathHelper.floor(entity.posZ);
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = entity.world.getBlockState(pos);
        if (state.getBlock() == FBlocks.INSTANCE.getHONEY_BLOCK()) {
            entity.motionY *= 0.5D;
        }
    }

    public static IBlockState getState(IBlockState state, Block block) {
        String variant = null;
        if (block == Blocks.LOG) {
            variant = state.getValue(BlockOldLog.VARIANT).getName();
        }
        if (block == Blocks.LOG2) {
            variant = state.getValue(BlockNewLog.VARIANT).getName();
        }

        if (variant != null) {
            switch (variant) {
                case "acacia":
                    return FBlocks.INSTANCE.getSTRIPPED_ACACIA_LOG().getDefaultState();
                case "jungle":
                    return FBlocks.INSTANCE.getSTRIPPED_JUNGLE_LOG().getDefaultState();
                case "birch":
                    return FBlocks.INSTANCE.getSTRIPPED_BIRCH_LOG().getDefaultState();
                case "oak":
                    return FBlocks.INSTANCE.getSTRIPPED_OAK_LOG().getDefaultState();
                case "spruce":
                    return FBlocks.INSTANCE.getSTRIPPED_SPRUCE_LOG().getDefaultState();
                case "dark_oak":
                    return FBlocks.INSTANCE.getSTRIPPED_DARK_OAK_LOG().getDefaultState();
            }
        }
        throw new IllegalStateException("Invalid log");
    }
}