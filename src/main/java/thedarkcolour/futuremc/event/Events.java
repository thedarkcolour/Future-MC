package thedarkcolour.futuremc.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thedarkcolour.futuremc.block.BlockStrippedLog;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.sound.Sounds;

@Mod.EventBusSubscriber
public final class Events {
    @SubscribeEvent
    public static void eatHoneyBottleEvent(final PlaySoundAtEntityEvent event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase) event.getEntity();
            if (e.getActiveItemStack().getItem() == Init.HONEY_BOTTLE) {
                event.setSound(Sounds.HONEY_BOTTLE_DRINK);
            }
        }
    }

    @SubscribeEvent
    public static void stripLogEvent(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();

        if (FutureConfig.general.strippedLogs) {
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMobDeath(LivingDeathEvent event) {
        if(FutureConfig.general.trident) {
            Entity entity = event.getEntity();
            if(entity instanceof EntityElderGuardian) {
                entity.getEntityWorld().spawnEntity(new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, new ItemStack(Init.TRIDENT, 1)));
            }
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
                    return Init.STRIPPED_ACACIA_LOG.getDefaultState();
                case "jungle":
                    return Init.STRIPPED_JUNGLE_LOG.getDefaultState();
                case "birch":
                    return Init.STRIPPED_BIRCH_LOG.getDefaultState();
                case "oak":
                    return Init.STRIPPED_OAK_LOG.getDefaultState();
                case "spruce":
                    return Init.STRIPPED_SPRUCE_LOG.getDefaultState();
                case "dark_oak":
                    return Init.STRIPPED_DARK_OAK_LOG.getDefaultState();
            }
        }
        throw new IllegalStateException("Invalid log");
    }
}