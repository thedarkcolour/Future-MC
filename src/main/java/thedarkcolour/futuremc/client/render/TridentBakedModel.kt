package thedarkcolour.futuremc.client.render

import net.minecraft.block.state.IBlockState
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.ForgeHooksClient
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.tuple.Pair
import javax.vecmath.Matrix4f

// default to hand model for everything else
class TridentBakedModel(private val hand: IBakedModel, private val inventory: IBakedModel) : IBakedModel {
    override fun getParticleTexture(): TextureAtlasSprite {
        return inventory.particleTexture
    }

    override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): List<BakedQuad> {
        return inventory.getQuads(state, side, rand)
    }

    override fun isBuiltInRenderer(): Boolean {
        return false
    }

    override fun isAmbientOcclusion(): Boolean {
        return inventory.isAmbientOcclusion
    }

    override fun isGui3d() = false

    override fun getOverrides(): ItemOverrideList {
        return inventory.overrides
    }

    override fun handlePerspective(camera: TransformType): Pair<out IBakedModel, Matrix4f> {
        return when (camera) {
            TransformType.FIRST_PERSON_LEFT_HAND,
            TransformType.FIRST_PERSON_RIGHT_HAND,
            TransformType.THIRD_PERSON_LEFT_HAND,
            TransformType.THIRD_PERSON_RIGHT_HAND
            -> ForgeHooksClient.handlePerspective(hand, camera)
            else -> ForgeHooksClient.handlePerspective(inventory, camera)
        }
    }

    companion object {
        val TRIDENT_USE_ACTION = EnumHelper.addAction("TRIDENT_FMC")!!
        @SideOnly(Side.CLIENT)
        val TRIDENT_ARM_POSE = EnumHelper.addEnum(ModelBiped.ArmPose::class.java, "TRIDENT_FMC", emptyArray())!!
    }
}