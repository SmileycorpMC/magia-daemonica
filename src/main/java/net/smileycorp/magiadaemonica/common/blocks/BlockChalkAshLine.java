package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockChalkAshLine extends BlockLine {

    public BlockChalkAshLine() {
        super("chalk_ash_line");
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(3) > 0) return;
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat(), 0, 0, 0);
        if (rand.nextInt(10) == 0) world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5 + (rand.nextFloat() - 0.5) * 0.05,
                pos.getY(), pos.getZ() + 0.5 + (rand.nextFloat() - 0.5) * 0.05, 0, 0, 0);
    }

}
