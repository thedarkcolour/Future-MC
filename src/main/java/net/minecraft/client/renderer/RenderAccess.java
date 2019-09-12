package net.minecraft.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OtherRenderAccess;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.vector.Vector3f;

public final class RenderAccess extends OtherRenderAccess {
    public static void renderItem(ItemStack stack, ItemCameraTransforms.TransformType cameraTransformType) {
        if(!stack.isEmpty()) {
            IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, null);
            Minecraft.getMinecraft().getRenderItem().renderItemModel(stack, model, cameraTransformType, false);
        }
    }

    public static Vector3f getViewVector(Entity entityIn, int partialTicks) {
        return Minecraft.getMinecraft().renderGlobal.getViewVector(entityIn, partialTicks);
    }
}