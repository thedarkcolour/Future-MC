package thedarkcolour.futuremc.block.updateaquatic;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thedarkcolour.core.block.FBlock;

import java.util.Random;

public class BlockPowderSnow extends FBlock {
    // todo powder snow sound type
    public BlockPowderSnow(Properties properties) {
        super(properties);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        return block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!(entityIn instanceof EntityLivingBase) || worldIn.getBlockState(entityIn.getPosition()) == state) {
            entityIn.fallDistance = 0.0f;
            entityIn.motionX *= 0.9;
            entityIn.motionY *= 1.5;
            entityIn.motionZ *= 0.9;

            if (worldIn.isRemote) {
                Random rand = worldIn.rand;
                boolean flag = entityIn.lastTickPosX != entityIn.posX || entityIn.lastTickPosZ != entityIn.posZ;
                if (flag && rand.nextBoolean()) {
                    //worldIn.spawnParticle(FParticles.SNOWFLAKE, entityIn.posX, pos.getY() + 1, entityIn.posZ, randomBetween(rand, -0.083333336f, 0.083333336f), 0.05f, randomBetween(rand, -0.083333336f, 0.083333336f));
                }
            }

            //entityIn.getCapability(FPlayerDataProvider.DATA_CAP)
        }
    }

    public static float randomBetween(Random rand, float min, float max) {
        return rand.nextFloat() * (max - min) + min;
    }
}
