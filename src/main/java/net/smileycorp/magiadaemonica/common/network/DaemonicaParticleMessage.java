package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;
import net.smileycorp.magiadaemonica.common.EnumParticle;

public class DaemonicaParticleMessage implements IMessage {

	private EnumParticle type = EnumParticle.PIXEL;
	private double x, y, z = 0;
	private Double[] data = {};

	public DaemonicaParticleMessage() {}

	public DaemonicaParticleMessage(EnumParticle type, double x, double y, double z, Double... data) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = EnumParticle.values()[buf.readByte()];
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		data = type.readBytes(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((byte) type.ordinal());
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		type.writeBytes(buf, data);
	}
	
	public IMessage process(MessageContext ctx) {
		if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.spawnParticle(type, x, y, z, data));
		return null;
	}
	
}
