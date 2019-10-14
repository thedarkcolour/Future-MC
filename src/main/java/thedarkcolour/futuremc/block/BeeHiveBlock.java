package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import thedarkcolour.futuremc.entity.BeeEntity;
import thedarkcolour.futuremc.tile.BeeHiveTileEntity;
import thedarkcolour.futuremc.tile.Tile;

import java.util.List;

import static thedarkcolour.futuremc.block.Props.FACING;
import static thedarkcolour.futuremc.block.Props.HONEY_LEVEL;

public class BeeHiveBlock extends InteractionBlock {
    public BeeHiveBlock(boolean isNest) {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(isNest ? 0.3F : 0.6F));
        setRegistryName(isNest ? "bee_nest" : "beehive");
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(HONEY_LEVEL, 0));
    }

    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.AXE;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Tile.BEEHIVE.create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, HONEY_LEVEL);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof BeeHiveTileEntity) {
            return worldIn.getBlockState(pos).get(HONEY_LEVEL);
        }

        return 0;
    }

    @Override
    public boolean removedByPlayer(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof BeeHiveTileEntity && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) == 0) {
                ((BeeHiveTileEntity)tile).angerBees(player, BeeHiveTileEntity.BeeState.DELIVERED);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            List<BeeEntity> bees = worldIn.getEntitiesWithinAABB(BeeEntity.class, (new AxisAlignedBB(pos)).expand(8.0D, 6.0D, 8.0D));
            if (!bees.isEmpty()) {
                List<PlayerEntity> players = worldIn.getEntitiesWithinAABB(PlayerEntity.class, (new AxisAlignedBB(pos)).expand(8.0D, 6.0D, 8.0D));
                int size = players.size();

                for (BeeEntity bee : bees) {
                    if (bee.getAttackTarget() == null) {
                        bee.setBeeAttacker(players.get(worldIn.rand.nextInt(size)));
                    }
                }
            }
        }

        return super.removedByPlayer(state, worldIn, pos, player, willHarvest, fluid);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
}