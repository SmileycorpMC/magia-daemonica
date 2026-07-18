package net.smileycorp.magiadaemonica.common.invocations.components;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;

public class MateriaComponent implements MagiaComponent {

    public static final ResourceLocation OFFHAND_FLAG = Constants.loc("offhand");

    private final Ingredient materia;

    public MateriaComponent(Ingredient materia) {
        this.materia = materia;
    }

    @Override
    public boolean canApply(InvocationContext ctx) {
        EntityPlayer player = ctx.getPlayer();
        for (EnumHand hand : EnumHand.values()) if (materia.apply(player.getHeldItem(hand))) {
            if (hand == EnumHand.OFF_HAND) ctx.setFlag(OFFHAND_FLAG);
            return true;
        }
        return true;
    }

    @Override
    public void consumeComponent(InvocationContext ctx) {
        EntityPlayer player = ctx.getPlayer();
        ItemStack stack = player.getHeldItem(ctx.hasFlag(OFFHAND_FLAG) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        if (!player.isCreative()) {
            if (stack.isItemStackDamageable()) stack.setItemDamage(stack.getItemDamage() + 1);
            else stack.shrink(1);
        }
    }

    @Override
    public ITextComponent getDescription() {
        return new TextComponentTranslation("invocation.magiadaemonica.component.materia", materia);
    }

}
