package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.common.invocations.Invocation;
import net.smileycorp.magiadaemonica.common.invocations.InvocationsRegistry;

import java.util.UUID;

public class InvocationMessage implements IMessage {

    private ResourceLocation name = null;
    private UUID player = null;
    private Object[] args = null;

    public InvocationMessage() {}

    public InvocationMessage(ResourceLocation phrase, EntityPlayer player, Object... args) {
        this.name = phrase;
        this.player = player.getUniqueID();
        this.args = args;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        name = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        Invocation invocation = InvocationsRegistry.getInvocation(name);
        if (!(invocation instanceof Invocation.ClientInvocation)) return;
        player = new UUID(buf.readLong(), buf.readLong());
        args = ((Invocation.ClientInvocation) invocation).readFromBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (name == null) return;
        Invocation invocation = InvocationsRegistry.getInvocation(name);
        if (!(invocation instanceof Invocation.ClientInvocation)) return;
        ByteBufUtils.writeUTF8String(buf, name.toString());
        buf.writeLong(player.getMostSignificantBits());
        buf.writeLong(player.getLeastSignificantBits());
        ((Invocation.ClientInvocation) invocation).writeToBuf(buf, args);
    }

    public IMessage process(MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        if (ctx.side == Side.CLIENT) mc.addScheduledTask(() -> {
            Invocation invocation = InvocationsRegistry.getInvocation(name);
            if (!(invocation instanceof Invocation.ClientInvocation)) return;
            EntityPlayer player = mc.world.getPlayerEntityByUUID(this.player);
            if (player == null) return;
            ((Invocation.ClientInvocation) invocation).packetReceived(player, args);
        });
        return null;
    }

    public static void send(EntityPlayer player, ResourceLocation name, Object... args) {
        PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new InvocationMessage(name, player, args),
                new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 32));
    }

}
