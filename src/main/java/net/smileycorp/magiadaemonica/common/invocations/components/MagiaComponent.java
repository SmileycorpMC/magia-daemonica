package net.smileycorp.magiadaemonica.common.invocations.components;

import net.minecraft.util.text.ITextComponent;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;

public interface MagiaComponent {

    boolean canApply(InvocationContext ctx);

    void consumeComponent(InvocationContext ctx);

    ITextComponent getDescription();

}
