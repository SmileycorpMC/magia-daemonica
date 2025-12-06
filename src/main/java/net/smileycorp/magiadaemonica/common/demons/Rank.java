package net.smileycorp.magiadaemonica.common.demons;

import net.minecraft.util.math.MathHelper;

import java.util.Locale;
import java.util.Random;

public enum Rank {

    PRINCE(5),
    ARCHBARON(4),
    BARON(4),
    ARCHLORD(3),
    GREATER_LORD(3),
    LESSER_LORD(3),
    ARCHDEMON(2),
    GREATER_DEMON(2),
    LESSER_DEMON(2),
    CAMBION(1),
    GREATER_IMP(1),
    LESSER_IMP(1);

    private static final float MULTIPLIER = (float) Math.sqrt(values().length);
    private final String name;
    private final int contracts;

    Rank(int contracts) {
        this.name = name().toLowerCase(Locale.US);
        this.contracts = contracts;
    }

    public String getName() {
        return name;
    }

    public String getDemonName(Random rand, Domain domain) {
        if (this == PRINCE) return domain.getPrince();
        return DemonNameGenerator.generate(rand);
    }

    public boolean isPersistent() {
        return this == PRINCE;
    }

    public static Rank get(String name) {
        if (name == null) return null;
        name = name.toLowerCase(Locale.US);
        for (Rank rank : values()) if (rank.name.equals(name)) return rank;
        return null;
    }

    public static Rank get(Random rand, int points) {
        int mult = (int) Math.round(MathHelper.clamp(points/1000f + (1 + rand.nextGaussian() * MULTIPLIER), 1, values().length));
        return values()[values().length - mult];
    }

    public int getBaseContractCount() {
        return contracts;
    }

}
