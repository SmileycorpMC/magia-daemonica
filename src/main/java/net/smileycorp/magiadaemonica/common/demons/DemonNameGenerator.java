package net.smileycorp.magiadaemonica.common.demons;

import java.util.Random;

public class DemonNameGenerator {

    private static final String[] prefixes = {"Lu", "Ba", "Be", "Bel", "Ab", "Za", "Me", "Sa", "A", "Az",
        "As", "Na", "Bar", "Li", "As", "Ma", "Ash", "La", "Go", "Ay", "Pa", "Mo", "Er", "Y"};
    private static final String[] middles = {"ci", "ha", "he", "i", "ze", "phe", "ba", "pho", "zi", "phi",
            "ma", "za", "ra", "ri", "ta", "be", "ba", "thi", "rae", "mo", "sto", "vi", "a", "de", "el", "fe", "mu", "bu",
            "zu", "a", "aa", "e", "ee", "y", "zy", "ry", "lo", "zu"};
    private static final String[] suffixes = {"r", "t", "th", "al", "b", "gor", "don", "met", "el", "sto",
            "el", "zel", "hem", "roth", "as", "nus", "rus", "tos", "hah", "del", "hl", "cles", "tan", "than", "b", "l",
            "lb", "der", "r", "h", "n", "ch", "s", "ul"};


    public static String generate(Random rand) {
        int length = Math.round((float)rand.nextGaussian() + 1.5f);
        StringBuilder builder = new StringBuilder( prefixes[rand.nextInt(prefixes.length)]);
        for (int i = 0; i < length; i++) builder.append(middles[rand.nextInt(middles.length)]);
        return builder.append(suffixes[rand.nextInt(suffixes.length)]).toString();
    }

}
