package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;

public class ChooseRelicMessage implements IMessage {

    public ChooseRelicMessage() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(NetworkClientHandler::openChooseRelicGUI);
        return null;
    }

    public static void send(EntityPlayerMP player) {
        PacketHandler.NETWORK_INSTANCE.sendTo(new ChooseRelicMessage(), player);
    }

}
