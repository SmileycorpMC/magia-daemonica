package net.smileycorp.magiadaemonica.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.common.items.ItemInscribedScroll;

public class InscribeScrollMessage implements IMessage {

    private String inscription;
    private boolean mainhand;

    public InscribeScrollMessage() {}

    public InscribeScrollMessage(String inscription, boolean mainhand) {
        this.inscription = inscription;
        this.mainhand = mainhand;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        mainhand = buf.readBoolean();
        inscription = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(mainhand);
        ByteBufUtils.writeUTF8String(buf, inscription);
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.SERVER) FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ItemStack stack = player.getHeldItem(mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
            if (stack.getItem() != DaemonicaItems.BLANK_SCROLL) return;
            ItemStack newScroll = ItemInscribedScroll.inscribeScroll(new ItemStack(DaemonicaItems.INSCRIBED_SCROLL), inscription);
            if (!player.isCreative()) stack.shrink(1);
            if (!player.addItemStackToInventory(newScroll)) player.dropItem(newScroll, false);
        });
        return null;
    }

}
