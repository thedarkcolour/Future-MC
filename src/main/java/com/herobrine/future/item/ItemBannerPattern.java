package com.herobrine.future.item;

import com.herobrine.future.FutureMC;
import com.herobrine.future.client.Modeled;
import com.herobrine.future.init.FutureConfig;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBannerPattern extends Item implements Modeled {
    public ItemBannerPattern() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setUnlocalizedName(FutureMC.ID + "." + "banner_pattern");
        setRegistryName("banner_pattern");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.MISC : FutureMC.CREATIVE_TAB);
        addModel();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 1:
            case 2: return EnumRarity.UNCOMMON;
            case 3:
            case 4: return EnumRarity.EPIC;
            default: return super.getRarity(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        switch (stack.getMetadata()) {
            case 1: tooltip.add(I18n.format("item.minecraftfuture.banner_pattern.creeper")); break;
            case 2: tooltip.add(I18n.format("item.minecraftfuture.banner_pattern.skull")); break;
            case 3: tooltip.add(I18n.format("item.minecraftfuture.banner_pattern.thing")); break;
            case 4: tooltip.add(I18n.format("item.minecraftfuture.banner_pattern.globe")); break;
            default: tooltip.add(I18n.format("item.minecraftfuture.banner_pattern.flower"));
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            for (int i = 0; i < 5; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public void model() {
        for(int i = 0; i < 5; i++) {
            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName(), "inventory"));
        }
    }
}