package net.minecraft.client.renderer;

// TODO Replace with reflection
//      Comment out until use
//      Use Lazy init for method objects and invoke those instead of reflecting each time
public final class RenderUtils {
    /*public static void renderItem(ItemStack stack, ItemCameraTransforms.TransformType cameraTransformType) {
        if(!stack.isEmpty()) {
            IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, null);
            Minecraft.getMinecraft().getRenderItem().renderItemModel(stack, model, cameraTransformType, false);
        }
    }

    public static TextureAtlasSprite makeTextureAtlasSprite(ResourceLocation loc) {
        return ReflectUtils.invokeMethod(TextureAtlasSprite.class, "makeAtlasSprite", loc);
    }

    public static Vector3f getViewVector(Entity entityIn, int partialTicks) {
        return ReflectUtils.invokeMethod(RenderGlobal.class, "getViewVector", Minecraft.getMinecraft().renderGlobal, entityIn, partialTicks);
    }*/
}