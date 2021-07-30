package thedarkcolour.futuremc.block.buzzybees

import net.minecraft.block.BlockSlime
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityBoat
import net.minecraft.entity.item.EntityMinecart
import net.minecraft.entity.item.EntityTNTPrimed
import net.minecraft.pathfinding.PathNodeType
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.compat.checkQuark
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FSounds
import vazkii.quark.api.INonSticky
import kotlin.math.abs

class HoneyBlockBlock(properties: Properties) : FBlock(properties), INonSticky {
    init {
        blockHardness = 0.0f
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.GROUP
    }

    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return AABB
    }

    // todo if there's issues in multi player see if custom packets will work
    override fun onFallenUpon(worldIn: World, pos: BlockPos, entityIn: Entity, fallDistance: Float) {
        entityIn.playSound(FSounds.HONEY_BLOCK_SLIDE, 1.0f, 1.0f)

        if (worldIn.isRemote) {
            spawnParticles(entityIn, 10)
        }

        entityIn.fall(fallDistance, 0.2f)
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (isSliding(pos, entityIn)) {
            updateVelocity(entityIn)
            slideParticles(worldIn, entityIn)
        } else {
            entityIn.motionX *= 0.4
            entityIn.motionZ *= 0.4
        }
    }

    private fun updateVelocity(entityIn: Entity) {
        if (entityIn.motionY < -0.13) {
            val a = -0.05 / entityIn.motionY
            entityIn.motionX *= a
            entityIn.motionY = -0.05
            entityIn.motionZ *= a
        }
        entityIn.fallDistance = 0.0f
    }

    private fun isSliding(pos: BlockPos, entity: Entity): Boolean {
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

    // todo if there's issues in multi player see if custom packets will work
    private fun slideParticles(worldIn: World, entityIn: Entity) {
        if (hasSlideParticles(entityIn)) {
            if (worldIn.rand.nextInt(5) == 0) {
                entityIn.playSound(FSounds.HONEY_BLOCK_SLIDE, 1.0f, 1.0f)
            }

            if (worldIn.isRemote && worldIn.rand.nextInt(5) == 0) {
                spawnParticles(entityIn, 5)
            }
        }
    }

    private fun hasSlideParticles(entity: Entity): Boolean {
        return entity is EntityLivingBase || entity is EntityMinecart || entity is EntityTNTPrimed || entity is EntityBoat
    }

    // test for client
    private fun spawnParticles(entityIn: Entity, particleCount: Int) {
        for (i in 0 until particleCount) {
            entityIn.world.spawnParticle(
                EnumParticleTypes.BLOCK_CRACK, entityIn.posX, entityIn.posY, entityIn.posZ,
                0.0, 0.0, 0.0, getStateId(defaultState)
            )
        }
    }

    @Suppress("DEPRECATION")
    override fun shouldSideBeRendered(
        blockState: IBlockState,
        blockAccess: IBlockAccess,
        pos: BlockPos,
        side: EnumFacing
    ): Boolean {
        return blockAccess.getBlockState(pos.offset(side)).block == this || super.shouldSideBeRendered(blockState, blockAccess, pos, side)
    }

    override fun isOpaqueCube(state: IBlockState) = false
    override fun isFullBlock(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false
    override fun getRenderLayer() = BlockRenderLayer.TRANSLUCENT
    override fun isStickyBlock(state: IBlockState) = true

    override fun getAiPathNodeType(
        state: IBlockState,
        worldIn: IBlockAccess,
        pos: BlockPos,
        entityLiving: EntityLiving?
    ): PathNodeType? {
        // has the same value as water,
        // so we'll just use water.
        return PathNodeType.WATER
    }

    override fun canStickToBlock(
        world: World, pistonPos: BlockPos, pos: BlockPos, slimePos: BlockPos,
        state: IBlockState, slimeState: IBlockState, direction: EnumFacing
    ): Boolean {
        return !(slimeState.block is BlockSlime || checkQuark()?.isColoredSlime(slimeState) == true)
    }

    companion object {
        private val AABB = cube(1.0, 0.0, 1.0, 15.0, 15.0, 15.0)
    }
}