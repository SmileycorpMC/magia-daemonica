package net.smileycorp.magiadaemonica.common.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenFlowerPatch extends WorldGenerator {

	private final IBlockState state, rareState;
	private final int min, max;
	private final double rareStateChance;

	public WorldGenFlowerPatch(IBlockState state, int min, int max) {
		this(state, min, max, null, 0);
	}

	public WorldGenFlowerPatch(IBlockState state, int min, int max, IBlockState rareState, double rareStateChance) {
		this.state = state;
		this.min = min;
		this.max = max;
		this.rareState = rareState;
		this.rareStateChance = rareStateChance;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
		for (int i = 0; i < (min < max ? (rand.nextInt(max - min) + min) : max); ++i) {
			mutable.move(EnumFacing.getHorizontal(rand.nextInt(4)));
			IBlockState soil = world.getBlockState(mutable.down());
			if ((soil.getMaterial() == Material.GRASS || soil.getMaterial() == Material.GROUND) && soil.isFullBlock() &&
					world.isAirBlock(mutable) && (!world.provider.isNether() || mutable.getY() < world.getHeight()))
				setBlockAndNotifyAdequately(world, mutable, rareState != null && rand.nextFloat() <= rareStateChance ? rareState : state);
		}
		return true;
	}

}
