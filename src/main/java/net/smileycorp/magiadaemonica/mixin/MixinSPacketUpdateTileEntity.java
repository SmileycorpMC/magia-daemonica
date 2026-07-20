package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.smileycorp.magiadaemonica.client.NetworkClientHandler;
import net.smileycorp.magiadaemonica.common.blocks.tiles.RitualTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SPacketUpdateTileEntity.class)
public abstract class MixinSPacketUpdateTileEntity {

    @Shadow public abstract NBTTagCompound getNbtCompound();

    @Inject(at = @At(value = "HEAD"), method = "processPacket(Lnet/minecraft/network/play/INetHandlerPlayClient;)V", cancellable = true)
    public void magiadaemonica$processPacket(INetHandlerPlayClient handler, CallbackInfo callback) {
        if (!getNbtCompound().hasKey("isRitualTile")) return;
        Minecraft.getMinecraft().addScheduledTask(() -> NetworkClientHandler.syncRitualTile(handler, (SPacketUpdateTileEntity) (Object) this));
        callback.cancel();
    }

}
