package net.smileycorp.magiadaemonica.common.rituals;

public interface PatternTransformer {

    PatternTransformer MIRROR = pattern -> {
        int[][][] mirror = new int[pattern.length][pattern[0].length][pattern[0][0].length];
        for (int j = 0; j < pattern.length; j++)
            for (int i = 0; i < pattern[0].length; i++)
                System.arraycopy(pattern[j][i], 0, mirror[mirror.length - j - 1][mirror[0].length - i - 1], 0, pattern[j][i].length);
        return mirror;
    };

    int[][][] apply(int[][][] pattern);

}
