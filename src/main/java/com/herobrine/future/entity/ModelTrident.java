package com.herobrine.future.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTrident extends ModelBase {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("minecraftfuture:textures/entity/trident.png");
    private final ModelRenderer modelRenderer;

    public ModelTrident() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.modelRenderer = new ModelRenderer(this, 0, 0);
        this.modelRenderer.addBox(-0.5F, -4.0F, -0.5F, 1, 31, 1, 0.0F);
        ModelRenderer model = new ModelRenderer(this, 4, 0);
        model.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1);
        this.modelRenderer.addChild(model);
        ModelRenderer model1 = new ModelRenderer(this, 4, 3);
        model1.addBox(-2.5F, -3.0F, -0.5F, 1, 4, 1);
        this.modelRenderer.addChild(model1);
        ModelRenderer model2 = new ModelRenderer(this, 4, 3);
        model2.mirror = true;
        model2.addBox(1.5F, -3.0F, -0.5F, 1, 4, 1);
        this.modelRenderer.addChild(model2);
    }

    public void renderer() {
        this.modelRenderer.render(0.0625F);
    }
}
