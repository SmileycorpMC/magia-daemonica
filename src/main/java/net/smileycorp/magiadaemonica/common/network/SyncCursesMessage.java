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

public class SyncCursesMessage implements IMessage {

    private final List<Pair<ResourceLocation, Integer>> curses = Lists.newArrayList();

    public SyncCursesMessage() {}

    public SyncCursesMessage(Collection<Map.Entry<ResourceLocation, Integer>> curses) {
        curses.stream().map(Pair::of).forEach(this.curses::add);
    }

    public SyncCursesMessage(ResourceLocation curse, int level) {
        curses.add(Pair.of(curse, level));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        while (buf.isReadable()) curses.add(Pair.of(new ResourceLocation(ByteBufUtils.readUTF8String(buf)), buf.readInt()));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (Pair<ResourceLocation, Integer> pair : curses) {
            ByteBufUtils.writeUTF8String(buf, pair.getFirst().toString());
            buf.writeInt(pair.getSecond());
        }
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.addCurses(curses));
        return null;
    }

    public static void send(EntityPlayerMP player) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES ,null)) return;
        PacketHandler.NETWORK_INSTANCE.sendTo(new SyncCursesMessage(player.getCapability(DaemonicaCapabilities.CURSES, null).getCurses()), player);
    }

}
