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
import net.smileycorp.magiadaemonica.common.demons.contracts.Contract;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;

public class OpenContractMessage implements IMessage {

    private int entity;
    private Contract contract;

    public OpenContractMessage() {}

    public OpenContractMessage(int entity, Contract contract) {
        this.entity = entity;
        this.contract = contract;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entity = buf.readInt();
        contract = Contract.readFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entity);
        ByteBufUtils.writeTag(buf, contract.writeToNBT());
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() ->
                NetworkClientHandler.openContractGUI(entity, contract));
        return null;
    }

    public static void send(EntityPlayer player, EntityContract contract) {
        PacketHandler.NETWORK_INSTANCE.sendTo(new OpenContractMessage(contract.getEntityId(), contract.getContract()),
                (EntityPlayerMP) player);
    }

}
