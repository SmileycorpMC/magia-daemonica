package net.smileycorp.magiadaemonica.integration.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.common.items.ItemCalixPerpetuus;

import java.util.List;

public class CalixPerpetuusWrapper implements ICustomCraftingRecipeWrapper {

    private final List<ItemStack> stacks = Lists.newArrayList();
    private final List<ItemStack> chalices = Lists.newArrayList();
    private final ICraftingGridHelper craftingHelper;
    private final Item item;

    public CalixPerpetuusWrapper(ICraftingGridHelper craftingHelper, Item item) {
        this.craftingHelper = craftingHelper;
        this.item = item;
        if (item == Items.MILK_BUCKET) {
            stacks.add(new ItemStack(Items.MILK_BUCKET));
            chalices.add(ItemCalixPerpetuus.setMilk(new ItemStack(DaemonicaItems.CALIX_PERPETUUS), true));
            return;
        }
        for (PotionType type : ForgeRegistries.POTION_TYPES) {
            if (type.getEffects().isEmpty()) continue;
            this.stacks.add(PotionUtils.addPotionToItemStack(new ItemStack(item), type));
            chalices.add(PotionUtils.addPotionToItemStack(new ItemStack(DaemonicaItems.CALIX_PERPETUUS), type));
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, Lists.newArrayList(stacks,
                Lists.newArrayList(new ItemStack(DaemonicaItems.CALIX_PERPETUUS))));
        List<List<ItemStack>> outputs = Lists.newArrayList();
        outputs.add(chalices);
        ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IIngredients ingredients) {
        IGuiItemStackGroup displayStacks = layout.getItemStacks();
        IFocus<?> focus = layout.getFocus();
        List<List<ItemStack>> inputs = Lists.newArrayList();
        List<ItemStack> outputs = Lists.newArrayList();
        if (focus.getValue() instanceof ItemStack) {
            ItemStack stack = (ItemStack) focus.getValue();
            if (stack.getItem() instanceof ItemPotion) {
                inputs.add(Lists.newArrayList(stack));
                inputs.add(Lists.newArrayList(new ItemStack(DaemonicaItems.CALIX_PERPETUUS)));
                outputs.add(ItemCalixPerpetuus.copyEffects(new ItemStack(DaemonicaItems.CALIX_PERPETUUS), stack));
            } else if (stack.getItem() == DaemonicaItems.CALIX_PERPETUUS) {
                if (focus.getMode() == IFocus.Mode.INPUT) {
                    inputs.add(stacks);
                    inputs.add(Lists.newArrayList(stack));
                } else if (ItemCalixPerpetuus.hasPotion(stack)) {
                    inputs.add(Lists.newArrayList(ItemCalixPerpetuus.copyEffects(new ItemStack(item), stack)));
                    inputs.add(Lists.newArrayList(new ItemStack(DaemonicaItems.CALIX_PERPETUUS)));
                    outputs.add(stack);
                }
            }
        }
        if (inputs.isEmpty()) {
            inputs.add(stacks);
            inputs.add(Lists.newArrayList(new ItemStack(DaemonicaItems.CALIX_PERPETUUS)));
        }
        if (outputs.isEmpty()) outputs.addAll(chalices);
        craftingHelper.setInputs(displayStacks, inputs, 3, 3);
        craftingHelper.setOutput(displayStacks, outputs);
    }

    public static List<CalixPerpetuusWrapper> getRecipes(ICraftingGridHelper craftingHelper) {
        List<CalixPerpetuusWrapper> recipes = Lists.newArrayList();
        recipes.add(new CalixPerpetuusWrapper(craftingHelper, Items.MILK_BUCKET));
        for (Item item : ForgeRegistries.ITEMS) if (item instanceof ItemPotion)
            recipes.add(new CalixPerpetuusWrapper(craftingHelper, item));
        return recipes;
    }

}
