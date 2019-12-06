package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import thedarkcolour.futuremc.sound.Sounds;

public class HoneyBlockBlock extends Block {
    private static final VoxelShape SHAPE = makeCuboidShape(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
    public HoneyBlockBlock() {
        super(Properties.create(Material.CLAY, DyeColor.ORANGE).sound(Sounds.HONEY_BLOCK));
        setRegistryName("honey_block");
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
        if (canSlide(pos, entity)) {
            if (entity.getMotion().y < -0.05) {
                entity.setMotion(entity.getMotion().x, -0.05, entity.getMotion().z);
            }

            entity.fallDistance = 0F;
            spawnSlideParticles(worldIn, pos, entity);

            if (worldIn.getGameTime() % 10L == 0L) {
                entity.playSound(Sounds.HONEY_BLOCK_SLIDE, 1F, 1F);
            }
        }

        super.onEntityCollision(state, worldIn, pos, entity);
    }

    private boolean canSlide(BlockPos pos, Entity entity) {
        if (entity.onGround) {
            return false;
        } else if (entity.posY > pos.getY() + 0.9375 - 1.0E-7) {
            return false;
        } else if (entity.getMotion().y >= 0) {
            return false;
        } else {
            double x = Math.abs(pos.getX() + 0.5 - entity.posX);
            double z = Math.abs(pos.getX() + 0.5 - entity.posZ);
            double d = 0.4375 + entity.getSize(Pose.STANDING).width / 2F;
            return x + 1.0E-7 > d || z + 1.0E-7 > d;
        }
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entity, float fallDistance) {
        spawnFallParticles(worldIn, pos, entity);
        entity.fall(fallDistance, 1F);
    }

    private void spawnSlideParticles(World worldIn, BlockPos pos, Entity entity) {
        float width = entity.getSize(Pose.STANDING).width;
        spawnParticles(entity, worldIn, pos, 1, (worldIn.rand.nextFloat() - 0.5) * width, worldIn.rand.nextFloat() / 2F, (worldIn.rand.nextFloat() - 0.5) * width, worldIn.rand.nextFloat() - 0.5, worldIn.rand.nextFloat() - 1F, worldIn.rand.nextFloat() - 0.5);
    }

    private void spawnFallParticles(World worldIn, BlockPos pos, Entity entity) {
        float width = entity.getSize(Pose.STANDING).width;
        spawnParticles(entity, worldIn, pos, 10, (worldIn.rand.nextFloat() - 0.5) * width, 0, (worldIn.rand.nextFloat() - 0.5) * width, worldIn.rand.nextFloat() - 0.5, 0.5, worldIn.rand.nextFloat() - 0.5);
    }

    private void spawnParticles(Entity entity, World worldIn, BlockPos pos, int numOfParticles, double x, double y, double z, double motionX, double motionY, double motionZ) {
        for (int i = 0; i < numOfParticles; ++i) {
            worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, worldIn.getBlockState(pos)), entity.posX + x, entity.posY + y, entity.posZ + z, motionX, motionY, motionZ);
        }
    }

    public static void onEntityJump(final LivingJumpEvent event) {
        LivingEntity entity = event.getEntityLiving();
        int x = MathHelper.floor(entity.posX);
        int y = MathHelper.floor(entity.posY - 0.20000000298023224);
        int z = MathHelper.floor(entity.posZ);
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = entity.world.getBlockState(pos);
        if (state.getBlock() == Blocks.HONEY_BLOCK) {
            entity.setMotion(entity.getMotion().mul(0, 0.5D, 0));
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}