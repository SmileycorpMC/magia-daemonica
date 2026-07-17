package net.smileycorp.magiadaemonica.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;

public class PacketRitualTile extends SPacketUpdateTileEntity {

    public PacketRitualTile() {
        super();
    }

    public PacketRitualTile(BlockPos pos, NBTTagCompound nbt) {
       super(pos, 0, nbt);
    }

    public void processPacket(INetHandlerPlayClient netHandler) {
        Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.syncRitualTile(netHandler, this));
    }

}
