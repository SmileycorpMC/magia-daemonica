package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockChalkAshLine extends BlockLine {

    public BlockChalkAshLine() {
        super("chalk_ash_line");
        setSoundType(SoundType.SAND);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(3) > 0) return;
        world.spawnParticle(rand.nextInt(10) == 0 ? EnumParticleTypes.FLAME : EnumParticleTypes.SMOKE_NORMAL,
                pos.getX() + rand.nextFloat(), pos.getY() + 0.05, pos.getZ() + rand.nextFloat(), 0, 0, 0);
    }

}
