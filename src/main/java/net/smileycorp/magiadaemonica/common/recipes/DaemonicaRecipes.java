package net.smileycorp.magiadaemonica.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.integration.FutureMCIntegration;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DaemonicaRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
       addOredict();
       addSmelting();
       addCustomRecipes(event.getRegistry());
       if (Loader.isModLoaded("futuremc")) FutureMCIntegration.registerRecipes();
    }

    private static void addOredict() {
        OreDictionary.registerOre("suet", new ItemStack(DaemonicaItems.MATERIAL, 1, 0));
        OreDictionary.registerOre("tallow", new ItemStack(DaemonicaItems.MATERIAL, 1, 1));
        OreDictionary.registerOre("wax", new ItemStack(DaemonicaItems.MATERIAL, 1, 1));
    }

    private static void addSmelting() {
        GameRegistry.addSmelting(new ItemStack(DaemonicaItems.MATERIAL, 1, 0), new ItemStack(DaemonicaItems.MATERIAL, 1, 1), 0.1f);
    }

    private static void addCustomRecipes(IForgeRegistry<IRecipe> registry) {
        registry.register(new RecipeCalixPerpetuusFilling());
    }

}
