package net.smileycorp.magiadaemonica.common.rituals;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface RitualType<T extends Ritual> {

    void tryPlace(World world, BlockPos pos, IBlockState state);

    float[][] getCandles(ResourceLocation name);

    PatternMatcher<T> getPattern(ResourceLocation name);

    int getMaxPower(ResourceLocation name);

    T getRitualFromNBT(NBTTagCompound nbt);

}
