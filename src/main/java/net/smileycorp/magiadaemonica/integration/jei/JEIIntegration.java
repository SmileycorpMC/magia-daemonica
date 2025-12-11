package net.smileycorp.magiadaemonica.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        ICraftingGridHelper craftingHelper = registry.getJeiHelpers().getGuiHelper().createCraftingGridHelper(1, 0);
        registry.handleRecipes(CalixPerpetuusWrapper.class, r -> r, VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipes(CalixPerpetuusWrapper.getRecipes(craftingHelper), VanillaRecipeCategoryUid.CRAFTING);
    }

}
