package net.smileycorp.magiadaemonica.integration;


import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes;
import thedarkcolour.futuremc.registry.FItems;

public class FutureMCIntegration {

    public static void registerRecipes() {
        OreDictionary.registerOre("wax", new ItemStack(FItems.INSTANCE.getHONEYCOMB()));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DaemonicaBlocks.CHALK)), new ItemStack(DaemonicaBlocks.CHALK_STAIRS));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DaemonicaBlocks.CHALK)), new ItemStack(DaemonicaBlocks.CHALK_SLAB, 2));
    }

}
