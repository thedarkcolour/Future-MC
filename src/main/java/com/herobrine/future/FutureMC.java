package com.herobrine.future;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.entity.Entities;
import com.herobrine.future.entity.trident.EntityTrident;
import com.herobrine.future.init.Init;
import com.herobrine.future.items.OreDict;
import com.herobrine.future.proxy.CommonProxy;
import com.herobrine.future.tile.GuiHandler;
import com.herobrine.future.worldgen.WorldGenBamboo;
import com.herobrine.future.worldgen.WorldGenFlower;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Init.MODID,
        name = FutureMC.MODNAME,
        version = FutureMC.VERSION,
        dependencies = "required-after:forge@[14.23.5.2776,)", useMetadata = true
)
public class FutureMC {
    public static final String MODNAME = "Future MC";
    public static final String VERSION = "0.1.8";
    public static Logger LOGGER;

    @SidedProxy(clientSide = "com.herobrine.future.proxy.ClientProxy", serverSide = "com.herobrine.future.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static FutureMC instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(FutureConfig.class);
        Entities.init();
        proxy.preInit(e);
        LOGGER = e.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        GameRegistry.addSmelting(new ItemStack(Blocks.STONE), new ItemStack(Init.SMOOTH_STONE), 0.1F);
        GameRegistry.addSmelting(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Init.SMOOTH_QUARTZ), 0.1F);
        if(FutureConfig.general.trident) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Init.TRIDENT, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityTrident trident = new EntityTrident(worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
                trident.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return trident;
            }
        });
        OreDict.registerOres();
        registerGenerators();
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    public void registerGenerators() {
        if(FutureConfig.modFlowers.lily && FutureConfig.modFlowers.lilyGen) {
            GameRegistry.registerWorldGenerator(new WorldGenFlower(Init.LILY_OF_VALLEY), 0);
        }

        if(FutureConfig.modFlowers.cornflower && FutureConfig.modFlowers.cornflowerGen) {
            GameRegistry.registerWorldGenerator(new WorldGenFlower(Init.CORNFLOWER), 0);
        }

        if(FutureConfig.general.berryBush && FutureConfig.general.berryBushGen) {
            GameRegistry.registerWorldGenerator(new WorldGenFlower(Init.BERRY_BUSH), 0);
        }

        if(FutureConfig.general.bamboo && FutureConfig.general.bambooSpawnsInJungles) {
            GameRegistry.registerWorldGenerator(new WorldGenBamboo(), 0);
        }
    }
}