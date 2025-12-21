package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(at = @At(value = "HEAD"), method = "isGlowing", cancellable = true)
    public void magiadaemonica$isGlowing(CallbackInfoReturnable<Boolean> callback) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        if (((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != DaemonicaItems.OCULUS_AETHEREUS) return;
        callback.setReturnValue(true);
    }

}
