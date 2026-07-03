package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;

public class FillChatMessage implements IMessage {

    private String text;

    public FillChatMessage() {}

    public FillChatMessage(String text) {
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (text != null) ByteBufUtils.writeUTF8String(buf, text);
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.fillChat(text));
        return null;
    }

    public static void send(EntityPlayer player, String text) {
        PacketHandler.NETWORK_INSTANCE.sendTo(new FillChatMessage(text), (EntityPlayerMP) player);
    }

}
