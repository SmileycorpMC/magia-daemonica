package net.smileycorp.magiadaemonica.common.invocations.components;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;

import java.util.Locale;

public class VocalisComponent implements MagiaComponent {

    private final String regex;
    private final String plainText;

    public VocalisComponent(String regex) {
        this.regex = regex.toLowerCase(Locale.US);
        plainText = replaceRegex(this.regex, " ");
    }

    @Override
    public boolean canApply(InvocationContext ctx) {
        String phrase = ctx.getPhrase();
        if (phrase == null) return false;
        return phrase.matches(regex);
    }

    @Override
    public void consumeComponent(InvocationContext ctx) {}

    @Override
    public ITextComponent getDescription() {
        return new TextComponentTranslation("invocation.magiadaemonica.component.vocal", regex);
    }

    @Override
    public boolean isVocalis() {
        return true;
    }

    public String getText() {
        return plainText;
    }

    private static String replaceRegex(String string, String replaceWith) {
        StringBuilder builder = new StringBuilder();
        int opens = 0;
        int sqOpens = 0;
        for (char c : string.toCharArray()) {
            if (c == '(') {
                if (opens == 0 && sqOpens == 0) builder.append(replaceWith);
                opens++;
            }
            else if (c == '[') {
                if (opens == 0 && sqOpens == 0) builder.append(replaceWith);
                sqOpens++;
            }
            else if (c == ')' && opens > 0) opens--;
            else if (c == ']' && sqOpens > 0) sqOpens--;
            else if (opens > 0 || sqOpens > 0) continue;
            if (c == ' ' || (c >= 'a' && c <= 'z')) builder.append(c);
        }
        return builder.toString();
    }

}
