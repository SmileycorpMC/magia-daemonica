package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;

public class AddCurseBoonMessage implements IMessage {

    private boolean isCurse;
    private ResourceLocation loc;

    public AddCurseBoonMessage() {}

    public AddCurseBoonMessage(boolean isCurse, ResourceLocation loc) {
        this.isCurse = isCurse;
        this.loc = loc;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isCurse = buf.readBoolean();
        loc = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isCurse);
        ByteBufUtils.writeUTF8String(buf, loc.toString());
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.SERVER) FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (isCurse) player.sendMessage(new TextComponentTranslation("message."+ Constants.MODID + ".curse.add",
                    new TextComponentTranslation("curse." + loc.getResourceDomain() + "." + loc.getResourcePath()), Curses.add(player, loc)));
            else player.sendMessage(new TextComponentTranslation("message."+ Constants.MODID + ".boon.add",
                        new TextComponentTranslation("boon." + loc.getResourceDomain() + "." + loc.getResourcePath()), Boons.add(player, loc)));
        });
        return null;
    }

    public static void send(boolean isCurse, ResourceLocation loc) {
        PacketHandler.NETWORK_INSTANCE.sendToServer(new AddCurseBoonMessage(isCurse, loc));
    }

}
