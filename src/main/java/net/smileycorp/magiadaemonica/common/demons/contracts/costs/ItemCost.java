package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.Domain;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;

import java.util.Random;

public class ItemCost implements Cost {

    public static ResourceLocation ID = Constants.loc("item");

    private final ItemStack stack;

    public ItemCost(ItemStack ingredient) {
        this.stack = ingredient;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void pay(EntityPlayer player) {
        int count = stack.getCount();
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.isItemEqual(this.stack)) continue;
            if (this.stack.hasTagCompound()) {
                if (!stack.hasTagCompound()) continue;
                if (!stack.getTagCompound().equals(this.stack.getTagCompound())) continue;
            }
            int toRemove = count;
            if (count > stack.getCount()) toRemove = count - stack.getCount();
            stack.shrink(toRemove);
            count -= toRemove;
            if (count <= 0) return;
        }
    }

    @Override
    public boolean canPay(EntityPlayer player) {
        int count = stack.getCount();
        for (ItemStack stack : player.inventory.mainInventory) {
            if (!stack.isItemEqual(this.stack)) continue;
            if (this.stack.hasTagCompound()) {
                if (!stack.hasTagCompound()) continue;
                if (!stack.getTagCompound().equals(this.stack.getTagCompound())) continue;
            }
            count -= stack.getCount();
            if (count <= 0) return true;
        }
        return false;
    }

    @Override
    public Object[] getDescriptionArguments() {
        StringBuilder builder = new StringBuilder();
        builder.append(stack.getCount() + "x ");
        builder.append(stack.getDisplayName());
        return new Object[] {builder.toString()};
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

    public static ItemCost generate(Demon demon, EntityPlayer player, int tier) {
        Random rand = player.getRNG();
        boolean food = rand.nextInt(10) < (demon.getDomain() == Domain.GLUTTONY ? 7 : 2);
        EnumRarity rarity = EnumRarity.COMMON;
        ItemStack stack;
        if (food) stack = ContractsUtils.getFood(rand);
        else {
            rarity = EnumRarity.values()[MathHelper.clamp((tier + rand.nextInt((tier + 1) / 2)) / 2,  0, 3)];
            stack = ContractsUtils.getItem(rarity, rand);
            rarity = (EnumRarity) stack.getItem().getForgeRarity(stack);
        }
        int count = !stack.isStackable() ? 1 : (int) ((rand.nextFloat() * tier * 0.4f
                / ((float) (rarity.ordinal() * 2) + 1)) * stack.getMaxStackSize());
        stack.setCount(MathHelper.clamp(count, 1, stack.getMaxStackSize()));
        return new ItemCost(stack);
    }

}
