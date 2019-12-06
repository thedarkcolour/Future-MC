package thedarkcolour.futuremc.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import thedarkcolour.futuremc.entity.BeeEntity;
import thedarkcolour.futuremc.tile.BeeHiveTileEntity;

public class DebuggerItem extends Item {
    public DebuggerItem() {// TODO
        super(new Item.Properties().maxStackSize(1).group(null));
        setRegistryName("debugger");
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();

        if (worldIn.getTileEntity(pos) instanceof BeeHiveTileEntity) {
            BeeHiveTileEntity hive = (BeeHiveTileEntity) worldIn.getTileEntity(pos);
            if (!worldIn.isRemote) {
                if (player.isSneaking()) {
                    hive.setHoneyLevel(5);
                } else {
                    player.sendMessage(new StringTextComponent("Bees: " + hive.getNumberOfBees() + ", HoneyLevel: " + hive.getHoneyLevel() + ", " + pos));
                }
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target instanceof BeeEntity && !playerIn.world.isRemote) {
            BeeEntity bee = (BeeEntity) target;

            playerIn.sendMessage(new StringTextComponent("Hive: " + bee.getHivePos() + ", Flower: " + bee.getFlowerPos()));
        }

        return false;
    }
}