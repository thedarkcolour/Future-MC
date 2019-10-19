package thedarkcolour.futuremc.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import thedarkcolour.core.item.Modeled;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBannerPattern extends Item implements Modeled {
    public ItemBannerPattern() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setTranslationKey(FutureMC.ID + "." + "banner_pattern");
        setRegistryName("banner_pattern");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.MISC : FutureMC.TAB);
        addModel();
    }

    public static BannerPattern getBannerPattern(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 1:
                return BannerPattern.CREEPER;
            case 2:
                return BannerPattern.SKULL;
            case 3:
                return BannerPattern.MOJANG;
            case 4:
                return Init.globeBannerPattern;
            default:
                return BannerPattern.FLOWER;
        }
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