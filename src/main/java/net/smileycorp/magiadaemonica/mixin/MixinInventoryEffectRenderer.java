package net.smileycorp.magiadaemonica.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryEffectRenderer.class)
public class MixinInventoryEffectRenderer {

    @Unique
    private static final ResourceLocation texture = Constants.loc("textures/gui/sin_lock.png");;

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/potion/Potion;renderInventoryEffect(Lnet/minecraft/potion/PotionEffect;Lnet/minecraft/client/gui/Gui;IIF)V", remap = false), method = "drawActivePotionEffects")
    public void magiadaemonica$drawActivePotionEffects$renderInventoryEffect(CallbackInfo callback, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local Potion potion) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.player.isPotionActive(DaemonicaPotions.SIN)) return;
        if (potion == DaemonicaPotions.SIN) return;;
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        mc.renderEngine.bindTexture(texture);
        Gui.drawScaledCustomSizeModalRect(i + 3, j + 4, 0, 0 , 24, 24, 24, 24, 24, 24);
        GlStateManager.popMatrix();
    }

}
