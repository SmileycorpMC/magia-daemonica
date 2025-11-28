package net.smileycorp.magiadaemonica.common.demons;

import java.util.Random;

public class DemonNameGenerator {

    private static final String[] prefixes = {"Lu", "Ba", "Be", "Bel", "Beel", "Ab", "Za", "Me", "Sa", "A", "Az",
        "As", "Na", "Bar", "Li", "As", "Ma", "Ash"};
    private static final String[] middles = {"ci", "ha", "he", "i", "ze", "phe", "ba", "pho", "zi", "phi",
            "ma", "za", "ra", "ri", "ta", "be", "ba", "thi", "rae", "mo", "sto", "vi", "a", "de"};
    private static final String[] suffixes = {"fer", "mut", "moth", "al", "bub", "gor", "don", "met", "el", "sto",
            "el", "zel", "hem", "roth", "as", "nus", "rus", "tos", "rith", "hah", "del", "pehl", "cles", "tan", "than"};


    public static String generate(Random rand) {
        int length = Math.round((float)rand.nextGaussian());
        if (length < 0) return prefixes[rand.nextInt(prefixes.length)] + suffixes[rand.nextInt(suffixes.length)];
        if (length > 1)  return prefixes[rand.nextInt(prefixes.length)] + middles[rand.nextInt(middles.length)]
                + middles[rand.nextInt(middles.length)] + middles[rand.nextInt(middles.length)] + suffixes[rand.nextInt(suffixes.length)];
        if (length > 0)  return prefixes[rand.nextInt(prefixes.length)] + middles[rand.nextInt(middles.length)]
                + middles[rand.nextInt(middles.length)] + suffixes[rand.nextInt(suffixes.length)];
        return prefixes[rand.nextInt(prefixes.length)]
                + middles[rand.nextInt(middles.length)]
                + suffixes[rand.nextInt(suffixes.length)];
    }

}
