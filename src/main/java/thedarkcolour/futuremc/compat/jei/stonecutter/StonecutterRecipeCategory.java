package thedarkcolour.futuremc.compat.jei.stonecutter;

import com.google.common.collect.Lists;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.compat.jei.FutureMCJEIPlugin;
import thedarkcolour.futuremc.recipe.StonecutterRecipe;
import thedarkcolour.futuremc.recipe.StonecutterRecipes;

import java.util.List;

public class StonecutterRecipeCategory implements IRecipeCategory<StonecutterRecipeWrapper> {
    public static final String NAME = "container.jei.minecraftfuture.stonecutter.name";
    private final IDrawable background;

    public StonecutterRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(FutureMCJEIPlugin.RECIPE_BACKGROUNDS, 0, 45, 84, 44);
    }

    public static List<StonecutterRecipeWrapper> getAllRecipeWrappers() {
        List<StonecutterRecipeWrapper> wrappers = Lists.newArrayList();

        ItemStack input;
        for (StonecutterRecipe recipe : StonecutterRecipes.allRecipes()) {
            input = recipe.getInput();
            for (int i = 0; i < recipe.getTotalOutputs(); ++i) {
                wrappers.add(new StonecutterRecipeWrapper(input, recipe.getOutput(i), i));
            }
        }

        return wrappers;
    }

    @Override
    public String getUid() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return I18n.format(NAME);
    }

    @Override
    public String getModName() {
        return FutureMC.ID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, StonecutterRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();
        group.init(0, true, 2, 13);
        group.set(0, recipeWrapper.input);

        group.init(1, false, 60, 13);
        group.set(1, recipeWrapper.output);

        group.init(2, false, 27, 2);
        group.set(2, recipeWrapper.output);

        /*if (FutureMC.CLIENT) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiStonecutter) {
                ContainerStonecutter container = (ContainerStonecutter) Minecraft.getMinecraft().player.openContainer;
                //noinspection OptionalGetWithoutIsPresent
                container.setCurrentRecipe(StonecutterRecipes.getRecipe(recipeWrapper.input).get());
                Minecraft.getMinecraft().playerController.sendEnchantPacket(Minecraft.getMinecraft().player.openContainer.windowId, recipeWrapper.index);
            }
        }*/
    }
}