package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.client.rituals.RitualsClient;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;

public class SyncRitualMessage implements IMessage {

    private BlockPos pos;
    private NBTTagCompound ritual;

    public SyncRitualMessage() {}

    public SyncRitualMessage(BlockPos pos, Ritual ritual) {
        this.pos = pos;
        this.ritual = ritual.writeToNBT();
        this.ritual.setString("id", ritual.getID().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        ritual = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeTag(buf, ritual);
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> RitualsClient.getInstance().addRitual(pos, ritual));
        return null;
    }

}
