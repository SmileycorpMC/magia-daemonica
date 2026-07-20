package net.smileycorp.magiadaemonica.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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
        return tile instanceof RitualTile && ((RitualTile) tile).isActive();
    }

    boolean renderBlock();

    default SPacketUpdateTileEntity getUpdatePacket(TileEntity tile) {
        NBTTagCompound nbt = tile.getUpdateTag();
        nbt.setBoolean("isRitualTile", true);
        return new SPacketUpdateTileEntity(tile.getPos(), -1, nbt);
    }
}
