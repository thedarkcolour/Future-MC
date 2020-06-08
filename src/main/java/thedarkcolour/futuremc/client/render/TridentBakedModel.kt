package thedarkcolour.futuremc.client.render

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.ForgeHooksClient
import org.apache.commons.lang3.tuple.Pair
import javax.vecmath.Matrix4f

// default to hand model for everything else
class TridentBakedModel(private val hand: IBakedModel, private val inventory: IBakedModel) : IBakedModel {
    override fun getParticleTexture(): TextureAtlasSprite {
        return hand.particleTexture
    }

    override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): List<BakedQuad> {
        return hand.getQuads(state, side, rand)
    }

    override fun isBuiltInRenderer(): Boolean {
        return false
    }

    override fun isAmbientOcclusion(): Boolean {
        return hand.isAmbientOcclusion
    }

    override fun isGui3d() = false

    override fun getOverrides(): ItemOverrideList {
        return hand.overrides
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
}