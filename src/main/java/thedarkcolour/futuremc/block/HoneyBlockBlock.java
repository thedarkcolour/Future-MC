package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.futuremc.sound.Sounds;

public class HoneyBlockBlock extends Block {
    private static final VoxelShape SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 15, 15);
    private static SoundType HONEY_BLOCK = new SoundType(1.0F, 1.0F, null, null, null, null, null) {
        @Override
        public SoundEvent getBreakSound() {
            return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("honey_block_break"));
        }

        @Override
        public SoundEvent getStepSound() {
            return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("honey_block_step"));
        }

        @Override
        public SoundEvent getPlaceSound() {
            return getBreakSound();
        }

        @Override
        public SoundEvent getHitSound() {
            return getBreakSound();
        }

        @Override
        public SoundEvent getFallSound() {
            return getStepSound();
        }
    };

    public HoneyBlockBlock() {
        super(Block.Properties.create(Material.CLAY, DyeColor.ORANGE).sound(HONEY_BLOCK));
        setRegistryName("honey_block");
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        fallParticles(worldIn, pos, entityIn);
        entityIn.fall(fallDistance, 1.0F);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (slide(pos, entityIn)) {
            if (entityIn.getMotion().y < -0.05D) {
                entityIn.setMotion(entityIn.getMotion().x, -0.05D, entityIn.getMotion().x);
            }

            entityIn.fallDistance = 0.0F;
            slideParticles(worldIn, pos, entityIn);
            if (worldIn.getGameTime() % 10L == 0L) {
                entityIn.playSound(Sounds.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }
        }

        super.onEntityCollision(state, worldIn, pos, entityIn);
    }

    private boolean slide(BlockPos pos, Entity entity) {
        if (entity.onGround) {
            return false;
        } else if (entity.posY > pos.getY() + 0.9375D - 1.0E-7D) {
            return false;
        } else if (entity.getMotion().y >= 0.0D) {
            return false;
        } else {
            double x = Math.abs(pos.getX() + 0.5D - entity.posX);
            double y = Math.abs(pos.getZ() + 0.5D - entity.posZ);
            double double_3 = 0.4375D + entity.getSize(Pose.STANDING).width / 2.0F;
            return x + 1.0E-7D > double_3 || y + 1.0E-7D > double_3;
        }
    }

    private void slideParticles(World worldIn, BlockPos pos, Entity entityIn) {
        float width = entityIn.getSize(Pose.STANDING).width;
        spawnParticles(entityIn, worldIn, pos, 1, (worldIn.rand.nextFloat() - 0.5D) * width, worldIn.rand.nextFloat() / 2.0F, (worldIn.rand.nextFloat() - 0.5D) * width, worldIn.rand.nextFloat() - 0.5D, (worldIn.rand.nextFloat() - 1.0F), worldIn.rand.nextFloat() - 0.5D);
    }

    private void fallParticles(World worldIn, BlockPos pos, Entity entityIn) {
        float width = entityIn.getSize(Pose.STANDING).width;
        spawnParticles(entityIn, worldIn, pos, 10, (worldIn.rand.nextFloat() - 0.5D) * width, 0.0D, (worldIn.rand.nextFloat() - 0.5D) * width, worldIn.rand.nextFloat() - 0.5D, 0.5D, worldIn.rand.nextFloat() - 0.5D);
    }

    private void spawnParticles(Entity entity, World worldIn, BlockPos pos, int numOfParticles, double x, double y, double z, double motionX, double motionY, double motionZ) {
        for (int i = 0; i < numOfParticles; ++i) {
            worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, worldIn.getBlockState(pos)), entity.posX + x, entity.posY + y, entity.posZ + z, motionX, motionY, motionZ);
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public static void onEntityJump(final LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntityLiving();
        int x = MathHelper.floor(entity.posX);
        int y = MathHelper.floor(entity.posY - 0.20000000298023224D);
        int z = MathHelper.floor(entity.posZ);
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = entity.world.getBlockState(pos);
        if (state.getBlock() == Blocks.HONEY_BLOCK) {
            entity.setMotion(entity.getMotion().mul(0, 0.5D, 0));
        }
    }
}