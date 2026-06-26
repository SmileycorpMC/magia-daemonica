package net.smileycorp.magiadaemonica.common.rituals;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircle;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircles;

import java.util.Map;

public class RitualsRegistry {

    private static final Map<ResourceLocation, RitualType<? extends Ritual>> RITUAL_TYPES = Maps.newHashMap();

    public static void registerRitualType(ResourceLocation id, RitualType<? extends Ritual> factory) {
        RITUAL_TYPES.put(id, factory);
    }

    public static void registerDefaults() {
        registerRitualType(SummoningCircle.ID, SummoningCircles.INSTANCE);
    }

    public static Ritual getRitualFromNBT(NBTTagCompound nbt) {
        RitualType<? extends Ritual> type = RITUAL_TYPES.get(new ResourceLocation(nbt.getString("id")));
        return type == null ? null : type.getRitualFromNBT(nbt);
    }

    public static PatternMatcher<? extends Ritual> getPattern(ResourceLocation id, ResourceLocation name) {
        RitualType<? extends Ritual> type = RITUAL_TYPES.get(id);
        return type == null ? null : type.getPattern(name);
    }

    public static float[][] getCandles(ResourceLocation id, ResourceLocation name) {
        RitualType<? extends Ritual> type = RITUAL_TYPES.get(id);
        return type == null ? new float[][] {} : type.getCandles(name);
    }

    public static void tryPlace(World world, BlockPos pos, IBlockState state) {
        for (RitualType<? extends Ritual> type : RITUAL_TYPES.values()) type.tryPlace(world, pos, state);
    }

}
