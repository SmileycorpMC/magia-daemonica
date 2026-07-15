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
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;

import java.util.List;

public class PickCurseBoonMessage implements IMessage {

    private boolean isCurse;
    private List<ResourceLocation> locs = Lists.newArrayList();

    public PickCurseBoonMessage() {}

    public PickCurseBoonMessage(boolean isCurse, List<ResourceLocation> locs) {
        this.isCurse = isCurse;
        this.locs.addAll(locs);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isCurse = buf.readBoolean();
        while (buf.isReadable()) locs.add(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isCurse);
        locs.forEach(loc -> ByteBufUtils.writeUTF8String(buf, loc.toString()));
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.openPickCurseBoonGUI(isCurse, locs));
        return null;
    }

    public static void send(EntityPlayerMP player, boolean isCurse, List<ResourceLocation> locs) {
        PacketHandler.NETWORK_INSTANCE.sendTo(new PickCurseBoonMessage(isCurse, locs), player);
    }

}
