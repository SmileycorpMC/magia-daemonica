package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;

public class BlankScrollMessage implements IMessage {

    private boolean mainhand;

    public BlankScrollMessage() {}

    public BlankScrollMessage(boolean mainhand) {
        this.mainhand = mainhand;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        mainhand = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(mainhand);
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.openBlankScrollGUI(mainhand));
        return null;
    }

    public static void send(EntityPlayer player, boolean mainhand) {
        PacketHandler.NETWORK_INSTANCE.sendTo(new BlankScrollMessage(mainhand), (EntityPlayerMP) player);
    }

}
