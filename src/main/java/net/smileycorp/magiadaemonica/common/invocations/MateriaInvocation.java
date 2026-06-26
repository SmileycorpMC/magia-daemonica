package net.smileycorp.magiadaemonica.common.invocations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumHand;

public abstract class MateriaInvocation implements Invocation {

    private final Ingredient materia;

    public MateriaInvocation(Ingredient materia) {
        this.materia = materia;
    }

    @Override
    public void apply(String invocation, EntityPlayer player) {
        for (EnumHand hand : EnumHand.values()) {
            ItemStack stack = player.getHeldItem(hand);
            if (!materia.apply(stack)) continue;
            if (!tryCast(player)) return;
            if (stack.isItemStackDamageable()) stack.setItemDamage(stack.getItemDamage() + 1);
            else stack.shrink(1);
            return;
        }
    }

    protected abstract boolean tryCast(EntityPlayer player);

}
