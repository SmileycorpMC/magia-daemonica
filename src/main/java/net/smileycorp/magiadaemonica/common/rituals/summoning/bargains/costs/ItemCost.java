package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.atlas.api.util.RecipeUtils;

public class ItemCost implements Cost {

    private final ItemStack ingredient;

    public ItemCost(ItemStack ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public void pay(EntityPlayer player, int tier) {
        int count = ingredient.getCount();
        for (ItemStack stack : player.inventory.mainInventory)
            if (RecipeUtils.compareItemStacksWithSize(stack, ingredient, ingredient.hasTagCompound())) {
                int toRemove = count;
                if (count > stack.getCount()) toRemove = count - stack.getCount();
                stack.shrink(toRemove);
                count -= toRemove;
                if (count <= 0) return;
            }
    }

    @Override
    public boolean canPay(EntityPlayer player, int tier) {
        int count = ingredient.getCount();
        for (ItemStack stack : player.inventory.mainInventory)
            if (RecipeUtils.compareItemStacksWithSize(stack, ingredient, ingredient.hasTagCompound())) {
                count -= stack.getCount();
                if (count <= 0) return true;
            }
        return false;
    }

    @Override
    public TextComponentBase getDescription(int tier) {
        return null;
    }

}
