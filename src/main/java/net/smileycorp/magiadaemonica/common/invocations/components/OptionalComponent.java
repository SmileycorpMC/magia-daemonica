package net.smileycorp.magiadaemonica.common.invocations.components;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;

public class OptionalComponent implements MagiaComponent {

    private final ResourceLocation flag;
    private final MagiaComponent component;

    public OptionalComponent(ResourceLocation flag, MagiaComponent component) {
        this.flag = flag;
        this.component = component;
    }

    @Override
    public boolean canApply(InvocationContext ctx) {
        if (component.canApply(ctx)) ctx.setFlag(flag);
        return true;
    }

    @Override
    public void consumeComponent(InvocationContext ctx) {
        if (ctx.hasFlag(flag)) component.consumeComponent(ctx);
    }

    @Override
    public ITextComponent getDescription() {
        return new TextComponentTranslation("invocation.magiadaemonica.component.optional", component.getDescription());
    }

}
