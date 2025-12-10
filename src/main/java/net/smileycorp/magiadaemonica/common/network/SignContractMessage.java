package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;

public class SignContractMessage implements IMessage {

    private int entity;

    public SignContractMessage() {}

    public SignContractMessage(int entity) {
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
        if (ctx.side == Side.SERVER) FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            EntityPlayerMP player = ctx.getServerHandler().player;
            EntityContract entity = (EntityContract) player.world.getEntityByID(this.entity);
            entity.accept(player);
        });
        return null;
    }

    public static void send(int entity) {
        PacketHandler.NETWORK_INSTANCE.sendToServer(new SignContractMessage(entity));
    }

}
