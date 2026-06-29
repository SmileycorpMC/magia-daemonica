package net.smileycorp.magiadaemonica.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.potion.Potion;
import net.smileycorp.magiadaemonica.client.ClientUtils;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryEffectRenderer.class)
public class MixinInventoryEffectRenderer {

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/potion/Potion;renderInventoryEffect(Lnet/minecraft/potion/PotionEffect;Lnet/minecraft/client/gui/Gui;IIF)V", remap = false), method = "drawActivePotionEffects")
    public void magiadaemonica$drawActivePotionEffects$renderInventoryEffect(CallbackInfo callback, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local Potion potion) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.player.isPotionActive(DaemonicaPotions.SIN)) return;
        if (potion == DaemonicaPotions.SIN) return;
        ClientUtils.renderSinLock(i + 4, j + 5);
    }

}
