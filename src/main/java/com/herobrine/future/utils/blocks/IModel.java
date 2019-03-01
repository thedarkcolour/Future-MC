package com.herobrine.future.utils.blocks;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public interface IModel {
    default void model(Item item, int metadata, ModelResourceLocation model) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, model);
    }

    void models();
}
