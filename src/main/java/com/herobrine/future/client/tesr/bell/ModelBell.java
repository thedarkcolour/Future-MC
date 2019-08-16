package com.herobrine.future.client.tesr.bell;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("FieldCanBeLocal")
@SideOnly(Side.CLIENT)
public class ModelBell extends ModelBase {
    private final ModelRenderer bell;
    private final ModelRenderer fixture;

    public ModelBell() {
        this.textureWidth = 32;

        this.bell = new ModelRenderer(this, 0, 0);
        this.bell.addBox(-3.0F, -6.0F, -3.0F, 6, 7, 6);
        this.bell.setRotationPoint(8.0F, 12.0F, 8.0F);

        this.fixture = new ModelRenderer(this, 0, 13);
        this.fixture.addBox(4.0F, 4.0F, 4.0F, 8, 2, 8);
        this.fixture.setRotationPoint(-8.0F, -12.0F, -8.0F);

        this.bell.addChild(fixture);
    }

    public void renderBell(float a, float b, float c) {
        this.bell.rotateAngleX = a;
        this.bell.rotateAngleZ = b;
        this.bell.render(c);
    }
}