package thedarkcolour.futuremc.registry

import net.minecraft.block.*
import net.minecraft.block.Block.Properties
import net.minecraft.block.PressurePlateBlock.Sensitivity
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.tags.BlockTags
import net.minecraft.tags.Tag
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.block.*
import thedarkcolour.futuremc.block.PressurePlateBlock
import thedarkcolour.futuremc.config.Config
import thedarkcolour.futuremc.feature.HugeFungusFeatureConfig

@Suppress("HasPlatformType", "MemberVisibilityCanBePrivate")
object FBlocks {
    val NETHER_WOOD = Material.Builder(MaterialColor.WOOD).build()

    val NETHER_GOLD_ORE = NetherGoldOreBlock(Properties.create(Material.ROCK).hardnessAndResistance(3.0f, 3.0f)).setRegistryName("nether_gold_ore")
    val SOUL_FIRE = SoulFireBlock(Properties.create(Material.FIRE, MaterialColor.LIGHT_BLUE).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0f).lightValue(10).sound(SoundType.CLOTH).noDrops()).setRegistryName("soul_fire")
    val SOUL_SOIL = Block(Properties.create(Material.EARTH, MaterialColor.BROWN).hardnessAndResistance(0.5f).sound(FSounds.SOUL_SOIL)).setRegistryName("soul_soil")
    val BASALT = RotatedPillarBlock(Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(1.25f, 4.2f).sound(FSounds.BASALT)).setRegistryName("basalt")
    val POLISHED_BASALT = RotatedPillarBlock(Properties.from(BASALT)).setRegistryName("polished_basalt")
    val SOUL_FIRE_TORCH = StandingSoulFireTorchBlock(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0f).lightValue(10).sound(SoundType.WOOD)).setRegistryName("soul_fire_torch")
    val SOUL_FIRE_WALL_TORCH = WallSoulFireTorchBlock(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0f).lightValue(10).sound(SoundType.WOOD).lootFrom(SOUL_FIRE_TORCH)).setRegistryName("soul_fire_wall_torch")
    val SOUL_FIRE_LANTERN = LanternBlock(Properties.create(Material.IRON).hardnessAndResistance(3.5f).sound(SoundType.LANTERN).lightValue(10).nonOpaque()).setRegistryName("soul_fire_lantern")
    val WARPED_STEM = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(1.0f).sound(FSounds.NETHER_STEM)).setRegistryName("warped_stem")
    val STRIPPED_WARPED_STEM = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(1.0f).sound(FSounds.NETHER_STEM)).setRegistryName("stripped_warped_stem")
    val WARPED_HYPHAE = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f).sound(FSounds.NETHER_STEM)).setRegistryName("warped_hyphae")
    val STRIPPED_WARPED_HYPHAE = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f).sound(FSounds.NETHER_STEM)).setRegistryName("stripped_warped_hyphae")
    val WARPED_NYLIUM = NyliumBlock(Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(1.0f).sound(FSounds.NYLIUM).tickRandomly()).setRegistryName("warped_nylium")
    val WARPED_FUNGUS = FungusBlock(Properties.create(Material.PLANTS).hardnessAndResistance(0.0f).doesNotBlockMovement().sound(FSounds.FUNGUS)) {
        FFeatures.HUGE_FUNGUS.configure(HugeFungusFeatureConfig.WARPED_PLANTED)
    }.setRegistryName("warped_fungus")
    val WARPED_WART_BLOCK = Block(Properties.create(Material.ORGANIC, MaterialColor.CYAN).hardnessAndResistance(1.0f)).setRegistryName("warped_wart_block")
    val WARPED_ROOTS = RootsBlock(Properties.create(Material.TALL_PLANTS, MaterialColor.CYAN).doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.ROOTS)).setRegistryName("warped_roots")
    val NETHER_SPROUTS = SproutsBlock(Properties.create(Material.TALL_PLANTS, MaterialColor.CYAN).doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.NETHER_SPROUTS)).setRegistryName("nether_sprouts")
    val CRIMSON_STEM = RotatedPillarBlock(Properties.create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.0f).sound(FSounds.NETHER_STEM)).setRegistryName("crimson_stem")
    val STRIPPED_CRIMSON_STEM = RotatedPillarBlock(Properties.create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.0f).sound(FSounds.NETHER_STEM)).setRegistryName("stripped_crimson_stem")
    val CRIMSON_HYPHAE = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f).sound(FSounds.NETHER_STEM)).setRegistryName("crimson_hyphae")
    val STRIPPED_CRIMSON_HYPHAE = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f).sound(FSounds.NETHER_STEM)).setRegistryName("stripped_crimson_hyphae")
    val CRIMSON_NYLIUM = NyliumBlock(Properties.create(Material.ROCK, MaterialColor.RED).hardnessAndResistance(1.0f).sound(FSounds.NYLIUM).tickRandomly()).setRegistryName("crimson_nylium")
    val CRIMSON_FUNGUS = FungusBlock(Properties.create(Material.PLANTS).hardnessAndResistance(0.0f).doesNotBlockMovement().sound(FSounds.FUNGUS)) {
        FFeatures.HUGE_FUNGUS.configure(HugeFungusFeatureConfig.CRIMSON_PLANTED)
    }.setRegistryName("crimson_fungus")
    val SHROOMLIGHT = Block(Properties.create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.0f).sound(FSounds.SHROOMLIGHT).lightValue(15)).setRegistryName("shroomlight")
    val WEEPING_VINES = WeepingVinesBlock(Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.WEEPING_VINES)).setRegistryKey("weeping_vines")
    val WEEPING_VINES_PLANT = WeepingVinesPlantBlock(Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.WEEPING_VINES)).setRegistryName("weeping_vines_plant")
    val TWISTING_VINES = TwistingVinesBlock(Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.WEEPING_VINES)).setRegistryKey("twisting_vines")
    val TWISTING_VINES_PLANT = TwistingVinesPlantBlock(Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.WEEPING_VINES)).setRegistryName("twisting_vines_plant")
    val CRIMSON_ROOTS = RootsBlock(Properties.create(Material.TALL_PLANTS, MaterialColor.NETHERRACK).doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.ROOTS)).setRegistryName("crimson_roots")
    val CRIMSON_PLANKS = Block(Properties.create(NETHER_WOOD, MaterialColor.NETHERRACK).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)).setRegistryName("crimson_planks")
    val WARPED_PLANKS = Block(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)).setRegistryName("warped_planks")
    val CRIMSON_SLAB = SlabBlock(Properties.from(CRIMSON_PLANKS)).setRegistryName("crimson_slab")
    val WARPED_SLAB = SlabBlock(Properties.from(WARPED_PLANKS)).setRegistryName("warped_slab")
    val CRIMSON_PRESSURE_PLATE = PressurePlateBlock(Sensitivity.EVERYTHING, Properties.create(NETHER_WOOD, MaterialColor.NETHERRACK).doesNotBlockMovement().hardnessAndResistance(0.5f).sound(SoundType.WOOD)).setRegistryName("crimson_pressure_plate")
    val WARPED_PRESSURE_PLATE = PressurePlateBlock(Sensitivity.EVERYTHING, Properties.create(NETHER_WOOD, MaterialColor.CYAN).doesNotBlockMovement().hardnessAndResistance(0.5f).sound(SoundType.WOOD)).setRegistryName("warped_pressure_plate")
    //val CRIMSON_FENCE = FenceBlock()
    val NETHERITE_BLOCK = Block(Properties.create(Material.IRON, MaterialColor.BLACK).hardnessAndResistance(50.0f, 1200.0f).sound(FSounds.NETHERITE)).setRegistryName("netherite_block")
    val ANCIENT_DEBRIS = Block(Properties.create(Material.IRON, MaterialColor.BLACK).hardnessAndResistance(30.0f, 1200.0f).sound(FSounds.ANCIENT_DEBRIS)).setRegistryName("ancient_debris")

    val NYLIUM: Tag<Block> = BlockTags.Wrapper(ResourceLocation(FutureMC.ID, "nylium"))

    fun registerBlocks(blocks: IForgeRegistry<Block>) {

        if (FutureMC.DEBUG) {
            blocks.register(NETHER_GOLD_ORE)
            blocks.register(SOUL_FIRE)
            blocks.register(SOUL_SOIL)
            blocks.register(BASALT)
            blocks.register(POLISHED_BASALT)
            blocks.register(SOUL_FIRE_TORCH)
            blocks.register(SOUL_FIRE_WALL_TORCH)
            blocks.register(SOUL_FIRE_LANTERN)
            blocks.register(WARPED_STEM)
            blocks.register(STRIPPED_WARPED_STEM)
            blocks.register(WARPED_HYPHAE)
            blocks.register(STRIPPED_WARPED_HYPHAE)
            blocks.register(WARPED_NYLIUM)
            blocks.register(WARPED_FUNGUS)
            blocks.register(WARPED_WART_BLOCK)
            blocks.register(WARPED_ROOTS)
            blocks.register(NETHER_SPROUTS)
            blocks.register(CRIMSON_STEM)
            blocks.register(STRIPPED_CRIMSON_STEM)
            blocks.register(CRIMSON_HYPHAE)
            blocks.register(STRIPPED_CRIMSON_HYPHAE)
            blocks.register(CRIMSON_NYLIUM)
            blocks.register(CRIMSON_FUNGUS)
            blocks.register(SHROOMLIGHT)
            blocks.register(WEEPING_VINES)
            blocks.register(WEEPING_VINES_PLANT)
            blocks.register(TWISTING_VINES)
            blocks.register(TWISTING_VINES_PLANT)
            blocks.register(CRIMSON_ROOTS)
            blocks.register(CRIMSON_PLANKS)
            blocks.register(WARPED_PLANKS)
            blocks.register(CRIMSON_SLAB)
            blocks.register(WARPED_SLAB)
            blocks.register(CRIMSON_PRESSURE_PLATE)
            blocks.register(WARPED_PRESSURE_PLATE)
        }

        blocks.register(NETHERITE_BLOCK, Config.netherite)
        blocks.register(ANCIENT_DEBRIS, Config.ancientDebrisEnabled)
    }

    fun setRenderLayers() {
        if (FutureMC.DEBUG) {
            RenderTypeLookup.setRenderLayer(CRIMSON_FUNGUS, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(WARPED_FUNGUS, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(CRIMSON_ROOTS, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(WARPED_ROOTS, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(NETHER_SPROUTS, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(WEEPING_VINES, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(TWISTING_VINES, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(WEEPING_VINES_PLANT, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(TWISTING_VINES_PLANT, RenderType.getCutout())
        }
    }
}