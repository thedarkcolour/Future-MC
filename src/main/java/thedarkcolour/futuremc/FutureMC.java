package thedarkcolour.futuremc;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.futuremc.block.BeeHiveBlock;
import thedarkcolour.futuremc.block.Blocks;
import thedarkcolour.futuremc.block.HoneyBlockBlock;
import thedarkcolour.futuremc.entity.BeeEntity;
import thedarkcolour.futuremc.entity.BeeRenderer;
import thedarkcolour.futuremc.item.DebuggerItem;
import thedarkcolour.futuremc.item.HoneyBottleItem;
import thedarkcolour.futuremc.item.Items;
import thedarkcolour.futuremc.item.SummonEggItem;
import thedarkcolour.futuremc.tile.BeeHiveTileEntity;
import thedarkcolour.futuremc.world.functions.CopyState;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Mod(FutureMC.ID)
public class FutureMC {
    public static final String ID = "futuremc";
    public static final ItemGroup TAB = new ItemGroup("FutureMC"){
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.HONEYCOMB);
        }
    };

    public FutureMC() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        if (DEBUG) {
            MinecraftForge.EVENT_BUS.addListener(this::onTooltip);
        }
        MinecraftForge.EVENT_BUS.addListener(HoneyBlockBlock::onEntityJump);
        MinecraftForge.EVENT_BUS.addListener(HoneyBottleItem::onHoneyBottleEaten);
        LootFunctionManager.registerFunction(new CopyState.Serializer());
    }

    @SubscribeEvent
    public void onBlockRegistry(final RegistryEvent.Register<Block> event) {
        registerBlock(new BeeHiveBlock(false));
        registerBlock(new BeeHiveBlock(true));
        registerBlock(new HoneyBlockBlock());
        registerBlock(new Block(Block.Properties.create(Material.CLAY, DyeColor.ORANGE).sound(SoundType.CORAL).hardnessAndResistance(0.6F)).setRegistryName("honeycomb_block"));
    }

    @SubscribeEvent
    public void onItemRegistry(final RegistryEvent.Register<Item> event) {
        registerItem(Blocks.BEE_NEST);
        registerItem(Blocks.BEEHIVE);
        registerItem(Blocks.HONEY_BLOCK);
        registerItem(Blocks.HONEYCOMB_BLOCK);
        registerItem(new HoneyBottleItem());
        registerItem(new Item(DEFAULT_ITEM_PROPERTIES).setRegistryName("honeycomb"));
        if (DEBUG) {
            registerItem(new DebuggerItem());
        }

        // Registering entities along with their spawn eggs
        registerEntityType(EntityType.Builder.create(BeeEntity::new, EntityClassification.CREATURE).size(0.7F, 0.7F).build("bee").setRegistryName("bee"));
    }

    @SubscribeEvent
    public void onTileRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        registerTileEntityType(TileEntityType.Builder.create(BeeHiveTileEntity::new, Blocks.BEE_NEST, Blocks.BEEHIVE).build(null).setRegistryName("beehive"));
    }

    @SubscribeEvent
    public void onSoundRegistry(final RegistryEvent.Register<SoundEvent> event) {
        registerSoundEvent("honey_block_break");
        registerSoundEvent("honey_block_step");
        registerSoundEvent("honey_block_slide");
        registerSoundEvent("bee_enter_hive");
        registerSoundEvent("bee_exit_hive");
        registerSoundEvent("bee_sting");
        registerSoundEvent("bee_death");
        registerSoundEvent("bee_hurt");
        registerSoundEvent("bee_pollinate");
        registerSoundEvent("bee_work");
        registerSoundEvent("bee_aggressive");
        registerSoundEvent("bee_passive");
        registerSoundEvent("honey_bottle_drink");
        registerSoundEvent("beehive_shear");
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(BeeEntity.class, BeeRenderer::new);

        Minecraft.getInstance().getItemColors().register(Items.BEE_SPAWN_EGG::color, Items.BEE_SPAWN_EGG);
    }

    private static final String ADVANCED_INFO_TEXT_PRE = TextFormatting.DARK_GRAY + "     ";

    @OnlyIn(Dist.CLIENT)
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

    private void registerBlock(Block block) {
        ForgeRegistries.BLOCKS.register(block);
    }

    private void registerItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
    }

    private void registerItem(Block block) {
        ForgeRegistries.ITEMS.register(new BlockItem(block, DEFAULT_ITEM_PROPERTIES).setRegistryName(block.getRegistryName()));
    }

    private void registerTileEntityType(TileEntityType<?> tileEntityType) {
        ForgeRegistries.TILE_ENTITIES.register(tileEntityType);
    }

    private void registerSoundEvent(String name) {
        ResourceLocation res = new ResourceLocation(ID, name);
        ForgeRegistries.SOUND_EVENTS.register(new SoundEvent(res).setRegistryName(name));
    }

    private <T extends Entity> void registerEntityType(EntityType<T> type) {
        //((ForgeRegistry<Item>)ForgeRegistries.ITEMS).unfreeze();
        ForgeRegistries.ENTITIES.register(type);
        registerItem(new SummonEggItem<>(type, 16770398, 2500144));
        //((ForgeRegistry<Item>)ForgeRegistries.ITEMS).freeze();
    }

    public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().group(TAB);

    public static final boolean DEBUG;

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