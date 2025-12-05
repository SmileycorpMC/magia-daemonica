package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.Constants;

public class ItemOffering implements Offering {

    public static ResourceLocation ID = Constants.loc("item");

    private final ItemStack stack;

    public ItemOffering(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void grant(EntityPlayer player) {
        ItemStack stack = this.stack.copy();
        if (player.addItemStackToInventory(stack)) return;;
        EntityItem item = player.dropItem(stack, false);
        if (item == null) return;
        item.setNoPickupDelay();
        item.setOwner(player.getName());
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

    public static ItemOffering fromNBT(NBTTagCompound nbt) {
        return new ItemOffering(new ItemStack(nbt.getCompoundTag("stack")));
    }

}
