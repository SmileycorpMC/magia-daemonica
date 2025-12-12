package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.smileycorp.magiadaemonica.common.items.InfernalRelic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem {

    @Shadow public abstract ItemStack getItem();

    @Inject(at = @At("HEAD"), method = "attackEntityFrom", cancellable = true)
    public void magiadaemonica$attackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
        if (getItem().getItem() instanceof InfernalRelic) callback.setReturnValue(false);
    }

}
