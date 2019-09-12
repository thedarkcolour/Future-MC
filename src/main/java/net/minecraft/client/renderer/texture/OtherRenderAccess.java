package net.minecraft.client.renderer.texture;

import net.minecraft.util.ResourceLocation;

@SuppressWarnings("NonFinalUtilityClass")
public class OtherRenderAccess {
    public static TextureAtlasSprite makeTextureAtlasSprite(ResourceLocation loc) {
        return TextureAtlasSprite.makeAtlasSprite(loc);
    }
}
