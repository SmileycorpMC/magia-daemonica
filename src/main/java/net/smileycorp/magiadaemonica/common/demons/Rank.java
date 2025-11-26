package net.smileycorp.magiadaemonica.common.demons;

import net.minecraft.util.math.MathHelper;

import java.util.Locale;
import java.util.Random;

public enum Rank {

    PRINCE,
    ARCHBARRON,
    BARRON,
    ARCHLORD,
    GREATER_LORD,
    LESSER_LORD,
    GREATER_DEMON,
    LESSER_DEMON,
    GREATER_IMP,
    LESSER_IMP;

    private final String name;

    Rank() {
        this.name = name().toLowerCase(Locale.US);
    }

    public String getName() {
        return name;
    }

    public String getDemonName(Random rand, Domain domain) {
        if (this == PRINCE) return domain.getPrince();
        return DemonNameGenerator.generate(rand);
    }

    public static Rank get(String name) {
        if (name == null) return null;
        name = name.toLowerCase(Locale.US);
        for (Rank rank : values()) if (rank.name.equals(name)) return rank;
        return null;
    }

    public static Rank get(Random rand, int points) {
        int mult = (int) MathHelper.clamp(( ((rand.nextGaussian() * rand.nextGaussian()) + 1) * points) / 1000f, 0, values().length);
        return values()[mult];
    }

}
