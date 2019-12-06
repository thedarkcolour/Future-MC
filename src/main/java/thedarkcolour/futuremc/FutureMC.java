package thedarkcolour.futuremc;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thedarkcolour.futuremc.block.Blocks;
import thedarkcolour.futuremc.block.HoneyBlockBlock;
import thedarkcolour.futuremc.entity.BeeEntity;
import thedarkcolour.futuremc.entity.BeeRenderer;
import thedarkcolour.futuremc.entity.EntityTypes;
import thedarkcolour.futuremc.item.HoneyBottleItem;
import thedarkcolour.futuremc.item.Items;
import thedarkcolour.futuremc.item.SpawnEggItem;
import thedarkcolour.futuremc.tile.TileEntityTypes;
import thedarkcolour.futuremc.world.functions.CopyState;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Mod(FutureMC.ID)
public class FutureMC {
    public static final String ID = "futuremc";
    public static final ItemGroup TAB = new ItemGroup("futuremc") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.HONEYCOMB);
        }
    };
    public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().group(TAB);
    public static Logger logger;
    public static final boolean DEBUG;
    private static final String ADVANCED_INFO_TEXT_PRE = TextFormatting.DARK_GRAY + "     ";

    public FutureMC() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        if (DEBUG) {
            MinecraftForge.EVENT_BUS.addListener(this::onTooltip);
        }
        MinecraftForge.EVENT_BUS.addListener(HoneyBlockBlock::onEntityJump);
        MinecraftForge.EVENT_BUS.addListener(HoneyBottleItem::onHoneyBottleEaten);
        LootFunctionManager.registerFunction(new CopyState.Serializer());
        logger = LogManager.getLogger();

        //BeeNestGenerator.init();
    }

    @SubscribeEvent
    public void onBlockRegistry(final Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(Blocks.BEE_NEST);
        registry.register(Blocks.BEEHIVE);
        registry.register(Blocks.HONEY_BLOCK);
        registry.register(Blocks.HONEYCOMB_BLOCK);
    }

    @SubscribeEvent
    public void onItemRegistry(final Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(Items.BEE_NEST);
        registry.register(Items.BEEHIVE);
        registry.register(Items.HONEY_BLOCK);
        registry.register(Items.HONEYCOMB_BLOCK);
        registry.register(Items.HONEY_BOTTLE);
        registry.register(Items.HONEYCOMB);
        registry.register(Items.BEE_SPAWN_EGG);
        if (DEBUG) {
            registry.register(Items.DEBUGGER);
        }
    }

    @SubscribeEvent
    public void onEntityRegistry(final Register<EntityType<?>> event) {
        event.getRegistry().register(EntityTypes.BEE);
    }

    @SubscribeEvent
    public void onTileRegistry(final Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityTypes.BEEHIVE);
    }

    @SubscribeEvent
    public void onClientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(BeeEntity.class, BeeRenderer::new);

        Minecraft.getInstance().getItemColors().register(((SpawnEggItem)Items.BEE_SPAWN_EGG)::color, Items.BEE_SPAWN_EGG);
    }

    @SubscribeEvent
    public void onTooltip(final ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (event.getFlags().isAdvanced() && !stack.isEmpty()) {
            CompoundNBT compound = event.getItemStack().getTag();
            if (compound != null && !compound.isEmpty()) {
                int limit = 400;
                String compoundStrg = compound.toString();
                int compoundStrgLength = compoundStrg.length();

                String compoundDisplay;
                if (limit > 0 && compoundStrgLength > limit) {
                    compoundDisplay = compoundStrg.substring(0, limit) + TextFormatting.GRAY + " (" + (compoundStrgLength - limit) + " more characters...)";
                } else {
                    compoundDisplay = compoundStrg;
                }
                event.getToolTip().add(new StringTextComponent(ADVANCED_INFO_TEXT_PRE + compoundDisplay));
            }
        }
    }

    static {
        boolean thrown = false;

        try {
            new FileReader("debug_future_mc.txt");
        } catch (FileNotFoundException e) {
            thrown = true;
        }

        DEBUG = !thrown;
    }
}