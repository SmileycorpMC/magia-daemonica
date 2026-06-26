package net.smileycorp.magiadaemonica.common.rituals;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.Func;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public abstract class PatternMatcher<T extends Ritual> {

    private final int width, height, depth;
    private final boolean rotate, mirror;
    private final int[][][] pattern;
    private final Key[] keys;

    public PatternMatcher(boolean rotate, boolean mirror, int[][][] pattern, Key[] keys) {
        int width = 0;
        int height = 0;
        for (int[][] layer : pattern) {
            if (height < layer.length) height = layer.length;
            for (int[] column : layer) if (width < column.length) width = column.length;
        }
        this.width = width;
        this.height = height;
        depth = pattern.length;
        this.rotate = rotate;
        this.mirror = mirror;
        this.pattern = new int[depth][height][width];
        for (int[][] layer : this.pattern) for (int[] column : layer) Arrays.fill(column, -1);
        for (int i = 0; i < pattern.length; i++) System.arraycopy(pattern[i], 0, this.pattern[i], 0, pattern[i].length);
        this.keys = keys;
    }

    public MatchContext matches(World world, BlockPos pos, IBlockState state) {
                for (int i = 0; i < keys.length; i++) {
            Key key = keys[i];
            if (key == Key.ANY || key == Key.AIR) continue;
            //find a key that matches the given block
            if (!key.matches(state, world, pos)) continue;
            for (Rotation rotation : rotate ? Rotation.values() : new Rotation[]{Rotation.NORTH}) {
                int[][][] pattern = rotation.apply(this.pattern);
                int width = rotation.getAxis() == EnumFacing.Axis.X ? this.height : this.width;
                int height = rotation.getAxis() == EnumFacing.Axis.X ? this.width : this.height;
                BlockPos start = matchesPattern(world, i, pos, width, height, pattern);
                if (start != null) return new MatchContext(start, rotation, false, width, height, pattern);
                if (mirror) {
                    pattern = PatternTransformer.MIRROR.apply(pattern);
                    start = matchesPattern(world, i, pos, width, height, pattern);
                    if (start != null) return new MatchContext(start, rotation, true, width, height, pattern);
                }
            }
        }
        return null;
    }

    protected BlockPos matchesPattern(World world, int key, BlockPos pos, int width, int height, int[][][] pattern) {
        //search for a fitting key location
        for (int j = 0; j < depth; j++) for (int i = 0; i < height; i++) for (int k = 0; k < width; k++) {
            if (pattern[j][k][i] != key) continue;
            //if key match found check the surrounding pattern
            BlockPos start = matchesWithKey(world, pos, i, j, k, width, height, pattern);
            if (start != null) return start;
        }
        return null;
    }

    private BlockPos matchesWithKey(World world, BlockPos pos, int x, int y, int z, int width, int height, int[][][] pattern) {
        BlockPos start = pos.add(-x, -y, -z);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(start);
        for (int j = 0; j < depth; j++) for (int i = 0; i < height; i++) for (int k = 0; k < width; k++) {
            if (i == x && j == y && k == z) continue;
            int key = pattern[j][k][i];
            if (key == -1) continue;
            mutable.setPos(start.getX() + i, start.getY() + j, start.getZ() + k);
            if (!keys[key].matches(world.getBlockState(mutable), world, mutable)) return null;
        }
        return start;
    }

    public abstract T create(World world, MatchContext ctx);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public boolean canRotate() {
        return rotate;
    }

    public boolean canMirror() {
        return mirror;
    }

    public int[][][] getPattern() {
        return ArrayUtils.clone(pattern);
    }

    public Key[] getKeys() {
        return ArrayUtils.clone(keys);
    }

    public interface Key {

        Key ANY = Func::True;
        Key AIR = (state, world, pos) -> state.getMaterial() == Material.AIR;
        Key CANDLE = (state, world, pos) -> state.getBlock() == DaemonicaBlocks.SCENTED_CANDLE;
        Key CHALK = (state, world, pos) -> state.getBlock() == DaemonicaBlocks.CHALK_LINE;
        Key CHALK_CANDLE = (state, world, pos) -> state.getBlock() == DaemonicaBlocks.CHALK_CANDLE;

        boolean matches(IBlockState state, World world, BlockPos pos);

    }

    public static class MatchContext {

        private final BlockPos pos;
        private final Rotation rotation;
        private final boolean mirrored;
        private final int width, height;
        private final int[][][] pattern;

        public MatchContext(BlockPos pos, Rotation rotation, boolean mirrored, int width, int height, int[][][] pattern) {
            this.pos = pos;
            this.rotation = rotation;
            this.mirrored = mirrored;
            this.width = width;
            this.height = height;
            this.pattern = pattern;
        }

        public BlockPos getPos() {
            return pos;
        }

        public Rotation getRotation() {
            return rotation;
        }

        public boolean isMirrored() {
            return mirrored;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }


        public int[][][] getPattern() {
            return pattern;
        }
    }

}
