package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @Inject(at = @At(value = "HEAD"), method = "shouldHeal", cancellable = true)
    public void magiadaemonica$onLivingUpdate$shouldHeal(CallbackInfoReturnable<Boolean> callback) {
        if (Curses.has((EntityPlayer) (Object) this, CursesRegistry.TORPIDITY)) callback.setReturnValue(false);
    }

}
