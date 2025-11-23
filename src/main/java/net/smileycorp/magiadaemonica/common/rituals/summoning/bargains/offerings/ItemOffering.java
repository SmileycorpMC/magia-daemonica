package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemOffering implements Offering {

    private final ItemStack stack;

    public ItemOffering(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void grant(EntityPlayer player) {
        ItemStack stack = this.stack.copy();
        if (!player.addItemStackToInventory(stack)) player.dropItem(stack, false, true);
    }

}
