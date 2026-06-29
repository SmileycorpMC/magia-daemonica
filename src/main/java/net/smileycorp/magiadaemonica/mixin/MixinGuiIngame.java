package net.smileycorp.magiadaemonica.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.smileycorp.magiadaemonica.client.ClientUtils;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;renderHUDEffect(Lnet/minecraft/potion/PotionEffect;Lnet/minecraft/client/gui/Gui;IIFF)V", remap = false), method = "renderPotionEffects")
    public void magiadaemonica$drawActivePotionEffects$renderHUDEffect(Potion instance, PotionEffect effect, Gui gui, int x, int y, float z, float alpha, Operation<Void> original) {
        original.call(instance, effect, gui, x, y, z, alpha);
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.player.isPotionActive(DaemonicaPotions.SIN)) return;
        if (instance == DaemonicaPotions.SIN) return;
        ClientUtils.renderSinLock(x + 1, y + 1);
    }

}
