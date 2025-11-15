package net.smileycorp.magiadaemonica.common.rituals.summoningcircle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.blocks.BlockChalkLine;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;
import net.smileycorp.magiadaemonica.common.blocks.tiles.TileSummoningCircle;
import net.smileycorp.magiadaemonica.common.rituals.Rotation;

public class SummoningCircle {

    private final BlockPos pos;
    private final int width, height;
    private final ResourceLocation name;
    private boolean mirror;
    private EnumFacing facing = EnumFacing.NORTH;

    public SummoningCircle(BlockPos pos, int width, int height, ResourceLocation name) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public void setBlocks(World world) {
        BlockPos.MutableBlockPos mutable;
        boolean addedRenderer = false;
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                mutable = new BlockPos.MutableBlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                IBlockState state = world.getBlockState(mutable);
                if (state.getBlock() != DaemonicaBlocks.CHALK_LINE) continue;
                world.setBlockState(mutable, state.withProperty(BlockChalkLine.ACTIVE, true));
                TileSummoningCircle tile = (TileSummoningCircle) world.getTileEntity(mutable);
                tile.setOrigin(pos, width, height);
                if (!addedRenderer) {
                    tile.setRenderProperties(name, facing, mirror);
                    addedRenderer = true;
                }
            }
        }
    }

    public void mirror() {
        this.mirror = true;
    }

    public void setFacing(Rotation rotation) {
        facing = rotation.getFacing();
    }

}
