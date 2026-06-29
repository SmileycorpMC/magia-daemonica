package net.smileycorp.magiadaemonica.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.command.CommandEffect;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import net.smileycorp.magiadaemonica.common.potions.PotionSin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandEffect.class)
public class MixinCommandEffect {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;clearActivePotions()V"), method = "execute")
    public void magiadaemonica$execute$clearActivePotions(MinecraftServer server, ICommandSender sender, String[] args, CallbackInfo callback, @Local EntityLivingBase entitylivingbase) {
        if (entitylivingbase.isPotionActive(DaemonicaPotions.SIN)) PotionSin.remove(entitylivingbase);
    }

}
