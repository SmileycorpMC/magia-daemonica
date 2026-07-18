package net.smileycorp.magiadaemonica.common.invocations.components;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;

public class VocalisComponent implements MagiaComponent {

    private final String regex;

    public VocalisComponent(String regex) {
        this.regex = regex;
    }
    
    @Override
    public boolean canApply(InvocationContext ctx) {
        String phrase = ctx.getPhrase();
        if (phrase == null) return false;
        System.out.println(phrase + ", " + regex);
        return phrase.matches(regex);
    }

    @Override
    public void consumeComponent(InvocationContext ctx) {}

    @Override
    public ITextComponent getDescription() {
        return new TextComponentTranslation("invocation.magiadaemonica.component.vocal", regex);
    }


}
