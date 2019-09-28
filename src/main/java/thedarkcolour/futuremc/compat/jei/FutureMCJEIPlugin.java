package thedarkcolour.futuremc.compat.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientBlacklist;

//@JEIPlugin
public class FutureMCJEIPlugin {//implements IModPlugin {
    //@Override
    public void register(IModRegistry registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        IIngredientBlacklist ingredientBlacklist = helpers.getIngredientBlacklist();

        //ItemRegistryCore items = ModuleCore.getItems();
//
        //BlockRegistryCore blocks = ModuleCore.getBlocks();
//
        //JeiUtil.addDescription(registry,
        //        blocks.analyzer,
        //        blocks.bogEarth,
        //        blocks.escritoire,
        //        blocks.humus
        //);
    }
}