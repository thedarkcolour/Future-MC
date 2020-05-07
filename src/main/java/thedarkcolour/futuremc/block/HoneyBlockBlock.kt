package thedarkcolour.futuremc.block

import net.minecraft.block.BlockSlime
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.api.IStickyBlock
import thedarkcolour.futuremc.compat.checkQuark
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FSounds
import vazkii.quark.api.INonSticky
import kotlin.math.abs

class HoneyBlockBlock(properties: Properties) : FBlock(properties), IStickyBlock, INonSticky {
    init {
        blockHardness = 0.0f
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.GROUP
    }

    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return AABB
    }

    override fun onFallenUpon(worldIn: World, pos: BlockPos, entityIn: Entity, fallDistance: Float) {
        fallParticles(worldIn, entityIn)
        entityIn.fall(fallDistance, 1.0f)
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (slide(pos, entityIn)) {
            if (entityIn.motionY < -0.05) {
                entityIn.motionY = -0.05
            }
            entityIn.fallDistance = 0.0f
            slideParticles(worldIn, entityIn)
            if (worldIn.worldTime % 10L == 0L) {
                entityIn.playSound(FSounds.HONEY_BLOCK_SLIDE, 1.0f, 1.0f)
            }
        }
        super.onEntityCollision(worldIn, pos, state, entityIn)
    }

    private fun slide(pos: BlockPos, entity: Entity): Boolean {
        return when {
            entity.onGround -> false
            entity.posY > pos.y + 0.9375 - 1.0E-7 -> false
            entity.motionY >= 0.0 -> false
            else -> {
                val x = abs(pos.x + 0.5 - entity.posX)
                val y = abs(pos.z + 0.5 - entity.posZ)
                val width = 0.4375 + entity.width / 2.0f
                x + 1.0E-7 > width || y + 1.0E-7 > width
            }
        }
    }

    private fun slideParticles(worldIn: World, entityIn: Entity) {
        val width = entityIn.width
        spawnParticles(
            entityIn,
            worldIn,
            1,
            (worldIn.rand.nextFloat() - 0.5) * width,
            worldIn.rand.nextFloat() / 2.0f.toDouble(),
            (worldIn.rand.nextFloat() - 0.5) * width,
            worldIn.rand.nextFloat() - 0.5,
            (worldIn.rand.nextFloat() - 1.0f).toDouble(),
            worldIn.rand.nextFloat() - 0.5
        )
    }

    private fun fallParticles(worldIn: World, entityIn: Entity) {
        val width = entityIn.width
        spawnParticles(
            entityIn,
            worldIn,
            10,
            (worldIn.rand.nextFloat().toDouble() - 0.5) * width,
            0.0,
            (worldIn.rand.nextFloat() - 0.5) * width,
            worldIn.rand.nextFloat() - 0.5,
            0.5,
            worldIn.rand.nextFloat() - 0.5
        )
    }

    private fun spawnParticles(
        entity: Entity, worldIn: World, numOfParticles: Int,
        posX: Double, posY: Double, posZ: Double, motX: Double, motY: Double, motZ: Double
    ) {
        for (i in 0 until numOfParticles) {
            worldIn.spawnParticle(
                EnumParticleTypes.BLOCK_CRACK, entity.posX + posX, entity.posY + posY, entity.posZ + posZ,
                motX, motY, motZ, getStateId(defaultState)
            )
        }
    }

    @Suppress("DEPRECATION")
    override fun shouldSideBeRendered(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        return blockAccess.getBlockState(pos.offset(side)).block == this || super.shouldSideBeRendered(blockState, blockAccess, pos, side)
    }

    override fun isOpaqueCube(state: IBlockState) = false

    override fun isFullBlock(state: IBlockState) = false

    override fun isFullCube(state: IBlockState) = false

    override fun getRenderLayer() = BlockRenderLayer.TRANSLUCENT

    override fun isStickyBlock(state: IBlockState) = true

    override fun canStickTo(state: IBlockState, other: IBlockState): Boolean {
        return other.block !is BlockSlime && checkQuark()?.isNotColoredSlime(other) == true
    }

    override fun canStickToBlock(
        world: World, pistonPos: BlockPos, pos: BlockPos, slimePos: BlockPos,
        state: IBlockState, slimeState: IBlockState, direction: EnumFacing
    ): Boolean {
        return canStickTo(state, slimeState)
    }

    companion object {
        private val AABB = makeAABB(1.0, 0.0, 1.0, 15.0, 15.0, 15.0)
    }
}