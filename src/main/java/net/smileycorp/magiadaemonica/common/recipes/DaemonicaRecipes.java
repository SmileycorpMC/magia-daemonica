package net.smileycorp.magiadaemonica.common.recipes;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
       addTrades();
       addCustomRecipes(event.getRegistry());
       if (Loader.isModLoaded("futuremc")) FutureMCIntegration.registerRecipes();
    }

    private static void addOredict() {
        OreDictionary.registerOre("flint", new ItemStack(Items.FLINT));
        OreDictionary.registerOre("suet", new ItemStack(DaemonicaItems.FOOD, 1, 0));
        OreDictionary.registerOre("tallow", new ItemStack(DaemonicaItems.FOOD, 1, 1));
        OreDictionary.registerOre("wax", new ItemStack(DaemonicaItems.FOOD, 1, 1));
        OreDictionary.registerOre("knife", DaemonicaItems.FLINT_KNIFE);
        OreDictionary.registerOre("knife", new ItemStack(DaemonicaItems.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("knife", new ItemStack(DaemonicaItems.LAPIS_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("knife", new ItemStack(DaemonicaItems.PRISMARINE_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("knife", new ItemStack(DaemonicaItems.OBSIDIAN_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("knife", new ItemStack(DaemonicaItems.SICA_INFERNALEM, 1, OreDictionary.WILDCARD_VALUE));
    }

    private static void addSmelting() {
        GameRegistry.addSmelting(new ItemStack(DaemonicaItems.FOOD, 1, 0), new ItemStack(DaemonicaItems.FOOD, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(DaemonicaItems.FOOD, 1, 2), new ItemStack(DaemonicaItems.MATERIAL, 1, 1), 0.1f);
    }

    private static void addTrades() {
        ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:priest")).getCareer(0)
                .addTrade(2, new EntityVillager.ListItemForEmeralds(DaemonicaItems.MATERIAL, new EntityVillager.PriceInfo(1, 1)));
    }

    private static void addCustomRecipes(IForgeRegistry<IRecipe> registry) {
        registry.register(new RecipeCalixPerpetuusFilling());
    }

}
