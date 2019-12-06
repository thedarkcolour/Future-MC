package thedarkcolour.futuremc.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.block.Blocks;
import thedarkcolour.futuremc.entity.EntityTypes;

public class Items extends net.minecraft.item.Items {
    public static final Item BEE_NEST = itemBlockOf(Blocks.BEE_NEST);
    public static final Item BEEHIVE = itemBlockOf(Blocks.BEEHIVE);
    public static final Item HONEY_BLOCK = itemBlockOf(Blocks.HONEY_BLOCK);
    public static final Item HONEYCOMB_BLOCK = itemBlockOf(Blocks.HONEYCOMB_BLOCK);
    public static final Item HONEY_BOTTLE = new HoneyBottleItem();
    public static final Item HONEYCOMB = new Item(FutureMC.DEFAULT_ITEM_PROPERTIES).setRegistryName("honeycomb");
    public static final Item BEE_SPAWN_EGG = new SpawnEggItem<>(EntityTypes.BEE, 16770398, 2500144);
    public static final Item DEBUGGER = new DebuggerItem();

    private static Item itemBlockOf(Block block) {
        return new BlockItem(block, FutureMC.DEFAULT_ITEM_PROPERTIES).setRegistryName(block.getRegistryName());
    }
}