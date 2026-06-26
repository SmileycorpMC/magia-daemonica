package net.smileycorp.magiadaemonica.common.rituals;

import net.minecraft.util.EnumFacing;

public enum Rotation implements PatternTransformer, PosTransformer {

    NORTH(EnumFacing.NORTH, pattern -> pattern, pos -> pos),
    EAST(EnumFacing.EAST, pattern -> {
        int depth = pattern.length;
        int width = pattern[0].length;
        int height = pattern[0][0].length;
        int[][][] rotated = new int[depth][height][width];
        for (int y = 0; y < pattern.length; y++)
            for (int x = 0; x < width; x++)
                for (int z = 0; z < height; z++)
                    rotated[y][z][x] = pattern[y][x][z];
        return rotated;
    }, pos -> new float[] {pos[1],-pos[0]}),
    SOUTH(EnumFacing.SOUTH, pattern -> {
        int depth = pattern.length;
        int width = pattern[0].length;
        int height = pattern[0][0].length;
        int[][][] rotated = new int[depth][height][width];
        for (int y = 0; y < pattern.length; y++)
            for (int x = 0; x < width; x++)
                for (int z = 0; z < height; z++)
                    rotated[y][width - x - 1][height - z - 1] = pattern[y][x][z];
        return rotated;
    }, pos -> new float[] {-pos[0], -pos[1]}),
    WEST(EnumFacing.WEST, pattern -> {
        int depth = pattern.length;
        int width = pattern[0].length;
        int height = pattern[0][0].length;
        int[][][] rotated = new int[depth][height][width];
        for (int y = 0; y < pattern.length; y++)
            for (int x = 0; x < width; x++)
                for (int z = 0; z < height; z++)
                    rotated[y][height - z - 1][width - x - 1] = pattern[y][x][z];
        return rotated;
    }, pos -> new float[] {-pos[1], pos[0]});

    private final EnumFacing facing;
    private final PatternTransformer patternFunc;
    private final PosTransformer posFunc;

    Rotation(EnumFacing facing, PatternTransformer patternFunc, PosTransformer posFunc) {
        this.facing = facing;
        this.patternFunc = patternFunc;
        this.posFunc = posFunc;
    }

    public EnumFacing.Axis getAxis() {
        return facing.getAxis();
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public int[][][] apply(int[][][] pattern) {
        return patternFunc.apply(pattern);
    }

    public float[] apply(float[] pos) {
        return posFunc.apply(pos);
    }

    public int getAngle() {
        return ordinal() * 90;
    }

}
