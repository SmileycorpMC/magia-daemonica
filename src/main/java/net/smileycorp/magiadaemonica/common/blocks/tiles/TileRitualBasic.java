package net.smileycorp.magiadaemonica.common.blocks.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;
import org.jetbrains.annotations.Nullable;

public class TileRitualBasic extends TileEntity implements RitualTile {

    private BlockPos ritual;

    public TileRitualBasic() {}

    public TileRitualBasic(BlockPos ritual) {
        setRitual(ritual);
    }

    @Override
    public void setRitual(BlockPos ritual) {
        this.ritual = ritual;
        markDirty();
    }

    @Override
    public Ritual getRitual() {
        return ritual == null || world == null ? null : Rituals.get(world).getRitual(pos);
    }

    @Override
    public boolean isActive() {
        Ritual ritual = getRitual();
        return ritual != null && ritual.isActive();
    }

    @Override
    public boolean renderBlock() {
        return ritual == null;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world instanceof WorldServer) ((WorldServer) world).getPlayerChunkMap().markBlockForUpdate(pos);
    }

    public void forceUpdate() {
        if (world instanceof WorldServer) ((WorldServer) world).getPlayerChunkMap().markBlockForUpdate(pos);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        boolean refresh = oldState.getBlock() != newState.getBlock();
        if (refresh && ritual != null &! world.isRemote) {
            Rituals.get(world).removeRitual(ritual);
            ritual = null;
        }
        return refresh;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("ritual")) setRitual(NBTUtil.getPosFromTag(compound.getCompoundTag("ritual")));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (ritual != null) compound.setTag("ritual", NBTUtil.createPosTag(ritual));
        return super.writeToNBT(compound);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (ritual != null &! world.isRemote) Rituals.get(world).removeRitual(ritual);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return RitualTile.super.getUpdatePacket(this);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        if (ritual != null) nbt.setTag("ritual", NBTUtil.createPosTag(ritual));
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        if (nbt.hasKey("ritual")) ritual = NBTUtil.getPosFromTag(nbt.getCompoundTag("ritual"));
    }

}
