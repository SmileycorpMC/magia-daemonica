package net.smileycorp.magiadaemonica.common.rituals.summoning;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.blocks.BlockChalkLine;
import net.smileycorp.magiadaemonica.common.rituals.RitualsServer;

import java.util.Arrays;
import java.util.Map;

public class SummoningCircles {

    private static Map<ResourceLocation, SummoningCirclePattern> PATTERNS = Maps.newHashMap();
    private static Map<ResourceLocation, Object> CANDLES = Maps.newHashMap();
    private static Map<ResourceLocation, Integer> MAX_POWER = Maps.newHashMap();

    public static SummoningCirclePattern FIVE_POINT_PENTACLE = register(Constants.loc("five_point_pentacle"), 2000, true, false, new int[][]{
                    {-1, -1, 1, 2, 1, -1, -1},
                    {-1, 1, 0, 1, 0, 1, -1},
                    {2, 1, 1, 1, 1, 1, 2},
                    {1, 1, 1, 0, 1, 1, 1},
                    {1, 0, 1, 1, 1, 0, 1},
                    {-1, 2, 1, 0, 1, 2, -1},
                    {-1, -1, 1, 1, 1, -1, -1}},
            new float[][]{{0, -3.06f}, {-2.87f, -0.87f}, {2.87f, -0.87f}, {-1.75f, 2.375f}, {1.75f, 2.375f}});

    private static SummoningCirclePattern register(ResourceLocation name, int maxPower, boolean rotate, boolean mirror, int[][] pattern, float[][] candles) {
        SummoningCirclePattern circle = new SummoningCirclePattern(name, rotate, mirror, pattern);
        PATTERNS.put(name, circle);
        CANDLES.put(name, candles);
        MAX_POWER.put(name, maxPower);
        return circle;
    }

    public static SummoningCircle findMatch(World world, BlockPos pos) {
        for (SummoningCirclePattern pattern : PATTERNS.values()) {
            SummoningCircle circle = pattern.matches(world, pos);
            if (circle != null) return circle;
        }
        return null;
    }

    public static void tryPlace(World world, BlockPos pos) {
        SummoningCircle circle = findMatch(world, pos);
        if (circle == null) return;
        circle.setBlocks(world, BlockChalkLine.RitualState.INACTIVE);
        RitualsServer.get((WorldServer) world).addRitual(circle);
    }

    public static float[][] getCandles(ResourceLocation name) {
        float[][] candles = (float[][]) CANDLES.get(name);
        return Arrays.copyOf(candles, candles.length);
    }

    public static int getMaxPower(ResourceLocation name) {
        return MAX_POWER.get(name);
    }

}
