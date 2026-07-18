package net.smileycorp.magiadaemonica.common.invocations.components;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.capabilities.Sanguis;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;

public class SanguisComponent implements MagiaComponent {

    private final int amount;

    public SanguisComponent(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean canApply(InvocationContext ctx) {
        return Sanguis.get(ctx.getPlayer()) >= amount;
    }

    @Override
    public void consumeComponent(InvocationContext ctx) {}

    @Override
    public ITextComponent getDescription() {
        return new TextComponentTranslation("invocation.magiadaemonica.component.sanguis", amount);
    }

}
