package net.smileycorp.magiadaemonica.common.data;

import java.util.Locale;

public enum DemonFaction {

    LUST,
    GLUTTONY,
    GREED,
    SLOTH,
    WRATH,
    ENVY,
    PRIDE;

    private final String name;

    DemonFaction() {
        this.name = name().toLowerCase(Locale.US);
    }

    public String getName() {
        return name;
    }

}
