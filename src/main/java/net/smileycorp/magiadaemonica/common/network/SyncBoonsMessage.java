package net.smileycorp.magiadaemonica.common.network;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.data.Pair;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SyncBoonsMessage implements IMessage {

    private final List<Pair<ResourceLocation, Integer>> boons = Lists.newArrayList();

    public SyncBoonsMessage() {}

    public SyncBoonsMessage(Collection<Map.Entry<ResourceLocation, Integer>> boons) {
        boons.stream().map(Pair::of).forEach(this.boons::add);
    }

    public SyncBoonsMessage(ResourceLocation curse, int level) {
        boons.add(Pair.of(curse, level));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        while (buf.isReadable()) boons.add(Pair.of(new ResourceLocation(ByteBufUtils.readUTF8String(buf)), buf.readInt()));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (Pair<ResourceLocation, Integer> pair : boons) {
            ByteBufUtils.writeUTF8String(buf, pair.getFirst().toString());
            buf.writeInt(pair.getSecond());
        }
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.setBoons(boons));
        return null;
    }

    public static void send(EntityPlayerMP player) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES ,null)) return;
        PacketHandler.NETWORK_INSTANCE.sendTo(new SyncBoonsMessage(player.getCapability(DaemonicaCapabilities.BOONS, null).getBoons()), player);
    }

}
