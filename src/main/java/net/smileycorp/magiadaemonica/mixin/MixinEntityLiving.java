package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityLiving.class)
public abstract class MixinEntityLiving extends EntityLivingBase {

    public MixinEntityLiving(World world) {
        super(world);
    }

    @Inject(at = @At(value = "HEAD"), method = "isServerWorld", cancellable = true)
    public void magiadaemonica$isServerWorld(CallbackInfoReturnable<Boolean> callback) {
        if (isPotionActive(DaemonicaPotions.PETRIFIED)) callback.setReturnValue(false);
    }

}
