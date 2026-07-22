package net.smileycorp.magiadaemonica.integration.jei;

import mezz.jei.api.*;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.smileycorp.magiadaemonica.common.recipes.PlantHybridizationRegistry;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registry.addRecipeCategories(new PlantHybridizationCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        ICraftingGridHelper craftingHelper = registry.getJeiHelpers().getGuiHelper().createCraftingGridHelper(1, 0);
        registry.handleRecipes(CalixPerpetuusWrapper.class, r -> r, VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipes(CalixPerpetuusWrapper.getRecipes(craftingHelper), VanillaRecipeCategoryUid.CRAFTING);

        registry.handleRecipes(PlantHybridizationRegistry.HybridizationRecipe.class, PlantHybridizationCategory.Wrapper::new, PlantHybridizationCategory.ID);
        registry.addRecipes(PlantHybridizationRegistry.getInstance().getRecipes(), PlantHybridizationCategory.ID);
    }

}
