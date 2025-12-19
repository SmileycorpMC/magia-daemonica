package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    private static float gamma;

    @Inject(at = @At(value = "HEAD"), method = "updateLightmap")
    public void magiadaemonica$updateLightmap$HEAD(float pt, CallbackInfo callback) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        if (((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != DaemonicaItems.OCULUS_AETHEREUS) return;
        gamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1.5f;
    }

    @Inject(at = @At(value = "TAIL"), method = "updateLightmap")
    public void magiadaemonica$updateLightmap$TAIL(float pt, CallbackInfo callback) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        if (((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != DaemonicaItems.OCULUS_AETHEREUS) return;
        mc.gameSettings.gammaSetting = gamma;
    }

}
