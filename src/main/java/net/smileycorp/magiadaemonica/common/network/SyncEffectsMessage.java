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
import net.smileycorp.atlas.api.data.Pair;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.util.Effect;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SyncEffectsMessage implements IMessage {

    private final List<Pair<ResourceLocation, Effect>> effects = Lists.newArrayList();

    public SyncEffectsMessage() {}

    public SyncEffectsMessage(Collection<Map.Entry<ResourceLocation, Effect>> effects) {
        effects.stream().map(Pair::of).forEach(this.effects::add);
    }

    public SyncEffectsMessage(ResourceLocation curse, Effect effect) {
        effects.add(Pair.of(curse, effect));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        while (buf.isReadable()) effects.add(Pair.of(new ResourceLocation(ByteBufUtils.readUTF8String(buf)),
                new Effect(buf.readInt(), buf.readInt())));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (Pair<ResourceLocation, Effect> pair : effects) {
            ByteBufUtils.writeUTF8String(buf, pair.getFirst().toString());
            Effect effect = pair.getSecond();
            buf.writeInt(effect.getLevel());
            buf.writeInt(effect.getDuration());
        }
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.setEffects(effects));
        return null;
    }

    public static void send(EntityPlayerMP player) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES ,null)) return;
        PacketHandler.NETWORK_INSTANCE.sendTo(new SyncEffectsMessage(player.getCapability(DaemonicaCapabilities.EFFECTS, null).getEffects()), player);
    }

}
