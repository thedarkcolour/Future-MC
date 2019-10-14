package thedarkcolour.futuremc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.block.Blocks;

import java.util.Objects;

public class SummonEggItem<T extends Entity> extends Item {
    private final EntityType<T> type;
    private final int primary;
    private final int secondary;

    public SummonEggItem(EntityType<T> type, int primary, int secondary) {
        super(FutureMC.DEFAULT_ITEM_PROPERTIES);
        this.type = type;
        this.primary = primary;
        this.secondary = secondary;
        this.setRegistryName(type.getRegistryName().getPath() + "_spawn_egg");
    }

    @Override
    protected String getDefaultTranslationKey() {
        return "item.futurmc.spawn_egg";
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(I18n.format(type.getTranslationKey()) + " " + I18n.format(getDefaultTranslationKey()));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();

        if(world.isRemote) {
            return ActionResultType.SUCCESS;
        }
        else {
            ItemStack stack = context.getItem();
            BlockPos pos = context.getPos();
            Direction facing = context.getFace();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (block == Blocks.SPAWNER) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof MobSpawnerTileEntity) {
                    AbstractSpawner logic = ((MobSpawnerTileEntity)te).getSpawnerBaseLogic();
                    EntityType<T> entity = type;
                    if (entity != null) {
                        logic.setEntityType(entity);
                        te.markDirty();
                        world.notifyBlockUpdate(pos, state, state, 3);
                    }

                    stack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
            }

            BlockPos pos1;
            if (state.getCollisionShape(world, pos).isEmpty()) {
                pos1 = pos;
            } else {
                pos1 = pos.offset(facing);
            }

            EntityType<T> entity = type;
            if (entity == null || entity.spawn(world, stack, context.getPlayer(), pos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(pos, pos1) && facing == Direction.UP) != null) {
                stack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
    }

    public int color(@SuppressWarnings("unused") ItemStack stack, int tintIndex) {
        return tintIndex == 0 ? primary : secondary;
    }
}