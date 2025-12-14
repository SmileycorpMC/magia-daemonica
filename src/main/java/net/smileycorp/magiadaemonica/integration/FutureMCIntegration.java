package net.smileycorp.magiadaemonica.integration;


import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes;

public class FutureMCIntegration {

    public static void registerRecipes() {
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DaemonicaBlocks.CHALK)), new ItemStack(DaemonicaBlocks.CHALK_STAIRS));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DaemonicaBlocks.CHALK)), new ItemStack(DaemonicaBlocks.CHALK_SLAB, 2));
    }

}
