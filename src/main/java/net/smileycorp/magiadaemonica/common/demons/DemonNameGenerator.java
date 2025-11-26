package net.smileycorp.magiadaemonica.common.demons;

import java.util.Random;

public class DemonNameGenerator {

    private static final String[] prefixes = {"lu", "ba", "be", "bel", "beel", "ab", "za", "me", "sa", "a", "az",
        "as", "na", "bar", "li", "as", "ma"};
    private static final String[] middles = {"ci", "ha", "he", "i", "ze", "phe", "ba", "pho", "zi", "phi",
            "ma", "za", "ra", "ri", "ta", "be", "ba", "thi", "rae", "mo", "sto"};
    private static final String[] suffixes = {"fer", "mut", "moth", "al", "bub", "gor", "don", "met", "el", "sto",
            "el", "zel", "hem", "roth", "as", "nus", "rus", "tos", "rith", "hah", "del", "pehl", "cles"};


    public static String generate(Random rand) {
        return prefixes[rand.nextInt(prefixes.length)]
                + middles[rand.nextInt(middles.length)]
                + suffixes[rand.nextInt(suffixes.length)];
    }

}
