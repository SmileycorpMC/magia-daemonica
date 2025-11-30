package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.atlas.api.util.RecipeUtils;

public class ItemCost implements Cost {

    private final ItemStack stack;

    public ItemCost(ItemStack ingredient) {
        this.stack = ingredient;
    }

    @Override
    public void pay(EntityPlayer player, int tier) {
        int count = stack.getCount();
        for (ItemStack stack : player.inventory.mainInventory)
            if (RecipeUtils.compareItemStacksWithSize(stack, this.stack, this.stack.hasTagCompound())) {
                int toRemove = count;
                if (count > stack.getCount()) toRemove = count - stack.getCount();
                stack.shrink(toRemove);
                count -= toRemove;
                if (count <= 0) return;
            }
    }

    @Override
    public boolean canPay(EntityPlayer player, int tier) {
        int count = stack.getCount();
        for (ItemStack stack : player.inventory.mainInventory)
            if (RecipeUtils.compareItemStacksWithSize(stack, this.stack, this.stack.hasTagCompound())) {
                count -= stack.getCount();
                if (count <= 0) return true;
            }
        return false;
    }

    @Override
    public TextComponentBase getDescription(int tier) {
        return null;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
        return nbt;
    }

    public static ItemCost fromNBT(NBTTagCompound nbt) {
        return new ItemCost(new ItemStack(nbt.getCompoundTag("stack")));
    }

}
