package thedarkcolour.futuremc.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.compat.jei.recipe.SimpleRecipeWrapper;

public class AdvancedFurnaceRecipeCategory implements IRecipeCategory<SimpleRecipeWrapper> {
    public static final String SMOKING = "container.jei.futuremc.smoker.name";
    public static final String BLASTING = "container.jei.futuremc.blast_furnace.name";

    private final String title;
    private final IDrawable background;
    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;

    public AdvancedFurnaceRecipeCategory(IGuiHelper helper, String title, Block block) {
        this.title = title;

        this.background = helper.createDrawable(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 114, 82, 54);
        this.animatedFlame = helper.createAnimatedDrawable(helper.createDrawable(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 82, 114, 14, 14), 300, IDrawableAnimated.StartDirection.TOP, true);
        this.arrow = helper.drawableBuilder(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 82, 128, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.icon = helper.createDrawableIngredient(new ItemStack(block));
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        animatedFlame.draw(minecraft, 1, 20);
        arrow.draw(minecraft, 24, 18);
    }

    @Override
    public String getUid() {
        return title;
    }

    @Override
    public String getTitle() {
        return I18n.format(title);
    }

    @Override
    public String getModName() {
        return FutureMC.ID;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SimpleRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, false, 60, 18);

        guiItemStacks.set(ingredients);
    }
}