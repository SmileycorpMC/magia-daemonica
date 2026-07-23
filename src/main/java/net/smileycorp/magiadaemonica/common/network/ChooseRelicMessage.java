package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;

public class ChooseRelicMessage implements IMessage {

    private int amount;

    public ChooseRelicMessage() {}

    public ChooseRelicMessage(int amount) {
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        amount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(amount);
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.openChooseRelicGUI(amount));
        return null;
    }

    public static void send(EntityPlayerMP player, int amount) {
        PacketHandler.NETWORK_INSTANCE.sendTo(new ChooseRelicMessage(amount), player);
    }

}
