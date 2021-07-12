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
import thedarkcolour.futuremc.block.StairsBlock
import thedarkcolour.futuremc.block.StoneButtonBlock
import thedarkcolour.futuremc.block.vine.TwistingVinesBlock
import thedarkcolour.futuremc.block.vine.TwistingVinesPlantBlock
import thedarkcolour.futuremc.block.vine.WeepingVinesBlock
import thedarkcolour.futuremc.block.vine.WeepingVinesPlantBlock
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.feature.HugeFungusFeatureConfig

@Suppress("HasPlatformType", "MemberVisibilityCanBePrivate")
object FBlocks {
    // @formatter:off
    val NETHER_WOOD = Material.Builder(MaterialColor.WOOD).build()

    val NETHER_GOLD_ORE = NetherGoldOreBlock(Properties.create(Material.ROCK).hardnessAndResistance(3.0f, 3.0f).sound(FSounds.NETHER_GOLD_ORE)).setRegistryKey("nether_gold_ore")
    val SOUL_FIRE = SoulFireBlock(Properties.create(Material.FIRE, MaterialColor.LIGHT_BLUE).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0f).lightValue(10).sound(SoundType.CLOTH).noDrops()).setRegistryKey("soul_fire")
    val SOUL_SOIL = Block(Properties.create(Material.EARTH, MaterialColor.BROWN).hardnessAndResistance(0.5f).sound(FSounds.SOUL_SOIL)).setRegistryKey("soul_soil")
    val BASALT = RotatedPillarBlock(Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(1.25f, 4.2f).sound(FSounds.BASALT)).setRegistryKey("basalt")
    val POLISHED_BASALT = RotatedPillarBlock(Properties.from(BASALT)).setRegistryKey("polished_basalt")
    val SOUL_TORCH = StandingSoulFireTorchBlock(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0f).lightValue(10).sound(SoundType.WOOD)).setRegistryKey("soul_torch")
    val SOUL_WALL_TORCH = WallSoulFireTorchBlock(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0f).lightValue(10).sound(SoundType.WOOD).lootFrom(SOUL_TORCH)).setRegistryKey("soul_wall_torch")
    val SOUL_FIRE_LANTERN = LanternBlock(Properties.create(Material.IRON).hardnessAndResistance(3.5f).sound(SoundType.LANTERN).lightValue(10).nonOpaque()).setRegistryKey("soul_lantern")
    val WARPED_STEM = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(1.0f).sound(FSounds.NETHER_STEM)).setRegistryKey("warped_stem")
    val STRIPPED_WARPED_STEM = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(1.0f).sound(FSounds.NETHER_STEM)).setRegistryKey("stripped_warped_stem")
    val WARPED_HYPHAE = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f).sound(FSounds.NETHER_STEM)).setRegistryKey("warped_hyphae")
    val STRIPPED_WARPED_HYPHAE = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f).sound(FSounds.NETHER_STEM)).setRegistryKey("stripped_warped_hyphae")
    val WARPED_NYLIUM = NyliumBlock(Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(1.0f).sound(FSounds.NYLIUM).tickRandomly()).setRegistryKey("warped_nylium")
    val WARPED_FUNGUS = FungusBlock(Properties.create(Material.PLANTS).hardnessAndResistance(0.0f).doesNotBlockMovement().sound(FSounds.FUNGUS)) {
        FFeatures.HUGE_FUNGUS.configure(HugeFungusFeatureConfig.WARPED_PLANTED)
    }.setRegistryKey("warped_fungus")
    val WARPED_WART_BLOCK = Block(Properties.create(Material.ORGANIC, MaterialColor.CYAN).hardnessAndResistance(1.0f)).setRegistryKey("warped_wart_block")
    val WARPED_ROOTS = RootsBlock(Properties.create(Material.TALL_PLANTS, MaterialColor.CYAN).doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.ROOTS)).setRegistryKey("warped_roots")
    val NETHER_SPROUTS = SproutsBlock(Properties.create(Material.TALL_PLANTS, MaterialColor.CYAN).doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.NETHER_SPROUTS)).setRegistryKey("nether_sprouts")
    val CRIMSON_STEM = RotatedPillarBlock(Properties.create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.0f).sound(FSounds.NETHER_STEM)).setRegistryKey("crimson_stem")
    val STRIPPED_CRIMSON_STEM = RotatedPillarBlock(Properties.create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.0f).sound(FSounds.NETHER_STEM)).setRegistryKey("stripped_crimson_stem")
    val CRIMSON_HYPHAE = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f).sound(FSounds.NETHER_STEM)).setRegistryKey("crimson_hyphae")
    val STRIPPED_CRIMSON_HYPHAE = RotatedPillarBlock(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f).sound(FSounds.NETHER_STEM)).setRegistryKey("stripped_crimson_hyphae")
    val CRIMSON_NYLIUM = NyliumBlock(Properties.create(Material.ROCK, MaterialColor.RED).hardnessAndResistance(1.0f).sound(FSounds.NYLIUM).tickRandomly()).setRegistryKey("crimson_nylium")
    val CRIMSON_FUNGUS = FungusBlock(Properties.create(Material.PLANTS).hardnessAndResistance(0.0f).doesNotBlockMovement().sound(FSounds.FUNGUS)) {
        FFeatures.HUGE_FUNGUS.configure(HugeFungusFeatureConfig.CRIMSON_PLANTED)
    }.setRegistryKey("crimson_fungus")
    val SHROOMLIGHT = Block(Properties.create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.0f).sound(FSounds.SHROOMLIGHT).lightValue(15)).setRegistryKey("shroomlight")
    val WEEPING_VINES = WeepingVinesBlock(Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.WEEPING_VINES)).setRegistryKey("weeping_vines")
    val WEEPING_VINES_PLANT = WeepingVinesPlantBlock(Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.WEEPING_VINES)).setRegistryKey("weeping_vines_plant")
    val TWISTING_VINES = TwistingVinesBlock(Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.WEEPING_VINES)).setRegistryKey("twisting_vines")
    val TWISTING_VINES_PLANT = TwistingVinesPlantBlock(Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.WEEPING_VINES)).setRegistryKey("twisting_vines_plant")
    val CRIMSON_ROOTS = RootsBlock(Properties.create(Material.TALL_PLANTS, MaterialColor.NETHERRACK).doesNotBlockMovement().hardnessAndResistance(0.0f).sound(FSounds.ROOTS)).setRegistryKey("crimson_roots")
    val CRIMSON_PLANKS = Block(Properties.create(NETHER_WOOD, MaterialColor.NETHERRACK).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)).setRegistryKey("crimson_planks")
    val WARPED_PLANKS = Block(Properties.create(NETHER_WOOD, MaterialColor.CYAN).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)).setRegistryKey("warped_planks")
    val CRIMSON_SLAB = SlabBlock(Properties.from(CRIMSON_PLANKS)).setRegistryKey("crimson_slab")
    val WARPED_SLAB = SlabBlock(Properties.from(WARPED_PLANKS)).setRegistryKey("warped_slab")
    val CRIMSON_PRESSURE_PLATE = PressurePlateBlock(Sensitivity.EVERYTHING, Properties.create(NETHER_WOOD, MaterialColor.NETHERRACK).doesNotBlockMovement().hardnessAndResistance(0.5f).sound(SoundType.WOOD)).setRegistryKey("crimson_pressure_plate")
    val WARPED_PRESSURE_PLATE = PressurePlateBlock(Sensitivity.EVERYTHING, Properties.create(NETHER_WOOD, MaterialColor.CYAN).doesNotBlockMovement().hardnessAndResistance(0.5f).sound(SoundType.WOOD)).setRegistryKey("warped_pressure_plate")
    //val CRIMSON_FENCE = FenceBlock() todo
    val NETHERITE_BLOCK = BeaconBaseBlock(Properties.create(Material.IRON, MaterialColor.BLACK).hardnessAndResistance(50.0f, 1200.0f).sound(FSounds.NETHERITE)).setRegistryKey("netherite_block")
    val ANCIENT_DEBRIS = Block(Properties.create(Material.IRON, MaterialColor.BLACK).hardnessAndResistance(30.0f, 1200.0f).sound(FSounds.ANCIENT_DEBRIS)).setRegistryKey("ancient_debris")
    val BLACKSTONE = Block(Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(1.5f, 6.0f)).setRegistryKey("blackstone")
    val BLACKSTONE_STAIRS = StairsBlock(BLACKSTONE::getDefaultState, Properties.from(BLACKSTONE)).setRegistryKey("blackstone_stairs")
    val BLACKSTONE_WALL = FWallBlock(Properties.from(BLACKSTONE)).setRegistryKey("blackstone_wall")
    val BLACKSTONE_SLAB = SlabBlock(Properties.from(BLACKSTONE).hardnessAndResistance(2.0F, 6.0F)).setRegistryKey("blackstone_slab")
    val POLISHED_BLACKSTONE = Block(Properties.from(BLACKSTONE).hardnessAndResistance(2.0F, 6.0F)).setRegistryKey("polished_blackstone")
    val POLISHED_BLACKSTONE_BRICKS = Block(Properties.from(POLISHED_BLACKSTONE).hardnessAndResistance(1.5F, 6.0F)).setRegistryKey("polished_blackstone_bricks")
    val CRACKED_POLISHED_BLACKSTONE_BRICKS = Block(Properties.from(POLISHED_BLACKSTONE_BRICKS)).setRegistryKey("cracked_polished_blackstone_bricks")
    val CHISELED_POLISHED_BLACKSTONE = Block(Properties.from(POLISHED_BLACKSTONE).hardnessAndResistance(1.5F, 6.0F)).setRegistryKey("chiseled_polished_blackstone")
    val POLISHED_BLACKSTONE_BRICK_SLAB = SlabBlock(Properties.from(POLISHED_BLACKSTONE_BRICKS).hardnessAndResistance(2.0F, 6.0F)).setRegistryKey("polished_blackstone_brick_slab")
    val POLISHED_BLACKSTONE_BRICK_STAIRS = StairsBlock(POLISHED_BLACKSTONE_BRICKS::getDefaultState, Properties.from(POLISHED_BLACKSTONE_BRICKS)).setRegistryKey("polished_blackstone_brick_stairs")
    val POLISHED_BLACKSTONE_BRICK_WALL = FWallBlock(Properties.from(POLISHED_BLACKSTONE_BRICKS)).setRegistryKey("polished_blackstone_brick_wall")
    val GILDED_BLACKSTONE = Block(Properties.from(BLACKSTONE).sound(FSounds.GILDED_BLACKSTONE)).setRegistryKey("gilded_blackstone")
    val POLISHED_BLACKSTONE_STAIRS = StairsBlock(POLISHED_BLACKSTONE::getDefaultState, Properties.from(POLISHED_BLACKSTONE)).setRegistryKey("polished_blackstone_stairs")
    val POLISHED_BLACKSTONE_SLAB = SlabBlock(Properties.from(POLISHED_BLACKSTONE)).setRegistryKey("polished_blackstone_slab")
    val POLISHED_BLACKSTONE_PRESSURE_PLATE = PressurePlateBlock(Sensitivity.MOBS, Properties.create(Material.ROCK, MaterialColor.BLACK).doesNotBlockMovement().hardnessAndResistance(0.5f)).setRegistryKey("polished_blackstone_pressure_plate")
    val POLISHED_BLACKSTONE_BUTTON = StoneButtonBlock(Properties.create(Material.MISCELLANEOUS, MaterialColor.BLACK).doesNotBlockMovement().hardnessAndResistance(0.5f)).setRegistryKey("polished_blackstone_button")
    val POLISHED_BLACKSTONE_WALL = FWallBlock(Properties.from(POLISHED_BLACKSTONE)).setRegistryKey("polished_blackstone_wall")
    val CHISELED_NETHER_BRICKS = Block(Properties.create(Material.ROCK, MaterialColor.NETHERRACK).hardnessAndResistance(2.0F, 6.0F).sound(FSounds.NETHER_BRICK)).setRegistryKey("chiseled_nether_bricks")
    val CRACKED_NETHER_BRICKS = Block(Properties.create(Material.ROCK, MaterialColor.NETHERRACK).hardnessAndResistance(2.0F, 6.0F).sound(FSounds.NETHER_BRICK)).setRegistryKey("cracked_nether_bricks")
    val QUARTZ_BRICKS = Block(Properties.from(Blocks.QUARTZ_BLOCK)).setRegistryKey("quartz_bricks")
    val CRYING_OBSIDIAN = CryingObsidianBlock(Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(50.0f, 1200.0f).lightValue(10)).setRegistryKey("crying_obsidian")
    val RESPAWN_ANCHOR = RespawnAnchorBlock(Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(50.0f, 1200.0f)).setRegistryKey("respawn_anchor")

    val NYLIUM: Tag<Block> = BlockTags.Wrapper(ResourceLocation(FutureMC.ID, "nylium"))
    val WALL_POST_OVERRIDE: Tag<Block> = BlockTags.Wrapper(ResourceLocation(FutureMC.ID, "wall_post_override"))
    // @formatter:on

    fun registerBlocks(blocks: IForgeRegistry<Block>) {

        blocks.register(NETHER_GOLD_ORE)
        blocks.register(SOUL_FIRE)
        blocks.register(SOUL_SOIL)
        blocks.register(BASALT)
        blocks.register(POLISHED_BASALT)
        blocks.register(SOUL_TORCH)
        blocks.register(SOUL_WALL_TORCH)
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
        if (FConfig.blackstone.value) {
            blocks.register(BLACKSTONE)
            blocks.register(BLACKSTONE_STAIRS)
            blocks.register(BLACKSTONE_WALL)
            blocks.register(BLACKSTONE_SLAB)
            blocks.register(POLISHED_BLACKSTONE)
            blocks.register(POLISHED_BLACKSTONE_BRICKS)
            blocks.register(CRACKED_POLISHED_BLACKSTONE_BRICKS)
            blocks.register(CHISELED_POLISHED_BLACKSTONE)
            blocks.register(POLISHED_BLACKSTONE_BRICK_SLAB)
            blocks.register(POLISHED_BLACKSTONE_BRICK_STAIRS)
            blocks.register(POLISHED_BLACKSTONE_BRICK_WALL)
            blocks.register(GILDED_BLACKSTONE)
            blocks.register(POLISHED_BLACKSTONE_STAIRS)
            blocks.register(POLISHED_BLACKSTONE_SLAB)
            blocks.register(POLISHED_BLACKSTONE_PRESSURE_PLATE)
            blocks.register(POLISHED_BLACKSTONE_BUTTON)
            blocks.register(POLISHED_BLACKSTONE_WALL)
        }

        blocks.registerIf(NETHERITE_BLOCK, FConfig.netherite)
        blocks.registerIf(ANCIENT_DEBRIS, FConfig.ancientDebrisEnabled)
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
            RenderTypeLookup.setRenderLayer(SOUL_TORCH, RenderType.getCutout())
            RenderTypeLookup.setRenderLayer(SOUL_WALL_TORCH, RenderType.getCutout())
        }
    }
}