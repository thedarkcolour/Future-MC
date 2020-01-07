package thedarkcolour.futuremc.block

import net.minecraft.block.BlockHorizontal.FACING
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.BlockBase
import thedarkcolour.core.gui.Gui
import thedarkcolour.futuremc.init.FBlocks
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced.TileBlastFurnace
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced.TileSmoker
import java.util.*

class BlockFurnaceAdvanced(private val type: FurnaceType) : BlockBase(type.type), ITileEntityProvider {
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        if (type == FurnaceType.SMOKER) {
            return TileSmoker()
        }
        return if (type == FurnaceType.BLAST_FURNACE) {
            TileBlastFurnace()
        } else null
    }

    override fun getLightValue(state: IBlockState): Int {
        return if (state.getValue(BlockFurnaceAdvanced.LIT)) 13 else 0
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) {
            return true
        }
        val te = worldIn.getTileEntity(pos) as? TileFurnaceAdvanced ?: return false
        val block = worldIn.getBlockState(pos).block
        if (block == FBlocks.BLAST_FURNACE) {
            if (te !is TileBlastFurnace) {
                return false
            }
        }
        if (block == FBlocks.SMOKER) {
            if (te !is TileSmoker) {
                return false
            }
        }
        Gui.FURNACE.open(playerIn, worldIn, pos)
        return true
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING, LIT)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(LIT, meta and 4 != 0).withProperty(FACING, EnumFacing.byHorizontalIndex(meta and -5))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return (if (state.getValue(LIT)) 4 else 0) or state.getValue<EnumFacing>(FACING).horizontalIndex
    }

    override fun getStateForPlacement(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState {
        return defaultState.withProperty(FACING, placer.horizontalFacing.opposite).withProperty(LIT, false)
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val tile = worldIn.getTileEntity(pos)
        if (tile is TileFurnaceAdvanced) {
            InventoryHelper.dropInventoryItems(worldIn, pos, tile)
        }
        worldIn.removeTileEntity(pos)
    }

    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        if (stateIn.getValue(LIT)) {
            val facing = stateIn.getValue(FACING)
            val d0 = pos.x.toDouble() + 0.5
            val d1 = pos.y.toDouble() + rand.nextDouble() * 6.0 / 16.0
            val d2 = pos.z.toDouble() + 0.5
            val d3 = rand.nextDouble() * 0.6 - 0.3
            if (rand.nextDouble() < 0.1) {
                worldIn.playSound(pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false)
            }
            if (worldIn.getBlockState(pos).block == FBlocks.SMOKER) {
                when (facing) {
                    EnumFacing.WEST -> {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52, d1, d2 + d3, 0.0, 0.0, 0.0)
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52, d1, d2 + d3, 0.0, 0.0, 0.0)
                    }
                    EnumFacing.EAST -> {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52, d1, d2 + d3, 0.0, 0.0, 0.0)
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52, d1, d2 + d3, 0.0, 0.0, 0.0)
                    }
                    EnumFacing.NORTH -> {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 - 0.52, 0.0, 0.0, 0.0)
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 - 0.52, 0.0, 0.0, 0.0)
                    }
                    EnumFacing.SOUTH -> {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + 0.52, 0.0, 0.0, 0.0)
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + 0.52, 0.0, 0.0, 0.0)
                    }
                    else -> {}
                }
            }
        }
    }

    enum class FurnaceType {
        SMOKER, BLAST_FURNACE;

        val type = name.toLowerCase()

        fun canCraft(stack: ItemStack): Boolean {
            return if (this == BLAST_FURNACE) {
                BlastFurnaceRecipes.getRecipe(stack).isPresent
            } else {
                SmokerRecipes.getRecipe(stack).isPresent
            }
        }

    }

    companion object {
        val LIT: PropertyBool = PropertyBool.create("lit")
    }
}/*
    public static final class Recipes {
        private static final ArrayList<Recipe> SMOKER_RECIPES = Lists.newArrayListWithExpectedSize(8);
        private static final ArrayList<Recipe> BLAST_FURNACE_RECIPES = Lists.newArrayListWithExpectedSize(8);

        public static void setup() {
            smokerRecipe(new ItemStack(Items.FISH), new ItemStack(Items.COOKED_FISH));
            smokerRecipe(new ItemStack(Items.CHICKEN), new ItemStack(Items.COOKED_CHICKEN));
            smokerRecipe(new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP));
            smokerRecipe(new ItemStack(Items.FISH, 1, 1), new ItemStack(Items.COOKED_FISH, 1, 1));
            smokerRecipe(new ItemStack(Items.BEEF), new ItemStack(Items.COOKED_BEEF));
            smokerRecipe(new ItemStack(Items.MUTTON), new ItemStack(Items.COOKED_MUTTON));
            smokerRecipe(new ItemStack(Items.RABBIT), new ItemStack(Items.COOKED_RABBIT));
            smokerRecipe(new ItemStack(Items.POTATO), new ItemStack(Items.BAKED_POTATO));
            blastFurnaceRecipe(new ItemStack(Blocks.COAL_ORE), new ItemStack(Items.COAL));
            blastFurnaceRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT));
            blastFurnaceRecipe(new ItemStack(Blocks.DIAMOND_ORE), new ItemStack(Items.DIAMOND));
            blastFurnaceRecipe(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(Items.EMERALD));
            blastFurnaceRecipe(new ItemStack(Blocks.GOLD_ORE), new ItemStack(Items.GOLD_INGOT));
            blastFurnaceRecipe(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Items.DYE, 1, 4));
            blastFurnaceRecipe(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE));
            blastFurnaceRecipe(new ItemStack(Blocks.QUARTZ_ORE), new ItemStack(Items.QUARTZ));
        }

        public static void blastFurnaceRecipe(ItemStack input, ItemStack output) {
            BLAST_FURNACE_RECIPES.add(new Recipe(input, output));
        }

        public static void smokerRecipe(ItemStack input, ItemStack output) {
            SMOKER_RECIPES.add(new Recipe(input, output));
        }

        public static ArrayList<Recipe> getAllRecipes() {
            return SMOKER_RECIPES;
        }

        public static ArrayList<Recipe> getBlastFurnaceRecipes() {
            return BLAST_FURNACE_RECIPES;
        }

        public static void removeSmokerRecipe(ItemStack input) {
            SMOKER_RECIPES.removeIf(recipe -> recipe.input.isItemEqual(input));
        }

        public static void removeBlastFurnaceRecipe(ItemStack input) {
            BLAST_FURNACE_RECIPES.removeIf(recipe -> recipe.input.isItemEqual(input));
        }

        public static void clearSmoker() {
            SMOKER_RECIPES.clear();
        }

        public static void clearBlastFurnace() {
            BLAST_FURNACE_RECIPES.clear();
        }
    }

    public static class Recipe {
        public final ItemStack input;
        public final ItemStack output;

        public Recipe(ItemStack input, ItemStack output) {
            this.input = input;
            this.output = output;
        }
    }
/*
    public static final class ValidItemExceptionsForSmoker {
        private static final ArrayList<ItemStack> VALID_ITEMS = new ArrayList<>();
        private static final ArrayList<ItemStack> BLACKLIST = new ArrayList<>();

        public static boolean isItemValid(ItemStack stack) {
            if(stack.isEmpty() || VALID_ITEMS.isEmpty()) {
                return false;
            }
            return VALID_ITEMS.stream().anyMatch(stack1 -> stack1.isItemEqualIgnoreDurability(stack));
        }

        public static boolean isBlacklisted(ItemStack stack) {
            return BLACKLIST.stream().anyMatch(stack1 -> stack1.isItemEqualIgnoreDurability(stack));
        }

        public static void addSmeltableItem(ItemStack stack) {
            if(isBlacklisted(stack)) {
                FutureMC.logger.log(Level.WARN, "Tried to add valid Smoker input that was removed by ZenScript " + stack.getItem().getRegistryName());
            }
            VALID_ITEMS.add(stack);
        }

        public static void removeSmeltableItem(ItemStack stack) {
            if(isItemValid(stack)) {
                FutureMC.logger.log(Level.WARN, "Tried to remove valid Smoker input that was added by ZenScript " + stack.getItem().getRegistryName());
            }
            BLACKLIST.add(stack);
        }
    }

    public static final class ValidItemExceptionsForBlastFurnace {
        private static final ArrayList<ItemStack> VALID_ITEMS = new ArrayList<>();
        private static final ArrayList<ItemStack> BLACKLIST = new ArrayList<>();

        public static boolean isItemValid(ItemStack stack) {
            if(stack.isEmpty() || VALID_ITEMS.isEmpty()) {
                return false;
            }
            return VALID_ITEMS.stream().anyMatch(stack1 -> stack1.isItemEqualIgnoreDurability(stack));
        }

        public static boolean isBlacklisted(ItemStack stack) {
            return BLACKLIST.stream().anyMatch(stack1 -> stack1.isItemEqualIgnoreDurability(stack));
        }

        public static void addSmeltableItem(ItemStack stack) {
            if(isBlacklisted(stack)) {
                FutureMC.logger.log(Level.WARN, "Tried to add valid BlastFurnace input that was removed by ZenScript " + stack.getItem().getRegistryName());
            }

            VALID_ITEMS.add(stack);
        }

        public static void removeSmeltableItem(ItemStack stack) {
            if(isItemValid(stack)) {
                FutureMC.logger.log(Level.WARN, "Tried to remove valid BlastFurnace input that was added by ZenScript " + stack.getItem().getRegistryName());
            }
            BLACKLIST.add(stack);
        }
    }*/
       companion object  {
          val  LIT:/*@@remtuc@@*/PropertyBool? = PropertyBool.create("lit")
          val  FACING:/*@@mjgqxq@@*/PropertyDirection? = BlockHorizontal.FACING
    }
    init {
        setHardness(3.5f)
        setCreativeTab(if (FConfig.useVanillaCreativeTabs)CreativeTabs.DECORATIONS else FutureMC.TAB)
        this.type = type
    }
}*/