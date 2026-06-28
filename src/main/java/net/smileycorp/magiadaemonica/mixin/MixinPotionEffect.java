package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionEffect.class)
public abstract class MixinPotionEffect {
    
    @Shadow @Final private Potion potion;
    
    @Inject(at = @At("HEAD"), method = "doesShowParticles", cancellable = true)
    public void raids$doesShowParticles(CallbackInfoReturnable<Boolean> callback) {
        if (potion == DaemonicaPotions.BLEED) callback.setReturnValue(false);
    }
    
}
