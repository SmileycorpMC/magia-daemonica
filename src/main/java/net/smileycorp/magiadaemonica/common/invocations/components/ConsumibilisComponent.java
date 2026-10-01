package net.smileycorp.magiadaemonica.common.invocations.components;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.capabilities.ComponentTracker;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;

public class ConsumibilisComponent implements MagiaComponent {

    private final ItemStack stack;

    public ConsumibilisComponent(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean canApply(InvocationContext ctx) {
        return ComponentTracker.hasConsumed(ctx.getPlayer(), stack);
    }

    @Override
    public void consumeComponent(InvocationContext ctx) {}

    @Override
    public ITextComponent getDescription() {
        return new TextComponentTranslation("invocation.magiadaemonica.component.consumibilis", stack.getDisplayName());
    }

}
