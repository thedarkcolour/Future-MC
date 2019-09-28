package thedarkcolour.futuremc.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.core.item.Modeled;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;

public class ItemDye extends Item implements Modeled {
    public ItemDye() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setTranslationKey(FutureMC.ID + "." + "dye");
        setRegistryName("dye");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.MISC : FutureMC.TAB);
        addModel();
    }

    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName() + "white", "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName() + "blue", "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(getRegistryName() + "brown", "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation(getRegistryName() + "black", "inventory"));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return getTranslationKey() + "." + stack.getMetadata();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            for(int i = 0; i < 4; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if(target instanceof EntitySheep) {
            EntitySheep entityIn = (EntitySheep) target;
            EnumDyeColor color = getColor(stack);

            if(entityIn.getFleeceColor() != color) {
                entityIn.setFleeceColor(color);
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    public EnumDyeColor getColor(ItemStack stack) {
        if(stack.getMetadata() > 3) return EnumDyeColor.WHITE;
        switch (stack.getMetadata()) {
            case 1: return EnumDyeColor.BLUE;
            case 2: return EnumDyeColor.BROWN;
            case 3: return EnumDyeColor.BLACK;
            default: return EnumDyeColor.WHITE;
        }
    }
}
