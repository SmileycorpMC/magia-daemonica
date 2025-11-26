package net.smileycorp.magiadaemonica.common.demons;

import java.util.Locale;
import java.util.Random;

public enum Domain {

    LUST("Asmodeus"),
    GLUTTONY("Beelzebub"),
    GREED("Mammon"),
    SLOTH("Belphegor"),
    WRATH("Satan"),
    ENVY("Leviathan"),
    PRIDE("Belial");

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

    public static Domain get(String name) {
        if (name == null) return null;
        name = name.toLowerCase(Locale.US);
        for (Domain domain : values()) if (domain.name.equals(name)) return domain;
        return null;
    }

    public static Domain get(Random rand) {
        return values()[rand.nextInt(values().length)];
    }

}
