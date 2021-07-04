@file:Suppress("UNUSED_PARAMETER")

package thedarkcolour.futuremc

import net.minecraft.block.BlockDispenser
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.dispenser.BehaviorDefaultDispenseItem
import net.minecraft.dispenser.IBehaviorDispenseItem
import net.minecraft.dispenser.IBlockSource
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemShears
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.launchwrapper.Launch
import net.minecraft.tileentity.BannerPattern
import net.minecraft.tileentity.TileEntityDispenser
import net.minecraft.world.World
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.core.command.GenerateCommand
import thedarkcolour.core.command.HealCommand
import thedarkcolour.core.command.ModeToggleCommand
import thedarkcolour.core.util.TODO
import thedarkcolour.core.util.registerServerDispenserBehaviour
import thedarkcolour.core.util.runOnClient
import thedarkcolour.futuremc.block.buzzybees.ShearDispenserBehaviour
import thedarkcolour.futuremc.capability.SwimmingCapability
import thedarkcolour.futuremc.client.gui.GuiType
import thedarkcolour.futuremc.client.tesr.bell.BellTileEntityRenderer
import thedarkcolour.futuremc.client.tesr.campfire.CampfireRenderer
import thedarkcolour.futuremc.command.FastGiveCommand
import thedarkcolour.futuremc.compat.QUARK
import thedarkcolour.futuremc.compat.isModLoaded
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.event.Events
import thedarkcolour.futuremc.item.BannerPatternItem
import thedarkcolour.futuremc.network.NetworkHandler
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FParticles
import thedarkcolour.futuremc.registry.FRecipes
import thedarkcolour.futuremc.tile.BeeHiveTile
import thedarkcolour.futuremc.tile.BellTileEntity
import thedarkcolour.futuremc.tile.CampfireTile
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
    const val DEPENDENCIES = "required-after:forgelin;required-after:forge@[14.23.5.2847]"

    // Blackboard is null when running tests
    @JvmField
    val DEBUG = Launch.blackboard?.get("fml.deobfuscatedEnvironment") == true
    @JvmField
    val LOGGER: Logger = LogManager.getLogger()

    lateinit var GROUP: CreativeTabs

    // Sided delegate is null when running tests
    val CLIENT = FMLCommonHandler.instance().sidedDelegate?.side == Side.CLIENT

    // whether the unit tests are running
    val TEST = Launch.blackboard == null

    init {
        Events.registerEvents()

        NetworkHandler.registerPackets()

        //if (DEBUG) {
        //    FutureWorldType
        //}
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
            SwimmingCapability.register()
        }

        GuiType.registerGuiHandler()

        if (FConfig.updateAquatic.trident) {
            // todo add separate option for this cause this feature is not actually in Vanilla
            //registerDispenserBehaviour(FItems.TRIDENT, IProjectileDispenserBehaviour(::EntityTrident))
        }
        if (FConfig.buzzyBees.bee.enabled) {
            for (item in ForgeRegistries.ITEMS) {
                if (item is ItemShears) {
                    registerServerDispenserBehaviour(item) { worldIn, source, stack, existing ->
                        ShearDispenserBehaviour().dispense(source, stack)
                    }
                }
            }
            registerServerDispenserBehaviour(Items.GLASS_BOTTLE, ::glassBottleBehaviour)
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
            ClientRegistry.bindTileEntitySpecialRenderer(CampfireTile::class.java, CampfireRenderer)
            //ClientRegistry.bindTileEntitySpecialRenderer(TileSeagrassRenderer::class.java, TESRSeagrassRenderer)

            /*(Minecraft.getMinecraft().resourceManager as IReloadableResourceManager).registerReloadListener(
                ISelectiveResourceReloadListener { _, predicate ->
                    if (predicate.test(VanillaResourceType.TEXTURES)) {
                        val field = Fluid::class.java.getDeclaredField("color")
                        field.isAccessible = true
                        field.set(FluidRegistry.WATER, 0x3f76e4)

                        // fix crash before getting to load screen

                        kotlin.runCatching {
                            if (FConfig.updateAquatic.newWaterColor) {
                                val textures =
                                    ObfuscationReflectionHelper.getPrivateValue<Array<TextureAtlasSprite>, BlockFluidRenderer>(
                                        BlockFluidRenderer::class.java,
                                        Minecraft.getMinecraft().blockRendererDispatcher.fluidRenderer,
                                        "field_178271_b"
                                    )

                                val map = Minecraft.getMinecraft().textureMapBlocks

                                textures[0] = map.getAtlasSprite("futuremc:blocks/water_still")
                                textures[1] = map.getAtlasSprite("futuremc:blocks/water_flow")
                            }
                        }
                    }
                }
            )*/
        }
    }

    private fun glassBottleBehaviour(worldIn: World, source: IBlockSource, stack: ItemStack, existing: IBehaviorDispenseItem?): ItemStack {
        val pos = source.blockPos.offset(source.blockState.getValue(BlockDispenser.FACING))
        val te = worldIn.getTileEntity(pos)

        if (te is BeeHiveTile && te.honeyLevel >= 5) {
            te.emptyHoney(worldIn, pos, null)
            stack.shrink(1)

            if (stack.isEmpty) {
                return stack
            } else {
                if (source.getBlockTileEntity<TileEntityDispenser>().addItemStack(ItemStack(FItems.HONEY_BOTTLE)) < 0) {
                    BehaviorDefaultDispenseItem().dispense(source, stack)
                }

                return ItemStack(FItems.HONEY_BOTTLE)
            }

        } else return existing?.dispense(source, stack) ?: stack
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
            GameRegistry.registerWorldGenerator(BambooWorldGen, 0)
        }
        if (FConfig.buzzyBees.bee.enabled) {
            BeeNestGenerator.refresh()
        }
        if (FConfig.netherUpdate.netherite) {
            GameRegistry.registerWorldGenerator(AncientDebrisWorldGen, 0)
        }
    }

    @EventHandler
    fun onServerStart(event: FMLServerStartingEvent) {
        event.registerServerCommand(FastGiveCommand)
        if (TODO()) {
            event.registerServerCommand(HealCommand())
            event.registerServerCommand(ModeToggleCommand())
            event.registerServerCommand(GenerateCommand())
        }
    }
}