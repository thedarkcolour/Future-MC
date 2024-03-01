@file:Suppress("UNUSED_PARAMETER")

package thedarkcolour.futuremc

import net.minecraft.block.BlockDispenser
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.dispenser.IBehaviorDispenseItem
import net.minecraft.init.Biomes
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemShears
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.launchwrapper.Launch
import net.minecraft.tileentity.BannerPattern
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.core.util.registerDispenserBehaviour
import thedarkcolour.core.util.runOnClient
import thedarkcolour.futuremc.block.buzzybees.BottleDispenserBehavior
import thedarkcolour.futuremc.block.buzzybees.ShearDispenserBehaviour
import thedarkcolour.futuremc.capability.FPlayerData
import thedarkcolour.futuremc.client.gui.GuiType
import thedarkcolour.futuremc.client.tesr.BellTileEntityRenderer
import thedarkcolour.futuremc.client.tesr.CampfireRenderer
import thedarkcolour.futuremc.client.tesr.WoodenSignRenderer
import thedarkcolour.futuremc.compat.QUARK
import thedarkcolour.futuremc.compat.isModLoaded
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.event.Events
import thedarkcolour.futuremc.item.BannerPatternItem
import thedarkcolour.futuremc.network.NetworkHandler
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FParticles
import thedarkcolour.futuremc.registry.FRecipes
import thedarkcolour.futuremc.tile.BellTileEntity
import thedarkcolour.futuremc.tile.CampfireTile
import thedarkcolour.futuremc.tile.WoodenSignTile
import thedarkcolour.futuremc.world.gen.feature.AncientDebrisWorldGen
import thedarkcolour.futuremc.world.gen.feature.BambooWorldGen
import thedarkcolour.futuremc.world.gen.feature.BeeNestGenerator
import thedarkcolour.futuremc.world.gen.feature.FlowerWorldGen

@Suppress("MemberVisibilityCanBePrivate")
@Mod(
    modid = FutureMC.ID,
    name = FutureMC.NAME,
    version = FutureMC.VERSION,
    dependencies = FutureMC.DEPENDENCIES,
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
    useMetadata = true
)
object FutureMC {
    const val ID = "futuremc"
    const val NAME = "Future MC"
    const val VERSION = "0.2.6"
    const val DEPENDENCIES = "required-after:forgelin;required-after:forge@[14.23.5.2847,)"

    // Blackboard is null when running tests
    @JvmField
    val DEBUG = Launch.blackboard?.get("fml.deobfuscatedEnvironment") == true
    @JvmField
    val LOGGER: Logger = LogManager.getLogger("FutureMC")

    // Creative tab
    lateinit var GROUP: CreativeTabs

    // Sided delegate is null when running tests
    val CLIENT = FMLCommonHandler.instance().sidedDelegate?.side == Side.CLIENT

    // whether the unit tests are running
    val TEST = Launch.blackboard == null

    init {
        // do not register events during tests
        if (!TEST) {
            MinecraftForge.EVENT_BUS.register(Events)
        }

        NetworkHandler.registerPackets()
    }

    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        // Quark has this fix already
        if (!isModLoaded(QUARK)) {
            for (recipe in ForgeRegistries.RECIPES) {
                // find the vanilla trapdoor recipe
                if (recipe is ShapedRecipes && recipe.recipeOutput.item == Item.getItemFromBlock(Blocks.TRAPDOOR)) {
                    // disable the recipe
                    recipe.recipeOutput.count = 0
                }
            }
        }
    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        if (DEBUG) {
            FPlayerData.register()
        }

        GuiType.registerGuiHandler()

        if (FConfig.buzzyBees.bee.enabled) {
            for (item in ForgeRegistries.ITEMS) {
                // Add the action for every type of shears
                if (item is ItemShears) {
                    val existing = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(item)

                    registerDispenserBehaviour(item, IBehaviorDispenseItem { source, stack ->
                        ShearDispenserBehaviour.dispense(source.world, source, stack, existing)
                    })
                }
            }
            val existing = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.GLASS_BOTTLE)
            registerDispenserBehaviour(Items.GLASS_BOTTLE, IBehaviorDispenseItem { source, stack ->
                BottleDispenserBehavior.dispense(source.world, source, stack, existing)
            })
        }
        if (FConfig.villageAndPillage.campfire.enabled) {
            // todo campfire lighting with flint steel and burnout with shovel
        }

        if (FConfig.villageAndPillage.loom.enabled) {
            val params = arrayOf(String::class.java, String::class.java)
            BannerPatternItem.GLOBE = EnumHelper.addEnum(BannerPattern::class.java, "GLOBE", params, "globe", "glo")!!
            BannerPatternItem.SNOUT = EnumHelper.addEnum(BannerPattern::class.java, "SNOUT", params, "snout", "pig")!!
        }

        FRecipes.registerFMCRecipes()

        registerWorldGen()

        FParticles.registerParticles()

        // TESR registering
        runOnClient {
            ClientRegistry.bindTileEntitySpecialRenderer(BellTileEntity::class.java, BellTileEntityRenderer())
            ClientRegistry.bindTileEntitySpecialRenderer(WoodenSignTile::class.java, WoodenSignRenderer())
            ClientRegistry.bindTileEntitySpecialRenderer(CampfireTile::class.java, CampfireRenderer)
        }
    }

    @EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        EntityBee.FLOWERS.removeIf { state ->
            state.material == Material.AIR || !ForgeRegistries.BLOCKS.containsValue(state.block) // try to fix #281
        }

        Biomes.PLAINS.addFlower(FBlocks.CORNFLOWER.defaultState, 5)
        for (biome in listOf(Biomes.FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.BIRCH_FOREST, Biomes.FOREST_HILLS, Biomes.MUTATED_BIRCH_FOREST, Biomes.MUTATED_BIRCH_FOREST_HILLS, Biomes.MUTATED_FOREST)) {
            Biomes.PLAINS.addFlower(FBlocks.CORNFLOWER.defaultState, 5)
            Biomes.PLAINS.addFlower(FBlocks.LILY_OF_THE_VALLEY.defaultState, 5)
        }
    }

    private fun registerWorldGen() {
        if (FConfig.villageAndPillage.lilyOfTheValley.enabled) {
            GameRegistry.registerWorldGenerator(FlowerWorldGen(FBlocks.LILY_OF_THE_VALLEY), 0)
        }
        if (FConfig.villageAndPillage.cornflower.enabled) {
            GameRegistry.registerWorldGenerator(FlowerWorldGen(FBlocks.CORNFLOWER), 0)
        }
        if (FConfig.villageAndPillage.sweetBerryBush.enabled) {
            GameRegistry.registerWorldGenerator(FlowerWorldGen(FBlocks.SWEET_BERRY_BUSH), 0)
        }
        if (FConfig.villageAndPillage.bamboo.enabled) {
            GameRegistry.registerWorldGenerator(BambooWorldGen, -1)
        }
        if (FConfig.buzzyBees.bee.enabled) {
            BeeNestGenerator.refresh()
        }
        if (FConfig.netherUpdate.netherite) {
            GameRegistry.registerWorldGenerator(AncientDebrisWorldGen, 0)
        }
    }
}