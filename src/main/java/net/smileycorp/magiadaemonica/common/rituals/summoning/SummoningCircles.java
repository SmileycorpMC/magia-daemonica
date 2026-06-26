package net.smileycorp.magiadaemonica.common.rituals.summoning;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.rituals.PatternMatcher;
import net.smileycorp.magiadaemonica.common.rituals.RitualType;
import net.smileycorp.magiadaemonica.common.rituals.RitualsServer;

import java.util.Arrays;
import java.util.Map;

public class SummoningCircles implements RitualType<SummoningCircle> {

    public static final SummoningCircles INSTANCE = new SummoningCircles();

    private final Map<ResourceLocation, SummoningCirclePattern> PATTERNS = Maps.newHashMap();
    private final Map<ResourceLocation, Object> CANDLES = Maps.newHashMap();
    private final Map<ResourceLocation, Integer> MAX_POWER = Maps.newHashMap();

    public SummoningCirclePattern FIVE_POINT_PENTACLE = register(Constants.loc("five_point_pentacle"), 2250, new float[][]{{0, -3.06f}, {-2.87f, -0.87f}, {2.87f, -0.87f}, {-1.75f, 2.375f}, {1.75f, 2.375f}}, true, false, new int[][]{
                    {-1, -1, 2, 1, 2, -1, -1},
                    {-1, 2, 0, 2, 0, 2, -1},
                    {1, 2, 2, 2, 2, 2, 1},
                    {2, 2, 2, 0, 2, 2, 2},
                    {2, 0, 2, 2, 2, 0, 2},
                    {-1, 1, 2, 0, 2, 1, -1},
                    {-1, -1, 2, 2, 2, -1, -1}},
            PatternMatcher.Key.AIR, PatternMatcher.Key.CHALK_CANDLE, PatternMatcher.Key.CHALK);

    private SummoningCirclePattern register(ResourceLocation name, int maxPower, float[][] candles, boolean rotate, boolean mirror, int[][] pattern, PatternMatcher.Key... keys) {
        SummoningCirclePattern circle = new SummoningCirclePattern(name, rotate, mirror, pattern, keys);
        PATTERNS.put(name, circle);
        CANDLES.put(name, candles);
        MAX_POWER.put(name, maxPower);
        return circle;
    }

    @Override
    public void tryPlace(World world, BlockPos pos, IBlockState state) {
        for (SummoningCirclePattern pattern : PATTERNS.values()) {
            PatternMatcher.MatchContext ctx = pattern.matches(world, pos, state);
            if (ctx == null) continue;
            SummoningCircle circle = pattern.create(world, ctx);
            circle.setBlocks(world, ctx.getPattern());
            RitualsServer.get((WorldServer) world).addRitual(circle);
        }
    }

    @Override
    public float[][] getCandles(ResourceLocation name) {
        float[][] candles = (float[][]) CANDLES.get(name);
        return Arrays.copyOf(candles, candles.length);
    }

    @Override
    public PatternMatcher<SummoningCircle> getPattern(ResourceLocation name) {
        return PATTERNS.get(name);
    }

    @Override
    public int getMaxPower(ResourceLocation name) {
        return MAX_POWER.get(name);
    }

    @Override
    public SummoningCircle getRitualFromNBT(NBTTagCompound nbt) {
        return SummoningCircle.fromNBT(nbt);
    }

}
