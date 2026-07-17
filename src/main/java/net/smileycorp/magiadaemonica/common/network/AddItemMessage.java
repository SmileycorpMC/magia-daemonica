package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class AddItemMessage implements IMessage {

    private ItemStack stack;

    public AddItemMessage() {}

    public AddItemMessage(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.SERVER) FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            EntityPlayerMP player = ctx.getServerHandler().player;
            EntityItem item = player.dropItem(stack, false);
            if (item == null) return;
            item.setNoPickupDelay();
            item.setOwner(player.getName());
        });
        return null;
    }

    public static void send(ItemStack stack) {
        PacketHandler.NETWORK_INSTANCE.sendToServer(new AddItemMessage(stack));
    }

}
