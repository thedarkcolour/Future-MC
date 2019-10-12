package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thedarkcolour.core.block.BlockBase;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.sound.Sounds;

public class BlockHoneyBlock extends BlockBase {
    private static final AxisAlignedBB AABB = makeAABB(1, 0, 1, 15, 15, 15);

    public BlockHoneyBlock() {
        super("honey_block", Material.CLAY, MapColor.ADOBE, Sounds.HONEY_BLOCK);
        setHardness(0.0F);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AABB;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        fallParticles(worldIn, pos, entityIn);
        entityIn.fall(fallDistance, 1.0F);
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (slide(pos, entityIn)) {
            if (entityIn.motionY < -0.05D) {
                entityIn.motionY = -0.05D;
            }

            entityIn.fallDistance = 0.0F;
            slideParticles(worldIn, pos, entityIn);
            if (worldIn.getWorldTime() % 10L == 0L) {
                entityIn.playSound(Sounds.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }
        }

        super.onEntityCollision(worldIn, pos, state, entityIn);
    }

    private boolean slide(BlockPos pos, Entity entity) {
        if (entity.onGround) {
            return false;
        } else if (entity.posY > pos.getY() + 0.9375D - 1.0E-7D) {
            return false;
        } else if (entity.motionY >= 0.0D) {
            return false;
        } else {
            double x = Math.abs(pos.getX() + 0.5D - entity.posX);
            double y = Math.abs(pos.getZ() + 0.5D - entity.posZ);
            double double_3 = 0.4375D + entity.width / 2.0F;
            return x + 1.0E-7D > double_3 || y + 1.0E-7D > double_3;
        }
    }

    private void slideParticles(World worldIn, BlockPos pos, Entity entityIn) {
        float width = entityIn.width;
        spawnParticles(entityIn, worldIn, pos, 1, (worldIn.rand.nextFloat() - 0.5D) * width, worldIn.rand.nextFloat() / 2.0F, (worldIn.rand.nextFloat() - 0.5D) * width, worldIn.rand.nextFloat() - 0.5D, (worldIn.rand.nextFloat() - 1.0F), worldIn.rand.nextFloat() - 0.5D);
    }

    private void fallParticles(World worldIn, BlockPos pos, Entity entityIn) {
        float width = entityIn.width;
        spawnParticles(entityIn, worldIn, pos, 10, ((double)worldIn.rand.nextFloat() - 0.5D) * width, 0.0D, (worldIn.rand.nextFloat() - 0.5D) * width, worldIn.rand.nextFloat() - 0.5D, 0.5D, worldIn.rand.nextFloat() - 0.5D);
    }

    private void spawnParticles(Entity entity, World worldIn, BlockPos pos, int numOfParticles, double double_1, double double_2, double double_3, double double_4, double double_5, double double_6) {
        for (int i = 0; i < numOfParticles; ++i) {
            worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, entity.posX + double_1, entity.posY + double_2, entity.posZ + double_3, double_4, double_5, double_6, Block.getStateId(getDefaultState()));
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() == this || super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isStickyBlock(IBlockState state) {
        return true;
    }
}