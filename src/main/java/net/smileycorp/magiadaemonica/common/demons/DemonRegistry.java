package net.smileycorp.magiadaemonica.common.demons;

import java.util.Random;

public class DemonRegistry {

    public static Demon create(Random rand, int points) {
        return create(rand, points, Domain.get(rand));
    }

    public static Demon create(Random rand, int points, Domain domain) {
        Rank rank = Rank.get(rand, points);
        return new Demon(rank.getDemonName(rand, domain), domain, rank);
    }

}
