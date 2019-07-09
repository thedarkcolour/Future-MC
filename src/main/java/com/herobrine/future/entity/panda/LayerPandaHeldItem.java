package com.herobrine.future.entity.panda;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class LayerPandaHeldItem implements LayerRenderer<EntityPanda> {
    @Override
    public void doRenderLayer(EntityPanda entityPanda, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack itemstack = entityPanda.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        if (entityPanda.func_213556_dX() && !itemstack.isEmpty() && !entityPanda.func_213566_eo()) {
            float f = -0.6F;
            float f1 = 1.4F;
            if (entityPanda.func_213578_dZ()) {
                f -= 0.2F * MathHelper.sin(ageInTicks * 0.6F) + 0.2F;
                f1 -= 0.09F * MathHelper.sin(ageInTicks * 0.6F);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.1F, f1, f);
            Minecraft.getMinecraft().getItemRenderer().renderItem(entityPanda, itemstack, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}