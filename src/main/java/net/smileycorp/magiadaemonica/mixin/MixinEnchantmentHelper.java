package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {

    @Inject(at = @At(value = "HEAD"), method = "hasBindingCurse", cancellable = true)
    private static void magiadaemonica$hasBindingCurse(ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
       if (stack.getItem() == DaemonicaItems.LORICA_ACULEATA) callback.setReturnValue(true);
    }

}
