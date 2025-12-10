package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;

public class ValidateContractMessage implements IMessage {

    private int entity;

    public ValidateContractMessage() {}

    public ValidateContractMessage(int entity) {
        this.entity = entity;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entity = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entity);
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() ->
                NetworkClientHandler.validateContract(entity));
        return null;
    }

    public static void send(EntityPlayer player, EntityContract contract) {
        PacketHandler.NETWORK_INSTANCE.sendTo(new ValidateContractMessage(contract.getEntityId()), (EntityPlayerMP) player);
    }

}
