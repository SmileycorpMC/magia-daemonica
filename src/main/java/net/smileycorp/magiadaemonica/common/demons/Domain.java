package net.smileycorp.magiadaemonica.common.demons;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.Constants;

import java.util.Locale;
import java.util.Random;

public enum Domain {

    PRIDE("Lucifer"),
    GREED("Mammon"),
    LUST("Asmodeus"),
    ENVY("Leviathan"),
    GLUTTONY("Beelzebub"),
    WRATH("Satanas"),
    SLOTH("Belphegor");

    private final String name, prince;

    Domain(String prince) {
        this.name = name().toLowerCase(Locale.US);
        this.prince = prince;
    }

    public String getName() {
        return name;
    }

    public String getPrince() {
        return prince;
    }

    public boolean isGreedy() {
        return this == GREED || this == GLUTTONY;
    }

    public static Domain get(String name) {
        if (name == null) return null;
        name = name.toLowerCase(Locale.US);
        for (Domain domain : values()) if (domain.name.equals(name)) return domain;
        return null;
    }

    public static Domain get(Random rand) {
        return values()[rand.nextInt(values().length)];
    }

    public ITextComponent getFormalName(String name, Rank rank) {
        return new TextComponentTranslation("demon.magiadaemonica.name", name,
                new TextComponentTranslation(rank.getTranslationKey()).getFormattedText(),
                new TextComponentTranslation("demon." + Constants.MODID + ".domain." + getName()).getFormattedText());
    }

}
