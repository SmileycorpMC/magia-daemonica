package net.smileycorp.magiadaemonica.common.recipes;

import net.minecraft.init.Items;
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
                if (chalice) return false;
                chalice = true;
                if (potion) return true;
                continue;
            }
            if (stack.getItem() == Items.MILK_BUCKET) {
                if (potion) return false;
                potion = true;
                if (chalice) return true;
                continue;
            }
            if (stack.getItem() instanceof ItemPotion) {
                if (potion) return false;
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
        boolean milk = false;
        ItemStack potion = null;
        ItemStack chalice = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == DaemonicaItems.CALIX_PERPETUUS) {
                chalice = stack.copy();
                if (milk) return ItemCalixPerpetuus.setMilk(chalice, true);
                if (potion != null) return ItemCalixPerpetuus.copyEffects(chalice, potion);
                continue;
            }
            if (stack.getItem() == Items.MILK_BUCKET) {
                if (chalice != null) return ItemCalixPerpetuus.setMilk(chalice, true);
                milk = true;
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
        return new ItemStack(DaemonicaItems.CALIX_PERPETUUS);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> items = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == Items.POTIONITEM) {
                items.set(i, new ItemStack(Items.GLASS_BOTTLE));
                continue;
            }
            ItemStack container = stack.getItem().getContainerItem(stack);
            if (!container.isEmpty()) items.set(i, container);
        }
        return items;
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
