package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.entity.ai.EntityAINearestAttackableTarget$1")
public class MixinEntityAINearestAttackableTarget {

    @Inject(at = @At("HEAD"), method = "apply(Lnet/minecraft/entity/EntityLivingBase;)Z", cancellable = true)
    public void magiadaemonica$apply(EntityLivingBase entity, CallbackInfoReturnable<Boolean> callback) {
        if (entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == DaemonicaItems.MORS_LARVA) callback.setReturnValue(false);
    }

}
