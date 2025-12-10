package net.smileycorp.magiadaemonica.common.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.common.items.ItemCalixPerpetuus;

public class RecipeCalixPerpetuusFilling extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public RecipeCalixPerpetuusFilling() {
        setRegistryName(Constants.loc("calix_perpetuus_filling"));
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        if (inv.getWidth() > 3 || inv.getHeight() > 3) return false;
        boolean potion = false;
        boolean chalice = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == DaemonicaItems.CALIX_PERPETUUS) {
                chalice = true;
                if (potion) return true;
                continue;
            }
            if (stack.getItem() instanceof ItemPotion) {
                if (PotionUtils.getEffectsFromStack(stack).isEmpty()) return false;
                potion = true;
                if (chalice) return true;
                continue;
            }
            if (!stack.isEmpty()) return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack potion = null;
        ItemStack chalice = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == DaemonicaItems.CALIX_PERPETUUS) {
                chalice = stack.copy();
                if (potion != null) return ItemCalixPerpetuus.copyEffects(chalice, potion);
                continue;
            }
            if (stack.getItem() instanceof ItemPotion) {
                potion = stack;
                if (chalice != null) return ItemCalixPerpetuus.copyEffects(chalice, potion);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 2 && height >= 2;
    }

}
