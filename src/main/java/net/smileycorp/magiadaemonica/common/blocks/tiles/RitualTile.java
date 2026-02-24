package net.smileycorp.magiadaemonica.common.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;

public interface RitualTile {

    void setRitual(BlockPos ritual);

    Ritual getRitual();

    boolean isActive();

    static boolean isActive(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileRitualBasic && ((TileRitualBasic) tile).isActive();
    }

}
